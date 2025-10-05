/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
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
/*     */ public class AbstractDriverManagerConnectionSource
/*     */   extends AbstractConnectionSource
/*     */ {
/*     */   public static class Builder<B extends Builder<B>>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     protected String connectionString;
/*     */     @PluginBuilderAttribute
/*     */     protected String driverClassName;
/*     */     @PluginBuilderAttribute
/*     */     protected char[] password;
/*     */     @PluginElement("Properties")
/*     */     protected Property[] properties;
/*     */     @PluginBuilderAttribute
/*     */     protected char[] userName;
/*     */     
/*     */     protected B asBuilder() {
/*  67 */       return (B)this;
/*     */     }
/*     */     
/*     */     public String getConnectionString() {
/*  71 */       return this.connectionString;
/*     */     }
/*     */     
/*     */     public String getDriverClassName() {
/*  75 */       return this.driverClassName;
/*     */     }
/*     */     
/*     */     public char[] getPassword() {
/*  79 */       return this.password;
/*     */     }
/*     */     
/*     */     public Property[] getProperties() {
/*  83 */       return this.properties;
/*     */     }
/*     */     
/*     */     public char[] getUserName() {
/*  87 */       return this.userName;
/*     */     }
/*     */     
/*     */     public B setConnectionString(String connectionString) {
/*  91 */       this.connectionString = connectionString;
/*  92 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setDriverClassName(String driverClassName) {
/*  96 */       this.driverClassName = driverClassName;
/*  97 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setPassword(char[] password) {
/* 101 */       this.password = password;
/* 102 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setProperties(Property[] properties) {
/* 106 */       this.properties = properties;
/* 107 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setUserName(char[] userName) {
/* 111 */       this.userName = userName;
/* 112 */       return asBuilder();
/*     */     }
/*     */   }
/*     */   
/* 116 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   public static Logger getLogger() {
/* 119 */     return LOGGER;
/*     */   }
/*     */ 
/*     */   
/*     */   private final String actualConnectionString;
/*     */   private final String connectionString;
/*     */   private final String driverClassName;
/*     */   private final char[] password;
/*     */   private final Property[] properties;
/*     */   private final char[] userName;
/*     */   
/*     */   public AbstractDriverManagerConnectionSource(String driverClassName, String connectionString, String actualConnectionString, char[] userName, char[] password, Property[] properties) {
/* 131 */     this.driverClassName = driverClassName;
/* 132 */     this.connectionString = connectionString;
/* 133 */     this.actualConnectionString = actualConnectionString;
/* 134 */     this.userName = userName;
/* 135 */     this.password = password;
/* 136 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public String getActualConnectionString() {
/* 140 */     return this.actualConnectionString;
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*     */     Connection connection;
/* 146 */     loadDriver();
/* 147 */     String actualConnectionString = getActualConnectionString();
/* 148 */     LOGGER.debug("{} getting connection for '{}'", getClass().getSimpleName(), actualConnectionString);
/*     */     
/* 150 */     if (this.properties != null && this.properties.length > 0) {
/* 151 */       if (this.userName != null || this.password != null) {
/* 152 */         throw new SQLException("Either set the userName and password, or set the Properties, but not both.");
/*     */       }
/* 154 */       connection = DriverManager.getConnection(actualConnectionString, toProperties(this.properties));
/*     */     } else {
/* 156 */       connection = DriverManager.getConnection(actualConnectionString, toString(this.userName), toString(this.password));
/*     */     } 
/* 158 */     LOGGER.debug("{} acquired connection for '{}': {} ({}@{})", getClass().getSimpleName(), actualConnectionString, connection, connection
/* 159 */         .getClass().getName(), Integer.toHexString(connection.hashCode()));
/* 160 */     return connection;
/*     */   }
/*     */   
/*     */   public String getConnectionString() {
/* 164 */     return this.connectionString;
/*     */   }
/*     */   
/*     */   public String getDriverClassName() {
/* 168 */     return this.driverClassName;
/*     */   }
/*     */   
/*     */   public char[] getPassword() {
/* 172 */     return this.password;
/*     */   }
/*     */   
/*     */   public Property[] getProperties() {
/* 176 */     return this.properties;
/*     */   }
/*     */   
/*     */   public char[] getUserName() {
/* 180 */     return this.userName;
/*     */   }
/*     */   
/*     */   protected void loadDriver() throws SQLException {
/* 184 */     loadDriver(this.driverClassName);
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
/*     */   protected void loadDriver(String className) throws SQLException {
/* 196 */     if (className != null) {
/*     */       
/* 198 */       LOGGER.debug("Loading driver class {}", className);
/*     */       try {
/* 200 */         Class.forName(className);
/* 201 */       } catch (Exception e) {
/* 202 */         throw new SQLException(String.format("The %s could not load the JDBC driver %s: %s", new Object[] {
/* 203 */                 getClass().getSimpleName(), className, e.toString() }), e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Properties toProperties(Property[] properties) {
/* 209 */     Properties props = new Properties();
/* 210 */     for (Property property : properties) {
/* 211 */       props.setProperty(property.getName(), property.getValue());
/*     */     }
/* 213 */     return props;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 218 */     return this.connectionString;
/*     */   }
/*     */   
/*     */   protected String toString(char[] value) {
/* 222 */     return (value == null) ? null : String.valueOf(value);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\jdbc\AbstractDriverManagerConnectionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */