/*     */ package org.apache.logging.log4j.core.config.plugins.processor;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginCache
/*     */ {
/*  35 */   private final Map<String, Map<String, PluginEntry>> categories = new TreeMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Map<String, PluginEntry>> getAllCategories() {
/*  45 */     return this.categories;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, PluginEntry> getCategory(String category) {
/*  55 */     String key = category.toLowerCase();
/*  56 */     return this.categories.computeIfAbsent(key, ignored -> new TreeMap<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeCache(OutputStream os) throws IOException {
/*  67 */     try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(os))) {
/*     */ 
/*     */       
/*  70 */       out.writeInt(this.categories.size());
/*  71 */       for (Map.Entry<String, Map<String, PluginEntry>> category : this.categories.entrySet()) {
/*  72 */         out.writeUTF(category.getKey());
/*  73 */         Map<String, PluginEntry> m = category.getValue();
/*  74 */         out.writeInt(m.size());
/*  75 */         for (Map.Entry<String, PluginEntry> entry : m.entrySet()) {
/*  76 */           PluginEntry plugin = entry.getValue();
/*  77 */           out.writeUTF(plugin.getKey());
/*  78 */           out.writeUTF(plugin.getClassName());
/*  79 */           out.writeUTF(plugin.getName());
/*  80 */           out.writeBoolean(plugin.isPrintable());
/*  81 */           out.writeBoolean(plugin.isDefer());
/*     */         } 
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
/*     */   public void loadCacheFiles(Enumeration<URL> resources) throws IOException {
/*  94 */     this.categories.clear();
/*  95 */     while (resources.hasMoreElements()) {
/*  96 */       URL url = resources.nextElement();
/*  97 */       try (DataInputStream in = new DataInputStream(new BufferedInputStream(url.openStream()))) {
/*  98 */         int count = in.readInt();
/*  99 */         for (int i = 0; i < count; i++) {
/* 100 */           String category = in.readUTF();
/* 101 */           Map<String, PluginEntry> m = getCategory(category);
/* 102 */           int entries = in.readInt();
/* 103 */           for (int j = 0; j < entries; j++) {
/*     */             
/* 105 */             String key = in.readUTF();
/* 106 */             String className = in.readUTF();
/* 107 */             String name = in.readUTF();
/* 108 */             boolean printable = in.readBoolean();
/* 109 */             boolean defer = in.readBoolean();
/* 110 */             m.computeIfAbsent(key, k -> {
/*     */                   PluginEntry entry = new PluginEntry();
/*     */                   entry.setKey(k);
/*     */                   entry.setClassName(className);
/*     */                   entry.setName(name);
/*     */                   entry.setPrintable(printable);
/*     */                   entry.setDefer(defer);
/*     */                   entry.setCategory(category);
/*     */                   return entry;
/*     */                 });
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 132 */     return this.categories.size();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\processor\PluginCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */