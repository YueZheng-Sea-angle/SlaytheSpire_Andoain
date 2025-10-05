/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Objects;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
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
/*     */ public class HttpURLConnectionManager
/*     */   extends HttpManager
/*     */ {
/*  42 */   private static final Charset CHARSET = Charset.forName("US-ASCII");
/*     */   
/*     */   private final URL url;
/*     */   
/*     */   private final boolean isHttps;
/*     */   
/*     */   private final String method;
/*     */   
/*     */   private final int connectTimeoutMillis;
/*     */   
/*     */   private final int readTimeoutMillis;
/*     */   
/*     */   private final Property[] headers;
/*     */   private final SslConfiguration sslConfiguration;
/*     */   private final boolean verifyHostname;
/*     */   
/*     */   public HttpURLConnectionManager(Configuration configuration, LoggerContext loggerContext, String name, URL url, String method, int connectTimeoutMillis, int readTimeoutMillis, Property[] headers, SslConfiguration sslConfiguration, boolean verifyHostname) {
/*  59 */     super(configuration, loggerContext, name);
/*  60 */     this.url = url;
/*  61 */     if (!url.getProtocol().equalsIgnoreCase("http") && !url.getProtocol().equalsIgnoreCase("https")) {
/*  62 */       throw new ConfigurationException("URL must have scheme http or https");
/*     */     }
/*  64 */     this.isHttps = this.url.getProtocol().equalsIgnoreCase("https");
/*  65 */     this.method = Objects.<String>requireNonNull(method, "method");
/*  66 */     this.connectTimeoutMillis = connectTimeoutMillis;
/*  67 */     this.readTimeoutMillis = readTimeoutMillis;
/*  68 */     this.headers = (headers != null) ? headers : Property.EMPTY_ARRAY;
/*  69 */     this.sslConfiguration = sslConfiguration;
/*  70 */     if (this.sslConfiguration != null && !this.isHttps) {
/*  71 */       throw new ConfigurationException("SSL configuration can only be specified with URL scheme https");
/*     */     }
/*  73 */     this.verifyHostname = verifyHostname;
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(Layout<?> layout, LogEvent event) throws IOException {
/*  78 */     HttpURLConnection urlConnection = (HttpURLConnection)this.url.openConnection();
/*  79 */     urlConnection.setAllowUserInteraction(false);
/*  80 */     urlConnection.setDoOutput(true);
/*  81 */     urlConnection.setDoInput(true);
/*  82 */     urlConnection.setRequestMethod(this.method);
/*  83 */     if (this.connectTimeoutMillis > 0) {
/*  84 */       urlConnection.setConnectTimeout(this.connectTimeoutMillis);
/*     */     }
/*  86 */     if (this.readTimeoutMillis > 0) {
/*  87 */       urlConnection.setReadTimeout(this.readTimeoutMillis);
/*     */     }
/*  89 */     if (layout.getContentType() != null) {
/*  90 */       urlConnection.setRequestProperty("Content-Type", layout.getContentType());
/*     */     }
/*  92 */     for (Property header : this.headers) {
/*  93 */       urlConnection.setRequestProperty(header
/*  94 */           .getName(), 
/*  95 */           header.isValueNeedsLookup() ? getConfiguration().getStrSubstitutor().replace(event, header.getValue()) : header.getValue());
/*     */     }
/*  97 */     if (this.sslConfiguration != null) {
/*  98 */       ((HttpsURLConnection)urlConnection).setSSLSocketFactory(this.sslConfiguration.getSslSocketFactory());
/*     */     }
/* 100 */     if (this.isHttps && !this.verifyHostname) {
/* 101 */       ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
/*     */     }
/*     */     
/* 104 */     byte[] msg = layout.toByteArray(event);
/* 105 */     urlConnection.setFixedLengthStreamingMode(msg.length);
/* 106 */     urlConnection.connect();
/* 107 */     try (OutputStream os = urlConnection.getOutputStream()) {
/* 108 */       os.write(msg);
/*     */     } 
/*     */     
/* 111 */     byte[] buffer = new byte[1024];
/* 112 */     try (InputStream is = urlConnection.getInputStream()) {
/* 113 */       while (-1 != is.read(buffer));
/*     */     
/*     */     }
/* 116 */     catch (IOException e) {
/* 117 */       StringBuilder errorMessage = new StringBuilder();
/* 118 */       try (InputStream es = urlConnection.getErrorStream()) {
/* 119 */         errorMessage.append(urlConnection.getResponseCode());
/* 120 */         if (urlConnection.getResponseMessage() != null) {
/* 121 */           errorMessage.append(' ').append(urlConnection.getResponseMessage());
/*     */         }
/* 123 */         if (es != null) {
/* 124 */           errorMessage.append(" - ");
/*     */           int n;
/* 126 */           while (-1 != (n = es.read(buffer))) {
/* 127 */             errorMessage.append(new String(buffer, 0, n, CHARSET));
/*     */           }
/*     */         } 
/*     */       } 
/* 131 */       if (urlConnection.getResponseCode() > -1) {
/* 132 */         throw new IOException(errorMessage.toString());
/*     */       }
/* 134 */       throw e;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\HttpURLConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */