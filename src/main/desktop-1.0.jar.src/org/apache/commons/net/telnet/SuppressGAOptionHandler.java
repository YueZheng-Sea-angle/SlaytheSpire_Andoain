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
/*    */ public class SuppressGAOptionHandler
/*    */   extends TelnetOptionHandler
/*    */ {
/*    */   public SuppressGAOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
/* 39 */     super(3, initlocal, initremote, acceptlocal, acceptremote);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SuppressGAOptionHandler() {
/* 49 */     super(3, false, false, false, false);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\SuppressGAOptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */