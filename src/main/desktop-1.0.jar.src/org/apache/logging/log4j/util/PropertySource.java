/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public interface PropertySource
/*     */ {
/*     */   int getPriority();
/*     */   
/*     */   default void forEach(BiConsumer<String, String> action) {}
/*     */   
/*     */   default CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default String getProperty(String key) {
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean containsProperty(String key) {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Comparator
/*     */     implements java.util.Comparator<PropertySource>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(PropertySource o1, PropertySource o2) {
/*  93 */       return Integer.compare(((PropertySource)Objects.<PropertySource>requireNonNull(o1)).getPriority(), ((PropertySource)Objects.<PropertySource>requireNonNull(o2)).getPriority());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Util
/*     */   {
/*     */     private static final String PREFIXES = "(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?";
/*     */ 
/*     */     
/* 104 */     private static final Pattern PROPERTY_TOKENIZER = Pattern.compile("(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?([A-Z]*[a-z0-9]+|[A-Z0-9]+)[-._/]?");
/* 105 */     private static final Map<CharSequence, List<CharSequence>> CACHE = new ConcurrentHashMap<>();
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
/*     */     public static List<CharSequence> tokenize(CharSequence value) {
/* 117 */       if (CACHE.containsKey(value)) {
/* 118 */         return CACHE.get(value);
/*     */       }
/* 120 */       List<CharSequence> tokens = new ArrayList<>();
/* 121 */       Matcher matcher = PROPERTY_TOKENIZER.matcher(value);
/* 122 */       while (matcher.find()) {
/* 123 */         tokens.add(matcher.group(1).toLowerCase());
/*     */       }
/* 125 */       CACHE.put(value, tokens);
/* 126 */       return tokens;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static CharSequence joinAsCamelCase(Iterable<? extends CharSequence> tokens) {
/* 136 */       StringBuilder sb = new StringBuilder();
/* 137 */       boolean first = true;
/* 138 */       for (CharSequence token : tokens) {
/* 139 */         if (first) {
/* 140 */           sb.append(token);
/*     */         } else {
/* 142 */           sb.append(Character.toUpperCase(token.charAt(0)));
/* 143 */           if (token.length() > 1) {
/* 144 */             sb.append(token.subSequence(1, token.length()));
/*     */           }
/*     */         } 
/* 147 */         first = false;
/*     */       } 
/* 149 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\PropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */