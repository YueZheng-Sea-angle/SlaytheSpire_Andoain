/*    */ package org.apache.logging.log4j.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Chars
/*    */ {
/*    */   public static final char CR = '\r';
/*    */   public static final char DQUOTE = '"';
/*    */   public static final char EQ = '=';
/*    */   public static final char LF = '\n';
/*    */   public static final char NUL = '\000';
/*    */   public static final char QUOTE = '\'';
/*    */   public static final char SPACE = ' ';
/*    */   public static final char TAB = '\t';
/*    */   
/*    */   public static char getUpperCaseHex(int digit) {
/* 55 */     if (digit < 0 || digit >= 16) {
/* 56 */       return Character.MIN_VALUE;
/*    */     }
/* 58 */     return (digit < 10) ? getNumericalDigit(digit) : getUpperCaseAlphaDigit(digit);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static char getLowerCaseHex(int digit) {
/* 68 */     if (digit < 0 || digit >= 16) {
/* 69 */       return Character.MIN_VALUE;
/*    */     }
/* 71 */     return (digit < 10) ? getNumericalDigit(digit) : getLowerCaseAlphaDigit(digit);
/*    */   }
/*    */   
/*    */   private static char getNumericalDigit(int digit) {
/* 75 */     return (char)(48 + digit);
/*    */   }
/*    */   
/*    */   private static char getUpperCaseAlphaDigit(int digit) {
/* 79 */     return (char)(65 + digit - 10);
/*    */   }
/*    */   
/*    */   private static char getLowerCaseAlphaDigit(int digit) {
/* 83 */     return (char)(97 + digit - 10);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Chars.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */