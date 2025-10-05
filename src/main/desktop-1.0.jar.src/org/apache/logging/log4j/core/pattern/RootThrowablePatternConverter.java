/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*    */ import org.apache.logging.log4j.util.Strings;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "RootThrowablePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"rEx", "rThrowable", "rException"})
/*    */ public final class RootThrowablePatternConverter
/*    */   extends ThrowablePatternConverter
/*    */ {
/*    */   private RootThrowablePatternConverter(Configuration config, String[] options) {
/* 44 */     super("RootThrowable", "throwable", options, config);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RootThrowablePatternConverter newInstance(Configuration config, String[] options) {
/* 56 */     return new RootThrowablePatternConverter(config, options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 64 */     ThrowableProxy proxy = event.getThrownProxy();
/* 65 */     Throwable throwable = event.getThrown();
/* 66 */     if (throwable != null && this.options.anyLines()) {
/* 67 */       if (proxy == null) {
/* 68 */         super.format(event, toAppendTo);
/*    */         return;
/*    */       } 
/* 71 */       String trace = proxy.getCauseStackTraceAsString(this.options.getIgnorePackages(), this.options.getTextRenderer(), getSuffix(event), this.options.getSeparator());
/* 72 */       int len = toAppendTo.length();
/* 73 */       if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
/* 74 */         toAppendTo.append(' ');
/*    */       }
/* 76 */       if (!this.options.allLines() || !Strings.LINE_SEPARATOR.equals(this.options.getSeparator())) {
/* 77 */         StringBuilder sb = new StringBuilder();
/* 78 */         String[] array = trace.split(Strings.LINE_SEPARATOR);
/* 79 */         int limit = this.options.minLines(array.length) - 1;
/* 80 */         for (int i = 0; i <= limit; i++) {
/* 81 */           sb.append(array[i]);
/* 82 */           if (i < limit) {
/* 83 */             sb.append(this.options.getSeparator());
/*    */           }
/*    */         } 
/* 86 */         toAppendTo.append(sb.toString());
/*    */       } else {
/*    */         
/* 89 */         toAppendTo.append(trace);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\RootThrowablePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */