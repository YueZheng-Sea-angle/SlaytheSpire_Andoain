/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.logging.log4j.Logger;
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
/*    */ public class ExecutorServices
/*    */ {
/* 27 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*    */   public static boolean shutdown(ExecutorService executorService, long timeout, TimeUnit timeUnit, String source) {
/* 48 */     if (executorService == null || executorService.isTerminated()) {
/* 49 */       return true;
/*    */     }
/* 51 */     executorService.shutdown();
/* 52 */     if (timeout > 0L && timeUnit == null)
/* 53 */       throw new IllegalArgumentException(
/* 54 */           String.format("%s can't shutdown %s when timeout = %,d and timeUnit = %s.", new Object[] {
/* 55 */               source, executorService, Long.valueOf(timeout), timeUnit
/*    */             })); 
/* 57 */     if (timeout > 0L) {
/*    */       
/*    */       try {
/* 60 */         if (!executorService.awaitTermination(timeout, timeUnit)) {
/* 61 */           executorService.shutdownNow();
/*    */           
/* 63 */           if (!executorService.awaitTermination(timeout, timeUnit)) {
/* 64 */             LOGGER.error("{} pool {} did not terminate after {} {}", source, executorService, Long.valueOf(timeout), timeUnit);
/*    */           }
/*    */           
/* 67 */           return false;
/*    */         } 
/* 69 */       } catch (InterruptedException ie) {
/*    */         
/* 71 */         executorService.shutdownNow();
/*    */         
/* 73 */         Thread.currentThread().interrupt();
/*    */       } 
/*    */     } else {
/* 76 */       executorService.shutdown();
/*    */     } 
/* 78 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ExecutorServices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */