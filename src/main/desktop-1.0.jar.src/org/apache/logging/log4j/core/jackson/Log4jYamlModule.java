/*    */ package org.apache.logging.log4j.core.jackson;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Version;
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.module.SimpleModule;
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
/*    */ final class Log4jYamlModule
/*    */   extends SimpleModule
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final boolean encodeThreadContextAsList;
/*    */   private final boolean includeStacktrace;
/*    */   private final boolean stacktraceAsString;
/*    */   
/*    */   Log4jYamlModule(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString) {
/* 39 */     super(Log4jYamlModule.class.getName(), new Version(2, 0, 0, null, null, null));
/* 40 */     this.encodeThreadContextAsList = encodeThreadContextAsList;
/* 41 */     this.includeStacktrace = includeStacktrace;
/* 42 */     this.stacktraceAsString = stacktraceAsString;
/*    */ 
/*    */ 
/*    */     
/* 46 */     (new Initializers.SimpleModuleInitializer()).initialize(this, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 52 */     super.setupModule(context);
/* 53 */     if (this.encodeThreadContextAsList) {
/* 54 */       (new Initializers.SetupContextInitializer()).setupModule(context, this.includeStacktrace, this.stacktraceAsString);
/*    */     } else {
/* 56 */       (new Initializers.SetupContextJsonInitializer()).setupModule(context, this.includeStacktrace, this.stacktraceAsString);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\Log4jYamlModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */