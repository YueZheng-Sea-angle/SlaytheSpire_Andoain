/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.CustomLevelComponentBuilder;
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
/*    */ class DefaultCustomLevelComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<CustomLevelComponentBuilder>
/*    */   implements CustomLevelComponentBuilder
/*    */ {
/*    */   public DefaultCustomLevelComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, int level) {
/* 30 */     super(builder, name, "CustomLevel");
/* 31 */     addAttribute("intLevel", level);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultCustomLevelComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */