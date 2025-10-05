/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public class AwaitUnconditionallyReliabilityStrategy
/*     */   implements ReliabilityStrategy, LocationAwareReliabilityStrategy
/*     */ {
/*     */   private static final long DEFAULT_SLEEP_MILLIS = 5000L;
/*  36 */   private static final long SLEEP_MILLIS = sleepMillis();
/*     */   private final LoggerConfig loggerConfig;
/*     */   
/*     */   public AwaitUnconditionallyReliabilityStrategy(LoggerConfig loggerConfig) {
/*  40 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig is null");
/*     */   }
/*     */   
/*     */   private static long sleepMillis() {
/*  44 */     return PropertiesUtil.getProperties().getLongProperty("log4j.waitMillisBeforeStopOldConfig", 5000L);
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
/*  58 */     this.loggerConfig.log(loggerName, fqcn, marker, level, data, t);
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
/*  72 */     this.loggerConfig.log(loggerName, fqcn, location, marker, level, data, t);
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
/*  83 */     this.loggerConfig.log(event);
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
/*  95 */     return this.loggerConfig;
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
/*     */   public void afterLogEvent() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeStopAppenders() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeStopConfiguration(Configuration configuration) {
/* 128 */     if (this.loggerConfig == configuration.getRootLogger())
/*     */       try {
/* 130 */         Thread.sleep(SLEEP_MILLIS);
/* 131 */       } catch (InterruptedException e) {
/* 132 */         StatusLogger.getLogger().warn("Sleep before stop configuration was interrupted.");
/*     */       }  
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\AwaitUnconditionallyReliabilityStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */