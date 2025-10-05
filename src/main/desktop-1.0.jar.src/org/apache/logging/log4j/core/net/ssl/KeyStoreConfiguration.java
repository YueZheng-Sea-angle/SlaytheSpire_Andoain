/*     */ package org.apache.logging.log4j.core.net.ssl;
/*     */ 
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import javax.net.ssl.KeyManagerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "KeyStore", category = "Core", printObject = true)
/*     */ public class KeyStoreConfiguration
/*     */   extends AbstractKeyStoreConfiguration
/*     */ {
/*     */   private final String keyManagerFactoryAlgorithm;
/*     */   
/*     */   public KeyStoreConfiguration(String location, PasswordProvider passwordProvider, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  48 */     super(location, passwordProvider, keyStoreType);
/*  49 */     this.keyManagerFactoryAlgorithm = (keyManagerFactoryAlgorithm == null) ? KeyManagerFactory.getDefaultAlgorithm() : keyManagerFactoryAlgorithm;
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
/*     */   public KeyStoreConfiguration(String location, char[] password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  63 */     this(location, new MemoryPasswordProvider(password), keyStoreType, keyManagerFactoryAlgorithm);
/*  64 */     if (password != null) {
/*  65 */       Arrays.fill(password, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KeyStoreConfiguration(String location, String password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/*  77 */     this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()), keyStoreType, keyManagerFactoryAlgorithm);
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
/*     */   public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute(value = "password", sensitive = true) char[] password, @PluginAttribute("passwordEnvironmentVariable") String passwordEnvironmentVariable, @PluginAttribute("passwordFile") String passwordFile, @PluginAttribute("type") String keyStoreType, @PluginAttribute("keyManagerFactoryAlgorithm") String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 106 */     if (password != null && passwordEnvironmentVariable != null && passwordFile != null) {
/* 107 */       throw new StoreConfigurationException("You MUST set only one of 'password', 'passwordEnvironmentVariable' or 'passwordFile'.");
/*     */     }
/*     */     
/*     */     try {
/* 111 */       PasswordProvider provider = (passwordFile != null) ? new FilePasswordProvider(passwordFile) : ((passwordEnvironmentVariable != null) ? new EnvironmentPasswordProvider(passwordEnvironmentVariable) : new MemoryPasswordProvider(password));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       if (password != null) {
/* 119 */         Arrays.fill(password, false);
/*     */       }
/* 121 */       return new KeyStoreConfiguration(location, provider, keyStoreType, keyManagerFactoryAlgorithm);
/* 122 */     } catch (Exception ex) {
/* 123 */       throw new StoreConfigurationException("Could not configure KeyStore", ex);
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
/*     */   public static KeyStoreConfiguration createKeyStoreConfiguration(String location, char[] password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 138 */     return createKeyStoreConfiguration(location, password, (String)null, (String)null, keyStoreType, keyManagerFactoryAlgorithm);
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
/*     */   public static KeyStoreConfiguration createKeyStoreConfiguration(String location, String password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
/* 161 */     return createKeyStoreConfiguration(location, (password == null) ? null : password
/* 162 */         .toCharArray(), keyStoreType, keyManagerFactoryAlgorithm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyManagerFactory initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
/* 169 */     KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(this.keyManagerFactoryAlgorithm);
/* 170 */     char[] password = getPasswordAsCharArray();
/*     */     try {
/* 172 */       kmFactory.init(getKeyStore(), password);
/*     */     } finally {
/* 174 */       if (password != null) {
/* 175 */         Arrays.fill(password, false);
/*     */       }
/*     */     } 
/* 178 */     return kmFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 183 */     int prime = 31;
/* 184 */     int result = super.hashCode();
/* 185 */     result = 31 * result + ((this.keyManagerFactoryAlgorithm == null) ? 0 : this.keyManagerFactoryAlgorithm.hashCode());
/* 186 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 191 */     if (this == obj) {
/* 192 */       return true;
/*     */     }
/* 194 */     if (!super.equals(obj)) {
/* 195 */       return false;
/*     */     }
/* 197 */     if (getClass() != obj.getClass()) {
/* 198 */       return false;
/*     */     }
/* 200 */     KeyStoreConfiguration other = (KeyStoreConfiguration)obj;
/* 201 */     if (!Objects.equals(this.keyManagerFactoryAlgorithm, other.keyManagerFactoryAlgorithm)) {
/* 202 */       return false;
/*     */     }
/* 204 */     return true;
/*     */   }
/*     */   
/*     */   public String getKeyManagerFactoryAlgorithm() {
/* 208 */     return this.keyManagerFactoryAlgorithm;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\KeyStoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */