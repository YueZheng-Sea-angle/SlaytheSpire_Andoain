/*     */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public abstract class AbstractPluginVisitor<A extends Annotation>
/*     */   implements PluginVisitor<A>
/*     */ {
/*  40 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Class<A> clazz;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected A annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] aliases;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> conversionType;
/*     */ 
/*     */ 
/*     */   
/*     */   protected StrSubstitutor substitutor;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Member member;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractPluginVisitor(Class<A> clazz) {
/*  73 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setAnnotation(Annotation anAnnotation) {
/*  79 */     Annotation a = Objects.<Annotation>requireNonNull(anAnnotation, "No annotation was provided");
/*  80 */     if (this.clazz.isInstance(a)) {
/*  81 */       this.annotation = (A)a;
/*     */     }
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setAliases(String... someAliases) {
/*  88 */     this.aliases = someAliases;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setConversionType(Class<?> aConversionType) {
/*  94 */     this.conversionType = Objects.<Class<?>>requireNonNull(aConversionType, "No conversion type class was provided");
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setStrSubstitutor(StrSubstitutor aSubstitutor) {
/* 100 */     this.substitutor = Objects.<StrSubstitutor>requireNonNull(aSubstitutor, "No StrSubstitutor was provided");
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginVisitor<A> setMember(Member aMember) {
/* 106 */     this.member = aMember;
/* 107 */     return this;
/*     */   }
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
/*     */   protected static String removeAttributeValue(Map<String, String> attributes, String name, String... aliases) {
/* 121 */     for (Map.Entry<String, String> entry : attributes.entrySet()) {
/* 122 */       String key = entry.getKey();
/* 123 */       String value = entry.getValue();
/* 124 */       if (key.equalsIgnoreCase(name)) {
/* 125 */         attributes.remove(key);
/* 126 */         return value;
/*     */       } 
/* 128 */       if (aliases != null) {
/* 129 */         for (String alias : aliases) {
/* 130 */           if (key.equalsIgnoreCase(alias)) {
/* 131 */             attributes.remove(key);
/* 132 */             return value;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object convert(String value, Object defaultValue) {
/* 148 */     if (defaultValue instanceof String) {
/* 149 */       return TypeConverters.convert(value, this.conversionType, Strings.trimToNull((String)defaultValue));
/*     */     }
/* 151 */     return TypeConverters.convert(value, this.conversionType, defaultValue);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\visitors\AbstractPluginVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */