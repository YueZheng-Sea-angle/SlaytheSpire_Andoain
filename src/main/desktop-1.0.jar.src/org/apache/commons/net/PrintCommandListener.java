/*     */ package org.apache.commons.net;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrintCommandListener
/*     */   implements ProtocolCommandListener
/*     */ {
/*     */   private final PrintWriter __writer;
/*     */   private final boolean __nologin;
/*     */   private final char __eolMarker;
/*     */   private final boolean __directionMarker;
/*     */   
/*     */   public PrintCommandListener(PrintStream stream) {
/*  47 */     this(new PrintWriter(stream));
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
/*     */   public PrintCommandListener(PrintStream stream, boolean suppressLogin) {
/*  60 */     this(new PrintWriter(stream), suppressLogin);
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
/*     */   public PrintCommandListener(PrintStream stream, boolean suppressLogin, char eolMarker) {
/*  74 */     this(new PrintWriter(stream), suppressLogin, eolMarker);
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
/*     */   public PrintCommandListener(PrintStream stream, boolean suppressLogin, char eolMarker, boolean showDirection) {
/*  89 */     this(new PrintWriter(stream), suppressLogin, eolMarker, showDirection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintCommandListener(PrintWriter writer) {
/*  99 */     this(writer, false);
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
/*     */   public PrintCommandListener(PrintWriter writer, boolean suppressLogin) {
/* 112 */     this(writer, suppressLogin, false);
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
/*     */   public PrintCommandListener(PrintWriter writer, boolean suppressLogin, char eolMarker) {
/* 127 */     this(writer, suppressLogin, eolMarker, false);
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
/*     */   public PrintCommandListener(PrintWriter writer, boolean suppressLogin, char eolMarker, boolean showDirection) {
/* 143 */     this.__writer = writer;
/* 144 */     this.__nologin = suppressLogin;
/* 145 */     this.__eolMarker = eolMarker;
/* 146 */     this.__directionMarker = showDirection;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void protocolCommandSent(ProtocolCommandEvent event) {
/* 152 */     if (this.__directionMarker) {
/* 153 */       this.__writer.print("> ");
/*     */     }
/* 155 */     if (this.__nologin) {
/* 156 */       String cmd = event.getCommand();
/* 157 */       if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
/* 158 */         this.__writer.print(cmd);
/* 159 */         this.__writer.println(" *******");
/*     */       } else {
/* 161 */         String IMAP_LOGIN = "LOGIN";
/* 162 */         if ("LOGIN".equalsIgnoreCase(cmd)) {
/* 163 */           String msg = event.getMessage();
/* 164 */           msg = msg.substring(0, msg.indexOf("LOGIN") + "LOGIN".length());
/* 165 */           this.__writer.print(msg);
/* 166 */           this.__writer.println(" *******");
/*     */         } else {
/* 168 */           this.__writer.print(getPrintableString(event.getMessage()));
/*     */         } 
/*     */       } 
/*     */     } else {
/* 172 */       this.__writer.print(getPrintableString(event.getMessage()));
/*     */     } 
/* 174 */     this.__writer.flush();
/*     */   }
/*     */   
/*     */   private String getPrintableString(String msg) {
/* 178 */     if (this.__eolMarker == '\000') {
/* 179 */       return msg;
/*     */     }
/* 181 */     int pos = msg.indexOf("\r\n");
/* 182 */     if (pos > 0) {
/* 183 */       StringBuilder sb = new StringBuilder();
/* 184 */       sb.append(msg.substring(0, pos));
/* 185 */       sb.append(this.__eolMarker);
/* 186 */       sb.append(msg.substring(pos));
/* 187 */       return sb.toString();
/*     */     } 
/* 189 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void protocolReplyReceived(ProtocolCommandEvent event) {
/* 195 */     if (this.__directionMarker) {
/* 196 */       this.__writer.print("< ");
/*     */     }
/* 198 */     this.__writer.print(event.getMessage());
/* 199 */     this.__writer.flush();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\PrintCommandListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */