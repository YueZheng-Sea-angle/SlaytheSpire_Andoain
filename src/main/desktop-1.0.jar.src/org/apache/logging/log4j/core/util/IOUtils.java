/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOUtils
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*     */   public static final int EOF = -1;
/*     */   
/*     */   public static int copy(Reader input, Writer output) throws IOException {
/*  62 */     long count = copyLarge(input, output);
/*  63 */     if (count > 2147483647L) {
/*  64 */       return -1;
/*     */     }
/*  66 */     return (int)count;
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
/*     */   public static long copyLarge(Reader input, Writer output) throws IOException {
/*  85 */     return copyLarge(input, output, new char[4096]);
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
/*     */   public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
/* 104 */     long count = 0L;
/*     */     int n;
/* 106 */     while (-1 != (n = input.read(buffer))) {
/* 107 */       output.write(buffer, 0, n);
/* 108 */       count += n;
/*     */     } 
/* 110 */     return count;
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
/*     */   public static String toString(Reader input) throws IOException {
/* 125 */     StringBuilderWriter sw = new StringBuilderWriter();
/* 126 */     copy(input, sw);
/* 127 */     return sw.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\IOUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */