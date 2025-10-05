/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
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
/*    */ class DefaultScriptFileComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<ScriptFileComponentBuilder>
/*    */   implements ScriptFileComponentBuilder
/*    */ {
/*    */   public DefaultScriptFileComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String path) {
/* 32 */     super(builder, (name != null) ? name : path, "ScriptFile");
/* 33 */     addAttribute("path", path);
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultScriptFileComponentBuilder addLanguage(String language) {
/* 38 */     addAttribute("language", language);
/* 39 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultScriptFileComponentBuilder addIsWatched(boolean isWatched) {
/* 44 */     addAttribute("isWatched", Boolean.toString(isWatched));
/* 45 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultScriptFileComponentBuilder addIsWatched(String isWatched) {
/* 50 */     addAttribute("isWatched", isWatched);
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultScriptFileComponentBuilder addCharset(String charset) {
/* 56 */     addAttribute("charset", charset);
/* 57 */     return this;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultScriptFileComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */