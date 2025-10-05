/*    */ package org.apache.logging.log4j.core.net.ssl;
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
/*    */ public class StoreConfigurationException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public StoreConfigurationException(Exception e) {
/* 26 */     super(e);
/*    */   }
/*    */   
/*    */   public StoreConfigurationException(String message) {
/* 30 */     super(message);
/*    */   }
/*    */   
/*    */   public StoreConfigurationException(String message, Exception e) {
/* 34 */     super(message, e);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\StoreConfigurationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */