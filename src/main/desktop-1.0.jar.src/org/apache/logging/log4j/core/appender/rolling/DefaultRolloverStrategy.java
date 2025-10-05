/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ @Plugin(name = "DefaultRolloverStrategy", category = "Core", printObject = true)
/*     */ public class DefaultRolloverStrategy
/*     */   extends AbstractRolloverStrategy
/*     */ {
/*     */   private static final int MIN_WINDOW_SIZE = 1;
/*     */   private static final int DEFAULT_WINDOW_SIZE = 7;
/*     */   private final int maxIndex;
/*     */   private final int minIndex;
/*     */   private final boolean useMax;
/*     */   private final int compressionLevel;
/*     */   private final List<Action> customActions;
/*     */   private final boolean stopCustomActionsOnError;
/*     */   private final PatternProcessor tempCompressedFilePattern;
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<DefaultRolloverStrategy>
/*     */   {
/*     */     @PluginBuilderAttribute("max")
/*     */     private String max;
/*     */     @PluginBuilderAttribute("min")
/*     */     private String min;
/*     */     @PluginBuilderAttribute("fileIndex")
/*     */     private String fileIndex;
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
/*     */     public DefaultRolloverStrategy build() {
/*     */       int minIndex, maxIndex;
/*     */       boolean useMax;
/* 121 */       if (this.fileIndex != null && this.fileIndex.equalsIgnoreCase("nomax")) {
/* 122 */         minIndex = Integer.MIN_VALUE;
/* 123 */         maxIndex = Integer.MAX_VALUE;
/* 124 */         useMax = false;
/*     */       } else {
/* 126 */         useMax = (this.fileIndex == null) ? true : this.fileIndex.equalsIgnoreCase("max");
/* 127 */         minIndex = 1;
/* 128 */         if (this.min != null) {
/* 129 */           minIndex = Integer.parseInt(this.min);
/* 130 */           if (minIndex < 1) {
/* 131 */             AbstractRolloverStrategy.LOGGER.error("Minimum window size too small. Limited to 1");
/* 132 */             minIndex = 1;
/*     */           } 
/*     */         } 
/* 135 */         maxIndex = 7;
/* 136 */         if (this.max != null) {
/* 137 */           maxIndex = Integer.parseInt(this.max);
/* 138 */           if (maxIndex < minIndex) {
/* 139 */             maxIndex = (minIndex < 7) ? 7 : minIndex;
/* 140 */             AbstractRolloverStrategy.LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
/*     */           } 
/*     */         } 
/*     */       } 
/* 144 */       int compressionLevel = Integers.parseInt(this.compressionLevelStr, -1);
/*     */       
/* 146 */       StrSubstitutor nonNullStrSubstitutor = (this.config != null) ? this.config.getStrSubstitutor() : new StrSubstitutor();
/* 147 */       return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, nonNullStrSubstitutor, this.customActions, this.stopCustomActionsOnError, this.tempCompressedFilePattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMax() {
/* 152 */       return this.max;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withMax(String max) {
/* 162 */       this.max = max;
/* 163 */       return this;
/*     */     }
/*     */     
/*     */     public String getMin() {
/* 167 */       return this.min;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withMin(String min) {
/* 177 */       this.min = min;
/* 178 */       return this;
/*     */     }
/*     */     
/*     */     public String getFileIndex() {
/* 182 */       return this.fileIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFileIndex(String fileIndex) {
/* 193 */       this.fileIndex = fileIndex;
/* 194 */       return this;
/*     */     }
/*     */     
/*     */     public String getCompressionLevelStr() {
/* 198 */       return this.compressionLevelStr;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withCompressionLevelStr(String compressionLevelStr) {
/* 208 */       this.compressionLevelStr = compressionLevelStr;
/* 209 */       return this;
/*     */     }
/*     */     
/*     */     public Action[] getCustomActions() {
/* 213 */       return this.customActions;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withCustomActions(Action[] customActions) {
/* 223 */       this.customActions = customActions;
/* 224 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isStopCustomActionsOnError() {
/* 228 */       return this.stopCustomActionsOnError;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withStopCustomActionsOnError(boolean stopCustomActionsOnError) {
/* 238 */       this.stopCustomActionsOnError = stopCustomActionsOnError;
/* 239 */       return this;
/*     */     }
/*     */     
/*     */     public String getTempCompressedFilePattern() {
/* 243 */       return this.tempCompressedFilePattern;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withTempCompressedFilePattern(String tempCompressedFilePattern) {
/* 253 */       this.tempCompressedFilePattern = tempCompressedFilePattern;
/* 254 */       return this;
/*     */     }
/*     */     
/*     */     public Configuration getConfig() {
/* 258 */       return this.config;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withConfig(Configuration config) {
/* 268 */       this.config = config;
/* 269 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 275 */     return new Builder();
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
/*     */   @PluginFactory
/*     */   @Deprecated
/*     */   public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") String max, @PluginAttribute("min") String min, @PluginAttribute("fileIndex") String fileIndex, @PluginAttribute("compressionLevel") String compressionLevelStr, @PluginElement("Actions") Action[] customActions, @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true) boolean stopCustomActionsOnError, @PluginConfiguration Configuration config) {
/* 304 */     return newBuilder()
/* 305 */       .withMin(min)
/* 306 */       .withMax(max)
/* 307 */       .withFileIndex(fileIndex)
/* 308 */       .withCompressionLevelStr(compressionLevelStr)
/* 309 */       .withCustomActions(customActions)
/* 310 */       .withStopCustomActionsOnError(stopCustomActionsOnError)
/* 311 */       .withConfig(config)
/* 312 */       .build();
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
/*     */   @Deprecated
/*     */   protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError) {
/* 344 */     this(minIndex, maxIndex, useMax, compressionLevel, strSubstitutor, customActions, stopCustomActionsOnError, (String)null);
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
/*     */   protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor strSubstitutor, Action[] customActions, boolean stopCustomActionsOnError, String tempCompressedFilePatternString) {
/* 361 */     super(strSubstitutor);
/* 362 */     this.minIndex = minIndex;
/* 363 */     this.maxIndex = maxIndex;
/* 364 */     this.useMax = useMax;
/* 365 */     this.compressionLevel = compressionLevel;
/* 366 */     this.stopCustomActionsOnError = stopCustomActionsOnError;
/* 367 */     this.customActions = (customActions == null) ? Collections.<Action>emptyList() : Arrays.<Action>asList(customActions);
/* 368 */     this.tempCompressedFilePattern = (tempCompressedFilePatternString != null) ? new PatternProcessor(tempCompressedFilePatternString) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCompressionLevel() {
/* 373 */     return this.compressionLevel;
/*     */   }
/*     */   
/*     */   public List<Action> getCustomActions() {
/* 377 */     return this.customActions;
/*     */   }
/*     */   
/*     */   public int getMaxIndex() {
/* 381 */     return this.maxIndex;
/*     */   }
/*     */   
/*     */   public int getMinIndex() {
/* 385 */     return this.minIndex;
/*     */   }
/*     */   
/*     */   public boolean isStopCustomActionsOnError() {
/* 389 */     return this.stopCustomActionsOnError;
/*     */   }
/*     */   
/*     */   public boolean isUseMax() {
/* 393 */     return this.useMax;
/*     */   }
/*     */   
/*     */   public PatternProcessor getTempCompressedFilePattern() {
/* 397 */     return this.tempCompressedFilePattern;
/*     */   }
/*     */   
/*     */   private int purge(int lowIndex, int highIndex, RollingFileManager manager) {
/* 401 */     return this.useMax ? purgeAscending(lowIndex, highIndex, manager) : purgeDescending(lowIndex, highIndex, manager);
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
/*     */   private int purgeAscending(int lowIndex, int highIndex, RollingFileManager manager) {
/* 414 */     SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 415 */     int maxFiles = highIndex - lowIndex + 1;
/*     */     
/* 417 */     boolean renameFiles = (!eligibleFiles.isEmpty() && ((Integer)eligibleFiles.lastKey()).intValue() >= this.maxIndex);
/* 418 */     while (eligibleFiles.size() >= maxFiles) {
/*     */       try {
/* 420 */         LOGGER.debug("Eligible files: {}", eligibleFiles);
/* 421 */         Integer key = eligibleFiles.firstKey();
/* 422 */         LOGGER.debug("Deleting {}", ((Path)eligibleFiles.get(key)).toFile().getAbsolutePath());
/* 423 */         Files.delete(eligibleFiles.get(key));
/* 424 */         eligibleFiles.remove(key);
/* 425 */         renameFiles = true;
/* 426 */       } catch (IOException ioe) {
/* 427 */         LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
/*     */         break;
/*     */       } 
/*     */     } 
/* 431 */     StringBuilder buf = new StringBuilder();
/* 432 */     if (renameFiles) {
/* 433 */       for (Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
/* 434 */         buf.setLength(0);
/*     */         
/* 436 */         manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(((Integer)entry.getKey()).intValue() - 1));
/* 437 */         String currentName = ((Path)entry.getValue()).toFile().getName();
/* 438 */         String renameTo = buf.toString();
/* 439 */         int suffixLength = suffixLength(renameTo);
/* 440 */         if (suffixLength > 0 && suffixLength(currentName) == 0) {
/* 441 */           renameTo = renameTo.substring(0, renameTo.length() - suffixLength);
/*     */         }
/* 443 */         FileRenameAction fileRenameAction = new FileRenameAction(((Path)entry.getValue()).toFile(), new File(renameTo), true);
/*     */         try {
/* 445 */           LOGGER.debug("DefaultRolloverStrategy.purgeAscending executing {}", fileRenameAction);
/* 446 */           if (!fileRenameAction.execute()) {
/* 447 */             return -1;
/*     */           }
/* 449 */         } catch (Exception ex) {
/* 450 */           LOGGER.warn("Exception during purge in RollingFileAppender", ex);
/* 451 */           return -1;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 456 */     return (eligibleFiles.size() > 0) ? (
/* 457 */       (((Integer)eligibleFiles.lastKey()).intValue() < highIndex) ? (((Integer)eligibleFiles.lastKey()).intValue() + 1) : highIndex) : lowIndex;
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
/*     */   private int purgeDescending(int lowIndex, int highIndex, RollingFileManager manager) {
/* 471 */     SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager, false);
/* 472 */     int maxFiles = highIndex - lowIndex + 1;
/* 473 */     LOGGER.debug("Eligible files: {}", eligibleFiles);
/* 474 */     while (eligibleFiles.size() >= maxFiles) {
/*     */       try {
/* 476 */         Integer key = eligibleFiles.firstKey();
/* 477 */         Files.delete(eligibleFiles.get(key));
/* 478 */         eligibleFiles.remove(key);
/* 479 */       } catch (IOException ioe) {
/* 480 */         LOGGER.error("Unable to delete {}, {}", eligibleFiles.firstKey(), ioe.getMessage(), ioe);
/*     */         break;
/*     */       } 
/*     */     } 
/* 484 */     StringBuilder buf = new StringBuilder();
/* 485 */     for (Map.Entry<Integer, Path> entry : eligibleFiles.entrySet()) {
/* 486 */       buf.setLength(0);
/*     */       
/* 488 */       manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(((Integer)entry.getKey()).intValue() + 1));
/* 489 */       String currentName = ((Path)entry.getValue()).toFile().getName();
/* 490 */       String renameTo = buf.toString();
/* 491 */       int suffixLength = suffixLength(renameTo);
/* 492 */       if (suffixLength > 0 && suffixLength(currentName) == 0) {
/* 493 */         renameTo = renameTo.substring(0, renameTo.length() - suffixLength);
/*     */       }
/* 495 */       FileRenameAction fileRenameAction = new FileRenameAction(((Path)entry.getValue()).toFile(), new File(renameTo), true);
/*     */       try {
/* 497 */         LOGGER.debug("DefaultRolloverStrategy.purgeDescending executing {}", fileRenameAction);
/* 498 */         if (!fileRenameAction.execute()) {
/* 499 */           return -1;
/*     */         }
/* 501 */       } catch (Exception ex) {
/* 502 */         LOGGER.warn("Exception during purge in RollingFileAppender", ex);
/* 503 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/* 507 */     return lowIndex;
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
/*     */     int fileIndex;
/*     */     CompositeAction compositeAction;
/* 520 */     StringBuilder buf = new StringBuilder(255);
/* 521 */     if (this.minIndex == Integer.MIN_VALUE) {
/* 522 */       SortedMap<Integer, Path> eligibleFiles = getEligibleFiles(manager);
/* 523 */       fileIndex = (eligibleFiles.size() > 0) ? (((Integer)eligibleFiles.lastKey()).intValue() + 1) : 1;
/* 524 */       manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
/*     */     } else {
/* 526 */       if (this.maxIndex < 0) {
/* 527 */         return null;
/*     */       }
/* 529 */       long startNanos = System.nanoTime();
/* 530 */       fileIndex = purge(this.minIndex, this.maxIndex, manager);
/* 531 */       if (fileIndex < 0) {
/* 532 */         return null;
/*     */       }
/* 534 */       manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
/* 535 */       if (LOGGER.isTraceEnabled()) {
/* 536 */         double durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
/* 537 */         LOGGER.trace("DefaultRolloverStrategy.purge() took {} milliseconds", Double.valueOf(durationMillis));
/*     */       } 
/*     */     } 
/*     */     
/* 541 */     String currentFileName = manager.getFileName();
/*     */     
/* 543 */     String renameTo = buf.toString();
/* 544 */     String compressedName = renameTo;
/* 545 */     Action compressAction = null;
/*     */     
/* 547 */     FileExtension fileExtension = manager.getFileExtension();
/* 548 */     if (fileExtension != null) {
/* 549 */       File renameToFile = new File(renameTo);
/* 550 */       renameTo = renameTo.substring(0, renameTo.length() - fileExtension.length());
/* 551 */       if (this.tempCompressedFilePattern != null) {
/* 552 */         buf.delete(0, buf.length());
/* 553 */         this.tempCompressedFilePattern.formatFileName(this.strSubstitutor, buf, Integer.valueOf(fileIndex));
/* 554 */         String tmpCompressedName = buf.toString();
/* 555 */         File tmpCompressedNameFile = new File(tmpCompressedName);
/* 556 */         File parentFile = tmpCompressedNameFile.getParentFile();
/* 557 */         if (parentFile != null) {
/* 558 */           parentFile.mkdirs();
/*     */         }
/*     */         
/* 561 */         compositeAction = new CompositeAction(Arrays.asList(new Action[] { fileExtension.createCompressAction(renameTo, tmpCompressedName, true, this.compressionLevel), (Action)new FileRenameAction(tmpCompressedNameFile, renameToFile, true) }), true);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 567 */         compressAction = fileExtension.createCompressAction(renameTo, compressedName, true, this.compressionLevel);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 572 */     if (currentFileName.equals(renameTo)) {
/* 573 */       LOGGER.warn("Attempt to rename file {} to itself will be ignored", currentFileName);
/* 574 */       return new RolloverDescriptionImpl(currentFileName, false, null, null);
/*     */     } 
/*     */     
/* 577 */     if (compressAction != null && manager.isAttributeViewEnabled()) {
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
/* 589 */       PosixViewAttributeAction posixViewAttributeAction = PosixViewAttributeAction.newBuilder().withBasePath(compressedName).withFollowLinks(false).withMaxDepth(1).withPathConditions(PathCondition.EMPTY_ARRAY).withSubst(getStrSubstitutor()).withFilePermissions(manager.getFilePermissions()).withFileOwner(manager.getFileOwner()).withFileGroup(manager.getFileGroup()).build();
/*     */       
/* 591 */       compositeAction = new CompositeAction(Arrays.asList(new Action[] { compressAction, (Action)posixViewAttributeAction }, ), false);
/*     */     } 
/*     */ 
/*     */     
/* 595 */     FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), manager.isRenameEmptyFiles());
/*     */     
/* 597 */     Action asyncAction = merge((Action)compositeAction, this.customActions, this.stopCustomActionsOnError);
/* 598 */     return new RolloverDescriptionImpl(currentFileName, false, (Action)renameAction, asyncAction);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 603 */     return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ", useMax=" + this.useMax + ")";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\DefaultRolloverStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */