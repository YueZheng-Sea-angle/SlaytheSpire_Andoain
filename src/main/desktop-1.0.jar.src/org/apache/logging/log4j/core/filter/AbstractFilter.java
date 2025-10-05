/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ public abstract class AbstractFilter
/*     */   extends AbstractLifeCycle
/*     */   implements Filter
/*     */ {
/*     */   protected final Filter.Result onMatch;
/*     */   protected final Filter.Result onMismatch;
/*     */   
/*     */   public static abstract class AbstractFilterBuilder<B extends AbstractFilterBuilder<B>>
/*     */   {
/*     */     public static final String ATTR_ON_MISMATCH = "onMismatch";
/*     */     public static final String ATTR_ON_MATCH = "onMatch";
/*     */     @PluginBuilderAttribute("onMatch")
/*  46 */     private Filter.Result onMatch = Filter.Result.NEUTRAL;
/*     */     
/*     */     @PluginBuilderAttribute("onMismatch")
/*  49 */     private Filter.Result onMismatch = Filter.Result.DENY;
/*     */ 
/*     */     
/*     */     public Filter.Result getOnMatch() {
/*  53 */       return this.onMatch;
/*     */     }
/*     */     
/*     */     public Filter.Result getOnMismatch() {
/*  57 */       return this.onMismatch;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setOnMatch(Filter.Result onMatch) {
/*  66 */       this.onMatch = onMatch;
/*  67 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setOnMismatch(Filter.Result onMismatch) {
/*  76 */       this.onMismatch = onMismatch;
/*  77 */       return asBuilder();
/*     */     }
/*     */ 
/*     */     
/*     */     public B asBuilder() {
/*  82 */       return (B)this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFilter() {
/* 101 */     this(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFilter(Filter.Result onMatch, Filter.Result onMismatch) {
/* 110 */     this.onMatch = (onMatch == null) ? Filter.Result.NEUTRAL : onMatch;
/* 111 */     this.onMismatch = (onMismatch == null) ? Filter.Result.DENY : onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean equalsImpl(Object obj) {
/* 116 */     if (this == obj) {
/* 117 */       return true;
/*     */     }
/* 119 */     if (!super.equalsImpl(obj)) {
/* 120 */       return false;
/*     */     }
/* 122 */     if (getClass() != obj.getClass()) {
/* 123 */       return false;
/*     */     }
/* 125 */     AbstractFilter other = (AbstractFilter)obj;
/* 126 */     if (this.onMatch != other.onMatch) {
/* 127 */       return false;
/*     */     }
/* 129 */     if (this.onMismatch != other.onMismatch) {
/* 130 */       return false;
/*     */     }
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 142 */     return Filter.Result.NEUTRAL;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 157 */     return Filter.Result.NEUTRAL;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 172 */     return Filter.Result.NEUTRAL;
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 187 */     return Filter.Result.NEUTRAL;
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
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 203 */     return filter(logger, level, marker, msg, new Object[] { p0 });
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
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 220 */     return filter(logger, level, marker, msg, new Object[] { p0, p1 });
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 238 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2 });
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 257 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3 });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 278 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4 });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 300 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5 });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 323 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 348 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 374 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
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
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 401 */     return filter(logger, level, marker, msg, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Filter.Result getOnMatch() {
/* 410 */     return this.onMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Filter.Result getOnMismatch() {
/* 419 */     return this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int hashCodeImpl() {
/* 424 */     int prime = 31;
/* 425 */     int result = super.hashCodeImpl();
/* 426 */     result = 31 * result + ((this.onMatch == null) ? 0 : this.onMatch.hashCode());
/* 427 */     result = 31 * result + ((this.onMismatch == null) ? 0 : this.onMismatch.hashCode());
/* 428 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 433 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\AbstractFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */