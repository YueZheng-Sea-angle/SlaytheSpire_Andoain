/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*    */ 
/*    */ @Plugin(name = "notEmpty", category = "Converter")
/*    */ @ConverterKeys({"notEmpty", "varsNotEmpty", "variablesNotEmpty"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class VariablesNotEmptyReplacementConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final List<PatternFormatter> formatters;
/*    */   
/*    */   private VariablesNotEmptyReplacementConverter(List<PatternFormatter> formatters) {
/* 44 */     super("notEmpty", "notEmpty");
/* 45 */     this.formatters = formatters;
/*    */   }
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
/*    */   public static VariablesNotEmptyReplacementConverter newInstance(Configuration config, String[] options) {
/* 59 */     if (options.length != 1) {
/* 60 */       LOGGER.error("Incorrect number of options on varsNotEmpty. Expected 1 received " + options.length);
/* 61 */       return null;
/*    */     } 
/* 63 */     if (options[0] == null) {
/* 64 */       LOGGER.error("No pattern supplied on varsNotEmpty");
/* 65 */       return null;
/*    */     } 
/* 67 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 68 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 69 */     return new VariablesNotEmptyReplacementConverter(formatters);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 77 */     int start = toAppendTo.length();
/* 78 */     boolean allVarsEmpty = true;
/* 79 */     boolean hasVars = false;
/* 80 */     for (int i = 0; i < this.formatters.size(); i++) {
/* 81 */       PatternFormatter formatter = this.formatters.get(i);
/* 82 */       int formatterStart = toAppendTo.length();
/* 83 */       formatter.format(event, toAppendTo);
/* 84 */       if (formatter.getConverter().isVariable()) {
/* 85 */         hasVars = true;
/* 86 */         allVarsEmpty = (allVarsEmpty && toAppendTo.length() == formatterStart);
/*    */       } 
/*    */     } 
/* 89 */     if (!hasVars || allVarsEmpty)
/* 90 */       toAppendTo.setLength(start); 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\VariablesNotEmptyReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */