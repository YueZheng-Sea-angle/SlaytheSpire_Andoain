/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import org.apache.logging.log4j.util.Constants;
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class ReusableSimpleMessage
/*     */   implements ReusableMessage, CharSequence, ParameterVisitable, Clearable
/*     */ {
/*     */   private static final long serialVersionUID = -9199974506498249809L;
/*     */   private CharSequence charSequence;
/*     */   
/*     */   public void set(String message) {
/*  32 */     this.charSequence = message;
/*     */   }
/*     */   
/*     */   public void set(CharSequence charSequence) {
/*  36 */     this.charSequence = charSequence;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  41 */     return String.valueOf(this.charSequence);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  46 */     return (this.charSequence instanceof String) ? (String)this.charSequence : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  51 */     return Constants.EMPTY_OBJECT_ARRAY;
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  61 */     buffer.append(this.charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/*  71 */     return emptyReplacement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/*  80 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <S> void forEachParameter(ParameterConsumer<S> action, S state) {}
/*     */ 
/*     */   
/*     */   public Message memento() {
/*  89 */     return new SimpleMessage(this.charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  96 */     return (this.charSequence == null) ? 0 : this.charSequence.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 101 */     return this.charSequence.charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 106 */     return this.charSequence.subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 111 */     this.charSequence = null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ReusableSimpleMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */