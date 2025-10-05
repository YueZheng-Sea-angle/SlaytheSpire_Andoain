/*     */ package org.slf4j.impl;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MarkerIgnoringBase;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleLogger
/*     */   extends MarkerIgnoringBase
/*     */ {
/*     */   private static final long serialVersionUID = -632788891211436180L;
/* 147 */   private static long START_TIME = System.currentTimeMillis();
/*     */   
/*     */   protected static final int LOG_LEVEL_TRACE = 0;
/*     */   
/*     */   protected static final int LOG_LEVEL_DEBUG = 10;
/*     */   
/*     */   protected static final int LOG_LEVEL_INFO = 20;
/*     */   
/*     */   protected static final int LOG_LEVEL_WARN = 30;
/*     */   
/*     */   protected static final int LOG_LEVEL_ERROR = 40;
/*     */   protected static final int LOG_LEVEL_OFF = 50;
/*     */   private static boolean INITIALIZED = false;
/* 160 */   static SimpleLoggerConfiguration CONFIG_PARAMS = null;
/*     */   
/*     */   static void lazyInit() {
/* 163 */     if (INITIALIZED) {
/*     */       return;
/*     */     }
/* 166 */     INITIALIZED = true;
/* 167 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void init() {
/* 173 */     CONFIG_PARAMS = new SimpleLoggerConfiguration();
/* 174 */     CONFIG_PARAMS.init();
/*     */   }
/*     */ 
/*     */   
/* 178 */   protected int currentLogLevel = 20;
/*     */   
/* 180 */   private transient String shortLogName = null;
/*     */ 
/*     */   
/*     */   public static final String SYSTEM_PREFIX = "org.slf4j.simpleLogger.";
/*     */ 
/*     */   
/*     */   public static final String LOG_KEY_PREFIX = "org.slf4j.simpleLogger.log.";
/*     */ 
/*     */   
/*     */   public static final String CACHE_OUTPUT_STREAM_STRING_KEY = "org.slf4j.simpleLogger.cacheOutputStream";
/*     */ 
/*     */   
/*     */   public static final String WARN_LEVEL_STRING_KEY = "org.slf4j.simpleLogger.warnLevelString";
/*     */ 
/*     */   
/*     */   public static final String LEVEL_IN_BRACKETS_KEY = "org.slf4j.simpleLogger.levelInBrackets";
/*     */ 
/*     */   
/*     */   public static final String LOG_FILE_KEY = "org.slf4j.simpleLogger.logFile";
/*     */ 
/*     */   
/*     */   public static final String SHOW_SHORT_LOG_NAME_KEY = "org.slf4j.simpleLogger.showShortLogName";
/*     */   
/*     */   public static final String SHOW_LOG_NAME_KEY = "org.slf4j.simpleLogger.showLogName";
/*     */   
/*     */   public static final String SHOW_THREAD_NAME_KEY = "org.slf4j.simpleLogger.showThreadName";
/*     */   
/*     */   public static final String DATE_TIME_FORMAT_KEY = "org.slf4j.simpleLogger.dateTimeFormat";
/*     */   
/*     */   public static final String SHOW_DATE_TIME_KEY = "org.slf4j.simpleLogger.showDateTime";
/*     */   
/*     */   public static final String DEFAULT_LOG_LEVEL_KEY = "org.slf4j.simpleLogger.defaultLogLevel";
/*     */ 
/*     */   
/*     */   SimpleLogger(String name) {
/* 215 */     this.name = name;
/*     */     
/* 217 */     String levelString = recursivelyComputeLevelString();
/* 218 */     if (levelString != null) {
/* 219 */       this.currentLogLevel = SimpleLoggerConfiguration.stringToLevel(levelString);
/*     */     } else {
/* 221 */       this.currentLogLevel = CONFIG_PARAMS.defaultLogLevel;
/*     */     } 
/*     */   }
/*     */   
/*     */   String recursivelyComputeLevelString() {
/* 226 */     String tempName = this.name;
/* 227 */     String levelString = null;
/* 228 */     int indexOfLastDot = tempName.length();
/* 229 */     while (levelString == null && indexOfLastDot > -1) {
/* 230 */       tempName = tempName.substring(0, indexOfLastDot);
/* 231 */       levelString = CONFIG_PARAMS.getStringProperty("org.slf4j.simpleLogger.log." + tempName, null);
/* 232 */       indexOfLastDot = String.valueOf(tempName).lastIndexOf(".");
/*     */     } 
/* 234 */     return levelString;
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
/*     */   private void log(int level, String message, Throwable t) {
/* 249 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/*     */     
/* 253 */     StringBuilder buf = new StringBuilder(32);
/*     */ 
/*     */     
/* 256 */     if (CONFIG_PARAMS.showDateTime) {
/* 257 */       if (CONFIG_PARAMS.dateFormatter != null) {
/* 258 */         buf.append(getFormattedDate());
/* 259 */         buf.append(' ');
/*     */       } else {
/* 261 */         buf.append(System.currentTimeMillis() - START_TIME);
/* 262 */         buf.append(' ');
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 267 */     if (CONFIG_PARAMS.showThreadName) {
/* 268 */       buf.append('[');
/* 269 */       buf.append(Thread.currentThread().getName());
/* 270 */       buf.append("] ");
/*     */     } 
/*     */     
/* 273 */     if (CONFIG_PARAMS.levelInBrackets) {
/* 274 */       buf.append('[');
/*     */     }
/*     */     
/* 277 */     String levelStr = renderLevel(level);
/* 278 */     buf.append(levelStr);
/* 279 */     if (CONFIG_PARAMS.levelInBrackets)
/* 280 */       buf.append(']'); 
/* 281 */     buf.append(' ');
/*     */ 
/*     */     
/* 284 */     if (CONFIG_PARAMS.showShortLogName) {
/* 285 */       if (this.shortLogName == null)
/* 286 */         this.shortLogName = computeShortName(); 
/* 287 */       buf.append(String.valueOf(this.shortLogName)).append(" - ");
/* 288 */     } else if (CONFIG_PARAMS.showLogName) {
/* 289 */       buf.append(String.valueOf(this.name)).append(" - ");
/*     */     } 
/*     */ 
/*     */     
/* 293 */     buf.append(message);
/*     */     
/* 295 */     write(buf, t);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String renderLevel(int level) {
/* 300 */     switch (level) {
/*     */       case 0:
/* 302 */         return "TRACE";
/*     */       case 10:
/* 304 */         return "DEBUG";
/*     */       case 20:
/* 306 */         return "INFO";
/*     */       case 30:
/* 308 */         return CONFIG_PARAMS.warnLevelString;
/*     */       case 40:
/* 310 */         return "ERROR";
/*     */     } 
/* 312 */     throw new IllegalStateException("Unrecognized level [" + level + "]");
/*     */   }
/*     */   
/*     */   void write(StringBuilder buf, Throwable t) {
/* 316 */     PrintStream targetStream = CONFIG_PARAMS.outputChoice.getTargetPrintStream();
/*     */     
/* 318 */     targetStream.println(buf.toString());
/* 319 */     writeThrowable(t, targetStream);
/* 320 */     targetStream.flush();
/*     */   }
/*     */   
/*     */   protected void writeThrowable(Throwable t, PrintStream targetStream) {
/* 324 */     if (t != null)
/* 325 */       t.printStackTrace(targetStream); 
/*     */   }
/*     */   
/*     */   private String getFormattedDate() {
/*     */     String dateText;
/* 330 */     Date now = new Date();
/*     */     
/* 332 */     synchronized (CONFIG_PARAMS.dateFormatter) {
/* 333 */       dateText = CONFIG_PARAMS.dateFormatter.format(now);
/*     */     } 
/* 335 */     return dateText;
/*     */   }
/*     */   
/*     */   private String computeShortName() {
/* 339 */     return this.name.substring(this.name.lastIndexOf(".") + 1);
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
/*     */   private void formatAndLog(int level, String format, Object arg1, Object arg2) {
/* 351 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/* 354 */     FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
/* 355 */     log(level, tp.getMessage(), tp.getThrowable());
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
/*     */   private void formatAndLog(int level, String format, Object... arguments) {
/* 367 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/* 370 */     FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
/* 371 */     log(level, tp.getMessage(), tp.getThrowable());
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
/*     */   protected boolean isLevelEnabled(int logLevel) {
/* 383 */     return (logLevel >= this.currentLogLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 388 */     return isLevelEnabled(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/* 396 */     log(0, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object param1) {
/* 404 */     formatAndLog(0, format, param1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object param1, Object param2) {
/* 412 */     formatAndLog(0, format, param1, param2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object... argArray) {
/* 420 */     formatAndLog(0, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 425 */     log(0, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 430 */     return isLevelEnabled(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String msg) {
/* 438 */     log(10, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object param1) {
/* 446 */     formatAndLog(10, format, param1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object param1, Object param2) {
/* 454 */     formatAndLog(10, format, param1, param2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object... argArray) {
/* 462 */     formatAndLog(10, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 467 */     log(10, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 472 */     return isLevelEnabled(20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String msg) {
/* 480 */     log(20, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg) {
/* 488 */     formatAndLog(20, format, arg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 496 */     formatAndLog(20, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object... argArray) {
/* 504 */     formatAndLog(20, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 509 */     log(20, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 514 */     return isLevelEnabled(30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String msg) {
/* 522 */     log(30, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 530 */     formatAndLog(30, format, arg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 538 */     formatAndLog(30, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object... argArray) {
/* 546 */     formatAndLog(30, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 551 */     log(30, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 556 */     return isLevelEnabled(40);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 564 */     log(40, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg) {
/* 572 */     formatAndLog(40, format, arg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 580 */     formatAndLog(40, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object... argArray) {
/* 588 */     formatAndLog(40, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 593 */     log(40, msg, t);
/*     */   }
/*     */   
/*     */   public void log(LoggingEvent event) {
/* 597 */     int levelInt = event.getLevel().toInt();
/*     */     
/* 599 */     if (!isLevelEnabled(levelInt)) {
/*     */       return;
/*     */     }
/* 602 */     FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
/* 603 */     log(levelInt, tp.getMessage(), event.getThrowable());
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\SimpleLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */