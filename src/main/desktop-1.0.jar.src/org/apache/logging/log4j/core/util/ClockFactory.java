/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClockFactory
/*     */ {
/*     */   public static final String PROPERTY_NAME = "log4j.Clock";
/*  37 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Clock getClock() {
/*  67 */     return createClock();
/*     */   }
/*     */   
/*     */   private static Map<String, Supplier<Clock>> aliases() {
/*  71 */     Map<String, Supplier<Clock>> result = new HashMap<>();
/*  72 */     result.put("SystemClock", SystemClock::new);
/*  73 */     result.put("SystemMillisClock", SystemMillisClock::new);
/*  74 */     result.put("CachedClock", CachedClock::instance);
/*  75 */     result.put("CoarseCachedClock", CoarseCachedClock::instance);
/*  76 */     result.put("org.apache.logging.log4j.core.util.CachedClock", CachedClock::instance);
/*  77 */     result.put("org.apache.logging.log4j.core.util.CoarseCachedClock", CoarseCachedClock::instance);
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   private static Clock createClock() {
/*  82 */     String userRequest = PropertiesUtil.getProperties().getStringProperty("log4j.Clock");
/*  83 */     if (userRequest == null) {
/*  84 */       LOGGER.trace("Using default SystemClock for timestamps.");
/*  85 */       return logSupportedPrecision(new SystemClock());
/*     */     } 
/*  87 */     Supplier<Clock> specified = aliases().get(userRequest);
/*  88 */     if (specified != null) {
/*  89 */       LOGGER.trace("Using specified {} for timestamps.", userRequest);
/*  90 */       return logSupportedPrecision((Clock)specified.get());
/*     */     } 
/*     */     try {
/*  93 */       Clock result = Loader.<Clock>newCheckedInstanceOf(userRequest, Clock.class);
/*  94 */       LOGGER.trace("Using {} for timestamps.", result.getClass().getName());
/*  95 */       return logSupportedPrecision(result);
/*  96 */     } catch (Exception e) {
/*  97 */       String fmt = "Could not create {}: {}, using default SystemClock for timestamps.";
/*  98 */       LOGGER.error("Could not create {}: {}, using default SystemClock for timestamps.", userRequest, e);
/*  99 */       return logSupportedPrecision(new SystemClock());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Clock logSupportedPrecision(Clock clock) {
/* 104 */     String support = (clock instanceof org.apache.logging.log4j.core.time.PreciseClock) ? "supports" : "does not support";
/* 105 */     LOGGER.debug("{} {} precise timestamps.", clock.getClass().getName(), support);
/* 106 */     return clock;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ClockFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */