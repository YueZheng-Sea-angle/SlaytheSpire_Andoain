/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.nio.file.attribute.PosixFilePermissions;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileManager
/*     */   extends OutputStreamManager
/*     */ {
/*  53 */   private static final FileManagerFactory FACTORY = new FileManagerFactory();
/*     */   
/*     */   private final boolean isAppend;
/*     */   
/*     */   private final boolean createOnDemand;
/*     */   
/*     */   private final boolean isLocking;
/*     */   
/*     */   private final String advertiseURI;
/*     */   
/*     */   private final int bufferSize;
/*     */   
/*     */   private final Set<PosixFilePermission> filePermissions;
/*     */   private final String fileOwner;
/*     */   private final String fileGroup;
/*     */   private final boolean attributeViewEnabled;
/*     */   
/*     */   @Deprecated
/*     */   protected FileManager(String fileName, OutputStream os, boolean append, boolean locking, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean writeHeader) {
/*  72 */     this(fileName, os, append, locking, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected FileManager(String fileName, OutputStream os, boolean append, boolean locking, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/*  83 */     super(os, fileName, layout, writeHeader, buffer);
/*  84 */     this.isAppend = append;
/*  85 */     this.createOnDemand = false;
/*  86 */     this.isLocking = locking;
/*  87 */     this.advertiseURI = advertiseURI;
/*  88 */     this.bufferSize = buffer.capacity();
/*  89 */     this.filePermissions = null;
/*  90 */     this.fileOwner = null;
/*  91 */     this.fileGroup = null;
/*  92 */     this.attributeViewEnabled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected FileManager(LoggerContext loggerContext, String fileName, OutputStream os, boolean append, boolean locking, boolean createOnDemand, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/* 103 */     super(loggerContext, os, fileName, createOnDemand, layout, writeHeader, buffer);
/* 104 */     this.isAppend = append;
/* 105 */     this.createOnDemand = createOnDemand;
/* 106 */     this.isLocking = locking;
/* 107 */     this.advertiseURI = advertiseURI;
/* 108 */     this.bufferSize = buffer.capacity();
/* 109 */     this.filePermissions = null;
/* 110 */     this.fileOwner = null;
/* 111 */     this.fileGroup = null;
/* 112 */     this.attributeViewEnabled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileManager(LoggerContext loggerContext, String fileName, OutputStream os, boolean append, boolean locking, boolean createOnDemand, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, boolean writeHeader, ByteBuffer buffer) {
/* 122 */     super(loggerContext, os, fileName, createOnDemand, layout, writeHeader, buffer);
/* 123 */     this.isAppend = append;
/* 124 */     this.createOnDemand = createOnDemand;
/* 125 */     this.isLocking = locking;
/* 126 */     this.advertiseURI = advertiseURI;
/* 127 */     this.bufferSize = buffer.capacity();
/*     */     
/* 129 */     Set<String> views = FileSystems.getDefault().supportedFileAttributeViews();
/* 130 */     if (views.contains("posix")) {
/* 131 */       this.filePermissions = (filePermissions != null) ? PosixFilePermissions.fromString(filePermissions) : null;
/* 132 */       this.fileGroup = fileGroup;
/*     */     } else {
/* 134 */       this.filePermissions = null;
/* 135 */       this.fileGroup = null;
/* 136 */       if (filePermissions != null) {
/* 137 */         LOGGER.warn("Posix file attribute permissions defined but it is not supported by this files system.");
/*     */       }
/* 139 */       if (fileGroup != null) {
/* 140 */         LOGGER.warn("Posix file attribute group defined but it is not supported by this files system.");
/*     */       }
/*     */     } 
/*     */     
/* 144 */     if (views.contains("owner")) {
/* 145 */       this.fileOwner = fileOwner;
/*     */     } else {
/* 147 */       this.fileOwner = null;
/* 148 */       if (fileOwner != null) {
/* 149 */         LOGGER.warn("Owner file attribute defined but it is not supported by this files system.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 154 */     this.attributeViewEnabled = (this.filePermissions != null || this.fileOwner != null || this.fileGroup != null);
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
/*     */   public static FileManager getFileManager(String fileName, boolean append, boolean locking, boolean bufferedIo, boolean createOnDemand, String advertiseUri, Layout<? extends Serializable> layout, int bufferSize, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
/* 179 */     if (locking && bufferedIo) {
/* 180 */       locking = false;
/*     */     }
/* 182 */     return narrow(FileManager.class, getManager(fileName, new FactoryData(append, locking, bufferedIo, bufferSize, createOnDemand, advertiseUri, layout, filePermissions, fileOwner, fileGroup, configuration), FACTORY));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream createOutputStream() throws IOException {
/* 188 */     String filename = getFileName();
/* 189 */     LOGGER.debug("Now writing to {} at {}", filename, new Date());
/* 190 */     File file = new File(filename);
/* 191 */     createParentDir(file);
/* 192 */     FileOutputStream fos = new FileOutputStream(file, this.isAppend);
/* 193 */     if (file.exists() && file.length() == 0L) {
/*     */       try {
/* 195 */         FileTime now = FileTime.fromMillis(System.currentTimeMillis());
/* 196 */         Files.setAttribute(file.toPath(), "creationTime", now, new java.nio.file.LinkOption[0]);
/* 197 */       } catch (Exception ex) {
/* 198 */         LOGGER.warn("Unable to set current file time for {}", filename);
/*     */       } 
/* 200 */       writeHeader(fos);
/*     */     } 
/* 202 */     defineAttributeView(Paths.get(filename, new String[0]));
/* 203 */     return fos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createParentDir(File file) {}
/*     */   
/*     */   protected void defineAttributeView(Path path) {
/* 210 */     if (this.attributeViewEnabled) {
/*     */       
/*     */       try {
/* 213 */         path.toFile().createNewFile();
/*     */         
/* 215 */         FileUtils.defineFilePosixAttributeView(path, this.filePermissions, this.fileOwner, this.fileGroup);
/* 216 */       } catch (Exception e) {
/* 217 */         LOGGER.error("Could not define attribute view on path \"{}\" got {}", path, e.getMessage(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 225 */     if (this.isLocking) {
/*     */       
/*     */       try {
/* 228 */         FileChannel channel = ((FileOutputStream)getOutputStream()).getChannel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 237 */         try (FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
/* 238 */           super.write(bytes, offset, length, immediateFlush);
/*     */         } 
/* 240 */       } catch (IOException ex) {
/* 241 */         throw new AppenderLoggingException("Unable to obtain lock on " + getName(), ex);
/*     */       } 
/*     */     } else {
/* 244 */       super.write(bytes, offset, length, immediateFlush);
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
/*     */ 
/*     */   
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/* 258 */     if (this.isLocking) {
/*     */       
/*     */       try {
/* 261 */         FileChannel channel = ((FileOutputStream)getOutputStream()).getChannel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 270 */         try (FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
/* 271 */           super.writeToDestination(bytes, offset, length);
/*     */         } 
/* 273 */       } catch (IOException ex) {
/* 274 */         throw new AppenderLoggingException("Unable to obtain lock on " + getName(), ex);
/*     */       } 
/*     */     } else {
/* 277 */       super.writeToDestination(bytes, offset, length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 286 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAppend() {
/* 293 */     return this.isAppend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCreateOnDemand() {
/* 301 */     return this.createOnDemand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocking() {
/* 309 */     return this.isLocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 318 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<PosixFilePermission> getFilePermissions() {
/* 328 */     return this.filePermissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileOwner() {
/* 338 */     return this.fileOwner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileGroup() {
/* 348 */     return this.fileGroup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttributeViewEnabled() {
/* 357 */     return this.attributeViewEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 367 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 368 */     result.put("fileURI", this.advertiseURI);
/* 369 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */     extends ConfigurationFactoryData
/*     */   {
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean locking;
/*     */ 
/*     */     
/*     */     private final boolean bufferedIo;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */     
/*     */     private final boolean createOnDemand;
/*     */ 
/*     */     
/*     */     private final String advertiseURI;
/*     */ 
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */     
/*     */     private final String filePermissions;
/*     */     
/*     */     private final String fileOwner;
/*     */     
/*     */     private final String fileGroup;
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean locking, boolean bufferedIo, int bufferSize, boolean createOnDemand, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
/* 405 */       super(configuration);
/* 406 */       this.append = append;
/* 407 */       this.locking = locking;
/* 408 */       this.bufferedIo = bufferedIo;
/* 409 */       this.bufferSize = bufferSize;
/* 410 */       this.createOnDemand = createOnDemand;
/* 411 */       this.advertiseURI = advertiseURI;
/* 412 */       this.layout = layout;
/* 413 */       this.filePermissions = filePermissions;
/* 414 */       this.fileOwner = fileOwner;
/* 415 */       this.fileGroup = fileGroup;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FileManagerFactory
/*     */     implements ManagerFactory<FileManager, FactoryData>
/*     */   {
/*     */     private FileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FileManager createManager(String name, FileManager.FactoryData data) {
/* 432 */       File file = new File(name);
/*     */       try {
/* 434 */         FileUtils.makeParentDirs(file);
/* 435 */         boolean writeHeader = (!data.append || !file.exists());
/* 436 */         int actualSize = data.bufferedIo ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
/* 437 */         ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[actualSize]);
/* 438 */         FileOutputStream fos = data.createOnDemand ? null : new FileOutputStream(file, data.append);
/*     */ 
/*     */         
/* 441 */         FileManager fm = new FileManager(data.getLoggerContext(), name, fos, data.append, data.locking, data.createOnDemand, data.advertiseURI, data.layout, data.filePermissions, data.fileOwner, data.fileGroup, writeHeader, byteBuffer);
/* 442 */         if (fos != null && fm.attributeViewEnabled) {
/* 443 */           fm.defineAttributeView(file.toPath());
/*     */         }
/* 445 */         return fm;
/* 446 */       } catch (IOException ex) {
/* 447 */         AbstractManager.LOGGER.error("FileManager (" + name + ") " + ex, ex);
/*     */         
/* 449 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\FileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */