/*    */ package org.apache.logging.log4j.core.parser;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.ObjectReader;
/*    */ import java.io.IOException;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
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
/*    */ class AbstractJacksonLogEventParser
/*    */   implements TextLogEventParser
/*    */ {
/*    */   private final ObjectReader objectReader;
/*    */   
/*    */   AbstractJacksonLogEventParser(ObjectMapper objectMapper) {
/* 31 */     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/* 32 */     this.objectReader = objectMapper.readerFor(Log4jLogEvent.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public LogEvent parseFrom(String input) throws ParseException {
/*    */     try {
/* 38 */       return (LogEvent)this.objectReader.readValue(input);
/* 39 */     } catch (IOException e) {
/* 40 */       throw new ParseException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public LogEvent parseFrom(byte[] input) throws ParseException {
/*    */     try {
/* 47 */       return (LogEvent)this.objectReader.readValue(input);
/* 48 */     } catch (IOException e) {
/* 49 */       throw new ParseException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public LogEvent parseFrom(byte[] input, int offset, int length) throws ParseException {
/*    */     try {
/* 56 */       return (LogEvent)this.objectReader.readValue(input, offset, length);
/* 57 */     } catch (IOException e) {
/* 58 */       throw new ParseException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\parser\AbstractJacksonLogEventParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */