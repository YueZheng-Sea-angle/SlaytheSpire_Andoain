/*     */ package org.apache.logging.log4j.core.config.builder.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.builder.api.Component;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DefaultComponentBuilder<T extends ComponentBuilder<T>, CB extends ConfigurationBuilder<? extends Configuration>>
/*     */   implements ComponentBuilder<T>
/*     */ {
/*     */   private final CB builder;
/*     */   private final String type;
/*  41 */   private final Map<String, String> attributes = new LinkedHashMap<>();
/*  42 */   private final List<Component> components = new ArrayList<>();
/*     */   private final String name;
/*     */   private final String value;
/*     */   
/*     */   public DefaultComponentBuilder(CB builder, String type) {
/*  47 */     this(builder, null, type, null);
/*     */   }
/*     */   
/*     */   public DefaultComponentBuilder(CB builder, String name, String type) {
/*  51 */     this(builder, name, type, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultComponentBuilder(CB builder, String name, String type, String value) {
/*  56 */     this.type = type;
/*  57 */     this.builder = builder;
/*  58 */     this.name = name;
/*  59 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public T addAttribute(String key, boolean value) {
/*  64 */     return put(key, Boolean.toString(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public T addAttribute(String key, Enum<?> value) {
/*  69 */     return put(key, value.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public T addAttribute(String key, int value) {
/*  74 */     return put(key, Integer.toString(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T addAttribute(String key, Level level) {
/*  80 */     return put(key, level.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public T addAttribute(String key, Object value) {
/*  85 */     return put(key, value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T addAttribute(String key, String value) {
/*  91 */     return put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T addComponent(ComponentBuilder<?> builder) {
/*  97 */     this.components.add(builder.build());
/*  98 */     return (T)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component build() {
/* 103 */     Component component = new Component(this.type, this.name, this.value);
/* 104 */     component.getAttributes().putAll(this.attributes);
/* 105 */     component.getComponents().addAll(this.components);
/* 106 */     return component;
/*     */   }
/*     */ 
/*     */   
/*     */   public CB getBuilder() {
/* 111 */     return this.builder;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 116 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected T put(String key, String value) {
/* 121 */     this.attributes.put(key, value);
/* 122 */     return (T)this;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\builder\impl\DefaultComponentBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */