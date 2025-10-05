/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import com.lmax.disruptor.LifecycleAware;
/*    */ import com.lmax.disruptor.Sequence;
/*    */ import com.lmax.disruptor.SequenceReportingEventHandler;
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
/*    */ public class RingBufferLogEventHandler
/*    */   implements SequenceReportingEventHandler<RingBufferLogEvent>, LifecycleAware
/*    */ {
/*    */   private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
/*    */   private Sequence sequenceCallback;
/*    */   private int counter;
/* 35 */   private long threadId = -1L;
/*    */ 
/*    */   
/*    */   public void setSequenceCallback(Sequence sequenceCallback) {
/* 39 */     this.sequenceCallback = sequenceCallback;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEvent(RingBufferLogEvent event, long sequence, boolean endOfBatch) throws Exception {
/*    */     try {
/* 50 */       if (event.isPopulated()) {
/* 51 */         event.execute(endOfBatch);
/*    */       }
/*    */     } finally {
/*    */       
/* 55 */       event.clear();
/*    */ 
/*    */ 
/*    */       
/* 59 */       notifyCallback(sequence);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void notifyCallback(long sequence) {
/* 64 */     if (++this.counter > 50) {
/* 65 */       this.sequenceCallback.set(sequence);
/* 66 */       this.counter = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getThreadId() {
/* 76 */     return this.threadId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onStart() {
/* 81 */     this.threadId = Thread.currentThread().getId();
/*    */   }
/*    */   
/*    */   public void onShutdown() {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\RingBufferLogEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */