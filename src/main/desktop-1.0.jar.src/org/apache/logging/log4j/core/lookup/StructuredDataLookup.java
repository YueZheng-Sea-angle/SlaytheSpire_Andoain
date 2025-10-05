/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.message.StructuredDataMessage;
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
/*    */ @Plugin(name = "sd", category = "Lookup")
/*    */ public class StructuredDataLookup
/*    */   implements StrLookup
/*    */ {
/*    */   public String lookup(String key) {
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/* 47 */     if (event == null || !(event.getMessage() instanceof StructuredDataMessage)) {
/* 48 */       return null;
/*    */     }
/* 50 */     StructuredDataMessage msg = (StructuredDataMessage)event.getMessage();
/* 51 */     if (key.equalsIgnoreCase("id"))
/* 52 */       return msg.getId().getName(); 
/* 53 */     if (key.equalsIgnoreCase("type")) {
/* 54 */       return msg.getType();
/*    */     }
/* 56 */     return msg.get(key);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\StructuredDataLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */