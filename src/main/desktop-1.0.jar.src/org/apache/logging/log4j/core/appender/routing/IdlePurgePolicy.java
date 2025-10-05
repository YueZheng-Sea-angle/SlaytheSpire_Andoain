/*     */ package org.apache.logging.log4j.core.appender.routing;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationScheduler;
/*     */ import org.apache.logging.log4j.core.config.Scheduled;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "IdlePurgePolicy", category = "Core", printObject = true)
/*     */ @Scheduled
/*     */ public class IdlePurgePolicy
/*     */   extends AbstractLifeCycle
/*     */   implements PurgePolicy, Runnable
/*     */ {
/*     */   private final long timeToLive;
/*     */   private final long checkInterval;
/*  45 */   private final ConcurrentMap<String, Long> appendersUsage = new ConcurrentHashMap<>();
/*     */   private RoutingAppender routingAppender;
/*     */   private final ConfigurationScheduler scheduler;
/*     */   private volatile ScheduledFuture<?> future;
/*     */   
/*     */   public IdlePurgePolicy(long timeToLive, long checkInterval, ConfigurationScheduler scheduler) {
/*  51 */     this.timeToLive = timeToLive;
/*  52 */     this.checkInterval = checkInterval;
/*  53 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize(RoutingAppender routingAppender) {
/*  58 */     this.routingAppender = routingAppender;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  63 */     setStopping();
/*  64 */     boolean stopped = stop(this.future);
/*  65 */     setStopped();
/*  66 */     return stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void purge() {
/*  74 */     long createTime = System.currentTimeMillis() - this.timeToLive;
/*  75 */     for (Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
/*  76 */       long entryValue = ((Long)entry.getValue()).longValue();
/*  77 */       if (entryValue < createTime && 
/*  78 */         this.appendersUsage.remove(entry.getKey(), Long.valueOf(entryValue))) {
/*  79 */         LOGGER.debug("Removing appender {}", entry.getKey());
/*  80 */         this.routingAppender.deleteAppender(entry.getKey());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(String key, LogEvent event) {
/*  88 */     long now = System.currentTimeMillis();
/*  89 */     this.appendersUsage.put(key, Long.valueOf(now));
/*  90 */     if (this.future == null) {
/*  91 */       synchronized (this) {
/*  92 */         if (this.future == null) {
/*  93 */           scheduleNext();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 102 */     purge();
/* 103 */     scheduleNext();
/*     */   }
/*     */   
/*     */   private void scheduleNext() {
/* 107 */     long updateTime = Long.MAX_VALUE;
/* 108 */     for (Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
/* 109 */       if (((Long)entry.getValue()).longValue() < updateTime) {
/* 110 */         updateTime = ((Long)entry.getValue()).longValue();
/*     */       }
/*     */     } 
/*     */     
/* 114 */     if (updateTime < Long.MAX_VALUE) {
/* 115 */       long interval = this.timeToLive - System.currentTimeMillis() - updateTime;
/* 116 */       this.future = this.scheduler.schedule(this, interval, TimeUnit.MILLISECONDS);
/*     */     } else {
/*     */       
/* 119 */       this.future = this.scheduler.schedule(this, this.checkInterval, TimeUnit.MILLISECONDS);
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
/*     */   @PluginFactory
/*     */   public static PurgePolicy createPurgePolicy(@PluginAttribute("timeToLive") String timeToLive, @PluginAttribute("checkInterval") String checkInterval, @PluginAttribute("timeUnit") String timeUnit, @PluginConfiguration Configuration configuration) {
/*     */     TimeUnit units;
/*     */     long ci;
/* 138 */     if (timeToLive == null) {
/* 139 */       LOGGER.error("A timeToLive value is required");
/* 140 */       return null;
/*     */     } 
/*     */     
/* 143 */     if (timeUnit == null) {
/* 144 */       units = TimeUnit.MINUTES;
/*     */     } else {
/*     */       try {
/* 147 */         units = TimeUnit.valueOf(timeUnit.toUpperCase());
/* 148 */       } catch (Exception ex) {
/* 149 */         LOGGER.error("Invalid timeUnit value {}. timeUnit set to MINUTES", timeUnit, ex);
/* 150 */         units = TimeUnit.MINUTES;
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     long ttl = units.toMillis(Long.parseLong(timeToLive));
/* 155 */     if (ttl < 0L) {
/* 156 */       LOGGER.error("timeToLive must be positive. timeToLive set to 0");
/* 157 */       ttl = 0L;
/*     */     } 
/*     */ 
/*     */     
/* 161 */     if (checkInterval == null) {
/* 162 */       ci = ttl;
/*     */     } else {
/* 164 */       ci = units.toMillis(Long.parseLong(checkInterval));
/* 165 */       if (ci < 0L) {
/* 166 */         LOGGER.error("checkInterval must be positive. checkInterval set equal to timeToLive = {}", Long.valueOf(ttl));
/* 167 */         ci = ttl;
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     return new IdlePurgePolicy(ttl, ci, configuration.getScheduler());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 176 */     return "timeToLive=" + this.timeToLive;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\routing\IdlePurgePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */