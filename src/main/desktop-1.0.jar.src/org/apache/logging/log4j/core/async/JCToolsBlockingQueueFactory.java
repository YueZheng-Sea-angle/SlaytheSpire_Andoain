/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.jctools.queues.MpscArrayQueue;
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
/*     */ @Plugin(name = "JCToolsBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
/*     */ public class JCToolsBlockingQueueFactory<E>
/*     */   implements BlockingQueueFactory<E>
/*     */ {
/*     */   private final WaitStrategy waitStrategy;
/*     */   
/*     */   private JCToolsBlockingQueueFactory(WaitStrategy waitStrategy) {
/*  41 */     this.waitStrategy = waitStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockingQueue<E> create(int capacity) {
/*  46 */     return new MpscBlockingQueue<>(capacity, this.waitStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static <E> JCToolsBlockingQueueFactory<E> createFactory(@PluginAttribute(value = "WaitStrategy", defaultString = "PARK") WaitStrategy waitStrategy) {
/*  52 */     return new JCToolsBlockingQueueFactory<>(waitStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MpscBlockingQueue<E>
/*     */     extends MpscArrayQueue<E>
/*     */     implements BlockingQueue<E>
/*     */   {
/*     */     private final JCToolsBlockingQueueFactory.WaitStrategy waitStrategy;
/*     */     
/*     */     MpscBlockingQueue(int capacity, JCToolsBlockingQueueFactory.WaitStrategy waitStrategy) {
/*  63 */       super(capacity);
/*  64 */       this.waitStrategy = waitStrategy;
/*     */     }
/*     */ 
/*     */     
/*     */     public int drainTo(Collection<? super E> c) {
/*  69 */       return drainTo(c, capacity());
/*     */     }
/*     */ 
/*     */     
/*     */     public int drainTo(Collection<? super E> c, int maxElements) {
/*  74 */       return drain(e -> c.add(e), maxElements);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/*  79 */       int idleCounter = 0;
/*  80 */       long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
/*     */       while (true) {
/*  82 */         if (offer(e))
/*  83 */           return true; 
/*  84 */         if (System.nanoTime() - timeoutNanos > 0L) {
/*  85 */           return false;
/*     */         }
/*  87 */         idleCounter = this.waitStrategy.idle(idleCounter);
/*  88 */         if (Thread.interrupted())
/*  89 */           throw new InterruptedException(); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/*  94 */       int idleCounter = 0;
/*  95 */       long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
/*     */       while (true) {
/*  97 */         E result = poll();
/*  98 */         if (result != null)
/*  99 */           return result; 
/* 100 */         if (System.nanoTime() - timeoutNanos > 0L) {
/* 101 */           return null;
/*     */         }
/* 103 */         idleCounter = this.waitStrategy.idle(idleCounter);
/* 104 */         if (Thread.interrupted())
/* 105 */           throw new InterruptedException(); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void put(E e) throws InterruptedException {
/* 110 */       int idleCounter = 0;
/*     */       while (true) {
/* 112 */         if (offer(e)) {
/*     */           return;
/*     */         }
/* 115 */         idleCounter = this.waitStrategy.idle(idleCounter);
/* 116 */         if (Thread.interrupted()) {
/* 117 */           throw new InterruptedException();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean offer(E e) {
/* 123 */       return offerIfBelowThreshold(e, capacity() - 32);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remainingCapacity() {
/* 128 */       return capacity() - size();
/*     */     }
/*     */ 
/*     */     
/*     */     public E take() throws InterruptedException {
/* 133 */       int idleCounter = 100;
/*     */       while (true) {
/* 135 */         E result = (E)relaxedPoll();
/* 136 */         if (result != null) {
/* 137 */           return result;
/*     */         }
/* 139 */         idleCounter = this.waitStrategy.idle(idleCounter);
/* 140 */         if (Thread.interrupted())
/* 141 */           throw new InterruptedException(); 
/*     */       } 
/*     */     } }
/*     */   public enum WaitStrategy { SPIN, YIELD, PARK, PROGRESSIVE;
/*     */     static {
/* 146 */       SPIN = new WaitStrategy("SPIN", 0, idleCounter -> idleCounter + 1);
/* 147 */       YIELD = new WaitStrategy("YIELD", 1, idleCounter -> {
/*     */             Thread.yield();
/*     */             return idleCounter + 1;
/*     */           });
/* 151 */       PARK = new WaitStrategy("PARK", 2, idleCounter -> {
/*     */             LockSupport.parkNanos(1L);
/*     */             return idleCounter + 1;
/*     */           });
/* 155 */       PROGRESSIVE = new WaitStrategy("PROGRESSIVE", 3, idleCounter -> {
/*     */             if (idleCounter > 200) {
/*     */               LockSupport.parkNanos(1L);
/*     */             } else if (idleCounter > 100) {
/*     */               Thread.yield();
/*     */             } 
/*     */             return idleCounter + 1;
/*     */           });
/*     */     }
/*     */     private final JCToolsBlockingQueueFactory.Idle idle;
/*     */     
/*     */     private int idle(int idleCounter) {
/* 167 */       return this.idle.idle(idleCounter);
/*     */     }
/*     */     
/*     */     WaitStrategy(JCToolsBlockingQueueFactory.Idle idle) {
/* 171 */       this.idle = idle;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static interface Idle {
/*     */     int idle(int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\JCToolsBlockingQueueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */