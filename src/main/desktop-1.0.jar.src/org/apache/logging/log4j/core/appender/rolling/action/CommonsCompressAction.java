/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.compress.compressors.CompressorException;
/*     */ import org.apache.commons.compress.compressors.CompressorStreamFactory;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CommonsCompressAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private static final int BUF_SIZE = 8192;
/*     */   private final String name;
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean deleteSource;
/*     */   
/*     */   public CommonsCompressAction(String name, File source, File destination, boolean deleteSource) {
/*  69 */     Objects.requireNonNull(source, "source");
/*  70 */     Objects.requireNonNull(destination, "destination");
/*  71 */     this.name = name;
/*  72 */     this.source = source;
/*  73 */     this.destination = destination;
/*  74 */     this.deleteSource = deleteSource;
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
/*  85 */     return execute(this.name, this.source, this.destination, this.deleteSource);
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
/*     */   public static boolean execute(String name, File source, File destination, boolean deleteSource) throws IOException {
/* 102 */     if (!source.exists()) {
/* 103 */       return false;
/*     */     }
/* 105 */     LOGGER.debug("Starting {} compression of {}", name, source.getPath());
/* 106 */     try(FileInputStream input = new FileInputStream(source); 
/* 107 */         BufferedOutputStream output = new BufferedOutputStream((OutputStream)(new CompressorStreamFactory())
/* 108 */           .createCompressorOutputStream(name, new FileOutputStream(destination)))) {
/*     */       
/* 110 */       IOUtils.copy(input, output, 8192);
/* 111 */       LOGGER.debug("Finished {} compression of {}", name, source.getPath());
/* 112 */     } catch (CompressorException e) {
/* 113 */       throw new IOException(e);
/*     */     } 
/*     */     
/* 116 */     if (deleteSource) {
/*     */       try {
/* 118 */         if (Files.deleteIfExists(source.toPath())) {
/* 119 */           LOGGER.debug("Deleted {}", source.toString());
/*     */         } else {
/* 121 */           LOGGER.warn("Unable to delete {} after {} compression. File did not exist", source.toString(), name);
/*     */         } 
/* 123 */       } catch (Exception ex) {
/* 124 */         LOGGER.warn("Unable to delete {} after {} compression, {}", source.toString(), name, ex.getMessage());
/*     */       } 
/*     */     }
/*     */     
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 138 */     LOGGER.warn("Exception during " + this.name + " compression of '" + this.source.toString() + "'.", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return CommonsCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 148 */     return this.name;
/*     */   }
/*     */   
/*     */   public File getSource() {
/* 152 */     return this.source;
/*     */   }
/*     */   
/*     */   public File getDestination() {
/* 156 */     return this.destination;
/*     */   }
/*     */   
/*     */   public boolean isDeleteSource() {
/* 160 */     return this.deleteSource;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\CommonsCompressAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */