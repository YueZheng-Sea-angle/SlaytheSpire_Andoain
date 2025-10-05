/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.ContextDataProvider;
/*     */ import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ public class ThreadContextDataInjector
/*     */ {
/*  57 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static Collection<ContextDataProvider> contextDataProviders = new ConcurrentLinkedDeque<>();
/*     */ 
/*     */   
/*  65 */   private static volatile List<ContextDataProvider> serviceProviders = null;
/*  66 */   private static final Lock providerLock = new ReentrantLock();
/*     */   
/*     */   public static void initServiceProviders() {
/*  69 */     if (serviceProviders == null) {
/*  70 */       providerLock.lock();
/*     */       try {
/*  72 */         if (serviceProviders == null) {
/*  73 */           serviceProviders = getServiceProviders();
/*     */         }
/*     */       } finally {
/*  76 */         providerLock.unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<ContextDataProvider> getServiceProviders() {
/*  82 */     List<ContextDataProvider> providers = new ArrayList<>();
/*  83 */     for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
/*     */       try {
/*  85 */         for (ContextDataProvider provider : ServiceLoader.<ContextDataProvider>load(ContextDataProvider.class, classLoader)) {
/*  86 */           if (providers.stream().noneMatch(p -> p.getClass().isAssignableFrom(provider.getClass()))) {
/*  87 */             providers.add(provider);
/*     */           }
/*     */         } 
/*  90 */       } catch (Throwable ex) {
/*  91 */         LOGGER.debug("Unable to access Context Data Providers {}", ex.getMessage());
/*     */       } 
/*     */     } 
/*  94 */     return providers;
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
/*     */   public static class ForDefaultThreadContextMap
/*     */     implements ContextDataInjector
/*     */   {
/* 108 */     private final List<ContextDataProvider> providers = ThreadContextDataInjector.getProviders();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringMap injectContextData(List<Property> props, StringMap contextData) {
/*     */       Map<String, String> copy;
/* 124 */       if (this.providers.size() == 1) {
/* 125 */         copy = ((ContextDataProvider)this.providers.get(0)).supplyContextData();
/*     */       } else {
/* 127 */         copy = new HashMap<>();
/* 128 */         for (ContextDataProvider provider : this.providers) {
/* 129 */           copy.putAll(provider.supplyContextData());
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 137 */       if (props == null || props.isEmpty())
/*     */       {
/*     */         
/* 140 */         return copy.isEmpty() ? ContextDataFactory.emptyFrozenContextData() : frozenStringMap(copy);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       StringMap result = new JdkMapAdapterStringMap(new HashMap<>(copy));
/* 147 */       for (int i = 0; i < props.size(); i++) {
/* 148 */         Property prop = props.get(i);
/* 149 */         if (!copy.containsKey(prop.getName())) {
/* 150 */           result.putValue(prop.getName(), prop.getValue());
/*     */         }
/*     */       } 
/* 153 */       result.freeze();
/* 154 */       return result;
/*     */     }
/*     */     
/*     */     private static JdkMapAdapterStringMap frozenStringMap(Map<String, String> copy) {
/* 158 */       JdkMapAdapterStringMap result = new JdkMapAdapterStringMap(copy);
/* 159 */       result.freeze();
/* 160 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap rawContextData() {
/* 165 */       ReadOnlyThreadContextMap map = ThreadContext.getThreadContextMap();
/* 166 */       if (map instanceof ReadOnlyStringMap) {
/* 167 */         return (ReadOnlyStringMap)map;
/*     */       }
/*     */       
/* 170 */       Map<String, String> copy = ThreadContext.getImmutableContext();
/* 171 */       return copy.isEmpty() ? (ReadOnlyStringMap)ContextDataFactory.emptyFrozenContextData() : (ReadOnlyStringMap)new JdkMapAdapterStringMap(copy);
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
/*     */   public static class ForGarbageFreeThreadContextMap
/*     */     implements ContextDataInjector
/*     */   {
/* 185 */     private final List<ContextDataProvider> providers = ThreadContextDataInjector.getProviders();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringMap injectContextData(List<Property> props, StringMap reusable) {
/* 201 */       ThreadContextDataInjector.copyProperties(props, reusable);
/* 202 */       for (int i = 0; i < this.providers.size(); i++) {
/* 203 */         reusable.putAll((ReadOnlyStringMap)((ContextDataProvider)this.providers.get(i)).supplyStringMap());
/*     */       }
/* 205 */       return reusable;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap rawContextData() {
/* 210 */       return (ReadOnlyStringMap)ThreadContext.getThreadContextMap().getReadOnlyContextData();
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
/*     */   public static class ForCopyOnWriteThreadContextMap
/*     */     implements ContextDataInjector
/*     */   {
/* 226 */     private final List<ContextDataProvider> providers = ThreadContextDataInjector.getProviders();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StringMap injectContextData(List<Property> props, StringMap ignore) {
/* 241 */       if (this.providers.size() == 1 && (props == null || props.isEmpty()))
/*     */       {
/* 243 */         return ((ContextDataProvider)this.providers.get(0)).supplyStringMap();
/*     */       }
/* 245 */       int count = (props == null) ? 0 : props.size();
/* 246 */       StringMap[] maps = new StringMap[this.providers.size()];
/* 247 */       for (int i = 0; i < this.providers.size(); i++) {
/* 248 */         maps[i] = ((ContextDataProvider)this.providers.get(i)).supplyStringMap();
/* 249 */         count += maps[i].size();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       StringMap result = ContextDataFactory.createContextData(count);
/* 256 */       ThreadContextDataInjector.copyProperties(props, result);
/* 257 */       for (StringMap map : maps) {
/* 258 */         result.putAll((ReadOnlyStringMap)map);
/*     */       }
/* 260 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap rawContextData() {
/* 265 */       return (ReadOnlyStringMap)ThreadContext.getThreadContextMap().getReadOnlyContextData();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyProperties(List<Property> properties, StringMap result) {
/* 276 */     if (properties != null) {
/* 277 */       for (int i = 0; i < properties.size(); i++) {
/* 278 */         Property prop = properties.get(i);
/* 279 */         result.putValue(prop.getName(), prop.getValue());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static List<ContextDataProvider> getProviders() {
/* 285 */     initServiceProviders();
/* 286 */     List<ContextDataProvider> providers = new ArrayList<>(contextDataProviders);
/* 287 */     if (serviceProviders != null) {
/* 288 */       providers.addAll(serviceProviders);
/*     */     }
/* 290 */     return providers;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ThreadContextDataInjector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */