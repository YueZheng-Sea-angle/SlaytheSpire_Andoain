/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class DiscardingAsyncQueueFullPolicy
/*    */   extends DefaultAsyncQueueFullPolicy
/*    */ {
/* 33 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final Level thresholdLevel;
/* 36 */   private final AtomicLong discardCount = new AtomicLong();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DiscardingAsyncQueueFullPolicy(Level thresholdLevel) {
/* 45 */     this.thresholdLevel = Objects.<Level>requireNonNull(thresholdLevel, "thresholdLevel");
/*    */   }
/*    */ 
/*    */   
/*    */   public EventRoute getRoute(long backgroundThreadId, Level level) {
/* 50 */     if (level.isLessSpecificThan(this.thresholdLevel)) {
/* 51 */       if (this.discardCount.getAndIncrement() == 0L) {
/* 52 */         LOGGER.warn("Async queue is full, discarding event with level {}. This message will only appear once; future events from {} are silently discarded until queue capacity becomes available.", level, this.thresholdLevel);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 57 */       return EventRoute.DISCARD;
/*    */     } 
/* 59 */     return super.getRoute(backgroundThreadId, level);
/*    */   }
/*    */   
/*    */   public static long getDiscardCount(AsyncQueueFullPolicy router) {
/* 63 */     if (router instanceof DiscardingAsyncQueueFullPolicy) {
/* 64 */       return ((DiscardingAsyncQueueFullPolicy)router).discardCount.get();
/*    */     }
/* 66 */     return 0L;
/*    */   }
/*    */   
/*    */   public Level getThresholdLevel() {
/* 70 */     return this.thresholdLevel;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\DiscardingAsyncQueueFullPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */