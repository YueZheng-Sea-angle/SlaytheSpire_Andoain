/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.layout.internal.ExcludeChecker;
/*     */ import org.apache.logging.log4j.core.layout.internal.IncludeChecker;
/*     */ import org.apache.logging.log4j.core.layout.internal.ListChecker;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.net.Severity;
/*     */ import org.apache.logging.log4j.core.util.JsonUtils;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ import org.apache.logging.log4j.util.TriConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "GelfLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class GelfLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   private static final char C = ',';
/*     */   private static final int COMPRESSION_THRESHOLD = 1024;
/*     */   private static final char Q = '"';
/*     */   private static final String QC = "\",";
/*     */   private static final String QU = "\"_";
/*     */   private final KeyValuePair[] additionalFields;
/*     */   private final int compressionThreshold;
/*     */   private final CompressionType compressionType;
/*     */   private final String host;
/*     */   private final boolean includeStacktrace;
/*     */   private final boolean includeThreadContext;
/*     */   private final boolean includeMapMessage;
/*     */   private final boolean includeNullDelimiter;
/*     */   private final boolean includeNewLineDelimiter;
/*     */   private final boolean omitEmptyFields;
/*     */   private final PatternLayout layout;
/*     */   private final FieldWriter mdcWriter;
/*     */   private final FieldWriter mapWriter;
/*     */   
/*     */   public enum CompressionType
/*     */   {
/*  74 */     GZIP
/*     */     {
/*     */       public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
/*  77 */         return new GZIPOutputStream(os);
/*     */       }
/*     */     },
/*  80 */     ZLIB
/*     */     {
/*     */       public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
/*  83 */         return new DeflaterOutputStream(os);
/*     */       }
/*     */     },
/*  86 */     OFF
/*     */     {
/*     */       public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
/*  89 */         return null;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract DeflaterOutputStream createDeflaterOutputStream(OutputStream param1OutputStream) throws IOException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractStringLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<GelfLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String host;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginElement("AdditionalField")
/*     */     private KeyValuePair[] additionalFields;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/* 125 */     private GelfLayout.CompressionType compressionType = GelfLayout.CompressionType.GZIP;
/*     */     
/*     */     @PluginBuilderAttribute
/* 128 */     private int compressionThreshold = 1024;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeStacktrace = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeThreadContext = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeNullDelimiter = false;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeNewLineDelimiter = false;
/*     */     
/*     */     @PluginBuilderAttribute
/* 143 */     private String threadContextIncludes = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 146 */     private String threadContextExcludes = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 149 */     private String mapMessageIncludes = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 152 */     private String mapMessageExcludes = null;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeMapMessage = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean omitEmptyFields = false;
/*     */     
/*     */     @PluginBuilderAttribute
/* 161 */     private String messagePattern = null;
/*     */     
/*     */     @PluginBuilderAttribute
/* 164 */     private String threadContextPrefix = "";
/*     */     
/*     */     @PluginBuilderAttribute
/* 167 */     private String mapPrefix = "";
/*     */     
/*     */     @PluginElement("PatternSelector")
/* 170 */     private PatternSelector patternSelector = null;
/*     */ 
/*     */     
/*     */     public Builder() {
/* 174 */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */     
/*     */     public GelfLayout build() {
/* 179 */       ListChecker mdcChecker = createChecker(this.threadContextExcludes, this.threadContextIncludes);
/* 180 */       ListChecker mapChecker = createChecker(this.mapMessageExcludes, this.mapMessageIncludes);
/* 181 */       PatternLayout patternLayout = null;
/* 182 */       if (this.messagePattern != null && this.patternSelector != null) {
/* 183 */         AbstractLayout.LOGGER.error("A message pattern and PatternSelector cannot both be specified on GelfLayout, ignoring message pattern");
/*     */         
/* 185 */         this.messagePattern = null;
/*     */       } 
/* 187 */       if (this.messagePattern != null)
/*     */       {
/*     */ 
/*     */         
/* 191 */         patternLayout = PatternLayout.newBuilder().withPattern(this.messagePattern).withAlwaysWriteExceptions(this.includeStacktrace).withConfiguration(getConfiguration()).build();
/*     */       }
/* 193 */       if (this.patternSelector != null)
/*     */       {
/*     */ 
/*     */         
/* 197 */         patternLayout = PatternLayout.newBuilder().withPatternSelector(this.patternSelector).withAlwaysWriteExceptions(this.includeStacktrace).withConfiguration(getConfiguration()).build();
/*     */       }
/* 199 */       return new GelfLayout(getConfiguration(), this.host, this.additionalFields, this.compressionType, this.compressionThreshold, this.includeStacktrace, this.includeThreadContext, this.includeMapMessage, this.includeNullDelimiter, this.includeNewLineDelimiter, this.omitEmptyFields, mdcChecker, mapChecker, patternLayout, this.threadContextPrefix, this.mapPrefix);
/*     */     }
/*     */ 
/*     */     
/*     */     private ListChecker createChecker(String excludes, String includes) {
/*     */       IncludeChecker includeChecker;
/*     */       ListChecker.NoopChecker noopChecker;
/* 206 */       ListChecker checker = null;
/* 207 */       if (excludes != null) {
/* 208 */         String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
/* 209 */         if (array.length > 0) {
/* 210 */           List<String> excludeList = new ArrayList<>(array.length);
/* 211 */           for (String str : array) {
/* 212 */             excludeList.add(str.trim());
/*     */           }
/* 214 */           ExcludeChecker excludeChecker = new ExcludeChecker(excludeList);
/*     */         } 
/*     */       } 
/* 217 */       if (includes != null) {
/* 218 */         String[] array = includes.split(Patterns.COMMA_SEPARATOR);
/* 219 */         if (array.length > 0) {
/* 220 */           List<String> includeList = new ArrayList<>(array.length);
/* 221 */           for (String str : array) {
/* 222 */             includeList.add(str.trim());
/*     */           }
/* 224 */           includeChecker = new IncludeChecker(includeList);
/*     */         } 
/*     */       } 
/* 227 */       if (includeChecker == null) {
/* 228 */         noopChecker = ListChecker.NOOP_CHECKER;
/*     */       }
/* 230 */       return (ListChecker)noopChecker;
/*     */     }
/*     */     
/*     */     public String getHost() {
/* 234 */       return this.host;
/*     */     }
/*     */     
/*     */     public GelfLayout.CompressionType getCompressionType() {
/* 238 */       return this.compressionType;
/*     */     }
/*     */     
/*     */     public int getCompressionThreshold() {
/* 242 */       return this.compressionThreshold;
/*     */     }
/*     */     
/*     */     public boolean isIncludeStacktrace() {
/* 246 */       return this.includeStacktrace;
/*     */     }
/*     */     
/*     */     public boolean isIncludeThreadContext() {
/* 250 */       return this.includeThreadContext;
/*     */     }
/*     */     public boolean isIncludeNullDelimiter() {
/* 253 */       return this.includeNullDelimiter;
/*     */     }
/*     */     public boolean isIncludeNewLineDelimiter() {
/* 256 */       return this.includeNewLineDelimiter;
/*     */     }
/*     */     
/*     */     public KeyValuePair[] getAdditionalFields() {
/* 260 */       return this.additionalFields;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setHost(String host) {
/* 269 */       this.host = host;
/* 270 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setCompressionType(GelfLayout.CompressionType compressionType) {
/* 279 */       this.compressionType = compressionType;
/* 280 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setCompressionThreshold(int compressionThreshold) {
/* 289 */       this.compressionThreshold = compressionThreshold;
/* 290 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeStacktrace(boolean includeStacktrace) {
/* 300 */       this.includeStacktrace = includeStacktrace;
/* 301 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeThreadContext(boolean includeThreadContext) {
/* 310 */       this.includeThreadContext = includeThreadContext;
/* 311 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeNullDelimiter(boolean includeNullDelimiter) {
/* 321 */       this.includeNullDelimiter = includeNullDelimiter;
/* 322 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeNewLineDelimiter(boolean includeNewLineDelimiter) {
/* 331 */       this.includeNewLineDelimiter = includeNewLineDelimiter;
/* 332 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setAdditionalFields(KeyValuePair[] additionalFields) {
/* 341 */       this.additionalFields = additionalFields;
/* 342 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setMessagePattern(String pattern) {
/* 351 */       this.messagePattern = pattern;
/* 352 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setPatternSelector(PatternSelector patternSelector) {
/* 361 */       this.patternSelector = patternSelector;
/* 362 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setMdcIncludes(String mdcIncludes) {
/* 371 */       this.threadContextIncludes = mdcIncludes;
/* 372 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setMdcExcludes(String mdcExcludes) {
/* 381 */       this.threadContextExcludes = mdcExcludes;
/* 382 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeMapMessage(boolean includeMapMessage) {
/* 391 */       this.includeMapMessage = includeMapMessage;
/* 392 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setMapMessageIncludes(String mapMessageIncludes) {
/* 401 */       this.mapMessageIncludes = mapMessageIncludes;
/* 402 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setMapMessageExcludes(String mapMessageExcludes) {
/* 411 */       this.mapMessageExcludes = mapMessageExcludes;
/* 412 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setThreadContextPrefix(String prefix) {
/* 421 */       if (prefix != null) {
/* 422 */         this.threadContextPrefix = prefix;
/*     */       }
/* 424 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setMapPrefix(String prefix) {
/* 433 */       if (prefix != null) {
/* 434 */         this.mapPrefix = prefix;
/*     */       }
/* 436 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public GelfLayout(String host, KeyValuePair[] additionalFields, CompressionType compressionType, int compressionThreshold, boolean includeStacktrace) {
/* 446 */     this((Configuration)null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true, true, false, false, false, (ListChecker)null, (ListChecker)null, (PatternLayout)null, "", "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private GelfLayout(Configuration config, String host, KeyValuePair[] additionalFields, CompressionType compressionType, int compressionThreshold, boolean includeStacktrace, boolean includeThreadContext, boolean includeMapMessage, boolean includeNullDelimiter, boolean includeNewLineDelimiter, boolean omitEmptyFields, ListChecker mdcChecker, ListChecker mapChecker, PatternLayout patternLayout, String mdcPrefix, String mapPrefix) {
/* 456 */     super(config, StandardCharsets.UTF_8, (AbstractStringLayout.Serializer)null, (AbstractStringLayout.Serializer)null);
/* 457 */     this.host = (host != null) ? host : NetUtils.getLocalHostname();
/* 458 */     this.additionalFields = (additionalFields != null) ? additionalFields : KeyValuePair.EMPTY_ARRAY;
/* 459 */     if (config == null) {
/* 460 */       for (KeyValuePair additionalField : this.additionalFields) {
/* 461 */         if (valueNeedsLookup(additionalField.getValue())) {
/* 462 */           throw new IllegalArgumentException("configuration needs to be set when there are additional fields with variables");
/*     */         }
/*     */       } 
/*     */     }
/* 466 */     this.compressionType = compressionType;
/* 467 */     this.compressionThreshold = compressionThreshold;
/* 468 */     this.includeStacktrace = includeStacktrace;
/* 469 */     this.includeThreadContext = includeThreadContext;
/* 470 */     this.includeMapMessage = includeMapMessage;
/* 471 */     this.includeNullDelimiter = includeNullDelimiter;
/* 472 */     this.includeNewLineDelimiter = includeNewLineDelimiter;
/* 473 */     this.omitEmptyFields = omitEmptyFields;
/* 474 */     if (includeNullDelimiter && compressionType != CompressionType.OFF) {
/* 475 */       throw new IllegalArgumentException("null delimiter cannot be used with compression");
/*     */     }
/* 477 */     this.mdcWriter = new FieldWriter(mdcChecker, mdcPrefix);
/* 478 */     this.mapWriter = new FieldWriter(mapChecker, mapPrefix);
/* 479 */     this.layout = patternLayout;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 484 */     StringBuilder sb = new StringBuilder();
/* 485 */     sb.append("host=").append(this.host);
/* 486 */     sb.append(", compressionType=").append(this.compressionType.toString());
/* 487 */     sb.append(", compressionThreshold=").append(this.compressionThreshold);
/* 488 */     sb.append(", includeStackTrace=").append(this.includeStacktrace);
/* 489 */     sb.append(", includeThreadContext=").append(this.includeThreadContext);
/* 490 */     sb.append(", includeNullDelimiter=").append(this.includeNullDelimiter);
/* 491 */     sb.append(", includeNewLineDelimiter=").append(this.includeNewLineDelimiter);
/* 492 */     String threadVars = this.mdcWriter.getChecker().toString();
/* 493 */     if (threadVars.length() > 0) {
/* 494 */       sb.append(", ").append(threadVars);
/*     */     }
/* 496 */     String mapVars = this.mapWriter.getChecker().toString();
/* 497 */     if (mapVars.length() > 0) {
/* 498 */       sb.append(", ").append(mapVars);
/*     */     }
/* 500 */     if (this.layout != null) {
/* 501 */       sb.append(", PatternLayout{").append(this.layout.toString()).append("}");
/*     */     }
/* 503 */     return sb.toString();
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
/*     */   @Deprecated
/*     */   public static GelfLayout createLayout(@PluginAttribute("host") String host, @PluginElement("AdditionalField") KeyValuePair[] additionalFields, @PluginAttribute(value = "compressionType", defaultString = "GZIP") CompressionType compressionType, @PluginAttribute(value = "compressionThreshold", defaultInt = 1024) int compressionThreshold, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) boolean includeStacktrace) {
/* 521 */     return new GelfLayout(null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true, true, false, false, false, null, null, null, "", "");
/*     */   }
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 527 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 532 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 537 */     return "application/json; charset=" + getCharset();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/* 542 */     StringBuilder text = toText(event, getStringBuilder(), false);
/* 543 */     byte[] bytes = getBytes(text.toString());
/* 544 */     return (this.compressionType != CompressionType.OFF && bytes.length > this.compressionThreshold) ? compress(bytes) : bytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(LogEvent event, ByteBufferDestination destination) {
/* 549 */     if (this.compressionType != CompressionType.OFF) {
/* 550 */       super.encode(event, destination);
/*     */       return;
/*     */     } 
/* 553 */     StringBuilder text = toText(event, getStringBuilder(), true);
/* 554 */     Encoder<StringBuilder> helper = getStringBuilderEncoder();
/* 555 */     helper.encode(text, destination);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 560 */     return (Objects.nonNull(this.layout) && this.layout.requiresLocation());
/*     */   }
/*     */   
/*     */   private byte[] compress(byte[] bytes) {
/*     */     try {
/* 565 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(this.compressionThreshold / 8);
/* 566 */       try (DeflaterOutputStream stream = this.compressionType.createDeflaterOutputStream(baos)) {
/* 567 */         if (stream == null) {
/* 568 */           return bytes;
/*     */         }
/* 570 */         stream.write(bytes);
/* 571 */         stream.finish();
/*     */       } 
/* 573 */       return baos.toByteArray();
/* 574 */     } catch (IOException e) {
/* 575 */       StatusLogger.getLogger().error(e);
/* 576 */       return bytes;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 582 */     StringBuilder text = toText(event, getStringBuilder(), false);
/* 583 */     return text.toString();
/*     */   }
/*     */   
/*     */   private StringBuilder toText(LogEvent event, StringBuilder builder, boolean gcFree) {
/* 587 */     builder.append('{');
/* 588 */     builder.append("\"version\":\"1.1\",");
/* 589 */     builder.append("\"host\":\"");
/* 590 */     JsonUtils.quoteAsString(toNullSafeString(this.host), builder);
/* 591 */     builder.append("\",");
/* 592 */     builder.append("\"timestamp\":").append(formatTimestamp(event.getTimeMillis())).append(',');
/* 593 */     builder.append("\"level\":").append(formatLevel(event.getLevel())).append(',');
/* 594 */     if (event.getThreadName() != null) {
/* 595 */       builder.append("\"_thread\":\"");
/* 596 */       JsonUtils.quoteAsString(event.getThreadName(), builder);
/* 597 */       builder.append("\",");
/*     */     } 
/* 599 */     if (event.getLoggerName() != null) {
/* 600 */       builder.append("\"_logger\":\"");
/* 601 */       JsonUtils.quoteAsString(event.getLoggerName(), builder);
/* 602 */       builder.append("\",");
/*     */     } 
/* 604 */     if (this.additionalFields.length > 0) {
/* 605 */       StrSubstitutor strSubstitutor = getConfiguration().getStrSubstitutor();
/* 606 */       for (KeyValuePair additionalField : this.additionalFields) {
/*     */ 
/*     */         
/* 609 */         String value = valueNeedsLookup(additionalField.getValue()) ? strSubstitutor.replace(event, additionalField.getValue()) : additionalField.getValue();
/* 610 */         if (Strings.isNotEmpty(value) || !this.omitEmptyFields) {
/* 611 */           builder.append("\"_");
/* 612 */           JsonUtils.quoteAsString(additionalField.getKey(), builder);
/* 613 */           builder.append("\":\"");
/* 614 */           JsonUtils.quoteAsString(toNullSafeString(value), builder);
/* 615 */           builder.append("\",");
/*     */         } 
/*     */       } 
/*     */     } 
/* 619 */     if (this.includeThreadContext) {
/* 620 */       event.getContextData().forEach(this.mdcWriter, builder);
/*     */     }
/* 622 */     if (this.includeMapMessage && event.getMessage() instanceof MapMessage) {
/* 623 */       ((MapMessage)event.getMessage()).forEach((key, value) -> this.mapWriter.accept(key, value, builder));
/*     */     }
/*     */     
/* 626 */     if (event.getThrown() != null || this.layout != null) {
/* 627 */       builder.append("\"full_message\":\"");
/* 628 */       if (this.layout != null) {
/* 629 */         StringBuilder messageBuffer = getMessageStringBuilder();
/* 630 */         this.layout.serialize(event, messageBuffer);
/* 631 */         JsonUtils.quoteAsString(messageBuffer, builder);
/* 632 */       } else if (this.includeStacktrace) {
/* 633 */         JsonUtils.quoteAsString(formatThrowable(event.getThrown()), builder);
/*     */       } else {
/* 635 */         JsonUtils.quoteAsString(event.getThrown().toString(), builder);
/*     */       } 
/* 637 */       builder.append("\",");
/*     */     } 
/*     */     
/* 640 */     builder.append("\"short_message\":\"");
/* 641 */     Message message = event.getMessage();
/* 642 */     if (message instanceof CharSequence) {
/* 643 */       JsonUtils.quoteAsString((CharSequence)message, builder);
/* 644 */     } else if (gcFree && message instanceof StringBuilderFormattable) {
/* 645 */       StringBuilder messageBuffer = getMessageStringBuilder();
/*     */       try {
/* 647 */         ((StringBuilderFormattable)message).formatTo(messageBuffer);
/* 648 */         JsonUtils.quoteAsString(messageBuffer, builder);
/*     */       } finally {
/* 650 */         trimToMaxSize(messageBuffer);
/*     */       } 
/*     */     } else {
/* 653 */       JsonUtils.quoteAsString(toNullSafeString(message.getFormattedMessage()), builder);
/*     */     } 
/* 655 */     builder.append('"');
/* 656 */     builder.append('}');
/* 657 */     if (this.includeNullDelimiter) {
/* 658 */       builder.append(false);
/*     */     }
/* 660 */     if (this.includeNewLineDelimiter) {
/* 661 */       builder.append('\n');
/*     */     }
/* 663 */     return builder;
/*     */   }
/*     */   
/*     */   private static boolean valueNeedsLookup(String value) {
/* 667 */     return (value != null && value.contains("${"));
/*     */   }
/*     */   
/*     */   private class FieldWriter implements TriConsumer<String, Object, StringBuilder> {
/*     */     private final ListChecker checker;
/*     */     private final String prefix;
/*     */     
/*     */     FieldWriter(ListChecker checker, String prefix) {
/* 675 */       this.checker = checker;
/* 676 */       this.prefix = prefix;
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(String key, Object value, StringBuilder stringBuilder) {
/* 681 */       String stringValue = String.valueOf(value);
/* 682 */       if (this.checker.check(key) && (Strings.isNotEmpty(stringValue) || !GelfLayout.this.omitEmptyFields)) {
/* 683 */         stringBuilder.append("\"_");
/* 684 */         JsonUtils.quoteAsString(Strings.concat(this.prefix, key), stringBuilder);
/* 685 */         stringBuilder.append("\":\"");
/* 686 */         JsonUtils.quoteAsString(GelfLayout.toNullSafeString(stringValue), stringBuilder);
/* 687 */         stringBuilder.append("\",");
/*     */       } 
/*     */     }
/*     */     
/*     */     public ListChecker getChecker() {
/* 692 */       return this.checker;
/*     */     }
/*     */   }
/*     */   
/* 696 */   private static final ThreadLocal<StringBuilder> messageStringBuilder = new ThreadLocal<>();
/*     */   
/*     */   private static StringBuilder getMessageStringBuilder() {
/* 699 */     StringBuilder result = messageStringBuilder.get();
/* 700 */     if (result == null) {
/* 701 */       result = new StringBuilder(1024);
/* 702 */       messageStringBuilder.set(result);
/*     */     } 
/* 704 */     result.setLength(0);
/* 705 */     return result;
/*     */   }
/*     */   
/*     */   private static CharSequence toNullSafeString(CharSequence s) {
/* 709 */     return (s == null) ? "" : s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CharSequence formatTimestamp(long timeMillis) {
/* 716 */     if (timeMillis < 1000L) {
/* 717 */       return "0";
/*     */     }
/* 719 */     StringBuilder builder = getTimestampStringBuilder();
/* 720 */     builder.append(timeMillis);
/* 721 */     builder.insert(builder.length() - 3, '.');
/* 722 */     return builder;
/*     */   }
/*     */   
/* 725 */   private static final ThreadLocal<StringBuilder> timestampStringBuilder = new ThreadLocal<>();
/*     */   
/*     */   private static StringBuilder getTimestampStringBuilder() {
/* 728 */     StringBuilder result = timestampStringBuilder.get();
/* 729 */     if (result == null) {
/* 730 */       result = new StringBuilder(20);
/* 731 */       timestampStringBuilder.set(result);
/*     */     } 
/* 733 */     result.setLength(0);
/* 734 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int formatLevel(Level level) {
/* 741 */     return Severity.getSeverity(level).getCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CharSequence formatThrowable(Throwable throwable) {
/* 749 */     StringWriter sw = new StringWriter(2048);
/* 750 */     PrintWriter pw = new PrintWriter(sw);
/* 751 */     throwable.printStackTrace(pw);
/* 752 */     pw.flush();
/* 753 */     return sw.getBuffer();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\GelfLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */