/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "loggers", category = "Core")
/*    */ public final class LoggersPlugin
/*    */ {
/*    */   @PluginFactory
/*    */   public static Loggers createLoggers(@PluginElement("Loggers") LoggerConfig[] loggers) {
/* 42 */     ConcurrentMap<String, LoggerConfig> loggerMap = new ConcurrentHashMap<>();
/* 43 */     LoggerConfig root = null;
/*    */     
/* 45 */     for (LoggerConfig logger : loggers) {
/* 46 */       if (logger != null) {
/* 47 */         if (logger.getName().isEmpty()) {
/* 48 */           if (root != null) {
/* 49 */             throw new IllegalStateException("Configuration has multiple root loggers. There can be only one.");
/*    */           }
/* 51 */           root = logger;
/*    */         } 
/* 53 */         loggerMap.put(logger.getName(), logger);
/*    */       } 
/*    */     } 
/*    */     
/* 57 */     return new Loggers(loggerMap, root);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\LoggersPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */