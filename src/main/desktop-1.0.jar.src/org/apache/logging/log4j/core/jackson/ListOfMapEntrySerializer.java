/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public class ListOfMapEntrySerializer
/*    */   extends StdSerializer<Map<String, String>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   protected ListOfMapEntrySerializer() {
/* 39 */     super(Map.class, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(Map<String, String> map, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 45 */     Set<Map.Entry<String, String>> entrySet = map.entrySet();
/* 46 */     MapEntry[] pairs = new MapEntry[entrySet.size()];
/* 47 */     int i = 0;
/* 48 */     for (Map.Entry<String, String> entry : entrySet) {
/* 49 */       pairs[i++] = new MapEntry(entry.getKey(), entry.getValue());
/*    */     }
/* 51 */     jgen.writeObject(pairs);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\ListOfMapEntrySerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */