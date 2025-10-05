/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationListener;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.NullConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
/*     */ import org.apache.logging.log4j.core.jmx.Server;
/*     */ import org.apache.logging.log4j.core.util.Cancellable;
/*     */ import org.apache.logging.log4j.core.util.ExecutorServices;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.spi.ExtendedLogger;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerContextShutdownAware;
/*     */ import org.apache.logging.log4j.spi.LoggerContextShutdownEnabled;
/*     */ import org.apache.logging.log4j.spi.LoggerRegistry;
/*     */ import org.apache.logging.log4j.spi.Terminable;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMapFactory;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoggerContext
/*     */   extends AbstractLifeCycle
/*     */   implements LoggerContext, AutoCloseable, Terminable, ConfigurationListener, LoggerContextShutdownEnabled
/*     */ {
/*     */   public static final String PROPERTY_CONFIG = "config";
/*     */   
/*     */   static {
/*     */     try {
/*  74 */       Loader.loadClass(ExecutorServices.class.getName());
/*  75 */     } catch (Exception e) {
/*  76 */       LOGGER.error("Failed to preload ExecutorServices class.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static final Configuration NULL_CONFIGURATION = (Configuration)new NullConfiguration();
/*     */   
/*  87 */   private final LoggerRegistry<Logger> loggerRegistry = new LoggerRegistry();
/*  88 */   private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile List<LoggerContextShutdownAware> listeners;
/*     */ 
/*     */   
/*  95 */   private volatile Configuration configuration = (Configuration)new DefaultConfiguration();
/*     */   private static final String EXTERNAL_CONTEXT_KEY = "__EXTERNAL_CONTEXT_KEY__";
/*  97 */   private ConcurrentMap<String, Object> externalMap = new ConcurrentHashMap<>();
/*     */   
/*     */   private String contextName;
/*     */   private volatile URI configLocation;
/*     */   private Cancellable shutdownCallback;
/* 102 */   private final Lock configLock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name) {
/* 110 */     this(name, (Object)null, (URI)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext) {
/* 120 */     this(name, externalContext, (URI)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext(String name, Object externalContext, URI configLocn) {
/* 131 */     this.contextName = name;
/* 132 */     if (externalContext == null) {
/* 133 */       this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
/*     */     } else {
/* 135 */       this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", externalContext);
/*     */     } 
/* 137 */     this.configLocation = configLocn;
/* 138 */     Thread runner = new Thread(new ThreadContextDataTask(), "Thread Context Data Task");
/* 139 */     runner.setDaemon(true);
/* 140 */     runner.start();
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
/*     */   public LoggerContext(String name, Object externalContext, String configLocn) {
/* 152 */     this.contextName = name;
/* 153 */     if (externalContext == null) {
/* 154 */       this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
/*     */     } else {
/* 156 */       this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", externalContext);
/*     */     } 
/* 158 */     if (configLocn != null) {
/*     */       URI uri;
/*     */       try {
/* 161 */         uri = (new File(configLocn)).toURI();
/* 162 */       } catch (Exception ex) {
/* 163 */         uri = null;
/*     */       } 
/* 165 */       this.configLocation = uri;
/*     */     } else {
/* 167 */       this.configLocation = null;
/*     */     } 
/* 169 */     Thread runner = new Thread(new ThreadContextDataTask(), "Thread Context Data Task");
/* 170 */     runner.setDaemon(true);
/* 171 */     runner.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addShutdownListener(LoggerContextShutdownAware listener) {
/* 176 */     if (this.listeners == null) {
/* 177 */       synchronized (this) {
/* 178 */         if (this.listeners == null) {
/* 179 */           this.listeners = new CopyOnWriteArrayList<>();
/*     */         }
/*     */       } 
/*     */     }
/* 183 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LoggerContextShutdownAware> getListeners() {
/* 188 */     return this.listeners;
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
/*     */   public static LoggerContext getContext() {
/* 210 */     return (LoggerContext)LogManager.getContext();
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
/*     */   public static LoggerContext getContext(boolean currentContext) {
/* 231 */     return (LoggerContext)LogManager.getContext(currentContext);
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
/*     */   public static LoggerContext getContext(ClassLoader loader, boolean currentContext, URI configLocation) {
/* 256 */     return (LoggerContext)LogManager.getContext(loader, currentContext, configLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 261 */     LOGGER.debug("Starting LoggerContext[name={}, {}]...", getName(), this);
/* 262 */     if (PropertiesUtil.getProperties().getBooleanProperty("log4j.LoggerContext.stacktrace.on.start", false)) {
/* 263 */       LOGGER.debug("Stack trace to locate invoker", new Exception("Not a real error, showing stack trace to locate invoker"));
/*     */     }
/*     */     
/* 266 */     if (this.configLock.tryLock()) {
/*     */       try {
/* 268 */         if (isInitialized() || isStopped()) {
/* 269 */           setStarting();
/* 270 */           reconfigure();
/* 271 */           if (this.configuration.isShutdownHookEnabled()) {
/* 272 */             setUpShutdownHook();
/*     */           }
/* 274 */           setStarted();
/*     */         } 
/*     */       } finally {
/* 277 */         this.configLock.unlock();
/*     */       } 
/*     */     }
/* 280 */     LOGGER.debug("LoggerContext[name={}, {}] started OK.", getName(), this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Configuration config) {
/* 289 */     LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", getName(), this, config);
/* 290 */     if (this.configLock.tryLock()) {
/*     */       try {
/* 292 */         if (isInitialized() || isStopped()) {
/* 293 */           if (this.configuration.isShutdownHookEnabled()) {
/* 294 */             setUpShutdownHook();
/*     */           }
/* 296 */           setStarted();
/*     */         } 
/*     */       } finally {
/* 299 */         this.configLock.unlock();
/*     */       } 
/*     */     }
/* 302 */     setConfiguration(config);
/* 303 */     LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", getName(), this, config);
/*     */   }
/*     */   
/*     */   private void setUpShutdownHook() {
/* 307 */     if (this.shutdownCallback == null) {
/* 308 */       LoggerContextFactory factory = LogManager.getFactory();
/* 309 */       if (factory instanceof ShutdownCallbackRegistry) {
/* 310 */         LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Shutdown hook enabled. Registering a new one.");
/*     */         try {
/* 312 */           final long shutdownTimeoutMillis = this.configuration.getShutdownTimeoutMillis();
/* 313 */           this.shutdownCallback = ((ShutdownCallbackRegistry)factory).addShutdownCallback(new Runnable()
/*     */               {
/*     */                 public void run()
/*     */                 {
/* 317 */                   LoggerContext context = LoggerContext.this;
/* 318 */                   AbstractLifeCycle.LOGGER.debug(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Stopping LoggerContext[name={}, {}]", context
/* 319 */                       .getName(), context);
/* 320 */                   context.stop(shutdownTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 325 */                   return "Shutdown callback for LoggerContext[name=" + LoggerContext.this.getName() + ']';
/*     */                 }
/*     */               });
/* 328 */         } catch (IllegalStateException e) {
/* 329 */           throw new IllegalStateException("Unable to register Log4j shutdown hook because JVM is shutting down.", e);
/*     */         }
/* 331 */         catch (SecurityException e) {
/* 332 */           LOGGER.error(ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Unable to register shutdown hook due to security restrictions", e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 341 */     stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void terminate() {
/* 346 */     stop();
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
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 370 */     LOGGER.debug("Stopping LoggerContext[name={}, {}]...", getName(), this);
/* 371 */     this.configLock.lock();
/*     */     try {
/* 373 */       if (isStopped()) {
/* 374 */         return true;
/*     */       }
/*     */       
/* 377 */       setStopping();
/*     */       try {
/* 379 */         Server.unregisterLoggerContext(getName());
/* 380 */       } catch (LinkageError|Exception e) {
/*     */         
/* 382 */         LOGGER.error("Unable to unregister MBeans", e);
/*     */       } 
/* 384 */       if (this.shutdownCallback != null) {
/* 385 */         this.shutdownCallback.cancel();
/* 386 */         this.shutdownCallback = null;
/*     */       } 
/* 388 */       Configuration prev = this.configuration;
/* 389 */       this.configuration = NULL_CONFIGURATION;
/* 390 */       updateLoggers();
/* 391 */       if (prev instanceof LifeCycle2) {
/* 392 */         ((LifeCycle2)prev).stop(timeout, timeUnit);
/*     */       } else {
/* 394 */         prev.stop();
/*     */       } 
/* 396 */       this.externalMap.clear();
/* 397 */       LogManager.getFactory().removeContext(this);
/*     */     } finally {
/* 399 */       this.configLock.unlock();
/* 400 */       setStopped();
/*     */     } 
/* 402 */     if (this.listeners != null) {
/* 403 */       for (LoggerContextShutdownAware listener : this.listeners) {
/*     */         try {
/* 405 */           listener.contextShutdown(this);
/* 406 */         } catch (Exception exception) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 411 */     LOGGER.debug("Stopped LoggerContext[name={}, {}] with status {}", getName(), this, Boolean.valueOf(true));
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 421 */     return this.contextName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getRootLogger() {
/* 430 */     return getLogger("");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 440 */     this.contextName = Objects.<String>requireNonNull(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getObject(String key) {
/* 445 */     return this.externalMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object putObject(String key, Object value) {
/* 450 */     return this.externalMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object putObjectIfAbsent(String key, Object value) {
/* 455 */     return this.externalMap.putIfAbsent(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeObject(String key) {
/* 460 */     return this.externalMap.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeObject(String key, Object value) {
/* 465 */     return this.externalMap.remove(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExternalContext(Object context) {
/* 474 */     if (context != null) {
/* 475 */       this.externalMap.put("__EXTERNAL_CONTEXT_KEY__", context);
/*     */     } else {
/* 477 */       this.externalMap.remove("__EXTERNAL_CONTEXT_KEY__");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getExternalContext() {
/* 488 */     return this.externalMap.get("__EXTERNAL_CONTEXT_KEY__");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name) {
/* 499 */     return getLogger(name, (MessageFactory)null);
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
/*     */   public Collection<Logger> getLoggers() {
/* 512 */     return this.loggerRegistry.getLoggers();
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
/*     */   public Logger getLogger(String name, MessageFactory messageFactory) {
/* 526 */     Logger logger = (Logger)this.loggerRegistry.getLogger(name, messageFactory);
/* 527 */     if (logger != null) {
/* 528 */       AbstractLogger.checkMessageFactory((ExtendedLogger)logger, messageFactory);
/* 529 */       return logger;
/*     */     } 
/*     */     
/* 532 */     logger = newInstance(this, name, messageFactory);
/* 533 */     this.loggerRegistry.putIfAbsent(name, messageFactory, (ExtendedLogger)logger);
/* 534 */     return (Logger)this.loggerRegistry.getLogger(name, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name) {
/* 545 */     return this.loggerRegistry.hasLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, MessageFactory messageFactory) {
/* 556 */     return this.loggerRegistry.hasLogger(name, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
/* 567 */     return this.loggerRegistry.hasLogger(name, messageFactoryClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration() {
/* 577 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter filter) {
/* 587 */     this.configuration.addFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeFilter(Filter filter) {
/* 596 */     this.configuration.removeFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration setConfiguration(Configuration config) {
/* 606 */     if (config == null) {
/* 607 */       LOGGER.error("No configuration found for context '{}'.", this.contextName);
/*     */       
/* 609 */       return this.configuration;
/*     */     } 
/* 611 */     this.configLock.lock();
/*     */     try {
/* 613 */       Configuration prev = this.configuration;
/* 614 */       config.addListener(this);
/*     */       
/* 616 */       ConcurrentMap<String, String> map = (ConcurrentMap<String, String>)config.getComponent("ContextProperties");
/*     */ 
/*     */       
/*     */       try {
/* 620 */         map.computeIfAbsent("hostName", s -> NetUtils.getLocalHostname());
/* 621 */       } catch (Exception ex) {
/* 622 */         LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", ex.toString());
/* 623 */         map.putIfAbsent("hostName", "unknown");
/*     */       } 
/* 625 */       map.putIfAbsent("contextName", this.contextName);
/* 626 */       config.start();
/* 627 */       this.configuration = config;
/* 628 */       updateLoggers();
/* 629 */       if (prev != null) {
/* 630 */         prev.removeListener(this);
/* 631 */         prev.stop();
/*     */       } 
/*     */       
/* 634 */       firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
/*     */       
/*     */       try {
/* 637 */         Server.reregisterMBeansAfterReconfigure();
/* 638 */       } catch (LinkageError|Exception e) {
/*     */         
/* 640 */         LOGGER.error("Could not reconfigure JMX", e);
/*     */       } 
/*     */       
/* 643 */       Log4jLogEvent.setNanoClock(this.configuration.getNanoClock());
/*     */       
/* 645 */       return prev;
/*     */     } finally {
/* 647 */       this.configLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void firePropertyChangeEvent(PropertyChangeEvent event) {
/* 652 */     for (PropertyChangeListener listener : this.propertyChangeListeners) {
/* 653 */       listener.propertyChange(event);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener) {
/* 658 */     this.propertyChangeListeners.add(Objects.requireNonNull(listener, "listener"));
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener) {
/* 662 */     this.propertyChangeListeners.remove(listener);
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
/*     */   public URI getConfigLocation() {
/* 674 */     return this.configLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfigLocation(URI configLocation) {
/* 683 */     this.configLocation = configLocation;
/* 684 */     reconfigure(configLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reconfigure(URI configURI) {
/* 691 */     Object externalContext = this.externalMap.get("__EXTERNAL_CONTEXT_KEY__");
/* 692 */     ClassLoader cl = ClassLoader.class.isInstance(externalContext) ? (ClassLoader)externalContext : null;
/* 693 */     LOGGER.debug("Reconfiguration started for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, configURI, this, cl);
/*     */     
/* 695 */     Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this, this.contextName, configURI, cl);
/* 696 */     if (instance == null) {
/* 697 */       LOGGER.error("Reconfiguration failed: No configuration found for '{}' at '{}' in '{}'", this.contextName, configURI, cl);
/*     */     } else {
/* 699 */       setConfiguration(instance);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 704 */       String location = (this.configuration == null) ? "?" : String.valueOf(this.configuration.getConfigurationSource());
/* 705 */       LOGGER.debug("Reconfiguration complete for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, location, this, cl);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reconfigure() {
/* 716 */     reconfigure(this.configLocation);
/*     */   }
/*     */   
/*     */   public void reconfigure(Configuration configuration) {
/* 720 */     setConfiguration(configuration);
/* 721 */     ConfigurationSource source = configuration.getConfigurationSource();
/* 722 */     if (source != null) {
/* 723 */       URI uri = source.getURI();
/* 724 */       if (uri != null) {
/* 725 */         this.configLocation = uri;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLoggers() {
/* 734 */     updateLoggers(this.configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLoggers(Configuration config) {
/* 743 */     Configuration old = this.configuration;
/* 744 */     for (Logger logger : this.loggerRegistry.getLoggers()) {
/* 745 */       logger.updateConfiguration(config);
/*     */     }
/* 747 */     firePropertyChangeEvent(new PropertyChangeEvent(this, "config", old, config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void onChange(Reconfigurable reconfigurable) {
/* 757 */     long startMillis = System.currentTimeMillis();
/* 758 */     LOGGER.debug("Reconfiguration started for context {} ({})", this.contextName, this);
/* 759 */     initApiModule();
/* 760 */     Configuration newConfig = reconfigurable.reconfigure();
/* 761 */     if (newConfig != null) {
/* 762 */       setConfiguration(newConfig);
/* 763 */       LOGGER.debug("Reconfiguration completed for {} ({}) in {} milliseconds.", this.contextName, this, 
/* 764 */           Long.valueOf(System.currentTimeMillis() - startMillis));
/*     */     } else {
/* 766 */       LOGGER.debug("Reconfiguration failed for {} ({}) in {} milliseconds.", this.contextName, this, 
/* 767 */           Long.valueOf(System.currentTimeMillis() - startMillis));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initApiModule() {
/* 772 */     ThreadContextMapFactory.init();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Logger newInstance(LoggerContext ctx, String name, MessageFactory messageFactory) {
/* 777 */     return new Logger(ctx, name, messageFactory);
/*     */   }
/*     */   
/*     */   private class ThreadContextDataTask implements Runnable {
/*     */     private ThreadContextDataTask() {}
/*     */     
/*     */     public void run() {
/* 784 */       AbstractLifeCycle.LOGGER.debug("Initializing Thread Context Data Service Providers");
/* 785 */       ThreadContextDataInjector.initServiceProviders();
/* 786 */       AbstractLifeCycle.LOGGER.debug("Thread Context Data Service Provider initialization complete");
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\LoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */