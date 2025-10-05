/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
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
/*    */ class DefaultScriptComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<ScriptComponentBuilder>
/*    */   implements ScriptComponentBuilder
/*    */ {
/*    */   public DefaultScriptComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String language, String text) {
/* 30 */     super(builder, name, "Script");
/* 31 */     if (language != null) {
/* 32 */       addAttribute("language", language);
/*    */     }
/* 34 */     if (text != null)
/* 35 */       addAttribute("text", text); 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultScriptComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */