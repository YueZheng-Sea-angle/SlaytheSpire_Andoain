/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.ThreadNameCachingStrategy;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.StringMap;
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
/*     */ public class ReusableLogEventFactory
/*     */   implements LogEventFactory, LocationAwareLogEventFactory
/*     */ {
/*  38 */   private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
/*  39 */   private static final Clock CLOCK = ClockFactory.getClock();
/*     */   
/*  41 */   private static final ThreadLocal<MutableLogEvent> mutableLogEventThreadLocal = new ThreadLocal<>();
/*  42 */   private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
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
/*     */   public LogEvent createEvent(String loggerName, Marker marker, String fqcn, Level level, Message message, List<Property> properties, Throwable t) {
/*  60 */     return createEvent(loggerName, marker, fqcn, null, level, message, properties, t);
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
/*     */   public LogEvent createEvent(String loggerName, Marker marker, String fqcn, StackTraceElement location, Level level, Message message, List<Property> properties, Throwable t) {
/*  80 */     MutableLogEvent result = getOrCreateMutableLogEvent();
/*  81 */     result.reserved = true;
/*     */ 
/*     */ 
/*     */     
/*  85 */     result.setLoggerName(loggerName);
/*  86 */     result.setMarker(marker);
/*  87 */     result.setLoggerFqcn(fqcn);
/*  88 */     result.setLevel((level == null) ? Level.OFF : level);
/*  89 */     result.setMessage(message);
/*  90 */     result.initTime(CLOCK, Log4jLogEvent.getNanoClock());
/*  91 */     result.setThrown(t);
/*  92 */     result.setSource(location);
/*  93 */     result.setContextData(this.injector.injectContextData(properties, (StringMap)result.getContextData()));
/*  94 */     result.setContextStack((ThreadContext.getDepth() == 0) ? (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK : ThreadContext.cloneStack());
/*     */     
/*  96 */     if (THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
/*  97 */       result.setThreadName(Thread.currentThread().getName());
/*  98 */       result.setThreadPriority(Thread.currentThread().getPriority());
/*     */     } 
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   private static MutableLogEvent getOrCreateMutableLogEvent() {
/* 104 */     MutableLogEvent result = mutableLogEventThreadLocal.get();
/* 105 */     return (result == null || result.reserved) ? createInstance(result) : result;
/*     */   }
/*     */   
/*     */   private static MutableLogEvent createInstance(MutableLogEvent existing) {
/* 109 */     MutableLogEvent result = new MutableLogEvent();
/*     */ 
/*     */     
/* 112 */     result.setThreadId(Thread.currentThread().getId());
/* 113 */     result.setThreadName(Thread.currentThread().getName());
/* 114 */     result.setThreadPriority(Thread.currentThread().getPriority());
/* 115 */     if (existing == null) {
/* 116 */       mutableLogEventThreadLocal.set(result);
/*     */     }
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void release(LogEvent logEvent) {
/* 128 */     if (logEvent instanceof MutableLogEvent) {
/* 129 */       MutableLogEvent mutableLogEvent = (MutableLogEvent)logEvent;
/* 130 */       mutableLogEvent.clear();
/* 131 */       mutableLogEvent.reserved = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ReusableLogEventFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */