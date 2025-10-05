/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.time.Instant;
/*     */ import org.apache.logging.log4j.core.time.MutableInstant;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
/*     */ import org.apache.logging.log4j.core.util.datetime.FixedDateFormat;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "DatePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"d", "date"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class DatePatternConverter
/*     */   extends LogEventPatternConverter
/*     */   implements ArrayPatternConverter
/*     */ {
/*     */   private static final String UNIX_FORMAT = "UNIX";
/*     */   private static final String UNIX_MILLIS_FORMAT = "UNIX_MILLIS";
/*     */   private final String[] options;
/*     */   
/*     */   private static abstract class Formatter
/*     */   {
/*     */     long previousTime;
/*     */     int nanos;
/*     */     
/*     */     private Formatter() {}
/*     */     
/*     */     public String toPattern() {
/*  52 */       return null;
/*     */     }
/*     */     
/*     */     abstract String format(Instant param1Instant);
/*     */     
/*     */     abstract void formatToBuffer(Instant param1Instant, StringBuilder param1StringBuilder); }
/*     */   
/*     */   private static final class PatternFormatter extends Formatter {
/*  60 */     private final StringBuilder cachedBuffer = new StringBuilder(64); private final FastDateFormat fastDateFormat;
/*     */     
/*     */     PatternFormatter(FastDateFormat fastDateFormat) {
/*  63 */       this.fastDateFormat = fastDateFormat;
/*     */     }
/*     */ 
/*     */     
/*     */     String format(Instant instant) {
/*  68 */       return this.fastDateFormat.format(instant.getEpochMillisecond());
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(Instant instant, StringBuilder destination) {
/*  73 */       long timeMillis = instant.getEpochMillisecond();
/*  74 */       if (this.previousTime != timeMillis) {
/*  75 */         this.cachedBuffer.setLength(0);
/*  76 */         this.fastDateFormat.format(timeMillis, this.cachedBuffer);
/*     */       } 
/*  78 */       destination.append(this.cachedBuffer);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toPattern() {
/*  83 */       return this.fastDateFormat.getPattern();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class FixedFormatter
/*     */     extends Formatter
/*     */   {
/*     */     private final FixedDateFormat fixedDateFormat;
/*  91 */     private final char[] cachedBuffer = new char[70];
/*  92 */     private int length = 0;
/*     */     
/*     */     FixedFormatter(FixedDateFormat fixedDateFormat) {
/*  95 */       this.fixedDateFormat = fixedDateFormat;
/*     */     }
/*     */ 
/*     */     
/*     */     String format(Instant instant) {
/* 100 */       return this.fixedDateFormat.formatInstant(instant);
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(Instant instant, StringBuilder destination) {
/* 105 */       long epochSecond = instant.getEpochSecond();
/* 106 */       int nanoOfSecond = instant.getNanoOfSecond();
/* 107 */       if (!this.fixedDateFormat.isEquivalent(this.previousTime, this.nanos, epochSecond, nanoOfSecond)) {
/* 108 */         this.length = this.fixedDateFormat.formatInstant(instant, this.cachedBuffer, 0);
/* 109 */         this.previousTime = epochSecond;
/* 110 */         this.nanos = nanoOfSecond;
/*     */       } 
/* 112 */       destination.append(this.cachedBuffer, 0, this.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toPattern() {
/* 117 */       return this.fixedDateFormat.getFormat();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UnixFormatter extends Formatter {
/*     */     private UnixFormatter() {}
/*     */     
/*     */     String format(Instant instant) {
/* 125 */       return Long.toString(instant.getEpochSecond());
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(Instant instant, StringBuilder destination) {
/* 130 */       destination.append(instant.getEpochSecond());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UnixMillisFormatter extends Formatter {
/*     */     private UnixMillisFormatter() {}
/*     */     
/*     */     String format(Instant instant) {
/* 138 */       return Long.toString(instant.getEpochMillisecond());
/*     */     }
/*     */ 
/*     */     
/*     */     void formatToBuffer(Instant instant, StringBuilder destination) {
/* 143 */       destination.append(instant.getEpochMillisecond());
/*     */     }
/*     */   }
/*     */   
/*     */   private final class CachedTime {
/*     */     public long epochSecond;
/*     */     public int nanoOfSecond;
/*     */     public String formatted;
/*     */     
/*     */     public CachedTime(Instant instant) {
/* 153 */       this.epochSecond = instant.getEpochSecond();
/* 154 */       this.nanoOfSecond = instant.getNanoOfSecond();
/* 155 */       this.formatted = DatePatternConverter.this.formatter.format(instant);
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
/* 170 */   private final ThreadLocal<MutableInstant> threadLocalMutableInstant = new ThreadLocal<>();
/* 171 */   private final ThreadLocal<Formatter> threadLocalFormatter = new ThreadLocal<>();
/*     */ 
/*     */   
/*     */   private final AtomicReference<CachedTime> cachedTime;
/*     */ 
/*     */   
/*     */   private final Formatter formatter;
/*     */ 
/*     */   
/*     */   private DatePatternConverter(String[] options) {
/* 181 */     super("Date", "date");
/* 182 */     this.options = (options == null) ? null : Arrays.<String>copyOf(options, options.length);
/* 183 */     this.formatter = createFormatter(options);
/* 184 */     this.cachedTime = new AtomicReference<>(fromEpochMillis(System.currentTimeMillis()));
/*     */   }
/*     */   
/*     */   private CachedTime fromEpochMillis(long epochMillis) {
/* 188 */     MutableInstant temp = new MutableInstant();
/* 189 */     temp.initFromEpochMilli(epochMillis, 0);
/* 190 */     return new CachedTime((Instant)temp);
/*     */   }
/*     */   
/*     */   private Formatter createFormatter(String[] options) {
/* 194 */     FixedDateFormat fixedDateFormat = FixedDateFormat.createIfSupported(options);
/* 195 */     if (fixedDateFormat != null) {
/* 196 */       return createFixedFormatter(fixedDateFormat);
/*     */     }
/* 198 */     return createNonFixedFormatter(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DatePatternConverter newInstance(String[] options) {
/* 208 */     return new DatePatternConverter(options);
/*     */   }
/*     */   
/*     */   private static Formatter createFixedFormatter(FixedDateFormat fixedDateFormat) {
/* 212 */     return new FixedFormatter(fixedDateFormat);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Formatter createNonFixedFormatter(String[] options) {
/* 217 */     Objects.requireNonNull(options);
/* 218 */     if (options.length == 0) {
/* 219 */       throw new IllegalArgumentException("Options array must have at least one element");
/*     */     }
/* 221 */     Objects.requireNonNull(options[0]);
/* 222 */     String patternOption = options[0];
/* 223 */     if ("UNIX".equals(patternOption)) {
/* 224 */       return new UnixFormatter();
/*     */     }
/* 226 */     if ("UNIX_MILLIS".equals(patternOption)) {
/* 227 */       return new UnixMillisFormatter();
/*     */     }
/*     */     
/* 230 */     FixedDateFormat.FixedFormat fixedFormat = FixedDateFormat.FixedFormat.lookup(patternOption);
/* 231 */     String pattern = (fixedFormat == null) ? patternOption : fixedFormat.getPattern();
/*     */ 
/*     */     
/* 234 */     TimeZone tz = null;
/* 235 */     if (options.length > 1 && options[1] != null) {
/* 236 */       tz = TimeZone.getTimeZone(options[1]);
/*     */     }
/*     */     
/*     */     try {
/* 240 */       FastDateFormat tempFormat = FastDateFormat.getInstance(pattern, tz);
/* 241 */       return new PatternFormatter(tempFormat);
/* 242 */     } catch (IllegalArgumentException e) {
/* 243 */       LOGGER.warn("Could not instantiate FastDateFormat with pattern " + pattern, e);
/*     */ 
/*     */       
/* 246 */       return createFixedFormatter(FixedDateFormat.create(FixedDateFormat.FixedFormat.DEFAULT, tz));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Date date, StringBuilder toAppendTo) {
/* 257 */     format(date.getTime(), toAppendTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder output) {
/* 265 */     format(event.getInstant(), output);
/*     */   }
/*     */   
/*     */   public void format(long epochMilli, StringBuilder output) {
/* 269 */     MutableInstant instant = getMutableInstant();
/* 270 */     instant.initFromEpochMilli(epochMilli, 0);
/* 271 */     format((Instant)instant, output);
/*     */   }
/*     */   
/*     */   private MutableInstant getMutableInstant() {
/* 275 */     if (Constants.ENABLE_THREADLOCALS) {
/* 276 */       MutableInstant result = this.threadLocalMutableInstant.get();
/* 277 */       if (result == null) {
/* 278 */         result = new MutableInstant();
/* 279 */         this.threadLocalMutableInstant.set(result);
/*     */       } 
/* 281 */       return result;
/*     */     } 
/* 283 */     return new MutableInstant();
/*     */   }
/*     */   
/*     */   public void format(Instant instant, StringBuilder output) {
/* 287 */     if (Constants.ENABLE_THREADLOCALS) {
/* 288 */       formatWithoutAllocation(instant, output);
/*     */     } else {
/* 290 */       formatWithoutThreadLocals(instant, output);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatWithoutAllocation(Instant instant, StringBuilder output) {
/* 295 */     getThreadLocalFormatter().formatToBuffer(instant, output);
/*     */   }
/*     */   
/*     */   private Formatter getThreadLocalFormatter() {
/* 299 */     Formatter result = this.threadLocalFormatter.get();
/* 300 */     if (result == null) {
/* 301 */       result = createFormatter(this.options);
/* 302 */       this.threadLocalFormatter.set(result);
/*     */     } 
/* 304 */     return result;
/*     */   }
/*     */   
/*     */   private void formatWithoutThreadLocals(Instant instant, StringBuilder output) {
/* 308 */     CachedTime cached = this.cachedTime.get();
/* 309 */     if (instant.getEpochSecond() != cached.epochSecond || instant.getNanoOfSecond() != cached.nanoOfSecond) {
/* 310 */       CachedTime newTime = new CachedTime(instant);
/* 311 */       if (this.cachedTime.compareAndSet(cached, newTime)) {
/* 312 */         cached = newTime;
/*     */       } else {
/* 314 */         cached = this.cachedTime.get();
/*     */       } 
/*     */     } 
/* 317 */     output.append(cached.formatted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(Object obj, StringBuilder output) {
/* 325 */     if (obj instanceof Date) {
/* 326 */       format((Date)obj, output);
/*     */     }
/* 328 */     super.format(obj, output);
/*     */   }
/*     */ 
/*     */   
/*     */   public void format(StringBuilder toAppendTo, Object... objects) {
/* 333 */     for (Object obj : objects) {
/* 334 */       if (obj instanceof Date) {
/* 335 */         format(obj, toAppendTo);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 347 */     return this.formatter.toPattern();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\DatePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */