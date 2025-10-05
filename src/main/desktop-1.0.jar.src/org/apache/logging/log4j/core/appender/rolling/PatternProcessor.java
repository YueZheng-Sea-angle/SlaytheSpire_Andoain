/*     */ package org.apache.logging.log4j.core.appender.rolling;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.DatePatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.FormattingInfo;
/*     */ import org.apache.logging.log4j.core.pattern.PatternConverter;
/*     */ import org.apache.logging.log4j.core.pattern.PatternParser;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternProcessor
/*     */ {
/*  41 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String KEY = "FileConverter";
/*     */   private static final char YEAR_CHAR = 'y';
/*     */   private static final char MONTH_CHAR = 'M';
/*  46 */   private static final char[] WEEK_CHARS = new char[] { 'w', 'W' };
/*  47 */   private static final char[] DAY_CHARS = new char[] { 'D', 'd', 'F', 'E' };
/*  48 */   private static final char[] HOUR_CHARS = new char[] { 'H', 'K', 'h', 'k' };
/*     */   
/*     */   private static final char MINUTE_CHAR = 'm';
/*     */   
/*     */   private static final char SECOND_CHAR = 's';
/*     */   private static final char MILLIS_CHAR = 'S';
/*     */   private final ArrayPatternConverter[] patternConverters;
/*     */   private final FormattingInfo[] patternFields;
/*     */   private final FileExtension fileExtension;
/*  57 */   private long prevFileTime = 0L;
/*  58 */   private long nextFileTime = 0L;
/*  59 */   private long currentFileTime = 0L;
/*     */   
/*     */   private boolean isTimeBased = false;
/*     */   
/*  63 */   private RolloverFrequency frequency = null;
/*     */   
/*     */   private final String pattern;
/*     */   
/*     */   public String getPattern() {
/*  68 */     return this.pattern;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  73 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor(String pattern) {
/*  81 */     this.pattern = pattern;
/*  82 */     PatternParser parser = createPatternParser();
/*  83 */     List<PatternConverter> converters = new ArrayList<>();
/*  84 */     List<FormattingInfo> fields = new ArrayList<>();
/*  85 */     parser.parse(pattern, converters, fields, false, false, false);
/*  86 */     FormattingInfo[] infoArray = new FormattingInfo[fields.size()];
/*  87 */     this.patternFields = fields.<FormattingInfo>toArray(infoArray);
/*  88 */     ArrayPatternConverter[] converterArray = new ArrayPatternConverter[converters.size()];
/*  89 */     this.patternConverters = converters.<ArrayPatternConverter>toArray(converterArray);
/*  90 */     this.fileExtension = FileExtension.lookupForFile(pattern);
/*     */     
/*  92 */     for (ArrayPatternConverter converter : this.patternConverters) {
/*  93 */       if (converter instanceof DatePatternConverter) {
/*  94 */         DatePatternConverter dateConverter = (DatePatternConverter)converter;
/*  95 */         this.frequency = calculateFrequency(dateConverter.getPattern());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternProcessor(String pattern, PatternProcessor copy) {
/* 107 */     this(pattern);
/* 108 */     this.prevFileTime = copy.prevFileTime;
/* 109 */     this.nextFileTime = copy.nextFileTime;
/* 110 */     this.currentFileTime = copy.currentFileTime;
/*     */   }
/*     */   
/*     */   public void setTimeBased(boolean isTimeBased) {
/* 114 */     this.isTimeBased = isTimeBased;
/*     */   }
/*     */   
/*     */   public long getCurrentFileTime() {
/* 118 */     return this.currentFileTime;
/*     */   }
/*     */   
/*     */   public void setCurrentFileTime(long currentFileTime) {
/* 122 */     this.currentFileTime = currentFileTime;
/*     */   }
/*     */   
/*     */   public long getPrevFileTime() {
/* 126 */     return this.prevFileTime;
/*     */   }
/*     */   
/*     */   public void setPrevFileTime(long prevFileTime) {
/* 130 */     LOGGER.debug("Setting prev file time to {}", new Date(prevFileTime));
/* 131 */     this.prevFileTime = prevFileTime;
/*     */   }
/*     */   
/*     */   public FileExtension getFileExtension() {
/* 135 */     return this.fileExtension;
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
/*     */   public long getNextTime(long currentMillis, int increment, boolean modulus) {
/* 150 */     this.prevFileTime = this.nextFileTime;
/*     */ 
/*     */     
/* 153 */     if (this.frequency == null) {
/* 154 */       throw new IllegalStateException("Pattern does not contain a date");
/*     */     }
/* 156 */     Calendar currentCal = Calendar.getInstance();
/* 157 */     currentCal.setTimeInMillis(currentMillis);
/* 158 */     Calendar cal = Calendar.getInstance();
/* 159 */     currentCal.setMinimalDaysInFirstWeek(7);
/* 160 */     cal.setMinimalDaysInFirstWeek(7);
/* 161 */     cal.set(currentCal.get(1), 0, 1, 0, 0, 0);
/* 162 */     cal.set(14, 0);
/* 163 */     if (this.frequency == RolloverFrequency.ANNUALLY) {
/* 164 */       increment(cal, 1, increment, modulus);
/* 165 */       long l = cal.getTimeInMillis();
/* 166 */       cal.add(1, -1);
/* 167 */       this.nextFileTime = cal.getTimeInMillis();
/* 168 */       return debugGetNextTime(l);
/*     */     } 
/* 170 */     cal.set(2, currentCal.get(2));
/* 171 */     if (this.frequency == RolloverFrequency.MONTHLY) {
/* 172 */       increment(cal, 2, increment, modulus);
/* 173 */       long l = cal.getTimeInMillis();
/* 174 */       cal.add(2, -1);
/* 175 */       this.nextFileTime = cal.getTimeInMillis();
/* 176 */       return debugGetNextTime(l);
/*     */     } 
/* 178 */     if (this.frequency == RolloverFrequency.WEEKLY) {
/* 179 */       cal.set(3, currentCal.get(3));
/* 180 */       increment(cal, 3, increment, modulus);
/* 181 */       cal.set(7, currentCal.getFirstDayOfWeek());
/* 182 */       long l = cal.getTimeInMillis();
/* 183 */       cal.add(3, -1);
/* 184 */       this.nextFileTime = cal.getTimeInMillis();
/* 185 */       return debugGetNextTime(l);
/*     */     } 
/* 187 */     cal.set(6, currentCal.get(6));
/* 188 */     if (this.frequency == RolloverFrequency.DAILY) {
/* 189 */       increment(cal, 6, increment, modulus);
/* 190 */       long l = cal.getTimeInMillis();
/* 191 */       cal.add(6, -1);
/* 192 */       this.nextFileTime = cal.getTimeInMillis();
/* 193 */       return debugGetNextTime(l);
/*     */     } 
/* 195 */     cal.set(11, currentCal.get(11));
/* 196 */     if (this.frequency == RolloverFrequency.HOURLY) {
/* 197 */       increment(cal, 11, increment, modulus);
/* 198 */       long l = cal.getTimeInMillis();
/* 199 */       cal.add(11, -1);
/* 200 */       this.nextFileTime = cal.getTimeInMillis();
/* 201 */       return debugGetNextTime(l);
/*     */     } 
/* 203 */     cal.set(12, currentCal.get(12));
/* 204 */     if (this.frequency == RolloverFrequency.EVERY_MINUTE) {
/* 205 */       increment(cal, 12, increment, modulus);
/* 206 */       long l = cal.getTimeInMillis();
/* 207 */       cal.add(12, -1);
/* 208 */       this.nextFileTime = cal.getTimeInMillis();
/* 209 */       return debugGetNextTime(l);
/*     */     } 
/* 211 */     cal.set(13, currentCal.get(13));
/* 212 */     if (this.frequency == RolloverFrequency.EVERY_SECOND) {
/* 213 */       increment(cal, 13, increment, modulus);
/* 214 */       long l = cal.getTimeInMillis();
/* 215 */       cal.add(13, -1);
/* 216 */       this.nextFileTime = cal.getTimeInMillis();
/* 217 */       return debugGetNextTime(l);
/*     */     } 
/* 219 */     cal.set(14, currentCal.get(14));
/* 220 */     increment(cal, 14, increment, modulus);
/* 221 */     long nextTime = cal.getTimeInMillis();
/* 222 */     cal.add(14, -1);
/* 223 */     this.nextFileTime = cal.getTimeInMillis();
/* 224 */     return debugGetNextTime(nextTime);
/*     */   }
/*     */   
/*     */   public void updateTime() {
/* 228 */     if (this.nextFileTime != 0L || !this.isTimeBased) {
/* 229 */       this.prevFileTime = this.nextFileTime;
/* 230 */       this.currentFileTime = 0L;
/*     */     } 
/*     */   }
/*     */   
/*     */   private long debugGetNextTime(long nextTime) {
/* 235 */     if (LOGGER.isTraceEnabled()) {
/* 236 */       LOGGER.trace("PatternProcessor.getNextTime returning {}, nextFileTime={}, prevFileTime={}, current={}, freq={}", 
/* 237 */           format(nextTime), format(this.nextFileTime), format(this.prevFileTime), format(System.currentTimeMillis()), this.frequency);
/*     */     }
/* 239 */     return nextTime;
/*     */   }
/*     */   
/*     */   private String format(long time) {
/* 243 */     return (new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS")).format(new Date(time));
/*     */   }
/*     */   
/*     */   private void increment(Calendar cal, int type, int increment, boolean modulate) {
/* 247 */     int interval = modulate ? (increment - cal.get(type) % increment) : increment;
/* 248 */     cal.add(type, interval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void formatFileName(StringBuilder buf, boolean useCurrentTime, Object obj) {
/* 257 */     long time = useCurrentTime ? this.currentFileTime : this.prevFileTime;
/* 258 */     if (time == 0L) {
/* 259 */       time = System.currentTimeMillis();
/*     */     }
/* 261 */     formatFileName(buf, new Object[] { new Date(time), obj });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void formatFileName(StrSubstitutor subst, StringBuilder buf, Object obj) {
/* 271 */     formatFileName(subst, buf, false, obj);
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
/*     */   public final void formatFileName(StrSubstitutor subst, StringBuilder buf, boolean useCurrentTime, Object obj) {
/* 284 */     LOGGER.debug("Formatting file name. useCurrentTime={}. currentFileTime={}, prevFileTime={}", 
/* 285 */         Boolean.valueOf(useCurrentTime), Long.valueOf(this.currentFileTime), Long.valueOf(this.prevFileTime));
/*     */     
/* 287 */     long time = useCurrentTime ? ((this.currentFileTime != 0L) ? this.currentFileTime : System.currentTimeMillis()) : ((this.prevFileTime != 0L) ? this.prevFileTime : System.currentTimeMillis());
/* 288 */     formatFileName(buf, new Object[] { new Date(time), obj });
/* 289 */     Log4jLogEvent log4jLogEvent = (new Log4jLogEvent.Builder()).setTimeMillis(time).build();
/* 290 */     String fileName = subst.replace((LogEvent)log4jLogEvent, buf);
/* 291 */     buf.setLength(0);
/* 292 */     buf.append(fileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void formatFileName(StringBuilder buf, Object... objects) {
/* 301 */     for (int i = 0; i < this.patternConverters.length; i++) {
/* 302 */       int fieldStart = buf.length();
/* 303 */       this.patternConverters[i].format(buf, objects);
/*     */       
/* 305 */       if (this.patternFields[i] != null) {
/* 306 */         this.patternFields[i].format(fieldStart, buf);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private RolloverFrequency calculateFrequency(String pattern) {
/* 312 */     if (patternContains(pattern, 'S')) {
/* 313 */       return RolloverFrequency.EVERY_MILLISECOND;
/*     */     }
/* 315 */     if (patternContains(pattern, 's')) {
/* 316 */       return RolloverFrequency.EVERY_SECOND;
/*     */     }
/* 318 */     if (patternContains(pattern, 'm')) {
/* 319 */       return RolloverFrequency.EVERY_MINUTE;
/*     */     }
/* 321 */     if (patternContains(pattern, HOUR_CHARS)) {
/* 322 */       return RolloverFrequency.HOURLY;
/*     */     }
/* 324 */     if (patternContains(pattern, DAY_CHARS)) {
/* 325 */       return RolloverFrequency.DAILY;
/*     */     }
/* 327 */     if (patternContains(pattern, WEEK_CHARS)) {
/* 328 */       return RolloverFrequency.WEEKLY;
/*     */     }
/* 330 */     if (patternContains(pattern, 'M')) {
/* 331 */       return RolloverFrequency.MONTHLY;
/*     */     }
/* 333 */     if (patternContains(pattern, 'y')) {
/* 334 */       return RolloverFrequency.ANNUALLY;
/*     */     }
/* 336 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private PatternParser createPatternParser() {
/* 341 */     return new PatternParser(null, "FileConverter", null);
/*     */   }
/*     */   
/*     */   private boolean patternContains(String pattern, char... chars) {
/* 345 */     for (char character : chars) {
/* 346 */       if (patternContains(pattern, character)) {
/* 347 */         return true;
/*     */       }
/*     */     } 
/* 350 */     return false;
/*     */   }
/*     */   
/*     */   private boolean patternContains(String pattern, char character) {
/* 354 */     return (pattern.indexOf(character) >= 0);
/*     */   }
/*     */   
/*     */   public RolloverFrequency getFrequency() {
/* 358 */     return this.frequency;
/*     */   }
/*     */   
/*     */   public long getNextFileTime() {
/* 362 */     return this.nextFileTime;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\PatternProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */