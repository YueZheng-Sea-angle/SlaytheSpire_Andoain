/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.PropertyComponentBuilder;
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
/*    */ class DefaultPropertyComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<PropertyComponentBuilder>
/*    */   implements PropertyComponentBuilder
/*    */ {
/*    */   public DefaultPropertyComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String value) {
/* 30 */     super(builder, name, "Property", value);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultPropertyComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */