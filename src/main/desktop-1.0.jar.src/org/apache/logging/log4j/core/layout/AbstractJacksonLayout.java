/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAnyGetter;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonRootName;
/*     */ import com.fasterxml.jackson.annotation.JsonUnwrapped;
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableProxy;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.time.Instant;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.core.util.StringBuilderWriter;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractJacksonLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   protected static final String DEFAULT_EOL = "\r\n";
/*     */   protected static final String COMPACT_EOL = "";
/*     */   protected final String eol;
/*     */   protected final ObjectWriter objectWriter;
/*     */   protected final boolean compact;
/*     */   protected final boolean complete;
/*     */   protected final boolean includeNullDelimiter;
/*     */   protected final ResolvableKeyValuePair[] additionalFields;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractStringLayout.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean eventEol;
/*     */     @PluginBuilderAttribute
/*     */     private String endOfLine;
/*     */     @PluginBuilderAttribute
/*     */     private boolean compact;
/*     */     @PluginBuilderAttribute
/*     */     private boolean complete;
/*     */     @PluginBuilderAttribute
/*     */     private boolean locationInfo;
/*     */     @PluginBuilderAttribute
/*     */     private boolean properties;
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeStacktrace = true;
/*     */     @PluginBuilderAttribute
/*     */     private boolean stacktraceAsString = false;
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeNullDelimiter = false;
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeTimeMillis = false;
/*     */     @PluginElement("AdditionalField")
/*     */     private KeyValuePair[] additionalFields;
/*     */     
/*     */     protected String toStringOrNull(byte[] header) {
/*  93 */       return (header == null) ? null : new String(header, Charset.defaultCharset());
/*     */     }
/*     */     
/*     */     public boolean getEventEol() {
/*  97 */       return this.eventEol;
/*     */     }
/*     */     
/*     */     public String getEndOfLine() {
/* 101 */       return this.endOfLine;
/*     */     }
/*     */     
/*     */     public boolean isCompact() {
/* 105 */       return this.compact;
/*     */     }
/*     */     
/*     */     public boolean isComplete() {
/* 109 */       return this.complete;
/*     */     }
/*     */     
/*     */     public boolean isLocationInfo() {
/* 113 */       return this.locationInfo;
/*     */     }
/*     */     
/*     */     public boolean isProperties() {
/* 117 */       return this.properties;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isIncludeStacktrace() {
/* 125 */       return this.includeStacktrace;
/*     */     }
/*     */     
/*     */     public boolean isStacktraceAsString() {
/* 129 */       return this.stacktraceAsString;
/*     */     }
/*     */     public boolean isIncludeNullDelimiter() {
/* 132 */       return this.includeNullDelimiter;
/*     */     }
/*     */     public boolean isIncludeTimeMillis() {
/* 135 */       return this.includeTimeMillis;
/*     */     }
/*     */     
/*     */     public KeyValuePair[] getAdditionalFields() {
/* 139 */       return this.additionalFields;
/*     */     }
/*     */     
/*     */     public B setEventEol(boolean eventEol) {
/* 143 */       this.eventEol = eventEol;
/* 144 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setEndOfLine(String endOfLine) {
/* 148 */       this.endOfLine = endOfLine;
/* 149 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setCompact(boolean compact) {
/* 153 */       this.compact = compact;
/* 154 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setComplete(boolean complete) {
/* 158 */       this.complete = complete;
/* 159 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setLocationInfo(boolean locationInfo) {
/* 163 */       this.locationInfo = locationInfo;
/* 164 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setProperties(boolean properties) {
/* 168 */       this.properties = properties;
/* 169 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeStacktrace(boolean includeStacktrace) {
/* 178 */       this.includeStacktrace = includeStacktrace;
/* 179 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setStacktraceAsString(boolean stacktraceAsString) {
/* 188 */       this.stacktraceAsString = stacktraceAsString;
/* 189 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeNullDelimiter(boolean includeNullDelimiter) {
/* 198 */       this.includeNullDelimiter = includeNullDelimiter;
/* 199 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeTimeMillis(boolean includeTimeMillis) {
/* 208 */       this.includeTimeMillis = includeTimeMillis;
/* 209 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setAdditionalFields(KeyValuePair[] additionalFields) {
/* 218 */       this.additionalFields = additionalFields;
/* 219 */       return asBuilder();
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
/*     */   @Deprecated
/*     */   protected AbstractJacksonLayout(Configuration config, ObjectWriter objectWriter, Charset charset, boolean compact, boolean complete, boolean eventEol, AbstractStringLayout.Serializer headerSerializer, AbstractStringLayout.Serializer footerSerializer) {
/* 234 */     this(config, objectWriter, charset, compact, complete, eventEol, headerSerializer, footerSerializer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected AbstractJacksonLayout(Configuration config, ObjectWriter objectWriter, Charset charset, boolean compact, boolean complete, boolean eventEol, AbstractStringLayout.Serializer headerSerializer, AbstractStringLayout.Serializer footerSerializer, boolean includeNullDelimiter) {
/* 241 */     this(config, objectWriter, charset, compact, complete, eventEol, (String)null, headerSerializer, footerSerializer, includeNullDelimiter, (KeyValuePair[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractJacksonLayout(Configuration config, ObjectWriter objectWriter, Charset charset, boolean compact, boolean complete, boolean eventEol, String endOfLine, AbstractStringLayout.Serializer headerSerializer, AbstractStringLayout.Serializer footerSerializer, boolean includeNullDelimiter, KeyValuePair[] additionalFields) {
/* 248 */     super(config, charset, headerSerializer, footerSerializer);
/* 249 */     this.objectWriter = objectWriter;
/* 250 */     this.compact = compact;
/* 251 */     this.complete = complete;
/* 252 */     this.eol = (endOfLine != null) ? endOfLine : ((compact && !eventEol) ? "" : "\r\n");
/* 253 */     this.includeNullDelimiter = includeNullDelimiter;
/* 254 */     this.additionalFields = prepareAdditionalFields(config, additionalFields);
/*     */   }
/*     */   
/*     */   protected static boolean valueNeedsLookup(String value) {
/* 258 */     return (value != null && value.contains("${"));
/*     */   }
/*     */   
/*     */   private static ResolvableKeyValuePair[] prepareAdditionalFields(Configuration config, KeyValuePair[] additionalFields) {
/* 262 */     if (additionalFields == null || additionalFields.length == 0)
/*     */     {
/* 264 */       return ResolvableKeyValuePair.EMPTY_ARRAY;
/*     */     }
/*     */ 
/*     */     
/* 268 */     ResolvableKeyValuePair[] resolvableFields = new ResolvableKeyValuePair[additionalFields.length];
/*     */     
/* 270 */     for (int i = 0; i < additionalFields.length; i++) {
/* 271 */       ResolvableKeyValuePair resolvable = resolvableFields[i] = new ResolvableKeyValuePair(additionalFields[i]);
/*     */ 
/*     */       
/* 274 */       if (config == null && resolvable.valueNeedsLookup) {
/* 275 */         throw new IllegalArgumentException("configuration needs to be set when there are additional fields with variables");
/*     */       }
/*     */     } 
/*     */     
/* 279 */     return resolvableFields;
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
/* 290 */     StringBuilderWriter writer = new StringBuilderWriter();
/*     */     try {
/* 292 */       toSerializable(event, (Writer)writer);
/* 293 */       return writer.toString();
/* 294 */     } catch (IOException e) {
/*     */       
/* 296 */       LOGGER.error(e);
/* 297 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LogEvent convertMutableToLog4jEvent(LogEvent event) {
/* 305 */     return (event instanceof Log4jLogEvent) ? event : Log4jLogEvent.createMemento(event);
/*     */   }
/*     */   
/*     */   protected Object wrapLogEvent(LogEvent event) {
/* 309 */     if (this.additionalFields.length > 0) {
/*     */       
/* 311 */       Map<String, String> additionalFieldsMap = resolveAdditionalFields(event);
/*     */       
/* 313 */       return new LogEventWithAdditionalFields(event, additionalFieldsMap);
/* 314 */     }  if (event instanceof Message)
/*     */     {
/* 316 */       return new ReadOnlyLogEventWrapper(event);
/*     */     }
/*     */     
/* 319 */     return event;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> resolveAdditionalFields(LogEvent logEvent) {
/* 325 */     Map<String, String> additionalFieldsMap = new LinkedHashMap<>(this.additionalFields.length);
/* 326 */     StrSubstitutor strSubstitutor = this.configuration.getStrSubstitutor();
/*     */ 
/*     */     
/* 329 */     for (ResolvableKeyValuePair pair : this.additionalFields) {
/* 330 */       if (pair.valueNeedsLookup) {
/*     */         
/* 332 */         additionalFieldsMap.put(pair.key, strSubstitutor.replace(logEvent, pair.value));
/*     */       } else {
/*     */         
/* 335 */         additionalFieldsMap.put(pair.key, pair.value);
/*     */       } 
/*     */     } 
/*     */     
/* 339 */     return additionalFieldsMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toSerializable(LogEvent event, Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
/* 344 */     this.objectWriter.writeValue(writer, wrapLogEvent(convertMutableToLog4jEvent(event)));
/* 345 */     writer.write(this.eol);
/* 346 */     if (this.includeNullDelimiter) {
/* 347 */       writer.write(0);
/*     */     }
/* 349 */     markEvent();
/*     */   }
/*     */   
/*     */   @JsonRootName("Event")
/*     */   @JacksonXmlRootElement(namespace = "http://logging.apache.org/log4j/2.0/events", localName = "Event")
/*     */   public static class LogEventWithAdditionalFields
/*     */   {
/*     */     private final Object logEvent;
/*     */     private final Map<String, String> additionalFields;
/*     */     
/*     */     public LogEventWithAdditionalFields(Object logEvent, Map<String, String> additionalFields) {
/* 360 */       this.logEvent = logEvent;
/* 361 */       this.additionalFields = additionalFields;
/*     */     }
/*     */     
/*     */     @JsonUnwrapped
/*     */     public Object getLogEvent() {
/* 366 */       return this.logEvent;
/*     */     }
/*     */ 
/*     */     
/*     */     @JsonAnyGetter
/*     */     public Map<String, String> getAdditionalFields() {
/* 372 */       return this.additionalFields;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class ResolvableKeyValuePair
/*     */   {
/* 381 */     static final ResolvableKeyValuePair[] EMPTY_ARRAY = new ResolvableKeyValuePair[0];
/*     */     
/*     */     final String key;
/*     */     final String value;
/*     */     final boolean valueNeedsLookup;
/*     */     
/*     */     ResolvableKeyValuePair(KeyValuePair pair) {
/* 388 */       this.key = pair.getKey();
/* 389 */       this.value = pair.getValue();
/* 390 */       this.valueNeedsLookup = AbstractJacksonLayout.valueNeedsLookup(this.value);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ReadOnlyLogEventWrapper
/*     */     implements LogEvent {
/*     */     @JsonIgnore
/*     */     private final LogEvent event;
/*     */     
/*     */     public ReadOnlyLogEventWrapper(LogEvent event) {
/* 400 */       this.event = event;
/*     */     }
/*     */ 
/*     */     
/*     */     public LogEvent toImmutable() {
/* 405 */       return this.event.toImmutable();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, String> getContextMap() {
/* 410 */       return this.event.getContextMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public ReadOnlyStringMap getContextData() {
/* 415 */       return this.event.getContextData();
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack getContextStack() {
/* 420 */       return this.event.getContextStack();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLoggerFqcn() {
/* 425 */       return this.event.getLoggerFqcn();
/*     */     }
/*     */ 
/*     */     
/*     */     public Level getLevel() {
/* 430 */       return this.event.getLevel();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLoggerName() {
/* 435 */       return this.event.getLoggerName();
/*     */     }
/*     */ 
/*     */     
/*     */     public Marker getMarker() {
/* 440 */       return this.event.getMarker();
/*     */     }
/*     */ 
/*     */     
/*     */     public Message getMessage() {
/* 445 */       return this.event.getMessage();
/*     */     }
/*     */ 
/*     */     
/*     */     public long getTimeMillis() {
/* 450 */       return this.event.getTimeMillis();
/*     */     }
/*     */ 
/*     */     
/*     */     public Instant getInstant() {
/* 455 */       return this.event.getInstant();
/*     */     }
/*     */ 
/*     */     
/*     */     public StackTraceElement getSource() {
/* 460 */       return this.event.getSource();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getThreadName() {
/* 465 */       return this.event.getThreadName();
/*     */     }
/*     */ 
/*     */     
/*     */     public long getThreadId() {
/* 470 */       return this.event.getThreadId();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getThreadPriority() {
/* 475 */       return this.event.getThreadPriority();
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable getThrown() {
/* 480 */       return this.event.getThrown();
/*     */     }
/*     */ 
/*     */     
/*     */     public ThrowableProxy getThrownProxy() {
/* 485 */       return this.event.getThrownProxy();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEndOfBatch() {
/* 490 */       return this.event.isEndOfBatch();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isIncludeLocation() {
/* 495 */       return this.event.isIncludeLocation();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setEndOfBatch(boolean endOfBatch) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void setIncludeLocation(boolean locationRequired) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public long getNanoTime() {
/* 510 */       return this.event.getNanoTime();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\AbstractJacksonLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */