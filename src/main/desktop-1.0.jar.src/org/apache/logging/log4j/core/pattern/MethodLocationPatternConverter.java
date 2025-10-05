/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.LocationAware;
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
/*    */ @Plugin(name = "MethodLocationPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"M", "method"})
/*    */ public final class MethodLocationPatternConverter
/*    */   extends LogEventPatternConverter
/*    */   implements LocationAware
/*    */ {
/* 32 */   private static final MethodLocationPatternConverter INSTANCE = new MethodLocationPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MethodLocationPatternConverter() {
/* 39 */     super("Method", "method");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MethodLocationPatternConverter newInstance(String[] options) {
/* 49 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 57 */     StackTraceElement element = event.getSource();
/*    */     
/* 59 */     if (element != null) {
/* 60 */       toAppendTo.append(element.getMethodName());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresLocation() {
/* 66 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MethodLocationPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */