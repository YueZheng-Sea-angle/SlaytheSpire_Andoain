/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
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
/*    */ public class NullConfiguration
/*    */   extends AbstractConfiguration
/*    */ {
/*    */   public static final String NULL_NAME = "Null";
/*    */   
/*    */   public NullConfiguration() {
/* 32 */     super(null, ConfigurationSource.NULL_SOURCE);
/*    */     
/* 34 */     setName("Null");
/* 35 */     LoggerConfig root = getRootLogger();
/* 36 */     root.setLevel(Level.OFF);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\NullConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */