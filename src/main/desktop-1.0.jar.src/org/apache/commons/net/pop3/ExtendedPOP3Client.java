/*     */ package org.apache.commons.net.pop3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.apache.commons.net.util.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedPOP3Client
/*     */   extends POP3SClient
/*     */ {
/*     */   public boolean auth(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
/*     */     byte[] serverChallenge;
/*     */     Mac hmac_md5;
/*     */     byte[] hmacResult;
/*     */     byte[] usernameBytes;
/*     */     byte[] toEncode;
/*  70 */     if (sendCommand(13, method.getAuthName()) != 2)
/*     */     {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     switch (method) {
/*     */       
/*     */       case PLAIN:
/*  78 */         return (sendCommand(new String(Base64.encodeBase64(("\000" + username + "\000" + password).getBytes(getCharset())), getCharset())) == 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case CRAM_MD5:
/*  85 */         serverChallenge = Base64.decodeBase64(getReplyString().substring(2).trim());
/*     */         
/*  87 */         hmac_md5 = Mac.getInstance("HmacMD5");
/*  88 */         hmac_md5.init(new SecretKeySpec(password.getBytes(getCharset()), "HmacMD5"));
/*     */         
/*  90 */         hmacResult = _convertToHexString(hmac_md5.doFinal(serverChallenge)).getBytes(getCharset());
/*     */         
/*  92 */         usernameBytes = username.getBytes(getCharset());
/*  93 */         toEncode = new byte[usernameBytes.length + 1 + hmacResult.length];
/*  94 */         System.arraycopy(usernameBytes, 0, toEncode, 0, usernameBytes.length);
/*  95 */         toEncode[usernameBytes.length] = 32;
/*  96 */         System.arraycopy(hmacResult, 0, toEncode, usernameBytes.length + 1, hmacResult.length);
/*     */         
/*  98 */         return (sendCommand(Base64.encodeBase64StringUnChunked(toEncode)) == 0);
/*     */     } 
/* 100 */     return false;
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
/*     */   private String _convertToHexString(byte[] a) {
/* 113 */     StringBuilder result = new StringBuilder(a.length * 2);
/* 114 */     for (byte element : a) {
/*     */       
/* 116 */       if ((element & 0xFF) <= 15) {
/* 117 */         result.append("0");
/*     */       }
/* 119 */       result.append(Integer.toHexString(element & 0xFF));
/*     */     } 
/* 121 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum AUTH_METHOD
/*     */   {
/* 130 */     PLAIN("PLAIN"),
/*     */ 
/*     */     
/* 133 */     CRAM_MD5("CRAM-MD5");
/*     */     
/*     */     private final String methodName;
/*     */     
/*     */     AUTH_METHOD(String methodName) {
/* 138 */       this.methodName = methodName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getAuthName() {
/* 146 */       return this.methodName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\pop3\ExtendedPOP3Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */