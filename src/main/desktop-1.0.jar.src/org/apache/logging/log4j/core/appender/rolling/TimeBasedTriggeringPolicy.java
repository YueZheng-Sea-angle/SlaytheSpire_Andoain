/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "TimeBasedTriggeringPolicy", category = "Core", printObject = true)
/*     */ public final class TimeBasedTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*     */   private long nextRolloverMillis;
/*     */   private final int interval;
/*     */   private final boolean modulate;
/*     */   private final long maxRandomDelayMillis;
/*     */   private RollingFileManager manager;
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<TimeBasedTriggeringPolicy>
/*     */   {
/*     */     @PluginBuilderAttribute
/*  39 */     private int interval = 1;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean modulate = false;
/*     */     
/*     */     @PluginBuilderAttribute
/*  45 */     private int maxRandomDelay = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     public TimeBasedTriggeringPolicy build() {
/*  50 */       long maxRandomDelayMillis = TimeUnit.SECONDS.toMillis(this.maxRandomDelay);
/*  51 */       return new TimeBasedTriggeringPolicy(this.interval, this.modulate, maxRandomDelayMillis);
/*     */     }
/*     */     
/*     */     public int getInterval() {
/*  55 */       return this.interval;
/*     */     }
/*     */     
/*     */     public boolean isModulate() {
/*  59 */       return this.modulate;
/*     */     }
/*     */     
/*     */     public int getMaxRandomDelay() {
/*  63 */       return this.maxRandomDelay;
/*     */     }
/*     */     
/*     */     public Builder withInterval(int interval) {
/*  67 */       this.interval = interval;
/*  68 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withModulate(boolean modulate) {
/*  72 */       this.modulate = modulate;
/*  73 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withMaxRandomDelay(int maxRandomDelay) {
/*  77 */       this.maxRandomDelay = maxRandomDelay;
/*  78 */       return this;
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
/*     */   private TimeBasedTriggeringPolicy(int interval, boolean modulate, long maxRandomDelayMillis) {
/*  91 */     this.interval = interval;
/*  92 */     this.modulate = modulate;
/*  93 */     this.maxRandomDelayMillis = maxRandomDelayMillis;
/*     */   }
/*     */   
/*     */   public int getInterval() {
/*  97 */     return this.interval;
/*     */   }
/*     */   
/*     */   public long getNextRolloverMillis() {
/* 101 */     return this.nextRolloverMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager aManager) {
/* 110 */     this.manager = aManager;
/* 111 */     long current = aManager.getFileTime();
/* 112 */     if (current == 0L) {
/* 113 */       current = System.currentTimeMillis();
/*     */     }
/*     */ 
/*     */     
/* 117 */     aManager.getPatternProcessor().getNextTime(current, this.interval, this.modulate);
/* 118 */     aManager.getPatternProcessor().setTimeBased(true);
/*     */     
/* 120 */     this
/* 121 */       .nextRolloverMillis = ThreadLocalRandom.current().nextLong(0L, 1L + this.maxRandomDelayMillis) + aManager.getPatternProcessor().getNextTime(current, this.interval, this.modulate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/* 131 */     long nowMillis = event.getTimeMillis();
/* 132 */     if (nowMillis >= this.nextRolloverMillis) {
/* 133 */       this
/* 134 */         .nextRolloverMillis = ThreadLocalRandom.current().nextLong(0L, 1L + this.maxRandomDelayMillis) + this.manager.getPatternProcessor().getNextTime(nowMillis, this.interval, this.modulate);
/* 135 */       this.manager.getPatternProcessor().setCurrentFileTime(System.currentTimeMillis());
/* 136 */       return true;
/*     */     } 
/* 138 */     return false;
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
/*     */   @Deprecated
/*     */   public static TimeBasedTriggeringPolicy createPolicy(@PluginAttribute("interval") String interval, @PluginAttribute("modulate") String modulate) {
/* 152 */     return newBuilder()
/* 153 */       .withInterval(Integers.parseInt(interval, 1))
/* 154 */       .withModulate(Boolean.parseBoolean(modulate))
/* 155 */       .build();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 160 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return "TimeBasedTriggeringPolicy(nextRolloverMillis=" + this.nextRolloverMillis + ", interval=" + this.interval + ", modulate=" + this.modulate + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\TimeBasedTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */