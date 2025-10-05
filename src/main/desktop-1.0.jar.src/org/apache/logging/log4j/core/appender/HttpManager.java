/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
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
/*    */ public abstract class HttpManager
/*    */   extends AbstractManager
/*    */ {
/*    */   private final Configuration configuration;
/*    */   
/*    */   protected HttpManager(Configuration configuration, LoggerContext loggerContext, String name) {
/* 31 */     super(loggerContext, name);
/* 32 */     this.configuration = Objects.<Configuration>requireNonNull(configuration);
/*    */   }
/*    */   
/*    */   public Configuration getConfiguration() {
/* 36 */     return this.configuration;
/*    */   }
/*    */   
/*    */   public void startup() {}
/*    */   
/*    */   public abstract void send(Layout<?> paramLayout, LogEvent paramLogEvent) throws Exception;
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\HttpManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */