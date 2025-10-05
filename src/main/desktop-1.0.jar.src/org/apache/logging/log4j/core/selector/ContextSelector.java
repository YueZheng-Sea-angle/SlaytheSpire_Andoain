/*     */ package org.apache.logging.log4j.core.selector;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
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
/*     */ 
/*     */ public interface ContextSelector
/*     */ {
/*     */   public static final long DEFAULT_STOP_TIMEOUT = 50L;
/*     */   
/*     */   default void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/*  43 */     if (hasContext(fqcn, loader, currentContext)) {
/*  44 */       getContext(fqcn, loader, currentContext).stop(50L, TimeUnit.MILLISECONDS);
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
/*     */ 
/*     */   
/*     */   default boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*  58 */     return false;
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
/*     */   LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext) {
/*  81 */     LoggerContext lc = getContext(fqcn, loader, currentContext);
/*  82 */     if (lc != null) {
/*  83 */       lc.putObject(entry.getKey(), entry.getValue());
/*     */     }
/*  85 */     return lc;
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
/*     */   LoggerContext getContext(String paramString, ClassLoader paramClassLoader, boolean paramBoolean, URI paramURI);
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
/*     */   default LoggerContext getContext(String fqcn, ClassLoader loader, Map.Entry<String, Object> entry, boolean currentContext, URI configLocation) {
/* 110 */     LoggerContext lc = getContext(fqcn, loader, currentContext, configLocation);
/* 111 */     if (lc != null) {
/* 112 */       lc.putObject(entry.getKey(), entry.getValue());
/*     */     }
/* 114 */     return lc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<LoggerContext> getLoggerContexts();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeContext(LoggerContext paramLoggerContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isClassLoaderDependent() {
/* 138 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\selector\ContextSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */