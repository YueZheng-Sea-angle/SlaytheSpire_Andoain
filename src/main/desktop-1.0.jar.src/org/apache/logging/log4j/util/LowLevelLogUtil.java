/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.Writer;
/*    */ import java.util.Objects;
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
/*    */ final class LowLevelLogUtil
/*    */ {
/* 34 */   private static PrintWriter writer = new PrintWriter(System.err, true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void log(String message) {
/* 43 */     if (message != null) {
/* 44 */       writer.println(message);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void logException(Throwable exception) {
/* 49 */     if (exception != null) {
/* 50 */       exception.printStackTrace(writer);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void logException(String message, Throwable exception) {
/* 55 */     log(message);
/* 56 */     logException(exception);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setOutputStream(OutputStream out) {
/* 65 */     writer = new PrintWriter(Objects.<OutputStream>requireNonNull(out), true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setWriter(Writer writer) {
/* 74 */     LowLevelLogUtil.writer = new PrintWriter(Objects.<Writer>requireNonNull(writer), true);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\LowLevelLogUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */