/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
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
/*     */ public class RollingRandomAccessFileManager
/*     */   extends RollingFileManager
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 262144;
/*  46 */   private static final RollingRandomAccessFileManagerFactory FACTORY = new RollingRandomAccessFileManagerFactory();
/*     */ 
/*     */ 
/*     */   
/*     */   private RandomAccessFile randomAccessFile;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RollingRandomAccessFileManager(LoggerContext loggerContext, RandomAccessFile raf, String fileName, String pattern, OutputStream os, boolean append, boolean immediateFlush, int bufferSize, long size, long time, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader) {
/*  56 */     this(loggerContext, raf, fileName, pattern, os, append, immediateFlush, bufferSize, size, time, policy, strategy, advertiseURI, layout, (String)null, (String)null, (String)null, writeHeader);
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
/*     */   public RollingRandomAccessFileManager(LoggerContext loggerContext, RandomAccessFile raf, String fileName, String pattern, OutputStream os, boolean append, boolean immediateFlush, int bufferSize, long size, long initialTime, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, boolean writeHeader) {
/*  70 */     super(loggerContext, fileName, pattern, os, append, false, size, initialTime, policy, strategy, advertiseURI, layout, filePermissions, fileOwner, fileGroup, writeHeader, 
/*  71 */         ByteBuffer.wrap(new byte[bufferSize]));
/*  72 */     this.randomAccessFile = raf;
/*  73 */     writeHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeHeader() {
/*  80 */     if (this.layout == null) {
/*     */       return;
/*     */     }
/*  83 */     byte[] header = this.layout.getHeader();
/*  84 */     if (header == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  88 */       if (this.randomAccessFile != null && this.randomAccessFile.length() == 0L)
/*     */       {
/*  90 */         this.randomAccessFile.write(header, 0, header.length);
/*     */       }
/*  92 */     } catch (IOException e) {
/*  93 */       logError("Unable to write header", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RollingRandomAccessFileManager getRollingRandomAccessFileManager(String fileName, String filePattern, boolean isAppend, boolean immediateFlush, int bufferSize, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
/* 102 */     if (strategy instanceof DirectWriteRolloverStrategy && fileName != null) {
/* 103 */       LOGGER.error("The fileName attribute must not be specified with the DirectWriteRolloverStrategy");
/* 104 */       return null;
/*     */     } 
/* 106 */     String name = (fileName == null) ? filePattern : fileName;
/* 107 */     return (RollingRandomAccessFileManager)narrow(RollingRandomAccessFileManager.class, (AbstractManager)getManager(name, new FactoryData(fileName, filePattern, isAppend, immediateFlush, bufferSize, policy, strategy, advertiseURI, layout, filePermissions, fileOwner, fileGroup, configuration), FACTORY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Boolean isEndOfBatch() {
/* 119 */     return Boolean.FALSE;
/*     */   }
/*     */ 
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
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 135 */     super.write(bytes, offset, length, immediateFlush);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/*     */     try {
/* 141 */       if (this.randomAccessFile == null) {
/* 142 */         String fileName = getFileName();
/* 143 */         File file = new File(fileName);
/* 144 */         FileUtils.makeParentDirs(file);
/* 145 */         createFileAfterRollover(fileName);
/*     */       } 
/* 147 */       this.randomAccessFile.write(bytes, offset, length);
/* 148 */       this.size += length;
/* 149 */     } catch (IOException ex) {
/* 150 */       String msg = "Error writing to RandomAccessFile " + getName();
/* 151 */       throw new AppenderLoggingException(msg, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createFileAfterRollover() throws IOException {
/* 157 */     createFileAfterRollover(getFileName());
/*     */   }
/*     */   
/*     */   private void createFileAfterRollover(String fileName) throws IOException {
/* 161 */     this.randomAccessFile = new RandomAccessFile(fileName, "rw");
/* 162 */     if (isAttributeViewEnabled()) {
/* 163 */       defineAttributeView(Paths.get(fileName, new String[0]));
/*     */     }
/* 165 */     if (isAppend()) {
/* 166 */       this.randomAccessFile.seek(this.randomAccessFile.length());
/*     */     }
/* 168 */     writeHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 173 */     flushBuffer(this.byteBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean closeOutputStream() {
/* 178 */     flush();
/* 179 */     if (this.randomAccessFile != null) {
/*     */       try {
/* 181 */         this.randomAccessFile.close();
/* 182 */         return true;
/* 183 */       } catch (IOException e) {
/* 184 */         logError("Unable to close RandomAccessFile", e);
/* 185 */         return false;
/*     */       } 
/*     */     }
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 198 */     return this.byteBuffer.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RollingRandomAccessFileManagerFactory
/*     */     implements ManagerFactory<RollingRandomAccessFileManager, FactoryData>
/*     */   {
/*     */     private RollingRandomAccessFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RollingRandomAccessFileManager createManager(String name, RollingRandomAccessFileManager.FactoryData data) {
/* 216 */       File file = null;
/* 217 */       long size = 0L;
/* 218 */       long time = System.currentTimeMillis();
/* 219 */       RandomAccessFile raf = null;
/* 220 */       if (data.fileName != null) {
/* 221 */         file = new File(name);
/*     */         
/* 223 */         if (!data.append) {
/* 224 */           file.delete();
/*     */         }
/* 226 */         size = data.append ? file.length() : 0L;
/* 227 */         if (file.exists()) {
/* 228 */           time = file.lastModified();
/*     */         }
/*     */         try {
/* 231 */           FileUtils.makeParentDirs(file);
/* 232 */           raf = new RandomAccessFile(name, "rw");
/* 233 */           if (data.append) {
/* 234 */             long length = raf.length();
/* 235 */             RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} seek to {}", name, Long.valueOf(length));
/* 236 */             raf.seek(length);
/*     */           } else {
/* 238 */             RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} set length to 0", name);
/* 239 */             raf.setLength(0L);
/*     */           } 
/* 241 */         } catch (IOException ex) {
/* 242 */           RollingRandomAccessFileManager.LOGGER.error("Cannot access RandomAccessFile " + ex, ex);
/* 243 */           if (raf != null) {
/*     */             try {
/* 245 */               raf.close();
/* 246 */             } catch (IOException e) {
/* 247 */               RollingRandomAccessFileManager.LOGGER.error("Cannot close RandomAccessFile {}", name, e);
/*     */             } 
/*     */           }
/* 250 */           return null;
/*     */         } 
/*     */       } 
/* 253 */       boolean writeHeader = (!data.append || file == null || !file.exists());
/*     */ 
/*     */ 
/*     */       
/* 257 */       RollingRandomAccessFileManager rrm = new RollingRandomAccessFileManager(data.getLoggerContext(), raf, name, data.pattern, (OutputStream)NullOutputStream.getInstance(), data.append, data.immediateFlush, data.bufferSize, size, time, data.policy, data.strategy, data.advertiseURI, data.layout, data.filePermissions, data.fileOwner, data.fileGroup, writeHeader);
/* 258 */       if (rrm.isAttributeViewEnabled()) {
/* 259 */         rrm.defineAttributeView(file.toPath());
/*     */       }
/* 261 */       return rrm;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */     extends ConfigurationFactoryData
/*     */   {
/*     */     private final String fileName;
/*     */ 
/*     */     
/*     */     private final String pattern;
/*     */ 
/*     */     
/*     */     private final boolean append;
/*     */ 
/*     */     
/*     */     private final boolean immediateFlush;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */ 
/*     */     
/*     */     private final TriggeringPolicy policy;
/*     */ 
/*     */     
/*     */     private final RolloverStrategy strategy;
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
/*     */     public FactoryData(String fileName, String pattern, boolean append, boolean immediateFlush, int bufferSize, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
/* 304 */       super(configuration);
/* 305 */       this.fileName = fileName;
/* 306 */       this.pattern = pattern;
/* 307 */       this.append = append;
/* 308 */       this.immediateFlush = immediateFlush;
/* 309 */       this.bufferSize = bufferSize;
/* 310 */       this.policy = policy;
/* 311 */       this.strategy = strategy;
/* 312 */       this.advertiseURI = advertiseURI;
/* 313 */       this.layout = layout;
/* 314 */       this.filePermissions = filePermissions;
/* 315 */       this.fileOwner = fileOwner;
/* 316 */       this.fileGroup = fileGroup;
/*     */     }
/*     */     
/*     */     public String getPattern() {
/* 320 */       return this.pattern;
/*     */     }
/*     */     
/*     */     public TriggeringPolicy getTriggeringPolicy() {
/* 324 */       return this.policy;
/*     */     }
/*     */     
/*     */     public RolloverStrategy getRolloverStrategy() {
/* 328 */       return this.strategy;
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
/*     */   public void updateData(Object data) {
/* 341 */     FactoryData factoryData = (FactoryData)data;
/* 342 */     setRolloverStrategy(factoryData.getRolloverStrategy());
/* 343 */     setPatternProcessor(new PatternProcessor(factoryData.getPattern(), getPatternProcessor()));
/* 344 */     setTriggeringPolicy(factoryData.getTriggeringPolicy());
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\RollingRandomAccessFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */