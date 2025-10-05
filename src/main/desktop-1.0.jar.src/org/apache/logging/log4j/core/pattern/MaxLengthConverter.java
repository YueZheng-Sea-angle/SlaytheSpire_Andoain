/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "maxLength", category = "Converter")
/*     */ @ConverterKeys({"maxLength", "maxLen"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class MaxLengthConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final List<PatternFormatter> formatters;
/*     */   private final int maxLength;
/*     */   
/*     */   public static MaxLengthConverter newInstance(Configuration config, String[] options) {
/*  51 */     if (options.length != 2) {
/*  52 */       LOGGER.error("Incorrect number of options on maxLength: expected 2 received {}: {}", Integer.valueOf(options.length), options);
/*     */       
/*  54 */       return null;
/*     */     } 
/*  56 */     if (options[0] == null) {
/*  57 */       LOGGER.error("No pattern supplied on maxLength");
/*  58 */       return null;
/*     */     } 
/*  60 */     if (options[1] == null) {
/*  61 */       LOGGER.error("No length supplied on maxLength");
/*  62 */       return null;
/*     */     } 
/*  64 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  65 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  66 */     return new MaxLengthConverter(formatters, AbstractAppender.parseInt(options[1], 100));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MaxLengthConverter(List<PatternFormatter> formatters, int maxLength) {
/*  80 */     super("MaxLength", "maxLength");
/*  81 */     this.maxLength = maxLength;
/*  82 */     this.formatters = formatters;
/*  83 */     LOGGER.trace("new MaxLengthConverter with {}", Integer.valueOf(maxLength));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  89 */     int initialLength = toAppendTo.length();
/*  90 */     for (int i = 0; i < this.formatters.size(); i++) {
/*  91 */       PatternFormatter formatter = this.formatters.get(i);
/*  92 */       formatter.format(event, toAppendTo);
/*  93 */       if (toAppendTo.length() > initialLength + this.maxLength) {
/*     */         break;
/*     */       }
/*     */     } 
/*  97 */     if (toAppendTo.length() > initialLength + this.maxLength) {
/*  98 */       toAppendTo.setLength(initialLength + this.maxLength);
/*  99 */       if (this.maxLength > 20)
/* 100 */         toAppendTo.append("..."); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MaxLengthConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */