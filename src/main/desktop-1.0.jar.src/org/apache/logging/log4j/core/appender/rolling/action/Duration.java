/*     */ package org.apache.logging.log4j.core.appender.rolling.action;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Duration
/*     */   implements Serializable, Comparable<Duration>
/*     */ {
/*     */   private static final long serialVersionUID = -3756810052716342061L;
/*  41 */   public static final Duration ZERO = new Duration(0L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int HOURS_PER_DAY = 24;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MINUTES_PER_HOUR = 60;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SECONDS_PER_MINUTE = 60;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SECONDS_PER_HOUR = 3600;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SECONDS_PER_DAY = 86400;
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static final Pattern PATTERN = Pattern.compile("P?(?:([0-9]+)D)?(T?(?:([0-9]+)H)?(?:([0-9]+)M)?(?:([0-9]+)?S)?)?", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long seconds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Duration(long seconds) {
/*  81 */     this.seconds = seconds;
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
/*     */   public static Duration parse(CharSequence text) {
/* 117 */     Objects.requireNonNull(text, "text");
/* 118 */     Matcher matcher = PATTERN.matcher(text);
/* 119 */     if (matcher.matches())
/*     */     {
/* 121 */       if (!"T".equals(matcher.group(2))) {
/* 122 */         String dayMatch = matcher.group(1);
/* 123 */         String hourMatch = matcher.group(3);
/* 124 */         String minuteMatch = matcher.group(4);
/* 125 */         String secondMatch = matcher.group(5);
/* 126 */         if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
/* 127 */           long daysAsSecs = parseNumber(text, dayMatch, 86400, "days");
/* 128 */           long hoursAsSecs = parseNumber(text, hourMatch, 3600, "hours");
/* 129 */           long minsAsSecs = parseNumber(text, minuteMatch, 60, "minutes");
/* 130 */           long seconds = parseNumber(text, secondMatch, 1, "seconds");
/*     */           try {
/* 132 */             return create(daysAsSecs, hoursAsSecs, minsAsSecs, seconds);
/* 133 */           } catch (ArithmeticException ex) {
/* 134 */             throw new IllegalArgumentException("Text cannot be parsed to a Duration (overflow) " + text, ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 139 */     throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseNumber(CharSequence text, String parsed, int multiplier, String errorText) {
/* 145 */     if (parsed == null) {
/* 146 */       return 0L;
/*     */     }
/*     */     try {
/* 149 */       long val = Long.parseLong(parsed);
/* 150 */       return val * multiplier;
/* 151 */     } catch (Exception ex) {
/* 152 */       throw new IllegalArgumentException("Text cannot be parsed to a Duration: " + errorText + " (in " + text + ")", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Duration create(long daysAsSecs, long hoursAsSecs, long minsAsSecs, long secs) {
/* 158 */     return create(daysAsSecs + hoursAsSecs + minsAsSecs + secs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Duration create(long seconds) {
/* 167 */     if (seconds == 0L) {
/* 168 */       return ZERO;
/*     */     }
/* 170 */     return new Duration(seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long toMillis() {
/* 179 */     return this.seconds * 1000L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 184 */     if (obj == this) {
/* 185 */       return true;
/*     */     }
/* 187 */     if (!(obj instanceof Duration)) {
/* 188 */       return false;
/*     */     }
/* 190 */     Duration other = (Duration)obj;
/* 191 */     return (other.seconds == this.seconds);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 196 */     return (int)(this.seconds ^ this.seconds >>> 32L);
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
/*     */   public String toString() {
/* 219 */     if (this == ZERO) {
/* 220 */       return "PT0S";
/*     */     }
/* 222 */     long days = this.seconds / 86400L;
/* 223 */     long hours = this.seconds % 86400L / 3600L;
/* 224 */     int minutes = (int)(this.seconds % 3600L / 60L);
/* 225 */     int secs = (int)(this.seconds % 60L);
/* 226 */     StringBuilder buf = new StringBuilder(24);
/* 227 */     buf.append("P");
/* 228 */     if (days != 0L) {
/* 229 */       buf.append(days).append('D');
/*     */     }
/* 231 */     if ((hours | minutes | secs) != 0L) {
/* 232 */       buf.append('T');
/*     */     }
/* 234 */     if (hours != 0L) {
/* 235 */       buf.append(hours).append('H');
/*     */     }
/* 237 */     if (minutes != 0) {
/* 238 */       buf.append(minutes).append('M');
/*     */     }
/* 240 */     if (secs == 0 && buf.length() > 0) {
/* 241 */       return buf.toString();
/*     */     }
/* 243 */     buf.append(secs).append('S');
/* 244 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Duration other) {
/* 254 */     return Long.signum(toMillis() - other.toMillis());
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\rolling\action\Duration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */