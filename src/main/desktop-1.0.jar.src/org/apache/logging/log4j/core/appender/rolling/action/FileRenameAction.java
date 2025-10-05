/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.file.AtomicMoveNotSupportedException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileRenameAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean renameEmptyFiles;
/*     */   
/*     */   public FileRenameAction(File src, File dst, boolean renameEmptyFiles) {
/*  56 */     this.source = src;
/*  57 */     this.destination = dst;
/*  58 */     this.renameEmptyFiles = renameEmptyFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() {
/*  68 */     return execute(this.source, this.destination, this.renameEmptyFiles);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestination() {
/*  77 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSource() {
/*  86 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRenameEmptyFiles() {
/*  95 */     return this.renameEmptyFiles;
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
/*     */   public static boolean execute(File source, File destination, boolean renameEmptyFiles) {
/* 107 */     if (renameEmptyFiles || source.length() > 0L) {
/* 108 */       File parent = destination.getParentFile();
/* 109 */       if (parent != null && !parent.exists()) {
/*     */ 
/*     */ 
/*     */         
/* 113 */         parent.mkdirs();
/* 114 */         if (!parent.exists()) {
/* 115 */           LOGGER.error("Unable to create directory {}", parent.getAbsolutePath());
/* 116 */           return false;
/*     */         } 
/*     */       } 
/*     */       
/*     */       try {
/* 121 */         return moveFile(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]));
/* 122 */       } catch (IOException exMove) {
/* 123 */         LOGGER.debug("Unable to move file {} to {}: {} {} - will try to copy and delete", source
/* 124 */             .getAbsolutePath(), destination.getAbsolutePath(), exMove.getClass().getName(), exMove
/* 125 */             .getMessage());
/* 126 */         boolean result = source.renameTo(destination);
/* 127 */         if (!result) {
/*     */           try {
/* 129 */             Files.copy(Paths.get(source.getAbsolutePath(), new String[0]), Paths.get(destination.getAbsolutePath(), new String[0]), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */             
/*     */             try {
/* 132 */               Files.delete(Paths.get(source.getAbsolutePath(), new String[0]));
/* 133 */               result = true;
/* 134 */               LOGGER.trace("Renamed file {} to {} using copy and delete", source
/* 135 */                   .getAbsolutePath(), destination.getAbsolutePath());
/* 136 */             } catch (IOException exDelete) {
/* 137 */               LOGGER.error("Unable to delete file {}: {} {}", source.getAbsolutePath(), exDelete
/* 138 */                   .getClass().getName(), exDelete.getMessage());
/*     */               try {
/* 140 */                 result = true;
/* 141 */                 (new PrintWriter(source.getAbsolutePath())).close();
/* 142 */                 LOGGER.trace("Renamed file {} to {} with copy and truncation", source
/* 143 */                     .getAbsolutePath(), destination.getAbsolutePath());
/* 144 */               } catch (IOException exOwerwrite) {
/* 145 */                 LOGGER.error("Unable to overwrite file {}: {} {}", source
/* 146 */                     .getAbsolutePath(), exOwerwrite.getClass().getName(), exOwerwrite
/* 147 */                     .getMessage());
/*     */               } 
/*     */             } 
/* 150 */           } catch (IOException exCopy) {
/* 151 */             LOGGER.error("Unable to copy file {} to {}: {} {}", source.getAbsolutePath(), destination
/* 152 */                 .getAbsolutePath(), exCopy.getClass().getName(), exCopy.getMessage());
/*     */           } 
/*     */         } else {
/* 155 */           LOGGER.trace("Renamed file {} to {} with source.renameTo", source
/* 156 */               .getAbsolutePath(), destination.getAbsolutePath());
/*     */         } 
/* 158 */         return result;
/*     */       }
/* 160 */       catch (RuntimeException ex) {
/* 161 */         LOGGER.error("Unable to rename file {} to {}: {} {}", source.getAbsolutePath(), destination
/* 162 */             .getAbsolutePath(), ex.getClass().getName(), ex.getMessage());
/*     */       } 
/*     */     } else {
/*     */       try {
/* 166 */         source.delete();
/* 167 */       } catch (Exception exDelete) {
/* 168 */         LOGGER.error("Unable to delete empty file {}: {} {}", source.getAbsolutePath(), exDelete
/* 169 */             .getClass().getName(), exDelete.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean moveFile(Path source, Path target) throws IOException {
/*     */     try {
/* 178 */       Files.move(source, target, new CopyOption[] { StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING });
/*     */       
/* 180 */       LOGGER.trace("Renamed file {} to {} with Files.move", source.toFile().getAbsolutePath(), target
/* 181 */           .toFile().getAbsolutePath());
/* 182 */       return true;
/* 183 */     } catch (AtomicMoveNotSupportedException ex) {
/* 184 */       Files.move(source, target, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 185 */       LOGGER.trace("Renamed file {} to {} with Files.move", source.toFile().getAbsolutePath(), target
/* 186 */           .toFile().getAbsolutePath());
/* 187 */       return true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     return FileRenameAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", renameEmptyFiles=" + this.renameEmptyFiles + ']';
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\FileRenameAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */