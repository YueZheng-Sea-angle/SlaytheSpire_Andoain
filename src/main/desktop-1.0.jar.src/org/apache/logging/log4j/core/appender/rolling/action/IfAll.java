/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "IfAll", category = "Core", printObject = true)
/*     */ public final class IfAll
/*     */   implements PathCondition
/*     */ {
/*     */   private final PathCondition[] components;
/*     */   
/*     */   private IfAll(PathCondition... filters) {
/*  39 */     this.components = Objects.<PathCondition[]>requireNonNull(filters, "filters");
/*     */   }
/*     */   
/*     */   public PathCondition[] getDeleteFilters() {
/*  43 */     return this.components;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Path baseDir, Path relativePath, BasicFileAttributes attrs) {
/*  54 */     if (this.components == null || this.components.length == 0) {
/*  55 */       return false;
/*     */     }
/*  57 */     return accept(this.components, baseDir, relativePath, attrs);
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
/*     */   
/*     */   public static boolean accept(PathCondition[] list, Path baseDir, Path relativePath, BasicFileAttributes attrs) {
/*  72 */     for (PathCondition component : list) {
/*  73 */       if (!component.accept(baseDir, relativePath, attrs)) {
/*  74 */         return false;
/*     */       }
/*     */     } 
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/*  87 */     beforeFileTreeWalk(this.components);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void beforeFileTreeWalk(PathCondition[] nestedConditions) {
/*  96 */     for (PathCondition condition : nestedConditions) {
/*  97 */       condition.beforeFileTreeWalk();
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
/*     */   @PluginFactory
/*     */   public static IfAll createAndCondition(@PluginElement("PathConditions") PathCondition... components) {
/* 110 */     return new IfAll(components);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return "IfAll" + Arrays.toString((Object[])this.components);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */