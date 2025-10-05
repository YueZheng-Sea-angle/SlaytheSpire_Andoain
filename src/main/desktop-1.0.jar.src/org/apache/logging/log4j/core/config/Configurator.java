/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Configurator
/*     */ {
/*  41 */   private static final String FQCN = Configurator.class.getName();
/*     */   
/*  43 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static Log4jContextFactory getFactory() {
/*  46 */     LoggerContextFactory factory = LogManager.getFactory();
/*  47 */     if (factory instanceof Log4jContextFactory) {
/*  48 */       return (Log4jContextFactory)factory;
/*     */     }
/*  50 */     if (factory != null) {
/*  51 */       LOGGER.error("LogManager returned an instance of {} which does not implement {}. Unable to initialize Log4j.", factory
/*  52 */           .getClass().getName(), Log4jContextFactory.class.getName());
/*     */     } else {
/*  54 */       LOGGER.fatal("LogManager did not return a LoggerContextFactory. This indicates something has gone terribly wrong!");
/*     */     } 
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(ClassLoader loader, ConfigurationSource source) {
/*  67 */     return initialize(loader, source, (Object)null);
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
/*     */   public static LoggerContext initialize(ClassLoader loader, ConfigurationSource source, Object externalContext) {
/*     */     try {
/*  84 */       Log4jContextFactory factory = getFactory();
/*  85 */       return (factory == null) ? null : factory
/*  86 */         .getContext(FQCN, loader, externalContext, false, source);
/*  87 */     } catch (Exception ex) {
/*  88 */       LOGGER.error("There was a problem obtaining a LoggerContext using the configuration source [{}]", source, ex);
/*     */       
/*  90 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, String configLocation) {
/* 101 */     return initialize(name, loader, configLocation, (Object)null);
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, String configLocation, Object externalContext) {
/* 115 */     if (Strings.isBlank(configLocation)) {
/* 116 */       return initialize(name, loader, (URI)null, externalContext);
/*     */     }
/* 118 */     if (configLocation.contains(",")) {
/* 119 */       String[] parts = configLocation.split(",");
/* 120 */       String scheme = null;
/* 121 */       List<URI> uris = new ArrayList<>(parts.length);
/* 122 */       for (String part : parts) {
/* 123 */         URI uri = NetUtils.toURI((scheme != null) ? (scheme + ":" + part.trim()) : part.trim());
/* 124 */         if (scheme == null && uri.getScheme() != null) {
/* 125 */           scheme = uri.getScheme();
/*     */         }
/* 127 */         uris.add(uri);
/*     */       } 
/* 129 */       return initialize(name, loader, uris, externalContext);
/*     */     } 
/* 131 */     return initialize(name, loader, NetUtils.toURI(configLocation), externalContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation) {
/* 142 */     return initialize(name, loader, configLocation, (Map.Entry<String, Object>)null);
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation, Object externalContext) {
/*     */     try {
/* 157 */       Log4jContextFactory factory = getFactory();
/* 158 */       return (factory == null) ? null : factory
/* 159 */         .getContext(FQCN, loader, externalContext, false, configLocation, name);
/* 160 */     } catch (Exception ex) {
/* 161 */       LOGGER.error("There was a problem initializing the LoggerContext [{}] using configuration at [{}].", name, configLocation, ex);
/*     */ 
/*     */       
/* 164 */       return null;
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
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, URI configLocation, Map.Entry<String, Object> entry) {
/*     */     try {
/* 179 */       Log4jContextFactory factory = getFactory();
/* 180 */       return (factory == null) ? null : factory
/* 181 */         .getContext(FQCN, loader, entry, false, configLocation, name);
/* 182 */     } catch (Exception ex) {
/* 183 */       LOGGER.error("There was a problem initializing the LoggerContext [{}] using configuration at [{}].", name, configLocation, ex);
/*     */ 
/*     */       
/* 186 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static LoggerContext initialize(String name, ClassLoader loader, List<URI> configLocations, Object externalContext) {
/*     */     try {
/* 192 */       Log4jContextFactory factory = getFactory();
/* 193 */       return (factory == null) ? null : factory
/*     */         
/* 195 */         .getContext(FQCN, loader, externalContext, false, configLocations, name);
/* 196 */     } catch (Exception ex) {
/* 197 */       LOGGER.error("There was a problem initializing the LoggerContext [{}] using configurations at [{}].", name, configLocations, ex);
/*     */ 
/*     */       
/* 200 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(String name, String configLocation) {
/* 210 */     return initialize(name, (ClassLoader)null, configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(Configuration configuration) {
/* 219 */     return initialize((ClassLoader)null, configuration, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(ClassLoader loader, Configuration configuration) {
/* 229 */     return initialize(loader, configuration, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LoggerContext initialize(ClassLoader loader, Configuration configuration, Object externalContext) {
/*     */     try {
/* 241 */       Log4jContextFactory factory = getFactory();
/* 242 */       return (factory == null) ? null : factory
/* 243 */         .getContext(FQCN, loader, externalContext, false, configuration);
/* 244 */     } catch (Exception ex) {
/* 245 */       LOGGER.error("There was a problem initializing the LoggerContext using configuration {}", configuration
/* 246 */           .getName(), ex);
/*     */       
/* 248 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reconfigure(Configuration configuration) {
/*     */     try {
/* 258 */       Log4jContextFactory factory = getFactory();
/* 259 */       if (factory != null) {
/* 260 */         factory.getContext(FQCN, null, null, false)
/* 261 */           .reconfigure(configuration);
/*     */       }
/* 263 */     } catch (Exception ex) {
/* 264 */       LOGGER.error("There was a problem initializing the LoggerContext using configuration {}", configuration
/* 265 */           .getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reconfigure() {
/*     */     try {
/* 275 */       Log4jContextFactory factory = getFactory();
/* 276 */       if (factory != null) {
/* 277 */         factory.getSelector().getContext(FQCN, null, false).reconfigure();
/*     */       } else {
/* 279 */         LOGGER.warn("Unable to reconfigure - Log4j has not been initialized.");
/*     */       } 
/* 281 */     } catch (Exception ex) {
/* 282 */       LOGGER.error("Error encountered trying to reconfigure logging", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reconfigure(URI uri) {
/*     */     try {
/* 293 */       Log4jContextFactory factory = getFactory();
/* 294 */       if (factory != null) {
/* 295 */         factory.getSelector().getContext(FQCN, null, false).setConfigLocation(uri);
/*     */       } else {
/* 297 */         LOGGER.warn("Unable to reconfigure - Log4j has not been initialized.");
/*     */       } 
/* 299 */     } catch (Exception ex) {
/* 300 */       LOGGER.error("Error encountered trying to reconfigure logging", ex);
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
/*     */   public static void setAllLevels(String parentLogger, Level level) {
/* 315 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 316 */     Configuration config = loggerContext.getConfiguration();
/* 317 */     boolean set = setLevel(parentLogger, level, config);
/* 318 */     for (Map.Entry<String, LoggerConfig> entry : config.getLoggers().entrySet()) {
/* 319 */       if (((String)entry.getKey()).startsWith(parentLogger)) {
/* 320 */         set |= setLevel(entry.getValue(), level);
/*     */       }
/*     */     } 
/* 323 */     if (set) {
/* 324 */       loggerContext.updateLoggers();
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean setLevel(LoggerConfig loggerConfig, Level level) {
/* 329 */     boolean set = !loggerConfig.getLevel().equals(level);
/* 330 */     if (set) {
/* 331 */       loggerConfig.setLevel(level);
/*     */     }
/* 333 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setLevel(Map<String, Level> levelMap) {
/* 344 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 345 */     Configuration config = loggerContext.getConfiguration();
/* 346 */     boolean set = false;
/* 347 */     for (Map.Entry<String, Level> entry : levelMap.entrySet()) {
/* 348 */       String loggerName = entry.getKey();
/* 349 */       Level level = entry.getValue();
/* 350 */       set |= setLevel(loggerName, level, config);
/*     */     } 
/* 352 */     if (set) {
/* 353 */       loggerContext.updateLoggers();
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
/*     */   public static void setLevel(String loggerName, Level level) {
/* 366 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 367 */     if (Strings.isEmpty(loggerName)) {
/* 368 */       setRootLevel(level);
/* 369 */     } else if (setLevel(loggerName, level, loggerContext.getConfiguration())) {
/* 370 */       loggerContext.updateLoggers();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean setLevel(String loggerName, Level level, Configuration config) {
/*     */     boolean set;
/* 376 */     LoggerConfig loggerConfig = config.getLoggerConfig(loggerName);
/* 377 */     if (!loggerName.equals(loggerConfig.getName())) {
/*     */       
/* 379 */       loggerConfig = new LoggerConfig(loggerName, level, true);
/* 380 */       config.addLogger(loggerName, loggerConfig);
/* 381 */       loggerConfig.setLevel(level);
/* 382 */       set = true;
/*     */     } else {
/* 384 */       set = setLevel(loggerConfig, level);
/*     */     } 
/* 386 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRootLevel(Level level) {
/* 396 */     LoggerContext loggerContext = LoggerContext.getContext(false);
/* 397 */     LoggerConfig loggerConfig = loggerContext.getConfiguration().getRootLogger();
/* 398 */     if (!loggerConfig.getLevel().equals(level)) {
/* 399 */       loggerConfig.setLevel(level);
/* 400 */       loggerContext.updateLoggers();
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
/*     */   public static void shutdown(LoggerContext ctx) {
/* 416 */     if (ctx != null) {
/* 417 */       ctx.stop();
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
/*     */   public static boolean shutdown(LoggerContext ctx, long timeout, TimeUnit timeUnit) {
/* 442 */     if (ctx != null) {
/* 443 */       return ctx.stop(timeout, timeUnit);
/*     */     }
/* 445 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\Configurator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */