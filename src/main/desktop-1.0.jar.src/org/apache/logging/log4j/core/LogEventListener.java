/*    */ package org.apache.logging.log4j.core;
/*    */ 
/*    */ import java.util.EventListener;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class LogEventListener
/*    */   implements EventListener
/*    */ {
/* 29 */   protected static final StatusLogger LOGGER = StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */   
/* 33 */   private final LoggerContext context = LoggerContext.getContext(false);
/*    */ 
/*    */   
/*    */   public void log(LogEvent event) {
/* 37 */     if (event == null) {
/*    */       return;
/*    */     }
/* 40 */     Logger logger = this.context.getLogger(event.getLoggerName());
/* 41 */     if (logger.privateConfig.filter(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown()))
/* 42 */       logger.privateConfig.logEvent(event); 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\LogEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */