/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public final class Loader
/*     */ {
/*  33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoader() {
/*  45 */     return getClassLoader(Loader.class, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getThreadContextClassLoader() {
/*  56 */     return LoaderUtil.getThreadContextClassLoader();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ClassLoader getClassLoader(Class<?> class1, Class<?> class2) {
/*  61 */     ClassLoader threadContextClassLoader = getThreadContextClassLoader();
/*  62 */     ClassLoader loader1 = (class1 == null) ? null : class1.getClassLoader();
/*  63 */     ClassLoader loader2 = (class2 == null) ? null : class2.getClassLoader();
/*     */     
/*  65 */     if (isChild(threadContextClassLoader, loader1)) {
/*  66 */       return isChild(threadContextClassLoader, loader2) ? threadContextClassLoader : loader2;
/*     */     }
/*  68 */     return isChild(loader1, loader2) ? loader1 : loader2;
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
/*     */   public static URL getResource(String resource, ClassLoader defaultLoader) {
/*     */     try {
/*  93 */       ClassLoader classLoader = getThreadContextClassLoader();
/*  94 */       if (classLoader != null) {
/*  95 */         LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
/*  96 */         URL url = classLoader.getResource(resource);
/*  97 */         if (url != null) {
/*  98 */           return url;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 103 */       classLoader = Loader.class.getClassLoader();
/* 104 */       if (classLoader != null) {
/* 105 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
/* 106 */         URL url = classLoader.getResource(resource);
/* 107 */         if (url != null) {
/* 108 */           return url;
/*     */         }
/*     */       } 
/*     */       
/* 112 */       if (defaultLoader != null) {
/* 113 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
/* 114 */         URL url = defaultLoader.getResource(resource);
/* 115 */         if (url != null) {
/* 116 */           return url;
/*     */         }
/*     */       } 
/* 119 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 123 */       LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
/* 131 */     return ClassLoader.getSystemResource(resource);
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
/*     */   public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
/*     */     try {
/* 155 */       ClassLoader classLoader = getThreadContextClassLoader();
/*     */       
/* 157 */       if (classLoader != null) {
/* 158 */         LOGGER.trace("Trying to find [{}] using context class loader {}.", resource, classLoader);
/* 159 */         InputStream is = classLoader.getResourceAsStream(resource);
/* 160 */         if (is != null) {
/* 161 */           return is;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 166 */       classLoader = Loader.class.getClassLoader();
/* 167 */       if (classLoader != null) {
/* 168 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, classLoader);
/* 169 */         InputStream is = classLoader.getResourceAsStream(resource);
/* 170 */         if (is != null) {
/* 171 */           return is;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 176 */       if (defaultLoader != null) {
/* 177 */         LOGGER.trace("Trying to find [{}] using {} class loader.", resource, defaultLoader);
/* 178 */         InputStream is = defaultLoader.getResourceAsStream(resource);
/* 179 */         if (is != null) {
/* 180 */           return is;
/*     */         }
/*     */       } 
/* 183 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */       
/* 187 */       LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     LOGGER.trace("Trying to find [{}] using ClassLoader.getSystemResource().", resource);
/* 195 */     return ClassLoader.getSystemResourceAsStream(resource);
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
/*     */   private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
/* 207 */     if (loader1 != null && loader2 != null) {
/* 208 */       ClassLoader parent = loader1.getParent();
/* 209 */       while (parent != null && parent != loader2) {
/* 210 */         parent = parent.getParent();
/*     */       }
/*     */       
/* 213 */       return (parent != null);
/*     */     } 
/* 215 */     return (loader1 != null);
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
/*     */   public static Class<?> initializeClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 228 */     return Class.forName(className, true, loader);
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
/*     */   public static Class<?> loadClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 241 */     return (loader != null) ? loader.loadClass(className) : null;
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
/*     */   public static Class<?> loadSystemClass(String className) throws ClassNotFoundException {
/*     */     try {
/* 254 */       return Class.forName(className, true, ClassLoader.getSystemClassLoader());
/* 255 */     } catch (Throwable t) {
/* 256 */       LOGGER.trace("Couldn't use SystemClassLoader. Trying Class.forName({}).", className, t);
/* 257 */       return Class.forName(className);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
/* 279 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 281 */       Thread.currentThread().setContextClassLoader(getClassLoader());
/* 282 */       return (T)LoaderUtil.newInstanceOf(className);
/*     */     } finally {
/* 284 */       Thread.currentThread().setContextClassLoader(contextClassLoader);
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
/*     */   public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 308 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 310 */       Thread.currentThread().setContextClassLoader(getClassLoader());
/* 311 */       return (T)LoaderUtil.newCheckedInstanceOf(className, clazz);
/*     */     } finally {
/* 313 */       Thread.currentThread().setContextClassLoader(contextClassLoader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
/* 334 */     String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
/* 335 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 337 */       Thread.currentThread().setContextClassLoader(getClassLoader());
/* 338 */       return (T)LoaderUtil.newCheckedInstanceOfProperty(propertyName, clazz);
/*     */     } finally {
/* 340 */       Thread.currentThread().setContextClassLoader(contextClassLoader);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isClassAvailable(String className) {
/* 351 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 353 */       Thread.currentThread().setContextClassLoader(getClassLoader());
/* 354 */       return LoaderUtil.isClassAvailable(className);
/*     */     } finally {
/* 356 */       Thread.currentThread().setContextClassLoader(contextClassLoader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isJansiAvailable() {
/* 361 */     return isClassAvailable("org.fusesource.jansi.AnsiRenderer");
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
/*     */   public static Class<?> loadClass(String className) throws ClassNotFoundException {
/* 374 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     try {
/* 376 */       Thread.currentThread().setContextClassLoader(getClassLoader());
/* 377 */       return LoaderUtil.loadClass(className);
/*     */     } finally {
/* 379 */       Thread.currentThread().setContextClassLoader(contextClassLoader);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Loader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */