/*    */ package org.apache.logging.log4j.core.config.builder.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*    */ import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
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
/*    */ class DefaultLoggerComponentBuilder
/*    */   extends DefaultComponentAndConfigurationBuilder<LoggerComponentBuilder>
/*    */   implements LoggerComponentBuilder
/*    */ {
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level) {
/* 38 */     super(builder, name, "Logger");
/* 39 */     if (level != null) {
/* 40 */       addAttribute("level", level);
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
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, boolean includeLocation) {
/* 53 */     super(builder, name, "Logger");
/* 54 */     if (level != null) {
/* 55 */       addAttribute("level", level);
/*    */     }
/* 57 */     addAttribute("includeLocation", includeLocation);
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
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, String type) {
/* 69 */     super(builder, name, type);
/* 70 */     if (level != null) {
/* 71 */       addAttribute("level", level);
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
/*    */   
/*    */   public DefaultLoggerComponentBuilder(DefaultConfigurationBuilder<? extends Configuration> builder, String name, String level, String type, boolean includeLocation) {
/* 85 */     super(builder, name, type);
/* 86 */     if (level != null) {
/* 87 */       addAttribute("level", level);
/*    */     }
/* 89 */     addAttribute("includeLocation", includeLocation);
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerComponentBuilder add(AppenderRefComponentBuilder builder) {
/* 94 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerComponentBuilder add(FilterComponentBuilder builder) {
/* 99 */     return addComponent((ComponentBuilder<?>)builder);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultLoggerComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */