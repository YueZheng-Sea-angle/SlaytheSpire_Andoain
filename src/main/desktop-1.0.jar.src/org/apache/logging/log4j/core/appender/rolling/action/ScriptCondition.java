/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptCondition", category = "Core", printObject = true)
/*     */ public class ScriptCondition
/*     */ {
/*  46 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   private final AbstractScript script;
/*     */ 
/*     */   
/*     */   private final Configuration configuration;
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptCondition(AbstractScript script, Configuration configuration) {
/*  58 */     this.script = Objects.<AbstractScript>requireNonNull(script, "script");
/*  59 */     this.configuration = Objects.<Configuration>requireNonNull(configuration, "configuration");
/*  60 */     if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef)) {
/*  61 */       configuration.getScriptManager().addScript(script);
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
/*     */   public List<PathWithAttributes> selectFilesToDelete(Path basePath, List<PathWithAttributes> candidates) {
/*  74 */     SimpleBindings bindings = new SimpleBindings();
/*  75 */     bindings.put("basePath", basePath);
/*  76 */     bindings.put("pathList", candidates);
/*  77 */     bindings.putAll(this.configuration.getProperties());
/*  78 */     bindings.put("configuration", this.configuration);
/*  79 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/*  80 */     bindings.put("statusLogger", LOGGER);
/*  81 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/*  82 */     return (List<PathWithAttributes>)object;
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
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static ScriptCondition createCondition(@PluginElement("Script") AbstractScript script, @PluginConfiguration Configuration configuration) {
/* 108 */     if (script == null) {
/* 109 */       LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptCondition");
/* 110 */       return null;
/*     */     } 
/* 112 */     if (script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
/* 113 */       configuration.getScriptManager().getScript(script.getName()) == null) {
/* 114 */       LOGGER.error("ScriptCondition: No script with name {} has been declared.", script.getName());
/* 115 */       return null;
/*     */     } 
/*     */     
/* 118 */     return new ScriptCondition(script, configuration);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\ScriptCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */