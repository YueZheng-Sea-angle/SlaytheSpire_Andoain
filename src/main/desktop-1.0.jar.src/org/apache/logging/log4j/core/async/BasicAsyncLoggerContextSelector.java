/*    */ package org.apache.logging.log4j.core.async;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*    */ import org.apache.logging.log4j.core.selector.ContextSelector;
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
/*    */ public class BasicAsyncLoggerContextSelector
/*    */   implements ContextSelector
/*    */ {
/* 36 */   private static final AsyncLoggerContext CONTEXT = new AsyncLoggerContext("AsyncDefault");
/*    */ 
/*    */   
/*    */   public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/* 40 */     LoggerContext ctx = getContext(fqcn, loader, currentContext);
/* 41 */     if (ctx != null && ctx.isStarted()) {
/* 42 */       ctx.stop(50L, TimeUnit.MILLISECONDS);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 48 */     LoggerContext ctx = getContext(fqcn, loader, currentContext);
/* 49 */     return (ctx != null && ctx.isStarted());
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 54 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 55 */     return (ctx != null) ? ctx : CONTEXT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 65 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 66 */     return (ctx != null) ? ctx : CONTEXT;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeContext(LoggerContext context) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isClassLoaderDependent() {
/* 76 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<LoggerContext> getLoggerContexts() {
/* 81 */     return Collections.singletonList(CONTEXT);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\async\BasicAsyncLoggerContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */