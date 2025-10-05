/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationScheduler;
/*     */ import org.apache.logging.log4j.core.config.CronScheduledFuture;
/*     */ import org.apache.logging.log4j.core.config.Scheduled;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.CronExpression;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "CronTriggeringPolicy", category = "Core", printObject = true)
/*     */ @Scheduled
/*     */ public final class CronTriggeringPolicy
/*     */   extends AbstractTriggeringPolicy
/*     */ {
/*     */   private static final String defaultSchedule = "0 0 0 * * ?";
/*     */   private RollingFileManager manager;
/*     */   private final CronExpression cronExpression;
/*     */   private final Configuration configuration;
/*     */   private final boolean checkOnStartup;
/*     */   private volatile Date lastRollDate;
/*     */   private CronScheduledFuture<?> future;
/*     */   
/*     */   private CronTriggeringPolicy(CronExpression schedule, boolean checkOnStartup, Configuration configuration) {
/*  53 */     this.cronExpression = Objects.<CronExpression>requireNonNull(schedule, "schedule");
/*  54 */     this.configuration = Objects.<Configuration>requireNonNull(configuration, "configuration");
/*  55 */     this.checkOnStartup = checkOnStartup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize(RollingFileManager aManager) {
/*  66 */     this.manager = aManager;
/*  67 */     Date now = new Date();
/*  68 */     Date lastRollForFile = this.cronExpression.getPrevFireTime(new Date(this.manager.getFileTime()));
/*  69 */     Date lastRegularRoll = this.cronExpression.getPrevFireTime(new Date());
/*  70 */     aManager.getPatternProcessor().setCurrentFileTime(lastRegularRoll.getTime());
/*  71 */     LOGGER.debug("LastRollForFile {}, LastRegularRole {}", lastRollForFile, lastRegularRoll);
/*  72 */     aManager.getPatternProcessor().setPrevFileTime(lastRegularRoll.getTime());
/*  73 */     aManager.getPatternProcessor().setTimeBased(true);
/*  74 */     if (this.checkOnStartup && lastRollForFile != null && lastRegularRoll != null && lastRollForFile
/*  75 */       .before(lastRegularRoll)) {
/*  76 */       this.lastRollDate = lastRollForFile;
/*  77 */       rollover();
/*     */     } 
/*     */     
/*  80 */     ConfigurationScheduler scheduler = this.configuration.getScheduler();
/*  81 */     if (!scheduler.isExecutorServiceSet())
/*     */     {
/*  83 */       scheduler.incrementScheduledItems();
/*     */     }
/*  85 */     if (!scheduler.isStarted()) {
/*  86 */       scheduler.start();
/*     */     }
/*  88 */     this.lastRollDate = lastRegularRoll;
/*  89 */     this.future = scheduler.scheduleWithCron(this.cronExpression, now, new CronTrigger());
/*  90 */     LOGGER.debug(scheduler.toString());
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
/*     */   public boolean isTriggeringEvent(LogEvent event) {
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public CronExpression getCronExpression() {
/* 106 */     return this.cronExpression;
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
/*     */   @PluginFactory
/*     */   public static CronTriggeringPolicy createPolicy(@PluginConfiguration Configuration configuration, @PluginAttribute("evaluateOnStartup") String evaluateOnStartup, @PluginAttribute("schedule") String schedule) {
/*     */     CronExpression cronExpression;
/* 125 */     boolean checkOnStartup = Boolean.parseBoolean(evaluateOnStartup);
/* 126 */     if (schedule == null) {
/* 127 */       LOGGER.info("No schedule specified, defaulting to Daily");
/* 128 */       cronExpression = getSchedule("0 0 0 * * ?");
/*     */     } else {
/* 130 */       cronExpression = getSchedule(schedule);
/* 131 */       if (cronExpression == null) {
/* 132 */         LOGGER.error("Invalid expression specified. Defaulting to Daily");
/* 133 */         cronExpression = getSchedule("0 0 0 * * ?");
/*     */       } 
/*     */     } 
/* 136 */     return new CronTriggeringPolicy(cronExpression, checkOnStartup, configuration);
/*     */   }
/*     */   
/*     */   private static CronExpression getSchedule(String expression) {
/*     */     try {
/* 141 */       return new CronExpression(expression);
/* 142 */     } catch (ParseException pe) {
/* 143 */       LOGGER.error("Invalid cron expression - " + expression, pe);
/* 144 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rollover() {
/* 149 */     this.manager.rollover(this.cronExpression.getPrevFireTime(new Date()), this.lastRollDate);
/* 150 */     if (this.future != null) {
/* 151 */       this.lastRollDate = this.future.getFireTime();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 157 */     setStopping();
/* 158 */     boolean stopped = stop((Future)this.future);
/* 159 */     setStopped();
/* 160 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 165 */     return "CronTriggeringPolicy(schedule=" + this.cronExpression.getCronExpression() + ")";
/*     */   }
/*     */   
/*     */   private class CronTrigger implements Runnable {
/*     */     private CronTrigger() {}
/*     */     
/*     */     public void run() {
/* 172 */       CronTriggeringPolicy.this.rollover();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\CronTriggeringPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */