/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ class MemoryPasswordProvider
/*    */   implements PasswordProvider
/*    */ {
/*    */   private final char[] password;
/*    */   
/*    */   public MemoryPasswordProvider(char[] chars) {
/* 34 */     if (chars != null) {
/* 35 */       this.password = (char[])chars.clone();
/*    */     } else {
/* 37 */       this.password = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] getPassword() {
/* 43 */     if (this.password == null) {
/* 44 */       return null;
/*    */     }
/* 46 */     return (char[])this.password.clone();
/*    */   }
/*    */   
/*    */   public void clearSecrets() {
/* 50 */     Arrays.fill(this.password, false);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\MemoryPasswordProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */