/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
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
/*     */ @Plugin(name = "KeyValuePair", category = "Core", printObject = true)
/*     */ public final class KeyValuePair
/*     */ {
/*  38 */   public static final KeyValuePair[] EMPTY_ARRAY = new KeyValuePair[0];
/*     */ 
/*     */   
/*     */   private final String key;
/*     */ 
/*     */   
/*     */   private final String value;
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyValuePair(String key, String value) {
/*  49 */     this.key = key;
/*  50 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  58 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  66 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  71 */     return this.key + '=' + this.value;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  76 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements Builder<KeyValuePair>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String key;
/*     */     @PluginBuilderAttribute
/*     */     private String value;
/*     */     
/*     */     public Builder setKey(String aKey) {
/*  88 */       this.key = aKey;
/*  89 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setValue(String aValue) {
/*  93 */       this.value = aValue;
/*  94 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public KeyValuePair build() {
/*  99 */       return new KeyValuePair(this.key, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 106 */     return Objects.hash(new Object[] { this.key, this.value });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 111 */     if (this == obj) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (obj == null) {
/* 115 */       return false;
/*     */     }
/* 117 */     if (getClass() != obj.getClass()) {
/* 118 */       return false;
/*     */     }
/* 120 */     KeyValuePair other = (KeyValuePair)obj;
/* 121 */     if (!Objects.equals(this.key, other.key)) {
/* 122 */       return false;
/*     */     }
/* 124 */     if (!Objects.equals(this.value, other.value)) {
/* 125 */       return false;
/*     */     }
/* 127 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\KeyValuePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */