/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.net.ftp.FTPFile;
/*     */ import org.apache.commons.net.ftp.FTPFileEntryParserImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MLSxEntryParser
/*     */   extends FTPFileEntryParserImpl
/*     */ {
/*  60 */   private static final MLSxEntryParser PARSER = new MLSxEntryParser();
/*     */   
/*  62 */   private static final HashMap<String, Integer> TYPE_TO_INT = new HashMap<String, Integer>();
/*     */   static {
/*  64 */     TYPE_TO_INT.put("file", Integer.valueOf(0));
/*  65 */     TYPE_TO_INT.put("cdir", Integer.valueOf(1));
/*  66 */     TYPE_TO_INT.put("pdir", Integer.valueOf(1));
/*  67 */     TYPE_TO_INT.put("dir", Integer.valueOf(1));
/*     */   }
/*     */   
/*  70 */   private static int[] UNIX_GROUPS = new int[] { 0, 1, 2 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private static int[][] UNIX_PERMS = new int[][] { {}, { 2 }, { 1 }, { 2, 1 }, { 0 }, { 0, 2 }, { 0, 1 }, { 0, 1, 2 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  98 */     if (entry.startsWith(" ")) {
/*  99 */       if (entry.length() > 1) {
/* 100 */         FTPFile fTPFile = new FTPFile();
/* 101 */         fTPFile.setRawListing(entry);
/* 102 */         fTPFile.setName(entry.substring(1));
/* 103 */         return fTPFile;
/*     */       } 
/* 105 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 109 */     String[] parts = entry.split(" ", 2);
/* 110 */     if (parts.length != 2 || parts[1].length() == 0) {
/* 111 */       return null;
/*     */     }
/* 113 */     String factList = parts[0];
/* 114 */     if (!factList.endsWith(";")) {
/* 115 */       return null;
/*     */     }
/* 117 */     FTPFile file = new FTPFile();
/* 118 */     file.setRawListing(entry);
/* 119 */     file.setName(parts[1]);
/* 120 */     String[] facts = factList.split(";");
/* 121 */     boolean hasUnixMode = parts[0].toLowerCase(Locale.ENGLISH).contains("unix.mode=");
/* 122 */     for (String fact : facts) {
/* 123 */       String[] factparts = fact.split("=", -1);
/*     */ 
/*     */ 
/*     */       
/* 127 */       if (factparts.length != 2) {
/* 128 */         return null;
/*     */       }
/* 130 */       String factname = factparts[0].toLowerCase(Locale.ENGLISH);
/* 131 */       String factvalue = factparts[1];
/* 132 */       if (factvalue.length() != 0) {
/*     */ 
/*     */         
/* 135 */         String valueLowerCase = factvalue.toLowerCase(Locale.ENGLISH);
/* 136 */         if ("size".equals(factname)) {
/* 137 */           file.setSize(Long.parseLong(factvalue));
/*     */         }
/* 139 */         else if ("sizd".equals(factname)) {
/* 140 */           file.setSize(Long.parseLong(factvalue));
/*     */         }
/* 142 */         else if ("modify".equals(factname)) {
/* 143 */           Calendar parsed = parseGMTdateTime(factvalue);
/* 144 */           if (parsed == null) {
/* 145 */             return null;
/*     */           }
/* 147 */           file.setTimestamp(parsed);
/*     */         }
/* 149 */         else if ("type".equals(factname)) {
/* 150 */           Integer intType = TYPE_TO_INT.get(valueLowerCase);
/* 151 */           if (intType == null) {
/* 152 */             file.setType(3);
/*     */           } else {
/* 154 */             file.setType(intType.intValue());
/*     */           }
/*     */         
/* 157 */         } else if (factname.startsWith("unix.")) {
/* 158 */           String unixfact = factname.substring("unix.".length()).toLowerCase(Locale.ENGLISH);
/* 159 */           if ("group".equals(unixfact)) {
/* 160 */             file.setGroup(factvalue);
/* 161 */           } else if ("owner".equals(unixfact)) {
/* 162 */             file.setUser(factvalue);
/* 163 */           } else if ("mode".equals(unixfact)) {
/* 164 */             int off = factvalue.length() - 3;
/* 165 */             for (int i = 0; i < 3; i++) {
/* 166 */               int ch = factvalue.charAt(off + i) - 48;
/* 167 */               if (ch >= 0 && ch <= 7) {
/* 168 */                 for (int p : UNIX_PERMS[ch]) {
/* 169 */                   file.setPermission(UNIX_GROUPS[i], p, true);
/*     */                 }
/*     */               }
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 177 */         else if (!hasUnixMode && "perm".equals(factname)) {
/* 178 */           doUnixPerms(file, valueLowerCase);
/*     */         } 
/*     */       } 
/* 181 */     }  return file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Calendar parseGMTdateTime(String timestamp) {
/*     */     SimpleDateFormat sdf;
/*     */     boolean hasMillis;
/* 194 */     if (timestamp.contains(".")) {
/* 195 */       sdf = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
/* 196 */       hasMillis = true;
/*     */     } else {
/* 198 */       sdf = new SimpleDateFormat("yyyyMMddHHmmss");
/* 199 */       hasMillis = false;
/*     */     } 
/* 201 */     TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */     
/* 203 */     sdf.setTimeZone(GMT);
/* 204 */     GregorianCalendar gc = new GregorianCalendar(GMT);
/* 205 */     ParsePosition pos = new ParsePosition(0);
/* 206 */     sdf.setLenient(false);
/* 207 */     Date parsed = sdf.parse(timestamp, pos);
/* 208 */     if (pos.getIndex() != timestamp.length()) {
/* 209 */       return null;
/*     */     }
/* 211 */     gc.setTime(parsed);
/* 212 */     if (!hasMillis) {
/* 213 */       gc.clear(14);
/*     */     }
/* 215 */     return gc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doUnixPerms(FTPFile file, String valueLowerCase) {
/* 222 */     for (char c : valueLowerCase.toCharArray()) {
/*     */       
/* 224 */       switch (c) {
/*     */         case 'a':
/* 226 */           file.setPermission(0, 1, true);
/*     */           break;
/*     */         case 'c':
/* 229 */           file.setPermission(0, 1, true);
/*     */           break;
/*     */         case 'd':
/* 232 */           file.setPermission(0, 1, true);
/*     */           break;
/*     */         case 'e':
/* 235 */           file.setPermission(0, 0, true);
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 'l':
/* 241 */           file.setPermission(0, 2, true);
/*     */           break;
/*     */         case 'm':
/* 244 */           file.setPermission(0, 1, true);
/*     */           break;
/*     */         case 'p':
/* 247 */           file.setPermission(0, 1, true);
/*     */           break;
/*     */         case 'r':
/* 250 */           file.setPermission(0, 0, true);
/*     */           break;
/*     */         case 'w':
/* 253 */           file.setPermission(0, 1, true);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FTPFile parseEntry(String entry) {
/* 263 */     return PARSER.parseFTPEntry(entry);
/*     */   }
/*     */   
/*     */   public static MLSxEntryParser getInstance() {
/* 267 */     return PARSER;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\MLSxEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */