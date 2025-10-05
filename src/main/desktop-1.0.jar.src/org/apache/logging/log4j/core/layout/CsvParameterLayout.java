/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.commons.csv.CSVFormat;
/*    */ import org.apache.commons.csv.QuoteMode;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ 
/*    */ 
/*    */ @Plugin(name = "CsvParameterLayout", category = "Core", elementType = "layout", printObject = true)
/*    */ public class CsvParameterLayout
/*    */   extends AbstractCsvLayout
/*    */ {
/*    */   public static AbstractCsvLayout createDefaultLayout() {
/* 54 */     return new CsvParameterLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf("Default"), null, null);
/*    */   }
/*    */   
/*    */   public static AbstractCsvLayout createLayout(CSVFormat format) {
/* 58 */     return new CsvParameterLayout(null, Charset.forName("UTF-8"), format, null, null);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static AbstractCsvLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute(value = "format", defaultString = "Default") String format, @PluginAttribute("delimiter") Character delimiter, @PluginAttribute("escape") Character escape, @PluginAttribute("quote") Character quote, @PluginAttribute("quoteMode") QuoteMode quoteMode, @PluginAttribute("nullString") String nullString, @PluginAttribute("recordSeparator") String recordSeparator, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute("header") String header, @PluginAttribute("footer") String footer) {
/* 78 */     CSVFormat csvFormat = createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
/* 79 */     return new CsvParameterLayout(config, charset, csvFormat, header, footer);
/*    */   }
/*    */   
/*    */   public CsvParameterLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
/* 83 */     super(config, charset, csvFormat, header, footer);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toSerializable(LogEvent event) {
/* 88 */     Message message = event.getMessage();
/* 89 */     Object[] parameters = message.getParameters();
/* 90 */     StringBuilder buffer = getStringBuilder();
/*    */     try {
/* 92 */       getFormat().printRecord(buffer, parameters);
/* 93 */       return buffer.toString();
/* 94 */     } catch (IOException e) {
/* 95 */       StatusLogger.getLogger().error(message, e);
/* 96 */       return getFormat().getCommentMarker() + " " + e;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\CsvParameterLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */