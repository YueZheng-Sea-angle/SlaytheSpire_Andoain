/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import javax.script.Bindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.script.AbstractScript;
/*     */ import org.apache.logging.log4j.core.script.ScriptManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptAppenderSelector", category = "Core", elementType = "appender", printObject = true)
/*     */ public class ScriptAppenderSelector
/*     */   extends AbstractAppender
/*     */ {
/*     */   public static final class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<Appender>
/*     */   {
/*     */     @PluginElement("AppenderSet")
/*     */     @Required
/*     */     private AppenderSet appenderSet;
/*     */     @PluginConfiguration
/*     */     @Required
/*     */     private Configuration configuration;
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     private String name;
/*     */     @PluginElement("Script")
/*     */     @Required
/*     */     private AbstractScript script;
/*     */     
/*     */     public Appender build() {
/*  66 */       if (this.name == null) {
/*  67 */         ScriptAppenderSelector.LOGGER.error("Name missing.");
/*  68 */         return null;
/*     */       } 
/*  70 */       if (this.script == null) {
/*  71 */         ScriptAppenderSelector.LOGGER.error("Script missing for ScriptAppenderSelector appender {}", this.name);
/*  72 */         return null;
/*     */       } 
/*  74 */       if (this.appenderSet == null) {
/*  75 */         ScriptAppenderSelector.LOGGER.error("AppenderSet missing for ScriptAppenderSelector appender {}", this.name);
/*  76 */         return null;
/*     */       } 
/*  78 */       if (this.configuration == null) {
/*  79 */         ScriptAppenderSelector.LOGGER.error("Configuration missing for ScriptAppenderSelector appender {}", this.name);
/*  80 */         return null;
/*     */       } 
/*  82 */       ScriptManager scriptManager = this.configuration.getScriptManager();
/*  83 */       scriptManager.addScript(this.script);
/*  84 */       Bindings bindings = scriptManager.createBindings(this.script);
/*  85 */       ScriptAppenderSelector.LOGGER.debug("ScriptAppenderSelector '{}' executing {} '{}': {}", this.name, this.script.getLanguage(), this.script
/*  86 */           .getName(), this.script.getScriptText());
/*  87 */       Object object = scriptManager.execute(this.script.getName(), bindings);
/*  88 */       String actualAppenderName = Objects.toString(object, null);
/*  89 */       ScriptAppenderSelector.LOGGER.debug("ScriptAppenderSelector '{}' selected '{}'", this.name, actualAppenderName);
/*  90 */       return this.appenderSet.createAppender(actualAppenderName, this.name);
/*     */     }
/*     */     
/*     */     public AppenderSet getAppenderSet() {
/*  94 */       return this.appenderSet;
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/*  98 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 102 */       return this.name;
/*     */     }
/*     */     
/*     */     public AbstractScript getScript() {
/* 106 */       return this.script;
/*     */     }
/*     */     
/*     */     public Builder withAppenderNodeSet(AppenderSet appenderSet) {
/* 110 */       this.appenderSet = appenderSet;
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/* 115 */       this.configuration = configuration;
/* 116 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withName(String name) {
/* 120 */       this.name = name;
/* 121 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withScript(AbstractScript script) {
/* 125 */       this.script = script;
/* 126 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 133 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   private ScriptAppenderSelector(String name, Filter filter, Layout<? extends Serializable> layout, Property[] properties) {
/* 138 */     super(name, filter, layout, true, Property.EMPTY_ARRAY);
/*     */   }
/*     */   
/*     */   public void append(LogEvent event) {}
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\ScriptAppenderSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */