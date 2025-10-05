/*    */ package org.apache.logging.log4j.core.config.yaml;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonFactory;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
/*    */ import java.io.IOException;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.json.JsonConfiguration;
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
/*    */ public class YamlConfiguration
/*    */   extends JsonConfiguration
/*    */ {
/*    */   public YamlConfiguration(LoggerContext loggerContext, ConfigurationSource configSource) {
/* 33 */     super(loggerContext, configSource);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ObjectMapper getObjectMapper() {
/* 38 */     return (new ObjectMapper((JsonFactory)new YAMLFactory())).configure(JsonParser.Feature.ALLOW_COMMENTS, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Configuration reconfigure() {
/*    */     try {
/* 44 */       ConfigurationSource source = getConfigurationSource().resetInputStream();
/* 45 */       if (source == null) {
/* 46 */         return null;
/*    */       }
/* 48 */       return (Configuration)new YamlConfiguration(getLoggerContext(), source);
/* 49 */     } catch (IOException ex) {
/* 50 */       LOGGER.error("Cannot locate file {}", getConfigurationSource(), ex);
/*    */       
/* 52 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\yaml\YamlConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */