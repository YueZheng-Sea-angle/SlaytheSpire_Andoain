/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventTranslatorVararg;
/*     */ import com.lmax.disruptor.dsl.Disruptor;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.ReliabilityStrategy;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ import org.apache.logging.log4j.util.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncLogger
/*     */   extends Logger
/*     */   implements EventTranslatorVararg<RingBufferLogEvent>
/*     */ {
/*  73 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  74 */   private static final Clock CLOCK = ClockFactory.getClock();
/*  75 */   private static final ContextDataInjector CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
/*     */   
/*  77 */   private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
/*     */   
/*  79 */   private final ThreadLocal<RingBufferLogEventTranslator> threadLocalTranslator = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private final AsyncLoggerDisruptor loggerDisruptor;
/*     */ 
/*     */   
/*     */   private volatile boolean includeLocation;
/*     */   
/*     */   private volatile NanoClock nanoClock;
/*     */   
/*     */   private final TranslatorType threadLocalTranslatorType;
/*     */   
/*     */   private final TranslatorType varargTranslatorType;
/*     */ 
/*     */   
/*     */   public AsyncLogger(LoggerContext context, String name, MessageFactory messageFactory, AsyncLoggerDisruptor loggerDisruptor) {
/*  95 */     super(context, name, messageFactory);
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
/* 148 */     this.threadLocalTranslatorType = new TranslatorType()
/*     */       {
/*     */         void log(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown)
/*     */         {
/* 152 */           AsyncLogger.this.logWithThreadLocalTranslator(fqcn, location, level, marker, message, thrown);
/*     */         }
/*     */ 
/*     */         
/*     */         void log(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 157 */           AsyncLogger.this.logWithThreadLocalTranslator(fqcn, level, marker, message, thrown);
/*     */         }
/*     */       };
/*     */     
/* 161 */     this.varargTranslatorType = new TranslatorType()
/*     */       {
/*     */         void log(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown)
/*     */         {
/* 165 */           AsyncLogger.this.logWithVarargTranslator(fqcn, location, level, marker, message, thrown);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         void log(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 171 */           AsyncLogger.this.logWithVarargTranslator(fqcn, level, marker, message, thrown);
/*     */         } }; this.loggerDisruptor = loggerDisruptor; this.includeLocation = this.privateConfig.loggerConfig.isIncludeLocation(); this.nanoClock = context.getConfiguration().getNanoClock();
/*     */   }
/*     */   protected void updateConfiguration(Configuration newConfig) { this.nanoClock = newConfig.getNanoClock(); this.includeLocation = newConfig.getLoggerConfig(this.name).isIncludeLocation(); super.updateConfiguration(newConfig); } NanoClock getNanoClock() { return this.nanoClock; } private RingBufferLogEventTranslator getCachedTranslator() { RingBufferLogEventTranslator result = this.threadLocalTranslator.get(); if (result == null) {
/*     */       result = new RingBufferLogEventTranslator(); this.threadLocalTranslator.set(result);
/* 176 */     }  return result; } private TranslatorType getTranslatorType() { return this.loggerDisruptor.isUseThreadLocals() ? this.threadLocalTranslatorType : this.varargTranslatorType; }
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable thrown) { getTranslatorType().log(fqcn, level, marker, message, thrown); }
/*     */   public void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) { getTranslatorType().log(fqcn, location, level, marker, message, throwable); } abstract class TranslatorType {
/*     */     abstract void log(String param1String, StackTraceElement param1StackTraceElement, Level param1Level, Marker param1Marker, Message param1Message, Throwable param1Throwable); abstract void log(String param1String, Level param1Level, Marker param1Marker, Message param1Message, Throwable param1Throwable); } private boolean isReused(Message message) {
/* 180 */     return message instanceof org.apache.logging.log4j.message.ReusableMessage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logWithThreadLocalTranslator(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 199 */     RingBufferLogEventTranslator translator = getCachedTranslator();
/* 200 */     initTranslator(translator, fqcn, level, marker, message, thrown);
/* 201 */     initTranslatorThreadValues(translator);
/* 202 */     publish(translator);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logWithThreadLocalTranslator(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
/* 222 */     RingBufferLogEventTranslator translator = getCachedTranslator();
/* 223 */     initTranslator(translator, fqcn, location, level, marker, message, thrown);
/* 224 */     initTranslatorThreadValues(translator);
/* 225 */     publish(translator);
/*     */   }
/*     */   
/*     */   private void publish(RingBufferLogEventTranslator translator) {
/* 229 */     if (!this.loggerDisruptor.tryPublish(translator)) {
/* 230 */       handleRingBufferFull(translator);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleRingBufferFull(RingBufferLogEventTranslator translator) {
/* 235 */     if (AbstractLogger.getRecursionDepth() > 1) {
/*     */       
/* 237 */       AsyncQueueFullMessageUtil.logWarningToStatusLogger();
/* 238 */       logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
/*     */       
/* 240 */       translator.clear();
/*     */       return;
/*     */     } 
/* 243 */     EventRoute eventRoute = this.loggerDisruptor.getEventRoute(translator.level);
/* 244 */     switch (eventRoute) {
/*     */       case ENQUEUE:
/* 246 */         this.loggerDisruptor.enqueueLogMessageWhenQueueFull(translator);
/*     */         return;
/*     */       case SYNCHRONOUS:
/* 249 */         logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
/*     */         
/* 251 */         translator.clear();
/*     */         return;
/*     */       case DISCARD:
/* 254 */         translator.clear();
/*     */         return;
/*     */     } 
/* 257 */     throw new IllegalStateException("Unknown EventRoute " + eventRoute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initTranslator(RingBufferLogEventTranslator translator, String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
/* 265 */     translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 270 */         ThreadContext.getImmutableStack(), location, CLOCK, this.nanoClock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initTranslator(RingBufferLogEventTranslator translator, String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 281 */     translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 286 */         ThreadContext.getImmutableStack(), 
/*     */ 
/*     */         
/* 289 */         calcLocationIfRequested(fqcn), CLOCK, this.nanoClock);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initTranslatorThreadValues(RingBufferLogEventTranslator translator) {
/* 297 */     if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
/* 298 */       translator.updateThreadValues();
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
/*     */   private StackTraceElement calcLocationIfRequested(String fqcn) {
/* 312 */     return this.includeLocation ? StackLocatorUtil.calcLocation(fqcn) : null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logWithVarargTranslator(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 331 */     Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
/* 332 */     if (disruptor == null) {
/* 333 */       LOGGER.error("Ignoring log event after Log4j has been shut down.");
/*     */       
/*     */       return;
/*     */     } 
/* 337 */     if (!isReused(message)) {
/* 338 */       InternalAsyncUtil.makeMessageImmutable(message);
/*     */     }
/* 340 */     StackTraceElement location = null;
/*     */     
/* 342 */     if (!disruptor.getRingBuffer().tryPublishEvent(this, new Object[] { this, 
/*     */           
/* 344 */           location = calcLocationIfRequested(fqcn), fqcn, level, marker, message, thrown
/*     */ 
/*     */ 
/*     */         
/*     */         }))
/*     */     {
/* 350 */       handleRingBufferFull(location, fqcn, level, marker, message, thrown);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logWithVarargTranslator(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable thrown) {
/* 371 */     Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
/* 372 */     if (disruptor == null) {
/* 373 */       LOGGER.error("Ignoring log event after Log4j has been shut down.");
/*     */       
/*     */       return;
/*     */     } 
/* 377 */     if (!isReused(message)) {
/* 378 */       InternalAsyncUtil.makeMessageImmutable(message);
/*     */     }
/*     */     
/* 381 */     if (!disruptor.getRingBuffer().tryPublishEvent(this, new Object[] { this, location, fqcn, level, marker, message, thrown
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }))
/*     */     {
/* 389 */       handleRingBufferFull(location, fqcn, level, marker, message, thrown);
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
/*     */   public void translateTo(RingBufferLogEvent event, long sequence, Object... args) {
/* 401 */     AsyncLogger asyncLogger = (AsyncLogger)args[0];
/* 402 */     StackTraceElement location = (StackTraceElement)args[1];
/* 403 */     String fqcn = (String)args[2];
/* 404 */     Level level = (Level)args[3];
/* 405 */     Marker marker = (Marker)args[4];
/* 406 */     Message message = (Message)args[5];
/* 407 */     Throwable thrown = (Throwable)args[6];
/*     */ 
/*     */     
/* 410 */     ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
/*     */     
/* 412 */     Thread currentThread = Thread.currentThread();
/* 413 */     String threadName = THREAD_NAME_CACHING_STRATEGY.getThreadName();
/* 414 */     event.setValues(asyncLogger, asyncLogger.getName(), marker, fqcn, level, message, thrown, CONTEXT_DATA_INJECTOR
/*     */ 
/*     */         
/* 417 */         .injectContextData(null, (StringMap)event.getContextData()), contextStack, currentThread
/* 418 */         .getId(), threadName, currentThread.getPriority(), location, CLOCK, this.nanoClock);
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
/*     */ 
/*     */   
/*     */   void logMessageInCurrentThread(String fqcn, Level level, Marker marker, Message message, Throwable thrown) {
/* 435 */     ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
/* 436 */     strategy.log((Supplier)this, getName(), fqcn, marker, level, message, thrown);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleRingBufferFull(StackTraceElement location, String fqcn, Level level, Marker marker, Message msg, Throwable thrown) {
/* 445 */     if (AbstractLogger.getRecursionDepth() > 1) {
/*     */       
/* 447 */       AsyncQueueFullMessageUtil.logWarningToStatusLogger();
/* 448 */       logMessageInCurrentThread(fqcn, level, marker, msg, thrown);
/*     */       return;
/*     */     } 
/* 451 */     EventRoute eventRoute = this.loggerDisruptor.getEventRoute(level);
/* 452 */     switch (eventRoute) {
/*     */       case ENQUEUE:
/* 454 */         this.loggerDisruptor.enqueueLogMessageWhenQueueFull(this, this, location, fqcn, level, marker, msg, thrown);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case SYNCHRONOUS:
/* 464 */         logMessageInCurrentThread(fqcn, level, marker, msg, thrown);
/*     */       
/*     */       case DISCARD:
/*     */         return;
/*     */     } 
/* 469 */     throw new IllegalStateException("Unknown EventRoute " + eventRoute);
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
/*     */   public void actualAsyncLog(RingBufferLogEvent event) {
/* 481 */     LoggerConfig privateConfigLoggerConfig = this.privateConfig.loggerConfig;
/* 482 */     List<Property> properties = privateConfigLoggerConfig.getPropertyList();
/*     */     
/* 484 */     if (properties != null) {
/* 485 */       onPropertiesPresent(event, properties);
/*     */     }
/*     */     
/* 488 */     privateConfigLoggerConfig.getReliabilityStrategy().log((Supplier)this, event);
/*     */   }
/*     */ 
/*     */   
/*     */   private void onPropertiesPresent(RingBufferLogEvent event, List<Property> properties) {
/* 493 */     StringMap contextData = getContextData(event);
/* 494 */     for (int i = 0, size = properties.size(); i < size; i++) {
/* 495 */       Property prop = properties.get(i);
/* 496 */       if (contextData.getValue(prop.getName()) == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 501 */         String value = prop.isValueNeedsLookup() ? this.privateConfig.config.getStrSubstitutor().replace(event, prop.getValue()) : prop.getValue();
/* 502 */         contextData.putValue(prop.getName(), value);
/*     */       } 
/* 504 */     }  event.setContextData(contextData);
/*     */   }
/*     */   
/*     */   private static StringMap getContextData(RingBufferLogEvent event) {
/* 508 */     StringMap contextData = (StringMap)event.getContextData();
/* 509 */     if (contextData.isFrozen()) {
/* 510 */       StringMap temp = ContextDataFactory.createContextData();
/* 511 */       temp.putAll((ReadOnlyStringMap)contextData);
/* 512 */       return temp;
/*     */     } 
/* 514 */     return contextData;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */