/*     */ package org.apache.logging.log4j.message;
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
/*     */ public final class ParameterizedNoReferenceMessageFactory
/*     */   extends AbstractMessageFactory
/*     */ {
/*     */   private static final long serialVersionUID = 5027639245636870500L;
/*     */   
/*     */   static class StatusMessage
/*     */     implements Message
/*     */   {
/*     */     private static final long serialVersionUID = 4199272162767841280L;
/*     */     private final String formattedMessage;
/*     */     private final Throwable throwable;
/*     */     
/*     */     public StatusMessage(String formattedMessage, Throwable throwable) {
/*  54 */       this.formattedMessage = formattedMessage;
/*  55 */       this.throwable = throwable;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormattedMessage() {
/*  60 */       return this.formattedMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormat() {
/*  65 */       return this.formattedMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getParameters() {
/*  70 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable getThrowable() {
/*  75 */       return this.throwable;
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
/*  88 */   public static final ParameterizedNoReferenceMessageFactory INSTANCE = new ParameterizedNoReferenceMessageFactory();
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
/*     */   public Message newMessage(String message, Object... params) {
/* 101 */     if (params == null) {
/* 102 */       return new SimpleMessage(message);
/*     */     }
/* 104 */     ParameterizedMessage msg = new ParameterizedMessage(message, params);
/* 105 */     return new StatusMessage(msg.getFormattedMessage(), msg.getThrowable());
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ParameterizedNoReferenceMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */