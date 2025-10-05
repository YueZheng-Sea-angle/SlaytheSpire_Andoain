/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "LevelRangeFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class LevelRangeFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final Level maxLevel;
/*     */   private final Level minLevel;
/*     */   
/*     */   @PluginFactory
/*     */   public static LevelRangeFilter createFilter(@PluginAttribute("minLevel") Level minLevel, @PluginAttribute("maxLevel") Level maxLevel, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/*  67 */     Level actualMinLevel = (minLevel == null) ? Level.ERROR : minLevel;
/*  68 */     Level actualMaxLevel = (maxLevel == null) ? Level.ERROR : maxLevel;
/*  69 */     Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
/*  70 */     Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
/*  71 */     return new LevelRangeFilter(actualMinLevel, actualMaxLevel, onMatch, onMismatch);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelRangeFilter(Level minLevel, Level maxLevel, Filter.Result onMatch, Filter.Result onMismatch) {
/*  78 */     super(onMatch, onMismatch);
/*  79 */     this.minLevel = minLevel;
/*  80 */     this.maxLevel = maxLevel;
/*     */   }
/*     */   
/*     */   private Filter.Result filter(Level level) {
/*  84 */     return level.isInRange(this.minLevel, this.maxLevel) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  89 */     return filter(event.getLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  95 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 101 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 107 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 113 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 119 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 125 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 131 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 138 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 145 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 152 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 160 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 168 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 176 */     return filter(level);
/*     */   }
/*     */   
/*     */   public Level getMinLevel() {
/* 180 */     return this.minLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 185 */     return this.minLevel.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\LevelRangeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */