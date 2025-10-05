/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class DeletingVisitor
/*     */   extends SimpleFileVisitor<Path>
/*     */ {
/*  37 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Path basePath;
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean testMode;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<? extends PathCondition> pathConditions;
/*     */ 
/*     */ 
/*     */   
/*     */   public DeletingVisitor(Path basePath, List<? extends PathCondition> pathConditions, boolean testMode) {
/*  54 */     this.testMode = testMode;
/*  55 */     this.basePath = Objects.<Path>requireNonNull(basePath, "basePath");
/*  56 */     this.pathConditions = Objects.<List<? extends PathCondition>>requireNonNull(pathConditions, "pathConditions");
/*  57 */     for (PathCondition condition : pathConditions) {
/*  58 */       condition.beforeFileTreeWalk();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/*  64 */     for (PathCondition pathFilter : this.pathConditions) {
/*  65 */       Path relative = this.basePath.relativize(file);
/*  66 */       if (!pathFilter.accept(this.basePath, relative, attrs)) {
/*  67 */         LOGGER.trace("Not deleting base={}, relative={}", this.basePath, relative);
/*  68 */         return FileVisitResult.CONTINUE;
/*     */       } 
/*     */     } 
/*  71 */     if (isTestMode()) {
/*  72 */       LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", file);
/*     */     } else {
/*  74 */       delete(file);
/*     */     } 
/*  76 */     return FileVisitResult.CONTINUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileVisitResult visitFileFailed(Path file, IOException ioException) throws IOException {
/*  83 */     if (ioException instanceof java.nio.file.NoSuchFileException) {
/*  84 */       LOGGER.info("File {} could not be accessed, it has likely already been deleted", file, ioException);
/*  85 */       return FileVisitResult.CONTINUE;
/*     */     } 
/*  87 */     return super.visitFileFailed(file, ioException);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void delete(Path file) throws IOException {
/*  98 */     LOGGER.trace("Deleting {}", file);
/*  99 */     Files.deleteIfExists(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTestMode() {
/* 108 */     return this.testMode;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\DeletingVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */