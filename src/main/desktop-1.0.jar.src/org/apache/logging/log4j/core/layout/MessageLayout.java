/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.message.Message;
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
/*    */ @Plugin(name = "MessageLayout", category = "Core", elementType = "layout", printObject = true)
/*    */ public class MessageLayout
/*    */   extends AbstractLayout<Message>
/*    */ {
/*    */   public MessageLayout() {
/* 39 */     super(null, null, null);
/*    */   }
/*    */   
/*    */   public MessageLayout(Configuration configuration, byte[] header, byte[] footer) {
/* 43 */     super(configuration, header, footer);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] toByteArray(LogEvent event) {
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Message toSerializable(LogEvent event) {
/* 53 */     return event.getMessage();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentType() {
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   @PluginFactory
/*    */   public static Layout<?> createLayout() {
/* 63 */     return new MessageLayout();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\MessageLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */