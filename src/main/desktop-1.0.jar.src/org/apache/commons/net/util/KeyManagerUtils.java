/*     */ package org.apache.commons.net.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import org.apache.commons.net.io.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class KeyManagerUtils
/*     */ {
/*  67 */   private static final String DEFAULT_STORE_TYPE = KeyStore.getDefaultType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KeyManager createClientKeyManager(KeyStore ks, String keyAlias, String keyPass) throws GeneralSecurityException {
/*  86 */     ClientKeyStore cks = new ClientKeyStore(ks, (keyAlias != null) ? keyAlias : findAlias(ks), keyPass);
/*  87 */     return new X509KeyManager(cks);
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
/*     */   public static KeyManager createClientKeyManager(String storeType, File storePath, String storePass, String keyAlias, String keyPass) throws IOException, GeneralSecurityException {
/* 107 */     KeyStore ks = loadStore(storeType, storePath, storePass);
/* 108 */     return createClientKeyManager(ks, keyAlias, keyPass);
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
/*     */   public static KeyManager createClientKeyManager(File storePath, String storePass, String keyAlias) throws IOException, GeneralSecurityException {
/* 126 */     return createClientKeyManager(DEFAULT_STORE_TYPE, storePath, storePass, keyAlias, storePass);
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
/*     */   public static KeyManager createClientKeyManager(File storePath, String storePass) throws IOException, GeneralSecurityException {
/* 144 */     return createClientKeyManager(DEFAULT_STORE_TYPE, storePath, storePass, null, storePass);
/*     */   }
/*     */ 
/*     */   
/*     */   private static KeyStore loadStore(String storeType, File storePath, String storePass) throws KeyStoreException, IOException, GeneralSecurityException {
/* 149 */     KeyStore ks = KeyStore.getInstance(storeType);
/* 150 */     FileInputStream stream = null;
/*     */     try {
/* 152 */       stream = new FileInputStream(storePath);
/* 153 */       ks.load(stream, storePass.toCharArray());
/*     */     } finally {
/* 155 */       Util.closeQuietly(stream);
/*     */     } 
/* 157 */     return ks;
/*     */   }
/*     */   
/*     */   private static String findAlias(KeyStore ks) throws KeyStoreException {
/* 161 */     Enumeration<String> e = ks.aliases();
/* 162 */     while (e.hasMoreElements()) {
/* 163 */       String entry = e.nextElement();
/* 164 */       if (ks.isKeyEntry(entry)) {
/* 165 */         return entry;
/*     */       }
/*     */     } 
/* 168 */     throw new KeyStoreException("Cannot find a private key entry");
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ClientKeyStore
/*     */   {
/*     */     private final X509Certificate[] certChain;
/*     */     private final PrivateKey key;
/*     */     private final String keyAlias;
/*     */     
/*     */     ClientKeyStore(KeyStore ks, String keyAlias, String keyPass) throws GeneralSecurityException {
/* 179 */       this.keyAlias = keyAlias;
/* 180 */       this.key = (PrivateKey)ks.getKey(this.keyAlias, keyPass.toCharArray());
/* 181 */       Certificate[] certs = ks.getCertificateChain(this.keyAlias);
/* 182 */       X509Certificate[] X509certs = new X509Certificate[certs.length];
/* 183 */       for (int i = 0; i < certs.length; i++) {
/* 184 */         X509certs[i] = (X509Certificate)certs[i];
/*     */       }
/* 186 */       this.certChain = X509certs;
/*     */     }
/*     */     
/*     */     final X509Certificate[] getCertificateChain() {
/* 190 */       return this.certChain;
/*     */     }
/*     */     
/*     */     final PrivateKey getPrivateKey() {
/* 194 */       return this.key;
/*     */     }
/*     */     
/*     */     final String getAlias() {
/* 198 */       return this.keyAlias;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class X509KeyManager
/*     */     extends X509ExtendedKeyManager {
/*     */     private final KeyManagerUtils.ClientKeyStore keyStore;
/*     */     
/*     */     X509KeyManager(KeyManagerUtils.ClientKeyStore keyStore) {
/* 207 */       this.keyStore = keyStore;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
/* 214 */       return this.keyStore.getAlias();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public X509Certificate[] getCertificateChain(String alias) {
/* 220 */       return this.keyStore.getCertificateChain();
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getClientAliases(String keyType, Principal[] issuers) {
/* 225 */       return new String[] { this.keyStore.getAlias() };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PrivateKey getPrivateKey(String alias) {
/* 231 */       return this.keyStore.getPrivateKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getServerAliases(String keyType, Principal[] issuers) {
/* 236 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
/* 241 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\ne\\util\KeyManagerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */