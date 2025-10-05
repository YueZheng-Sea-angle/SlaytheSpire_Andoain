/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ public class ContextDataDeserializer
/*    */   extends StdDeserializer<StringMap>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   ContextDataDeserializer() {
/* 42 */     super(Map.class);
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
/*    */   public StringMap deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
/* 54 */     StringMap contextData = ContextDataFactory.createContextData();
/*    */     
/* 56 */     while (jp.nextToken() != JsonToken.END_OBJECT) {
/* 57 */       String fieldName = jp.getCurrentName();
/*    */ 
/*    */       
/* 60 */       jp.nextToken();
/* 61 */       contextData.putValue(fieldName, jp.getText());
/*    */     } 
/* 63 */     return contextData;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\ContextDataDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */