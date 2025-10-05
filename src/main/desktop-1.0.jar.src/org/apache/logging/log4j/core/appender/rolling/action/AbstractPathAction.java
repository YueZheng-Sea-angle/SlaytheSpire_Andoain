/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitOption;
/*     */ import java.nio.file.FileVisitor;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPathAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private final String basePathString;
/*     */   private final Set<FileVisitOption> options;
/*     */   private final int maxDepth;
/*     */   private final List<PathCondition> pathConditions;
/*     */   private final StrSubstitutor subst;
/*     */   
/*     */   protected AbstractPathAction(String basePath, boolean followSymbolicLinks, int maxDepth, PathCondition[] pathFilters, StrSubstitutor subst) {
/*  59 */     this.basePathString = basePath;
/*  60 */     this
/*  61 */       .options = followSymbolicLinks ? EnumSet.<FileVisitOption>of(FileVisitOption.FOLLOW_LINKS) : Collections.<FileVisitOption>emptySet();
/*  62 */     this.maxDepth = maxDepth;
/*  63 */     this.pathConditions = Arrays.asList(Arrays.copyOf(pathFilters, pathFilters.length));
/*  64 */     this.subst = subst;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean execute() throws IOException {
/*  69 */     return execute(createFileVisitor(getBasePath(), this.pathConditions));
/*     */   }
/*     */   
/*     */   public boolean execute(FileVisitor<Path> visitor) throws IOException {
/*  73 */     long start = System.nanoTime();
/*  74 */     LOGGER.debug("Starting {}", this);
/*     */     
/*  76 */     Files.walkFileTree(getBasePath(), this.options, this.maxDepth, visitor);
/*     */     
/*  78 */     double duration = (System.nanoTime() - start);
/*  79 */     LOGGER.debug("{} complete in {} seconds", getClass().getSimpleName(), Double.valueOf(duration / TimeUnit.SECONDS.toNanos(1L)));
/*     */ 
/*     */     
/*  82 */     return true;
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
/*     */   protected abstract FileVisitor<Path> createFileVisitor(Path paramPath, List<PathCondition> paramList);
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
/*     */   public Path getBasePath() {
/* 106 */     return Paths.get(this.subst.replace(getBasePathString()), new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBasePathString() {
/* 115 */     return this.basePathString;
/*     */   }
/*     */   
/*     */   public StrSubstitutor getStrSubstitutor() {
/* 119 */     return this.subst;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<FileVisitOption> getOptions() {
/* 128 */     return Collections.unmodifiableSet(this.options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFollowSymbolicLinks() {
/* 137 */     return this.options.contains(FileVisitOption.FOLLOW_LINKS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxDepth() {
/* 146 */     return this.maxDepth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PathCondition> getPathConditions() {
/* 155 */     return Collections.unmodifiableList(this.pathConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return getClass().getSimpleName() + "[basePath=" + getBasePath() + ", options=" + this.options + ", maxDepth=" + this.maxDepth + ", conditions=" + this.pathConditions + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\AbstractPathAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */