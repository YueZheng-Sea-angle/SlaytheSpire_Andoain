/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.ObjectMessage;
/*     */ import org.apache.logging.log4j.message.SimpleMessage;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class ScriptFilter
/*     */   extends AbstractFilter
/*     */ {
/*  46 */   private static Logger logger = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final AbstractScript script;
/*     */   
/*     */   private final Configuration configuration;
/*     */   
/*     */   private ScriptFilter(AbstractScript script, Configuration configuration, Filter.Result onMatch, Filter.Result onMismatch) {
/*  53 */     super(onMatch, onMismatch);
/*  54 */     this.script = script;
/*  55 */     this.configuration = configuration;
/*  56 */     if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef)) {
/*  57 */       configuration.getScriptManager().addScript(script);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  64 */     SimpleBindings bindings = new SimpleBindings();
/*  65 */     bindings.put("logger", logger);
/*  66 */     bindings.put("level", level);
/*  67 */     bindings.put("marker", marker);
/*  68 */     bindings.put("message", new SimpleMessage(msg));
/*  69 */     bindings.put("parameters", params);
/*  70 */     bindings.put("throwable", (Object)null);
/*  71 */     bindings.putAll(this.configuration.getProperties());
/*  72 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/*  73 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/*  74 */     return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  80 */     SimpleBindings bindings = new SimpleBindings();
/*  81 */     bindings.put("logger", logger);
/*  82 */     bindings.put("level", level);
/*  83 */     bindings.put("marker", marker);
/*  84 */     bindings.put("message", (msg instanceof String) ? new SimpleMessage((String)msg) : new ObjectMessage(msg));
/*  85 */     bindings.put("parameters", (Object)null);
/*  86 */     bindings.put("throwable", t);
/*  87 */     bindings.putAll(this.configuration.getProperties());
/*  88 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/*  89 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/*  90 */     return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  96 */     SimpleBindings bindings = new SimpleBindings();
/*  97 */     bindings.put("logger", logger);
/*  98 */     bindings.put("level", level);
/*  99 */     bindings.put("marker", marker);
/* 100 */     bindings.put("message", msg);
/* 101 */     bindings.put("parameters", (Object)null);
/* 102 */     bindings.put("throwable", t);
/* 103 */     bindings.putAll(this.configuration.getProperties());
/* 104 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/* 105 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/* 106 */     return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 111 */     SimpleBindings bindings = new SimpleBindings();
/* 112 */     bindings.put("logEvent", event);
/* 113 */     bindings.putAll(this.configuration.getProperties());
/* 114 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/* 115 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/* 116 */     return (object == null || !Boolean.TRUE.equals(object)) ? this.onMismatch : this.onMatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     return this.script.getName();
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
/*     */   @PluginFactory
/*     */   public static ScriptFilter createFilter(@PluginElement("Script") AbstractScript script, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch, @PluginConfiguration Configuration configuration) {
/* 141 */     if (script == null) {
/* 142 */       LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
/* 143 */       return null;
/*     */     } 
/* 145 */     if (script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
/* 146 */       configuration.getScriptManager().getScript(script.getName()) == null) {
/* 147 */       logger.error("No script with name {} has been declared.", script.getName());
/* 148 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 152 */     return new ScriptFilter(script, configuration, match, mismatch);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\ScriptFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */