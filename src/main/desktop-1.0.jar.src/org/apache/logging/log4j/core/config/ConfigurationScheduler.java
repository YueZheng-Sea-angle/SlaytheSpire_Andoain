/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.util.CronExpression;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
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
/*     */ public class ConfigurationScheduler
/*     */   extends AbstractLifeCycle
/*     */ {
/*  38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*  39 */   private static final String SIMPLE_NAME = "Log4j2 " + ConfigurationScheduler.class.getSimpleName();
/*     */   
/*     */   private static final int MAX_SCHEDULED_ITEMS = 5;
/*     */   private volatile ScheduledExecutorService executorService;
/*  43 */   private int scheduledItems = 0;
/*     */   private final String name;
/*     */   
/*     */   public ConfigurationScheduler() {
/*  47 */     this(SIMPLE_NAME);
/*     */   }
/*     */   
/*     */   public ConfigurationScheduler(String name) {
/*  51 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  56 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  61 */     setStopping();
/*  62 */     if (isExecutorServiceSet()) {
/*  63 */       LOGGER.debug("{} shutting down threads in {}", this.name, getExecutorService());
/*  64 */       this.executorService.shutdown();
/*     */       try {
/*  66 */         this.executorService.awaitTermination(timeout, timeUnit);
/*  67 */       } catch (InterruptedException ie) {
/*  68 */         this.executorService.shutdownNow();
/*     */         try {
/*  70 */           this.executorService.awaitTermination(timeout, timeUnit);
/*  71 */         } catch (InterruptedException inner) {
/*  72 */           LOGGER.warn("{} stopped but some scheduled services may not have completed.", this.name);
/*     */         } 
/*     */         
/*  75 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*  78 */     setStopped();
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isExecutorServiceSet() {
/*  83 */     return (this.executorService != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void incrementScheduledItems() {
/*  90 */     if (isExecutorServiceSet()) {
/*  91 */       LOGGER.error("{} attempted to increment scheduled items after start", this.name);
/*     */     } else {
/*  93 */       this.scheduledItems++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decrementScheduledItems() {
/* 101 */     if (!isStarted() && this.scheduledItems > 0) {
/* 102 */       this.scheduledItems--;
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
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 116 */     return getExecutorService().schedule(callable, delay, unit);
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
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 128 */     return getExecutorService().schedule(command, delay, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronScheduledFuture<?> scheduleWithCron(CronExpression cronExpression, Runnable command) {
/* 139 */     return scheduleWithCron(cronExpression, new Date(), command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CronScheduledFuture<?> scheduleWithCron(CronExpression cronExpression, Date startDate, Runnable command) {
/* 150 */     Date fireDate = cronExpression.getNextValidTimeAfter((startDate == null) ? new Date() : startDate);
/* 151 */     CronRunnable runnable = new CronRunnable(command, cronExpression);
/* 152 */     ScheduledFuture<?> future = schedule(runnable, nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
/* 153 */     CronScheduledFuture<?> cronScheduledFuture = new CronScheduledFuture(future, fireDate);
/* 154 */     runnable.setScheduledFuture(cronScheduledFuture);
/* 155 */     LOGGER.debug("{} scheduled cron expression {} to fire at {}", this.name, cronExpression.getCronExpression(), fireDate);
/* 156 */     return cronScheduledFuture;
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
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 172 */     return getExecutorService().scheduleAtFixedRate(command, initialDelay, period, unit);
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
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 186 */     return getExecutorService().scheduleWithFixedDelay(command, initialDelay, delay, unit);
/*     */   }
/*     */   
/*     */   public long nextFireInterval(Date fireDate) {
/* 190 */     return fireDate.getTime() - (new Date()).getTime();
/*     */   }
/*     */   
/*     */   private ScheduledExecutorService getExecutorService() {
/* 194 */     if (this.executorService == null) {
/* 195 */       synchronized (this) {
/* 196 */         if (this.executorService == null) {
/* 197 */           if (this.scheduledItems > 0) {
/* 198 */             LOGGER.debug("{} starting {} threads", this.name, Integer.valueOf(this.scheduledItems));
/* 199 */             this.scheduledItems = Math.min(this.scheduledItems, 5);
/*     */             
/* 201 */             ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(this.scheduledItems, (ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("Scheduled"));
/* 202 */             executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
/* 203 */             executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
/* 204 */             this.executorService = executor;
/*     */           } else {
/*     */             
/* 207 */             LOGGER.debug("{}: No scheduled items", this.name);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/* 212 */     return this.executorService;
/*     */   }
/*     */   
/*     */   public class CronRunnable
/*     */     implements Runnable {
/*     */     private final CronExpression cronExpression;
/*     */     private final Runnable runnable;
/*     */     private CronScheduledFuture<?> scheduledFuture;
/*     */     
/*     */     public CronRunnable(Runnable runnable, CronExpression cronExpression) {
/* 222 */       this.cronExpression = cronExpression;
/* 223 */       this.runnable = runnable;
/*     */     }
/*     */     
/*     */     public void setScheduledFuture(CronScheduledFuture<?> future) {
/* 227 */       this.scheduledFuture = future;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 233 */         long millis = this.scheduledFuture.getFireTime().getTime() - System.currentTimeMillis();
/* 234 */         if (millis > 0L) {
/* 235 */           ConfigurationScheduler.LOGGER.debug("{} Cron thread woke up {} millis early. Sleeping", ConfigurationScheduler.this.name, Long.valueOf(millis));
/*     */           try {
/* 237 */             Thread.sleep(millis);
/* 238 */           } catch (InterruptedException interruptedException) {}
/*     */         } 
/*     */ 
/*     */         
/* 242 */         this.runnable.run();
/* 243 */       } catch (Throwable ex) {
/* 244 */         ConfigurationScheduler.LOGGER.error("{} caught error running command", ConfigurationScheduler.this.name, ex);
/*     */       } finally {
/* 246 */         Date fireDate = this.cronExpression.getNextValidTimeAfter(new Date());
/* 247 */         ScheduledFuture<?> future = ConfigurationScheduler.this.schedule(this, ConfigurationScheduler.this.nextFireInterval(fireDate), TimeUnit.MILLISECONDS);
/* 248 */         ConfigurationScheduler.LOGGER.debug("{} Cron expression {} scheduled to fire again at {}", ConfigurationScheduler.this.name, this.cronExpression.getCronExpression(), fireDate);
/*     */         
/* 250 */         this.scheduledFuture.reset(future, fireDate);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 256 */       return "CronRunnable{" + this.cronExpression.getCronExpression() + " - " + this.scheduledFuture.getFireTime();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 262 */     StringBuilder sb = new StringBuilder("ConfigurationScheduler [name=");
/* 263 */     sb.append(this.name);
/* 264 */     sb.append(", [");
/* 265 */     if (this.executorService != null) {
/* 266 */       Queue<Runnable> queue = ((ScheduledThreadPoolExecutor)this.executorService).getQueue();
/* 267 */       boolean first = true;
/* 268 */       for (Runnable runnable : queue) {
/* 269 */         if (!first) {
/* 270 */           sb.append(", ");
/*     */         }
/* 272 */         sb.append(runnable.toString());
/* 273 */         first = false;
/*     */       } 
/*     */     } 
/* 276 */     sb.append("]");
/* 277 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ConfigurationScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */