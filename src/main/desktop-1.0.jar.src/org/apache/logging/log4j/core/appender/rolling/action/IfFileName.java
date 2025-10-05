/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "IfFileName", category = "Core", printObject = true)
/*     */ public final class IfFileName
/*     */   implements PathCondition
/*     */ {
/*  47 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private final PathMatcher pathMatcher;
/*     */ 
/*     */   
/*     */   private final String syntaxAndPattern;
/*     */ 
/*     */   
/*     */   private final PathCondition[] nestedConditions;
/*     */ 
/*     */ 
/*     */   
/*     */   private IfFileName(String glob, String regex, PathCondition... nestedConditions) {
/*  61 */     if (regex == null && glob == null) {
/*  62 */       throw new IllegalArgumentException("Specify either a path glob or a regular expression. Both cannot be null.");
/*     */     }
/*     */     
/*  65 */     this.syntaxAndPattern = createSyntaxAndPatternString(glob, regex);
/*  66 */     this.pathMatcher = FileSystems.getDefault().getPathMatcher(this.syntaxAndPattern);
/*  67 */     this.nestedConditions = PathCondition.copy(nestedConditions);
/*     */   }
/*     */   
/*     */   static String createSyntaxAndPatternString(String glob, String regex) {
/*  71 */     if (glob != null) {
/*  72 */       return glob.startsWith("glob:") ? glob : ("glob:" + glob);
/*     */     }
/*  74 */     return regex.startsWith("regex:") ? regex : ("regex:" + regex);
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
/*     */   public String getSyntaxAndPattern() {
/*  86 */     return this.syntaxAndPattern;
/*     */   }
/*     */   
/*     */   public List<PathCondition> getNestedConditions() {
/*  90 */     return Collections.unmodifiableList(Arrays.asList(this.nestedConditions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accept(Path basePath, Path relativePath, BasicFileAttributes attrs) {
/* 101 */     boolean result = this.pathMatcher.matches(relativePath);
/*     */     
/* 103 */     String match = result ? "matches" : "does not match";
/* 104 */     String accept = result ? "ACCEPTED" : "REJECTED";
/* 105 */     LOGGER.trace("IfFileName {}: '{}' {} relative path '{}'", accept, this.syntaxAndPattern, match, relativePath);
/* 106 */     if (result) {
/* 107 */       return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
/*     */     }
/* 109 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeFileTreeWalk() {
/* 119 */     IfAll.beforeFileTreeWalk(this.nestedConditions);
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
/*     */   @PluginFactory
/*     */   public static IfFileName createNameCondition(@PluginAttribute("glob") String glob, @PluginAttribute("regex") String regex, @PluginElement("PathConditions") PathCondition... nestedConditions) {
/* 141 */     return new IfFileName(glob, regex, nestedConditions);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
/* 147 */     return "IfFileName(" + this.syntaxAndPattern + nested + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\IfFileName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */