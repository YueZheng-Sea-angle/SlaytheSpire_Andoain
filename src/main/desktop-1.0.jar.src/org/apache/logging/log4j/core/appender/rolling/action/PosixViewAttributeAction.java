/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.FileVisitor;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ @Plugin(name = "PosixViewAttribute", category = "Core", printObject = true)
/*     */ public class PosixViewAttributeAction
/*     */   extends AbstractPathAction
/*     */ {
/*     */   private final Set<PosixFilePermission> filePermissions;
/*     */   private final String fileOwner;
/*     */   private final String fileGroup;
/*     */   
/*     */   private PosixViewAttributeAction(String basePath, boolean followSymbolicLinks, int maxDepth, PathCondition[] pathConditions, StrSubstitutor subst, Set<PosixFilePermission> filePermissions, String fileOwner, String fileGroup) {
/*  71 */     super(basePath, followSymbolicLinks, maxDepth, pathConditions, subst);
/*  72 */     this.filePermissions = filePermissions;
/*  73 */     this.fileOwner = fileOwner;
/*  74 */     this.fileGroup = fileGroup;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  79 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<PosixViewAttributeAction>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     private StrSubstitutor subst;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "No base path provided")
/*     */     private String basePath;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean followLinks = false;
/*     */     
/*     */     @PluginBuilderAttribute
/*  99 */     private int maxDepth = 1;
/*     */ 
/*     */     
/*     */     @PluginElement("PathConditions")
/*     */     private PathCondition[] pathConditions;
/*     */     
/*     */     @PluginBuilderAttribute("filePermissions")
/*     */     private String filePermissionsString;
/*     */     
/*     */     private Set<PosixFilePermission> filePermissions;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String fileOwner;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String fileGroup;
/*     */ 
/*     */     
/*     */     public PosixViewAttributeAction build() {
/* 118 */       if (Strings.isEmpty(this.basePath)) {
/* 119 */         AbstractAction.LOGGER.error("Posix file attribute view action not valid because base path is empty.");
/* 120 */         return null;
/*     */       } 
/*     */       
/* 123 */       if (this.filePermissions == null && Strings.isEmpty(this.filePermissionsString) && 
/* 124 */         Strings.isEmpty(this.fileOwner) && Strings.isEmpty(this.fileGroup)) {
/* 125 */         AbstractAction.LOGGER.error("Posix file attribute view not valid because nor permissions, user or group defined.");
/* 126 */         return null;
/*     */       } 
/*     */       
/* 129 */       if (!FileUtils.isFilePosixAttributeViewSupported()) {
/* 130 */         AbstractAction.LOGGER.warn("Posix file attribute view defined but it is not supported by this files system.");
/* 131 */         return null;
/*     */       } 
/*     */       
/* 134 */       return new PosixViewAttributeAction(this.basePath, this.followLinks, this.maxDepth, this.pathConditions, (this.subst != null) ? this.subst : this.configuration
/* 135 */           .getStrSubstitutor(), (this.filePermissions != null) ? this.filePermissions : ((this.filePermissionsString != null) ? 
/*     */           
/* 137 */           PosixFilePermissions.fromString(this.filePermissionsString) : null), this.fileOwner, this.fileGroup);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/* 149 */       this.configuration = configuration;
/* 150 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withSubst(StrSubstitutor subst) {
/* 160 */       this.subst = subst;
/* 161 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withBasePath(String basePath) {
/* 170 */       this.basePath = basePath;
/* 171 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFollowLinks(boolean followLinks) {
/* 180 */       this.followLinks = followLinks;
/* 181 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withMaxDepth(int maxDepth) {
/* 190 */       this.maxDepth = maxDepth;
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withPathConditions(PathCondition[] pathConditions) {
/* 201 */       this.pathConditions = pathConditions;
/* 202 */       return this;
/*     */     }
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
/*     */     public Builder withFilePermissionsString(String filePermissionsString) {
/* 215 */       this.filePermissionsString = filePermissionsString;
/* 216 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFilePermissions(Set<PosixFilePermission> filePermissions) {
/* 225 */       this.filePermissions = filePermissions;
/* 226 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFileOwner(String fileOwner) {
/* 235 */       this.fileOwner = fileOwner;
/* 236 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFileGroup(String fileGroup) {
/* 245 */       this.fileGroup = fileGroup;
/* 246 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileVisitor<Path> createFileVisitor(final Path basePath, final List<PathCondition> conditions) {
/* 253 */     return new SimpleFileVisitor<Path>()
/*     */       {
/*     */         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 256 */           for (PathCondition pathFilter : conditions) {
/* 257 */             Path relative = basePath.relativize(file);
/* 258 */             if (!pathFilter.accept(basePath, relative, attrs)) {
/* 259 */               AbstractAction.LOGGER.trace("Not defining posix attribute base={}, relative={}", basePath, relative);
/* 260 */               return FileVisitResult.CONTINUE;
/*     */             } 
/*     */           } 
/* 263 */           FileUtils.defineFilePosixAttributeView(file, PosixViewAttributeAction.this.filePermissions, PosixViewAttributeAction.this.fileOwner, PosixViewAttributeAction.this.fileGroup);
/* 264 */           return FileVisitResult.CONTINUE;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<PosixFilePermission> getFilePermissions() {
/* 276 */     return this.filePermissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileOwner() {
/* 286 */     return this.fileOwner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileGroup() {
/* 296 */     return this.fileGroup;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 301 */     return "PosixViewAttributeAction [filePermissions=" + this.filePermissions + ", fileOwner=" + this.fileOwner + ", fileGroup=" + this.fileGroup + ", getBasePath()=" + 
/* 302 */       getBasePath() + ", getMaxDepth()=" + 
/* 303 */       getMaxDepth() + ", getPathConditions()=" + 
/* 304 */       getPathConditions() + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\PosixViewAttributeAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */