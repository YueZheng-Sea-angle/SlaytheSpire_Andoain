/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
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
/*     */ public class DefaultShutdownCallbackRegistry
/*     */   implements ShutdownCallbackRegistry, LifeCycle2, Runnable
/*     */ {
/*  43 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  45 */   private final AtomicReference<LifeCycle.State> state = new AtomicReference<>(LifeCycle.State.INITIALIZED);
/*     */   
/*     */   private final ThreadFactory threadFactory;
/*     */   
/*  49 */   private final Collection<Reference<Cancellable>> hooks = new CopyOnWriteArrayList<>();
/*     */ 
/*     */   
/*     */   private Reference<Thread> shutdownHookRef;
/*     */ 
/*     */   
/*     */   public DefaultShutdownCallbackRegistry() {
/*  56 */     this(Executors.defaultThreadFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultShutdownCallbackRegistry(ThreadFactory threadFactory) {
/*  65 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  73 */     if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
/*  74 */       for (Reference<Cancellable> hookRef : this.hooks) {
/*  75 */         Cancellable hook = hookRef.get();
/*  76 */         if (hook != null) {
/*     */           try {
/*  78 */             hook.run();
/*  79 */           } catch (Throwable t1) {
/*     */             try {
/*  81 */               LOGGER.error(SHUTDOWN_HOOK_MARKER, "Caught exception executing shutdown hook {}", hook, t1);
/*  82 */             } catch (Throwable t2) {
/*  83 */               System.err.println("Caught exception " + t2.getClass() + " logging exception " + t1.getClass());
/*  84 */               t1.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*  89 */       this.state.set(LifeCycle.State.STOPPED);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class RegisteredCancellable implements Cancellable {
/*     */     private Runnable callback;
/*     */     private Collection<Reference<Cancellable>> registered;
/*     */     
/*     */     RegisteredCancellable(Runnable callback, Collection<Reference<Cancellable>> registered) {
/*  98 */       this.callback = callback;
/*  99 */       this.registered = registered;
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 104 */       this.callback = null;
/* 105 */       Collection<Reference<Cancellable>> references = this.registered;
/* 106 */       if (references != null) {
/* 107 */         this.registered = null;
/* 108 */         references.removeIf(ref -> {
/*     */               Cancellable value = ref.get();
/* 110 */               return (value == null || value == this);
/*     */             });
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 117 */       Runnable runnableHook = this.callback;
/* 118 */       if (runnableHook != null) {
/* 119 */         runnableHook.run();
/* 120 */         this.callback = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 126 */       return String.valueOf(this.callback);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Cancellable addShutdownCallback(Runnable callback) {
/* 132 */     if (isStarted()) {
/* 133 */       Cancellable receipt = new RegisteredCancellable(callback, this.hooks);
/* 134 */       this.hooks.add(new SoftReference<>(receipt));
/* 135 */       return receipt;
/*     */     } 
/* 137 */     throw new IllegalStateException("Cannot add new shutdown hook as this is not started. Current state: " + ((LifeCycle.State)this.state
/* 138 */         .get()).name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 150 */     if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
/*     */       try {
/* 152 */         addShutdownHook(this.threadFactory.newThread(this));
/* 153 */         this.state.set(LifeCycle.State.STARTED);
/* 154 */       } catch (IllegalStateException ex) {
/* 155 */         this.state.set(LifeCycle.State.STOPPED);
/* 156 */         throw ex;
/* 157 */       } catch (Exception e) {
/* 158 */         LOGGER.catching(e);
/* 159 */         this.state.set(LifeCycle.State.STOPPED);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void addShutdownHook(Thread thread) {
/* 165 */     this.shutdownHookRef = new WeakReference<>(thread);
/* 166 */     Runtime.getRuntime().addShutdownHook(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 171 */     stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 179 */     if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
/*     */       try {
/* 181 */         removeShutdownHook();
/*     */       } finally {
/* 183 */         this.state.set(LifeCycle.State.STOPPED);
/*     */       } 
/*     */     }
/* 186 */     return true;
/*     */   }
/*     */   
/*     */   private void removeShutdownHook() {
/* 190 */     Thread shutdownThread = this.shutdownHookRef.get();
/* 191 */     if (shutdownThread != null) {
/* 192 */       Runtime.getRuntime().removeShutdownHook(shutdownThread);
/* 193 */       this.shutdownHookRef.enqueue();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LifeCycle.State getState() {
/* 199 */     return this.state.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 209 */     return (this.state.get() == LifeCycle.State.STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/* 214 */     return (this.state.get() == LifeCycle.State.STOPPED);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\DefaultShutdownCallbackRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */