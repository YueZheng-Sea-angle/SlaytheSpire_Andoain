/*    */ package org.apache.logging.log4j.core.selector;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.impl.ContextAnchor;
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
/*    */ public class BasicContextSelector
/*    */   implements ContextSelector
/*    */ {
/* 33 */   private static final LoggerContext CONTEXT = new LoggerContext("Default");
/*    */ 
/*    */   
/*    */   public void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/* 37 */     LoggerContext ctx = getContext(fqcn, loader, currentContext);
/* 38 */     if (ctx != null && ctx.isStarted()) {
/* 39 */       ctx.stop(50L, TimeUnit.MILLISECONDS);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 45 */     LoggerContext ctx = getContext(fqcn, loader, currentContext);
/* 46 */     return (ctx != null && ctx.isStarted());
/*    */   }
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
/* 51 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 52 */     return (ctx != null) ? ctx : CONTEXT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 60 */     LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 61 */     return (ctx != null) ? ctx : CONTEXT;
/*    */   }
/*    */   
/*    */   public LoggerContext locateContext(String name, String configLocation) {
/* 65 */     return CONTEXT;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeContext(LoggerContext context) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isClassLoaderDependent() {
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<LoggerContext> getLoggerContexts() {
/* 80 */     List<LoggerContext> list = new ArrayList<>();
/* 81 */     list.add(CONTEXT);
/* 82 */     return Collections.unmodifiableList(list);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\selector\BasicContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */