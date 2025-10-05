/*     */ package org.apache.commons.net.pop3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ListIterator;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.commons.net.io.DotTerminatedMessageReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class POP3Client
/*     */   extends POP3
/*     */ {
/*     */   private static POP3MessageInfo __parseStatus(String line) {
/*  59 */     StringTokenizer tokenizer = new StringTokenizer(line);
/*     */     
/*  61 */     if (!tokenizer.hasMoreElements()) {
/*  62 */       return null;
/*     */     }
/*     */     
/*  65 */     int size = 0, num = size;
/*     */ 
/*     */     
/*     */     try {
/*  69 */       num = Integer.parseInt(tokenizer.nextToken());
/*     */       
/*  71 */       if (!tokenizer.hasMoreElements()) {
/*  72 */         return null;
/*     */       }
/*     */       
/*  75 */       size = Integer.parseInt(tokenizer.nextToken());
/*     */     }
/*  77 */     catch (NumberFormatException e) {
/*     */       
/*  79 */       return null;
/*     */     } 
/*     */     
/*  82 */     return new POP3MessageInfo(num, size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static POP3MessageInfo __parseUID(String line) {
/*  90 */     StringTokenizer tokenizer = new StringTokenizer(line);
/*     */     
/*  92 */     if (!tokenizer.hasMoreElements()) {
/*  93 */       return null;
/*     */     }
/*     */     
/*  96 */     int num = 0;
/*     */ 
/*     */     
/*     */     try {
/* 100 */       num = Integer.parseInt(tokenizer.nextToken());
/*     */       
/* 102 */       if (!tokenizer.hasMoreElements()) {
/* 103 */         return null;
/*     */       }
/*     */       
/* 106 */       line = tokenizer.nextToken();
/*     */     }
/* 108 */     catch (NumberFormatException e) {
/*     */       
/* 110 */       return null;
/*     */     } 
/*     */     
/* 113 */     return new POP3MessageInfo(num, line);
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
/*     */   public boolean capa() throws IOException {
/* 125 */     if (sendCommand(12) == 0) {
/* 126 */       getAdditionalReply();
/* 127 */       return true;
/*     */     } 
/* 129 */     return false;
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
/*     */   public boolean login(String username, String password) throws IOException {
/* 152 */     if (getState() != 0) {
/* 153 */       return false;
/*     */     }
/*     */     
/* 156 */     if (sendCommand(0, username) != 0) {
/* 157 */       return false;
/*     */     }
/*     */     
/* 160 */     if (sendCommand(1, password) != 0) {
/* 161 */       return false;
/*     */     }
/*     */     
/* 164 */     setState(1);
/*     */     
/* 166 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean login(String username, String timestamp, String secret) throws IOException, NoSuchAlgorithmException {
/* 211 */     if (getState() != 0) {
/* 212 */       return false;
/*     */     }
/*     */     
/* 215 */     MessageDigest md5 = MessageDigest.getInstance("MD5");
/* 216 */     timestamp = timestamp + secret;
/* 217 */     byte[] digest = md5.digest(timestamp.getBytes(getCharset()));
/* 218 */     StringBuilder digestBuffer = new StringBuilder(128);
/*     */     
/* 220 */     for (int i = 0; i < digest.length; i++) {
/* 221 */       int digit = digest[i] & 0xFF;
/* 222 */       if (digit <= 15) {
/* 223 */         digestBuffer.append("0");
/*     */       }
/* 225 */       digestBuffer.append(Integer.toHexString(digit));
/*     */     } 
/*     */     
/* 228 */     StringBuilder buffer = new StringBuilder(256);
/* 229 */     buffer.append(username);
/* 230 */     buffer.append(' ');
/* 231 */     buffer.append(digestBuffer.toString());
/*     */     
/* 233 */     if (sendCommand(9, buffer.toString()) != 0) {
/* 234 */       return false;
/*     */     }
/*     */     
/* 237 */     setState(1);
/*     */     
/* 239 */     return true;
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
/*     */   public boolean logout() throws IOException {
/* 260 */     if (getState() == 1) {
/* 261 */       setState(2);
/*     */     }
/* 263 */     sendCommand(2);
/* 264 */     return (this._replyCode == 0);
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
/*     */   public boolean noop() throws IOException {
/* 282 */     if (getState() == 1) {
/* 283 */       return (sendCommand(7) == 0);
/*     */     }
/* 285 */     return false;
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
/*     */   public boolean deleteMessage(int messageId) throws IOException {
/* 306 */     if (getState() == 1) {
/* 307 */       return (sendCommand(6, Integer.toString(messageId)) == 0);
/*     */     }
/*     */     
/* 310 */     return false;
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
/*     */   public boolean reset() throws IOException {
/* 327 */     if (getState() == 1) {
/* 328 */       return (sendCommand(8) == 0);
/*     */     }
/* 330 */     return false;
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
/*     */   public POP3MessageInfo status() throws IOException {
/* 350 */     if (getState() != 1) {
/* 351 */       return null;
/*     */     }
/* 353 */     if (sendCommand(3) != 0) {
/* 354 */       return null;
/*     */     }
/* 356 */     return __parseStatus(this._lastReplyLine.substring(3));
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
/*     */   public POP3MessageInfo listMessage(int messageId) throws IOException {
/* 379 */     if (getState() != 1) {
/* 380 */       return null;
/*     */     }
/* 382 */     if (sendCommand(4, Integer.toString(messageId)) != 0)
/*     */     {
/* 384 */       return null;
/*     */     }
/* 386 */     return __parseStatus(this._lastReplyLine.substring(3));
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
/*     */   public POP3MessageInfo[] listMessages() throws IOException {
/* 409 */     if (getState() != 1) {
/* 410 */       return null;
/*     */     }
/* 412 */     if (sendCommand(4) != 0) {
/* 413 */       return null;
/*     */     }
/* 415 */     getAdditionalReply();
/*     */ 
/*     */     
/* 418 */     POP3MessageInfo[] messages = new POP3MessageInfo[this._replyLines.size() - 2];
/*     */     
/* 420 */     ListIterator<String> en = this._replyLines.listIterator(1);
/*     */ 
/*     */     
/* 423 */     for (int line = 0; line < messages.length; line++) {
/* 424 */       messages[line] = __parseStatus(en.next());
/*     */     }
/*     */     
/* 427 */     return messages;
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
/*     */   public POP3MessageInfo listUniqueIdentifier(int messageId) throws IOException {
/* 450 */     if (getState() != 1) {
/* 451 */       return null;
/*     */     }
/* 453 */     if (sendCommand(11, Integer.toString(messageId)) != 0)
/*     */     {
/* 455 */       return null;
/*     */     }
/* 457 */     return __parseUID(this._lastReplyLine.substring(3));
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
/*     */   public POP3MessageInfo[] listUniqueIdentifiers() throws IOException {
/* 480 */     if (getState() != 1) {
/* 481 */       return null;
/*     */     }
/* 483 */     if (sendCommand(11) != 0) {
/* 484 */       return null;
/*     */     }
/* 486 */     getAdditionalReply();
/*     */ 
/*     */     
/* 489 */     POP3MessageInfo[] messages = new POP3MessageInfo[this._replyLines.size() - 2];
/*     */     
/* 491 */     ListIterator<String> en = this._replyLines.listIterator(1);
/*     */ 
/*     */     
/* 494 */     for (int line = 0; line < messages.length; line++) {
/* 495 */       messages[line] = __parseUID(en.next());
/*     */     }
/*     */     
/* 498 */     return messages;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader retrieveMessage(int messageId) throws IOException {
/* 529 */     if (getState() != 1) {
/* 530 */       return null;
/*     */     }
/* 532 */     if (sendCommand(5, Integer.toString(messageId)) != 0) {
/* 533 */       return null;
/*     */     }
/*     */     
/* 536 */     return (Reader)new DotTerminatedMessageReader(this._reader);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reader retrieveMessageTop(int messageId, int numLines) throws IOException {
/* 571 */     if (numLines < 0 || getState() != 1) {
/* 572 */       return null;
/*     */     }
/* 574 */     if (sendCommand(10, Integer.toString(messageId) + " " + Integer.toString(numLines)) != 0)
/*     */     {
/* 576 */       return null;
/*     */     }
/*     */     
/* 579 */     return (Reader)new DotTerminatedMessageReader(this._reader);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\pop3\POP3Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */