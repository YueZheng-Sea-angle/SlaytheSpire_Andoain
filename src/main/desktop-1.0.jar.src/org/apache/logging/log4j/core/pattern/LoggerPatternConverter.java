/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
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
/*    */ @Plugin(name = "LoggerPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"c", "logger"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class LoggerPatternConverter
/*    */   extends NamePatternConverter
/*    */ {
/* 34 */   private static final LoggerPatternConverter INSTANCE = new LoggerPatternConverter(null);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LoggerPatternConverter(String[] options) {
/* 43 */     super("Logger", "logger", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LoggerPatternConverter newInstance(String[] options) {
/* 54 */     if (options == null || options.length == 0) {
/* 55 */       return INSTANCE;
/*    */     }
/*    */     
/* 58 */     return new LoggerPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 66 */     abbreviate(event.getLoggerName(), toAppendTo);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\LoggerPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */