/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import org.apache.logging.log4j.core.util.Loader;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ReliabilityStrategyFactory
/*    */ {
/*    */   public static ReliabilityStrategy getReliabilityStrategy(LoggerConfig loggerConfig) {
/* 49 */     String strategy = PropertiesUtil.getProperties().getStringProperty("log4j.ReliabilityStrategy", "AwaitCompletion");
/*    */     
/* 51 */     if ("AwaitCompletion".equals(strategy)) {
/* 52 */       return new AwaitCompletionReliabilityStrategy(loggerConfig);
/*    */     }
/* 54 */     if ("AwaitUnconditionally".equals(strategy)) {
/* 55 */       return new AwaitUnconditionallyReliabilityStrategy(loggerConfig);
/*    */     }
/* 57 */     if ("Locking".equals(strategy)) {
/* 58 */       return new LockingReliabilityStrategy(loggerConfig);
/*    */     }
/*    */     try {
/* 61 */       Class<? extends ReliabilityStrategy> cls = Loader.loadClass(strategy).asSubclass(ReliabilityStrategy.class);
/*    */       
/* 63 */       return cls.getConstructor(new Class[] { LoggerConfig.class }).newInstance(new Object[] { loggerConfig });
/* 64 */     } catch (Exception dynamicFailed) {
/* 65 */       StatusLogger.getLogger().warn("Could not create ReliabilityStrategy for '{}', using default AwaitCompletionReliabilityStrategy: {}", strategy, dynamicFailed);
/*    */       
/* 67 */       return new AwaitCompletionReliabilityStrategy(loggerConfig);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ReliabilityStrategyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */