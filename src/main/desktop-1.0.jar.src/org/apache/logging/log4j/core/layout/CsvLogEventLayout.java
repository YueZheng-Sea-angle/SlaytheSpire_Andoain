/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.csv.CSVFormat;
/*     */ import org.apache.commons.csv.QuoteMode;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ @Plugin(name = "CsvLogEventLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public class CsvLogEventLayout
/*     */   extends AbstractCsvLayout
/*     */ {
/*     */   public static CsvLogEventLayout createDefaultLayout() {
/*  45 */     return new CsvLogEventLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf("Default"), null, null);
/*     */   }
/*     */   
/*     */   public static CsvLogEventLayout createLayout(CSVFormat format) {
/*  49 */     return new CsvLogEventLayout(null, Charset.forName("UTF-8"), format, null, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static CsvLogEventLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value = "format", defaultString = "Default") String format, @PluginAttribute("delimiter") Character delimiter, @PluginAttribute("escape") Character escape, @PluginAttribute("quote") Character quote, @PluginAttribute("quoteMode") QuoteMode quoteMode, @PluginAttribute("nullString") String nullString, @PluginAttribute("recordSeparator") String recordSeparator, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("header") String header, @PluginAttribute("footer") String footer) {
/*  69 */     CSVFormat csvFormat = createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
/*  70 */     return new CsvLogEventLayout(config, charset, csvFormat, header, footer);
/*     */   }
/*     */   
/*     */   protected CsvLogEventLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
/*  74 */     super(config, charset, csvFormat, header, footer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/*  79 */     StringBuilder buffer = getStringBuilder();
/*  80 */     CSVFormat format = getFormat();
/*     */     try {
/*  82 */       format.print(Long.valueOf(event.getNanoTime()), buffer, true);
/*  83 */       format.print(Long.valueOf(event.getTimeMillis()), buffer, false);
/*  84 */       format.print(event.getLevel(), buffer, false);
/*  85 */       format.print(Long.valueOf(event.getThreadId()), buffer, false);
/*  86 */       format.print(event.getThreadName(), buffer, false);
/*  87 */       format.print(Integer.valueOf(event.getThreadPriority()), buffer, false);
/*  88 */       format.print(event.getMessage().getFormattedMessage(), buffer, false);
/*  89 */       format.print(event.getLoggerFqcn(), buffer, false);
/*  90 */       format.print(event.getLoggerName(), buffer, false);
/*  91 */       format.print(event.getMarker(), buffer, false);
/*  92 */       format.print(event.getThrownProxy(), buffer, false);
/*  93 */       format.print(event.getSource(), buffer, false);
/*  94 */       format.print(event.getContextData(), buffer, false);
/*  95 */       format.print(event.getContextStack(), buffer, false);
/*  96 */       format.println(buffer);
/*  97 */       return buffer.toString();
/*  98 */     } catch (IOException e) {
/*  99 */       StatusLogger.getLogger().error(event.toString(), e);
/* 100 */       return format.getCommentMarker() + " " + e;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\CsvLogEventLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */