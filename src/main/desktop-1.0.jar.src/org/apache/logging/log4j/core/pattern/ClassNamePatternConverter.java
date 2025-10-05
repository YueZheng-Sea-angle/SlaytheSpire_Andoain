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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "ClassNamePatternConverter", category = "Converter")
/*    */ @ConverterKeys({"C", "class"})
/*    */ public final class ClassNamePatternConverter
/*    */   extends NamePatternConverter
/*    */   implements LocationAware
/*    */ {
/*    */   private static final String NA = "?";
/*    */   
/*    */   private ClassNamePatternConverter(String[] options) {
/* 39 */     super("Class Name", "class name", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClassNamePatternConverter newInstance(String[] options) {
/* 49 */     return new ClassNamePatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 60 */     StackTraceElement element = event.getSource();
/* 61 */     if (element == null) {
/* 62 */       toAppendTo.append("?");
/*    */     } else {
/* 64 */       abbreviate(element.getClassName(), toAppendTo);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresLocation() {
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\ClassNamePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */