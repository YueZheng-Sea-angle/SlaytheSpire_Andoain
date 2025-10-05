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
/*    */ public class SimpleOptionHandler
/*    */   extends TelnetOptionHandler
/*    */ {
/*    */   public SimpleOptionHandler(int optcode, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
/* 44 */     super(optcode, initlocal, initremote, acceptlocal, acceptremote);
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
/*    */   public SimpleOptionHandler(int optcode) {
/* 56 */     super(optcode, false, false, false, false);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\SimpleOptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */