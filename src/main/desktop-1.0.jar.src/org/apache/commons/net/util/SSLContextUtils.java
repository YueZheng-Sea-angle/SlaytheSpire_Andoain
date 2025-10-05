/*    */ package org.apache.commons.net.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.security.GeneralSecurityException;
/*    */ import javax.net.ssl.KeyManager;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.TrustManager;
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
/*    */ public class SSLContextUtils
/*    */ {
/*    */   public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager) throws IOException {
/* 47 */     (new KeyManager[1])[0] = keyManager; (new TrustManager[1])[0] = trustManager; return createSSLContext(protocol, (keyManager == null) ? null : new KeyManager[1], (trustManager == null) ? null : new TrustManager[1]);
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
/*    */   public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) throws IOException {
/*    */     SSLContext ctx;
/*    */     try {
/* 64 */       ctx = SSLContext.getInstance(protocol);
/* 65 */       ctx.init(keyManagers, trustManagers, null);
/* 66 */     } catch (GeneralSecurityException e) {
/* 67 */       IOException ioe = new IOException("Could not initialize SSL context");
/* 68 */       ioe.initCause(e);
/* 69 */       throw ioe;
/*    */     } 
/* 71 */     return ctx;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\ne\\util\SSLContextUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */