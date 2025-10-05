/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PatternMatch", category = "Core", printObject = true)
/*     */ public final class PatternMatch
/*     */ {
/*     */   private final String key;
/*     */   private final String pattern;
/*     */   
/*     */   public PatternMatch(String key, String pattern) {
/*  46 */     this.key = key;
/*  47 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  55 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/*  63 */     return this.pattern;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  68 */     return this.key + '=' + this.pattern;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  73 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<PatternMatch>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     @PluginBuilderAttribute
/*     */     private String key;
/*     */     @PluginBuilderAttribute
/*     */     private String pattern;
/*     */     
/*     */     public Builder setKey(String key) {
/*  87 */       this.key = key;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPattern(String pattern) {
/*  92 */       this.pattern = pattern;
/*  93 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public PatternMatch build() {
/*  98 */       return new PatternMatch(this.key, this.pattern);
/*     */     }
/*     */     
/*     */     protected Object readResolve() throws ObjectStreamException {
/* 102 */       return new PatternMatch(this.key, this.pattern);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     return Objects.hash(new Object[] { this.key, this.pattern });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 113 */     if (this == obj) {
/* 114 */       return true;
/*     */     }
/* 116 */     if (obj == null) {
/* 117 */       return false;
/*     */     }
/* 119 */     if (getClass() != obj.getClass()) {
/* 120 */       return false;
/*     */     }
/* 122 */     PatternMatch other = (PatternMatch)obj;
/* 123 */     if (!Objects.equals(this.key, other.key)) {
/* 124 */       return false;
/*     */     }
/* 126 */     if (!Objects.equals(this.pattern, other.pattern)) {
/* 127 */       return false;
/*     */     }
/* 129 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\PatternMatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */