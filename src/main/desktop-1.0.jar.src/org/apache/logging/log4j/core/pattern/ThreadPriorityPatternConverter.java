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
/*    */ @Plugin(name = "ThreadPriorityPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"tp", "threadPriority"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class ThreadPriorityPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/* 35 */   private static final ThreadPriorityPatternConverter INSTANCE = new ThreadPriorityPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ThreadPriorityPatternConverter() {
/* 42 */     super("ThreadPriority", "threadPriority");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ThreadPriorityPatternConverter newInstance(String[] options) {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 61 */     toAppendTo.append(event.getThreadPriority());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\ThreadPriorityPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */