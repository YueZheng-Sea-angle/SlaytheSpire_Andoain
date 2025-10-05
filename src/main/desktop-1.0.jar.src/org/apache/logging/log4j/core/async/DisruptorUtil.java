/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import com.lmax.disruptor.BlockingWaitStrategy;
/*     */ import com.lmax.disruptor.BusySpinWaitStrategy;
/*     */ import com.lmax.disruptor.ExceptionHandler;
/*     */ import com.lmax.disruptor.SleepingWaitStrategy;
/*     */ import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
/*     */ import com.lmax.disruptor.WaitStrategy;
/*     */ import com.lmax.disruptor.YieldingWaitStrategy;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ final class DisruptorUtil
/*     */ {
/*  43 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final int RINGBUFFER_MIN_SIZE = 128;
/*     */ 
/*     */   
/*     */   private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
/*     */ 
/*     */   
/*     */   private static final int RINGBUFFER_NO_GC_DEFAULT_SIZE = 4096;
/*     */   
/*  54 */   static final boolean ASYNC_LOGGER_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL = PropertiesUtil.getProperties()
/*  55 */     .getBooleanProperty("AsyncLogger.SynchronizeEnqueueWhenQueueFull", true);
/*  56 */   static final boolean ASYNC_CONFIG_SYNCHRONIZE_ENQUEUE_WHEN_QUEUE_FULL = PropertiesUtil.getProperties()
/*  57 */     .getBooleanProperty("AsyncLoggerConfig.SynchronizeEnqueueWhenQueueFull", true);
/*     */   
/*     */   static WaitStrategy createWaitStrategy(String propertyName) {
/*     */     long sleepTimeNs;
/*     */     String key;
/*     */     int retries;
/*  63 */     String strategy = PropertiesUtil.getProperties().getStringProperty(propertyName, "Timeout");
/*  64 */     LOGGER.trace("property {}={}", propertyName, strategy);
/*  65 */     String strategyUp = Strings.toRootUpperCase(strategy);
/*  66 */     long timeoutMillis = parseAdditionalLongProperty(propertyName, "Timeout", 10L);
/*     */ 
/*     */     
/*  69 */     switch (strategyUp) {
/*     */       
/*     */       case "SLEEP":
/*  72 */         sleepTimeNs = parseAdditionalLongProperty(propertyName, "SleepTimeNs", 100L);
/*  73 */         key = getFullPropertyKey(propertyName, "Retries");
/*     */         
/*  75 */         retries = PropertiesUtil.getProperties().getIntegerProperty(key, 200);
/*  76 */         return (WaitStrategy)new SleepingWaitStrategy(retries, sleepTimeNs);
/*     */       case "YIELD":
/*  78 */         return (WaitStrategy)new YieldingWaitStrategy();
/*     */       case "BLOCK":
/*  80 */         return (WaitStrategy)new BlockingWaitStrategy();
/*     */       case "BUSYSPIN":
/*  82 */         return (WaitStrategy)new BusySpinWaitStrategy();
/*     */       case "TIMEOUT":
/*  84 */         return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
/*     */     } 
/*  86 */     return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getFullPropertyKey(String strategyKey, String additionalKey) {
/*  91 */     return strategyKey.startsWith("AsyncLogger.") ? ("AsyncLogger." + additionalKey) : ("AsyncLoggerConfig." + additionalKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseAdditionalLongProperty(String propertyName, String additionalKey, long defaultValue) {
/* 100 */     String key = getFullPropertyKey(propertyName, additionalKey);
/* 101 */     return PropertiesUtil.getProperties().getLongProperty(key, defaultValue);
/*     */   }
/*     */   
/*     */   static int calculateRingBufferSize(String propertyName) {
/* 105 */     int ringBufferSize = Constants.ENABLE_THREADLOCALS ? 4096 : 262144;
/* 106 */     String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, 
/* 107 */         String.valueOf(ringBufferSize));
/*     */     try {
/* 109 */       int size = Integer.parseInt(userPreferredRBSize);
/* 110 */       if (size < 128) {
/* 111 */         size = 128;
/* 112 */         LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", userPreferredRBSize, 
/* 113 */             Integer.valueOf(128));
/*     */       } 
/* 115 */       ringBufferSize = size;
/* 116 */     } catch (Exception ex) {
/* 117 */       LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", userPreferredRBSize, Integer.valueOf(ringBufferSize));
/*     */     } 
/* 119 */     return Integers.ceilingNextPowerOfTwo(ringBufferSize);
/*     */   }
/*     */   
/*     */   static ExceptionHandler<RingBufferLogEvent> getAsyncLoggerExceptionHandler() {
/* 123 */     String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLogger.ExceptionHandler");
/* 124 */     if (cls == null) {
/* 125 */       return new AsyncLoggerDefaultExceptionHandler();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 130 */       Class<? extends ExceptionHandler<RingBufferLogEvent>> klass = Loader.loadClass(cls);
/* 131 */       return klass.newInstance();
/* 132 */     } catch (Exception ignored) {
/* 133 */       LOGGER.debug("Invalid AsyncLogger.ExceptionHandler value: error creating {}: ", cls, ignored);
/* 134 */       return new AsyncLoggerDefaultExceptionHandler();
/*     */     } 
/*     */   }
/*     */   
/*     */   static ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper> getAsyncLoggerConfigExceptionHandler() {
/* 139 */     String cls = PropertiesUtil.getProperties().getStringProperty("AsyncLoggerConfig.ExceptionHandler");
/* 140 */     if (cls == null) {
/* 141 */       return new AsyncLoggerConfigDefaultExceptionHandler();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 146 */       Class<? extends ExceptionHandler<AsyncLoggerConfigDisruptor.Log4jEventWrapper>> klass = Loader.loadClass(cls);
/* 147 */       return klass.newInstance();
/* 148 */     } catch (Exception ignored) {
/* 149 */       LOGGER.debug("Invalid AsyncLoggerConfig.ExceptionHandler value: error creating {}: ", cls, ignored);
/* 150 */       return new AsyncLoggerConfigDefaultExceptionHandler();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getExecutorThreadId(ExecutorService executor) {
/* 162 */     Future<Long> result = executor.submit(() -> Long.valueOf(Thread.currentThread().getId()));
/*     */     try {
/* 164 */       return ((Long)result.get()).longValue();
/* 165 */     } catch (Exception ex) {
/* 166 */       String msg = "Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.";
/*     */       
/* 168 */       throw new IllegalStateException("Could not obtain executor thread Id. Giving up to avoid the risk of application deadlock.", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\DisruptorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */