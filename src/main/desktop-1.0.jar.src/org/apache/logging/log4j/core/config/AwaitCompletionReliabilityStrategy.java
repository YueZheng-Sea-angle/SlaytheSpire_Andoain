/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ public class AwaitCompletionReliabilityStrategy
/*     */   implements ReliabilityStrategy, LocationAwareReliabilityStrategy
/*     */ {
/*     */   private static final int MAX_RETRIES = 3;
/*  40 */   private final AtomicInteger counter = new AtomicInteger();
/*  41 */   private final AtomicBoolean shutdown = new AtomicBoolean(false);
/*  42 */   private final Lock shutdownLock = new ReentrantLock();
/*  43 */   private final Condition noLogEvents = this.shutdownLock.newCondition();
/*     */   private final LoggerConfig loggerConfig;
/*     */   
/*     */   public AwaitCompletionReliabilityStrategy(LoggerConfig loggerConfig) {
/*  47 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig is null");
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
/*     */   public void log(Supplier<LoggerConfig> reconfigured, String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/*  61 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  63 */       config.log(loggerName, fqcn, marker, level, data, t);
/*     */     } finally {
/*  65 */       config.getReliabilityStrategy().afterLogEvent();
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
/*     */   public void log(Supplier<LoggerConfig> reconfigured, String loggerName, String fqcn, StackTraceElement location, Marker marker, Level level, Message data, Throwable t) {
/*  80 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  82 */       config.log(loggerName, fqcn, location, marker, level, data, t);
/*     */     } finally {
/*  84 */       config.getReliabilityStrategy().afterLogEvent();
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
/*     */   public void log(Supplier<LoggerConfig> reconfigured, LogEvent event) {
/*  96 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  98 */       config.log(event);
/*     */     } finally {
/* 100 */       config.getReliabilityStrategy().afterLogEvent();
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
/*     */   public LoggerConfig getActiveLoggerConfig(Supplier<LoggerConfig> next) {
/* 113 */     LoggerConfig result = this.loggerConfig;
/* 114 */     if (!beforeLogEvent()) {
/* 115 */       result = (LoggerConfig)next.get();
/* 116 */       return (result == this.loggerConfig) ? result : result.getReliabilityStrategy().getActiveLoggerConfig(next);
/*     */     } 
/* 118 */     return result;
/*     */   }
/*     */   
/*     */   private boolean beforeLogEvent() {
/* 122 */     return (this.counter.incrementAndGet() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterLogEvent() {
/* 127 */     if (this.counter.decrementAndGet() == 0 && this.shutdown.get()) {
/* 128 */       signalCompletionIfShutdown();
/*     */     }
/*     */   }
/*     */   
/*     */   private void signalCompletionIfShutdown() {
/* 133 */     Lock lock = this.shutdownLock;
/* 134 */     lock.lock();
/*     */     try {
/* 136 */       this.noLogEvents.signalAll();
/*     */     } finally {
/* 138 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeStopAppenders() {
/* 149 */     waitForCompletion();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void waitForCompletion() {
/* 156 */     this.shutdownLock.lock();
/*     */     try {
/* 158 */       if (this.shutdown.compareAndSet(false, true)) {
/* 159 */         int retries = 0;
/*     */         
/* 161 */         while (!this.counter.compareAndSet(0, -2147483648)) {
/*     */ 
/*     */           
/* 164 */           if (this.counter.get() < 0) {
/*     */             return;
/*     */           }
/*     */           
/*     */           try {
/* 169 */             this.noLogEvents.await((retries + 1), TimeUnit.SECONDS);
/* 170 */           } catch (InterruptedException ie) {
/* 171 */             if (++retries > 3) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 178 */       this.shutdownLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beforeStopConfiguration(Configuration configuration) {}
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\AwaitCompletionReliabilityStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */