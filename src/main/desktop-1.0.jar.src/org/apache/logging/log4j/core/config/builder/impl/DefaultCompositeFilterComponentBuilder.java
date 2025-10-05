/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.CompositeFilterComponentBuilder;
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
/*    */ class DefaultCompositeFilterComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<CompositeFilterComponentBuilder>
/*    */   implements CompositeFilterComponentBuilder
/*    */ {
/*    */   public DefaultCompositeFilterComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String onMatch, String onMismatch) {
/* 33 */     super(builder, "Filters");
/* 34 */     addAttribute("onMatch", onMatch);
/* 35 */     addAttribute("onMismatch", onMismatch);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompositeFilterComponentBuilder add(FilterComponentBuilder builder) {
/* 40 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultCompositeFilterComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */