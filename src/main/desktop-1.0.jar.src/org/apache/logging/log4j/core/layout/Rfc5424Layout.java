/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.TlsSyslogFrame;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.layout.internal.ExcludeChecker;
/*     */ import org.apache.logging.log4j.core.layout.internal.IncludeChecker;
/*     */ import org.apache.logging.log4j.core.layout.internal.ListChecker;
/*     */ import org.apache.logging.log4j.core.net.Facility;
/*     */ import org.apache.logging.log4j.core.net.Priority;
/*     */ import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternFormatter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.StructuredDataId;
/*     */ import org.apache.logging.log4j.message.StructuredDataMessage;
/*     */ import org.apache.logging.log4j.util.ProcessIdUtil;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
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
/*     */ @Plugin(name = "Rfc5424Layout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class Rfc5424Layout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   public static final int DEFAULT_ENTERPRISE_NUMBER = 18060;
/*     */   public static final String DEFAULT_ID = "Audit";
/*  84 */   public static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final Pattern PARAM_VALUE_ESCAPE_PATTERN = Pattern.compile("[\\\"\\]\\\\]");
/*     */   
/*     */   public static final String DEFAULT_MDCID = "mdc";
/*     */   
/*     */   private static final String LF = "\n";
/*     */   
/*     */   private static final int TWO_DIGITS = 10;
/*     */   
/*     */   private static final int THREE_DIGITS = 100;
/*     */   
/*     */   private static final int MILLIS_PER_MINUTE = 60000;
/*     */   
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */   
/*     */   private static final String COMPONENT_KEY = "RFC5424-Converter";
/*     */   
/*     */   private final Facility facility;
/*     */   
/*     */   private final String defaultId;
/*     */   
/*     */   private final int enterpriseNumber;
/*     */   
/*     */   private final boolean includeMdc;
/*     */   
/*     */   private final String mdcId;
/*     */   private final StructuredDataId mdcSdId;
/*     */   private final String localHostName;
/*     */   private final String appName;
/*     */   private final String messageId;
/*     */   private final String configName;
/*     */   private final String mdcPrefix;
/*     */   private final String eventPrefix;
/*     */   private final List<String> mdcExcludes;
/*     */   private final List<String> mdcIncludes;
/*     */   private final List<String> mdcRequired;
/*     */   private final ListChecker listChecker;
/*     */   private final boolean includeNewLine;
/*     */   private final String escapeNewLine;
/*     */   private final boolean useTlsMessageFormat;
/*     */   private long lastTimestamp;
/*     */   private String timestamppStr;
/*     */   private final List<PatternFormatter> exceptionFormatters;
/*     */   private final Map<String, FieldFormatter> fieldFormatters;
/*     */   private final String procId;
/*     */   
/*     */   private Rfc5424Layout(Configuration config, Facility facility, String id, int ein, boolean includeMDC, boolean includeNL, String escapeNL, String mdcId, String mdcPrefix, String eventPrefix, String appName, String messageId, String excludes, String includes, String required, Charset charset, String exceptionPattern, boolean useTLSMessageFormat, LoggerFields[] loggerFields) {
/* 134 */     super(charset); IncludeChecker includeChecker; this.lastTimestamp = -1L;
/* 135 */     PatternParser exceptionParser = createPatternParser(config, (Class)ThrowablePatternConverter.class);
/* 136 */     this.exceptionFormatters = (exceptionPattern == null) ? null : exceptionParser.parse(exceptionPattern);
/* 137 */     this.facility = facility;
/* 138 */     this.defaultId = (id == null) ? "Audit" : id;
/* 139 */     this.enterpriseNumber = ein;
/* 140 */     this.includeMdc = includeMDC;
/* 141 */     this.includeNewLine = includeNL;
/* 142 */     this.escapeNewLine = (escapeNL == null) ? null : Matcher.quoteReplacement(escapeNL);
/* 143 */     this.mdcId = (mdcId != null) ? mdcId : ((id == null) ? "mdc" : id);
/* 144 */     this.mdcSdId = new StructuredDataId(this.mdcId, this.enterpriseNumber, null, null);
/* 145 */     this.mdcPrefix = mdcPrefix;
/* 146 */     this.eventPrefix = eventPrefix;
/* 147 */     this.appName = appName;
/* 148 */     this.messageId = messageId;
/* 149 */     this.useTlsMessageFormat = useTLSMessageFormat;
/* 150 */     this.localHostName = NetUtils.getLocalHostname();
/* 151 */     ListChecker checker = null;
/* 152 */     if (excludes != null) {
/* 153 */       String[] array = excludes.split(Patterns.COMMA_SEPARATOR);
/* 154 */       if (array.length > 0) {
/* 155 */         this.mdcExcludes = new ArrayList<>(array.length);
/* 156 */         for (String str : array) {
/* 157 */           this.mdcExcludes.add(str.trim());
/*     */         }
/* 159 */         ExcludeChecker excludeChecker = new ExcludeChecker(this.mdcExcludes);
/*     */       } else {
/* 161 */         this.mdcExcludes = null;
/*     */       } 
/*     */     } else {
/* 164 */       this.mdcExcludes = null;
/*     */     } 
/* 166 */     if (includes != null) {
/* 167 */       String[] array = includes.split(Patterns.COMMA_SEPARATOR);
/* 168 */       if (array.length > 0) {
/* 169 */         this.mdcIncludes = new ArrayList<>(array.length);
/* 170 */         for (String str : array) {
/* 171 */           this.mdcIncludes.add(str.trim());
/*     */         }
/* 173 */         includeChecker = new IncludeChecker(this.mdcIncludes);
/*     */       } else {
/* 175 */         this.mdcIncludes = null;
/*     */       } 
/*     */     } else {
/* 178 */       this.mdcIncludes = null;
/*     */     } 
/* 180 */     if (required != null) {
/* 181 */       String[] array = required.split(Patterns.COMMA_SEPARATOR);
/* 182 */       if (array.length > 0) {
/* 183 */         this.mdcRequired = new ArrayList<>(array.length);
/* 184 */         for (String str : array) {
/* 185 */           this.mdcRequired.add(str.trim());
/*     */         }
/*     */       } else {
/* 188 */         this.mdcRequired = null;
/*     */       } 
/*     */     } else {
/*     */       
/* 192 */       this.mdcRequired = null;
/*     */     } 
/* 194 */     this.listChecker = (includeChecker != null) ? (ListChecker)includeChecker : (ListChecker)ListChecker.NOOP_CHECKER;
/* 195 */     String name = (config == null) ? null : config.getName();
/* 196 */     this.configName = Strings.isNotEmpty(name) ? name : null;
/* 197 */     this.fieldFormatters = createFieldFormatters(loggerFields, config);
/* 198 */     this.procId = ProcessIdUtil.getProcessId();
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, FieldFormatter> createFieldFormatters(LoggerFields[] loggerFields, Configuration config) {
/* 203 */     Map<String, FieldFormatter> sdIdMap = new HashMap<>((loggerFields == null) ? 0 : loggerFields.length);
/* 204 */     if (loggerFields != null) {
/* 205 */       for (LoggerFields loggerField : loggerFields) {
/* 206 */         StructuredDataId key = (loggerField.getSdId() == null) ? this.mdcSdId : loggerField.getSdId();
/* 207 */         Map<String, List<PatternFormatter>> sdParams = new HashMap<>();
/* 208 */         Map<String, String> fields = loggerField.getMap();
/* 209 */         if (!fields.isEmpty()) {
/* 210 */           PatternParser fieldParser = createPatternParser(config, (Class<? extends PatternConverter>)null);
/*     */           
/* 212 */           for (Map.Entry<String, String> entry : fields.entrySet()) {
/* 213 */             List<PatternFormatter> formatters = fieldParser.parse(entry.getValue());
/* 214 */             sdParams.put(entry.getKey(), formatters);
/*     */           } 
/*     */           
/* 217 */           FieldFormatter fieldFormatter = new FieldFormatter(sdParams, loggerField.getDiscardIfAllFieldsAreEmpty());
/* 218 */           sdIdMap.put(key.toString(), fieldFormatter);
/*     */         } 
/*     */       } 
/*     */     }
/* 222 */     return (sdIdMap.size() > 0) ? sdIdMap : null;
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
/*     */   private static PatternParser createPatternParser(Configuration config, Class<? extends PatternConverter> filterClass) {
/* 234 */     if (config == null) {
/* 235 */       return new PatternParser(config, "Converter", LogEventPatternConverter.class, filterClass);
/*     */     }
/* 237 */     PatternParser parser = (PatternParser)config.getComponent("RFC5424-Converter");
/* 238 */     if (parser == null) {
/* 239 */       parser = new PatternParser(config, "Converter", ThrowablePatternConverter.class);
/* 240 */       config.addComponent("RFC5424-Converter", parser);
/* 241 */       parser = (PatternParser)config.getComponent("RFC5424-Converter");
/*     */     } 
/* 243 */     return parser;
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
/*     */   public Map<String, String> getContentFormat() {
/* 257 */     Map<String, String> result = new HashMap<>();
/* 258 */     result.put("structured", "true");
/* 259 */     result.put("formatType", "RFC5424");
/* 260 */     return result;
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
/* 271 */     StringBuilder buf = getStringBuilder();
/* 272 */     appendPriority(buf, event.getLevel());
/* 273 */     appendTimestamp(buf, event.getTimeMillis());
/* 274 */     appendSpace(buf);
/* 275 */     appendHostName(buf);
/* 276 */     appendSpace(buf);
/* 277 */     appendAppName(buf);
/* 278 */     appendSpace(buf);
/* 279 */     appendProcessId(buf);
/* 280 */     appendSpace(buf);
/* 281 */     appendMessageId(buf, event.getMessage());
/* 282 */     appendSpace(buf);
/* 283 */     appendStructuredElements(buf, event);
/* 284 */     appendMessage(buf, event);
/* 285 */     if (this.useTlsMessageFormat) {
/* 286 */       return (new TlsSyslogFrame(buf.toString())).toString();
/*     */     }
/* 288 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void appendPriority(StringBuilder buffer, Level logLevel) {
/* 292 */     buffer.append('<');
/* 293 */     buffer.append(Priority.getPriority(this.facility, logLevel));
/* 294 */     buffer.append(">1 ");
/*     */   }
/*     */   
/*     */   private void appendTimestamp(StringBuilder buffer, long milliseconds) {
/* 298 */     buffer.append(computeTimeStampString(milliseconds));
/*     */   }
/*     */   
/*     */   private void appendSpace(StringBuilder buffer) {
/* 302 */     buffer.append(' ');
/*     */   }
/*     */   
/*     */   private void appendHostName(StringBuilder buffer) {
/* 306 */     buffer.append(this.localHostName);
/*     */   }
/*     */   
/*     */   private void appendAppName(StringBuilder buffer) {
/* 310 */     if (this.appName != null) {
/* 311 */       buffer.append(this.appName);
/* 312 */     } else if (this.configName != null) {
/* 313 */       buffer.append(this.configName);
/*     */     } else {
/* 315 */       buffer.append('-');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendProcessId(StringBuilder buffer) {
/* 320 */     buffer.append(getProcId());
/*     */   }
/*     */   
/*     */   private void appendMessageId(StringBuilder buffer, Message message) {
/* 324 */     boolean isStructured = message instanceof StructuredDataMessage;
/* 325 */     String type = isStructured ? ((StructuredDataMessage)message).getType() : null;
/* 326 */     if (type != null) {
/* 327 */       buffer.append(type);
/* 328 */     } else if (this.messageId != null) {
/* 329 */       buffer.append(this.messageId);
/*     */     } else {
/* 331 */       buffer.append('-');
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendMessage(StringBuilder buffer, LogEvent event) {
/* 336 */     Message message = event.getMessage();
/*     */ 
/*     */     
/* 339 */     String text = (message instanceof StructuredDataMessage || message instanceof org.apache.logging.log4j.message.MessageCollectionMessage) ? message.getFormat() : message.getFormattedMessage();
/*     */     
/* 341 */     if (text != null && text.length() > 0) {
/* 342 */       buffer.append(' ').append(escapeNewlines(text, this.escapeNewLine));
/*     */     }
/*     */     
/* 345 */     if (this.exceptionFormatters != null && event.getThrown() != null) {
/* 346 */       StringBuilder exception = new StringBuilder("\n");
/* 347 */       for (PatternFormatter formatter : this.exceptionFormatters) {
/* 348 */         formatter.format(event, exception);
/*     */       }
/* 350 */       buffer.append(escapeNewlines(exception.toString(), this.escapeNewLine));
/*     */     } 
/* 352 */     if (this.includeNewLine) {
/* 353 */       buffer.append("\n");
/*     */     }
/*     */   }
/*     */   
/*     */   private void appendStructuredElements(StringBuilder buffer, LogEvent event) {
/* 358 */     Message message = event.getMessage();
/* 359 */     boolean isStructured = (message instanceof StructuredDataMessage || message instanceof org.apache.logging.log4j.message.StructuredDataCollectionMessage);
/*     */ 
/*     */     
/* 362 */     if (!isStructured && this.fieldFormatters != null && this.fieldFormatters.isEmpty() && !this.includeMdc) {
/* 363 */       buffer.append('-');
/*     */       
/*     */       return;
/*     */     } 
/* 367 */     Map<String, StructuredDataElement> sdElements = new HashMap<>();
/* 368 */     Map<String, String> contextMap = event.getContextData().toMap();
/*     */     
/* 370 */     if (this.mdcRequired != null) {
/* 371 */       checkRequired(contextMap);
/*     */     }
/*     */     
/* 374 */     if (this.fieldFormatters != null) {
/* 375 */       for (Map.Entry<String, FieldFormatter> sdElement : this.fieldFormatters.entrySet()) {
/* 376 */         String sdId = sdElement.getKey();
/* 377 */         StructuredDataElement elem = ((FieldFormatter)sdElement.getValue()).format(event);
/* 378 */         sdElements.put(sdId, elem);
/*     */       } 
/*     */     }
/*     */     
/* 382 */     if (this.includeMdc && contextMap.size() > 0) {
/* 383 */       String mdcSdIdStr = this.mdcSdId.toString();
/* 384 */       StructuredDataElement union = sdElements.get(mdcSdIdStr);
/* 385 */       if (union != null) {
/* 386 */         union.union(contextMap);
/* 387 */         sdElements.put(mdcSdIdStr, union);
/*     */       } else {
/* 389 */         StructuredDataElement formattedContextMap = new StructuredDataElement(contextMap, this.mdcPrefix, false);
/* 390 */         sdElements.put(mdcSdIdStr, formattedContextMap);
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     if (isStructured) {
/* 395 */       if (message instanceof org.apache.logging.log4j.message.MessageCollectionMessage) {
/* 396 */         for (StructuredDataMessage data : message) {
/* 397 */           addStructuredData(sdElements, data);
/*     */         }
/*     */       } else {
/* 400 */         addStructuredData(sdElements, (StructuredDataMessage)message);
/*     */       } 
/*     */     }
/*     */     
/* 404 */     if (sdElements.isEmpty()) {
/* 405 */       buffer.append('-');
/*     */       
/*     */       return;
/*     */     } 
/* 409 */     for (Map.Entry<String, StructuredDataElement> entry : sdElements.entrySet()) {
/* 410 */       formatStructuredElement(entry.getKey(), entry.getValue(), buffer, this.listChecker);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addStructuredData(Map<String, StructuredDataElement> sdElements, StructuredDataMessage data) {
/* 415 */     Map<String, String> map = data.getData();
/* 416 */     StructuredDataId id = data.getId();
/* 417 */     String sdId = getId(id);
/*     */     
/* 419 */     if (sdElements.containsKey(sdId)) {
/* 420 */       StructuredDataElement union = sdElements.get(id.toString());
/* 421 */       union.union(map);
/* 422 */       sdElements.put(sdId, union);
/*     */     } else {
/* 424 */       StructuredDataElement formattedData = new StructuredDataElement(map, this.eventPrefix, false);
/* 425 */       sdElements.put(sdId, formattedData);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String escapeNewlines(String text, String replacement) {
/* 430 */     if (null == replacement) {
/* 431 */       return text;
/*     */     }
/* 433 */     return NEWLINE_PATTERN.matcher(text).replaceAll(replacement);
/*     */   }
/*     */   
/*     */   protected String getProcId() {
/* 437 */     return this.procId;
/*     */   }
/*     */   
/*     */   protected List<String> getMdcExcludes() {
/* 441 */     return this.mdcExcludes;
/*     */   }
/*     */   
/*     */   protected List<String> getMdcIncludes() {
/* 445 */     return this.mdcIncludes;
/*     */   }
/*     */   
/*     */   private String computeTimeStampString(long now) {
/*     */     long last;
/* 450 */     synchronized (this) {
/* 451 */       last = this.lastTimestamp;
/* 452 */       if (now == this.lastTimestamp) {
/* 453 */         return this.timestamppStr;
/*     */       }
/*     */     } 
/*     */     
/* 457 */     StringBuilder buffer = new StringBuilder();
/* 458 */     Calendar cal = new GregorianCalendar();
/* 459 */     cal.setTimeInMillis(now);
/* 460 */     buffer.append(Integer.toString(cal.get(1)));
/* 461 */     buffer.append('-');
/* 462 */     pad(cal.get(2) + 1, 10, buffer);
/* 463 */     buffer.append('-');
/* 464 */     pad(cal.get(5), 10, buffer);
/* 465 */     buffer.append('T');
/* 466 */     pad(cal.get(11), 10, buffer);
/* 467 */     buffer.append(':');
/* 468 */     pad(cal.get(12), 10, buffer);
/* 469 */     buffer.append(':');
/* 470 */     pad(cal.get(13), 10, buffer);
/* 471 */     buffer.append('.');
/* 472 */     pad(cal.get(14), 100, buffer);
/*     */     
/* 474 */     int tzmin = (cal.get(15) + cal.get(16)) / 60000;
/* 475 */     if (tzmin == 0) {
/* 476 */       buffer.append('Z');
/*     */     } else {
/* 478 */       if (tzmin < 0) {
/* 479 */         tzmin = -tzmin;
/* 480 */         buffer.append('-');
/*     */       } else {
/* 482 */         buffer.append('+');
/*     */       } 
/* 484 */       int tzhour = tzmin / 60;
/* 485 */       tzmin -= tzhour * 60;
/* 486 */       pad(tzhour, 10, buffer);
/* 487 */       buffer.append(':');
/* 488 */       pad(tzmin, 10, buffer);
/*     */     } 
/* 490 */     synchronized (this) {
/* 491 */       if (last == this.lastTimestamp) {
/* 492 */         this.lastTimestamp = now;
/* 493 */         this.timestamppStr = buffer.toString();
/*     */       } 
/*     */     } 
/* 496 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   private void pad(int val, int max, StringBuilder buf) {
/* 500 */     while (max > 1) {
/* 501 */       if (val < max) {
/* 502 */         buf.append('0');
/*     */       }
/* 504 */       max /= 10;
/*     */     } 
/* 506 */     buf.append(Integer.toString(val));
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatStructuredElement(String id, StructuredDataElement data, StringBuilder sb, ListChecker checker) {
/* 511 */     if ((id == null && this.defaultId == null) || data.discard()) {
/*     */       return;
/*     */     }
/*     */     
/* 515 */     sb.append('[');
/* 516 */     sb.append(id);
/* 517 */     if (!this.mdcSdId.toString().equals(id)) {
/* 518 */       appendMap(data.getPrefix(), data.getFields(), sb, (ListChecker)ListChecker.NOOP_CHECKER);
/*     */     } else {
/* 520 */       appendMap(data.getPrefix(), data.getFields(), sb, checker);
/*     */     } 
/* 522 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private String getId(StructuredDataId id) {
/* 526 */     StringBuilder sb = new StringBuilder();
/* 527 */     if (id == null || id.getName() == null) {
/* 528 */       sb.append(this.defaultId);
/*     */     } else {
/* 530 */       sb.append(id.getName());
/*     */     } 
/* 532 */     int ein = (id != null) ? id.getEnterpriseNumber() : this.enterpriseNumber;
/* 533 */     if (ein < 0) {
/* 534 */       ein = this.enterpriseNumber;
/*     */     }
/* 536 */     if (ein >= 0) {
/* 537 */       sb.append('@').append(ein);
/*     */     }
/* 539 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void checkRequired(Map<String, String> map) {
/* 543 */     for (String key : this.mdcRequired) {
/* 544 */       String value = map.get(key);
/* 545 */       if (value == null) {
/* 546 */         throw new LoggingException("Required key " + key + " is missing from the " + this.mdcId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void appendMap(String prefix, Map<String, String> map, StringBuilder sb, ListChecker checker) {
/* 553 */     SortedMap<String, String> sorted = new TreeMap<>(map);
/* 554 */     for (Map.Entry<String, String> entry : sorted.entrySet()) {
/* 555 */       if (checker.check(entry.getKey()) && entry.getValue() != null) {
/* 556 */         sb.append(' ');
/* 557 */         if (prefix != null) {
/* 558 */           sb.append(prefix);
/*     */         }
/* 560 */         String safeKey = escapeNewlines(escapeSDParams(entry.getKey()), this.escapeNewLine);
/* 561 */         String safeValue = escapeNewlines(escapeSDParams(entry.getValue()), this.escapeNewLine);
/* 562 */         StringBuilders.appendKeyDqValue(sb, safeKey, safeValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String escapeSDParams(String value) {
/* 568 */     return PARAM_VALUE_ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$0");
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 573 */     StringBuilder sb = new StringBuilder();
/* 574 */     sb.append("facility=").append(this.facility.name());
/* 575 */     sb.append(" appName=").append(this.appName);
/* 576 */     sb.append(" defaultId=").append(this.defaultId);
/* 577 */     sb.append(" enterpriseNumber=").append(this.enterpriseNumber);
/* 578 */     sb.append(" newLine=").append(this.includeNewLine);
/* 579 */     sb.append(" includeMDC=").append(this.includeMdc);
/* 580 */     sb.append(" messageId=").append(this.messageId);
/* 581 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static Rfc5424Layout createLayout(@PluginAttribute(value = "facility", defaultString = "LOCAL0") Facility facility, @PluginAttribute("id") String id, @PluginAttribute(value = "enterpriseNumber", defaultInt = 18060) int enterpriseNumber, @PluginAttribute(value = "includeMDC", defaultBoolean = true) boolean includeMDC, @PluginAttribute(value = "mdcId", defaultString = "mdc") String mdcId, @PluginAttribute("mdcPrefix") String mdcPrefix, @PluginAttribute("eventPrefix") String eventPrefix, @PluginAttribute("newLine") boolean newLine, @PluginAttribute("newLineEscape") String escapeNL, @PluginAttribute("appName") String appName, @PluginAttribute("messageId") String msgId, @PluginAttribute("mdcExcludes") String excludes, @PluginAttribute("mdcIncludes") String includes, @PluginAttribute("mdcRequired") String required, @PluginAttribute("exceptionPattern") String exceptionPattern, @PluginAttribute("useTlsMessageFormat") boolean useTlsMessageFormat, @PluginElement("LoggerFields") LoggerFields[] loggerFields, @PluginConfiguration Configuration config) {
/* 632 */     if (includes != null && excludes != null) {
/* 633 */       LOGGER.error("mdcIncludes and mdcExcludes are mutually exclusive. Includes wil be ignored");
/* 634 */       includes = null;
/*     */     } 
/*     */     
/* 637 */     return new Rfc5424Layout(config, facility, id, enterpriseNumber, includeMDC, newLine, escapeNL, mdcId, mdcPrefix, eventPrefix, appName, msgId, excludes, includes, required, StandardCharsets.UTF_8, exceptionPattern, useTlsMessageFormat, loggerFields);
/*     */   }
/*     */ 
/*     */   
/*     */   private class FieldFormatter
/*     */   {
/*     */     private final Map<String, List<PatternFormatter>> delegateMap;
/*     */     
/*     */     private final boolean discardIfEmpty;
/*     */     
/*     */     public FieldFormatter(Map<String, List<PatternFormatter>> fieldMap, boolean discardIfEmpty) {
/* 648 */       this.discardIfEmpty = discardIfEmpty;
/* 649 */       this.delegateMap = fieldMap;
/*     */     }
/*     */     
/*     */     public Rfc5424Layout.StructuredDataElement format(LogEvent event) {
/* 653 */       Map<String, String> map = new HashMap<>(this.delegateMap.size());
/*     */       
/* 655 */       for (Map.Entry<String, List<PatternFormatter>> entry : this.delegateMap.entrySet()) {
/* 656 */         StringBuilder buffer = new StringBuilder();
/* 657 */         for (PatternFormatter formatter : entry.getValue()) {
/* 658 */           formatter.format(event, buffer);
/*     */         }
/* 660 */         map.put(entry.getKey(), buffer.toString());
/*     */       } 
/* 662 */       return new Rfc5424Layout.StructuredDataElement(map, Rfc5424Layout.this.eventPrefix, this.discardIfEmpty);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StructuredDataElement
/*     */   {
/*     */     private final Map<String, String> fields;
/*     */     private final boolean discardIfEmpty;
/*     */     private final String prefix;
/*     */     
/*     */     public StructuredDataElement(Map<String, String> fields, String prefix, boolean discardIfEmpty) {
/* 674 */       this.discardIfEmpty = discardIfEmpty;
/* 675 */       this.fields = fields;
/* 676 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     boolean discard() {
/* 680 */       if (!this.discardIfEmpty) {
/* 681 */         return false;
/*     */       }
/* 683 */       boolean foundNotEmptyValue = false;
/* 684 */       for (Map.Entry<String, String> entry : this.fields.entrySet()) {
/* 685 */         if (Strings.isNotEmpty(entry.getValue())) {
/* 686 */           foundNotEmptyValue = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 690 */       return !foundNotEmptyValue;
/*     */     }
/*     */     
/*     */     void union(Map<String, String> addFields) {
/* 694 */       this.fields.putAll(addFields);
/*     */     }
/*     */     
/*     */     Map<String, String> getFields() {
/* 698 */       return this.fields;
/*     */     }
/*     */     
/*     */     String getPrefix() {
/* 702 */       return this.prefix;
/*     */     }
/*     */   }
/*     */   
/*     */   public Facility getFacility() {
/* 707 */     return this.facility;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\Rfc5424Layout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */