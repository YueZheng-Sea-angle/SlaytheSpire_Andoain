/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "log4j", category = "Lookup")
/*    */ public class Log4jLookup
/*    */   extends AbstractConfigurationAwareLookup
/*    */ {
/*    */   public static final String KEY_CONFIG_LOCATION = "configLocation";
/*    */   public static final String KEY_CONFIG_PARENT_LOCATION = "configParentLocation";
/* 38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private static String asPath(URI uri) {
/* 41 */     if (uri.getScheme() == null || uri.getScheme().equals("file")) {
/* 42 */       return uri.getPath();
/*    */     }
/* 44 */     return uri.toString();
/*    */   }
/*    */   
/*    */   private static URI getParent(URI uri) throws URISyntaxException {
/* 48 */     String s = uri.toString();
/* 49 */     int offset = s.lastIndexOf('/');
/* 50 */     if (offset > -1) {
/* 51 */       return new URI(s.substring(0, offset));
/*    */     }
/* 53 */     return new URI("../");
/*    */   }
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 58 */     if (this.configuration != null) {
/* 59 */       ConfigurationSource configSrc = this.configuration.getConfigurationSource();
/* 60 */       File file = configSrc.getFile();
/* 61 */       if (file != null) {
/* 62 */         switch (key) {
/*    */           case "configLocation":
/* 64 */             return file.getAbsolutePath();
/*    */           
/*    */           case "configParentLocation":
/* 67 */             return file.getParentFile().getAbsolutePath();
/*    */         } 
/*    */         
/* 70 */         return null;
/*    */       } 
/*    */ 
/*    */       
/* 74 */       URL url = configSrc.getURL();
/* 75 */       if (url != null) {
/*    */         try {
/* 77 */           switch (key) {
/*    */             case "configLocation":
/* 79 */               return asPath(url.toURI());
/*    */             
/*    */             case "configParentLocation":
/* 82 */               return asPath(getParent(url.toURI()));
/*    */           } 
/*    */           
/* 85 */           return null;
/*    */         }
/* 87 */         catch (URISyntaxException use) {
/* 88 */           LOGGER.error(use);
/*    */         } 
/*    */       }
/*    */     } 
/*    */     
/* 93 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\Log4jLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */