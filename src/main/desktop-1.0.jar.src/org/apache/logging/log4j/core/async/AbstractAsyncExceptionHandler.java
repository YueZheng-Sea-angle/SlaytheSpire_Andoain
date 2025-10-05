/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import com.lmax.disruptor.ExceptionHandler;
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
/*    */ abstract class AbstractAsyncExceptionHandler<T>
/*    */   implements ExceptionHandler<T>
/*    */ {
/*    */   public void handleEventException(Throwable throwable, long sequence, T event) {
/*    */     try {
/* 32 */       System.err.print("AsyncLogger error handling event seq=");
/* 33 */       System.err.print(sequence);
/* 34 */       System.err.print(", value='");
/*    */       try {
/* 36 */         System.err.print(event);
/* 37 */       } catch (Throwable t) {
/* 38 */         System.err.print("ERROR calling toString() on ");
/* 39 */         System.err.print(event.getClass().getName());
/* 40 */         System.err.print(": ");
/* 41 */         System.err.print(t.getClass().getName());
/* 42 */         System.err.print(": ");
/* 43 */         System.err.print(t.getMessage());
/*    */       } 
/* 45 */       System.err.print("': ");
/* 46 */       System.err.print(throwable.getClass().getName());
/* 47 */       System.err.print(": ");
/* 48 */       System.err.println(throwable.getMessage());
/*    */ 
/*    */       
/* 51 */       throwable.printStackTrace();
/* 52 */     } catch (Throwable throwable1) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleOnStartException(Throwable throwable) {
/* 60 */     System.err.println("AsyncLogger error starting:");
/* 61 */     throwable.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleOnShutdownException(Throwable throwable) {
/* 66 */     System.err.println("AsyncLogger error shutting down:");
/* 67 */     throwable.printStackTrace();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AbstractAsyncExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */