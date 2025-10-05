/*     */ package org.apache.commons.net.nntp;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Article
/*     */   implements Threadable
/*     */ {
/*     */   private long articleNumber;
/*     */   private String subject;
/*     */   private String date;
/*     */   private String articleId;
/*     */   private String simplifiedSubject;
/*     */   private String from;
/*     */   private ArrayList<String> references;
/*     */   private boolean isReply = false;
/*     */   public Article kid;
/*     */   public Article next;
/*     */   
/*     */   public Article() {
/*  40 */     this.articleNumber = -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReference(String msgId) {
/*  48 */     if (msgId == null || msgId.length() == 0) {
/*     */       return;
/*     */     }
/*  51 */     if (this.references == null) {
/*  52 */       this.references = new ArrayList<String>();
/*     */     }
/*  54 */     this.isReply = true;
/*  55 */     for (String s : msgId.split(" ")) {
/*  56 */       this.references.add(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getReferences() {
/*  65 */     if (this.references == null) {
/*  66 */       return new String[0];
/*     */     }
/*  68 */     return this.references.<String>toArray(new String[this.references.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void simplifySubject() {
/*  76 */     int start = 0;
/*  77 */     String subject = getSubject();
/*  78 */     int len = subject.length();
/*     */     
/*  80 */     boolean done = false;
/*     */     
/*  82 */     while (!done) {
/*  83 */       done = true;
/*     */ 
/*     */ 
/*     */       
/*  87 */       while (start < len && subject.charAt(start) == ' ') {
/*  88 */         start++;
/*     */       }
/*     */       
/*  91 */       if (start < len - 2 && (subject.charAt(start) == 'r' || subject.charAt(start) == 'R') && (subject.charAt(start + 1) == 'e' || subject.charAt(start + 1) == 'E'))
/*     */       {
/*     */ 
/*     */         
/*  95 */         if (subject.charAt(start + 2) == ':') {
/*  96 */           start += 3;
/*  97 */           done = false;
/*  98 */         } else if (start < len - 2 && (subject.charAt(start + 2) == '[' || subject.charAt(start + 2) == '(')) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 103 */           int i = start + 3;
/*     */           
/* 105 */           while (i < len && subject.charAt(i) >= '0' && subject.charAt(i) <= '9') {
/* 106 */             i++;
/*     */           }
/*     */           
/* 109 */           if (i < len - 1 && (subject.charAt(i) == ']' || subject.charAt(i) == ')') && subject.charAt(i + 1) == ':') {
/*     */ 
/*     */ 
/*     */             
/* 113 */             start = i + 2;
/* 114 */             done = false;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 119 */       if ("(no subject)".equals(this.simplifiedSubject)) {
/* 120 */         this.simplifiedSubject = "";
/*     */       }
/*     */       
/* 123 */       int end = len;
/*     */       
/* 125 */       while (end > start && subject.charAt(end - 1) < ' ') {
/* 126 */         end--;
/*     */       }
/*     */       
/* 129 */       if (start == 0 && end == len) {
/* 130 */         this.simplifiedSubject = subject; continue;
/*     */       } 
/* 132 */       this.simplifiedSubject = subject.substring(start, end);
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
/*     */   public static void printThread(Article article) {
/* 144 */     printThread(article, 0, System.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void printThread(Article article, PrintStream ps) {
/* 155 */     printThread(article, 0, ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void printThread(Article article, int depth) {
/* 165 */     printThread(article, depth, System.out);
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
/*     */   public static void printThread(Article article, int depth, PrintStream ps) {
/* 177 */     for (int i = 0; i < depth; i++) {
/* 178 */       ps.print("==>");
/*     */     }
/* 180 */     ps.println(article.getSubject() + "\t" + article.getFrom() + "\t" + article.getArticleId());
/* 181 */     if (article.kid != null) {
/* 182 */       printThread(article.kid, depth + 1);
/*     */     }
/* 184 */     if (article.next != null) {
/* 185 */       printThread(article.next, depth);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getArticleId() {
/* 190 */     return this.articleId;
/*     */   }
/*     */   
/*     */   public long getArticleNumberLong() {
/* 194 */     return this.articleNumber;
/*     */   }
/*     */   
/*     */   public String getDate() {
/* 198 */     return this.date;
/*     */   }
/*     */   
/*     */   public String getFrom() {
/* 202 */     return this.from;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 206 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setArticleId(String string) {
/* 210 */     this.articleId = string;
/*     */   }
/*     */   
/*     */   public void setArticleNumber(long l) {
/* 214 */     this.articleNumber = l;
/*     */   }
/*     */   
/*     */   public void setDate(String string) {
/* 218 */     this.date = string;
/*     */   }
/*     */   
/*     */   public void setFrom(String string) {
/* 222 */     this.from = string;
/*     */   }
/*     */   
/*     */   public void setSubject(String string) {
/* 226 */     this.subject = string;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDummy() {
/* 232 */     return (this.articleNumber == -1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public String messageThreadId() {
/* 237 */     return this.articleId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] messageThreadReferences() {
/* 242 */     return getReferences();
/*     */   }
/*     */ 
/*     */   
/*     */   public String simplifiedSubject() {
/* 247 */     if (this.simplifiedSubject == null) {
/* 248 */       simplifySubject();
/*     */     }
/* 250 */     return this.simplifiedSubject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean subjectIsReply() {
/* 256 */     return this.isReply;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChild(Threadable child) {
/* 262 */     this.kid = (Article)child;
/* 263 */     flushSubjectCache();
/*     */   }
/*     */   
/*     */   private void flushSubjectCache() {
/* 267 */     this.simplifiedSubject = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNext(Threadable next) {
/* 273 */     this.next = (Article)next;
/* 274 */     flushSubjectCache();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Threadable makeDummy() {
/* 280 */     return new Article();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 285 */     return this.articleNumber + " " + this.articleId + " " + this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getArticleNumber() {
/* 292 */     return (int)this.articleNumber;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void setArticleNumber(int a) {
/* 297 */     this.articleNumber = a;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addHeaderField(String name, String val) {}
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\Article.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */