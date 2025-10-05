/*     */ package org.apache.commons.net.nntp;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NewGroupsOrNewsQuery
/*     */ {
/*     */   private final String __date;
/*     */   private final String __time;
/*     */   private StringBuffer __distributions;
/*     */   private StringBuffer __newsgroups;
/*     */   private final boolean __isGMT;
/*     */   
/*     */   public NewGroupsOrNewsQuery(Calendar date, boolean gmt) {
/*  63 */     this.__distributions = null;
/*  64 */     this.__newsgroups = null;
/*  65 */     this.__isGMT = gmt;
/*     */     
/*  67 */     StringBuilder buffer = new StringBuilder();
/*     */ 
/*     */     
/*  70 */     int num = date.get(1);
/*  71 */     String str = Integer.toString(num);
/*  72 */     num = str.length();
/*     */     
/*  74 */     if (num >= 2) {
/*  75 */       buffer.append(str.substring(num - 2));
/*     */     } else {
/*  77 */       buffer.append("00");
/*     */     } 
/*     */ 
/*     */     
/*  81 */     num = date.get(2) + 1;
/*  82 */     str = Integer.toString(num);
/*  83 */     num = str.length();
/*     */     
/*  85 */     if (num == 1) {
/*  86 */       buffer.append('0');
/*  87 */       buffer.append(str);
/*  88 */     } else if (num == 2) {
/*  89 */       buffer.append(str);
/*     */     } else {
/*  91 */       buffer.append("01");
/*     */     } 
/*     */ 
/*     */     
/*  95 */     num = date.get(5);
/*  96 */     str = Integer.toString(num);
/*  97 */     num = str.length();
/*     */     
/*  99 */     if (num == 1) {
/* 100 */       buffer.append('0');
/* 101 */       buffer.append(str);
/* 102 */     } else if (num == 2) {
/* 103 */       buffer.append(str);
/*     */     } else {
/* 105 */       buffer.append("01");
/*     */     } 
/*     */     
/* 108 */     this.__date = buffer.toString();
/*     */     
/* 110 */     buffer.setLength(0);
/*     */ 
/*     */     
/* 113 */     num = date.get(11);
/* 114 */     str = Integer.toString(num);
/* 115 */     num = str.length();
/*     */     
/* 117 */     if (num == 1) {
/* 118 */       buffer.append('0');
/* 119 */       buffer.append(str);
/* 120 */     } else if (num == 2) {
/* 121 */       buffer.append(str);
/*     */     } else {
/* 123 */       buffer.append("00");
/*     */     } 
/*     */ 
/*     */     
/* 127 */     num = date.get(12);
/* 128 */     str = Integer.toString(num);
/* 129 */     num = str.length();
/*     */     
/* 131 */     if (num == 1) {
/* 132 */       buffer.append('0');
/* 133 */       buffer.append(str);
/* 134 */     } else if (num == 2) {
/* 135 */       buffer.append(str);
/*     */     } else {
/* 137 */       buffer.append("00");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 142 */     num = date.get(13);
/* 143 */     str = Integer.toString(num);
/* 144 */     num = str.length();
/*     */     
/* 146 */     if (num == 1) {
/* 147 */       buffer.append('0');
/* 148 */       buffer.append(str);
/* 149 */     } else if (num == 2) {
/* 150 */       buffer.append(str);
/*     */     } else {
/* 152 */       buffer.append("00");
/*     */     } 
/*     */     
/* 155 */     this.__time = buffer.toString();
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
/*     */   public void addNewsgroup(String newsgroup) {
/* 171 */     if (this.__newsgroups != null) {
/* 172 */       this.__newsgroups.append(',');
/*     */     } else {
/* 174 */       this.__newsgroups = new StringBuffer();
/*     */     } 
/* 176 */     this.__newsgroups.append(newsgroup);
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
/*     */   public void omitNewsgroup(String newsgroup) {
/* 200 */     addNewsgroup("!" + newsgroup);
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
/*     */   public void addDistribution(String distribution) {
/* 216 */     if (this.__distributions != null) {
/* 217 */       this.__distributions.append(',');
/*     */     } else {
/* 219 */       this.__distributions = new StringBuffer();
/*     */     } 
/* 221 */     this.__distributions.append(distribution);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDate() {
/* 232 */     return this.__date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTime() {
/* 243 */     return this.__time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGMT() {
/* 253 */     return this.__isGMT;
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
/*     */   public String getDistributions() {
/* 265 */     return (this.__distributions == null) ? null : this.__distributions.toString();
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
/*     */   public String getNewsgroups() {
/* 277 */     return (this.__newsgroups == null) ? null : this.__newsgroups.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\NewGroupsOrNewsQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */