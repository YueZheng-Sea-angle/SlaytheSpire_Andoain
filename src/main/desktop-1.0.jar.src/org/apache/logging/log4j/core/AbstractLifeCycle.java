/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class AbstractLifeCycle
/*     */   implements LifeCycle2
/*     */ {
/*     */   public static final int DEFAULT_STOP_TIMEOUT = 0;
/*  33 */   public static final TimeUnit DEFAULT_STOP_TIMEUNIT = TimeUnit.MILLISECONDS;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Logger getStatusLogger() {
/*  46 */     return LOGGER;
/*     */   }
/*     */   
/*  49 */   private volatile LifeCycle.State state = LifeCycle.State.INITIALIZED;
/*     */   
/*     */   protected boolean equalsImpl(Object obj) {
/*  52 */     if (this == obj) {
/*  53 */       return true;
/*     */     }
/*  55 */     if (obj == null) {
/*  56 */       return false;
/*     */     }
/*  58 */     if (getClass() != obj.getClass()) {
/*  59 */       return false;
/*     */     }
/*  61 */     LifeCycle other = (LifeCycle)obj;
/*  62 */     if (this.state != other.getState()) {
/*  63 */       return false;
/*     */     }
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public LifeCycle.State getState() {
/*  70 */     return this.state;
/*     */   }
/*     */   
/*     */   protected int hashCodeImpl() {
/*  74 */     int prime = 31;
/*  75 */     int result = 1;
/*  76 */     result = 31 * result + ((this.state == null) ? 0 : this.state.hashCode());
/*  77 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/*  81 */     return (this.state == LifeCycle.State.INITIALIZED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/*  86 */     return (this.state == LifeCycle.State.STARTED);
/*     */   }
/*     */   
/*     */   public boolean isStarting() {
/*  90 */     return (this.state == LifeCycle.State.STARTING);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/*  95 */     return (this.state == LifeCycle.State.STOPPED);
/*     */   }
/*     */   
/*     */   public boolean isStopping() {
/*  99 */     return (this.state == LifeCycle.State.STOPPING);
/*     */   }
/*     */   
/*     */   protected void setStarted() {
/* 103 */     setState(LifeCycle.State.STARTED);
/*     */   }
/*     */   
/*     */   protected void setStarting() {
/* 107 */     setState(LifeCycle.State.STARTING);
/*     */   }
/*     */   
/*     */   protected void setState(LifeCycle.State newState) {
/* 111 */     this.state = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStopped() {
/* 117 */     setState(LifeCycle.State.STOPPED);
/*     */   }
/*     */   
/*     */   protected void setStopping() {
/* 121 */     setState(LifeCycle.State.STOPPING);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 126 */     this.state = LifeCycle.State.INITIALIZED;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 131 */     setStarted();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 136 */     stop(0L, DEFAULT_STOP_TIMEUNIT);
/*     */   }
/*     */   
/*     */   protected boolean stop(Future<?> future) {
/* 140 */     boolean stopped = true;
/* 141 */     if (future != null) {
/* 142 */       if (future.isCancelled() || future.isDone()) {
/* 143 */         return true;
/*     */       }
/* 145 */       stopped = future.cancel(true);
/*     */     } 
/* 147 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 152 */     this.state = LifeCycle.State.STOPPED;
/* 153 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\AbstractLifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */