/*     */ package org.apache.logging.log4j.core.util.datetime;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.time.Instant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedDateFormat
/*     */ {
/*     */   private static final char NONE = '\000';
/*     */   private final FixedFormat fixedFormat;
/*     */   private final TimeZone timeZone;
/*     */   private final int length;
/*     */   private final int secondFractionDigits;
/*     */   private final FastDateFormat fastDateFormat;
/*     */   private final char timeSeparatorChar;
/*     */   private final char millisSeparatorChar;
/*     */   private final int timeSeparatorLength;
/*     */   private final int millisSeparatorLength;
/*     */   private final FixedTimeZoneFormat fixedTimeZoneFormat;
/*     */   private volatile long midnightToday;
/*     */   private volatile long midnightTomorrow;
/*     */   
/*     */   public enum FixedFormat
/*     */   {
/*  49 */     ABSOLUTE("HH:mm:ss,SSS", null, 0, ':', 1, ',', 1, 3, null),
/*     */ 
/*     */ 
/*     */     
/*  53 */     ABSOLUTE_MICROS("HH:mm:ss,nnnnnn", null, 0, ':', 1, ',', 1, 6, null),
/*     */ 
/*     */ 
/*     */     
/*  57 */     ABSOLUTE_NANOS("HH:mm:ss,nnnnnnnnn", null, 0, ':', 1, ',', 1, 9, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     ABSOLUTE_PERIOD("HH:mm:ss.SSS", null, 0, ':', 1, '.', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     COMPACT("yyyyMMddHHmmssSSS", "yyyyMMdd", 0, ' ', 0, ' ', 0, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     DATE("dd MMM yyyy HH:mm:ss,SSS", "dd MMM yyyy ", 0, ':', 1, ',', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     DATE_PERIOD("dd MMM yyyy HH:mm:ss.SSS", "dd MMM yyyy ", 0, ':', 1, '.', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     DEFAULT("yyyy-MM-dd HH:mm:ss,SSS", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 3, null),
/*     */ 
/*     */ 
/*     */     
/*  86 */     DEFAULT_MICROS("yyyy-MM-dd HH:mm:ss,nnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 6, null),
/*     */ 
/*     */ 
/*     */     
/*  90 */     DEFAULT_NANOS("yyyy-MM-dd HH:mm:ss,nnnnnnnnn", "yyyy-MM-dd ", 0, ':', 1, ',', 1, 9, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     DEFAULT_PERIOD("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd ", 0, ':', 1, '.', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     ISO8601_BASIC("yyyyMMdd'T'HHmmss,SSS", "yyyyMMdd'T'", 2, ' ', 0, ',', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     ISO8601_BASIC_PERIOD("yyyyMMdd'T'HHmmss.SSS", "yyyyMMdd'T'", 2, ' ', 0, '.', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     ISO8601("yyyy-MM-dd'T'HH:mm:ss,SSS", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     ISO8601_OFFSET_DATE_TIME_HH("yyyy-MM-dd'T'HH:mm:ss,SSSX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedDateFormat.FixedTimeZoneFormat.HH),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     ISO8601_OFFSET_DATE_TIME_HHMM("yyyy-MM-dd'T'HH:mm:ss,SSSXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedDateFormat.FixedTimeZoneFormat.HHMM),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     ISO8601_OFFSET_DATE_TIME_HHCMM("yyyy-MM-dd'T'HH:mm:ss,SSSXXX", "yyyy-MM-dd'T'", 2, ':', 1, ',', 1, 3, FixedDateFormat.FixedTimeZoneFormat.HHCMM),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     ISO8601_PERIOD("yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     ISO8601_PERIOD_MICROS("yyyy-MM-dd'T'HH:mm:ss.nnnnnn", "yyyy-MM-dd'T'", 2, ':', 1, '.', 1, 6, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     US_MONTH_DAY_YEAR2_TIME("dd/MM/yy HH:mm:ss.SSS", "dd/MM/yy ", 0, ':', 1, '.', 1, 3, null),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     US_MONTH_DAY_YEAR4_TIME("dd/MM/yyyy HH:mm:ss.SSS", "dd/MM/yyyy ", 0, ':', 1, '.', 1, 3, null);
/*     */     
/*     */     private static final String DEFAULT_SECOND_FRACTION_PATTERN = "SSS";
/* 155 */     private static final int MILLI_FRACTION_DIGITS = "SSS".length();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final char SECOND_FRACTION_PATTERN = 'n';
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String datePattern;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int escapeCount;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final char timeSeparatorChar;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int timeSeparatorLength;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final char millisSeparatorChar;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int millisSeparatorLength;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int secondFractionDigits;
/*     */ 
/*     */ 
/*     */     
/*     */     private final FixedDateFormat.FixedTimeZoneFormat fixedTimeZoneFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     FixedFormat(String pattern, String datePattern, int escapeCount, char timeSeparator, int timeSepLength, char millisSeparator, int millisSepLength, int secondFractionDigits, FixedDateFormat.FixedTimeZoneFormat timeZoneFormat) {
/*     */       this.timeSeparatorChar = timeSeparator;
/*     */       this.timeSeparatorLength = timeSepLength;
/*     */       this.millisSeparatorChar = millisSeparator;
/*     */       this.millisSeparatorLength = millisSepLength;
/*     */       this.pattern = Objects.<String>requireNonNull(pattern);
/*     */       this.datePattern = datePattern;
/*     */       this.escapeCount = escapeCount;
/*     */       this.secondFractionDigits = secondFractionDigits;
/*     */       this.fixedTimeZoneFormat = timeZoneFormat;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     private static final int[] EMPTY_RANGE = new int[] { -1, -1 };
/*     */     public String getPattern() {
/*     */       return this.pattern;
/*     */     }
/*     */     
/*     */     private static int[] nanoRange(String pattern) {
/* 237 */       int indexStart = pattern.indexOf('n');
/* 238 */       int indexEnd = -1;
/* 239 */       if (indexStart >= 0) {
/* 240 */         indexEnd = pattern.indexOf('Z', indexStart);
/* 241 */         indexEnd = (indexEnd < 0) ? pattern.indexOf('X', indexStart) : indexEnd;
/* 242 */         indexEnd = (indexEnd < 0) ? pattern.length() : indexEnd;
/* 243 */         for (int i = indexStart + 1; i < indexEnd; i++) {
/* 244 */           if (pattern.charAt(i) != 'n') {
/* 245 */             return EMPTY_RANGE;
/*     */           }
/*     */         } 
/*     */       } 
/* 249 */       return new int[] { indexStart, indexEnd };
/*     */     }
/*     */     
/*     */     public String getDatePattern() {
/*     */       return this.datePattern;
/*     */     }
/*     */     
/*     */     public int getLength()
/*     */     {
/* 258 */       return this.pattern.length() - this.escapeCount;
/*     */     }
/*     */     public static FixedFormat lookup(String nameOrPattern) {
/*     */       for (FixedFormat type : values()) {
/*     */         if (type.name().equals(nameOrPattern) || type.getPattern().equals(nameOrPattern))
/*     */           return type; 
/*     */       } 
/*     */       return null;
/*     */     }
/* 267 */     public int getDatePatternLength() { return (getDatePattern() == null) ? 0 : (getDatePattern().length() - this.escapeCount); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastDateFormat getFastDateFormat() {
/* 277 */       return getFastDateFormat((TimeZone)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastDateFormat getFastDateFormat(TimeZone tz) {
/* 288 */       return (getDatePattern() == null) ? null : FastDateFormat.getInstance(getDatePattern(), tz);
/*     */     } static FixedFormat lookupIgnoringNanos(String pattern) { int[] nanoRange = nanoRange(pattern); int nanoStart = nanoRange[0]; int nanoEnd = nanoRange[1]; if (nanoStart > 0) {
/*     */         String subPattern = pattern.substring(0, nanoStart) + "SSS" + pattern.substring(nanoEnd, pattern.length());
/*     */         for (FixedFormat type : values()) {
/*     */           if (type.getPattern().equals(subPattern))
/*     */             return type; 
/*     */         } 
/*     */       } 
/* 296 */       return null; } public int getSecondFractionDigits() { return this.secondFractionDigits; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FixedDateFormat.FixedTimeZoneFormat getFixedTimeZoneFormat() {
/* 304 */       return this.fixedTimeZoneFormat;
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
/*     */   public enum FixedTimeZoneFormat
/*     */   {
/* 322 */     HH(false, false, 3),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     HHMM(false, true, 5),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     HHCMM(':', true, 6);
/*     */     private final char timeSeparatorChar;
/*     */     private final int timeSeparatorCharLen;
/*     */     private final boolean useMinutes;
/*     */     private final int length;
/*     */     
/*     */     FixedTimeZoneFormat(char timeSeparatorChar, boolean minutes, int length) {
/* 339 */       this.timeSeparatorChar = timeSeparatorChar;
/* 340 */       this.timeSeparatorCharLen = (timeSeparatorChar != '\000') ? 1 : 0;
/* 341 */       this.useMinutes = minutes;
/* 342 */       this.length = length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getLength() {
/* 352 */       return this.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int write(int offset, char[] buffer, int pos) {
/* 360 */       buffer[pos++] = (offset < 0) ? '-' : '+';
/* 361 */       int absOffset = Math.abs(offset);
/* 362 */       int hours = absOffset / 3600000;
/* 363 */       int ms = absOffset - 3600000 * hours;
/*     */ 
/*     */       
/* 366 */       int temp = hours / 10;
/* 367 */       buffer[pos++] = (char)(temp + 48);
/*     */ 
/*     */       
/* 370 */       buffer[pos++] = (char)(hours - 10 * temp + 48);
/*     */ 
/*     */       
/* 373 */       if (this.useMinutes) {
/* 374 */         buffer[pos] = this.timeSeparatorChar;
/* 375 */         pos += this.timeSeparatorCharLen;
/* 376 */         int minutes = ms / 60000;
/* 377 */         ms -= 60000 * minutes;
/*     */         
/* 379 */         temp = minutes / 10;
/* 380 */         buffer[pos++] = (char)(temp + 48);
/*     */ 
/*     */         
/* 383 */         buffer[pos++] = (char)(minutes - 10 * temp + 48);
/*     */       } 
/* 385 */       return pos;
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
/* 403 */   private final int[] dstOffsets = new int[25];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char[] cachedDate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int dateLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FixedDateFormat(FixedFormat fixedFormat, TimeZone tz) {
/* 423 */     this(fixedFormat, tz, fixedFormat.getSecondFractionDigits());
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
/*     */   FixedDateFormat(FixedFormat fixedFormat, TimeZone tz, int secondFractionDigits) {
/* 437 */     this.fixedFormat = Objects.<FixedFormat>requireNonNull(fixedFormat);
/* 438 */     this.timeZone = Objects.<TimeZone>requireNonNull(tz);
/* 439 */     this.timeSeparatorChar = fixedFormat.timeSeparatorChar;
/* 440 */     this.timeSeparatorLength = fixedFormat.timeSeparatorLength;
/* 441 */     this.millisSeparatorChar = fixedFormat.millisSeparatorChar;
/* 442 */     this.millisSeparatorLength = fixedFormat.millisSeparatorLength;
/* 443 */     this.fixedTimeZoneFormat = fixedFormat.fixedTimeZoneFormat;
/* 444 */     this.length = fixedFormat.getLength();
/* 445 */     this.secondFractionDigits = Math.max(1, Math.min(9, secondFractionDigits));
/* 446 */     this.fastDateFormat = fixedFormat.getFastDateFormat(tz);
/*     */   }
/*     */   public static FixedDateFormat createIfSupported(String... options) {
/*     */     TimeZone tz;
/* 450 */     if (options == null || options.length == 0 || options[0] == null) {
/* 451 */       return new FixedDateFormat(FixedFormat.DEFAULT, TimeZone.getDefault());
/*     */     }
/*     */     
/* 454 */     if (options.length > 1) {
/* 455 */       if (options[1] != null) {
/* 456 */         tz = TimeZone.getTimeZone(options[1]);
/*     */       } else {
/* 458 */         tz = TimeZone.getDefault();
/*     */       } 
/*     */     } else {
/* 461 */       tz = TimeZone.getDefault();
/*     */     } 
/*     */     
/* 464 */     String option0 = options[0];
/* 465 */     FixedFormat withoutNanos = FixedFormat.lookupIgnoringNanos(option0);
/* 466 */     if (withoutNanos != null) {
/* 467 */       int[] nanoRange = FixedFormat.nanoRange(option0);
/* 468 */       int nanoStart = nanoRange[0];
/* 469 */       int nanoEnd = nanoRange[1];
/* 470 */       int secondFractionDigits = nanoEnd - nanoStart;
/* 471 */       return new FixedDateFormat(withoutNanos, tz, secondFractionDigits);
/*     */     } 
/* 473 */     FixedFormat type = FixedFormat.lookup(option0);
/* 474 */     return (type == null) ? null : new FixedDateFormat(type, tz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FixedDateFormat create(FixedFormat format) {
/* 484 */     return new FixedDateFormat(format, TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FixedDateFormat create(FixedFormat format, TimeZone tz) {
/* 495 */     return new FixedDateFormat(format, (tz != null) ? tz : TimeZone.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 504 */     return this.fixedFormat.getPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 513 */     return this.timeZone;
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
/*     */   public long millisSinceMidnight(long currentTime) {
/* 528 */     if (currentTime >= this.midnightTomorrow || currentTime < this.midnightToday) {
/* 529 */       updateMidnightMillis(currentTime);
/*     */     }
/* 531 */     return currentTime - this.midnightToday;
/*     */   }
/*     */   
/*     */   private void updateMidnightMillis(long now) {
/* 535 */     if (now >= this.midnightTomorrow || now < this.midnightToday) {
/* 536 */       synchronized (this) {
/* 537 */         updateCachedDate(now);
/* 538 */         this.midnightToday = calcMidnightMillis(now, 0);
/* 539 */         this.midnightTomorrow = calcMidnightMillis(now, 1);
/*     */         
/* 541 */         updateDaylightSavingTime();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private long calcMidnightMillis(long time, int addDays) {
/* 547 */     Calendar cal = Calendar.getInstance(this.timeZone);
/* 548 */     cal.setTimeInMillis(time);
/* 549 */     cal.set(11, 0);
/* 550 */     cal.set(12, 0);
/* 551 */     cal.set(13, 0);
/* 552 */     cal.set(14, 0);
/* 553 */     cal.add(5, addDays);
/* 554 */     return cal.getTimeInMillis();
/*     */   }
/*     */   
/*     */   private void updateDaylightSavingTime() {
/* 558 */     Arrays.fill(this.dstOffsets, 0);
/* 559 */     int ONE_HOUR = (int)TimeUnit.HOURS.toMillis(1L);
/* 560 */     if (this.timeZone.getOffset(this.midnightToday) != this.timeZone.getOffset(this.midnightToday + (23 * ONE_HOUR))) {
/* 561 */       int i; for (i = 0; i < this.dstOffsets.length; i++) {
/* 562 */         long time = this.midnightToday + (i * ONE_HOUR);
/* 563 */         this.dstOffsets[i] = this.timeZone.getOffset(time) - this.timeZone.getRawOffset();
/*     */       } 
/* 565 */       if (this.dstOffsets[0] > this.dstOffsets[23])
/*     */       {
/* 567 */         for (i = this.dstOffsets.length - 1; i >= 0; i--) {
/* 568 */           this.dstOffsets[i] = this.dstOffsets[i] - this.dstOffsets[0];
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateCachedDate(long now) {
/* 575 */     if (this.fastDateFormat != null) {
/* 576 */       StringBuilder result = this.fastDateFormat.<StringBuilder>format(now, new StringBuilder());
/* 577 */       this.cachedDate = result.toString().toCharArray();
/* 578 */       this.dateLength = result.length();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String formatInstant(Instant instant) {
/* 583 */     char[] result = new char[this.length << 1];
/* 584 */     int written = formatInstant(instant, result, 0);
/* 585 */     return new String(result, 0, written);
/*     */   }
/*     */   
/*     */   public int formatInstant(Instant instant, char[] buffer, int startPos) {
/* 589 */     long epochMillisecond = instant.getEpochMillisecond();
/* 590 */     int result = format(epochMillisecond, buffer, startPos);
/* 591 */     result -= digitsLessThanThree();
/* 592 */     int pos = formatNanoOfMillisecond(instant.getNanoOfMillisecond(), buffer, startPos + result);
/* 593 */     return writeTimeZone(epochMillisecond, buffer, pos);
/*     */   }
/*     */   
/*     */   private int digitsLessThanThree() {
/* 597 */     return Math.max(0, FixedFormat.MILLI_FRACTION_DIGITS - this.secondFractionDigits);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(long epochMillis) {
/* 603 */     char[] result = new char[this.length << 1];
/* 604 */     int written = format(epochMillis, result, 0);
/* 605 */     return new String(result, 0, written);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int format(long epochMillis, char[] buffer, int startPos) {
/* 616 */     int ms = (int)millisSinceMidnight(epochMillis);
/* 617 */     writeDate(buffer, startPos);
/* 618 */     int pos = writeTime(ms, buffer, startPos + this.dateLength);
/* 619 */     return pos - startPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeDate(char[] buffer, int startPos) {
/* 625 */     if (this.cachedDate != null) {
/* 626 */       System.arraycopy(this.cachedDate, 0, buffer, startPos, this.dateLength);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int writeTime(int ms, char[] buffer, int pos) {
/* 633 */     int hourOfDay = ms / 3600000;
/* 634 */     int hours = hourOfDay + daylightSavingTime(hourOfDay) / 3600000;
/* 635 */     ms -= 3600000 * hourOfDay;
/*     */     
/* 637 */     int minutes = ms / 60000;
/* 638 */     ms -= 60000 * minutes;
/*     */     
/* 640 */     int seconds = ms / 1000;
/* 641 */     ms -= 1000 * seconds;
/*     */ 
/*     */     
/* 644 */     int temp = hours / 10;
/* 645 */     buffer[pos++] = (char)(temp + 48);
/*     */ 
/*     */     
/* 648 */     buffer[pos++] = (char)(hours - 10 * temp + 48);
/* 649 */     buffer[pos] = this.timeSeparatorChar;
/* 650 */     pos += this.timeSeparatorLength;
/*     */ 
/*     */     
/* 653 */     temp = minutes / 10;
/* 654 */     buffer[pos++] = (char)(temp + 48);
/*     */ 
/*     */     
/* 657 */     buffer[pos++] = (char)(minutes - 10 * temp + 48);
/* 658 */     buffer[pos] = this.timeSeparatorChar;
/* 659 */     pos += this.timeSeparatorLength;
/*     */ 
/*     */     
/* 662 */     temp = seconds / 10;
/* 663 */     buffer[pos++] = (char)(temp + 48);
/* 664 */     buffer[pos++] = (char)(seconds - 10 * temp + 48);
/* 665 */     buffer[pos] = this.millisSeparatorChar;
/* 666 */     pos += this.millisSeparatorLength;
/*     */ 
/*     */     
/* 669 */     temp = ms / 100;
/* 670 */     buffer[pos++] = (char)(temp + 48);
/*     */     
/* 672 */     ms -= 100 * temp;
/* 673 */     temp = ms / 10;
/* 674 */     buffer[pos++] = (char)(temp + 48);
/*     */     
/* 676 */     ms -= 10 * temp;
/* 677 */     buffer[pos++] = (char)(ms + 48);
/* 678 */     return pos;
/*     */   }
/*     */   
/*     */   private int writeTimeZone(long epochMillis, char[] buffer, int pos) {
/* 682 */     if (this.fixedTimeZoneFormat != null) {
/* 683 */       pos = this.fixedTimeZoneFormat.write(this.timeZone.getOffset(epochMillis), buffer, pos);
/*     */     }
/* 685 */     return pos;
/*     */   }
/*     */   
/* 688 */   static int[] TABLE = new int[] { 100000, 10000, 1000, 100, 10, 1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int formatNanoOfMillisecond(int nanoOfMillisecond, char[] buffer, int pos) {
/* 699 */     int remain = nanoOfMillisecond;
/* 700 */     for (int i = 0; i < this.secondFractionDigits - FixedFormat.MILLI_FRACTION_DIGITS; i++) {
/* 701 */       int divisor = TABLE[i];
/* 702 */       int temp = remain / divisor;
/* 703 */       buffer[pos++] = (char)(temp + 48);
/* 704 */       remain -= divisor * temp;
/*     */     } 
/* 706 */     return pos;
/*     */   }
/*     */   
/*     */   private int daylightSavingTime(int hourOfDay) {
/* 710 */     return (hourOfDay > 23) ? this.dstOffsets[23] : this.dstOffsets[hourOfDay];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEquivalent(long oldEpochSecond, int oldNanoOfSecond, long epochSecond, int nanoOfSecond) {
/* 718 */     if (oldEpochSecond == epochSecond) {
/* 719 */       if (this.secondFractionDigits <= 3)
/*     */       {
/* 721 */         return (oldNanoOfSecond / 1000000L == nanoOfSecond / 1000000L);
/*     */       }
/* 723 */       return (oldNanoOfSecond == nanoOfSecond);
/*     */     } 
/* 725 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\datetime\FixedDateFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */