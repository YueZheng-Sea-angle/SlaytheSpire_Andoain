/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*    */ @Plugin(name = "MarkerNamePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"markerSimpleName"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class MarkerSimpleNamePatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private MarkerSimpleNamePatternConverter(String[] options) {
/* 37 */     super("MarkerSimpleName", "markerSimpleName");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MarkerSimpleNamePatternConverter newInstance(String[] options) {
/* 47 */     return new MarkerSimpleNamePatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 55 */     Marker marker = event.getMarker();
/* 56 */     if (marker != null)
/* 57 */       toAppendTo.append(marker.getName()); 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MarkerSimpleNamePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */