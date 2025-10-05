/*    */ package org.apache.logging.log4j.core.net;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Rfc1349TrafficClass
/*    */ {
/* 33 */   IPTOS_NORMAL(0),
/* 34 */   IPTOS_LOWCOST(2),
/* 35 */   IPTOS_LOWDELAY(16),
/* 36 */   IPTOS_RELIABILITY(4),
/* 37 */   IPTOS_THROUGHPUT(8);
/*    */   
/*    */   private final int trafficClass;
/*    */ 
/*    */   
/*    */   Rfc1349TrafficClass(int trafficClass) {
/* 43 */     this.trafficClass = trafficClass;
/*    */   }
/*    */   
/*    */   public int value() {
/* 47 */     return this.trafficClass;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\Rfc1349TrafficClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */