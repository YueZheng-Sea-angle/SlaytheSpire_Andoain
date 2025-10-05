/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggerRegistry<T extends ExtendedLogger>
/*     */ {
/*  33 */   private static final String DEFAULT_FACTORY_KEY = AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName();
/*     */   
/*     */   private final MapFactory<T> factory;
/*     */   
/*     */   private final Map<String, Map<String, T>> map;
/*     */ 
/*     */   
/*     */   public static interface MapFactory<T extends ExtendedLogger>
/*     */   {
/*     */     Map<String, T> createInnerMap();
/*     */ 
/*     */     
/*     */     Map<String, Map<String, T>> createOuterMap();
/*     */ 
/*     */     
/*     */     void putIfAbsent(Map<String, T> param1Map, String param1String, T param1T);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ConcurrentMapFactory<T extends ExtendedLogger>
/*     */     implements MapFactory<T>
/*     */   {
/*     */     public Map<String, T> createInnerMap() {
/*  56 */       return new ConcurrentHashMap<>();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, Map<String, T>> createOuterMap() {
/*  61 */       return new ConcurrentHashMap<>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putIfAbsent(Map<String, T> innerMap, String name, T logger) {
/*  66 */       ((ConcurrentMap<String, T>)innerMap).putIfAbsent(name, logger);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class WeakMapFactory<T extends ExtendedLogger>
/*     */     implements MapFactory<T>
/*     */   {
/*     */     public Map<String, T> createInnerMap() {
/*  77 */       return new WeakHashMap<>();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, Map<String, T>> createOuterMap() {
/*  82 */       return new WeakHashMap<>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putIfAbsent(Map<String, T> innerMap, String name, T logger) {
/*  87 */       innerMap.put(name, logger);
/*     */     }
/*     */   }
/*     */   
/*     */   public LoggerRegistry() {
/*  92 */     this(new ConcurrentMapFactory<>());
/*     */   }
/*     */   
/*     */   public LoggerRegistry(MapFactory<T> factory) {
/*  96 */     this.factory = Objects.<MapFactory<T>>requireNonNull(factory, "factory");
/*  97 */     this.map = factory.createOuterMap();
/*     */   }
/*     */   
/*     */   private static String factoryClassKey(Class<? extends MessageFactory> messageFactoryClass) {
/* 101 */     return (messageFactoryClass == null) ? DEFAULT_FACTORY_KEY : messageFactoryClass.getName();
/*     */   }
/*     */   
/*     */   private static String factoryKey(MessageFactory messageFactory) {
/* 105 */     return (messageFactory == null) ? DEFAULT_FACTORY_KEY : messageFactory.getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getLogger(String name) {
/* 114 */     return getOrCreateInnerMap(DEFAULT_FACTORY_KEY).get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getLogger(String name, MessageFactory messageFactory) {
/* 125 */     return getOrCreateInnerMap(factoryKey(messageFactory)).get(name);
/*     */   }
/*     */   
/*     */   public Collection<T> getLoggers() {
/* 129 */     return getLoggers(new ArrayList<>());
/*     */   }
/*     */   
/*     */   public Collection<T> getLoggers(Collection<T> destination) {
/* 133 */     for (Map<String, T> inner : this.map.values()) {
/* 134 */       destination.addAll(inner.values());
/*     */     }
/* 136 */     return destination;
/*     */   }
/*     */   
/*     */   private Map<String, T> getOrCreateInnerMap(String factoryName) {
/* 140 */     Map<String, T> inner = this.map.get(factoryName);
/* 141 */     if (inner == null) {
/* 142 */       inner = this.factory.createInnerMap();
/* 143 */       this.map.put(factoryName, inner);
/*     */     } 
/* 145 */     return inner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name) {
/* 154 */     return getOrCreateInnerMap(DEFAULT_FACTORY_KEY).containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, MessageFactory messageFactory) {
/* 165 */     return getOrCreateInnerMap(factoryKey(messageFactory)).containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
/* 176 */     return getOrCreateInnerMap(factoryClassKey(messageFactoryClass)).containsKey(name);
/*     */   }
/*     */   
/*     */   public void putIfAbsent(String name, MessageFactory messageFactory, T logger) {
/* 180 */     this.factory.putIfAbsent(getOrCreateInnerMap(factoryKey(messageFactory)), name, logger);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\LoggerRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */