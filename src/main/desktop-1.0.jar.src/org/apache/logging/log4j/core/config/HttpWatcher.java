/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.net.UrlConnectionFactory;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.util.AbstractWatcher;
/*     */ import org.apache.logging.log4j.core.util.Source;
/*     */ import org.apache.logging.log4j.core.util.Watcher;
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
/*     */ @Plugin(name = "http", category = "Watcher", elementType = "watcher", printObject = true)
/*     */ @PluginAliases({"https"})
/*     */ public class HttpWatcher
/*     */   extends AbstractWatcher
/*     */ {
/*  45 */   private Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private SslConfiguration sslConfiguration;
/*     */   
/*     */   private URL url;
/*     */   private volatile long lastModifiedMillis;
/*     */   private static final int NOT_MODIFIED = 304;
/*     */   private static final int OK = 200;
/*     */   private static final int BUF_SIZE = 1024;
/*     */   private static final String HTTP = "http";
/*     */   private static final String HTTPS = "https";
/*     */   
/*     */   public HttpWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
/*  58 */     super(configuration, reconfigurable, configurationListeners);
/*  59 */     this.sslConfiguration = SslConfigurationFactory.getSslConfiguration();
/*  60 */     this.lastModifiedMillis = lastModifiedMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/*  65 */     return this.lastModifiedMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isModified() {
/*  70 */     return refreshConfiguration();
/*     */   }
/*     */ 
/*     */   
/*     */   public void watching(Source source) {
/*  75 */     if (!source.getURI().getScheme().equals("http") && !source.getURI().getScheme().equals("https")) {
/*  76 */       throw new IllegalArgumentException("HttpWatcher requires a url using the HTTP or HTTPS protocol, not " + source
/*  77 */           .getURI().getScheme());
/*     */     }
/*     */     try {
/*  80 */       this.url = source.getURI().toURL();
/*  81 */     } catch (MalformedURLException ex) {
/*  82 */       throw new IllegalArgumentException("Invalid URL for HttpWatcher " + source.getURI(), ex);
/*     */     } 
/*  84 */     super.watching(source);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
/*  90 */     HttpWatcher watcher = new HttpWatcher(getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
/*  91 */     if (getSource() != null) {
/*  92 */       watcher.watching(getSource());
/*     */     }
/*  94 */     return (Watcher)watcher;
/*     */   }
/*     */   
/*     */   private boolean refreshConfiguration() {
/*     */     try {
/*  99 */       HttpURLConnection urlConnection = UrlConnectionFactory.createConnection(this.url, this.lastModifiedMillis, this.sslConfiguration);
/*     */       
/* 101 */       urlConnection.connect();
/*     */       
/*     */       try {
/* 104 */         int code = urlConnection.getResponseCode();
/* 105 */         switch (code) {
/*     */           case 304:
/* 107 */             this.LOGGER.debug("Configuration Not Modified");
/* 108 */             return false;
/*     */           
/*     */           case 200:
/* 111 */             try (InputStream is = urlConnection.getInputStream()) {
/* 112 */               ConfigurationSource configSource = getConfiguration().getConfigurationSource();
/* 113 */               configSource.setData(readStream(is));
/* 114 */               this.lastModifiedMillis = urlConnection.getLastModified();
/* 115 */               configSource.setModifiedMillis(this.lastModifiedMillis);
/* 116 */               this.LOGGER.debug("Content was modified for {}", this.url.toString());
/* 117 */               return true;
/* 118 */             } catch (IOException e) {
/* 119 */               try (InputStream es = urlConnection.getErrorStream()) {
/* 120 */                 this.LOGGER.info("Error accessing configuration at {}: {}", this.url, readStream(es));
/* 121 */               } catch (IOException ioe) {
/* 122 */                 this.LOGGER.error("Error accessing configuration at {}: {}", this.url, e.getMessage());
/*     */               } 
/* 124 */               return false;
/*     */             } 
/*     */         } 
/*     */         
/* 128 */         if (code < 0) {
/* 129 */           this.LOGGER.info("Invalid response code returned");
/*     */         } else {
/* 131 */           this.LOGGER.info("Unexpected response code returned {}", Integer.valueOf(code));
/*     */         } 
/* 133 */         return false;
/*     */       
/*     */       }
/* 136 */       catch (IOException ioe) {
/* 137 */         this.LOGGER.error("Error accessing configuration at {}: {}", this.url, ioe.getMessage());
/*     */       } 
/* 139 */     } catch (IOException ioe) {
/* 140 */       this.LOGGER.error("Error connecting to configuration at {}: {}", this.url, ioe.getMessage());
/*     */     } 
/* 142 */     return false;
/*     */   }
/*     */   
/*     */   private byte[] readStream(InputStream is) throws IOException {
/* 146 */     ByteArrayOutputStream result = new ByteArrayOutputStream();
/* 147 */     byte[] buffer = new byte[1024];
/*     */     int length;
/* 149 */     while ((length = is.read(buffer)) != -1) {
/* 150 */       result.write(buffer, 0, length);
/*     */     }
/* 152 */     return result.toByteArray();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\HttpWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */