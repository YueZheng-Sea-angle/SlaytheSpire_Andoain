/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ class ThrowableProxyHelper
/*     */ {
/*     */   static final class CacheEntry
/*     */   {
/*     */     private final ExtendedClassInfo element;
/*     */     private final ClassLoader loader;
/*     */     
/*     */     private CacheEntry(ExtendedClassInfo element, ClassLoader loader) {
/*  53 */       this.element = element;
/*  54 */       this.loader = loader;
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
/*     */   static ExtendedStackTraceElement[] toExtendedStackTrace(ThrowableProxy src, Stack<Class<?>> stack, Map<String, CacheEntry> map, StackTraceElement[] rootTrace, StackTraceElement[] stackTrace) {
/*     */     int stackLength;
/*  74 */     if (rootTrace != null) {
/*  75 */       int rootIndex = rootTrace.length - 1;
/*  76 */       int stackIndex = stackTrace.length - 1;
/*  77 */       while (rootIndex >= 0 && stackIndex >= 0 && rootTrace[rootIndex].equals(stackTrace[stackIndex])) {
/*  78 */         rootIndex--;
/*  79 */         stackIndex--;
/*     */       } 
/*  81 */       src.setCommonElementCount(stackTrace.length - 1 - stackIndex);
/*  82 */       stackLength = stackIndex + 1;
/*     */     } else {
/*  84 */       src.setCommonElementCount(0);
/*  85 */       stackLength = stackTrace.length;
/*     */     } 
/*  87 */     ExtendedStackTraceElement[] extStackTrace = new ExtendedStackTraceElement[stackLength];
/*  88 */     Class<?> clazz = stack.isEmpty() ? null : stack.peek();
/*  89 */     ClassLoader lastLoader = null;
/*  90 */     for (int i = stackLength - 1; i >= 0; i--) {
/*  91 */       ExtendedClassInfo extClassInfo; StackTraceElement stackTraceElement = stackTrace[i];
/*  92 */       String className = stackTraceElement.getClassName();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       if (clazz != null && className.equals(clazz.getName())) {
/*  98 */         CacheEntry entry = toCacheEntry(clazz, true);
/*  99 */         extClassInfo = entry.element;
/* 100 */         lastLoader = entry.loader;
/* 101 */         stack.pop();
/* 102 */         clazz = stack.isEmpty() ? null : stack.peek();
/*     */       } else {
/* 104 */         CacheEntry cacheEntry = map.get(className);
/* 105 */         if (cacheEntry != null) {
/* 106 */           CacheEntry entry = cacheEntry;
/* 107 */           extClassInfo = entry.element;
/* 108 */           if (entry.loader != null) {
/* 109 */             lastLoader = entry.loader;
/*     */           }
/*     */         } else {
/* 112 */           CacheEntry entry = toCacheEntry(loadClass(lastLoader, className), false);
/* 113 */           extClassInfo = entry.element;
/* 114 */           map.put(className, entry);
/* 115 */           if (entry.loader != null) {
/* 116 */             lastLoader = entry.loader;
/*     */           }
/*     */         } 
/*     */       } 
/* 120 */       extStackTrace[i] = new ExtendedStackTraceElement(stackTraceElement, extClassInfo);
/*     */     } 
/* 122 */     return extStackTrace;
/*     */   }
/*     */   
/*     */   static ThrowableProxy[] toSuppressedProxies(Throwable thrown, Set<Throwable> suppressedVisited) {
/*     */     try {
/* 127 */       Throwable[] suppressed = thrown.getSuppressed();
/* 128 */       if (suppressed == null || suppressed.length == 0) {
/* 129 */         return ThrowableProxy.EMPTY_ARRAY;
/*     */       }
/* 131 */       List<ThrowableProxy> proxies = new ArrayList<>(suppressed.length);
/* 132 */       if (suppressedVisited == null) {
/* 133 */         suppressedVisited = new HashSet<>(suppressed.length);
/*     */       }
/* 135 */       for (int i = 0; i < suppressed.length; i++) {
/* 136 */         Throwable candidate = suppressed[i];
/* 137 */         if (suppressedVisited.add(candidate)) {
/* 138 */           proxies.add(new ThrowableProxy(candidate, suppressedVisited));
/*     */         }
/*     */       } 
/* 141 */       return proxies.<ThrowableProxy>toArray(new ThrowableProxy[proxies.size()]);
/* 142 */     } catch (Exception e) {
/* 143 */       StatusLogger.getLogger().error(e);
/*     */       
/* 145 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CacheEntry toCacheEntry(Class<?> callerClass, boolean exact) {
/* 156 */     String location = "?";
/* 157 */     String version = "?";
/* 158 */     ClassLoader lastLoader = null;
/* 159 */     if (callerClass != null) {
/*     */       try {
/* 161 */         CodeSource source = callerClass.getProtectionDomain().getCodeSource();
/* 162 */         if (source != null) {
/* 163 */           URL locationURL = source.getLocation();
/* 164 */           if (locationURL != null) {
/* 165 */             String str = locationURL.toString().replace('\\', '/');
/* 166 */             int index = str.lastIndexOf("/");
/* 167 */             if (index >= 0 && index == str.length() - 1) {
/* 168 */               index = str.lastIndexOf("/", index - 1);
/*     */             }
/* 170 */             location = str.substring(index + 1);
/*     */           } 
/*     */         } 
/* 173 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 176 */       Package pkg = callerClass.getPackage();
/* 177 */       if (pkg != null) {
/* 178 */         String ver = pkg.getImplementationVersion();
/* 179 */         if (ver != null) {
/* 180 */           version = ver;
/*     */         }
/*     */       } 
/*     */       try {
/* 184 */         lastLoader = callerClass.getClassLoader();
/* 185 */       } catch (SecurityException e) {
/* 186 */         lastLoader = null;
/*     */       } 
/*     */     } 
/* 189 */     return new CacheEntry(new ExtendedClassInfo(exact, location, version), lastLoader);
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
/*     */   private static Class<?> loadClass(ClassLoader lastLoader, String className) {
/*     */     Class<?> clazz;
/* 203 */     if (lastLoader != null) {
/*     */       try {
/* 205 */         clazz = lastLoader.loadClass(className);
/* 206 */         if (clazz != null) {
/* 207 */           return clazz;
/*     */         }
/* 209 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 214 */       clazz = LoaderUtil.loadClass(className);
/* 215 */     } catch (ClassNotFoundException|NoClassDefFoundError e) {
/* 216 */       return loadClass(className);
/* 217 */     } catch (SecurityException e) {
/* 218 */       return null;
/*     */     } 
/* 220 */     return clazz;
/*     */   }
/*     */   
/*     */   private static Class<?> loadClass(String className) {
/*     */     try {
/* 225 */       return Loader.loadClass(className, ThrowableProxyHelper.class.getClassLoader());
/* 226 */     } catch (ClassNotFoundException|NoClassDefFoundError|SecurityException e) {
/* 227 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ThrowableProxyHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */