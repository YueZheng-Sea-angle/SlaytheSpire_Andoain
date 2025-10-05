/*     */ package org.apache.commons.net.ntp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimeStamp
/*     */   implements Serializable, Comparable<TimeStamp>
/*     */ {
/*     */   private static final long serialVersionUID = 8139806907588338737L;
/*     */   protected static final long msb0baseTime = 2085978496000L;
/*     */   protected static final long msb1baseTime = -2208988800000L;
/*     */   public static final String NTP_DATE_FORMAT = "EEE, MMM dd yyyy HH:mm:ss.SSS";
/*     */   private final long ntpTime;
/*     */   private DateFormat simpleFormatter;
/*     */   private DateFormat utcFormatter;
/*     */   
/*     */   public TimeStamp(long ntpTime) {
/*  95 */     this.ntpTime = ntpTime;
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
/*     */   public TimeStamp(String hexStamp) throws NumberFormatException {
/* 108 */     this.ntpTime = decodeNtpHexString(hexStamp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeStamp(Date d) {
/* 119 */     this.ntpTime = (d == null) ? 0L : toNtpTime(d.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long ntpValue() {
/* 129 */     return this.ntpTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSeconds() {
/* 139 */     return this.ntpTime >>> 32L & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFraction() {
/* 149 */     return this.ntpTime & 0xFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTime() {
/* 159 */     return getTime(this.ntpTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate() {
/* 169 */     long time = getTime(this.ntpTime);
/* 170 */     return new Date(time);
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
/*     */   public static long getTime(long ntpTimeValue) {
/* 188 */     long seconds = ntpTimeValue >>> 32L & 0xFFFFFFFFL;
/* 189 */     long fraction = ntpTimeValue & 0xFFFFFFFFL;
/*     */ 
/*     */     
/* 192 */     fraction = Math.round(1000.0D * fraction / 4.294967296E9D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     long msb = seconds & 0x80000000L;
/* 204 */     if (msb == 0L)
/*     */     {
/* 206 */       return 2085978496000L + seconds * 1000L + fraction;
/*     */     }
/*     */     
/* 209 */     return -2208988800000L + seconds * 1000L + fraction;
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
/*     */   public static TimeStamp getNtpTime(long date) {
/* 225 */     return new TimeStamp(toNtpTime(date));
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
/*     */   public static TimeStamp getCurrentTime() {
/* 237 */     return getNtpTime(System.currentTimeMillis());
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
/*     */   protected static long decodeNtpHexString(String hexString) throws NumberFormatException {
/* 251 */     if (hexString == null) {
/* 252 */       throw new NumberFormatException("null");
/*     */     }
/* 254 */     int ind = hexString.indexOf('.');
/* 255 */     if (ind == -1) {
/* 256 */       if (hexString.length() == 0) {
/* 257 */         return 0L;
/*     */       }
/* 259 */       return Long.parseLong(hexString, 16) << 32L;
/*     */     } 
/*     */     
/* 262 */     return Long.parseLong(hexString.substring(0, ind), 16) << 32L | Long.parseLong(hexString.substring(ind + 1), 16);
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
/*     */   public static TimeStamp parseNtpString(String s) throws NumberFormatException {
/* 277 */     return new TimeStamp(decodeNtpHexString(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static long toNtpTime(long t) {
/*     */     long baseTime;
/* 288 */     boolean useBase1 = (t < 2085978496000L);
/*     */     
/* 290 */     if (useBase1) {
/* 291 */       baseTime = t - -2208988800000L;
/*     */     } else {
/*     */       
/* 294 */       baseTime = t - 2085978496000L;
/*     */     } 
/*     */     
/* 297 */     long seconds = baseTime / 1000L;
/* 298 */     long fraction = baseTime % 1000L * 4294967296L / 1000L;
/*     */     
/* 300 */     if (useBase1) {
/* 301 */       seconds |= 0x80000000L;
/*     */     }
/*     */     
/* 304 */     long time = seconds << 32L | fraction;
/* 305 */     return time;
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
/*     */   public int hashCode() {
/* 322 */     return (int)(this.ntpTime ^ this.ntpTime >>> 32L);
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
/*     */   public boolean equals(Object obj) {
/* 338 */     if (obj instanceof TimeStamp) {
/* 339 */       return (this.ntpTime == ((TimeStamp)obj).ntpValue());
/*     */     }
/* 341 */     return false;
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
/*     */   public String toString() {
/* 356 */     return toString(this.ntpTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendHexString(StringBuilder buf, long l) {
/* 367 */     String s = Long.toHexString(l);
/* 368 */     for (int i = s.length(); i < 8; i++) {
/* 369 */       buf.append('0');
/*     */     }
/* 371 */     buf.append(s);
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
/*     */   public static String toString(long ntpTime) {
/* 386 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 388 */     appendHexString(buf, ntpTime >>> 32L & 0xFFFFFFFFL);
/*     */ 
/*     */     
/* 391 */     buf.append('.');
/* 392 */     appendHexString(buf, ntpTime & 0xFFFFFFFFL);
/*     */     
/* 394 */     return buf.toString();
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
/*     */   public String toDateString() {
/* 408 */     if (this.simpleFormatter == null) {
/* 409 */       this.simpleFormatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS", Locale.US);
/* 410 */       this.simpleFormatter.setTimeZone(TimeZone.getDefault());
/*     */     } 
/* 412 */     Date ntpDate = getDate();
/* 413 */     return this.simpleFormatter.format(ntpDate);
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
/*     */   public String toUTCString() {
/* 427 */     if (this.utcFormatter == null) {
/* 428 */       this.utcFormatter = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss.SSS 'UTC'", Locale.US);
/*     */       
/* 430 */       this.utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */     } 
/* 432 */     Date ntpDate = getDate();
/* 433 */     return this.utcFormatter.format(ntpDate);
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
/*     */   public int compareTo(TimeStamp anotherTimeStamp) {
/* 450 */     long thisVal = this.ntpTime;
/* 451 */     long anotherVal = anotherTimeStamp.ntpTime;
/* 452 */     return (thisVal < anotherVal) ? -1 : ((thisVal == anotherVal) ? 0 : 1);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ntp\TimeStamp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */