/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import javax.net.ssl.HttpsURLConnection;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
/*    */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*    */ import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
/*    */ import org.apache.logging.log4j.core.util.AuthorizationProvider;
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
/*    */ public class UrlConnectionFactory
/*    */ {
/* 36 */   private static int DEFAULT_TIMEOUT = 60000;
/* 37 */   private static int connectTimeoutMillis = DEFAULT_TIMEOUT;
/* 38 */   private static int readTimeoutMillis = DEFAULT_TIMEOUT;
/*    */   
/*    */   private static final String JSON = "application/json";
/*    */   private static final String XML = "application/xml";
/*    */   private static final String PROPERTIES = "text/x-java-properties";
/*    */   private static final String TEXT = "text/plain";
/*    */   private static final String HTTP = "http";
/*    */   private static final String HTTPS = "https";
/*    */   
/*    */   public static HttpURLConnection createConnection(URL url, long lastModifiedMillis, SslConfiguration sslConfiguration) throws IOException {
/* 48 */     HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
/* 49 */     AuthorizationProvider provider = ConfigurationFactory.getAuthorizationProvider();
/* 50 */     if (provider != null) {
/* 51 */       provider.addAuthorization(urlConnection);
/*    */     }
/* 53 */     urlConnection.setAllowUserInteraction(false);
/* 54 */     urlConnection.setDoOutput(true);
/* 55 */     urlConnection.setDoInput(true);
/* 56 */     urlConnection.setRequestMethod("GET");
/* 57 */     if (connectTimeoutMillis > 0) {
/* 58 */       urlConnection.setConnectTimeout(connectTimeoutMillis);
/*    */     }
/* 60 */     if (readTimeoutMillis > 0) {
/* 61 */       urlConnection.setReadTimeout(readTimeoutMillis);
/*    */     }
/* 63 */     String[] fileParts = url.getFile().split("\\.");
/* 64 */     String type = fileParts[fileParts.length - 1].trim();
/* 65 */     String contentType = isXml(type) ? "application/xml" : (isJson(type) ? "application/json" : (isProperties(type) ? "text/x-java-properties" : "text/plain"));
/* 66 */     urlConnection.setRequestProperty("Content-Type", contentType);
/* 67 */     if (lastModifiedMillis > 0L) {
/* 68 */       urlConnection.setIfModifiedSince(lastModifiedMillis);
/*    */     }
/* 70 */     if (url.getProtocol().equals("https") && sslConfiguration != null) {
/* 71 */       ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslConfiguration.getSslSocketFactory());
/* 72 */       if (!sslConfiguration.isVerifyHostName()) {
/* 73 */         ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
/*    */       }
/*    */     } 
/* 76 */     return urlConnection;
/*    */   }
/*    */   
/*    */   public static URLConnection createConnection(URL url) throws IOException {
/* 80 */     URLConnection urlConnection = null;
/* 81 */     if (url.getProtocol().equals("https") || url.getProtocol().equals("http")) {
/* 82 */       urlConnection = createConnection(url, 0L, SslConfigurationFactory.getSslConfiguration());
/*    */     } else {
/* 84 */       urlConnection = url.openConnection();
/*    */     } 
/* 86 */     return urlConnection;
/*    */   }
/*    */ 
/*    */   
/*    */   private static boolean isXml(String type) {
/* 91 */     return type.equalsIgnoreCase("xml");
/*    */   }
/*    */   
/*    */   private static boolean isJson(String type) {
/* 95 */     return (type.equalsIgnoreCase("json") || type.equalsIgnoreCase("jsn"));
/*    */   }
/*    */   
/*    */   private static boolean isProperties(String type) {
/* 99 */     return type.equalsIgnoreCase("properties");
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\UrlConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */