/*    */ package org.apache.logging.log4j.spi;
/*    */ 
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
/*    */ @Deprecated
/*    */ public class LoggerContextKey
/*    */ {
/*    */   public static String create(String name) {
/* 32 */     return create(name, AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS);
/*    */   }
/*    */   
/*    */   public static String create(String name, MessageFactory messageFactory) {
/* 36 */     Class<? extends MessageFactory> messageFactoryClass = (messageFactory != null) ? (Class)messageFactory.getClass() : AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS;
/*    */     
/* 38 */     return create(name, messageFactoryClass);
/*    */   }
/*    */   
/*    */   public static String create(String name, Class<? extends MessageFactory> messageFactoryClass) {
/* 42 */     Class<? extends MessageFactory> mfClass = (messageFactoryClass != null) ? messageFactoryClass : AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS;
/*    */     
/* 44 */     return name + "." + mfClass.getName();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\LoggerContextKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */