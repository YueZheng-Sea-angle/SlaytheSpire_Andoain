/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.db.ColumnMapping;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ @Plugin(name = "Column", category = "Core", printObject = true)
/*     */ public final class ColumnConfig
/*     */ {
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<ColumnConfig>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No name provided")
/*     */     private String name;
/*     */     @PluginBuilderAttribute
/*     */     private String pattern;
/*     */     @PluginBuilderAttribute
/*     */     private String literal;
/*     */     @PluginBuilderAttribute
/*     */     private boolean isEventTimestamp;
/*     */     @PluginBuilderAttribute
/*     */     private boolean isUnicode = true;
/*     */     @PluginBuilderAttribute
/*     */     private boolean isClob;
/*     */     
/*     */     public ColumnConfig build() {
/*  66 */       if (Strings.isEmpty(this.name)) {
/*  67 */         ColumnConfig.LOGGER.error("The column config is not valid because it does not contain a column name.");
/*  68 */         return null;
/*     */       } 
/*     */       
/*  71 */       boolean isPattern = Strings.isNotEmpty(this.pattern);
/*  72 */       boolean isLiteralValue = Strings.isNotEmpty(this.literal);
/*     */       
/*  74 */       if ((isPattern && isLiteralValue) || (isPattern && this.isEventTimestamp) || (isLiteralValue && this.isEventTimestamp)) {
/*  75 */         ColumnConfig.LOGGER.error("The pattern, literal, and isEventTimestamp attributes are mutually exclusive.");
/*  76 */         return null;
/*     */       } 
/*     */       
/*  79 */       if (this.isEventTimestamp) {
/*  80 */         return new ColumnConfig(this.name, null, null, true, false, false);
/*     */       }
/*     */       
/*  83 */       if (isLiteralValue) {
/*  84 */         return new ColumnConfig(this.name, null, this.literal, false, false, false);
/*     */       }
/*     */       
/*  87 */       if (isPattern) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  93 */         PatternLayout layout = PatternLayout.newBuilder().withPattern(this.pattern).withConfiguration(this.configuration).withAlwaysWriteExceptions(false).build();
/*  94 */         return new ColumnConfig(this.name, layout, null, false, this.isUnicode, this.isClob);
/*     */       } 
/*     */       
/*  97 */       ColumnConfig.LOGGER.error("To configure a column you must specify a pattern or literal or set isEventDate to true.");
/*  98 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setClob(boolean clob) {
/* 107 */       this.isClob = clob;
/* 108 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setConfiguration(Configuration configuration) {
/* 117 */       this.configuration = configuration;
/* 118 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setEventTimestamp(boolean eventTimestamp) {
/* 128 */       this.isEventTimestamp = eventTimestamp;
/* 129 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setLiteral(String literal) {
/* 139 */       this.literal = literal;
/* 140 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setName(String name) {
/* 149 */       this.name = name;
/* 150 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setPattern(String pattern) {
/* 160 */       this.pattern = pattern;
/* 161 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setUnicode(boolean unicode) {
/* 170 */       this.isUnicode = unicode;
/* 171 */       return this;
/*     */     }
/*     */   }
/*     */   
/* 175 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private final String columnName;
/*     */   private final String columnNameKey;
/*     */   private final PatternLayout layout;
/*     */   private final String literalValue;
/*     */   private final boolean eventTimestamp;
/*     */   private final boolean unicode;
/*     */   private final boolean clob;
/*     */   
/*     */   @Deprecated
/*     */   public static ColumnConfig createColumnConfig(Configuration config, String name, String pattern, String literalValue, String eventTimestamp, String unicode, String clob) {
/* 186 */     if (Strings.isEmpty(name)) {
/* 187 */       LOGGER.error("The column config is not valid because it does not contain a column name.");
/* 188 */       return null;
/*     */     } 
/*     */     
/* 191 */     boolean isEventTimestamp = Boolean.parseBoolean(eventTimestamp);
/* 192 */     boolean isUnicode = Booleans.parseBoolean(unicode, true);
/* 193 */     boolean isClob = Boolean.parseBoolean(clob);
/*     */     
/* 195 */     return newBuilder()
/* 196 */       .setConfiguration(config)
/* 197 */       .setName(name)
/* 198 */       .setPattern(pattern)
/* 199 */       .setLiteral(literalValue)
/* 200 */       .setEventTimestamp(isEventTimestamp)
/* 201 */       .setUnicode(isUnicode)
/* 202 */       .setClob(isClob)
/* 203 */       .build();
/*     */   }
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 207 */     return new Builder();
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
/*     */   private ColumnConfig(String columnName, PatternLayout layout, String literalValue, boolean eventDate, boolean unicode, boolean clob) {
/* 222 */     this.columnName = columnName;
/* 223 */     this.columnNameKey = ColumnMapping.toKey(columnName);
/* 224 */     this.layout = layout;
/* 225 */     this.literalValue = literalValue;
/* 226 */     this.eventTimestamp = eventDate;
/* 227 */     this.unicode = unicode;
/* 228 */     this.clob = clob;
/*     */   }
/*     */   
/*     */   public String getColumnName() {
/* 232 */     return this.columnName;
/*     */   }
/*     */   
/*     */   public String getColumnNameKey() {
/* 236 */     return this.columnNameKey;
/*     */   }
/*     */   
/*     */   public PatternLayout getLayout() {
/* 240 */     return this.layout;
/*     */   }
/*     */   
/*     */   public String getLiteralValue() {
/* 244 */     return this.literalValue;
/*     */   }
/*     */   
/*     */   public boolean isClob() {
/* 248 */     return this.clob;
/*     */   }
/*     */   
/*     */   public boolean isEventTimestamp() {
/* 252 */     return this.eventTimestamp;
/*     */   }
/*     */   
/*     */   public boolean isUnicode() {
/* 256 */     return this.unicode;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 261 */     return "{ name=" + this.columnName + ", layout=" + this.layout + ", literal=" + this.literalValue + ", timestamp=" + this.eventTimestamp + " }";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\jdbc\ColumnConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */