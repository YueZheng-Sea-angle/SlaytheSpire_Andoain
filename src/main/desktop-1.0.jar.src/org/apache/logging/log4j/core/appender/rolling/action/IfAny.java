/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*    */ @Plugin(name = "IfAny", category = "Core", printObject = true)
/*    */ public final class IfAny
/*    */   implements PathCondition
/*    */ {
/*    */   private final PathCondition[] components;
/*    */   
/*    */   private IfAny(PathCondition... filters) {
/* 39 */     this.components = Objects.<PathCondition[]>requireNonNull(filters, "filters");
/*    */   }
/*    */   
/*    */   public PathCondition[] getDeleteFilters() {
/* 43 */     return this.components;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean accept(Path baseDir, Path relativePath, BasicFileAttributes attrs) {
/* 52 */     for (PathCondition component : this.components) {
/* 53 */       if (component.accept(baseDir, relativePath, attrs)) {
/* 54 */         return true;
/*    */       }
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeFileTreeWalk() {
/* 66 */     for (PathCondition condition : this.components) {
/* 67 */       condition.beforeFileTreeWalk();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @PluginFactory
/*    */   public static IfAny createOrCondition(@PluginElement("PathConditions") PathCondition... components) {
/* 80 */     return new IfAny(components);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return "IfAny" + Arrays.toString((Object[])this.components);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAny.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */