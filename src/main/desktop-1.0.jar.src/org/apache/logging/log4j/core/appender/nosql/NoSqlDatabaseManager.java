/*     */ package org.apache.logging.log4j.core.appender.nosql;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.message.MapMessage;
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
/*     */ public final class NoSqlDatabaseManager<W>
/*     */   extends AbstractDatabaseManager
/*     */ {
/*  37 */   private static final NoSQLDatabaseManagerFactory FACTORY = new NoSQLDatabaseManagerFactory();
/*     */   
/*     */   private final NoSqlProvider<NoSqlConnection<W, ? extends NoSqlObject<W>>> provider;
/*     */   
/*     */   private NoSqlConnection<W, ? extends NoSqlObject<W>> connection;
/*     */ 
/*     */   
/*     */   private NoSqlDatabaseManager(String name, int bufferSize, NoSqlProvider<NoSqlConnection<W, ? extends NoSqlObject<W>>> provider) {
/*  45 */     super(name, bufferSize);
/*  46 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startupInternal() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shutdownInternal() {
/*  57 */     return Closer.closeSilently(this.connection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connectAndStart() {
/*     */     try {
/*  63 */       this.connection = this.provider.getConnection();
/*  64 */     } catch (Exception e) {
/*  65 */       throw new AppenderLoggingException("Failed to get connection from NoSQL connection provider.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(LogEvent event, Serializable serializable) {
/*  71 */     if (!isRunning() || this.connection == null || this.connection.isClosed()) {
/*  72 */       throw new AppenderLoggingException("Cannot write logging event; NoSQL manager not connected to the database.");
/*     */     }
/*     */ 
/*     */     
/*  76 */     NoSqlObject<W> entity = this.connection.createObject();
/*  77 */     if (serializable instanceof MapMessage) {
/*  78 */       setFields((MapMessage<?, ?>)serializable, entity);
/*     */     } else {
/*  80 */       setFields(event, entity);
/*     */     } 
/*     */     
/*  83 */     this.connection.insertObject(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setFields(MapMessage<?, ?> mapMessage, NoSqlObject<W> noSqlObject) {
/*  88 */     mapMessage.forEach((key, value) -> noSqlObject.set(key, value));
/*     */   }
/*     */   
/*     */   private void setFields(LogEvent event, NoSqlObject<W> entity) {
/*  92 */     entity.set("level", event.getLevel());
/*  93 */     entity.set("loggerName", event.getLoggerName());
/*  94 */     entity.set("message", (event.getMessage() == null) ? null : event.getMessage().getFormattedMessage());
/*     */     
/*  96 */     StackTraceElement source = event.getSource();
/*  97 */     if (source == null) {
/*  98 */       entity.set("source", (Object)null);
/*     */     } else {
/* 100 */       entity.set("source", convertStackTraceElement(source));
/*     */     } 
/*     */     
/* 103 */     Marker marker = event.getMarker();
/* 104 */     if (marker == null) {
/* 105 */       entity.set("marker", (Object)null);
/*     */     } else {
/* 107 */       entity.set("marker", buildMarkerEntity(marker));
/*     */     } 
/*     */     
/* 110 */     entity.set("threadId", Long.valueOf(event.getThreadId()));
/* 111 */     entity.set("threadName", event.getThreadName());
/* 112 */     entity.set("threadPriority", Integer.valueOf(event.getThreadPriority()));
/* 113 */     entity.set("millis", Long.valueOf(event.getTimeMillis()));
/* 114 */     entity.set("date", new Date(event.getTimeMillis()));
/*     */ 
/*     */     
/* 117 */     Throwable thrown = event.getThrown();
/* 118 */     if (thrown == null) {
/* 119 */       entity.set("thrown", (Object)null);
/*     */     } else {
/* 121 */       NoSqlObject<W> originalExceptionEntity = this.connection.createObject();
/* 122 */       NoSqlObject<W> exceptionEntity = originalExceptionEntity;
/* 123 */       exceptionEntity.set("type", thrown.getClass().getName());
/* 124 */       exceptionEntity.set("message", thrown.getMessage());
/* 125 */       exceptionEntity.set("stackTrace", convertStackTrace(thrown.getStackTrace()));
/* 126 */       while (thrown.getCause() != null) {
/* 127 */         thrown = thrown.getCause();
/* 128 */         NoSqlObject<W> causingExceptionEntity = this.connection.createObject();
/* 129 */         causingExceptionEntity.set("type", thrown.getClass().getName());
/* 130 */         causingExceptionEntity.set("message", thrown.getMessage());
/* 131 */         causingExceptionEntity.set("stackTrace", convertStackTrace(thrown.getStackTrace()));
/* 132 */         exceptionEntity.set("cause", causingExceptionEntity);
/* 133 */         exceptionEntity = causingExceptionEntity;
/*     */       } 
/*     */       
/* 136 */       entity.set("thrown", originalExceptionEntity);
/*     */     } 
/*     */     
/* 139 */     ReadOnlyStringMap contextMap = event.getContextData();
/* 140 */     if (contextMap == null) {
/* 141 */       entity.set("contextMap", (Object)null);
/*     */     } else {
/* 143 */       NoSqlObject<W> contextMapEntity = this.connection.createObject();
/* 144 */       contextMap.forEach((key, val) -> contextMapEntity.set(key, val));
/* 145 */       entity.set("contextMap", contextMapEntity);
/*     */     } 
/*     */     
/* 148 */     ThreadContext.ContextStack contextStack = event.getContextStack();
/* 149 */     if (contextStack == null) {
/* 150 */       entity.set("contextStack", (Object)null);
/*     */     } else {
/* 152 */       entity.set("contextStack", contextStack.asList().toArray());
/*     */     } 
/*     */   }
/*     */   
/*     */   private NoSqlObject<W> buildMarkerEntity(Marker marker) {
/* 157 */     NoSqlObject<W> entity = this.connection.createObject();
/* 158 */     entity.set("name", marker.getName());
/*     */     
/* 160 */     Marker[] parents = marker.getParents();
/* 161 */     if (parents != null) {
/*     */       
/* 163 */       NoSqlObject[] arrayOfNoSqlObject = new NoSqlObject[parents.length];
/* 164 */       for (int i = 0; i < parents.length; i++) {
/* 165 */         arrayOfNoSqlObject[i] = buildMarkerEntity(parents[i]);
/*     */       }
/* 167 */       entity.set("parents", (NoSqlObject<W>[])arrayOfNoSqlObject);
/*     */     } 
/* 169 */     return entity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean commitAndClose() {
/* 178 */     return true;
/*     */   }
/*     */   
/*     */   private NoSqlObject<W>[] convertStackTrace(StackTraceElement[] stackTrace) {
/* 182 */     NoSqlObject<W>[] stackTraceEntities = this.connection.createList(stackTrace.length);
/* 183 */     for (int i = 0; i < stackTrace.length; i++) {
/* 184 */       stackTraceEntities[i] = convertStackTraceElement(stackTrace[i]);
/*     */     }
/* 186 */     return stackTraceEntities;
/*     */   }
/*     */   
/*     */   private NoSqlObject<W> convertStackTraceElement(StackTraceElement element) {
/* 190 */     NoSqlObject<W> elementEntity = this.connection.createObject();
/* 191 */     elementEntity.set("className", element.getClassName());
/* 192 */     elementEntity.set("methodName", element.getMethodName());
/* 193 */     elementEntity.set("fileName", element.getFileName());
/* 194 */     elementEntity.set("lineNumber", Integer.valueOf(element.getLineNumber()));
/* 195 */     return elementEntity;
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
/*     */   public static NoSqlDatabaseManager<?> getNoSqlDatabaseManager(String name, int bufferSize, NoSqlProvider<?> provider) {
/* 208 */     return (NoSqlDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, provider), FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FactoryData
/*     */     extends AbstractDatabaseManager.AbstractFactoryData
/*     */   {
/*     */     private final NoSqlProvider<?> provider;
/*     */     
/*     */     protected FactoryData(int bufferSize, NoSqlProvider<?> provider) {
/* 218 */       super(bufferSize, null);
/* 219 */       this.provider = provider;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class NoSQLDatabaseManagerFactory
/*     */     implements ManagerFactory<NoSqlDatabaseManager<?>, FactoryData>
/*     */   {
/*     */     private NoSQLDatabaseManagerFactory() {}
/*     */ 
/*     */     
/*     */     public NoSqlDatabaseManager<?> createManager(String name, NoSqlDatabaseManager.FactoryData data) {
/* 231 */       return new NoSqlDatabaseManager(name, data.getBufferSize(), data.provider);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\nosql\NoSqlDatabaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */