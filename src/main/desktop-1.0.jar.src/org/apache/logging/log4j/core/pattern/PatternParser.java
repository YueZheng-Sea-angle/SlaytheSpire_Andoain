/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.util.NanoClock;
/*     */ import org.apache.logging.log4j.core.util.SystemNanoClock;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PatternParser
/*     */ {
/*     */   static final String DISABLE_ANSI = "disableAnsi";
/*     */   static final String NO_CONSOLE_NO_ANSI = "noConsoleNoAnsi";
/*     */   private static final char ESCAPE_CHAR = '%';
/*     */   
/*     */   private enum ParserState
/*     */   {
/*  59 */     LITERAL_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     CONVERTER_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     DOT_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     MIN_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     MAX_STATE;
/*     */   }
/*     */   
/*  82 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final int BUF_SIZE = 32;
/*     */ 
/*     */   
/*     */   private static final int DECIMAL = 10;
/*     */ 
/*     */   
/*     */   private final Configuration config;
/*     */ 
/*     */   
/*     */   private final Map<String, Class<PatternConverter>> converterRules;
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternParser(String converterKey) {
/*  99 */     this(null, converterKey, null, null);
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
/*     */   public PatternParser(Configuration config, String converterKey, Class<?> expected) {
/* 113 */     this(config, converterKey, expected, null);
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
/*     */   public PatternParser(Configuration config, String converterKey, Class<?> expectedClass, Class<?> filterClass) {
/* 130 */     this.config = config;
/* 131 */     PluginManager manager = new PluginManager(converterKey);
/* 132 */     manager.collectPlugins((config == null) ? null : config.getPluginPackages());
/* 133 */     Map<String, PluginType<?>> plugins = manager.getPlugins();
/* 134 */     Map<String, Class<PatternConverter>> converters = new LinkedHashMap<>();
/*     */     
/* 136 */     for (PluginType<?> type : plugins.values()) {
/*     */       
/*     */       try {
/* 139 */         Class<PatternConverter> clazz = type.getPluginClass();
/* 140 */         if (filterClass != null && !filterClass.isAssignableFrom(clazz)) {
/*     */           continue;
/*     */         }
/* 143 */         ConverterKeys keys = clazz.<ConverterKeys>getAnnotation(ConverterKeys.class);
/* 144 */         if (keys != null) {
/* 145 */           for (String key : keys.value()) {
/* 146 */             if (converters.containsKey(key)) {
/* 147 */               LOGGER.warn("Converter key '{}' is already mapped to '{}'. Sorry, Dave, I can't let you do that! Ignoring plugin [{}].", key, converters
/*     */                   
/* 149 */                   .get(key), clazz);
/*     */             } else {
/* 151 */               converters.put(key, clazz);
/*     */             } 
/*     */           } 
/*     */         }
/* 155 */       } catch (Exception ex) {
/* 156 */         LOGGER.error("Error processing plugin " + type.getElementName(), ex);
/*     */       } 
/*     */     } 
/* 159 */     this.converterRules = converters;
/*     */   }
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern) {
/* 163 */     return parse(pattern, false, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean noConsoleNoAnsi) {
/* 168 */     return parse(pattern, alwaysWriteExceptions, false, noConsoleNoAnsi);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PatternFormatter> parse(String pattern, boolean alwaysWriteExceptions, boolean disableAnsi, boolean noConsoleNoAnsi) {
/* 173 */     List<PatternFormatter> list = new ArrayList<>();
/* 174 */     List<PatternConverter> converters = new ArrayList<>();
/* 175 */     List<FormattingInfo> fields = new ArrayList<>();
/*     */     
/* 177 */     parse(pattern, converters, fields, disableAnsi, noConsoleNoAnsi, true);
/*     */     
/* 179 */     Iterator<FormattingInfo> fieldIter = fields.iterator();
/* 180 */     boolean handlesThrowable = false;
/*     */     
/* 182 */     for (PatternConverter converter : converters) {
/* 183 */       LogEventPatternConverter pc; FormattingInfo field; if (converter instanceof NanoTimePatternConverter)
/*     */       {
/*     */         
/* 186 */         if (this.config != null) {
/* 187 */           this.config.setNanoClock((NanoClock)new SystemNanoClock());
/*     */         }
/*     */       }
/*     */       
/* 191 */       if (converter instanceof LogEventPatternConverter) {
/* 192 */         pc = (LogEventPatternConverter)converter;
/* 193 */         handlesThrowable |= pc.handlesThrowable();
/*     */       } else {
/* 195 */         pc = SimpleLiteralPatternConverter.of("");
/*     */       } 
/*     */ 
/*     */       
/* 199 */       if (fieldIter.hasNext()) {
/* 200 */         field = fieldIter.next();
/*     */       } else {
/* 202 */         field = FormattingInfo.getDefault();
/*     */       } 
/* 204 */       list.add(new PatternFormatter(pc, field));
/*     */     } 
/* 206 */     if (alwaysWriteExceptions && !handlesThrowable) {
/* 207 */       LogEventPatternConverter pc = ExtendedThrowablePatternConverter.newInstance(this.config, null);
/* 208 */       list.add(new PatternFormatter(pc, FormattingInfo.getDefault()));
/*     */     } 
/* 210 */     return list;
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
/*     */   private static int extractConverter(char lastChar, String pattern, int start, StringBuilder convBuf, StringBuilder currentLiteral) {
/* 238 */     int i = start;
/* 239 */     convBuf.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 246 */     if (!Character.isUnicodeIdentifierStart(lastChar)) {
/* 247 */       return i;
/*     */     }
/*     */     
/* 250 */     convBuf.append(lastChar);
/*     */     
/* 252 */     while (i < pattern.length() && Character.isUnicodeIdentifierPart(pattern.charAt(i))) {
/* 253 */       convBuf.append(pattern.charAt(i));
/* 254 */       currentLiteral.append(pattern.charAt(i));
/* 255 */       i++;
/*     */     } 
/*     */     
/* 258 */     return i;
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
/*     */   private static int extractOptions(String pattern, int start, List<String> options) {
/* 273 */     int i = start;
/* 274 */     while (i < pattern.length() && pattern.charAt(i) == '{') {
/*     */       
/* 276 */       int begin = ++i;
/* 277 */       int depth = 1;
/* 278 */       while (depth > 0 && i < pattern.length()) {
/* 279 */         char c = pattern.charAt(i);
/* 280 */         if (c == '{') {
/* 281 */           depth++;
/* 282 */         } else if (c == '}') {
/* 283 */           depth--;
/*     */         } 
/*     */         
/* 286 */         i++;
/*     */       } 
/*     */       
/* 289 */       if (depth > 0) {
/* 290 */         i = pattern.lastIndexOf('}');
/* 291 */         if (i == -1 || i < start)
/*     */         {
/*     */           
/* 294 */           return begin;
/*     */         }
/* 296 */         return i + 1;
/*     */       } 
/*     */       
/* 299 */       options.add(pattern.substring(begin, i - 1));
/*     */     } 
/*     */     
/* 302 */     return i;
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
/*     */   public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean noConsoleNoAnsi, boolean convertBackslashes) {
/* 322 */     parse(pattern, patternConverters, formattingInfos, false, noConsoleNoAnsi, convertBackslashes);
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
/*     */   public void parse(String pattern, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean disableAnsi, boolean noConsoleNoAnsi, boolean convertBackslashes) {
/* 344 */     Objects.requireNonNull(pattern, "pattern");
/*     */     
/* 346 */     StringBuilder currentLiteral = new StringBuilder(32);
/*     */     
/* 348 */     int patternLength = pattern.length();
/* 349 */     ParserState state = ParserState.LITERAL_STATE;
/*     */     
/* 351 */     int i = 0;
/* 352 */     FormattingInfo formattingInfo = FormattingInfo.getDefault();
/*     */     
/* 354 */     while (i < patternLength) {
/* 355 */       char c = pattern.charAt(i++);
/*     */       
/* 357 */       switch (state) {
/*     */ 
/*     */         
/*     */         case LITERAL_STATE:
/* 361 */           if (i == patternLength) {
/* 362 */             currentLiteral.append(c);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 367 */           if (c == '%') {
/*     */             
/* 369 */             switch (pattern.charAt(i)) {
/*     */               case '%':
/* 371 */                 currentLiteral.append(c);
/* 372 */                 i++;
/*     */                 continue;
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 378 */             if (currentLiteral.length() != 0) {
/* 379 */               patternConverters.add(literalPattern(currentLiteral.toString(), convertBackslashes));
/* 380 */               formattingInfos.add(FormattingInfo.getDefault());
/*     */             } 
/*     */             
/* 383 */             currentLiteral.setLength(0);
/* 384 */             currentLiteral.append(c);
/* 385 */             state = ParserState.CONVERTER_STATE;
/* 386 */             formattingInfo = FormattingInfo.getDefault();
/*     */             continue;
/*     */           } 
/* 389 */           currentLiteral.append(c);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case CONVERTER_STATE:
/* 395 */           currentLiteral.append(c);
/*     */           
/* 397 */           switch (c) {
/*     */ 
/*     */             
/*     */             case '0':
/* 401 */               formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), true);
/*     */               continue;
/*     */ 
/*     */             
/*     */             case '-':
/* 406 */               formattingInfo = new FormattingInfo(true, formattingInfo.getMinLength(), formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
/*     */               continue;
/*     */             
/*     */             case '.':
/* 410 */               state = ParserState.DOT_STATE;
/*     */               continue;
/*     */           } 
/*     */ 
/*     */           
/* 415 */           if (c >= '0' && c <= '9') {
/*     */             
/* 417 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), c - 48, formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
/* 418 */             state = ParserState.MIN_STATE; continue;
/*     */           } 
/* 420 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
/*     */ 
/*     */ 
/*     */           
/* 424 */           state = ParserState.LITERAL_STATE;
/* 425 */           formattingInfo = FormattingInfo.getDefault();
/* 426 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case MIN_STATE:
/* 433 */           currentLiteral.append(c);
/*     */           
/* 435 */           if (c >= '0' && c <= '9') {
/*     */ 
/*     */             
/* 438 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength() * 10 + c - 48, formattingInfo.getMaxLength(), formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad()); continue;
/* 439 */           }  if (c == '.') {
/* 440 */             state = ParserState.DOT_STATE; continue;
/*     */           } 
/* 442 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
/*     */           
/* 444 */           state = ParserState.LITERAL_STATE;
/* 445 */           formattingInfo = FormattingInfo.getDefault();
/* 446 */           currentLiteral.setLength(0);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case DOT_STATE:
/* 452 */           currentLiteral.append(c);
/* 453 */           switch (c) {
/*     */             
/*     */             case '-':
/* 456 */               formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength(), false, formattingInfo.isZeroPad());
/*     */               continue;
/*     */           } 
/*     */ 
/*     */           
/* 461 */           if (c >= '0' && c <= '9') {
/*     */             
/* 463 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), c - 48, formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad());
/* 464 */             state = ParserState.MAX_STATE; continue;
/*     */           } 
/* 466 */           LOGGER.error("Error occurred in position " + i + ".\n Was expecting digit, instead got char \"" + c + "\".");
/*     */ 
/*     */           
/* 469 */           state = ParserState.LITERAL_STATE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case MAX_STATE:
/* 476 */           currentLiteral.append(c);
/*     */           
/* 478 */           if (c >= '0' && c <= '9') {
/*     */ 
/*     */             
/* 481 */             formattingInfo = new FormattingInfo(formattingInfo.isLeftAligned(), formattingInfo.getMinLength(), formattingInfo.getMaxLength() * 10 + c - 48, formattingInfo.isLeftTruncate(), formattingInfo.isZeroPad()); continue;
/*     */           } 
/* 483 */           i = finalizeConverter(c, pattern, i, currentLiteral, formattingInfo, this.converterRules, patternConverters, formattingInfos, disableAnsi, noConsoleNoAnsi, convertBackslashes);
/*     */           
/* 485 */           state = ParserState.LITERAL_STATE;
/* 486 */           formattingInfo = FormattingInfo.getDefault();
/* 487 */           currentLiteral.setLength(0);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 495 */     if (currentLiteral.length() != 0) {
/* 496 */       patternConverters.add(literalPattern(currentLiteral.toString(), convertBackslashes));
/* 497 */       formattingInfos.add(FormattingInfo.getDefault());
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
/*     */   private PatternConverter createConverter(String converterId, StringBuilder currentLiteral, Map<String, Class<PatternConverter>> rules, List<String> options, boolean disableAnsi, boolean noConsoleNoAnsi) {
/* 522 */     String converterName = converterId;
/* 523 */     Class<PatternConverter> converterClass = null;
/*     */     
/* 525 */     if (rules == null) {
/* 526 */       LOGGER.error("Null rules for [" + converterId + ']');
/* 527 */       return null;
/*     */     } 
/* 529 */     for (int i = converterId.length(); i > 0 && converterClass == null; i--) {
/* 530 */       converterName = converterName.substring(0, i);
/* 531 */       converterClass = rules.get(converterName);
/*     */     } 
/*     */     
/* 534 */     if (converterClass == null) {
/* 535 */       LOGGER.error("Unrecognized format specifier [" + converterId + ']');
/* 536 */       return null;
/*     */     } 
/*     */     
/* 539 */     if (AnsiConverter.class.isAssignableFrom(converterClass)) {
/* 540 */       options.add("disableAnsi=" + disableAnsi);
/* 541 */       options.add("noConsoleNoAnsi=" + noConsoleNoAnsi);
/*     */     } 
/*     */ 
/*     */     
/* 545 */     Method[] methods = converterClass.getDeclaredMethods();
/* 546 */     Method newInstanceMethod = null;
/* 547 */     for (Method method : methods) {
/* 548 */       if (Modifier.isStatic(method.getModifiers()) && method
/* 549 */         .getDeclaringClass().equals(converterClass) && method
/* 550 */         .getName().equals("newInstance") && 
/* 551 */         areValidNewInstanceParameters(method.getParameterTypes())) {
/* 552 */         if (newInstanceMethod == null) {
/* 553 */           newInstanceMethod = method;
/* 554 */         } else if (method.getReturnType().equals(newInstanceMethod.getReturnType())) {
/* 555 */           LOGGER.error("Class " + converterClass + " cannot contain multiple static newInstance methods");
/* 556 */           return null;
/*     */         } 
/*     */       }
/*     */     } 
/* 560 */     if (newInstanceMethod == null) {
/* 561 */       LOGGER.error("Class " + converterClass + " does not contain a static newInstance method");
/* 562 */       return null;
/*     */     } 
/*     */     
/* 565 */     Class<?>[] parmTypes = newInstanceMethod.getParameterTypes();
/* 566 */     Object[] parms = (parmTypes.length > 0) ? new Object[parmTypes.length] : null;
/*     */     
/* 568 */     if (parms != null) {
/* 569 */       int j = 0;
/* 570 */       boolean errors = false;
/* 571 */       for (Class<?> clazz : parmTypes) {
/* 572 */         if (clazz.isArray() && clazz.getName().equals("[Ljava.lang.String;")) {
/* 573 */           String[] optionsArray = options.<String>toArray(new String[options.size()]);
/* 574 */           parms[j] = optionsArray;
/* 575 */         } else if (clazz.isAssignableFrom(Configuration.class)) {
/* 576 */           parms[j] = this.config;
/*     */         } else {
/* 578 */           LOGGER.error("Unknown parameter type " + clazz.getName() + " for static newInstance method of " + converterClass
/* 579 */               .getName());
/* 580 */           errors = true;
/*     */         } 
/* 582 */         j++;
/*     */       } 
/* 584 */       if (errors) {
/* 585 */         return null;
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 590 */       Object newObj = newInstanceMethod.invoke(null, parms);
/*     */       
/* 592 */       if (newObj instanceof PatternConverter) {
/* 593 */         currentLiteral.delete(0, currentLiteral.length() - converterId.length() - converterName.length());
/*     */         
/* 595 */         return (PatternConverter)newObj;
/*     */       } 
/* 597 */       LOGGER.warn("Class {} does not extend PatternConverter.", converterClass.getName());
/* 598 */     } catch (Exception ex) {
/* 599 */       LOGGER.error("Error creating converter for " + converterId, ex);
/*     */     } 
/*     */     
/* 602 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean areValidNewInstanceParameters(Class<?>[] parameterTypes) {
/* 607 */     for (Class<?> clazz : parameterTypes) {
/* 608 */       if (!clazz.isAssignableFrom(Configuration.class) && (
/* 609 */         !clazz.isArray() || !"[Ljava.lang.String;".equals(clazz.getName()))) {
/* 610 */         return false;
/*     */       }
/*     */     } 
/* 613 */     return true;
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
/*     */   private int finalizeConverter(char c, String pattern, int start, StringBuilder currentLiteral, FormattingInfo formattingInfo, Map<String, Class<PatternConverter>> rules, List<PatternConverter> patternConverters, List<FormattingInfo> formattingInfos, boolean disableAnsi, boolean noConsoleNoAnsi, boolean convertBackslashes) {
/* 648 */     int i = start;
/* 649 */     StringBuilder convBuf = new StringBuilder();
/* 650 */     i = extractConverter(c, pattern, i, convBuf, currentLiteral);
/*     */     
/* 652 */     String converterId = convBuf.toString();
/*     */     
/* 654 */     List<String> options = new ArrayList<>();
/* 655 */     i = extractOptions(pattern, i, options);
/*     */     
/* 657 */     PatternConverter pc = createConverter(converterId, currentLiteral, rules, options, disableAnsi, noConsoleNoAnsi);
/*     */ 
/*     */     
/* 660 */     if (pc == null) {
/*     */       StringBuilder msg;
/*     */       
/* 663 */       if (Strings.isEmpty(converterId)) {
/* 664 */         msg = new StringBuilder("Empty conversion specifier starting at position ");
/*     */       } else {
/* 666 */         msg = new StringBuilder("Unrecognized conversion specifier [");
/* 667 */         msg.append(converterId);
/* 668 */         msg.append("] starting at position ");
/*     */       } 
/*     */       
/* 671 */       msg.append(i);
/* 672 */       msg.append(" in conversion pattern.");
/*     */       
/* 674 */       LOGGER.error(msg.toString());
/*     */       
/* 676 */       patternConverters.add(literalPattern(currentLiteral.toString(), convertBackslashes));
/* 677 */       formattingInfos.add(FormattingInfo.getDefault());
/*     */     } else {
/* 679 */       patternConverters.add(pc);
/* 680 */       formattingInfos.add(formattingInfo);
/*     */       
/* 682 */       if (currentLiteral.length() > 0) {
/* 683 */         patternConverters
/* 684 */           .add(literalPattern(currentLiteral.toString(), convertBackslashes));
/* 685 */         formattingInfos.add(FormattingInfo.getDefault());
/*     */       } 
/*     */     } 
/*     */     
/* 689 */     currentLiteral.setLength(0);
/*     */     
/* 691 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private LogEventPatternConverter literalPattern(String literal, boolean convertBackslashes) {
/* 696 */     if (this.config != null && LiteralPatternConverter.containsSubstitutionSequence(literal)) {
/* 697 */       return new LiteralPatternConverter(this.config, literal, convertBackslashes);
/*     */     }
/* 699 */     return SimpleLiteralPatternConverter.of(literal, convertBackslashes);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\PatternParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */