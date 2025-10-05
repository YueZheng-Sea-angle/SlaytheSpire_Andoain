/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.ContextDataInjector;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "DynamicThresholdFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class DynamicThresholdFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   @PluginFactory
/*     */   public static DynamicThresholdFilter createFilter(@PluginAttribute("key") String key, @PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("defaultThreshold") Level defaultThreshold, @PluginAttribute("onMatch") Filter.Result onMatch, @PluginAttribute("onMismatch") Filter.Result onMismatch) {
/*  67 */     Map<String, Level> map = new HashMap<>();
/*  68 */     for (KeyValuePair pair : pairs) {
/*  69 */       map.put(pair.getKey(), Level.toLevel(pair.getValue()));
/*     */     }
/*  71 */     Level level = (defaultThreshold == null) ? Level.ERROR : defaultThreshold;
/*  72 */     return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
/*     */   }
/*     */   
/*  75 */   private Level defaultThreshold = Level.ERROR;
/*     */   private final String key;
/*  77 */   private final ContextDataInjector injector = ContextDataInjectorFactory.createInjector();
/*  78 */   private Map<String, Level> levelMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   private DynamicThresholdFilter(String key, Map<String, Level> pairs, Level defaultLevel, Filter.Result onMatch, Filter.Result onMismatch) {
/*  82 */     super(onMatch, onMismatch);
/*  83 */     Objects.requireNonNull(key, "key cannot be null");
/*  84 */     this.key = key;
/*  85 */     this.levelMap = pairs;
/*  86 */     this.defaultThreshold = defaultLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  91 */     if (this == obj) {
/*  92 */       return true;
/*     */     }
/*  94 */     if (!equalsImpl(obj)) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (getClass() != obj.getClass()) {
/*  98 */       return false;
/*     */     }
/* 100 */     DynamicThresholdFilter other = (DynamicThresholdFilter)obj;
/* 101 */     if (!Objects.equals(this.defaultThreshold, other.defaultThreshold)) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (!Objects.equals(this.key, other.key)) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (!Objects.equals(this.levelMap, other.levelMap)) {
/* 108 */       return false;
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   private Filter.Result filter(Level level, ReadOnlyStringMap contextMap) {
/* 114 */     String value = (String)contextMap.getValue(this.key);
/* 115 */     if (value != null) {
/* 116 */       Level ctxLevel = this.levelMap.get(value);
/* 117 */       if (ctxLevel == null) {
/* 118 */         ctxLevel = this.defaultThreshold;
/*     */       }
/* 120 */       return level.isMoreSpecificThan(ctxLevel) ? this.onMatch : this.onMismatch;
/*     */     } 
/* 122 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 128 */     return filter(event.getLevel(), event.getContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 134 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 140 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 146 */     return filter(level, currentContextData());
/*     */   }
/*     */   
/*     */   private ReadOnlyStringMap currentContextData() {
/* 150 */     return this.injector.rawContextData();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 156 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 162 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 168 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 174 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 181 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 188 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 195 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 203 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 211 */     return filter(level, currentContextData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 219 */     return filter(level, currentContextData());
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 223 */     return this.key;
/*     */   }
/*     */   
/*     */   public Map<String, Level> getLevelMap() {
/* 227 */     return this.levelMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 232 */     int prime = 31;
/* 233 */     int result = hashCodeImpl();
/* 234 */     result = 31 * result + ((this.defaultThreshold == null) ? 0 : this.defaultThreshold.hashCode());
/* 235 */     result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
/* 236 */     result = 31 * result + ((this.levelMap == null) ? 0 : this.levelMap.hashCode());
/* 237 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 242 */     StringBuilder sb = new StringBuilder();
/* 243 */     sb.append("key=").append(this.key);
/* 244 */     sb.append(", default=").append(this.defaultThreshold);
/* 245 */     if (this.levelMap.size() > 0) {
/* 246 */       sb.append('{');
/* 247 */       boolean first = true;
/* 248 */       for (Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
/* 249 */         if (!first) {
/* 250 */           sb.append(", ");
/* 251 */           first = false;
/*     */         } 
/* 253 */         sb.append(entry.getKey()).append('=').append(entry.getValue());
/*     */       } 
/* 255 */       sb.append('}');
/*     */     } 
/* 257 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\DynamicThresholdFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */