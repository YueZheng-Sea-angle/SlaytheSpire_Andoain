/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
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
/*    */ 
/*    */ @Plugin(name = "date", category = "Lookup")
/*    */ public class DateLookup
/*    */   implements StrLookup
/*    */ {
/* 36 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 37 */   private static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String lookup(String key) {
/* 46 */     return formatDate(System.currentTimeMillis(), key);
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
/* 57 */     return (event == null) ? lookup(key) : formatDate(event.getTimeMillis(), key);
/*    */   }
/*    */   
/*    */   private String formatDate(long date, String format) {
/* 61 */     DateFormat dateFormat = null;
/* 62 */     if (format != null) {
/*    */       try {
/* 64 */         dateFormat = new SimpleDateFormat(format);
/* 65 */       } catch (Exception ex) {
/* 66 */         LOGGER.error(LOOKUP, "Invalid date format: [{}], using default", format, ex);
/*    */       } 
/*    */     }
/* 69 */     if (dateFormat == null) {
/* 70 */       dateFormat = DateFormat.getInstance();
/*    */     }
/* 72 */     return dateFormat.format(new Date(date));
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\DateLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */