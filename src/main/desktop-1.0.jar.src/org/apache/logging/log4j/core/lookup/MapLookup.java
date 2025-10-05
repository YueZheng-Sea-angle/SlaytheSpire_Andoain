/*     */ package org.apache.logging.log4j.core.lookup;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "map", category = "Lookup")
/*     */ public class MapLookup
/*     */   implements StrLookup
/*     */ {
/*     */   private final Map<String, String> map;
/*     */   
/*     */   public MapLookup() {
/*  42 */     this.map = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapLookup(Map<String, String> map) {
/*  52 */     this.map = map;
/*     */   }
/*     */   
/*     */   static Map<String, String> initMap(String[] srcArgs, Map<String, String> destMap) {
/*  56 */     for (int i = 0; i < srcArgs.length; i++) {
/*  57 */       int next = i + 1;
/*  58 */       String value = srcArgs[i];
/*  59 */       destMap.put(Integer.toString(i), value);
/*  60 */       destMap.put(value, (next < srcArgs.length) ? srcArgs[next] : null);
/*     */     } 
/*  62 */     return destMap;
/*     */   }
/*     */   
/*     */   static HashMap<String, String> newMap(int initialCapacity) {
/*  66 */     return new HashMap<>(initialCapacity);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void setMainArguments(String... args) {
/*  97 */     MainMapLookup.setMainArguments(args);
/*     */   }
/*     */   
/*     */   static Map<String, String> toMap(List<String> args) {
/* 101 */     if (args == null) {
/* 102 */       return null;
/*     */     }
/* 104 */     int size = args.size();
/* 105 */     return initMap(args.<String>toArray(new String[size]), newMap(size));
/*     */   }
/*     */   
/*     */   static Map<String, String> toMap(String[] args) {
/* 109 */     if (args == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     return initMap(args, newMap(args.length));
/*     */   }
/*     */   
/*     */   protected Map<String, String> getMap() {
/* 116 */     return this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   public String lookup(LogEvent event, String key) {
/* 121 */     boolean isMapMessage = (event != null && event.getMessage() instanceof MapMessage);
/* 122 */     if (this.map == null && !isMapMessage) {
/* 123 */       return null;
/*     */     }
/* 125 */     if (this.map != null && this.map.containsKey(key)) {
/* 126 */       String obj = this.map.get(key);
/* 127 */       if (obj != null) {
/* 128 */         return obj;
/*     */       }
/*     */     } 
/* 131 */     if (isMapMessage) {
/* 132 */       return ((MapMessage)event.getMessage()).get(key);
/*     */     }
/* 134 */     return null;
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
/*     */   public String lookup(String key) {
/* 149 */     if (this.map == null) {
/* 150 */       return null;
/*     */     }
/* 152 */     return this.map.get(key);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\MapLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */