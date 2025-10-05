/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager>
/*     */   extends AbstractAppender
/*     */ {
/*     */   public static final int DEFAULT_RECONNECT_INTERVAL_MILLIS = 5000;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B> {}
/*     */   
/*  49 */   private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*  50 */   private final Lock readLock = this.lock.readLock();
/*  51 */   private final Lock writeLock = this.lock.writeLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private T manager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected AbstractDatabaseAppender(String name, Filter filter, boolean ignoreExceptions, T manager) {
/*  67 */     super(name, filter, null, ignoreExceptions, Property.EMPTY_ARRAY);
/*  68 */     this.manager = manager;
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
/*     */   protected AbstractDatabaseAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, T manager) {
/*  84 */     super(name, filter, layout, ignoreExceptions, properties);
/*  85 */     this.manager = manager;
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
/*     */   @Deprecated
/*     */   protected AbstractDatabaseAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, T manager) {
/* 102 */     super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
/* 103 */     this.manager = manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void append(LogEvent event) {
/* 108 */     this.readLock.lock();
/*     */     try {
/* 110 */       getManager().write(event, toSerializable(event));
/* 111 */     } catch (LoggingException e) {
/* 112 */       LOGGER.error("Unable to write to database [{}] for appender [{}].", getManager().getName(), 
/* 113 */           getName(), e);
/* 114 */       throw e;
/* 115 */     } catch (Exception e) {
/* 116 */       LOGGER.error("Unable to write to database [{}] for appender [{}].", getManager().getName(), 
/* 117 */           getName(), e);
/* 118 */       throw new AppenderLoggingException("Unable to write to database in appender: " + e.getMessage(), e);
/*     */     } finally {
/* 120 */       this.readLock.unlock();
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
/*     */   public final Layout<LogEvent> getLayout() {
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T getManager() {
/* 141 */     return this.manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void replaceManager(T manager) {
/* 152 */     this.writeLock.lock();
/*     */     try {
/* 154 */       T old = getManager();
/* 155 */       if (!manager.isRunning()) {
/* 156 */         manager.startup();
/*     */       }
/* 158 */       this.manager = manager;
/* 159 */       old.close();
/*     */     } finally {
/* 161 */       this.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void start() {
/* 167 */     if (getManager() == null) {
/* 168 */       LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", getName());
/*     */     }
/* 170 */     super.start();
/* 171 */     if (getManager() != null) {
/* 172 */       getManager().startup();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 178 */     setStopping();
/* 179 */     boolean stopped = stop(timeout, timeUnit, false);
/* 180 */     if (getManager() != null) {
/* 181 */       stopped &= getManager().stop(timeout, timeUnit);
/*     */     }
/* 183 */     setStopped();
/* 184 */     return stopped;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */