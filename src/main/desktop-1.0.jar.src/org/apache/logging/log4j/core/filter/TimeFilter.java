/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalTime;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Clock;
/*     */ import org.apache.logging.log4j.core.util.ClockFactory;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "TimeFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class TimeFilter
/*     */   extends AbstractFilter
/*     */ {
/*  47 */   private static final Clock CLOCK = ClockFactory.getClock();
/*  48 */   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long HOUR_MS = 3600000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long DAY_MS = 86400000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long start;
/*     */ 
/*     */ 
/*     */   
/*     */   private final LocalTime startTime;
/*     */ 
/*     */   
/*     */   private volatile long end;
/*     */ 
/*     */   
/*     */   private final LocalTime endTime;
/*     */ 
/*     */   
/*     */   private final long duration;
/*     */ 
/*     */   
/*     */   private final ZoneId timeZone;
/*     */ 
/*     */ 
/*     */   
/*     */   TimeFilter(LocalTime start, LocalTime end, ZoneId timeZone, Filter.Result onMatch, Filter.Result onMismatch, LocalDate now) {
/*  81 */     super(onMatch, onMismatch);
/*  82 */     this.startTime = start;
/*  83 */     this.endTime = end;
/*  84 */     this.timeZone = timeZone;
/*  85 */     this.start = ZonedDateTime.of(now, this.startTime, timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
/*  86 */     long endMillis = ZonedDateTime.of(now, this.endTime, timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
/*  87 */     if (end.isBefore(start))
/*     */     {
/*  89 */       endMillis += 86400000L;
/*     */     }
/*  91 */     this
/*  92 */       .duration = this.startTime.isBefore(this.endTime) ? Duration.between(this.startTime, this.endTime).toMillis() : Duration.between(this.startTime, this.endTime).plusHours(24L).toMillis();
/*  93 */     long difference = endMillis - this.start - this.duration;
/*  94 */     if (difference != 0L)
/*     */     {
/*  96 */       endMillis -= difference;
/*     */     }
/*  98 */     this.end = endMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   private TimeFilter(LocalTime start, LocalTime end, ZoneId timeZone, Filter.Result onMatch, Filter.Result onMismatch) {
/* 103 */     this(start, end, timeZone, onMatch, onMismatch, LocalDate.now(timeZone));
/*     */   }
/*     */   
/*     */   private synchronized void adjustTimes(long currentTimeMillis) {
/* 107 */     if (currentTimeMillis <= this.end) {
/*     */       return;
/*     */     }
/* 110 */     LocalDate date = Instant.ofEpochMilli(currentTimeMillis).atZone(this.timeZone).toLocalDate();
/* 111 */     this.start = ZonedDateTime.of(date, this.startTime, this.timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
/* 112 */     long endMillis = ZonedDateTime.of(date, this.endTime, this.timeZone).withEarlierOffsetAtOverlap().toInstant().toEpochMilli();
/* 113 */     if (this.endTime.isBefore(this.startTime))
/*     */     {
/* 115 */       endMillis += 86400000L;
/*     */     }
/* 117 */     long difference = endMillis - this.start - this.duration;
/* 118 */     if (difference != 0L)
/*     */     {
/* 120 */       endMillis -= difference;
/*     */     }
/* 122 */     this.end = endMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Filter.Result filter(long currentTimeMillis) {
/* 133 */     if (currentTimeMillis > this.end) {
/* 134 */       adjustTimes(currentTimeMillis);
/*     */     }
/* 136 */     return (currentTimeMillis >= this.start && currentTimeMillis <= this.end) ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/* 141 */     return filter(event.getTimeMillis());
/*     */   }
/*     */   
/*     */   private Filter.Result filter() {
/* 145 */     return filter(CLOCK.currentTimeMillis());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/* 151 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/* 157 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/* 163 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
/* 169 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
/* 175 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
/* 181 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
/* 187 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 193 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 199 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 206 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 213 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 220 */     return filter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 227 */     return filter();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 232 */     StringBuilder sb = new StringBuilder();
/* 233 */     sb.append("start=").append(this.start);
/* 234 */     sb.append(", end=").append(this.end);
/* 235 */     sb.append(", timezone=").append(this.timeZone.toString());
/* 236 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static TimeFilter createFilter(@PluginAttribute("start") String start, @PluginAttribute("end") String end, @PluginAttribute("timezone") String tz, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) {
/* 256 */     LocalTime startTime = parseTimestamp(start, LocalTime.MIN);
/* 257 */     LocalTime endTime = parseTimestamp(end, LocalTime.MAX);
/* 258 */     ZoneId timeZone = (tz == null) ? ZoneId.systemDefault() : ZoneId.of(tz);
/* 259 */     Filter.Result onMatch = (match == null) ? Filter.Result.NEUTRAL : match;
/* 260 */     Filter.Result onMismatch = (mismatch == null) ? Filter.Result.DENY : mismatch;
/* 261 */     return new TimeFilter(startTime, endTime, timeZone, onMatch, onMismatch);
/*     */   }
/*     */   
/*     */   private static LocalTime parseTimestamp(String timestamp, LocalTime defaultValue) {
/* 265 */     if (timestamp == null) {
/* 266 */       return defaultValue;
/*     */     }
/*     */     
/*     */     try {
/* 270 */       return LocalTime.parse(timestamp, FORMATTER);
/* 271 */     } catch (Exception e) {
/* 272 */       LOGGER.warn("Error parsing TimeFilter timestamp value {}", timestamp, e);
/* 273 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\filter\TimeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */