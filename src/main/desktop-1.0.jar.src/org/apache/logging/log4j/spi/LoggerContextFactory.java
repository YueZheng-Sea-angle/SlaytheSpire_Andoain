/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.net.URI;
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
/*     */ public interface LoggerContextFactory
/*     */ {
/*     */   default void shutdown(String fqcn, ClassLoader loader, boolean currentContext, boolean allContexts) {
/*  36 */     if (hasContext(fqcn, loader, currentContext)) {
/*  37 */       LoggerContext ctx = getContext(fqcn, loader, null, currentContext);
/*  38 */       if (ctx instanceof Terminable) {
/*  39 */         ((Terminable)ctx).terminate();
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
/*     */ 
/*     */   
/*     */   default boolean hasContext(String fqcn, ClassLoader loader, boolean currentContext) {
/*  54 */     return false;
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
/*     */   LoggerContext getContext(String paramString, ClassLoader paramClassLoader, Object paramObject, boolean paramBoolean);
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
/*     */   LoggerContext getContext(String paramString1, ClassLoader paramClassLoader, Object paramObject, boolean paramBoolean, URI paramURI, String paramString2);
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
/*     */   void removeContext(LoggerContext paramLoggerContext);
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
/*     */   default boolean isClassLoaderDependent() {
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\LoggerContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */