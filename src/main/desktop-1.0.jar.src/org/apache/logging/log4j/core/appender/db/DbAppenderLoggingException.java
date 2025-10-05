/*    */ package org.apache.logging.log4j.core.appender.db;
/*    */ 
/*    */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
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
/*    */ public class DbAppenderLoggingException
/*    */   extends AppenderLoggingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public DbAppenderLoggingException(String format, Object... args) {
/* 37 */     super(format, args);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DbAppenderLoggingException(String message, Throwable cause) {
/* 47 */     super(message, cause);
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
/*    */   public DbAppenderLoggingException(Throwable cause, String format, Object... args) {
/* 59 */     super(cause, format, args);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\DbAppenderLoggingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */