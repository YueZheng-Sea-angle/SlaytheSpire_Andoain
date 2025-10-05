/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.time.Duration;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.time.temporal.TemporalUnit;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PropertiesUtil
/*     */ {
/*     */   private static final String LOG4J_PROPERTIES_FILE_NAME = "log4j2.component.properties";
/*     */   private static final String LOG4J_SYSTEM_PROPERTIES_FILE_NAME = "log4j2.system.properties";
/*     */   private static final String SYSTEM = "system:";
/*  54 */   private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
/*     */ 
/*     */ 
/*     */   
/*     */   private final Environment environment;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesUtil(Properties props) {
/*  64 */     this.environment = new Environment(new PropertiesPropertySource(props));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesUtil(String propertiesFileName) {
/*  74 */     this.environment = new Environment(new PropertyFilePropertySource(propertiesFileName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Properties loadClose(InputStream in, Object source) {
/*  85 */     Properties props = new Properties();
/*  86 */     if (null != in) {
/*     */       try {
/*  88 */         props.load(in);
/*  89 */       } catch (IOException e) {
/*  90 */         LowLevelLogUtil.logException("Unable to read " + source, e);
/*     */       } finally {
/*     */         try {
/*  93 */           in.close();
/*  94 */         } catch (IOException e) {
/*  95 */           LowLevelLogUtil.logException("Unable to close " + source, e);
/*     */         } 
/*     */       } 
/*     */     }
/*  99 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertiesUtil getProperties() {
/* 108 */     return LOG4J_PROPERTIES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasProperty(String name) {
/* 118 */     return this.environment.containsKey(name);
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
/*     */   public boolean getBooleanProperty(String name) {
/* 130 */     return getBooleanProperty(name, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBooleanProperty(String name, boolean defaultValue) {
/* 141 */     String prop = getStringProperty(name);
/* 142 */     return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
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
/*     */   public boolean getBooleanProperty(String name, boolean defaultValueIfAbsent, boolean defaultValueIfPresent) {
/* 155 */     String prop = getStringProperty(name);
/* 156 */     return (prop == null) ? defaultValueIfAbsent : (
/* 157 */       prop.isEmpty() ? defaultValueIfPresent : "true".equalsIgnoreCase(prop));
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
/*     */   public Boolean getBooleanProperty(String[] prefixes, String key, Supplier<Boolean> supplier) {
/* 170 */     for (String prefix : prefixes) {
/* 171 */       if (hasProperty(prefix + key)) {
/* 172 */         return Boolean.valueOf(getBooleanProperty(prefix + key));
/*     */       }
/*     */     } 
/* 175 */     return (supplier != null) ? supplier.get() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharsetProperty(String name) {
/* 185 */     return getCharsetProperty(name, Charset.defaultCharset());
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
/*     */   public Charset getCharsetProperty(String name, Charset defaultValue) {
/* 197 */     String charsetName = getStringProperty(name);
/* 198 */     if (charsetName == null) {
/* 199 */       return defaultValue;
/*     */     }
/* 201 */     if (Charset.isSupported(charsetName)) {
/* 202 */       return Charset.forName(charsetName);
/*     */     }
/* 204 */     ResourceBundle bundle = getCharsetsResourceBundle();
/* 205 */     if (bundle.containsKey(name)) {
/* 206 */       String mapped = bundle.getString(name);
/* 207 */       if (Charset.isSupported(mapped)) {
/* 208 */         return Charset.forName(mapped);
/*     */       }
/*     */     } 
/* 211 */     LowLevelLogUtil.log("Unable to get Charset '" + charsetName + "' for property '" + name + "', using default " + defaultValue + " and continuing.");
/*     */     
/* 213 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDoubleProperty(String name, double defaultValue) {
/* 224 */     String prop = getStringProperty(name);
/* 225 */     if (prop != null) {
/*     */       try {
/* 227 */         return Double.parseDouble(prop);
/* 228 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 231 */     return defaultValue;
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
/*     */   public int getIntegerProperty(String name, int defaultValue) {
/* 243 */     String prop = getStringProperty(name);
/* 244 */     if (prop != null) {
/*     */       try {
/* 246 */         return Integer.parseInt(prop);
/* 247 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 251 */     return defaultValue;
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
/*     */   public Integer getIntegerProperty(String[] prefixes, String key, Supplier<Integer> supplier) {
/* 264 */     for (String prefix : prefixes) {
/* 265 */       if (hasProperty(prefix + key)) {
/* 266 */         return Integer.valueOf(getIntegerProperty(prefix + key, 0));
/*     */       }
/*     */     } 
/* 269 */     return (supplier != null) ? supplier.get() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLongProperty(String name, long defaultValue) {
/* 280 */     String prop = getStringProperty(name);
/* 281 */     if (prop != null) {
/*     */       try {
/* 283 */         return Long.parseLong(prop);
/* 284 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 287 */     return defaultValue;
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
/*     */   public Long getLongProperty(String[] prefixes, String key, Supplier<Long> supplier) {
/* 300 */     for (String prefix : prefixes) {
/* 301 */       if (hasProperty(prefix + key)) {
/* 302 */         return Long.valueOf(getLongProperty(prefix + key, 0L));
/*     */       }
/*     */     } 
/* 305 */     return (supplier != null) ? supplier.get() : null;
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
/*     */   public Duration getDurationProperty(String name, Duration defaultValue) {
/* 317 */     String prop = getStringProperty(name);
/* 318 */     if (prop != null) {
/* 319 */       return TimeUnit.getDuration(prop);
/*     */     }
/* 321 */     return defaultValue;
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
/*     */   public Duration getDurationProperty(String[] prefixes, String key, Supplier<Duration> supplier) {
/* 334 */     for (String prefix : prefixes) {
/* 335 */       if (hasProperty(prefix + key)) {
/* 336 */         return getDurationProperty(prefix + key, null);
/*     */       }
/*     */     } 
/* 339 */     return (supplier != null) ? supplier.get() : null;
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
/*     */   public String getStringProperty(String[] prefixes, String key, Supplier<String> supplier) {
/* 352 */     for (String prefix : prefixes) {
/* 353 */       String result = getStringProperty(prefix + key);
/* 354 */       if (result != null) {
/* 355 */         return result;
/*     */       }
/*     */     } 
/* 358 */     return (supplier != null) ? supplier.get() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStringProperty(String name) {
/* 368 */     return this.environment.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStringProperty(String name, String defaultValue) {
/* 379 */     String prop = getStringProperty(name);
/* 380 */     return (prop == null) ? defaultValue : prop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties getSystemProperties() {
/*     */     try {
/* 390 */       return new Properties(System.getProperties());
/* 391 */     } catch (SecurityException ex) {
/* 392 */       LowLevelLogUtil.logException("Unable to access system properties.", ex);
/*     */       
/* 394 */       return new Properties();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reload() {
/* 404 */     this.environment.reload();
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
/*     */   private static class Environment
/*     */   {
/* 422 */     private final Set<PropertySource> sources = new TreeSet<>(new PropertySource.Comparator());
/* 423 */     private final Map<CharSequence, String> literal = new ConcurrentHashMap<>();
/* 424 */     private final Map<CharSequence, String> normalized = new ConcurrentHashMap<>();
/* 425 */     private final Map<List<CharSequence>, String> tokenized = new ConcurrentHashMap<>();
/*     */     
/*     */     private Environment(PropertySource propertySource) {
/* 428 */       PropertyFilePropertySource sysProps = new PropertyFilePropertySource("log4j2.system.properties");
/*     */       try {
/* 430 */         sysProps.forEach((key, value) -> {
/*     */               if (System.getProperty(key) == null) {
/*     */                 System.setProperty(key, value);
/*     */               }
/*     */             });
/* 435 */       } catch (SecurityException securityException) {}
/*     */ 
/*     */       
/* 438 */       this.sources.add(propertySource);
/* 439 */       for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
/*     */         try {
/* 441 */           for (PropertySource source : ServiceLoader.<PropertySource>load(PropertySource.class, classLoader)) {
/* 442 */             this.sources.add(source);
/*     */           }
/* 444 */         } catch (Throwable throwable) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 451 */       reload();
/*     */     }
/*     */     
/*     */     private synchronized void reload() {
/* 455 */       this.literal.clear();
/* 456 */       this.normalized.clear();
/* 457 */       this.tokenized.clear();
/* 458 */       for (Iterator<PropertySource> iterator = this.sources.iterator(); iterator.hasNext(); ) { PropertySource source = iterator.next();
/* 459 */         source.forEach((key, value) -> {
/*     */               if (key != null && value != null) {
/*     */                 this.literal.put(key, value);
/*     */                 List<CharSequence> tokens = PropertySource.Util.tokenize(key);
/*     */                 if (tokens.isEmpty()) {
/*     */                   this.normalized.put(source.getNormalForm(Collections.singleton(key)), value);
/*     */                 } else {
/*     */                   this.normalized.put(source.getNormalForm(tokens), value);
/*     */                   this.tokenized.put(tokens, value);
/*     */                 } 
/*     */               } 
/*     */             }); }
/*     */     
/*     */     }
/*     */     
/*     */     private static boolean hasSystemProperty(String key) {
/*     */       try {
/* 476 */         return System.getProperties().containsKey(key);
/* 477 */       } catch (SecurityException ignored) {
/* 478 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     private String get(String key) {
/* 483 */       if (this.normalized.containsKey(key)) {
/* 484 */         return this.normalized.get(key);
/*     */       }
/* 486 */       if (this.literal.containsKey(key)) {
/* 487 */         return this.literal.get(key);
/*     */       }
/* 489 */       if (hasSystemProperty(key)) {
/* 490 */         return System.getProperty(key);
/*     */       }
/* 492 */       for (PropertySource source : this.sources) {
/* 493 */         if (source.containsProperty(key)) {
/* 494 */           return source.getProperty(key);
/*     */         }
/*     */       } 
/* 497 */       return this.tokenized.get(PropertySource.Util.tokenize(key));
/*     */     }
/*     */     
/*     */     private boolean containsKey(String key) {
/* 501 */       return (this.normalized.containsKey(key) || this.literal
/* 502 */         .containsKey(key) || 
/* 503 */         hasSystemProperty(key) || this.tokenized
/* 504 */         .containsKey(PropertySource.Util.tokenize(key)));
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
/*     */   public static Properties extractSubset(Properties properties, String prefix) {
/* 517 */     Properties subset = new Properties();
/*     */     
/* 519 */     if (prefix == null || prefix.length() == 0) {
/* 520 */       return subset;
/*     */     }
/*     */     
/* 523 */     String prefixToMatch = (prefix.charAt(prefix.length() - 1) != '.') ? (prefix + '.') : prefix;
/*     */     
/* 525 */     List<String> keys = new ArrayList<>();
/*     */     
/* 527 */     for (String key : properties.stringPropertyNames()) {
/* 528 */       if (key.startsWith(prefixToMatch)) {
/* 529 */         subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
/* 530 */         keys.add(key);
/*     */       } 
/*     */     } 
/* 533 */     for (String key : keys) {
/* 534 */       properties.remove(key);
/*     */     }
/*     */     
/* 537 */     return subset;
/*     */   }
/*     */   
/*     */   static ResourceBundle getCharsetsResourceBundle() {
/* 541 */     return ResourceBundle.getBundle("Log4j-charsets");
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
/*     */   public static Map<String, Properties> partitionOnCommonPrefixes(Properties properties) {
/* 553 */     Map<String, Properties> parts = new ConcurrentHashMap<>();
/* 554 */     for (String key : properties.stringPropertyNames()) {
/* 555 */       String prefix = key.substring(0, key.indexOf('.'));
/* 556 */       if (!parts.containsKey(prefix)) {
/* 557 */         parts.put(prefix, new Properties());
/*     */       }
/* 559 */       ((Properties)parts.get(prefix)).setProperty(key.substring(key.indexOf('.') + 1), properties.getProperty(key));
/*     */     } 
/* 561 */     return parts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOsWindows() {
/* 570 */     return getStringProperty("os.name", "").startsWith("Windows");
/*     */   }
/*     */   
/*     */   private enum TimeUnit {
/* 574 */     NANOS("ns,nano,nanos,nanosecond,nanoseconds", ChronoUnit.NANOS),
/* 575 */     MICROS("us,micro,micros,microsecond,microseconds", ChronoUnit.MICROS),
/* 576 */     MILLIS("ms,milli,millis,millsecond,milliseconds", ChronoUnit.MILLIS),
/* 577 */     SECONDS("s,second,seconds", ChronoUnit.SECONDS),
/* 578 */     MINUTES("m,minute,minutes", ChronoUnit.MINUTES),
/* 579 */     HOURS("h,hour,hours", ChronoUnit.HOURS),
/* 580 */     DAYS("d,day,days", ChronoUnit.DAYS);
/*     */     
/*     */     private final String[] descriptions;
/*     */     private final ChronoUnit timeUnit;
/*     */     
/*     */     TimeUnit(String descriptions, ChronoUnit timeUnit) {
/* 586 */       this.descriptions = descriptions.split(",");
/* 587 */       this.timeUnit = timeUnit;
/*     */     }
/*     */     
/*     */     ChronoUnit getTimeUnit() {
/* 591 */       return this.timeUnit;
/*     */     }
/*     */     
/*     */     static Duration getDuration(String time) {
/* 595 */       String value = time.trim();
/* 596 */       TemporalUnit temporalUnit = ChronoUnit.MILLIS;
/* 597 */       long timeVal = 0L;
/* 598 */       for (TimeUnit timeUnit : values()) {
/* 599 */         for (String suffix : timeUnit.descriptions) {
/* 600 */           if (value.endsWith(suffix)) {
/* 601 */             temporalUnit = timeUnit.timeUnit;
/* 602 */             timeVal = Long.parseLong(value.substring(0, value.length() - suffix.length()));
/*     */           } 
/*     */         } 
/*     */       } 
/* 606 */       return Duration.of(timeVal, temporalUnit);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\PropertiesUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */