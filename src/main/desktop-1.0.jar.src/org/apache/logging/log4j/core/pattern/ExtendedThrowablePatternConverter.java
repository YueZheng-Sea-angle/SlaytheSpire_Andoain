/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
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
/*    */ @Plugin(name = "ExtendedThrowablePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"xEx", "xThrowable", "xException"})
/*    */ public final class ExtendedThrowablePatternConverter
/*    */   extends ThrowablePatternConverter
/*    */ {
/*    */   private ExtendedThrowablePatternConverter(Configuration config, String[] options) {
/* 43 */     super("ExtendedThrowable", "throwable", options, config);
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
/*    */   public static ExtendedThrowablePatternConverter newInstance(Configuration config, String[] options) {
/* 55 */     return new ExtendedThrowablePatternConverter(config, options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 63 */     ThrowableProxy proxy = event.getThrownProxy();
/* 64 */     Throwable throwable = event.getThrown();
/* 65 */     if ((throwable != null || proxy != null) && this.options.anyLines()) {
/* 66 */       if (proxy == null) {
/* 67 */         super.format(event, toAppendTo);
/*    */         return;
/*    */       } 
/* 70 */       int len = toAppendTo.length();
/* 71 */       if (len > 0 && !Character.isWhitespace(toAppendTo.charAt(len - 1))) {
/* 72 */         toAppendTo.append(' ');
/*    */       }
/* 74 */       proxy.formatExtendedStackTraceTo(toAppendTo, this.options.getIgnorePackages(), this.options
/* 75 */           .getTextRenderer(), getSuffix(event), this.options.getSeparator());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\ExtendedThrowablePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */