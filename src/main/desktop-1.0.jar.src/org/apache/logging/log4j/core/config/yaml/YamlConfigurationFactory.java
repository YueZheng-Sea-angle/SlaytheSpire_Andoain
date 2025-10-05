/*    */ package org.apache.logging.log4j.core.config.yaml;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.util.Loader;
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
/*    */ @Plugin(name = "YamlConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(7)
/*    */ public class YamlConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 34 */   private static final String[] SUFFIXES = new String[] { ".yml", ".yaml" };
/*    */   
/* 36 */   private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser", "com.fasterxml.jackson.dataformat.yaml.YAMLFactory" };
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean isActive;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public YamlConfigurationFactory() {
/* 46 */     for (String dependency : dependencies) {
/* 47 */       if (!Loader.isClassAvailable(dependency)) {
/* 48 */         LOGGER.debug("Missing dependencies for Yaml support, ConfigurationFactory {} is inactive", getClass().getName());
/* 49 */         this.isActive = false;
/*    */         return;
/*    */       } 
/*    */     } 
/* 53 */     this.isActive = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isActive() {
/* 58 */     return this.isActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 63 */     if (!this.isActive) {
/* 64 */       return null;
/*    */     }
/* 66 */     return (Configuration)new YamlConfiguration(loggerContext, source);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 71 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\yaml\YamlConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */