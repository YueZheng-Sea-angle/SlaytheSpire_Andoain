/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.ErrorHandler;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class DefaultErrorHandler
/*     */   implements ErrorHandler
/*     */ {
/*  37 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final int MAX_EXCEPTION_COUNT = 3;
/*     */   
/*  41 */   private static final long EXCEPTION_INTERVAL_NANOS = TimeUnit.MINUTES.toNanos(5L);
/*     */   
/*  43 */   private int exceptionCount = 0;
/*     */   
/*  45 */   private long lastExceptionInstantNanos = System.nanoTime() - EXCEPTION_INTERVAL_NANOS - 1L;
/*     */   
/*     */   private final Appender appender;
/*     */   
/*     */   public DefaultErrorHandler(Appender appender) {
/*  50 */     this.appender = Objects.<Appender>requireNonNull(appender, "appender");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/*  59 */     boolean allowed = acquirePermit();
/*  60 */     if (allowed) {
/*  61 */       LOGGER.error(msg);
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
/*     */   public void error(String msg, Throwable error) {
/*  73 */     boolean allowed = acquirePermit();
/*  74 */     if (allowed) {
/*  75 */       LOGGER.error(msg, error);
/*     */     }
/*  77 */     if (!this.appender.ignoreExceptions() && error != null && !(error instanceof AppenderLoggingException)) {
/*  78 */       throw new AppenderLoggingException(msg, error);
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
/*     */   public void error(String msg, LogEvent event, Throwable error) {
/*  91 */     boolean allowed = acquirePermit();
/*  92 */     if (allowed) {
/*  93 */       LOGGER.error(msg, error);
/*     */     }
/*  95 */     if (!this.appender.ignoreExceptions() && error != null && !(error instanceof AppenderLoggingException)) {
/*  96 */       throw new AppenderLoggingException(msg, error);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean acquirePermit() {
/* 101 */     long currentInstantNanos = System.nanoTime();
/* 102 */     synchronized (this) {
/* 103 */       if (currentInstantNanos - this.lastExceptionInstantNanos > EXCEPTION_INTERVAL_NANOS) {
/* 104 */         this.lastExceptionInstantNanos = currentInstantNanos;
/* 105 */         return true;
/* 106 */       }  if (this.exceptionCount < 3) {
/* 107 */         this.exceptionCount++;
/* 108 */         this.lastExceptionInstantNanos = currentInstantNanos;
/* 109 */         return true;
/*     */       } 
/* 111 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Appender getAppender() {
/* 117 */     return this.appender;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\DefaultErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */