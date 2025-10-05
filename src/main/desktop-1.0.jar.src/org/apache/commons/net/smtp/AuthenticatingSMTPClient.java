/*     */ package org.apache.commons.net.smtp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuthenticatingSMTPClient
/*     */   extends SMTPSClient
/*     */ {
/*     */   public AuthenticatingSMTPClient() {}
/*     */   
/*     */   public AuthenticatingSMTPClient(String protocol) {
/*  54 */     super(protocol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingSMTPClient(String proto, boolean implicit) {
/*  65 */     super(proto, implicit);
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
/*     */   public AuthenticatingSMTPClient(String proto, boolean implicit, String encoding) {
/*  77 */     super(proto, implicit, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingSMTPClient(boolean implicit, SSLContext ctx) {
/*  88 */     super(implicit, ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthenticatingSMTPClient(String protocol, String encoding) {
/*  98 */     super(protocol, false, encoding);
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
/*     */   public int ehlo(String hostname) throws IOException {
/* 117 */     return sendCommand(15, hostname);
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
/*     */   public boolean elogin(String hostname) throws IOException {
/* 137 */     return SMTPReply.isPositiveCompletion(ehlo(hostname));
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
/*     */   public boolean elogin() throws IOException {
/* 160 */     InetAddress host = getLocalAddress();
/* 161 */     String name = host.getHostName();
/*     */     
/* 163 */     if (name == null) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     return SMTPReply.isPositiveCompletion(ehlo(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getEnhancedReplyCode() {
/* 177 */     String reply = getReplyString().substring(4);
/* 178 */     String[] parts = reply.substring(0, reply.indexOf(' ')).split("\\.");
/* 179 */     int[] res = new int[parts.length];
/* 180 */     for (int i = 0; i < parts.length; i++)
/*     */     {
/* 182 */       res[i] = Integer.parseInt(parts[i]);
/*     */     }
/* 184 */     return res;
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
/*     */   public boolean auth(AUTH_METHOD method, String username, String password) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
/* 218 */     if (!SMTPReply.isPositiveIntermediate(sendCommand(14, AUTH_METHOD.getAuthName(method))))
/*     */     {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     if (method.equals(AUTH_METHOD.PLAIN))
/*     */     {
/*     */       
/* 226 */       return SMTPReply.isPositiveCompletion(sendCommand(Base64.encodeBase64StringUnChunked(("\000" + username + "\000" + password).getBytes(getCharset()))));
/*     */     }
/*     */ 
/*     */     
/* 230 */     if (method.equals(AUTH_METHOD.CRAM_MD5)) {
/*     */ 
/*     */       
/* 233 */       byte[] serverChallenge = Base64.decodeBase64(getReplyString().substring(4).trim());
/*     */       
/* 235 */       Mac hmac_md5 = Mac.getInstance("HmacMD5");
/* 236 */       hmac_md5.init(new SecretKeySpec(password.getBytes(getCharset()), "HmacMD5"));
/*     */       
/* 238 */       byte[] hmacResult = _convertToHexString(hmac_md5.doFinal(serverChallenge)).getBytes(getCharset());
/*     */       
/* 240 */       byte[] usernameBytes = username.getBytes(getCharset());
/* 241 */       byte[] toEncode = new byte[usernameBytes.length + 1 + hmacResult.length];
/* 242 */       System.arraycopy(usernameBytes, 0, toEncode, 0, usernameBytes.length);
/* 243 */       toEncode[usernameBytes.length] = 32;
/* 244 */       System.arraycopy(hmacResult, 0, toEncode, usernameBytes.length + 1, hmacResult.length);
/*     */       
/* 246 */       return SMTPReply.isPositiveCompletion(sendCommand(Base64.encodeBase64StringUnChunked(toEncode)));
/*     */     } 
/*     */     
/* 249 */     if (method.equals(AUTH_METHOD.LOGIN)) {
/*     */ 
/*     */ 
/*     */       
/* 253 */       if (!SMTPReply.isPositiveIntermediate(sendCommand(Base64.encodeBase64StringUnChunked(username.getBytes(getCharset())))))
/*     */       {
/* 255 */         return false;
/*     */       }
/* 257 */       return SMTPReply.isPositiveCompletion(sendCommand(Base64.encodeBase64StringUnChunked(password.getBytes(getCharset()))));
/*     */     } 
/*     */     
/* 260 */     if (method.equals(AUTH_METHOD.XOAUTH))
/*     */     {
/* 262 */       return SMTPReply.isPositiveIntermediate(sendCommand(Base64.encodeBase64StringUnChunked(username.getBytes(getCharset()))));
/*     */     }
/*     */ 
/*     */     
/* 266 */     return false;
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
/* 279 */     StringBuilder result = new StringBuilder(a.length * 2);
/* 280 */     for (byte element : a) {
/*     */       
/* 282 */       if ((element & 0xFF) <= 15) {
/* 283 */         result.append("0");
/*     */       }
/* 285 */       result.append(Integer.toHexString(element & 0xFF));
/*     */     } 
/* 287 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum AUTH_METHOD
/*     */   {
/* 296 */     PLAIN,
/*     */     
/* 298 */     CRAM_MD5,
/*     */     
/* 300 */     LOGIN,
/*     */     
/* 302 */     XOAUTH;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String getAuthName(AUTH_METHOD method) {
/* 311 */       if (method.equals(PLAIN))
/* 312 */         return "PLAIN"; 
/* 313 */       if (method.equals(CRAM_MD5))
/* 314 */         return "CRAM-MD5"; 
/* 315 */       if (method.equals(LOGIN))
/* 316 */         return "LOGIN"; 
/* 317 */       if (method.equals(XOAUTH)) {
/* 318 */         return "XOAUTH";
/*     */       }
/* 320 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\smtp\AuthenticatingSMTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */