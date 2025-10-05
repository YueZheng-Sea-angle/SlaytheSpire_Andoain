/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringEncoder
/*     */ {
/*     */   public static byte[] toBytes(String str, Charset charset) {
/*  39 */     if (str != null) {
/*  40 */       return str.getBytes((charset != null) ? charset : Charset.defaultCharset());
/*     */     }
/*  42 */     return null;
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
/*     */   @Deprecated
/*     */   public static byte[] encodeSingleByteChars(CharSequence s) {
/*  56 */     int length = s.length();
/*  57 */     byte[] result = new byte[length];
/*  58 */     encodeString(s, 0, length, result);
/*  59 */     return result;
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
/*     */   @Deprecated
/*     */   public static int encodeIsoChars(CharSequence charArray, int charIndex, byte[] byteArray, int byteIndex, int length) {
/*  73 */     int i = 0;
/*  74 */     for (; i < length; i++) {
/*  75 */       char c = charArray.charAt(charIndex++);
/*  76 */       if (c > 'ÿ') {
/*     */         break;
/*     */       }
/*  79 */       byteArray[byteIndex++] = (byte)c;
/*     */     } 
/*  81 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int encodeString(CharSequence charArray, int charOffset, int charLength, byte[] byteArray) {
/*  92 */     int byteOffset = 0;
/*  93 */     int length = Math.min(charLength, byteArray.length);
/*  94 */     int charDoneIndex = charOffset + length;
/*  95 */     while (charOffset < charDoneIndex) {
/*  96 */       int done = encodeIsoChars(charArray, charOffset, byteArray, byteOffset, length);
/*  97 */       charOffset += done;
/*  98 */       byteOffset += done;
/*  99 */       if (done != length) {
/* 100 */         char c = charArray.charAt(charOffset++);
/* 101 */         if (Character.isHighSurrogate(c) && charOffset < charDoneIndex && 
/* 102 */           Character.isLowSurrogate(charArray.charAt(charOffset))) {
/* 103 */           if (charLength > byteArray.length) {
/* 104 */             charDoneIndex++;
/* 105 */             charLength--;
/*     */           } 
/* 107 */           charOffset++;
/*     */         } 
/* 109 */         byteArray[byteOffset++] = 63;
/* 110 */         length = Math.min(charDoneIndex - charOffset, byteArray.length - byteOffset);
/*     */       } 
/*     */     } 
/* 113 */     return byteOffset;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\StringEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */