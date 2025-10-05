/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ @Plugin(name = "File", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class FileAppender
/*     */   extends AbstractOutputStreamAppender<FileManager>
/*     */ {
/*     */   public static final String PLUGIN_NAME = "File";
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private final String fileName;
/*     */   private final Advertiser advertiser;
/*     */   private final Object advertisement;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<FileAppender>
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
/*     */     public FileAppender build() {
/*  85 */       boolean bufferedIo = isBufferedIo();
/*  86 */       int bufferSize = getBufferSize();
/*  87 */       if (this.locking && bufferedIo) {
/*  88 */         FileAppender.LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for {}", this.fileName);
/*  89 */         bufferedIo = false;
/*     */       } 
/*  91 */       if (!bufferedIo && bufferSize > 0) {
/*  92 */         FileAppender.LOGGER.warn("The bufferSize is set to {} but bufferedIo is false: {}", Integer.valueOf(bufferSize), Boolean.valueOf(bufferedIo));
/*     */       }
/*  94 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*     */       
/*  96 */       FileManager manager = FileManager.getFileManager(this.fileName, this.append, this.locking, bufferedIo, this.createOnDemand, this.advertiseUri, layout, bufferSize, this.filePermissions, this.fileOwner, this.fileGroup, 
/*  97 */           getConfiguration());
/*  98 */       if (manager == null) {
/*  99 */         return null;
/*     */       }
/*     */       
/* 102 */       return new FileAppender(getName(), layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), (!bufferedIo || 
/* 103 */           isImmediateFlush()), this.advertise ? getConfiguration().getAdvertiser() : null, 
/* 104 */           getPropertyArray());
/*     */     }
/*     */     
/*     */     public String getAdvertiseUri() {
/* 108 */       return this.advertiseUri;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/* 112 */       return this.fileName;
/*     */     }
/*     */     
/*     */     public boolean isAdvertise() {
/* 116 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public boolean isAppend() {
/* 120 */       return this.append;
/*     */     }
/*     */     
/*     */     public boolean isCreateOnDemand() {
/* 124 */       return this.createOnDemand;
/*     */     }
/*     */     
/*     */     public boolean isLocking() {
/* 128 */       return this.locking;
/*     */     }
/*     */     
/*     */     public String getFilePermissions() {
/* 132 */       return this.filePermissions;
/*     */     }
/*     */     
/*     */     public String getFileOwner() {
/* 136 */       return this.fileOwner;
/*     */     }
/*     */     
/*     */     public String getFileGroup() {
/* 140 */       return this.fileGroup;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 144 */       this.advertise = advertise;
/* 145 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseUri(String advertiseUri) {
/* 149 */       this.advertiseUri = advertiseUri;
/* 150 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 154 */       this.append = append;
/* 155 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileName(String fileName) {
/* 159 */       this.fileName = fileName;
/* 160 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withCreateOnDemand(boolean createOnDemand) {
/* 164 */       this.createOnDemand = createOnDemand;
/* 165 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withLocking(boolean locking) {
/* 169 */       this.locking = locking;
/* 170 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFilePermissions(String filePermissions) {
/* 174 */       this.filePermissions = filePermissions;
/* 175 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileOwner(String fileOwner) {
/* 179 */       this.fileOwner = fileOwner;
/* 180 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileGroup(String fileGroup) {
/* 184 */       this.fileGroup = fileGroup;
/* 185 */       return (B)asBuilder();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> FileAppender createAppender(String fileName, String append, String locking, String name, String immediateFlush, String ignoreExceptions, String bufferedIo, String bufferSizeStr, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseUri, Configuration config) {
/* 230 */     return ((Builder)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)newBuilder()
/* 231 */       .withAdvertise(Boolean.parseBoolean(advertise))
/* 232 */       .withAdvertiseUri(advertiseUri)
/* 233 */       .withAppend(Booleans.parseBoolean(append, true))
/* 234 */       .withBufferedIo(Booleans.parseBoolean(bufferedIo, true)))
/* 235 */       .withBufferSize(Integers.parseInt(bufferSizeStr, 8192)))
/* 236 */       .setConfiguration(config))
/* 237 */       .withFileName(fileName).setFilter(filter)).setIgnoreExceptions(Booleans.parseBoolean(ignoreExceptions, true)))
/* 238 */       .withImmediateFlush(Booleans.parseBoolean(immediateFlush, true))).setLayout(layout))
/* 239 */       .withLocking(Boolean.parseBoolean(locking)).setName(name))
/* 240 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 246 */     return (B)(new Builder<>()).asBuilder();
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
/*     */   private FileAppender(String name, Layout<? extends Serializable> layout, Filter filter, FileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
/* 259 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 260 */     if (advertiser != null) {
/* 261 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 262 */       configuration.putAll(manager.getContentFormat());
/* 263 */       configuration.put("contentType", layout.getContentType());
/* 264 */       configuration.put("name", name);
/* 265 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 267 */       this.advertisement = null;
/*     */     } 
/* 269 */     this.fileName = filename;
/* 270 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 278 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 283 */     setStopping();
/* 284 */     stop(timeout, timeUnit, false);
/* 285 */     if (this.advertiser != null) {
/* 286 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 288 */     setStopped();
/* 289 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\FileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */