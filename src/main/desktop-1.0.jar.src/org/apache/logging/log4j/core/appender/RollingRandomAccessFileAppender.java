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
/*     */ import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
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
/*     */ @Plugin(name = "RollingRandomAccessFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RollingRandomAccessFileAppender
/*     */   extends AbstractOutputStreamAppender<RollingRandomAccessFileManager>
/*     */ {
/*     */   private final String fileName;
/*     */   private final String filePattern;
/*     */   private final Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RollingRandomAccessFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("fileName")
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute("filePattern")
/*     */     private String filePattern;
/*     */     @PluginBuilderAttribute("append")
/*     */     private boolean append;
/*     */     @PluginElement("Policy")
/*     */     private TriggeringPolicy policy;
/*     */     @PluginElement("Strategy")
/*     */     private RolloverStrategy strategy;
/*     */     @PluginBuilderAttribute("advertise")
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute("advertiseURI")
/*     */     private String advertiseURI;
/*     */     @PluginBuilderAttribute
/*     */     private String filePermissions;
/*     */     @PluginBuilderAttribute
/*     */     private String fileOwner;
/*     */     @PluginBuilderAttribute
/*     */     private String fileGroup;
/*     */     
/*     */     public Builder() {
/*  68 */       this.append = true;
/*     */       withBufferSize(262144);
/*     */       setIgnoreExceptions(true);
/*     */       withImmediateFlush(true);
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
/*     */     public RollingRandomAccessFileAppender build() {
/*  94 */       String name = getName();
/*  95 */       if (name == null) {
/*  96 */         RollingRandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
/*  97 */         return null;
/*     */       } 
/*     */       
/* 100 */       if (this.strategy == null) {
/* 101 */         if (this.fileName != null) {
/* 102 */           this
/*     */ 
/*     */             
/* 105 */             .strategy = (RolloverStrategy)DefaultRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(getConfiguration()).build();
/*     */         } else {
/* 107 */           this
/*     */ 
/*     */             
/* 110 */             .strategy = (RolloverStrategy)DirectWriteRolloverStrategy.newBuilder().withCompressionLevelStr(String.valueOf(-1)).withConfig(getConfiguration()).build();
/*     */         } 
/* 112 */       } else if (this.fileName == null && !(this.strategy instanceof org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy)) {
/* 113 */         RollingRandomAccessFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a DirectFileRolloverStrategy must be configured");
/* 114 */         return null;
/*     */       } 
/*     */       
/* 117 */       if (this.filePattern == null) {
/* 118 */         RollingRandomAccessFileAppender.LOGGER.error("No filename pattern provided for FileAppender with name " + name);
/* 119 */         return null;
/*     */       } 
/*     */       
/* 122 */       if (this.policy == null) {
/* 123 */         RollingRandomAccessFileAppender.LOGGER.error("A TriggeringPolicy must be provided");
/* 124 */         return null;
/*     */       } 
/*     */       
/* 127 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*     */       
/* 129 */       boolean immediateFlush = isImmediateFlush();
/* 130 */       int bufferSize = getBufferSize();
/*     */       
/* 132 */       RollingRandomAccessFileManager manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(this.fileName, this.filePattern, this.append, immediateFlush, bufferSize, this.policy, this.strategy, this.advertiseURI, layout, this.filePermissions, this.fileOwner, this.fileGroup, 
/*     */           
/* 134 */           getConfiguration());
/* 135 */       if (manager == null) {
/* 136 */         return null;
/*     */       }
/*     */       
/* 139 */       manager.initialize();
/*     */       
/* 141 */       return new RollingRandomAccessFileAppender(name, layout, getFilter(), manager, this.fileName, this.filePattern, 
/* 142 */           isIgnoreExceptions(), immediateFlush, bufferSize, this.advertise ? 
/* 143 */           getConfiguration().getAdvertiser() : null, getPropertyArray());
/*     */     }
/*     */     
/*     */     public B withFileName(String fileName) {
/* 147 */       this.fileName = fileName;
/* 148 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFilePattern(String filePattern) {
/* 152 */       this.filePattern = filePattern;
/* 153 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 157 */       this.append = append;
/* 158 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withPolicy(TriggeringPolicy policy) {
/* 162 */       this.policy = policy;
/* 163 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withStrategy(RolloverStrategy strategy) {
/* 167 */       this.strategy = strategy;
/* 168 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 172 */       this.advertise = advertise;
/* 173 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseURI(String advertiseURI) {
/* 177 */       this.advertiseURI = advertiseURI;
/* 178 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFilePermissions(String filePermissions) {
/* 182 */       this.filePermissions = filePermissions;
/* 183 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileOwner(String fileOwner) {
/* 187 */       this.fileOwner = fileOwner;
/* 188 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileGroup(String fileGroup) {
/* 192 */       this.fileGroup = fileGroup;
/* 193 */       return (B)asBuilder();
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
/*     */   private RollingRandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingRandomAccessFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, int bufferSize, Advertiser advertiser, Property[] properties) {
/* 207 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 208 */     if (advertiser != null) {
/* 209 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 210 */       configuration.put("contentType", layout.getContentType());
/* 211 */       configuration.put("name", name);
/* 212 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 214 */       this.advertisement = null;
/*     */     } 
/* 216 */     this.fileName = fileName;
/* 217 */     this.filePattern = filePattern;
/* 218 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 223 */     setStopping();
/* 224 */     stop(timeout, timeUnit, false);
/* 225 */     if (this.advertiser != null) {
/* 226 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 228 */     setStopped();
/* 229 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 239 */     RollingRandomAccessFileManager manager = getManager();
/* 240 */     manager.checkRollover(event);
/*     */ 
/*     */     
/* 243 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 252 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilePattern() {
/* 261 */     return this.filePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 269 */     return getManager().getBufferSize();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> RollingRandomAccessFileAppender createAppender(String fileName, String filePattern, String append, String name, String immediateFlush, String bufferSizeStr, TriggeringPolicy policy, RolloverStrategy strategy, Layout<? extends Serializable> layout, Filter filter, String ignoreExceptions, String advertise, String advertiseURI, Configuration configuration) {
/* 317 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 318 */     boolean isIgnoreExceptions = Booleans.parseBoolean(ignoreExceptions, true);
/* 319 */     boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, true);
/* 320 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 321 */     int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
/*     */     
/* 323 */     return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<Builder<Builder<Builder<Builder<B>>>>>)((Builder<B>)((Builder<Builder<B>>)newBuilder()
/* 324 */       .withAdvertise(isAdvertise)
/* 325 */       .withAdvertiseURI(advertiseURI)
/* 326 */       .withAppend(isAppend)
/* 327 */       .withBufferSize(bufferSize))
/* 328 */       .setConfiguration(configuration))
/* 329 */       .withFileName(fileName)
/* 330 */       .withFilePattern(filePattern).setFilter(filter)).setIgnoreExceptions(isIgnoreExceptions))
/* 331 */       .withImmediateFlush(isImmediateFlush)).setLayout(layout)).setName(name))
/* 332 */       .withPolicy(policy)
/* 333 */       .withStrategy(strategy)
/* 334 */       .build();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 339 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\RollingRandomAccessFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */