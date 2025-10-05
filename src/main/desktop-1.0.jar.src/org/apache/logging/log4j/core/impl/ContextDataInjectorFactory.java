/*    */ package org.apache.logging.log4j.core.impl;
/*    */ 
/*    */ import org.apache.logging.log4j.ThreadContext;
/*    */ import org.apache.logging.log4j.core.ContextDataInjector;
/*    */ import org.apache.logging.log4j.core.util.Loader;
/*    */ import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*    */ public class ContextDataInjectorFactory
/*    */ {
/*    */   public static ContextDataInjector createInjector() {
/* 69 */     String className = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextDataInjector");
/* 70 */     if (className == null) {
/* 71 */       return createDefaultInjector();
/*    */     }
/*    */     try {
/* 74 */       Class<? extends ContextDataInjector> cls = Loader.loadClass(className).asSubclass(ContextDataInjector.class);
/*    */       
/* 76 */       return cls.newInstance();
/* 77 */     } catch (Exception dynamicFailed) {
/* 78 */       ContextDataInjector result = createDefaultInjector();
/* 79 */       StatusLogger.getLogger().warn("Could not create ContextDataInjector for '{}', using default {}: {}", className, result
/*    */           
/* 81 */           .getClass().getName(), dynamicFailed);
/* 82 */       return result;
/*    */     } 
/*    */   }
/*    */   
/*    */   private static ContextDataInjector createDefaultInjector() {
/* 87 */     ReadOnlyThreadContextMap threadContextMap = ThreadContext.getThreadContextMap();
/*    */ 
/*    */     
/* 90 */     if (threadContextMap instanceof org.apache.logging.log4j.spi.DefaultThreadContextMap || threadContextMap == null) {
/* 91 */       return new ThreadContextDataInjector.ForDefaultThreadContextMap();
/*    */     }
/* 93 */     if (threadContextMap instanceof org.apache.logging.log4j.spi.CopyOnWrite) {
/* 94 */       return new ThreadContextDataInjector.ForCopyOnWriteThreadContextMap();
/*    */     }
/* 96 */     return new ThreadContextDataInjector.ForGarbageFreeThreadContextMap();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ContextDataInjectorFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */