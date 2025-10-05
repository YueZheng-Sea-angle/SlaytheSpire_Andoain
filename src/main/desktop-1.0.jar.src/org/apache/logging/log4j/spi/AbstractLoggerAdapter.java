/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLoggerAdapter<L>
/*     */   implements LoggerAdapter<L>, LoggerContextShutdownAware
/*     */ {
/*  41 */   protected final Map<LoggerContext, ConcurrentMap<String, L>> registry = new ConcurrentHashMap<>();
/*     */   
/*  43 */   private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
/*     */ 
/*     */   
/*     */   public L getLogger(String name) {
/*  47 */     LoggerContext context = getContext();
/*  48 */     ConcurrentMap<String, L> loggers = getLoggersInContext(context);
/*  49 */     L logger = loggers.get(name);
/*  50 */     if (logger != null) {
/*  51 */       return logger;
/*     */     }
/*  53 */     loggers.putIfAbsent(name, newLogger(name, context));
/*  54 */     return loggers.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void contextShutdown(LoggerContext loggerContext) {
/*  59 */     this.registry.remove(loggerContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConcurrentMap<String, L> getLoggersInContext(LoggerContext context) {
/*     */     ConcurrentMap<String, L> loggers;
/*  70 */     this.lock.readLock().lock();
/*     */     try {
/*  72 */       loggers = this.registry.get(context);
/*     */     } finally {
/*  74 */       this.lock.readLock().unlock();
/*     */     } 
/*     */     
/*  77 */     if (loggers != null) {
/*  78 */       return loggers;
/*     */     }
/*  80 */     this.lock.writeLock().lock();
/*     */     try {
/*  82 */       loggers = this.registry.get(context);
/*  83 */       if (loggers == null) {
/*  84 */         loggers = new ConcurrentHashMap<>();
/*  85 */         this.registry.put(context, loggers);
/*  86 */         if (context instanceof LoggerContextShutdownEnabled) {
/*  87 */           ((LoggerContextShutdownEnabled)context).addShutdownListener(this);
/*     */         }
/*     */       } 
/*  90 */       return loggers;
/*     */     } finally {
/*  92 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<LoggerContext> getLoggerContexts() {
/* 100 */     return new HashSet<>(this.registry.keySet());
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
/*     */   protected abstract L newLogger(String paramString, LoggerContext paramLoggerContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract LoggerContext getContext();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected LoggerContext getContext(Class<?> callerClass) {
/* 130 */     ClassLoader cl = null;
/* 131 */     if (callerClass != null) {
/* 132 */       cl = callerClass.getClassLoader();
/*     */     }
/* 134 */     if (cl == null) {
/* 135 */       cl = LoaderUtil.getThreadContextClassLoader();
/*     */     }
/* 137 */     return LogManager.getContext(cl, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 142 */     this.lock.writeLock().lock();
/*     */     try {
/* 144 */       this.registry.clear();
/*     */     } finally {
/* 146 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\AbstractLoggerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */