/*     */ package org.apache.commons.net.imap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IMAPClient
/*     */   extends IMAP
/*     */ {
/*     */   private static final char DQUOTE = '"';
/*     */   private static final String DQUOTE_S = "\"";
/*     */   
/*     */   public boolean capability() throws IOException {
/*  41 */     return doCommand(IMAPCommand.CAPABILITY);
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
/*     */   public boolean noop() throws IOException {
/*  53 */     return doCommand(IMAPCommand.NOOP);
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
/*     */   public boolean logout() throws IOException {
/*  67 */     return doCommand(IMAPCommand.LOGOUT);
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
/*  90 */     if (getState() != IMAP.IMAPState.NOT_AUTH_STATE)
/*     */     {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     if (!doCommand(IMAPCommand.LOGIN, username + " " + password))
/*     */     {
/*  97 */       return false;
/*     */     }
/*     */     
/* 100 */     setState(IMAP.IMAPState.AUTH_STATE);
/*     */     
/* 102 */     return true;
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
/*     */   public boolean select(String mailboxName) throws IOException {
/* 115 */     return doCommand(IMAPCommand.SELECT, mailboxName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean examine(String mailboxName) throws IOException {
/* 126 */     return doCommand(IMAPCommand.EXAMINE, mailboxName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean create(String mailboxName) throws IOException {
/* 137 */     return doCommand(IMAPCommand.CREATE, mailboxName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean delete(String mailboxName) throws IOException {
/* 148 */     return doCommand(IMAPCommand.DELETE, mailboxName);
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
/*     */   public boolean rename(String oldMailboxName, String newMailboxName) throws IOException {
/* 160 */     return doCommand(IMAPCommand.RENAME, oldMailboxName + " " + newMailboxName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean subscribe(String mailboxName) throws IOException {
/* 171 */     return doCommand(IMAPCommand.SUBSCRIBE, mailboxName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean unsubscribe(String mailboxName) throws IOException {
/* 182 */     return doCommand(IMAPCommand.UNSUBSCRIBE, mailboxName);
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
/*     */   public boolean list(String refName, String mailboxName) throws IOException {
/* 194 */     return doCommand(IMAPCommand.LIST, refName + " " + mailboxName);
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
/*     */   public boolean lsub(String refName, String mailboxName) throws IOException {
/* 206 */     return doCommand(IMAPCommand.LSUB, refName + " " + mailboxName);
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
/*     */   public boolean status(String mailboxName, String[] itemNames) throws IOException {
/* 218 */     if (itemNames == null || itemNames.length < 1) {
/* 219 */       throw new IllegalArgumentException("STATUS command requires at least one data item name");
/*     */     }
/*     */     
/* 222 */     StringBuilder sb = new StringBuilder();
/* 223 */     sb.append(mailboxName);
/*     */     
/* 225 */     sb.append(" (");
/* 226 */     for (int i = 0; i < itemNames.length; i++) {
/*     */       
/* 228 */       if (i > 0) {
/* 229 */         sb.append(" ");
/*     */       }
/* 231 */       sb.append(itemNames[i]);
/*     */     } 
/* 233 */     sb.append(")");
/*     */     
/* 235 */     return doCommand(IMAPCommand.STATUS, sb.toString());
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
/*     */   public boolean append(String mailboxName, String flags, String datetime, String message) throws IOException {
/* 250 */     StringBuilder args = new StringBuilder(mailboxName);
/* 251 */     if (flags != null) {
/* 252 */       args.append(" ").append(flags);
/*     */     }
/* 254 */     if (datetime != null) {
/* 255 */       args.append(" ");
/* 256 */       if (datetime.charAt(0) == '"') {
/* 257 */         args.append(datetime);
/*     */       } else {
/* 259 */         args.append('"').append(datetime).append('"');
/*     */       } 
/*     */     } 
/* 262 */     args.append(" ");
/*     */     
/* 264 */     if (message.startsWith("\"") && message.endsWith("\"")) {
/* 265 */       args.append(message);
/* 266 */       return doCommand(IMAPCommand.APPEND, args.toString());
/*     */     } 
/* 268 */     args.append('{').append(message.length()).append('}');
/* 269 */     int status = sendCommand(IMAPCommand.APPEND, args.toString());
/* 270 */     return (IMAPReply.isContinuation(status) && IMAPReply.isSuccess(sendData(message)));
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
/*     */   @Deprecated
/*     */   public boolean append(String mailboxName, String flags, String datetime) throws IOException {
/* 287 */     String args = mailboxName;
/* 288 */     if (flags != null) {
/* 289 */       args = args + " " + flags;
/*     */     }
/* 291 */     if (datetime != null) {
/* 292 */       if (datetime.charAt(0) == '{') {
/* 293 */         args = args + " " + datetime;
/*     */       } else {
/* 295 */         args = args + " {" + datetime + "}";
/*     */       } 
/*     */     }
/* 298 */     return doCommand(IMAPCommand.APPEND, args);
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
/*     */   @Deprecated
/*     */   public boolean append(String mailboxName) throws IOException {
/* 312 */     return append(mailboxName, (String)null, (String)null);
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
/*     */   public boolean check() throws IOException {
/* 324 */     return doCommand(IMAPCommand.CHECK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean close() throws IOException {
/* 334 */     return doCommand(IMAPCommand.CLOSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean expunge() throws IOException {
/* 344 */     return doCommand(IMAPCommand.EXPUNGE);
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
/*     */   public boolean search(String charset, String criteria) throws IOException {
/* 356 */     String args = "";
/* 357 */     if (charset != null) {
/* 358 */       args = args + "CHARSET " + charset;
/*     */     }
/* 360 */     args = args + criteria;
/* 361 */     return doCommand(IMAPCommand.SEARCH, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean search(String criteria) throws IOException {
/* 372 */     return search((String)null, criteria);
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
/*     */   public boolean fetch(String sequenceSet, String itemNames) throws IOException {
/* 388 */     return doCommand(IMAPCommand.FETCH, sequenceSet + " " + itemNames);
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
/*     */   public boolean store(String sequenceSet, String itemNames, String itemValues) throws IOException {
/* 402 */     return doCommand(IMAPCommand.STORE, sequenceSet + " " + itemNames + " " + itemValues);
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
/*     */   public boolean copy(String sequenceSet, String mailboxName) throws IOException {
/* 414 */     return doCommand(IMAPCommand.COPY, sequenceSet + " " + mailboxName);
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
/*     */   public boolean uid(String command, String commandArgs) throws IOException {
/* 426 */     return doCommand(IMAPCommand.UID, command + " " + commandArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum STATUS_DATA_ITEMS
/*     */   {
/* 435 */     MESSAGES,
/*     */     
/* 437 */     RECENT,
/*     */     
/* 439 */     UIDNEXT,
/*     */     
/* 441 */     UIDVALIDITY,
/*     */     
/* 443 */     UNSEEN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum SEARCH_CRITERIA
/*     */   {
/* 452 */     ALL,
/*     */     
/* 454 */     ANSWERED,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 459 */     BCC,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 464 */     BEFORE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 469 */     BODY,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     CC,
/*     */     
/* 476 */     DELETED,
/*     */     
/* 478 */     DRAFT,
/*     */     
/* 480 */     FLAGGED,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 485 */     FROM,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 494 */     HEADER,
/*     */     
/* 496 */     KEYWORD,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 501 */     LARGER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 506 */     NEW,
/*     */     
/* 508 */     NOT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 514 */     OLD,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 519 */     ON,
/*     */     
/* 521 */     OR,
/*     */     
/* 523 */     RECENT,
/*     */     
/* 525 */     SEEN,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 530 */     SENTBEFORE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 535 */     SENTON,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 540 */     SENTSINCE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 545 */     SINCE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 550 */     SMALLER,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 555 */     SUBJECT,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 560 */     TEXT,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 565 */     TO,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 570 */     UID,
/*     */     
/* 572 */     UNANSWERED,
/*     */     
/* 574 */     UNDELETED,
/*     */     
/* 576 */     UNDRAFT,
/*     */     
/* 578 */     UNFLAGGED,
/*     */     
/* 580 */     UNKEYWORD,
/*     */     
/* 582 */     UNSEEN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum FETCH_ITEM_NAMES
/*     */   {
/* 591 */     ALL,
/*     */     
/* 593 */     FAST,
/*     */     
/* 595 */     FULL,
/*     */     
/* 597 */     BODY,
/*     */     
/* 599 */     BODYSTRUCTURE,
/*     */     
/* 601 */     ENVELOPE,
/*     */     
/* 603 */     FLAGS,
/*     */     
/* 605 */     INTERNALDATE,
/*     */     
/* 607 */     RFC822,
/*     */     
/* 609 */     UID;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\imap\IMAPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */