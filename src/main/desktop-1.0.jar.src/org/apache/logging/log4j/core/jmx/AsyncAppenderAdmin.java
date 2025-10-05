/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.core.appender.AsyncAppender;
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
/*     */ public class AsyncAppenderAdmin
/*     */   implements AsyncAppenderAdminMBean
/*     */ {
/*     */   private final String contextName;
/*     */   private final AsyncAppender asyncAppender;
/*     */   private final ObjectName objectName;
/*     */   
/*     */   public AsyncAppenderAdmin(String contextName, AsyncAppender appender) {
/*  43 */     this.contextName = Objects.<String>requireNonNull(contextName, "contextName");
/*  44 */     this.asyncAppender = Objects.<AsyncAppender>requireNonNull(appender, "async appender");
/*     */     try {
/*  46 */       String ctxName = Server.escape(this.contextName);
/*  47 */       String configName = Server.escape(appender.getName());
/*  48 */       String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", new Object[] { ctxName, configName });
/*  49 */       this.objectName = new ObjectName(name);
/*  50 */     } catch (Exception e) {
/*  51 */       throw new IllegalStateException(e);
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
/*  62 */     return this.objectName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  67 */     return this.asyncAppender.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLayout() {
/*  72 */     return String.valueOf(this.asyncAppender.getLayout());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoreExceptions() {
/*  77 */     return this.asyncAppender.ignoreExceptions();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getErrorHandler() {
/*  82 */     return String.valueOf(this.asyncAppender.getHandler());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilter() {
/*  87 */     return String.valueOf(this.asyncAppender.getFilter());
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAppenderRefs() {
/*  92 */     return this.asyncAppender.getAppenderRefStrings();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIncludeLocation() {
/* 103 */     return this.asyncAppender.isIncludeLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlocking() {
/* 113 */     return this.asyncAppender.isBlocking();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorRef() {
/* 122 */     return this.asyncAppender.getErrorRef();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueueCapacity() {
/* 127 */     return this.asyncAppender.getQueueCapacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getQueueRemainingCapacity() {
/* 132 */     return this.asyncAppender.getQueueRemainingCapacity();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\AsyncAppenderAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */