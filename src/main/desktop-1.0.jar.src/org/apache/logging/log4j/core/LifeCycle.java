/*    */ package org.apache.logging.log4j.core;
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
/*    */ public interface LifeCycle
/*    */ {
/*    */   State getState();
/*    */   
/*    */   void initialize();
/*    */   
/*    */   void start();
/*    */   
/*    */   void stop();
/*    */   
/*    */   boolean isStarted();
/*    */   
/*    */   boolean isStopped();
/*    */   
/*    */   public enum State
/*    */   {
/* 39 */     INITIALIZING,
/*    */     
/* 41 */     INITIALIZED,
/*    */     
/* 43 */     STARTING,
/*    */     
/* 45 */     STARTED,
/*    */     
/* 47 */     STOPPING,
/*    */     
/* 49 */     STOPPED;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\LifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */