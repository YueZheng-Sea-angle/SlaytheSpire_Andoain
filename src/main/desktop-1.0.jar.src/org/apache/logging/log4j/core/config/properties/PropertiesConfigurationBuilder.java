/*     */ package org.apache.logging.log4j.core.config.properties;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationException;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
/*     */ import org.apache.logging.log4j.core.util.Builder;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertiesConfigurationBuilder
/*     */   extends ConfigurationBuilderFactory
/*     */   implements Builder<PropertiesConfiguration>
/*     */ {
/*     */   private static final String ADVERTISER_KEY = "advertiser";
/*     */   private static final String STATUS_KEY = "status";
/*     */   private static final String SHUTDOWN_HOOK = "shutdownHook";
/*     */   private static final String SHUTDOWN_TIMEOUT = "shutdownTimeout";
/*     */   private static final String VERBOSE = "verbose";
/*     */   private static final String DEST = "dest";
/*     */   private static final String PACKAGES = "packages";
/*     */   private static final String CONFIG_NAME = "name";
/*     */   private static final String MONITOR_INTERVAL = "monitorInterval";
/*     */   private static final String CONFIG_TYPE = "type";
/*  72 */   private final ConfigurationBuilder<PropertiesConfiguration> builder = newConfigurationBuilder(PropertiesConfiguration.class);
/*     */   private LoggerContext loggerContext;
/*     */   
/*     */   public PropertiesConfigurationBuilder setRootProperties(Properties rootProperties) {
/*  76 */     this.rootProperties = rootProperties;
/*  77 */     return this;
/*     */   }
/*     */   private Properties rootProperties;
/*     */   public PropertiesConfigurationBuilder setConfigurationSource(ConfigurationSource source) {
/*  81 */     this.builder.setConfigurationSource(source);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertiesConfiguration build() {
/*  87 */     for (String key : this.rootProperties.stringPropertyNames()) {
/*  88 */       if (!key.contains(".")) {
/*  89 */         this.builder.addRootProperty(key, this.rootProperties.getProperty(key));
/*     */       }
/*     */     } 
/*  92 */     this.builder
/*  93 */       .setStatusLevel(Level.toLevel(this.rootProperties.getProperty("status"), Level.ERROR))
/*  94 */       .setShutdownHook(this.rootProperties.getProperty("shutdownHook"))
/*  95 */       .setShutdownTimeout(Long.parseLong(this.rootProperties.getProperty("shutdownTimeout", "0")), TimeUnit.MILLISECONDS)
/*  96 */       .setVerbosity(this.rootProperties.getProperty("verbose"))
/*  97 */       .setDestination(this.rootProperties.getProperty("dest"))
/*  98 */       .setPackages(this.rootProperties.getProperty("packages"))
/*  99 */       .setConfigurationName(this.rootProperties.getProperty("name"))
/* 100 */       .setMonitorInterval(this.rootProperties.getProperty("monitorInterval", "0"))
/* 101 */       .setAdvertiser(this.rootProperties.getProperty("advertiser"));
/*     */     
/* 103 */     Properties propertyPlaceholders = PropertiesUtil.extractSubset(this.rootProperties, "property");
/* 104 */     for (String key : propertyPlaceholders.stringPropertyNames()) {
/* 105 */       this.builder.addProperty(key, propertyPlaceholders.getProperty(key));
/*     */     }
/*     */     
/* 108 */     Map<String, Properties> scripts = PropertiesUtil.partitionOnCommonPrefixes(
/* 109 */         PropertiesUtil.extractSubset(this.rootProperties, "script"));
/* 110 */     for (Map.Entry<String, Properties> entry : scripts.entrySet()) {
/* 111 */       Properties scriptProps = entry.getValue();
/* 112 */       String type = (String)scriptProps.remove("type");
/* 113 */       if (type == null) {
/* 114 */         throw new ConfigurationException("No type provided for script - must be Script or ScriptFile");
/*     */       }
/* 116 */       if (type.equalsIgnoreCase("script")) {
/* 117 */         this.builder.add(createScript(scriptProps)); continue;
/*     */       } 
/* 119 */       this.builder.add(createScriptFile(scriptProps));
/*     */     } 
/*     */ 
/*     */     
/* 123 */     Properties levelProps = PropertiesUtil.extractSubset(this.rootProperties, "customLevel");
/* 124 */     if (levelProps.size() > 0) {
/* 125 */       for (String key : levelProps.stringPropertyNames()) {
/* 126 */         this.builder.add(this.builder.newCustomLevel(key, Integer.parseInt(levelProps.getProperty(key))));
/*     */       }
/*     */     }
/*     */     
/* 130 */     String filterProp = this.rootProperties.getProperty("filters");
/* 131 */     if (filterProp != null) {
/* 132 */       String[] filterNames = filterProp.split(",");
/* 133 */       for (String filterName : filterNames) {
/* 134 */         String name = filterName.trim();
/* 135 */         this.builder.add(createFilter(name, PropertiesUtil.extractSubset(this.rootProperties, "filter." + name)));
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 140 */       Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "filter"));
/* 141 */       for (Map.Entry<String, Properties> entry : filters.entrySet()) {
/* 142 */         this.builder.add(createFilter(((String)entry.getKey()).trim(), entry.getValue()));
/*     */       }
/*     */     } 
/*     */     
/* 146 */     String appenderProp = this.rootProperties.getProperty("appenders");
/* 147 */     if (appenderProp != null) {
/* 148 */       String[] appenderNames = appenderProp.split(",");
/* 149 */       for (String appenderName : appenderNames) {
/* 150 */         String name = appenderName.trim();
/* 151 */         this.builder.add(createAppender(appenderName.trim(), 
/* 152 */               PropertiesUtil.extractSubset(this.rootProperties, "appender." + name)));
/*     */       } 
/*     */     } else {
/*     */       
/* 156 */       Map<String, Properties> appenders = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "appender"));
/* 157 */       for (Map.Entry<String, Properties> entry : appenders.entrySet()) {
/* 158 */         this.builder.add(createAppender(((String)entry.getKey()).trim(), entry.getValue()));
/*     */       }
/*     */     } 
/*     */     
/* 162 */     String loggerProp = this.rootProperties.getProperty("loggers");
/* 163 */     if (loggerProp != null) {
/* 164 */       String[] loggerNames = loggerProp.split(",");
/* 165 */       for (String loggerName : loggerNames) {
/* 166 */         String name = loggerName.trim();
/* 167 */         if (!name.equals("root")) {
/* 168 */           this.builder.add(createLogger(name, PropertiesUtil.extractSubset(this.rootProperties, "logger." + name)));
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 174 */       Map<String, Properties> loggers = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(this.rootProperties, "logger"));
/* 175 */       for (Map.Entry<String, Properties> entry : loggers.entrySet()) {
/* 176 */         String name = ((String)entry.getKey()).trim();
/* 177 */         if (!name.equals("root")) {
/* 178 */           this.builder.add(createLogger(name, entry.getValue()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 183 */     Properties props = PropertiesUtil.extractSubset(this.rootProperties, "rootLogger");
/* 184 */     if (props.size() > 0) {
/* 185 */       this.builder.add(createRootLogger(props));
/*     */     }
/*     */     
/* 188 */     this.builder.setLoggerContext(this.loggerContext);
/*     */     
/* 190 */     return (PropertiesConfiguration)this.builder.build(false);
/*     */   }
/*     */   
/*     */   private ScriptComponentBuilder createScript(Properties properties) {
/* 194 */     String name = (String)properties.remove("name");
/* 195 */     String language = (String)properties.remove("language");
/* 196 */     String text = (String)properties.remove("text");
/* 197 */     ScriptComponentBuilder scriptBuilder = this.builder.newScript(name, language, text);
/* 198 */     return processRemainingProperties(scriptBuilder, properties);
/*     */   }
/*     */ 
/*     */   
/*     */   private ScriptFileComponentBuilder createScriptFile(Properties properties) {
/* 203 */     String name = (String)properties.remove("name");
/* 204 */     String path = (String)properties.remove("path");
/* 205 */     ScriptFileComponentBuilder scriptFileBuilder = this.builder.newScriptFile(name, path);
/* 206 */     return processRemainingProperties(scriptFileBuilder, properties);
/*     */   }
/*     */   
/*     */   private AppenderComponentBuilder createAppender(String key, Properties properties) {
/* 210 */     String name = (String)properties.remove("name");
/* 211 */     if (Strings.isEmpty(name)) {
/* 212 */       throw new ConfigurationException("No name attribute provided for Appender " + key);
/*     */     }
/* 214 */     String type = (String)properties.remove("type");
/* 215 */     if (Strings.isEmpty(type)) {
/* 216 */       throw new ConfigurationException("No type attribute provided for Appender " + key);
/*     */     }
/* 218 */     AppenderComponentBuilder appenderBuilder = this.builder.newAppender(name, type);
/* 219 */     addFiltersToComponent(appenderBuilder, properties);
/* 220 */     Properties layoutProps = PropertiesUtil.extractSubset(properties, "layout");
/* 221 */     if (layoutProps.size() > 0) {
/* 222 */       appenderBuilder.add(createLayout(name, layoutProps));
/*     */     }
/*     */     
/* 225 */     return processRemainingProperties(appenderBuilder, properties);
/*     */   }
/*     */   
/*     */   private FilterComponentBuilder createFilter(String key, Properties properties) {
/* 229 */     String type = (String)properties.remove("type");
/* 230 */     if (Strings.isEmpty(type)) {
/* 231 */       throw new ConfigurationException("No type attribute provided for Filter " + key);
/*     */     }
/* 233 */     String onMatch = (String)properties.remove("onMatch");
/* 234 */     String onMismatch = (String)properties.remove("onMismatch");
/* 235 */     FilterComponentBuilder filterBuilder = this.builder.newFilter(type, onMatch, onMismatch);
/* 236 */     return processRemainingProperties(filterBuilder, properties);
/*     */   }
/*     */   
/*     */   private AppenderRefComponentBuilder createAppenderRef(String key, Properties properties) {
/* 240 */     String ref = (String)properties.remove("ref");
/* 241 */     if (Strings.isEmpty(ref)) {
/* 242 */       throw new ConfigurationException("No ref attribute provided for AppenderRef " + key);
/*     */     }
/* 244 */     AppenderRefComponentBuilder appenderRefBuilder = this.builder.newAppenderRef(ref);
/* 245 */     String level = Strings.trimToNull((String)properties.remove("level"));
/* 246 */     if (!Strings.isEmpty(level)) {
/* 247 */       appenderRefBuilder.addAttribute("level", level);
/*     */     }
/* 249 */     return addFiltersToComponent(appenderRefBuilder, properties);
/*     */   }
/*     */   private LoggerComponentBuilder createLogger(String key, Properties properties) {
/*     */     LoggerComponentBuilder loggerBuilder;
/* 253 */     String name = (String)properties.remove("name");
/* 254 */     String location = (String)properties.remove("includeLocation");
/* 255 */     if (Strings.isEmpty(name)) {
/* 256 */       throw new ConfigurationException("No name attribute provided for Logger " + key);
/*     */     }
/* 258 */     String level = Strings.trimToNull((String)properties.remove("level"));
/* 259 */     String type = (String)properties.remove("type");
/*     */ 
/*     */     
/* 262 */     if (type != null) {
/* 263 */       if (type.equalsIgnoreCase("asyncLogger")) {
/* 264 */         if (location != null) {
/* 265 */           boolean includeLocation = Boolean.parseBoolean(location);
/* 266 */           loggerBuilder = this.builder.newAsyncLogger(name, level, includeLocation);
/*     */         } else {
/* 268 */           loggerBuilder = this.builder.newAsyncLogger(name, level);
/*     */         } 
/*     */       } else {
/* 271 */         throw new ConfigurationException("Unknown Logger type " + type + " for Logger " + name);
/*     */       } 
/* 273 */     } else if (location != null) {
/* 274 */       boolean includeLocation = Boolean.parseBoolean(location);
/* 275 */       loggerBuilder = this.builder.newLogger(name, level, includeLocation);
/*     */     } else {
/* 277 */       loggerBuilder = this.builder.newLogger(name, level);
/*     */     } 
/* 279 */     addLoggersToComponent(loggerBuilder, properties);
/* 280 */     addFiltersToComponent(loggerBuilder, properties);
/* 281 */     String additivity = (String)properties.remove("additivity");
/* 282 */     if (!Strings.isEmpty(additivity)) {
/* 283 */       loggerBuilder.addAttribute("additivity", additivity);
/*     */     }
/* 285 */     return loggerBuilder;
/*     */   }
/*     */   private RootLoggerComponentBuilder createRootLogger(Properties properties) {
/*     */     RootLoggerComponentBuilder loggerBuilder;
/* 289 */     String level = Strings.trimToNull((String)properties.remove("level"));
/* 290 */     String type = (String)properties.remove("type");
/* 291 */     String location = (String)properties.remove("includeLocation");
/*     */ 
/*     */     
/* 294 */     if (type != null) {
/* 295 */       if (type.equalsIgnoreCase("asyncRoot")) {
/* 296 */         if (location != null) {
/* 297 */           boolean includeLocation = Boolean.parseBoolean(location);
/* 298 */           loggerBuilder = this.builder.newAsyncRootLogger(level, includeLocation);
/*     */         } else {
/* 300 */           loggerBuilder = this.builder.newAsyncRootLogger(level);
/*     */         } 
/*     */       } else {
/* 303 */         throw new ConfigurationException("Unknown Logger type for root logger" + type);
/*     */       } 
/* 305 */     } else if (location != null) {
/* 306 */       boolean includeLocation = Boolean.parseBoolean(location);
/* 307 */       loggerBuilder = this.builder.newRootLogger(level, includeLocation);
/*     */     } else {
/* 309 */       loggerBuilder = this.builder.newRootLogger(level);
/*     */     } 
/* 311 */     addLoggersToComponent(loggerBuilder, properties);
/* 312 */     return addFiltersToComponent(loggerBuilder, properties);
/*     */   }
/*     */   
/*     */   private LayoutComponentBuilder createLayout(String appenderName, Properties properties) {
/* 316 */     String type = (String)properties.remove("type");
/* 317 */     if (Strings.isEmpty(type)) {
/* 318 */       throw new ConfigurationException("No type attribute provided for Layout on Appender " + appenderName);
/*     */     }
/* 320 */     LayoutComponentBuilder layoutBuilder = this.builder.newLayout(type);
/* 321 */     return processRemainingProperties(layoutBuilder, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <B extends ComponentBuilder<B>> ComponentBuilder<B> createComponent(ComponentBuilder<?> parent, String key, Properties properties) {
/* 327 */     String name = (String)properties.remove("name");
/* 328 */     String type = (String)properties.remove("type");
/* 329 */     if (Strings.isEmpty(type)) {
/* 330 */       throw new ConfigurationException("No type attribute provided for component " + key);
/*     */     }
/* 332 */     ComponentBuilder<B> componentBuilder = parent.getBuilder().newComponent(name, type);
/* 333 */     return processRemainingProperties(componentBuilder, properties);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <B extends ComponentBuilder<?>> B processRemainingProperties(B builder, Properties properties) {
/* 338 */     while (properties.size() > 0) {
/* 339 */       String propertyName = properties.stringPropertyNames().iterator().next();
/* 340 */       int index = propertyName.indexOf('.');
/* 341 */       if (index > 0) {
/* 342 */         String prefix = propertyName.substring(0, index);
/* 343 */         Properties componentProperties = PropertiesUtil.extractSubset(properties, prefix);
/* 344 */         builder.addComponent(createComponent((ComponentBuilder<?>)builder, prefix, componentProperties)); continue;
/*     */       } 
/* 346 */       builder.addAttribute(propertyName, properties.getProperty(propertyName));
/* 347 */       properties.remove(propertyName);
/*     */     } 
/*     */     
/* 350 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <B extends org.apache.logging.log4j.core.config.builder.api.FilterableComponentBuilder<? extends ComponentBuilder<?>>> B addFiltersToComponent(B componentBuilder, Properties properties) {
/* 355 */     Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(
/* 356 */         PropertiesUtil.extractSubset(properties, "filter"));
/* 357 */     for (Map.Entry<String, Properties> entry : filters.entrySet()) {
/* 358 */       componentBuilder.add(createFilter(((String)entry.getKey()).trim(), entry.getValue()));
/*     */     }
/* 360 */     return componentBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   private <B extends org.apache.logging.log4j.core.config.builder.api.LoggableComponentBuilder<? extends ComponentBuilder<?>>> B addLoggersToComponent(B loggerBuilder, Properties properties) {
/* 365 */     Map<String, Properties> appenderRefs = PropertiesUtil.partitionOnCommonPrefixes(
/* 366 */         PropertiesUtil.extractSubset(properties, "appenderRef"));
/* 367 */     for (Map.Entry<String, Properties> entry : appenderRefs.entrySet()) {
/* 368 */       loggerBuilder.add(createAppenderRef(((String)entry.getKey()).trim(), entry.getValue()));
/*     */     }
/* 370 */     return loggerBuilder;
/*     */   }
/*     */   
/*     */   public PropertiesConfigurationBuilder setLoggerContext(LoggerContext loggerContext) {
/* 374 */     this.loggerContext = loggerContext;
/* 375 */     return this;
/*     */   }
/*     */   
/*     */   public LoggerContext getLoggerContext() {
/* 379 */     return this.loggerContext;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\properties\PropertiesConfigurationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */