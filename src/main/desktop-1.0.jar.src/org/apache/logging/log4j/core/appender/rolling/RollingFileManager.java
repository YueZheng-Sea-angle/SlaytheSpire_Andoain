/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ConfigurationFactoryData;
/*     */ import org.apache.logging.log4j.core.appender.FileManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;
/*     */ import org.apache.logging.log4j.core.appender.rolling.action.Action;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingFileManager
/*     */   extends FileManager
/*     */ {
/*  59 */   private static RollingFileManagerFactory factory = new RollingFileManagerFactory();
/*     */   private static final int MAX_TRIES = 3;
/*     */   private static final int MIN_DURATION = 100;
/*  62 */   private static final FileTime EPOCH = FileTime.fromMillis(0L);
/*     */   
/*     */   protected long size;
/*     */   private long initialTime;
/*     */   private volatile PatternProcessor patternProcessor;
/*  67 */   private final Semaphore semaphore = new Semaphore(1);
/*  68 */   private final Log4jThreadFactory threadFactory = Log4jThreadFactory.createThreadFactory("RollingFileManager");
/*     */   private volatile TriggeringPolicy triggeringPolicy;
/*     */   private volatile RolloverStrategy rolloverStrategy;
/*     */   private volatile boolean renameEmptyFiles;
/*     */   private volatile boolean initialized;
/*     */   private volatile String fileName;
/*     */   private final boolean directWrite;
/*  75 */   private final CopyOnWriteArrayList<RolloverListener> rolloverListeners = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*  79 */   private final ExecutorService asyncExecutor = new ThreadPoolExecutor(0, 2147483647, 0L, TimeUnit.MILLISECONDS, new EmptyQueue(), (ThreadFactory)this.threadFactory);
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final AtomicReferenceFieldUpdater<RollingFileManager, TriggeringPolicy> triggeringPolicyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, TriggeringPolicy.class, "triggeringPolicy");
/*     */ 
/*     */   
/*  86 */   private static final AtomicReferenceFieldUpdater<RollingFileManager, RolloverStrategy> rolloverStrategyUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, RolloverStrategy.class, "rolloverStrategy");
/*     */ 
/*     */   
/*  89 */   private static final AtomicReferenceFieldUpdater<RollingFileManager, PatternProcessor> patternProcessorUpdater = AtomicReferenceFieldUpdater.newUpdater(RollingFileManager.class, PatternProcessor.class, "patternProcessor");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean writeHeader) {
/*  96 */     this(fileName, pattern, os, append, size, initialTime, triggeringPolicy, rolloverStrategy, advertiseURI, layout, writeHeader, 
/*  97 */         ByteBuffer.wrap(new byte[Constants.ENCODER_BYTE_BUFFER_SIZE]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected RollingFileManager(String fileName, String pattern, OutputStream os, boolean append, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/* 105 */     super((fileName != null) ? fileName : pattern, os, append, false, advertiseURI, layout, writeHeader, buffer);
/*     */     
/* 107 */     this.size = size;
/* 108 */     this.initialTime = initialTime;
/* 109 */     this.triggeringPolicy = triggeringPolicy;
/* 110 */     this.rolloverStrategy = rolloverStrategy;
/* 111 */     this.patternProcessor = new PatternProcessor(pattern);
/* 112 */     this.patternProcessor.setPrevFileTime(initialTime);
/* 113 */     this.fileName = fileName;
/* 114 */     this.directWrite = rolloverStrategy instanceof DirectWriteRolloverStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected RollingFileManager(LoggerContext loggerContext, String fileName, String pattern, OutputStream os, boolean append, boolean createOnDemand, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer buffer) {
/* 122 */     super(loggerContext, (fileName != null) ? fileName : pattern, os, append, false, createOnDemand, advertiseURI, layout, writeHeader, buffer);
/*     */     
/* 124 */     this.size = size;
/* 125 */     this.initialTime = initialTime;
/* 126 */     this.triggeringPolicy = triggeringPolicy;
/* 127 */     this.rolloverStrategy = rolloverStrategy;
/* 128 */     this.patternProcessor = new PatternProcessor(pattern);
/* 129 */     this.patternProcessor.setPrevFileTime(initialTime);
/* 130 */     this.fileName = fileName;
/* 131 */     this.directWrite = rolloverStrategy instanceof DirectWriteRolloverStrategy;
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
/*     */   protected RollingFileManager(LoggerContext loggerContext, String fileName, String pattern, OutputStream os, boolean append, boolean createOnDemand, long size, long initialTime, TriggeringPolicy triggeringPolicy, RolloverStrategy rolloverStrategy, String advertiseURI, Layout<? extends Serializable> layout, String filePermissions, String fileOwner, String fileGroup, boolean writeHeader, ByteBuffer buffer) {
/* 143 */     super(loggerContext, (fileName != null) ? fileName : pattern, os, append, false, createOnDemand, advertiseURI, layout, filePermissions, fileOwner, fileGroup, writeHeader, buffer);
/*     */     
/* 145 */     this.size = size;
/* 146 */     this.initialTime = initialTime;
/* 147 */     this.patternProcessor = new PatternProcessor(pattern);
/* 148 */     this.patternProcessor.setPrevFileTime(initialTime);
/* 149 */     this.triggeringPolicy = triggeringPolicy;
/* 150 */     this.rolloverStrategy = rolloverStrategy;
/* 151 */     this.fileName = fileName;
/* 152 */     this.directWrite = rolloverStrategy instanceof DirectFileRolloverStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 157 */     if (!this.initialized) {
/* 158 */       LOGGER.debug("Initializing triggering policy {}", this.triggeringPolicy);
/* 159 */       this.initialized = true;
/*     */       
/* 161 */       if (this.directWrite) {
/*     */         
/* 163 */         File file = new File(getFileName());
/* 164 */         if (file.exists()) {
/* 165 */           this.size = file.length();
/*     */         } else {
/* 167 */           ((DirectFileRolloverStrategy)this.rolloverStrategy).clearCurrentFileName();
/*     */         } 
/*     */       } 
/* 170 */       this.triggeringPolicy.initialize(this);
/* 171 */       if (this.triggeringPolicy instanceof LifeCycle) {
/* 172 */         ((LifeCycle)this.triggeringPolicy).start();
/*     */       }
/* 174 */       if (this.directWrite) {
/*     */         
/* 176 */         File file = new File(getFileName());
/* 177 */         if (file.exists()) {
/* 178 */           this.size = file.length();
/*     */         } else {
/* 180 */           ((DirectFileRolloverStrategy)this.rolloverStrategy).clearCurrentFileName();
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RollingFileManager getFileManager(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean immediateFlush, boolean createOnDemand, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
/* 212 */     if (strategy instanceof DirectWriteRolloverStrategy && fileName != null) {
/* 213 */       LOGGER.error("The fileName attribute must not be specified with the DirectWriteRolloverStrategy");
/* 214 */       return null;
/*     */     } 
/* 216 */     String name = (fileName == null) ? pattern : fileName;
/* 217 */     return (RollingFileManager)narrow(RollingFileManager.class, (AbstractManager)getManager(name, new FactoryData(fileName, pattern, append, bufferedIO, policy, strategy, advertiseURI, layout, bufferSize, immediateFlush, createOnDemand, filePermissions, fileOwner, fileGroup, configuration), factory));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRolloverListener(RolloverListener listener) {
/* 227 */     this.rolloverListeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeRolloverListener(RolloverListener listener) {
/* 235 */     this.rolloverListeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 244 */     if (this.directWrite) {
/* 245 */       this.fileName = ((DirectFileRolloverStrategy)this.rolloverStrategy).getCurrentFileName(this);
/*     */     }
/* 247 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createParentDir(File file) {
/* 252 */     if (this.directWrite) {
/* 253 */       file.getParentFile().mkdirs();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isDirectWrite() {
/* 258 */     return this.directWrite;
/*     */   }
/*     */   
/*     */   public FileExtension getFileExtension() {
/* 262 */     return this.patternProcessor.getFileExtension();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 269 */     super.write(bytes, offset, length, immediateFlush);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/* 274 */     this.size += length;
/* 275 */     super.writeToDestination(bytes, offset, length);
/*     */   }
/*     */   
/*     */   public boolean isRenameEmptyFiles() {
/* 279 */     return this.renameEmptyFiles;
/*     */   }
/*     */   
/*     */   public void setRenameEmptyFiles(boolean renameEmptyFiles) {
/* 283 */     this.renameEmptyFiles = renameEmptyFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFileSize() {
/* 291 */     return this.size + this.byteBuffer.position();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFileTime() {
/* 299 */     return this.initialTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void checkRollover(LogEvent event) {
/* 307 */     if (this.triggeringPolicy.isTriggeringEvent(event)) {
/* 308 */       rollover();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/*     */     int i;
/* 314 */     LOGGER.debug("Shutting down RollingFileManager {}", getName());
/* 315 */     boolean stopped = true;
/* 316 */     if (this.triggeringPolicy instanceof LifeCycle2) {
/* 317 */       stopped &= ((LifeCycle2)this.triggeringPolicy).stop(timeout, timeUnit);
/* 318 */     } else if (this.triggeringPolicy instanceof LifeCycle) {
/* 319 */       ((LifeCycle)this.triggeringPolicy).stop();
/* 320 */       i = stopped & true;
/*     */     } 
/* 322 */     boolean status = (super.releaseSub(timeout, timeUnit) && i != 0);
/* 323 */     this.asyncExecutor.shutdown();
/*     */     
/*     */     try {
/* 326 */       long millis = timeUnit.toMillis(timeout);
/* 327 */       long waitInterval = (100L < millis) ? millis : 100L;
/*     */       
/* 329 */       for (int count = 1; count <= 3 && !this.asyncExecutor.isTerminated(); count++) {
/* 330 */         this.asyncExecutor.awaitTermination(waitInterval * count, TimeUnit.MILLISECONDS);
/*     */       }
/* 332 */       if (this.asyncExecutor.isTerminated()) {
/* 333 */         LOGGER.debug("All asynchronous threads have terminated");
/*     */       } else {
/* 335 */         this.asyncExecutor.shutdownNow();
/*     */         try {
/* 337 */           this.asyncExecutor.awaitTermination(timeout, timeUnit);
/* 338 */           if (this.asyncExecutor.isTerminated()) {
/* 339 */             LOGGER.debug("All asynchronous threads have terminated");
/*     */           } else {
/* 341 */             LOGGER.debug("RollingFileManager shutting down but some asynchronous services may not have completed");
/*     */           } 
/* 343 */         } catch (InterruptedException inner) {
/* 344 */           LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
/*     */         } 
/*     */       } 
/* 347 */     } catch (InterruptedException ie) {
/* 348 */       this.asyncExecutor.shutdownNow();
/*     */       try {
/* 350 */         this.asyncExecutor.awaitTermination(timeout, timeUnit);
/* 351 */         if (this.asyncExecutor.isTerminated()) {
/* 352 */           LOGGER.debug("All asynchronous threads have terminated");
/*     */         }
/* 354 */       } catch (InterruptedException inner) {
/* 355 */         LOGGER.warn("RollingFileManager stopped but some asynchronous services may not have completed.");
/*     */       } 
/*     */       
/* 358 */       Thread.currentThread().interrupt();
/*     */     } 
/* 360 */     LOGGER.debug("RollingFileManager shutdown completed with status {}", Boolean.valueOf(status));
/* 361 */     return status;
/*     */   }
/*     */   
/*     */   public synchronized void rollover(Date prevFileTime, Date prevRollTime) {
/* 365 */     getPatternProcessor().setPrevFileTime(prevFileTime.getTime());
/* 366 */     getPatternProcessor().setCurrentFileTime(prevRollTime.getTime());
/* 367 */     rollover();
/*     */   }
/*     */   
/*     */   public synchronized void rollover() {
/* 371 */     if (!hasOutputStream() && !isCreateOnDemand() && !isDirectWrite()) {
/*     */       return;
/*     */     }
/* 374 */     String currentFileName = this.fileName;
/* 375 */     if (this.rolloverListeners.size() > 0) {
/* 376 */       for (RolloverListener listener : this.rolloverListeners) {
/*     */         try {
/* 378 */           listener.rolloverTriggered(currentFileName);
/* 379 */         } catch (Exception ex) {
/* 380 */           LOGGER.warn("Rollover Listener {} failed with {}: {}", listener.getClass().getSimpleName(), ex
/* 381 */               .getClass().getName(), ex.getMessage());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 386 */     boolean interrupted = Thread.interrupted();
/*     */     try {
/* 388 */       if (interrupted) {
/* 389 */         LOGGER.warn("RollingFileManager cleared thread interrupted state, continue to rollover");
/*     */       }
/*     */       
/* 392 */       if (rollover(this.rolloverStrategy)) {
/*     */         try {
/* 394 */           this.size = 0L;
/* 395 */           this.initialTime = System.currentTimeMillis();
/* 396 */           createFileAfterRollover();
/* 397 */         } catch (IOException e) {
/* 398 */           logError("Failed to create file after rollover", e);
/*     */         } 
/*     */       }
/*     */     } finally {
/* 402 */       if (interrupted) {
/* 403 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 406 */     if (this.rolloverListeners.size() > 0) {
/* 407 */       for (RolloverListener listener : this.rolloverListeners) {
/*     */         try {
/* 409 */           listener.rolloverComplete(currentFileName);
/* 410 */         } catch (Exception ex) {
/* 411 */           LOGGER.warn("Rollover Listener {} failed with {}: {}", listener.getClass().getSimpleName(), ex
/* 412 */               .getClass().getName(), ex.getMessage());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected void createFileAfterRollover() throws IOException {
/* 419 */     setOutputStream(createOutputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor getPatternProcessor() {
/* 427 */     return this.patternProcessor;
/*     */   }
/*     */   
/*     */   public void setTriggeringPolicy(TriggeringPolicy triggeringPolicy) {
/* 431 */     triggeringPolicy.initialize(this);
/* 432 */     TriggeringPolicy policy = this.triggeringPolicy;
/* 433 */     int count = 0;
/* 434 */     boolean policyUpdated = false;
/*     */     do {
/* 436 */       count++;
/* 437 */     } while (!(policyUpdated = triggeringPolicyUpdater.compareAndSet(this, this.triggeringPolicy, triggeringPolicy)) && count < 3);
/*     */     
/* 439 */     if (policyUpdated) {
/* 440 */       if (triggeringPolicy instanceof LifeCycle) {
/* 441 */         ((LifeCycle)triggeringPolicy).start();
/*     */       }
/* 443 */       if (policy instanceof LifeCycle) {
/* 444 */         ((LifeCycle)policy).stop();
/*     */       }
/* 446 */     } else if (triggeringPolicy instanceof LifeCycle) {
/* 447 */       ((LifeCycle)triggeringPolicy).stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRolloverStrategy(RolloverStrategy rolloverStrategy) {
/* 452 */     rolloverStrategyUpdater.compareAndSet(this, this.rolloverStrategy, rolloverStrategy);
/*     */   }
/*     */   
/*     */   public void setPatternProcessor(PatternProcessor patternProcessor) {
/* 456 */     patternProcessorUpdater.compareAndSet(this, this.patternProcessor, patternProcessor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends TriggeringPolicy> T getTriggeringPolicy() {
/* 467 */     return (T)this.triggeringPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Semaphore getSemaphore() {
/* 476 */     return this.semaphore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RolloverStrategy getRolloverStrategy() {
/* 484 */     return this.rolloverStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean rollover(RolloverStrategy strategy) {
/* 489 */     boolean releaseRequired = false;
/*     */     
/*     */     try {
/* 492 */       this.semaphore.acquire();
/* 493 */       releaseRequired = true;
/* 494 */     } catch (InterruptedException e) {
/* 495 */       logError("Thread interrupted while attempting to check rollover", e);
/* 496 */       return false;
/*     */     } 
/*     */     
/* 499 */     boolean success = true;
/*     */     
/*     */     try {
/* 502 */       RolloverDescription descriptor = strategy.rollover(this);
/* 503 */       if (descriptor != null) {
/* 504 */         writeFooter();
/* 505 */         closeOutputStream();
/* 506 */         if (descriptor.getSynchronous() != null) {
/* 507 */           LOGGER.debug("RollingFileManager executing synchronous {}", descriptor.getSynchronous());
/*     */           try {
/* 509 */             success = descriptor.getSynchronous().execute();
/* 510 */           } catch (Exception ex) {
/* 511 */             success = false;
/* 512 */             logError("Caught error in synchronous task", ex);
/*     */           } 
/*     */         } 
/*     */         
/* 516 */         if (success && descriptor.getAsynchronous() != null) {
/* 517 */           LOGGER.debug("RollingFileManager executing async {}", descriptor.getAsynchronous());
/* 518 */           this.asyncExecutor.execute((Runnable)new AsyncAction(descriptor.getAsynchronous(), this));
/* 519 */           releaseRequired = false;
/*     */         } 
/* 521 */         return true;
/*     */       } 
/* 523 */       return false;
/*     */     } finally {
/* 525 */       if (releaseRequired) {
/* 526 */         this.semaphore.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AsyncAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private final Action action;
/*     */ 
/*     */ 
/*     */     
/*     */     private final RollingFileManager manager;
/*     */ 
/*     */ 
/*     */     
/*     */     public AsyncAction(Action act, RollingFileManager manager) {
/* 546 */       this.action = act;
/* 547 */       this.manager = manager;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean execute() throws IOException {
/*     */       try {
/* 561 */         return this.action.execute();
/*     */       } finally {
/* 563 */         this.manager.semaphore.release();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 572 */       this.action.close();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isComplete() {
/* 582 */       return this.action.isComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 587 */       StringBuilder builder = new StringBuilder();
/* 588 */       builder.append(super.toString());
/* 589 */       builder.append("[action=");
/* 590 */       builder.append(this.action);
/* 591 */       builder.append(", manager=");
/* 592 */       builder.append(this.manager);
/* 593 */       builder.append(", isComplete()=");
/* 594 */       builder.append(isComplete());
/* 595 */       builder.append(", isInterrupted()=");
/* 596 */       builder.append(isInterrupted());
/* 597 */       builder.append("]");
/* 598 */       return builder.toString();
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
/*     */     private final boolean bufferedIO;
/*     */ 
/*     */     
/*     */     private final int bufferSize;
/*     */     
/*     */     private final boolean immediateFlush;
/*     */     
/*     */     private final boolean createOnDemand;
/*     */     
/*     */     private final TriggeringPolicy policy;
/*     */     
/*     */     private final RolloverStrategy strategy;
/*     */     
/*     */     private final String advertiseURI;
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
/*     */     public FactoryData(String fileName, String pattern, boolean append, boolean bufferedIO, TriggeringPolicy policy, RolloverStrategy strategy, String advertiseURI, Layout<? extends Serializable> layout, int bufferSize, boolean immediateFlush, boolean createOnDemand, String filePermissions, String fileOwner, String fileGroup, Configuration configuration) {
/* 641 */       super(configuration);
/* 642 */       this.fileName = fileName;
/* 643 */       this.pattern = pattern;
/* 644 */       this.append = append;
/* 645 */       this.bufferedIO = bufferedIO;
/* 646 */       this.bufferSize = bufferSize;
/* 647 */       this.policy = policy;
/* 648 */       this.strategy = strategy;
/* 649 */       this.advertiseURI = advertiseURI;
/* 650 */       this.layout = layout;
/* 651 */       this.immediateFlush = immediateFlush;
/* 652 */       this.createOnDemand = createOnDemand;
/* 653 */       this.filePermissions = filePermissions;
/* 654 */       this.fileOwner = fileOwner;
/* 655 */       this.fileGroup = fileGroup;
/*     */     }
/*     */     
/*     */     public TriggeringPolicy getTriggeringPolicy() {
/* 659 */       return this.policy;
/*     */     }
/*     */     
/*     */     public RolloverStrategy getRolloverStrategy() {
/* 663 */       return this.strategy;
/*     */     }
/*     */     
/*     */     public String getPattern() {
/* 667 */       return this.pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 672 */       StringBuilder builder = new StringBuilder();
/* 673 */       builder.append(super.toString());
/* 674 */       builder.append("[pattern=");
/* 675 */       builder.append(this.pattern);
/* 676 */       builder.append(", append=");
/* 677 */       builder.append(this.append);
/* 678 */       builder.append(", bufferedIO=");
/* 679 */       builder.append(this.bufferedIO);
/* 680 */       builder.append(", bufferSize=");
/* 681 */       builder.append(this.bufferSize);
/* 682 */       builder.append(", policy=");
/* 683 */       builder.append(this.policy);
/* 684 */       builder.append(", strategy=");
/* 685 */       builder.append(this.strategy);
/* 686 */       builder.append(", advertiseURI=");
/* 687 */       builder.append(this.advertiseURI);
/* 688 */       builder.append(", layout=");
/* 689 */       builder.append(this.layout);
/* 690 */       builder.append(", filePermissions=");
/* 691 */       builder.append(this.filePermissions);
/* 692 */       builder.append(", fileOwner=");
/* 693 */       builder.append(this.fileOwner);
/* 694 */       builder.append("]");
/* 695 */       return builder.toString();
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
/*     */   public void updateData(Object data) {
/* 707 */     FactoryData factoryData = (FactoryData)data;
/* 708 */     setRolloverStrategy(factoryData.getRolloverStrategy());
/* 709 */     setPatternProcessor(new PatternProcessor(factoryData.getPattern(), getPatternProcessor()));
/* 710 */     setTriggeringPolicy(factoryData.getTriggeringPolicy());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RollingFileManagerFactory
/*     */     implements ManagerFactory<RollingFileManager, FactoryData>
/*     */   {
/*     */     private RollingFileManagerFactory() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RollingFileManager createManager(String name, RollingFileManager.FactoryData data) {
/* 726 */       long size = 0L;
/* 727 */       File file = null;
/* 728 */       if (data.fileName != null) {
/* 729 */         file = new File(data.fileName);
/*     */         
/*     */         try {
/* 732 */           FileUtils.makeParentDirs(file);
/* 733 */           boolean created = data.createOnDemand ? false : file.createNewFile();
/* 734 */           RollingFileManager.LOGGER.trace("New file '{}' created = {}", name, Boolean.valueOf(created));
/* 735 */         } catch (IOException ioe) {
/* 736 */           RollingFileManager.LOGGER.error("Unable to create file " + name, ioe);
/* 737 */           return null;
/*     */         } 
/* 739 */         size = data.append ? file.length() : 0L;
/*     */       } 
/*     */       
/*     */       try {
/* 743 */         int actualSize = data.bufferedIO ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
/* 744 */         ByteBuffer buffer = ByteBuffer.wrap(new byte[actualSize]);
/*     */         
/* 746 */         OutputStream os = (data.createOnDemand || data.fileName == null) ? null : new FileOutputStream(data.fileName, data.append);
/*     */         
/* 748 */         long initialTime = (file == null || !file.exists()) ? 0L : RollingFileManager.initialFileTime(file);
/* 749 */         boolean writeHeader = (file != null && file.exists() && file.length() == 0L);
/*     */ 
/*     */ 
/*     */         
/* 753 */         RollingFileManager rm = new RollingFileManager(data.getLoggerContext(), data.fileName, data.pattern, os, data.append, data.createOnDemand, size, initialTime, data.policy, data.strategy, data.advertiseURI, data.layout, data.filePermissions, data.fileOwner, data.fileGroup, writeHeader, buffer);
/* 754 */         if (os != null && rm.isAttributeViewEnabled()) {
/* 755 */           rm.defineAttributeView(file.toPath());
/*     */         }
/*     */         
/* 758 */         return rm;
/* 759 */       } catch (IOException ex) {
/* 760 */         RollingFileManager.LOGGER.error("RollingFileManager (" + name + ") " + ex, ex);
/*     */         
/* 762 */         return null;
/*     */       } 
/*     */     } }
/*     */   
/*     */   private static long initialFileTime(File file) {
/* 767 */     Path path = file.toPath();
/* 768 */     if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 770 */         BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
/* 771 */         FileTime fileTime = attrs.creationTime();
/* 772 */         if (fileTime.compareTo(EPOCH) > 0) {
/* 773 */           LOGGER.debug("Returning file creation time for {}", file.getAbsolutePath());
/* 774 */           return fileTime.toMillis();
/*     */         } 
/* 776 */         LOGGER.info("Unable to obtain file creation time for " + file.getAbsolutePath());
/* 777 */       } catch (Exception ex) {
/* 778 */         LOGGER.info("Unable to calculate file creation time for " + file.getAbsolutePath() + ": " + ex.getMessage());
/*     */       } 
/*     */     }
/* 781 */     return file.lastModified();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class EmptyQueue
/*     */     extends ArrayBlockingQueue<Runnable>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     EmptyQueue() {
/* 792 */       super(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remainingCapacity() {
/* 797 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(Runnable runnable) {
/* 802 */       throw new IllegalStateException("Queue is full");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void put(Runnable runnable) throws InterruptedException {
/* 808 */       throw new InterruptedException("Unable to insert into queue");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offer(Runnable runnable, long timeout, TimeUnit timeUnit) throws InterruptedException {
/* 813 */       Thread.sleep(timeUnit.toMillis(timeout));
/* 814 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends Runnable> collection) {
/* 819 */       if (collection.size() > 0) {
/* 820 */         throw new IllegalArgumentException("Too many items in collection");
/*     */       }
/* 822 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\RollingFileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */