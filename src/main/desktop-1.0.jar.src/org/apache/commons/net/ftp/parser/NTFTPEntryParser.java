/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import org.apache.commons.net.ftp.Configurable;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ import org.apache.commons.net.ftp.FTPFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NTFTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   private static final String DEFAULT_DATE_FORMAT = "MM-dd-yy hh:mma";
/*     */   private static final String DEFAULT_DATE_FORMAT2 = "MM-dd-yy kk:mm";
/*     */   private final FTPTimestampParser timestampParser;
/*     */   private static final String REGEX = "(\\S+)\\s+(\\S+)\\s+(?:(<DIR>)|([0-9]+))\\s+(\\S.*)";
/*     */   
/*     */   public NTFTPEntryParser() {
/*  61 */     this((FTPClientConfig)null);
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
/*     */   public NTFTPEntryParser(FTPClientConfig config) {
/*  78 */     super("(\\S+)\\s+(\\S+)\\s+(?:(<DIR>)|([0-9]+))\\s+(\\S.*)", 32);
/*  79 */     configure(config);
/*  80 */     FTPClientConfig config2 = new FTPClientConfig("WINDOWS", "MM-dd-yy kk:mm", null, null, null, null);
/*     */ 
/*     */ 
/*     */     
/*  84 */     config2.setDefaultDateFormatStr("MM-dd-yy kk:mm");
/*  85 */     this.timestampParser = new FTPTimestampParserImpl();
/*  86 */     ((Configurable)this.timestampParser).configure(config2);
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
/*     */   public FTPFile parseFTPEntry(String entry) {
/* 102 */     FTPFile f = new FTPFile();
/* 103 */     f.setRawListing(entry);
/*     */     
/* 105 */     if (matches(entry)) {
/*     */       
/* 107 */       String datestr = group(1) + " " + group(2);
/* 108 */       String dirString = group(3);
/* 109 */       String size = group(4);
/* 110 */       String name = group(5);
/*     */       
/*     */       try {
/* 113 */         f.setTimestamp(parseTimestamp(datestr));
/*     */       }
/* 115 */       catch (ParseException e) {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 120 */           f.setTimestamp(this.timestampParser.parseTimestamp(datestr));
/*     */         }
/* 122 */         catch (ParseException e2) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       if (null == name || name.equals(".") || name.equals(".."))
/*     */       {
/* 130 */         return null;
/*     */       }
/* 132 */       f.setName(name);
/*     */ 
/*     */       
/* 135 */       if ("<DIR>".equals(dirString)) {
/*     */         
/* 137 */         f.setType(1);
/* 138 */         f.setSize(0L);
/*     */       }
/*     */       else {
/*     */         
/* 142 */         f.setType(0);
/* 143 */         if (null != size)
/*     */         {
/* 145 */           f.setSize(Long.parseLong(size));
/*     */         }
/*     */       } 
/* 148 */       return f;
/*     */     } 
/* 150 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPClientConfig getDefaultConfiguration() {
/* 161 */     return new FTPClientConfig("WINDOWS", "MM-dd-yy hh:mma", null, null, null, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\NTFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */