/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
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
/*    */ @Plugin(name = "IfNot", category = "Core", printObject = true)
/*    */ public final class IfNot
/*    */   implements PathCondition
/*    */ {
/*    */   private final PathCondition negate;
/*    */   
/*    */   private IfNot(PathCondition negate) {
/* 37 */     this.negate = Objects.<PathCondition>requireNonNull(negate, "filter");
/*    */   }
/*    */   
/*    */   public PathCondition getWrappedFilter() {
/* 41 */     return this.negate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean accept(Path baseDir, Path relativePath, BasicFileAttributes attrs) {
/* 50 */     return !this.negate.accept(baseDir, relativePath, attrs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void beforeFileTreeWalk() {
/* 59 */     this.negate.beforeFileTreeWalk();
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
/*    */   public static IfNot createNotCondition(@PluginElement("PathConditions") PathCondition condition) {
/* 71 */     return new IfNot(condition);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return "IfNot(" + this.negate + ")";
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfNot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */