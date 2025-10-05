/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.util.AbstractWatcher;
/*    */ import org.apache.logging.log4j.core.util.FileWatcher;
/*    */ import org.apache.logging.log4j.core.util.Source;
/*    */ import org.apache.logging.log4j.core.util.Watcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigurationFileWatcher
/*    */   extends AbstractWatcher
/*    */   implements FileWatcher
/*    */ {
/*    */   private File file;
/*    */   private long lastModifiedMillis;
/*    */   
/*    */   public ConfigurationFileWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners, long lastModifiedMillis) {
/* 37 */     super(configuration, reconfigurable, configurationListeners);
/* 38 */     this.lastModifiedMillis = lastModifiedMillis;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLastModified() {
/* 43 */     return (this.file != null) ? this.file.lastModified() : 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fileModified(File file) {
/* 48 */     this.lastModifiedMillis = file.lastModified();
/*    */   }
/*    */ 
/*    */   
/*    */   public void watching(Source source) {
/* 53 */     this.file = source.getFile();
/* 54 */     this.lastModifiedMillis = this.file.lastModified();
/* 55 */     super.watching(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isModified() {
/* 60 */     return (this.lastModifiedMillis != this.file.lastModified());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationListener> listeners, long lastModifiedMillis) {
/* 66 */     ConfigurationFileWatcher watcher = new ConfigurationFileWatcher(getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
/*    */     
/* 68 */     if (getSource() != null) {
/* 69 */       watcher.watching(getSource());
/*    */     }
/* 71 */     return (Watcher)watcher;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\ConfigurationFileWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */