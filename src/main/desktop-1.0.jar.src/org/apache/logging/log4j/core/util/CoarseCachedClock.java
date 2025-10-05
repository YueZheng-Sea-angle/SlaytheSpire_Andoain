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
/*    */ public final class CoarseCachedClock
/*    */   implements Clock
/*    */ {
/*    */   private static volatile CoarseCachedClock instance;
/* 27 */   private static final Object INSTANCE_LOCK = new Object();
/*    */   
/* 29 */   private volatile long millis = System.currentTimeMillis();
/*    */   
/* 31 */   private final Thread updater = new Log4jThread("CoarseCachedClock Updater Thread")
/*    */     {
/*    */       public void run() {
/*    */         while (true) {
/* 35 */           CoarseCachedClock.this.millis = System.currentTimeMillis();
/*    */ 
/*    */           
/* 38 */           LockSupport.parkNanos(1000000L);
/*    */         } 
/*    */       }
/*    */     };
/*    */   
/*    */   private CoarseCachedClock() {
/* 44 */     this.updater.setDaemon(true);
/* 45 */     this.updater.start();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CoarseCachedClock instance() {
/* 55 */     CoarseCachedClock result = instance;
/* 56 */     if (result == null) {
/* 57 */       synchronized (INSTANCE_LOCK) {
/* 58 */         result = instance;
/* 59 */         if (result == null) {
/* 60 */           instance = result = new CoarseCachedClock();
/*    */         }
/*    */       } 
/*    */     }
/* 64 */     return result;
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
/*    */   public long currentTimeMillis() {
/* 76 */     return this.millis;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\CoarseCachedClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */