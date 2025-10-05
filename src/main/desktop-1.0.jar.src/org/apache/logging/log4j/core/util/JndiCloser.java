/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
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
/*    */ public final class JndiCloser
/*    */ {
/*    */   public static void close(Context context) throws NamingException {
/* 41 */     if (context != null) {
/* 42 */       context.close();
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
/*    */   public static boolean closeSilently(Context context) {
/*    */     try {
/* 55 */       close(context);
/* 56 */       return true;
/* 57 */     } catch (NamingException ignored) {
/*    */       
/* 59 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\JndiCloser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */