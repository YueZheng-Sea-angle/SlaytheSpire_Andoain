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
/*     */ public class NetwareFTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   private static final String DEFAULT_DATE_FORMAT = "MMM dd yyyy";
/*     */   private static final String DEFAULT_RECENT_DATE_FORMAT = "MMM dd HH:mm";
/*     */   private static final String REGEX = "(d|-){1}\\s+\\[([-A-Z]+)\\]\\s+(\\S+)\\s+(\\d+)\\s+(\\S+\\s+\\S+\\s+((\\d+:\\d+)|(\\d{4})))\\s+(.*)";
/*     */   
/*     */   public NetwareFTPEntryParser() {
/*  67 */     this((FTPClientConfig)null);
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
/*     */   public NetwareFTPEntryParser(FTPClientConfig config) {
/*  83 */     super("(d|-){1}\\s+\\[([-A-Z]+)\\]\\s+(\\S+)\\s+(\\d+)\\s+(\\S+\\s+\\S+\\s+((\\d+:\\d+)|(\\d{4})))\\s+(.*)");
/*  84 */     configure(config);
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
/* 117 */     FTPFile f = new FTPFile();
/* 118 */     if (matches(entry)) {
/* 119 */       String dirString = group(1);
/* 120 */       String attrib = group(2);
/* 121 */       String user = group(3);
/* 122 */       String size = group(4);
/* 123 */       String datestr = group(5);
/* 124 */       String name = group(9);
/*     */       
/*     */       try {
/* 127 */         f.setTimestamp(parseTimestamp(datestr));
/* 128 */       } catch (ParseException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       if (dirString.trim().equals("d")) {
/* 134 */         f.setType(1);
/*     */       } else {
/*     */         
/* 137 */         f.setType(0);
/*     */       } 
/*     */       
/* 140 */       f.setUser(user);
/*     */ 
/*     */       
/* 143 */       f.setName(name.trim());
/*     */ 
/*     */       
/* 146 */       f.setSize(Long.parseLong(size.trim()));
/*     */ 
/*     */ 
/*     */       
/* 150 */       if (attrib.indexOf("R") != -1) {
/* 151 */         f.setPermission(0, 0, true);
/*     */       }
/*     */       
/* 154 */       if (attrib.indexOf("W") != -1) {
/* 155 */         f.setPermission(0, 1, true);
/*     */       }
/*     */ 
/*     */       
/* 159 */       return f;
/*     */     } 
/* 161 */     return null;
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
/* 173 */     return new FTPClientConfig("NETWARE", "MMM dd yyyy", "MMM dd HH:mm", null, null, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\NetwareFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */