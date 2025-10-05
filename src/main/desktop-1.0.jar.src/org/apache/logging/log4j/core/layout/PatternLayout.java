/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.impl.LocationAware;
/*     */ import org.apache.logging.log4j.core.pattern.FormattingInfo;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.pattern.RegexReplacement;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PatternLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class PatternLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
/*     */   public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %notEmpty{%x }- %m%n";
/*     */   public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
/*     */   public static final String KEY = "Converter";
/*     */   private final String conversionPattern;
/*     */   private final PatternSelector patternSelector;
/*     */   private final AbstractStringLayout.Serializer eventSerializer;
/*     */   
/*     */   private PatternLayout(Configuration config, RegexReplacement replace, String eventPattern, PatternSelector patternSelector, Charset charset, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi, String headerPattern, String footerPattern) {
/* 110 */     super(config, charset, 
/* 111 */         newSerializerBuilder()
/* 112 */         .setConfiguration(config)
/* 113 */         .setReplace(replace)
/* 114 */         .setPatternSelector(patternSelector)
/* 115 */         .setAlwaysWriteExceptions(alwaysWriteExceptions)
/* 116 */         .setDisableAnsi(disableAnsi)
/* 117 */         .setNoConsoleNoAnsi(noConsoleNoAnsi)
/* 118 */         .setPattern(headerPattern)
/* 119 */         .build(), 
/* 120 */         newSerializerBuilder()
/* 121 */         .setConfiguration(config)
/* 122 */         .setReplace(replace)
/* 123 */         .setPatternSelector(patternSelector)
/* 124 */         .setAlwaysWriteExceptions(alwaysWriteExceptions)
/* 125 */         .setDisableAnsi(disableAnsi)
/* 126 */         .setNoConsoleNoAnsi(noConsoleNoAnsi)
/* 127 */         .setPattern(footerPattern)
/* 128 */         .build());
/* 129 */     this.conversionPattern = eventPattern;
/* 130 */     this.patternSelector = patternSelector;
/* 131 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 140 */       .eventSerializer = newSerializerBuilder().setConfiguration(config).setReplace(replace).setPatternSelector(patternSelector).setAlwaysWriteExceptions(alwaysWriteExceptions).setDisableAnsi(disableAnsi).setNoConsoleNoAnsi(noConsoleNoAnsi).setPattern(eventPattern).setDefaultPattern("%m%n").build();
/*     */   }
/*     */   
/*     */   public static SerializerBuilder newSerializerBuilder() {
/* 144 */     return new SerializerBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresLocation() {
/* 149 */     return (this.eventSerializer instanceof LocationAware && ((LocationAware)this.eventSerializer).requiresLocation());
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
/*     */   @Deprecated
/*     */   public static AbstractStringLayout.Serializer createSerializer(Configuration configuration, RegexReplacement replace, String pattern, String defaultPattern, PatternSelector patternSelector, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
/* 170 */     SerializerBuilder builder = newSerializerBuilder();
/* 171 */     builder.setAlwaysWriteExceptions(alwaysWriteExceptions);
/* 172 */     builder.setConfiguration(configuration);
/* 173 */     builder.setDefaultPattern(defaultPattern);
/* 174 */     builder.setNoConsoleNoAnsi(noConsoleNoAnsi);
/* 175 */     builder.setPattern(pattern);
/* 176 */     builder.setPatternSelector(patternSelector);
/* 177 */     builder.setReplace(replace);
/* 178 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConversionPattern() {
/* 187 */     return this.conversionPattern;
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
/*     */   public Map<String, String> getContentFormat() {
/* 202 */     Map<String, String> result = new HashMap<>();
/* 203 */     result.put("structured", "false");
/* 204 */     result.put("formatType", "conversion");
/* 205 */     result.put("format", this.conversionPattern);
/* 206 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 217 */     return this.eventSerializer.toSerializable(event);
/*     */   }
/*     */   
/*     */   public void serialize(LogEvent event, StringBuilder stringBuilder) {
/* 221 */     this.eventSerializer.toSerializable(event, stringBuilder);
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(LogEvent event, ByteBufferDestination destination) {
/* 226 */     StringBuilder text = toText(this.eventSerializer, event, getStringBuilder());
/* 227 */     Encoder<StringBuilder> encoder = getStringBuilderEncoder();
/* 228 */     encoder.encode(text, destination);
/* 229 */     trimToMaxSize(text);
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
/*     */   private StringBuilder toText(AbstractStringLayout.Serializer2 serializer, LogEvent event, StringBuilder destination) {
/* 241 */     return serializer.toSerializable(event, destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PatternParser createPatternParser(Configuration config) {
/* 250 */     if (config == null) {
/* 251 */       return new PatternParser(config, "Converter", LogEventPatternConverter.class);
/*     */     }
/* 253 */     PatternParser parser = (PatternParser)config.getComponent("Converter");
/* 254 */     if (parser == null) {
/* 255 */       parser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
/* 256 */       config.addComponent("Converter", parser);
/* 257 */       parser = (PatternParser)config.getComponent("Converter");
/*     */     } 
/* 259 */     return parser;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 264 */     return (this.patternSelector == null) ? this.conversionPattern : this.patternSelector.toString();
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
/*     */   @PluginFactory
/*     */   @Deprecated
/*     */   public static PatternLayout createLayout(@PluginAttribute(value = "pattern", defaultString = "%m%n") String pattern, @PluginElement("PatternSelector") PatternSelector patternSelector, @PluginConfiguration Configuration config, @PluginElement("Replace") RegexReplacement replace, @PluginAttribute("charset") Charset charset, @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) boolean alwaysWriteExceptions, @PluginAttribute("noConsoleNoAnsi") boolean noConsoleNoAnsi, @PluginAttribute("header") String headerPattern, @PluginAttribute("footer") String footerPattern) {
/* 304 */     return newBuilder()
/* 305 */       .withPattern(pattern)
/* 306 */       .withPatternSelector(patternSelector)
/* 307 */       .withConfiguration(config)
/* 308 */       .withRegexReplacement(replace)
/* 309 */       .withCharset(charset)
/* 310 */       .withAlwaysWriteExceptions(alwaysWriteExceptions)
/* 311 */       .withNoConsoleNoAnsi(noConsoleNoAnsi)
/* 312 */       .withHeader(headerPattern)
/* 313 */       .withFooter(footerPattern)
/* 314 */       .build();
/*     */   }
/*     */   
/*     */   private static interface PatternSerializer
/*     */     extends AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2, LocationAware {}
/*     */   
/*     */   private static final class NoFormatPatternSerializer implements PatternSerializer {
/*     */     private final LogEventPatternConverter[] converters;
/*     */     
/*     */     private NoFormatPatternSerializer(PatternFormatter[] formatters) {
/* 324 */       this.converters = new LogEventPatternConverter[formatters.length];
/* 325 */       for (int i = 0; i < formatters.length; i++) {
/* 326 */         this.converters[i] = formatters[i].getConverter();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toSerializable(LogEvent event) {
/* 332 */       StringBuilder sb = AbstractStringLayout.getStringBuilder();
/*     */       try {
/* 334 */         return toSerializable(event, sb).toString();
/*     */       } finally {
/* 336 */         AbstractStringLayout.trimToMaxSize(sb);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
/* 342 */       for (LogEventPatternConverter converter : this.converters) {
/* 343 */         converter.format(event, buffer);
/*     */       }
/* 345 */       return buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresLocation() {
/* 350 */       for (LogEventPatternConverter converter : this.converters) {
/* 351 */         if (converter instanceof LocationAware && ((LocationAware)converter).requiresLocation()) {
/* 352 */           return true;
/*     */         }
/*     */       } 
/* 355 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 360 */       return super.toString() + "[converters=" + Arrays.toString((Object[])this.converters) + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PatternFormatterPatternSerializer
/*     */     implements PatternSerializer {
/*     */     private final PatternFormatter[] formatters;
/*     */     
/*     */     private PatternFormatterPatternSerializer(PatternFormatter[] formatters) {
/* 369 */       this.formatters = formatters;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toSerializable(LogEvent event) {
/* 374 */       StringBuilder sb = AbstractStringLayout.getStringBuilder();
/*     */       try {
/* 376 */         return toSerializable(event, sb).toString();
/*     */       } finally {
/* 378 */         AbstractStringLayout.trimToMaxSize(sb);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
/* 384 */       for (PatternFormatter formatter : this.formatters) {
/* 385 */         formatter.format(event, buffer);
/*     */       }
/* 387 */       return buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresLocation() {
/* 392 */       for (PatternFormatter formatter : this.formatters) {
/* 393 */         if (formatter.requiresLocation()) {
/* 394 */           return true;
/*     */         }
/*     */       } 
/* 397 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 402 */       return super.toString() + "[formatters=" + 
/*     */         
/* 404 */         Arrays.toString((Object[])this.formatters) + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PatternSerializerWithReplacement
/*     */     implements AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2, LocationAware
/*     */   {
/*     */     private final PatternLayout.PatternSerializer delegate;
/*     */     private final RegexReplacement replace;
/*     */     
/*     */     private PatternSerializerWithReplacement(PatternLayout.PatternSerializer delegate, RegexReplacement replace) {
/* 415 */       this.delegate = delegate;
/* 416 */       this.replace = replace;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toSerializable(LogEvent event) {
/* 421 */       StringBuilder sb = AbstractStringLayout.getStringBuilder();
/*     */       try {
/* 423 */         return toSerializable(event, sb).toString();
/*     */       } finally {
/* 425 */         AbstractStringLayout.trimToMaxSize(sb);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuilder toSerializable(LogEvent event, StringBuilder buf) {
/* 431 */       StringBuilder buffer = this.delegate.toSerializable(event, buf);
/* 432 */       String str = buffer.toString();
/* 433 */       str = this.replace.format(str);
/* 434 */       buffer.setLength(0);
/* 435 */       buffer.append(str);
/* 436 */       return buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresLocation() {
/* 441 */       return this.delegate.requiresLocation();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 446 */       return super.toString() + "[delegate=" + this.delegate + ", replace=" + this.replace + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SerializerBuilder
/*     */     implements org.apache.logging.log4j.core.util.Builder<AbstractStringLayout.Serializer>
/*     */   {
/*     */     private Configuration configuration;
/*     */     
/*     */     private RegexReplacement replace;
/*     */     
/*     */     private String pattern;
/*     */     
/*     */     private String defaultPattern;
/*     */     
/*     */     private PatternSelector patternSelector;
/*     */     private boolean alwaysWriteExceptions;
/*     */     private boolean disableAnsi;
/*     */     private boolean noConsoleNoAnsi;
/*     */     
/*     */     public AbstractStringLayout.Serializer build() {
/* 468 */       if (Strings.isEmpty(this.pattern) && Strings.isEmpty(this.defaultPattern)) {
/* 469 */         return null;
/*     */       }
/* 471 */       if (this.patternSelector == null) {
/*     */         try {
/* 473 */           PatternParser parser = PatternLayout.createPatternParser(this.configuration);
/* 474 */           List<PatternFormatter> list = parser.parse((this.pattern == null) ? this.defaultPattern : this.pattern, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi);
/*     */           
/* 476 */           PatternFormatter[] formatters = list.<PatternFormatter>toArray(PatternFormatter.EMPTY_ARRAY);
/* 477 */           boolean hasFormattingInfo = false;
/* 478 */           for (PatternFormatter formatter : formatters) {
/* 479 */             FormattingInfo info = formatter.getFormattingInfo();
/* 480 */             if (info != null && info != FormattingInfo.getDefault()) {
/* 481 */               hasFormattingInfo = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 485 */           PatternLayout.PatternSerializer serializer = hasFormattingInfo ? new PatternLayout.PatternFormatterPatternSerializer(formatters) : new PatternLayout.NoFormatPatternSerializer(formatters);
/*     */ 
/*     */           
/* 488 */           return (this.replace == null) ? serializer : new PatternLayout.PatternSerializerWithReplacement(serializer, this.replace);
/* 489 */         } catch (RuntimeException ex) {
/* 490 */           throw new IllegalArgumentException("Cannot parse pattern '" + this.pattern + "'", ex);
/*     */         } 
/*     */       }
/* 493 */       return new PatternLayout.PatternSelectorSerializer(this.patternSelector, this.replace);
/*     */     }
/*     */     
/*     */     public SerializerBuilder setConfiguration(Configuration configuration) {
/* 497 */       this.configuration = configuration;
/* 498 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setReplace(RegexReplacement replace) {
/* 502 */       this.replace = replace;
/* 503 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setPattern(String pattern) {
/* 507 */       this.pattern = pattern;
/* 508 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setDefaultPattern(String defaultPattern) {
/* 512 */       this.defaultPattern = defaultPattern;
/* 513 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setPatternSelector(PatternSelector patternSelector) {
/* 517 */       this.patternSelector = patternSelector;
/* 518 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 522 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 523 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setDisableAnsi(boolean disableAnsi) {
/* 527 */       this.disableAnsi = disableAnsi;
/* 528 */       return this;
/*     */     }
/*     */     
/*     */     public SerializerBuilder setNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 532 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 533 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PatternSelectorSerializer
/*     */     implements AbstractStringLayout.Serializer, AbstractStringLayout.Serializer2, LocationAware
/*     */   {
/*     */     private final PatternSelector patternSelector;
/*     */     private final RegexReplacement replace;
/*     */     
/*     */     private PatternSelectorSerializer(PatternSelector patternSelector, RegexReplacement replace) {
/* 544 */       this.patternSelector = patternSelector;
/* 545 */       this.replace = replace;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toSerializable(LogEvent event) {
/* 550 */       StringBuilder sb = AbstractStringLayout.getStringBuilder();
/*     */       try {
/* 552 */         return toSerializable(event, sb).toString();
/*     */       } finally {
/* 554 */         AbstractStringLayout.trimToMaxSize(sb);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public StringBuilder toSerializable(LogEvent event, StringBuilder buffer) {
/* 560 */       for (PatternFormatter formatter : this.patternSelector.getFormatters(event)) {
/* 561 */         formatter.format(event, buffer);
/*     */       }
/* 563 */       if (this.replace != null) {
/* 564 */         String str = buffer.toString();
/* 565 */         str = this.replace.format(str);
/* 566 */         buffer.setLength(0);
/* 567 */         buffer.append(str);
/*     */       } 
/* 569 */       return buffer;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresLocation() {
/* 574 */       return (this.patternSelector instanceof LocationAware && ((LocationAware)this.patternSelector).requiresLocation());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 579 */       StringBuilder builder = new StringBuilder();
/* 580 */       builder.append(super.toString());
/* 581 */       builder.append("[patternSelector=");
/* 582 */       builder.append(this.patternSelector);
/* 583 */       builder.append(", replace=");
/* 584 */       builder.append(this.replace);
/* 585 */       builder.append("]");
/* 586 */       return builder.toString();
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
/*     */   public static PatternLayout createDefaultLayout() {
/* 598 */     return newBuilder().build();
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
/*     */   public static PatternLayout createDefaultLayout(Configuration configuration) {
/* 611 */     return newBuilder().withConfiguration(configuration).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 621 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<PatternLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/* 629 */     private String pattern = "%m%n";
/*     */ 
/*     */     
/*     */     @PluginElement("PatternSelector")
/*     */     private PatternSelector patternSelector;
/*     */ 
/*     */     
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     
/*     */     @PluginElement("Replace")
/*     */     private RegexReplacement regexReplacement;
/*     */     
/*     */     @PluginBuilderAttribute
/* 643 */     private Charset charset = Charset.defaultCharset();
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean alwaysWriteExceptions = true;
/*     */     
/*     */     @PluginBuilderAttribute
/* 649 */     private boolean disableAnsi = !useAnsiEscapeCodes();
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean noConsoleNoAnsi;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String header;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String footer;
/*     */ 
/*     */     
/*     */     private boolean useAnsiEscapeCodes() {
/* 664 */       PropertiesUtil propertiesUtil = PropertiesUtil.getProperties();
/* 665 */       boolean isPlatformSupportsAnsi = !propertiesUtil.isOsWindows();
/* 666 */       boolean isJansiRequested = !propertiesUtil.getBooleanProperty("log4j.skipJansi", true);
/* 667 */       return (isPlatformSupportsAnsi || isJansiRequested);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withPattern(String pattern) {
/* 675 */       this.pattern = pattern;
/* 676 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withPatternSelector(PatternSelector patternSelector) {
/* 684 */       this.patternSelector = patternSelector;
/* 685 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withConfiguration(Configuration configuration) {
/* 693 */       this.configuration = configuration;
/* 694 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withRegexReplacement(RegexReplacement regexReplacement) {
/* 702 */       this.regexReplacement = regexReplacement;
/* 703 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withCharset(Charset charset) {
/* 712 */       if (charset != null) {
/* 713 */         this.charset = charset;
/*     */       }
/* 715 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withAlwaysWriteExceptions(boolean alwaysWriteExceptions) {
/* 723 */       this.alwaysWriteExceptions = alwaysWriteExceptions;
/* 724 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withDisableAnsi(boolean disableAnsi) {
/* 733 */       this.disableAnsi = disableAnsi;
/* 734 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withNoConsoleNoAnsi(boolean noConsoleNoAnsi) {
/* 742 */       this.noConsoleNoAnsi = noConsoleNoAnsi;
/* 743 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withHeader(String header) {
/* 751 */       this.header = header;
/* 752 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withFooter(String footer) {
/* 760 */       this.footer = footer;
/* 761 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PatternLayout build() {
/* 767 */       if (this.configuration == null) {
/* 768 */         this.configuration = (Configuration)new DefaultConfiguration();
/*     */       }
/* 770 */       return new PatternLayout(this.configuration, this.regexReplacement, this.pattern, this.patternSelector, this.charset, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.header, this.footer);
/*     */     }
/*     */     
/*     */     private Builder() {} }
/*     */   
/*     */   public AbstractStringLayout.Serializer getEventSerializer() {
/* 776 */     return this.eventSerializer;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\PatternLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */