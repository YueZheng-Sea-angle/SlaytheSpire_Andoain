/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ @Plugin(name = "OnStartupTriggeringPolicy", category = "Core", printObject = true)
/*     */ public class OnStartupTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*  35 */   private static final long JVM_START_TIME = initStartTime();
/*     */   
/*     */   private final long minSize;
/*     */   
/*     */   private OnStartupTriggeringPolicy(long minSize) {
/*  40 */     this.minSize = minSize;
/*     */   }
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
/*     */   private static long initStartTime() {
/*     */     try {
/*  55 */       Class<?> factoryClass = Loader.loadSystemClass("java.lang.management.ManagementFactory");
/*  56 */       Method getRuntimeMXBean = factoryClass.getMethod("getRuntimeMXBean", new Class[0]);
/*  57 */       Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
/*     */       
/*  59 */       Class<?> runtimeMXBeanClass = Loader.loadSystemClass("java.lang.management.RuntimeMXBean");
/*  60 */       Method getStartTime = runtimeMXBeanClass.getMethod("getStartTime", new Class[0]);
/*  61 */       Long result = (Long)getStartTime.invoke(runtimeMXBean, new Object[0]);
/*     */       
/*  63 */       return result.longValue();
/*  64 */     } catch (Throwable t) {
/*  65 */       StatusLogger.getLogger().error("Unable to call ManagementFactory.getRuntimeMXBean().getStartTime(), using system time for OnStartupTriggeringPolicy", t);
/*     */ 
/*     */       
/*  68 */       return System.currentTimeMillis();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager manager) {
/*  78 */     if (manager.getFileTime() < JVM_START_TIME && manager.getFileSize() >= this.minSize) {
/*  79 */       StatusLogger.getLogger().debug("Initiating rollover at startup");
/*  80 */       if (this.minSize == 0L) {
/*  81 */         manager.setRenameEmptyFiles(true);
/*     */       }
/*  83 */       manager.skipFooter(true);
/*  84 */       manager.rollover();
/*  85 */       manager.skipFooter(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     return "OnStartupTriggeringPolicy";
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static OnStartupTriggeringPolicy createPolicy(@PluginAttribute(value = "minSize", defaultLong = 1L) long minSize) {
/* 107 */     return new OnStartupTriggeringPolicy(minSize);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\OnStartupTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */