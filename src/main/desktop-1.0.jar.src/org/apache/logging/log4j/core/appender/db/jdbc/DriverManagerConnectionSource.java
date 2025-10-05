/*    */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Property;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*    */ @Plugin(name = "DriverManager", category = "Core", elementType = "connectionSource", printObject = true)
/*    */ public class DriverManagerConnectionSource
/*    */   extends AbstractDriverManagerConnectionSource
/*    */ {
/*    */   public static class Builder<B extends Builder<B>>
/*    */     extends AbstractDriverManagerConnectionSource.Builder<B>
/*    */     implements org.apache.logging.log4j.core.util.Builder<DriverManagerConnectionSource>
/*    */   {
/*    */     public DriverManagerConnectionSource build() {
/* 48 */       return new DriverManagerConnectionSource(getDriverClassName(), getConnectionString(), getConnectionString(), 
/* 49 */           getUserName(), getPassword(), getProperties());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static <B extends Builder<B>> B newBuilder() {
/* 56 */     return (B)(new Builder<>()).asBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public DriverManagerConnectionSource(String driverClassName, String connectionString, String actualConnectionString, char[] userName, char[] password, Property[] properties) {
/* 61 */     super(driverClassName, connectionString, actualConnectionString, userName, password, properties);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\jdbc\DriverManagerConnectionSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */