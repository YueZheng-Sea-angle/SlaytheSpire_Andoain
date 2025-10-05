/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
/*    */ import java.io.IOException;
/*    */ import org.apache.logging.log4j.message.ObjectMessage;
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
/*    */ final class ObjectMessageSerializer
/*    */   extends StdScalarSerializer<ObjectMessage>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   ObjectMessageSerializer() {
/* 38 */     super(ObjectMessage.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(ObjectMessage value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 44 */     jgen.writeObject(value.getParameter());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\ObjectMessageSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */