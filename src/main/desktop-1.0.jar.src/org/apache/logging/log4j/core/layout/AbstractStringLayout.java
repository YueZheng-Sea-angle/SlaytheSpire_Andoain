/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
/*     */ import org.apache.logging.log4j.core.impl.LocationAware;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.StringEncoder;
/*     */ import org.apache.logging.log4j.spi.AbstractLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractStringLayout
/*     */   extends AbstractLayout<String>
/*     */   implements StringLayout, LocationAware
/*     */ {
/*     */   protected static final int DEFAULT_STRING_BUILDER_SIZE = 1024;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractLayout.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute("charset")
/*     */     private Charset charset;
/*     */     @PluginElement("footerSerializer")
/*     */     private AbstractStringLayout.Serializer footerSerializer;
/*     */     @PluginElement("headerSerializer")
/*     */     private AbstractStringLayout.Serializer headerSerializer;
/*     */     
/*     */     public Charset getCharset() {
/*  58 */       return this.charset;
/*     */     }
/*     */     
/*     */     public AbstractStringLayout.Serializer getFooterSerializer() {
/*  62 */       return this.footerSerializer;
/*     */     }
/*     */     
/*     */     public AbstractStringLayout.Serializer getHeaderSerializer() {
/*  66 */       return this.headerSerializer;
/*     */     }
/*     */     
/*     */     public B setCharset(Charset charset) {
/*  70 */       this.charset = charset;
/*  71 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setFooterSerializer(AbstractStringLayout.Serializer footerSerializer) {
/*  75 */       this.footerSerializer = footerSerializer;
/*  76 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setHeaderSerializer(AbstractStringLayout.Serializer headerSerializer) {
/*  80 */       this.headerSerializer = headerSerializer;
/*  81 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public static interface Serializer
/*     */     extends Serializer2 {
/*     */     String toSerializable(LogEvent param1LogEvent);
/*     */     
/*     */     default StringBuilder toSerializable(LogEvent event, StringBuilder builder) {
/*  96 */       builder.append(toSerializable(event));
/*  97 */       return builder;
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
/* 115 */   protected static final int MAX_STRING_BUILDER_SIZE = Math.max(1024, 
/* 116 */       size("log4j.layoutStringBuilder.maxSize", 2048));
/*     */   
/* 118 */   private static final ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();
/*     */   
/*     */   private Encoder<StringBuilder> textEncoder;
/*     */   private final Charset charset;
/*     */   private final Serializer footerSerializer;
/*     */   private final Serializer headerSerializer;
/*     */   
/*     */   protected static StringBuilder getStringBuilder() {
/* 126 */     if (AbstractLogger.getRecursionDepth() > 1)
/*     */     {
/* 128 */       return new StringBuilder(1024);
/*     */     }
/* 130 */     StringBuilder result = threadLocal.get();
/* 131 */     if (result == null) {
/* 132 */       result = new StringBuilder(1024);
/* 133 */       threadLocal.set(result);
/*     */     } 
/* 135 */     trimToMaxSize(result);
/* 136 */     result.setLength(0);
/* 137 */     return result;
/*     */   }
/*     */   
/*     */   private static int size(String property, int defaultValue) {
/* 141 */     return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
/*     */   }
/*     */   
/*     */   protected static void trimToMaxSize(StringBuilder stringBuilder) {
/* 145 */     StringBuilders.trimToMaxSize(stringBuilder, MAX_STRING_BUILDER_SIZE);
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
/*     */   protected AbstractStringLayout(Charset charset) {
/* 159 */     this(charset, (byte[])null, (byte[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractStringLayout(Charset aCharset, byte[] header, byte[] footer) {
/* 170 */     super((Configuration)null, header, footer);
/* 171 */     this.headerSerializer = null;
/* 172 */     this.footerSerializer = null;
/* 173 */     this.charset = (aCharset == null) ? StandardCharsets.UTF_8 : aCharset;
/* 174 */     this.textEncoder = Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null;
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
/*     */   protected AbstractStringLayout(Configuration config, Charset aCharset, Serializer headerSerializer, Serializer footerSerializer) {
/* 187 */     super(config, (byte[])null, (byte[])null);
/* 188 */     this.headerSerializer = headerSerializer;
/* 189 */     this.footerSerializer = footerSerializer;
/* 190 */     this.charset = (aCharset == null) ? StandardCharsets.UTF_8 : aCharset;
/* 191 */     this.textEncoder = Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null;
/*     */   }
/*     */   
/*     */   protected byte[] getBytes(String s) {
/* 195 */     return s.getBytes(this.charset);
/*     */   }
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 200 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 208 */     return "text/plain";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 218 */     return serializeToBytes(this.footerSerializer, super.getFooter());
/*     */   }
/*     */   
/*     */   public Serializer getFooterSerializer() {
/* 222 */     return this.footerSerializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 232 */     return serializeToBytes(this.headerSerializer, super.getHeader());
/*     */   }
/*     */   
/*     */   public Serializer getHeaderSerializer() {
/* 236 */     return this.headerSerializer;
/*     */   }
/*     */   
/*     */   private DefaultLogEventFactory getLogEventFactory() {
/* 240 */     return DefaultLogEventFactory.getInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Encoder<StringBuilder> getStringBuilderEncoder() {
/* 249 */     if (this.textEncoder == null) {
/* 250 */       this.textEncoder = new StringBuilderEncoder(getCharset());
/*     */     }
/* 252 */     return this.textEncoder;
/*     */   }
/*     */   
/*     */   protected byte[] serializeToBytes(Serializer serializer, byte[] defaultValue) {
/* 256 */     String serializable = serializeToString(serializer);
/* 257 */     if (serializable == null) {
/* 258 */       return defaultValue;
/*     */     }
/* 260 */     return StringEncoder.toBytes(serializable, getCharset());
/*     */   }
/*     */   
/*     */   protected String serializeToString(Serializer serializer) {
/* 264 */     if (serializer == null) {
/* 265 */       return null;
/*     */     }
/* 267 */     LoggerConfig rootLogger = getConfiguration().getRootLogger();
/*     */     
/* 269 */     LogEvent logEvent = getLogEventFactory().createEvent(rootLogger.getName(), null, "", rootLogger
/* 270 */         .getLevel(), null, null, null);
/* 271 */     return serializer.toSerializable(logEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/* 282 */     return getBytes((String)toSerializable(event));
/*     */   }
/*     */   
/*     */   public static interface Serializer2 {
/*     */     StringBuilder toSerializable(LogEvent param1LogEvent, StringBuilder param1StringBuilder);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\AbstractStringLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */