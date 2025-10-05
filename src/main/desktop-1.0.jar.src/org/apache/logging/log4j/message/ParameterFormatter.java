/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ParameterFormatter
/*     */ {
/*     */   static final String RECURSION_PREFIX = "[...";
/*     */   static final String RECURSION_SUFFIX = "...]";
/*     */   static final String ERROR_PREFIX = "[!!!";
/*     */   static final String ERROR_SEPARATOR = "=>";
/*     */   static final String ERROR_MSG_SEPARATOR = ":";
/*     */   static final String ERROR_SUFFIX = "!!!]";
/*     */   private static final char DELIM_START = '{';
/*     */   private static final char DELIM_STOP = '}';
/*     */   private static final char ESCAPE_CHAR = '\\';
/*  65 */   private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_REF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int countArgumentPlaceholders(String messagePattern) {
/*  77 */     if (messagePattern == null) {
/*  78 */       return 0;
/*     */     }
/*  80 */     int length = messagePattern.length();
/*  81 */     int result = 0;
/*  82 */     boolean isEscaped = false;
/*  83 */     for (int i = 0; i < length - 1; i++) {
/*  84 */       char curChar = messagePattern.charAt(i);
/*  85 */       if (curChar == '\\') {
/*  86 */         isEscaped = !isEscaped;
/*  87 */       } else if (curChar == '{') {
/*  88 */         if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
/*  89 */           result++;
/*  90 */           i++;
/*     */         } 
/*  92 */         isEscaped = false;
/*     */       } else {
/*  94 */         isEscaped = false;
/*     */       } 
/*     */     } 
/*  97 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int countArgumentPlaceholders2(String messagePattern, int[] indices) {
/* 107 */     if (messagePattern == null) {
/* 108 */       return 0;
/*     */     }
/* 110 */     int length = messagePattern.length();
/* 111 */     int result = 0;
/* 112 */     boolean isEscaped = false;
/* 113 */     for (int i = 0; i < length - 1; i++) {
/* 114 */       char curChar = messagePattern.charAt(i);
/* 115 */       if (curChar == '\\') {
/* 116 */         isEscaped = !isEscaped;
/* 117 */         indices[0] = -1;
/* 118 */         result++;
/* 119 */       } else if (curChar == '{') {
/* 120 */         if (!isEscaped && messagePattern.charAt(i + 1) == '}') {
/* 121 */           indices[result] = i;
/* 122 */           result++;
/* 123 */           i++;
/*     */         } 
/* 125 */         isEscaped = false;
/*     */       } else {
/* 127 */         isEscaped = false;
/*     */       } 
/*     */     } 
/* 130 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int countArgumentPlaceholders3(char[] messagePattern, int length, int[] indices) {
/* 140 */     int result = 0;
/* 141 */     boolean isEscaped = false;
/* 142 */     for (int i = 0; i < length - 1; i++) {
/* 143 */       char curChar = messagePattern[i];
/* 144 */       if (curChar == '\\') {
/* 145 */         isEscaped = !isEscaped;
/* 146 */       } else if (curChar == '{') {
/* 147 */         if (!isEscaped && messagePattern[i + 1] == '}') {
/* 148 */           indices[result] = i;
/* 149 */           result++;
/* 150 */           i++;
/*     */         } 
/* 152 */         isEscaped = false;
/*     */       } else {
/* 154 */         isEscaped = false;
/*     */       } 
/*     */     } 
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String format(String messagePattern, Object[] arguments) {
/* 168 */     StringBuilder result = new StringBuilder();
/* 169 */     int argCount = (arguments == null) ? 0 : arguments.length;
/* 170 */     formatMessage(result, messagePattern, arguments, argCount);
/* 171 */     return result.toString();
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
/*     */   static void formatMessage2(StringBuilder buffer, String messagePattern, Object[] arguments, int argCount, int[] indices) {
/* 183 */     if (messagePattern == null || arguments == null || argCount == 0) {
/* 184 */       buffer.append(messagePattern);
/*     */       return;
/*     */     } 
/* 187 */     int previous = 0;
/* 188 */     for (int i = 0; i < argCount; i++) {
/* 189 */       buffer.append(messagePattern, previous, indices[i]);
/* 190 */       previous = indices[i] + 2;
/* 191 */       recursiveDeepToString(arguments[i], buffer);
/*     */     } 
/* 193 */     buffer.append(messagePattern, previous, messagePattern.length());
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
/*     */   static void formatMessage3(StringBuilder buffer, char[] messagePattern, int patternLength, Object[] arguments, int argCount, int[] indices) {
/* 205 */     if (messagePattern == null) {
/*     */       return;
/*     */     }
/* 208 */     if (arguments == null || argCount == 0) {
/* 209 */       buffer.append(messagePattern);
/*     */       return;
/*     */     } 
/* 212 */     int previous = 0;
/* 213 */     for (int i = 0; i < argCount; i++) {
/* 214 */       buffer.append(messagePattern, previous, indices[i]);
/* 215 */       previous = indices[i] + 2;
/* 216 */       recursiveDeepToString(arguments[i], buffer);
/*     */     } 
/* 218 */     buffer.append(messagePattern, previous, patternLength);
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
/*     */   static void formatMessage(StringBuilder buffer, String messagePattern, Object[] arguments, int argCount) {
/* 230 */     if (messagePattern == null || arguments == null || argCount == 0) {
/* 231 */       buffer.append(messagePattern);
/*     */       return;
/*     */     } 
/* 234 */     int escapeCounter = 0;
/* 235 */     int currentArgument = 0;
/* 236 */     int i = 0;
/* 237 */     int len = messagePattern.length();
/* 238 */     for (; i < len - 1; i++) {
/* 239 */       char curChar = messagePattern.charAt(i);
/* 240 */       if (curChar == '\\') {
/* 241 */         escapeCounter++;
/*     */       } else {
/* 243 */         if (isDelimPair(curChar, messagePattern, i)) {
/* 244 */           i++;
/*     */ 
/*     */           
/* 247 */           writeEscapedEscapeChars(escapeCounter, buffer);
/*     */           
/* 249 */           if (isOdd(escapeCounter)) {
/*     */             
/* 251 */             writeDelimPair(buffer);
/*     */           } else {
/*     */             
/* 254 */             writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
/* 255 */             currentArgument++;
/*     */           } 
/*     */         } else {
/* 258 */           handleLiteralChar(buffer, escapeCounter, curChar);
/*     */         } 
/* 260 */         escapeCounter = 0;
/*     */       } 
/*     */     } 
/* 263 */     handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDelimPair(char curChar, String messagePattern, int curCharIndex) {
/* 273 */     return (curChar == '{' && messagePattern.charAt(curCharIndex + 1) == '}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleRemainingCharIfAny(String messagePattern, int len, StringBuilder buffer, int escapeCounter, int i) {
/* 284 */     if (i == len - 1) {
/* 285 */       char curChar = messagePattern.charAt(i);
/* 286 */       handleLastChar(buffer, escapeCounter, curChar);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void handleLastChar(StringBuilder buffer, int escapeCounter, char curChar) {
/* 296 */     if (curChar == '\\') {
/* 297 */       writeUnescapedEscapeChars(escapeCounter + 1, buffer);
/*     */     } else {
/* 299 */       handleLiteralChar(buffer, escapeCounter, curChar);
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
/*     */   private static void handleLiteralChar(StringBuilder buffer, int escapeCounter, char curChar) {
/* 312 */     writeUnescapedEscapeChars(escapeCounter, buffer);
/* 313 */     buffer.append(curChar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeDelimPair(StringBuilder buffer) {
/* 322 */     buffer.append('{');
/* 323 */     buffer.append('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOdd(int number) {
/* 332 */     return ((number & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeEscapedEscapeChars(int escapeCounter, StringBuilder buffer) {
/* 342 */     int escapedEscapes = escapeCounter >> 1;
/* 343 */     writeUnescapedEscapeChars(escapedEscapes, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeUnescapedEscapeChars(int escapeCounter, StringBuilder buffer) {
/* 353 */     while (escapeCounter > 0) {
/* 354 */       buffer.append('\\');
/* 355 */       escapeCounter--;
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
/*     */   private static void writeArgOrDelimPair(Object[] arguments, int argCount, int currentArgument, StringBuilder buffer) {
/* 367 */     if (currentArgument < argCount) {
/* 368 */       recursiveDeepToString(arguments[currentArgument], buffer);
/*     */     } else {
/* 370 */       writeDelimPair(buffer);
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
/*     */   static String deepToString(Object o) {
/* 393 */     if (o == null) {
/* 394 */       return null;
/*     */     }
/*     */     
/* 397 */     if (o instanceof String) {
/* 398 */       return (String)o;
/*     */     }
/* 400 */     if (o instanceof Integer) {
/* 401 */       return Integer.toString(((Integer)o).intValue());
/*     */     }
/* 403 */     if (o instanceof Long) {
/* 404 */       return Long.toString(((Long)o).longValue());
/*     */     }
/* 406 */     if (o instanceof Double) {
/* 407 */       return Double.toString(((Double)o).doubleValue());
/*     */     }
/* 409 */     if (o instanceof Boolean) {
/* 410 */       return Boolean.toString(((Boolean)o).booleanValue());
/*     */     }
/* 412 */     if (o instanceof Character) {
/* 413 */       return Character.toString(((Character)o).charValue());
/*     */     }
/* 415 */     if (o instanceof Short) {
/* 416 */       return Short.toString(((Short)o).shortValue());
/*     */     }
/* 418 */     if (o instanceof Float) {
/* 419 */       return Float.toString(((Float)o).floatValue());
/*     */     }
/* 421 */     if (o instanceof Byte) {
/* 422 */       return Byte.toString(((Byte)o).byteValue());
/*     */     }
/* 424 */     StringBuilder str = new StringBuilder();
/* 425 */     recursiveDeepToString(o, str);
/* 426 */     return str.toString();
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
/*     */   static void recursiveDeepToString(Object o, StringBuilder str) {
/* 448 */     recursiveDeepToString(o, str, null);
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
/*     */   private static void recursiveDeepToString(Object o, StringBuilder str, Set<Object> dejaVu) {
/* 473 */     if (appendSpecialTypes(o, str)) {
/*     */       return;
/*     */     }
/* 476 */     if (isMaybeRecursive(o)) {
/* 477 */       appendPotentiallyRecursiveValue(o, str, dejaVu);
/*     */     } else {
/* 479 */       tryObjectToString(o, str);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean appendSpecialTypes(Object o, StringBuilder str) {
/* 484 */     return (StringBuilders.appendSpecificTypes(str, o) || appendDate(o, str));
/*     */   }
/*     */   
/*     */   private static boolean appendDate(Object o, StringBuilder str) {
/* 488 */     if (!(o instanceof Date)) {
/* 489 */       return false;
/*     */     }
/* 491 */     Date date = (Date)o;
/* 492 */     SimpleDateFormat format = SIMPLE_DATE_FORMAT_REF.get();
/* 493 */     str.append(format.format(date));
/* 494 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isMaybeRecursive(Object o) {
/* 501 */     return (o.getClass().isArray() || o instanceof Map || o instanceof Collection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendPotentiallyRecursiveValue(Object o, StringBuilder str, Set<Object> dejaVu) {
/* 508 */     Class<?> oClass = o.getClass();
/* 509 */     if (oClass.isArray()) {
/* 510 */       appendArray(o, str, dejaVu, oClass);
/* 511 */     } else if (o instanceof Map) {
/* 512 */       appendMap(o, str, dejaVu);
/* 513 */     } else if (o instanceof Collection) {
/* 514 */       appendCollection(o, str, dejaVu);
/*     */     } else {
/* 516 */       throw new IllegalArgumentException("was expecting a container, found " + oClass);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendArray(Object o, StringBuilder str, Set<Object> dejaVu, Class<?> oClass) {
/* 525 */     if (oClass == byte[].class) {
/* 526 */       str.append(Arrays.toString((byte[])o));
/* 527 */     } else if (oClass == short[].class) {
/* 528 */       str.append(Arrays.toString((short[])o));
/* 529 */     } else if (oClass == int[].class) {
/* 530 */       str.append(Arrays.toString((int[])o));
/* 531 */     } else if (oClass == long[].class) {
/* 532 */       str.append(Arrays.toString((long[])o));
/* 533 */     } else if (oClass == float[].class) {
/* 534 */       str.append(Arrays.toString((float[])o));
/* 535 */     } else if (oClass == double[].class) {
/* 536 */       str.append(Arrays.toString((double[])o));
/* 537 */     } else if (oClass == boolean[].class) {
/* 538 */       str.append(Arrays.toString((boolean[])o));
/* 539 */     } else if (oClass == char[].class) {
/* 540 */       str.append(Arrays.toString((char[])o));
/*     */     } else {
/*     */       
/* 543 */       Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
/* 544 */       boolean seen = !effectiveDejaVu.add(o);
/* 545 */       if (seen) {
/* 546 */         String id = identityToString(o);
/* 547 */         str.append("[...").append(id).append("...]");
/*     */       } else {
/* 549 */         Object[] oArray = (Object[])o;
/* 550 */         str.append('[');
/* 551 */         boolean first = true;
/* 552 */         for (Object current : oArray) {
/* 553 */           if (first) {
/* 554 */             first = false;
/*     */           } else {
/* 556 */             str.append(", ");
/*     */           } 
/* 558 */           recursiveDeepToString(current, str, cloneDejaVu(effectiveDejaVu));
/*     */         } 
/* 560 */         str.append(']');
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
/*     */   private static void appendMap(Object o, StringBuilder str, Set<Object> dejaVu) {
/* 572 */     Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
/* 573 */     boolean seen = !effectiveDejaVu.add(o);
/* 574 */     if (seen) {
/* 575 */       String id = identityToString(o);
/* 576 */       str.append("[...").append(id).append("...]");
/*     */     } else {
/* 578 */       Map<?, ?> oMap = (Map<?, ?>)o;
/* 579 */       str.append('{');
/* 580 */       boolean isFirst = true;
/* 581 */       for (Map.Entry<?, ?> o1 : oMap.entrySet()) {
/* 582 */         Map.Entry<?, ?> current = o1;
/* 583 */         if (isFirst) {
/* 584 */           isFirst = false;
/*     */         } else {
/* 586 */           str.append(", ");
/*     */         } 
/* 588 */         Object key = current.getKey();
/* 589 */         Object value = current.getValue();
/* 590 */         recursiveDeepToString(key, str, cloneDejaVu(effectiveDejaVu));
/* 591 */         str.append('=');
/* 592 */         recursiveDeepToString(value, str, cloneDejaVu(effectiveDejaVu));
/*     */       } 
/* 594 */       str.append('}');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendCollection(Object o, StringBuilder str, Set<Object> dejaVu) {
/* 605 */     Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
/* 606 */     boolean seen = !effectiveDejaVu.add(o);
/* 607 */     if (seen) {
/* 608 */       String id = identityToString(o);
/* 609 */       str.append("[...").append(id).append("...]");
/*     */     } else {
/* 611 */       Collection<?> oCol = (Collection)o;
/* 612 */       str.append('[');
/* 613 */       boolean isFirst = true;
/* 614 */       for (Object anOCol : oCol) {
/* 615 */         if (isFirst) {
/* 616 */           isFirst = false;
/*     */         } else {
/* 618 */           str.append(", ");
/*     */         } 
/* 620 */         recursiveDeepToString(anOCol, str, cloneDejaVu(effectiveDejaVu));
/*     */       } 
/* 622 */       str.append(']');
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Set<Object> getOrCreateDejaVu(Set<Object> dejaVu) {
/* 627 */     return (dejaVu == null) ? 
/* 628 */       createDejaVu() : dejaVu;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<Object> createDejaVu() {
/* 633 */     return Collections.newSetFromMap(new IdentityHashMap<>());
/*     */   }
/*     */   
/*     */   private static Set<Object> cloneDejaVu(Set<Object> dejaVu) {
/* 637 */     Set<Object> clonedDejaVu = createDejaVu();
/* 638 */     clonedDejaVu.addAll(dejaVu);
/* 639 */     return clonedDejaVu;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void tryObjectToString(Object o, StringBuilder str) {
/*     */     try {
/* 645 */       str.append(o.toString());
/* 646 */     } catch (Throwable t) {
/* 647 */       handleErrorInObjectToString(o, str, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void handleErrorInObjectToString(Object o, StringBuilder str, Throwable t) {
/* 652 */     str.append("[!!!");
/* 653 */     str.append(identityToString(o));
/* 654 */     str.append("=>");
/* 655 */     String msg = t.getMessage();
/* 656 */     String className = t.getClass().getName();
/* 657 */     str.append(className);
/* 658 */     if (!className.equals(msg)) {
/* 659 */       str.append(":");
/* 660 */       str.append(msg);
/*     */     } 
/* 662 */     str.append("!!!]");
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
/*     */   static String identityToString(Object obj) {
/* 686 */     if (obj == null) {
/* 687 */       return null;
/*     */     }
/* 689 */     return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\ParameterFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */