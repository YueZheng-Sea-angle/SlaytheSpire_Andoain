/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.lookup.ConfigurationStrSubstitutor;
/*     */ import org.apache.logging.log4j.core.lookup.Interpolator;
/*     */ import org.apache.logging.log4j.core.lookup.StrLookup;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.net.UrlConnectionFactory;
/*     */ import org.apache.logging.log4j.core.util.AuthorizationProvider;
/*     */ import org.apache.logging.log4j.core.util.BasicAuthorizationProvider;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConfigurationFactory
/*     */   extends ConfigurationBuilderFactory
/*     */ {
/*     */   public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
/*     */   public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
/*     */   public static final String LOG4J1_CONFIGURATION_FILE_PROPERTY = "log4j.configuration";
/*     */   public static final String LOG4J1_EXPERIMENTAL = "log4j1.compatibility";
/*     */   public static final String AUTHORIZATION_PROVIDER = "log4j2.authorizationProvider";
/*     */   public static final String CATEGORY = "ConfigurationFactory";
/* 114 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String TEST_PREFIX = "log4j2-test";
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String DEFAULT_PREFIX = "log4j2";
/*     */ 
/*     */   
/*     */   protected static final String LOG4J1_VERSION = "1";
/*     */ 
/*     */   
/*     */   protected static final String LOG4J2_VERSION = "2";
/*     */ 
/*     */   
/*     */   private static final String CLASS_LOADER_SCHEME = "classloader";
/*     */ 
/*     */   
/*     */   private static final String CLASS_PATH_SCHEME = "classpath";
/*     */ 
/*     */   
/*     */   private static final String OVERRIDE_PARAM = "override";
/*     */ 
/*     */   
/*     */   private static volatile List<ConfigurationFactory> factories;
/*     */ 
/*     */   
/* 143 */   private static ConfigurationFactory configFactory = new Factory();
/*     */   
/* 145 */   protected final StrSubstitutor substitutor = (StrSubstitutor)new ConfigurationStrSubstitutor((StrLookup)new Interpolator());
/*     */   
/* 147 */   private static final Lock LOCK = new ReentrantLock();
/*     */ 
/*     */   
/*     */   private static final String HTTPS = "https";
/*     */ 
/*     */   
/*     */   private static final String HTTP = "http";
/*     */ 
/*     */   
/*     */   private static volatile AuthorizationProvider authorizationProvider;
/*     */ 
/*     */ 
/*     */   
/*     */   public static ConfigurationFactory getInstance() {
/* 161 */     if (factories == null) {
/* 162 */       LOCK.lock();
/*     */       try {
/* 164 */         if (factories == null) {
/* 165 */           List<ConfigurationFactory> list = new ArrayList<>();
/* 166 */           PropertiesUtil props = PropertiesUtil.getProperties();
/* 167 */           String factoryClass = props.getStringProperty("log4j.configurationFactory");
/* 168 */           if (factoryClass != null) {
/* 169 */             addFactory(list, factoryClass);
/*     */           }
/* 171 */           PluginManager manager = new PluginManager("ConfigurationFactory");
/* 172 */           manager.collectPlugins();
/* 173 */           Map<String, PluginType<?>> plugins = manager.getPlugins();
/* 174 */           List<Class<? extends ConfigurationFactory>> ordered = new ArrayList<>(plugins.size());
/* 175 */           for (PluginType<?> type : plugins.values()) {
/*     */             try {
/* 177 */               ordered.add(type.getPluginClass().asSubclass(ConfigurationFactory.class));
/* 178 */             } catch (Exception ex) {
/* 179 */               LOGGER.warn("Unable to add class {}", type.getPluginClass(), ex);
/*     */             } 
/*     */           } 
/* 182 */           Collections.sort(ordered, OrderComparator.getInstance());
/* 183 */           for (Class<? extends ConfigurationFactory> clazz : ordered) {
/* 184 */             addFactory(list, clazz);
/*     */           }
/*     */ 
/*     */           
/* 188 */           factories = Collections.unmodifiableList(list);
/* 189 */           authorizationProvider = authorizationProvider(props);
/*     */         } 
/*     */       } finally {
/* 192 */         LOCK.unlock();
/*     */       } 
/*     */     } 
/*     */     
/* 196 */     LOGGER.debug("Using configurationFactory {}", configFactory);
/* 197 */     return configFactory;
/*     */   }
/*     */   public static AuthorizationProvider authorizationProvider(PropertiesUtil props) {
/*     */     BasicAuthorizationProvider basicAuthorizationProvider;
/* 201 */     String authClass = props.getStringProperty("log4j2.authorizationProvider");
/* 202 */     AuthorizationProvider provider = null;
/* 203 */     if (authClass != null) {
/*     */       try {
/* 205 */         Object obj = LoaderUtil.newInstanceOf(authClass);
/* 206 */         if (obj instanceof AuthorizationProvider) {
/* 207 */           provider = (AuthorizationProvider)obj;
/*     */         } else {
/* 209 */           LOGGER.warn("{} is not an AuthorizationProvider, using default", obj.getClass().getName());
/*     */         } 
/* 211 */       } catch (Exception ex) {
/* 212 */         LOGGER.warn("Unable to create {}, using default: {}", authClass, ex.getMessage());
/*     */       } 
/*     */     }
/* 215 */     if (provider == null) {
/* 216 */       basicAuthorizationProvider = new BasicAuthorizationProvider(props);
/*     */     }
/* 218 */     return (AuthorizationProvider)basicAuthorizationProvider;
/*     */   }
/*     */   
/*     */   public static AuthorizationProvider getAuthorizationProvider() {
/* 222 */     return authorizationProvider;
/*     */   }
/*     */   
/*     */   private static void addFactory(Collection<ConfigurationFactory> list, String factoryClass) {
/*     */     try {
/* 227 */       addFactory(list, Loader.loadClass(factoryClass).asSubclass(ConfigurationFactory.class));
/* 228 */     } catch (Exception ex) {
/* 229 */       LOGGER.error("Unable to load class {}", factoryClass, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addFactory(Collection<ConfigurationFactory> list, Class<? extends ConfigurationFactory> factoryClass) {
/*     */     try {
/* 236 */       list.add(ReflectionUtil.instantiate(factoryClass));
/* 237 */     } catch (Exception ex) {
/* 238 */       LOGGER.error("Unable to create instance of {}", factoryClass.getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setConfigurationFactory(ConfigurationFactory factory) {
/* 247 */     configFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetConfigurationFactory() {
/* 255 */     configFactory = new Factory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeConfigurationFactory(ConfigurationFactory factory) {
/* 263 */     if (configFactory == factory) {
/* 264 */       configFactory = new Factory();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract String[] getSupportedTypes();
/*     */   
/*     */   protected String getTestPrefix() {
/* 271 */     return "log4j2-test";
/*     */   }
/*     */   
/*     */   protected String getDefaultPrefix() {
/* 275 */     return "log4j2";
/*     */   }
/*     */   
/*     */   protected String getVersion() {
/* 279 */     return "2";
/*     */   }
/*     */   
/*     */   protected boolean isActive() {
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Configuration getConfiguration(LoggerContext paramLoggerContext, ConfigurationSource paramConfigurationSource);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
/* 296 */     if (!isActive()) {
/* 297 */       return null;
/*     */     }
/* 299 */     if (configLocation != null) {
/* 300 */       ConfigurationSource source = ConfigurationSource.fromUri(configLocation);
/* 301 */       if (source != null) {
/* 302 */         return getConfiguration(loggerContext, source);
/*     */       }
/*     */     } 
/* 305 */     return null;
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
/*     */   public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation, ClassLoader loader) {
/* 319 */     if (!isActive()) {
/* 320 */       return null;
/*     */     }
/* 322 */     if (loader == null) {
/* 323 */       return getConfiguration(loggerContext, name, configLocation);
/*     */     }
/* 325 */     if (isClassLoaderUri(configLocation)) {
/* 326 */       String path = extractClassLoaderUriPath(configLocation);
/* 327 */       ConfigurationSource source = ConfigurationSource.fromResource(path, loader);
/* 328 */       if (source != null) {
/* 329 */         Configuration configuration = getConfiguration(loggerContext, source);
/* 330 */         if (configuration != null) {
/* 331 */           return configuration;
/*     */         }
/*     */       } 
/*     */     } 
/* 335 */     return getConfiguration(loggerContext, name, configLocation);
/*     */   }
/*     */   
/*     */   static boolean isClassLoaderUri(URI uri) {
/* 339 */     if (uri == null) {
/* 340 */       return false;
/*     */     }
/* 342 */     String scheme = uri.getScheme();
/* 343 */     return (scheme == null || scheme.equals("classloader") || scheme.equals("classpath"));
/*     */   }
/*     */   
/*     */   static String extractClassLoaderUriPath(URI uri) {
/* 347 */     return (uri.getScheme() == null) ? uri.getPath() : uri.getSchemeSpecificPart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConfigurationSource getInputFromString(String config, ClassLoader loader) {
/*     */     try {
/* 358 */       URL url = new URL(config);
/* 359 */       URLConnection urlConnection = UrlConnectionFactory.createConnection(url);
/* 360 */       File file = FileUtils.fileFromUri(url.toURI());
/* 361 */       if (file != null) {
/* 362 */         return new ConfigurationSource(urlConnection.getInputStream(), FileUtils.fileFromUri(url.toURI()));
/*     */       }
/* 364 */       return new ConfigurationSource(urlConnection.getInputStream(), url, urlConnection.getLastModified());
/* 365 */     } catch (Exception ex) {
/* 366 */       ConfigurationSource source = ConfigurationSource.fromResource(config, loader);
/* 367 */       if (source == null) {
/*     */         try {
/* 369 */           File file = new File(config);
/* 370 */           return new ConfigurationSource(new FileInputStream(file), file);
/* 371 */         } catch (FileNotFoundException fnfe) {
/*     */           
/* 373 */           LOGGER.catching(Level.DEBUG, fnfe);
/*     */         } 
/*     */       }
/* 376 */       return source;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Factory
/*     */     extends ConfigurationFactory
/*     */   {
/*     */     private static final String ALL_TYPES = "*";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Factory() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
/* 396 */       if (configLocation == null) {
/* 397 */         String configLocationStr = this.substitutor.replace(PropertiesUtil.getProperties()
/* 398 */             .getStringProperty("log4j.configurationFile"));
/* 399 */         if (configLocationStr != null) {
/* 400 */           String[] sources = parseConfigLocations(configLocationStr);
/* 401 */           if (sources.length > 1) {
/* 402 */             List<AbstractConfiguration> configs = new ArrayList<>();
/* 403 */             for (String sourceLocation : sources) {
/* 404 */               Configuration configuration = getConfiguration(loggerContext, sourceLocation.trim());
/* 405 */               if (configuration != null) {
/* 406 */                 if (configuration instanceof AbstractConfiguration) {
/* 407 */                   configs.add((AbstractConfiguration)configuration);
/*     */                 } else {
/* 409 */                   LOGGER.error("Failed to created configuration at {}", sourceLocation);
/* 410 */                   return null;
/*     */                 } 
/*     */               } else {
/* 413 */                 LOGGER.warn("Unable to create configuration for {}, ignoring", sourceLocation);
/*     */               } 
/*     */             } 
/* 416 */             if (configs.size() > 1)
/* 417 */               return (Configuration)new CompositeConfiguration(configs); 
/* 418 */             if (configs.size() == 1) {
/* 419 */               return configs.get(0);
/*     */             }
/*     */           } 
/* 422 */           return getConfiguration(loggerContext, configLocationStr);
/*     */         } 
/* 424 */         String log4j1ConfigStr = this.substitutor.replace(PropertiesUtil.getProperties()
/* 425 */             .getStringProperty("log4j.configuration"));
/* 426 */         if (log4j1ConfigStr != null) {
/* 427 */           System.setProperty("log4j1.compatibility", "true");
/* 428 */           return getConfiguration("1", loggerContext, log4j1ConfigStr);
/*     */         } 
/* 430 */         for (ConfigurationFactory factory : getFactories()) {
/* 431 */           String[] types = factory.getSupportedTypes();
/* 432 */           if (types != null) {
/* 433 */             for (String type : types) {
/* 434 */               if (type.equals("*")) {
/* 435 */                 Configuration configuration = factory.getConfiguration(loggerContext, name, configLocation);
/* 436 */                 if (configuration != null) {
/* 437 */                   return configuration;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } else {
/* 444 */         String[] sources = parseConfigLocations(configLocation);
/* 445 */         if (sources.length > 1) {
/* 446 */           List<AbstractConfiguration> configs = new ArrayList<>();
/* 447 */           for (String sourceLocation : sources) {
/* 448 */             Configuration configuration = getConfiguration(loggerContext, sourceLocation.trim());
/* 449 */             if (configuration instanceof AbstractConfiguration) {
/* 450 */               configs.add((AbstractConfiguration)configuration);
/*     */             } else {
/* 452 */               LOGGER.error("Failed to created configuration at {}", sourceLocation);
/* 453 */               return null;
/*     */             } 
/*     */           } 
/* 456 */           return (Configuration)new CompositeConfiguration(configs);
/*     */         } 
/*     */         
/* 459 */         String configLocationStr = configLocation.toString();
/* 460 */         for (ConfigurationFactory factory : getFactories()) {
/* 461 */           String[] types = factory.getSupportedTypes();
/* 462 */           if (types != null) {
/* 463 */             for (String type : types) {
/* 464 */               if (type.equals("*") || configLocationStr.endsWith(type)) {
/* 465 */                 Configuration configuration = factory.getConfiguration(loggerContext, name, configLocation);
/* 466 */                 if (configuration != null) {
/* 467 */                   return configuration;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 475 */       Configuration config = getConfiguration(loggerContext, true, name);
/* 476 */       if (config == null) {
/* 477 */         config = getConfiguration(loggerContext, true, (String)null);
/* 478 */         if (config == null) {
/* 479 */           config = getConfiguration(loggerContext, false, name);
/* 480 */           if (config == null) {
/* 481 */             config = getConfiguration(loggerContext, false, (String)null);
/*     */           }
/*     */         } 
/*     */       } 
/* 485 */       if (config != null) {
/* 486 */         return config;
/*     */       }
/* 488 */       LOGGER.warn("No Log4j 2 configuration file found. Using default configuration (logging only errors to the console), or user programmatically provided configurations. Set system property 'log4j2.debug' to show Log4j 2 internal initialization logging. See https://logging.apache.org/log4j/2.x/manual/configuration.html for instructions on how to configure Log4j 2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 494 */       return new DefaultConfiguration();
/*     */     }
/*     */     
/*     */     private Configuration getConfiguration(LoggerContext loggerContext, String configLocationStr) {
/* 498 */       return getConfiguration((String)null, loggerContext, configLocationStr);
/*     */     }
/*     */ 
/*     */     
/*     */     private Configuration getConfiguration(String requiredVersion, LoggerContext loggerContext, String configLocationStr) {
/* 503 */       ConfigurationSource source = null;
/*     */       try {
/* 505 */         source = ConfigurationSource.fromUri(NetUtils.toURI(configLocationStr));
/* 506 */       } catch (Exception ex) {
/*     */         
/* 508 */         LOGGER.catching(Level.DEBUG, ex);
/*     */       } 
/* 510 */       if (source == null) {
/* 511 */         ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/* 512 */         source = getInputFromString(configLocationStr, loader);
/*     */       } 
/* 514 */       if (source != null) {
/* 515 */         for (ConfigurationFactory factory : getFactories()) {
/* 516 */           if (requiredVersion != null && !factory.getVersion().equals(requiredVersion)) {
/*     */             continue;
/*     */           }
/* 519 */           String[] types = factory.getSupportedTypes();
/* 520 */           if (types != null) {
/* 521 */             for (String type : types) {
/* 522 */               if (type.equals("*") || configLocationStr.endsWith(type)) {
/* 523 */                 Configuration config = factory.getConfiguration(loggerContext, source);
/* 524 */                 if (config != null) {
/* 525 */                   return config;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       }
/* 532 */       return null;
/*     */     }
/*     */     
/*     */     private Configuration getConfiguration(LoggerContext loggerContext, boolean isTest, String name) {
/* 536 */       boolean named = Strings.isNotEmpty(name);
/* 537 */       ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
/* 538 */       for (ConfigurationFactory factory : getFactories()) {
/*     */         
/* 540 */         String prefix = isTest ? factory.getTestPrefix() : factory.getDefaultPrefix();
/* 541 */         String[] types = factory.getSupportedTypes();
/* 542 */         if (types == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 546 */         for (String suffix : types) {
/* 547 */           if (!suffix.equals("*")) {
/*     */ 
/*     */             
/* 550 */             String configName = named ? (prefix + name + suffix) : (prefix + suffix);
/*     */             
/* 552 */             ConfigurationSource source = ConfigurationSource.fromResource(configName, loader);
/* 553 */             if (source != null) {
/* 554 */               if (!factory.isActive()) {
/* 555 */                 LOGGER.warn("Found configuration file {} for inactive ConfigurationFactory {}", configName, factory.getClass().getName());
/*     */               }
/* 557 */               return factory.getConfiguration(loggerContext, source);
/*     */             } 
/*     */           } 
/*     */         } 
/* 561 */       }  return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getSupportedTypes() {
/* 566 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(LoggerContext loggerContext, ConfigurationSource source) {
/* 571 */       if (source != null) {
/* 572 */         String config = source.getLocation();
/* 573 */         for (ConfigurationFactory factory : getFactories()) {
/* 574 */           String[] types = factory.getSupportedTypes();
/* 575 */           if (types != null) {
/* 576 */             for (String type : types) {
/* 577 */               if (type.equals("*") || (config != null && config.endsWith(type))) {
/* 578 */                 Configuration c = factory.getConfiguration(loggerContext, source);
/* 579 */                 if (c != null) {
/* 580 */                   LOGGER.debug("Loaded configuration from {}", source);
/* 581 */                   return c;
/*     */                 } 
/* 583 */                 LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", config);
/* 584 */                 return null;
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 590 */       LOGGER.error("Cannot process configuration, input source is null");
/* 591 */       return null;
/*     */     }
/*     */     
/*     */     private String[] parseConfigLocations(URI configLocations) {
/* 595 */       String[] uris = configLocations.toString().split("\\?");
/* 596 */       List<String> locations = new ArrayList<>();
/* 597 */       if (uris.length > 1) {
/* 598 */         locations.add(uris[0]);
/* 599 */         String[] pairs = configLocations.getQuery().split("&");
/* 600 */         for (String pair : pairs) {
/* 601 */           int idx = pair.indexOf("=");
/*     */           try {
/* 603 */             String key = (idx > 0) ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
/* 604 */             if (key.equalsIgnoreCase("override")) {
/* 605 */               locations.add(URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
/*     */             }
/* 607 */           } catch (UnsupportedEncodingException ex) {
/* 608 */             LOGGER.warn("Invalid query parameter in {}", configLocations);
/*     */           } 
/*     */         } 
/* 611 */         return locations.<String>toArray(Strings.EMPTY_ARRAY);
/*     */       } 
/* 613 */       return new String[] { uris[0] };
/*     */     }
/*     */     
/*     */     private String[] parseConfigLocations(String configLocations) {
/* 617 */       String[] uris = configLocations.split(",");
/* 618 */       if (uris.length > 1) {
/* 619 */         return uris;
/*     */       }
/*     */       try {
/* 622 */         return parseConfigLocations(new URI(configLocations));
/* 623 */       } catch (URISyntaxException ex) {
/* 624 */         LOGGER.warn("Error parsing URI {}", configLocations);
/*     */         
/* 626 */         return new String[] { configLocations };
/*     */       } 
/*     */     } }
/*     */   
/*     */   static List<ConfigurationFactory> getFactories() {
/* 631 */     return factories;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ConfigurationFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */