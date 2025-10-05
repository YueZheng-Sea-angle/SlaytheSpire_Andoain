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
/*     */ @Plugin(name = "MemoryMappedFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class MemoryMappedFileAppender
/*     */   extends AbstractOutputStreamAppender<MemoryMappedFileManager>
/*     */ {
/*     */   private static final int BIT_POSITION_1GB = 30;
/*     */   private static final int MAX_REGION_LENGTH = 1073741824;
/*     */   private static final int MIN_REGION_LENGTH = 256;
/*     */   private final String fileName;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<MemoryMappedFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("fileName")
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute("append")
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute("regionLength")
/*  61 */     private int regionLength = 33554432;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute("advertise")
/*     */     private boolean advertise;
/*     */     
/*     */     @PluginBuilderAttribute("advertiseURI")
/*     */     private String advertiseURI;
/*     */ 
/*     */     
/*     */     public MemoryMappedFileAppender build() {
/*  72 */       String name = getName();
/*  73 */       int actualRegionLength = MemoryMappedFileAppender.determineValidRegionLength(name, this.regionLength);
/*     */       
/*  75 */       if (name == null) {
/*  76 */         MemoryMappedFileAppender.LOGGER.error("No name provided for MemoryMappedFileAppender");
/*  77 */         return null;
/*     */       } 
/*     */       
/*  80 */       if (this.fileName == null) {
/*  81 */         MemoryMappedFileAppender.LOGGER.error("No filename provided for MemoryMappedFileAppender with name " + name);
/*  82 */         return null;
/*     */       } 
/*  84 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*  85 */       MemoryMappedFileManager manager = MemoryMappedFileManager.getFileManager(this.fileName, this.append, isImmediateFlush(), actualRegionLength, this.advertiseURI, layout);
/*     */       
/*  87 */       if (manager == null) {
/*  88 */         return null;
/*     */       }
/*     */       
/*  91 */       return new MemoryMappedFileAppender(name, layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), false, this.advertise ? 
/*  92 */           getConfiguration().getAdvertiser() : null, getPropertyArray());
/*     */     }
/*     */     
/*     */     public B setFileName(String fileName) {
/*  96 */       this.fileName = fileName;
/*  97 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAppend(boolean append) {
/* 101 */       this.append = append;
/* 102 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setRegionLength(int regionLength) {
/* 106 */       this.regionLength = regionLength;
/* 107 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertise(boolean advertise) {
/* 111 */       this.advertise = advertise;
/* 112 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertiseURI(String advertiseURI) {
/* 116 */       this.advertiseURI = advertiseURI;
/* 117 */       return (B)asBuilder();
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
/*     */   private MemoryMappedFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, MemoryMappedFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
/* 134 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 135 */     if (advertiser != null) {
/* 136 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 137 */       configuration.putAll(manager.getContentFormat());
/* 138 */       configuration.put("contentType", layout.getContentType());
/* 139 */       configuration.put("name", name);
/* 140 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/* 142 */     this.fileName = filename;
/* 143 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 148 */     setStopping();
/* 149 */     stop(timeout, timeUnit, false);
/* 150 */     if (this.advertiser != null) {
/* 151 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 153 */     setStopped();
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 163 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegionLength() {
/* 172 */     return getManager().getRegionLength();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> MemoryMappedFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String regionLengthStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration config) {
/* 212 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 213 */     boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, false);
/* 214 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 215 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 216 */     int regionLength = Integers.parseInt(regionLengthStr, 33554432);
/*     */ 
/*     */     
/* 219 */     return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<Builder<Builder<Builder<Builder<B>>>>>)((Builder<B>)newBuilder()
/* 220 */       .setAdvertise(isAdvertise)
/* 221 */       .setAdvertiseURI(advertiseURI)
/* 222 */       .setAppend(isAppend)
/* 223 */       .setConfiguration(config))
/* 224 */       .setFileName(fileName).setFilter(filter)).setIgnoreExceptions(ignoreExceptions))
/* 225 */       .withImmediateFlush(isImmediateFlush)).setLayout(layout)).setName(name))
/* 226 */       .setRegionLength(regionLength)
/* 227 */       .build();
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 233 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int determineValidRegionLength(String name, int regionLength) {
/* 240 */     if (regionLength > 1073741824) {
/* 241 */       LOGGER.info("MemoryMappedAppender[{}] Reduced region length from {} to max length: {}", name, Integer.valueOf(regionLength), 
/* 242 */           Integer.valueOf(1073741824));
/* 243 */       return 1073741824;
/*     */     } 
/* 245 */     if (regionLength < 256) {
/* 246 */       LOGGER.info("MemoryMappedAppender[{}] Expanded region length from {} to min length: {}", name, Integer.valueOf(regionLength), 
/* 247 */           Integer.valueOf(256));
/* 248 */       return 256;
/*     */     } 
/* 250 */     int result = Integers.ceilingNextPowerOfTwo(regionLength);
/* 251 */     if (regionLength != result) {
/* 252 */       LOGGER.info("MemoryMappedAppender[{}] Rounded up region length from {} to next power of two: {}", name, 
/* 253 */           Integer.valueOf(regionLength), Integer.valueOf(result));
/*     */     }
/* 255 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\MemoryMappedFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */