/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "SortByModificationTime", category = "Core", printObject = true)
/*    */ public class PathSortByModificationTime
/*    */   implements PathSorter, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final boolean recentFirst;
/*    */   private final int multiplier;
/*    */   
/*    */   public PathSortByModificationTime(boolean recentFirst) {
/* 44 */     this.recentFirst = recentFirst;
/* 45 */     this.multiplier = recentFirst ? 1 : -1;
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
/*    */   public static PathSorter createSorter(@PluginAttribute(value = "recentFirst", defaultBoolean = true) boolean recentFirst) {
/* 57 */     return new PathSortByModificationTime(recentFirst);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRecentFirst() {
/* 66 */     return this.recentFirst;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(PathWithAttributes path1, PathWithAttributes path2) {
/* 76 */     long lastModified1 = path1.getAttributes().lastModifiedTime().toMillis();
/* 77 */     long lastModified2 = path2.getAttributes().lastModifiedTime().toMillis();
/* 78 */     int result = Long.signum(lastModified2 - lastModified1);
/* 79 */     if (result == 0) {
/*    */       
/*    */       try {
/*    */         
/* 83 */         result = path2.getPath().compareTo(path1.getPath());
/* 84 */       } catch (ClassCastException ex) {
/* 85 */         result = path2.getPath().toString().compareTo(path1.getPath().toString());
/*    */       } 
/*    */     }
/* 88 */     return this.multiplier * result;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\PathSortByModificationTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */