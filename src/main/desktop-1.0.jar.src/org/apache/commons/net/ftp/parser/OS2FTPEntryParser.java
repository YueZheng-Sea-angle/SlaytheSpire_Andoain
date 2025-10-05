/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParseException;
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
/*     */ public class OS2FTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   private static final String DEFAULT_DATE_FORMAT = "MM-dd-yy HH:mm";
/*     */   private static final String REGEX = "\\s*([0-9]+)\\s*(\\s+|[A-Z]+)\\s*(DIR|\\s+)\\s*(\\S+)\\s+(\\S+)\\s+(\\S.*)";
/*     */   
/*     */   public OS2FTPEntryParser() {
/*  56 */     this((FTPClientConfig)null);
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
/*     */   public OS2FTPEntryParser(FTPClientConfig config) {
/*  73 */     super("\\s*([0-9]+)\\s*(\\s+|[A-Z]+)\\s*(DIR|\\s+)\\s*(\\S+)\\s+(\\S+)\\s+(\\S.*)");
/*  74 */     configure(config);
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
/*     */   public FTPFile parseFTPEntry(String entry) {
/*  91 */     FTPFile f = new FTPFile();
/*  92 */     if (matches(entry)) {
/*     */       
/*  94 */       String size = group(1);
/*  95 */       String attrib = group(2);
/*  96 */       String dirString = group(3);
/*  97 */       String datestr = group(4) + " " + group(5);
/*  98 */       String name = group(6);
/*     */       
/*     */       try {
/* 101 */         f.setTimestamp(parseTimestamp(datestr));
/*     */       }
/* 103 */       catch (ParseException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       if (dirString.trim().equals("DIR") || attrib.trim().equals("DIR")) {
/*     */         
/* 112 */         f.setType(1);
/*     */       }
/*     */       else {
/*     */         
/* 116 */         f.setType(0);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 121 */       f.setName(name.trim());
/*     */ 
/*     */       
/* 124 */       f.setSize(Long.parseLong(size.trim()));
/*     */       
/* 126 */       return f;
/*     */     } 
/* 128 */     return null;
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
/*     */   protected FTPClientConfig getDefaultConfiguration() {
/* 140 */     return new FTPClientConfig("OS/2", "MM-dd-yy HH:mm", null, null, null, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\OS2FTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */