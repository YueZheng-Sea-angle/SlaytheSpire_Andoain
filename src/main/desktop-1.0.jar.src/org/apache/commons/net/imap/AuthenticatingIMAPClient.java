/*     */ package org.apache.commons.net.imap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import javax.net.ssl.SSLContext;
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
/*     */ public class AuthenticatingIMAPClient
/*     */   extends IMAPSClient
/*     */ {
/*     */   public AuthenticatingIMAPClient() {
/*  42 */     this("TLS", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingIMAPClient(boolean implicit) {
/*  51 */     this("TLS", implicit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingIMAPClient(String proto) {
/*  60 */     this(proto, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingIMAPClient(String proto, boolean implicit) {
/*  70 */     this(proto, implicit, (SSLContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingIMAPClient(String proto, boolean implicit, SSLContext ctx) {
/*  81 */     super(proto, implicit, ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingIMAPClient(boolean implicit, SSLContext ctx) {
/*  91 */     this("TLS", implicit, ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingIMAPClient(SSLContext context) {
/* 100 */     this(false, context);
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
/*     */   public boolean authenticate(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
/* 125 */     return auth(method, username, password);
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
/*     */   public boolean auth(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
/*     */     int i;
/*     */     byte[] serverChallenge;
/*     */     int result;
/*     */     Mac hmac_md5;
/*     */     byte[] hmacResult;
/*     */     byte[] usernameBytes;
/*     */     byte[] toEncode;
/*     */     int j;
/* 150 */     if (!IMAPReply.isContinuation(sendCommand(IMAPCommand.AUTHENTICATE, method.getAuthName())))
/*     */     {
/* 152 */       return false;
/*     */     }
/*     */     
/* 155 */     switch (method) {
/*     */ 
/*     */       
/*     */       case PLAIN:
/* 159 */         i = sendData(Base64.encodeBase64StringUnChunked(("\000" + username + "\000" + password).getBytes(getCharset())));
/*     */ 
/*     */         
/* 162 */         if (i == 0)
/*     */         {
/* 164 */           setState(IMAP.IMAPState.AUTH_STATE);
/*     */         }
/* 166 */         return (i == 0);
/*     */ 
/*     */ 
/*     */       
/*     */       case CRAM_MD5:
/* 171 */         serverChallenge = Base64.decodeBase64(getReplyString().substring(2).trim());
/*     */         
/* 173 */         hmac_md5 = Mac.getInstance("HmacMD5");
/* 174 */         hmac_md5.init(new SecretKeySpec(password.getBytes(getCharset()), "HmacMD5"));
/*     */         
/* 176 */         hmacResult = _convertToHexString(hmac_md5.doFinal(serverChallenge)).getBytes(getCharset());
/*     */         
/* 178 */         usernameBytes = username.getBytes(getCharset());
/* 179 */         toEncode = new byte[usernameBytes.length + 1 + hmacResult.length];
/* 180 */         System.arraycopy(usernameBytes, 0, toEncode, 0, usernameBytes.length);
/* 181 */         toEncode[usernameBytes.length] = 32;
/* 182 */         System.arraycopy(hmacResult, 0, toEncode, usernameBytes.length + 1, hmacResult.length);
/*     */         
/* 184 */         j = sendData(Base64.encodeBase64StringUnChunked(toEncode));
/* 185 */         if (j == 0)
/*     */         {
/* 187 */           setState(IMAP.IMAPState.AUTH_STATE);
/*     */         }
/* 189 */         return (j == 0);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case LOGIN:
/* 195 */         if (sendData(Base64.encodeBase64StringUnChunked(username.getBytes(getCharset()))) != 3)
/*     */         {
/* 197 */           return false;
/*     */         }
/* 199 */         result = sendData(Base64.encodeBase64StringUnChunked(password.getBytes(getCharset())));
/* 200 */         if (result == 0)
/*     */         {
/* 202 */           setState(IMAP.IMAPState.AUTH_STATE);
/*     */         }
/* 204 */         return (result == 0);
/*     */ 
/*     */       
/*     */       case XOAUTH:
/* 208 */         result = sendData(username);
/* 209 */         if (result == 0)
/*     */         {
/* 211 */           setState(IMAP.IMAPState.AUTH_STATE);
/*     */         }
/* 213 */         return (result == 0);
/*     */     } 
/*     */     
/* 216 */     return false;
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
/*     */   private String _convertToHexString(byte[] a) {
/* 228 */     StringBuilder result = new StringBuilder(a.length * 2);
/* 229 */     for (byte element : a) {
/*     */       
/* 231 */       if ((element & 0xFF) <= 15) {
/* 232 */         result.append("0");
/*     */       }
/* 234 */       result.append(Integer.toHexString(element & 0xFF));
/*     */     } 
/* 236 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum AUTH_METHOD
/*     */   {
/* 245 */     PLAIN("PLAIN"),
/*     */     
/* 247 */     CRAM_MD5("CRAM-MD5"),
/*     */     
/* 249 */     LOGIN("LOGIN"),
/*     */     
/* 251 */     XOAUTH("XOAUTH");
/*     */     
/*     */     private final String authName;
/*     */     
/*     */     AUTH_METHOD(String name) {
/* 256 */       this.authName = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getAuthName() {
/* 264 */       return this.authName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\imap\AuthenticatingIMAPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */