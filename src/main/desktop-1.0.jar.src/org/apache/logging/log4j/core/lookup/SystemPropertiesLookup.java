/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.MarkerManager;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*    */ @Plugin(name = "sys", category = "Lookup")
/*    */ public class SystemPropertiesLookup
/*    */   extends AbstractLookup
/*    */ {
/* 32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 33 */   private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(LogEvent event, String key) {
/*    */     try {
/* 44 */       return System.getProperty(key);
/* 45 */     } catch (Exception ex) {
/* 46 */       LOGGER.warn(LOOKUP, "Error while getting system property [{}].", key, ex);
/* 47 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\SystemPropertiesLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */