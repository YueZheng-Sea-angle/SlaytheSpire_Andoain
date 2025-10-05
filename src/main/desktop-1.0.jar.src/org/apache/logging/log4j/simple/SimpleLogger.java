/*     */ package org.apache.logging.log4j.simple;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
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
/*     */ public class SimpleLogger
/*     */   extends AbstractLogger
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final char SPACE = ' ';
/*     */   private final DateFormat dateFormatter;
/*     */   private Level level;
/*     */   private final boolean showDateTime;
/*     */   private final boolean showContextMap;
/*     */   private PrintStream stream;
/*     */   private final String logName;
/*     */   
/*     */   public SimpleLogger(String name, Level defaultLevel, boolean showLogName, boolean showShortLogName, boolean showDateTime, boolean showContextMap, String dateTimeFormat, MessageFactory messageFactory, PropertiesUtil props, PrintStream stream) {
/*  65 */     super(name, messageFactory);
/*  66 */     String lvl = props.getStringProperty("org.apache.logging.log4j.simplelog." + name + ".level");
/*  67 */     this.level = Level.toLevel(lvl, defaultLevel);
/*  68 */     if (showShortLogName) {
/*  69 */       int index = name.lastIndexOf(".");
/*  70 */       if (index > 0 && index < name.length()) {
/*  71 */         this.logName = name.substring(index + 1);
/*     */       } else {
/*  73 */         this.logName = name;
/*     */       } 
/*  75 */     } else if (showLogName) {
/*  76 */       this.logName = name;
/*     */     } else {
/*  78 */       this.logName = null;
/*     */     } 
/*  80 */     this.showDateTime = showDateTime;
/*  81 */     this.showContextMap = showContextMap;
/*  82 */     this.stream = stream;
/*     */     
/*  84 */     if (showDateTime) {
/*     */       DateFormat format;
/*     */       try {
/*  87 */         format = new SimpleDateFormat(dateTimeFormat);
/*  88 */       } catch (IllegalArgumentException e) {
/*     */         
/*  90 */         format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS zzz");
/*     */       } 
/*  92 */       this.dateFormatter = format;
/*     */     } else {
/*  94 */       this.dateFormatter = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 100 */     return this.level;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, Message msg, Throwable t) {
/* 105 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, CharSequence msg, Throwable t) {
/* 110 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, Object msg, Throwable t) {
/* 115 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String msg) {
/* 120 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String msg, Object... p1) {
/* 125 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0) {
/* 130 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1) {
/* 136 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 142 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 148 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 155 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 162 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 169 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 177 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 185 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 193 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level testLevel, Marker marker, String msg, Throwable t) {
/* 198 */     return (this.level.intLevel() >= testLevel.intLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level mgsLevel, Marker marker, Message msg, Throwable throwable) {
/*     */     Throwable t;
/* 204 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 206 */     if (this.showDateTime) {
/* 207 */       String dateText; Date now = new Date();
/*     */       
/* 209 */       synchronized (this.dateFormatter) {
/* 210 */         dateText = this.dateFormatter.format(now);
/*     */       } 
/* 212 */       sb.append(dateText);
/* 213 */       sb.append(' ');
/*     */     } 
/*     */     
/* 216 */     sb.append(mgsLevel.toString());
/* 217 */     sb.append(' ');
/* 218 */     if (Strings.isNotEmpty(this.logName)) {
/* 219 */       sb.append(this.logName);
/* 220 */       sb.append(' ');
/*     */     } 
/* 222 */     sb.append(msg.getFormattedMessage());
/* 223 */     if (this.showContextMap) {
/* 224 */       Map<String, String> mdc = ThreadContext.getImmutableContext();
/* 225 */       if (mdc.size() > 0) {
/* 226 */         sb.append(' ');
/* 227 */         sb.append(mdc.toString());
/* 228 */         sb.append(' ');
/*     */       } 
/*     */     } 
/* 231 */     Object[] params = msg.getParameters();
/*     */     
/* 233 */     if (throwable == null && params != null && params.length > 0 && params[params.length - 1] instanceof Throwable) {
/*     */       
/* 235 */       t = (Throwable)params[params.length - 1];
/*     */     } else {
/* 237 */       t = throwable;
/*     */     } 
/* 239 */     this.stream.println(sb.toString());
/* 240 */     if (t != null) {
/* 241 */       this.stream.print(' ');
/* 242 */       t.printStackTrace(this.stream);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 247 */     if (level != null) {
/* 248 */       this.level = level;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStream(PrintStream stream) {
/* 253 */     this.stream = stream;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\simple\SimpleLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */