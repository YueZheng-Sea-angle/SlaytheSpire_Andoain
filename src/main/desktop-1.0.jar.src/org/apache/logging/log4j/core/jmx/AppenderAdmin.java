/*    */ package org.apache.logging.log4j.core.jmx;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.management.ObjectName;
/*    */ import org.apache.logging.log4j.core.Appender;
/*    */ import org.apache.logging.log4j.core.filter.AbstractFilterable;
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
/*    */ public class AppenderAdmin
/*    */   implements AppenderAdminMBean
/*    */ {
/*    */   private final String contextName;
/*    */   private final Appender appender;
/*    */   private final ObjectName objectName;
/*    */   
/*    */   public AppenderAdmin(String contextName, Appender appender) {
/* 44 */     this.contextName = Objects.<String>requireNonNull(contextName, "contextName");
/* 45 */     this.appender = Objects.<Appender>requireNonNull(appender, "appender");
/*    */     try {
/* 47 */       String ctxName = Server.escape(this.contextName);
/* 48 */       String configName = Server.escape(appender.getName());
/* 49 */       String name = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { ctxName, configName });
/* 50 */       this.objectName = new ObjectName(name);
/* 51 */     } catch (Exception e) {
/* 52 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectName getObjectName() {
/* 63 */     return this.objectName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 68 */     return this.appender.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLayout() {
/* 73 */     return String.valueOf(this.appender.getLayout());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isIgnoreExceptions() {
/* 78 */     return this.appender.ignoreExceptions();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getErrorHandler() {
/* 83 */     return String.valueOf(this.appender.getHandler());
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFilter() {
/* 88 */     if (this.appender instanceof AbstractFilterable) {
/* 89 */       return String.valueOf(((AbstractFilterable)this.appender).getFilter());
/*    */     }
/* 91 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\jmx\AppenderAdmin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */