/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "RollingFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RollingFileAppender
/*     */   extends AbstractOutputStreamAppender<RollingFileManager>
/*     */ {
/*     */   public static final String PLUGIN_NAME = "RollingFile";
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private final String fileName;
/*     */   private final String filePattern;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RollingFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     private String filePattern;
/*     */     @PluginBuilderAttribute
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute
/*     */     private boolean locking;
/*     */     @PluginElement("Policy")
/*     */     @Required
/*     */     private TriggeringPolicy policy;
/*     */     @PluginElement("Strategy")
/*     */     private RolloverStrategy strategy;
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
/*     */     public RollingFileAppender build() {
/* 107 */       boolean isBufferedIo = isBufferedIo();
/* 108 */       int bufferSize = getBufferSize();
/* 109 */       if (getName() == null) {
/* 110 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No name provided.", getName());
/* 111 */         return null;
/*     */       } 
/*     */       
/* 114 */       if (!isBufferedIo && bufferSize > 0) {
/* 115 */         RollingFileAppender.LOGGER.warn("RollingFileAppender '{}': The bufferSize is set to {} but bufferedIO is not true", getName(), Integer.valueOf(bufferSize));
/*     */       }
/*     */       
/* 118 */       if (this.filePattern == null) {
/* 119 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No file name pattern provided.", getName());
/* 120 */         return null;
/*     */       } 
/*     */       
/* 123 */       if (this.policy == null) {
/* 124 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No TriggeringPolicy provided.", getName());
/* 125 */         return null;
/*     */       } 
/*     */       
/* 128 */       if (this.strategy == null) {
/* 129 */         if (this.fileName != null) {
/* 130 */           this
/*     */ 
/*     */             
/* 133 */             .strategy = (RolloverStrategy)DefaultRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(getConfiguration()).build();
/*     */         } else {
/* 135 */           this
/*     */ 
/*     */             
/* 138 */             .strategy = (RolloverStrategy)DirectWriteRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(getConfiguration()).build();
/*     */         } 
/* 140 */       } else if (this.fileName == null && !(this.strategy instanceof DirectFileRolloverStrategy)) {
/* 141 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a {} must be configured", getName(), DirectFileRolloverStrategy.class.getSimpleName());
/* 142 */         return null;
/*     */       } 
/*     */       
/* 145 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/* 146 */       RollingFileManager manager = RollingFileManager.getFileManager(this.fileName, this.filePattern, this.append, isBufferedIo, this.policy, this.strategy, this.advertiseUri, layout, bufferSize, 
/* 147 */           isImmediateFlush(), this.createOnDemand, this.filePermissions, this.fileOwner, this.fileGroup, 
/* 148 */           getConfiguration());
/* 149 */       if (manager == null) {
/* 150 */         return null;
/*     */       }
/*     */       
/* 153 */       manager.initialize();
/*     */       
/* 155 */       return new RollingFileAppender(getName(), layout, getFilter(), manager, this.fileName, this.filePattern, 
/* 156 */           isIgnoreExceptions(), (!isBufferedIo || isImmediateFlush()), this.advertise ? 
/* 157 */           getConfiguration().getAdvertiser() : null, getPropertyArray());
/*     */     }
/*     */     
/*     */     public String getAdvertiseUri() {
/* 161 */       return this.advertiseUri;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/* 165 */       return this.fileName;
/*     */     }
/*     */     
/*     */     public boolean isAdvertise() {
/* 169 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public boolean isAppend() {
/* 173 */       return this.append;
/*     */     }
/*     */     
/*     */     public boolean isCreateOnDemand() {
/* 177 */       return this.createOnDemand;
/*     */     }
/*     */     
/*     */     public boolean isLocking() {
/* 181 */       return this.locking;
/*     */     }
/*     */     
/*     */     public String getFilePermissions() {
/* 185 */       return this.filePermissions;
/*     */     }
/*     */     
/*     */     public String getFileOwner() {
/* 189 */       return this.fileOwner;
/*     */     }
/*     */     
/*     */     public String getFileGroup() {
/* 193 */       return this.fileGroup;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 197 */       this.advertise = advertise;
/* 198 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseUri(String advertiseUri) {
/* 202 */       this.advertiseUri = advertiseUri;
/* 203 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 207 */       this.append = append;
/* 208 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileName(String fileName) {
/* 212 */       this.fileName = fileName;
/* 213 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withCreateOnDemand(boolean createOnDemand) {
/* 217 */       this.createOnDemand = createOnDemand;
/* 218 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withLocking(boolean locking) {
/* 222 */       this.locking = locking;
/* 223 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public String getFilePattern() {
/* 227 */       return this.filePattern;
/*     */     }
/*     */     
/*     */     public TriggeringPolicy getPolicy() {
/* 231 */       return this.policy;
/*     */     }
/*     */     
/*     */     public RolloverStrategy getStrategy() {
/* 235 */       return this.strategy;
/*     */     }
/*     */     
/*     */     public B withFilePattern(String filePattern) {
/* 239 */       this.filePattern = filePattern;
/* 240 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withPolicy(TriggeringPolicy policy) {
/* 244 */       this.policy = policy;
/* 245 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withStrategy(RolloverStrategy strategy) {
/* 249 */       this.strategy = strategy;
/* 250 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFilePermissions(String filePermissions) {
/* 254 */       this.filePermissions = filePermissions;
/* 255 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileOwner(String fileOwner) {
/* 259 */       this.fileOwner = fileOwner;
/* 260 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileGroup(String fileGroup) {
/* 264 */       this.fileGroup = fileGroup;
/* 265 */       return (B)asBuilder();
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
/*     */   private RollingFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
/* 281 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 282 */     if (advertiser != null) {
/* 283 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 284 */       configuration.put("contentType", layout.getContentType());
/* 285 */       configuration.put("name", name);
/* 286 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/* 288 */     this.fileName = fileName;
/* 289 */     this.filePattern = filePattern;
/* 290 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 295 */     setStopping();
/* 296 */     boolean stopped = stop(timeout, timeUnit, false);
/* 297 */     if (this.advertiser != null) {
/* 298 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 300 */     setStopped();
/* 301 */     return stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 311 */     getManager().checkRollover(event);
/* 312 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 320 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilePattern() {
/* 328 */     return this.filePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends TriggeringPolicy> T getTriggeringPolicy() {
/* 337 */     return (T)getManager().getTriggeringPolicy();
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
/*     */   public static <B extends Builder<B>> RollingFileAppender createAppender(String fileName, String filePattern, String append, String name, String bufferedIO, String bufferSizeStr, String immediateFlush, TriggeringPolicy policy, RolloverStrategy strategy, Layout<? extends Serializable> layout, Filter filter, String ignore, String advertise, String advertiseUri, Configuration config) {
/* 381 */     int bufferSize = Integers.parseInt(bufferSizeStr, 8192);
/*     */     
/* 383 */     return ((Builder<B>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)newBuilder()
/* 384 */       .withAdvertise(Boolean.parseBoolean(advertise))
/* 385 */       .withAdvertiseUri(advertiseUri)
/* 386 */       .withAppend(Booleans.parseBoolean(append, true))
/* 387 */       .withBufferedIo(Booleans.parseBoolean(bufferedIO, true)))
/* 388 */       .withBufferSize(bufferSize))
/* 389 */       .setConfiguration(config))
/* 390 */       .withFileName(fileName)
/* 391 */       .withFilePattern(filePattern).setFilter(filter)).setIgnoreExceptions(Booleans.parseBoolean(ignore, true)))
/* 392 */       .withImmediateFlush(Booleans.parseBoolean(immediateFlush, true))).setLayout(layout))
/* 393 */       .withCreateOnDemand(false)
/* 394 */       .withLocking(false).setName(name))
/* 395 */       .withPolicy(policy)
/* 396 */       .withStrategy(strategy)
/* 397 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 409 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\RollingFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */