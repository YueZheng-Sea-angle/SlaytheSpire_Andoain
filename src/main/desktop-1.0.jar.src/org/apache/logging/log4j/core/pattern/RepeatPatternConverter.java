/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ @Plugin(name = "repeat", category = "Converter")
/*     */ @ConverterKeys({":|", "repeat"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class RepeatPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final String result;
/*     */   
/*     */   public static RepeatPatternConverter newInstance(Configuration config, String[] options) {
/*  43 */     if (options.length != 2) {
/*  44 */       LOGGER.error("Incorrect number of options on repeat. Expected 2 received " + options.length);
/*  45 */       return null;
/*     */     } 
/*  47 */     if (options[0] == null) {
/*  48 */       LOGGER.error("No string supplied on repeat");
/*  49 */       return null;
/*     */     } 
/*  51 */     if (options[1] == null) {
/*  52 */       LOGGER.error("No repeat count supplied on repeat");
/*  53 */       return null;
/*     */     } 
/*  55 */     int count = 0;
/*  56 */     String result = options[0];
/*     */     try {
/*  58 */       count = Integer.parseInt(options[1].trim());
/*  59 */       result = Strings.repeat(options[0], count);
/*  60 */     } catch (Exception ex) {
/*  61 */       LOGGER.error("The repeat count is not an integer: {}", options[1].trim());
/*     */     } 
/*     */     
/*  64 */     return new RepeatPatternConverter(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RepeatPatternConverter(String result) {
/*  74 */     super("repeat", "repeat");
/*  75 */     this.result = result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Object obj, StringBuilder toAppendTo) {
/*  85 */     format(toAppendTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  95 */     format(toAppendTo);
/*     */   }
/*     */   
/*     */   private void format(StringBuilder toAppendTo) {
/*  99 */     if (this.result != null)
/* 100 */       toAppendTo.append(this.result); 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\RepeatPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */