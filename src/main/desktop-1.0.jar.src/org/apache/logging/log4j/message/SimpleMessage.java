/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ public class SimpleMessage
/*     */   implements Message, StringBuilderFormattable, CharSequence
/*     */ {
/*     */   private static final long serialVersionUID = -8398002534962715992L;
/*     */   private String message;
/*     */   private transient CharSequence charSequence;
/*     */   
/*     */   public SimpleMessage() {
/*  38 */     this((String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMessage(String message) {
/*  46 */     this.message = message;
/*  47 */     this.charSequence = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleMessage(CharSequence charSequence) {
/*  56 */     this.charSequence = charSequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  65 */     return this.message = (this.message == null) ? String.valueOf(this.charSequence) : this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  70 */     buffer.append((this.message != null) ? this.message : this.charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  79 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  93 */     if (this == o) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (o == null || getClass() != o.getClass()) {
/*  97 */       return false;
/*     */     }
/*     */     
/* 100 */     SimpleMessage that = (SimpleMessage)o;
/*     */     
/* 102 */     if ((this.charSequence != null) ? !this.charSequence.equals(that.charSequence) : (that.charSequence != null)) return false;
/*     */   
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 107 */     return (this.charSequence != null) ? this.charSequence.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 130 */     return (this.charSequence == null) ? 0 : this.charSequence.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 135 */     return this.charSequence.charAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 140 */     return this.charSequence.subSequence(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 145 */     getFormattedMessage();
/* 146 */     out.defaultWriteObject();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 150 */     in.defaultReadObject();
/* 151 */     this.charSequence = this.message;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\SimpleMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */