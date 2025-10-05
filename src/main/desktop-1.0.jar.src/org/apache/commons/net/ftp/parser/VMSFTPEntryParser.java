/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.text.ParseException;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ import org.apache.commons.net.ftp.FTPFile;
/*     */ import org.apache.commons.net.ftp.FTPFileEntryParser;
/*     */ import org.apache.commons.net.ftp.FTPListParseEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VMSFTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   private static final String DEFAULT_DATE_FORMAT = "d-MMM-yyyy HH:mm:ss";
/*     */   private static final String REGEX = "(.*?;[0-9]+)\\s*(\\d+)/\\d+\\s*(\\S+)\\s+(\\S+)\\s+\\[(([0-9$A-Za-z_]+)|([0-9$A-Za-z_]+),([0-9$a-zA-Z_]+))\\]?\\s*\\([a-zA-Z]*,([a-zA-Z]*),([a-zA-Z]*),([a-zA-Z]*)\\)";
/*     */   
/*     */   public VMSFTPEntryParser() {
/*  75 */     this((FTPClientConfig)null);
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
/*     */   public VMSFTPEntryParser(FTPClientConfig config) {
/*  92 */     super("(.*?;[0-9]+)\\s*(\\d+)/\\d+\\s*(\\S+)\\s+(\\S+)\\s+\\[(([0-9$A-Za-z_]+)|([0-9$A-Za-z_]+),([0-9$a-zA-Z_]+))\\]?\\s*\\([a-zA-Z]*,([a-zA-Z]*),([a-zA-Z]*),([a-zA-Z]*)\\)");
/*  93 */     configure(config);
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
/* 110 */     long longBlock = 512L;
/*     */     
/* 112 */     if (matches(entry)) {
/*     */       String grp, user;
/* 114 */       FTPFile f = new FTPFile();
/* 115 */       f.setRawListing(entry);
/* 116 */       String name = group(1);
/* 117 */       String size = group(2);
/* 118 */       String datestr = group(3) + " " + group(4);
/* 119 */       String owner = group(5);
/* 120 */       String[] permissions = new String[3];
/* 121 */       permissions[0] = group(9);
/* 122 */       permissions[1] = group(10);
/* 123 */       permissions[2] = group(11);
/*     */       
/*     */       try {
/* 126 */         f.setTimestamp(parseTimestamp(datestr));
/*     */       }
/* 128 */       catch (ParseException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 136 */       StringTokenizer t = new StringTokenizer(owner, ",");
/* 137 */       switch (t.countTokens()) {
/*     */         case 1:
/* 139 */           grp = null;
/* 140 */           user = t.nextToken();
/*     */           break;
/*     */         case 2:
/* 143 */           grp = t.nextToken();
/* 144 */           user = t.nextToken();
/*     */           break;
/*     */         default:
/* 147 */           grp = null;
/* 148 */           user = null;
/*     */           break;
/*     */       } 
/* 151 */       if (name.lastIndexOf(".DIR") != -1) {
/*     */         
/* 153 */         f.setType(1);
/*     */       }
/*     */       else {
/*     */         
/* 157 */         f.setType(0);
/*     */       } 
/*     */ 
/*     */       
/* 161 */       if (isVersioning()) {
/*     */         
/* 163 */         f.setName(name);
/*     */       }
/*     */       else {
/*     */         
/* 167 */         name = name.substring(0, name.lastIndexOf(";"));
/* 168 */         f.setName(name);
/*     */       } 
/*     */ 
/*     */       
/* 172 */       long sizeInBytes = Long.parseLong(size) * longBlock;
/* 173 */       f.setSize(sizeInBytes);
/*     */       
/* 175 */       f.setGroup(grp);
/* 176 */       f.setUser(user);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 184 */       for (int access = 0; access < 3; access++) {
/*     */         
/* 186 */         String permission = permissions[access];
/*     */         
/* 188 */         f.setPermission(access, 0, (permission.indexOf('R') >= 0));
/* 189 */         f.setPermission(access, 1, (permission.indexOf('W') >= 0));
/* 190 */         f.setPermission(access, 2, (permission.indexOf('E') >= 0));
/*     */       } 
/*     */       
/* 193 */       return f;
/*     */     } 
/* 195 */     return null;
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
/*     */   public String readNextEntry(BufferedReader reader) throws IOException {
/* 214 */     String line = reader.readLine();
/* 215 */     StringBuilder entry = new StringBuilder();
/* 216 */     while (line != null) {
/*     */       
/* 218 */       if (line.startsWith("Directory") || line.startsWith("Total")) {
/* 219 */         line = reader.readLine();
/*     */         
/*     */         continue;
/*     */       } 
/* 223 */       entry.append(line);
/* 224 */       if (line.trim().endsWith(")")) {
/*     */         break;
/*     */       }
/*     */       
/* 228 */       line = reader.readLine();
/*     */     } 
/* 230 */     return (entry.length() == 0) ? null : entry.toString();
/*     */   }
/*     */   
/*     */   protected boolean isVersioning() {
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FTPClientConfig getDefaultConfiguration() {
/* 245 */     return new FTPClientConfig("VMS", "d-MMM-yyyy HH:mm:ss", null, null, null, null);
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
/*     */   @Deprecated
/*     */   public FTPFile[] parseFileList(InputStream listStream) throws IOException {
/* 262 */     FTPListParseEngine engine = new FTPListParseEngine((FTPFileEntryParser)this);
/* 263 */     engine.readServerList(listStream, null);
/* 264 */     return engine.getFiles();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\VMSFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */