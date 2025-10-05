/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "TrustStore", category = "Core", printObject = true)
/*     */ public class TrustStoreConfiguration
/*     */   extends AbstractKeyStoreConfiguration
/*     */ {
/*     */   private final String trustManagerFactoryAlgorithm;
/*     */   
/*     */   public TrustStoreConfiguration(String location, PasswordProvider passwordProvider, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  43 */     super(location, passwordProvider, keyStoreType);
/*  44 */     this
/*  45 */       .trustManagerFactoryAlgorithm = (trustManagerFactoryAlgorithm == null) ? TrustManagerFactory.getDefaultAlgorithm() : trustManagerFactoryAlgorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TrustStoreConfiguration(String location, char[] password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  54 */     this(location, new MemoryPasswordProvider(password), keyStoreType, trustManagerFactoryAlgorithm);
/*  55 */     if (password != null) {
/*  56 */       Arrays.fill(password, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TrustStoreConfiguration(String location, String password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  66 */     this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()), keyStoreType, trustManagerFactoryAlgorithm);
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
/*     */   @PluginFactory
/*     */   public static TrustStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute(value = "password", sensitive = true) char[] password, @PluginAttribute("passwordEnvironmentVariable") String passwordEnvironmentVariable, @PluginAttribute("passwordFile") String passwordFile, @PluginAttribute("type") String keyStoreType, @PluginAttribute("trustManagerFactoryAlgorithm") String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  95 */     if (password != null && passwordEnvironmentVariable != null && passwordFile != null) {
/*  96 */       throw new IllegalStateException("You MUST set only one of 'password', 'passwordEnvironmentVariable' or 'passwordFile'.");
/*     */     }
/*     */     
/*     */     try {
/* 100 */       PasswordProvider provider = (passwordFile != null) ? new FilePasswordProvider(passwordFile) : ((passwordEnvironmentVariable != null) ? new EnvironmentPasswordProvider(passwordEnvironmentVariable) : new MemoryPasswordProvider(password));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       if (password != null) {
/* 108 */         Arrays.fill(password, false);
/*     */       }
/* 110 */       return new TrustStoreConfiguration(location, provider, keyStoreType, trustManagerFactoryAlgorithm);
/* 111 */     } catch (Exception ex) {
/* 112 */       throw new StoreConfigurationException("Could not configure TrustStore", ex);
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
/*     */   @Deprecated
/*     */   public static TrustStoreConfiguration createKeyStoreConfiguration(String location, char[] password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 127 */     return createKeyStoreConfiguration(location, password, null, null, keyStoreType, trustManagerFactoryAlgorithm);
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
/*     */   @Deprecated
/*     */   public static TrustStoreConfiguration createKeyStoreConfiguration(String location, String password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 150 */     return createKeyStoreConfiguration(location, (password == null) ? null : password.toCharArray(), null, null, keyStoreType, trustManagerFactoryAlgorithm);
/*     */   }
/*     */ 
/*     */   
/*     */   public TrustManagerFactory initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
/* 155 */     TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(this.trustManagerFactoryAlgorithm);
/* 156 */     tmFactory.init(getKeyStore());
/* 157 */     return tmFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 162 */     int prime = 31;
/* 163 */     int result = super.hashCode();
/*     */     
/* 165 */     result = 31 * result + ((this.trustManagerFactoryAlgorithm == null) ? 0 : this.trustManagerFactoryAlgorithm.hashCode());
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 171 */     if (this == obj) {
/* 172 */       return true;
/*     */     }
/* 174 */     if (!super.equals(obj)) {
/* 175 */       return false;
/*     */     }
/* 177 */     if (getClass() != obj.getClass()) {
/* 178 */       return false;
/*     */     }
/* 180 */     TrustStoreConfiguration other = (TrustStoreConfiguration)obj;
/* 181 */     if (!Objects.equals(this.trustManagerFactoryAlgorithm, other.trustManagerFactoryAlgorithm)) {
/* 182 */       return false;
/*     */     }
/* 184 */     return true;
/*     */   }
/*     */   
/*     */   public String getTrustManagerFactoryAlgorithm() {
/* 188 */     return this.trustManagerFactoryAlgorithm;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\TrustStoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */