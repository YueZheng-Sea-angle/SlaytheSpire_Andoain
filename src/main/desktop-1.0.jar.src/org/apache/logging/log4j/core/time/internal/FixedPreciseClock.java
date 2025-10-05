/*    */ package org.apache.logging.log4j.core.time.internal;
/*    */ 
/*    */ import org.apache.logging.log4j.core.time.MutableInstant;
/*    */ import org.apache.logging.log4j.core.time.PreciseClock;
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
/*    */ public class FixedPreciseClock
/*    */   implements PreciseClock
/*    */ {
/*    */   private final long currentTimeMillis;
/*    */   private final int nanosOfMillisecond;
/*    */   
/*    */   public FixedPreciseClock() {
/* 34 */     this(0L);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedPreciseClock(long currentTimeMillis) {
/* 42 */     this(currentTimeMillis, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedPreciseClock(long currentTimeMillis, int nanosOfMillisecond) {
/* 52 */     this.currentTimeMillis = currentTimeMillis;
/* 53 */     this.nanosOfMillisecond = nanosOfMillisecond;
/*    */   }
/*    */ 
/*    */   
/*    */   public void init(MutableInstant instant) {
/* 58 */     instant.initFromEpochMilli(this.currentTimeMillis, this.nanosOfMillisecond);
/*    */   }
/*    */ 
/*    */   
/*    */   public long currentTimeMillis() {
/* 63 */     return this.currentTimeMillis;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\time\internal\FixedPreciseClock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */