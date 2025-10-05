/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ @Plugin(name = "MapRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
/*     */ public final class MapRewritePolicy
/*     */   implements RewritePolicy
/*     */ {
/*  44 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final Map<String, Object> map;
/*     */   
/*     */   private final Mode mode;
/*     */   
/*     */   private MapRewritePolicy(Map<String, Object> map, Mode mode) {
/*  51 */     this.map = map;
/*  52 */     this.mode = mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEvent rewrite(LogEvent source) {
/*  63 */     Message msg = source.getMessage();
/*  64 */     if (msg == null || !(msg instanceof MapMessage)) {
/*  65 */       return source;
/*     */     }
/*     */ 
/*     */     
/*  69 */     MapMessage<?, Object> mapMsg = (MapMessage<?, Object>)msg;
/*  70 */     Map<String, Object> newMap = new HashMap<>(mapMsg.getData());
/*  71 */     switch (this.mode)
/*     */     { case Add:
/*  73 */         newMap.putAll(this.map);
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
/*  84 */         mapMessage = mapMsg.newInstance(newMap);
/*  85 */         return (LogEvent)(new Log4jLogEvent.Builder(source)).setMessage((Message)mapMessage).build(); }  for (Map.Entry<String, Object> entry : this.map.entrySet()) { if (newMap.containsKey(entry.getKey())) newMap.put(entry.getKey(), entry.getValue());  }  MapMessage mapMessage = mapMsg.newInstance(newMap); return (LogEvent)(new Log4jLogEvent.Builder(source)).setMessage((Message)mapMessage).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Mode
/*     */   {
/*  97 */     Add,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     Update;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     StringBuilder sb = new StringBuilder();
/* 108 */     sb.append("mode=").append(this.mode);
/* 109 */     sb.append(" {");
/* 110 */     boolean first = true;
/* 111 */     for (Map.Entry<String, Object> entry : this.map.entrySet()) {
/* 112 */       if (!first) {
/* 113 */         sb.append(", ");
/*     */       }
/* 115 */       sb.append(entry.getKey()).append('=').append(entry.getValue());
/* 116 */       first = false;
/*     */     } 
/* 118 */     sb.append('}');
/* 119 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static MapRewritePolicy createPolicy(@PluginAttribute("mode") String mode, @PluginElement("KeyValuePair") KeyValuePair[] pairs) {
/* 132 */     Mode op = (mode == null) ? (op = Mode.Add) : Mode.valueOf(mode);
/* 133 */     if (pairs == null || pairs.length == 0) {
/* 134 */       LOGGER.error("keys and values must be specified for the MapRewritePolicy");
/* 135 */       return null;
/*     */     } 
/* 137 */     Map<String, Object> map = new HashMap<>();
/* 138 */     for (KeyValuePair pair : pairs) {
/* 139 */       String key = pair.getKey();
/* 140 */       if (key == null) {
/* 141 */         LOGGER.error("A null key is not valid in MapRewritePolicy");
/*     */       } else {
/*     */         
/* 144 */         String value = pair.getValue();
/* 145 */         if (value == null)
/* 146 */         { LOGGER.error("A null value for key " + key + " is not allowed in MapRewritePolicy"); }
/*     */         else
/*     */         
/* 149 */         { map.put(pair.getKey(), pair.getValue()); } 
/*     */       } 
/* 151 */     }  if (map.isEmpty()) {
/* 152 */       LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
/* 153 */       return null;
/*     */     } 
/* 155 */     return new MapRewritePolicy(map, op);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rewrite\MapRewritePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */