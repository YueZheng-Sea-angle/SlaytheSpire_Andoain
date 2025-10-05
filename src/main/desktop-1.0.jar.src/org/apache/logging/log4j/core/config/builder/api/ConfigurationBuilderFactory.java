/*    */ package org.apache.logging.log4j.core.config.builder.api;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
/*    */ import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
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
/*    */ public abstract class ConfigurationBuilderFactory
/*    */ {
/*    */   public static ConfigurationBuilder<BuiltConfiguration> newConfigurationBuilder() {
/* 34 */     return (ConfigurationBuilder<BuiltConfiguration>)new DefaultConfigurationBuilder();
/*    */   }
/*    */   
/*    */   public static <T extends BuiltConfiguration> ConfigurationBuilder<T> newConfigurationBuilder(Class<T> clazz) {
/* 38 */     return (ConfigurationBuilder<T>)new DefaultConfigurationBuilder(clazz);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\api\ConfigurationBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */