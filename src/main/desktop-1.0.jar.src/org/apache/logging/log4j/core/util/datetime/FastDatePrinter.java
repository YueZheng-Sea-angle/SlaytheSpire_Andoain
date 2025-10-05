/*      */ package org.apache.logging.log4j.core.util.datetime;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.FieldPosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.apache.logging.log4j.core.util.Throwables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FastDatePrinter
/*      */   implements DatePrinter, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   public static final int FULL = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int MEDIUM = 2;
/*      */   public static final int SHORT = 3;
/*      */   private final String mPattern;
/*      */   private final TimeZone mTimeZone;
/*      */   private final Locale mLocale;
/*      */   private transient Rule[] mRules;
/*      */   private transient int mMaxLengthEstimate;
/*      */   private static final int MAX_DIGITS = 10;
/*      */   
/*      */   protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
/*  155 */     this.mPattern = pattern;
/*  156 */     this.mTimeZone = timeZone;
/*  157 */     this.mLocale = locale;
/*      */     
/*  159 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init() {
/*  166 */     List<Rule> rulesList = parsePattern();
/*  167 */     this.mRules = rulesList.<Rule>toArray(new Rule[rulesList.size()]);
/*      */     
/*  169 */     int len = 0;
/*  170 */     for (int i = this.mRules.length; --i >= 0;) {
/*  171 */       len += this.mRules[i].estimateLength();
/*      */     }
/*      */     
/*  174 */     this.mMaxLengthEstimate = len;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Rule> parsePattern() {
/*  186 */     DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
/*  187 */     List<Rule> rules = new ArrayList<>();
/*      */     
/*  189 */     String[] ERAs = symbols.getEras();
/*  190 */     String[] months = symbols.getMonths();
/*  191 */     String[] shortMonths = symbols.getShortMonths();
/*  192 */     String[] weekdays = symbols.getWeekdays();
/*  193 */     String[] shortWeekdays = symbols.getShortWeekdays();
/*  194 */     String[] AmPmStrings = symbols.getAmPmStrings();
/*      */     
/*  196 */     int length = this.mPattern.length();
/*  197 */     int[] indexRef = new int[1];
/*      */     
/*  199 */     for (int i = 0; i < length; i++) {
/*  200 */       Rule rule; String sub; indexRef[0] = i;
/*  201 */       String token = parseToken(this.mPattern, indexRef);
/*  202 */       i = indexRef[0];
/*      */       
/*  204 */       int tokenLen = token.length();
/*  205 */       if (tokenLen == 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  210 */       char c = token.charAt(0);
/*      */       
/*  212 */       switch (c) {
/*      */         case 'G':
/*  214 */           rule = new TextField(0, ERAs);
/*      */           break;
/*      */         case 'Y':
/*      */         case 'y':
/*  218 */           if (tokenLen == 2) {
/*  219 */             rule = TwoDigitYearField.INSTANCE;
/*      */           } else {
/*  221 */             rule = selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
/*      */           } 
/*  223 */           if (c == 'Y') {
/*  224 */             rule = new WeekYear((NumberRule)rule);
/*      */           }
/*      */           break;
/*      */         case 'M':
/*  228 */           if (tokenLen >= 4) {
/*  229 */             rule = new TextField(2, months); break;
/*  230 */           }  if (tokenLen == 3) {
/*  231 */             rule = new TextField(2, shortMonths); break;
/*  232 */           }  if (tokenLen == 2) {
/*  233 */             rule = TwoDigitMonthField.INSTANCE; break;
/*      */           } 
/*  235 */           rule = UnpaddedMonthField.INSTANCE;
/*      */           break;
/*      */         
/*      */         case 'd':
/*  239 */           rule = selectNumberRule(5, tokenLen);
/*      */           break;
/*      */         case 'h':
/*  242 */           rule = new TwelveHourField(selectNumberRule(10, tokenLen));
/*      */           break;
/*      */         case 'H':
/*  245 */           rule = selectNumberRule(11, tokenLen);
/*      */           break;
/*      */         case 'm':
/*  248 */           rule = selectNumberRule(12, tokenLen);
/*      */           break;
/*      */         case 's':
/*  251 */           rule = selectNumberRule(13, tokenLen);
/*      */           break;
/*      */         case 'S':
/*  254 */           rule = selectNumberRule(14, tokenLen);
/*      */           break;
/*      */         case 'E':
/*  257 */           rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
/*      */           break;
/*      */         case 'u':
/*  260 */           rule = new DayInWeekField(selectNumberRule(7, tokenLen));
/*      */           break;
/*      */         case 'D':
/*  263 */           rule = selectNumberRule(6, tokenLen);
/*      */           break;
/*      */         case 'F':
/*  266 */           rule = selectNumberRule(8, tokenLen);
/*      */           break;
/*      */         case 'w':
/*  269 */           rule = selectNumberRule(3, tokenLen);
/*      */           break;
/*      */         case 'W':
/*  272 */           rule = selectNumberRule(4, tokenLen);
/*      */           break;
/*      */         case 'a':
/*  275 */           rule = new TextField(9, AmPmStrings);
/*      */           break;
/*      */         case 'k':
/*  278 */           rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
/*      */           break;
/*      */         case 'K':
/*  281 */           rule = selectNumberRule(10, tokenLen);
/*      */           break;
/*      */         case 'X':
/*  284 */           rule = Iso8601_Rule.getRule(tokenLen);
/*      */           break;
/*      */         case 'z':
/*  287 */           if (tokenLen >= 4) {
/*  288 */             rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1); break;
/*      */           } 
/*  290 */           rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
/*      */           break;
/*      */         
/*      */         case 'Z':
/*  294 */           if (tokenLen == 1) {
/*  295 */             rule = TimeZoneNumberRule.INSTANCE_NO_COLON; break;
/*  296 */           }  if (tokenLen == 2) {
/*  297 */             rule = Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES; break;
/*      */           } 
/*  299 */           rule = TimeZoneNumberRule.INSTANCE_COLON;
/*      */           break;
/*      */         
/*      */         case '\'':
/*  303 */           sub = token.substring(1);
/*  304 */           if (sub.length() == 1) {
/*  305 */             rule = new CharacterLiteral(sub.charAt(0)); break;
/*      */           } 
/*  307 */           rule = new StringLiteral(sub);
/*      */           break;
/*      */         
/*      */         default:
/*  311 */           throw new IllegalArgumentException("Illegal pattern component: " + token);
/*      */       } 
/*      */       
/*  314 */       rules.add(rule);
/*      */     } 
/*      */     
/*  317 */     return rules;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String parseToken(String pattern, int[] indexRef) {
/*  328 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  330 */     int i = indexRef[0];
/*  331 */     int length = pattern.length();
/*      */     
/*  333 */     char c = pattern.charAt(i);
/*  334 */     if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
/*      */ 
/*      */       
/*  337 */       buf.append(c);
/*      */       
/*  339 */       while (i + 1 < length) {
/*  340 */         char peek = pattern.charAt(i + 1);
/*  341 */         if (peek == c) {
/*  342 */           buf.append(c);
/*  343 */           i++;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  350 */       buf.append('\'');
/*      */       
/*  352 */       boolean inLiteral = false;
/*      */       
/*  354 */       for (; i < length; i++) {
/*  355 */         c = pattern.charAt(i);
/*      */         
/*  357 */         if (c == '\'')
/*  358 */         { if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
/*      */             
/*  360 */             i++;
/*  361 */             buf.append(c);
/*      */           } else {
/*  363 */             inLiteral = !inLiteral;
/*      */           }  }
/*  365 */         else { if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
/*      */             
/*  367 */             i--;
/*      */             break;
/*      */           } 
/*  370 */           buf.append(c); }
/*      */       
/*      */       } 
/*      */     } 
/*      */     
/*  375 */     indexRef[0] = i;
/*  376 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NumberRule selectNumberRule(int field, int padding) {
/*  387 */     switch (padding) {
/*      */       case 1:
/*  389 */         return new UnpaddedNumberField(field);
/*      */       case 2:
/*  391 */         return new TwoDigitNumberField(field);
/*      */     } 
/*  393 */     return new PaddedNumberField(field, padding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public StringBuilder format(Object obj, StringBuilder toAppendTo, FieldPosition pos) {
/*  411 */     if (obj instanceof Date)
/*  412 */       return format((Date)obj, toAppendTo); 
/*  413 */     if (obj instanceof Calendar)
/*  414 */       return format((Calendar)obj, toAppendTo); 
/*  415 */     if (obj instanceof Long) {
/*  416 */       return format(((Long)obj).longValue(), toAppendTo);
/*      */     }
/*  418 */     throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj
/*  419 */         .getClass().getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String format(Object obj) {
/*  431 */     if (obj instanceof Date)
/*  432 */       return format((Date)obj); 
/*  433 */     if (obj instanceof Calendar)
/*  434 */       return format((Calendar)obj); 
/*  435 */     if (obj instanceof Long) {
/*  436 */       return format(((Long)obj).longValue());
/*      */     }
/*  438 */     throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj
/*  439 */         .getClass().getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(long millis) {
/*  448 */     Calendar c = newCalendar();
/*  449 */     c.setTimeInMillis(millis);
/*  450 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String applyRulesToString(Calendar c) {
/*  459 */     return ((StringBuilder)applyRules(c, new StringBuilder(this.mMaxLengthEstimate))).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Calendar newCalendar() {
/*  467 */     return Calendar.getInstance(this.mTimeZone, this.mLocale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Date date) {
/*  475 */     Calendar c = newCalendar();
/*  476 */     c.setTime(date);
/*  477 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Calendar calendar) {
/*  485 */     return ((StringBuilder)format(calendar, new StringBuilder(this.mMaxLengthEstimate))).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <B extends Appendable> B format(long millis, B buf) {
/*  493 */     Calendar c = newCalendar();
/*  494 */     c.setTimeInMillis(millis);
/*  495 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <B extends Appendable> B format(Date date, B buf) {
/*  503 */     Calendar c = newCalendar();
/*  504 */     c.setTime(date);
/*  505 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <B extends Appendable> B format(Calendar calendar, B buf) {
/*  514 */     if (!calendar.getTimeZone().equals(this.mTimeZone)) {
/*  515 */       calendar = (Calendar)calendar.clone();
/*  516 */       calendar.setTimeZone(this.mTimeZone);
/*      */     } 
/*  518 */     return applyRules(calendar, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
/*  533 */     return applyRules(calendar, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <B extends Appendable> B applyRules(Calendar calendar, B buf) {
/*      */     try {
/*  547 */       for (Rule rule : this.mRules) {
/*  548 */         rule.appendTo((Appendable)buf, calendar);
/*      */       }
/*  550 */     } catch (IOException ioe) {
/*  551 */       Throwables.rethrow(ioe);
/*      */     } 
/*  553 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPattern() {
/*  563 */     return this.mPattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  571 */     return this.mTimeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  579 */     return this.mLocale;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxLengthEstimate() {
/*  592 */     return this.mMaxLengthEstimate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  605 */     if (!(obj instanceof FastDatePrinter)) {
/*  606 */       return false;
/*      */     }
/*  608 */     FastDatePrinter other = (FastDatePrinter)obj;
/*  609 */     return (this.mPattern.equals(other.mPattern) && this.mTimeZone
/*  610 */       .equals(other.mTimeZone) && this.mLocale
/*  611 */       .equals(other.mLocale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  621 */     return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  631 */     return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  645 */     in.defaultReadObject();
/*  646 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendDigits(Appendable buffer, int value) throws IOException {
/*  656 */     buffer.append((char)(value / 10 + 48));
/*  657 */     buffer.append((char)(value % 10 + 48));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendFullDigits(Appendable buffer, int value, int minFieldWidth) throws IOException {
/*  671 */     if (value < 10000) {
/*      */ 
/*      */       
/*  674 */       int nDigits = 4;
/*  675 */       if (value < 1000) {
/*  676 */         nDigits--;
/*  677 */         if (value < 100) {
/*  678 */           nDigits--;
/*  679 */           if (value < 10) {
/*  680 */             nDigits--;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  685 */       for (int i = minFieldWidth - nDigits; i > 0; i--) {
/*  686 */         buffer.append('0');
/*      */       }
/*      */       
/*  689 */       switch (nDigits) {
/*      */         case 4:
/*  691 */           buffer.append((char)(value / 1000 + 48));
/*  692 */           value %= 1000;
/*      */         case 3:
/*  694 */           if (value >= 100) {
/*  695 */             buffer.append((char)(value / 100 + 48));
/*  696 */             value %= 100;
/*      */           } else {
/*  698 */             buffer.append('0');
/*      */           } 
/*      */         case 2:
/*  701 */           if (value >= 10) {
/*  702 */             buffer.append((char)(value / 10 + 48));
/*  703 */             value %= 10;
/*      */           } else {
/*  705 */             buffer.append('0');
/*      */           } 
/*      */         case 1:
/*  708 */           buffer.append((char)(value + 48));
/*      */           break;
/*      */       } 
/*      */ 
/*      */     
/*      */     } else {
/*  714 */       char[] work = new char[10];
/*  715 */       int digit = 0;
/*  716 */       while (value != 0) {
/*  717 */         work[digit++] = (char)(value % 10 + 48);
/*  718 */         value /= 10;
/*      */       } 
/*      */ 
/*      */       
/*  722 */       while (digit < minFieldWidth) {
/*  723 */         buffer.append('0');
/*  724 */         minFieldWidth--;
/*      */       } 
/*      */ 
/*      */       
/*  728 */       while (--digit >= 0) {
/*  729 */         buffer.append(work[digit]);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface Rule
/*      */   {
/*      */     int estimateLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void appendTo(Appendable param1Appendable, Calendar param1Calendar) throws IOException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface NumberRule
/*      */     extends Rule
/*      */   {
/*      */     void appendTo(Appendable param1Appendable, int param1Int) throws IOException;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CharacterLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final char mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CharacterLiteral(char value) {
/*  784 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  792 */       return 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  800 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StringLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final String mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StringLiteral(String value) {
/*  817 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  825 */       return this.mValue.length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  833 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TextField
/*      */     implements Rule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String[] mValues;
/*      */ 
/*      */ 
/*      */     
/*      */     TextField(int field, String[] values) {
/*  852 */       this.mField = field;
/*  853 */       this.mValues = values;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  861 */       int max = 0;
/*  862 */       for (int i = this.mValues.length; --i >= 0; ) {
/*  863 */         int len = this.mValues[i].length();
/*  864 */         if (len > max) {
/*  865 */           max = len;
/*      */         }
/*      */       } 
/*  868 */       return max;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  876 */       buffer.append(this.mValues[calendar.get(this.mField)]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class UnpaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     UnpaddedNumberField(int field) {
/*  892 */       this.mField = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  900 */       return 4;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  908 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/*  916 */       if (value < 10) {
/*  917 */         buffer.append((char)(value + 48));
/*  918 */       } else if (value < 100) {
/*  919 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  921 */         FastDatePrinter.appendFullDigits(buffer, value, 1);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class UnpaddedMonthField
/*      */     implements NumberRule
/*      */   {
/*  930 */     static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  944 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/*  952 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/*  960 */       if (value < 10) {
/*  961 */         buffer.append((char)(value + 48));
/*      */       } else {
/*  963 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class PaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */     
/*      */     private final int mSize;
/*      */ 
/*      */ 
/*      */     
/*      */     PaddedNumberField(int field, int size) {
/*  982 */       if (size < 3)
/*      */       {
/*  984 */         throw new IllegalArgumentException();
/*      */       }
/*  986 */       this.mField = field;
/*  987 */       this.mSize = size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  995 */       return this.mSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1003 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1011 */       FastDatePrinter.appendFullDigits(buffer, value, this.mSize);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwoDigitNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwoDigitNumberField(int field) {
/* 1027 */       this.mField = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1035 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1043 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1051 */       if (value < 100) {
/* 1052 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/* 1054 */         FastDatePrinter.appendFullDigits(buffer, value, 2);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitYearField
/*      */     implements NumberRule
/*      */   {
/* 1063 */     static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1076 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1084 */       appendTo(buffer, calendar.get(1) % 100);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1092 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitMonthField
/*      */     implements NumberRule
/*      */   {
/* 1100 */     static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1113 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1121 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(Appendable buffer, int value) throws IOException {
/* 1129 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwelveHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwelveHourField(FastDatePrinter.NumberRule rule) {
/* 1146 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1154 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1162 */       int value = calendar.get(10);
/* 1163 */       if (value == 0) {
/* 1164 */         value = calendar.getLeastMaximum(10) + 1;
/*      */       }
/* 1166 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1174 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwentyFourHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwentyFourHourField(FastDatePrinter.NumberRule rule) {
/* 1191 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1199 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1207 */       int value = calendar.get(11);
/* 1208 */       if (value == 0) {
/* 1209 */         value = calendar.getMaximum(11) + 1;
/*      */       }
/* 1211 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1219 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class DayInWeekField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */     
/*      */     DayInWeekField(FastDatePrinter.NumberRule rule) {
/* 1230 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1235 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1240 */       int value = calendar.get(7);
/* 1241 */       this.mRule.appendTo(buffer, (value != 1) ? (value - 1) : 7);
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1246 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class WeekYear
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */     
/*      */     WeekYear(FastDatePrinter.NumberRule rule) {
/* 1257 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1262 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1267 */       this.mRule.appendTo(buffer, calendar.getWeekYear());
/*      */     }
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, int value) throws IOException {
/* 1272 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1278 */   private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<>(7);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
/* 1290 */     TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
/* 1291 */     String value = cTimeZoneDisplayCache.get(key);
/* 1292 */     if (value == null) {
/*      */       
/* 1294 */       value = tz.getDisplayName(daylight, style, locale);
/* 1295 */       String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
/* 1296 */       if (prior != null) {
/* 1297 */         value = prior;
/*      */       }
/*      */     } 
/* 1300 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNameRule
/*      */     implements Rule
/*      */   {
/*      */     private final Locale mLocale;
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */     
/*      */     private final String mStandard;
/*      */     
/*      */     private final String mDaylight;
/*      */ 
/*      */     
/*      */     TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
/* 1320 */       this.mLocale = locale;
/* 1321 */       this.mStyle = style;
/*      */       
/* 1323 */       this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
/* 1324 */       this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1335 */       return Math.max(this.mStandard.length(), this.mDaylight.length());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1343 */       TimeZone zone = calendar.getTimeZone();
/* 1344 */       if (calendar.get(16) != 0) {
/* 1345 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
/*      */       } else {
/* 1347 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNumberRule
/*      */     implements Rule
/*      */   {
/* 1357 */     static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
/* 1358 */     static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean mColon;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneNumberRule(boolean colon) {
/* 1368 */       this.mColon = colon;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1376 */       return 5;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1385 */       int offset = calendar.get(15) + calendar.get(16);
/*      */       
/* 1387 */       if (offset < 0) {
/* 1388 */         buffer.append('-');
/* 1389 */         offset = -offset;
/*      */       } else {
/* 1391 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1394 */       int hours = offset / 3600000;
/* 1395 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1397 */       if (this.mColon) {
/* 1398 */         buffer.append(':');
/*      */       }
/*      */       
/* 1401 */       int minutes = offset / 60000 - 60 * hours;
/* 1402 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Iso8601_Rule
/*      */     implements Rule
/*      */   {
/* 1413 */     static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
/*      */     
/* 1415 */     static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
/*      */     
/* 1417 */     static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
/*      */ 
/*      */ 
/*      */     
/*      */     final int length;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static Iso8601_Rule getRule(int tokenLen) {
/* 1427 */       switch (tokenLen) {
/*      */         case 1:
/* 1429 */           return ISO8601_HOURS;
/*      */         case 2:
/* 1431 */           return ISO8601_HOURS_MINUTES;
/*      */         case 3:
/* 1433 */           return ISO8601_HOURS_COLON_MINUTES;
/*      */       } 
/* 1435 */       throw new IllegalArgumentException("invalid number of X");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Iso8601_Rule(int length) {
/* 1447 */       this.length = length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1455 */       return this.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(Appendable buffer, Calendar calendar) throws IOException {
/* 1463 */       int offset = calendar.get(15) + calendar.get(16);
/* 1464 */       if (offset == 0) {
/* 1465 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1469 */       if (offset < 0) {
/* 1470 */         buffer.append('-');
/* 1471 */         offset = -offset;
/*      */       } else {
/* 1473 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1476 */       int hours = offset / 3600000;
/* 1477 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1479 */       if (this.length < 5) {
/*      */         return;
/*      */       }
/*      */       
/* 1483 */       if (this.length == 6) {
/* 1484 */         buffer.append(':');
/*      */       }
/*      */       
/* 1487 */       int minutes = offset / 60000 - 60 * hours;
/* 1488 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneDisplayKey
/*      */   {
/*      */     private final TimeZone mTimeZone;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Locale mLocale;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
/* 1511 */       this.mTimeZone = timeZone;
/* 1512 */       if (daylight) {
/* 1513 */         this.mStyle = style | Integer.MIN_VALUE;
/*      */       } else {
/* 1515 */         this.mStyle = style;
/*      */       } 
/* 1517 */       this.mLocale = locale;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1525 */       return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1533 */       if (this == obj) {
/* 1534 */         return true;
/*      */       }
/* 1536 */       if (obj instanceof TimeZoneDisplayKey) {
/* 1537 */         TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
/* 1538 */         return (this.mTimeZone
/* 1539 */           .equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale
/*      */           
/* 1541 */           .equals(other.mLocale));
/*      */       } 
/* 1543 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\datetime\FastDatePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */