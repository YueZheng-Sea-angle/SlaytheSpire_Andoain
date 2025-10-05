/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
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
/*     */ public abstract class AbstractFileAppender<M extends OutputStreamManager>
/*     */   extends AbstractOutputStreamAppender<M>
/*     */ {
/*     */   private final String fileName;
/*     */   private final Advertiser advertiser;
/*     */   private final Object advertisement;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute
/*     */     private boolean locking;
/*     */     @PluginBuilderAttribute
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute
/*     */     private String advertiseUri;
/*     */     @PluginBuilderAttribute
/*     */     private boolean createOnDemand;
/*     */     @PluginBuilderAttribute
/*     */     private String filePermissions;
/*     */     @PluginBuilderAttribute
/*     */     private String fileOwner;
/*     */     @PluginBuilderAttribute
/*     */     private String fileGroup;
/*     */     
/*     */     public String getAdvertiseUri() {
/*  73 */       return this.advertiseUri;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/*  77 */       return this.fileName;
/*     */     }
/*     */     
/*     */     public boolean isAdvertise() {
/*  81 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public boolean isAppend() {
/*  85 */       return this.append;
/*     */     }
/*     */     
/*     */     public boolean isCreateOnDemand() {
/*  89 */       return this.createOnDemand;
/*     */     }
/*     */     
/*     */     public boolean isLocking() {
/*  93 */       return this.locking;
/*     */     }
/*     */     
/*     */     public String getFilePermissions() {
/*  97 */       return this.filePermissions;
/*     */     }
/*     */     
/*     */     public String getFileOwner() {
/* 101 */       return this.fileOwner;
/*     */     }
/*     */     
/*     */     public String getFileGroup() {
/* 105 */       return this.fileGroup;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 109 */       this.advertise = advertise;
/* 110 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseUri(String advertiseUri) {
/* 114 */       this.advertiseUri = advertiseUri;
/* 115 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 119 */       this.append = append;
/* 120 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileName(String fileName) {
/* 124 */       this.fileName = fileName;
/* 125 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withCreateOnDemand(boolean createOnDemand) {
/* 129 */       this.createOnDemand = createOnDemand;
/* 130 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withLocking(boolean locking) {
/* 134 */       this.locking = locking;
/* 135 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFilePermissions(String filePermissions) {
/* 139 */       this.filePermissions = filePermissions;
/* 140 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileOwner(String fileOwner) {
/* 144 */       this.fileOwner = fileOwner;
/* 145 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileGroup(String fileGroup) {
/* 149 */       this.fileGroup = fileGroup;
/* 150 */       return (B)asBuilder();
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
/*     */   private AbstractFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, M manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
/* 165 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 166 */     if (advertiser != null) {
/* 167 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 168 */       configuration.putAll(manager.getContentFormat());
/* 169 */       configuration.put("contentType", layout.getContentType());
/* 170 */       configuration.put("name", name);
/* 171 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 173 */       this.advertisement = null;
/*     */     } 
/* 175 */     this.fileName = filename;
/* 176 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 184 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 189 */     setStopping();
/* 190 */     stop(timeout, timeUnit, false);
/* 191 */     if (this.advertiser != null) {
/* 192 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 194 */     setStopped();
/* 195 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\AbstractFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */