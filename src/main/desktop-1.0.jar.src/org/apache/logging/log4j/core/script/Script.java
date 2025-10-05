/*    */ package org.apache.logging.log4j.core.script;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginValue;
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
/*    */ @Plugin(name = "Script", category = "Core", printObject = true)
/*    */ public class Script
/*    */   extends AbstractScript
/*    */ {
/*    */   private static final String ATTR_LANGUAGE = "language";
/*    */   private static final String ATTR_SCRIPT_TEXT = "scriptText";
/*    */   static final String PLUGIN_NAME = "Script";
/*    */   
/*    */   public Script(String name, String language, String scriptText) {
/* 36 */     super(name, language, scriptText);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static Script createScript(@PluginAttribute("name") String name, @PluginAttribute("language") String language, @PluginValue("scriptText") String scriptText) {
/* 46 */     if (language == null) {
/* 47 */       LOGGER.error("No '{}' attribute provided for {} plugin '{}'", "language", "Script", name);
/* 48 */       language = "JavaScript";
/*    */     } 
/* 50 */     if (scriptText == null) {
/* 51 */       LOGGER.error("No '{}' attribute provided for {} plugin '{}'", "scriptText", "Script", name);
/* 52 */       return null;
/*    */     } 
/* 54 */     return new Script(name, language, scriptText);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return (getName() != null) ? getName() : super.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\script\Script.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */