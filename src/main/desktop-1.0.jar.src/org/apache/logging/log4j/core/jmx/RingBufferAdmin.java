/*    */ package org.apache.logging.log4j.core.jmx;
/*    */ 
/*    */ import com.lmax.disruptor.RingBuffer;
/*    */ import javax.management.ObjectName;
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
/*    */ public class RingBufferAdmin
/*    */   implements RingBufferAdminMBean
/*    */ {
/*    */   private final RingBuffer<?> ringBuffer;
/*    */   private final ObjectName objectName;
/*    */   
/*    */   public static RingBufferAdmin forAsyncLogger(RingBuffer<?> ringBuffer, String contextName) {
/* 32 */     String ctxName = Server.escape(contextName);
/* 33 */     String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", new Object[] { ctxName });
/* 34 */     return new RingBufferAdmin(ringBuffer, name);
/*    */   }
/*    */ 
/*    */   
/*    */   public static RingBufferAdmin forAsyncLoggerConfig(RingBuffer<?> ringBuffer, String contextName, String configName) {
/* 39 */     String ctxName = Server.escape(contextName);
/* 40 */     String cfgName = Server.escape(configName);
/* 41 */     String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", new Object[] { ctxName, cfgName });
/* 42 */     return new RingBufferAdmin(ringBuffer, name);
/*    */   }
/*    */   
/*    */   protected RingBufferAdmin(RingBuffer<?> ringBuffer, String mbeanName) {
/* 46 */     this.ringBuffer = ringBuffer;
/*    */     try {
/* 48 */       this.objectName = new ObjectName(mbeanName);
/* 49 */     } catch (Exception e) {
/* 50 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public long getBufferSize() {
/* 56 */     return (this.ringBuffer == null) ? 0L : this.ringBuffer.getBufferSize();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getRemainingCapacity() {
/* 61 */     return (this.ringBuffer == null) ? 0L : this.ringBuffer.remainingCapacity();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectName getObjectName() {
/* 72 */     return this.objectName;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\RingBufferAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */