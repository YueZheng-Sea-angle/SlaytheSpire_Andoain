/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.ProcessIdUtil;
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
/*    */ @Plugin(name = "ProcessIdPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"pid", "processId"})
/*    */ public final class ProcessIdPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/*    */   private static final String DEFAULT_DEFAULT_VALUE = "???";
/*    */   private final String pid;
/*    */   
/*    */   private ProcessIdPatternConverter(String... options) {
/* 33 */     super("Process ID", "pid");
/* 34 */     String defaultValue = (options.length > 0) ? options[0] : "???";
/* 35 */     String discoveredPid = ProcessIdUtil.getProcessId();
/* 36 */     this.pid = discoveredPid.equals("-") ? defaultValue : discoveredPid;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getProcessId() {
/* 44 */     return this.pid;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 48 */     System.out.println((new ProcessIdPatternConverter(new String[0])).pid);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ProcessIdPatternConverter newInstance(String[] options) {
/* 58 */     return new ProcessIdPatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 66 */     toAppendTo.append(this.pid);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\ProcessIdPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */