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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "RandomAccessFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RandomAccessFileAppender
/*     */   extends AbstractOutputStreamAppender<RandomAccessFileManager>
/*     */ {
/*     */   private final String fileName;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RandomAccessFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("fileName")
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute("append")
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute("advertise")
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute("advertiseURI")
/*     */     private String advertiseURI;
/*     */     
/*     */     public Builder() {
/*  66 */       withBufferSize(262144);
/*     */     }
/*     */ 
/*     */     
/*     */     public RandomAccessFileAppender build() {
/*  71 */       String name = getName();
/*  72 */       if (name == null) {
/*  73 */         RandomAccessFileAppender.LOGGER.error("No name provided for RandomAccessFileAppender");
/*  74 */         return null;
/*     */       } 
/*     */       
/*  77 */       if (this.fileName == null) {
/*  78 */         RandomAccessFileAppender.LOGGER.error("No filename provided for RandomAccessFileAppender with name {}", name);
/*  79 */         return null;
/*     */       } 
/*  81 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*  82 */       boolean immediateFlush = isImmediateFlush();
/*  83 */       RandomAccessFileManager manager = RandomAccessFileManager.getFileManager(this.fileName, this.append, immediateFlush, 
/*  84 */           getBufferSize(), this.advertiseURI, layout, null);
/*  85 */       if (manager == null) {
/*  86 */         return null;
/*     */       }
/*     */       
/*  89 */       return new RandomAccessFileAppender(name, layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), immediateFlush, this.advertise ? 
/*  90 */           getConfiguration().getAdvertiser() : null, getPropertyArray());
/*     */     }
/*     */     
/*     */     public B setFileName(String fileName) {
/*  94 */       this.fileName = fileName;
/*  95 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAppend(boolean append) {
/*  99 */       this.append = append;
/* 100 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertise(boolean advertise) {
/* 104 */       this.advertise = advertise;
/* 105 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertiseURI(String advertiseURI) {
/* 109 */       this.advertiseURI = advertiseURI;
/* 110 */       return (B)asBuilder();
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
/*     */   private RandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RandomAccessFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
/* 124 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 125 */     if (advertiser != null) {
/*     */       
/* 127 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 128 */       configuration.putAll(manager.getContentFormat());
/* 129 */       configuration.put("contentType", layout.getContentType());
/* 130 */       configuration.put("name", name);
/* 131 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/* 133 */     this.fileName = filename;
/* 134 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 139 */     setStopping();
/* 140 */     stop(timeout, timeUnit, false);
/* 141 */     if (this.advertiser != null) {
/* 142 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 144 */     setStopped();
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 154 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 162 */     return getManager().getBufferSize();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> RandomAccessFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String bufferSizeStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration configuration) {
/* 204 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 205 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 206 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 207 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 208 */     int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
/*     */     
/* 210 */     return ((Builder)((Builder<Builder>)((Builder<Builder<Builder>>)((Builder<Builder<Builder<Builder>>>)((Builder<Builder<Builder<Builder<Builder>>>>)((Builder<B>)((Builder<Builder<B>>)newBuilder()
/* 211 */       .setAdvertise(isAdvertise)
/* 212 */       .setAdvertiseURI(advertiseURI)
/* 213 */       .setAppend(isAppend)
/* 214 */       .withBufferSize(bufferSize))
/* 215 */       .setConfiguration(configuration))
/* 216 */       .setFileName(fileName).setFilter(filter)).setIgnoreExceptions(ignoreExceptions))
/* 217 */       .withImmediateFlush(isFlush)).setLayout(layout)).setName(name))
/* 218 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 227 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */