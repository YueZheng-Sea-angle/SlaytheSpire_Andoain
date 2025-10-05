/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.message.MessageFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LambdaUtil
/*    */ {
/*    */   public static Object[] getAll(Supplier<?>... suppliers) {
/* 42 */     if (suppliers == null) {
/* 43 */       return null;
/*    */     }
/* 45 */     Object[] result = new Object[suppliers.length];
/* 46 */     for (int i = 0; i < result.length; i++) {
/* 47 */       result[i] = get(suppliers[i]);
/*    */     }
/* 49 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object get(Supplier<?> supplier) {
/* 60 */     if (supplier == null) {
/* 61 */       return null;
/*    */     }
/* 63 */     Object result = supplier.get();
/* 64 */     return (result instanceof Message) ? ((Message)result).getFormattedMessage() : result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Message get(MessageSupplier supplier) {
/* 74 */     if (supplier == null) {
/* 75 */       return null;
/*    */     }
/* 77 */     return supplier.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Message getMessage(Supplier<?> supplier, MessageFactory messageFactory) {
/* 88 */     if (supplier == null) {
/* 89 */       return null;
/*    */     }
/* 91 */     Object result = supplier.get();
/* 92 */     return (result instanceof Message) ? (Message)result : messageFactory.newMessage(result);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\LambdaUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */