/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventFactory;
/*     */ import com.lmax.disruptor.EventHandler;
/*     */ import com.lmax.disruptor.EventTranslatorTwoArg;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.RingBuffer;
/*     */ import com.lmax.disruptor.Sequence;
/*     */ import com.lmax.disruptor.SequenceReportingEventHandler;
/*     */ import com.lmax.disruptor.TimeoutException;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import com.lmax.disruptor.dsl.ProducerType;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.LogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.MutableLogEvent;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncLoggerConfigDisruptor
/*     */   extends AbstractLifeCycle
/*     */   implements AsyncLoggerConfigDelegate
/*     */ {
/*     */   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
/*     */   private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
/*     */   
/*     */   public static class Log4jEventWrapper
/*     */   {
/*     */     private AsyncLoggerConfig loggerConfig;
/*     */     private LogEvent event;
/*     */     
/*     */     public Log4jEventWrapper() {}
/*     */     
/*     */     public Log4jEventWrapper(MutableLogEvent mutableLogEvent) {
/*  71 */       this.event = (LogEvent)mutableLogEvent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/*  81 */       this.loggerConfig = null;
/*  82 */       if (this.event instanceof MutableLogEvent) {
/*  83 */         ((MutableLogEvent)this.event).clear();
/*     */       } else {
/*  85 */         this.event = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  91 */       return String.valueOf(this.event);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Log4jEventWrapperHandler
/*     */     implements SequenceReportingEventHandler<Log4jEventWrapper>
/*     */   {
/*     */     private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
/*     */     private Sequence sequenceCallback;
/*     */     private int counter;
/*     */     
/*     */     private Log4jEventWrapperHandler() {}
/*     */     
/*     */     public void setSequenceCallback(Sequence sequenceCallback) {
/* 105 */       this.sequenceCallback = sequenceCallback;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onEvent(AsyncLoggerConfigDisruptor.Log4jEventWrapper event, long sequence, boolean endOfBatch) throws Exception {
/* 111 */       event.event.setEndOfBatch(endOfBatch);
/* 112 */       event.loggerConfig.logToAsyncLoggerConfigsOnCurrentThread(event.event);
/* 113 */       event.clear();
/*     */       
/* 115 */       notifyIntermediateProgress(sequence);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void notifyIntermediateProgress(long sequence) {
/* 123 */       if (++this.counter > 50) {
/* 124 */         this.sequenceCallback.set(sequence);
/* 125 */         this.counter = 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   private static final EventFactory<Log4jEventWrapper> FACTORY = Log4jEventWrapper::new;
/*     */   
/*     */   private static final EventFactory<Log4jEventWrapper> MUTABLE_FACTORY = () -> new Log4jEventWrapper(new MutableLogEvent());
/*     */   
/*     */   private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> TRANSLATOR;
/*     */   
/*     */   private static final EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> MUTABLE_TRANSLATOR;
/*     */   private int ringBufferSize;
/*     */   private AsyncQueueFullPolicy asyncQueueFullPolicy;
/*     */   
/*     */   static {
/* 145 */     TRANSLATOR = ((ringBufferElement, sequence, logEvent, loggerConfig) -> {
/*     */         ringBufferElement.event = logEvent;
/*     */ 
/*     */ 
/*     */         
/*     */         ringBufferElement.loggerConfig = loggerConfig;
/*     */       });
/*     */ 
/*     */     
/* 154 */     MUTABLE_TRANSLATOR = ((ringBufferElement, sequence, logEvent, loggerConfig) -> {
/*     */         ((MutableLogEvent)ringBufferElement.event).initFrom(logEvent);
/*     */         ringBufferElement.loggerConfig = loggerConfig;
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 162 */   private Boolean mutable = Boolean.FALSE;
/*     */   
/*     */   private volatile Disruptor<Log4jEventWrapper> disruptor;
/*     */   
/*     */   private long backgroundThreadId;
/*     */   private EventFactory<Log4jEventWrapper> factory;
/*     */   private EventTranslatorTwoArg<Log4jEventWrapper, LogEvent, AsyncLoggerConfig> translator;
/*     */   private volatile boolean alreadyLoggedWarning;
/* 170 */   private final Object queueFullEnqueueLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogEventFactory(LogEventFactory logEventFactory) {
/* 180 */     this.mutable = Boolean.valueOf((this.mutable.booleanValue() || logEventFactory instanceof org.apache.logging.log4j.core.impl.ReusableLogEventFactory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/* 191 */     if (this.disruptor != null) {
/* 192 */       LOGGER.trace("AsyncLoggerConfigDisruptor not starting new disruptor for this configuration, using existing object.");
/*     */       
/*     */       return;
/*     */     } 
/* 196 */     LOGGER.trace("AsyncLoggerConfigDisruptor creating new disruptor for this configuration.");
/* 197 */     this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLoggerConfig.RingBufferSize");
/* 198 */     WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLoggerConfig.WaitStrategy");
/*     */     
/* 200 */     Log4jThreadFactory log4jThreadFactory = new Log4jThreadFactory("AsyncLoggerConfig", true, 5)
/*     */       {
/*     */         public Thread newThread(Runnable r) {
/* 203 */           Thread result = super.newThread(r);
/* 204 */           AsyncLoggerConfigDisruptor.this.backgroundThreadId = result.getId();
/* 205 */           return result;
/*     */         }
/*     */       };
/* 208 */     this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
/*     */     
/* 210 */     this.translator = this.mutable.booleanValue() ? MUTABLE_TRANSLATOR : TRANSLATOR;
/* 211 */     this.factory = this.mutable.booleanValue() ? MUTABLE_FACTORY : FACTORY;
/* 212 */     this.disruptor = new Disruptor(this.factory, this.ringBufferSize, (ThreadFactory)log4jThreadFactory, ProducerType.MULTI, waitStrategy);
/*     */     
/* 214 */     ExceptionHandler<Log4jEventWrapper> errorHandler = DisruptorUtil.getAsyncLoggerConfigExceptionHandler();
/* 215 */     this.disruptor.setDefaultExceptionHandler(errorHandler);
/*     */     
/* 217 */     Log4jEventWrapperHandler[] handlers = { new Log4jEventWrapperHandler() };
/* 218 */     this.disruptor.handleEventsWith((EventHandler[])handlers);
/*     */     
/* 220 */     LOGGER.debug("Starting AsyncLoggerConfig disruptor for this configuration with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", 
/* 221 */         Integer.valueOf(this.disruptor.getRingBuffer().getBufferSize()), waitStrategy
/* 222 */         .getClass().getSimpleName(), errorHandler);
/* 223 */     this.disruptor.start();
/* 224 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 233 */     Disruptor<Log4jEventWrapper> temp = this.disruptor;
/* 234 */     if (temp == null) {
/* 235 */       LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor for this configuration already shut down.");
/* 236 */       return true;
/*     */     } 
/* 238 */     setStopping();
/* 239 */     LOGGER.trace("AsyncLoggerConfigDisruptor: shutting down disruptor for this configuration.");
/*     */ 
/*     */     
/* 242 */     this.disruptor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     for (int i = 0; hasBacklog(temp) && i < 200; i++) {
/*     */       try {
/* 249 */         Thread.sleep(50L);
/* 250 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 255 */       temp.shutdown(timeout, timeUnit);
/* 256 */     } catch (TimeoutException e) {
/* 257 */       LOGGER.warn("AsyncLoggerConfigDisruptor: shutdown timed out after {} {}", Long.valueOf(timeout), timeUnit);
/* 258 */       temp.halt();
/*     */     } 
/* 260 */     LOGGER.trace("AsyncLoggerConfigDisruptor: disruptor has been shut down.");
/*     */     
/* 262 */     if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
/* 263 */       LOGGER.trace("AsyncLoggerConfigDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, 
/* 264 */           Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy)));
/*     */     }
/* 266 */     setStopped();
/* 267 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBacklog(Disruptor<?> theDisruptor) {
/* 274 */     RingBuffer<?> ringBuffer = theDisruptor.getRingBuffer();
/* 275 */     return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public EventRoute getEventRoute(Level logLevel) {
/* 280 */     int remainingCapacity = remainingDisruptorCapacity();
/* 281 */     if (remainingCapacity < 0) {
/* 282 */       return EventRoute.DISCARD;
/*     */     }
/* 284 */     return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
/*     */   }
/*     */   
/*     */   private int remainingDisruptorCapacity() {
/* 288 */     Disruptor<Log4jEventWrapper> temp = this.disruptor;
/* 289 */     if (hasLog4jBeenShutDown(temp)) {
/* 290 */       return -1;
/*     */     }
/* 292 */     return (int)temp.getRingBuffer().remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasLog4jBeenShutDown(Disruptor<Log4jEventWrapper> aDisruptor) {
/* 299 */     if (aDisruptor == null) {
/* 300 */       LOGGER.warn("Ignoring log event after log4j was shut down");
/* 301 */       return true;
/*     */     } 
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void enqueueEvent(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
/*     */     try {
/* 310 */       LogEvent logEvent = prepareEvent(event);
/* 311 */       enqueue(logEvent, asyncLoggerConfig);
/* 312 */     } catch (NullPointerException npe) {
/*     */ 
/*     */       
/* 315 */       LOGGER.warn("Ignoring log event after log4j was shut down: {} [{}] {}", event.getLevel(), event
/* 316 */           .getLoggerName(), event.getMessage().getFormattedMessage() + (
/* 317 */           (event.getThrown() == null) ? "" : Throwables.toStringList(event.getThrown())));
/*     */     } 
/*     */   }
/*     */   private LogEvent prepareEvent(LogEvent event) {
/*     */     Log4jLogEvent log4jLogEvent;
/* 322 */     LogEvent logEvent = ensureImmutable(event);
/* 323 */     if (logEvent.getMessage() instanceof org.apache.logging.log4j.message.ReusableMessage) {
/* 324 */       if (logEvent instanceof Log4jLogEvent) {
/* 325 */         ((Log4jLogEvent)logEvent).makeMessageImmutable();
/* 326 */       } else if (logEvent instanceof MutableLogEvent) {
/*     */ 
/*     */         
/* 329 */         if (this.translator != MUTABLE_TRANSLATOR)
/*     */         {
/* 331 */           log4jLogEvent = ((MutableLogEvent)logEvent).createMemento();
/*     */         }
/*     */       } else {
/* 334 */         showWarningAboutCustomLogEventWithReusableMessage((LogEvent)log4jLogEvent);
/*     */       } 
/*     */     } else {
/* 337 */       InternalAsyncUtil.makeMessageImmutable(log4jLogEvent.getMessage());
/*     */     } 
/* 339 */     return (LogEvent)log4jLogEvent;
/*     */   }
/*     */   
/*     */   private void showWarningAboutCustomLogEventWithReusableMessage(LogEvent logEvent) {
/* 343 */     if (!this.alreadyLoggedWarning) {
/* 344 */       LOGGER.warn("Custom log event of type {} contains a mutable message of type {}. AsyncLoggerConfig does not know how to make an immutable copy of this message. This may result in ConcurrentModificationExceptions or incorrect log messages if the application modifies objects in the message while the background thread is writing it to the appenders.", logEvent
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 349 */           .getClass().getName(), logEvent.getMessage().getClass().getName());
/* 350 */       this.alreadyLoggedWarning = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enqueue(LogEvent logEvent, AsyncLoggerConfig asyncLoggerConfig) {
/* 355 */     if (synchronizeEnqueueWhenQueueFull()) {
/* 356 */       synchronized (this.queueFullEnqueueLock) {
/* 357 */         this.disruptor.getRingBuffer().publishEvent(this.translator, logEvent, asyncLoggerConfig);
/*     */       } 
/*     */     } else {
/* 360 */       this.disruptor.getRingBuffer().publishEvent(this.translator, logEvent, asyncLoggerConfig);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean synchronizeEnqueueWhenQueueFull() {
/* 365 */     if (DisruptorUtil.ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL && this.backgroundThreadId != 
/*     */       
/* 367 */       Thread.currentThread().getId())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 372 */       if (!(Thread.currentThread() instanceof org.apache.logging.log4j.core.util.Log4jThread)); } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public boolean tryEnqueue(LogEvent event, AsyncLoggerConfig asyncLoggerConfig) {
/* 377 */     LogEvent logEvent = prepareEvent(event);
/* 378 */     return this.disruptor.getRingBuffer().tryPublishEvent(this.translator, logEvent, asyncLoggerConfig);
/*     */   }
/*     */   
/*     */   private LogEvent ensureImmutable(LogEvent event) {
/* 382 */     LogEvent result = event;
/* 383 */     if (event instanceof RingBufferLogEvent)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 390 */       result = ((RingBufferLogEvent)event).createMemento();
/*     */     }
/* 392 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String contextName, String loggerConfigName) {
/* 403 */     return RingBufferAdmin.forAsyncLoggerConfig(this.disruptor.getRingBuffer(), contextName, loggerConfigName);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfigDisruptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */