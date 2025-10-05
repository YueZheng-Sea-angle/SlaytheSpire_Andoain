/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.Objects;
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
/*    */ public class PathWithAttributes
/*    */ {
/*    */   private final Path path;
/*    */   private final BasicFileAttributes attributes;
/*    */   
/*    */   public PathWithAttributes(Path path, BasicFileAttributes attributes) {
/* 33 */     this.path = Objects.<Path>requireNonNull(path, "path");
/* 34 */     this.attributes = Objects.<BasicFileAttributes>requireNonNull(attributes, "attributes");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return this.path + " (modified: " + this.attributes.lastModifiedTime() + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Path getPath() {
/* 48 */     return this.path;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicFileAttributes getAttributes() {
/* 57 */     return this.attributes;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\PathWithAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */