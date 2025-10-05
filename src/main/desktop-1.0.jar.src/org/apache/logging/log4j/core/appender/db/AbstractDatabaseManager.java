/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.io.Flushable;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
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
/*     */ public abstract class AbstractDatabaseManager
/*     */   extends AbstractManager
/*     */   implements Flushable
/*     */ {
/*     */   private final ArrayList<LogEvent> buffer;
/*     */   private final int bufferSize;
/*     */   private final Layout<? extends Serializable> layout;
/*     */   private boolean running;
/*     */   
/*     */   protected static abstract class AbstractFactoryData
/*     */   {
/*     */     private final int bufferSize;
/*     */     private final Layout<? extends Serializable> layout;
/*     */     
/*     */     protected AbstractFactoryData(int bufferSize, Layout<? extends Serializable> layout) {
/*  50 */       this.bufferSize = bufferSize;
/*  51 */       this.layout = layout;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getBufferSize() {
/*  60 */       return this.bufferSize;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Layout<? extends Serializable> getLayout() {
/*  69 */       return this.layout;
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
/*     */ 
/*     */   
/*     */   protected static <M extends AbstractDatabaseManager, T extends AbstractFactoryData> M getManager(String name, T data, ManagerFactory<M, T> factory) {
/*  87 */     return (M)AbstractManager.getManager(name, factory, data);
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
/*     */   protected AbstractDatabaseManager(String name, int bufferSize) {
/* 104 */     this(name, bufferSize, (Layout<? extends Serializable>)null);
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
/*     */   protected AbstractDatabaseManager(String name, int bufferSize, Layout<? extends Serializable> layout) {
/* 116 */     super(null, name);
/* 117 */     this.bufferSize = bufferSize;
/* 118 */     this.buffer = new ArrayList<>(bufferSize + 1);
/* 119 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   protected void buffer(LogEvent event) {
/* 123 */     this.buffer.add(event.toImmutable());
/* 124 */     if (this.buffer.size() >= this.bufferSize || event.isEndOfBatch()) {
/* 125 */       flush();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean commitAndClose();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void connectAndStart();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void flush() {
/* 151 */     if (isRunning() && isBuffered()) {
/* 152 */       connectAndStart();
/*     */       try {
/* 154 */         for (LogEvent event : this.buffer) {
/* 155 */           writeInternal(event, (this.layout != null) ? this.layout.toSerializable(event) : null);
/*     */         }
/*     */       } finally {
/* 158 */         commitAndClose();
/*     */         
/* 160 */         this.buffer.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isBuffered() {
/* 166 */     return (this.bufferSize > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 176 */     return this.running;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 181 */     return shutdown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized boolean shutdown() {
/* 191 */     boolean closed = true;
/* 192 */     flush();
/* 193 */     if (isRunning()) {
/*     */       try {
/* 195 */         closed &= shutdownInternal();
/* 196 */       } catch (Exception e) {
/* 197 */         logWarn("Caught exception while performing database shutdown operations", e);
/* 198 */         closed = false;
/*     */       } finally {
/* 200 */         this.running = false;
/*     */       } 
/*     */     }
/* 203 */     return closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean shutdownInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void startup() {
/* 220 */     if (!isRunning()) {
/*     */       try {
/* 222 */         startupInternal();
/* 223 */         this.running = true;
/* 224 */       } catch (Exception e) {
/* 225 */         logError("Could not perform database startup operations", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void startupInternal() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 240 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final synchronized void write(LogEvent event) {
/* 251 */     write(event, (Serializable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void write(LogEvent event, Serializable serializable) {
/* 261 */     if (isBuffered()) {
/* 262 */       buffer(event);
/*     */     } else {
/* 264 */       writeThrough(event, serializable);
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
/*     */   @Deprecated
/*     */   protected void writeInternal(LogEvent event) {
/* 277 */     writeInternal(event, (Serializable)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeInternal(LogEvent paramLogEvent, Serializable paramSerializable);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeThrough(LogEvent event, Serializable serializable) {
/* 289 */     connectAndStart();
/*     */     try {
/* 291 */       writeInternal(event, serializable);
/*     */     } finally {
/* 293 */       commitAndClose();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\AbstractDatabaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */