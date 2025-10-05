/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ @Plugin(name = "IntegerPatternConverter", category = "FileConverter")
/*    */ @ConverterKeys({"i", "index"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class IntegerPatternConverter
/*    */   extends AbstractPatternConverter
/*    */   implements ArrayPatternConverter
/*    */ {
/* 35 */   private static final IntegerPatternConverter INSTANCE = new IntegerPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private IntegerPatternConverter() {
/* 41 */     super("Integer", "integer");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IntegerPatternConverter newInstance(String[] options) {
/* 51 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void format(StringBuilder toAppendTo, Object... objects) {
/* 56 */     for (int i = 0; i < objects.length; i++) {
/* 57 */       if (objects[i] instanceof Integer) {
/* 58 */         format(objects[i], toAppendTo); break;
/*    */       } 
/* 60 */       if (objects[i] instanceof NotANumber) {
/* 61 */         toAppendTo.append("\000");
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuilder toAppendTo) {
/* 72 */     if (obj instanceof Integer) {
/* 73 */       toAppendTo.append(((Integer)obj).intValue());
/* 74 */     } else if (obj instanceof Date) {
/* 75 */       toAppendTo.append(((Date)obj).getTime());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\IntegerPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */