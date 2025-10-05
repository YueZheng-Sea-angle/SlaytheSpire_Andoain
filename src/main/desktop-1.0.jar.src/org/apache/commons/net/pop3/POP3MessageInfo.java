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
/*    */ public final class POP3MessageInfo
/*    */ {
/*    */   public int number;
/*    */   public int size;
/*    */   public String identifier;
/*    */   
/*    */   public POP3MessageInfo() {
/* 54 */     this(0, null, 0);
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
/*    */   public POP3MessageInfo(int num, int octets) {
/* 66 */     this(num, null, octets);
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
/*    */   public POP3MessageInfo(int num, String uid) {
/* 78 */     this(num, uid, -1);
/*    */   }
/*    */   
/*    */   private POP3MessageInfo(int num, String uid, int size) {
/* 82 */     this.number = num;
/* 83 */     this.size = size;
/* 84 */     this.identifier = uid;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\pop3\POP3MessageInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */