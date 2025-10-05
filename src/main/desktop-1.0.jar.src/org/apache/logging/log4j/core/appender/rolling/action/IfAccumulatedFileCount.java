/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "IfAccumulatedFileCount", category = "Core", printObject = true)
/*     */ public final class IfAccumulatedFileCount
/*     */   implements PathCondition
/*     */ {
/*  38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private final int threshold;
/*     */   private int count;
/*     */   private final PathCondition[] nestedConditions;
/*     */   
/*     */   private IfAccumulatedFileCount(int thresholdParam, PathCondition... nestedConditions) {
/*  44 */     if (thresholdParam <= 0) {
/*  45 */       throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdParam);
/*     */     }
/*  47 */     this.threshold = thresholdParam;
/*  48 */     this.nestedConditions = PathCondition.copy(nestedConditions);
/*     */   }
/*     */   
/*     */   public int getThresholdCount() {
/*  52 */     return this.threshold;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  56 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
/*  67 */     boolean result = (++this.count > this.threshold);
/*  68 */     String match = result ? ">" : "<=";
/*  69 */     String accept = result ? "ACCEPTED" : "REJECTED";
/*  70 */     LOGGER.trace("IfAccumulatedFileCount {}: {} count '{}' {} threshold '{}'", accept, relativePath, Integer.valueOf(this.count), match, 
/*  71 */         Integer.valueOf(this.threshold));
/*  72 */     if (result) {
/*  73 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/*  75 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/*  85 */     this.count = 0;
/*  86 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   
/*     */   @PluginFactory
/*     */   public static IfAccumulatedFileCount createFileCountCondition(@PluginAttribute(value = "exceeds", defaultInt = 2147483647) int threshold, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 102 */     if (threshold == Integer.MAX_VALUE) {
/* 103 */       LOGGER.error("IfAccumulatedFileCount invalid or missing threshold value.");
/*     */     }
/* 105 */     return new IfAccumulatedFileCount(threshold, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 111 */     return "IfAccumulatedFileCount(exceeds=" + this.threshold + nested + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAccumulatedFileCount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */