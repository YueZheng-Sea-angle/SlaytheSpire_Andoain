/*     */ package org.apache.commons.net.smtp;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleSMTPHeader
/*     */ {
/*     */   private final String __subject;
/*     */   private final String __from;
/*     */   private final String __to;
/*     */   private final StringBuffer __headerFields;
/*     */   private boolean hasHeaderDate;
/*     */   private StringBuffer __cc;
/*     */   
/*     */   public SimpleSMTPHeader(String from, String to, String subject) {
/*  77 */     if (from == null) {
/*  78 */       throw new IllegalArgumentException("From cannot be null");
/*     */     }
/*  80 */     this.__to = to;
/*  81 */     this.__from = from;
/*  82 */     this.__subject = subject;
/*  83 */     this.__headerFields = new StringBuffer();
/*  84 */     this.__cc = null;
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
/* 101 */     if (!this.hasHeaderDate && "Date".equals(headerField)) {
/* 102 */       this.hasHeaderDate = true;
/*     */     }
/* 104 */     this.__headerFields.append(headerField);
/* 105 */     this.__headerFields.append(": ");
/* 106 */     this.__headerFields.append(value);
/* 107 */     this.__headerFields.append('\n');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCC(String address) {
/* 118 */     if (this.__cc == null) {
/* 119 */       this.__cc = new StringBuffer();
/*     */     } else {
/* 121 */       this.__cc.append(", ");
/*     */     } 
/*     */     
/* 124 */     this.__cc.append(address);
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
/*     */   public String toString() {
/* 139 */     StringBuilder header = new StringBuilder();
/*     */     
/* 141 */     String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
/* 142 */     SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
/*     */     
/* 144 */     if (!this.hasHeaderDate) {
/* 145 */       addHeaderField("Date", format.format(new Date()));
/*     */     }
/* 147 */     if (this.__headerFields.length() > 0) {
/* 148 */       header.append(this.__headerFields.toString());
/*     */     }
/*     */     
/* 151 */     header.append("From: ").append(this.__from).append("\n");
/*     */     
/* 153 */     if (this.__to != null) {
/* 154 */       header.append("To: ").append(this.__to).append("\n");
/*     */     }
/*     */     
/* 157 */     if (this.__cc != null)
/*     */     {
/* 159 */       header.append("Cc: ").append(this.__cc.toString()).append("\n");
/*     */     }
/*     */     
/* 162 */     if (this.__subject != null)
/*     */     {
/* 164 */       header.append("Subject: ").append(this.__subject).append("\n");
/*     */     }
/*     */     
/* 167 */     header.append('\n');
/*     */     
/* 169 */     return header.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\smtp\SimpleSMTPHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */