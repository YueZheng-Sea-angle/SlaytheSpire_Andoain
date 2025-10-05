/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.logging.log4j.Logger;
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
/*     */ public class PluginManager
/*     */ {
/*  36 */   private static final CopyOnWriteArrayList<String> PACKAGES = new CopyOnWriteArrayList<>();
/*     */   
/*     */   private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
/*  39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  41 */   private Map<String, PluginType<?>> plugins = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final String category;
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginManager(String category) {
/*  50 */     this.category = category;
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
/*     */   @Deprecated
/*     */   public static void main(String[] args) {
/*  64 */     System.err.println("ERROR: this tool is superseded by the annotation processor included in log4j-core.");
/*  65 */     System.err.println("If the annotation processor does not work for you, please see the manual page:");
/*  66 */     System.err.println("http://logging.apache.org/log4j/2.x/manual/configuration.html#ConfigurationSyntax");
/*  67 */     System.exit(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPackage(String p) {
/*  76 */     if (Strings.isBlank(p)) {
/*     */       return;
/*     */     }
/*  79 */     PACKAGES.addIfAbsent(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addPackages(Collection<String> packages) {
/*  88 */     for (String pkg : packages) {
/*  89 */       if (Strings.isNotBlank(pkg)) {
/*  90 */         PACKAGES.addIfAbsent(pkg);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PluginType<?> getPluginType(String name) {
/* 102 */     return this.plugins.get(name.toLowerCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, PluginType<?>> getPlugins() {
/* 111 */     return this.plugins;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void collectPlugins() {
/* 118 */     collectPlugins(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void collectPlugins(List<String> packages) {
/* 128 */     String categoryLowerCase = this.category.toLowerCase();
/* 129 */     Map<String, PluginType<?>> newPlugins = new LinkedHashMap<>();
/*     */ 
/*     */     
/* 132 */     Map<String, List<PluginType<?>>> builtInPlugins = PluginRegistry.getInstance().loadFromMainClassLoader();
/* 133 */     if (builtInPlugins.isEmpty())
/*     */     {
/*     */       
/* 136 */       builtInPlugins = PluginRegistry.getInstance().loadFromPackage("org.apache.logging.log4j.core");
/*     */     }
/* 138 */     mergeByName(newPlugins, builtInPlugins.get(categoryLowerCase));
/*     */ 
/*     */     
/* 141 */     for (Map<String, List<PluginType<?>>> pluginsByCategory : PluginRegistry.getInstance().getPluginsByCategoryByBundleId().values()) {
/* 142 */       mergeByName(newPlugins, pluginsByCategory.get(categoryLowerCase));
/*     */     }
/*     */ 
/*     */     
/* 146 */     for (String pkg : PACKAGES) {
/* 147 */       mergeByName(newPlugins, PluginRegistry.getInstance().loadFromPackage(pkg).get(categoryLowerCase));
/*     */     }
/*     */     
/* 150 */     if (packages != null) {
/* 151 */       for (String pkg : packages) {
/* 152 */         mergeByName(newPlugins, PluginRegistry.getInstance().loadFromPackage(pkg).get(categoryLowerCase));
/*     */       }
/*     */     }
/*     */     
/* 156 */     LOGGER.debug("PluginManager '{}' found {} plugins", this.category, Integer.valueOf(newPlugins.size()));
/*     */     
/* 158 */     this.plugins = newPlugins;
/*     */   }
/*     */   
/*     */   private static void mergeByName(Map<String, PluginType<?>> newPlugins, List<PluginType<?>> plugins) {
/* 162 */     if (plugins == null) {
/*     */       return;
/*     */     }
/* 165 */     for (PluginType<?> pluginType : plugins) {
/* 166 */       String key = pluginType.getKey();
/* 167 */       PluginType<?> existing = newPlugins.get(key);
/* 168 */       if (existing == null) {
/* 169 */         newPlugins.put(key, pluginType); continue;
/* 170 */       }  if (!existing.getPluginClass().equals(pluginType.getPluginClass()))
/* 171 */         LOGGER.warn("Plugin [{}] is already mapped to {}, ignoring {}", key, existing
/* 172 */             .getPluginClass(), pluginType.getPluginClass()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */