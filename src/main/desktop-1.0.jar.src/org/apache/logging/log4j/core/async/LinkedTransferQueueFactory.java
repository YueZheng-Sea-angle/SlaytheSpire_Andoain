/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.LinkedTransferQueue;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "LinkedTransferQueue", category = "Core", elementType = "BlockingQueueFactory")
/*    */ public class LinkedTransferQueueFactory<E>
/*    */   implements BlockingQueueFactory<E>
/*    */ {
/*    */   public BlockingQueue<E> create(int capacity) {
/* 36 */     return new LinkedTransferQueue<>();
/*    */   }
/*    */   
/*    */   @PluginFactory
/*    */   public static <E> LinkedTransferQueueFactory<E> createFactory() {
/* 41 */     return new LinkedTransferQueueFactory<>();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\LinkedTransferQueueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */