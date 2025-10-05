/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.concurrent.Delayed;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.ScheduledFuture;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ public class CronScheduledFuture<V>
/*    */   implements ScheduledFuture<V>
/*    */ {
/*    */   private volatile FutureData futureData;
/*    */   
/*    */   public CronScheduledFuture(ScheduledFuture<V> future, Date runDate) {
/* 34 */     this.futureData = new FutureData(future, runDate);
/*    */   }
/*    */   
/*    */   public Date getFireTime() {
/* 38 */     return this.futureData.runDate;
/*    */   }
/*    */   
/*    */   void reset(ScheduledFuture<?> future, Date runDate) {
/* 42 */     this.futureData = new FutureData(future, runDate);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getDelay(TimeUnit unit) {
/* 47 */     return this.futureData.scheduledFuture.getDelay(unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Delayed delayed) {
/* 52 */     return this.futureData.scheduledFuture.compareTo(delayed);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 57 */     return this.futureData.scheduledFuture.cancel(mayInterruptIfRunning);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 62 */     return this.futureData.scheduledFuture.isCancelled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 67 */     return this.futureData.scheduledFuture.isDone();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public V get() throws InterruptedException, ExecutionException {
/* 73 */     return (V)this.futureData.scheduledFuture.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 80 */     return (V)this.futureData.scheduledFuture.get(timeout, unit);
/*    */   }
/*    */   
/*    */   private class FutureData
/*    */   {
/*    */     private final ScheduledFuture<?> scheduledFuture;
/*    */     private final Date runDate;
/*    */     
/*    */     FutureData(ScheduledFuture<?> future, Date runDate) {
/* 89 */       this.scheduledFuture = future;
/* 90 */       this.runDate = runDate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\CronScheduledFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */