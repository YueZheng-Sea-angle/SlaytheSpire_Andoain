/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationListener;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrappedFileWatcher
/*     */   extends AbstractWatcher
/*     */   implements FileWatcher
/*     */ {
/*     */   private final FileWatcher watcher;
/*     */   private volatile long lastModifiedMillis;
/*     */   
/*     */   public WrappedFileWatcher(FileWatcher watcher, Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
/*  38 */     super(configuration, reconfigurable, configurationListeners);
/*  39 */     this.watcher = watcher;
/*  40 */     this.lastModifiedMillis = lastModifiedMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public WrappedFileWatcher(FileWatcher watcher) {
/*  45 */     super(null, null, null);
/*  46 */     this.watcher = watcher;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/*  51 */     return this.lastModifiedMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fileModified(File file) {
/*  56 */     this.watcher.fileModified(file);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isModified() {
/*  61 */     long lastModified = getSource().getFile().lastModified();
/*  62 */     if (this.lastModifiedMillis != lastModified) {
/*  63 */       this.lastModifiedMillis = lastModified;
/*  64 */       return true;
/*     */     } 
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ConfigurationListener> getListeners() {
/*  71 */     if (super.getListeners() != null) {
/*  72 */       return Collections.unmodifiableList(super.getListeners());
/*     */     }
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void modified() {
/*  80 */     if (getListeners() != null) {
/*  81 */       super.modified();
/*     */     }
/*  83 */     fileModified(getSource().getFile());
/*  84 */     this.lastModifiedMillis = getSource().getFile().lastModified();
/*     */   }
/*     */ 
/*     */   
/*     */   public void watching(Source source) {
/*  89 */     this.lastModifiedMillis = source.getFile().lastModified();
/*  90 */     super.watching(source);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
/*  96 */     WrappedFileWatcher watcher = new WrappedFileWatcher(this.watcher, getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
/*     */     
/*  98 */     if (getSource() != null) {
/*  99 */       watcher.watching(getSource());
/*     */     }
/* 101 */     return watcher;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\WrappedFileWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */