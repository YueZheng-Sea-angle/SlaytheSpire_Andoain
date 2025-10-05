/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
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
/*    */ public class Log4jJsonObjectMapper
/*    */   extends ObjectMapper
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Log4jJsonObjectMapper() {
/* 36 */     this(false, true, false, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jJsonObjectMapper(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString, boolean objectMessageAsJsonObject) {
/* 43 */     registerModule((Module)new Log4jJsonModule(encodeThreadContextAsList, includeStacktrace, stacktraceAsString, objectMessageAsJsonObject));
/* 44 */     setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\Log4jJsonObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */