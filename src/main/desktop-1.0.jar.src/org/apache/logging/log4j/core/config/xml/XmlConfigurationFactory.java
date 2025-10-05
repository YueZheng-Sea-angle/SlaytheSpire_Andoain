/*    */ package org.apache.logging.log4j.core.config.xml;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.Order;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "XmlConfigurationFactory", category = "ConfigurationFactory")
/*    */ @Order(5)
/*    */ public class XmlConfigurationFactory
/*    */   extends ConfigurationFactory
/*    */ {
/* 36 */   public static final String[] SUFFIXES = new String[] { ".xml", "*" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 46 */     return (Configuration)new XmlConfiguration(loggerContext, source);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getSupportedTypes() {
/* 55 */     return SUFFIXES;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\xml\XmlConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */