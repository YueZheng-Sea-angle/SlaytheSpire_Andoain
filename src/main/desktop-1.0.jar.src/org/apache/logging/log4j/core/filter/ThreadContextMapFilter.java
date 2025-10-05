/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ThreadContextMapFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PluginAliases({"ContextMapFilter"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class ThreadContextMapFilter
/*     */   extends MapFilter
/*     */ {
/*  52 */   private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
/*     */   
/*     */   private final String key;
/*     */   
/*     */   private final String value;
/*     */   private final boolean useMap;
/*     */   
/*     */   public ThreadContextMapFilter(Map<String, List<String>> pairs, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  60 */     super(pairs, oper, onMatch, onMismatch);
/*  61 */     if (pairs.size() == 1) {
/*  62 */       Iterator<Map.Entry<String, List<String>>> iter = pairs.entrySet().iterator();
/*  63 */       Map.Entry<String, List<String>> entry = iter.next();
/*  64 */       if (((List)entry.getValue()).size() == 1) {
/*  65 */         this.key = entry.getKey();
/*  66 */         this.value = ((List<String>)entry.getValue()).get(0);
/*  67 */         this.useMap = false;
/*     */       } else {
/*  69 */         this.key = null;
/*  70 */         this.value = null;
/*  71 */         this.useMap = true;
/*     */       } 
/*     */     } else {
/*  74 */       this.key = null;
/*  75 */       this.value = null;
/*  76 */       this.useMap = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  83 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  89 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  95 */     return filter();
/*     */   }
/*     */   
/*     */   private Filter.Result filter() {
/*  99 */     boolean match = false;
/* 100 */     if (this.useMap) {
/* 101 */       ReadOnlyStringMap currentContextData = null;
/* 102 */       IndexedReadOnlyStringMap map = getStringMap();
/* 103 */       for (int i = 0; i < map.size(); i++) {
/* 104 */         if (currentContextData == null) {
/* 105 */           currentContextData = currentContextData();
/*     */         }
/* 107 */         String toMatch = (String)currentContextData.getValue(map.getKeyAt(i));
/* 108 */         match = (toMatch != null && ((List)map.getValueAt(i)).contains(toMatch));
/* 109 */         if ((!isAnd() && match) || (isAnd() && !match)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } else {
/* 114 */       match = this.value.equals(currentContextData().getValue(this.key));
/*     */     } 
/* 116 */     return match ? this.onMatch : this.onMismatch;
/*     */   }
/*     */   
/*     */   private ReadOnlyStringMap currentContextData() {
/* 120 */     return this.injector.rawContextData();
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 125 */     return filter(event.getContextData()) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 131 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 137 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 143 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 149 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 156 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 163 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 170 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 178 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 186 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 194 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 204 */     if (pairs == null || pairs.length == 0) {
/* 205 */       LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
/* 206 */       return null;
/*     */     } 
/* 208 */     Map<String, List<String>> map = new HashMap<>();
/* 209 */     for (KeyValuePair pair : pairs) {
/* 210 */       String key = pair.getKey();
/* 211 */       if (key == null) {
/* 212 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 215 */         String value = pair.getValue();
/* 216 */         if (value == null) {
/* 217 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 220 */           List<String> list = map.get(pair.getKey());
/* 221 */           if (list != null)
/* 222 */           { list.add(value); }
/*     */           else
/* 224 */           { list = new ArrayList<>();
/* 225 */             list.add(value);
/* 226 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 229 */     }  if (map.isEmpty()) {
/* 230 */       LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
/* 231 */       return null;
/*     */     } 
/* 233 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 234 */     return new ThreadContextMapFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\ThreadContextMapFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */