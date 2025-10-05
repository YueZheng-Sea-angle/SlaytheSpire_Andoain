/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
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
/*     */ public class ArrayUtils
/*     */ {
/*     */   public static boolean isEmpty(byte[] array) {
/*  35 */     return (getLength(array) == 0);
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
/*     */   public static int getLength(Object array) {
/*  59 */     if (array == null) {
/*  60 */       return 0;
/*     */     }
/*  62 */     return Array.getLength(array);
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
/*     */   private static Object remove(Object array, int index) {
/*  87 */     int length = getLength(array);
/*  88 */     if (index < 0 || index >= length) {
/*  89 */       throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
/*     */     }
/*     */     
/*  92 */     Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
/*  93 */     System.arraycopy(array, 0, result, 0, index);
/*  94 */     if (index < length - 1) {
/*  95 */       System.arraycopy(array, index + 1, result, index, length - index - 1);
/*     */     }
/*     */     
/*  98 */     return result;
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
/*     */   public static <T> T[] remove(T[] array, int index) {
/* 132 */     return (T[])remove(array, index);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ArrayUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */