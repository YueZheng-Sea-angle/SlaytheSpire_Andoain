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
/*    */ class Log4jJsonModule
/*    */   extends SimpleModule
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final boolean encodeThreadContextAsList;
/*    */   private final boolean includeStacktrace;
/*    */   private final boolean stacktraceAsString;
/*    */   private final boolean objectMessageAsJsonObject;
/*    */   
/*    */   Log4jJsonModule(boolean encodeThreadContextAsList, boolean includeStacktrace, boolean stacktraceAsString, boolean objectMessageAsJsonObject) {
/* 40 */     super(Log4jJsonModule.class.getName(), new Version(2, 0, 0, null, null, null));
/* 41 */     this.encodeThreadContextAsList = encodeThreadContextAsList;
/* 42 */     this.includeStacktrace = includeStacktrace;
/* 43 */     this.stacktraceAsString = stacktraceAsString;
/* 44 */     this.objectMessageAsJsonObject = objectMessageAsJsonObject;
/*    */ 
/*    */ 
/*    */     
/* 48 */     (new Initializers.SimpleModuleInitializer()).initialize(this, objectMessageAsJsonObject);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setupModule(Module.SetupContext context) {
/* 54 */     super.setupModule(context);
/* 55 */     if (this.encodeThreadContextAsList) {
/* 56 */       (new Initializers.SetupContextInitializer()).setupModule(context, this.includeStacktrace, this.stacktraceAsString);
/*    */     } else {
/* 58 */       (new Initializers.SetupContextJsonInitializer()).setupModule(context, this.includeStacktrace, this.stacktraceAsString);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jackson\Log4jJsonModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */