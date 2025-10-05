/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class FormattingInfo
/*     */ {
/*  30 */   private static final char[] SPACES = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final char[] ZEROS = new char[] { '0', '0', '0', '0', '0', '0', '0', '0' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final FormattingInfo DEFAULT = new FormattingInfo(false, 0, 2147483647, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int minLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean leftAlign;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean leftTruncate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean zeroPad;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormattingInfo(boolean leftAlign, int minLength, int maxLength, boolean leftTruncate) {
/*  80 */     this(leftAlign, minLength, maxLength, leftTruncate, false);
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
/*     */   public FormattingInfo(boolean leftAlign, int minLength, int maxLength, boolean leftTruncate, boolean zeroPad) {
/*  98 */     this.leftAlign = leftAlign;
/*  99 */     this.minLength = minLength;
/* 100 */     this.maxLength = maxLength;
/* 101 */     this.leftTruncate = leftTruncate;
/* 102 */     this.zeroPad = zeroPad;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FormattingInfo getDefault() {
/* 111 */     return DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftAligned() {
/* 120 */     return this.leftAlign;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftTruncate() {
/* 129 */     return this.leftTruncate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isZeroPad() {
/* 138 */     return this.zeroPad;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinLength() {
/* 147 */     return this.minLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLength() {
/* 156 */     return this.maxLength;
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
/*     */   public void format(int fieldStart, StringBuilder buffer) {
/* 168 */     int rawLength = buffer.length() - fieldStart;
/*     */     
/* 170 */     if (rawLength > this.maxLength) {
/* 171 */       if (this.leftTruncate) {
/* 172 */         buffer.delete(fieldStart, buffer.length() - this.maxLength);
/*     */       } else {
/* 174 */         buffer.delete(fieldStart + this.maxLength, fieldStart + buffer.length());
/*     */       } 
/* 176 */     } else if (rawLength < this.minLength) {
/* 177 */       if (this.leftAlign) {
/* 178 */         int fieldEnd = buffer.length();
/* 179 */         buffer.setLength(fieldStart + this.minLength);
/*     */         
/* 181 */         for (int i = fieldEnd; i < buffer.length(); i++) {
/* 182 */           buffer.setCharAt(i, ' ');
/*     */         }
/*     */       } else {
/* 185 */         int padLength = this.minLength - rawLength;
/*     */         
/* 187 */         char[] paddingArray = this.zeroPad ? ZEROS : SPACES;
/*     */         
/* 189 */         for (; padLength > paddingArray.length; padLength -= paddingArray.length) {
/* 190 */           buffer.insert(fieldStart, paddingArray);
/*     */         }
/*     */         
/* 193 */         buffer.insert(fieldStart, paddingArray, 0, padLength);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 205 */     StringBuilder sb = new StringBuilder();
/* 206 */     sb.append(super.toString());
/* 207 */     sb.append("[leftAlign=");
/* 208 */     sb.append(this.leftAlign);
/* 209 */     sb.append(", maxLength=");
/* 210 */     sb.append(this.maxLength);
/* 211 */     sb.append(", minLength=");
/* 212 */     sb.append(this.minLength);
/* 213 */     sb.append(", leftTruncate=");
/* 214 */     sb.append(this.leftTruncate);
/* 215 */     sb.append(", zeroPad=");
/* 216 */     sb.append(this.zeroPad);
/* 217 */     sb.append(']');
/* 218 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\FormattingInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */