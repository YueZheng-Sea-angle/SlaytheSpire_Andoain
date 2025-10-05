/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class ReusableObjectMessage
/*     */   implements ReusableMessage, ParameterVisitable, Clearable
/*     */ {
/*     */   private static final long serialVersionUID = 6922476812535519960L;
/*     */   private transient Object obj;
/*     */   
/*     */   public void set(Object object) {
/*  33 */     this.obj = object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/*  43 */     return String.valueOf(this.obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/*  48 */     StringBuilders.appendValue(buffer, this.obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/*  58 */     return (this.obj instanceof String) ? (String)this.obj : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getParameter() {
/*  68 */     return this.obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/*  78 */     return new Object[] { this.obj };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return getFormattedMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/*  93 */     return (this.obj instanceof Throwable) ? (Throwable)this.obj : null;
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
/*     */   public Object[] swapParameters(Object[] emptyReplacement) {
/* 106 */     if (emptyReplacement.length == 0) {
/* 107 */       Object[] params = new Object[10];
/* 108 */       params[0] = this.obj;
/* 109 */       return params;
/*     */     } 
/* 111 */     emptyReplacement[0] = this.obj;
/* 112 */     return emptyReplacement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getParameterCount() {
/* 121 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public <S> void forEachParameter(ParameterConsumer<S> action, S state) {
/* 126 */     action.accept(this.obj, 0, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message memento() {
/* 131 */     return new ObjectMessage(this.obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 136 */     this.obj = null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ReusableObjectMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */