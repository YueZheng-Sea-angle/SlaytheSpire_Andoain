/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.util.IndexedStringMap;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
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
/*     */ public class ContextDataFactory
/*     */ {
/*  49 */   private static final String CLASS_NAME = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextData");
/*  50 */   private static final Class<? extends StringMap> CACHED_CLASS = createCachedClass(CLASS_NAME);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private static final Constructor<?> DEFAULT_CONSTRUCTOR = createDefaultConstructor(CACHED_CLASS);
/*  59 */   private static final Constructor<?> INITIAL_CAPACITY_CONSTRUCTOR = createInitialCapacityConstructor(CACHED_CLASS);
/*     */   
/*  61 */   private static final StringMap EMPTY_STRING_MAP = createContextData(0);
/*     */   
/*     */   static {
/*  64 */     EMPTY_STRING_MAP.freeze();
/*     */   }
/*     */   
/*     */   private static Class<? extends StringMap> createCachedClass(String className) {
/*  68 */     if (className == null) {
/*  69 */       return null;
/*     */     }
/*     */     try {
/*  72 */       return Loader.loadClass(className).asSubclass(IndexedStringMap.class);
/*  73 */     } catch (Exception any) {
/*  74 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<?> createDefaultConstructor(Class<? extends StringMap> cachedClass) {
/*  79 */     if (cachedClass == null) {
/*  80 */       return null;
/*     */     }
/*     */     try {
/*  83 */       return cachedClass.getConstructor(new Class[0]);
/*  84 */     } catch (NoSuchMethodException|IllegalAccessError ignored) {
/*  85 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<?> createInitialCapacityConstructor(Class<? extends StringMap> cachedClass) {
/*  90 */     if (cachedClass == null) {
/*  91 */       return null;
/*     */     }
/*     */     try {
/*  94 */       return cachedClass.getConstructor(new Class[] { int.class });
/*  95 */     } catch (NoSuchMethodException|IllegalAccessError ignored) {
/*  96 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static StringMap createContextData() {
/* 101 */     if (DEFAULT_CONSTRUCTOR == null) {
/* 102 */       return (StringMap)new SortedArrayStringMap();
/*     */     }
/*     */     try {
/* 105 */       return (StringMap)DEFAULT_CONSTRUCTOR.newInstance(new Object[0]);
/* 106 */     } catch (Throwable ignored) {
/* 107 */       return (StringMap)new SortedArrayStringMap();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static StringMap createContextData(int initialCapacity) {
/* 112 */     if (INITIAL_CAPACITY_CONSTRUCTOR == null) {
/* 113 */       return (StringMap)new SortedArrayStringMap(initialCapacity);
/*     */     }
/*     */     try {
/* 116 */       return (StringMap)INITIAL_CAPACITY_CONSTRUCTOR.newInstance(new Object[] { Integer.valueOf(initialCapacity) });
/* 117 */     } catch (Throwable ignored) {
/* 118 */       return (StringMap)new SortedArrayStringMap(initialCapacity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static StringMap createContextData(Map<String, String> context) {
/* 123 */     StringMap contextData = createContextData(context.size());
/* 124 */     for (Map.Entry<String, String> entry : context.entrySet()) {
/* 125 */       contextData.putValue(entry.getKey(), entry.getValue());
/*     */     }
/* 127 */     return contextData;
/*     */   }
/*     */   
/*     */   public static StringMap createContextData(ReadOnlyStringMap readOnlyStringMap) {
/* 131 */     StringMap contextData = createContextData(readOnlyStringMap.size());
/* 132 */     contextData.putAll(readOnlyStringMap);
/* 133 */     return contextData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringMap emptyFrozenContextData() {
/* 142 */     return EMPTY_STRING_MAP;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ContextDataFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */