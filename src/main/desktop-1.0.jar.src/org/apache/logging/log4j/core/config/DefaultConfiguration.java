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
/*    */ public class DefaultConfiguration
/*    */   extends AbstractConfiguration
/*    */ {
/*    */   public static final String DEFAULT_NAME = "Default";
/*    */   public static final String DEFAULT_LEVEL = "org.apache.logging.log4j.level";
/*    */   public static final String DEFAULT_PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
/*    */   
/*    */   public DefaultConfiguration() {
/* 46 */     super(null, ConfigurationSource.NULL_SOURCE);
/* 47 */     setToDefault();
/*    */   }
/*    */   
/*    */   protected void doConfigure() {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\DefaultConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */