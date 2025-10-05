/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.util.Objects;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Ssl", category = "Core", printObject = true)
/*     */ public class SslConfiguration
/*     */ {
/*  45 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*     */   
/*     */   private final KeyStoreConfiguration keyStoreConfig;
/*     */   private final TrustStoreConfiguration trustStoreConfig;
/*     */   private final SSLContext sslContext;
/*     */   private final String protocol;
/*     */   private final boolean verifyHostName;
/*     */   
/*     */   private SslConfiguration(String protocol, KeyStoreConfiguration keyStoreConfig, TrustStoreConfiguration trustStoreConfig, boolean verifyHostName) {
/*  54 */     this.keyStoreConfig = keyStoreConfig;
/*  55 */     this.trustStoreConfig = trustStoreConfig;
/*  56 */     this.protocol = (protocol == null) ? "SSL" : protocol;
/*  57 */     this.sslContext = createSslContext();
/*  58 */     this.verifyHostName = verifyHostName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearSecrets() {
/*  65 */     if (this.keyStoreConfig != null) {
/*  66 */       this.keyStoreConfig.clearSecrets();
/*     */     }
/*  68 */     if (this.trustStoreConfig != null) {
/*  69 */       this.trustStoreConfig.clearSecrets();
/*     */     }
/*     */   }
/*     */   
/*     */   public SSLSocketFactory getSslSocketFactory() {
/*  74 */     return this.sslContext.getSocketFactory();
/*     */   }
/*     */   
/*     */   public SSLServerSocketFactory getSslServerSocketFactory() {
/*  78 */     return this.sslContext.getServerSocketFactory();
/*     */   }
/*     */   
/*     */   private SSLContext createSslContext() {
/*  82 */     SSLContext context = null;
/*     */     
/*     */     try {
/*  85 */       context = createSslContextBasedOnConfiguration();
/*  86 */       LOGGER.debug("Creating SSLContext with the given parameters");
/*     */     }
/*  88 */     catch (TrustStoreConfigurationException e) {
/*  89 */       context = createSslContextWithTrustStoreFailure();
/*     */     }
/*  91 */     catch (KeyStoreConfigurationException e) {
/*  92 */       context = createSslContextWithKeyStoreFailure();
/*     */     } 
/*  94 */     return context;
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLContext createSslContextWithTrustStoreFailure() {
/*     */     SSLContext context;
/*     */     try {
/* 101 */       context = createSslContextWithDefaultTrustManagerFactory();
/* 102 */       LOGGER.debug("Creating SSLContext with default truststore");
/*     */     }
/* 104 */     catch (KeyStoreConfigurationException e) {
/* 105 */       context = createDefaultSslContext();
/* 106 */       LOGGER.debug("Creating SSLContext with default configuration");
/*     */     } 
/* 108 */     return context;
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLContext createSslContextWithKeyStoreFailure() {
/*     */     SSLContext context;
/*     */     try {
/* 115 */       context = createSslContextWithDefaultKeyManagerFactory();
/* 116 */       LOGGER.debug("Creating SSLContext with default keystore");
/*     */     }
/* 118 */     catch (TrustStoreConfigurationException e) {
/* 119 */       context = createDefaultSslContext();
/* 120 */       LOGGER.debug("Creating SSLContext with default configuration");
/*     */     } 
/* 122 */     return context;
/*     */   }
/*     */   
/*     */   private SSLContext createSslContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
/* 126 */     return createSslContext(false, false);
/*     */   }
/*     */   
/*     */   private SSLContext createSslContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
/*     */     try {
/* 131 */       return createSslContext(true, false);
/* 132 */     } catch (KeyStoreConfigurationException dummy) {
/* 133 */       LOGGER.debug("Exception occurred while using default keystore. This should be a BUG");
/* 134 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLContext createSslContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
/*     */     try {
/* 140 */       return createSslContext(false, true);
/*     */     }
/* 142 */     catch (TrustStoreConfigurationException dummy) {
/* 143 */       LOGGER.debug("Exception occurred while using default truststore. This should be a BUG");
/* 144 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private SSLContext createDefaultSslContext() {
/*     */     try {
/* 150 */       return SSLContext.getDefault();
/* 151 */     } catch (NoSuchAlgorithmException e) {
/* 152 */       LOGGER.error("Failed to create an SSLContext with default configuration", e);
/* 153 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private SSLContext createSslContext(boolean loadDefaultKeyManagerFactory, boolean loadDefaultTrustManagerFactory) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
/*     */     try {
/* 160 */       KeyManager[] kManagers = null;
/* 161 */       TrustManager[] tManagers = null;
/*     */       
/* 163 */       SSLContext newSslContext = SSLContext.getInstance(this.protocol);
/* 164 */       if (!loadDefaultKeyManagerFactory) {
/* 165 */         KeyManagerFactory kmFactory = loadKeyManagerFactory();
/* 166 */         kManagers = kmFactory.getKeyManagers();
/*     */       } 
/* 168 */       if (!loadDefaultTrustManagerFactory) {
/* 169 */         TrustManagerFactory tmFactory = loadTrustManagerFactory();
/* 170 */         tManagers = tmFactory.getTrustManagers();
/*     */       } 
/*     */       
/* 173 */       newSslContext.init(kManagers, tManagers, null);
/* 174 */       return newSslContext;
/*     */     }
/* 176 */     catch (NoSuchAlgorithmException e) {
/* 177 */       LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol", e);
/* 178 */       throw new TrustStoreConfigurationException(e);
/*     */     }
/* 180 */     catch (KeyManagementException e) {
/* 181 */       LOGGER.error("Failed to initialize the SSLContext", e);
/* 182 */       throw new KeyStoreConfigurationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
/* 187 */     if (this.trustStoreConfig == null) {
/* 188 */       throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
/*     */     }
/*     */     
/*     */     try {
/* 192 */       return this.trustStoreConfig.initTrustManagerFactory();
/*     */     }
/* 194 */     catch (NoSuchAlgorithmException e) {
/* 195 */       LOGGER.error("The specified algorithm is not available from the specified provider", e);
/* 196 */       throw new TrustStoreConfigurationException(e);
/* 197 */     } catch (KeyStoreException e) {
/* 198 */       LOGGER.error("Failed to initialize the TrustManagerFactory", e);
/* 199 */       throw new TrustStoreConfigurationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
/* 204 */     if (this.keyStoreConfig == null) {
/* 205 */       throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
/*     */     }
/*     */     
/*     */     try {
/* 209 */       return this.keyStoreConfig.initKeyManagerFactory();
/*     */     }
/* 211 */     catch (NoSuchAlgorithmException e) {
/* 212 */       LOGGER.error("The specified algorithm is not available from the specified provider", e);
/* 213 */       throw new KeyStoreConfigurationException(e);
/* 214 */     } catch (KeyStoreException e) {
/* 215 */       LOGGER.error("Failed to initialize the TrustManagerFactory", e);
/* 216 */       throw new KeyStoreConfigurationException(e);
/* 217 */     } catch (UnrecoverableKeyException e) {
/* 218 */       LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)", e);
/* 219 */       throw new KeyStoreConfigurationException(e);
/*     */     } 
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
/*     */   @PluginFactory
/*     */   public static SslConfiguration createSSLConfiguration(@PluginAttribute("protocol") String protocol, @PluginElement("KeyStore") KeyStoreConfiguration keyStoreConfig, @PluginElement("TrustStore") TrustStoreConfiguration trustStoreConfig) {
/* 238 */     return new SslConfiguration(protocol, keyStoreConfig, trustStoreConfig, false);
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
/*     */   public static SslConfiguration createSSLConfiguration(@PluginAttribute("protocol") String protocol, @PluginElement("KeyStore") KeyStoreConfiguration keyStoreConfig, @PluginElement("TrustStore") TrustStoreConfiguration trustStoreConfig, @PluginAttribute("verifyHostName") boolean verifyHostName) {
/* 258 */     return new SslConfiguration(protocol, keyStoreConfig, trustStoreConfig, verifyHostName);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 263 */     return Objects.hash(new Object[] { this.keyStoreConfig, this.protocol, this.sslContext, this.trustStoreConfig });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 268 */     if (this == obj) {
/* 269 */       return true;
/*     */     }
/* 271 */     if (obj == null) {
/* 272 */       return false;
/*     */     }
/* 274 */     if (getClass() != obj.getClass()) {
/* 275 */       return false;
/*     */     }
/* 277 */     SslConfiguration other = (SslConfiguration)obj;
/* 278 */     if (!Objects.equals(this.keyStoreConfig, other.keyStoreConfig)) {
/* 279 */       return false;
/*     */     }
/* 281 */     if (!Objects.equals(this.protocol, other.protocol)) {
/* 282 */       return false;
/*     */     }
/* 284 */     if (!Objects.equals(this.sslContext, other.sslContext)) {
/* 285 */       return false;
/*     */     }
/* 287 */     if (!Objects.equals(this.trustStoreConfig, other.trustStoreConfig)) {
/* 288 */       return false;
/*     */     }
/* 290 */     return true;
/*     */   }
/*     */   
/*     */   public KeyStoreConfiguration getKeyStoreConfig() {
/* 294 */     return this.keyStoreConfig;
/*     */   }
/*     */   
/*     */   public TrustStoreConfiguration getTrustStoreConfig() {
/* 298 */     return this.trustStoreConfig;
/*     */   }
/*     */   
/*     */   public SSLContext getSslContext() {
/* 302 */     return this.sslContext;
/*     */   }
/*     */   
/*     */   public String getProtocol() {
/* 306 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public boolean isVerifyHostName() {
/* 310 */     return this.verifyHostName;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\SslConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */