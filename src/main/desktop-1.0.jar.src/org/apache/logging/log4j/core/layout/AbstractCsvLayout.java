/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.commons.csv.CSVFormat;
/*    */ import org.apache.commons.csv.QuoteMode;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
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
/*    */ public abstract class AbstractCsvLayout
/*    */   extends AbstractStringLayout
/*    */ {
/*    */   protected static final String DEFAULT_CHARSET = "UTF-8";
/*    */   protected static final String DEFAULT_FORMAT = "Default";
/*    */   private static final String CONTENT_TYPE = "text/csv";
/*    */   private final CSVFormat format;
/*    */   
/*    */   protected static CSVFormat createFormat(String format, Character delimiter, Character escape, Character quote, QuoteMode quoteMode, String nullString, String recordSeparator) {
/* 40 */     CSVFormat csvFormat = CSVFormat.valueOf(format);
/* 41 */     if (isNotNul(delimiter)) {
/* 42 */       csvFormat = csvFormat.withDelimiter(delimiter.charValue());
/*    */     }
/* 44 */     if (isNotNul(escape)) {
/* 45 */       csvFormat = csvFormat.withEscape(escape);
/*    */     }
/* 47 */     if (isNotNul(quote)) {
/* 48 */       csvFormat = csvFormat.withQuote(quote);
/*    */     }
/* 50 */     if (quoteMode != null) {
/* 51 */       csvFormat = csvFormat.withQuoteMode(quoteMode);
/*    */     }
/* 53 */     if (nullString != null) {
/* 54 */       csvFormat = csvFormat.withNullString(nullString);
/*    */     }
/* 56 */     if (recordSeparator != null) {
/* 57 */       csvFormat = csvFormat.withRecordSeparator(recordSeparator);
/*    */     }
/* 59 */     return csvFormat;
/*    */   }
/*    */   
/*    */   private static boolean isNotNul(Character character) {
/* 63 */     return (character != null && character.charValue() != '\000');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractCsvLayout(Configuration config, Charset charset, CSVFormat csvFormat, String header, String footer) {
/* 70 */     super(config, charset, 
/* 71 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(header).build(), 
/* 72 */         PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footer).build());
/* 73 */     this.format = csvFormat;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentType() {
/* 78 */     return "text/csv; charset=" + getCharset();
/*    */   }
/*    */   
/*    */   public CSVFormat getFormat() {
/* 82 */     return this.format;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\AbstractCsvLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */