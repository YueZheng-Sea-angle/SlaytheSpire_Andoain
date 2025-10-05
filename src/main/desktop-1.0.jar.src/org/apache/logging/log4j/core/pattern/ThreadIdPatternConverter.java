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
/*    */ 
/*    */ @Plugin(name = "ThreadIdPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"T", "tid", "threadId"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class ThreadIdPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/* 35 */   private static final ThreadIdPatternConverter INSTANCE = new ThreadIdPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ThreadIdPatternConverter() {
/* 42 */     super("ThreadId", "threadId");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ThreadIdPatternConverter newInstance(String[] options) {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 61 */     toAppendTo.append(event.getThreadId());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\ThreadIdPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */