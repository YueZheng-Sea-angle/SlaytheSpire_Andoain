/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.util.Strings;
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
/*    */ public final class NameUtil
/*    */ {
/*    */   public static String getSubName(String name) {
/* 34 */     if (Strings.isEmpty(name)) {
/* 35 */       return null;
/*    */     }
/* 37 */     int i = name.lastIndexOf('.');
/* 38 */     return (i > 0) ? name.substring(0, i) : "";
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
/*    */ 
/*    */ 
/*    */   
/*    */   public static String md5(String input) {
/* 54 */     Objects.requireNonNull(input, "input");
/*    */     try {
/* 56 */       byte[] inputBytes = input.getBytes();
/* 57 */       MessageDigest digest = MessageDigest.getInstance("MD5");
/* 58 */       byte[] bytes = digest.digest(inputBytes);
/* 59 */       StringBuilder md5 = new StringBuilder(bytes.length * 2);
/* 60 */       for (byte b : bytes) {
/* 61 */         String hex = Integer.toHexString(0xFF & b);
/* 62 */         if (hex.length() == 1) {
/* 63 */           md5.append('0');
/*    */         }
/* 65 */         md5.append(hex);
/*    */       } 
/* 67 */       return md5.toString();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/* 73 */     catch (NoSuchAlgorithmException error) {
/* 74 */       throw new RuntimeException(error);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\NameUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */