/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "event", category = "Lookup")
/*    */ public class EventLookup
/*    */   extends AbstractLookup
/*    */ {
/*    */   public String lookup(LogEvent event, String key) {
/* 36 */     if (event == null) {
/* 37 */       return null;
/*    */     }
/* 39 */     switch (key) {
/*    */       case "Marker":
/* 41 */         return (event.getMarker() != null) ? event.getMarker().getName() : null;
/*    */       
/*    */       case "ThreadName":
/* 44 */         return event.getThreadName();
/*    */       
/*    */       case "Level":
/* 47 */         return event.getLevel().toString();
/*    */       
/*    */       case "ThreadId":
/* 50 */         return Long.toString(event.getThreadId());
/*    */       
/*    */       case "Timestamp":
/* 53 */         return Long.toString(event.getTimeMillis());
/*    */       
/*    */       case "Exception":
/* 56 */         if (event.getThrown() != null) {
/* 57 */           return event.getThrown().getClass().getSimpleName();
/*    */         }
/* 59 */         if (event.getThrownProxy() != null) {
/* 60 */           return event.getThrownProxy().getName();
/*    */         }
/* 62 */         return null;
/*    */       
/*    */       case "Logger":
/* 65 */         return event.getLoggerName();
/*    */       
/*    */       case "Message":
/* 68 */         return event.getMessage().getFormattedMessage();
/*    */     } 
/*    */     
/* 71 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\EventLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */