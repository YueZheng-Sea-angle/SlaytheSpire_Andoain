/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public abstract class NameAbbreviator
/*     */ {
/*  33 */   private static final NameAbbreviator DEFAULT = new NOPAbbreviator();
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
/*     */   public static NameAbbreviator getAbbreviator(String pattern) {
/*  49 */     if (pattern.length() > 0) {
/*     */       boolean isNegativeNumber;
/*     */       
/*  52 */       String number, trimmed = pattern.trim();
/*     */       
/*  54 */       if (trimmed.isEmpty()) {
/*  55 */         return DEFAULT;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  62 */       if (trimmed.length() > 1 && trimmed.charAt(0) == '-') {
/*  63 */         isNegativeNumber = true;
/*  64 */         number = trimmed.substring(1);
/*     */       } else {
/*  66 */         isNegativeNumber = false;
/*  67 */         number = trimmed;
/*     */       } 
/*     */       
/*  70 */       int i = 0;
/*     */       
/*  72 */       while (i < number.length() && number.charAt(i) >= '0' && number
/*  73 */         .charAt(i) <= '9') {
/*  74 */         i++;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  80 */       if (i == number.length()) {
/*  81 */         return new MaxElementAbbreviator(Integer.parseInt(number), isNegativeNumber ? MaxElementAbbreviator.Strategy.DROP : MaxElementAbbreviator.Strategy.RETAIN);
/*     */       }
/*     */ 
/*     */       
/*  85 */       ArrayList<PatternAbbreviatorFragment> fragments = new ArrayList<>(5);
/*     */ 
/*     */       
/*  88 */       int pos = 0;
/*     */       
/*  90 */       while (pos < trimmed.length() && pos >= 0) {
/*  91 */         int charCount, ellipsisPos = pos;
/*     */         
/*  93 */         if (trimmed.charAt(pos) == '*') {
/*  94 */           charCount = Integer.MAX_VALUE;
/*  95 */           ellipsisPos++;
/*  96 */         } else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
/*  97 */           charCount = trimmed.charAt(pos) - 48;
/*  98 */           ellipsisPos++;
/*     */         } else {
/* 100 */           charCount = 0;
/*     */         } 
/*     */         
/* 103 */         char ellipsis = Character.MIN_VALUE;
/*     */         
/* 105 */         if (ellipsisPos < trimmed.length()) {
/* 106 */           ellipsis = trimmed.charAt(ellipsisPos);
/*     */           
/* 108 */           if (ellipsis == '.') {
/* 109 */             ellipsis = Character.MIN_VALUE;
/*     */           }
/*     */         } 
/*     */         
/* 113 */         fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
/* 114 */         pos = trimmed.indexOf('.', pos);
/*     */         
/* 116 */         if (pos == -1) {
/*     */           break;
/*     */         }
/*     */         
/* 120 */         pos++;
/*     */       } 
/*     */       
/* 123 */       return new PatternAbbreviator(fragments);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NameAbbreviator getDefaultAbbreviator() {
/* 138 */     return DEFAULT;
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
/*     */   public abstract void abbreviate(String paramString, StringBuilder paramStringBuilder);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NOPAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     public void abbreviate(String original, StringBuilder destination) {
/* 164 */       destination.append(original);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MaxElementAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final int count;
/*     */     
/*     */     private final Strategy strategy;
/*     */ 
/*     */     
/*     */     private enum Strategy
/*     */     {
/* 179 */       DROP(0)
/*     */       {
/*     */         void abbreviate(int count, String original, StringBuilder destination)
/*     */         {
/* 183 */           int start = 0;
/*     */           
/* 185 */           for (int i = 0; i < count; i++) {
/* 186 */             int nextStart = original.indexOf('.', start);
/* 187 */             if (nextStart == -1) {
/* 188 */               destination.append(original);
/*     */               return;
/*     */             } 
/* 191 */             start = nextStart + 1;
/*     */           } 
/* 193 */           destination.append(original, start, original.length());
/*     */         }
/*     */       },
/* 196 */       RETAIN(1)
/*     */       {
/*     */ 
/*     */         
/*     */         void abbreviate(int count, String original, StringBuilder destination)
/*     */         {
/* 202 */           int end = original.length() - 1;
/*     */           
/* 204 */           for (int i = count; i > 0; i--) {
/* 205 */             end = original.lastIndexOf('.', end - 1);
/* 206 */             if (end == -1) {
/* 207 */               destination.append(original);
/*     */               return;
/*     */             } 
/*     */           } 
/* 211 */           destination.append(original, end + 1, original.length());
/*     */         }
/*     */       };
/*     */       
/*     */       final int minCount;
/*     */       
/*     */       Strategy(int minCount) {
/* 218 */         this.minCount = minCount;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       abstract void abbreviate(int param2Int, String param2String, StringBuilder param2StringBuilder);
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
/*     */     public MaxElementAbbreviator(int count, Strategy strategy) {
/* 241 */       this.count = Math.max(count, strategy.minCount);
/* 242 */       this.strategy = strategy;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void abbreviate(String original, StringBuilder destination) {
/* 253 */       this.strategy.abbreviate(this.count, original, destination);
/*     */     } } private enum Strategy { DROP(0) {
/*     */       void abbreviate(int count, String original, StringBuilder destination) {
/*     */         int start = 0;
/*     */         for (int i = 0; i < count; i++) {
/*     */           int nextStart = original.indexOf('.', start);
/*     */           if (nextStart == -1) {
/*     */             destination.append(original);
/*     */             return;
/*     */           } 
/*     */           start = nextStart + 1;
/*     */         } 
/*     */         destination.append(original, start, original.length());
/*     */       }
/*     */     },
/*     */     RETAIN(1) { void abbreviate(int count, String original, StringBuilder destination) {
/*     */         int end = original.length() - 1;
/*     */         for (int i = count; i > 0; i--) {
/*     */           end = original.lastIndexOf('.', end - 1);
/*     */           if (end == -1) {
/*     */             destination.append(original);
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         destination.append(original, end + 1, original.length());
/*     */       } }; final int minCount; Strategy(int minCount) {
/*     */       this.minCount = minCount;
/*     */     } abstract void abbreviate(int param1Int, String param1String, StringBuilder param1StringBuilder); } private static final class PatternAbbreviatorFragment { PatternAbbreviatorFragment(int charCount, char ellipsis) {
/* 281 */       this.charCount = charCount;
/* 282 */       this.ellipsis = ellipsis;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int charCount;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final char ellipsis;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int abbreviate(String input, int inputIndex, StringBuilder buf) {
/* 300 */       int nextDot = input.indexOf('.', inputIndex);
/* 301 */       if (nextDot < 0) {
/* 302 */         buf.append(input, inputIndex, input.length());
/* 303 */         return nextDot;
/*     */       } 
/* 305 */       if (nextDot - inputIndex > this.charCount) {
/* 306 */         buf.append(input, inputIndex, inputIndex + this.charCount);
/* 307 */         if (this.ellipsis != '\000') {
/* 308 */           buf.append(this.ellipsis);
/*     */         }
/* 310 */         buf.append('.');
/*     */       } else {
/*     */         
/* 313 */         buf.append(input, inputIndex, nextDot + 1);
/*     */       } 
/* 315 */       return nextDot + 1;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class PatternAbbreviator
/*     */     extends NameAbbreviator
/*     */   {
/*     */     private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PatternAbbreviator(List<NameAbbreviator.PatternAbbreviatorFragment> fragments) {
/* 334 */       if (fragments.isEmpty()) {
/* 335 */         throw new IllegalArgumentException("fragments must have at least one element");
/*     */       }
/*     */ 
/*     */       
/* 339 */       this.fragments = fragments.<NameAbbreviator.PatternAbbreviatorFragment>toArray(new NameAbbreviator.PatternAbbreviatorFragment[0]);
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
/*     */     public void abbreviate(String original, StringBuilder destination) {
/* 351 */       int originalIndex = 0;
/* 352 */       int iteration = 0;
/* 353 */       int originalLength = original.length();
/* 354 */       while (originalIndex >= 0 && originalIndex < originalLength) {
/* 355 */         originalIndex = fragment(iteration++).abbreviate(original, originalIndex, destination);
/*     */       }
/*     */     }
/*     */     
/*     */     NameAbbreviator.PatternAbbreviatorFragment fragment(int index) {
/* 360 */       return this.fragments[Math.min(index, this.fragments.length - 1)];
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\NameAbbreviator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */