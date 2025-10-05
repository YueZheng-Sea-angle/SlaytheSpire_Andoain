/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ public class ObjectMessage
/*     */   implements Message, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = -5903272448334166185L;
/*     */   private transient Object obj;
/*     */   private transient String objectString;
/*     */   
/*     */   public ObjectMessage(Object obj) {
/*  43 */     this.obj = (obj == null) ? "null" : obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  54 */     if (this.objectString == null) {
/*  55 */       this.objectString = String.valueOf(this.obj);
/*     */     }
/*  57 */     return this.objectString;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  62 */     if (this.objectString != null) {
/*  63 */       buffer.append(this.objectString);
/*     */     } else {
/*  65 */       StringBuilders.appendValue(buffer, this.obj);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  76 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter() {
/*  86 */     return this.obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  96 */     return new Object[] { this.obj };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 101 */     if (this == o) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (o == null || getClass() != o.getClass()) {
/* 105 */       return false;
/*     */     }
/*     */     
/* 108 */     ObjectMessage that = (ObjectMessage)o;
/* 109 */     return (this.obj == null) ? ((that.obj == null)) : equalObjectsOrStrings(this.obj, that.obj);
/*     */   }
/*     */   
/*     */   private boolean equalObjectsOrStrings(Object left, Object right) {
/* 113 */     return (left.equals(right) || String.valueOf(left).equals(String.valueOf(right)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     return (this.obj != null) ? this.obj.hashCode() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return getFormattedMessage();
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 127 */     out.defaultWriteObject();
/* 128 */     if (this.obj instanceof java.io.Serializable) {
/* 129 */       out.writeObject(this.obj);
/*     */     } else {
/* 131 */       out.writeObject(String.valueOf(this.obj));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 136 */     in.defaultReadObject();
/* 137 */     this.obj = in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 147 */     return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ObjectMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */