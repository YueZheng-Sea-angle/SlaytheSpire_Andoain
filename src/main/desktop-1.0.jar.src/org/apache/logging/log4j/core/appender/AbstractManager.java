/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractManager
/*     */   implements AutoCloseable
/*     */ {
/*  48 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static final Map<String, AbstractManager> MAP = new HashMap<>();
/*     */   
/*  54 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */   
/*     */   protected int count;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final LoggerContext loggerContext;
/*     */ 
/*     */   
/*     */   protected AbstractManager(LoggerContext loggerContext, String name) {
/*  66 */     this.loggerContext = loggerContext;
/*  67 */     this.name = name;
/*  68 */     LOGGER.debug("Starting {} {}", getClass().getSimpleName(), name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  76 */     stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
/*     */   }
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  80 */     boolean stopped = true;
/*  81 */     LOCK.lock();
/*     */     try {
/*  83 */       this.count--;
/*  84 */       if (this.count <= 0) {
/*  85 */         MAP.remove(this.name);
/*  86 */         LOGGER.debug("Shutting down {} {}", getClass().getSimpleName(), getName());
/*  87 */         stopped = releaseSub(timeout, timeUnit);
/*  88 */         LOGGER.debug("Shut down {} {}, all resources released: {}", getClass().getSimpleName(), getName(), Boolean.valueOf(stopped));
/*     */       } 
/*     */     } finally {
/*  91 */       LOCK.unlock();
/*     */     } 
/*  93 */     return stopped;
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
/*     */   public static <M extends AbstractManager, T> M getManager(String name, ManagerFactory<M, T> factory, T data) {
/* 109 */     LOCK.lock();
/*     */     
/*     */     try {
/* 112 */       AbstractManager abstractManager = MAP.get(name);
/* 113 */       if (abstractManager == null) {
/* 114 */         abstractManager = ((ManagerFactory<AbstractManager, T>)Objects.requireNonNull(factory, "factory")).createManager(name, data);
/* 115 */         if (abstractManager == null) {
/* 116 */           throw new IllegalStateException("ManagerFactory [" + factory + "] unable to create manager for [" + name + "] with data [" + data + "]");
/*     */         }
/*     */         
/* 119 */         MAP.put(name, abstractManager);
/*     */       } else {
/* 121 */         abstractManager.updateData(data);
/*     */       } 
/* 123 */       abstractManager.count++;
/* 124 */       return (M)abstractManager;
/*     */     } finally {
/* 126 */       LOCK.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateData(Object data) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasManager(String name) {
/* 145 */     LOCK.lock();
/*     */     try {
/* 147 */       return MAP.containsKey(name);
/*     */     } finally {
/* 149 */       LOCK.unlock();
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
/*     */   protected static <M extends AbstractManager> M narrow(Class<M> narrowClass, AbstractManager manager) {
/* 165 */     if (narrowClass.isAssignableFrom(manager.getClass())) {
/* 166 */       return (M)manager;
/*     */     }
/* 168 */     throw new ConfigurationException("Configuration has multiple incompatible Appenders pointing to the same resource '" + manager
/*     */         
/* 170 */         .getName() + "'");
/*     */   }
/*     */   
/*     */   protected static StatusLogger logger() {
/* 174 */     return StatusLogger.getLogger();
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
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 186 */     return true;
/*     */   }
/*     */   
/*     */   protected int getCount() {
/* 190 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getLoggerContext() {
/* 201 */     return this.loggerContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void release() {
/* 210 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 218 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 229 */     return new HashMap<>();
/*     */   }
/*     */   
/*     */   protected void log(Level level, String message, Throwable throwable) {
/* 233 */     Message m = LOGGER.getMessageFactory().newMessage("{} {} {}: {}", new Object[] {
/* 234 */           getClass().getSimpleName(), getName(), message, throwable });
/* 235 */     LOGGER.log(level, m, throwable);
/*     */   }
/*     */   
/*     */   protected void logDebug(String message, Throwable throwable) {
/* 239 */     log(Level.DEBUG, message, throwable);
/*     */   }
/*     */   
/*     */   protected void logError(String message, Throwable throwable) {
/* 243 */     log(Level.ERROR, message, throwable);
/*     */   }
/*     */   
/*     */   protected void logWarn(String message, Throwable throwable) {
/* 247 */     log(Level.WARN, message, throwable);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AbstractManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */