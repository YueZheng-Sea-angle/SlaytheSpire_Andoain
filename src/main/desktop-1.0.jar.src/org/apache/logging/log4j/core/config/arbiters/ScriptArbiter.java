/*     */ package org.apache.logging.log4j.core.config.arbiters;
/*     */ 
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginNode;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
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
/*     */ @Plugin(name = "ScriptArbiter", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
/*     */ public class ScriptArbiter
/*     */   implements Arbiter
/*     */ {
/*     */   private final AbstractScript script;
/*     */   private final Configuration configuration;
/*     */   
/*     */   private ScriptArbiter(Configuration configuration, AbstractScript script) {
/*  45 */     this.configuration = configuration;
/*  46 */     this.script = script;
/*  47 */     if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef)) {
/*  48 */       configuration.getScriptManager().addScript(script);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCondition() {
/*  57 */     SimpleBindings bindings = new SimpleBindings();
/*  58 */     bindings.putAll(this.configuration.getProperties());
/*  59 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/*  60 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/*  61 */     return Boolean.parseBoolean(object.toString());
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  66 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ScriptArbiter> {
/*  71 */     private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */     
/*     */     @PluginConfiguration
/*     */     private AbstractConfiguration configuration;
/*     */     
/*     */     @PluginNode
/*     */     private Node node;
/*     */     
/*     */     public Builder setConfiguration(AbstractConfiguration configuration) {
/*  80 */       this.configuration = configuration;
/*  81 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public Builder setNode(Node node) {
/*  85 */       this.node = node;
/*  86 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public Builder asBuilder() {
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public ScriptArbiter build() {
/*  94 */       AbstractScript script = null;
/*  95 */       for (Node child : this.node.getChildren()) {
/*  96 */         PluginType<?> type = child.getType();
/*  97 */         if (type == null) {
/*  98 */           LOGGER.error("Node {} is missing a Plugintype", child.getName());
/*     */           continue;
/*     */         } 
/* 101 */         if (AbstractScript.class.isAssignableFrom(type.getPluginClass())) {
/* 102 */           script = (AbstractScript)this.configuration.createPluginObject(type, child);
/* 103 */           this.node.getChildren().remove(child);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 108 */       if (script == null) {
/* 109 */         LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
/* 110 */         return null;
/*     */       } 
/* 112 */       if (script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
/* 113 */         this.configuration.getScriptManager().getScript(script.getName()) == null) {
/* 114 */         LOGGER.error("No script with name {} has been declared.", script.getName());
/* 115 */         return null;
/*     */       } 
/*     */       
/* 118 */       return new ScriptArbiter((Configuration)this.configuration, script);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\arbiters\ScriptArbiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */