/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public final class Closer
/*    */ {
/*    */   public static boolean close(AutoCloseable closeable) throws Exception {
/* 41 */     if (closeable != null) {
/* 42 */       StatusLogger.getLogger().debug("Closing {} {}", closeable.getClass().getSimpleName(), closeable);
/* 43 */       closeable.close();
/* 44 */       return true;
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean closeSilently(AutoCloseable closeable) {
/*    */     try {
/* 57 */       return close(closeable);
/* 58 */     } catch (Exception ignored) {
/* 59 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */