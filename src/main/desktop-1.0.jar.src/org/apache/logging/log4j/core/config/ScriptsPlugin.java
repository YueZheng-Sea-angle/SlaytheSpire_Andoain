/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.script.AbstractScript;
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
/*    */ @Plugin(name = "scripts", category = "Core")
/*    */ public final class ScriptsPlugin
/*    */ {
/*    */   @PluginFactory
/*    */   public static AbstractScript[] createScripts(@PluginElement("Scripts") AbstractScript[] scripts) {
/* 42 */     return scripts;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ScriptsPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */