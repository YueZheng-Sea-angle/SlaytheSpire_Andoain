/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitor;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Delete", category = "Core", printObject = true)
/*     */ public class DeleteAction
/*     */   extends AbstractPathAction
/*     */ {
/*     */   private final PathSorter pathSorter;
/*     */   private final boolean testMode;
/*     */   private final ScriptCondition scriptCondition;
/*     */   
/*     */   DeleteAction(String basePath, boolean followSymbolicLinks, int maxDepth, boolean testMode, PathSorter sorter, PathCondition[] pathConditions, ScriptCondition scriptCondition, StrSubstitutor subst) {
/*  65 */     super(basePath, followSymbolicLinks, maxDepth, pathConditions, subst);
/*  66 */     this.testMode = testMode;
/*  67 */     this.pathSorter = Objects.<PathSorter>requireNonNull(sorter, "sorter");
/*  68 */     this.scriptCondition = scriptCondition;
/*  69 */     if (scriptCondition == null && (pathConditions == null || pathConditions.length == 0)) {
/*  70 */       LOGGER.error("Missing Delete conditions: unconditional Delete not supported");
/*  71 */       throw new IllegalArgumentException("Unconditional Delete not supported");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws IOException {
/*  82 */     return (this.scriptCondition != null) ? executeScript() : super.execute();
/*     */   }
/*     */   
/*     */   private boolean executeScript() throws IOException {
/*  86 */     List<PathWithAttributes> selectedForDeletion = callScript();
/*  87 */     if (selectedForDeletion == null) {
/*  88 */       LOGGER.trace("Script returned null list (no files to delete)");
/*  89 */       return true;
/*     */     } 
/*  91 */     deleteSelectedFiles(selectedForDeletion);
/*  92 */     return true;
/*     */   }
/*     */   
/*     */   private List<PathWithAttributes> callScript() throws IOException {
/*  96 */     List<PathWithAttributes> sortedPaths = getSortedPaths();
/*  97 */     trace("Sorted paths:", sortedPaths);
/*  98 */     List<PathWithAttributes> result = this.scriptCondition.selectFilesToDelete(getBasePath(), sortedPaths);
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   private void deleteSelectedFiles(List<PathWithAttributes> selectedForDeletion) throws IOException {
/* 103 */     trace("Paths the script selected for deletion:", selectedForDeletion);
/* 104 */     for (PathWithAttributes pathWithAttributes : selectedForDeletion) {
/* 105 */       Path path = (pathWithAttributes == null) ? null : pathWithAttributes.getPath();
/* 106 */       if (isTestMode()) {
/* 107 */         LOGGER.info("Deleting {} (TEST MODE: file not actually deleted)", path); continue;
/*     */       } 
/* 109 */       delete(path);
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
/*     */   protected void delete(Path path) throws IOException {
/* 121 */     LOGGER.trace("Deleting {}", path);
/* 122 */     Files.deleteIfExists(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(FileVisitor<Path> visitor) throws IOException {
/* 132 */     List<PathWithAttributes> sortedPaths = getSortedPaths();
/* 133 */     trace("Sorted paths:", sortedPaths);
/*     */     
/* 135 */     for (PathWithAttributes element : sortedPaths) {
/*     */       try {
/* 137 */         visitor.visitFile(element.getPath(), element.getAttributes());
/* 138 */       } catch (IOException ioex) {
/* 139 */         LOGGER.error("Error in post-rollover Delete when visiting {}", element.getPath(), ioex);
/* 140 */         visitor.visitFileFailed(element.getPath(), ioex);
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     return true;
/*     */   }
/*     */   
/*     */   private void trace(String label, List<PathWithAttributes> sortedPaths) {
/* 148 */     LOGGER.trace(label);
/* 149 */     for (PathWithAttributes pathWithAttributes : sortedPaths) {
/* 150 */       LOGGER.trace(pathWithAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<PathWithAttributes> getSortedPaths() throws IOException {
/* 161 */     SortingVisitor sort = new SortingVisitor(this.pathSorter);
/* 162 */     super.execute(sort);
/* 163 */     List<PathWithAttributes> sortedPaths = sort.getSortedPaths();
/* 164 */     return sortedPaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTestMode() {
/* 173 */     return this.testMode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FileVisitor<Path> createFileVisitor(Path visitorBaseDir, List<PathCondition> conditions) {
/* 178 */     return new DeletingVisitor(visitorBaseDir, conditions, this.testMode);
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
/*     */   @PluginFactory
/*     */   public static DeleteAction createDeleteAction(@PluginAttribute("basePath") String basePath, @PluginAttribute("followLinks") boolean followLinks, @PluginAttribute(value = "maxDepth", defaultInt = 1) int maxDepth, @PluginAttribute("testMode") boolean testMode, @PluginElement("PathSorter") PathSorter sorterParameter, @PluginElement("PathConditions") PathCondition[] pathConditions, @PluginElement("ScriptCondition") ScriptCondition scriptCondition, @PluginConfiguration Configuration config) {
/* 211 */     PathSorter sorter = (sorterParameter == null) ? new PathSortByModificationTime(true) : sorterParameter;
/* 212 */     return new DeleteAction(basePath, followLinks, maxDepth, testMode, sorter, pathConditions, scriptCondition, config
/* 213 */         .getStrSubstitutor());
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\DeleteAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */