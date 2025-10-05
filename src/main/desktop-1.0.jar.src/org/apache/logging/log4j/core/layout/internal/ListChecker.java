/*    */ package org.apache.logging.log4j.core.layout.internal;
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
/*    */ public interface ListChecker
/*    */ {
/* 25 */   public static final NoopChecker NOOP_CHECKER = new NoopChecker();
/*    */ 
/*    */   
/*    */   boolean check(String paramString);
/*    */ 
/*    */   
/*    */   public static class NoopChecker
/*    */     implements ListChecker
/*    */   {
/*    */     public boolean check(String key) {
/* 35 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 40 */       return "";
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\internal\ListChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */