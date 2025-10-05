/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.spi.LoggerFactoryBinder;
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
/*    */ public class StaticLoggerBinder
/*    */   implements LoggerFactoryBinder
/*    */ {
/* 44 */   private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final StaticLoggerBinder getSingleton() {
/* 52 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public static String REQUESTED_API_VERSION = "1.6.99";
/*    */   
/* 62 */   private static final String loggerFactoryClassStr = SimpleLoggerFactory.class.getName();
/*    */ 
/*    */ 
/*    */   
/*    */   private final ILoggerFactory loggerFactory;
/*    */ 
/*    */ 
/*    */   
/*    */   private StaticLoggerBinder() {
/* 71 */     this.loggerFactory = new SimpleLoggerFactory();
/*    */   }
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 75 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public String getLoggerFactoryClassStr() {
/* 79 */     return loggerFactoryClassStr;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\StaticLoggerBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */