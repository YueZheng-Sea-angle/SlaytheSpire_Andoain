/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventHandler;
/*     */ import com.lmax.disruptor.EventTranslatorVararg;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.RingBuffer;
/*     */ import com.lmax.disruptor.TimeoutException;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import com.lmax.disruptor.dsl.ProducerType;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*     */ import org.apache.logging.log4j.core.util.Throwables;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ class AsyncLoggerDisruptor
/*     */   extends AbstractLifeCycle
/*     */ {
/*     */   private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;
/*     */   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
/*  50 */   private final Object queueFullEnqueueLock = new Object();
/*     */   
/*     */   private volatile Disruptor<RingBufferLogEvent> disruptor;
/*     */   
/*     */   private String contextName;
/*     */   private boolean useThreadLocalTranslator = true;
/*     */   private long backgroundThreadId;
/*     */   private AsyncQueueFullPolicy asyncQueueFullPolicy;
/*     */   private int ringBufferSize;
/*     */   
/*     */   AsyncLoggerDisruptor(String contextName) {
/*  61 */     this.contextName = contextName;
/*     */   }
/*     */   
/*     */   public String getContextName() {
/*  65 */     return this.contextName;
/*     */   }
/*     */   
/*     */   public void setContextName(String name) {
/*  69 */     this.contextName = name;
/*     */   }
/*     */   
/*     */   Disruptor<RingBufferLogEvent> getDisruptor() {
/*  73 */     return this.disruptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/*  83 */     if (this.disruptor != null) {
/*  84 */       LOGGER.trace("[{}] AsyncLoggerDisruptor not starting new disruptor for this context, using existing object.", this.contextName);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  89 */     if (isStarting()) {
/*  90 */       LOGGER.trace("[{}] AsyncLoggerDisruptor is already starting.", this.contextName);
/*     */       return;
/*     */     } 
/*  93 */     setStarting();
/*  94 */     LOGGER.trace("[{}] AsyncLoggerDisruptor creating new disruptor for this context.", this.contextName);
/*  95 */     this.ringBufferSize = DisruptorUtil.calculateRingBufferSize("AsyncLogger.RingBufferSize");
/*  96 */     WaitStrategy waitStrategy = DisruptorUtil.createWaitStrategy("AsyncLogger.WaitStrategy");
/*     */     
/*  98 */     Log4jThreadFactory log4jThreadFactory = new Log4jThreadFactory("AsyncLogger[" + this.contextName + "]", true, 5)
/*     */       {
/*     */         public Thread newThread(Runnable r) {
/* 101 */           Thread result = super.newThread(r);
/* 102 */           AsyncLoggerDisruptor.this.backgroundThreadId = result.getId();
/* 103 */           return result;
/*     */         }
/*     */       };
/* 106 */     this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
/*     */     
/* 108 */     this.disruptor = new Disruptor(RingBufferLogEvent.FACTORY, this.ringBufferSize, (ThreadFactory)log4jThreadFactory, ProducerType.MULTI, waitStrategy);
/*     */ 
/*     */     
/* 111 */     ExceptionHandler<RingBufferLogEvent> errorHandler = DisruptorUtil.getAsyncLoggerExceptionHandler();
/* 112 */     this.disruptor.setDefaultExceptionHandler(errorHandler);
/*     */     
/* 114 */     RingBufferLogEventHandler[] handlers = { new RingBufferLogEventHandler() };
/* 115 */     this.disruptor.handleEventsWith((EventHandler[])handlers);
/*     */     
/* 117 */     LOGGER.debug("[{}] Starting AsyncLogger disruptor for this context with ringbufferSize={}, waitStrategy={}, exceptionHandler={}...", this.contextName, 
/* 118 */         Integer.valueOf(this.disruptor.getRingBuffer().getBufferSize()), waitStrategy
/* 119 */         .getClass().getSimpleName(), errorHandler);
/* 120 */     this.disruptor.start();
/*     */     
/* 122 */     LOGGER.trace("[{}] AsyncLoggers use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
/*     */     
/* 124 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 133 */     Disruptor<RingBufferLogEvent> temp = getDisruptor();
/* 134 */     if (temp == null) {
/* 135 */       LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor for this context already shut down.", this.contextName);
/* 136 */       return true;
/*     */     } 
/* 138 */     setStopping();
/* 139 */     LOGGER.debug("[{}] AsyncLoggerDisruptor: shutting down disruptor for this context.", this.contextName);
/*     */ 
/*     */     
/* 142 */     this.disruptor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     for (int i = 0; hasBacklog(temp) && i < 200; i++) {
/*     */       try {
/* 149 */         Thread.sleep(50L);
/* 150 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 155 */       temp.shutdown(timeout, timeUnit);
/* 156 */     } catch (TimeoutException e) {
/* 157 */       LOGGER.warn("[{}] AsyncLoggerDisruptor: shutdown timed out after {} {}", this.contextName, Long.valueOf(timeout), timeUnit);
/* 158 */       temp.halt();
/*     */     } 
/*     */     
/* 161 */     LOGGER.trace("[{}] AsyncLoggerDisruptor: disruptor has been shut down.", this.contextName);
/*     */     
/* 163 */     if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
/* 164 */       LOGGER.trace("AsyncLoggerDisruptor: {} discarded {} events.", this.asyncQueueFullPolicy, 
/* 165 */           Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy)));
/*     */     }
/* 167 */     setStopped();
/* 168 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasBacklog(Disruptor<?> theDisruptor) {
/* 175 */     RingBuffer<?> ringBuffer = theDisruptor.getRingBuffer();
/* 176 */     return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String jmxContextName) {
/* 186 */     RingBuffer<RingBufferLogEvent> ring = (this.disruptor == null) ? null : this.disruptor.getRingBuffer();
/* 187 */     return RingBufferAdmin.forAsyncLogger(ring, jmxContextName);
/*     */   }
/*     */   
/*     */   EventRoute getEventRoute(Level logLevel) {
/* 191 */     int remainingCapacity = remainingDisruptorCapacity();
/* 192 */     if (remainingCapacity < 0) {
/* 193 */       return EventRoute.DISCARD;
/*     */     }
/* 195 */     return this.asyncQueueFullPolicy.getRoute(this.backgroundThreadId, logLevel);
/*     */   }
/*     */   
/*     */   private int remainingDisruptorCapacity() {
/* 199 */     Disruptor<RingBufferLogEvent> temp = this.disruptor;
/* 200 */     if (hasLog4jBeenShutDown(temp)) {
/* 201 */       return -1;
/*     */     }
/* 203 */     return (int)temp.getRingBuffer().remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasLog4jBeenShutDown(Disruptor<RingBufferLogEvent> aDisruptor) {
/* 209 */     if (aDisruptor == null) {
/* 210 */       LOGGER.warn("Ignoring log event after log4j was shut down");
/* 211 */       return true;
/*     */     } 
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean tryPublish(RingBufferLogEventTranslator translator) {
/*     */     try {
/* 221 */       return this.disruptor.getRingBuffer().tryPublishEvent(translator);
/* 222 */     } catch (NullPointerException npe) {
/*     */       
/* 224 */       logWarningOnNpeFromDisruptorPublish(translator);
/* 225 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void enqueueLogMessageWhenQueueFull(RingBufferLogEventTranslator translator) {
/*     */     try {
/* 234 */       if (synchronizeEnqueueWhenQueueFull()) {
/* 235 */         synchronized (this.queueFullEnqueueLock) {
/* 236 */           this.disruptor.publishEvent(translator);
/*     */         } 
/*     */       } else {
/* 239 */         this.disruptor.publishEvent(translator);
/*     */       } 
/* 241 */     } catch (NullPointerException npe) {
/*     */       
/* 243 */       logWarningOnNpeFromDisruptorPublish(translator);
/*     */     } 
/*     */   }
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
/*     */   void enqueueLogMessageWhenQueueFull(EventTranslatorVararg<RingBufferLogEvent> translator, AsyncLogger asyncLogger, StackTraceElement location, String fqcn, Level level, Marker marker, Message msg, Throwable thrown) {
/*     */     try {
/* 260 */       if (synchronizeEnqueueWhenQueueFull()) {
/* 261 */         synchronized (this.queueFullEnqueueLock) {
/* 262 */           this.disruptor.getRingBuffer().publishEvent(translator, new Object[] { asyncLogger, location, fqcn, level, marker, msg, thrown });
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 272 */         this.disruptor.getRingBuffer().publishEvent(translator, new Object[] { asyncLogger, location, fqcn, level, marker, msg, thrown });
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 281 */     catch (NullPointerException npe) {
/*     */       
/* 283 */       logWarningOnNpeFromDisruptorPublish(level, fqcn, msg, thrown);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean synchronizeEnqueueWhenQueueFull() {
/* 288 */     if (DisruptorUtil.ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL && this.backgroundThreadId != 
/*     */       
/* 290 */       Thread.currentThread().getId())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 295 */       if (!(Thread.currentThread() instanceof org.apache.logging.log4j.core.util.Log4jThread)); } 
/*     */     return false;
/*     */   }
/*     */   private void logWarningOnNpeFromDisruptorPublish(RingBufferLogEventTranslator translator) {
/* 299 */     logWarningOnNpeFromDisruptorPublish(translator.level, translator.loggerName, translator.message, translator.thrown);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void logWarningOnNpeFromDisruptorPublish(Level level, String fqcn, Message msg, Throwable thrown) {
/* 305 */     LOGGER.warn("[{}] Ignoring log event after log4j was shut down: {} [{}] {}{}", this.contextName, level, fqcn, msg
/* 306 */         .getFormattedMessage(), (thrown == null) ? "" : Throwables.toStringList(thrown));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseThreadLocals() {
/* 317 */     return this.useThreadLocalTranslator;
/*     */   }
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
/*     */   public void setUseThreadLocals(boolean allow) {
/* 332 */     this.useThreadLocalTranslator = allow;
/* 333 */     LOGGER.trace("[{}] AsyncLoggers have been modified to use a {} translator", this.contextName, this.useThreadLocalTranslator ? "threadlocal" : "vararg");
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncLoggerDisruptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */