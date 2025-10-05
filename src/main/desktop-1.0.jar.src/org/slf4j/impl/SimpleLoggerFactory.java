/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
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
/*    */ public class SimpleLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/*    */   ConcurrentMap<String, Logger> loggerMap;
/*    */   
/*    */   public SimpleLoggerFactory() {
/* 44 */     this.loggerMap = new ConcurrentHashMap<String, Logger>();
/* 45 */     SimpleLogger.lazyInit();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Logger getLogger(String name) {
/* 52 */     Logger simpleLogger = this.loggerMap.get(name);
/* 53 */     if (simpleLogger != null) {
/* 54 */       return simpleLogger;
/*    */     }
/* 56 */     SimpleLogger simpleLogger1 = new SimpleLogger(name);
/* 57 */     Logger oldInstance = (Logger)this.loggerMap.putIfAbsent(name, simpleLogger1);
/* 58 */     return (oldInstance == null) ? (Logger)simpleLogger1 : oldInstance;
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
/*    */ 
/*    */   
/*    */   void reset() {
/* 72 */     this.loggerMap.clear();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\SimpleLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */