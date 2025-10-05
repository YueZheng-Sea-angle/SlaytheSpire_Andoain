/*    */ package org.apache.logging.log4j.core.config.properties;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.logging.log4j.core.LifeCycle;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*    */ import org.apache.logging.log4j.core.config.builder.api.Component;
/*    */ import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
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
/*    */ public class PropertiesConfiguration
/*    */   extends BuiltConfiguration
/*    */   implements Reconfigurable
/*    */ {
/*    */   public PropertiesConfiguration(LoggerContext loggerContext, ConfigurationSource source, Component root) {
/* 36 */     super(loggerContext, source, root);
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration reconfigure() {
/*    */     try {
/* 42 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 43 */       if (source == null) {
/* 44 */         return null;
/*    */       }
/* 46 */       PropertiesConfigurationFactory factory = new PropertiesConfigurationFactory();
/* 47 */       PropertiesConfiguration config = factory.getConfiguration(getLoggerContext(), source);
/* 48 */       return (config == null || config.getState() != LifeCycle.State.INITIALIZING) ? null : (Configuration)config;
/* 49 */     } catch (IOException ex) {
/* 50 */       LOGGER.error("Cannot locate file {}: {}", getConfigurationSource(), ex);
/*    */       
/* 52 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\properties\PropertiesConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */