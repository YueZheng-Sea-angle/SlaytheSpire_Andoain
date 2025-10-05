/*    */ package org.apache.logging.log4j.internal;
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
/*    */ public class LogManagerStatus
/*    */ {
/*    */   private static boolean initialized = false;
/*    */   
/*    */   public static void setInitialized(boolean managerStatus) {
/* 27 */     initialized = managerStatus;
/*    */   }
/*    */   
/*    */   public static boolean isInitialized() {
/* 31 */     return initialized;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\internal\LogManagerStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */