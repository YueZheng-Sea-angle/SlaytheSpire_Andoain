/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.regex.MatchResult;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VMSVersioningFTPEntryParser
/*     */   extends VMSFTPEntryParser
/*     */ {
/*     */   private final Pattern _preparse_pattern_;
/*     */   private static final String PRE_PARSE_REGEX = "(.*?);([0-9]+)\\s*.*";
/*     */   
/*     */   public VMSVersioningFTPEntryParser() {
/*  63 */     this(null);
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
/*     */   public VMSVersioningFTPEntryParser(FTPClientConfig config) {
/*  81 */     configure(config);
/*     */ 
/*     */     
/*     */     try {
/*  85 */       this._preparse_pattern_ = Pattern.compile("(.*?);([0-9]+)\\s*.*");
/*     */     }
/*  87 */     catch (PatternSyntaxException pse) {
/*     */       
/*  89 */       throw new IllegalArgumentException("Unparseable regex supplied:  (.*?);([0-9]+)\\s*.*");
/*     */     } 
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
/*     */   public List<String> preParse(List<String> original) {
/* 106 */     HashMap<String, Integer> existingEntries = new HashMap<String, Integer>();
/* 107 */     ListIterator<String> iter = original.listIterator();
/* 108 */     while (iter.hasNext()) {
/* 109 */       String entry = ((String)iter.next()).trim();
/* 110 */       MatchResult result = null;
/* 111 */       Matcher _preparse_matcher_ = this._preparse_pattern_.matcher(entry);
/* 112 */       if (_preparse_matcher_.matches()) {
/* 113 */         result = _preparse_matcher_.toMatchResult();
/* 114 */         String name = result.group(1);
/* 115 */         String version = result.group(2);
/* 116 */         Integer nv = Integer.valueOf(version);
/* 117 */         Integer existing = existingEntries.get(name);
/* 118 */         if (null != existing && 
/* 119 */           nv.intValue() < existing.intValue()) {
/* 120 */           iter.remove();
/*     */           
/*     */           continue;
/*     */         } 
/* 124 */         existingEntries.put(name, nv);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     while (iter.hasPrevious()) {
/* 133 */       String entry = ((String)iter.previous()).trim();
/* 134 */       MatchResult result = null;
/* 135 */       Matcher _preparse_matcher_ = this._preparse_pattern_.matcher(entry);
/* 136 */       if (_preparse_matcher_.matches()) {
/* 137 */         result = _preparse_matcher_.toMatchResult();
/* 138 */         String name = result.group(1);
/* 139 */         String version = result.group(2);
/* 140 */         Integer nv = Integer.valueOf(version);
/* 141 */         Integer existing = existingEntries.get(name);
/* 142 */         if (null != existing && 
/* 143 */           nv.intValue() < existing.intValue()) {
/* 144 */           iter.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 150 */     return original;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isVersioning() {
/* 156 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\VMSVersioningFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */