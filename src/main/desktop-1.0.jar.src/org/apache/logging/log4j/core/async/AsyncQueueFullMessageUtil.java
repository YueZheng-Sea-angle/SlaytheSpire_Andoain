/*    */ package org.apache.logging.log4j.core.async;
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
/*    */ public final class AsyncQueueFullMessageUtil
/*    */ {
/*    */   public static void logWarningToStatusLogger() {
/* 36 */     StatusLogger.getLogger()
/* 37 */       .warn("LOG4J2-2031: Log4j2 logged an event out of order to prevent deadlock caused by domain objects logging from their toString method when the async queue is full");
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncQueueFullMessageUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */