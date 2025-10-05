/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
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
/*     */ public class AsyncLoggerContext
/*     */   extends LoggerContext
/*     */ {
/*     */   private final AsyncLoggerDisruptor loggerDisruptor;
/*     */   
/*     */   public AsyncLoggerContext(String name) {
/*  38 */     super(name);
/*  39 */     this.loggerDisruptor = new AsyncLoggerDisruptor(name);
/*     */   }
/*     */   
/*     */   public AsyncLoggerContext(String name, Object externalContext) {
/*  43 */     super(name, externalContext);
/*  44 */     this.loggerDisruptor = new AsyncLoggerDisruptor(name);
/*     */   }
/*     */   
/*     */   public AsyncLoggerContext(String name, Object externalContext, URI configLocn) {
/*  48 */     super(name, externalContext, configLocn);
/*  49 */     this.loggerDisruptor = new AsyncLoggerDisruptor(name);
/*     */   }
/*     */   
/*     */   public AsyncLoggerContext(String name, Object externalContext, String configLocn) {
/*  53 */     super(name, externalContext, configLocn);
/*  54 */     this.loggerDisruptor = new AsyncLoggerDisruptor(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory) {
/*  59 */     return new AsyncLogger(ctx, name, messageFactory, this.loggerDisruptor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  64 */     super.setName("AsyncContext[" + name + "]");
/*  65 */     this.loggerDisruptor.setContextName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  75 */     this.loggerDisruptor.start();
/*  76 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Configuration config) {
/*  86 */     maybeStartHelper(config);
/*  87 */     super.start(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeStartHelper(Configuration config) {
/*  94 */     if (config instanceof org.apache.logging.log4j.core.config.DefaultConfiguration) {
/*  95 */       StatusLogger.getLogger().debug("[{}] Not starting Disruptor for DefaultConfiguration.", getName());
/*     */     } else {
/*  97 */       this.loggerDisruptor.start();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 103 */     setStopping();
/*     */     
/* 105 */     this.loggerDisruptor.stop(timeout, timeUnit);
/* 106 */     super.stop(timeout, timeUnit);
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin() {
/* 117 */     return this.loggerDisruptor.createRingBufferAdmin(getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseThreadLocals(boolean useThreadLocals) {
/* 125 */     this.loggerDisruptor.setUseThreadLocals(useThreadLocals);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncLoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */