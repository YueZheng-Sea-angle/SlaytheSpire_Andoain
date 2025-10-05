/*     */ package org.apache.logging.log4j.status;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatusData
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4341916115118014017L;
/*     */   private final long timestamp;
/*     */   private final StackTraceElement caller;
/*     */   private final Level level;
/*     */   private final Message msg;
/*     */   private String threadName;
/*     */   private final Throwable throwable;
/*     */   
/*     */   public StatusData(StackTraceElement caller, Level level, Message msg, Throwable t, String threadName) {
/*  55 */     this.timestamp = System.currentTimeMillis();
/*  56 */     this.caller = caller;
/*  57 */     this.level = level;
/*  58 */     this.msg = msg;
/*  59 */     this.throwable = t;
/*  60 */     this.threadName = threadName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTimestamp() {
/*  69 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StackTraceElement getStackTraceElement() {
/*  78 */     return this.caller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/*  87 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message getMessage() {
/*  96 */     return this.msg;
/*     */   }
/*     */   
/*     */   public String getThreadName() {
/* 100 */     if (this.threadName == null) {
/* 101 */       this.threadName = Thread.currentThread().getName();
/*     */     }
/* 103 */     return this.threadName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 112 */     return this.throwable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedStatus() {
/*     */     Throwable t;
/* 121 */     StringBuilder sb = new StringBuilder();
/* 122 */     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
/* 123 */     sb.append(format.format(new Date(this.timestamp)));
/* 124 */     sb.append(' ');
/* 125 */     sb.append(getThreadName());
/* 126 */     sb.append(' ');
/* 127 */     sb.append(this.level.toString());
/* 128 */     sb.append(' ');
/* 129 */     sb.append(this.msg.getFormattedMessage());
/* 130 */     Object[] params = this.msg.getParameters();
/*     */     
/* 132 */     if (this.throwable == null && params != null && params[params.length - 1] instanceof Throwable) {
/* 133 */       t = (Throwable)params[params.length - 1];
/*     */     } else {
/* 135 */       t = this.throwable;
/*     */     } 
/* 137 */     if (t != null) {
/* 138 */       sb.append(' ');
/* 139 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 140 */       t.printStackTrace(new PrintStream(baos));
/* 141 */       sb.append(baos.toString());
/*     */     } 
/* 143 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\status\StatusData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */