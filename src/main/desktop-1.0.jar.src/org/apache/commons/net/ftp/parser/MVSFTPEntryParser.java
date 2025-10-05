/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.List;
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
/*     */ public class MVSFTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   static final int UNKNOWN_LIST_TYPE = -1;
/*     */   static final int FILE_LIST_TYPE = 0;
/*     */   static final int MEMBER_LIST_TYPE = 1;
/*     */   static final int UNIX_LIST_TYPE = 2;
/*     */   static final int JES_LEVEL_1_LIST_TYPE = 3;
/*     */   static final int JES_LEVEL_2_LIST_TYPE = 4;
/*  43 */   private int isType = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnixFTPEntryParser unixFTPEntryParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String FILE_LIST_REGEX = "\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+[FV]\\S*\\s+\\S+\\s+\\S+\\s+(PS|PO|PO-E)\\s+(\\S+)\\s*";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String MEMBER_LIST_REGEX = "(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s*";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String JES_LEVEL_1_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String JES_LEVEL_2_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MVSFTPEntryParser() {
/* 246 */     super("");
/* 247 */     configure((FTPClientConfig)null);
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
/* 263 */     boolean isParsed = false;
/* 264 */     FTPFile f = new FTPFile();
/*     */     
/* 266 */     if (this.isType == 0) {
/* 267 */       isParsed = parseFileList(f, entry);
/* 268 */     } else if (this.isType == 1) {
/* 269 */       isParsed = parseMemberList(f, entry);
/* 270 */       if (!isParsed) {
/* 271 */         isParsed = parseSimpleEntry(f, entry);
/*     */       }
/* 273 */     } else if (this.isType == 2) {
/* 274 */       isParsed = parseUnixList(f, entry);
/* 275 */     } else if (this.isType == 3) {
/* 276 */       isParsed = parseJeslevel1List(f, entry);
/* 277 */     } else if (this.isType == 4) {
/* 278 */       isParsed = parseJeslevel2List(f, entry);
/*     */     } 
/*     */     
/* 281 */     if (!isParsed) {
/* 282 */       f = null;
/*     */     }
/*     */     
/* 285 */     return f;
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
/*     */   private boolean parseFileList(FTPFile file, String entry) {
/* 314 */     if (matches(entry)) {
/* 315 */       file.setRawListing(entry);
/* 316 */       String name = group(2);
/* 317 */       String dsorg = group(1);
/* 318 */       file.setName(name);
/*     */ 
/*     */       
/* 321 */       if ("PS".equals(dsorg)) {
/* 322 */         file.setType(0);
/*     */       }
/* 324 */       else if ("PO".equals(dsorg) || "PO-E".equals(dsorg)) {
/*     */         
/* 326 */         file.setType(1);
/*     */       } else {
/*     */         
/* 329 */         return false;
/*     */       } 
/*     */       
/* 332 */       return true;
/*     */     } 
/*     */     
/* 335 */     return false;
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
/*     */   private boolean parseMemberList(FTPFile file, String entry) {
/* 365 */     if (matches(entry)) {
/* 366 */       file.setRawListing(entry);
/* 367 */       String name = group(1);
/* 368 */       String datestr = group(2) + " " + group(3);
/* 369 */       file.setName(name);
/* 370 */       file.setType(0);
/*     */       try {
/* 372 */         file.setTimestamp(parseTimestamp(datestr));
/* 373 */       } catch (ParseException e) {
/* 374 */         e.printStackTrace();
/*     */ 
/*     */         
/* 377 */         return false;
/*     */       } 
/* 379 */       return true;
/*     */     } 
/*     */     
/* 382 */     return false;
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
/*     */   private boolean parseSimpleEntry(FTPFile file, String entry) {
/* 395 */     if (entry != null && entry.trim().length() > 0) {
/* 396 */       file.setRawListing(entry);
/* 397 */       String name = entry.split(" ")[0];
/* 398 */       file.setName(name);
/* 399 */       file.setType(0);
/* 400 */       return true;
/*     */     } 
/* 402 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean parseUnixList(FTPFile file, String entry) {
/* 413 */     file = this.unixFTPEntryParser.parseFTPEntry(entry);
/* 414 */     if (file == null) {
/* 415 */       return false;
/*     */     }
/* 417 */     return true;
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
/*     */   private boolean parseJeslevel1List(FTPFile file, String entry) {
/* 442 */     if (matches(entry) && 
/* 443 */       group(3).equalsIgnoreCase("OUTPUT")) {
/* 444 */       file.setRawListing(entry);
/* 445 */       String name = group(2);
/* 446 */       file.setName(name);
/* 447 */       file.setType(0);
/* 448 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 452 */     return false;
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
/*     */   private boolean parseJeslevel2List(FTPFile file, String entry) {
/* 480 */     if (matches(entry) && 
/* 481 */       group(4).equalsIgnoreCase("OUTPUT")) {
/* 482 */       file.setRawListing(entry);
/* 483 */       String name = group(2);
/* 484 */       file.setName(name);
/* 485 */       file.setType(0);
/* 486 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 490 */     return false;
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
/*     */   public List<String> preParse(List<String> orig) {
/* 507 */     if (orig != null && orig.size() > 0) {
/* 508 */       String header = orig.get(0);
/* 509 */       if (header.indexOf("Volume") >= 0 && header.indexOf("Dsname") >= 0) {
/* 510 */         setType(0);
/* 511 */         setRegex("\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+[FV]\\S*\\s+\\S+\\s+\\S+\\s+(PS|PO|PO-E)\\s+(\\S+)\\s*");
/* 512 */       } else if (header.indexOf("Name") >= 0 && header.indexOf("Id") >= 0) {
/* 513 */         setType(1);
/* 514 */         setRegex("(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s*");
/* 515 */       } else if (header.indexOf("total") == 0) {
/* 516 */         setType(2);
/* 517 */         this.unixFTPEntryParser = new UnixFTPEntryParser();
/* 518 */       } else if (header.indexOf("Spool Files") >= 30) {
/* 519 */         setType(3);
/* 520 */         setRegex("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*");
/* 521 */       } else if (header.indexOf("JOBNAME") == 0 && header.indexOf("JOBID") > 8) {
/*     */         
/* 523 */         setType(4);
/* 524 */         setRegex("(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*");
/*     */       } else {
/* 526 */         setType(-1);
/*     */       } 
/*     */       
/* 529 */       if (this.isType != 3) {
/* 530 */         orig.remove(0);
/*     */       }
/*     */     } 
/*     */     
/* 534 */     return orig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setType(int type) {
/* 542 */     this.isType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FTPClientConfig getDefaultConfiguration() {
/* 550 */     return new FTPClientConfig("MVS", "yyyy/MM/dd HH:mm", null, null, null, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\MVSFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */