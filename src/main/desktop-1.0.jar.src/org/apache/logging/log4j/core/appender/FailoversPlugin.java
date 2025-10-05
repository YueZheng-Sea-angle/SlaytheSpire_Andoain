/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.AppenderRef;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "failovers", category = "Core")
/*    */ public final class FailoversPlugin
/*    */ {
/* 33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*    */   @PluginFactory
/*    */   public static String[] createFailovers(@PluginElement("AppenderRef") AppenderRef... refs) {
/* 49 */     if (refs == null) {
/* 50 */       LOGGER.error("failovers must contain an appender reference");
/* 51 */       return null;
/*    */     } 
/* 53 */     String[] arr = new String[refs.length];
/* 54 */     for (int i = 0; i < refs.length; i++) {
/* 55 */       arr[i] = refs[i].getRef();
/*    */     }
/* 57 */     return arr;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\FailoversPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */