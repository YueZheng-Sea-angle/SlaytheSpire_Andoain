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
/*    */ @Plugin(name = "LineLocationPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"L", "line"})
/*    */ public final class LineLocationPatternConverter
/*    */   extends LogEventPatternConverter
/*    */   implements LocationAware
/*    */ {
/* 32 */   private static final LineLocationPatternConverter INSTANCE = new LineLocationPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LineLocationPatternConverter() {
/* 39 */     super("Line", "line");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LineLocationPatternConverter newInstance(String[] options) {
/* 50 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder output) {
/* 58 */     StackTraceElement element = event.getSource();
/*    */     
/* 60 */     if (element != null) {
/* 61 */       output.append(element.getLineNumber());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresLocation() {
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\LineLocationPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */