/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbstractKeyStoreConfiguration
/*     */   extends StoreConfiguration<KeyStore>
/*     */ {
/*     */   private final KeyStore keyStore;
/*     */   private final String keyStoreType;
/*     */   
/*     */   public AbstractKeyStoreConfiguration(String location, PasswordProvider passwordProvider, String keyStoreType) throws StoreConfigurationException {
/*  41 */     super(location, passwordProvider);
/*  42 */     this.keyStoreType = (keyStoreType == null) ? "JKS" : keyStoreType;
/*  43 */     this.keyStore = load();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AbstractKeyStoreConfiguration(String location, char[] password, String keyStoreType) throws StoreConfigurationException {
/*  52 */     this(location, new MemoryPasswordProvider(password), keyStoreType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AbstractKeyStoreConfiguration(String location, String password, String keyStoreType) throws StoreConfigurationException {
/*  61 */     this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()), keyStoreType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected KeyStore load() throws StoreConfigurationException {
/*  66 */     String loadLocation = getLocation();
/*  67 */     LOGGER.debug("Loading keystore from location {}", loadLocation);
/*     */     try {
/*  69 */       if (loadLocation == null) {
/*  70 */         throw new IOException("The location is null");
/*     */       }
/*  72 */       try (InputStream fin = openInputStream(loadLocation)) {
/*  73 */         KeyStore ks = KeyStore.getInstance(this.keyStoreType);
/*  74 */         char[] password = getPasswordAsCharArray();
/*     */         try {
/*  76 */           ks.load(fin, password);
/*     */         } finally {
/*  78 */           if (password != null) {
/*  79 */             Arrays.fill(password, false);
/*     */           }
/*     */         } 
/*  82 */         LOGGER.debug("KeyStore successfully loaded from location {}", loadLocation);
/*  83 */         return ks;
/*     */       } 
/*  85 */     } catch (CertificateException e) {
/*  86 */       LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {} for location {}", this.keyStoreType, loadLocation, e);
/*  87 */       throw new StoreConfigurationException(loadLocation, e);
/*  88 */     } catch (NoSuchAlgorithmException e) {
/*  89 */       LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found for location {}", loadLocation, e);
/*  90 */       throw new StoreConfigurationException(loadLocation, e);
/*  91 */     } catch (KeyStoreException e) {
/*  92 */       LOGGER.error("KeyStoreException for location {}", loadLocation, e);
/*  93 */       throw new StoreConfigurationException(loadLocation, e);
/*  94 */     } catch (FileNotFoundException e) {
/*  95 */       LOGGER.error("The keystore file {} is not found", loadLocation, e);
/*  96 */       throw new StoreConfigurationException(loadLocation, e);
/*  97 */     } catch (IOException e) {
/*  98 */       LOGGER.error("Something is wrong with the format of the keystore or the given password for location {}", loadLocation, e);
/*  99 */       throw new StoreConfigurationException(loadLocation, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private InputStream openInputStream(String filePathOrUri) {
/* 104 */     return ConfigurationSource.fromUri(NetUtils.toURI(filePathOrUri)).getInputStream();
/*     */   }
/*     */   
/*     */   public KeyStore getKeyStore() {
/* 108 */     return this.keyStore;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     int prime = 31;
/* 114 */     int result = super.hashCode();
/* 115 */     result = 31 * result + ((this.keyStore == null) ? 0 : this.keyStore.hashCode());
/* 116 */     result = 31 * result + ((this.keyStoreType == null) ? 0 : this.keyStoreType.hashCode());
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 122 */     if (this == obj) {
/* 123 */       return true;
/*     */     }
/* 125 */     if (!super.equals(obj)) {
/* 126 */       return false;
/*     */     }
/* 128 */     if (getClass() != obj.getClass()) {
/* 129 */       return false;
/*     */     }
/* 131 */     AbstractKeyStoreConfiguration other = (AbstractKeyStoreConfiguration)obj;
/* 132 */     if (!Objects.equals(this.keyStore, other.keyStore)) {
/* 133 */       return false;
/*     */     }
/* 135 */     if (!Objects.equals(this.keyStoreType, other.keyStoreType)) {
/* 136 */       return false;
/*     */     }
/* 138 */     return true;
/*     */   }
/*     */   
/*     */   public String getKeyStoreType() {
/* 142 */     return this.keyStoreType;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\AbstractKeyStoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */