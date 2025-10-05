/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
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
/*    */ class DefaultAppenderRefComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<AppenderRefComponentBuilder>
/*    */   implements AppenderRefComponentBuilder
/*    */ {
/*    */   public DefaultAppenderRefComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String ref) {
/* 33 */     super(builder, "AppenderRef");
/* 34 */     addAttribute("ref", ref);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AppenderRefComponentBuilder add(FilterComponentBuilder builder) {
/* 40 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultAppenderRefComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */