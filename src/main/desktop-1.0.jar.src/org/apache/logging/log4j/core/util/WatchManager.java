/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationScheduler;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WatchManager
/*     */   extends AbstractLifeCycle
/*     */ {
/*     */   private final class ConfigurationMonitor
/*     */   {
/*     */     private final Watcher watcher;
/*     */     private volatile long lastModifiedMillis;
/*     */     
/*     */     public ConfigurationMonitor(long lastModifiedMillis, Watcher watcher) {
/*  54 */       this.watcher = watcher;
/*  55 */       this.lastModifiedMillis = lastModifiedMillis;
/*     */     }
/*     */     
/*     */     public Watcher getWatcher() {
/*  59 */       return this.watcher;
/*     */     }
/*     */     
/*     */     private void setLastModifiedMillis(long lastModifiedMillis) {
/*  63 */       this.lastModifiedMillis = lastModifiedMillis;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  68 */       return "ConfigurationMonitor [watcher=" + this.watcher + ", lastModifiedMillis=" + this.lastModifiedMillis + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalUUID {
/*     */     private static final long LOW_MASK = 4294967295L;
/*     */     private static final long MID_MASK = 281470681743360L;
/*     */     private static final long HIGH_MASK = 1152640029630136320L;
/*     */     private static final int NODE_SIZE = 8;
/*     */     private static final int SHIFT_2 = 16;
/*     */     private static final int SHIFT_4 = 32;
/*     */     private static final int SHIFT_6 = 48;
/*     */     private static final int HUNDRED_NANOS_PER_MILLI = 10000;
/*     */     private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
/*  82 */     private static final AtomicInteger COUNT = new AtomicInteger(0);
/*     */     
/*     */     private static final long TYPE1 = 4096L;
/*     */     
/*     */     private static final byte VARIANT = -128;
/*     */     private static final int SEQUENCE_MASK = 16383;
/*     */     
/*     */     public static UUID get() {
/*  90 */       long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (COUNT.incrementAndGet() % 10000);
/*  91 */       long timeLow = (time & 0xFFFFFFFFL) << 32L;
/*  92 */       long timeMid = (time & 0xFFFF00000000L) >> 16L;
/*  93 */       long timeHi = (time & 0xFFF000000000000L) >> 48L;
/*  94 */       long most = timeLow | timeMid | 0x1000L | timeHi;
/*  95 */       return new UUID(most, COUNT.incrementAndGet());
/*     */     }
/*     */   }
/*     */   
/*     */   private final class WatchRunnable
/*     */     implements Runnable {
/* 101 */     private final String SIMPLE_NAME = WatchRunnable.class.getSimpleName();
/*     */     private WatchRunnable() {}
/*     */     
/*     */     public void run() {
/* 105 */       WatchManager.logger.trace("{} run triggered.", this.SIMPLE_NAME);
/* 106 */       for (Map.Entry<Source, WatchManager.ConfigurationMonitor> entry : (Iterable<Map.Entry<Source, WatchManager.ConfigurationMonitor>>)WatchManager.this.watchers.entrySet()) {
/* 107 */         Source source = entry.getKey();
/* 108 */         WatchManager.ConfigurationMonitor monitor = entry.getValue();
/* 109 */         if (monitor.getWatcher().isModified()) {
/* 110 */           long lastModified = monitor.getWatcher().getLastModified();
/* 111 */           if (WatchManager.logger.isInfoEnabled()) {
/* 112 */             WatchManager.logger.info("Source '{}' was modified on {} ({}), previous modification was on {} ({})", source, WatchManager.this
/* 113 */                 .millisToString(lastModified), Long.valueOf(lastModified), WatchManager.this.millisToString(monitor.lastModifiedMillis), 
/* 114 */                 Long.valueOf(monitor.lastModifiedMillis));
/*     */           }
/* 116 */           monitor.lastModifiedMillis = lastModified;
/* 117 */           monitor.getWatcher().modified();
/*     */         } 
/*     */       } 
/* 120 */       WatchManager.logger.trace("{} run ended.", this.SIMPLE_NAME);
/*     */     }
/*     */   }
/* 123 */   private static Logger logger = (Logger)StatusLogger.getLogger();
/* 124 */   private final ConcurrentMap<Source, ConfigurationMonitor> watchers = new ConcurrentHashMap<>();
/* 125 */   private int intervalSeconds = 0;
/*     */   
/*     */   private ScheduledFuture<?> future;
/*     */   
/*     */   private final ConfigurationScheduler scheduler;
/*     */   
/*     */   private final List<WatchEventService> eventServiceList;
/*     */   
/* 133 */   private final UUID id = LocalUUID.get();
/*     */   
/*     */   public WatchManager(ConfigurationScheduler scheduler) {
/* 136 */     this.scheduler = Objects.<ConfigurationScheduler>requireNonNull(scheduler, "scheduler");
/* 137 */     this.eventServiceList = getEventServices();
/*     */   }
/*     */   
/*     */   public void checkFiles() {
/* 141 */     (new WatchRunnable()).run();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Source, Watcher> getConfigurationWatchers() {
/* 151 */     Map<Source, Watcher> map = new HashMap<>(this.watchers.size());
/* 152 */     for (Map.Entry<Source, ConfigurationMonitor> entry : this.watchers.entrySet()) {
/* 153 */       map.put(entry.getKey(), ((ConfigurationMonitor)entry.getValue()).getWatcher());
/*     */     }
/* 155 */     return map;
/*     */   }
/*     */   
/*     */   private List<WatchEventService> getEventServices() {
/* 159 */     List<WatchEventService> list = new ArrayList<>();
/* 160 */     for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
/*     */       
/*     */       try {
/* 163 */         ServiceLoader<WatchEventService> serviceLoader = ServiceLoader.load(WatchEventService.class, classLoader);
/* 164 */         for (WatchEventService service : serviceLoader) {
/* 165 */           list.add(service);
/*     */         }
/* 167 */       } catch (Throwable ex) {
/* 168 */         LOGGER.debug("Unable to retrieve WatchEventService from ClassLoader {}", classLoader, ex);
/*     */       } 
/*     */     } 
/* 171 */     return list;
/*     */   }
/*     */   
/*     */   public UUID getId() {
/* 175 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIntervalSeconds() {
/* 184 */     return this.intervalSeconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Map<File, FileWatcher> getWatchers() {
/* 195 */     Map<File, FileWatcher> map = new HashMap<>(this.watchers.size());
/* 196 */     for (Map.Entry<Source, ConfigurationMonitor> entry : this.watchers.entrySet()) {
/* 197 */       if (((ConfigurationMonitor)entry.getValue()).getWatcher() instanceof org.apache.logging.log4j.core.config.ConfigurationFileWatcher) {
/* 198 */         map.put(((Source)entry.getKey()).getFile(), (FileWatcher)((ConfigurationMonitor)entry.getValue()).getWatcher()); continue;
/*     */       } 
/* 200 */       map.put(((Source)entry.getKey()).getFile(), new WrappedFileWatcher((FileWatcher)((ConfigurationMonitor)entry.getValue()).getWatcher()));
/*     */     } 
/*     */     
/* 203 */     return map;
/*     */   }
/*     */   
/*     */   public boolean hasEventListeners() {
/* 207 */     return (this.eventServiceList.size() > 0);
/*     */   }
/*     */   
/*     */   private String millisToString(long millis) {
/* 211 */     return (new Date(millis)).toString();
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
/*     */   public void reset() {
/* 225 */     logger.debug("Resetting {}", this);
/* 226 */     for (Source source : this.watchers.keySet()) {
/* 227 */       reset(source);
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
/*     */   public void reset(File file) {
/* 243 */     if (file == null) {
/*     */       return;
/*     */     }
/* 246 */     Source source = new Source(file);
/* 247 */     reset(source);
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
/*     */   public void reset(Source source) {
/* 262 */     if (source == null) {
/*     */       return;
/*     */     }
/* 265 */     ConfigurationMonitor monitor = this.watchers.get(source);
/* 266 */     if (monitor != null) {
/* 267 */       Watcher watcher = monitor.getWatcher();
/* 268 */       if (watcher.isModified()) {
/* 269 */         long lastModifiedMillis = watcher.getLastModified();
/* 270 */         if (logger.isDebugEnabled()) {
/* 271 */           logger.debug("Resetting file monitor for '{}' from {} ({}) to {} ({})", source.getLocation(), 
/* 272 */               millisToString(monitor.lastModifiedMillis), Long.valueOf(monitor.lastModifiedMillis), 
/* 273 */               millisToString(lastModifiedMillis), Long.valueOf(lastModifiedMillis));
/*     */         }
/* 275 */         monitor.setLastModifiedMillis(lastModifiedMillis);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setIntervalSeconds(int intervalSeconds) {
/* 281 */     if (!isStarted()) {
/* 282 */       if (this.intervalSeconds > 0 && intervalSeconds == 0) {
/* 283 */         this.scheduler.decrementScheduledItems();
/* 284 */       } else if (this.intervalSeconds == 0 && intervalSeconds > 0) {
/* 285 */         this.scheduler.incrementScheduledItems();
/*     */       } 
/* 287 */       this.intervalSeconds = intervalSeconds;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 293 */     super.start();
/*     */     
/* 295 */     if (this.intervalSeconds > 0) {
/* 296 */       this
/* 297 */         .future = this.scheduler.scheduleWithFixedDelay(new WatchRunnable(), this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
/*     */     }
/* 299 */     for (WatchEventService service : this.eventServiceList) {
/* 300 */       service.subscribe(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 306 */     setStopping();
/* 307 */     for (WatchEventService service : this.eventServiceList) {
/* 308 */       service.unsubscribe(this);
/*     */     }
/* 310 */     boolean stopped = stop(this.future);
/* 311 */     setStopped();
/* 312 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 317 */     return "WatchManager [intervalSeconds=" + this.intervalSeconds + ", watchers=" + this.watchers + ", scheduler=" + this.scheduler + ", future=" + this.future + "]";
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
/*     */   public void unwatch(Source source) {
/* 329 */     logger.debug("Unwatching configuration {}", source);
/* 330 */     this.watchers.remove(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unwatchFile(File file) {
/* 340 */     Source source = new Source(file);
/* 341 */     unwatch(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void watch(Source source, Watcher watcher) {
/* 351 */     watcher.watching(source);
/* 352 */     long lastModified = watcher.getLastModified();
/* 353 */     if (logger.isDebugEnabled()) {
/* 354 */       logger.debug("Watching configuration '{}' for lastModified {} ({})", source, millisToString(lastModified), 
/* 355 */           Long.valueOf(lastModified));
/*     */     }
/* 357 */     this.watchers.put(source, new ConfigurationMonitor(lastModified, watcher));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void watchFile(File file, FileWatcher fileWatcher) {
/*     */     Watcher watcher;
/* 368 */     if (fileWatcher instanceof Watcher) {
/* 369 */       watcher = (Watcher)fileWatcher;
/*     */     } else {
/* 371 */       watcher = new WrappedFileWatcher(fileWatcher);
/*     */     } 
/* 373 */     Source source = new Source(file);
/* 374 */     watch(source, watcher);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\WatchManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */