/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
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
/*    */ class DefaultRootLoggerComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<RootLoggerComponentBuilder>
/*    */   implements RootLoggerComponentBuilder
/*    */ {
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level) {
/* 37 */     super(builder, "", "Root");
/* 38 */     if (level != null) {
/* 39 */       addAttribute("level", level);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, boolean includeLocation) {
/* 51 */     super(builder, "", "Root");
/* 52 */     if (level != null) {
/* 53 */       addAttribute("level", level);
/*    */     }
/* 55 */     addAttribute("includeLocation", includeLocation);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type) {
/* 66 */     super(builder, "", type);
/* 67 */     if (level != null) {
/* 68 */       addAttribute("level", level);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultRootLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String level, String type, boolean includeLocation) {
/* 81 */     super(builder, "", type);
/* 82 */     if (level != null) {
/* 83 */       addAttribute("level", level);
/*    */     }
/* 85 */     addAttribute("includeLocation", includeLocation);
/*    */   }
/*    */ 
/*    */   
/*    */   public RootLoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
/* 90 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RootLoggerComponentBuilder add(FilterComponentBuilder builder) {
/* 96 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultRootLoggerComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */