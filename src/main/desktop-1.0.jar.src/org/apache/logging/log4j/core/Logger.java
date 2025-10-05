/*     */ package org.apache.logging.log4j.core;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LocationAwareReliabilityStrategy;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.ReliabilityStrategy;
/*     */ import org.apache.logging.log4j.core.filter.CompositeFilter;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MessageFactory;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Logger
/*     */   extends AbstractLogger
/*     */   implements Supplier<LoggerConfig>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected volatile PrivateConfig privateConfig;
/*     */   private final LoggerContext context;
/*     */   
/*     */   protected Logger(LoggerContext context, String name, MessageFactory messageFactory) {
/*  72 */     super(name, messageFactory);
/*  73 */     this.context = context;
/*  74 */     this.privateConfig = new PrivateConfig(context.getConfiguration(), this);
/*     */   }
/*     */   
/*     */   protected Object writeReplace() throws ObjectStreamException {
/*  78 */     return new LoggerProxy(getName(), getMessageFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParent() {
/*  89 */     LoggerConfig lc = this.privateConfig.loggerConfig.getName().equals(getName()) ? this.privateConfig.loggerConfig.getParent() : this.privateConfig.loggerConfig;
/*  90 */     if (lc == null) {
/*  91 */       return null;
/*     */     }
/*  93 */     String lcName = lc.getName();
/*  94 */     MessageFactory messageFactory = getMessageFactory();
/*  95 */     if (this.context.hasLogger(lcName, messageFactory)) {
/*  96 */       return this.context.getLogger(lcName, messageFactory);
/*     */     }
/*  98 */     return new Logger(this.context, lcName, messageFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerContext getContext() {
/* 107 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setLevel(Level level) {
/*     */     Level actualLevel;
/* 119 */     if (level == getLevel()) {
/*     */       return;
/*     */     }
/*     */     
/* 123 */     if (level != null) {
/* 124 */       actualLevel = level;
/*     */     } else {
/* 126 */       Logger parent = getParent();
/* 127 */       actualLevel = (parent != null) ? parent.getLevel() : this.privateConfig.loggerConfigLevel;
/*     */     } 
/* 129 */     this.privateConfig = new PrivateConfig(this.privateConfig, actualLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggerConfig get() {
/* 139 */     return this.privateConfig.loggerConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean requiresLocation() {
/* 145 */     return this.privateConfig.requiresLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
/* 151 */     Message msg = (message == null) ? (Message)new SimpleMessage("") : message;
/* 152 */     ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
/* 153 */     strategy.log(this, getName(), fqcn, marker, level, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
/* 159 */     ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
/* 160 */     if (strategy instanceof LocationAwareReliabilityStrategy) {
/* 161 */       ((LocationAwareReliabilityStrategy)strategy).log(this, getName(), fqcn, location, marker, level, message, throwable);
/*     */     } else {
/*     */       
/* 164 */       strategy.log(this, getName(), fqcn, marker, level, message, throwable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Throwable t) {
/* 170 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message) {
/* 175 */     return this.privateConfig.filter(level, marker, message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object... params) {
/* 180 */     return this.privateConfig.filter(level, marker, message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0) {
/* 185 */     return this.privateConfig.filter(level, marker, message, p0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1) {
/* 191 */     return this.privateConfig.filter(level, marker, message, p0, p1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
/* 197 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
/* 203 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 210 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 217 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 224 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 232 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 240 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 248 */     return this.privateConfig.filter(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, CharSequence message, Throwable t) {
/* 253 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Object message, Throwable t) {
/* 258 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled(Level level, Marker marker, Message message, Throwable t) {
/* 263 */     return this.privateConfig.filter(level, marker, message, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender appender) {
/* 272 */     this.privateConfig.config.addLoggerAppender(this, appender);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(Appender appender) {
/* 281 */     this.privateConfig.loggerConfig.removeAppender(appender.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Appender> getAppenders() {
/* 290 */     return this.privateConfig.loggerConfig.getAppenders();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Filter> getFilters() {
/* 300 */     Filter filter = this.privateConfig.loggerConfig.getFilter();
/* 301 */     if (filter == null)
/* 302 */       return (new ArrayList<>()).iterator(); 
/* 303 */     if (filter instanceof CompositeFilter) {
/* 304 */       return ((CompositeFilter)filter).iterator();
/*     */     }
/* 306 */     List<Filter> filters = new ArrayList<>();
/* 307 */     filters.add(filter);
/* 308 */     return filters.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 319 */     return this.privateConfig.loggerConfigLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int filterCount() {
/* 328 */     Filter filter = this.privateConfig.loggerConfig.getFilter();
/* 329 */     if (filter == null)
/* 330 */       return 0; 
/* 331 */     if (filter instanceof CompositeFilter) {
/* 332 */       return ((CompositeFilter)filter).size();
/*     */     }
/* 334 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter filter) {
/* 343 */     this.privateConfig.config.addLoggerFilter(this, filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/* 353 */     return this.privateConfig.loggerConfig.isAdditive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/* 363 */     this.privateConfig.config.setLoggerAdditive(this, additive);
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
/*     */   protected void updateConfiguration(Configuration newConfig) {
/* 384 */     this.privateConfig = new PrivateConfig(newConfig, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected class PrivateConfig
/*     */   {
/*     */     public final LoggerConfig loggerConfig;
/*     */     
/*     */     public final Configuration config;
/*     */     
/*     */     private final Level loggerConfigLevel;
/*     */     
/*     */     private final int intLevel;
/*     */     
/*     */     private final Logger logger;
/*     */     private final boolean requiresLocation;
/*     */     
/*     */     public PrivateConfig(Configuration config, Logger logger) {
/* 402 */       this.config = config;
/* 403 */       this.loggerConfig = config.getLoggerConfig(Logger.this.getName());
/* 404 */       this.loggerConfigLevel = this.loggerConfig.getLevel();
/* 405 */       this.intLevel = this.loggerConfigLevel.intLevel();
/* 406 */       this.logger = logger;
/* 407 */       this.requiresLocation = this.loggerConfig.requiresLocation();
/*     */     }
/*     */     
/*     */     public PrivateConfig(PrivateConfig pc, Level level) {
/* 411 */       this.config = pc.config;
/* 412 */       this.loggerConfig = pc.loggerConfig;
/* 413 */       this.loggerConfigLevel = level;
/* 414 */       this.intLevel = this.loggerConfigLevel.intLevel();
/* 415 */       this.logger = pc.logger;
/* 416 */       this.requiresLocation = this.loggerConfig.requiresLocation();
/*     */     }
/*     */     
/*     */     public PrivateConfig(PrivateConfig pc, LoggerConfig lc) {
/* 420 */       this.config = pc.config;
/* 421 */       this.loggerConfig = lc;
/* 422 */       this.loggerConfigLevel = lc.getLevel();
/* 423 */       this.intLevel = this.loggerConfigLevel.intLevel();
/* 424 */       this.logger = pc.logger;
/* 425 */       this.requiresLocation = this.loggerConfig.requiresLocation();
/*     */     }
/*     */ 
/*     */     
/*     */     public void logEvent(LogEvent event) {
/* 430 */       this.loggerConfig.log(event);
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg) {
/* 434 */       Filter filter = this.config.getFilter();
/* 435 */       if (filter != null) {
/* 436 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, new Object[0]);
/* 437 */         if (r != Filter.Result.NEUTRAL) {
/* 438 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 441 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Throwable t) {
/* 445 */       Filter filter = this.config.getFilter();
/* 446 */       if (filter != null) {
/* 447 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 448 */         if (r != Filter.Result.NEUTRAL) {
/* 449 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 452 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object... p1) {
/* 456 */       Filter filter = this.config.getFilter();
/* 457 */       if (filter != null) {
/* 458 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p1);
/* 459 */         if (r != Filter.Result.NEUTRAL) {
/* 460 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 463 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0) {
/* 467 */       Filter filter = this.config.getFilter();
/* 468 */       if (filter != null) {
/* 469 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0);
/* 470 */         if (r != Filter.Result.NEUTRAL) {
/* 471 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 474 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1) {
/* 479 */       Filter filter = this.config.getFilter();
/* 480 */       if (filter != null) {
/* 481 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1);
/* 482 */         if (r != Filter.Result.NEUTRAL) {
/* 483 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 486 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 491 */       Filter filter = this.config.getFilter();
/* 492 */       if (filter != null) {
/* 493 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2);
/* 494 */         if (r != Filter.Result.NEUTRAL) {
/* 495 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 498 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 503 */       Filter filter = this.config.getFilter();
/* 504 */       if (filter != null) {
/* 505 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3);
/* 506 */         if (r != Filter.Result.NEUTRAL) {
/* 507 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 510 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 516 */       Filter filter = this.config.getFilter();
/* 517 */       if (filter != null) {
/* 518 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4);
/* 519 */         if (r != Filter.Result.NEUTRAL) {
/* 520 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 523 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 529 */       Filter filter = this.config.getFilter();
/* 530 */       if (filter != null) {
/* 531 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5);
/* 532 */         if (r != Filter.Result.NEUTRAL) {
/* 533 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 536 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 542 */       Filter filter = this.config.getFilter();
/* 543 */       if (filter != null) {
/* 544 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6);
/* 545 */         if (r != Filter.Result.NEUTRAL) {
/* 546 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 549 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 556 */       Filter filter = this.config.getFilter();
/* 557 */       if (filter != null) {
/* 558 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7);
/* 559 */         if (r != Filter.Result.NEUTRAL) {
/* 560 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 563 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 570 */       Filter filter = this.config.getFilter();
/* 571 */       if (filter != null) {
/* 572 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/* 573 */         if (r != Filter.Result.NEUTRAL) {
/* 574 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 577 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean filter(Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 584 */       Filter filter = this.config.getFilter();
/* 585 */       if (filter != null) {
/* 586 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*     */         
/* 588 */         if (r != Filter.Result.NEUTRAL) {
/* 589 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 592 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, CharSequence msg, Throwable t) {
/* 596 */       Filter filter = this.config.getFilter();
/* 597 */       if (filter != null) {
/* 598 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 599 */         if (r != Filter.Result.NEUTRAL) {
/* 600 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 603 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, Object msg, Throwable t) {
/* 607 */       Filter filter = this.config.getFilter();
/* 608 */       if (filter != null) {
/* 609 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 610 */         if (r != Filter.Result.NEUTRAL) {
/* 611 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 614 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */     
/*     */     boolean filter(Level level, Marker marker, Message msg, Throwable t) {
/* 618 */       Filter filter = this.config.getFilter();
/* 619 */       if (filter != null) {
/* 620 */         Filter.Result r = filter.filter(this.logger, level, marker, msg, t);
/* 621 */         if (r != Filter.Result.NEUTRAL) {
/* 622 */           return (r == Filter.Result.ACCEPT);
/*     */         }
/*     */       } 
/* 625 */       return (level != null && this.intLevel >= level.intLevel());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 630 */       StringBuilder builder = new StringBuilder();
/* 631 */       builder.append("PrivateConfig [loggerConfig=");
/* 632 */       builder.append(this.loggerConfig);
/* 633 */       builder.append(", config=");
/* 634 */       builder.append(this.config);
/* 635 */       builder.append(", loggerConfigLevel=");
/* 636 */       builder.append(this.loggerConfigLevel);
/* 637 */       builder.append(", intLevel=");
/* 638 */       builder.append(this.intLevel);
/* 639 */       builder.append(", logger=");
/* 640 */       builder.append(this.logger);
/* 641 */       builder.append("]");
/* 642 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class LoggerProxy
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final MessageFactory messageFactory;
/*     */ 
/*     */     
/*     */     public LoggerProxy(String name, MessageFactory messageFactory) {
/* 659 */       this.name = name;
/* 660 */       this.messageFactory = messageFactory;
/*     */     }
/*     */     
/*     */     protected Object readResolve() throws ObjectStreamException {
/* 664 */       return new Logger(LoggerContext.getContext(), this.name, this.messageFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 675 */     String nameLevel = "" + getName() + ':' + getLevel();
/* 676 */     if (this.context == null) {
/* 677 */       return nameLevel;
/*     */     }
/* 679 */     String contextName = this.context.getName();
/* 680 */     return (contextName == null) ? nameLevel : (nameLevel + " in " + contextName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 685 */     if (this == o) {
/* 686 */       return true;
/*     */     }
/* 688 */     if (o == null || getClass() != o.getClass()) {
/* 689 */       return false;
/*     */     }
/* 691 */     Logger that = (Logger)o;
/* 692 */     return getName().equals(that.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 697 */     return getName().hashCode();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */