/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*    */ import org.apache.logging.log4j.message.StructuredDataId;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "LoggerFields", category = "Core", printObject = true)
/*    */ public final class LoggerFields
/*    */ {
/*    */   private final Map<String, String> map;
/*    */   private final String sdId;
/*    */   private final String enterpriseId;
/*    */   private final boolean discardIfAllFieldsAreEmpty;
/*    */   
/*    */   private LoggerFields(Map<String, String> map, String sdId, String enterpriseId, boolean discardIfAllFieldsAreEmpty) {
/* 44 */     this.sdId = sdId;
/* 45 */     this.enterpriseId = enterpriseId;
/* 46 */     this.map = Collections.unmodifiableMap(map);
/* 47 */     this.discardIfAllFieldsAreEmpty = discardIfAllFieldsAreEmpty;
/*    */   }
/*    */   
/*    */   public Map<String, String> getMap() {
/* 51 */     return this.map;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return this.map.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static LoggerFields createLoggerFields(@PluginElement("LoggerFields") KeyValuePair[] keyValuePairs, @PluginAttribute("sdId") String sdId, @PluginAttribute("enterpriseId") String enterpriseId, @PluginAttribute("discardIfAllFieldsAreEmpty") boolean discardIfAllFieldsAreEmpty) {
/* 78 */     Map<String, String> map = new HashMap<>();
/*    */     
/* 80 */     for (KeyValuePair keyValuePair : keyValuePairs) {
/* 81 */       map.put(keyValuePair.getKey(), keyValuePair.getValue());
/*    */     }
/*    */     
/* 84 */     return new LoggerFields(map, sdId, enterpriseId, discardIfAllFieldsAreEmpty);
/*    */   }
/*    */   
/*    */   public StructuredDataId getSdId() {
/* 88 */     if (this.enterpriseId == null || this.sdId == null) {
/* 89 */       return null;
/*    */     }
/* 91 */     int eId = Integer.parseInt(this.enterpriseId);
/* 92 */     return new StructuredDataId(this.sdId, eId, null, null);
/*    */   }
/*    */   
/*    */   public boolean getDiscardIfAllFieldsAreEmpty() {
/* 96 */     return this.discardIfAllFieldsAreEmpty;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\LoggerFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */