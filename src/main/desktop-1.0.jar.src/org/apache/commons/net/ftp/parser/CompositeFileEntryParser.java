/*    */ package org.apache.commons.net.ftp.parser;
/*    */ 
/*    */ import org.apache.commons.net.ftp.FTPFile;
/*    */ import org.apache.commons.net.ftp.FTPFileEntryParser;
/*    */ import org.apache.commons.net.ftp.FTPFileEntryParserImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompositeFileEntryParser
/*    */   extends FTPFileEntryParserImpl
/*    */ {
/*    */   private final FTPFileEntryParser[] ftpFileEntryParsers;
/*    */   private FTPFileEntryParser cachedFtpFileEntryParser;
/*    */   
/*    */   public CompositeFileEntryParser(FTPFileEntryParser[] ftpFileEntryParsers) {
/* 40 */     this.cachedFtpFileEntryParser = null;
/* 41 */     this.ftpFileEntryParsers = ftpFileEntryParsers;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FTPFile parseFTPEntry(String listEntry) {
/* 47 */     if (this.cachedFtpFileEntryParser != null) {
/*    */       
/* 49 */       FTPFile matched = this.cachedFtpFileEntryParser.parseFTPEntry(listEntry);
/* 50 */       if (matched != null)
/*    */       {
/* 52 */         return matched;
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 57 */       for (FTPFileEntryParser ftpFileEntryParser : this.ftpFileEntryParsers) {
/*    */         
/* 59 */         FTPFile matched = ftpFileEntryParser.parseFTPEntry(listEntry);
/* 60 */         if (matched != null) {
/*    */           
/* 62 */           this.cachedFtpFileEntryParser = ftpFileEntryParser;
/* 63 */           return matched;
/*    */         } 
/*    */       } 
/*    */     } 
/* 67 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\CompositeFileEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */