/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.rolling.FileSize;
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
/*     */ @Plugin(name = "IfAccumulatedFileSize", category = "Core", printObject = true)
/*     */ public final class IfAccumulatedFileSize
/*     */   implements PathCondition
/*     */ {
/*  39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private final long thresholdBytes;
/*     */   private long accumulatedSize;
/*     */   private final PathCondition[] nestedConditions;
/*     */   
/*     */   private IfAccumulatedFileSize(long thresholdSize, PathCondition... nestedConditions) {
/*  45 */     if (thresholdSize <= 0L) {
/*  46 */       throw new IllegalArgumentException("Count must be a positive integer but was " + thresholdSize);
/*     */     }
/*  48 */     this.thresholdBytes = thresholdSize;
/*  49 */     this.nestedConditions = PathCondition.copy(nestedConditions);
/*     */   }
/*     */   
/*     */   public long getThresholdBytes() {
/*  53 */     return this.thresholdBytes;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  57 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
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
/*  68 */     this.accumulatedSize += attrs.size();
/*  69 */     boolean result = (this.accumulatedSize > this.thresholdBytes);
/*  70 */     String match = result ? ">" : "<=";
/*  71 */     String accept = result ? "ACCEPTED" : "REJECTED";
/*  72 */     LOGGER.trace("IfAccumulatedFileSize {}: {} accumulated size '{}' {} thresholdBytes '{}'", accept, relativePath, 
/*  73 */         Long.valueOf(this.accumulatedSize), match, Long.valueOf(this.thresholdBytes));
/*  74 */     if (result) {
/*  75 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/*  87 */     this.accumulatedSize = 0L;
/*  88 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   public static IfAccumulatedFileSize createFileSizeCondition(@PluginAttribute("exceeds") String size, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 104 */     if (size == null) {
/* 105 */       LOGGER.error("IfAccumulatedFileSize missing mandatory size threshold.");
/*     */     }
/* 107 */     long threshold = (size == null) ? Long.MAX_VALUE : FileSize.parse(size, Long.MAX_VALUE);
/* 108 */     return new IfAccumulatedFileSize(threshold, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 114 */     return "IfAccumulatedFileSize(exceeds=" + this.thresholdBytes + nested + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAccumulatedFileSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */