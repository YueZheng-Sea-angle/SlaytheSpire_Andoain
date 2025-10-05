/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class DefaultFlowMessageFactory
/*     */   implements FlowMessageFactory, Serializable
/*     */ {
/*     */   private static final String EXIT_DEFAULT_PREFIX = "Exit";
/*     */   private static final String ENTRY_DEFAULT_PREFIX = "Enter";
/*     */   private static final long serialVersionUID = 8578655591131397576L;
/*     */   private final String entryText;
/*     */   private final String exitText;
/*     */   
/*     */   public DefaultFlowMessageFactory() {
/*  39 */     this("Enter", "Exit");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFlowMessageFactory(String entryText, String exitText) {
/*  48 */     this.entryText = entryText;
/*  49 */     this.exitText = exitText;
/*     */   }
/*     */   
/*     */   private static class AbstractFlowMessage
/*     */     implements FlowMessage {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final Message message;
/*     */     private final String text;
/*     */     
/*     */     AbstractFlowMessage(String text, Message message) {
/*  59 */       this.message = message;
/*  60 */       this.text = text;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormattedMessage() {
/*  65 */       if (this.message != null) {
/*  66 */         return this.text + " " + this.message.getFormattedMessage();
/*     */       }
/*  68 */       return this.text;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormat() {
/*  73 */       if (this.message != null) {
/*  74 */         return this.text + ": " + this.message.getFormat();
/*     */       }
/*  76 */       return this.text;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getParameters() {
/*  81 */       if (this.message != null) {
/*  82 */         return this.message.getParameters();
/*     */       }
/*  84 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable getThrowable() {
/*  89 */       if (this.message != null) {
/*  90 */         return this.message.getThrowable();
/*     */       }
/*  92 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Message getMessage() {
/*  97 */       return this.message;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getText() {
/* 102 */       return this.text;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SimpleEntryMessage
/*     */     extends AbstractFlowMessage implements EntryMessage {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SimpleEntryMessage(String entryText, Message message) {
/* 111 */       super(entryText, message);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SimpleExitMessage
/*     */     extends AbstractFlowMessage
/*     */     implements ExitMessage
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final Object result;
/*     */     private final boolean isVoid;
/*     */     
/*     */     SimpleExitMessage(String exitText, EntryMessage message) {
/* 124 */       super(exitText, message.getMessage());
/* 125 */       this.result = null;
/* 126 */       this.isVoid = true;
/*     */     }
/*     */     
/*     */     SimpleExitMessage(String exitText, Object result, EntryMessage message) {
/* 130 */       super(exitText, message.getMessage());
/* 131 */       this.result = result;
/* 132 */       this.isVoid = false;
/*     */     }
/*     */     
/*     */     SimpleExitMessage(String exitText, Object result, Message message) {
/* 136 */       super(exitText, message);
/* 137 */       this.result = result;
/* 138 */       this.isVoid = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormattedMessage() {
/* 143 */       String formattedMessage = super.getFormattedMessage();
/* 144 */       if (this.isVoid) {
/* 145 */         return formattedMessage;
/*     */       }
/* 147 */       return formattedMessage + ": " + this.result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEntryText() {
/* 156 */     return this.entryText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExitText() {
/* 164 */     return this.exitText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntryMessage newEntryMessage(Message message) {
/* 174 */     return new SimpleEntryMessage(this.entryText, makeImmutable(message));
/*     */   }
/*     */   
/*     */   private Message makeImmutable(Message message) {
/* 178 */     if (!(message instanceof ReusableMessage)) {
/* 179 */       return message;
/*     */     }
/* 181 */     return new SimpleMessage(message.getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExitMessage newExitMessage(EntryMessage message) {
/* 191 */     return new SimpleExitMessage(this.exitText, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExitMessage newExitMessage(Object result, EntryMessage message) {
/* 201 */     return new SimpleExitMessage(this.exitText, result, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExitMessage newExitMessage(Object result, Message message) {
/* 211 */     return new SimpleExitMessage(this.exitText, result, message);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\DefaultFlowMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */