/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
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
/*     */ @Plugin(name = "LevelPatternSelector", category = "Core", elementType = "patternSelector", printObject = true)
/*     */ public class LevelPatternSelector
/*     */   implements PatternSelector, LocationAware
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<LevelPatternSelector>
/*     */   {
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
/*     */     public LevelPatternSelector build() {
/*  69 */       if (this.defaultPattern == null) {
/*  70 */         this.defaultPattern = "%m%n";
/*     */       }
/*  72 */       if (this.properties == null || this.properties.length == 0) {
/*  73 */         LevelPatternSelector.LOGGER.warn("No marker patterns were provided with PatternMatch");
/*  74 */         return null;
/*     */       } 
/*  76 */       return new LevelPatternSelector(this.properties, this.defaultPattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.configuration);
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder setProperties(PatternMatch[] properties) {
/*  81 */       this.properties = properties;
/*  82 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDefaultPattern(String defaultPattern) {
/*  86 */       this.defaultPattern = defaultPattern;
/*  87 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/*  91 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/*  92 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDisableAnsi(boolean disableAnsi) {
/*  96 */       this.disableAnsi = disableAnsi;
/*  97 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 101 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 102 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 106 */       this.configuration = configuration;
/* 107 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 112 */   private final Map<String, PatternFormatter[]> formatterMap = (Map)new HashMap<>();
/*     */   
/* 114 */   private final Map<String, String> patternMap = new HashMap<>();
/*     */   
/*     */   private final PatternFormatter[] defaultFormatters;
/*     */   
/*     */   private final String defaultPattern;
/*     */   
/* 120 */   private static Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean requiresLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public LevelPatternSelector(PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration config) {
/* 131 */     this(properties, defaultPattern, alwaysWriteExceptions, false, noConsoleNoAnsi, config);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelPatternSelector(PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, Configuration config) {
/* 137 */     boolean needsLocation = false;
/* 138 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 139 */     for (PatternMatch property : properties) {
/*     */       try {
/* 141 */         List<PatternFormatter> list = parser.parse(property.getPattern(), alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/*     */         
/* 143 */         PatternFormatter[] formatters = list.<PatternFormatter>toArray(PatternFormatter.EMPTY_ARRAY);
/* 144 */         this.formatterMap.put(property.getKey(), formatters);
/* 145 */         for (int i = 0; !needsLocation && i < formatters.length; i++) {
/* 146 */           needsLocation = formatters[i].requiresLocation();
/*     */         }
/*     */         
/* 149 */         this.patternMap.put(property.getKey(), property.getPattern());
/* 150 */       } catch (RuntimeException ex) {
/* 151 */         throw new IllegalArgumentException("Cannot parse pattern '" + property.getPattern() + "'", ex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 155 */       List<PatternFormatter> list = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi);
/*     */       
/* 157 */       this.defaultFormatters = list.<PatternFormatter>toArray(PatternFormatter.EMPTY_ARRAY);
/* 158 */       this.defaultPattern = defaultPattern;
/* 159 */       for (int i = 0; !needsLocation && i < this.defaultFormatters.length; i++) {
/* 160 */         needsLocation = this.defaultFormatters[i].requiresLocation();
/*     */       }
/* 162 */     } catch (RuntimeException ex) {
/* 163 */       throw new IllegalArgumentException("Cannot parse pattern '" + defaultPattern + "'", ex);
/*     */     } 
/* 165 */     this.requiresLocation = needsLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 170 */     return this.requiresLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public PatternFormatter[] getFormatters(LogEvent event) {
/* 175 */     Level level = event.getLevel();
/* 176 */     if (level == null) {
/* 177 */       return this.defaultFormatters;
/*     */     }
/* 179 */     for (String key : this.formatterMap.keySet()) {
/* 180 */       if (level.name().equalsIgnoreCase(key)) {
/* 181 */         return this.formatterMap.get(key);
/*     */       }
/*     */     } 
/* 184 */     return this.defaultFormatters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 194 */     return new Builder();
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
/*     */   @Deprecated
/*     */   public static LevelPatternSelector createSelector(PatternMatch[] properties, String defaultPattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi, Configuration configuration) {
/* 214 */     Builder builder = newBuilder();
/* 215 */     builder.setProperties(properties);
/* 216 */     builder.setDefaultPattern(defaultPattern);
/* 217 */     builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
/* 218 */     builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
/* 219 */     builder.setConfiguration(configuration);
/* 220 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     StringBuilder sb = new StringBuilder();
/* 226 */     boolean first = true;
/* 227 */     for (Map.Entry<String, String> entry : this.patternMap.entrySet()) {
/* 228 */       if (!first) {
/* 229 */         sb.append(", ");
/*     */       }
/* 231 */       sb.append("key=\"").append(entry.getKey()).append("\", pattern=\"").append(entry.getValue()).append("\"");
/* 232 */       first = false;
/*     */     } 
/* 234 */     if (!first) {
/* 235 */       sb.append(", ");
/*     */     }
/* 237 */     sb.append("default=\"").append(this.defaultPattern).append("\"");
/* 238 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\LevelPatternSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */