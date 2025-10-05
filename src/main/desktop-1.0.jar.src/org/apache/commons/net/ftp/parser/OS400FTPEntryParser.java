/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.io.File;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OS400FTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   private static final String DEFAULT_DATE_FORMAT = "yy/MM/dd HH:mm:ss";
/*     */   private static final String REGEX = "(\\S+)\\s+(?:(\\d+)\\s+)?(?:(\\S+)\\s+(\\S+)\\s+)?(\\*STMF|\\*DIR|\\*FILE|\\*MEM)\\s+(?:(\\S+)\\s*)?";
/*     */   
/*     */   public OS400FTPEntryParser() {
/* 259 */     this((FTPClientConfig)null);
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
/*     */   public OS400FTPEntryParser(FTPClientConfig config) {
/* 276 */     super("(\\S+)\\s+(?:(\\d+)\\s+)?(?:(\\S+)\\s+(\\S+)\\s+)?(\\*STMF|\\*DIR|\\*FILE|\\*MEM)\\s+(?:(\\S+)\\s*)?");
/* 277 */     configure(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPFile parseFTPEntry(String entry) {
/* 285 */     FTPFile file = new FTPFile();
/* 286 */     file.setRawListing(entry);
/*     */ 
/*     */     
/* 289 */     if (matches(entry)) {
/*     */       int type;
/* 291 */       String usr = group(1);
/* 292 */       String filesize = group(2);
/* 293 */       String datestr = "";
/* 294 */       if (!isNullOrEmpty(group(3)) || !isNullOrEmpty(group(4)))
/*     */       {
/* 296 */         datestr = group(3) + " " + group(4);
/*     */       }
/* 298 */       String typeStr = group(5);
/* 299 */       String name = group(6);
/*     */       
/* 301 */       boolean mustScanForPathSeparator = true;
/*     */ 
/*     */       
/*     */       try {
/* 305 */         file.setTimestamp(parseTimestamp(datestr));
/*     */       }
/* 307 */       catch (ParseException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 313 */       if (typeStr.equalsIgnoreCase("*STMF")) {
/*     */         
/* 315 */         type = 0;
/* 316 */         if (isNullOrEmpty(filesize) || isNullOrEmpty(name))
/*     */         {
/* 318 */           return null;
/*     */         }
/*     */       }
/* 321 */       else if (typeStr.equalsIgnoreCase("*DIR")) {
/*     */         
/* 323 */         type = 1;
/* 324 */         if (isNullOrEmpty(filesize) || isNullOrEmpty(name))
/*     */         {
/* 326 */           return null;
/*     */         }
/*     */       }
/* 329 */       else if (typeStr.equalsIgnoreCase("*FILE")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 338 */         if (name != null && name.toUpperCase().endsWith(".SAVF"))
/*     */         {
/* 340 */           mustScanForPathSeparator = false;
/* 341 */           type = 0;
/*     */         }
/*     */         else
/*     */         {
/* 345 */           return null;
/*     */         }
/*     */       
/* 348 */       } else if (typeStr.equalsIgnoreCase("*MEM")) {
/*     */         
/* 350 */         mustScanForPathSeparator = false;
/* 351 */         type = 0;
/*     */         
/* 353 */         if (isNullOrEmpty(name))
/*     */         {
/* 355 */           return null;
/*     */         }
/* 357 */         if (!isNullOrEmpty(filesize) || !isNullOrEmpty(datestr))
/*     */         {
/* 359 */           return null;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 367 */         name = name.replace('/', File.separatorChar);
/*     */       }
/*     */       else {
/*     */         
/* 371 */         type = 3;
/*     */       } 
/*     */       
/* 374 */       file.setType(type);
/*     */       
/* 376 */       file.setUser(usr);
/*     */ 
/*     */       
/*     */       try {
/* 380 */         file.setSize(Long.parseLong(filesize));
/*     */       }
/* 382 */       catch (NumberFormatException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 387 */       if (name.endsWith("/"))
/*     */       {
/* 389 */         name = name.substring(0, name.length() - 1);
/*     */       }
/* 391 */       if (mustScanForPathSeparator) {
/*     */         
/* 393 */         int pos = name.lastIndexOf('/');
/* 394 */         if (pos > -1)
/*     */         {
/* 396 */           name = name.substring(pos + 1);
/*     */         }
/*     */       } 
/*     */       
/* 400 */       file.setName(name);
/*     */       
/* 402 */       return file;
/*     */     } 
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isNullOrEmpty(String string) {
/* 415 */     if (string == null || string.length() == 0) {
/* 416 */       return true;
/*     */     }
/* 418 */     return false;
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
/* 429 */     return new FTPClientConfig("OS/400", "yy/MM/dd HH:mm:ss", null, null, null, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\OS400FTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */