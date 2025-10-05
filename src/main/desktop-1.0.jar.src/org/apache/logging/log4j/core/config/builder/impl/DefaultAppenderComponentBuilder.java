/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
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
/*    */ class DefaultAppenderComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<AppenderComponentBuilder>
/*    */   implements AppenderComponentBuilder
/*    */ {
/*    */   public DefaultAppenderComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String type) {
/* 34 */     super(builder, name, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public AppenderComponentBuilder add(LayoutComponentBuilder builder) {
/* 39 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public AppenderComponentBuilder add(FilterComponentBuilder builder) {
/* 44 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultAppenderComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */