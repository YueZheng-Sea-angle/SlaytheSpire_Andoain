/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.LocationAware;
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
/*    */ @Plugin(name = "FullLocationPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"l", "location"})
/*    */ public final class FullLocationPatternConverter
/*    */   extends LogEventPatternConverter
/*    */   implements LocationAware
/*    */ {
/* 32 */   private static final FullLocationPatternConverter INSTANCE = new FullLocationPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private FullLocationPatternConverter() {
/* 39 */     super("Full Location", "fullLocation");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static FullLocationPatternConverter newInstance(String[] options) {
/* 49 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder output) {
/* 57 */     StackTraceElement element = event.getSource();
/*    */     
/* 59 */     if (element != null) {
/* 60 */       output.append(element.toString());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresLocation() {
/* 66 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\FullLocationPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */