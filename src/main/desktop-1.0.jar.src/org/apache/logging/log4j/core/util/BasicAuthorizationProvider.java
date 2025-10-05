/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.net.URLConnection;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.Base64Util;
/*    */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*    */ public class BasicAuthorizationProvider
/*    */   implements AuthorizationProvider
/*    */ {
/* 30 */   private static final String[] PREFIXES = new String[] { "log4j2.config.", "logging.auth." };
/*    */   
/*    */   private static final String AUTH_USER_NAME = "username";
/*    */   private static final String AUTH_PASSWORD = "password";
/*    */   private static final String AUTH_PASSWORD_DECRYPTOR = "passwordDecryptor";
/*    */   public static final String CONFIG_USER_NAME = "log4j2.configurationUserName";
/*    */   public static final String CONFIG_PASSWORD = "log4j2.configurationPassword";
/*    */   public static final String PASSWORD_DECRYPTOR = "log4j2.passwordDecryptor";
/* 38 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/* 40 */   private String authString = null;
/*    */   
/*    */   public BasicAuthorizationProvider(PropertiesUtil props) {
/* 43 */     String userName = props.getStringProperty(PREFIXES, "username", () -> props.getStringProperty("log4j2.configurationUserName"));
/*    */     
/* 45 */     String password = props.getStringProperty(PREFIXES, "password", () -> props.getStringProperty("log4j2.configurationPassword"));
/*    */     
/* 47 */     String decryptor = props.getStringProperty(PREFIXES, "passwordDecryptor", () -> props.getStringProperty("log4j2.passwordDecryptor"));
/*    */     
/* 49 */     if (decryptor != null) {
/*    */       try {
/* 51 */         Object obj = LoaderUtil.newInstanceOf(decryptor);
/* 52 */         if (obj instanceof PasswordDecryptor) {
/* 53 */           password = ((PasswordDecryptor)obj).decryptPassword(password);
/*    */         }
/* 55 */       } catch (Exception ex) {
/* 56 */         LOGGER.warn("Unable to decrypt password.", ex);
/*    */       } 
/*    */     }
/* 59 */     if (userName != null && password != null) {
/* 60 */       this.authString = "Basic " + Base64Util.encode(userName + ":" + password);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAuthorization(URLConnection urlConnection) {
/* 66 */     if (this.authString != null)
/* 67 */       urlConnection.setRequestProperty("Authorization", this.authString); 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\BasicAuthorizationProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */