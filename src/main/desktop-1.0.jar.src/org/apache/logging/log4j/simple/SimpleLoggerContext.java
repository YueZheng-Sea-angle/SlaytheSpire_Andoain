/*     */ package org.apache.logging.log4j.simple;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.spi.ExtendedLogger;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerRegistry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleLoggerContext
/*     */   implements LoggerContext
/*     */ {
/*     */   private static final String SYSTEM_OUT = "system.out";
/*     */   private static final String SYSTEM_ERR = "system.err";
/*     */   protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
/*     */   protected static final String SYSTEM_PREFIX = "org.apache.logging.log4j.simplelog.";
/*     */   private final PropertiesUtil props;
/*     */   private final boolean showLogName;
/*     */   private final boolean showShortName;
/*     */   private final boolean showDateTime;
/*     */   private final boolean showContextMap;
/*     */   private final String dateTimeFormat;
/*     */   private final Level defaultLevel;
/*     */   private final PrintStream stream;
/*     */   private final LoggerRegistry<ExtendedLogger> loggerRegistry;
/*     */   
/*     */   public SimpleLoggerContext() {
/*     */     PrintStream ps;
/*  67 */     this.loggerRegistry = new LoggerRegistry();
/*     */ 
/*     */     
/*  70 */     this.props = new PropertiesUtil("log4j2.simplelog.properties");
/*     */     
/*  72 */     this.showContextMap = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showContextMap", false);
/*  73 */     this.showLogName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showlogname", false);
/*  74 */     this.showShortName = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showShortLogname", true);
/*  75 */     this.showDateTime = this.props.getBooleanProperty("org.apache.logging.log4j.simplelog.showdatetime", false);
/*  76 */     String lvl = this.props.getStringProperty("org.apache.logging.log4j.simplelog.level");
/*  77 */     this.defaultLevel = Level.toLevel(lvl, Level.ERROR);
/*     */     
/*  79 */     this.dateTimeFormat = this.showDateTime ? this.props.getStringProperty("org.apache.logging.log4j.simplelog.dateTimeFormat", "yyyy/MM/dd HH:mm:ss:SSS zzz") : null;
/*     */ 
/*     */     
/*  82 */     String fileName = this.props.getStringProperty("org.apache.logging.log4j.simplelog.logFile", "system.err");
/*     */     
/*  84 */     if ("system.err".equalsIgnoreCase(fileName)) {
/*  85 */       ps = System.err;
/*  86 */     } else if ("system.out".equalsIgnoreCase(fileName)) {
/*  87 */       ps = System.out;
/*     */     } else {
/*     */       try {
/*  90 */         FileOutputStream os = new FileOutputStream(fileName);
/*  91 */         ps = new PrintStream(os);
/*  92 */       } catch (FileNotFoundException fnfe) {
/*  93 */         ps = System.err;
/*     */       } 
/*     */     } 
/*  96 */     this.stream = ps;
/*     */   }
/*     */ 
/*     */   
/*     */   public ExtendedLogger getLogger(String name) {
/* 101 */     return getLogger(name, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedLogger getLogger(String name, MessageFactory messageFactory) {
/* 107 */     ExtendedLogger extendedLogger = this.loggerRegistry.getLogger(name, messageFactory);
/* 108 */     if (extendedLogger != null) {
/* 109 */       AbstractLogger.checkMessageFactory(extendedLogger, messageFactory);
/* 110 */       return extendedLogger;
/*     */     } 
/* 112 */     SimpleLogger simpleLogger = new SimpleLogger(name, this.defaultLevel, this.showLogName, this.showShortName, this.showDateTime, this.showContextMap, this.dateTimeFormat, messageFactory, this.props, this.stream);
/*     */     
/* 114 */     this.loggerRegistry.putIfAbsent(name, messageFactory, (ExtendedLogger)simpleLogger);
/* 115 */     return this.loggerRegistry.getLogger(name, messageFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name) {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, MessageFactory messageFactory) {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLogger(String name, Class<? extends MessageFactory> messageFactoryClass) {
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getExternalContext() {
/* 135 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\simple\SimpleLoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */