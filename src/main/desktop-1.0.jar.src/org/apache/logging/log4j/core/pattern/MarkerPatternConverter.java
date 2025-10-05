/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*    */ import org.apache.logging.log4j.util.StringBuilders;
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
/*    */ @Plugin(name = "MarkerPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"marker"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class MarkerPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private MarkerPatternConverter(String[] options) {
/* 38 */     super("Marker", "marker");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MarkerPatternConverter newInstance(String[] options) {
/* 48 */     return new MarkerPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 56 */     Marker marker = event.getMarker();
/* 57 */     if (marker != null)
/* 58 */       StringBuilders.appendValue(toAppendTo, marker); 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MarkerPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */