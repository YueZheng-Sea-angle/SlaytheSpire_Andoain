/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.impl.ContextDataFactory;
/*    */ import org.apache.logging.log4j.util.StringMap;
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
/*    */ public class ContextDataAsEntryListDeserializer
/*    */   extends StdDeserializer<StringMap>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   ContextDataAsEntryListDeserializer() {
/* 42 */     super(Map.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StringMap deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
/* 48 */     List<MapEntry> list = (List<MapEntry>)jp.readValueAs(new TypeReference<List<MapEntry>>() {
/*    */         
/*    */         });
/* 51 */     new ContextDataFactory(); StringMap contextData = ContextDataFactory.createContextData();
/* 52 */     for (MapEntry mapEntry : list) {
/* 53 */       contextData.putValue(mapEntry.getKey(), mapEntry.getValue());
/*    */     }
/* 55 */     return contextData;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\ContextDataAsEntryListDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */