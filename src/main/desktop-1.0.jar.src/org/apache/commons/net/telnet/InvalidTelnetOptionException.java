/*    */ package org.apache.commons.net.telnet;
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
/*    */ public class InvalidTelnetOptionException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -2516777155928793597L;
/*    */   private final int optionCode;
/*    */   private final String msg;
/*    */   
/*    */   public InvalidTelnetOptionException(String message, int optcode) {
/* 48 */     this.optionCode = optcode;
/* 49 */     this.msg = message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 60 */     return this.msg + ": " + this.optionCode;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\InvalidTelnetOptionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */