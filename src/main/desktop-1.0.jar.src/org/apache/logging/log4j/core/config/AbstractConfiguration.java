/*      */ package org.apache.logging.log4j.core.config;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.core.Appender;
/*      */ import org.apache.logging.log4j.core.Filter;
/*      */ import org.apache.logging.log4j.core.Layout;
/*      */ import org.apache.logging.log4j.core.LifeCycle;
/*      */ import org.apache.logging.log4j.core.LifeCycle2;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.Logger;
/*      */ import org.apache.logging.log4j.core.LoggerContext;
/*      */ import org.apache.logging.log4j.core.Version;
/*      */ import org.apache.logging.log4j.core.appender.ConsoleAppender;
/*      */ import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
/*      */ import org.apache.logging.log4j.core.async.AsyncLoggerConfigDisruptor;
/*      */ import org.apache.logging.log4j.core.config.arbiters.Arbiter;
/*      */ import org.apache.logging.log4j.core.config.arbiters.SelectArbiter;
/*      */ import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
/*      */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*      */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*      */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*      */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*      */ import org.apache.logging.log4j.core.lookup.ConfigurationStrSubstitutor;
/*      */ import org.apache.logging.log4j.core.lookup.Interpolator;
/*      */ import org.apache.logging.log4j.core.lookup.MapLookup;
/*      */ import org.apache.logging.log4j.core.lookup.RuntimeStrSubstitutor;
/*      */ import org.apache.logging.log4j.core.lookup.StrLookup;
/*      */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*      */ import org.apache.logging.log4j.core.net.Advertiser;
/*      */ import org.apache.logging.log4j.core.script.AbstractScript;
/*      */ import org.apache.logging.log4j.core.script.ScriptManager;
/*      */ import org.apache.logging.log4j.core.util.DummyNanoClock;
/*      */ import org.apache.logging.log4j.core.util.Loader;
/*      */ import org.apache.logging.log4j.core.util.NameUtil;
/*      */ import org.apache.logging.log4j.core.util.NanoClock;
/*      */ import org.apache.logging.log4j.core.util.Source;
/*      */ import org.apache.logging.log4j.core.util.WatchManager;
/*      */ import org.apache.logging.log4j.core.util.Watcher;
/*      */ import org.apache.logging.log4j.core.util.WatcherFactory;
/*      */ import org.apache.logging.log4j.util.PropertiesUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractConfiguration
/*      */   extends AbstractFilterable
/*      */   implements Configuration
/*      */ {
/*      */   private static final int BUF_SIZE = 16384;
/*      */   protected Node rootNode;
/*   95 */   protected final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  100 */   protected final List<String> pluginPackages = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PluginManager pluginManager;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isShutdownHookEnabled = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  115 */   protected long shutdownTimeoutMillis = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ScriptManager scriptManager;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  125 */   private Advertiser advertiser = new DefaultAdvertiser();
/*  126 */   private Node advertiserNode = null;
/*      */   private Object advertisement;
/*      */   private String name;
/*  129 */   private ConcurrentMap<String, Appender> appenders = new ConcurrentHashMap<>();
/*  130 */   private ConcurrentMap<String, LoggerConfig> loggerConfigs = new ConcurrentHashMap<>();
/*  131 */   private List<CustomLevelConfig> customLevels = Collections.emptyList();
/*  132 */   private final ConcurrentMap<String, String> propertyMap = new ConcurrentHashMap<>();
/*  133 */   private final StrLookup tempLookup = (StrLookup)new Interpolator(this.propertyMap);
/*  134 */   private final StrSubstitutor subst = (StrSubstitutor)new RuntimeStrSubstitutor(this.tempLookup);
/*  135 */   private final StrSubstitutor configurationStrSubstitutor = (StrSubstitutor)new ConfigurationStrSubstitutor(this.subst);
/*  136 */   private LoggerConfig root = new LoggerConfig();
/*  137 */   private final ConcurrentMap<String, Object> componentMap = new ConcurrentHashMap<>();
/*      */   private final ConfigurationSource configurationSource;
/*  139 */   private final ConfigurationScheduler configurationScheduler = new ConfigurationScheduler();
/*  140 */   private final WatchManager watchManager = new WatchManager(this.configurationScheduler);
/*      */   private AsyncLoggerConfigDisruptor asyncLoggerConfigDisruptor;
/*  142 */   private NanoClock nanoClock = (NanoClock)new DummyNanoClock();
/*      */ 
/*      */   
/*      */   private final WeakReference<LoggerContext> loggerContext;
/*      */ 
/*      */   
/*      */   protected AbstractConfiguration(LoggerContext loggerContext, ConfigurationSource configurationSource) {
/*  149 */     this.loggerContext = new WeakReference<>(loggerContext);
/*      */ 
/*      */     
/*  152 */     this.configurationSource = Objects.<ConfigurationSource>requireNonNull(configurationSource, "configurationSource is null");
/*  153 */     this.componentMap.put("ContextProperties", this.propertyMap);
/*  154 */     this.pluginManager = new PluginManager("Core");
/*  155 */     this.rootNode = new Node();
/*  156 */     setState(LifeCycle.State.INITIALIZING);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ConfigurationSource getConfigurationSource() {
/*  162 */     return this.configurationSource;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> getPluginPackages() {
/*  167 */     return this.pluginPackages;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> getProperties() {
/*  172 */     return this.propertyMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public ScriptManager getScriptManager() {
/*  177 */     return this.scriptManager;
/*      */   }
/*      */   
/*      */   public void setScriptManager(ScriptManager scriptManager) {
/*  181 */     this.scriptManager = scriptManager;
/*      */   }
/*      */   
/*      */   public PluginManager getPluginManager() {
/*  185 */     return this.pluginManager;
/*      */   }
/*      */   
/*      */   public void setPluginManager(PluginManager pluginManager) {
/*  189 */     this.pluginManager = pluginManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public WatchManager getWatchManager() {
/*  194 */     return this.watchManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public ConfigurationScheduler getScheduler() {
/*  199 */     return this.configurationScheduler;
/*      */   }
/*      */   
/*      */   public Node getRootNode() {
/*  203 */     return this.rootNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate() {
/*  210 */     if (this.asyncLoggerConfigDisruptor == null) {
/*  211 */       this.asyncLoggerConfigDisruptor = new AsyncLoggerConfigDisruptor();
/*      */     }
/*  213 */     return (AsyncLoggerConfigDelegate)this.asyncLoggerConfigDisruptor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initialize() {
/*  221 */     LOGGER.debug(Version.getProductString() + " initializing configuration {}", this);
/*  222 */     this.subst.setConfiguration(this);
/*  223 */     this.configurationStrSubstitutor.setConfiguration(this);
/*      */     try {
/*  225 */       this.scriptManager = new ScriptManager(this, this.watchManager);
/*  226 */     } catch (LinkageError|Exception e) {
/*      */       
/*  228 */       LOGGER.info("Cannot initialize scripting support because this JRE does not support it.", e);
/*      */     } 
/*  230 */     this.pluginManager.collectPlugins(this.pluginPackages);
/*  231 */     PluginManager levelPlugins = new PluginManager("Level");
/*  232 */     levelPlugins.collectPlugins(this.pluginPackages);
/*  233 */     Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
/*  234 */     if (plugins != null) {
/*  235 */       for (PluginType<?> type : plugins.values()) {
/*      */         
/*      */         try {
/*  238 */           Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
/*  239 */         } catch (Exception e) {
/*  240 */           LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e.getClass()
/*  241 */               .getSimpleName(), e);
/*      */         } 
/*      */       } 
/*      */     }
/*  245 */     setup();
/*  246 */     setupAdvertisement();
/*  247 */     doConfigure();
/*  248 */     setState(LifeCycle.State.INITIALIZED);
/*  249 */     LOGGER.debug("Configuration {} initialized", this);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initializeWatchers(Reconfigurable reconfigurable, ConfigurationSource configSource, int monitorIntervalSeconds) {
/*  254 */     if (configSource.getFile() != null || configSource.getURL() != null) {
/*  255 */       if (monitorIntervalSeconds > 0) {
/*  256 */         this.watchManager.setIntervalSeconds(monitorIntervalSeconds);
/*  257 */         if (configSource.getFile() != null) {
/*  258 */           Source cfgSource = new Source(configSource);
/*  259 */           long lastModifeid = configSource.getFile().lastModified();
/*  260 */           ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(this, reconfigurable, this.listeners, lastModifeid);
/*      */           
/*  262 */           this.watchManager.watch(cfgSource, (Watcher)watcher);
/*  263 */         } else if (configSource.getURL() != null) {
/*  264 */           monitorSource(reconfigurable, configSource);
/*      */         } 
/*  266 */       } else if (this.watchManager.hasEventListeners() && configSource.getURL() != null && monitorIntervalSeconds >= 0) {
/*      */         
/*  268 */         monitorSource(reconfigurable, configSource);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void monitorSource(Reconfigurable reconfigurable, ConfigurationSource configSource) {
/*  274 */     if (configSource.getLastModified() > 0L) {
/*  275 */       Source cfgSource = new Source(configSource);
/*      */       
/*  277 */       Watcher watcher = WatcherFactory.getInstance(this.pluginPackages).newWatcher(cfgSource, this, reconfigurable, this.listeners, configSource.getLastModified());
/*  278 */       if (watcher != null) {
/*  279 */         this.watchManager.watch(cfgSource, watcher);
/*      */       }
/*      */     } else {
/*  282 */       LOGGER.info("{} does not support dynamic reconfiguration", configSource.getURI());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void start() {
/*  292 */     if (getState().equals(LifeCycle.State.INITIALIZING)) {
/*  293 */       initialize();
/*      */     }
/*  295 */     LOGGER.debug("Starting configuration {}", this);
/*  296 */     setStarting();
/*  297 */     if (this.watchManager.getIntervalSeconds() >= 0) {
/*  298 */       this.watchManager.start();
/*      */     }
/*  300 */     if (hasAsyncLoggers()) {
/*  301 */       this.asyncLoggerConfigDisruptor.start();
/*      */     }
/*  303 */     Set<LoggerConfig> alreadyStarted = new HashSet<>();
/*  304 */     for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  305 */       logger.start();
/*  306 */       alreadyStarted.add(logger);
/*      */     } 
/*  308 */     for (Appender appender : this.appenders.values()) {
/*  309 */       appender.start();
/*      */     }
/*  311 */     if (!alreadyStarted.contains(this.root)) {
/*  312 */       this.root.start();
/*      */     }
/*  314 */     super.start();
/*  315 */     LOGGER.debug("Started configuration {} OK.", this);
/*      */   }
/*      */   
/*      */   private boolean hasAsyncLoggers() {
/*  319 */     if (this.root instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig) {
/*  320 */       return true;
/*      */     }
/*  322 */     for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  323 */       if (logger instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig) {
/*  324 */         return true;
/*      */       }
/*      */     } 
/*  327 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  335 */     setStopping();
/*  336 */     stop(timeout, timeUnit, false);
/*  337 */     LOGGER.trace("Stopping {}...", this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  352 */     for (LoggerConfig loggerConfig : this.loggerConfigs.values()) {
/*  353 */       loggerConfig.getReliabilityStrategy().beforeStopConfiguration(this);
/*      */     }
/*  355 */     this.root.getReliabilityStrategy().beforeStopConfiguration(this);
/*      */     
/*  357 */     String cls = getClass().getSimpleName();
/*  358 */     LOGGER.trace("{} notified {} ReliabilityStrategies that config will be stopped.", cls, Integer.valueOf(this.loggerConfigs.size() + 1));
/*      */ 
/*      */     
/*  361 */     if (!this.loggerConfigs.isEmpty()) {
/*  362 */       LOGGER.trace("{} stopping {} LoggerConfigs.", cls, Integer.valueOf(this.loggerConfigs.size()));
/*  363 */       for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  364 */         logger.stop(timeout, timeUnit);
/*      */       }
/*      */     } 
/*  367 */     LOGGER.trace("{} stopping root LoggerConfig.", cls);
/*  368 */     if (!this.root.isStopped()) {
/*  369 */       this.root.stop(timeout, timeUnit);
/*      */     }
/*      */     
/*  372 */     if (hasAsyncLoggers()) {
/*  373 */       LOGGER.trace("{} stopping AsyncLoggerConfigDisruptor.", cls);
/*  374 */       this.asyncLoggerConfigDisruptor.stop(timeout, timeUnit);
/*      */     } 
/*      */     
/*  377 */     LOGGER.trace("{} notifying ReliabilityStrategies that appenders will be stopped.", cls);
/*  378 */     for (LoggerConfig loggerConfig : this.loggerConfigs.values()) {
/*  379 */       loggerConfig.getReliabilityStrategy().beforeStopAppenders();
/*      */     }
/*  381 */     this.root.getReliabilityStrategy().beforeStopAppenders();
/*      */ 
/*      */     
/*  384 */     Appender[] array = (Appender[])this.appenders.values().toArray((Object[])new Appender[this.appenders.size()]);
/*  385 */     List<Appender> async = getAsyncAppenders(array);
/*  386 */     if (!async.isEmpty()) {
/*      */       
/*  388 */       LOGGER.trace("{} stopping {} AsyncAppenders.", cls, Integer.valueOf(async.size()));
/*  389 */       for (Appender appender : async) {
/*  390 */         if (appender instanceof LifeCycle2) {
/*  391 */           ((LifeCycle2)appender).stop(timeout, timeUnit); continue;
/*      */         } 
/*  393 */         appender.stop();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  398 */     LOGGER.trace("{} stopping remaining Appenders.", cls);
/*  399 */     int appenderCount = 0;
/*  400 */     for (int i = array.length - 1; i >= 0; i--) {
/*  401 */       if (array[i].isStarted()) {
/*  402 */         if (array[i] instanceof LifeCycle2) {
/*  403 */           ((LifeCycle2)array[i]).stop(timeout, timeUnit);
/*      */         } else {
/*  405 */           array[i].stop();
/*      */         } 
/*  407 */         appenderCount++;
/*      */       } 
/*      */     } 
/*  410 */     LOGGER.trace("{} stopped {} remaining Appenders.", cls, Integer.valueOf(appenderCount));
/*      */     
/*  412 */     LOGGER.trace("{} cleaning Appenders from {} LoggerConfigs.", cls, Integer.valueOf(this.loggerConfigs.size() + 1));
/*  413 */     for (LoggerConfig loggerConfig : this.loggerConfigs.values())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  420 */       loggerConfig.clearAppenders();
/*      */     }
/*  422 */     this.root.clearAppenders();
/*      */     
/*  424 */     if (this.watchManager.isStarted()) {
/*  425 */       this.watchManager.stop(timeout, timeUnit);
/*      */     }
/*  427 */     this.configurationScheduler.stop(timeout, timeUnit);
/*      */     
/*  429 */     if (this.advertiser != null && this.advertisement != null) {
/*  430 */       this.advertiser.unadvertise(this.advertisement);
/*      */     }
/*  432 */     setStopped();
/*  433 */     LOGGER.debug("Stopped {} OK", this);
/*  434 */     return true;
/*      */   }
/*      */   
/*      */   private List<Appender> getAsyncAppenders(Appender[] all) {
/*  438 */     List<Appender> result = new ArrayList<>();
/*  439 */     for (int i = all.length - 1; i >= 0; i--) {
/*  440 */       if (all[i] instanceof org.apache.logging.log4j.core.appender.AsyncAppender) {
/*  441 */         result.add(all[i]);
/*      */       }
/*      */     } 
/*  444 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isShutdownHookEnabled() {
/*  449 */     return this.isShutdownHookEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getShutdownTimeoutMillis() {
/*  454 */     return this.shutdownTimeoutMillis;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setup() {}
/*      */ 
/*      */   
/*      */   protected Level getDefaultStatus() {
/*  462 */     String statusLevel = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR
/*  463 */         .name());
/*      */     try {
/*  465 */       return Level.toLevel(statusLevel);
/*  466 */     } catch (Exception ex) {
/*  467 */       return Level.ERROR;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void createAdvertiser(String advertiserString, ConfigurationSource configSource, byte[] buffer, String contentType) {
/*  473 */     if (advertiserString != null) {
/*  474 */       Node node = new Node(null, advertiserString, null);
/*  475 */       Map<String, String> attributes = node.getAttributes();
/*  476 */       attributes.put("content", new String(buffer));
/*  477 */       attributes.put("contentType", contentType);
/*  478 */       attributes.put("name", "configuration");
/*  479 */       if (configSource.getLocation() != null) {
/*  480 */         attributes.put("location", configSource.getLocation());
/*      */       }
/*  482 */       this.advertiserNode = node;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupAdvertisement() {
/*  487 */     if (this.advertiserNode != null) {
/*  488 */       String nodeName = this.advertiserNode.getName();
/*  489 */       PluginType<?> type = this.pluginManager.getPluginType(nodeName);
/*  490 */       if (type != null) {
/*  491 */         Class<? extends Advertiser> clazz = type.getPluginClass().asSubclass(Advertiser.class);
/*      */         try {
/*  493 */           this.advertiser = clazz.newInstance();
/*  494 */           this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
/*  495 */         } catch (InstantiationException e) {
/*  496 */           LOGGER.error("InstantiationException attempting to instantiate advertiser: {}", nodeName, e);
/*  497 */         } catch (IllegalAccessException e) {
/*  498 */           LOGGER.error("IllegalAccessException attempting to instantiate advertiser: {}", nodeName, e);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getComponent(String componentName) {
/*  507 */     return (T)this.componentMap.get(componentName);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addComponent(String componentName, Object obj) {
/*  512 */     this.componentMap.putIfAbsent(componentName, obj);
/*      */   }
/*      */   
/*      */   protected void preConfigure(Node node) {
/*      */     try {
/*  517 */       for (Node child : node.getChildren()) {
/*  518 */         if (child.getType() == null) {
/*  519 */           LOGGER.error("Unable to locate plugin type for " + child.getName());
/*      */           continue;
/*      */         } 
/*  522 */         Class<?> clazz = child.getType().getPluginClass();
/*  523 */         if (clazz.isAnnotationPresent((Class)Scheduled.class)) {
/*  524 */           this.configurationScheduler.incrementScheduledItems();
/*      */         }
/*  526 */         preConfigure(child);
/*      */       } 
/*  528 */     } catch (Exception ex) {
/*  529 */       LOGGER.error("Error capturing node data for node " + node.getName(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void processConditionals(Node node) {
/*      */     try {
/*  540 */       List<Node> addList = new ArrayList<>();
/*  541 */       List<Node> removeList = new ArrayList<>();
/*  542 */       for (Node child : node.getChildren()) {
/*  543 */         PluginType<?> type = child.getType();
/*  544 */         if (type != null && "Arbiter".equals(type.getElementName())) {
/*  545 */           Class<?> clazz = type.getPluginClass();
/*  546 */           if (SelectArbiter.class.isAssignableFrom(clazz)) {
/*  547 */             removeList.add(child);
/*  548 */             addList.addAll(processSelect(child, type)); continue;
/*  549 */           }  if (Arbiter.class.isAssignableFrom(clazz)) {
/*  550 */             removeList.add(child);
/*      */             try {
/*  552 */               Arbiter condition = (Arbiter)createPluginObject(type, child, (LogEvent)null);
/*  553 */               if (condition.isCondition()) {
/*  554 */                 addList.addAll(child.getChildren());
/*  555 */                 processConditionals(child);
/*      */               } 
/*  557 */             } catch (Exception inner) {
/*  558 */               LOGGER.error("Exception processing {}: Ignoring and including children", type
/*  559 */                   .getPluginClass());
/*  560 */               processConditionals(child);
/*      */             }  continue;
/*      */           } 
/*  563 */           LOGGER.error("Encountered Condition Plugin that does not implement Condition: {}", child
/*  564 */               .getName());
/*  565 */           processConditionals(child);
/*      */           continue;
/*      */         } 
/*  568 */         processConditionals(child);
/*      */       } 
/*      */       
/*  571 */       if (!removeList.isEmpty()) {
/*  572 */         List<Node> children = node.getChildren();
/*  573 */         children.removeAll(removeList);
/*  574 */         children.addAll(addList);
/*  575 */         for (Node grandChild : addList) {
/*  576 */           grandChild.setParent(node);
/*      */         }
/*      */       } 
/*  579 */     } catch (Exception ex) {
/*  580 */       LOGGER.error("Error capturing node data for node " + node.getName(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Node> processSelect(Node selectNode, PluginType<?> type) {
/*  592 */     List<Node> addList = new ArrayList<>();
/*  593 */     SelectArbiter select = (SelectArbiter)createPluginObject(type, selectNode, (LogEvent)null);
/*  594 */     List<Arbiter> conditions = new ArrayList<>();
/*  595 */     for (Node child : selectNode.getChildren()) {
/*  596 */       PluginType<?> nodeType = child.getType();
/*  597 */       if (nodeType != null) {
/*  598 */         if (Arbiter.class.isAssignableFrom(nodeType.getPluginClass())) {
/*  599 */           Arbiter arbiter = (Arbiter)createPluginObject(nodeType, child, (LogEvent)null);
/*  600 */           conditions.add(arbiter);
/*  601 */           child.setObject(arbiter); continue;
/*      */         } 
/*  603 */         LOGGER.error("Invalid Node {} for Select. Must be a Condition", child
/*  604 */             .getName());
/*      */         continue;
/*      */       } 
/*  607 */       LOGGER.error("No PluginType for node {}", child.getName());
/*      */     } 
/*      */     
/*  610 */     Arbiter condition = select.evaluateConditions(conditions);
/*  611 */     if (condition != null) {
/*  612 */       for (Node child : selectNode.getChildren()) {
/*  613 */         if (condition == child.getObject()) {
/*  614 */           addList.addAll(child.getChildren());
/*  615 */           processConditionals(child);
/*      */         } 
/*      */       } 
/*      */     }
/*  619 */     return addList;
/*      */   }
/*      */   
/*      */   protected void doConfigure() {
/*  623 */     processConditionals(this.rootNode);
/*  624 */     preConfigure(this.rootNode);
/*  625 */     this.configurationScheduler.start();
/*  626 */     if (this.rootNode.hasChildren() && ((Node)this.rootNode.getChildren().get(0)).getName().equalsIgnoreCase("Properties")) {
/*  627 */       Node first = this.rootNode.getChildren().get(0);
/*  628 */       createConfiguration(first, (LogEvent)null);
/*  629 */       if (first.getObject() != null) {
/*  630 */         StrLookup lookup = first.<StrLookup>getObject();
/*  631 */         this.subst.setVariableResolver(lookup);
/*  632 */         this.configurationStrSubstitutor.setVariableResolver(lookup);
/*      */       } 
/*      */     } else {
/*  635 */       Map<String, String> map = getComponent("ContextProperties");
/*  636 */       MapLookup mapLookup = (map == null) ? null : new MapLookup(map);
/*  637 */       Interpolator interpolator = new Interpolator((StrLookup)mapLookup, this.pluginPackages);
/*  638 */       this.subst.setVariableResolver((StrLookup)interpolator);
/*  639 */       this.configurationStrSubstitutor.setVariableResolver((StrLookup)interpolator);
/*      */     } 
/*      */     
/*  642 */     boolean setLoggers = false;
/*  643 */     boolean setRoot = false;
/*  644 */     for (Node child : this.rootNode.getChildren()) {
/*  645 */       if (child.getName().equalsIgnoreCase("Properties")) {
/*  646 */         if (this.tempLookup == this.subst.getVariableResolver()) {
/*  647 */           LOGGER.error("Properties declaration must be the first element in the configuration");
/*      */         }
/*      */         continue;
/*      */       } 
/*  651 */       createConfiguration(child, (LogEvent)null);
/*  652 */       if (child.getObject() == null) {
/*      */         continue;
/*      */       }
/*  655 */       if (child.getName().equalsIgnoreCase("Scripts")) {
/*  656 */         for (AbstractScript script : (AbstractScript[])child.<AbstractScript[]>getObject((Class)AbstractScript[].class)) {
/*  657 */           if (script instanceof org.apache.logging.log4j.core.script.ScriptRef) {
/*  658 */             LOGGER.error("Script reference to {} not added. Scripts definition cannot contain script references", script
/*  659 */                 .getName());
/*  660 */           } else if (this.scriptManager != null) {
/*  661 */             this.scriptManager.addScript(script);
/*      */           } 
/*      */         }  continue;
/*  664 */       }  if (child.getName().equalsIgnoreCase("Appenders")) {
/*  665 */         this.appenders = child.<ConcurrentMap<String, Appender>>getObject(); continue;
/*  666 */       }  if (child.isInstanceOf(Filter.class)) {
/*  667 */         addFilter(child.<Filter>getObject(Filter.class)); continue;
/*  668 */       }  if (child.getName().equalsIgnoreCase("Loggers")) {
/*  669 */         Loggers l = child.<Loggers>getObject();
/*  670 */         this.loggerConfigs = l.getMap();
/*  671 */         setLoggers = true;
/*  672 */         if (l.getRoot() != null) {
/*  673 */           this.root = l.getRoot();
/*  674 */           setRoot = true;
/*      */         }  continue;
/*  676 */       }  if (child.getName().equalsIgnoreCase("CustomLevels")) {
/*  677 */         this.customLevels = ((CustomLevels)child.<CustomLevels>getObject(CustomLevels.class)).getCustomLevels(); continue;
/*  678 */       }  if (child.isInstanceOf(CustomLevelConfig.class)) {
/*  679 */         List<CustomLevelConfig> copy = new ArrayList<>(this.customLevels);
/*  680 */         copy.add(child.getObject(CustomLevelConfig.class));
/*  681 */         this.customLevels = copy; continue;
/*      */       } 
/*  683 */       List<String> expected = Arrays.asList(new String[] { "\"Appenders\"", "\"Loggers\"", "\"Properties\"", "\"Scripts\"", "\"CustomLevels\"" });
/*      */       
/*  685 */       LOGGER.error("Unknown object \"{}\" of type {} is ignored: try nesting it inside one of: {}.", child
/*  686 */           .getName(), child.<T>getObject().getClass().getName(), expected);
/*      */     } 
/*      */ 
/*      */     
/*  690 */     if (!setLoggers) {
/*  691 */       LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
/*  692 */       setToDefault(); return;
/*      */     } 
/*  694 */     if (!setRoot) {
/*  695 */       LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
/*  696 */       setToDefault();
/*      */     } 
/*      */ 
/*      */     
/*  700 */     for (Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
/*  701 */       LoggerConfig loggerConfig = entry.getValue();
/*  702 */       for (AppenderRef ref : loggerConfig.getAppenderRefs()) {
/*  703 */         Appender app = this.appenders.get(ref.getRef());
/*  704 */         if (app != null) {
/*  705 */           loggerConfig.addAppender(app, ref.getLevel(), ref.getFilter()); continue;
/*      */         } 
/*  707 */         LOGGER.error("Unable to locate appender \"{}\" for logger config \"{}\"", ref.getRef(), loggerConfig);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  714 */     setParents();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setToDefault() {
/*  719 */     setName("Default@" + Integer.toHexString(hashCode()));
/*      */ 
/*      */ 
/*      */     
/*  723 */     PatternLayout patternLayout = PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n").withConfiguration(this).build();
/*  724 */     ConsoleAppender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout((Layout)patternLayout);
/*  725 */     consoleAppender.start();
/*  726 */     addAppender((Appender)consoleAppender);
/*  727 */     LoggerConfig rootLoggerConfig = getRootLogger();
/*  728 */     rootLoggerConfig.addAppender((Appender)consoleAppender, (Level)null, (Filter)null);
/*      */     
/*  730 */     Level defaultLevel = Level.ERROR;
/*  731 */     String levelName = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level", defaultLevel
/*  732 */         .name());
/*  733 */     Level level = Level.valueOf(levelName);
/*  734 */     rootLoggerConfig.setLevel((level != null) ? level : defaultLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  743 */     this.name = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  753 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addListener(ConfigurationListener listener) {
/*  763 */     this.listeners.add(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeListener(ConfigurationListener listener) {
/*  773 */     this.listeners.remove(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Appender> T getAppender(String appenderName) {
/*  785 */     return (appenderName != null) ? (T)this.appenders.get(appenderName) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Appender> getAppenders() {
/*  795 */     return this.appenders;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAppender(Appender appender) {
/*  805 */     if (appender != null) {
/*  806 */       this.appenders.putIfAbsent(appender.getName(), appender);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public StrSubstitutor getStrSubstitutor() {
/*  812 */     return this.subst;
/*      */   }
/*      */ 
/*      */   
/*      */   public StrSubstitutor getConfigurationStrSubstitutor() {
/*  817 */     return this.configurationStrSubstitutor;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAdvertiser(Advertiser advertiser) {
/*  822 */     this.advertiser = advertiser;
/*      */   }
/*      */ 
/*      */   
/*      */   public Advertiser getAdvertiser() {
/*  827 */     return this.advertiser;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReliabilityStrategy getReliabilityStrategy(LoggerConfig loggerConfig) {
/*  838 */     return ReliabilityStrategyFactory.getReliabilityStrategy(loggerConfig);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addLoggerAppender(Logger logger, Appender appender) {
/*  853 */     if (appender == null || logger == null) {
/*      */       return;
/*      */     }
/*  856 */     String loggerName = logger.getName();
/*  857 */     this.appenders.putIfAbsent(appender.getName(), appender);
/*  858 */     LoggerConfig lc = getLoggerConfig(loggerName);
/*  859 */     if (lc.getName().equals(loggerName)) {
/*  860 */       lc.addAppender(appender, (Level)null, (Filter)null);
/*      */     } else {
/*  862 */       LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
/*  863 */       nlc.addAppender(appender, (Level)null, (Filter)null);
/*  864 */       nlc.setParent(lc);
/*  865 */       this.loggerConfigs.putIfAbsent(loggerName, nlc);
/*  866 */       setParents();
/*  867 */       logger.getContext().updateLoggers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addLoggerFilter(Logger logger, Filter filter) {
/*  882 */     String loggerName = logger.getName();
/*  883 */     LoggerConfig lc = getLoggerConfig(loggerName);
/*  884 */     if (lc.getName().equals(loggerName)) {
/*  885 */       lc.addFilter(filter);
/*      */     } else {
/*  887 */       LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), lc.isAdditive());
/*  888 */       nlc.addFilter(filter);
/*  889 */       nlc.setParent(lc);
/*  890 */       this.loggerConfigs.putIfAbsent(loggerName, nlc);
/*  891 */       setParents();
/*  892 */       logger.getContext().updateLoggers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setLoggerAdditive(Logger logger, boolean additive) {
/*  907 */     String loggerName = logger.getName();
/*  908 */     LoggerConfig lc = getLoggerConfig(loggerName);
/*  909 */     if (lc.getName().equals(loggerName)) {
/*  910 */       lc.setAdditive(additive);
/*      */     } else {
/*  912 */       LoggerConfig nlc = new LoggerConfig(loggerName, lc.getLevel(), additive);
/*  913 */       nlc.setParent(lc);
/*  914 */       this.loggerConfigs.putIfAbsent(loggerName, nlc);
/*  915 */       setParents();
/*  916 */       logger.getContext().updateLoggers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeAppender(String appenderName) {
/*  928 */     for (LoggerConfig logger : this.loggerConfigs.values()) {
/*  929 */       logger.removeAppender(appenderName);
/*      */     }
/*  931 */     Appender app = (appenderName != null) ? this.appenders.remove(appenderName) : null;
/*      */     
/*  933 */     if (app != null) {
/*  934 */       app.stop();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<CustomLevelConfig> getCustomLevels() {
/*  945 */     return Collections.unmodifiableList(this.customLevels);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerConfig getLoggerConfig(String loggerName) {
/*  957 */     LoggerConfig loggerConfig = this.loggerConfigs.get(loggerName);
/*  958 */     if (loggerConfig != null) {
/*  959 */       return loggerConfig;
/*      */     }
/*  961 */     String substr = loggerName;
/*  962 */     while ((substr = NameUtil.getSubName(substr)) != null) {
/*  963 */       loggerConfig = this.loggerConfigs.get(substr);
/*  964 */       if (loggerConfig != null) {
/*  965 */         return loggerConfig;
/*      */       }
/*      */     } 
/*  968 */     return this.root;
/*      */   }
/*      */ 
/*      */   
/*      */   public LoggerContext getLoggerContext() {
/*  973 */     return this.loggerContext.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerConfig getRootLogger() {
/*  983 */     return this.root;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, LoggerConfig> getLoggers() {
/*  993 */     return Collections.unmodifiableMap(this.loggerConfigs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LoggerConfig getLogger(String loggerName) {
/* 1003 */     return this.loggerConfigs.get(loggerName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addLogger(String loggerName, LoggerConfig loggerConfig) {
/* 1015 */     this.loggerConfigs.putIfAbsent(loggerName, loggerConfig);
/* 1016 */     setParents();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeLogger(String loggerName) {
/* 1026 */     this.loggerConfigs.remove(loggerName);
/* 1027 */     setParents();
/*      */   }
/*      */ 
/*      */   
/*      */   public void createConfiguration(Node node, LogEvent event) {
/* 1032 */     PluginType<?> type = node.getType();
/* 1033 */     if (type != null && type.isDeferChildren()) {
/* 1034 */       node.setObject(createPluginObject(type, node, event));
/*      */     } else {
/* 1036 */       for (Node child : node.getChildren()) {
/* 1037 */         createConfiguration(child, event);
/*      */       }
/*      */       
/* 1040 */       if (type == null) {
/* 1041 */         if (node.getParent() != null) {
/* 1042 */           LOGGER.error("Unable to locate plugin for {}", node.getName());
/*      */         }
/*      */       } else {
/* 1045 */         node.setObject(createPluginObject(type, node, event));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createPluginObject(PluginType<?> type, Node node) {
/* 1057 */     if (getState().equals(LifeCycle.State.INITIALIZING)) {
/* 1058 */       return createPluginObject(type, node, (LogEvent)null);
/*      */     }
/* 1060 */     LOGGER.warn("Plugin Object creation is not allowed after initialization");
/* 1061 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object createPluginObject(PluginType<?> type, Node node, LogEvent event) {
/* 1102 */     Class<?> clazz = type.getPluginClass();
/*      */     
/* 1104 */     if (Map.class.isAssignableFrom(clazz)) {
/*      */       try {
/* 1106 */         return createPluginMap(node);
/* 1107 */       } catch (Exception e) {
/* 1108 */         LOGGER.warn("Unable to create Map for {} of class {}", type.getElementName(), clazz, e);
/*      */       } 
/*      */     }
/*      */     
/* 1112 */     if (Collection.class.isAssignableFrom(clazz)) {
/*      */       try {
/* 1114 */         return createPluginCollection(node);
/* 1115 */       } catch (Exception e) {
/* 1116 */         LOGGER.warn("Unable to create List for {} of class {}", type.getElementName(), clazz, e);
/*      */       } 
/*      */     }
/*      */     
/* 1120 */     return (new PluginBuilder(type)).withConfiguration(this).withConfigurationNode(node).forLogEvent(event).build();
/*      */   }
/*      */   
/*      */   private static Map<String, ?> createPluginMap(Node node) {
/* 1124 */     Map<String, Object> map = new LinkedHashMap<>();
/* 1125 */     for (Node child : node.getChildren()) {
/* 1126 */       Object object = child.getObject();
/* 1127 */       map.put(child.getName(), object);
/*      */     } 
/* 1129 */     return map;
/*      */   }
/*      */   
/*      */   private static Collection<?> createPluginCollection(Node node) {
/* 1133 */     List<Node> children = node.getChildren();
/* 1134 */     Collection<Object> list = new ArrayList(children.size());
/* 1135 */     for (Node child : children) {
/* 1136 */       Object object = child.getObject();
/* 1137 */       list.add(object);
/*      */     } 
/* 1139 */     return list;
/*      */   }
/*      */   
/*      */   private void setParents() {
/* 1143 */     for (Map.Entry<String, LoggerConfig> entry : this.loggerConfigs.entrySet()) {
/* 1144 */       LoggerConfig logger = entry.getValue();
/* 1145 */       String key = entry.getKey();
/* 1146 */       if (!key.isEmpty()) {
/* 1147 */         int i = key.lastIndexOf('.');
/* 1148 */         if (i > 0) {
/* 1149 */           key = key.substring(0, i);
/* 1150 */           LoggerConfig parent = getLoggerConfig(key);
/* 1151 */           if (parent == null) {
/* 1152 */             parent = this.root;
/*      */           }
/* 1154 */           logger.setParent(parent); continue;
/*      */         } 
/* 1156 */         logger.setParent(this.root);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static byte[] toByteArray(InputStream is) throws IOException {
/* 1171 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*      */ 
/*      */     
/* 1174 */     byte[] data = new byte[16384];
/*      */     int nRead;
/* 1176 */     while ((nRead = is.read(data, 0, data.length)) != -1) {
/* 1177 */       buffer.write(data, 0, nRead);
/*      */     }
/*      */     
/* 1180 */     return buffer.toByteArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public NanoClock getNanoClock() {
/* 1185 */     return this.nanoClock;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNanoClock(NanoClock nanoClock) {
/* 1190 */     this.nanoClock = Objects.<NanoClock>requireNonNull(nanoClock, "nanoClock");
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\AbstractConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */