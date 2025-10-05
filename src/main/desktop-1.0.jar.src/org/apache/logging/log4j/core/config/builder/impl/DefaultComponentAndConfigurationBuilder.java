/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
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
/*    */ class DefaultComponentAndConfigurationBuilder<T extends ComponentBuilder<T>>
/*    */   extends DefaultComponentBuilder<T, DefaultConfigurationBuilder<? extends Configuration>>
/*    */ {
/*    */   DefaultComponentAndConfigurationBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String type, String value) {
/* 34 */     super(builder, name, type, value);
/*    */   }
/*    */ 
/*    */   
/*    */   DefaultComponentAndConfigurationBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String type) {
/* 39 */     super(builder, name, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public DefaultComponentAndConfigurationBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String type) {
/* 44 */     super(builder, type);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultComponentAndConfigurationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */