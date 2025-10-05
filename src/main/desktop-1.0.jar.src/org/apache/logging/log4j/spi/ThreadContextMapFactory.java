/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ProviderUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadContextMapFactory
/*     */ {
/*  49 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
/*     */   private static final String GC_FREE_THREAD_CONTEXT_KEY = "log4j2.garbagefree.threadContextMap";
/*     */   private static boolean GcFreeThreadContextKey;
/*     */   private static String ThreadContextMapName;
/*     */   
/*     */   static {
/*  57 */     initPrivate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void init() {
/*  65 */     CopyOnWriteSortedArrayThreadContextMap.init();
/*  66 */     GarbageFreeSortedArrayThreadContextMap.init();
/*  67 */     DefaultThreadContextMap.init();
/*  68 */     initPrivate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initPrivate() {
/*  76 */     PropertiesUtil properties = PropertiesUtil.getProperties();
/*  77 */     ThreadContextMapName = properties.getStringProperty("log4j2.threadContextMap");
/*  78 */     GcFreeThreadContextKey = properties.getBooleanProperty("log4j2.garbagefree.threadContextMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThreadContextMap createThreadContextMap() {
/*  85 */     ClassLoader cl = ProviderUtil.findClassLoader();
/*  86 */     ThreadContextMap result = null;
/*  87 */     if (ThreadContextMapName != null) {
/*     */       try {
/*  89 */         Class<?> clazz = cl.loadClass(ThreadContextMapName);
/*  90 */         if (ThreadContextMap.class.isAssignableFrom(clazz)) {
/*  91 */           result = (ThreadContextMap)clazz.newInstance();
/*     */         }
/*  93 */       } catch (ClassNotFoundException cnfe) {
/*  94 */         LOGGER.error("Unable to locate configured ThreadContextMap {}", ThreadContextMapName);
/*  95 */       } catch (Exception ex) {
/*  96 */         LOGGER.error("Unable to create configured ThreadContextMap {}", ThreadContextMapName, ex);
/*     */       } 
/*     */     }
/*  99 */     if (result == null && ProviderUtil.hasProviders() && LogManager.getFactory() != null) {
/* 100 */       String factoryClassName = LogManager.getFactory().getClass().getName();
/* 101 */       for (Provider provider : ProviderUtil.getProviders()) {
/* 102 */         if (factoryClassName.equals(provider.getClassName())) {
/* 103 */           Class<? extends ThreadContextMap> clazz = provider.loadThreadContextMap();
/* 104 */           if (clazz != null) {
/*     */             try {
/* 106 */               result = clazz.newInstance();
/*     */               break;
/* 108 */             } catch (Exception e) {
/* 109 */               LOGGER.error("Unable to locate or load configured ThreadContextMap {}", provider
/* 110 */                   .getThreadContextMap(), e);
/* 111 */               result = createDefaultThreadContextMap();
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 117 */     if (result == null) {
/* 118 */       result = createDefaultThreadContextMap();
/*     */     }
/* 120 */     return result;
/*     */   }
/*     */   
/*     */   private static ThreadContextMap createDefaultThreadContextMap() {
/* 124 */     if (Constants.ENABLE_THREADLOCALS) {
/* 125 */       if (GcFreeThreadContextKey) {
/* 126 */         return new GarbageFreeSortedArrayThreadContextMap();
/*     */       }
/* 128 */       return new CopyOnWriteSortedArrayThreadContextMap();
/*     */     } 
/* 130 */     return new DefaultThreadContextMap(true);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\ThreadContextMapFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */