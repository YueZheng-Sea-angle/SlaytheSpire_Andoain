/*    */ package org.apache.logging.log4j.core.config.json;
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
/*    */ @Plugin(name = "JsonConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(6)
/*    */ public class JsonConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 34 */   private static final String[] SUFFIXES = new String[] { ".json", ".jsn" };
/*    */   
/* 36 */   private static final String[] dependencies = new String[] { "com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser" };
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean isActive;
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonConfigurationFactory() {
/* 45 */     for (String dependency : dependencies) {
/* 46 */       if (!Loader.isClassAvailable(dependency)) {
/* 47 */         LOGGER.debug("Missing dependencies for Json support, ConfigurationFactory {} is inactive", getClass().getName());
/* 48 */         this.isActive = false;
/*    */         return;
/*    */       } 
/*    */     } 
/* 52 */     this.isActive = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isActive() {
/* 57 */     return this.isActive;
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 62 */     if (!this.isActive) {
/* 63 */       return null;
/*    */     }
/* 65 */     return (Configuration)new JsonConfiguration(loggerContext, source);
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 70 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\json\JsonConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */