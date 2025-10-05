/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Objects;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GzCompressAction
/*     */   extends AbstractAction
/*     */ {
/*     */   private static final int BUF_SIZE = 8192;
/*     */   private final File source;
/*     */   private final File destination;
/*     */   private final boolean deleteSource;
/*     */   private final int compressionLevel;
/*     */   
/*     */   public GzCompressAction(File source, File destination, boolean deleteSource, int compressionLevel) {
/*  70 */     Objects.requireNonNull(source, "source");
/*  71 */     Objects.requireNonNull(destination, "destination");
/*     */     
/*  73 */     this.source = source;
/*  74 */     this.destination = destination;
/*  75 */     this.deleteSource = deleteSource;
/*  76 */     this.compressionLevel = compressionLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public GzCompressAction(File source, File destination, boolean deleteSource) {
/*  86 */     this(source, destination, deleteSource, -1);
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
/*  97 */     return execute(this.source, this.destination, this.deleteSource, this.compressionLevel);
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
/*     */   @Deprecated
/*     */   public static boolean execute(File source, File destination, boolean deleteSource) throws IOException {
/* 114 */     return execute(source, destination, deleteSource, -1);
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
/*     */   public static boolean execute(File source, File destination, boolean deleteSource, int compressionLevel) throws IOException {
/* 134 */     if (source.exists()) {
/* 135 */       try(FileInputStream fis = new FileInputStream(source); 
/* 136 */           OutputStream fos = new FileOutputStream(destination); 
/* 137 */           OutputStream gzipOut = new ConfigurableLevelGZIPOutputStream(fos, 8192, compressionLevel); 
/*     */ 
/*     */           
/* 140 */           OutputStream os = new BufferedOutputStream(gzipOut, 8192)) {
/* 141 */         byte[] inbuf = new byte[8192];
/*     */         
/*     */         int n;
/* 144 */         while ((n = fis.read(inbuf)) != -1) {
/* 145 */           os.write(inbuf, 0, n);
/*     */         }
/*     */       } 
/*     */       
/* 149 */       if (deleteSource && !source.delete()) {
/* 150 */         LOGGER.warn("Unable to delete {}.", source);
/*     */       }
/*     */       
/* 153 */       return true;
/*     */     } 
/*     */     
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   private static final class ConfigurableLevelGZIPOutputStream
/*     */     extends GZIPOutputStream {
/*     */     ConfigurableLevelGZIPOutputStream(OutputStream out, int bufSize, int level) throws IOException {
/* 162 */       super(out, bufSize);
/* 163 */       this.def.setLevel(level);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reportException(Exception ex) {
/* 174 */     LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 179 */     return GzCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
/*     */   }
/*     */ 
/*     */   
/*     */   public File getSource() {
/* 184 */     return this.source;
/*     */   }
/*     */   
/*     */   public File getDestination() {
/* 188 */     return this.destination;
/*     */   }
/*     */   
/*     */   public boolean isDeleteSource() {
/* 192 */     return this.deleteSource;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\GzCompressAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */