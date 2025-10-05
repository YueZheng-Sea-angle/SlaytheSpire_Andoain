/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.StructuredDataMessage;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ @Plugin(name = "StructuredDataFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class StructuredDataFilter
/*     */   extends MapFilter
/*     */ {
/*     */   private static final int MAX_BUFFER_SIZE = 2048;
/*  49 */   private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private StructuredDataFilter(Map<String, List<String>> map, boolean oper, Filter.Result onMatch, Filter.Result onMismatch) {
/*  53 */     super(map, oper, onMatch, onMismatch);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  59 */     if (msg instanceof StructuredDataMessage) {
/*  60 */       return filter((StructuredDataMessage)msg);
/*     */     }
/*  62 */     return Filter.Result.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  67 */     Message msg = event.getMessage();
/*  68 */     if (msg instanceof StructuredDataMessage) {
/*  69 */       return filter((StructuredDataMessage)msg);
/*     */     }
/*  71 */     return super.filter(event);
/*     */   }
/*     */   
/*     */   protected Filter.Result filter(StructuredDataMessage message) {
/*  75 */     boolean match = false;
/*  76 */     IndexedReadOnlyStringMap map = getStringMap();
/*  77 */     for (int i = 0; i < map.size(); i++) {
/*  78 */       StringBuilder toMatch = getValue(message, map.getKeyAt(i));
/*  79 */       if (toMatch != null) {
/*  80 */         match = listContainsValue((List<String>)map.getValueAt(i), toMatch);
/*     */       } else {
/*  82 */         match = false;
/*     */       } 
/*  84 */       if ((!isAnd() && match) || (isAnd() && !match)) {
/*     */         break;
/*     */       }
/*     */     } 
/*  88 */     return match ? this.onMatch : this.onMismatch;
/*     */   }
/*     */   
/*     */   private StringBuilder getValue(StructuredDataMessage data, String key) {
/*  92 */     StringBuilder sb = getStringBuilder();
/*  93 */     if (key.equalsIgnoreCase("id")) {
/*  94 */       data.getId().formatTo(sb);
/*  95 */       return sb;
/*  96 */     }  if (key.equalsIgnoreCase("id.name"))
/*  97 */       return appendOrNull(data.getId().getName(), sb); 
/*  98 */     if (key.equalsIgnoreCase("type"))
/*  99 */       return appendOrNull(data.getType(), sb); 
/* 100 */     if (key.equalsIgnoreCase("message")) {
/* 101 */       data.formatTo(sb);
/* 102 */       return sb;
/*     */     } 
/* 104 */     return appendOrNull(data.get(key), sb);
/*     */   }
/*     */ 
/*     */   
/*     */   private StringBuilder getStringBuilder() {
/* 109 */     StringBuilder result = threadLocalStringBuilder.get();
/* 110 */     if (result == null) {
/* 111 */       result = new StringBuilder();
/* 112 */       threadLocalStringBuilder.set(result);
/*     */     } 
/* 114 */     StringBuilders.trimToMaxSize(result, 2048);
/* 115 */     result.setLength(0);
/* 116 */     return result;
/*     */   }
/*     */   
/*     */   private StringBuilder appendOrNull(String value, StringBuilder sb) {
/* 120 */     if (value == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     sb.append(value);
/* 124 */     return sb;
/*     */   }
/*     */   
/*     */   private boolean listContainsValue(List<String> candidates, StringBuilder toMatch) {
/* 128 */     if (toMatch == null) {
/* 129 */       for (int i = 0; i < candidates.size(); i++) {
/* 130 */         String candidate = candidates.get(i);
/* 131 */         if (candidate == null) {
/* 132 */           return true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 136 */       for (int i = 0; i < candidates.size(); i++) {
/* 137 */         String candidate = candidates.get(i);
/* 138 */         if (candidate == null) {
/* 139 */           return false;
/*     */         }
/* 141 */         if (StringBuilders.equals(candidate, 0, candidate.length(), toMatch, 0, toMatch.length())) {
/* 142 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 146 */     return false;
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
/*     */   @PluginFactory
/*     */   public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] pairs, @PluginAttribute("operator") String oper, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 164 */     if (pairs == null || pairs.length == 0) {
/* 165 */       LOGGER.error("keys and values must be specified for the StructuredDataFilter");
/* 166 */       return null;
/*     */     } 
/* 168 */     Map<String, List<String>> map = new HashMap<>();
/* 169 */     for (KeyValuePair pair : pairs) {
/* 170 */       String key = pair.getKey();
/* 171 */       if (key == null) {
/* 172 */         LOGGER.error("A null key is not valid in MapFilter");
/*     */       } else {
/*     */         
/* 175 */         String value = pair.getValue();
/* 176 */         if (value == null) {
/* 177 */           LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
/*     */         } else {
/*     */           
/* 180 */           List<String> list = map.get(pair.getKey());
/* 181 */           if (list != null)
/* 182 */           { list.add(value); }
/*     */           else
/* 184 */           { list = new ArrayList<>();
/* 185 */             list.add(value);
/* 186 */             map.put(pair.getKey(), list); } 
/*     */         } 
/*     */       } 
/* 189 */     }  if (map.isEmpty()) {
/* 190 */       LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
/* 191 */       return null;
/*     */     } 
/* 193 */     boolean isAnd = (oper == null || !oper.equalsIgnoreCase("or"));
/* 194 */     return new StructuredDataFilter(map, isAnd, match, mismatch);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\StructuredDataFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */