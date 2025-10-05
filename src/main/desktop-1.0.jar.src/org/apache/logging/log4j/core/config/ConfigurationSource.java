/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Objects;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import org.apache.logging.log4j.core.net.UrlConnectionFactory;
/*     */ import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.util.AuthorizationProvider;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Source;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public class ConfigurationSource
/*     */ {
/*  56 */   public static final ConfigurationSource NULL_SOURCE = new ConfigurationSource(Constants.EMPTY_BYTE_ARRAY, null, 0L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static final ConfigurationSource COMPOSITE_SOURCE = new ConfigurationSource(Constants.EMPTY_BYTE_ARRAY, null, 0L);
/*     */   
/*     */   private static final String HTTPS = "https";
/*     */   
/*     */   private final File file;
/*     */   
/*     */   private final URL url;
/*     */   
/*     */   private final String location;
/*     */   
/*     */   private final InputStream stream;
/*     */   
/*     */   private volatile byte[] data;
/*     */   
/*     */   private volatile Source source;
/*     */   
/*     */   private final long lastModified;
/*     */   
/*     */   private volatile long modifiedMillis;
/*     */   
/*     */   public ConfigurationSource(InputStream stream, File file) {
/*  82 */     this.stream = Objects.<InputStream>requireNonNull(stream, "stream is null");
/*  83 */     this.file = Objects.<File>requireNonNull(file, "file is null");
/*  84 */     this.location = file.getAbsolutePath();
/*  85 */     this.url = null;
/*  86 */     this.data = null;
/*  87 */     long modified = 0L;
/*     */     try {
/*  89 */       modified = file.lastModified();
/*  90 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  93 */     this.lastModified = modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream, URL url) {
/* 104 */     this.stream = Objects.<InputStream>requireNonNull(stream, "stream is null");
/* 105 */     this.url = Objects.<URL>requireNonNull(url, "URL is null");
/* 106 */     this.location = url.toString();
/* 107 */     this.file = null;
/* 108 */     this.data = null;
/* 109 */     this.lastModified = 0L;
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
/*     */   public ConfigurationSource(InputStream stream, URL url, long lastModified) {
/* 121 */     this.stream = Objects.<InputStream>requireNonNull(stream, "stream is null");
/* 122 */     this.url = Objects.<URL>requireNonNull(url, "URL is null");
/* 123 */     this.location = url.toString();
/* 124 */     this.file = null;
/* 125 */     this.data = null;
/* 126 */     this.lastModified = lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream) throws IOException {
/* 137 */     this(toByteArray(stream), (URL)null, 0L);
/*     */   }
/*     */   
/*     */   public ConfigurationSource(Source source, byte[] data, long lastModified) throws IOException {
/* 141 */     Objects.requireNonNull(source, "source is null");
/* 142 */     this.data = Objects.<byte[]>requireNonNull(data, "data is null");
/* 143 */     this.stream = new ByteArrayInputStream(data);
/* 144 */     this.file = source.getFile();
/* 145 */     this.url = source.getURI().toURL();
/* 146 */     this.location = source.getLocation();
/* 147 */     this.lastModified = lastModified;
/*     */   }
/*     */   
/*     */   private ConfigurationSource(byte[] data, URL url, long lastModified) {
/* 151 */     this.data = Objects.<byte[]>requireNonNull(data, "data is null");
/* 152 */     this.stream = new ByteArrayInputStream(data);
/* 153 */     this.file = null;
/* 154 */     this.url = url;
/* 155 */     this.location = null;
/* 156 */     this.lastModified = lastModified;
/* 157 */     if (url == null) {
/* 158 */       this.data = data;
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
/*     */   private static byte[] toByteArray(InputStream inputStream) throws IOException {
/* 170 */     int buffSize = Math.max(4096, inputStream.available());
/* 171 */     ByteArrayOutputStream contents = new ByteArrayOutputStream(buffSize);
/* 172 */     byte[] buff = new byte[buffSize];
/*     */     
/* 174 */     int length = inputStream.read(buff);
/* 175 */     while (length > 0) {
/* 176 */       contents.write(buff, 0, length);
/* 177 */       length = inputStream.read(buff);
/*     */     } 
/* 179 */     return contents.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 189 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 199 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setSource(Source source) {
/* 203 */     this.source = source;
/*     */   }
/*     */   
/*     */   public void setData(byte[] data) {
/* 207 */     this.data = data;
/*     */   }
/*     */   
/*     */   public void setModifiedMillis(long modifiedMillis) {
/* 211 */     this.modifiedMillis = modifiedMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 219 */     URI sourceURI = null;
/* 220 */     if (this.url != null) {
/*     */       try {
/* 222 */         sourceURI = this.url.toURI();
/* 223 */       } catch (URISyntaxException uRISyntaxException) {}
/*     */     }
/*     */ 
/*     */     
/* 227 */     if (sourceURI == null && this.file != null) {
/* 228 */       sourceURI = this.file.toURI();
/*     */     }
/* 230 */     if (sourceURI == null && this.location != null) {
/*     */       try {
/* 232 */         sourceURI = new URI(this.location);
/* 233 */       } catch (URISyntaxException ex) {
/*     */         
/*     */         try {
/* 236 */           sourceURI = new URI("file://" + this.location);
/* 237 */         } catch (URISyntaxException uRISyntaxException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 242 */     return sourceURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 250 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 260 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() {
/* 269 */     return this.stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource resetInputStream() throws IOException {
/* 279 */     if (this.source != null)
/* 280 */       return new ConfigurationSource(this.source, this.data, this.lastModified); 
/* 281 */     if (this.file != null)
/* 282 */       return new ConfigurationSource(new FileInputStream(this.file), this.file); 
/* 283 */     if (this.url != null && this.data != null)
/*     */     {
/* 285 */       return new ConfigurationSource(this.data, this.url, (this.modifiedMillis == 0L) ? this.lastModified : this.modifiedMillis); } 
/* 286 */     if (this.url != null)
/* 287 */       return fromUri(getURI()); 
/* 288 */     if (this.data != null) {
/* 289 */       return new ConfigurationSource(this.data, null, this.lastModified);
/*     */     }
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 296 */     if (this.location != null) {
/* 297 */       return this.location;
/*     */     }
/* 299 */     if (this == NULL_SOURCE) {
/* 300 */       return "NULL_SOURCE";
/*     */     }
/* 302 */     int length = (this.data == null) ? -1 : this.data.length;
/* 303 */     return "stream (" + length + " bytes, unknown location)";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurationSource fromUri(URI configLocation) {
/* 312 */     File configFile = FileUtils.fileFromUri(configLocation);
/* 313 */     if (configFile != null && configFile.exists() && configFile.canRead()) {
/*     */       try {
/* 315 */         return new ConfigurationSource(new FileInputStream(configFile), configFile);
/* 316 */       } catch (FileNotFoundException ex) {
/* 317 */         ConfigurationFactory.LOGGER.error("Cannot locate file {}", configLocation.getPath(), ex);
/*     */       } 
/*     */     }
/* 320 */     if (ConfigurationFactory.isClassLoaderUri(configLocation)) {
/* 321 */       ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/* 322 */       String path = ConfigurationFactory.extractClassLoaderUriPath(configLocation);
/* 323 */       return fromResource(path, loader);
/*     */     } 
/* 325 */     if (!configLocation.isAbsolute()) {
/* 326 */       ConfigurationFactory.LOGGER.error("File not found in file system or classpath: {}", configLocation.toString());
/* 327 */       return null;
/*     */     } 
/*     */     try {
/* 330 */       URL url = configLocation.toURL();
/* 331 */       URLConnection urlConnection = UrlConnectionFactory.createConnection(url);
/* 332 */       InputStream is = urlConnection.getInputStream();
/* 333 */       long lastModified = urlConnection.getLastModified();
/* 334 */       return new ConfigurationSource(is, configLocation.toURL(), lastModified);
/* 335 */     } catch (FileNotFoundException ex) {
/* 336 */       ConfigurationFactory.LOGGER.warn("Could not locate file {}", configLocation.toString());
/* 337 */     } catch (MalformedURLException ex) {
/* 338 */       ConfigurationFactory.LOGGER.error("Invalid URL {}", configLocation.toString(), ex);
/* 339 */     } catch (Exception ex) {
/* 340 */       ConfigurationFactory.LOGGER.error("Unable to access {}", configLocation.toString(), ex);
/*     */     } 
/* 342 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurationSource fromResource(String resource, ClassLoader loader) {
/* 352 */     URL url = Loader.getResource(resource, loader);
/* 353 */     if (url == null) {
/* 354 */       return null;
/*     */     }
/* 356 */     return getConfigurationSource(url);
/*     */   }
/*     */   
/*     */   private static ConfigurationSource getConfigurationSource(URL url) {
/*     */     try {
/* 361 */       URLConnection urlConnection = url.openConnection();
/* 362 */       AuthorizationProvider provider = ConfigurationFactory.authorizationProvider(PropertiesUtil.getProperties());
/* 363 */       provider.addAuthorization(urlConnection);
/* 364 */       if (url.getProtocol().equals("https")) {
/* 365 */         SslConfiguration sslConfiguration = SslConfigurationFactory.getSslConfiguration();
/* 366 */         if (sslConfiguration != null) {
/* 367 */           ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslConfiguration.getSslSocketFactory());
/* 368 */           if (!sslConfiguration.isVerifyHostName()) {
/* 369 */             ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
/*     */           }
/*     */         } 
/*     */       } 
/* 373 */       File file = FileUtils.fileFromUri(url.toURI());
/*     */       try {
/* 375 */         if (file != null) {
/* 376 */           return new ConfigurationSource(urlConnection.getInputStream(), FileUtils.fileFromUri(url.toURI()));
/*     */         }
/* 378 */         return new ConfigurationSource(urlConnection.getInputStream(), url, urlConnection.getLastModified());
/*     */       }
/* 380 */       catch (FileNotFoundException ex) {
/* 381 */         ConfigurationFactory.LOGGER.info("Unable to locate file {}, ignoring.", url.toString());
/* 382 */         return null;
/*     */       } 
/* 384 */     } catch (IOException|URISyntaxException ex) {
/* 385 */       ConfigurationFactory.LOGGER.warn("Error accessing {} due to {}, ignoring.", url.toString(), ex
/* 386 */           .getMessage());
/* 387 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ConfigurationSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */