/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public class Log4jThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   private static final String PREFIX = "TF-";
/*    */   
/*    */   public static Log4jThreadFactory createDaemonThreadFactory(String threadFactoryName) {
/* 40 */     return new Log4jThreadFactory(threadFactoryName, true, 5);
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
/*    */   
/*    */   public static Log4jThreadFactory createThreadFactory(String threadFactoryName) {
/* 55 */     return new Log4jThreadFactory(threadFactoryName, false, 5);
/*    */   }
/*    */   
/* 58 */   private static final AtomicInteger FACTORY_NUMBER = new AtomicInteger(1);
/* 59 */   private static final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
/*    */ 
/*    */   
/*    */   private final boolean daemon;
/*    */ 
/*    */   
/*    */   private final ThreadGroup group;
/*    */ 
/*    */   
/*    */   private final int priority;
/*    */ 
/*    */   
/*    */   private final String threadNamePrefix;
/*    */ 
/*    */ 
/*    */   
/*    */   public Log4jThreadFactory(String threadFactoryName, boolean daemon, int priority) {
/* 76 */     this.threadNamePrefix = "TF-" + FACTORY_NUMBER.getAndIncrement() + "-" + threadFactoryName + "-";
/* 77 */     this.daemon = daemon;
/* 78 */     this.priority = priority;
/* 79 */     SecurityManager securityManager = System.getSecurityManager();
/* 80 */     this
/* 81 */       .group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable runnable) {
/* 87 */     Thread thread = new Log4jThread(this.group, runnable, this.threadNamePrefix + THREAD_NUMBER.getAndIncrement(), 0L);
/* 88 */     if (thread.isDaemon() != this.daemon) {
/* 89 */       thread.setDaemon(this.daemon);
/*    */     }
/* 91 */     if (thread.getPriority() != this.priority) {
/* 92 */       thread.setPriority(this.priority);
/*    */     }
/* 94 */     return thread;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\Log4jThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */