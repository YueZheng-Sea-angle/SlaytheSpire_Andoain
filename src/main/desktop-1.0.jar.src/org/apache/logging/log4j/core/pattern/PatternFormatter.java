/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
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
/*    */ public class PatternFormatter
/*    */ {
/* 30 */   public static final PatternFormatter[] EMPTY_ARRAY = new PatternFormatter[0];
/*    */   
/*    */   private final LogEventPatternConverter converter;
/*    */   private final FormattingInfo field;
/*    */   private final boolean skipFormattingInfo;
/*    */   
/*    */   public PatternFormatter(LogEventPatternConverter converter, FormattingInfo field) {
/* 37 */     this.converter = converter;
/* 38 */     this.field = field;
/* 39 */     this.skipFormattingInfo = (field == FormattingInfo.getDefault());
/*    */   }
/*    */   
/*    */   public void format(LogEvent event, StringBuilder buf) {
/* 43 */     if (this.skipFormattingInfo) {
/* 44 */       this.converter.format(event, buf);
/*    */     } else {
/* 46 */       formatWithInfo(event, buf);
/*    */     } 
/*    */   }
/*    */   private void formatWithInfo(LogEvent event, StringBuilder buf) {
/* 50 */     int startField = buf.length();
/* 51 */     this.converter.format(event, buf);
/* 52 */     this.field.format(startField, buf);
/*    */   }
/*    */   
/*    */   public LogEventPatternConverter getConverter() {
/* 56 */     return this.converter;
/*    */   }
/*    */   
/*    */   public FormattingInfo getFormattingInfo() {
/* 60 */     return this.field;
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
/*    */   
/*    */   public boolean handlesThrowable() {
/* 73 */     return this.converter.handlesThrowable();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean requiresLocation() {
/* 82 */     return (this.converter instanceof LocationAware && ((LocationAware)this.converter).requiresLocation());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     StringBuilder sb = new StringBuilder();
/* 93 */     sb.append(super.toString());
/* 94 */     sb.append("[converter=");
/* 95 */     sb.append(this.converter);
/* 96 */     sb.append(", field=");
/* 97 */     sb.append(this.field);
/* 98 */     sb.append(']');
/* 99 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\PatternFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */