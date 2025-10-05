/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*    */ import org.apache.logging.log4j.util.Strings;
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
/*    */ @Plugin(name = "LineSeparatorPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"n"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class LineSeparatorPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/* 35 */   private static final LineSeparatorPatternConverter INSTANCE = new LineSeparatorPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LineSeparatorPatternConverter() {
/* 41 */     super("Line Sep", "lineSep");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LineSeparatorPatternConverter newInstance(String[] options) {
/* 52 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent ignored, StringBuilder toAppendTo) {
/* 60 */     toAppendTo.append(Strings.LINE_SEPARATOR);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object ignored, StringBuilder output) {
/* 68 */     output.append(Strings.LINE_SEPARATOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVariable() {
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\LineSeparatorPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */