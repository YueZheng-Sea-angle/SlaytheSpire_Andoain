/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.net.ftp.Configurable;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTPTimestampParserImpl
/*     */   implements FTPTimestampParser, Configurable
/*     */ {
/*     */   private SimpleDateFormat defaultDateFormat;
/*     */   private int defaultDateSmallestUnitIndex;
/*     */   private SimpleDateFormat recentDateFormat;
/*     */   private int recentDateSmallestUnitIndex;
/*     */   private boolean lenientFutureDates = false;
/*  68 */   private static final int[] CALENDAR_UNITS = new int[] { 14, 13, 12, 11, 5, 2, 1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getEntry(SimpleDateFormat dateFormat) {
/*  83 */     if (dateFormat == null) {
/*  84 */       return 0;
/*     */     }
/*  86 */     String FORMAT_CHARS = "SsmHdM";
/*  87 */     String pattern = dateFormat.toPattern();
/*  88 */     for (char ch : "SsmHdM".toCharArray()) {
/*  89 */       if (pattern.indexOf(ch) != -1) {
/*  90 */         switch (ch) {
/*     */           case 'S':
/*  92 */             return indexOf(14);
/*     */           case 's':
/*  94 */             return indexOf(13);
/*     */           case 'm':
/*  96 */             return indexOf(12);
/*     */           case 'H':
/*  98 */             return indexOf(11);
/*     */           case 'd':
/* 100 */             return indexOf(5);
/*     */           case 'M':
/* 102 */             return indexOf(2);
/*     */         } 
/*     */       }
/*     */     } 
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int indexOf(int calendarUnit) {
/* 114 */     for (int i = 0; i < CALENDAR_UNITS.length; i++) {
/* 115 */       if (calendarUnit == CALENDAR_UNITS[i]) {
/* 116 */         return i;
/*     */       }
/*     */     } 
/* 119 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setPrecision(int index, Calendar working) {
/* 128 */     if (index <= 0) {
/*     */       return;
/*     */     }
/* 131 */     int field = CALENDAR_UNITS[index - 1];
/*     */ 
/*     */     
/* 134 */     int value = working.get(field);
/* 135 */     if (value == 0)
/*     */     {
/*     */       
/* 138 */       working.clear(field);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPTimestampParserImpl() {
/* 146 */     setDefaultDateFormat("MMM d yyyy", null);
/* 147 */     setRecentDateFormat("MMM d HH:mm", null);
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
/*     */   public Calendar parseTimestamp(String timestampStr) throws ParseException {
/* 169 */     Calendar now = Calendar.getInstance();
/* 170 */     return parseTimestamp(timestampStr, now);
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
/*     */   public Calendar parseTimestamp(String timestampStr, Calendar serverTime) throws ParseException {
/* 190 */     Calendar working = (Calendar)serverTime.clone();
/* 191 */     working.setTimeZone(getServerTimeZone());
/*     */     
/* 193 */     Date parsed = null;
/*     */     
/* 195 */     if (this.recentDateFormat != null) {
/* 196 */       Calendar now = (Calendar)serverTime.clone();
/* 197 */       now.setTimeZone(getServerTimeZone());
/* 198 */       if (this.lenientFutureDates)
/*     */       {
/*     */         
/* 201 */         now.add(5, 1);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 212 */       String year = Integer.toString(now.get(1));
/* 213 */       String timeStampStrPlusYear = timestampStr + " " + year;
/* 214 */       SimpleDateFormat hackFormatter = new SimpleDateFormat(this.recentDateFormat.toPattern() + " yyyy", this.recentDateFormat.getDateFormatSymbols());
/*     */       
/* 216 */       hackFormatter.setLenient(false);
/* 217 */       hackFormatter.setTimeZone(this.recentDateFormat.getTimeZone());
/* 218 */       ParsePosition parsePosition = new ParsePosition(0);
/* 219 */       parsed = hackFormatter.parse(timeStampStrPlusYear, parsePosition);
/*     */       
/* 221 */       if (parsed != null && parsePosition.getIndex() == timeStampStrPlusYear.length()) {
/* 222 */         working.setTime(parsed);
/* 223 */         if (working.after(now)) {
/* 224 */           working.add(1, -1);
/*     */         }
/* 226 */         setPrecision(this.recentDateSmallestUnitIndex, working);
/* 227 */         return working;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     ParsePosition pp = new ParsePosition(0);
/* 232 */     parsed = this.defaultDateFormat.parse(timestampStr, pp);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (parsed != null && pp.getIndex() == timestampStr.length()) {
/* 241 */       working.setTime(parsed);
/*     */     } else {
/* 243 */       throw new ParseException("Timestamp '" + timestampStr + "' could not be parsed using a server time of " + serverTime.getTime().toString(), pp.getErrorIndex());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 248 */     setPrecision(this.defaultDateSmallestUnitIndex, working);
/* 249 */     return working;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleDateFormat getDefaultDateFormat() {
/* 256 */     return this.defaultDateFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultDateFormatString() {
/* 262 */     return this.defaultDateFormat.toPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDefaultDateFormat(String format, DateFormatSymbols dfs) {
/* 269 */     if (format != null) {
/* 270 */       if (dfs != null) {
/* 271 */         this.defaultDateFormat = new SimpleDateFormat(format, dfs);
/*     */       } else {
/* 273 */         this.defaultDateFormat = new SimpleDateFormat(format);
/*     */       } 
/* 275 */       this.defaultDateFormat.setLenient(false);
/*     */     } else {
/* 277 */       this.defaultDateFormat = null;
/*     */     } 
/* 279 */     this.defaultDateSmallestUnitIndex = getEntry(this.defaultDateFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleDateFormat getRecentDateFormat() {
/* 285 */     return this.recentDateFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRecentDateFormatString() {
/* 291 */     return this.recentDateFormat.toPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRecentDateFormat(String format, DateFormatSymbols dfs) {
/* 298 */     if (format != null) {
/* 299 */       if (dfs != null) {
/* 300 */         this.recentDateFormat = new SimpleDateFormat(format, dfs);
/*     */       } else {
/* 302 */         this.recentDateFormat = new SimpleDateFormat(format);
/*     */       } 
/* 304 */       this.recentDateFormat.setLenient(false);
/*     */     } else {
/* 306 */       this.recentDateFormat = null;
/*     */     } 
/* 308 */     this.recentDateSmallestUnitIndex = getEntry(this.recentDateFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getShortMonths() {
/* 316 */     return this.defaultDateFormat.getDateFormatSymbols().getShortMonths();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getServerTimeZone() {
/* 324 */     return this.defaultDateFormat.getTimeZone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setServerTimeZone(String serverTimeZoneId) {
/* 333 */     TimeZone serverTimeZone = TimeZone.getDefault();
/* 334 */     if (serverTimeZoneId != null) {
/* 335 */       serverTimeZone = TimeZone.getTimeZone(serverTimeZoneId);
/*     */     }
/* 337 */     this.defaultDateFormat.setTimeZone(serverTimeZone);
/* 338 */     if (this.recentDateFormat != null) {
/* 339 */       this.recentDateFormat.setTimeZone(serverTimeZone);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(FTPClientConfig config) {
/* 368 */     DateFormatSymbols dfs = null;
/*     */     
/* 370 */     String languageCode = config.getServerLanguageCode();
/* 371 */     String shortmonths = config.getShortMonthNames();
/* 372 */     if (shortmonths != null) {
/* 373 */       dfs = FTPClientConfig.getDateFormatSymbols(shortmonths);
/* 374 */     } else if (languageCode != null) {
/* 375 */       dfs = FTPClientConfig.lookupDateFormatSymbols(languageCode);
/*     */     } else {
/* 377 */       dfs = FTPClientConfig.lookupDateFormatSymbols("en");
/*     */     } 
/*     */ 
/*     */     
/* 381 */     String recentFormatString = config.getRecentDateFormatStr();
/* 382 */     setRecentDateFormat(recentFormatString, dfs);
/*     */     
/* 384 */     String defaultFormatString = config.getDefaultDateFormatStr();
/* 385 */     if (defaultFormatString == null) {
/* 386 */       throw new IllegalArgumentException("defaultFormatString cannot be null");
/*     */     }
/* 388 */     setDefaultDateFormat(defaultFormatString, dfs);
/*     */     
/* 390 */     setServerTimeZone(config.getServerTimeZoneId());
/*     */     
/* 392 */     this.lenientFutureDates = config.isLenientFutureDates();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isLenientFutureDates() {
/* 398 */     return this.lenientFutureDates;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setLenientFutureDates(boolean lenientFutureDates) {
/* 404 */     this.lenientFutureDates = lenientFutureDates;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\parser\FTPTimestampParserImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */