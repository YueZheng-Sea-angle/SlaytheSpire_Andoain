/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.processor.PluginCache;
/*     */ import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginRegistry
/*     */ {
/*  49 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static volatile PluginRegistry INSTANCE;
/*  52 */   private static final Object INSTANCE_LOCK = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final AtomicReference<Map<String, List<PluginType<?>>>> pluginsByCategoryRef = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final ConcurrentMap<Long, Map<String, List<PluginType<?>>>> pluginsByCategoryByBundleId = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final ConcurrentMap<String, Map<String, List<PluginType<?>>>> pluginsByCategoryByPackage = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PluginRegistry getInstance() {
/*  82 */     PluginRegistry result = INSTANCE;
/*  83 */     if (result == null) {
/*  84 */       synchronized (INSTANCE_LOCK) {
/*  85 */         result = INSTANCE;
/*  86 */         if (result == null) {
/*  87 */           INSTANCE = result = new PluginRegistry();
/*     */         }
/*     */       } 
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  98 */     this.pluginsByCategoryRef.set(null);
/*  99 */     this.pluginsByCategoryByPackage.clear();
/* 100 */     this.pluginsByCategoryByBundleId.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Long, Map<String, List<PluginType<?>>>> getPluginsByCategoryByBundleId() {
/* 107 */     return this.pluginsByCategoryByBundleId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<PluginType<?>>> loadFromMainClassLoader() {
/* 114 */     Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryRef.get();
/* 115 */     if (existing != null)
/*     */     {
/* 117 */       return existing;
/*     */     }
/* 119 */     Map<String, List<PluginType<?>>> newPluginsByCategory = decodeCacheFiles(Loader.getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (this.pluginsByCategoryRef.compareAndSet(null, newPluginsByCategory)) {
/* 125 */       return newPluginsByCategory;
/*     */     }
/* 127 */     return this.pluginsByCategoryRef.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBundlePlugins(long bundleId) {
/* 134 */     this.pluginsByCategoryByBundleId.remove(Long.valueOf(bundleId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<PluginType<?>>> loadFromBundle(long bundleId, ClassLoader loader) {
/* 141 */     Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryByBundleId.get(Long.valueOf(bundleId));
/* 142 */     if (existing != null)
/*     */     {
/* 144 */       return existing;
/*     */     }
/* 146 */     Map<String, List<PluginType<?>>> newPluginsByCategory = decodeCacheFiles(loader);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     existing = this.pluginsByCategoryByBundleId.putIfAbsent(Long.valueOf(bundleId), newPluginsByCategory);
/* 152 */     if (existing != null) {
/* 153 */       return existing;
/*     */     }
/* 155 */     return newPluginsByCategory;
/*     */   }
/*     */   
/*     */   private Map<String, List<PluginType<?>>> decodeCacheFiles(ClassLoader loader) {
/* 159 */     long startTime = System.nanoTime();
/* 160 */     PluginCache cache = new PluginCache();
/*     */     try {
/* 162 */       Enumeration<URL> resources = loader.getResources("META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
/* 163 */       if (resources == null) {
/* 164 */         LOGGER.info("Plugin preloads not available from class loader {}", loader);
/*     */       } else {
/* 166 */         cache.loadCacheFiles(resources);
/*     */       } 
/* 168 */     } catch (IOException ioe) {
/* 169 */       LOGGER.warn("Unable to preload plugins", ioe);
/*     */     } 
/* 171 */     Map<String, List<PluginType<?>>> newPluginsByCategory = new HashMap<>();
/* 172 */     int pluginCount = 0;
/* 173 */     for (Map.Entry<String, Map<String, PluginEntry>> outer : (Iterable<Map.Entry<String, Map<String, PluginEntry>>>)cache.getAllCategories().entrySet()) {
/* 174 */       String categoryLowerCase = outer.getKey();
/* 175 */       List<PluginType<?>> types = new ArrayList<>(((Map)outer.getValue()).size());
/* 176 */       newPluginsByCategory.put(categoryLowerCase, types);
/* 177 */       for (Map.Entry<String, PluginEntry> inner : (Iterable<Map.Entry<String, PluginEntry>>)((Map)outer.getValue()).entrySet()) {
/* 178 */         PluginEntry entry = inner.getValue();
/* 179 */         String className = entry.getClassName();
/*     */         try {
/* 181 */           Class<?> clazz = loader.loadClass(className);
/* 182 */           PluginType<?> type = new PluginType(entry, clazz, entry.getName());
/* 183 */           types.add(type);
/* 184 */           pluginCount++;
/* 185 */         } catch (ClassNotFoundException e) {
/* 186 */           LOGGER.info("Plugin [{}] could not be loaded due to missing classes.", className, e);
/* 187 */         } catch (LinkageError e) {
/* 188 */           LOGGER.info("Plugin [{}] could not be loaded due to linkage error.", className, e);
/*     */         } 
/*     */       } 
/*     */     } 
/* 192 */     int numPlugins = pluginCount;
/* 193 */     LOGGER.debug(() -> {
/*     */           long endTime = System.nanoTime();
/*     */           StringBuilder sb = new StringBuilder("Took ");
/*     */           DecimalFormat numFormat = new DecimalFormat("#0.000000");
/*     */           sb.append(numFormat.format((endTime - startTime) * 1.0E-9D));
/*     */           sb.append(" seconds to load ").append(numPlugins);
/*     */           sb.append(" plugins from ").append(loader);
/*     */           return sb.toString();
/*     */         });
/* 202 */     return newPluginsByCategory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<PluginType<?>>> loadFromPackage(String pkg) {
/* 209 */     if (Strings.isBlank(pkg))
/*     */     {
/* 211 */       return Collections.emptyMap();
/*     */     }
/* 213 */     Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryByPackage.get(pkg);
/* 214 */     if (existing != null)
/*     */     {
/* 216 */       return existing;
/*     */     }
/*     */     
/* 219 */     long startTime = System.nanoTime();
/* 220 */     ResolverUtil resolver = new ResolverUtil();
/* 221 */     ClassLoader classLoader = Loader.getClassLoader();
/* 222 */     if (classLoader != null) {
/* 223 */       resolver.setClassLoader(classLoader);
/*     */     }
/* 225 */     resolver.findInPackage(new PluginTest(), pkg);
/*     */     
/* 227 */     Map<String, List<PluginType<?>>> newPluginsByCategory = new HashMap<>();
/* 228 */     for (Class<?> clazz : resolver.getClasses()) {
/* 229 */       Plugin plugin = clazz.<Plugin>getAnnotation(Plugin.class);
/* 230 */       String categoryLowerCase = plugin.category().toLowerCase();
/* 231 */       List<PluginType<?>> list = newPluginsByCategory.get(categoryLowerCase);
/* 232 */       if (list == null) {
/* 233 */         newPluginsByCategory.put(categoryLowerCase, list = new ArrayList<>());
/*     */       }
/* 235 */       PluginEntry mainEntry = new PluginEntry();
/*     */       
/* 237 */       String mainElementName = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
/* 238 */       mainEntry.setKey(plugin.name().toLowerCase());
/* 239 */       mainEntry.setName(plugin.name());
/* 240 */       mainEntry.setCategory(plugin.category());
/* 241 */       mainEntry.setClassName(clazz.getName());
/* 242 */       mainEntry.setPrintable(plugin.printObject());
/* 243 */       mainEntry.setDefer(plugin.deferChildren());
/* 244 */       PluginType<?> mainType = new PluginType(mainEntry, clazz, mainElementName);
/* 245 */       list.add(mainType);
/* 246 */       PluginAliases pluginAliases = clazz.<PluginAliases>getAnnotation(PluginAliases.class);
/* 247 */       if (pluginAliases != null) {
/* 248 */         for (String alias : pluginAliases.value()) {
/* 249 */           PluginEntry aliasEntry = new PluginEntry();
/*     */           
/* 251 */           String aliasElementName = plugin.elementType().equals("") ? alias.trim() : plugin.elementType();
/* 252 */           aliasEntry.setKey(alias.trim().toLowerCase());
/* 253 */           aliasEntry.setName(plugin.name());
/* 254 */           aliasEntry.setCategory(plugin.category());
/* 255 */           aliasEntry.setClassName(clazz.getName());
/* 256 */           aliasEntry.setPrintable(plugin.printObject());
/* 257 */           aliasEntry.setDefer(plugin.deferChildren());
/* 258 */           PluginType<?> aliasType = new PluginType(aliasEntry, clazz, aliasElementName);
/* 259 */           list.add(aliasType);
/*     */         } 
/*     */       }
/*     */     } 
/* 263 */     LOGGER.debug(() -> {
/*     */           long endTime = System.nanoTime();
/*     */           
/*     */           StringBuilder sb = new StringBuilder("Took ");
/*     */           
/*     */           DecimalFormat numFormat = new DecimalFormat("#0.000000");
/*     */           
/*     */           sb.append(numFormat.format((endTime - startTime) * 1.0E-9D));
/*     */           
/*     */           sb.append(" seconds to load ").append(resolver.getClasses().size());
/*     */           sb.append(" plugins from package ").append(pkg);
/*     */           return sb.toString();
/*     */         });
/* 276 */     existing = this.pluginsByCategoryByPackage.putIfAbsent(pkg, newPluginsByCategory);
/* 277 */     if (existing != null) {
/* 278 */       return existing;
/*     */     }
/* 280 */     return newPluginsByCategory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PluginTest
/*     */     implements ResolverUtil.Test
/*     */   {
/*     */     public boolean matches(Class<?> type) {
/* 292 */       return (type != null && type.isAnnotationPresent((Class)Plugin.class));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 297 */       return "annotated with @" + Plugin.class.getSimpleName();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(URI resource) {
/* 302 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean doesMatchClass() {
/* 307 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean doesMatchResource() {
/* 312 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */