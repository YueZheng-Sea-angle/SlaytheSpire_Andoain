/*    */ package org.apache.logging.log4j.core.config.plugins.visitors;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginVisitorStrategy;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public final class PluginVisitors
/*    */ {
/* 31 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
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
/*    */   public static PluginVisitor<? extends Annotation> findVisitor(Class<? extends Annotation> annotation) {
/* 45 */     PluginVisitorStrategy strategy = annotation.<PluginVisitorStrategy>getAnnotation(PluginVisitorStrategy.class);
/* 46 */     if (strategy == null) {
/* 47 */       return null;
/*    */     }
/*    */     try {
/* 50 */       return strategy.value().newInstance();
/* 51 */     } catch (Exception e) {
/* 52 */       LOGGER.error("Error loading PluginVisitor [{}] for annotation [{}].", strategy.value(), annotation, e);
/* 53 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\visitors\PluginVisitors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */