/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.DelayQueue;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "BurstFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class BurstFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private static final long NANOS_IN_SECONDS = 1000000000L;
/*     */   private static final int DEFAULT_RATE = 10;
/*     */   private static final int DEFAULT_RATE_MULTIPLE = 100;
/*     */   private static final int HASH_SHIFT = 32;
/*     */   private final Level level;
/*     */   private final long burstInterval;
/*  77 */   private final DelayQueue<LogDelay> history = new DelayQueue<>();
/*     */   
/*  79 */   private final Queue<LogDelay> available = new ConcurrentLinkedQueue<>();
/*     */   
/*     */   static LogDelay createLogDelay(long expireTime) {
/*  82 */     return new LogDelay(expireTime);
/*     */   }
/*     */ 
/*     */   
/*     */   private BurstFilter(Level level, float rate, long maxBurst, Filter.Result onMatch, Filter.Result onMismatch) {
/*  87 */     super(onMatch, onMismatch);
/*  88 */     this.level = level;
/*  89 */     this.burstInterval = (long)(1.0E9F * (float)maxBurst / rate);
/*  90 */     for (int i = 0; i < maxBurst; i++) {
/*  91 */       this.available.add(createLogDelay(0L));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  98 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 104 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 110 */     return filter(level);
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 115 */     return filter(event.getLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 121 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 127 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 133 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 139 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 146 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 153 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 160 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 168 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 176 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 184 */     return filter(level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Filter.Result filter(Level level) {
/* 195 */     if (this.level.isMoreSpecificThan(level)) {
/* 196 */       LogDelay delay = this.history.poll();
/* 197 */       while (delay != null) {
/* 198 */         this.available.add(delay);
/* 199 */         delay = this.history.poll();
/*     */       } 
/* 201 */       delay = this.available.poll();
/* 202 */       if (delay != null) {
/* 203 */         delay.setDelay(this.burstInterval);
/* 204 */         this.history.add(delay);
/* 205 */         return this.onMatch;
/*     */       } 
/* 207 */       return this.onMismatch;
/*     */     } 
/* 209 */     return this.onMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAvailable() {
/* 218 */     return this.available.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 225 */     for (LogDelay delay : this.history) {
/* 226 */       this.history.remove(delay);
/* 227 */       this.available.add(delay);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     return "level=" + this.level.toString() + ", interval=" + this.burstInterval + ", max=" + this.history.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LogDelay
/*     */     implements Delayed
/*     */   {
/*     */     private long expireTime;
/*     */ 
/*     */     
/*     */     LogDelay(long expireTime) {
/* 244 */       this.expireTime = expireTime;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDelay(long delay) {
/* 250 */       this.expireTime = delay + System.nanoTime();
/*     */     }
/*     */ 
/*     */     
/*     */     public long getDelay(TimeUnit timeUnit) {
/* 255 */       return timeUnit.convert(this.expireTime - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Delayed delayed) {
/* 260 */       long diff = this.expireTime - ((LogDelay)delayed).expireTime;
/* 261 */       return Long.signum(diff);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 266 */       if (this == o) {
/* 267 */         return true;
/*     */       }
/* 269 */       if (o == null || getClass() != o.getClass()) {
/* 270 */         return false;
/*     */       }
/*     */       
/* 273 */       LogDelay logDelay = (LogDelay)o;
/*     */       
/* 275 */       if (this.expireTime != logDelay.expireTime) {
/* 276 */         return false;
/*     */       }
/*     */       
/* 279 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 284 */       return (int)(this.expireTime ^ this.expireTime >>> 32L);
/*     */     }
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 290 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder extends AbstractFilter.AbstractFilterBuilder<Builder> implements org.apache.logging.log4j.core.util.Builder<BurstFilter> {
/*     */     @PluginBuilderAttribute
/* 295 */     private Level level = Level.WARN;
/*     */     
/*     */     @PluginBuilderAttribute
/* 298 */     private float rate = 10.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private long maxBurst;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLevel(Level level) {
/* 310 */       this.level = level;
/* 311 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setRate(float rate) {
/* 320 */       this.rate = rate;
/* 321 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setMaxBurst(long maxBurst) {
/* 331 */       this.maxBurst = maxBurst;
/* 332 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public BurstFilter build() {
/* 337 */       if (this.rate <= 0.0F) {
/* 338 */         this.rate = 10.0F;
/*     */       }
/* 340 */       if (this.maxBurst <= 0L) {
/* 341 */         this.maxBurst = (long)(this.rate * 100.0F);
/*     */       }
/* 343 */       return new BurstFilter(this.level, this.rate, this.maxBurst, getOnMatch(), getOnMismatch());
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\BurstFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */