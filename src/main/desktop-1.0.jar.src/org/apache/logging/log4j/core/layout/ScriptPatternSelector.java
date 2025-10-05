/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.script.SimpleBindings;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.impl.LocationAware;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ScriptPatternSelector", category = "Core", elementType = "patternSelector", printObject = true)
/*     */ public class ScriptPatternSelector
/*     */   implements PatternSelector, LocationAware
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ScriptPatternSelector>
/*     */   {
/*     */     @PluginElement("Script")
/*     */     private AbstractScript script;
/*     */     @PluginElement("PatternMatch")
/*     */     private PatternMatch[] properties;
/*     */     @PluginBuilderAttribute("defaultPattern")
/*     */     private String defaultPattern;
/*     */     @PluginBuilderAttribute("alwaysWriteExceptions")
/*     */     private boolean alwaysWriteExceptions = true;
/*     */     @PluginBuilderAttribute("disableAnsi")
/*     */     private boolean disableAnsi;
/*     */     @PluginBuilderAttribute("noConsoleNoAnsi")
/*     */     private boolean noConsoleNoAnsi;
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     public ScriptPatternSelector build() {
/*  81 */       if (this.script == null) {
/*  82 */         ScriptPatternSelector.LOGGER.error("A Script, ScriptFile or ScriptRef element must be provided for this ScriptFilter");
/*  83 */         return null;
/*     */       } 
/*  85 */       if (this.script instanceof org.apache.logging.log4j.core.script.ScriptRef && 
/*  86 */         this.configuration.getScriptManager().getScript(this.script.getName()) == null) {
/*  87 */         ScriptPatternSelector.LOGGER.error("No script with name {} has been declared.", this.script.getName());
/*  88 */         return null;
/*     */       } 
/*     */       
/*  91 */       if (this.defaultPattern == null) {
/*  92 */         this.defaultPattern = "%m%n";
/*     */       }
/*  94 */       if (this.properties == null || this.properties.length == 0) {
/*  95 */         ScriptPatternSelector.LOGGER.warn("No marker patterns were provided");
/*  96 */         return null;
/*     */       } 
/*  98 */       return new ScriptPatternSelector(this.script, this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration);
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder setScript(AbstractScript script) {
/* 103 */       this.script = script;
/* 104 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProperties(PatternMatch[] properties) {
/* 108 */       this.properties = properties;
/* 109 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDefaultPattern(String defaultPattern) {
/* 113 */       this.defaultPattern = defaultPattern;
/* 114 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 118 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 119 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDisableAnsi(boolean disableAnsi) {
/* 123 */       this.disableAnsi = disableAnsi;
/* 124 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 128 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration config) {
/* 133 */       this.configuration = config;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     private Builder() {} }
/* 138 */   private final Map<String, PatternFormatter[]> formatterMap = (Map)new HashMap<>();
/*     */   
/* 140 */   private final Map<String, String> patternMap = new HashMap<>();
/*     */   
/*     */   private final PatternFormatter[] defaultFormatters;
/*     */   
/*     */   private final String defaultPattern;
/*     */   
/* 146 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final AbstractScript script;
/*     */   
/*     */   private final Configuration configuration;
/*     */   
/*     */   private final boolean requiresLocation;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ScriptPatternSelector(AbstractScript script, PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, Configuration config) {
/* 158 */     this.script = script;
/* 159 */     this.configuration = config;
/* 160 */     if (!(script instanceof org.apache.logging.log4j.core.script.ScriptRef)) {
/* 161 */       config.getScriptManager().addScript(script);
/*     */     }
/* 163 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 164 */     boolean needsLocation = false;
/* 165 */     for (PatternMatch property : properties) {
/*     */       try {
/* 167 */         List<PatternFormatter> list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/* 168 */         PatternFormatter[] formatters = list.<PatternFormatter>toArray(PatternFormatter.EMPTY_ARRAY);
/* 169 */         this.formatterMap.put(property.getKey(), formatters);
/* 170 */         this.patternMap.put(property.getKey(), property.getPattern());
/* 171 */         for (int i = 0; !needsLocation && i < formatters.length; i++) {
/* 172 */           needsLocation = formatters[i].requiresLocation();
/*     */         }
/* 174 */       } catch (RuntimeException ex) {
/* 175 */         throw new IllegalArgumentException("Cannot parse pattern '" + property.getPattern() + "'", ex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 179 */       List<PatternFormatter> list = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/* 180 */       this.defaultFormatters = list.<PatternFormatter>toArray(PatternFormatter.EMPTY_ARRAY);
/* 181 */       this.defaultPattern = defaultPattern;
/* 182 */       for (int i = 0; !needsLocation && i < this.defaultFormatters.length; i++) {
/* 183 */         needsLocation = this.defaultFormatters[i].requiresLocation();
/*     */       }
/* 185 */     } catch (RuntimeException ex) {
/* 186 */       throw new IllegalArgumentException("Cannot parse pattern '" + defaultPattern + "'", ex);
/*     */     } 
/* 188 */     this.requiresLocation = needsLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 193 */     return this.requiresLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public PatternFormatter[] getFormatters(LogEvent event) {
/* 198 */     SimpleBindings bindings = new SimpleBindings();
/* 199 */     bindings.putAll(this.configuration.getProperties());
/* 200 */     bindings.put("substitutor", this.configuration.getStrSubstitutor());
/* 201 */     bindings.put("logEvent", event);
/* 202 */     Object object = this.configuration.getScriptManager().execute(this.script.getName(), bindings);
/* 203 */     if (object == null) {
/* 204 */       return this.defaultFormatters;
/*     */     }
/* 206 */     PatternFormatter[] patternFormatter = this.formatterMap.get(object.toString());
/*     */     
/* 208 */     return (patternFormatter == null) ? this.defaultFormatters : patternFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 219 */     return new Builder();
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
/*     */   @Deprecated
/*     */   public static ScriptPatternSelector createSelector(AbstractScript script, PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration configuration) {
/* 242 */     Builder builder = newBuilder();
/* 243 */     builder.setScript(script);
/* 244 */     builder.setProperties(properties);
/* 245 */     builder.setDefaultPattern(defaultPattern);
/* 246 */     builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
/* 247 */     builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
/* 248 */     builder.setConfiguration(configuration);
/* 249 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 254 */     StringBuilder sb = new StringBuilder();
/* 255 */     boolean first = true;
/* 256 */     for (Map.Entry<String, String> entry : this.patternMap.entrySet()) {
/* 257 */       if (!first) {
/* 258 */         sb.append(", ");
/*     */       }
/* 260 */       sb.append("key=\"").append(entry.getKey()).append("\", pattern=\"").append(entry.getValue()).append("\"");
/* 261 */       first = false;
/*     */     } 
/* 263 */     if (!first) {
/* 264 */       sb.append(", ");
/*     */     }
/* 266 */     sb.append("default=\"").append(this.defaultPattern).append("\"");
/* 267 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\ScriptPatternSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */