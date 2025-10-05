/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ public final class Assert
/*     */ {
/*     */   public static boolean isEmpty(Object o) {
/*  44 */     if (o == null) {
/*  45 */       return true;
/*     */     }
/*  47 */     if (o instanceof CharSequence) {
/*  48 */       return (((CharSequence)o).length() == 0);
/*     */     }
/*  50 */     if (o.getClass().isArray()) {
/*  51 */       return (((Object[])o).length == 0);
/*     */     }
/*  53 */     if (o instanceof Collection) {
/*  54 */       return ((Collection)o).isEmpty();
/*     */     }
/*  56 */     if (o instanceof Map) {
/*  57 */       return ((Map)o).isEmpty();
/*     */     }
/*  59 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNonEmpty(Object o) {
/*  70 */     return !isEmpty(o);
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
/*     */   public static <T> T requireNonEmpty(T value) {
/*  82 */     return requireNonEmpty(value, "");
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
/*     */   public static <T> T requireNonEmpty(T value, String message) {
/*  95 */     if (isEmpty(value)) {
/*  96 */       throw new IllegalArgumentException(message);
/*     */     }
/*  98 */     return value;
/*     */   }
/*     */   
/*     */   public static int valueIsAtLeast(int value, int minValue) {
/* 102 */     if (value < minValue) {
/* 103 */       throw new IllegalArgumentException("Value should be at least " + minValue + " but was " + value);
/*     */     }
/* 105 */     return value;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Assert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */