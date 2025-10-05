/*    */ package org.apache.logging.log4j.core.config;
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
/*    */ public class ConfigurationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -2413951820300775294L;
/*    */   
/*    */   public ConfigurationException(String message) {
/* 33 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConfigurationException(String message, Throwable cause) {
/* 43 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConfigurationException(Throwable cause) {
/* 52 */     super(cause);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ConfigurationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */