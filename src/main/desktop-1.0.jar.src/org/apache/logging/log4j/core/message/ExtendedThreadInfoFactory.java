/*    */ package org.apache.logging.log4j.core.message;
/*    */ 
/*    */ import java.lang.management.ManagementFactory;
/*    */ import java.lang.management.ThreadInfo;
/*    */ import java.lang.management.ThreadMXBean;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.message.ThreadDumpMessage;
/*    */ import org.apache.logging.log4j.message.ThreadInformation;
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
/*    */ public class ExtendedThreadInfoFactory
/*    */   implements ThreadDumpMessage.ThreadInfoFactory
/*    */ {
/*    */   public ExtendedThreadInfoFactory() {
/* 34 */     Method[] methods = ThreadInfo.class.getMethods();
/* 35 */     boolean basic = true;
/* 36 */     for (Method method : methods) {
/* 37 */       if (method.getName().equals("getLockInfo")) {
/* 38 */         basic = false;
/*    */         break;
/*    */       } 
/*    */     } 
/* 42 */     if (basic) {
/* 43 */       throw new IllegalStateException();
/*    */     }
/*    */   }
/*    */   
/*    */   public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
/* 48 */     ThreadMXBean bean = ManagementFactory.getThreadMXBean();
/* 49 */     ThreadInfo[] array = bean.dumpAllThreads(true, true);
/*    */     
/* 51 */     Map<ThreadInformation, StackTraceElement[]> threads = (Map)new HashMap<>(array.length);
/*    */     
/* 53 */     for (ThreadInfo info : array) {
/* 54 */       threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
/*    */     }
/* 56 */     return threads;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\message\ExtendedThreadInfoFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */