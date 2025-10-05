/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationListener;
/*    */ import org.apache.logging.log4j.core.config.Reconfigurable;
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
/*    */ 
/*    */ public abstract class AbstractWatcher
/*    */   implements Watcher
/*    */ {
/*    */   private final Reconfigurable reconfigurable;
/*    */   private final List<ConfigurationListener> configurationListeners;
/*    */   private final Log4jThreadFactory threadFactory;
/*    */   private final Configuration configuration;
/*    */   private Source source;
/*    */   
/*    */   public AbstractWatcher(Configuration configuration, Reconfigurable reconfigurable, List<ConfigurationListener> configurationListeners) {
/* 38 */     this.configuration = configuration;
/* 39 */     this.reconfigurable = reconfigurable;
/* 40 */     this.configurationListeners = configurationListeners;
/* 41 */     this
/* 42 */       .threadFactory = (configurationListeners != null) ? Log4jThreadFactory.createDaemonThreadFactory("ConfiguratonFileWatcher") : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<ConfigurationListener> getListeners() {
/* 47 */     return this.configurationListeners;
/*    */   }
/*    */ 
/*    */   
/*    */   public void modified() {
/* 52 */     for (ConfigurationListener configurationListener : this.configurationListeners) {
/* 53 */       Thread thread = this.threadFactory.newThread(new ReconfigurationRunnable(configurationListener, this.reconfigurable));
/* 54 */       thread.start();
/*    */     } 
/*    */   }
/*    */   
/*    */   public Configuration getConfiguration() {
/* 59 */     return this.configuration;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract long getLastModified();
/*    */ 
/*    */   
/*    */   public abstract boolean isModified();
/*    */ 
/*    */   
/*    */   public void watching(Source source) {
/* 70 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public Source getSource() {
/* 75 */     return this.source;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class ReconfigurationRunnable
/*    */     implements Runnable
/*    */   {
/*    */     private final ConfigurationListener configurationListener;
/*    */     
/*    */     private final Reconfigurable reconfigurable;
/*    */     
/*    */     public ReconfigurationRunnable(ConfigurationListener configurationListener, Reconfigurable reconfigurable) {
/* 87 */       this.configurationListener = configurationListener;
/* 88 */       this.reconfigurable = reconfigurable;
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 93 */       this.configurationListener.onChange(this.reconfigurable);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\AbstractWatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */