/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.util.Closer;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.NullOutputStream;
/*     */ import org.apache.logging.log4j.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryMappedFileManager
/*     */   extends OutputStreamManager
/*     */ {
/*     */   static final int DEFAULT_REGION_LENGTH = 33554432;
/*     */   private static final int MAX_REMAP_COUNT = 10;
/*  66 */   private static final MemoryMappedFileManagerFactory FACTORY = new MemoryMappedFileManagerFactory();
/*     */   
/*     */   private static final double NANOS_PER_MILLISEC = 1000000.0D;
/*     */   
/*     */   private final boolean immediateFlush;
/*     */   
/*     */   private final int regionLength;
/*     */   private final String advertiseURI;
/*     */   private final RandomAccessFile randomAccessFile;
/*     */   private MappedByteBuffer mappedBuffer;
/*     */   private long mappingOffset;
/*     */   
/*     */   protected MemoryMappedFileManager(RandomAccessFile file, String fileName, OutputStream os, boolean immediateFlush, long position, int regionLength, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) throws IOException {
/*  79 */     super(os, fileName, layout, writeHeader, ByteBuffer.wrap(Constants.EMPTY_BYTE_ARRAY));
/*  80 */     this.immediateFlush = immediateFlush;
/*  81 */     this.randomAccessFile = Objects.<RandomAccessFile>requireNonNull(file, "RandomAccessFile");
/*  82 */     this.regionLength = regionLength;
/*  83 */     this.advertiseURI = advertiseURI;
/*  84 */     this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), getFileName(), position, regionLength);
/*  85 */     this.byteBuffer = this.mappedBuffer;
/*  86 */     this.mappingOffset = position;
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
/*     */   public static MemoryMappedFileManager getFileManager(String fileName, boolean append, boolean immediateFlush, int regionLength, String advertiseURI, Layout<? extends Serializable> layout) {
/* 103 */     return narrow(MemoryMappedFileManager.class, getManager(fileName, new FactoryData(append, immediateFlush, regionLength, advertiseURI, layout), FACTORY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Boolean isEndOfBatch() {
/* 114 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setEndOfBatch(boolean endOfBatch) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 128 */     while (length > this.mappedBuffer.remaining()) {
/* 129 */       int chunk = this.mappedBuffer.remaining();
/* 130 */       this.mappedBuffer.put(bytes, offset, chunk);
/* 131 */       offset += chunk;
/* 132 */       length -= chunk;
/* 133 */       remap();
/*     */     } 
/* 135 */     this.mappedBuffer.put(bytes, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void remap() {
/* 142 */     long offset = this.mappingOffset + this.mappedBuffer.position();
/* 143 */     int length = this.mappedBuffer.remaining() + this.regionLength;
/*     */     try {
/* 145 */       unsafeUnmap(this.mappedBuffer);
/* 146 */       long fileLength = this.randomAccessFile.length() + this.regionLength;
/* 147 */       LOGGER.debug("{} {} extending {} by {} bytes to {}", getClass().getSimpleName(), getName(), getFileName(), 
/* 148 */           Integer.valueOf(this.regionLength), Long.valueOf(fileLength));
/*     */       
/* 150 */       long startNanos = System.nanoTime();
/* 151 */       this.randomAccessFile.setLength(fileLength);
/* 152 */       float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
/* 153 */       LOGGER.debug("{} {} extended {} OK in {} millis", getClass().getSimpleName(), getName(), getFileName(), 
/* 154 */           Float.valueOf(millis));
/*     */       
/* 156 */       this.mappedBuffer = mmap(this.randomAccessFile.getChannel(), getFileName(), offset, length);
/* 157 */       this.byteBuffer = this.mappedBuffer;
/* 158 */       this.mappingOffset = offset;
/* 159 */     } catch (Exception ex) {
/* 160 */       logError("Unable to remap", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 166 */     this.mappedBuffer.force();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean closeOutputStream() {
/* 171 */     long position = this.mappedBuffer.position();
/* 172 */     long length = this.mappingOffset + position;
/*     */     try {
/* 174 */       unsafeUnmap(this.mappedBuffer);
/* 175 */     } catch (Exception ex) {
/* 176 */       logError("Unable to unmap MappedBuffer", ex);
/*     */     } 
/*     */     try {
/* 179 */       LOGGER.debug("MMapAppender closing. Setting {} length to {} (offset {} + position {})", getFileName(), 
/* 180 */           Long.valueOf(length), Long.valueOf(this.mappingOffset), Long.valueOf(position));
/* 181 */       this.randomAccessFile.setLength(length);
/* 182 */       this.randomAccessFile.close();
/* 183 */       return true;
/* 184 */     } catch (IOException ex) {
/* 185 */       logError("Unable to close MemoryMappedFile", ex);
/* 186 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static MappedByteBuffer mmap(FileChannel fileChannel, String fileName, long start, int size) throws IOException {
/* 192 */     for (int i = 1;; i++) {
/*     */       try {
/* 194 */         LOGGER.debug("MMapAppender remapping {} start={}, size={}", fileName, Long.valueOf(start), Integer.valueOf(size));
/*     */         
/* 196 */         long startNanos = System.nanoTime();
/* 197 */         MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, start, size);
/* 198 */         map.order(ByteOrder.nativeOrder());
/*     */         
/* 200 */         float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
/* 201 */         LOGGER.debug("MMapAppender remapped {} OK in {} millis", fileName, Float.valueOf(millis));
/*     */         
/* 203 */         return map;
/* 204 */       } catch (IOException e) {
/* 205 */         if (e.getMessage() == null || !e.getMessage().endsWith("user-mapped section open")) {
/* 206 */           throw e;
/*     */         }
/* 208 */         LOGGER.debug("Remap attempt {}/{} failed. Retrying...", Integer.valueOf(i), Integer.valueOf(10), e);
/* 209 */         if (i < 10) {
/* 210 */           Thread.yield();
/*     */         } else {
/*     */           try {
/* 213 */             Thread.sleep(1L);
/* 214 */           } catch (InterruptedException ignored) {
/* 215 */             Thread.currentThread().interrupt();
/* 216 */             throw e;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void unsafeUnmap(MappedByteBuffer mbb) throws PrivilegedActionException {
/* 224 */     LOGGER.debug("MMapAppender unmapping old buffer...");
/* 225 */     long startNanos = System.nanoTime();
/* 226 */     AccessController.doPrivileged(() -> {
/*     */           Method getCleanerMethod = mbb.getClass().getMethod("cleaner", new Class[0]);
/*     */           getCleanerMethod.setAccessible(true);
/*     */           Object cleaner = getCleanerMethod.invoke(mbb, new Object[0]);
/*     */           Method cleanMethod = cleaner.getClass().getMethod("clean", new Class[0]);
/*     */           cleanMethod.invoke(cleaner, new Object[0]);
/*     */           return null;
/*     */         });
/* 234 */     float millis = (float)((System.nanoTime() - startNanos) / 1000000.0D);
/* 235 */     LOGGER.debug("MMapAppender unmapped buffer OK in {} millis", Float.valueOf(millis));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 244 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegionLength() {
/* 253 */     return this.regionLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImmediateFlush() {
/* 263 */     return this.immediateFlush;
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
/*     */   public Map<String, String> getContentFormat() {
/* 276 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 277 */     result.put("fileURI", this.advertiseURI);
/* 278 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flushBuffer(ByteBuffer buffer) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getByteBuffer() {
/* 288 */     return this.mappedBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer drain(ByteBuffer buf) {
/* 293 */     remap();
/* 294 */     return this.mappedBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int regionLength;
/*     */ 
/*     */     
/*     */     private final String advertiseURI;
/*     */ 
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean immediateFlush, int regionLength, String advertiseURI, Layout<? extends Serializable> layout) {
/* 318 */       this.append = append;
/* 319 */       this.immediateFlush = immediateFlush;
/* 320 */       this.regionLength = regionLength;
/* 321 */       this.advertiseURI = advertiseURI;
/* 322 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MemoryMappedFileManagerFactory
/*     */     implements ManagerFactory<MemoryMappedFileManager, FactoryData>
/*     */   {
/*     */     private MemoryMappedFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MemoryMappedFileManager createManager(String name, MemoryMappedFileManager.FactoryData data) {
/* 342 */       File file = new File(name);
/* 343 */       if (!data.append) {
/* 344 */         file.delete();
/*     */       }
/*     */       
/* 347 */       boolean writeHeader = (!data.append || !file.exists());
/* 348 */       NullOutputStream nullOutputStream = NullOutputStream.getInstance();
/* 349 */       RandomAccessFile raf = null;
/*     */       try {
/* 351 */         FileUtils.makeParentDirs(file);
/* 352 */         raf = new RandomAccessFile(name, "rw");
/* 353 */         long position = data.append ? raf.length() : 0L;
/* 354 */         raf.setLength(position + data.regionLength);
/* 355 */         return new MemoryMappedFileManager(raf, name, (OutputStream)nullOutputStream, data.immediateFlush, position, data.regionLength, data
/* 356 */             .advertiseURI, data.layout, writeHeader);
/* 357 */       } catch (Exception ex) {
/* 358 */         AbstractManager.LOGGER.error("MemoryMappedFileManager (" + name + ") " + ex, ex);
/* 359 */         Closer.closeSilently(raf);
/*     */         
/* 361 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\MemoryMappedFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */