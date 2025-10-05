/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*    */ public class SslConfigurationFactory
/*    */ {
/* 28 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 29 */   private static SslConfiguration sslConfiguration = null;
/*    */   
/*    */   private static final String trustStorelocation = "log4j2.trustStoreLocation";
/*    */   private static final String trustStorePassword = "log4j2.trustStorePassword";
/*    */   private static final String trustStorePasswordFile = "log4j2.trustStorePasswordFile";
/*    */   private static final String trustStorePasswordEnvVar = "log4j2.trustStorePasswordEnvironmentVariable";
/*    */   private static final String trustStoreKeyStoreType = "log4j2.trustStoreKeyStoreType";
/*    */   private static final String trustStoreKeyManagerFactoryAlgorithm = "log4j2.trustStoreKeyManagerFactoryAlgorithm";
/*    */   private static final String keyStoreLocation = "log4j2.keyStoreLocation";
/*    */   private static final String keyStorePassword = "log4j2.keyStorePassword";
/*    */   private static final String keyStorePasswordFile = "log4j2.keyStorePasswordFile";
/*    */   private static final String keyStorePasswordEnvVar = "log4j2.keyStorePasswordEnvironmentVariable";
/*    */   private static final String keyStoreType = "log4j2.keyStoreType";
/*    */   private static final String keyStoreKeyManagerFactoryAlgorithm = "log4j2.keyStoreKeyManagerFactoryAlgorithm";
/*    */   private static final String verifyHostName = "log4j2.sslVerifyHostName";
/*    */   
/*    */   static {
/* 46 */     PropertiesUtil props = PropertiesUtil.getProperties();
/* 47 */     KeyStoreConfiguration keyStoreConfiguration = null;
/* 48 */     TrustStoreConfiguration trustStoreConfiguration = null;
/* 49 */     String location = props.getStringProperty("log4j2.trustStoreLocation");
/* 50 */     if (location != null) {
/* 51 */       String password = props.getStringProperty("log4j2.trustStorePassword");
/* 52 */       char[] passwordChars = null;
/* 53 */       if (password != null) {
/* 54 */         passwordChars = password.toCharArray();
/*    */       }
/*    */       try {
/* 57 */         trustStoreConfiguration = TrustStoreConfiguration.createKeyStoreConfiguration(location, passwordChars, props
/* 58 */             .getStringProperty("log4j2.trustStorePasswordEnvironmentVariable"), props.getStringProperty("log4j2.trustStorePasswordFile"), props
/* 59 */             .getStringProperty("log4j2.trustStoreKeyStoreType"), props.getStringProperty("log4j2.trustStoreKeyManagerFactoryAlgorithm"));
/* 60 */       } catch (Exception ex) {
/* 61 */         LOGGER.warn("Unable to create trust store configuration due to: {} {}", ex.getClass().getName(), ex
/* 62 */             .getMessage());
/*    */       } 
/*    */     } 
/* 65 */     location = props.getStringProperty("log4j2.keyStoreLocation");
/* 66 */     if (location != null) {
/* 67 */       String password = props.getStringProperty("log4j2.keyStorePassword");
/* 68 */       char[] passwordChars = null;
/* 69 */       if (password != null) {
/* 70 */         passwordChars = password.toCharArray();
/*    */       }
/*    */       try {
/* 73 */         keyStoreConfiguration = KeyStoreConfiguration.createKeyStoreConfiguration(location, passwordChars, props
/* 74 */             .getStringProperty("log4j2.keyStorePasswordEnvironmentVariable"), props.getStringProperty("log4j2.keyStorePasswordFile"), props
/* 75 */             .getStringProperty("log4j2.keyStoreType"), props.getStringProperty("log4j2.keyStoreKeyManagerFactoryAlgorithm"));
/* 76 */       } catch (Exception ex) {
/* 77 */         LOGGER.warn("Unable to create key store configuration due to: {} {}", ex.getClass().getName(), ex
/* 78 */             .getMessage());
/*    */       } 
/*    */     } 
/* 81 */     if (trustStoreConfiguration != null || keyStoreConfiguration != null) {
/* 82 */       boolean isVerifyHostName = props.getBooleanProperty("log4j2.sslVerifyHostName", false);
/* 83 */       sslConfiguration = SslConfiguration.createSSLConfiguration("https", keyStoreConfiguration, trustStoreConfiguration, isVerifyHostName);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static SslConfiguration getSslConfiguration() {
/* 89 */     return sslConfiguration;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\SslConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */