/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.util.Calendar;
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
/*     */ public class EnterpriseUnixFTPEntryParser
/*     */   extends RegexFTPFileEntryParserImpl
/*     */ {
/*     */   private static final String MONTHS = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
/*     */   private static final String REGEX = "(([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z]))(\\S*)\\s*(\\S+)\\s*(\\S*)\\s*(\\d*)\\s*(\\d*)\\s*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s*((?:[012]\\d*)|(?:3[01]))\\s*((\\d\\d\\d\\d)|((?:[01]\\d)|(?:2[0123])):([012345]\\d))\\s(\\S*)(\\s*.*)";
/*     */   
/*     */   public EnterpriseUnixFTPEntryParser() {
/*  73 */     super("(([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z]))(\\S*)\\s*(\\S+)\\s*(\\S*)\\s*(\\d*)\\s*(\\d*)\\s*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s*((?:[012]\\d*)|(?:3[01]))\\s*((\\d\\d\\d\\d)|((?:[01]\\d)|(?:2[0123])):([012345]\\d))\\s(\\S*)(\\s*.*)");
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
/*  90 */     FTPFile file = new FTPFile();
/*  91 */     file.setRawListing(entry);
/*     */     
/*  93 */     if (matches(entry)) {
/*     */       
/*  95 */       String usr = group(14);
/*  96 */       String grp = group(15);
/*  97 */       String filesize = group(16);
/*  98 */       String mo = group(17);
/*  99 */       String da = group(18);
/* 100 */       String yr = group(20);
/* 101 */       String hr = group(21);
/* 102 */       String min = group(22);
/* 103 */       String name = group(23);
/*     */       
/* 105 */       file.setType(0);
/* 106 */       file.setUser(usr);
/* 107 */       file.setGroup(grp);
/*     */       
/*     */       try {
/* 110 */         file.setSize(Long.parseLong(filesize));
/*     */       }
/* 112 */       catch (NumberFormatException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       Calendar cal = Calendar.getInstance();
/* 118 */       cal.set(14, 0);
/* 119 */       cal.set(13, 0);
/* 120 */       cal.set(12, 0);
/* 121 */       cal.set(11, 0);
/*     */       
/* 123 */       int pos = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)".indexOf(mo);
/* 124 */       int month = pos / 4;
/*     */       
/*     */       try {
/*     */         int missingUnit;
/*     */         
/* 129 */         if (yr != null) {
/*     */ 
/*     */           
/* 132 */           cal.set(1, Integer.parseInt(yr));
/* 133 */           missingUnit = 11;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 138 */           missingUnit = 13;
/* 139 */           int year = cal.get(1);
/*     */ 
/*     */ 
/*     */           
/* 143 */           if (cal.get(2) < month)
/*     */           {
/* 145 */             year--;
/*     */           }
/* 147 */           cal.set(1, year);
/* 148 */           cal.set(11, Integer.parseInt(hr));
/* 149 */           cal.set(12, Integer.parseInt(min));
/*     */         } 
/* 151 */         cal.set(2, month);
/* 152 */         cal.set(5, Integer.parseInt(da));
/* 153 */         cal.clear(missingUnit);
/* 154 */         file.setTimestamp(cal);
/*     */       }
/* 156 */       catch (NumberFormatException e) {}
/*     */ 
/*     */ 
/*     */       
/* 160 */       file.setName(name);
/*     */       
/* 162 */       return file;
/*     */     } 
/* 164 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\EnterpriseUnixFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */