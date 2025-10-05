/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class UnixFTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   static final String DEFAULT_DATE_FORMAT = "MMM d yyyy";
/*     */   static final String DEFAULT_RECENT_DATE_FORMAT = "MMM d HH:mm";
/*     */   static final String NUMERIC_DATE_FORMAT = "yyyy-MM-dd HH:mm";
/*  60 */   public static final FTPClientConfig NUMERIC_DATE_CONFIG = new FTPClientConfig("UNIX", "yyyy-MM-dd HH:mm", null, null, null, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String REGEX = "([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s*(\\d+)\\s+(?:(\\S+(?:\\s\\S+)*?)\\s+)?(?:(\\S+(?:\\s\\S+)*)\\s+)?(\\d+(?:,\\s*\\d+)?)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3}))\\s+(\\d+(?::\\d+)?)\\s(.*)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean trimLeadingSpaces;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnixFTPEntryParser() {
/* 144 */     this((FTPClientConfig)null);
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
/*     */   public UnixFTPEntryParser(FTPClientConfig config) {
/* 161 */     this(config, false);
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
/*     */   public UnixFTPEntryParser(FTPClientConfig config, boolean trimLeadingSpaces) {
/* 179 */     super("([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s*(\\d+)\\s+(?:(\\S+(?:\\s\\S+)*?)\\s+)?(?:(\\S+(?:\\s\\S+)*)\\s+)?(\\d+(?:,\\s*\\d+)?)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3}))\\s+(\\d+(?::\\d+)?)\\s(.*)");
/* 180 */     configure(config);
/* 181 */     this.trimLeadingSpaces = trimLeadingSpaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> preParse(List<String> original) {
/* 189 */     ListIterator<String> iter = original.listIterator();
/* 190 */     while (iter.hasNext()) {
/* 191 */       String entry = iter.next();
/* 192 */       if (entry.matches("^total \\d+$")) {
/* 193 */         iter.remove();
/*     */       }
/*     */     } 
/* 196 */     return original;
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
/*     */   public FTPFile parseFTPEntry(String entry) {
/* 211 */     FTPFile file = new FTPFile();
/* 212 */     file.setRawListing(entry);
/*     */     
/* 214 */     boolean isDevice = false;
/*     */     
/* 216 */     if (matches(entry)) {
/*     */       int type;
/* 218 */       String typeStr = group(1);
/* 219 */       String hardLinkCount = group(15);
/* 220 */       String usr = group(16);
/* 221 */       String grp = group(17);
/* 222 */       String filesize = group(18);
/* 223 */       String datestr = group(19) + " " + group(20);
/* 224 */       String name = group(21);
/* 225 */       if (this.trimLeadingSpaces) {
/* 226 */         name = name.replaceFirst("^\\s+", "");
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 231 */         file.setTimestamp(parseTimestamp(datestr));
/*     */       }
/* 233 */       catch (ParseException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 242 */       switch (typeStr.charAt(0)) {
/*     */         
/*     */         case 'd':
/* 245 */           type = 1;
/*     */           break;
/*     */         case 'e':
/* 248 */           type = 2;
/*     */           break;
/*     */         case 'l':
/* 251 */           type = 2;
/*     */           break;
/*     */         case 'b':
/*     */         case 'c':
/* 255 */           isDevice = true;
/* 256 */           type = 0;
/*     */           break;
/*     */         case '-':
/*     */         case 'f':
/* 260 */           type = 0;
/*     */           break;
/*     */         default:
/* 263 */           type = 3;
/*     */           break;
/*     */       } 
/* 266 */       file.setType(type);
/*     */       
/* 268 */       int g = 4;
/* 269 */       for (int access = 0; access < 3; access++, g += 4) {
/*     */ 
/*     */         
/* 272 */         file.setPermission(access, 0, !group(g).equals("-"));
/*     */         
/* 274 */         file.setPermission(access, 1, !group(g + 1).equals("-"));
/*     */ 
/*     */         
/* 277 */         String execPerm = group(g + 2);
/* 278 */         if (!execPerm.equals("-") && !Character.isUpperCase(execPerm.charAt(0))) {
/*     */           
/* 280 */           file.setPermission(access, 2, true);
/*     */         }
/*     */         else {
/*     */           
/* 284 */           file.setPermission(access, 2, false);
/*     */         } 
/*     */       } 
/*     */       
/* 288 */       if (!isDevice) {
/*     */         
/*     */         try {
/*     */           
/* 292 */           file.setHardLinkCount(Integer.parseInt(hardLinkCount));
/*     */         }
/* 294 */         catch (NumberFormatException e) {}
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 300 */       file.setUser(usr);
/* 301 */       file.setGroup(grp);
/*     */ 
/*     */       
/*     */       try {
/* 305 */         file.setSize(Long.parseLong(filesize));
/*     */       }
/* 307 */       catch (NumberFormatException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       if (type == 2) {
/*     */ 
/*     */         
/* 317 */         int end = name.indexOf(" -> ");
/*     */         
/* 319 */         if (end == -1)
/*     */         {
/* 321 */           file.setName(name);
/*     */         }
/*     */         else
/*     */         {
/* 325 */           file.setName(name.substring(0, end));
/* 326 */           file.setLink(name.substring(end + 4));
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 332 */         file.setName(name);
/*     */       } 
/* 334 */       return file;
/*     */     } 
/* 336 */     return null;
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
/* 347 */     return new FTPClientConfig("UNIX", "MMM d yyyy", "MMM d HH:mm", null, null, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\UnixFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */