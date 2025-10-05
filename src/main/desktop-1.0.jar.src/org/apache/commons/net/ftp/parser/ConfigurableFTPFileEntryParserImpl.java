/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.Calendar;
/*     */ import org.apache.commons.net.ftp.Configurable;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
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
/*     */ public abstract class ConfigurableFTPFileEntryParserImpl
/*     */   extends RegexFTPFileEntryParserImpl
/*     */   implements Configurable
/*     */ {
/*     */   private final FTPTimestampParser timestampParser;
/*     */   
/*     */   public ConfigurableFTPFileEntryParserImpl(String regex) {
/*  55 */     super(regex);
/*  56 */     this.timestampParser = new FTPTimestampParserImpl();
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
/*     */   public ConfigurableFTPFileEntryParserImpl(String regex, int flags) {
/*  69 */     super(regex, flags);
/*  70 */     this.timestampParser = new FTPTimestampParserImpl();
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
/*     */   public Calendar parseTimestamp(String timestampStr) throws ParseException {
/*  85 */     return this.timestampParser.parseTimestamp(timestampStr);
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
/*     */   public void configure(FTPClientConfig config) {
/* 104 */     if (this.timestampParser instanceof Configurable) {
/* 105 */       FTPClientConfig defaultCfg = getDefaultConfiguration();
/* 106 */       if (config != null) {
/* 107 */         if (null == config.getDefaultDateFormatStr()) {
/* 108 */           config.setDefaultDateFormatStr(defaultCfg.getDefaultDateFormatStr());
/*     */         }
/* 110 */         if (null == config.getRecentDateFormatStr()) {
/* 111 */           config.setRecentDateFormatStr(defaultCfg.getRecentDateFormatStr());
/*     */         }
/* 113 */         ((Configurable)this.timestampParser).configure(config);
/*     */       } else {
/* 115 */         ((Configurable)this.timestampParser).configure(defaultCfg);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract FTPClientConfig getDefaultConfiguration();
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\ConfigurableFTPFileEntryParserImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */