/*    */ package org.apache.logging.log4j.simple;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.logging.log4j.spi.LoggerContext;
/*    */ import org.apache.logging.log4j.spi.LoggerContextFactory;
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
/*    */ public class SimpleLoggerContextFactory
/*    */   implements LoggerContextFactory
/*    */ {
/* 29 */   private static LoggerContext context = new SimpleLoggerContext();
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
/* 34 */     return context;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, URI configLocation, String name) {
/* 40 */     return context;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeContext(LoggerContext removeContext) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isClassLoaderDependent() {
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\simple\SimpleLoggerContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */