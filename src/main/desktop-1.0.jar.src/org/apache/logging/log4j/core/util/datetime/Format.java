/*    */ package org.apache.logging.log4j.core.util.datetime;
/*    */ 
/*    */ import java.text.FieldPosition;
/*    */ import java.text.ParseException;
/*    */ import java.text.ParsePosition;
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
/*    */ public abstract class Format
/*    */ {
/*    */   public final String format(Object obj) {
/* 29 */     return format(obj, new StringBuilder(), new FieldPosition(0)).toString();
/*    */   }
/*    */   
/*    */   public abstract StringBuilder format(Object paramObject, StringBuilder paramStringBuilder, FieldPosition paramFieldPosition);
/*    */   
/*    */   public abstract Object parseObject(String paramString, ParsePosition paramParsePosition);
/*    */   
/*    */   public Object parseObject(String source) throws ParseException {
/* 37 */     ParsePosition pos = new ParsePosition(0);
/* 38 */     Object result = parseObject(source, pos);
/* 39 */     if (pos.getIndex() == 0) {
/* 40 */       throw new ParseException("Format.parseObject(String) failed", pos.getErrorIndex());
/*    */     }
/* 42 */     return result;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\datetime\Format.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */