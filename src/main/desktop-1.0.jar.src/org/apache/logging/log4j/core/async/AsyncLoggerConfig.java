/*     */ package org.apache.logging.log4j.core.async;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.jmx.RingBufferAdmin;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "asyncLogger", category = "Core", printObject = true)
/*     */ public class AsyncLoggerConfig
/*     */   extends LoggerConfig
/*     */ {
/*  74 */   private static final ThreadLocal<Boolean> ASYNC_LOGGER_ENTERED = new ThreadLocal<Boolean>()
/*     */     {
/*     */       protected Boolean initialValue() {
/*  77 */         return Boolean.FALSE;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final AsyncLoggerConfigDelegate delegate;
/*     */ 
/*     */ 
/*     */   
/*     */   protected AsyncLoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
/*  88 */     super(name, appenders, filter, level, additive, properties, config, includeLocation);
/*     */     
/*  90 */     this.delegate = config.getAsyncLoggerConfigDelegate();
/*  91 */     this.delegate.setLogEventFactory(getLogEventFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(LogEvent event, LoggerConfig.LoggerConfigPredicate predicate) {
/*  97 */     if (predicate == LoggerConfig.LoggerConfigPredicate.ALL && ASYNC_LOGGER_ENTERED
/*  98 */       .get() == Boolean.FALSE && 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 103 */       hasAppenders()) {
/*     */       
/* 105 */       ASYNC_LOGGER_ENTERED.set(Boolean.TRUE);
/*     */ 
/*     */       
/*     */       try {
/* 109 */         super.log(event, LoggerConfig.LoggerConfigPredicate.SYNCHRONOUS_ONLY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 115 */         logToAsyncDelegate(event);
/*     */       } finally {
/* 117 */         ASYNC_LOGGER_ENTERED.set(Boolean.FALSE);
/*     */       } 
/*     */     } else {
/* 120 */       super.log(event, predicate);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void callAppenders(LogEvent event) {
/* 126 */     super.callAppenders(event);
/*     */   }
/*     */   
/*     */   private void logToAsyncDelegate(LogEvent event) {
/* 130 */     if (!isFiltered(event)) {
/*     */ 
/*     */       
/* 133 */       populateLazilyInitializedFields(event);
/* 134 */       if (!this.delegate.tryEnqueue(event, this)) {
/* 135 */         handleQueueFull(event);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleQueueFull(LogEvent event) {
/* 141 */     if (AbstractLogger.getRecursionDepth() > 1) {
/*     */       
/* 143 */       AsyncQueueFullMessageUtil.logWarningToStatusLogger();
/* 144 */       logToAsyncLoggerConfigsOnCurrentThread(event);
/*     */     } else {
/*     */       
/* 147 */       EventRoute eventRoute = this.delegate.getEventRoute(event.getLevel());
/* 148 */       eventRoute.logMessage(this, event);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void populateLazilyInitializedFields(LogEvent event) {
/* 153 */     event.getSource();
/* 154 */     event.getThreadName();
/*     */   }
/*     */   
/*     */   void logInBackgroundThread(LogEvent event) {
/* 158 */     this.delegate.enqueueEvent(event, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void logToAsyncLoggerConfigsOnCurrentThread(LogEvent event) {
/* 168 */     log(event, LoggerConfig.LoggerConfigPredicate.ASYNCHRONOUS_ONLY);
/*     */   }
/*     */   
/*     */   private String displayName() {
/* 172 */     return "".equals(getName()) ? "root" : getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 177 */     LOGGER.trace("AsyncLoggerConfig[{}] starting...", displayName());
/* 178 */     super.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 183 */     setStopping();
/* 184 */     stop(timeout, timeUnit, false);
/* 185 */     LOGGER.trace("AsyncLoggerConfig[{}] stopping...", displayName());
/* 186 */     setStopped();
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RingBufferAdmin createRingBufferAdmin(String contextName) {
/* 198 */     return this.delegate.createRingBufferAdmin(contextName, getName());
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
/*     */   @Deprecated
/*     */   public static LoggerConfig createLogger(String additivity, String levelName, String loggerName, String includeLocation, AppenderRef[] refs, Property[] properties, Configuration config, Filter filter) {
/*     */     Level level;
/* 225 */     if (loggerName == null) {
/* 226 */       LOGGER.error("Loggers cannot be configured without a name");
/* 227 */       return null;
/*     */     } 
/*     */     
/* 230 */     List<AppenderRef> appenderRefs = Arrays.asList(refs);
/*     */     
/*     */     try {
/* 233 */       level = Level.toLevel(levelName, Level.ERROR);
/* 234 */     } catch (Exception ex) {
/* 235 */       LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
/*     */ 
/*     */       
/* 238 */       level = Level.ERROR;
/*     */     } 
/* 240 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 241 */     boolean additive = Booleans.parseBoolean(additivity, true);
/*     */     
/* 243 */     return new AsyncLoggerConfig(name, appenderRefs, filter, level, additive, properties, config, 
/* 244 */         includeLocation(includeLocation));
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
/*     */   @PluginFactory
/*     */   public static LoggerConfig createLogger(@PluginAttribute(value = "additivity", defaultBoolean = true) boolean additivity, @PluginAttribute("level") Level level, @Required(message = "Loggers cannot be configured without a name") @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 271 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 272 */     return new AsyncLoggerConfig(name, Arrays.asList(refs), filter, level, additivity, properties, config, 
/* 273 */         includeLocation(includeLocation));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean includeLocation(String includeLocationConfigValue) {
/* 278 */     return Boolean.parseBoolean(includeLocationConfigValue);
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
/*     */   @Plugin(name = "asyncRoot", category = "Core", printObject = true)
/*     */   public static class RootLogger
/*     */     extends LoggerConfig
/*     */   {
/*     */     @Deprecated
/*     */     public static LoggerConfig createLogger(String additivity, String levelName, String includeLocation, AppenderRef[] refs, Property[] properties, Configuration config, Filter filter) {
/* 299 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 300 */       Level level = null;
/*     */       try {
/* 302 */         level = Level.toLevel(levelName, Level.ERROR);
/* 303 */       } catch (Exception ex) {
/* 304 */         LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
/* 305 */         level = Level.ERROR;
/*     */       } 
/* 307 */       boolean additive = Booleans.parseBoolean(additivity, true);
/* 308 */       return new AsyncLoggerConfig("", appenderRefs, filter, level, additive, properties, config, 
/*     */           
/* 310 */           AsyncLoggerConfig.includeLocation(includeLocation));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginFactory
/*     */     public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 325 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 326 */       Level actualLevel = (level == null) ? Level.ERROR : level;
/* 327 */       boolean additive = Booleans.parseBoolean(additivity, true);
/* 328 */       return new AsyncLoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, 
/* 329 */           AsyncLoggerConfig.includeLocation(includeLocation));
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\AsyncLoggerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */