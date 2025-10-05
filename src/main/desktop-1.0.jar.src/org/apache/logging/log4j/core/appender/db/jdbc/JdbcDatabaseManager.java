/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.io.StringReader;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLTransactionRollbackException;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
/*     */ import org.apache.logging.log4j.core.appender.db.ColumnMapping;
/*     */ import org.apache.logging.log4j.core.appender.db.DbAppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.config.plugins.convert.DateTypeConverter;
/*     */ import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ public final class JdbcDatabaseManager
/*     */   extends AbstractDatabaseManager
/*     */ {
/*     */   private static final class FactoryData
/*     */     extends AbstractDatabaseManager.AbstractFactoryData
/*     */   {
/*     */     private final ConnectionSource connectionSource;
/*     */     private final String tableName;
/*     */     private final ColumnConfig[] columnConfigs;
/*     */     private final ColumnMapping[] columnMappings;
/*     */     private final boolean immediateFail;
/*     */     private final boolean retry;
/*     */     private final long reconnectIntervalMillis;
/*     */     private final boolean truncateStrings;
/*     */     
/*     */     protected FactoryData(int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings, boolean immediateFail, long reconnectIntervalMillis, boolean truncateStrings) {
/*  83 */       super(bufferSize, layout);
/*  84 */       this.connectionSource = connectionSource;
/*  85 */       this.tableName = tableName;
/*  86 */       this.columnConfigs = columnConfigs;
/*  87 */       this.columnMappings = columnMappings;
/*  88 */       this.immediateFail = immediateFail;
/*  89 */       this.retry = (reconnectIntervalMillis > 0L);
/*  90 */       this.reconnectIntervalMillis = reconnectIntervalMillis;
/*  91 */       this.truncateStrings = truncateStrings;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  96 */       return String.format("FactoryData [connectionSource=%s, tableName=%s, columnConfigs=%s, columnMappings=%s, immediateFail=%s, retry=%s, reconnectIntervalMillis=%s, truncateStrings=%s]", new Object[] { this.connectionSource, this.tableName, 
/*     */             
/*  98 */             Arrays.toString((Object[])this.columnConfigs), Arrays.toString((Object[])this.columnMappings), 
/*  99 */             Boolean.valueOf(this.immediateFail), Boolean.valueOf(this.retry), Long.valueOf(this.reconnectIntervalMillis), Boolean.valueOf(this.truncateStrings) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class JdbcDatabaseManagerFactory
/*     */     implements ManagerFactory<JdbcDatabaseManager, FactoryData>
/*     */   {
/*     */     private static final char PARAMETER_MARKER = '?';
/*     */     
/*     */     private JdbcDatabaseManagerFactory() {}
/*     */     
/*     */     public JdbcDatabaseManager createManager(String name, JdbcDatabaseManager.FactoryData data) {
/* 112 */       StringBuilder sb = (new StringBuilder("insert into ")).append(data.tableName).append(" (");
/*     */ 
/*     */       
/* 115 */       JdbcDatabaseManager.appendColumnNames("INSERT", data, sb);
/* 116 */       sb.append(") values (");
/* 117 */       int i = 1;
/* 118 */       if (data.columnMappings != null) {
/* 119 */         for (ColumnMapping mapping : data.columnMappings) {
/* 120 */           String mappingName = mapping.getName();
/* 121 */           if (Strings.isNotEmpty(mapping.getLiteralValue())) {
/* 122 */             JdbcDatabaseManager.logger().trace("Adding INSERT VALUES literal for ColumnMapping[{}]: {}={} ", Integer.valueOf(i), mappingName, mapping
/* 123 */                 .getLiteralValue());
/* 124 */             sb.append(mapping.getLiteralValue());
/* 125 */           } else if (Strings.isNotEmpty(mapping.getParameter())) {
/* 126 */             JdbcDatabaseManager.logger().trace("Adding INSERT VALUES parameter for ColumnMapping[{}]: {}={} ", Integer.valueOf(i), mappingName, mapping
/* 127 */                 .getParameter());
/* 128 */             sb.append(mapping.getParameter());
/*     */           } else {
/* 130 */             JdbcDatabaseManager.logger().trace("Adding INSERT VALUES parameter marker for ColumnMapping[{}]: {}={} ", Integer.valueOf(i), mappingName, 
/* 131 */                 Character.valueOf('?'));
/* 132 */             sb.append('?');
/*     */           } 
/* 134 */           sb.append(',');
/* 135 */           i++;
/*     */         } 
/*     */       }
/* 138 */       int columnConfigsLen = (data.columnConfigs == null) ? 0 : data.columnConfigs.length;
/* 139 */       List<ColumnConfig> columnConfigs = new ArrayList<>(columnConfigsLen);
/* 140 */       if (data.columnConfigs != null) {
/* 141 */         for (ColumnConfig config : data.columnConfigs) {
/* 142 */           if (Strings.isNotEmpty(config.getLiteralValue())) {
/* 143 */             sb.append(config.getLiteralValue());
/*     */           } else {
/* 145 */             sb.append('?');
/* 146 */             columnConfigs.add(config);
/*     */           } 
/* 148 */           sb.append(',');
/*     */         } 
/*     */       }
/*     */       
/* 152 */       sb.setCharAt(sb.length() - 1, ')');
/* 153 */       String sqlStatement = sb.toString();
/*     */       
/* 155 */       return new JdbcDatabaseManager(name, sqlStatement, columnConfigs, data);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class Reconnector
/*     */     extends Log4jThread
/*     */   {
/* 164 */     private final CountDownLatch latch = new CountDownLatch(1);
/*     */     private volatile boolean shutdown;
/*     */     
/*     */     private Reconnector() {
/* 168 */       super("JdbcDatabaseManager-Reconnector");
/*     */     }
/*     */     
/*     */     public void latch() {
/*     */       try {
/* 173 */         this.latch.await();
/* 174 */       } catch (InterruptedException interruptedException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void reconnect() throws SQLException {
/* 180 */       JdbcDatabaseManager.this.closeResources(false);
/* 181 */       JdbcDatabaseManager.this.connectAndPrepare();
/* 182 */       JdbcDatabaseManager.this.reconnector = null;
/* 183 */       this.shutdown = true;
/* 184 */       JdbcDatabaseManager.logger().debug("Connection reestablished to {}", JdbcDatabaseManager.this.factoryData);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 189 */       while (!this.shutdown) {
/*     */         try {
/* 191 */           sleep(JdbcDatabaseManager.this.factoryData.reconnectIntervalMillis);
/* 192 */           reconnect();
/* 193 */         } catch (InterruptedException|SQLException e) {
/* 194 */           JdbcDatabaseManager.logger().debug("Cannot reestablish JDBC connection to {}: {}", JdbcDatabaseManager.this.factoryData, e.getLocalizedMessage(), e);
/*     */         } finally {
/*     */           
/* 197 */           this.latch.countDown();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 203 */       this.shutdown = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 208 */       return String.format("Reconnector [latch=%s, shutdown=%s]", new Object[] { this.latch, Boolean.valueOf(this.shutdown) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ResultSetColumnMetaData
/*     */   {
/*     */     private final String schemaName;
/*     */     
/*     */     private final String catalogName;
/*     */     private final String tableName;
/*     */     private final String name;
/*     */     private final String nameKey;
/*     */     private final String label;
/*     */     private final int displaySize;
/*     */     private final int type;
/*     */     private final String typeName;
/*     */     private final String className;
/*     */     private final int precision;
/*     */     private final int scale;
/*     */     private final boolean isStringType;
/*     */     
/*     */     public ResultSetColumnMetaData(ResultSetMetaData rsMetaData, int j) throws SQLException {
/* 231 */       this(rsMetaData.getSchemaName(j), rsMetaData
/* 232 */           .getCatalogName(j), rsMetaData
/* 233 */           .getTableName(j), rsMetaData
/* 234 */           .getColumnName(j), rsMetaData
/* 235 */           .getColumnLabel(j), rsMetaData
/* 236 */           .getColumnDisplaySize(j), rsMetaData
/* 237 */           .getColumnType(j), rsMetaData
/* 238 */           .getColumnTypeName(j), rsMetaData
/* 239 */           .getColumnClassName(j), rsMetaData
/* 240 */           .getPrecision(j), rsMetaData
/* 241 */           .getScale(j));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ResultSetColumnMetaData(String schemaName, String catalogName, String tableName, String name, String label, int displaySize, int type, String typeName, String className, int precision, int scale) {
/* 248 */       this.schemaName = schemaName;
/* 249 */       this.catalogName = catalogName;
/* 250 */       this.tableName = tableName;
/* 251 */       this.name = name;
/* 252 */       this.nameKey = ColumnMapping.toKey(name);
/* 253 */       this.label = label;
/* 254 */       this.displaySize = displaySize;
/* 255 */       this.type = type;
/* 256 */       this.typeName = typeName;
/* 257 */       this.className = className;
/* 258 */       this.precision = precision;
/* 259 */       this.scale = scale;
/*     */ 
/*     */       
/* 262 */       this.isStringType = (type == 1 || type == -16 || type == -1 || type == -9 || type == 12);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getCatalogName() {
/* 272 */       return this.catalogName;
/*     */     }
/*     */     
/*     */     public String getClassName() {
/* 276 */       return this.className;
/*     */     }
/*     */     
/*     */     public int getDisplaySize() {
/* 280 */       return this.displaySize;
/*     */     }
/*     */     
/*     */     public String getLabel() {
/* 284 */       return this.label;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 288 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getNameKey() {
/* 292 */       return this.nameKey;
/*     */     }
/*     */     
/*     */     public int getPrecision() {
/* 296 */       return this.precision;
/*     */     }
/*     */     
/*     */     public int getScale() {
/* 300 */       return this.scale;
/*     */     }
/*     */     
/*     */     public String getSchemaName() {
/* 304 */       return this.schemaName;
/*     */     }
/*     */     
/*     */     public String getTableName() {
/* 308 */       return this.tableName;
/*     */     }
/*     */     
/*     */     public int getType() {
/* 312 */       return this.type;
/*     */     }
/*     */     
/*     */     public String getTypeName() {
/* 316 */       return this.typeName;
/*     */     }
/*     */     
/*     */     public boolean isStringType() {
/* 320 */       return this.isStringType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 325 */       return String.format("ColumnMetaData [schemaName=%s, catalogName=%s, tableName=%s, name=%s, nameKey=%s, label=%s, displaySize=%s, type=%s, typeName=%s, className=%s, precision=%s, scale=%s, isStringType=%s]", new Object[] { this.schemaName, this.catalogName, this.tableName, this.name, this.nameKey, this.label, 
/*     */             
/* 327 */             Integer.valueOf(this.displaySize), Integer.valueOf(this.type), this.typeName, this.className, 
/* 328 */             Integer.valueOf(this.precision), Integer.valueOf(this.scale), Boolean.valueOf(this.isStringType) });
/*     */     }
/*     */     
/*     */     public String truncate(String string) {
/* 332 */       return (this.precision > 0) ? Strings.left(string, this.precision) : string;
/*     */     }
/*     */   }
/*     */   private final List<ColumnConfig> columnConfigs;
/* 336 */   private static final JdbcDatabaseManagerFactory INSTANCE = new JdbcDatabaseManagerFactory(); private final String sqlStatement;
/*     */   
/*     */   private static void appendColumnName(int i, String columnName, StringBuilder sb) {
/* 339 */     if (i > 1) {
/* 340 */       sb.append(',');
/*     */     }
/* 342 */     sb.append(columnName);
/*     */   }
/*     */   private final FactoryData factoryData;
/*     */   private volatile Connection connection;
/*     */   private volatile PreparedStatement statement;
/*     */   private volatile Reconnector reconnector;
/*     */   private volatile boolean isBatchSupported;
/*     */   private volatile Map<String, ResultSetColumnMetaData> columnMetaData;
/*     */   
/*     */   private static void appendColumnNames(String sqlVerb, FactoryData data, StringBuilder sb) {
/* 352 */     int i = 1;
/* 353 */     String messagePattern = "Appending {} {}[{}]: {}={} ";
/* 354 */     if (data.columnMappings != null) {
/* 355 */       for (ColumnMapping colMapping : data.columnMappings) {
/* 356 */         String columnName = colMapping.getName();
/* 357 */         appendColumnName(i, columnName, sb);
/* 358 */         logger().trace("Appending {} {}[{}]: {}={} ", sqlVerb, colMapping.getClass().getSimpleName(), Integer.valueOf(i), columnName, colMapping);
/*     */         
/* 360 */         i++;
/*     */       } 
/*     */     }
/* 363 */     if (data.columnConfigs != null) {
/* 364 */       for (ColumnConfig colConfig : data.columnConfigs) {
/* 365 */         String columnName = colConfig.getColumnName();
/* 366 */         appendColumnName(i, columnName, sb);
/* 367 */         logger().trace("Appending {} {}[{}]: {}={} ", sqlVerb, colConfig.getClass().getSimpleName(), Integer.valueOf(i), columnName, colConfig);
/*     */         
/* 369 */         i++;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static JdbcDatabaseManagerFactory getFactory() {
/* 375 */     return INSTANCE;
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
/*     */   @Deprecated
/*     */   public static JdbcDatabaseManager getJDBCDatabaseManager(String name, int bufferSize, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs) {
/* 393 */     return (JdbcDatabaseManager)getManager(name, new FactoryData(bufferSize, null, connectionSource, tableName, columnConfigs, ColumnMapping.EMPTY_ARRAY, false, 5000L, true), 
/*     */ 
/*     */         
/* 396 */         getFactory());
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
/*     */   @Deprecated
/*     */   public static JdbcDatabaseManager getManager(String name, int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings) {
/* 415 */     return (JdbcDatabaseManager)getManager(name, new FactoryData(bufferSize, layout, connectionSource, tableName, columnConfigs, columnMappings, false, 5000L, true), 
/* 416 */         getFactory());
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
/*     */   
/*     */   @Deprecated
/*     */   public static JdbcDatabaseManager getManager(String name, int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings, boolean immediateFail, long reconnectIntervalMillis) {
/* 440 */     return (JdbcDatabaseManager)getManager(name, new FactoryData(bufferSize, null, connectionSource, tableName, columnConfigs, columnMappings, false, 5000L, true), 
/* 441 */         getFactory());
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
/*     */   
/*     */   public static JdbcDatabaseManager getManager(String name, int bufferSize, Layout<? extends Serializable> layout, ConnectionSource connectionSource, String tableName, ColumnConfig[] columnConfigs, ColumnMapping[] columnMappings, boolean immediateFail, long reconnectIntervalMillis, boolean truncateStrings) {
/* 464 */     return (JdbcDatabaseManager)getManager(name, new FactoryData(bufferSize, layout, connectionSource, tableName, columnConfigs, columnMappings, immediateFail, reconnectIntervalMillis, truncateStrings), 
/* 465 */         getFactory());
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
/*     */   private JdbcDatabaseManager(String name, String sqlStatement, List<ColumnConfig> columnConfigs, FactoryData factoryData) {
/* 480 */     super(name, factoryData.getBufferSize());
/* 481 */     this.sqlStatement = sqlStatement;
/* 482 */     this.columnConfigs = columnConfigs;
/* 483 */     this.factoryData = factoryData;
/*     */   }
/*     */   
/*     */   private void checkConnection() {
/* 487 */     boolean connClosed = true;
/*     */     try {
/* 489 */       connClosed = isClosed(this.connection);
/* 490 */     } catch (SQLException sQLException) {}
/*     */ 
/*     */     
/* 493 */     boolean stmtClosed = true;
/*     */     try {
/* 495 */       stmtClosed = isClosed(this.statement);
/* 496 */     } catch (SQLException sQLException) {}
/*     */ 
/*     */     
/* 499 */     if (!isRunning() || connClosed || stmtClosed) {
/*     */       
/* 501 */       closeResources(false);
/*     */       
/* 503 */       if (this.reconnector != null && !this.factoryData.immediateFail) {
/* 504 */         this.reconnector.latch();
/* 505 */         if (this.connection == null)
/* 506 */           throw new AppenderLoggingException("Error writing to JDBC Manager '%s': JDBC connection not available [%s]", new Object[] {
/* 507 */                 getName(), fieldsToString()
/*     */               }); 
/* 509 */         if (this.statement == null)
/* 510 */           throw new AppenderLoggingException("Error writing to JDBC Manager '%s': JDBC statement not available [%s].", new Object[] {
/* 511 */                 getName(), this.connection, fieldsToString()
/*     */               }); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void closeResources(boolean logExceptions) {
/* 518 */     PreparedStatement tempPreparedStatement = this.statement;
/* 519 */     this.statement = null;
/*     */ 
/*     */     
/*     */     try {
/* 523 */       Closer.close(tempPreparedStatement);
/* 524 */     } catch (Exception e) {
/* 525 */       if (logExceptions) {
/* 526 */         logWarn("Failed to close SQL statement logging event or flushing buffer", e);
/*     */       }
/*     */     } 
/*     */     
/* 530 */     Connection tempConnection = this.connection;
/* 531 */     this.connection = null;
/*     */ 
/*     */     
/*     */     try {
/* 535 */       Closer.close(tempConnection);
/* 536 */     } catch (Exception e) {
/* 537 */       if (logExceptions) {
/* 538 */         logWarn("Failed to close database connection logging event or flushing buffer", e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean commitAndClose() {
/* 545 */     boolean closed = true;
/*     */     try {
/* 547 */       if (this.connection != null && !this.connection.isClosed()) {
/* 548 */         if (isBuffered() && this.isBatchSupported && this.statement != null) {
/* 549 */           int[] result; logger().debug("Executing batch PreparedStatement {}", this.statement);
/*     */           
/*     */           try {
/* 552 */             result = this.statement.executeBatch();
/* 553 */           } catch (SQLTransactionRollbackException e) {
/* 554 */             logger().debug("{} executing batch PreparedStatement {}, retrying.", e, this.statement);
/* 555 */             result = this.statement.executeBatch();
/*     */           } 
/* 557 */           logger().debug("Batch result: {}", Arrays.toString(result));
/*     */         } 
/* 559 */         logger().debug("Committing Connection {}", this.connection);
/* 560 */         this.connection.commit();
/*     */       } 
/* 562 */     } catch (SQLException e) {
/* 563 */       throw new DbAppenderLoggingException(e, "Failed to commit transaction logging event or flushing buffer [%s]", new Object[] {
/* 564 */             fieldsToString() });
/*     */     } finally {
/* 566 */       closeResources(true);
/*     */     } 
/* 568 */     return true;
/*     */   }
/*     */   
/*     */   private boolean commitAndCloseAll() {
/* 572 */     if (this.connection != null || this.statement != null) {
/*     */       try {
/* 574 */         commitAndClose();
/* 575 */         return true;
/* 576 */       } catch (AppenderLoggingException e) {
/*     */         
/* 578 */         Throwable cause = e.getCause();
/* 579 */         Throwable actual = (cause == null) ? (Throwable)e : cause;
/* 580 */         logger().debug("{} committing and closing connection: {}", actual, actual.getClass().getSimpleName(), e
/* 581 */             .toString(), e);
/*     */       } 
/*     */     }
/* 584 */     if (this.factoryData.connectionSource != null) {
/* 585 */       this.factoryData.connectionSource.stop();
/*     */     }
/* 587 */     return true;
/*     */   }
/*     */   
/*     */   private void connectAndPrepare() throws SQLException {
/* 591 */     logger().debug("Acquiring JDBC connection from {}", getConnectionSource());
/* 592 */     this.connection = getConnectionSource().getConnection();
/* 593 */     logger().debug("Acquired JDBC connection {}", this.connection);
/* 594 */     logger().debug("Getting connection metadata {}", this.connection);
/* 595 */     DatabaseMetaData databaseMetaData = this.connection.getMetaData();
/* 596 */     logger().debug("Connection metadata {}", databaseMetaData);
/* 597 */     this.isBatchSupported = databaseMetaData.supportsBatchUpdates();
/* 598 */     logger().debug("Connection supportsBatchUpdates: {}", Boolean.valueOf(this.isBatchSupported));
/* 599 */     this.connection.setAutoCommit(false);
/* 600 */     logger().debug("Preparing SQL {}", this.sqlStatement);
/* 601 */     this.statement = this.connection.prepareStatement(this.sqlStatement);
/* 602 */     logger().debug("Prepared SQL {}", this.statement);
/* 603 */     if (this.factoryData.truncateStrings) {
/* 604 */       initColumnMetaData();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connectAndStart() {
/* 610 */     checkConnection();
/* 611 */     synchronized (this) {
/*     */       try {
/* 613 */         connectAndPrepare();
/* 614 */       } catch (SQLException e) {
/* 615 */         reconnectOn(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Reconnector createReconnector() {
/* 621 */     Reconnector recon = new Reconnector();
/* 622 */     recon.setDaemon(true);
/* 623 */     recon.setPriority(1);
/* 624 */     return recon;
/*     */   }
/*     */   
/*     */   private String createSqlSelect() {
/* 628 */     StringBuilder sb = new StringBuilder("select ");
/* 629 */     appendColumnNames("SELECT", this.factoryData, sb);
/* 630 */     sb.append(" from ");
/* 631 */     sb.append(this.factoryData.tableName);
/* 632 */     sb.append(" where 1=0");
/* 633 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String fieldsToString() {
/* 637 */     return String.format("columnConfigs=%s, sqlStatement=%s, factoryData=%s, connection=%s, statement=%s, reconnector=%s, isBatchSupported=%s, columnMetaData=%s", new Object[] { this.columnConfigs, this.sqlStatement, this.factoryData, this.connection, this.statement, this.reconnector, 
/*     */           
/* 639 */           Boolean.valueOf(this.isBatchSupported), this.columnMetaData });
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectionSource getConnectionSource() {
/* 644 */     return this.factoryData.connectionSource;
/*     */   }
/*     */   
/*     */   public String getSqlStatement() {
/* 648 */     return this.sqlStatement;
/*     */   }
/*     */   
/*     */   public String getTableName() {
/* 652 */     return this.factoryData.tableName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initColumnMetaData() throws SQLException {
/* 659 */     String sqlSelect = createSqlSelect();
/* 660 */     logger().debug("Getting SQL metadata for table {}: {}", this.factoryData.tableName, sqlSelect);
/* 661 */     try (PreparedStatement mdStatement = this.connection.prepareStatement(sqlSelect)) {
/* 662 */       ResultSetMetaData rsMetaData = mdStatement.getMetaData();
/* 663 */       logger().debug("SQL metadata: {}", rsMetaData);
/* 664 */       if (rsMetaData != null) {
/* 665 */         int columnCount = rsMetaData.getColumnCount();
/* 666 */         this.columnMetaData = new HashMap<>(columnCount);
/* 667 */         for (int i = 0, j = 1; i < columnCount; i++, j++) {
/* 668 */           ResultSetColumnMetaData value = new ResultSetColumnMetaData(rsMetaData, j);
/* 669 */           this.columnMetaData.put(value.getNameKey(), value);
/*     */         } 
/*     */       } else {
/* 672 */         logger().warn("{}: truncateStrings is true and ResultSetMetaData is null for statement: {}; manager will not perform truncation.", 
/*     */             
/* 674 */             getClass().getSimpleName(), mdStatement);
/*     */       } 
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
/*     */   private boolean isClosed(Statement statement) throws SQLException {
/* 687 */     return (statement == null || statement.isClosed());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isClosed(Connection connection) throws SQLException {
/* 698 */     return (connection == null || connection.isClosed());
/*     */   }
/*     */   
/*     */   private void reconnectOn(Exception exception) {
/* 702 */     if (!this.factoryData.retry) {
/* 703 */       throw new AppenderLoggingException("Cannot connect and prepare", exception);
/*     */     }
/* 705 */     if (this.reconnector == null) {
/* 706 */       this.reconnector = createReconnector();
/*     */       try {
/* 708 */         this.reconnector.reconnect();
/* 709 */       } catch (SQLException reconnectEx) {
/* 710 */         logger().debug("Cannot reestablish JDBC connection to {}: {}; starting reconnector thread {}", this.factoryData, reconnectEx, this.reconnector
/* 711 */             .getName(), reconnectEx);
/* 712 */         this.reconnector.start();
/* 713 */         this.reconnector.latch();
/* 714 */         if (this.connection == null || this.statement == null) {
/* 715 */           throw new AppenderLoggingException(exception, "Error sending to %s for %s [%s]", new Object[] { getName(), this.factoryData, 
/* 716 */                 fieldsToString() });
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setFields(MapMessage<?, ?> mapMessage) throws SQLException {
/* 723 */     IndexedReadOnlyStringMap map = mapMessage.getIndexedReadOnlyStringMap();
/* 724 */     String simpleName = this.statement.getClass().getName();
/* 725 */     int j = 1;
/* 726 */     if (this.factoryData.columnMappings != null) {
/* 727 */       for (ColumnMapping mapping : this.factoryData.columnMappings) {
/* 728 */         if (mapping.getLiteralValue() == null) {
/* 729 */           String source = mapping.getSource();
/* 730 */           String key = Strings.isEmpty(source) ? mapping.getName() : source;
/* 731 */           Object value = map.getValue(key);
/* 732 */           if (logger().isTraceEnabled()) {
/*     */             
/* 734 */             String valueStr = (value instanceof String) ? ("\"" + value + "\"") : Objects.toString(value, null);
/* 735 */             logger().trace("{} setObject({}, {}) for key '{}' and mapping '{}'", simpleName, Integer.valueOf(j), valueStr, key, mapping
/* 736 */                 .getName());
/*     */           } 
/* 738 */           setStatementObject(j, mapping.getNameKey(), value);
/* 739 */           j++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setStatementObject(int j, String nameKey, Object value) throws SQLException {
/* 749 */     if (this.statement == null) {
/* 750 */       throw new AppenderLoggingException("Cannot set a value when the PreparedStatement is null.");
/*     */     }
/* 752 */     if (value == null) {
/* 753 */       if (this.columnMetaData == null) {
/* 754 */         throw new AppenderLoggingException("Cannot set a value when the column metadata is null.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 759 */       this.statement.setNull(j, ((ResultSetColumnMetaData)this.columnMetaData.get(nameKey)).getType());
/*     */     } else {
/* 761 */       this.statement.setObject(j, truncate(nameKey, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shutdownInternal() {
/* 767 */     if (this.reconnector != null) {
/* 768 */       this.reconnector.shutdown();
/* 769 */       this.reconnector.interrupt();
/* 770 */       this.reconnector = null;
/*     */     } 
/* 772 */     return commitAndCloseAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startupInternal() throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object truncate(String nameKey, Object value) {
/* 784 */     if (value != null && this.factoryData.truncateStrings && this.columnMetaData != null) {
/* 785 */       ResultSetColumnMetaData resultSetColumnMetaData = this.columnMetaData.get(nameKey);
/* 786 */       if (resultSetColumnMetaData != null) {
/* 787 */         if (resultSetColumnMetaData.isStringType()) {
/* 788 */           value = resultSetColumnMetaData.truncate(value.toString());
/*     */         }
/*     */       } else {
/* 791 */         logger().error("Missing ResultSetColumnMetaData for {}, connection={}, statement={}", nameKey, this.connection, this.statement);
/*     */       } 
/*     */     } 
/*     */     
/* 795 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(LogEvent event, Serializable serializable) {
/* 800 */     StringReader reader = null;
/*     */     try {
/* 802 */       if (!isRunning() || isClosed(this.connection) || isClosed(this.statement)) {
/* 803 */         throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database, running=%s, [%s]).", new Object[] {
/*     */               
/* 805 */               Boolean.valueOf(isRunning()), fieldsToString()
/*     */             });
/*     */       }
/* 808 */       this.statement.clearParameters();
/* 809 */       if (serializable instanceof MapMessage) {
/* 810 */         setFields((MapMessage<?, ?>)serializable);
/*     */       }
/* 812 */       int j = 1;
/* 813 */       if (this.factoryData.columnMappings != null) {
/* 814 */         for (ColumnMapping mapping : this.factoryData.columnMappings) {
/* 815 */           if (ThreadContextMap.class.isAssignableFrom(mapping.getType()) || ReadOnlyStringMap.class
/* 816 */             .isAssignableFrom(mapping.getType())) {
/* 817 */             this.statement.setObject(j++, event.getContextData().toMap());
/* 818 */           } else if (ThreadContextStack.class.isAssignableFrom(mapping.getType())) {
/* 819 */             this.statement.setObject(j++, event.getContextStack().asList());
/* 820 */           } else if (Date.class.isAssignableFrom(mapping.getType())) {
/* 821 */             this.statement.setObject(j++, DateTypeConverter.fromMillis(event.getTimeMillis(), mapping
/* 822 */                   .getType().asSubclass(Date.class)));
/*     */           } else {
/* 824 */             StringLayout layout = mapping.getLayout();
/* 825 */             if (layout != null) {
/* 826 */               if (Clob.class.isAssignableFrom(mapping.getType())) {
/* 827 */                 this.statement.setClob(j++, new StringReader((String)layout.toSerializable(event)));
/* 828 */               } else if (NClob.class.isAssignableFrom(mapping.getType())) {
/* 829 */                 this.statement.setNClob(j++, new StringReader((String)layout.toSerializable(event)));
/*     */               } else {
/* 831 */                 Object value = TypeConverters.convert((String)layout.toSerializable(event), mapping
/* 832 */                     .getType(), null);
/* 833 */                 setStatementObject(j++, mapping.getNameKey(), value);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/* 839 */       for (ColumnConfig column : this.columnConfigs) {
/* 840 */         if (column.isEventTimestamp()) {
/* 841 */           this.statement.setTimestamp(j++, new Timestamp(event.getTimeMillis())); continue;
/* 842 */         }  if (column.isClob()) {
/* 843 */           reader = new StringReader(column.getLayout().toSerializable(event));
/* 844 */           if (column.isUnicode()) {
/* 845 */             this.statement.setNClob(j++, reader); continue;
/*     */           } 
/* 847 */           this.statement.setClob(j++, reader); continue;
/*     */         } 
/* 849 */         if (column.isUnicode()) {
/* 850 */           this.statement.setNString(j++, Objects.toString(
/* 851 */                 truncate(column.getColumnNameKey(), column.getLayout().toSerializable(event)), null)); continue;
/*     */         } 
/* 853 */         this.statement.setString(j++, Objects.toString(
/* 854 */               truncate(column.getColumnNameKey(), column.getLayout().toSerializable(event)), null));
/*     */       } 
/*     */ 
/*     */       
/* 858 */       if (isBuffered() && this.isBatchSupported) {
/* 859 */         logger().debug("addBatch for {}", this.statement);
/* 860 */         this.statement.addBatch();
/*     */       } else {
/* 862 */         int executeUpdate = this.statement.executeUpdate();
/* 863 */         logger().debug("executeUpdate = {} for {}", Integer.valueOf(executeUpdate), this.statement);
/* 864 */         if (executeUpdate == 0)
/* 865 */           throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager [%s].", new Object[] {
/* 866 */                 fieldsToString()
/*     */               }); 
/*     */       } 
/* 869 */     } catch (SQLException e) {
/* 870 */       throw new DbAppenderLoggingException(e, "Failed to insert record for log event in JDBC manager: %s [%s]", new Object[] { e, 
/* 871 */             fieldsToString() });
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/* 876 */         if (this.statement != null) {
/* 877 */           this.statement.clearParameters();
/*     */         }
/* 879 */       } catch (SQLException sQLException) {}
/*     */ 
/*     */       
/* 882 */       Closer.closeSilently(reader);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeThrough(LogEvent event, Serializable serializable) {
/* 888 */     connectAndStart();
/*     */     try {
/*     */       try {
/* 891 */         writeInternal(event, serializable);
/*     */       } finally {
/* 893 */         commitAndClose();
/*     */       } 
/* 895 */     } catch (DbAppenderLoggingException e) {
/* 896 */       reconnectOn((Exception)e);
/*     */       try {
/* 898 */         writeInternal(event, serializable);
/*     */       } finally {
/* 900 */         commitAndClose();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\db\jdbc\JdbcDatabaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */