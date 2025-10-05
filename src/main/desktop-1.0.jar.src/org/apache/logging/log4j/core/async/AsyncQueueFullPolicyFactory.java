/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncQueueFullPolicyFactory
/*     */ {
/*     */   static final String PROPERTY_NAME_ASYNC_EVENT_ROUTER = "log4j2.AsyncQueueFullPolicy";
/*     */   static final String PROPERTY_VALUE_DEFAULT_ASYNC_EVENT_ROUTER = "Default";
/*     */   static final String PROPERTY_VALUE_DISCARDING_ASYNC_EVENT_ROUTER = "Discard";
/*     */   static final String PROPERTY_NAME_DISCARDING_THRESHOLD_LEVEL = "log4j2.DiscardThreshold";
/*  51 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*     */   public static AsyncQueueFullPolicy create() {
/*  68 */     String router = PropertiesUtil.getProperties().getStringProperty("log4j2.AsyncQueueFullPolicy");
/*  69 */     if (router == null || isRouterSelected(router, (Class)DefaultAsyncQueueFullPolicy.class, "Default"))
/*     */     {
/*  71 */       return new DefaultAsyncQueueFullPolicy();
/*     */     }
/*  73 */     if (isRouterSelected(router, (Class)DiscardingAsyncQueueFullPolicy.class, "Discard"))
/*     */     {
/*  75 */       return createDiscardingAsyncQueueFullPolicy();
/*     */     }
/*  77 */     return createCustomRouter(router);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isRouterSelected(String propertyValue, Class<? extends AsyncQueueFullPolicy> policy, String shortPropertyValue) {
/*  84 */     return (propertyValue != null && (shortPropertyValue.equalsIgnoreCase(propertyValue) || policy
/*  85 */       .getName().equals(propertyValue) || policy
/*  86 */       .getSimpleName().equals(propertyValue)));
/*     */   }
/*     */   
/*     */   private static AsyncQueueFullPolicy createCustomRouter(String router) {
/*     */     try {
/*  91 */       Class<? extends AsyncQueueFullPolicy> cls = Loader.loadClass(router).asSubclass(AsyncQueueFullPolicy.class);
/*  92 */       LOGGER.debug("Creating custom AsyncQueueFullPolicy '{}'", router);
/*  93 */       return cls.newInstance();
/*  94 */     } catch (Exception ex) {
/*  95 */       LOGGER.debug("Using DefaultAsyncQueueFullPolicy. Could not create custom AsyncQueueFullPolicy '{}': {}", router, ex
/*  96 */           .toString());
/*  97 */       return new DefaultAsyncQueueFullPolicy();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static AsyncQueueFullPolicy createDiscardingAsyncQueueFullPolicy() {
/* 102 */     PropertiesUtil util = PropertiesUtil.getProperties();
/* 103 */     String level = util.getStringProperty("log4j2.DiscardThreshold", Level.INFO.name());
/* 104 */     Level thresholdLevel = Level.toLevel(level, Level.INFO);
/* 105 */     LOGGER.debug("Creating custom DiscardingAsyncQueueFullPolicy(discardThreshold:{})", thresholdLevel);
/* 106 */     return new DiscardingAsyncQueueFullPolicy(thresholdLevel);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncQueueFullPolicyFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */