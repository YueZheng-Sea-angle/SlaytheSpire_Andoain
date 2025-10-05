/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ZipCompressAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private static final int BUF_SIZE = 8192;
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean deleteSource;
/*     */   private final int level;
/*     */   
/*     */   public ZipCompressAction(File source, File destination, boolean deleteSource, int level) {
/*  64 */     Objects.requireNonNull(source, "source");
/*  65 */     Objects.requireNonNull(destination, "destination");
/*     */     
/*  67 */     this.source = source;
/*  68 */     this.destination = destination;
/*  69 */     this.deleteSource = deleteSource;
/*  70 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws IOException {
/*  81 */     return execute(this.source, this.destination, this.deleteSource, this.level);
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
/*     */   public static boolean execute(File source, File destination, boolean deleteSource, int level) throws IOException {
/*  97 */     if (source.exists()) {
/*  98 */       try(FileInputStream fis = new FileInputStream(source); 
/*  99 */           ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination))) {
/* 100 */         zos.setLevel(level);
/*     */         
/* 102 */         ZipEntry zipEntry = new ZipEntry(source.getName());
/* 103 */         zos.putNextEntry(zipEntry);
/*     */         
/* 105 */         byte[] inbuf = new byte[8192];
/*     */         
/*     */         int n;
/* 108 */         while ((n = fis.read(inbuf)) != -1) {
/* 109 */           zos.write(inbuf, 0, n);
/*     */         }
/*     */       } 
/*     */       
/* 113 */       if (deleteSource && !source.delete()) {
/* 114 */         LOGGER.warn("Unable to delete " + source.toString() + '.');
/*     */       }
/*     */       
/* 117 */       return true;
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 130 */     LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     return ZipCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", level=" + this.level + ", deleteSource=" + this.deleteSource + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   public File getSource() {
/* 140 */     return this.source;
/*     */   }
/*     */   
/*     */   public File getDestination() {
/* 144 */     return this.destination;
/*     */   }
/*     */   
/*     */   public boolean isDeleteSource() {
/* 148 */     return this.deleteSource;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 152 */     return this.level;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\ZipCompressAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */