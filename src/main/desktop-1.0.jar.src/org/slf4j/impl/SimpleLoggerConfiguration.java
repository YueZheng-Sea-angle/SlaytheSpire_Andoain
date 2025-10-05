/*     */ package org.slf4j.impl;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Properties;
/*     */ import org.slf4j.helpers.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleLoggerConfiguration
/*     */ {
/*     */   private static final String CONFIGURATION_FILE = "simplelogger.properties";
/*  34 */   static int DEFAULT_LOG_LEVEL_DEFAULT = 20;
/*  35 */   int defaultLogLevel = DEFAULT_LOG_LEVEL_DEFAULT;
/*     */   
/*     */   private static final boolean SHOW_DATE_TIME_DEFAULT = false;
/*     */   
/*     */   boolean showDateTime = false;
/*  40 */   private static final String DATE_TIME_FORMAT_STR_DEFAULT = null;
/*  41 */   private static String dateTimeFormatStr = DATE_TIME_FORMAT_STR_DEFAULT;
/*     */   
/*  43 */   DateFormat dateFormatter = null;
/*     */   
/*     */   private static final boolean SHOW_THREAD_NAME_DEFAULT = true;
/*     */   
/*     */   boolean showThreadName = true;
/*     */   
/*     */   static final boolean SHOW_LOG_NAME_DEFAULT = true;
/*     */   
/*     */   boolean showLogName = true;
/*     */   
/*     */   private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = false;
/*     */   boolean showShortLogName = false;
/*     */   private static final boolean LEVEL_IN_BRACKETS_DEFAULT = false;
/*     */   boolean levelInBrackets = false;
/*  57 */   private static String LOG_FILE_DEFAULT = "System.err";
/*  58 */   private String logFile = LOG_FILE_DEFAULT;
/*  59 */   OutputChoice outputChoice = null;
/*     */   
/*     */   private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
/*     */   
/*     */   private boolean cacheOutputStream = false;
/*     */   private static final String WARN_LEVELS_STRING_DEFAULT = "WARN";
/*  65 */   String warnLevelString = "WARN";
/*     */   
/*  67 */   private final Properties properties = new Properties();
/*     */   
/*     */   void init() {
/*  70 */     loadProperties();
/*     */     
/*  72 */     String defaultLogLevelString = getStringProperty("org.slf4j.simpleLogger.defaultLogLevel", null);
/*  73 */     if (defaultLogLevelString != null) {
/*  74 */       this.defaultLogLevel = stringToLevel(defaultLogLevelString);
/*     */     }
/*  76 */     this.showLogName = getBooleanProperty("org.slf4j.simpleLogger.showLogName", true);
/*  77 */     this.showShortLogName = getBooleanProperty("org.slf4j.simpleLogger.showShortLogName", false);
/*  78 */     this.showDateTime = getBooleanProperty("org.slf4j.simpleLogger.showDateTime", false);
/*  79 */     this.showThreadName = getBooleanProperty("org.slf4j.simpleLogger.showThreadName", true);
/*  80 */     dateTimeFormatStr = getStringProperty("org.slf4j.simpleLogger.dateTimeFormat", DATE_TIME_FORMAT_STR_DEFAULT);
/*  81 */     this.levelInBrackets = getBooleanProperty("org.slf4j.simpleLogger.levelInBrackets", false);
/*  82 */     this.warnLevelString = getStringProperty("org.slf4j.simpleLogger.warnLevelString", "WARN");
/*     */     
/*  84 */     this.logFile = getStringProperty("org.slf4j.simpleLogger.logFile", this.logFile);
/*     */     
/*  86 */     this.cacheOutputStream = getBooleanProperty("org.slf4j.simpleLogger.cacheOutputStream", false);
/*  87 */     this.outputChoice = computeOutputChoice(this.logFile, this.cacheOutputStream);
/*     */     
/*  89 */     if (dateTimeFormatStr != null) {
/*     */       try {
/*  91 */         this.dateFormatter = new SimpleDateFormat(dateTimeFormatStr);
/*  92 */       } catch (IllegalArgumentException e) {
/*  93 */         Util.report("Bad date format in simplelogger.properties; will output relative time", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadProperties() {
/* 100 */     InputStream in = AccessController.<InputStream>doPrivileged(new PrivilegedAction<InputStream>() {
/*     */           public InputStream run() {
/* 102 */             ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
/* 103 */             if (threadCL != null) {
/* 104 */               return threadCL.getResourceAsStream("simplelogger.properties");
/*     */             }
/* 106 */             return ClassLoader.getSystemResourceAsStream("simplelogger.properties");
/*     */           }
/*     */         });
/*     */     
/* 110 */     if (null != in) {
/*     */       
/* 112 */       try { this.properties.load(in); }
/* 113 */       catch (IOException e)
/*     */       
/*     */       { 
/*     */         try {
/* 117 */           in.close();
/* 118 */         } catch (IOException iOException) {} } finally { try { in.close(); } catch (IOException e) {} }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String getStringProperty(String name, String defaultValue) {
/* 126 */     String prop = getStringProperty(name);
/* 127 */     return (prop == null) ? defaultValue : prop;
/*     */   }
/*     */   
/*     */   boolean getBooleanProperty(String name, boolean defaultValue) {
/* 131 */     String prop = getStringProperty(name);
/* 132 */     return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
/*     */   }
/*     */   
/*     */   String getStringProperty(String name) {
/* 136 */     String prop = null;
/*     */     try {
/* 138 */       prop = System.getProperty(name);
/* 139 */     } catch (SecurityException e) {}
/*     */ 
/*     */     
/* 142 */     return (prop == null) ? this.properties.getProperty(name) : prop;
/*     */   }
/*     */   
/*     */   static int stringToLevel(String levelStr) {
/* 146 */     if ("trace".equalsIgnoreCase(levelStr))
/* 147 */       return 0; 
/* 148 */     if ("debug".equalsIgnoreCase(levelStr))
/* 149 */       return 10; 
/* 150 */     if ("info".equalsIgnoreCase(levelStr))
/* 151 */       return 20; 
/* 152 */     if ("warn".equalsIgnoreCase(levelStr))
/* 153 */       return 30; 
/* 154 */     if ("error".equalsIgnoreCase(levelStr))
/* 155 */       return 40; 
/* 156 */     if ("off".equalsIgnoreCase(levelStr)) {
/* 157 */       return 50;
/*     */     }
/*     */     
/* 160 */     return 20;
/*     */   }
/*     */   
/*     */   private static OutputChoice computeOutputChoice(String logFile, boolean cacheOutputStream) {
/* 164 */     if ("System.err".equalsIgnoreCase(logFile)) {
/* 165 */       if (cacheOutputStream) {
/* 166 */         return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR);
/*     */       }
/* 168 */       return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
/* 169 */     }  if ("System.out".equalsIgnoreCase(logFile)) {
/* 170 */       if (cacheOutputStream) {
/* 171 */         return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT);
/*     */       }
/* 173 */       return new OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT);
/*     */     } 
/*     */     try {
/* 176 */       FileOutputStream fos = new FileOutputStream(logFile);
/* 177 */       PrintStream printStream = new PrintStream(fos);
/* 178 */       return new OutputChoice(printStream);
/* 179 */     } catch (FileNotFoundException e) {
/* 180 */       Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
/* 181 */       return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\SimpleLoggerConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */