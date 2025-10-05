/*    */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
/*    */ import javax.sql.DataSource;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.Strings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "DataSource", category = "Core", elementType = "connectionSource", printObject = true)
/*    */ public final class DataSourceConnectionSource
/*    */   extends AbstractConnectionSource
/*    */ {
/* 39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final DataSource dataSource;
/*    */   private final String description;
/*    */   
/*    */   private DataSourceConnectionSource(String dataSourceName, DataSource dataSource) {
/* 45 */     this.dataSource = dataSource;
/* 46 */     this.description = "dataSource{ name=" + dataSourceName + ", value=" + dataSource + " }";
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getConnection() throws SQLException {
/* 51 */     return this.dataSource.getConnection();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return this.description;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") String jndiName) {
/* 68 */     if (Strings.isEmpty(jndiName)) {
/* 69 */       LOGGER.error("No JNDI name provided.");
/* 70 */       return null;
/*    */     } 
/*    */     
/*    */     try {
/* 74 */       InitialContext context = new InitialContext();
/* 75 */       DataSource dataSource = (DataSource)context.lookup(jndiName);
/* 76 */       if (dataSource == null) {
/* 77 */         LOGGER.error("No data source found with JNDI name [" + jndiName + "].");
/* 78 */         return null;
/*    */       } 
/*    */       
/* 81 */       return new DataSourceConnectionSource(jndiName, dataSource);
/* 82 */     } catch (NamingException e) {
/* 83 */       LOGGER.error(e.getMessage(), e);
/* 84 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\jdbc\DataSourceConnectionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */