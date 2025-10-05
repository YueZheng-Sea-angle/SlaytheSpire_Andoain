/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.NullOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomAccessFileManager
/*     */   extends OutputStreamManager
/*     */ {
/*     */   static final int DEFAULT_BUFFER_SIZE = 262144;
/*  42 */   private static final RandomAccessFileManagerFactory FACTORY = new RandomAccessFileManagerFactory();
/*     */   
/*     */   private final String advertiseURI;
/*     */   
/*     */   private final RandomAccessFile randomAccessFile;
/*     */ 
/*     */   
/*     */   protected RandomAccessFileManager(LoggerContext loggerContext, RandomAccessFile file, String fileName, OutputStream os, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) {
/*  50 */     super(loggerContext, os, fileName, false, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*  51 */     this.randomAccessFile = file;
/*  52 */     this.advertiseURI = advertiseURI;
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
/*     */   public static RandomAccessFileManager getFileManager(String fileName, boolean append, boolean immediateFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/*  72 */     return narrow(RandomAccessFileManager.class, getManager(fileName, new FactoryData(append, immediateFlush, bufferSize, advertiseURI, layout, configuration), FACTORY));
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
/*  83 */     return Boolean.FALSE;
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
/*     */   protected void writeToDestination(byte[] bytes, int offset, int length) {
/*     */     try {
/*  98 */       this.randomAccessFile.write(bytes, offset, length);
/*  99 */     } catch (IOException ex) {
/* 100 */       String msg = "Error writing to RandomAccessFile " + getName();
/* 101 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 107 */     flushBuffer(this.byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean closeOutputStream() {
/* 112 */     flush();
/*     */     try {
/* 114 */       this.randomAccessFile.close();
/* 115 */       return true;
/* 116 */     } catch (IOException ex) {
/* 117 */       logError("Unable to close RandomAccessFile", ex);
/* 118 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 128 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 136 */     return this.byteBuffer.capacity();
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
/*     */   public Map<String, String> getContentFormat() {
/* 150 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 151 */     result.put("fileURI", this.advertiseURI);
/* 152 */     return result;
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
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final String advertiseURI;
/*     */     
/*     */     private final Layout<? extends Serializable> layout;
/*     */ 
/*     */     
/*     */     public FactoryData(boolean append, boolean immediateFlush, int bufferSize, String advertiseURI, Layout<? extends Serializable> layout, Configuration configuration) {
/* 174 */       super(configuration);
/* 175 */       this.append = append;
/* 176 */       this.immediateFlush = immediateFlush;
/* 177 */       this.bufferSize = bufferSize;
/* 178 */       this.advertiseURI = advertiseURI;
/* 179 */       this.layout = layout;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RandomAccessFileManagerFactory
/*     */     implements ManagerFactory<RandomAccessFileManager, FactoryData>
/*     */   {
/*     */     private RandomAccessFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RandomAccessFileManager createManager(String name, RandomAccessFileManager.FactoryData data) {
/* 198 */       File file = new File(name);
/* 199 */       if (!data.append) {
/* 200 */         file.delete();
/*     */       }
/*     */       
/* 203 */       boolean writeHeader = (!data.append || !file.exists());
/* 204 */       NullOutputStream nullOutputStream = NullOutputStream.getInstance();
/*     */       
/*     */       try {
/* 207 */         FileUtils.makeParentDirs(file);
/* 208 */         RandomAccessFile raf = new RandomAccessFile(name, "rw");
/* 209 */         if (data.append) {
/* 210 */           raf.seek(raf.length());
/*     */         } else {
/* 212 */           raf.setLength(0L);
/*     */         } 
/* 214 */         return new RandomAccessFileManager(data.getLoggerContext(), raf, name, (OutputStream)nullOutputStream, data
/* 215 */             .bufferSize, data.advertiseURI, data.layout, writeHeader);
/* 216 */       } catch (Exception ex) {
/* 217 */         AbstractManager.LOGGER.error("RandomAccessFileManager (" + name + ") " + ex, ex);
/*     */         
/* 219 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */