/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ public class LockingReliabilityStrategy
/*     */   implements ReliabilityStrategy, LocationAwareReliabilityStrategy
/*     */ {
/*     */   private final LoggerConfig loggerConfig;
/*  35 */   private final ReadWriteLock reconfigureLock = new ReentrantReadWriteLock();
/*     */   private volatile boolean isStopping;
/*     */   
/*     */   public LockingReliabilityStrategy(LoggerConfig loggerConfig) {
/*  39 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig was null");
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
/*  53 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  55 */       config.log(loggerName, fqcn, marker, level, data, t);
/*     */     } finally {
/*  57 */       config.getReliabilityStrategy().afterLogEvent();
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
/*  72 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  74 */       config.log(loggerName, fqcn, location, marker, level, data, t);
/*     */     } finally {
/*  76 */       config.getReliabilityStrategy().afterLogEvent();
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
/*  88 */     LoggerConfig config = getActiveLoggerConfig(reconfigured);
/*     */     try {
/*  90 */       config.log(event);
/*     */     } finally {
/*  92 */       config.getReliabilityStrategy().afterLogEvent();
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
/* 105 */     LoggerConfig result = this.loggerConfig;
/* 106 */     if (!beforeLogEvent()) {
/* 107 */       result = (LoggerConfig)next.get();
/* 108 */       return (result == this.loggerConfig) ? result : result.getReliabilityStrategy().getActiveLoggerConfig(next);
/*     */     } 
/* 110 */     return result;
/*     */   }
/*     */   
/*     */   private boolean beforeLogEvent() {
/* 114 */     this.reconfigureLock.readLock().lock();
/* 115 */     if (this.isStopping) {
/* 116 */       this.reconfigureLock.readLock().unlock();
/* 117 */       return false;
/*     */     } 
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterLogEvent() {
/* 124 */     this.reconfigureLock.readLock().unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeStopAppenders() {
/* 134 */     this.reconfigureLock.writeLock().lock();
/*     */     try {
/* 136 */       this.isStopping = true;
/*     */     } finally {
/* 138 */       this.reconfigureLock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beforeStopConfiguration(Configuration configuration) {}
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\LockingReliabilityStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */