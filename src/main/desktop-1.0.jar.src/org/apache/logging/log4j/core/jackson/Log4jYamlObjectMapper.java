/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
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
/*    */ 
/*    */ public class Log4jYamlObjectMapper
/*    */   extends YAMLMapper
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Log4jYamlObjectMapper() {
/* 37 */     this(false, true, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jYamlObjectMapper(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString) {
/* 44 */     registerModule((Module)new Log4jYamlModule(encodeThreadContextAsList, includeStacktrace, stacktraceAsString));
/* 45 */     setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\Log4jYamlObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */