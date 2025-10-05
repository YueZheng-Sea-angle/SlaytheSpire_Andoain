/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TransferQueue;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.async.ArrayBlockingQueueFactory;
/*     */ import org.apache.logging.log4j.core.async.AsyncQueueFullMessageUtil;
/*     */ import org.apache.logging.log4j.core.async.AsyncQueueFullPolicy;
/*     */ import org.apache.logging.log4j.core.async.AsyncQueueFullPolicyFactory;
/*     */ import org.apache.logging.log4j.core.async.BlockingQueueFactory;
/*     */ import org.apache.logging.log4j.core.async.DiscardingAsyncQueueFullPolicy;
/*     */ import org.apache.logging.log4j.core.async.EventRoute;
/*     */ import org.apache.logging.log4j.core.async.InternalAsyncUtil;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Async", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class AsyncAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_QUEUE_SIZE = 1024;
/*     */   private final BlockingQueue<LogEvent> queue;
/*     */   private final int queueSize;
/*     */   private final boolean blocking;
/*     */   private final long shutdownTimeout;
/*     */   private final Configuration config;
/*     */   private final AppenderRef[] appenderRefs;
/*     */   private final String errorRef;
/*     */   private final boolean includeLocation;
/*     */   private AppenderControl errorAppender;
/*     */   private AsyncAppenderEventDispatcher dispatcher;
/*     */   private AsyncQueueFullPolicy asyncQueueFullPolicy;
/*     */   
/*     */   private AsyncAppender(String name, Filter filter, AppenderRef[] appenderRefs, String errorRef, int queueSize, boolean blocking, boolean ignoreExceptions, long shutdownTimeout, Configuration config, boolean includeLocation, BlockingQueueFactory<LogEvent> blockingQueueFactory, Property[] properties) {
/*  80 */     super(name, filter, (Layout<? extends Serializable>)null, ignoreExceptions, properties);
/*  81 */     this.queue = blockingQueueFactory.create(queueSize);
/*  82 */     this.queueSize = queueSize;
/*  83 */     this.blocking = blocking;
/*  84 */     this.shutdownTimeout = shutdownTimeout;
/*  85 */     this.config = config;
/*  86 */     this.appenderRefs = appenderRefs;
/*  87 */     this.errorRef = errorRef;
/*  88 */     this.includeLocation = includeLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  93 */     Map<String, Appender> map = this.config.getAppenders();
/*  94 */     List<AppenderControl> appenders = new ArrayList<>();
/*  95 */     for (AppenderRef appenderRef : this.appenderRefs) {
/*  96 */       Appender appender = map.get(appenderRef.getRef());
/*  97 */       if (appender != null) {
/*  98 */         appenders.add(new AppenderControl(appender, appenderRef.getLevel(), appenderRef.getFilter()));
/*     */       } else {
/* 100 */         LOGGER.error("No appender named {} was configured", appenderRef);
/*     */       } 
/*     */     } 
/* 103 */     if (this.errorRef != null) {
/* 104 */       Appender appender = map.get(this.errorRef);
/* 105 */       if (appender != null) {
/* 106 */         this.errorAppender = new AppenderControl(appender, null, null);
/*     */       } else {
/* 108 */         LOGGER.error("Unable to set up error Appender. No appender named {} was configured", this.errorRef);
/*     */       } 
/*     */     } 
/* 111 */     if (appenders.size() > 0) {
/* 112 */       this
/* 113 */         .dispatcher = new AsyncAppenderEventDispatcher(getName(), this.errorAppender, appenders, this.queue);
/* 114 */     } else if (this.errorRef == null) {
/* 115 */       throw new ConfigurationException("No appenders are available for AsyncAppender " + getName());
/*     */     } 
/* 117 */     this.asyncQueueFullPolicy = AsyncQueueFullPolicyFactory.create();
/*     */     
/* 119 */     this.dispatcher.start();
/* 120 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 125 */     setStopping();
/* 126 */     stop(timeout, timeUnit, false);
/* 127 */     LOGGER.trace("AsyncAppender stopping. Queue still has {} events.", Integer.valueOf(this.queue.size()));
/*     */     try {
/* 129 */       this.dispatcher.stop(this.shutdownTimeout);
/* 130 */     } catch (InterruptedException ignored) {
/*     */       
/* 132 */       Thread.currentThread().interrupt();
/* 133 */       LOGGER.warn("Interrupted while stopping AsyncAppender {}", getName());
/*     */     } 
/* 135 */     LOGGER.trace("AsyncAppender stopped. Queue has {} events.", Integer.valueOf(this.queue.size()));
/*     */     
/* 137 */     if (DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy) > 0L) {
/* 138 */       LOGGER.trace("AsyncAppender: {} discarded {} events.", this.asyncQueueFullPolicy, 
/* 139 */           Long.valueOf(DiscardingAsyncQueueFullPolicy.getDiscardCount(this.asyncQueueFullPolicy)));
/*     */     }
/* 141 */     setStopped();
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent logEvent) {
/* 152 */     if (!isStarted()) {
/* 153 */       throw new IllegalStateException("AsyncAppender " + getName() + " is not active");
/*     */     }
/* 155 */     Log4jLogEvent memento = Log4jLogEvent.createMemento(logEvent, this.includeLocation);
/* 156 */     InternalAsyncUtil.makeMessageImmutable(logEvent.getMessage());
/* 157 */     if (!transfer((LogEvent)memento)) {
/* 158 */       if (this.blocking) {
/* 159 */         if (AbstractLogger.getRecursionDepth() > 1) {
/*     */           
/* 161 */           AsyncQueueFullMessageUtil.logWarningToStatusLogger();
/* 162 */           logMessageInCurrentThread(logEvent);
/*     */         } else {
/*     */           
/* 165 */           EventRoute route = this.asyncQueueFullPolicy.getRoute(this.dispatcher.getId(), memento.getLevel());
/* 166 */           route.logMessage(this, (LogEvent)memento);
/*     */         } 
/*     */       } else {
/* 169 */         error("Appender " + getName() + " is unable to write primary appenders. queue is full");
/* 170 */         logToErrorAppenderIfNecessary(false, (LogEvent)memento);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean transfer(LogEvent memento) {
/* 176 */     return (this.queue instanceof TransferQueue) ? ((TransferQueue<LogEvent>)this.queue)
/* 177 */       .tryTransfer(memento) : this.queue
/* 178 */       .offer(memento);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessageInCurrentThread(LogEvent logEvent) {
/* 187 */     logEvent.setEndOfBatch(this.queue.isEmpty());
/* 188 */     this.dispatcher.dispatch(logEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessageInBackgroundThread(LogEvent logEvent) {
/*     */     try {
/* 199 */       this.queue.put(logEvent);
/* 200 */     } catch (InterruptedException ignored) {
/* 201 */       boolean appendSuccessful = handleInterruptedException(logEvent);
/* 202 */       logToErrorAppenderIfNecessary(appendSuccessful, logEvent);
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
/*     */   private boolean handleInterruptedException(LogEvent memento) {
/* 218 */     boolean appendSuccessful = this.queue.offer(memento);
/* 219 */     if (!appendSuccessful) {
/* 220 */       LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", 
/* 221 */           getName());
/*     */     }
/*     */     
/* 224 */     Thread.currentThread().interrupt();
/* 225 */     return appendSuccessful;
/*     */   }
/*     */   
/*     */   private void logToErrorAppenderIfNecessary(boolean appendSuccessful, LogEvent logEvent) {
/* 229 */     if (!appendSuccessful && this.errorAppender != null) {
/* 230 */       this.errorAppender.callAppender(logEvent);
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
/*     */   public static AsyncAppender createAppender(AppenderRef[] appenderRefs, String errorRef, boolean blocking, long shutdownTimeout, int size, String name, boolean includeLocation, Filter filter, Configuration config, boolean ignoreExceptions) {
/* 259 */     if (name == null) {
/* 260 */       LOGGER.error("No name provided for AsyncAppender");
/* 261 */       return null;
/*     */     } 
/* 263 */     if (appenderRefs == null) {
/* 264 */       LOGGER.error("No appender references provided to AsyncAppender {}", name);
/*     */     }
/*     */     
/* 267 */     return new AsyncAppender(name, filter, appenderRefs, errorRef, size, blocking, ignoreExceptions, shutdownTimeout, config, includeLocation, (BlockingQueueFactory<LogEvent>)new ArrayBlockingQueueFactory(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 273 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractFilterable.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<AsyncAppender>
/*     */   {
/*     */     @PluginElement("AppenderRef")
/*     */     @Required(message = "No appender references provided to AsyncAppender")
/*     */     private AppenderRef[] appenderRefs;
/*     */     @PluginBuilderAttribute
/*     */     @PluginAliases({"error-ref"})
/*     */     private String errorRef;
/*     */     @PluginBuilderAttribute
/*     */     private boolean blocking = true;
/*     */     @PluginBuilderAttribute
/* 290 */     private long shutdownTimeout = 0L;
/*     */     
/*     */     @PluginBuilderAttribute
/* 293 */     private int bufferSize = 1024;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No name provided for AsyncAppender")
/*     */     private String name;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeLocation = false;
/*     */     
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean ignoreExceptions = true;
/*     */     
/*     */     @PluginElement("BlockingQueueFactory")
/* 309 */     private BlockingQueueFactory<LogEvent> blockingQueueFactory = (BlockingQueueFactory<LogEvent>)new ArrayBlockingQueueFactory();
/*     */ 
/*     */     
/*     */     public Builder setAppenderRefs(AppenderRef[] appenderRefs) {
/* 313 */       this.appenderRefs = appenderRefs;
/* 314 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setErrorRef(String errorRef) {
/* 318 */       this.errorRef = errorRef;
/* 319 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBlocking(boolean blocking) {
/* 323 */       this.blocking = blocking;
/* 324 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setShutdownTimeout(long shutdownTimeout) {
/* 328 */       this.shutdownTimeout = shutdownTimeout;
/* 329 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBufferSize(int bufferSize) {
/* 333 */       this.bufferSize = bufferSize;
/* 334 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setName(String name) {
/* 338 */       this.name = name;
/* 339 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIncludeLocation(boolean includeLocation) {
/* 343 */       this.includeLocation = includeLocation;
/* 344 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 348 */       this.configuration = configuration;
/* 349 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIgnoreExceptions(boolean ignoreExceptions) {
/* 353 */       this.ignoreExceptions = ignoreExceptions;
/* 354 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBlockingQueueFactory(BlockingQueueFactory<LogEvent> blockingQueueFactory) {
/* 358 */       this.blockingQueueFactory = blockingQueueFactory;
/* 359 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public AsyncAppender build() {
/* 364 */       return new AsyncAppender(this.name, getFilter(), this.appenderRefs, this.errorRef, this.bufferSize, this.blocking, this.ignoreExceptions, this.shutdownTimeout, this.configuration, this.includeLocation, this.blockingQueueFactory, 
/* 365 */           getPropertyArray());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAppenderRefStrings() {
/* 375 */     String[] result = new String[this.appenderRefs.length];
/* 376 */     for (int i = 0; i < result.length; i++) {
/* 377 */       result[i] = this.appenderRefs[i].getRef();
/*     */     }
/* 379 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 389 */     return this.includeLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlocking() {
/* 399 */     return this.blocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorRef() {
/* 408 */     return this.errorRef;
/*     */   }
/*     */   
/*     */   public int getQueueCapacity() {
/* 412 */     return this.queueSize;
/*     */   }
/*     */   
/*     */   public int getQueueRemainingCapacity() {
/* 416 */     return this.queue.remainingCapacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getQueueSize() {
/* 426 */     return this.queue.size();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AsyncAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */