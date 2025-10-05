/*     */ package org.apache.commons.net.util;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TrustManagerUtils
/*     */ {
/*  35 */   private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[0];
/*     */   
/*     */   private static class TrustManager
/*     */     implements X509TrustManager {
/*     */     private final boolean checkServerValidity;
/*     */     
/*     */     TrustManager(boolean checkServerValidity) {
/*  42 */       this.checkServerValidity = checkServerValidity;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] certificates, String authType) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
/*  58 */       if (this.checkServerValidity) {
/*  59 */         for (X509Certificate certificate : certificates)
/*     */         {
/*  61 */           certificate.checkValidity();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/*  72 */       return TrustManagerUtils.EMPTY_X509CERTIFICATE_ARRAY;
/*     */     }
/*     */   }
/*     */   
/*  76 */   private static final X509TrustManager ACCEPT_ALL = new TrustManager(false);
/*     */   
/*  78 */   private static final X509TrustManager CHECK_SERVER_VALIDITY = new TrustManager(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509TrustManager getAcceptAllTrustManager() {
/*  86 */     return ACCEPT_ALL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509TrustManager getValidateServerCertificateTrustManager() {
/*  96 */     return CHECK_SERVER_VALIDITY;
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
/*     */   public static X509TrustManager getDefaultTrustManager(KeyStore keyStore) throws GeneralSecurityException {
/* 111 */     String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
/* 112 */     TrustManagerFactory instance = TrustManagerFactory.getInstance(defaultAlgorithm);
/* 113 */     instance.init(keyStore);
/* 114 */     return (X509TrustManager)instance.getTrustManagers()[0];
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\ne\\util\TrustManagerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */