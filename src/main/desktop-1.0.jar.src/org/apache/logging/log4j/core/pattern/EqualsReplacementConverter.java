/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*    */ import org.apache.logging.log4j.util.StringBuilders;
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
/*    */ @Plugin(name = "equals", category = "Converter")
/*    */ @ConverterKeys({"equals"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class EqualsReplacementConverter
/*    */   extends EqualsBaseReplacementConverter
/*    */ {
/*    */   public static EqualsReplacementConverter newInstance(Configuration config, String[] options) {
/* 43 */     if (options.length != 3) {
/* 44 */       LOGGER.error("Incorrect number of options on equals. Expected 3 received " + options.length);
/* 45 */       return null;
/*    */     } 
/* 47 */     if (options[0] == null) {
/* 48 */       LOGGER.error("No pattern supplied on equals");
/* 49 */       return null;
/*    */     } 
/* 51 */     if (options[1] == null) {
/* 52 */       LOGGER.error("No test string supplied on equals");
/* 53 */       return null;
/*    */     } 
/* 55 */     if (options[2] == null) {
/* 56 */       LOGGER.error("No substitution supplied on equals");
/* 57 */       return null;
/*    */     } 
/* 59 */     String p = options[1];
/* 60 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 61 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 62 */     return new EqualsReplacementConverter(formatters, p, options[2], parser);
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
/*    */   private EqualsReplacementConverter(List<PatternFormatter> formatters, String testString, String substitution, PatternParser parser) {
/* 75 */     super("equals", "equals", formatters, testString, substitution, parser);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean equals(String str, StringBuilder buff, int from, int len) {
/* 80 */     return StringBuilders.equals(str, 0, str.length(), buff, from, len);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\EqualsReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */