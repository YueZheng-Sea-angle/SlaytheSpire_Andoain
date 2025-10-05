/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.util.regex.MatchResult;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ public abstract class RegexFTPFileEntryParserImpl
/*     */   extends FTPFileEntryParserImpl
/*     */ {
/*  41 */   private Pattern pattern = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private MatchResult result = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   protected Matcher _matcher_ = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexFTPFileEntryParserImpl(String regex) {
/*  71 */     compileRegex(regex, 0);
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
/*     */   public RegexFTPFileEntryParserImpl(String regex, int flags) {
/*  91 */     compileRegex(regex, flags);
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
/*     */   public boolean matches(String s) {
/* 103 */     this.result = null;
/* 104 */     this._matcher_ = this.pattern.matcher(s);
/* 105 */     if (this._matcher_.matches()) {
/* 106 */       this.result = this._matcher_.toMatchResult();
/*     */     }
/* 108 */     return (null != this.result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGroupCnt() {
/* 118 */     if (this.result == null) {
/* 119 */       return 0;
/*     */     }
/* 121 */     return this.result.groupCount();
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
/*     */   public String group(int matchnum) {
/* 135 */     if (this.result == null) {
/* 136 */       return null;
/*     */     }
/* 138 */     return this.result.group(matchnum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroupsAsString() {
/* 149 */     StringBuilder b = new StringBuilder();
/* 150 */     for (int i = 1; i <= this.result.groupCount(); i++) {
/* 151 */       b.append(i).append(") ").append(this.result.group(i)).append(System.getProperty("line.separator"));
/*     */     }
/*     */     
/* 154 */     return b.toString();
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
/*     */   public boolean setRegex(String regex) {
/* 166 */     compileRegex(regex, 0);
/* 167 */     return true;
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
/*     */   public boolean setRegex(String regex, int flags) {
/* 181 */     compileRegex(regex, flags);
/* 182 */     return true;
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
/*     */   private void compileRegex(String regex, int flags) {
/*     */     try {
/* 197 */       this.pattern = Pattern.compile(regex, flags);
/* 198 */     } catch (PatternSyntaxException pse) {
/* 199 */       throw new IllegalArgumentException("Unparseable regex supplied: " + regex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\RegexFTPFileEntryParserImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */