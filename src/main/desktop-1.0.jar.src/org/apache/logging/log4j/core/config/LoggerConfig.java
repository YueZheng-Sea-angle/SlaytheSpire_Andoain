/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
/*     */ import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.LocationAware;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.LogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.ReusableLogEventFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.StackLocatorUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "logger", category = "Core", printObject = true)
/*     */ public class LoggerConfig
/*     */   extends AbstractFilterable
/*     */   implements LocationAware
/*     */ {
/*     */   public static final String ROOT = "root";
/*  67 */   private static LogEventFactory LOG_EVENT_FACTORY = null;
/*     */   
/*  69 */   private List<AppenderRef> appenderRefs = new ArrayList<>();
/*  70 */   private final AppenderControlArraySet appenders = new AppenderControlArraySet();
/*     */   private final String name;
/*     */   private LogEventFactory logEventFactory;
/*     */   private Level level;
/*     */   private boolean additive = true;
/*     */   private boolean includeLocation = true;
/*     */   private LoggerConfig parent;
/*     */   private Map<Property, Boolean> propertiesMap;
/*     */   private final List<Property> properties;
/*     */   private final boolean propertiesRequireLookup;
/*     */   private final Configuration config;
/*     */   private final ReliabilityStrategy reliabilityStrategy;
/*     */   
/*     */   static {
/*  84 */     String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
/*  85 */     if (factory != null) {
/*     */       try {
/*  87 */         Class<?> clazz = Loader.loadClass(factory);
/*  88 */         if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz)) {
/*  89 */           LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance();
/*     */         }
/*  91 */       } catch (Exception ex) {
/*  92 */         LOGGER.error("Unable to create LogEventFactory {}", factory, ex);
/*     */       } 
/*     */     }
/*  95 */     if (LOG_EVENT_FACTORY == null) {
/*  96 */       LOG_EVENT_FACTORY = Constants.ENABLE_THREADLOCALS ? (LogEventFactory)new ReusableLogEventFactory() : (LogEventFactory)new DefaultLogEventFactory();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig() {
/* 106 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 107 */     this.level = Level.ERROR;
/* 108 */     this.name = "";
/* 109 */     this.properties = null;
/* 110 */     this.propertiesRequireLookup = false;
/* 111 */     this.config = null;
/* 112 */     this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig(String name, Level level, boolean additive) {
/* 123 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 124 */     this.name = name;
/* 125 */     this.level = level;
/* 126 */     this.additive = additive;
/* 127 */     this.properties = null;
/* 128 */     this.propertiesRequireLookup = false;
/* 129 */     this.config = null;
/* 130 */     this.reliabilityStrategy = new DefaultReliabilityStrategy(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected LoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
/* 136 */     super(filter);
/* 137 */     this.logEventFactory = LOG_EVENT_FACTORY;
/* 138 */     this.name = name;
/* 139 */     this.appenderRefs = appenders;
/* 140 */     this.level = level;
/* 141 */     this.additive = additive;
/* 142 */     this.includeLocation = includeLocation;
/* 143 */     this.config = config;
/* 144 */     if (properties != null && properties.length > 0) {
/* 145 */       this.properties = Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(properties, properties.length)));
/*     */     } else {
/*     */       
/* 148 */       this.properties = null;
/*     */     } 
/* 150 */     this.propertiesRequireLookup = containsPropertyRequiringLookup(properties);
/* 151 */     this.reliabilityStrategy = config.getReliabilityStrategy(this);
/*     */   }
/*     */   
/*     */   private static boolean containsPropertyRequiringLookup(Property[] properties) {
/* 155 */     if (properties == null) {
/* 156 */       return false;
/*     */     }
/* 158 */     for (int i = 0; i < properties.length; i++) {
/* 159 */       if (properties[i].isValueNeedsLookup()) {
/* 160 */         return true;
/*     */       }
/*     */     } 
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 168 */     return super.getFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 177 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(LoggerConfig parent) {
/* 186 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig getParent() {
/* 195 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender appender, Level level, Filter filter) {
/* 206 */     this.appenders.add(new AppenderControl(appender, level, filter));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(String name) {
/* 215 */     AppenderControl removed = null;
/* 216 */     while ((removed = this.appenders.remove(name)) != null) {
/* 217 */       cleanupFilter(removed);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 227 */     return this.appenders.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clearAppenders() {
/*     */     do {
/* 235 */       AppenderControl[] original = this.appenders.clear();
/* 236 */       for (AppenderControl ctl : original) {
/* 237 */         cleanupFilter(ctl);
/*     */       }
/* 239 */     } while (!this.appenders.isEmpty());
/*     */   }
/*     */   
/*     */   private void cleanupFilter(AppenderControl ctl) {
/* 243 */     Filter filter = ctl.getFilter();
/* 244 */     if (filter != null) {
/* 245 */       ctl.removeFilter(filter);
/* 246 */       filter.stop();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AppenderRef> getAppenderRefs() {
/* 256 */     return this.appenderRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(Level level) {
/* 265 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 274 */     return (this.level == null) ? ((this.parent == null) ? Level.ERROR : this.parent.getLevel()) : this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogEventFactory getLogEventFactory() {
/* 283 */     return this.logEventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogEventFactory(LogEventFactory logEventFactory) {
/* 292 */     this.logEventFactory = logEventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/* 301 */     return this.additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 310 */     this.additive = additive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 320 */     return this.includeLocation;
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
/*     */   @Deprecated
/*     */   public Map<Property, Boolean> getProperties() {
/* 338 */     if (this.properties == null) {
/* 339 */       return null;
/*     */     }
/* 341 */     if (this.propertiesMap == null) {
/* 342 */       Map<Property, Boolean> result = new HashMap<>(this.properties.size() * 2);
/* 343 */       for (int i = 0; i < this.properties.size(); i++) {
/* 344 */         result.put(this.properties.get(i), Boolean.valueOf(((Property)this.properties.get(i)).isValueNeedsLookup()));
/*     */       }
/* 346 */       this.propertiesMap = Collections.unmodifiableMap(result);
/*     */     } 
/* 348 */     return this.propertiesMap;
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
/*     */   public List<Property> getPropertyList() {
/* 364 */     return this.properties;
/*     */   }
/*     */   
/*     */   public boolean isPropertiesRequireLookup() {
/* 368 */     return this.propertiesRequireLookup;
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
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public void log(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/* 384 */     List<Property> props = getProperties(loggerName, fqcn, marker, level, data, t);
/* 385 */     LogEvent logEvent = this.logEventFactory.createEvent(loggerName, marker, fqcn, 
/* 386 */         location(fqcn), level, data, props, t);
/*     */     try {
/* 388 */       log(logEvent, LoggerConfigPredicate.ALL);
/*     */     } finally {
/*     */       
/* 391 */       ReusableLogEventFactory.release(logEvent);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StackTraceElement location(String fqcn) {
/* 396 */     return requiresLocation() ? 
/* 397 */       StackLocatorUtil.calcLocation(fqcn) : null;
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
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public void log(String loggerName, String fqcn, StackTraceElement location, Marker marker, Level level, Message data, Throwable t) {
/* 414 */     List<Property> props = getProperties(loggerName, fqcn, marker, level, data, t);
/* 415 */     LogEvent logEvent = this.logEventFactory.createEvent(loggerName, marker, fqcn, location, level, data, props, t);
/*     */     try {
/* 417 */       log(logEvent, LoggerConfigPredicate.ALL);
/*     */     } finally {
/*     */       
/* 420 */       ReusableLogEventFactory.release(logEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Property> getProperties(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t) {
/* 431 */     List<Property> snapshot = this.properties;
/* 432 */     if (snapshot == null || !this.propertiesRequireLookup) {
/* 433 */       return snapshot;
/*     */     }
/* 435 */     return getPropertiesWithLookups(loggerName, fqcn, marker, level, data, t, snapshot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Property> getPropertiesWithLookups(String loggerName, String fqcn, Marker marker, Level level, Message data, Throwable t, List<Property> props) {
/* 446 */     List<Property> results = new ArrayList<>(props.size());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 454 */     Log4jLogEvent log4jLogEvent = Log4jLogEvent.newBuilder().setMessage(data).setMarker(marker).setLevel(level).setLoggerName(loggerName).setLoggerFqcn(fqcn).setThrown(t).build();
/* 455 */     for (int i = 0; i < props.size(); i++) {
/* 456 */       Property prop = props.get(i);
/*     */ 
/*     */       
/* 459 */       String value = prop.isValueNeedsLookup() ? this.config.getStrSubstitutor().replace((LogEvent)log4jLogEvent, prop.getValue()) : prop.getValue();
/* 460 */       results.add(Property.createProperty(prop.getName(), value));
/*     */     } 
/* 462 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(LogEvent event) {
/* 471 */     log(event, LoggerConfigPredicate.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(LogEvent event, LoggerConfigPredicate predicate) {
/* 482 */     if (!isFiltered(event)) {
/* 483 */       processLogEvent(event, predicate);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReliabilityStrategy getReliabilityStrategy() {
/* 494 */     return this.reliabilityStrategy;
/*     */   }
/*     */   
/*     */   private void processLogEvent(LogEvent event, LoggerConfigPredicate predicate) {
/* 498 */     event.setIncludeLocation(isIncludeLocation());
/* 499 */     if (predicate.allow(this)) {
/* 500 */       callAppenders(event);
/*     */     }
/* 502 */     logParent(event, predicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 507 */     if (!this.includeLocation) {
/* 508 */       return false;
/*     */     }
/* 510 */     AppenderControl[] controls = this.appenders.get();
/* 511 */     LoggerConfig loggerConfig = this;
/* 512 */     while (loggerConfig != null) {
/* 513 */       for (AppenderControl control : controls) {
/* 514 */         Appender appender = control.getAppender();
/* 515 */         if (appender instanceof LocationAware && ((LocationAware)appender).requiresLocation()) {
/* 516 */           return true;
/*     */         }
/*     */       } 
/* 519 */       if (loggerConfig.additive) {
/* 520 */         loggerConfig = loggerConfig.parent;
/* 521 */         if (loggerConfig != null) {
/* 522 */           controls = loggerConfig.appenders.get();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 528 */     return false;
/*     */   }
/*     */   
/*     */   private void logParent(LogEvent event, LoggerConfigPredicate predicate) {
/* 532 */     if (this.additive && this.parent != null) {
/* 533 */       this.parent.log(event, predicate);
/*     */     }
/*     */   }
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   protected void callAppenders(LogEvent event) {
/* 539 */     AppenderControl[] controls = this.appenders.get();
/*     */     
/* 541 */     for (int i = 0; i < controls.length; i++) {
/* 542 */       controls[i].callAppender(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 548 */     return Strings.isEmpty(this.name) ? "root" : this.name;
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
/*     */   public static LoggerConfig createLogger(String additivity, Level level, @PluginAttribute("name") String loggerName, String includeLocation, AppenderRef[] refs, Property[] properties, @PluginConfiguration Configuration config, Filter filter) {
/* 576 */     if (loggerName == null) {
/* 577 */       LOGGER.error("Loggers cannot be configured without a name");
/* 578 */       return null;
/*     */     } 
/*     */     
/* 581 */     List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 582 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 583 */     boolean additive = Booleans.parseBoolean(additivity, true);
/*     */     
/* 585 */     return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, 
/* 586 */         includeLocation(includeLocation, config));
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
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static LoggerConfig createLogger(@PluginAttribute(value = "additivity", defaultBoolean = true) boolean additivity, @PluginAttribute("level") Level level, @Required(message = "Loggers cannot be configured without a name") @PluginAttribute("name") String loggerName, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 616 */     String name = loggerName.equals("root") ? "" : loggerName;
/* 617 */     return new LoggerConfig(name, Arrays.asList(refs), filter, level, additivity, properties, config, 
/* 618 */         includeLocation(includeLocation, config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected static boolean includeLocation(String includeLocationConfigValue) {
/* 626 */     return includeLocation(includeLocationConfigValue, (Configuration)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean includeLocation(String includeLocationConfigValue, Configuration configuration) {
/* 632 */     if (includeLocationConfigValue == null) {
/* 633 */       LoggerContext context = null;
/* 634 */       if (configuration != null) {
/* 635 */         context = configuration.getLoggerContext();
/*     */       }
/* 637 */       if (context != null) {
/* 638 */         return !(context instanceof org.apache.logging.log4j.core.async.AsyncLoggerContext);
/*     */       }
/* 640 */       return !AsyncLoggerContextSelector.isSelected();
/*     */     } 
/*     */     
/* 643 */     return Boolean.parseBoolean(includeLocationConfigValue);
/*     */   }
/*     */   
/*     */   protected final boolean hasAppenders() {
/* 647 */     return !this.appenders.isEmpty();
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
/*     */   @Plugin(name = "root", category = "Core", printObject = true)
/*     */   public static class RootLogger
/*     */     extends LoggerConfig
/*     */   {
/*     */     @PluginFactory
/*     */     public static LoggerConfig createLogger(@PluginAttribute("additivity") String additivity, @PluginAttribute("level") Level level, @PluginAttribute("includeLocation") String includeLocation, @PluginElement("AppenderRef") AppenderRef[] refs, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter) {
/* 667 */       List<AppenderRef> appenderRefs = Arrays.asList(refs);
/* 668 */       Level actualLevel = (level == null) ? Level.ERROR : level;
/* 669 */       boolean additive = Booleans.parseBoolean(additivity, true);
/*     */       
/* 671 */       return new LoggerConfig("", appenderRefs, filter, actualLevel, additive, properties, config, 
/* 672 */           includeLocation(includeLocation, config));
/*     */     }
/*     */   }
/*     */   
/*     */   protected enum LoggerConfigPredicate {
/* 677 */     ALL
/*     */     {
/*     */       boolean allow(LoggerConfig config) {
/* 680 */         return true;
/*     */       }
/*     */     },
/* 683 */     ASYNCHRONOUS_ONLY
/*     */     {
/*     */       boolean allow(LoggerConfig config) {
/* 686 */         return config instanceof org.apache.logging.log4j.core.async.AsyncLoggerConfig;
/*     */       }
/*     */     },
/* 689 */     SYNCHRONOUS_ONLY
/*     */     {
/*     */       boolean allow(LoggerConfig config) {
/* 692 */         return !ASYNCHRONOUS_ONLY.allow(config);
/*     */       }
/*     */     };
/*     */     
/*     */     abstract boolean allow(LoggerConfig param1LoggerConfig);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\LoggerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */