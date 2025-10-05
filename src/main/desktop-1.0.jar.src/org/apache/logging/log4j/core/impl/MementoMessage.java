/*    */ package org.apache.logging.log4j.core.impl;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*    */ public final class MementoMessage
/*    */   implements Message, StringBuilderFormattable
/*    */ {
/*    */   private final String formattedMessage;
/*    */   private final String format;
/*    */   private final Object[] parameters;
/*    */   
/*    */   public MementoMessage(String formattedMessage, String format, Object[] parameters) {
/* 40 */     this.formattedMessage = formattedMessage;
/* 41 */     this.format = format;
/* 42 */     this.parameters = parameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFormattedMessage() {
/* 47 */     return this.formattedMessage;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFormat() {
/* 52 */     return this.format;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object[] getParameters() {
/* 57 */     return this.parameters;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getThrowable() {
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatTo(StringBuilder buffer) {
/* 72 */     buffer.append(this.formattedMessage);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     return "MementoMessage{formattedMessage='" + this.formattedMessage + '\'' + ", format='" + this.format + '\'' + ", parameters=" + 
/*    */ 
/*    */       
/* 80 */       Arrays.toString(this.parameters) + '}';
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\MementoMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */