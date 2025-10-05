/*      */ package org.apache.commons.net.nntp;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Vector;
/*      */ import org.apache.commons.net.MalformedServerReplyException;
/*      */ import org.apache.commons.net.io.DotTerminatedMessageReader;
/*      */ import org.apache.commons.net.io.DotTerminatedMessageWriter;
/*      */ import org.apache.commons.net.io.Util;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NNTPClient
/*      */   extends NNTP
/*      */ {
/*      */   private void __parseArticlePointer(String reply, ArticleInfo pointer) throws MalformedServerReplyException {
/*  100 */     String[] tokens = reply.split(" ");
/*  101 */     if (tokens.length >= 3) {
/*  102 */       int i = 1;
/*      */ 
/*      */       
/*      */       try {
/*  106 */         pointer.articleNumber = Long.parseLong(tokens[i++]);
/*      */         
/*  108 */         pointer.articleId = tokens[i++];
/*      */         
/*      */         return;
/*  111 */       } catch (NumberFormatException e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  116 */     throw new MalformedServerReplyException("Could not parse article pointer.\nServer reply: " + reply);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void __parseGroupReply(String reply, NewsgroupInfo info) throws MalformedServerReplyException {
/*  131 */     String[] tokens = reply.split(" ");
/*  132 */     if (tokens.length >= 5) {
/*  133 */       int i = 1;
/*      */ 
/*      */       
/*      */       try {
/*  137 */         info._setArticleCount(Long.parseLong(tokens[i++]));
/*      */         
/*  139 */         info._setFirstArticle(Long.parseLong(tokens[i++]));
/*      */         
/*  141 */         info._setLastArticle(Long.parseLong(tokens[i++]));
/*      */         
/*  143 */         info._setNewsgroup(tokens[i++]);
/*      */         
/*  145 */         info._setPostingPermission(0);
/*      */         return;
/*  147 */       } catch (NumberFormatException e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  153 */     throw new MalformedServerReplyException("Could not parse newsgroup info.\nServer reply: " + reply);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static NewsgroupInfo __parseNewsgroupListEntry(String entry) {
/*  161 */     String[] tokens = entry.split(" ");
/*  162 */     if (tokens.length < 4) {
/*  163 */       return null;
/*      */     }
/*  165 */     NewsgroupInfo result = new NewsgroupInfo();
/*      */     
/*  167 */     int i = 0;
/*      */     
/*  169 */     result._setNewsgroup(tokens[i++]);
/*      */ 
/*      */     
/*      */     try {
/*  173 */       long lastNum = Long.parseLong(tokens[i++]);
/*  174 */       long firstNum = Long.parseLong(tokens[i++]);
/*  175 */       result._setFirstArticle(firstNum);
/*  176 */       result._setLastArticle(lastNum);
/*  177 */       if (firstNum == 0L && lastNum == 0L) {
/*  178 */         result._setArticleCount(0L);
/*      */       } else {
/*  180 */         result._setArticleCount(lastNum - firstNum + 1L);
/*      */       } 
/*  182 */     } catch (NumberFormatException e) {
/*  183 */       return null;
/*      */     } 
/*      */     
/*  186 */     switch (tokens[i++].charAt(0))
/*      */     
/*      */     { case 'Y':
/*      */       case 'y':
/*  190 */         result._setPostingPermission(2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  209 */         return result;case 'N': case 'n': result._setPostingPermission(3); return result;case 'M': case 'm': result._setPostingPermission(1); return result; }  result._setPostingPermission(0); return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Article __parseArticleEntry(String line) {
/*  225 */     Article article = new Article();
/*  226 */     article.setSubject(line);
/*  227 */     String[] parts = line.split("\t");
/*  228 */     if (parts.length > 6) {
/*  229 */       int i = 0;
/*      */       try {
/*  231 */         article.setArticleNumber(Long.parseLong(parts[i++]));
/*  232 */         article.setSubject(parts[i++]);
/*  233 */         article.setFrom(parts[i++]);
/*  234 */         article.setDate(parts[i++]);
/*  235 */         article.setArticleId(parts[i++]);
/*  236 */         article.addReference(parts[i++]);
/*  237 */       } catch (NumberFormatException e) {}
/*      */     } 
/*      */ 
/*      */     
/*  241 */     return article;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private NewsgroupInfo[] __readNewsgroupListing() throws IOException {
/*  247 */     DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
/*      */ 
/*      */     
/*  250 */     Vector<NewsgroupInfo> list = new Vector<NewsgroupInfo>(2048);
/*      */     
/*      */     try {
/*      */       String line;
/*  254 */       while ((line = dotTerminatedMessageReader.readLine()) != null) {
/*  255 */         NewsgroupInfo tmp = __parseNewsgroupListEntry(line);
/*  256 */         if (tmp != null) {
/*  257 */           list.addElement(tmp); continue;
/*      */         } 
/*  259 */         throw new MalformedServerReplyException(line);
/*      */       } 
/*      */     } finally {
/*      */       
/*  263 */       dotTerminatedMessageReader.close();
/*      */     } 
/*      */     int size;
/*  266 */     if ((size = list.size()) < 1) {
/*  267 */       return new NewsgroupInfo[0];
/*      */     }
/*      */     
/*  270 */     NewsgroupInfo[] info = new NewsgroupInfo[size];
/*  271 */     list.copyInto((Object[])info);
/*      */     
/*  273 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BufferedReader __retrieve(int command, String articleId, ArticleInfo pointer) throws IOException {
/*  280 */     if (articleId != null) {
/*      */       
/*  282 */       if (!NNTPReply.isPositiveCompletion(sendCommand(command, articleId))) {
/*  283 */         return null;
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  288 */     else if (!NNTPReply.isPositiveCompletion(sendCommand(command))) {
/*  289 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  294 */     if (pointer != null) {
/*  295 */       __parseArticlePointer(getReplyString(), pointer);
/*      */     }
/*      */     
/*  298 */     return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BufferedReader __retrieve(int command, long articleNumber, ArticleInfo pointer) throws IOException {
/*  305 */     if (!NNTPReply.isPositiveCompletion(sendCommand(command, Long.toString(articleNumber))))
/*      */     {
/*  307 */       return null;
/*      */     }
/*      */     
/*  310 */     if (pointer != null) {
/*  311 */       __parseArticlePointer(getReplyString(), pointer);
/*      */     }
/*      */     
/*  314 */     return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticle(String articleId, ArticleInfo pointer) throws IOException {
/*  361 */     return __retrieve(0, articleId, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader retrieveArticle(String articleId) throws IOException {
/*  375 */     return retrieveArticle(articleId, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader retrieveArticle() throws IOException {
/*  387 */     return retrieveArticle((String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticle(long articleNumber, ArticleInfo pointer) throws IOException {
/*  432 */     return __retrieve(0, articleNumber, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticle(long articleNumber) throws IOException {
/*  444 */     return retrieveArticle(articleNumber, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleHeader(String articleId, ArticleInfo pointer) throws IOException {
/*  492 */     return __retrieve(3, articleId, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader retrieveArticleHeader(String articleId) throws IOException {
/*  505 */     return retrieveArticleHeader(articleId, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader retrieveArticleHeader() throws IOException {
/*  516 */     return retrieveArticleHeader((String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleHeader(long articleNumber, ArticleInfo pointer) throws IOException {
/*  562 */     return __retrieve(3, articleNumber, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleHeader(long articleNumber) throws IOException {
/*  575 */     return retrieveArticleHeader(articleNumber, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleBody(String articleId, ArticleInfo pointer) throws IOException {
/*  623 */     return __retrieve(1, articleId, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader retrieveArticleBody(String articleId) throws IOException {
/*  637 */     return retrieveArticleBody(articleId, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader retrieveArticleBody() throws IOException {
/*  649 */     return retrieveArticleBody((String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleBody(long articleNumber, ArticleInfo pointer) throws IOException {
/*  695 */     return __retrieve(1, articleNumber, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleBody(long articleNumber) throws IOException {
/*  707 */     return retrieveArticleBody(articleNumber, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectNewsgroup(String newsgroup, NewsgroupInfo info) throws IOException {
/*  732 */     if (!NNTPReply.isPositiveCompletion(group(newsgroup))) {
/*  733 */       return false;
/*      */     }
/*      */     
/*  736 */     if (info != null) {
/*  737 */       __parseGroupReply(getReplyString(), info);
/*      */     }
/*      */     
/*  740 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectNewsgroup(String newsgroup) throws IOException {
/*  751 */     return selectNewsgroup(newsgroup, (NewsgroupInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String listHelp() throws IOException {
/*  768 */     if (!NNTPReply.isInformational(help())) {
/*  769 */       return null;
/*      */     }
/*      */     
/*  772 */     StringWriter help = new StringWriter();
/*  773 */     DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
/*  774 */     Util.copyReader((Reader)dotTerminatedMessageReader, help);
/*  775 */     dotTerminatedMessageReader.close();
/*  776 */     help.close();
/*  777 */     return help.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] listOverviewFmt() throws IOException {
/*  788 */     if (!NNTPReply.isPositiveCompletion(sendCommand("LIST", "OVERVIEW.FMT"))) {
/*  789 */       return null;
/*      */     }
/*      */     
/*  792 */     DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
/*      */     
/*  794 */     ArrayList<String> list = new ArrayList<String>(); String line;
/*  795 */     while ((line = dotTerminatedMessageReader.readLine()) != null) {
/*  796 */       list.add(line);
/*      */     }
/*  798 */     dotTerminatedMessageReader.close();
/*  799 */     return list.<String>toArray(new String[list.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectArticle(String articleId, ArticleInfo pointer) throws IOException {
/*  830 */     if (articleId != null) {
/*  831 */       if (!NNTPReply.isPositiveCompletion(stat(articleId))) {
/*  832 */         return false;
/*      */       }
/*      */     }
/*  835 */     else if (!NNTPReply.isPositiveCompletion(stat())) {
/*  836 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  840 */     if (pointer != null) {
/*  841 */       __parseArticlePointer(getReplyString(), pointer);
/*      */     }
/*      */     
/*  844 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectArticle(String articleId) throws IOException {
/*  855 */     return selectArticle(articleId, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectArticle(ArticleInfo pointer) throws IOException {
/*  867 */     return selectArticle((String)null, pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectArticle(long articleNumber, ArticleInfo pointer) throws IOException {
/*  899 */     if (!NNTPReply.isPositiveCompletion(stat(articleNumber))) {
/*  900 */       return false;
/*      */     }
/*      */     
/*  903 */     if (pointer != null) {
/*  904 */       __parseArticlePointer(getReplyString(), pointer);
/*      */     }
/*      */     
/*  907 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectArticle(long articleNumber) throws IOException {
/*  917 */     return selectArticle(articleNumber, (ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectPreviousArticle(ArticleInfo pointer) throws IOException {
/*  948 */     if (!NNTPReply.isPositiveCompletion(last())) {
/*  949 */       return false;
/*      */     }
/*      */     
/*  952 */     if (pointer != null) {
/*  953 */       __parseArticlePointer(getReplyString(), pointer);
/*      */     }
/*      */     
/*  956 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectPreviousArticle() throws IOException {
/*  964 */     return selectPreviousArticle((ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectNextArticle(ArticleInfo pointer) throws IOException {
/*  994 */     if (!NNTPReply.isPositiveCompletion(next())) {
/*  995 */       return false;
/*      */     }
/*      */     
/*  998 */     if (pointer != null) {
/*  999 */       __parseArticlePointer(getReplyString(), pointer);
/*      */     }
/*      */     
/* 1002 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean selectNextArticle() throws IOException {
/* 1011 */     return selectNextArticle((ArticleInfo)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NewsgroupInfo[] listNewsgroups() throws IOException {
/* 1037 */     if (!NNTPReply.isPositiveCompletion(list())) {
/* 1038 */       return null;
/*      */     }
/*      */     
/* 1041 */     return __readNewsgroupListing();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<String> iterateNewsgroupListing() throws IOException {
/* 1062 */     if (NNTPReply.isPositiveCompletion(list())) {
/* 1063 */       return new ReplyIterator(this._reader_);
/*      */     }
/* 1065 */     throw new IOException("LIST command failed: " + getReplyString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<NewsgroupInfo> iterateNewsgroups() throws IOException {
/* 1086 */     return new NewsgroupIterator(iterateNewsgroupListing());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NewsgroupInfo[] listNewsgroups(String wildmat) throws IOException {
/* 1104 */     if (!NNTPReply.isPositiveCompletion(listActive(wildmat))) {
/* 1105 */       return null;
/*      */     }
/* 1107 */     return __readNewsgroupListing();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<String> iterateNewsgroupListing(String wildmat) throws IOException {
/* 1124 */     if (NNTPReply.isPositiveCompletion(listActive(wildmat))) {
/* 1125 */       return new ReplyIterator(this._reader_);
/*      */     }
/* 1127 */     throw new IOException("LIST ACTIVE " + wildmat + " command failed: " + getReplyString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<NewsgroupInfo> iterateNewsgroups(String wildmat) throws IOException {
/* 1143 */     return new NewsgroupIterator(iterateNewsgroupListing(wildmat));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NewsgroupInfo[] listNewNewsgroups(NewGroupsOrNewsQuery query) throws IOException {
/* 1171 */     if (!NNTPReply.isPositiveCompletion(newgroups(query.getDate(), query.getTime(), query.isGMT(), query.getDistributions())))
/*      */     {
/*      */ 
/*      */       
/* 1175 */       return null;
/*      */     }
/*      */     
/* 1178 */     return __readNewsgroupListing();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<String> iterateNewNewsgroupListing(NewGroupsOrNewsQuery query) throws IOException {
/* 1201 */     if (NNTPReply.isPositiveCompletion(newgroups(query.getDate(), query.getTime(), query.isGMT(), query.getDistributions())))
/*      */     {
/*      */       
/* 1204 */       return new ReplyIterator(this._reader_);
/*      */     }
/* 1206 */     throw new IOException("NEWGROUPS command failed: " + getReplyString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<NewsgroupInfo> iterateNewNewsgroups(NewGroupsOrNewsQuery query) throws IOException {
/* 1229 */     return new NewsgroupIterator(iterateNewNewsgroupListing(query));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] listNewNews(NewGroupsOrNewsQuery query) throws IOException {
/* 1261 */     if (!NNTPReply.isPositiveCompletion(newnews(query.getNewsgroups(), query.getDate(), query.getTime(), query.isGMT(), query.getDistributions())))
/*      */     {
/*      */       
/* 1264 */       return null;
/*      */     }
/*      */     
/* 1267 */     Vector<String> list = new Vector<String>();
/* 1268 */     DotTerminatedMessageReader dotTerminatedMessageReader = new DotTerminatedMessageReader(this._reader_);
/*      */     
/*      */     try {
/*      */       String line;
/* 1272 */       while ((line = dotTerminatedMessageReader.readLine()) != null) {
/* 1273 */         list.addElement(line);
/*      */       }
/*      */     } finally {
/* 1276 */       dotTerminatedMessageReader.close();
/*      */     } 
/*      */     
/* 1279 */     int size = list.size();
/* 1280 */     if (size < 1) {
/* 1281 */       return new String[0];
/*      */     }
/*      */     
/* 1284 */     String[] result = new String[size];
/* 1285 */     list.copyInto((Object[])result);
/*      */     
/* 1287 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<String> iterateNewNews(NewGroupsOrNewsQuery query) throws IOException {
/* 1314 */     if (NNTPReply.isPositiveCompletion(newnews(query.getNewsgroups(), query.getDate(), query.getTime(), query.isGMT(), query.getDistributions())))
/*      */     {
/*      */       
/* 1317 */       return new ReplyIterator(this._reader_);
/*      */     }
/* 1319 */     throw new IOException("NEWNEWS command failed: " + getReplyString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean completePendingCommand() throws IOException {
/* 1356 */     return NNTPReply.isPositiveCompletion(getReply());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer postArticle() throws IOException {
/* 1399 */     if (!NNTPReply.isPositiveIntermediate(post())) {
/* 1400 */       return null;
/*      */     }
/*      */     
/* 1403 */     return (Writer)new DotTerminatedMessageWriter(this._writer_);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer forwardArticle(String articleId) throws IOException {
/* 1409 */     if (!NNTPReply.isPositiveIntermediate(ihave(articleId))) {
/* 1410 */       return null;
/*      */     }
/*      */     
/* 1413 */     return (Writer)new DotTerminatedMessageWriter(this._writer_);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean logout() throws IOException {
/* 1428 */     return NNTPReply.isPositiveCompletion(quit());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean authenticate(String username, String password) throws IOException {
/* 1445 */     int replyCode = authinfoUser(username);
/*      */     
/* 1447 */     if (replyCode == 381) {
/*      */       
/* 1449 */       replyCode = authinfoPass(password);
/*      */       
/* 1451 */       if (replyCode == 281) {
/*      */         
/* 1453 */         this._isAllowedToPost = true;
/* 1454 */         return true;
/*      */       } 
/*      */     } 
/* 1457 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BufferedReader __retrieveArticleInfo(String articleRange) throws IOException {
/* 1474 */     if (!NNTPReply.isPositiveCompletion(xover(articleRange))) {
/* 1475 */       return null;
/*      */     }
/*      */     
/* 1478 */     return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleInfo(long articleNumber) throws IOException {
/* 1490 */     return __retrieveArticleInfo(Long.toString(articleNumber));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveArticleInfo(long lowArticleNumber, long highArticleNumber) throws IOException {
/* 1506 */     return __retrieveArticleInfo(lowArticleNumber + "-" + highArticleNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterable<Article> iterateArticleInfo(long lowArticleNumber, long highArticleNumber) throws IOException {
/* 1524 */     BufferedReader info = retrieveArticleInfo(lowArticleNumber, highArticleNumber);
/* 1525 */     if (info == null) {
/* 1526 */       throw new IOException("XOVER command failed: " + getReplyString());
/*      */     }
/*      */     
/* 1529 */     return new ArticleIterator(new ReplyIterator(info, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private BufferedReader __retrieveHeader(String header, String articleRange) throws IOException {
/* 1547 */     if (!NNTPReply.isPositiveCompletion(xhdr(header, articleRange))) {
/* 1548 */       return null;
/*      */     }
/*      */     
/* 1551 */     return (BufferedReader)new DotTerminatedMessageReader(this._reader_);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveHeader(String header, long articleNumber) throws IOException {
/* 1565 */     return __retrieveHeader(header, Long.toString(articleNumber));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BufferedReader retrieveHeader(String header, long lowArticleNumber, long highArticleNumber) throws IOException {
/* 1582 */     return __retrieveHeader(header, lowArticleNumber + "-" + highArticleNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveHeader(String header, int lowArticleNumber, int highArticleNumber) throws IOException {
/* 1607 */     return retrieveHeader(header, lowArticleNumber, highArticleNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleInfo(int lowArticleNumber, int highArticleNumber) throws IOException {
/* 1619 */     return retrieveArticleInfo(lowArticleNumber, highArticleNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveHeader(String a, int b) throws IOException {
/* 1631 */     return retrieveHeader(a, b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean selectArticle(int a, ArticlePointer ap) throws IOException {
/* 1643 */     ArticleInfo ai = __ap2ai(ap);
/* 1644 */     boolean b = selectArticle(a, ai);
/* 1645 */     __ai2ap(ai, ap);
/* 1646 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleInfo(int lowArticleNumber) throws IOException {
/* 1657 */     return retrieveArticleInfo(lowArticleNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean selectArticle(int a) throws IOException {
/* 1668 */     return selectArticle(a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleHeader(int a) throws IOException {
/* 1679 */     return retrieveArticleHeader(a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleHeader(int a, ArticlePointer ap) throws IOException {
/* 1691 */     ArticleInfo ai = __ap2ai(ap);
/* 1692 */     Reader rdr = retrieveArticleHeader(a, ai);
/* 1693 */     __ai2ap(ai, ap);
/* 1694 */     return rdr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleBody(int a) throws IOException {
/* 1705 */     return retrieveArticleBody(a);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticle(int articleNumber, ArticlePointer pointer) throws IOException {
/* 1718 */     ArticleInfo ai = __ap2ai(pointer);
/* 1719 */     Reader rdr = retrieveArticle(articleNumber, ai);
/* 1720 */     __ai2ap(ai, pointer);
/* 1721 */     return rdr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticle(int articleNumber) throws IOException {
/* 1733 */     return retrieveArticle(articleNumber);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleBody(int a, ArticlePointer ap) throws IOException {
/* 1745 */     ArticleInfo ai = __ap2ai(ap);
/* 1746 */     Reader rdr = retrieveArticleBody(a, ai);
/* 1747 */     __ai2ap(ai, ap);
/* 1748 */     return rdr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticle(String articleId, ArticlePointer pointer) throws IOException {
/* 1761 */     ArticleInfo ai = __ap2ai(pointer);
/* 1762 */     Reader rdr = retrieveArticle(articleId, ai);
/* 1763 */     __ai2ap(ai, pointer);
/* 1764 */     return rdr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleBody(String articleId, ArticlePointer pointer) throws IOException {
/* 1777 */     ArticleInfo ai = __ap2ai(pointer);
/* 1778 */     Reader rdr = retrieveArticleBody(articleId, ai);
/* 1779 */     __ai2ap(ai, pointer);
/* 1780 */     return rdr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Reader retrieveArticleHeader(String articleId, ArticlePointer pointer) throws IOException {
/* 1793 */     ArticleInfo ai = __ap2ai(pointer);
/* 1794 */     Reader rdr = retrieveArticleHeader(articleId, ai);
/* 1795 */     __ai2ap(ai, pointer);
/* 1796 */     return rdr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean selectArticle(String articleId, ArticlePointer pointer) throws IOException {
/* 1809 */     ArticleInfo ai = __ap2ai(pointer);
/* 1810 */     boolean b = selectArticle(articleId, ai);
/* 1811 */     __ai2ap(ai, pointer);
/* 1812 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean selectArticle(ArticlePointer pointer) throws IOException {
/* 1824 */     ArticleInfo ai = __ap2ai(pointer);
/* 1825 */     boolean b = selectArticle(ai);
/* 1826 */     __ai2ap(ai, pointer);
/* 1827 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean selectNextArticle(ArticlePointer pointer) throws IOException {
/* 1839 */     ArticleInfo ai = __ap2ai(pointer);
/* 1840 */     boolean b = selectNextArticle(ai);
/* 1841 */     __ai2ap(ai, pointer);
/* 1842 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public boolean selectPreviousArticle(ArticlePointer pointer) throws IOException {
/* 1854 */     ArticleInfo ai = __ap2ai(pointer);
/* 1855 */     boolean b = selectPreviousArticle(ai);
/* 1856 */     __ai2ap(ai, pointer);
/* 1857 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ArticleInfo __ap2ai(ArticlePointer ap) {
/* 1863 */     if (ap == null) {
/* 1864 */       return null;
/*      */     }
/* 1866 */     ArticleInfo ai = new ArticleInfo();
/* 1867 */     return ai;
/*      */   }
/*      */ 
/*      */   
/*      */   private void __ai2ap(ArticleInfo ai, ArticlePointer ap) {
/* 1872 */     if (ap != null) {
/* 1873 */       ap.articleId = ai.articleId;
/* 1874 */       ap.articleNumber = (int)ai.articleNumber;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\NNTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */