/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*     */ import org.apache.logging.log4j.core.time.Instant;
/*     */ import org.apache.logging.log4j.core.time.MutableInstant;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ public abstract class AbstractLogEvent
/*     */   implements LogEvent
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private volatile MutableInstant instant;
/*     */   
/*     */   public LogEvent toImmutable() {
/*  47 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadOnlyStringMap getContextData() {
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContextMap() {
/*  60 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getContextStack() {
/*  65 */     return (ThreadContext.ContextStack)ThreadContext.EMPTY_STACK;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerFqcn() {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLoggerName() {
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Marker getMarker() {
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public StackTraceElement getSource() {
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getThreadId() {
/* 100 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadName() {
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/* 110 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrown() {
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ThrowableProxy getThrownProxy() {
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getTimeMillis() {
/* 125 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public Instant getInstant() {
/* 130 */     return (Instant)getMutableInstant();
/*     */   }
/*     */   
/*     */   protected final MutableInstant getMutableInstant() {
/* 134 */     if (this.instant == null) {
/* 135 */       this.instant = new MutableInstant();
/*     */     }
/* 137 */     return this.instant;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfBatch() {
/* 142 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndOfBatch(boolean endOfBatch) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeLocation(boolean locationRequired) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNanoTime() {
/* 162 */     return 0L;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\AbstractLogEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */