/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.EventTranslator;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RingBufferLogEventTranslator
/*     */   implements EventTranslator<RingBufferLogEvent>
/*     */ {
/*  40 */   private static final ContextDataInjector INJECTOR = ContextDataInjectorFactory.createInjector();
/*     */   private AsyncLogger asyncLogger;
/*     */   String loggerName;
/*     */   protected Marker marker;
/*     */   protected String fqcn;
/*     */   protected Level level;
/*     */   protected Message message;
/*     */   protected Throwable thrown;
/*     */   private ThreadContext.ContextStack contextStack;
/*  49 */   private long threadId = Thread.currentThread().getId();
/*  50 */   private String threadName = Thread.currentThread().getName();
/*  51 */   private int threadPriority = Thread.currentThread().getPriority();
/*     */   
/*     */   private StackTraceElement location;
/*     */   
/*     */   private Clock clock;
/*     */   private NanoClock nanoClock;
/*     */   
/*     */   public void translateTo(RingBufferLogEvent event, long sequence) {
/*     */     try {
/*  60 */       event.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, INJECTOR
/*     */ 
/*     */           
/*  63 */           .injectContextData(null, (StringMap)event.getContextData()), this.contextStack, this.threadId, this.threadName, this.threadPriority, this.location, this.clock, this.nanoClock);
/*     */     } finally {
/*     */       
/*  66 */       clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clear() {
/*  74 */     setBasicValues(null, null, null, null, null, null, null, null, null, null, null);
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
/*     */   public void setBasicValues(AsyncLogger anAsyncLogger, String aLoggerName, Marker aMarker, String theFqcn, Level aLevel, Message msg, Throwable aThrowable, ThreadContext.ContextStack aContextStack, StackTraceElement aLocation, Clock aClock, NanoClock aNanoClock) {
/*  92 */     this.asyncLogger = anAsyncLogger;
/*  93 */     this.loggerName = aLoggerName;
/*  94 */     this.marker = aMarker;
/*  95 */     this.fqcn = theFqcn;
/*  96 */     this.level = aLevel;
/*  97 */     this.message = msg;
/*  98 */     this.thrown = aThrowable;
/*  99 */     this.contextStack = aContextStack;
/* 100 */     this.location = aLocation;
/* 101 */     this.clock = aClock;
/* 102 */     this.nanoClock = aNanoClock;
/*     */   }
/*     */   
/*     */   public void updateThreadValues() {
/* 106 */     Thread currentThread = Thread.currentThread();
/* 107 */     this.threadId = currentThread.getId();
/* 108 */     this.threadName = currentThread.getName();
/* 109 */     this.threadPriority = currentThread.getPriority();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */