/*     */ package org.apache.logging.log4j.core.appender.rewrite;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.config.AppenderControl;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
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
/*     */ @Plugin(name = "Rewrite", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RewriteAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final Configuration config;
/*  45 */   private final ConcurrentMap<String, AppenderControl> appenders = new ConcurrentHashMap<>();
/*     */   
/*     */   private final RewritePolicy rewritePolicy;
/*     */   
/*     */   private final AppenderRef[] appenderRefs;
/*     */   
/*     */   private RewriteAppender(String name, Filter filter, boolean ignoreExceptions, AppenderRef[] appenderRefs, RewritePolicy rewritePolicy, Configuration config, Property[] properties) {
/*  52 */     super(name, filter, null, ignoreExceptions, properties);
/*  53 */     this.config = config;
/*  54 */     this.rewritePolicy = rewritePolicy;
/*  55 */     this.appenderRefs = appenderRefs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  60 */     for (AppenderRef ref : this.appenderRefs) {
/*  61 */       String name = ref.getRef();
/*  62 */       Appender appender = this.config.getAppender(name);
/*  63 */       if (appender != null) {
/*     */         
/*  65 */         Filter filter = (appender instanceof AbstractAppender) ? ((AbstractAppender)appender).getFilter() : null;
/*  66 */         this.appenders.put(name, new AppenderControl(appender, ref.getLevel(), filter));
/*     */       } else {
/*  68 */         LOGGER.error("Appender " + ref + " cannot be located. Reference ignored");
/*     */       } 
/*     */     } 
/*  71 */     super.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*  80 */     if (this.rewritePolicy != null) {
/*  81 */       event = this.rewritePolicy.rewrite(event);
/*     */     }
/*  83 */     for (AppenderControl control : this.appenders.values()) {
/*  84 */       control.callAppender(event);
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
/*     */ 
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
/*     */   public static RewriteAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("AppenderRef") AppenderRef[] appenderRefs, @PluginConfiguration Configuration config, @PluginElement("RewritePolicy") RewritePolicy rewritePolicy, @PluginElement("Filter") Filter filter) {
/* 108 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 109 */     if (name == null) {
/* 110 */       LOGGER.error("No name provided for RewriteAppender");
/* 111 */       return null;
/*     */     } 
/* 113 */     if (appenderRefs == null) {
/* 114 */       LOGGER.error("No appender references defined for RewriteAppender");
/* 115 */       return null;
/*     */     } 
/* 117 */     return new RewriteAppender(name, filter, ignoreExceptions, appenderRefs, rewritePolicy, config, null);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rewrite\RewriteAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */