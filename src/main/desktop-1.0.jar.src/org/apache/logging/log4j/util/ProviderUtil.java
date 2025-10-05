/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Properties;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.Provider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ProviderUtil
/*     */ {
/*     */   protected static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
/*  48 */   protected static final Collection<Provider> PROVIDERS = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected static final Lock STARTUP_LOCK = new ReentrantLock();
/*     */   
/*     */   private static final String API_VERSION = "Log4jAPIVersion";
/*  58 */   private static final String[] COMPATIBLE_API_VERSIONS = new String[] { "2.6.0" };
/*  59 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static volatile ProviderUtil instance;
/*     */ 
/*     */   
/*     */   private ProviderUtil() {
/*  66 */     for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
/*     */       try {
/*  68 */         loadProviders(classLoader);
/*  69 */       } catch (Throwable ex) {
/*  70 */         LOGGER.debug("Unable to retrieve provider from ClassLoader {}", classLoader, ex);
/*     */       } 
/*     */     } 
/*  73 */     for (LoaderUtil.UrlResource resource : LoaderUtil.findUrlResources("META-INF/log4j-provider.properties")) {
/*  74 */       loadProvider(resource.getUrl(), resource.getClassLoader());
/*     */     }
/*     */   }
/*     */   
/*     */   protected static void addProvider(Provider provider) {
/*  79 */     PROVIDERS.add(provider);
/*  80 */     LOGGER.debug("Loaded Provider {}", provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void loadProvider(URL url, ClassLoader cl) {
/*     */     try {
/*  92 */       Properties props = PropertiesUtil.loadClose(url.openStream(), url);
/*  93 */       if (validVersion(props.getProperty("Log4jAPIVersion"))) {
/*  94 */         Provider provider = new Provider(props, url, cl);
/*  95 */         PROVIDERS.add(provider);
/*  96 */         LOGGER.debug("Loaded Provider {}", provider);
/*     */       } 
/*  98 */     } catch (IOException e) {
/*  99 */       LOGGER.error("Unable to open {}", url, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void loadProviders(ClassLoader classLoader) {
/* 108 */     ServiceLoader<Provider> serviceLoader = ServiceLoader.load(Provider.class, classLoader);
/* 109 */     for (Provider provider : serviceLoader) {
/* 110 */       if (validVersion(provider.getVersions()) && !PROVIDERS.contains(provider)) {
/* 111 */         PROVIDERS.add(provider);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected static void loadProviders(Enumeration<URL> urls, ClassLoader cl) {
/* 121 */     if (urls != null) {
/* 122 */       while (urls.hasMoreElements()) {
/* 123 */         loadProvider(urls.nextElement(), cl);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Iterable<Provider> getProviders() {
/* 129 */     lazyInit();
/* 130 */     return PROVIDERS;
/*     */   }
/*     */   
/*     */   public static boolean hasProviders() {
/* 134 */     lazyInit();
/* 135 */     return !PROVIDERS.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void lazyInit() {
/* 145 */     if (instance == null) {
/*     */       try {
/* 147 */         STARTUP_LOCK.lockInterruptibly();
/*     */         try {
/* 149 */           if (instance == null) {
/* 150 */             instance = new ProviderUtil();
/*     */           }
/*     */         } finally {
/* 153 */           STARTUP_LOCK.unlock();
/*     */         } 
/* 155 */       } catch (InterruptedException e) {
/* 156 */         LOGGER.fatal("Interrupted before Log4j Providers could be loaded.", e);
/* 157 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static ClassLoader findClassLoader() {
/* 163 */     return LoaderUtil.getThreadContextClassLoader();
/*     */   }
/*     */   
/*     */   private static boolean validVersion(String version) {
/* 167 */     for (String v : COMPATIBLE_API_VERSIONS) {
/* 168 */       if (version.startsWith(v)) {
/* 169 */         return true;
/*     */       }
/*     */     } 
/* 172 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\ProviderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */