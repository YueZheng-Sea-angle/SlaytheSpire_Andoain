/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Failover", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class FailoverAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private static final int DEFAULT_INTERVAL_SECONDS = 60;
/*     */   private final String primaryRef;
/*     */   private final String[] failovers;
/*     */   private final Configuration config;
/*     */   private AppenderControl primary;
/*  59 */   private final List<AppenderControl> failoverAppenders = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private final long intervalNanos;
/*     */   
/*     */   private volatile long nextCheckNanos;
/*     */ 
/*     */   
/*     */   private FailoverAppender(String name, Filter filter, String primary, String[] failovers, int intervalMillis, Configuration config, boolean ignoreExceptions, Property[] properties) {
/*  68 */     super(name, filter, (Layout<? extends Serializable>)null, ignoreExceptions, properties);
/*  69 */     this.primaryRef = primary;
/*  70 */     this.failovers = failovers;
/*  71 */     this.config = config;
/*  72 */     this.intervalNanos = TimeUnit.MILLISECONDS.toNanos(intervalMillis);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  77 */     Map<String, Appender> map = this.config.getAppenders();
/*  78 */     int errors = 0;
/*  79 */     Appender appender = map.get(this.primaryRef);
/*  80 */     if (appender != null) {
/*  81 */       this.primary = new AppenderControl(appender, null, null);
/*     */     } else {
/*  83 */       LOGGER.error("Unable to locate primary Appender " + this.primaryRef);
/*  84 */       errors++;
/*     */     } 
/*  86 */     for (String name : this.failovers) {
/*  87 */       Appender foAppender = map.get(name);
/*  88 */       if (foAppender != null) {
/*  89 */         this.failoverAppenders.add(new AppenderControl(foAppender, null, null));
/*     */       } else {
/*  91 */         LOGGER.error("Failover appender " + name + " is not configured");
/*     */       } 
/*     */     } 
/*  94 */     if (this.failoverAppenders.isEmpty()) {
/*  95 */       LOGGER.error("No failover appenders are available");
/*  96 */       errors++;
/*     */     } 
/*  98 */     if (errors == 0) {
/*  99 */       super.start();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 109 */     if (!isStarted()) {
/* 110 */       error("FailoverAppender " + getName() + " did not start successfully");
/*     */       return;
/*     */     } 
/* 113 */     long localCheckNanos = this.nextCheckNanos;
/* 114 */     if (localCheckNanos == 0L || System.nanoTime() - localCheckNanos > 0L) {
/* 115 */       callAppender(event);
/*     */     } else {
/* 117 */       failover(event, (Exception)null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void callAppender(LogEvent event) {
/*     */     try {
/* 123 */       this.primary.callAppender(event);
/* 124 */       this.nextCheckNanos = 0L;
/* 125 */     } catch (Exception ex) {
/* 126 */       this.nextCheckNanos = System.nanoTime() + this.intervalNanos;
/* 127 */       failover(event, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void failover(LogEvent event, Exception ex) {
/* 132 */     LoggingException loggingException = (ex != null) ? ((ex instanceof LoggingException) ? (LoggingException)ex : new LoggingException(ex)) : null;
/*     */     
/* 134 */     boolean written = false;
/* 135 */     Exception failoverException = null;
/* 136 */     for (AppenderControl control : this.failoverAppenders) {
/*     */       try {
/* 138 */         control.callAppender(event);
/* 139 */         written = true;
/*     */         break;
/* 141 */       } catch (Exception fex) {
/* 142 */         if (failoverException == null) {
/* 143 */           failoverException = fex;
/*     */         }
/*     */       } 
/*     */     } 
/* 147 */     if (!written && !ignoreExceptions()) {
/* 148 */       if (loggingException != null) {
/* 149 */         throw loggingException;
/*     */       }
/* 151 */       throw new LoggingException("Unable to write to failover appenders", failoverException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     StringBuilder sb = new StringBuilder(getName());
/* 158 */     sb.append(" primary=").append(this.primary).append(", failover={");
/* 159 */     boolean first = true;
/* 160 */     for (String str : this.failovers) {
/* 161 */       if (!first) {
/* 162 */         sb.append(", ");
/*     */       }
/* 164 */       sb.append(str);
/* 165 */       first = false;
/*     */     } 
/* 167 */     sb.append('}');
/* 168 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static FailoverAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("primary") String primary, @PluginElement("Failovers") String[] failovers, @PluginAliases({"retryInterval"}) @PluginAttribute("retryIntervalSeconds") String retryIntervalSeconds, @PluginConfiguration Configuration config, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore) {
/*     */     int retryIntervalMillis;
/* 193 */     if (name == null) {
/* 194 */       LOGGER.error("A name for the Appender must be specified");
/* 195 */       return null;
/*     */     } 
/* 197 */     if (primary == null) {
/* 198 */       LOGGER.error("A primary Appender must be specified");
/* 199 */       return null;
/*     */     } 
/* 201 */     if (failovers == null || failovers.length == 0) {
/* 202 */       LOGGER.error("At least one failover Appender must be specified");
/* 203 */       return null;
/*     */     } 
/*     */     
/* 206 */     int seconds = parseInt(retryIntervalSeconds, 60);
/*     */     
/* 208 */     if (seconds >= 0) {
/* 209 */       retryIntervalMillis = seconds * 1000;
/*     */     } else {
/* 211 */       LOGGER.warn("Interval " + retryIntervalSeconds + " is less than zero. Using default");
/* 212 */       retryIntervalMillis = 60000;
/*     */     } 
/*     */     
/* 215 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/* 217 */     return new FailoverAppender(name, filter, primary, failovers, retryIntervalMillis, config, ignoreExceptions, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\FailoverAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */