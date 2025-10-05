/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ public final class LoaderUtil
/*     */ {
/*     */   public static final String IGNORE_TCCL_PROPERTY = "log4j.ignoreTCL";
/*  46 */   private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
/*     */ 
/*     */   
/*     */   private static Boolean ignoreTCCL;
/*     */ 
/*     */   
/*     */   private static final boolean GET_CLASS_LOADER_DISABLED;
/*     */   
/*  54 */   private static final PrivilegedAction<ClassLoader> TCCL_GETTER = new ThreadContextClassLoaderGetter();
/*     */   
/*     */   static {
/*  57 */     if (SECURITY_MANAGER != null) {
/*     */       boolean getClassLoaderDisabled;
/*     */       try {
/*  60 */         SECURITY_MANAGER.checkPermission(new RuntimePermission("getClassLoader"));
/*  61 */         getClassLoaderDisabled = false;
/*  62 */       } catch (SecurityException ignored) {
/*  63 */         getClassLoaderDisabled = true;
/*     */       } 
/*  65 */       GET_CLASS_LOADER_DISABLED = getClassLoaderDisabled;
/*     */     } else {
/*  67 */       GET_CLASS_LOADER_DISABLED = false;
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
/*     */   public static ClassLoader getThreadContextClassLoader() {
/*  83 */     if (GET_CLASS_LOADER_DISABLED)
/*     */     {
/*     */       
/*  86 */       return LoaderUtil.class.getClassLoader();
/*     */     }
/*  88 */     return (SECURITY_MANAGER == null) ? TCCL_GETTER.run() : AccessController.<ClassLoader>doPrivileged(TCCL_GETTER);
/*     */   }
/*     */   
/*     */   private static class ThreadContextClassLoaderGetter
/*     */     implements PrivilegedAction<ClassLoader>
/*     */   {
/*     */     private ThreadContextClassLoaderGetter() {}
/*     */     
/*     */     public ClassLoader run() {
/*  97 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  98 */       if (cl != null) {
/*  99 */         return cl;
/*     */       }
/* 101 */       ClassLoader ccl = LoaderUtil.class.getClassLoader();
/* 102 */       return (ccl == null && !LoaderUtil.GET_CLASS_LOADER_DISABLED) ? ClassLoader.getSystemClassLoader() : ccl;
/*     */     }
/*     */   }
/*     */   
/*     */   public static ClassLoader[] getClassLoaders() {
/* 107 */     Collection<ClassLoader> classLoaders = new LinkedHashSet<>();
/* 108 */     ClassLoader tcl = getThreadContextClassLoader();
/* 109 */     if (tcl != null) {
/* 110 */       classLoaders.add(tcl);
/*     */     }
/* 112 */     accumulateClassLoaders(LoaderUtil.class.getClassLoader(), classLoaders);
/* 113 */     accumulateClassLoaders((tcl == null) ? null : tcl.getParent(), classLoaders);
/* 114 */     ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
/* 115 */     if (systemClassLoader != null) {
/* 116 */       classLoaders.add(systemClassLoader);
/*     */     }
/* 118 */     return classLoaders.<ClassLoader>toArray(new ClassLoader[classLoaders.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void accumulateClassLoaders(ClassLoader loader, Collection<ClassLoader> loaders) {
/* 127 */     if (loader != null && loaders.add(loader)) {
/* 128 */       accumulateClassLoaders(loader.getParent(), loaders);
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
/*     */   public static boolean isClassAvailable(String className) {
/*     */     try {
/* 141 */       Class<?> clazz = loadClass(className);
/* 142 */       return (clazz != null);
/* 143 */     } catch (ClassNotFoundException|LinkageError e) {
/* 144 */       return false;
/* 145 */     } catch (Throwable e) {
/* 146 */       LowLevelLogUtil.logException("Unknown error checking for existence of class: " + className, e);
/* 147 */       return false;
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
/*     */   public static Class<?> loadClass(String className) throws ClassNotFoundException {
/* 161 */     if (isIgnoreTccl()) {
/* 162 */       return Class.forName(className);
/*     */     }
/*     */     try {
/* 165 */       ClassLoader tccl = getThreadContextClassLoader();
/* 166 */       if (tccl != null) {
/* 167 */         return tccl.loadClass(className);
/*     */       }
/* 169 */     } catch (Throwable throwable) {}
/*     */     
/* 171 */     return Class.forName(className);
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
/*     */   public static <T> T newInstanceOf(Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
/*     */     try {
/* 187 */       return clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 188 */     } catch (NoSuchMethodException ignored) {
/*     */       
/* 190 */       return clazz.newInstance();
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
/*     */   public static <T> T newInstanceOf(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
/* 209 */     return newInstanceOf((Class)loadClass(className));
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
/*     */   public static <T> T newCheckedInstanceOf(String className, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
/* 230 */     return clazz.cast(newInstanceOf(className));
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
/*     */   public static <T> T newCheckedInstanceOfProperty(String propertyName, Class<T> clazz) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
/* 251 */     String className = PropertiesUtil.getProperties().getStringProperty(propertyName);
/* 252 */     if (className == null) {
/* 253 */       return null;
/*     */     }
/* 255 */     return newCheckedInstanceOf(className, clazz);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isIgnoreTccl() {
/* 260 */     if (ignoreTCCL == null) {
/* 261 */       String ignoreTccl = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", null);
/* 262 */       ignoreTCCL = Boolean.valueOf((ignoreTccl != null && !"false".equalsIgnoreCase(ignoreTccl.trim())));
/*     */     } 
/* 264 */     return ignoreTCCL.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<URL> findResources(String resource) {
/* 275 */     Collection<UrlResource> urlResources = findUrlResources(resource);
/* 276 */     Collection<URL> resources = new LinkedHashSet<>(urlResources.size());
/* 277 */     for (UrlResource urlResource : urlResources) {
/* 278 */       resources.add(urlResource.getUrl());
/*     */     }
/* 280 */     return resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Collection<UrlResource> findUrlResources(String resource) {
/* 288 */     ClassLoader[] candidates = { getThreadContextClassLoader(), LoaderUtil.class.getClassLoader(), GET_CLASS_LOADER_DISABLED ? null : ClassLoader.getSystemClassLoader() };
/*     */     
/* 290 */     Collection<UrlResource> resources = new LinkedHashSet<>();
/* 291 */     for (ClassLoader cl : candidates) {
/* 292 */       if (cl != null) {
/*     */         try {
/* 294 */           Enumeration<URL> resourceEnum = cl.getResources(resource);
/* 295 */           while (resourceEnum.hasMoreElements()) {
/* 296 */             resources.add(new UrlResource(cl, resourceEnum.nextElement()));
/*     */           }
/* 298 */         } catch (IOException e) {
/* 299 */           LowLevelLogUtil.logException(e);
/*     */         } 
/*     */       }
/*     */     } 
/* 303 */     return resources;
/*     */   }
/*     */ 
/*     */   
/*     */   static class UrlResource
/*     */   {
/*     */     private final ClassLoader classLoader;
/*     */     
/*     */     private final URL url;
/*     */     
/*     */     UrlResource(ClassLoader classLoader, URL url) {
/* 314 */       this.classLoader = classLoader;
/* 315 */       this.url = url;
/*     */     }
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 319 */       return this.classLoader;
/*     */     }
/*     */     
/*     */     public URL getUrl() {
/* 323 */       return this.url;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 328 */       if (this == o) {
/* 329 */         return true;
/*     */       }
/* 331 */       if (o == null || getClass() != o.getClass()) {
/* 332 */         return false;
/*     */       }
/*     */       
/* 335 */       UrlResource that = (UrlResource)o;
/*     */       
/* 337 */       if ((this.classLoader != null) ? !this.classLoader.equals(that.classLoader) : (that.classLoader != null)) {
/* 338 */         return false;
/*     */       }
/* 340 */       if ((this.url != null) ? !this.url.equals(that.url) : (that.url != null)) {
/* 341 */         return false;
/*     */       }
/*     */       
/* 344 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 349 */       return Objects.hashCode(this.classLoader) + Objects.hashCode(this.url);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\LoaderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */