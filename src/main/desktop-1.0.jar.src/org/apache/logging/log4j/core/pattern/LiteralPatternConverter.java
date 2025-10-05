/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.util.OptionConverter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class LiteralPatternConverter
/*    */   extends LogEventPatternConverter
/*    */   implements ArrayPatternConverter
/*    */ {
/*    */   private final String literal;
/*    */   private final Configuration config;
/*    */   private final boolean substitute;
/*    */   
/*    */   public LiteralPatternConverter(Configuration config, String literal, boolean convertBackslashes) {
/* 49 */     super("Literal", "literal");
/* 50 */     this.literal = convertBackslashes ? OptionConverter.convertSpecialChars(literal) : literal;
/* 51 */     this.config = config;
/* 52 */     this.substitute = (config != null && containsSubstitutionSequence(literal));
/*    */   }
/*    */   
/*    */   static boolean containsSubstitutionSequence(String literal) {
/* 56 */     return (literal != null && literal.contains("${"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 64 */     toAppendTo.append(this.substitute ? this.config.getStrSubstitutor().replace(event, this.literal) : this.literal);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuilder output) {
/* 72 */     output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(StringBuilder output, Object... objects) {
/* 80 */     output.append(this.substitute ? this.config.getStrSubstitutor().replace(this.literal) : this.literal);
/*    */   }
/*    */   
/*    */   public String getLiteral() {
/* 84 */     return this.literal;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVariable() {
/* 89 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "LiteralPatternConverter[literal=" + this.literal + ", config=" + this.config + ", substitute=" + this.substitute + "]";
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\LiteralPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */