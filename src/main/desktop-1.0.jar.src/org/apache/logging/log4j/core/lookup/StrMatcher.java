/*     */ package org.apache.logging.log4j.core.lookup;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StrMatcher
/*     */ {
/*  36 */   private static final StrMatcher COMMA_MATCHER = new CharMatcher(',');
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final StrMatcher TAB_MATCHER = new CharMatcher('\t');
/*     */ 
/*     */ 
/*     */   
/*  44 */   private static final StrMatcher SPACE_MATCHER = new CharMatcher(' ');
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static final StrMatcher SPLIT_MATCHER = new CharSetMatcher(" \t\n\r\f".toCharArray());
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final StrMatcher TRIM_MATCHER = new TrimMatcher();
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final StrMatcher SINGLE_QUOTE_MATCHER = new CharMatcher('\'');
/*     */ 
/*     */ 
/*     */   
/*  61 */   private static final StrMatcher DOUBLE_QUOTE_MATCHER = new CharMatcher('"');
/*     */ 
/*     */ 
/*     */   
/*  65 */   private static final StrMatcher QUOTE_MATCHER = new CharSetMatcher("'\"".toCharArray());
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final StrMatcher NONE_MATCHER = new NoMatcher();
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
/*     */   public static StrMatcher commaMatcher() {
/*  83 */     return COMMA_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher tabMatcher() {
/*  92 */     return TAB_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher spaceMatcher() {
/* 101 */     return SPACE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher splitMatcher() {
/* 111 */     return SPLIT_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher trimMatcher() {
/* 120 */     return TRIM_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher singleQuoteMatcher() {
/* 129 */     return SINGLE_QUOTE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher doubleQuoteMatcher() {
/* 138 */     return DOUBLE_QUOTE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher quoteMatcher() {
/* 147 */     return QUOTE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher noneMatcher() {
/* 156 */     return NONE_MATCHER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher charMatcher(char ch) {
/* 166 */     return new CharMatcher(ch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher charSetMatcher(char[] chars) {
/* 176 */     if (chars == null || chars.length == 0) {
/* 177 */       return NONE_MATCHER;
/*     */     }
/* 179 */     if (chars.length == 1) {
/* 180 */       return new CharMatcher(chars[0]);
/*     */     }
/* 182 */     return new CharSetMatcher(chars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher charSetMatcher(String chars) {
/* 192 */     if (Strings.isEmpty(chars)) {
/* 193 */       return NONE_MATCHER;
/*     */     }
/* 195 */     if (chars.length() == 1) {
/* 196 */       return new CharMatcher(chars.charAt(0));
/*     */     }
/* 198 */     return new CharSetMatcher(chars.toCharArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StrMatcher stringMatcher(String str) {
/* 208 */     if (Strings.isEmpty(str)) {
/* 209 */       return NONE_MATCHER;
/*     */     }
/* 211 */     return new StringMatcher(str);
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int isMatch(char[] paramArrayOfchar, int paramInt1, int paramInt2, int paramInt3);
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
/*     */ 
/*     */   
/*     */   public int isMatch(char[] buffer, int pos) {
/* 262 */     return isMatch(buffer, pos, 0, buffer.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CharSetMatcher
/*     */     extends StrMatcher
/*     */   {
/*     */     private final char[] chars;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CharSetMatcher(char[] chars) {
/* 279 */       this.chars = (char[])chars.clone();
/* 280 */       Arrays.sort(this.chars);
/*     */     }
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
/*     */     public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
/* 294 */       return (Arrays.binarySearch(this.chars, buffer[pos]) >= 0) ? 1 : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class CharMatcher
/*     */     extends StrMatcher
/*     */   {
/*     */     private final char ch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CharMatcher(char ch) {
/* 312 */       this.ch = ch;
/*     */     }
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
/*     */     public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
/* 326 */       return (this.ch == buffer[pos]) ? 1 : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class StringMatcher
/*     */     extends StrMatcher
/*     */   {
/*     */     private final char[] chars;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     StringMatcher(String str) {
/* 344 */       this.chars = str.toCharArray();
/*     */     }
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
/*     */     public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
/* 358 */       int len = this.chars.length;
/* 359 */       if (pos + len > bufferEnd) {
/* 360 */         return 0;
/*     */       }
/* 362 */       for (int i = 0; i < this.chars.length; i++, pos++) {
/* 363 */         if (this.chars[i] != buffer[pos]) {
/* 364 */           return 0;
/*     */         }
/*     */       } 
/* 367 */       return len;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 372 */       return super.toString() + ' ' + Arrays.toString(this.chars);
/*     */     }
/*     */   }
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
/*     */   static final class NoMatcher
/*     */     extends StrMatcher
/*     */   {
/*     */     public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
/* 400 */       return 0;
/*     */     }
/*     */   }
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
/*     */   static final class TrimMatcher
/*     */     extends StrMatcher
/*     */   {
/*     */     public int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd) {
/* 427 */       return (buffer[pos] <= ' ') ? 1 : 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\StrMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */