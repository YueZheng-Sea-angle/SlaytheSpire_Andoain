/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.IndexedStringMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
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
/*     */ @Plugin(name = "MapFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class MapFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final IndexedStringMap map;
/*     */   private final boolean isAnd;
/*     */   
/*     */   protected MapFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  55 */     super(onMatch, onMismatch);
/*  56 */     this.isAnd = oper;
/*  57 */     Objects.requireNonNull(map, "map cannot be null");
/*     */     
/*  59 */     this.map = (IndexedStringMap)new SortedArrayStringMap(map.size());
/*  60 */     for (Map.Entry<String, List<String>> entry : map.entrySet()) {
/*  61 */       this.map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  68 */     if (msg instanceof MapMessage) {
/*  69 */       return filter((MapMessage<?, ?>)msg) ? this.onMatch : this.onMismatch;
/*     */     }
/*  71 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  76 */     Message msg = event.getMessage();
/*  77 */     if (msg instanceof MapMessage) {
/*  78 */       return filter((MapMessage<?, ?>)msg) ? this.onMatch : this.onMismatch;
/*     */     }
/*  80 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */   
/*     */   protected boolean filter(MapMessage<?, ?> mapMessage) {
/*  84 */     boolean match = false;
/*  85 */     for (int i = 0; i < this.map.size(); i++) {
/*  86 */       String toMatch = mapMessage.get(this.map.getKeyAt(i));
/*  87 */       match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
/*     */       
/*  89 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/*  93 */     return match;
/*     */   }
/*     */   
/*     */   protected boolean filter(Map<String, String> data) {
/*  97 */     boolean match = false;
/*  98 */     for (int i = 0; i < this.map.size(); i++) {
/*  99 */       String toMatch = data.get(this.map.getKeyAt(i));
/* 100 */       match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
/*     */       
/* 102 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/* 106 */     return match;
/*     */   }
/*     */   
/*     */   protected boolean filter(ReadOnlyStringMap data) {
/* 110 */     boolean match = false;
/* 111 */     for (int i = 0; i < this.map.size(); i++) {
/* 112 */       String toMatch = (String)data.getValue(this.map.getKeyAt(i));
/* 113 */       match = (toMatch != null && ((List)this.map.getValueAt(i)).contains(toMatch));
/*     */       
/* 115 */       if ((!this.isAnd && match) || (this.isAnd && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/* 119 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 125 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 131 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 137 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 143 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 150 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 157 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 164 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 172 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 180 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 188 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     StringBuilder sb = new StringBuilder();
/* 194 */     sb.append("isAnd=").append(this.isAnd);
/* 195 */     if (this.map.size() > 0) {
/* 196 */       sb.append(", {");
/* 197 */       for (int i = 0; i < this.map.size(); i++) {
/* 198 */         if (i > 0) {
/* 199 */           sb.append(", ");
/*     */         }
/* 201 */         List<String> list = (List<String>)this.map.getValueAt(i);
/* 202 */         String value = (list.size() > 1) ? list.get(0) : list.toString();
/* 203 */         sb.append(this.map.getKeyAt(i)).append('=').append(value);
/*     */       } 
/* 205 */       sb.append('}');
/*     */     } 
/* 207 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected boolean isAnd() {
/* 211 */     return this.isAnd;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected Map<String, List<String>> getMap() {
/* 217 */     Map<String, List<String>> result = new HashMap<>(this.map.size());
/* 218 */     this.map.forEach((key, value) -> (List)result.put(key, (List)value));
/* 219 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IndexedReadOnlyStringMap getStringMap() {
/* 228 */     return (IndexedReadOnlyStringMap)this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static MapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 238 */     if (pairs == null || pairs.length == 0) {
/* 239 */       LOGGER.error("keys and values must be specified for the MapFilter");
/* 240 */       return null;
/*     */     } 
/* 242 */     Map<String, List<String>> map = new HashMap<>();
/* 243 */     for (KeyValuePair pair : pairs) {
/* 244 */       String key = pair.getKey();
/* 245 */       if (key == null) {
/* 246 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 249 */         String value = pair.getValue();
/* 250 */         if (value == null) {
/* 251 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 254 */           List<String> list = map.get(pair.getKey());
/* 255 */           if (list != null)
/* 256 */           { list.add(value); }
/*     */           else
/* 258 */           { list = new ArrayList<>();
/* 259 */             list.add(value);
/* 260 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 263 */     }  if (map.isEmpty()) {
/* 264 */       LOGGER.error("MapFilter is not configured with any valid key value pairs");
/* 265 */       return null;
/*     */     } 
/* 267 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 268 */     return new MapFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\MapFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */