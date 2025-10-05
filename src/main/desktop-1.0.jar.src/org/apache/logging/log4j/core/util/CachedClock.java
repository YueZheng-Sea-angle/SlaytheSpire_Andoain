/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.concurrent.locks.LockSupport;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CachedClock
/*    */   implements Clock
/*    */ {
/*    */   private static final int UPDATE_THRESHOLD = 1000;
/*    */   private static volatile CachedClock instance;
/* 32 */   private static final Object INSTANCE_LOCK = new Object();
/* 33 */   private volatile long millis = System.currentTimeMillis();
/* 34 */   private short count = 0;
/*    */   
/*    */   private CachedClock() {
/* 37 */     Thread updater = new Log4jThread(() -> { while (true) { long time = System.currentTimeMillis(); this.millis = time; LockSupport.parkNanos(1000000L); }  }"CachedClock Updater Thread");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     updater.setDaemon(true);
/* 47 */     updater.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public static CachedClock instance() {
/* 52 */     CachedClock result = instance;
/* 53 */     if (result == null) {
/* 54 */       synchronized (INSTANCE_LOCK) {
/* 55 */         result = instance;
/* 56 */         if (result == null) {
/* 57 */           instance = result = new CachedClock();
/*    */         }
/*    */       } 
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long currentTimeMillis() {
/* 79 */     if ((this.count = (short)(this.count + 1)) > 1000) {
/* 80 */       this.millis = System.currentTimeMillis();
/* 81 */       this.count = 0;
/*    */     } 
/* 83 */     return this.millis;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\CachedClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */