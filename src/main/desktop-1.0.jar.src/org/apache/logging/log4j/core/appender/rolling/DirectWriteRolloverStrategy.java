/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.PosixViewAttributeAction;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ @Plugin(name = "DirectWriteRolloverStrategy", category = "Core", printObject = true)
/*     */ public class DirectWriteRolloverStrategy
/*     */   extends AbstractRolloverStrategy
/*     */   implements DirectFileRolloverStrategy
/*     */ {
/*     */   private static final int DEFAULT_MAX_FILES = 7;
/*     */   private final int maxFiles;
/*     */   private final int compressionLevel;
/*     */   private final List<Action> customActions;
/*     */   private final boolean stopCustomActionsOnError;
/*     */   private volatile String currentFileName;
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<DirectWriteRolloverStrategy>
/*     */   {
/*     */     @PluginBuilderAttribute("maxFiles")
/*     */     private String maxFiles;
/*     */     @PluginBuilderAttribute("compressionLevel")
/*     */     private String compressionLevelStr;
/*     */     @PluginElement("Actions")
/*     */     private Action[] customActions;
/*     */     @PluginBuilderAttribute("stopCustomActionsOnError")
/*     */     private boolean stopCustomActionsOnError = true;
/*     */     @PluginBuilderAttribute("tempCompressedFilePattern")
/*     */     private String tempCompressedFilePattern;
/*     */     @PluginConfiguration
/*     */     private Configuration config;
/*     */     
/*     */     public DirectWriteRolloverStrategy build() {
/*  88 */       int maxIndex = Integer.MAX_VALUE;
/*  89 */       if (this.maxFiles != null) {
/*  90 */         maxIndex = Integer.parseInt(this.maxFiles);
/*  91 */         if (maxIndex < 0) {
/*  92 */           maxIndex = Integer.MAX_VALUE;
/*  93 */         } else if (maxIndex < 2) {
/*  94 */           AbstractRolloverStrategy.LOGGER.error("Maximum files too small. Limited to 7");
/*  95 */           maxIndex = 7;
/*     */         } 
/*     */       } 
/*  98 */       int compressionLevel = Integers.parseInt(this.compressionLevelStr, -1);
/*  99 */       return new DirectWriteRolloverStrategy(maxIndex, compressionLevel, this.config.getStrSubstitutor(), this.customActions, this.stopCustomActionsOnError, this.tempCompressedFilePattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMaxFiles() {
/* 104 */       return this.maxFiles;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withMaxFiles(String maxFiles) {
/* 114 */       this.maxFiles = maxFiles;
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public String getCompressionLevelStr() {
/* 119 */       return this.compressionLevelStr;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withCompressionLevelStr(String compressionLevelStr) {
/* 129 */       this.compressionLevelStr = compressionLevelStr;
/* 130 */       return this;
/*     */     }
/*     */     
/*     */     public Action[] getCustomActions() {
/* 134 */       return this.customActions;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withCustomActions(Action[] customActions) {
/* 144 */       this.customActions = customActions;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isStopCustomActionsOnError() {
/* 149 */       return this.stopCustomActionsOnError;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withStopCustomActionsOnError(boolean stopCustomActionsOnError) {
/* 159 */       this.stopCustomActionsOnError = stopCustomActionsOnError;
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public String getTempCompressedFilePattern() {
/* 164 */       return this.tempCompressedFilePattern;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withTempCompressedFilePattern(String tempCompressedFilePattern) {
/* 174 */       this.tempCompressedFilePattern = tempCompressedFilePattern;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Configuration getConfig() {
/* 179 */       return this.config;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withConfig(Configuration config) {
/* 189 */       this.config = config;
/* 190 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 196 */     return new Builder();
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
/*     */   @Deprecated
/*     */   @PluginFactory
/*     */   public static DirectWriteRolloverStrategy createStrategy(@PluginAttribute("maxFiles") String maxFiles, @PluginAttribute("compressionLevel") String compressionLevelStr, @PluginElement("Actions") Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) boolean stopCustomActionsOnError, @PluginConfiguration Configuration config) {
/* 220 */     return newBuilder().withMaxFiles(maxFiles)
/* 221 */       .withCompressionLevelStr(compressionLevelStr)
/* 222 */       .withCustomActions(customActions)
/* 223 */       .withStopCustomActionsOnError(stopCustomActionsOnError)
/* 224 */       .withConfig(config)
/* 225 */       .build();
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
/* 237 */   private int nextIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final PatternProcessor tempCompressedFilePattern;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean usePrevTime = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected DirectWriteRolloverStrategy(int maxFiles, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError) {
/* 253 */     this(maxFiles, compressionLevel, strSubstitutor, customActions, stopCustomActionsOnError, (String)null);
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
/*     */   protected DirectWriteRolloverStrategy(int maxFiles, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError, String tempCompressedFilePatternString) {
/* 268 */     super(strSubstitutor);
/* 269 */     this.maxFiles = maxFiles;
/* 270 */     this.compressionLevel = compressionLevel;
/* 271 */     this.stopCustomActionsOnError = stopCustomActionsOnError;
/* 272 */     this.customActions = (customActions == null) ? Collections.<Action>emptyList() : Arrays.<Action>asList(customActions);
/* 273 */     this.tempCompressedFilePattern = (tempCompressedFilePatternString != null) ? new PatternProcessor(tempCompressedFilePatternString) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCompressionLevel() {
/* 278 */     return this.compressionLevel;
/*     */   }
/*     */   
/*     */   public List<Action> getCustomActions() {
/* 282 */     return this.customActions;
/*     */   }
/*     */   
/*     */   public int getMaxFiles() {
/* 286 */     return this.maxFiles;
/*     */   }
/*     */   
/*     */   public boolean isStopCustomActionsOnError() {
/* 290 */     return this.stopCustomActionsOnError;
/*     */   }
/*     */   
/*     */   public PatternProcessor getTempCompressedFilePattern() {
/* 294 */     return this.tempCompressedFilePattern;
/*     */   }
/*     */   
/*     */   private int purge(RollingFileManager manager) {
/* 298 */     SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 299 */     LOGGER.debug("Found {} eligible files, max is  {}", Integer.valueOf(eligibleFiles.size()), Integer.valueOf(this.maxFiles));
/* 300 */     while (eligibleFiles.size() >= this.maxFiles) {
/*     */       try {
/* 302 */         Integer key = eligibleFiles.firstKey();
/* 303 */         Files.delete(eligibleFiles.get(key));
/* 304 */         eligibleFiles.remove(key);
/* 305 */       } catch (IOException ioe) {
/* 306 */         LOGGER.error("Unable to delete {}", eligibleFiles.firstKey(), ioe);
/*     */         break;
/*     */       } 
/*     */     } 
/* 310 */     return (eligibleFiles.size() > 0) ? ((Integer)eligibleFiles.lastKey()).intValue() : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCurrentFileName(RollingFileManager manager) {
/* 315 */     if (this.currentFileName == null) {
/* 316 */       SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 317 */       int fileIndex = (eligibleFiles.size() > 0) ? ((this.nextIndex > 0) ? this.nextIndex : eligibleFiles.size()) : 1;
/* 318 */       StringBuilder buf = new StringBuilder(255);
/* 319 */       manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, true, Integer.valueOf(fileIndex));
/* 320 */       int suffixLength = suffixLength(buf.toString());
/* 321 */       String name = (suffixLength > 0) ? buf.substring(0, buf.length() - suffixLength) : buf.toString();
/* 322 */       this.currentFileName = name;
/*     */     } 
/* 324 */     return this.currentFileName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearCurrentFileName() {
/* 329 */     this.currentFileName = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RolloverDescription rollover(RollingFileManager manager) throws SecurityException {
/*     */     CompositeAction compositeAction;
/* 341 */     LOGGER.debug("Rolling " + this.currentFileName);
/* 342 */     if (this.maxFiles < 0) {
/* 343 */       return null;
/*     */     }
/* 345 */     long startNanos = System.nanoTime();
/* 346 */     int fileIndex = purge(manager);
/* 347 */     if (LOGGER.isTraceEnabled()) {
/* 348 */       double durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
/* 349 */       LOGGER.trace("DirectWriteRolloverStrategy.purge() took {} milliseconds", Double.valueOf(durationMillis));
/*     */     } 
/* 351 */     Action compressAction = null;
/* 352 */     String sourceName = getCurrentFileName(manager);
/* 353 */     String compressedName = sourceName;
/* 354 */     this.currentFileName = null;
/* 355 */     this.nextIndex = fileIndex + 1;
/* 356 */     FileExtension fileExtension = manager.getFileExtension();
/* 357 */     if (fileExtension != null) {
/* 358 */       compressedName = compressedName + fileExtension.getExtension();
/* 359 */       if (this.tempCompressedFilePattern != null) {
/* 360 */         StringBuilder buf = new StringBuilder();
/* 361 */         this.tempCompressedFilePattern.formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
/* 362 */         String tmpCompressedName = buf.toString();
/* 363 */         File tmpCompressedNameFile = new File(tmpCompressedName);
/* 364 */         File parentFile = tmpCompressedNameFile.getParentFile();
/* 365 */         if (parentFile != null) {
/* 366 */           parentFile.mkdirs();
/*     */         }
/*     */         
/* 369 */         compositeAction = new CompositeAction(Arrays.asList(new Action[] { fileExtension.createCompressAction(sourceName, tmpCompressedName, true, this.compressionLevel), (Action)new FileRenameAction(tmpCompressedNameFile, new File(compressedName), true) }), true);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 375 */         compressAction = fileExtension.createCompressAction(sourceName, compressedName, true, this.compressionLevel);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 380 */     if (compressAction != null && manager.isAttributeViewEnabled()) {
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
/* 392 */       PosixViewAttributeAction posixViewAttributeAction = PosixViewAttributeAction.newBuilder().withBasePath(compressedName).withFollowLinks(false).withMaxDepth(1).withPathConditions(PathCondition.EMPTY_ARRAY).withSubst(getStrSubstitutor()).withFilePermissions(manager.getFilePermissions()).withFileOwner(manager.getFileOwner()).withFileGroup(manager.getFileGroup()).build();
/*     */       
/* 394 */       compositeAction = new CompositeAction(Arrays.asList(new Action[] { compressAction, (Action)posixViewAttributeAction }, ), false);
/*     */     } 
/*     */     
/* 397 */     Action asyncAction = merge((Action)compositeAction, this.customActions, this.stopCustomActionsOnError);
/* 398 */     return new RolloverDescriptionImpl(sourceName, false, null, asyncAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 403 */     return "DirectWriteRolloverStrategy(maxFiles=" + this.maxFiles + ')';
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\DirectWriteRolloverStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */