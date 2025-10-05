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
/*    */ 
/*    */ 
/*    */ @Plugin(name = "equalsIgnoreCase", category = "Converter")
/*    */ @ConverterKeys({"equalsIgnoreCase"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class EqualsIgnoreCaseReplacementConverter
/*    */   extends EqualsBaseReplacementConverter
/*    */ {
/*    */   public static EqualsIgnoreCaseReplacementConverter newInstance(Configuration config, String[] options) {
/* 45 */     if (options.length != 3) {
/* 46 */       LOGGER.error("Incorrect number of options on equalsIgnoreCase. Expected 3 received " + options.length);
/* 47 */       return null;
/*    */     } 
/* 49 */     if (options[0] == null) {
/* 50 */       LOGGER.error("No pattern supplied on equalsIgnoreCase");
/* 51 */       return null;
/*    */     } 
/* 53 */     if (options[1] == null) {
/* 54 */       LOGGER.error("No test string supplied on equalsIgnoreCase");
/* 55 */       return null;
/*    */     } 
/* 57 */     if (options[2] == null) {
/* 58 */       LOGGER.error("No substitution supplied on equalsIgnoreCase");
/* 59 */       return null;
/*    */     } 
/* 61 */     String p = options[1];
/* 62 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 63 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 64 */     return new EqualsIgnoreCaseReplacementConverter(formatters, p, options[2], parser);
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
/*    */   private EqualsIgnoreCaseReplacementConverter(List<PatternFormatter> formatters, String testString, String substitution, PatternParser parser) {
/* 77 */     super("equalsIgnoreCase", "equalsIgnoreCase", formatters, testString, substitution, parser);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean equals(String str, StringBuilder buff, int from, int len) {
/* 82 */     return StringBuilders.equalsIgnoreCase(str, 0, str.length(), buff, from, len);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\EqualsIgnoreCaseReplacementConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */