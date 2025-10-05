/*     */ package org.apache.logging.log4j.core.appender.db;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ColumnMapping", category = "Core", printObject = true)
/*     */ public class ColumnMapping
/*     */ {
/*  49 */   public static final ColumnMapping[] EMPTY_ARRAY = new ColumnMapping[0];
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ColumnMapping>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     @PluginElement("Layout")
/*     */     private StringLayout layout;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String literal;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No column name provided")
/*     */     private String name;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String parameter;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String pattern;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String source;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No conversion type provided")
/*  78 */     private Class<?> type = String.class;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ColumnMapping build() {
/*  84 */       if (this.pattern != null) {
/*  85 */         this
/*     */ 
/*     */ 
/*     */           
/*  89 */           .layout = (StringLayout)PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).withAlwaysWriteExceptions(false).build();
/*     */       }
/*  91 */       if (this.layout != null && this.literal != null && 
/*     */         
/*  93 */         !Date.class.isAssignableFrom(this.type) && 
/*  94 */         !ReadOnlyStringMap.class.isAssignableFrom(this.type) && 
/*  95 */         !ThreadContextMap.class.isAssignableFrom(this.type) && 
/*  96 */         !ThreadContextStack.class.isAssignableFrom(this.type)) {
/*  97 */         ColumnMapping.LOGGER.error("No 'layout' or 'literal' value specified and type ({}) is not compatible with ThreadContextMap, ThreadContextStack, or java.util.Date for the mapping", this.type, this);
/*  98 */         return null;
/*     */       } 
/* 100 */       if (this.literal != null && this.parameter != null) {
/* 101 */         ColumnMapping.LOGGER.error("Only one of 'literal' or 'parameter' can be set on the column mapping {}", this);
/* 102 */         return null;
/*     */       } 
/* 104 */       return new ColumnMapping(this.name, this.source, this.layout, this.literal, this.parameter, this.type);
/*     */     }
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 108 */       this.configuration = configuration;
/* 109 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLayout(StringLayout layout) {
/* 119 */       this.layout = layout;
/* 120 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLiteral(String literal) {
/* 130 */       this.literal = literal;
/* 131 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setName(String name) {
/* 140 */       this.name = name;
/* 141 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setParameter(String parameter) {
/* 151 */       this.parameter = parameter;
/* 152 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPattern(String pattern) {
/* 162 */       this.pattern = pattern;
/* 163 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSource(String source) {
/* 173 */       this.source = source;
/* 174 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setType(Class<?> type) {
/* 185 */       this.type = type;
/* 186 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 191 */       return "Builder [name=" + this.name + ", source=" + this.source + ", literal=" + this.literal + ", parameter=" + this.parameter + ", pattern=" + this.pattern + ", type=" + this.type + ", layout=" + this.layout + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 196 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger(); private final StringLayout layout; private final String literalValue; private final String name;
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 200 */     return new Builder();
/*     */   }
/*     */   private final String nameKey; private final String parameter; private final String source; private final Class<?> type;
/*     */   public static String toKey(String name) {
/* 204 */     return name.toUpperCase(Locale.ROOT);
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
/*     */   private ColumnMapping(String name, String source, StringLayout layout, String literalValue, String parameter, Class<?> type) {
/* 216 */     this.name = name;
/* 217 */     this.nameKey = toKey(name);
/* 218 */     this.source = source;
/* 219 */     this.layout = layout;
/* 220 */     this.literalValue = literalValue;
/* 221 */     this.parameter = parameter;
/* 222 */     this.type = type;
/*     */   }
/*     */   
/*     */   public StringLayout getLayout() {
/* 226 */     return this.layout;
/*     */   }
/*     */   
/*     */   public String getLiteralValue() {
/* 230 */     return this.literalValue;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 234 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getNameKey() {
/* 238 */     return this.nameKey;
/*     */   }
/*     */   
/*     */   public String getParameter() {
/* 242 */     return this.parameter;
/*     */   }
/*     */   
/*     */   public String getSource() {
/* 246 */     return this.source;
/*     */   }
/*     */   
/*     */   public Class<?> getType() {
/* 250 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 255 */     return "ColumnMapping [name=" + this.name + ", source=" + this.source + ", literalValue=" + this.literalValue + ", parameter=" + this.parameter + ", type=" + this.type + ", layout=" + this.layout + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\ColumnMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */