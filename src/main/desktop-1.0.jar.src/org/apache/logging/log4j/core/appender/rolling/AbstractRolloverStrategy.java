/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.pattern.NotANumber;
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
/*     */ public abstract class AbstractRolloverStrategy
/*     */   implements RolloverStrategy
/*     */ {
/*  47 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  49 */   public static final Pattern PATTERN_COUNTER = Pattern.compile(".*%((?<ZEROPAD>0)?(?<PADDING>\\d+))?i.*");
/*     */   
/*     */   protected final StrSubstitutor strSubstitutor;
/*     */   
/*     */   protected AbstractRolloverStrategy(StrSubstitutor strSubstitutor) {
/*  54 */     this.strSubstitutor = strSubstitutor;
/*     */   }
/*     */ 
/*     */   
/*     */   public StrSubstitutor getStrSubstitutor() {
/*  59 */     return this.strSubstitutor;
/*     */   }
/*     */   
/*     */   protected Action merge(Action compressAction, List<Action> custom, boolean stopOnError) {
/*  63 */     if (custom.isEmpty()) {
/*  64 */       return compressAction;
/*     */     }
/*  66 */     if (compressAction == null) {
/*  67 */       return (Action)new CompositeAction(custom, stopOnError);
/*     */     }
/*  69 */     List<Action> all = new ArrayList<>();
/*  70 */     all.add(compressAction);
/*  71 */     all.addAll(custom);
/*  72 */     return (Action)new CompositeAction(all, stopOnError);
/*     */   }
/*     */   
/*     */   protected int suffixLength(String lowFilename) {
/*  76 */     for (FileExtension extension : FileExtension.values()) {
/*  77 */       if (extension.isExtensionFor(lowFilename)) {
/*  78 */         return extension.length();
/*     */       }
/*     */     } 
/*  81 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(RollingFileManager manager) {
/*  86 */     return getEligibleFiles(manager, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(RollingFileManager manager, boolean isAscending) {
/*  91 */     StringBuilder buf = new StringBuilder();
/*  92 */     String pattern = manager.getPatternProcessor().getPattern();
/*  93 */     manager.getPatternProcessor().formatFileName(this.strSubstitutor, buf, NotANumber.NAN);
/*  94 */     String fileName = manager.isDirectWrite() ? "" : manager.getFileName();
/*  95 */     return getEligibleFiles(fileName, buf.toString(), pattern, isAscending);
/*     */   }
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(String path, String pattern) {
/*  99 */     return getEligibleFiles("", path, pattern, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(String path, String logfilePattern, boolean isAscending) {
/* 105 */     return getEligibleFiles("", path, logfilePattern, isAscending);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMap<Integer, Path> getEligibleFiles(String currentFile, String path, String logfilePattern, boolean isAscending) {
/* 110 */     TreeMap<Integer, Path> eligibleFiles = new TreeMap<>();
/* 111 */     File file = new File(path);
/* 112 */     File parent = file.getParentFile();
/* 113 */     if (parent == null) {
/* 114 */       parent = new File(".");
/*     */     } else {
/* 116 */       parent.mkdirs();
/*     */     } 
/* 118 */     if (!PATTERN_COUNTER.matcher(logfilePattern).matches()) {
/* 119 */       return eligibleFiles;
/*     */     }
/* 121 */     Path dir = parent.toPath();
/* 122 */     String fileName = file.getName();
/* 123 */     int suffixLength = suffixLength(fileName);
/*     */ 
/*     */     
/* 126 */     if (suffixLength > 0) {
/* 127 */       fileName = Pattern.quote(fileName.substring(0, fileName.length() - suffixLength)) + ".*";
/*     */     } else {
/* 129 */       fileName = Pattern.quote(fileName);
/*     */     } 
/*     */ 
/*     */     
/* 133 */     String filePattern = fileName.replaceFirst("0?\\u0000", "\\\\E(0?\\\\d+)\\\\Q");
/* 134 */     Pattern pattern = Pattern.compile(filePattern);
/* 135 */     Path current = (currentFile.length() > 0) ? (new File(currentFile)).toPath() : null;
/* 136 */     LOGGER.debug("Current file: {}", currentFile);
/*     */     
/* 138 */     try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
/* 139 */       for (Path entry : stream) {
/* 140 */         Matcher matcher = pattern.matcher(entry.toFile().getName());
/* 141 */         if (matcher.matches() && !entry.equals(current)) {
/*     */           try {
/* 143 */             Integer index = Integer.valueOf(Integer.parseInt(matcher.group(1)));
/* 144 */             eligibleFiles.put(index, entry);
/* 145 */           } catch (NumberFormatException ex) {
/* 146 */             LOGGER.debug("Ignoring file {} which matches pattern but the index is invalid.", entry
/* 147 */                 .toFile().getName());
/*     */           } 
/*     */         }
/*     */       } 
/* 151 */     } catch (IOException ioe) {
/* 152 */       throw new LoggingException("Error reading folder " + dir + " " + ioe.getMessage(), ioe);
/*     */     } 
/* 154 */     return isAscending ? eligibleFiles : eligibleFiles.descendingMap();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\AbstractRolloverStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */