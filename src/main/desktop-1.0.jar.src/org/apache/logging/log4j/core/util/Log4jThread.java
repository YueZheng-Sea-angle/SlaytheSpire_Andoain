/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class Log4jThread
/*    */   extends Thread
/*    */ {
/*    */   static final String PREFIX = "Log4j2-";
/* 29 */   private static final AtomicLong threadInitNumber = new AtomicLong();
/*    */   
/*    */   private static long nextThreadNum() {
/* 32 */     return threadInitNumber.getAndIncrement();
/*    */   }
/*    */   
/*    */   private static String toThreadName(Object name) {
/* 36 */     return "Log4j2-" + name;
/*    */   }
/*    */   
/*    */   public Log4jThread() {
/* 40 */     super(toThreadName(Long.valueOf(nextThreadNum())));
/*    */   }
/*    */   
/*    */   public Log4jThread(Runnable target) {
/* 44 */     super(target, toThreadName(Long.valueOf(nextThreadNum())));
/*    */   }
/*    */   
/*    */   public Log4jThread(Runnable target, String name) {
/* 48 */     super(target, toThreadName(name));
/*    */   }
/*    */   
/*    */   public Log4jThread(String name) {
/* 52 */     super(toThreadName(name));
/*    */   }
/*    */   
/*    */   public Log4jThread(ThreadGroup group, Runnable target) {
/* 56 */     super(group, target, toThreadName(Long.valueOf(nextThreadNum())));
/*    */   }
/*    */   
/*    */   public Log4jThread(ThreadGroup group, Runnable target, String name) {
/* 60 */     super(group, target, toThreadName(name));
/*    */   }
/*    */   
/*    */   public Log4jThread(ThreadGroup group, Runnable target, String name, long stackSize) {
/* 64 */     super(group, target, toThreadName(name), stackSize);
/*    */   }
/*    */   
/*    */   public Log4jThread(ThreadGroup group, String name) {
/* 68 */     super(group, toThreadName(name));
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Log4jThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */