/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AppenderRef;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
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
/*     */ public class LoggerConfigAdmin
/*     */   implements LoggerConfigAdminMBean
/*     */ {
/*     */   private final LoggerContext loggerContext;
/*     */   private final LoggerConfig loggerConfig;
/*     */   private final ObjectName objectName;
/*     */   
/*     */   public LoggerConfigAdmin(LoggerContext loggerContext, LoggerConfig loggerConfig) {
/*  47 */     this.loggerContext = Objects.<LoggerContext>requireNonNull(loggerContext, "loggerContext");
/*  48 */     this.loggerConfig = Objects.<LoggerConfig>requireNonNull(loggerConfig, "loggerConfig");
/*     */     try {
/*  50 */       String ctxName = Server.escape(loggerContext.getName());
/*  51 */       String configName = Server.escape(loggerConfig.getName());
/*  52 */       String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", new Object[] { ctxName, configName });
/*  53 */       this.objectName = new ObjectName(name);
/*  54 */     } catch (Exception e) {
/*  55 */       throw new IllegalStateException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectName getObjectName() {
/*  66 */     return this.objectName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  71 */     return this.loggerConfig.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLevel() {
/*  76 */     return this.loggerConfig.getLevel().name();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLevel(String level) {
/*  81 */     this.loggerConfig.setLevel(Level.getLevel(level));
/*  82 */     this.loggerContext.updateLoggers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAdditive() {
/*  87 */     return this.loggerConfig.isAdditive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAdditive(boolean additive) {
/*  92 */     this.loggerConfig.setAdditive(additive);
/*  93 */     this.loggerContext.updateLoggers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/*  98 */     return this.loggerConfig.isIncludeLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilter() {
/* 103 */     return String.valueOf(this.loggerConfig.getFilter());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAppenderRefs() {
/* 108 */     List<AppenderRef> refs = this.loggerConfig.getAppenderRefs();
/* 109 */     String[] result = new String[refs.size()];
/* 110 */     for (int i = 0; i < result.length; i++) {
/* 111 */       result[i] = ((AppenderRef)refs.get(i)).getRef();
/*     */     }
/* 113 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\LoggerConfigAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */