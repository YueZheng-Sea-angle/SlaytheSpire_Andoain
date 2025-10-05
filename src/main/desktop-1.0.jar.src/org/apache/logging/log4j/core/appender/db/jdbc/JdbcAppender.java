/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.ColumnMapping;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "JDBC", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class JdbcAppender
/*     */   extends AbstractDatabaseAppender<JdbcDatabaseManager>
/*     */ {
/*     */   private final String description;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractDatabaseAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<JdbcAppender>
/*     */   {
/*     */     @PluginElement("ConnectionSource")
/*     */     @Required(message = "No ConnectionSource provided")
/*     */     private ConnectionSource connectionSource;
/*     */     @PluginBuilderAttribute
/*     */     private boolean immediateFail;
/*     */     @PluginBuilderAttribute
/*     */     private int bufferSize;
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No table name provided")
/*     */     private String tableName;
/*     */     @PluginElement("ColumnConfigs")
/*     */     private ColumnConfig[] columnConfigs;
/*     */     @PluginElement("ColumnMappings")
/*     */     private ColumnMapping[] columnMappings;
/*     */     @PluginBuilderAttribute
/*     */     private boolean truncateStrings = true;
/*     */     @PluginBuilderAttribute
/*  83 */     private long reconnectIntervalMillis = 5000L;
/*     */ 
/*     */ 
/*     */     
/*     */     public JdbcAppender build() {
/*  88 */       if (Assert.isEmpty(this.columnConfigs) && Assert.isEmpty(this.columnMappings)) {
/*  89 */         JdbcAppender.LOGGER.error("Cannot create JdbcAppender without any columns.");
/*  90 */         return null;
/*     */       } 
/*     */ 
/*     */       
/*  94 */       String managerName = "JdbcManager{name=" + getName() + ", bufferSize=" + this.bufferSize + ", tableName=" + this.tableName + ", columnConfigs=" + Arrays.toString((Object[])this.columnConfigs) + ", columnMappings=" + Arrays.toString((Object[])this.columnMappings) + '}';
/*  95 */       JdbcDatabaseManager manager = JdbcDatabaseManager.getManager(managerName, this.bufferSize, getLayout(), this.connectionSource, this.tableName, this.columnConfigs, this.columnMappings, this.immediateFail, this.reconnectIntervalMillis, this.truncateStrings);
/*     */ 
/*     */       
/*  98 */       if (manager == null) {
/*  99 */         return null;
/*     */       }
/* 101 */       return new JdbcAppender(getName(), getFilter(), getLayout(), isIgnoreExceptions(), getPropertyArray(), manager);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getReconnectIntervalMillis() {
/* 106 */       return this.reconnectIntervalMillis;
/*     */     }
/*     */     
/*     */     public boolean isImmediateFail() {
/* 110 */       return this.immediateFail;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setBufferSize(int bufferSize) {
/* 122 */       this.bufferSize = bufferSize;
/* 123 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setColumnConfigs(ColumnConfig... columnConfigs) {
/* 134 */       this.columnConfigs = columnConfigs;
/* 135 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setColumnMappings(ColumnMapping... columnMappings) {
/* 139 */       this.columnMappings = columnMappings;
/* 140 */       return (B)asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setConnectionSource(ConnectionSource connectionSource) {
/* 151 */       this.connectionSource = connectionSource;
/* 152 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public void setImmediateFail(boolean immediateFail) {
/* 156 */       this.immediateFail = immediateFail;
/*     */     }
/*     */     
/*     */     public void setReconnectIntervalMillis(long reconnectIntervalMillis) {
/* 160 */       this.reconnectIntervalMillis = reconnectIntervalMillis;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setTableName(String tableName) {
/* 171 */       this.tableName = tableName;
/* 172 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setTruncateStrings(boolean truncateStrings) {
/* 176 */       this.truncateStrings = truncateStrings;
/* 177 */       return (B)asBuilder();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> JdbcAppender createAppender(String name, String ignore, Filter filter, ConnectionSource connectionSource, String bufferSize, String tableName, ColumnConfig[] columnConfigs) {
/* 194 */     Assert.requireNonEmpty(name, "Name cannot be empty");
/* 195 */     Objects.requireNonNull(connectionSource, "ConnectionSource cannot be null");
/* 196 */     Assert.requireNonEmpty(tableName, "Table name cannot be empty");
/* 197 */     Assert.requireNonEmpty(columnConfigs, "ColumnConfigs cannot be empty");
/*     */     
/* 199 */     int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
/* 200 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/* 202 */     return ((Builder)((Builder)((Builder)newBuilder()
/* 203 */       .setBufferSize(bufferSizeInt)
/* 204 */       .setColumnConfigs(columnConfigs)
/* 205 */       .setConnectionSource(connectionSource)
/* 206 */       .setTableName(tableName).setName(name)).setIgnoreExceptions(ignoreExceptions)).setFilter(filter))
/* 207 */       .build();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 212 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JdbcAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties, JdbcDatabaseManager manager) {
/* 219 */     super(name, filter, layout, ignoreExceptions, properties, manager);
/* 220 */     this.description = getName() + "{ manager=" + getManager() + " }";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     return this.description;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\jdbc\JdbcAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */