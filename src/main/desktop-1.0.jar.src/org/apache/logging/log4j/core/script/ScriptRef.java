/*    */ package org.apache.logging.log4j.core.script;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
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
/*    */ @Plugin(name = "ScriptRef", category = "Core", printObject = true)
/*    */ public class ScriptRef
/*    */   extends AbstractScript
/*    */ {
/*    */   private final ScriptManager scriptManager;
/*    */   
/*    */   public ScriptRef(String name, ScriptManager scriptManager) {
/* 35 */     super(name, null, null);
/* 36 */     this.scriptManager = scriptManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLanguage() {
/* 41 */     AbstractScript script = this.scriptManager.getScript(getName());
/* 42 */     return (script != null) ? script.getLanguage() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getScriptText() {
/* 48 */     AbstractScript script = this.scriptManager.getScript(getName());
/* 49 */     return (script != null) ? script.getScriptText() : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static ScriptRef createReference(@PluginAttribute("ref") String name, @PluginConfiguration Configuration configuration) {
/* 58 */     if (name == null) {
/* 59 */       LOGGER.error("No script name provided");
/* 60 */       return null;
/*    */     } 
/* 62 */     return new ScriptRef(name, configuration.getScriptManager());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return "ref=" + getName();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\script\ScriptRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */