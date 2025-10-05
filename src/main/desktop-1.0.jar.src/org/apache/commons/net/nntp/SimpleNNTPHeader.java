/*     */ package org.apache.commons.net.nntp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleNNTPHeader
/*     */ {
/*     */   private final String __subject;
/*     */   private final String __from;
/*     */   private final StringBuilder __newsgroups;
/*     */   private final StringBuilder __headerFields;
/*     */   private int __newsgroupCount;
/*     */   
/*     */   public SimpleNNTPHeader(String from, String subject) {
/*  65 */     this.__from = from;
/*  66 */     this.__subject = subject;
/*  67 */     this.__newsgroups = new StringBuilder();
/*  68 */     this.__headerFields = new StringBuilder();
/*  69 */     this.__newsgroupCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNewsgroup(String newsgroup) {
/*  80 */     if (this.__newsgroupCount++ > 0) {
/*  81 */       this.__newsgroups.append(',');
/*     */     }
/*  83 */     this.__newsgroups.append(newsgroup);
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
/*     */   public void addHeaderField(String headerField, String value) {
/* 100 */     this.__headerFields.append(headerField);
/* 101 */     this.__headerFields.append(": ");
/* 102 */     this.__headerFields.append(value);
/* 103 */     this.__headerFields.append('\n');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFromAddress() {
/* 114 */     return this.__from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 124 */     return this.__subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNewsgroups() {
/* 135 */     return this.__newsgroups.toString();
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
/*     */   public String toString() {
/* 148 */     StringBuilder header = new StringBuilder();
/*     */     
/* 150 */     header.append("From: ");
/* 151 */     header.append(this.__from);
/* 152 */     header.append("\nNewsgroups: ");
/* 153 */     header.append(this.__newsgroups.toString());
/* 154 */     header.append("\nSubject: ");
/* 155 */     header.append(this.__subject);
/* 156 */     header.append('\n');
/* 157 */     if (this.__headerFields.length() > 0) {
/* 158 */       header.append(this.__headerFields.toString());
/*     */     }
/* 160 */     header.append('\n');
/*     */     
/* 162 */     return header.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\SimpleNNTPHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */