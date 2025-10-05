/*      */ package org.apache.logging.log4j.core.util;
/*      */ 
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import java.util.TreeSet;
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
/*      */ public final class CronExpression
/*      */ {
/*      */   protected static final int SECOND = 0;
/*      */   protected static final int MINUTE = 1;
/*      */   protected static final int HOUR = 2;
/*      */   protected static final int DAY_OF_MONTH = 3;
/*      */   protected static final int MONTH = 4;
/*      */   protected static final int DAY_OF_WEEK = 5;
/*      */   protected static final int YEAR = 6;
/*      */   protected static final int ALL_SPEC_INT = 99;
/*      */   protected static final int NO_SPEC_INT = 98;
/*  208 */   protected static final Integer ALL_SPEC = Integer.valueOf(99);
/*  209 */   protected static final Integer NO_SPEC = Integer.valueOf(98);
/*      */   
/*  211 */   protected static final Map<String, Integer> monthMap = new HashMap<>(20);
/*  212 */   protected static final Map<String, Integer> dayMap = new HashMap<>(60);
/*      */   
/*      */   static {
/*  215 */     monthMap.put("JAN", Integer.valueOf(0));
/*  216 */     monthMap.put("FEB", Integer.valueOf(1));
/*  217 */     monthMap.put("MAR", Integer.valueOf(2));
/*  218 */     monthMap.put("APR", Integer.valueOf(3));
/*  219 */     monthMap.put("MAY", Integer.valueOf(4));
/*  220 */     monthMap.put("JUN", Integer.valueOf(5));
/*  221 */     monthMap.put("JUL", Integer.valueOf(6));
/*  222 */     monthMap.put("AUG", Integer.valueOf(7));
/*  223 */     monthMap.put("SEP", Integer.valueOf(8));
/*  224 */     monthMap.put("OCT", Integer.valueOf(9));
/*  225 */     monthMap.put("NOV", Integer.valueOf(10));
/*  226 */     monthMap.put("DEC", Integer.valueOf(11));
/*      */     
/*  228 */     dayMap.put("SUN", Integer.valueOf(1));
/*  229 */     dayMap.put("MON", Integer.valueOf(2));
/*  230 */     dayMap.put("TUE", Integer.valueOf(3));
/*  231 */     dayMap.put("WED", Integer.valueOf(4));
/*  232 */     dayMap.put("THU", Integer.valueOf(5));
/*  233 */     dayMap.put("FRI", Integer.valueOf(6));
/*  234 */     dayMap.put("SAT", Integer.valueOf(7));
/*      */   }
/*      */   
/*      */   private final String cronExpression;
/*  238 */   private TimeZone timeZone = null;
/*      */   
/*      */   protected transient TreeSet<Integer> seconds;
/*      */   protected transient TreeSet<Integer> minutes;
/*      */   protected transient TreeSet<Integer> hours;
/*      */   protected transient TreeSet<Integer> daysOfMonth;
/*      */   protected transient TreeSet<Integer> months;
/*      */   protected transient TreeSet<Integer> daysOfWeek;
/*      */   protected transient TreeSet<Integer> years;
/*      */   protected transient boolean lastdayOfWeek = false;
/*  248 */   protected transient int nthdayOfWeek = 0;
/*      */   protected transient boolean lastdayOfMonth = false;
/*      */   protected transient boolean nearestWeekday = false;
/*  251 */   protected transient int lastdayOffset = 0;
/*      */   
/*      */   protected transient boolean expressionParsed = false;
/*  254 */   public static final int MAX_YEAR = Calendar.getInstance().get(1) + 100;
/*  255 */   public static final Calendar MIN_CAL = Calendar.getInstance();
/*      */   static {
/*  257 */     MIN_CAL.set(1970, 0, 1);
/*      */   }
/*  259 */   public static final Date MIN_DATE = MIN_CAL.getTime();
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
/*      */   public CronExpression(String cronExpression) throws ParseException {
/*  271 */     if (cronExpression == null) {
/*  272 */       throw new IllegalArgumentException("cronExpression cannot be null");
/*      */     }
/*      */     
/*  275 */     this.cronExpression = cronExpression.toUpperCase(Locale.US);
/*      */     
/*  277 */     buildExpression(this.cronExpression);
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
/*      */   public boolean isSatisfiedBy(Date date) {
/*  290 */     Calendar testDateCal = Calendar.getInstance(getTimeZone());
/*  291 */     testDateCal.setTime(date);
/*  292 */     testDateCal.set(14, 0);
/*  293 */     Date originalDate = testDateCal.getTime();
/*      */     
/*  295 */     testDateCal.add(13, -1);
/*      */     
/*  297 */     Date timeAfter = getTimeAfter(testDateCal.getTime());
/*      */     
/*  299 */     return (timeAfter != null && timeAfter.equals(originalDate));
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
/*      */   public Date getNextValidTimeAfter(Date date) {
/*  311 */     return getTimeAfter(date);
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
/*      */   public Date getNextInvalidTimeAfter(Date date) {
/*  323 */     long difference = 1000L;
/*      */ 
/*      */     
/*  326 */     Calendar adjustCal = Calendar.getInstance(getTimeZone());
/*  327 */     adjustCal.setTime(date);
/*  328 */     adjustCal.set(14, 0);
/*  329 */     Date lastDate = adjustCal.getTime();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  338 */     while (difference == 1000L) {
/*  339 */       Date newDate = getTimeAfter(lastDate);
/*  340 */       if (newDate == null) {
/*      */         break;
/*      */       }
/*      */       
/*  344 */       difference = newDate.getTime() - lastDate.getTime();
/*      */       
/*  346 */       if (difference == 1000L) {
/*  347 */         lastDate = newDate;
/*      */       }
/*      */     } 
/*      */     
/*  351 */     return new Date(lastDate.getTime() + 1000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  359 */     if (this.timeZone == null) {
/*  360 */       this.timeZone = TimeZone.getDefault();
/*      */     }
/*      */     
/*  363 */     return this.timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimeZone(TimeZone timeZone) {
/*  371 */     this.timeZone = timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  381 */     return this.cronExpression;
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
/*      */   public static boolean isValidExpression(String cronExpression) {
/*      */     try {
/*  395 */       new CronExpression(cronExpression);
/*  396 */     } catch (ParseException pe) {
/*  397 */       return false;
/*      */     } 
/*      */     
/*  400 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void validateExpression(String cronExpression) throws ParseException {
/*  405 */     new CronExpression(cronExpression);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void buildExpression(String expression) throws ParseException {
/*  416 */     this.expressionParsed = true;
/*      */ 
/*      */     
/*      */     try {
/*  420 */       if (this.seconds == null) {
/*  421 */         this.seconds = new TreeSet<>();
/*      */       }
/*  423 */       if (this.minutes == null) {
/*  424 */         this.minutes = new TreeSet<>();
/*      */       }
/*  426 */       if (this.hours == null) {
/*  427 */         this.hours = new TreeSet<>();
/*      */       }
/*  429 */       if (this.daysOfMonth == null) {
/*  430 */         this.daysOfMonth = new TreeSet<>();
/*      */       }
/*  432 */       if (this.months == null) {
/*  433 */         this.months = new TreeSet<>();
/*      */       }
/*  435 */       if (this.daysOfWeek == null) {
/*  436 */         this.daysOfWeek = new TreeSet<>();
/*      */       }
/*  438 */       if (this.years == null) {
/*  439 */         this.years = new TreeSet<>();
/*      */       }
/*      */       
/*  442 */       int exprOn = 0;
/*      */       
/*  444 */       StringTokenizer exprsTok = new StringTokenizer(expression, " \t", false);
/*      */ 
/*      */       
/*  447 */       while (exprsTok.hasMoreTokens() && exprOn <= 6) {
/*  448 */         String expr = exprsTok.nextToken().trim();
/*      */ 
/*      */         
/*  451 */         if (exprOn == 3 && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
/*  452 */           throw new ParseException("Support for specifying 'L' and 'LW' with other days of the month is not implemented", -1);
/*      */         }
/*      */         
/*  455 */         if (exprOn == 5 && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
/*  456 */           throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1);
/*      */         }
/*  458 */         if (exprOn == 5 && expr.indexOf('#') != -1 && expr.indexOf('#', expr.indexOf('#') + 1) != -1) {
/*  459 */           throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1);
/*      */         }
/*      */         
/*  462 */         StringTokenizer vTok = new StringTokenizer(expr, ",");
/*  463 */         while (vTok.hasMoreTokens()) {
/*  464 */           String v = vTok.nextToken();
/*  465 */           storeExpressionVals(0, v, exprOn);
/*      */         } 
/*      */         
/*  468 */         exprOn++;
/*      */       } 
/*      */       
/*  471 */       if (exprOn <= 5) {
/*  472 */         throw new ParseException("Unexpected end of expression.", expression
/*  473 */             .length());
/*      */       }
/*      */       
/*  476 */       if (exprOn <= 6) {
/*  477 */         storeExpressionVals(0, "*", 6);
/*      */       }
/*      */       
/*  480 */       TreeSet<Integer> dow = getSet(5);
/*  481 */       TreeSet<Integer> dom = getSet(3);
/*      */ 
/*      */       
/*  484 */       boolean dayOfMSpec = !dom.contains(NO_SPEC);
/*  485 */       boolean dayOfWSpec = !dow.contains(NO_SPEC);
/*      */       
/*  487 */       if ((!dayOfMSpec || dayOfWSpec) && (
/*  488 */         !dayOfWSpec || dayOfMSpec)) {
/*  489 */         throw new ParseException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.", 0);
/*      */       
/*      */       }
/*      */     }
/*  493 */     catch (ParseException pe) {
/*  494 */       throw pe;
/*  495 */     } catch (Exception e) {
/*  496 */       throw new ParseException("Illegal cron expression format (" + e
/*  497 */           .toString() + ")", 0);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int storeExpressionVals(int pos, String s, int type) throws ParseException {
/*  504 */     int incr = 0;
/*  505 */     int i = skipWhiteSpace(pos, s);
/*  506 */     if (i >= s.length()) {
/*  507 */       return i;
/*      */     }
/*  509 */     char c = s.charAt(i);
/*  510 */     if (c >= 'A' && c <= 'Z' && !s.equals("L") && !s.equals("LW") && !s.matches("^L-[0-9]*[W]?")) {
/*  511 */       String sub = s.substring(i, i + 3);
/*  512 */       int sval = -1;
/*  513 */       int eval = -1;
/*  514 */       if (type == 4) {
/*  515 */         sval = getMonthNumber(sub) + 1;
/*  516 */         if (sval <= 0) {
/*  517 */           throw new ParseException("Invalid Month value: '" + sub + "'", i);
/*      */         }
/*  519 */         if (s.length() > i + 3) {
/*  520 */           c = s.charAt(i + 3);
/*  521 */           if (c == '-') {
/*  522 */             i += 4;
/*  523 */             sub = s.substring(i, i + 3);
/*  524 */             eval = getMonthNumber(sub) + 1;
/*  525 */             if (eval <= 0) {
/*  526 */               throw new ParseException("Invalid Month value: '" + sub + "'", i);
/*      */             }
/*      */           } 
/*      */         } 
/*  530 */       } else if (type == 5) {
/*  531 */         sval = getDayOfWeekNumber(sub);
/*  532 */         if (sval < 0) {
/*  533 */           throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
/*      */         }
/*      */         
/*  536 */         if (s.length() > i + 3) {
/*  537 */           c = s.charAt(i + 3);
/*  538 */           switch (c) {
/*      */             case '-':
/*  540 */               i += 4;
/*  541 */               sub = s.substring(i, i + 3);
/*  542 */               eval = getDayOfWeekNumber(sub);
/*  543 */               if (eval < 0) {
/*  544 */                 throw new ParseException("Invalid Day-of-Week value: '" + sub + "'", i);
/*      */               }
/*      */               break;
/*      */ 
/*      */             
/*      */             case '#':
/*      */               try {
/*  551 */                 i += 4;
/*  552 */                 this.nthdayOfWeek = Integer.parseInt(s.substring(i));
/*  553 */                 if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5) {
/*  554 */                   throw new Exception();
/*      */                 }
/*  556 */               } catch (Exception e) {
/*  557 */                 throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
/*      */               } 
/*      */               break;
/*      */ 
/*      */             
/*      */             case 'L':
/*  563 */               this.lastdayOfWeek = true;
/*  564 */               i++;
/*      */               break;
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         } 
/*      */       } else {
/*  572 */         throw new ParseException("Illegal characters for this position: '" + sub + "'", i);
/*      */       } 
/*      */ 
/*      */       
/*  576 */       if (eval != -1) {
/*  577 */         incr = 1;
/*      */       }
/*  579 */       addToSet(sval, eval, incr, type);
/*  580 */       return i + 3;
/*      */     } 
/*      */     
/*  583 */     switch (c) {
/*      */       case '?':
/*  585 */         i++;
/*  586 */         if (i + 1 < s.length() && s
/*  587 */           .charAt(i) != ' ' && s.charAt(i + 1) != '\t') {
/*  588 */           throw new ParseException("Illegal character after '?': " + s
/*  589 */               .charAt(i), i);
/*      */         }
/*  591 */         if (type != 5 && type != 3) {
/*  592 */           throw new ParseException("'?' can only be specfied for Day-of-Month or Day-of-Week.", i);
/*      */         }
/*      */ 
/*      */         
/*  596 */         if (type == 5 && !this.lastdayOfMonth) {
/*  597 */           int val = ((Integer)this.daysOfMonth.last()).intValue();
/*  598 */           if (val == 98) {
/*  599 */             throw new ParseException("'?' can only be specfied for Day-of-Month -OR- Day-of-Week.", i);
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  604 */         addToSet(98, -1, 0, type);
/*  605 */         return i;
/*      */       case '*':
/*      */       case '/':
/*  608 */         if (c == '*' && i + 1 >= s.length()) {
/*  609 */           addToSet(99, -1, incr, type);
/*  610 */           return i + 1;
/*  611 */         }  if (c == '/' && (i + 1 >= s
/*  612 */           .length() || s.charAt(i + 1) == ' ' || s
/*  613 */           .charAt(i + 1) == '\t'))
/*  614 */           throw new ParseException("'/' must be followed by an integer.", i); 
/*  615 */         if (c == '*') {
/*  616 */           i++;
/*      */         }
/*  618 */         c = s.charAt(i);
/*  619 */         if (c == '/') {
/*  620 */           i++;
/*  621 */           if (i >= s.length()) {
/*  622 */             throw new ParseException("Unexpected end of string.", i);
/*      */           }
/*      */           
/*  625 */           incr = getNumericValue(s, i);
/*      */           
/*  627 */           i++;
/*  628 */           if (incr > 10) {
/*  629 */             i++;
/*      */           }
/*  631 */           if (incr > 59 && (type == 0 || type == 1))
/*  632 */             throw new ParseException("Increment > 60 : " + incr, i); 
/*  633 */           if (incr > 23 && type == 2)
/*  634 */             throw new ParseException("Increment > 24 : " + incr, i); 
/*  635 */           if (incr > 31 && type == 3)
/*  636 */             throw new ParseException("Increment > 31 : " + incr, i); 
/*  637 */           if (incr > 7 && type == 5)
/*  638 */             throw new ParseException("Increment > 7 : " + incr, i); 
/*  639 */           if (incr > 12 && type == 4) {
/*  640 */             throw new ParseException("Increment > 12 : " + incr, i);
/*      */           }
/*      */         } else {
/*  643 */           incr = 1;
/*      */         } 
/*  645 */         addToSet(99, -1, incr, type);
/*  646 */         return i;
/*      */       case 'L':
/*  648 */         i++;
/*  649 */         if (type == 3) {
/*  650 */           this.lastdayOfMonth = true;
/*      */         }
/*  652 */         if (type == 5) {
/*  653 */           addToSet(7, 7, 0, type);
/*      */         }
/*  655 */         if (type == 3 && s.length() > i) {
/*  656 */           c = s.charAt(i);
/*  657 */           if (c == '-') {
/*  658 */             ValueSet vs = getValue(0, s, i + 1);
/*  659 */             this.lastdayOffset = vs.value;
/*  660 */             if (this.lastdayOffset > 30) {
/*  661 */               throw new ParseException("Offset from last day must be <= 30", i + 1);
/*      */             }
/*  663 */             i = vs.pos;
/*      */           } 
/*  665 */           if (s.length() > i) {
/*  666 */             c = s.charAt(i);
/*  667 */             if (c == 'W') {
/*  668 */               this.nearestWeekday = true;
/*  669 */               i++;
/*      */             } 
/*      */           } 
/*      */         } 
/*  673 */         return i;
/*      */     } 
/*  675 */     if (c >= '0' && c <= '9') {
/*  676 */       int val = Integer.parseInt(String.valueOf(c));
/*  677 */       i++;
/*  678 */       if (i >= s.length()) {
/*  679 */         addToSet(val, -1, -1, type);
/*      */       } else {
/*  681 */         c = s.charAt(i);
/*  682 */         if (c >= '0' && c <= '9') {
/*  683 */           ValueSet vs = getValue(val, s, i);
/*  684 */           val = vs.value;
/*  685 */           i = vs.pos;
/*      */         } 
/*  687 */         i = checkNext(i, s, val, type);
/*  688 */         return i;
/*      */       } 
/*      */     } else {
/*  691 */       throw new ParseException("Unexpected character: " + c, i);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  696 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int checkNext(int pos, String s, int val, int type) throws ParseException {
/*      */     TreeSet<Integer> set;
/*  702 */     int v, v2, end = -1;
/*  703 */     int i = pos;
/*      */     
/*  705 */     if (i >= s.length()) {
/*  706 */       addToSet(val, end, -1, type);
/*  707 */       return i;
/*      */     } 
/*      */     
/*  710 */     char c = s.charAt(pos);
/*      */     
/*  712 */     if (c == 'L') {
/*  713 */       if (type == 5) {
/*  714 */         if (val < 1 || val > 7) {
/*  715 */           throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
/*      */         }
/*  717 */         this.lastdayOfWeek = true;
/*      */       } else {
/*  719 */         throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
/*      */       } 
/*  721 */       TreeSet<Integer> treeSet = getSet(type);
/*  722 */       treeSet.add(Integer.valueOf(val));
/*  723 */       i++;
/*  724 */       return i;
/*      */     } 
/*      */     
/*  727 */     if (c == 'W') {
/*  728 */       if (type == 3) {
/*  729 */         this.nearestWeekday = true;
/*      */       } else {
/*  731 */         throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
/*      */       } 
/*  733 */       if (val > 31) {
/*  734 */         throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
/*      */       }
/*  736 */       TreeSet<Integer> treeSet = getSet(type);
/*  737 */       treeSet.add(Integer.valueOf(val));
/*  738 */       i++;
/*  739 */       return i;
/*      */     } 
/*      */     
/*  742 */     switch (c) {
/*      */       case '#':
/*  744 */         if (type != 5) {
/*  745 */           throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i);
/*      */         }
/*  747 */         i++;
/*      */         try {
/*  749 */           this.nthdayOfWeek = Integer.parseInt(s.substring(i));
/*  750 */           if (this.nthdayOfWeek < 1 || this.nthdayOfWeek > 5) {
/*  751 */             throw new Exception();
/*      */           }
/*  753 */         } catch (Exception e) {
/*  754 */           throw new ParseException("A numeric value between 1 and 5 must follow the '#' option", i);
/*      */         } 
/*      */ 
/*      */         
/*  758 */         set = getSet(type);
/*  759 */         set.add(Integer.valueOf(val));
/*  760 */         i++;
/*  761 */         return i;
/*      */       case '-':
/*  763 */         i++;
/*  764 */         c = s.charAt(i);
/*  765 */         v = Integer.parseInt(String.valueOf(c));
/*  766 */         end = v;
/*  767 */         i++;
/*  768 */         if (i >= s.length()) {
/*  769 */           addToSet(val, end, 1, type);
/*  770 */           return i;
/*      */         } 
/*  772 */         c = s.charAt(i);
/*  773 */         if (c >= '0' && c <= '9') {
/*  774 */           ValueSet vs = getValue(v, s, i);
/*  775 */           end = vs.value;
/*  776 */           i = vs.pos;
/*      */         } 
/*  778 */         if (i < s.length() && (c = s.charAt(i)) == '/') {
/*  779 */           i++;
/*  780 */           c = s.charAt(i);
/*  781 */           int j = Integer.parseInt(String.valueOf(c));
/*  782 */           i++;
/*  783 */           if (i >= s.length()) {
/*  784 */             addToSet(val, end, j, type);
/*  785 */             return i;
/*      */           } 
/*  787 */           c = s.charAt(i);
/*  788 */           if (c >= '0' && c <= '9') {
/*  789 */             ValueSet vs = getValue(j, s, i);
/*  790 */             int v3 = vs.value;
/*  791 */             addToSet(val, end, v3, type);
/*  792 */             i = vs.pos;
/*      */           } else {
/*  794 */             addToSet(val, end, j, type);
/*      */           } 
/*  796 */           return i;
/*      */         } 
/*  798 */         addToSet(val, end, 1, type);
/*  799 */         return i;
/*      */       
/*      */       case '/':
/*  802 */         i++;
/*  803 */         c = s.charAt(i);
/*  804 */         v2 = Integer.parseInt(String.valueOf(c));
/*  805 */         i++;
/*  806 */         if (i >= s.length()) {
/*  807 */           addToSet(val, end, v2, type);
/*  808 */           return i;
/*      */         } 
/*  810 */         c = s.charAt(i);
/*  811 */         if (c >= '0' && c <= '9') {
/*  812 */           ValueSet vs = getValue(v2, s, i);
/*  813 */           int v3 = vs.value;
/*  814 */           addToSet(val, end, v3, type);
/*  815 */           i = vs.pos;
/*  816 */           return i;
/*      */         } 
/*  818 */         throw new ParseException("Unexpected character '" + c + "' after '/'", i);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  824 */     addToSet(val, end, 0, type);
/*  825 */     i++;
/*  826 */     return i;
/*      */   }
/*      */   
/*      */   public String getCronExpression() {
/*  830 */     return this.cronExpression;
/*      */   }
/*      */   
/*      */   public String getExpressionSummary() {
/*  834 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  836 */     buf.append("seconds: ");
/*  837 */     buf.append(getExpressionSetSummary(this.seconds));
/*  838 */     buf.append("\n");
/*  839 */     buf.append("minutes: ");
/*  840 */     buf.append(getExpressionSetSummary(this.minutes));
/*  841 */     buf.append("\n");
/*  842 */     buf.append("hours: ");
/*  843 */     buf.append(getExpressionSetSummary(this.hours));
/*  844 */     buf.append("\n");
/*  845 */     buf.append("daysOfMonth: ");
/*  846 */     buf.append(getExpressionSetSummary(this.daysOfMonth));
/*  847 */     buf.append("\n");
/*  848 */     buf.append("months: ");
/*  849 */     buf.append(getExpressionSetSummary(this.months));
/*  850 */     buf.append("\n");
/*  851 */     buf.append("daysOfWeek: ");
/*  852 */     buf.append(getExpressionSetSummary(this.daysOfWeek));
/*  853 */     buf.append("\n");
/*  854 */     buf.append("lastdayOfWeek: ");
/*  855 */     buf.append(this.lastdayOfWeek);
/*  856 */     buf.append("\n");
/*  857 */     buf.append("nearestWeekday: ");
/*  858 */     buf.append(this.nearestWeekday);
/*  859 */     buf.append("\n");
/*  860 */     buf.append("NthDayOfWeek: ");
/*  861 */     buf.append(this.nthdayOfWeek);
/*  862 */     buf.append("\n");
/*  863 */     buf.append("lastdayOfMonth: ");
/*  864 */     buf.append(this.lastdayOfMonth);
/*  865 */     buf.append("\n");
/*  866 */     buf.append("years: ");
/*  867 */     buf.append(getExpressionSetSummary(this.years));
/*  868 */     buf.append("\n");
/*      */     
/*  870 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getExpressionSetSummary(Set<Integer> set) {
/*  875 */     if (set.contains(NO_SPEC)) {
/*  876 */       return "?";
/*      */     }
/*  878 */     if (set.contains(ALL_SPEC)) {
/*  879 */       return "*";
/*      */     }
/*      */     
/*  882 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  884 */     Iterator<Integer> itr = set.iterator();
/*  885 */     boolean first = true;
/*  886 */     while (itr.hasNext()) {
/*  887 */       Integer iVal = itr.next();
/*  888 */       String val = iVal.toString();
/*  889 */       if (!first) {
/*  890 */         buf.append(",");
/*      */       }
/*  892 */       buf.append(val);
/*  893 */       first = false;
/*      */     } 
/*      */     
/*  896 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getExpressionSetSummary(ArrayList<Integer> list) {
/*  901 */     if (list.contains(NO_SPEC)) {
/*  902 */       return "?";
/*      */     }
/*  904 */     if (list.contains(ALL_SPEC)) {
/*  905 */       return "*";
/*      */     }
/*      */     
/*  908 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  910 */     Iterator<Integer> itr = list.iterator();
/*  911 */     boolean first = true;
/*  912 */     while (itr.hasNext()) {
/*  913 */       Integer iVal = itr.next();
/*  914 */       String val = iVal.toString();
/*  915 */       if (!first) {
/*  916 */         buf.append(",");
/*      */       }
/*  918 */       buf.append(val);
/*  919 */       first = false;
/*      */     } 
/*      */     
/*  922 */     return buf.toString();
/*      */   }
/*      */   
/*      */   protected int skipWhiteSpace(int i, String s) {
/*  926 */     for (; i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t'); i++);
/*      */ 
/*      */ 
/*      */     
/*  930 */     return i;
/*      */   }
/*      */   
/*      */   protected int findNextWhiteSpace(int i, String s) {
/*  934 */     for (; i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t'); i++);
/*      */ 
/*      */ 
/*      */     
/*  938 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addToSet(int val, int end, int incr, int type) throws ParseException {
/*  944 */     TreeSet<Integer> set = getSet(type);
/*      */     
/*  946 */     switch (type) {
/*      */       case 0:
/*      */       case 1:
/*  949 */         if ((val < 0 || val > 59 || end > 59) && val != 99) {
/*  950 */           throw new ParseException("Minute and Second values must be between 0 and 59", -1);
/*      */         }
/*      */         break;
/*      */ 
/*      */       
/*      */       case 2:
/*  956 */         if ((val < 0 || val > 23 || end > 23) && val != 99) {
/*  957 */           throw new ParseException("Hour values must be between 0 and 23", -1);
/*      */         }
/*      */         break;
/*      */       
/*      */       case 3:
/*  962 */         if ((val < 1 || val > 31 || end > 31) && val != 99 && val != 98)
/*      */         {
/*  964 */           throw new ParseException("Day of month values must be between 1 and 31", -1);
/*      */         }
/*      */         break;
/*      */       
/*      */       case 4:
/*  969 */         if ((val < 1 || val > 12 || end > 12) && val != 99) {
/*  970 */           throw new ParseException("Month values must be between 1 and 12", -1);
/*      */         }
/*      */         break;
/*      */       
/*      */       case 5:
/*  975 */         if ((val == 0 || val > 7 || end > 7) && val != 99 && val != 98)
/*      */         {
/*  977 */           throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
/*      */         }
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  985 */     if ((incr == 0 || incr == -1) && val != 99) {
/*  986 */       if (val != -1) {
/*  987 */         set.add(Integer.valueOf(val));
/*      */       } else {
/*  989 */         set.add(NO_SPEC);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  995 */     int startAt = val;
/*  996 */     int stopAt = end;
/*      */     
/*  998 */     if (val == 99 && incr <= 0) {
/*  999 */       incr = 1;
/* 1000 */       set.add(ALL_SPEC);
/*      */     } 
/*      */     
/* 1003 */     switch (type) {
/*      */       case 0:
/*      */       case 1:
/* 1006 */         if (stopAt == -1) {
/* 1007 */           stopAt = 59;
/*      */         }
/* 1009 */         if (startAt == -1 || startAt == 99) {
/* 1010 */           startAt = 0;
/*      */         }
/*      */         break;
/*      */       case 2:
/* 1014 */         if (stopAt == -1) {
/* 1015 */           stopAt = 23;
/*      */         }
/* 1017 */         if (startAt == -1 || startAt == 99) {
/* 1018 */           startAt = 0;
/*      */         }
/*      */         break;
/*      */       case 3:
/* 1022 */         if (stopAt == -1) {
/* 1023 */           stopAt = 31;
/*      */         }
/* 1025 */         if (startAt == -1 || startAt == 99) {
/* 1026 */           startAt = 1;
/*      */         }
/*      */         break;
/*      */       case 4:
/* 1030 */         if (stopAt == -1) {
/* 1031 */           stopAt = 12;
/*      */         }
/* 1033 */         if (startAt == -1 || startAt == 99) {
/* 1034 */           startAt = 1;
/*      */         }
/*      */         break;
/*      */       case 5:
/* 1038 */         if (stopAt == -1) {
/* 1039 */           stopAt = 7;
/*      */         }
/* 1041 */         if (startAt == -1 || startAt == 99) {
/* 1042 */           startAt = 1;
/*      */         }
/*      */         break;
/*      */       case 6:
/* 1046 */         if (stopAt == -1) {
/* 1047 */           stopAt = MAX_YEAR;
/*      */         }
/* 1049 */         if (startAt == -1 || startAt == 99) {
/* 1050 */           startAt = 1970;
/*      */         }
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1060 */     int max = -1;
/* 1061 */     if (stopAt < startAt) {
/* 1062 */       switch (type) {
/*      */         case 0:
/* 1064 */           max = 60;
/*      */           break;
/*      */         case 1:
/* 1067 */           max = 60;
/*      */           break;
/*      */         case 2:
/* 1070 */           max = 24;
/*      */           break;
/*      */         case 4:
/* 1073 */           max = 12;
/*      */           break;
/*      */         case 5:
/* 1076 */           max = 7;
/*      */           break;
/*      */         case 3:
/* 1079 */           max = 31;
/*      */           break;
/*      */         case 6:
/* 1082 */           throw new IllegalArgumentException("Start year must be less than stop year");
/*      */         default:
/* 1084 */           throw new IllegalArgumentException("Unexpected type encountered");
/*      */       } 
/* 1086 */       stopAt += max;
/*      */     } 
/*      */     int i;
/* 1089 */     for (i = startAt; i <= stopAt; i += incr) {
/* 1090 */       if (max == -1) {
/*      */         
/* 1092 */         set.add(Integer.valueOf(i));
/*      */       } else {
/*      */         
/* 1095 */         int i2 = i % max;
/*      */ 
/*      */         
/* 1098 */         if (i2 == 0 && (type == 4 || type == 5 || type == 3)) {
/* 1099 */           i2 = max;
/*      */         }
/*      */         
/* 1102 */         set.add(Integer.valueOf(i2));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   TreeSet<Integer> getSet(int type) {
/* 1108 */     switch (type) {
/*      */       case 0:
/* 1110 */         return this.seconds;
/*      */       case 1:
/* 1112 */         return this.minutes;
/*      */       case 2:
/* 1114 */         return this.hours;
/*      */       case 3:
/* 1116 */         return this.daysOfMonth;
/*      */       case 4:
/* 1118 */         return this.months;
/*      */       case 5:
/* 1120 */         return this.daysOfWeek;
/*      */       case 6:
/* 1122 */         return this.years;
/*      */     } 
/* 1124 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ValueSet getValue(int v, String s, int i) {
/* 1129 */     char c = s.charAt(i);
/* 1130 */     StringBuilder s1 = new StringBuilder(String.valueOf(v));
/* 1131 */     while (c >= '0' && c <= '9') {
/* 1132 */       s1.append(c);
/* 1133 */       i++;
/* 1134 */       if (i >= s.length()) {
/*      */         break;
/*      */       }
/* 1137 */       c = s.charAt(i);
/*      */     } 
/* 1139 */     ValueSet val = new ValueSet();
/*      */     
/* 1141 */     val.pos = (i < s.length()) ? i : (i + 1);
/* 1142 */     val.value = Integer.parseInt(s1.toString());
/* 1143 */     return val;
/*      */   }
/*      */   
/*      */   protected int getNumericValue(String s, int i) {
/* 1147 */     int endOfVal = findNextWhiteSpace(i, s);
/* 1148 */     String val = s.substring(i, endOfVal);
/* 1149 */     return Integer.parseInt(val);
/*      */   }
/*      */   
/*      */   protected int getMonthNumber(String s) {
/* 1153 */     Integer integer = monthMap.get(s);
/*      */     
/* 1155 */     if (integer == null) {
/* 1156 */       return -1;
/*      */     }
/*      */     
/* 1159 */     return integer.intValue();
/*      */   }
/*      */   
/*      */   protected int getDayOfWeekNumber(String s) {
/* 1163 */     Integer integer = dayMap.get(s);
/*      */     
/* 1165 */     if (integer == null) {
/* 1166 */       return -1;
/*      */     }
/*      */     
/* 1169 */     return integer.intValue();
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
/*      */   public Date getTimeAfter(Date afterTime) {
/* 1181 */     Calendar cl = new GregorianCalendar(getTimeZone());
/*      */ 
/*      */ 
/*      */     
/* 1185 */     afterTime = new Date(afterTime.getTime() + 1000L);
/*      */     
/* 1187 */     cl.setTime(afterTime);
/* 1188 */     cl.set(14, 0);
/*      */     
/* 1190 */     boolean gotOne = false;
/*      */     
/* 1192 */     while (!gotOne) {
/*      */       
/* 1194 */       if (cl.get(1) > 2999) {
/* 1195 */         return null;
/*      */       }
/*      */       
/* 1198 */       int sec = cl.get(13);
/* 1199 */       int min = cl.get(12);
/*      */ 
/*      */       
/* 1202 */       SortedSet<Integer> st = this.seconds.tailSet(Integer.valueOf(sec));
/* 1203 */       if (st != null && st.size() != 0) {
/* 1204 */         sec = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1206 */         sec = ((Integer)this.seconds.first()).intValue();
/* 1207 */         min++;
/* 1208 */         cl.set(12, min);
/*      */       } 
/* 1210 */       cl.set(13, sec);
/*      */       
/* 1212 */       min = cl.get(12);
/* 1213 */       int hr = cl.get(11);
/* 1214 */       int t = -1;
/*      */ 
/*      */       
/* 1217 */       st = this.minutes.tailSet(Integer.valueOf(min));
/* 1218 */       if (st != null && st.size() != 0) {
/* 1219 */         t = min;
/* 1220 */         min = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1222 */         min = ((Integer)this.minutes.first()).intValue();
/* 1223 */         hr++;
/*      */       } 
/* 1225 */       if (min != t) {
/* 1226 */         cl.set(13, 0);
/* 1227 */         cl.set(12, min);
/* 1228 */         setCalendarHour(cl, hr);
/*      */         continue;
/*      */       } 
/* 1231 */       cl.set(12, min);
/*      */       
/* 1233 */       hr = cl.get(11);
/* 1234 */       int day = cl.get(5);
/* 1235 */       t = -1;
/*      */ 
/*      */       
/* 1238 */       st = this.hours.tailSet(Integer.valueOf(hr));
/* 1239 */       if (st != null && st.size() != 0) {
/* 1240 */         t = hr;
/* 1241 */         hr = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1243 */         hr = ((Integer)this.hours.first()).intValue();
/* 1244 */         day++;
/*      */       } 
/* 1246 */       if (hr != t) {
/* 1247 */         cl.set(13, 0);
/* 1248 */         cl.set(12, 0);
/* 1249 */         cl.set(5, day);
/* 1250 */         setCalendarHour(cl, hr);
/*      */         continue;
/*      */       } 
/* 1253 */       cl.set(11, hr);
/*      */       
/* 1255 */       day = cl.get(5);
/* 1256 */       int mon = cl.get(2) + 1;
/*      */ 
/*      */       
/* 1259 */       t = -1;
/* 1260 */       int tmon = mon;
/*      */ 
/*      */       
/* 1263 */       boolean dayOfMSpec = !this.daysOfMonth.contains(NO_SPEC);
/* 1264 */       boolean dayOfWSpec = !this.daysOfWeek.contains(NO_SPEC);
/* 1265 */       if (dayOfMSpec && !dayOfWSpec) {
/* 1266 */         st = this.daysOfMonth.tailSet(Integer.valueOf(day));
/* 1267 */         if (this.lastdayOfMonth) {
/* 1268 */           if (!this.nearestWeekday) {
/* 1269 */             t = day;
/* 1270 */             day = getLastDayOfMonth(mon, cl.get(1));
/* 1271 */             day -= this.lastdayOffset;
/* 1272 */             if (t > day) {
/* 1273 */               mon++;
/* 1274 */               if (mon > 12) {
/* 1275 */                 mon = 1;
/* 1276 */                 tmon = 3333;
/* 1277 */                 cl.add(1, 1);
/*      */               } 
/* 1279 */               day = 1;
/*      */             } 
/*      */           } else {
/* 1282 */             t = day;
/* 1283 */             day = getLastDayOfMonth(mon, cl.get(1));
/* 1284 */             day -= this.lastdayOffset;
/*      */             
/* 1286 */             Calendar tcal = Calendar.getInstance(getTimeZone());
/* 1287 */             tcal.set(13, 0);
/* 1288 */             tcal.set(12, 0);
/* 1289 */             tcal.set(11, 0);
/* 1290 */             tcal.set(5, day);
/* 1291 */             tcal.set(2, mon - 1);
/* 1292 */             tcal.set(1, cl.get(1));
/*      */             
/* 1294 */             int ldom = getLastDayOfMonth(mon, cl.get(1));
/* 1295 */             int dow = tcal.get(7);
/*      */             
/* 1297 */             if (dow == 7 && day == 1) {
/* 1298 */               day += 2;
/* 1299 */             } else if (dow == 7) {
/* 1300 */               day--;
/* 1301 */             } else if (dow == 1 && day == ldom) {
/* 1302 */               day -= 2;
/* 1303 */             } else if (dow == 1) {
/* 1304 */               day++;
/*      */             } 
/*      */             
/* 1307 */             tcal.set(13, sec);
/* 1308 */             tcal.set(12, min);
/* 1309 */             tcal.set(11, hr);
/* 1310 */             tcal.set(5, day);
/* 1311 */             tcal.set(2, mon - 1);
/* 1312 */             Date nTime = tcal.getTime();
/* 1313 */             if (nTime.before(afterTime)) {
/* 1314 */               day = 1;
/* 1315 */               mon++;
/*      */             } 
/*      */           } 
/* 1318 */         } else if (this.nearestWeekday) {
/* 1319 */           t = day;
/* 1320 */           day = ((Integer)this.daysOfMonth.first()).intValue();
/*      */           
/* 1322 */           Calendar tcal = Calendar.getInstance(getTimeZone());
/* 1323 */           tcal.set(13, 0);
/* 1324 */           tcal.set(12, 0);
/* 1325 */           tcal.set(11, 0);
/* 1326 */           tcal.set(5, day);
/* 1327 */           tcal.set(2, mon - 1);
/* 1328 */           tcal.set(1, cl.get(1));
/*      */           
/* 1330 */           int ldom = getLastDayOfMonth(mon, cl.get(1));
/* 1331 */           int dow = tcal.get(7);
/*      */           
/* 1333 */           if (dow == 7 && day == 1) {
/* 1334 */             day += 2;
/* 1335 */           } else if (dow == 7) {
/* 1336 */             day--;
/* 1337 */           } else if (dow == 1 && day == ldom) {
/* 1338 */             day -= 2;
/* 1339 */           } else if (dow == 1) {
/* 1340 */             day++;
/*      */           } 
/*      */ 
/*      */           
/* 1344 */           tcal.set(13, sec);
/* 1345 */           tcal.set(12, min);
/* 1346 */           tcal.set(11, hr);
/* 1347 */           tcal.set(5, day);
/* 1348 */           tcal.set(2, mon - 1);
/* 1349 */           Date nTime = tcal.getTime();
/* 1350 */           if (nTime.before(afterTime)) {
/* 1351 */             day = ((Integer)this.daysOfMonth.first()).intValue();
/* 1352 */             mon++;
/*      */           } 
/* 1354 */         } else if (st != null && st.size() != 0) {
/* 1355 */           t = day;
/* 1356 */           day = ((Integer)st.first()).intValue();
/*      */           
/* 1358 */           int lastDay = getLastDayOfMonth(mon, cl.get(1));
/* 1359 */           if (day > lastDay) {
/* 1360 */             day = ((Integer)this.daysOfMonth.first()).intValue();
/* 1361 */             mon++;
/*      */           } 
/*      */         } else {
/* 1364 */           day = ((Integer)this.daysOfMonth.first()).intValue();
/* 1365 */           mon++;
/*      */         } 
/*      */         
/* 1368 */         if (day != t || mon != tmon) {
/* 1369 */           cl.set(13, 0);
/* 1370 */           cl.set(12, 0);
/* 1371 */           cl.set(11, 0);
/* 1372 */           cl.set(5, day);
/* 1373 */           cl.set(2, mon - 1);
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/* 1378 */       } else if (dayOfWSpec && !dayOfMSpec) {
/* 1379 */         if (this.lastdayOfWeek) {
/*      */           
/* 1381 */           int dow = ((Integer)this.daysOfWeek.first()).intValue();
/*      */           
/* 1383 */           int cDow = cl.get(7);
/* 1384 */           int daysToAdd = 0;
/* 1385 */           if (cDow < dow) {
/* 1386 */             daysToAdd = dow - cDow;
/*      */           }
/* 1388 */           if (cDow > dow) {
/* 1389 */             daysToAdd = dow + 7 - cDow;
/*      */           }
/*      */           
/* 1392 */           int lDay = getLastDayOfMonth(mon, cl.get(1));
/*      */           
/* 1394 */           if (day + daysToAdd > lDay) {
/*      */             
/* 1396 */             cl.set(13, 0);
/* 1397 */             cl.set(12, 0);
/* 1398 */             cl.set(11, 0);
/* 1399 */             cl.set(5, 1);
/* 1400 */             cl.set(2, mon);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 1406 */           while (day + daysToAdd + 7 <= lDay) {
/* 1407 */             daysToAdd += 7;
/*      */           }
/*      */           
/* 1410 */           day += daysToAdd;
/*      */           
/* 1412 */           if (daysToAdd > 0) {
/* 1413 */             cl.set(13, 0);
/* 1414 */             cl.set(12, 0);
/* 1415 */             cl.set(11, 0);
/* 1416 */             cl.set(5, day);
/* 1417 */             cl.set(2, mon - 1);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/* 1422 */         } else if (this.nthdayOfWeek != 0) {
/*      */           
/* 1424 */           int dow = ((Integer)this.daysOfWeek.first()).intValue();
/*      */           
/* 1426 */           int cDow = cl.get(7);
/* 1427 */           int daysToAdd = 0;
/* 1428 */           if (cDow < dow) {
/* 1429 */             daysToAdd = dow - cDow;
/* 1430 */           } else if (cDow > dow) {
/* 1431 */             daysToAdd = dow + 7 - cDow;
/*      */           } 
/*      */           
/* 1434 */           boolean dayShifted = false;
/* 1435 */           if (daysToAdd > 0) {
/* 1436 */             dayShifted = true;
/*      */           }
/*      */           
/* 1439 */           day += daysToAdd;
/* 1440 */           int weekOfMonth = day / 7;
/* 1441 */           if (day % 7 > 0) {
/* 1442 */             weekOfMonth++;
/*      */           }
/*      */           
/* 1445 */           daysToAdd = (this.nthdayOfWeek - weekOfMonth) * 7;
/* 1446 */           day += daysToAdd;
/* 1447 */           if (daysToAdd < 0 || day > 
/* 1448 */             getLastDayOfMonth(mon, cl
/* 1449 */               .get(1))) {
/* 1450 */             cl.set(13, 0);
/* 1451 */             cl.set(12, 0);
/* 1452 */             cl.set(11, 0);
/* 1453 */             cl.set(5, 1);
/* 1454 */             cl.set(2, mon);
/*      */             continue;
/*      */           } 
/* 1457 */           if (daysToAdd > 0 || dayShifted) {
/* 1458 */             cl.set(13, 0);
/* 1459 */             cl.set(12, 0);
/* 1460 */             cl.set(11, 0);
/* 1461 */             cl.set(5, day);
/* 1462 */             cl.set(2, mon - 1);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } else {
/* 1467 */           int cDow = cl.get(7);
/* 1468 */           int dow = ((Integer)this.daysOfWeek.first()).intValue();
/*      */           
/* 1470 */           st = this.daysOfWeek.tailSet(Integer.valueOf(cDow));
/* 1471 */           if (st != null && st.size() > 0) {
/* 1472 */             dow = ((Integer)st.first()).intValue();
/*      */           }
/*      */           
/* 1475 */           int daysToAdd = 0;
/* 1476 */           if (cDow < dow) {
/* 1477 */             daysToAdd = dow - cDow;
/*      */           }
/* 1479 */           if (cDow > dow) {
/* 1480 */             daysToAdd = dow + 7 - cDow;
/*      */           }
/*      */           
/* 1483 */           int lDay = getLastDayOfMonth(mon, cl.get(1));
/*      */           
/* 1485 */           if (day + daysToAdd > lDay) {
/*      */             
/* 1487 */             cl.set(13, 0);
/* 1488 */             cl.set(12, 0);
/* 1489 */             cl.set(11, 0);
/* 1490 */             cl.set(5, 1);
/* 1491 */             cl.set(2, mon);
/*      */             continue;
/*      */           } 
/* 1494 */           if (daysToAdd > 0) {
/* 1495 */             cl.set(13, 0);
/* 1496 */             cl.set(12, 0);
/* 1497 */             cl.set(11, 0);
/* 1498 */             cl.set(5, day + daysToAdd);
/* 1499 */             cl.set(2, mon - 1);
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1506 */         throw new UnsupportedOperationException("Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
/*      */       } 
/*      */       
/* 1509 */       cl.set(5, day);
/*      */       
/* 1511 */       mon = cl.get(2) + 1;
/*      */ 
/*      */       
/* 1514 */       int year = cl.get(1);
/* 1515 */       t = -1;
/*      */ 
/*      */ 
/*      */       
/* 1519 */       if (year > MAX_YEAR) {
/* 1520 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1524 */       st = this.months.tailSet(Integer.valueOf(mon));
/* 1525 */       if (st != null && st.size() != 0) {
/* 1526 */         t = mon;
/* 1527 */         mon = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1529 */         mon = ((Integer)this.months.first()).intValue();
/* 1530 */         year++;
/*      */       } 
/* 1532 */       if (mon != t) {
/* 1533 */         cl.set(13, 0);
/* 1534 */         cl.set(12, 0);
/* 1535 */         cl.set(11, 0);
/* 1536 */         cl.set(5, 1);
/* 1537 */         cl.set(2, mon - 1);
/*      */ 
/*      */         
/* 1540 */         cl.set(1, year);
/*      */         continue;
/*      */       } 
/* 1543 */       cl.set(2, mon - 1);
/*      */ 
/*      */ 
/*      */       
/* 1547 */       year = cl.get(1);
/* 1548 */       t = -1;
/*      */ 
/*      */       
/* 1551 */       st = this.years.tailSet(Integer.valueOf(year));
/* 1552 */       if (st != null && st.size() != 0) {
/* 1553 */         t = year;
/* 1554 */         year = ((Integer)st.first()).intValue();
/*      */       } else {
/* 1556 */         return null;
/*      */       } 
/*      */       
/* 1559 */       if (year != t) {
/* 1560 */         cl.set(13, 0);
/* 1561 */         cl.set(12, 0);
/* 1562 */         cl.set(11, 0);
/* 1563 */         cl.set(5, 1);
/* 1564 */         cl.set(2, 0);
/*      */ 
/*      */         
/* 1567 */         cl.set(1, year);
/*      */         continue;
/*      */       } 
/* 1570 */       cl.set(1, year);
/*      */       
/* 1572 */       gotOne = true;
/*      */     } 
/*      */     
/* 1575 */     return cl.getTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setCalendarHour(Calendar cal, int hour) {
/* 1586 */     cal.set(11, hour);
/* 1587 */     if (cal.get(11) != hour && hour != 24) {
/* 1588 */       cal.set(11, hour + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Date getTimeBefore(Date targetDate) {
/* 1593 */     Calendar cl = Calendar.getInstance(getTimeZone());
/*      */ 
/*      */     
/* 1596 */     cl.setTime(targetDate);
/* 1597 */     cl.set(14, 0);
/* 1598 */     Date targetDateNoMs = cl.getTime();
/*      */ 
/*      */     
/* 1601 */     Date start = targetDateNoMs;
/* 1602 */     long minIncrement = findMinIncrement();
/*      */     
/*      */     while (true) {
/* 1605 */       Date prevCheckDate = new Date(start.getTime() - minIncrement);
/* 1606 */       Date prevFireTime = getTimeAfter(prevCheckDate);
/* 1607 */       if (prevFireTime == null || prevFireTime.before(MIN_DATE)) {
/* 1608 */         return null;
/*      */       }
/* 1610 */       start = prevCheckDate;
/* 1611 */       if (prevFireTime.compareTo(targetDateNoMs) < 0)
/* 1612 */         return prevFireTime; 
/*      */     } 
/*      */   }
/*      */   public Date getPrevFireTime(Date targetDate) {
/* 1616 */     return getTimeBefore(targetDate);
/*      */   }
/*      */   
/*      */   private long findMinIncrement() {
/* 1620 */     if (this.seconds.size() != 1)
/* 1621 */       return (minInSet(this.seconds) * 1000); 
/* 1622 */     if (((Integer)this.seconds.first()).intValue() == 99) {
/* 1623 */       return 1000L;
/*      */     }
/* 1625 */     if (this.minutes.size() != 1)
/* 1626 */       return (minInSet(this.minutes) * 60000); 
/* 1627 */     if (((Integer)this.minutes.first()).intValue() == 99) {
/* 1628 */       return 60000L;
/*      */     }
/* 1630 */     if (this.hours.size() != 1)
/* 1631 */       return (minInSet(this.hours) * 3600000); 
/* 1632 */     if (((Integer)this.hours.first()).intValue() == 99) {
/* 1633 */       return 3600000L;
/*      */     }
/* 1635 */     return 86400000L;
/*      */   }
/*      */   
/*      */   private int minInSet(TreeSet<Integer> set) {
/* 1639 */     int previous = 0;
/* 1640 */     int min = Integer.MAX_VALUE;
/* 1641 */     boolean first = true;
/* 1642 */     for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext(); ) { int value = ((Integer)iterator.next()).intValue();
/* 1643 */       if (first) {
/* 1644 */         previous = value;
/* 1645 */         first = false;
/*      */         continue;
/*      */       } 
/* 1648 */       int diff = value - previous;
/* 1649 */       if (diff < min) {
/* 1650 */         min = diff;
/*      */       } }
/*      */ 
/*      */     
/* 1654 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getFinalFireTime() {
/* 1663 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean isLeapYear(int year) {
/* 1667 */     return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getLastDayOfMonth(int monthNum, int year) {
/* 1672 */     switch (monthNum) {
/*      */       case 1:
/* 1674 */         return 31;
/*      */       case 2:
/* 1676 */         return isLeapYear(year) ? 29 : 28;
/*      */       case 3:
/* 1678 */         return 31;
/*      */       case 4:
/* 1680 */         return 30;
/*      */       case 5:
/* 1682 */         return 31;
/*      */       case 6:
/* 1684 */         return 30;
/*      */       case 7:
/* 1686 */         return 31;
/*      */       case 8:
/* 1688 */         return 31;
/*      */       case 9:
/* 1690 */         return 30;
/*      */       case 10:
/* 1692 */         return 31;
/*      */       case 11:
/* 1694 */         return 30;
/*      */       case 12:
/* 1696 */         return 31;
/*      */     } 
/* 1698 */     throw new IllegalArgumentException("Illegal month number: " + monthNum);
/*      */   }
/*      */   
/*      */   private class ValueSet {
/*      */     public int value;
/*      */     public int pos;
/*      */     
/*      */     private ValueSet() {}
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\CronExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */