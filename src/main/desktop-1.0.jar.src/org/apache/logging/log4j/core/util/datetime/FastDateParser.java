/*     */ package org.apache.logging.log4j.core.util.datetime;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastDateParser
/*     */   implements DateParser, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3L;
/*  86 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
/*     */   
/*     */   private final String pattern;
/*     */   
/*     */   private final TimeZone timeZone;
/*     */   
/*     */   private final Locale locale;
/*     */   
/*     */   private final int century;
/*     */   
/*     */   private final int startYear;
/*     */   private transient List<StrategyAndWidth> patterns;
/*     */   private static final Comparator<String> LONGER_FIRST_LOWERCASE;
/*     */   
/*     */   static {
/* 101 */     LONGER_FIRST_LOWERCASE = ((left, right) -> right.compareTo(left));
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
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
/* 115 */     this(pattern, timeZone, locale, null);
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
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
/*     */     int centuryStartYear;
/* 130 */     this.pattern = pattern;
/* 131 */     this.timeZone = timeZone;
/* 132 */     this.locale = locale;
/*     */     
/* 134 */     Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
/*     */ 
/*     */     
/* 137 */     if (centuryStart != null) {
/* 138 */       definingCalendar.setTime(centuryStart);
/* 139 */       centuryStartYear = definingCalendar.get(1);
/*     */     }
/* 141 */     else if (locale.equals(JAPANESE_IMPERIAL)) {
/* 142 */       centuryStartYear = 0;
/*     */     }
/*     */     else {
/*     */       
/* 146 */       definingCalendar.setTime(new Date());
/* 147 */       centuryStartYear = definingCalendar.get(1) - 80;
/*     */     } 
/* 149 */     this.century = centuryStartYear / 100 * 100;
/* 150 */     this.startYear = centuryStartYear - this.century;
/*     */     
/* 152 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Calendar definingCalendar) {
/* 162 */     this.patterns = new ArrayList<>();
/*     */     
/* 164 */     StrategyParser fm = new StrategyParser(definingCalendar);
/*     */     while (true) {
/* 166 */       StrategyAndWidth field = fm.getNextStrategy();
/* 167 */       if (field == null) {
/*     */         break;
/*     */       }
/* 170 */       this.patterns.add(field);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StrategyAndWidth
/*     */   {
/*     */     final FastDateParser.Strategy strategy;
/*     */ 
/*     */     
/*     */     final int width;
/*     */ 
/*     */     
/*     */     StrategyAndWidth(FastDateParser.Strategy strategy, int width) {
/* 185 */       this.strategy = strategy;
/* 186 */       this.width = width;
/*     */     }
/*     */     
/*     */     int getMaxWidth(ListIterator<StrategyAndWidth> lt) {
/* 190 */       if (!this.strategy.isNumber() || !lt.hasNext()) {
/* 191 */         return 0;
/*     */       }
/* 193 */       FastDateParser.Strategy nextStrategy = ((StrategyAndWidth)lt.next()).strategy;
/* 194 */       lt.previous();
/* 195 */       return nextStrategy.isNumber() ? this.width : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class StrategyParser
/*     */   {
/*     */     private final Calendar definingCalendar;
/*     */     
/*     */     private int currentIdx;
/*     */     
/*     */     StrategyParser(Calendar definingCalendar) {
/* 207 */       this.definingCalendar = definingCalendar;
/*     */     }
/*     */     
/*     */     FastDateParser.StrategyAndWidth getNextStrategy() {
/* 211 */       if (this.currentIdx >= FastDateParser.this.pattern.length()) {
/* 212 */         return null;
/*     */       }
/*     */       
/* 215 */       char c = FastDateParser.this.pattern.charAt(this.currentIdx);
/* 216 */       if (FastDateParser.isFormatLetter(c)) {
/* 217 */         return letterPattern(c);
/*     */       }
/* 219 */       return literal();
/*     */     }
/*     */     
/*     */     private FastDateParser.StrategyAndWidth letterPattern(char c) {
/* 223 */       int begin = this.currentIdx; do {  }
/* 224 */       while (++this.currentIdx < FastDateParser.this.pattern.length() && 
/* 225 */         FastDateParser.this.pattern.charAt(this.currentIdx) == c);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       int width = this.currentIdx - begin;
/* 231 */       return new FastDateParser.StrategyAndWidth(FastDateParser.this.getStrategy(c, width, this.definingCalendar), width);
/*     */     }
/*     */     
/*     */     private FastDateParser.StrategyAndWidth literal() {
/* 235 */       boolean activeQuote = false;
/*     */       
/* 237 */       StringBuilder sb = new StringBuilder();
/* 238 */       while (this.currentIdx < FastDateParser.this.pattern.length()) {
/* 239 */         char c = FastDateParser.this.pattern.charAt(this.currentIdx);
/* 240 */         if (!activeQuote && FastDateParser.isFormatLetter(c))
/*     */           break; 
/* 242 */         if (c == '\'' && (++this.currentIdx == FastDateParser.this.pattern.length() || FastDateParser.this.pattern.charAt(this.currentIdx) != '\'')) {
/* 243 */           activeQuote = !activeQuote;
/*     */           continue;
/*     */         } 
/* 246 */         this.currentIdx++;
/* 247 */         sb.append(c);
/*     */       } 
/*     */       
/* 250 */       if (activeQuote) {
/* 251 */         throw new IllegalArgumentException("Unterminated quote");
/*     */       }
/*     */       
/* 254 */       String formatField = sb.toString();
/* 255 */       return new FastDateParser.StrategyAndWidth(new FastDateParser.CopyQuotedStrategy(formatField), formatField.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isFormatLetter(char c) {
/* 260 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 270 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 278 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 286 */     return this.locale;
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
/*     */   public boolean equals(Object obj) {
/* 300 */     if (!(obj instanceof FastDateParser)) {
/* 301 */       return false;
/*     */     }
/* 303 */     FastDateParser other = (FastDateParser)obj;
/* 304 */     return (this.pattern.equals(other.pattern) && this.timeZone
/* 305 */       .equals(other.timeZone) && this.locale
/* 306 */       .equals(other.locale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 316 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 340 */     in.defaultReadObject();
/*     */     
/* 342 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/* 343 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source) throws ParseException {
/* 351 */     return parse(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source) throws ParseException {
/* 359 */     ParsePosition pp = new ParsePosition(0);
/* 360 */     Date date = parse(source, pp);
/* 361 */     if (date == null) {
/*     */       
/* 363 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/* 364 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source, pp
/*     */             
/* 366 */             .getErrorIndex());
/*     */       }
/* 368 */       throw new ParseException("Unparseable date: " + source, pp.getErrorIndex());
/*     */     } 
/* 370 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source, ParsePosition pos) {
/* 378 */     return parse(source, pos);
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
/*     */   public Date parse(String source, ParsePosition pos) {
/* 396 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/* 397 */     cal.clear();
/*     */     
/* 399 */     return parse(source, pos, cal) ? cal.getTime() : null;
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
/*     */   public boolean parse(String source, ParsePosition pos, Calendar calendar) {
/* 417 */     ListIterator<StrategyAndWidth> lt = this.patterns.listIterator();
/* 418 */     while (lt.hasNext()) {
/* 419 */       StrategyAndWidth strategyAndWidth = lt.next();
/* 420 */       int maxWidth = strategyAndWidth.getMaxWidth(lt);
/* 421 */       if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth)) {
/* 422 */         return false;
/*     */       }
/*     */     } 
/* 425 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder simpleQuote(StringBuilder sb, String value) {
/* 432 */     for (int i = 0; i < value.length(); i++) {
/* 433 */       char c = value.charAt(i);
/* 434 */       switch (c) {
/*     */         case '$':
/*     */         case '(':
/*     */         case ')':
/*     */         case '*':
/*     */         case '+':
/*     */         case '.':
/*     */         case '?':
/*     */         case '[':
/*     */         case '\\':
/*     */         case '^':
/*     */         case '{':
/*     */         case '|':
/* 447 */           sb.append('\\'); break;
/*     */       } 
/* 449 */       sb.append(c);
/*     */     } 
/*     */     
/* 452 */     return sb;
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
/*     */   private static Map<String, Integer> appendDisplayNames(Calendar cal, Locale locale, int field, StringBuilder regex) {
/* 464 */     Map<String, Integer> values = new HashMap<>();
/*     */     
/* 466 */     Map<String, Integer> displayNames = cal.getDisplayNames(field, 0, locale);
/* 467 */     TreeSet<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
/* 468 */     for (Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
/* 469 */       String key = ((String)displayName.getKey()).toLowerCase(locale);
/* 470 */       if (sorted.add(key)) {
/* 471 */         values.put(key, displayName.getValue());
/*     */       }
/*     */     } 
/* 474 */     for (String symbol : sorted) {
/* 475 */       simpleQuote(regex, symbol).append('|');
/*     */     }
/* 477 */     return values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int adjustYear(int twoDigitYear) {
/* 486 */     int trial = this.century + twoDigitYear;
/* 487 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Strategy
/*     */   {
/*     */     private Strategy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 501 */       return false;
/*     */     }
/*     */     
/*     */     abstract boolean parse(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String, ParsePosition param1ParsePosition, int param1Int);
/*     */   }
/*     */   
/*     */   private static abstract class PatternStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private Pattern pattern;
/*     */     
/*     */     private PatternStrategy() {}
/*     */     
/*     */     void createPattern(StringBuilder regex) {
/* 515 */       createPattern(regex.toString());
/*     */     }
/*     */     
/*     */     void createPattern(String regex) {
/* 519 */       this.pattern = Pattern.compile(regex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 530 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 535 */       Matcher matcher = this.pattern.matcher(source.substring(pos.getIndex()));
/* 536 */       if (!matcher.lookingAt()) {
/* 537 */         pos.setErrorIndex(pos.getIndex());
/* 538 */         return false;
/*     */       } 
/* 540 */       pos.setIndex(pos.getIndex() + matcher.end(1));
/* 541 */       setCalendar(parser, calendar, matcher.group(1));
/* 542 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void setCalendar(FastDateParser param1FastDateParser, Calendar param1Calendar, String param1String);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getStrategy(char f, int width, Calendar definingCalendar) {
/* 555 */     switch (f) {
/*     */       default:
/* 557 */         throw new IllegalArgumentException("Format '" + f + "' not supported");
/*     */       case 'D':
/* 559 */         return DAY_OF_YEAR_STRATEGY;
/*     */       case 'E':
/* 561 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*     */       case 'F':
/* 563 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*     */       case 'G':
/* 565 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*     */       case 'H':
/* 567 */         return HOUR_OF_DAY_STRATEGY;
/*     */       case 'K':
/* 569 */         return HOUR_STRATEGY;
/*     */       case 'M':
/* 571 */         return (width >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*     */       case 'S':
/* 573 */         return MILLISECOND_STRATEGY;
/*     */       case 'W':
/* 575 */         return WEEK_OF_MONTH_STRATEGY;
/*     */       case 'a':
/* 577 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*     */       case 'd':
/* 579 */         return DAY_OF_MONTH_STRATEGY;
/*     */       case 'h':
/* 581 */         return HOUR12_STRATEGY;
/*     */       case 'k':
/* 583 */         return HOUR24_OF_DAY_STRATEGY;
/*     */       case 'm':
/* 585 */         return MINUTE_STRATEGY;
/*     */       case 's':
/* 587 */         return SECOND_STRATEGY;
/*     */       case 'u':
/* 589 */         return DAY_OF_WEEK_STRATEGY;
/*     */       case 'w':
/* 591 */         return WEEK_OF_YEAR_STRATEGY;
/*     */       case 'Y':
/*     */       case 'y':
/* 594 */         return (width > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*     */       case 'X':
/* 596 */         return ISO8601TimeZoneStrategy.getStrategy(width);
/*     */       case 'Z':
/* 598 */         if (width == 2)
/* 599 */           return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;  break;
/*     */       case 'z':
/*     */         break;
/*     */     } 
/* 603 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 608 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/* 616 */     synchronized (caches) {
/* 617 */       if (caches[field] == null) {
/* 618 */         caches[field] = new ConcurrentHashMap<>(3);
/*     */       }
/* 620 */       return caches[field];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
/* 631 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/* 632 */     Strategy strategy = cache.get(this.locale);
/* 633 */     if (strategy == null) {
/* 634 */       strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
/*     */ 
/*     */       
/* 637 */       Strategy inCache = cache.putIfAbsent(this.locale, strategy);
/* 638 */       if (inCache != null) {
/* 639 */         return inCache;
/*     */       }
/*     */     } 
/* 642 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CopyQuotedStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String formatField;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     CopyQuotedStrategy(String formatField) {
/* 657 */       this.formatField = formatField;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 665 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 670 */       for (int idx = 0; idx < this.formatField.length(); idx++) {
/* 671 */         int sIdx = idx + pos.getIndex();
/* 672 */         if (sIdx == source.length()) {
/* 673 */           pos.setErrorIndex(sIdx);
/* 674 */           return false;
/*     */         } 
/* 676 */         if (this.formatField.charAt(idx) != source.charAt(sIdx)) {
/* 677 */           pos.setErrorIndex(sIdx);
/* 678 */           return false;
/*     */         } 
/*     */       } 
/* 681 */       pos.setIndex(this.formatField.length() + pos.getIndex());
/* 682 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CaseInsensitiveTextStrategy
/*     */     extends PatternStrategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */     
/*     */     final Locale locale;
/*     */ 
/*     */     
/*     */     private final Map<String, Integer> lKeyValues;
/*     */ 
/*     */     
/*     */     CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
/* 701 */       this.field = field;
/* 702 */       this.locale = locale;
/*     */       
/* 704 */       StringBuilder regex = new StringBuilder();
/* 705 */       regex.append("((?iu)");
/* 706 */       this.lKeyValues = FastDateParser.appendDisplayNames(definingCalendar, locale, field, regex);
/* 707 */       regex.setLength(regex.length() - 1);
/* 708 */       regex.append(")");
/* 709 */       createPattern(regex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 717 */       Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
/* 718 */       cal.set(this.field, iVal.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NumberStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     NumberStrategy(int field) {
/* 734 */       this.field = field;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 742 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth) {
/* 747 */       int idx = pos.getIndex();
/* 748 */       int last = source.length();
/*     */       
/* 750 */       if (maxWidth == 0) {
/*     */         
/* 752 */         for (; idx < last; idx++) {
/* 753 */           char c = source.charAt(idx);
/* 754 */           if (!Character.isWhitespace(c)) {
/*     */             break;
/*     */           }
/*     */         } 
/* 758 */         pos.setIndex(idx);
/*     */       } else {
/* 760 */         int end = idx + maxWidth;
/* 761 */         if (last > end) {
/* 762 */           last = end;
/*     */         }
/*     */       } 
/*     */       
/* 766 */       for (; idx < last; idx++) {
/* 767 */         char c = source.charAt(idx);
/* 768 */         if (!Character.isDigit(c)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 773 */       if (pos.getIndex() == idx) {
/* 774 */         pos.setErrorIndex(idx);
/* 775 */         return false;
/*     */       } 
/*     */       
/* 778 */       int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
/* 779 */       pos.setIndex(idx);
/*     */       
/* 781 */       calendar.set(this.field, modify(parser, value));
/* 782 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int modify(FastDateParser parser, int iValue) {
/* 792 */       return iValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 797 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*     */     {
/*     */ 
/*     */       
/*     */       int modify(FastDateParser parser, int iValue)
/*     */       {
/* 803 */         return (iValue < 100) ? parser.adjustYear(iValue) : iValue;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   static class TimeZoneStrategy
/*     */     extends PatternStrategy
/*     */   {
/*     */     private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
/*     */     
/*     */     private static final String GMT_OPTION = "GMT[+-]\\d{1,2}:\\d{2}";
/*     */     private final Locale locale;
/* 815 */     private final Map<String, TzInfo> tzNames = new HashMap<>();
/*     */     private static final int ID = 0;
/*     */     
/*     */     private static class TzInfo { TimeZone zone;
/*     */       int dstOffset;
/*     */       
/*     */       TzInfo(TimeZone tz, boolean useDst) {
/* 822 */         this.zone = tz;
/* 823 */         this.dstOffset = useDst ? tz.getDSTSavings() : 0;
/*     */       } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TimeZoneStrategy(Locale locale) {
/* 837 */       this.locale = locale;
/*     */       
/* 839 */       StringBuilder sb = new StringBuilder();
/* 840 */       sb.append("((?iu)[+-]\\d{4}|GMT[+-]\\d{1,2}:\\d{2}");
/*     */       
/* 842 */       Set<String> sorted = new TreeSet<>(FastDateParser.LONGER_FIRST_LOWERCASE);
/*     */       
/* 844 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/* 845 */       for (String[] zoneNames : zones) {
/*     */         
/* 847 */         String tzId = zoneNames[0];
/* 848 */         if (!tzId.equalsIgnoreCase("GMT")) {
/*     */ 
/*     */           
/* 851 */           TimeZone tz = TimeZone.getTimeZone(tzId);
/*     */ 
/*     */           
/* 854 */           TzInfo standard = new TzInfo(tz, false);
/* 855 */           TzInfo tzInfo = standard;
/* 856 */           for (int i = 1; i < zoneNames.length; i++) {
/* 857 */             switch (i) {
/*     */               
/*     */               case 3:
/* 860 */                 tzInfo = new TzInfo(tz, true);
/*     */                 break;
/*     */               case 5:
/* 863 */                 tzInfo = standard;
/*     */                 break;
/*     */             } 
/* 866 */             if (zoneNames[i] != null) {
/* 867 */               String key = zoneNames[i].toLowerCase(locale);
/*     */ 
/*     */               
/* 870 */               if (sorted.add(key)) {
/* 871 */                 this.tzNames.put(key, tzInfo);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 878 */       for (String zoneName : sorted) {
/* 879 */         FastDateParser.simpleQuote(sb.append('|'), zoneName);
/*     */       }
/* 881 */       sb.append(")");
/* 882 */       createPattern(sb);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 890 */       if (value.charAt(0) == '+' || value.charAt(0) == '-') {
/* 891 */         TimeZone tz = TimeZone.getTimeZone("GMT" + value);
/* 892 */         cal.setTimeZone(tz);
/* 893 */       } else if (value.regionMatches(true, 0, "GMT", 0, 3)) {
/* 894 */         TimeZone tz = TimeZone.getTimeZone(value.toUpperCase());
/* 895 */         cal.setTimeZone(tz);
/*     */       } else {
/* 897 */         TzInfo tzInfo = this.tzNames.get(value.toLowerCase(this.locale));
/* 898 */         cal.set(16, tzInfo.dstOffset);
/* 899 */         cal.set(15, tzInfo.zone.getRawOffset());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ISO8601TimeZoneStrategy
/*     */     extends PatternStrategy
/*     */   {
/*     */     ISO8601TimeZoneStrategy(String pattern) {
/* 912 */       createPattern(pattern);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 920 */       if (value.equals("Z")) {
/* 921 */         cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       } else {
/* 923 */         cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
/*     */       } 
/*     */     }
/*     */     
/* 927 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/* 928 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/* 929 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 939 */       switch (tokenLen) {
/*     */         case 1:
/* 941 */           return ISO_8601_1_STRATEGY;
/*     */         case 2:
/* 943 */           return ISO_8601_2_STRATEGY;
/*     */         case 3:
/* 945 */           return ISO_8601_3_STRATEGY;
/*     */       } 
/* 947 */       throw new IllegalArgumentException("invalid number of X");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 952 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 955 */         return iValue - 1;
/*     */       }
/*     */     };
/* 958 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 959 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 960 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/* 961 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 962 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 963 */   private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(7)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 966 */         return (iValue != 7) ? (iValue + 1) : 1;
/*     */       }
/*     */     };
/* 969 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/* 970 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 971 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 974 */         return (iValue == 24) ? 0 : iValue;
/*     */       }
/*     */     };
/* 977 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*     */     {
/*     */       int modify(FastDateParser parser, int iValue) {
/* 980 */         return (iValue == 12) ? 0 : iValue;
/*     */       }
/*     */     };
/* 983 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 984 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 985 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 986 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\datetime\FastDateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */