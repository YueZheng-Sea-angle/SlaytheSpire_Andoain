/*    */ package org.apache.logging.log4j.core.appender.rolling.action;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileVisitResult;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.SimpleFileVisitor;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Logger;
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
/*    */ public class SortingVisitor
/*    */   extends SimpleFileVisitor<Path>
/*    */ {
/* 39 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   private final PathSorter sorter;
/* 41 */   private final List<PathWithAttributes> collected = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SortingVisitor(PathSorter sorter) {
/* 50 */     this.sorter = Objects.<PathSorter>requireNonNull(sorter, "sorter");
/*    */   }
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 55 */     this.collected.add(new PathWithAttributes(path, attrs));
/* 56 */     return FileVisitResult.CONTINUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FileVisitResult visitFileFailed(Path file, IOException ioException) throws IOException {
/* 63 */     if (ioException instanceof java.nio.file.NoSuchFileException) {
/* 64 */       LOGGER.info("File {} could not be accessed, it has likely already been deleted", file, ioException);
/* 65 */       return FileVisitResult.CONTINUE;
/*    */     } 
/* 67 */     return super.visitFileFailed(file, ioException);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<PathWithAttributes> getSortedPaths() {
/* 72 */     Collections.sort(this.collected, this.sorter);
/* 73 */     return this.collected;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\SortingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */