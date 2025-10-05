/*    */ package org.apache.commons.net.pop3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class POP3Command
/*    */ {
/*    */   public static final int USER = 0;
/*    */   public static final int PASS = 1;
/*    */   public static final int QUIT = 2;
/*    */   public static final int STAT = 3;
/*    */   public static final int LIST = 4;
/*    */   public static final int RETR = 5;
/*    */   public static final int DELE = 6;
/*    */   public static final int NOOP = 7;
/*    */   public static final int RSET = 8;
/*    */   public static final int APOP = 9;
/*    */   public static final int TOP = 10;
/*    */   public static final int UIDL = 11;
/*    */   public static final int CAPA = 12;
/*    */   public static final int AUTH = 13;
/*    */   private static final int _NEXT_ = 14;
/* 65 */   static final String[] _commands = new String[] { "USER", "PASS", "QUIT", "STAT", "LIST", "RETR", "DELE", "NOOP", "RSET", "APOP", "TOP", "UIDL", "CAPA", "AUTH" };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 71 */     if (_commands.length != 14) {
/* 72 */       throw new RuntimeException("Error in array definition");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String getCommand(int command) {
/* 88 */     return _commands[command];
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\pop3\POP3Command.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */