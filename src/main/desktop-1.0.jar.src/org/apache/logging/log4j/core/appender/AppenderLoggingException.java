/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import org.apache.logging.log4j.LoggingException;
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
/*    */ public class AppenderLoggingException
/*    */   extends LoggingException
/*    */ {
/*    */   private static final long serialVersionUID = 6545990597472958303L;
/*    */   
/*    */   public AppenderLoggingException(String message) {
/* 43 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AppenderLoggingException(String format, Object... args) {
/* 54 */     super(String.format(format, args));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AppenderLoggingException(String message, Throwable cause) {
/* 64 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AppenderLoggingException(Throwable cause) {
/* 73 */     super(cause);
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
/*    */   public AppenderLoggingException(Throwable cause, String format, Object... args) {
/* 85 */     super(String.format(format, args), cause);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AppenderLoggingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */