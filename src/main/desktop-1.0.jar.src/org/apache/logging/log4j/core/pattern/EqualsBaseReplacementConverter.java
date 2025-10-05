/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public abstract class EqualsBaseReplacementConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private final List<PatternFormatter> formatters;
/*    */   private final List<PatternFormatter> substitutionFormatters;
/*    */   private final String substitution;
/*    */   private final String testString;
/*    */   
/*    */   protected EqualsBaseReplacementConverter(String name, String style, List<PatternFormatter> formatters, String testString, String substitution, PatternParser parser) {
/* 47 */     super(name, style);
/* 48 */     this.testString = testString;
/* 49 */     this.substitution = substitution;
/* 50 */     this.formatters = formatters;
/*    */ 
/*    */     
/* 53 */     this.substitutionFormatters = substitution.contains("%") ? parser.parse(substitution) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 61 */     int initialSize = toAppendTo.length();
/* 62 */     for (int i = 0; i < this.formatters.size(); i++) {
/* 63 */       PatternFormatter formatter = this.formatters.get(i);
/* 64 */       formatter.format(event, toAppendTo);
/*    */     } 
/* 66 */     if (equals(this.testString, toAppendTo, initialSize, toAppendTo.length() - initialSize)) {
/* 67 */       toAppendTo.setLength(initialSize);
/* 68 */       parseSubstitution(event, toAppendTo);
/*    */     } 
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
/*    */   protected abstract boolean equals(String paramString, StringBuilder paramStringBuilder, int paramInt1, int paramInt2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void parseSubstitution(LogEvent event, StringBuilder substitutionBuffer) {
/* 90 */     if (this.substitutionFormatters != null) {
/* 91 */       for (int i = 0; i < this.substitutionFormatters.size(); i++) {
/* 92 */         PatternFormatter formatter = this.substitutionFormatters.get(i);
/* 93 */         formatter.format(event, substitutionBuffer);
/*    */       } 
/*    */     } else {
/* 96 */       substitutionBuffer.append(this.substitution);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\EqualsBaseReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */