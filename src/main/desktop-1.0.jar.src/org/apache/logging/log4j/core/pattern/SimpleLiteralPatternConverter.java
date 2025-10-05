/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.util.OptionConverter;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ abstract class SimpleLiteralPatternConverter
/*     */   extends LogEventPatternConverter
/*     */   implements ArrayPatternConverter
/*     */ {
/*     */   private SimpleLiteralPatternConverter() {
/*  33 */     super("SimpleLiteral", "literal");
/*     */   }
/*     */   
/*     */   static LogEventPatternConverter of(String literal, boolean convertBackslashes) {
/*  37 */     String value = convertBackslashes ? OptionConverter.convertSpecialChars(literal) : literal;
/*  38 */     return of(value);
/*     */   }
/*     */   
/*     */   static LogEventPatternConverter of(String literal) {
/*  42 */     if (literal == null || literal.isEmpty()) {
/*  43 */       return Noop.INSTANCE;
/*     */     }
/*  45 */     if (" ".equals(literal)) {
/*  46 */       return Space.INSTANCE;
/*     */     }
/*  48 */     return new StringValue(literal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void format(LogEvent ignored, StringBuilder output) {
/*  56 */     format(output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void format(Object ignored, StringBuilder output) {
/*  64 */     format(output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void format(StringBuilder output, Object... args) {
/*  72 */     format(output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isVariable() {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean handlesThrowable() {
/*  84 */     return false;
/*     */   }
/*     */   abstract void format(StringBuilder paramStringBuilder);
/*     */   
/*  88 */   private static final class Noop extends SimpleLiteralPatternConverter { private static final Noop INSTANCE = new Noop();
/*     */ 
/*     */     
/*     */     void format(StringBuilder output) {} }
/*     */ 
/*     */   
/*     */   private static final class Space
/*     */     extends SimpleLiteralPatternConverter
/*     */   {
/*  97 */     private static final Space INSTANCE = new Space();
/*     */ 
/*     */     
/*     */     void format(StringBuilder output) {
/* 101 */       output.append(' ');
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class StringValue
/*     */     extends SimpleLiteralPatternConverter {
/*     */     private final String literal;
/*     */     
/*     */     StringValue(String literal) {
/* 110 */       this.literal = literal;
/*     */     }
/*     */ 
/*     */     
/*     */     void format(StringBuilder output) {
/* 115 */       output.append(this.literal);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\SimpleLiteralPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */