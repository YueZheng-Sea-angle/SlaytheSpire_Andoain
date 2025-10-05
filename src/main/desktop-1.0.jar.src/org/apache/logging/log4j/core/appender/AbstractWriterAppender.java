/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.StringLayout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractWriterAppender<M extends WriterManager>
/*     */   extends AbstractAppender
/*     */ {
/*     */   protected final boolean immediateFlush;
/*     */   private final M manager;
/*  47 */   private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*  48 */   private final Lock readLock = this.readWriteLock.readLock();
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
/*     */   protected AbstractWriterAppender(String name, StringLayout layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, Property[] properties, M manager) {
/*  64 */     super(name, filter, (Layout<? extends Serializable>)layout, ignoreExceptions, properties);
/*  65 */     this.manager = manager;
/*  66 */     this.immediateFlush = immediateFlush;
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
/*     */   protected AbstractWriterAppender(String name, StringLayout layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, M manager) {
/*  83 */     super(name, filter, (Layout<? extends Serializable>)layout, ignoreExceptions, Property.EMPTY_ARRAY);
/*  84 */     this.manager = manager;
/*  85 */     this.immediateFlush = immediateFlush;
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
/*     */   public void append(LogEvent event) {
/*  99 */     this.readLock.lock();
/*     */     try {
/* 101 */       String str = (String)getStringLayout().toSerializable(event);
/* 102 */       if (str.length() > 0) {
/* 103 */         this.manager.write(str);
/* 104 */         if (this.immediateFlush || event.isEndOfBatch()) {
/* 105 */           this.manager.flush();
/*     */         }
/*     */       } 
/* 108 */     } catch (AppenderLoggingException ex) {
/* 109 */       error("Unable to write " + this.manager.getName() + " for appender " + getName(), event, (Throwable)ex);
/* 110 */       throw ex;
/*     */     } finally {
/* 112 */       this.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M getManager() {
/* 122 */     return this.manager;
/*     */   }
/*     */   
/*     */   public StringLayout getStringLayout() {
/* 126 */     return (StringLayout)getLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 131 */     if (getLayout() == null) {
/* 132 */       LOGGER.error("No layout set for the appender named [{}].", getName());
/*     */     }
/* 134 */     if (this.manager == null) {
/* 135 */       LOGGER.error("No OutputStreamManager set for the appender named [{}].", getName());
/*     */     }
/* 137 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 142 */     setStopping();
/* 143 */     boolean stopped = stop(timeout, timeUnit, false);
/* 144 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 145 */     setStopped();
/* 146 */     return stopped;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AbstractWriterAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */