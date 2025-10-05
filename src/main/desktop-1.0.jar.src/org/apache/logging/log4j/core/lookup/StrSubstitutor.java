/*      */ package org.apache.logging.log4j.core.lookup;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Properties;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.config.Configuration;
/*      */ import org.apache.logging.log4j.core.config.ConfigurationAware;
/*      */ import org.apache.logging.log4j.status.StatusLogger;
/*      */ import org.apache.logging.log4j.util.Strings;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StrSubstitutor
/*      */   implements ConfigurationAware
/*      */ {
/*      */   public static final char DEFAULT_ESCAPE = '$';
/*  153 */   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
/*      */ 
/*      */   
/*      */   public static final String DEFAULT_VALUE_DELIMITER_STRING = ":-";
/*      */ 
/*      */   
/*  164 */   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
/*      */   
/*      */   public static final String ESCAPE_DELIMITER_STRING = ":\\-";
/*  167 */   public static final StrMatcher DEFAULT_VALUE_ESCAPE_DELIMITER = StrMatcher.stringMatcher(":\\-");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int BUF_SIZE = 256;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char escapeChar;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher prefixMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher suffixMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private String valueDelimiterString;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueDelimiterMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrMatcher valueEscapeDelimiterMatcher;
/*      */ 
/*      */ 
/*      */   
/*      */   private StrLookup variableResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean enableSubstitutionInVariables = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private Configuration configuration;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean recursiveEvaluationAllowed;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor() {
/*  220 */     this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Map<String, String> valueMap) {
/*  230 */     this(new MapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix) {
/*  242 */     this(new MapLookup(valueMap), prefix, suffix, '$');
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
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape) {
/*  256 */     this(new MapLookup(valueMap), prefix, suffix, escape);
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
/*      */   public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape, String valueDelimiter) {
/*  271 */     this(new MapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(Properties properties) {
/*  281 */     this(toTypeSafeMap(properties));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrSubstitutor(StrLookup variableResolver) {
/*  290 */     this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
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
/*      */   public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape) {
/*  304 */     setVariableResolver(variableResolver);
/*  305 */     setVariablePrefix(prefix);
/*  306 */     setVariableSuffix(suffix);
/*  307 */     setEscapeChar(escape);
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
/*      */   public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape, String valueDelimiter) {
/*  321 */     setVariableResolver(variableResolver);
/*  322 */     setVariablePrefix(prefix);
/*  323 */     setVariableSuffix(suffix);
/*  324 */     setEscapeChar(escape);
/*  325 */     setValueDelimiter(valueDelimiter);
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
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
/*  340 */     this(variableResolver, prefixMatcher, suffixMatcher, escape, DEFAULT_VALUE_DELIMITER, DEFAULT_VALUE_ESCAPE_DELIMITER);
/*      */     
/*  342 */     this.valueDelimiterString = ":-";
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
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher) {
/*  357 */     setVariableResolver(variableResolver);
/*  358 */     setVariablePrefixMatcher(prefixMatcher);
/*  359 */     setVariableSuffixMatcher(suffixMatcher);
/*  360 */     setEscapeChar(escape);
/*  361 */     setValueDelimiterMatcher(valueDelimiterMatcher);
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
/*      */   public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape, StrMatcher valueDelimiterMatcher, StrMatcher valueEscapeMatcher) {
/*  378 */     setVariableResolver(variableResolver);
/*  379 */     setVariablePrefixMatcher(prefixMatcher);
/*  380 */     setVariableSuffixMatcher(suffixMatcher);
/*  381 */     setEscapeChar(escape);
/*  382 */     setValueDelimiterMatcher(valueDelimiterMatcher);
/*  383 */     this.valueEscapeDelimiterMatcher = valueEscapeMatcher;
/*      */   }
/*      */   
/*      */   StrSubstitutor(StrSubstitutor other) {
/*  387 */     Objects.requireNonNull(other, "other");
/*  388 */     setVariableResolver(other.getVariableResolver());
/*  389 */     setVariablePrefixMatcher(other.getVariablePrefixMatcher());
/*  390 */     setVariableSuffixMatcher(other.getVariableSuffixMatcher());
/*  391 */     setEscapeChar(other.getEscapeChar());
/*  392 */     setValueDelimiterMatcher(other.valueDelimiterMatcher);
/*  393 */     this.valueEscapeDelimiterMatcher = other.valueEscapeDelimiterMatcher;
/*  394 */     this.configuration = other.configuration;
/*  395 */     this.recursiveEvaluationAllowed = other.isRecursiveEvaluationAllowed();
/*  396 */     this.enableSubstitutionInVariables = other.isEnableSubstitutionInVariables();
/*  397 */     this.valueDelimiterString = other.valueDelimiterString;
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
/*      */   public static String replace(Object source, Map<String, String> valueMap) {
/*  410 */     return (new StrSubstitutor(valueMap)).replace(source);
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
/*      */   public static String replace(Object source, Map<String, String> valueMap, String prefix, String suffix) {
/*  427 */     return (new StrSubstitutor(valueMap, prefix, suffix)).replace(source);
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
/*      */   public static String replace(Object source, Properties valueProperties) {
/*  439 */     if (valueProperties == null) {
/*  440 */       return source.toString();
/*      */     }
/*  442 */     Map<String, String> valueMap = new HashMap<>();
/*  443 */     Enumeration<?> propNames = valueProperties.propertyNames();
/*  444 */     while (propNames.hasMoreElements()) {
/*  445 */       String propName = (String)propNames.nextElement();
/*  446 */       String propValue = valueProperties.getProperty(propName);
/*  447 */       valueMap.put(propName, propValue);
/*      */     } 
/*  449 */     return replace(source, valueMap);
/*      */   }
/*      */   
/*      */   private static Map<String, String> toTypeSafeMap(Properties properties) {
/*  453 */     Map<String, String> map = new HashMap<>(properties.size());
/*  454 */     for (String name : properties.stringPropertyNames()) {
/*  455 */       map.put(name, properties.getProperty(name));
/*      */     }
/*  457 */     return map;
/*      */   }
/*      */   
/*      */   private static String handleFailedReplacement(String input, Throwable throwable) {
/*  461 */     StatusLogger.getLogger().error("Replacement failed on {}", input, throwable);
/*  462 */     return input;
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
/*      */   public String replace(String source) {
/*  474 */     return replace((LogEvent)null, source);
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
/*      */   public String replace(LogEvent event, String source) {
/*  486 */     if (source == null) {
/*  487 */       return null;
/*      */     }
/*  489 */     StringBuilder buf = new StringBuilder(source);
/*      */     try {
/*  491 */       if (!substitute(event, buf, 0, source.length())) {
/*  492 */         return source;
/*      */       }
/*  494 */     } catch (Throwable t) {
/*  495 */       return handleFailedReplacement(source, t);
/*      */     } 
/*  497 */     return buf.toString();
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
/*      */   public String replace(String source, int offset, int length) {
/*  514 */     return replace((LogEvent)null, source, offset, length);
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
/*      */   
/*      */   public String replace(LogEvent event, String source, int offset, int length) {
/*  532 */     if (source == null) {
/*  533 */       return null;
/*      */     }
/*  535 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*      */     try {
/*  537 */       if (!substitute(event, buf, 0, length)) {
/*  538 */         return source.substring(offset, offset + length);
/*      */       }
/*  540 */     } catch (Throwable t) {
/*  541 */       return handleFailedReplacement(source, t);
/*      */     } 
/*  543 */     return buf.toString();
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
/*      */   public String replace(char[] source) {
/*  556 */     return replace((LogEvent)null, source);
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
/*      */   public String replace(LogEvent event, char[] source) {
/*  570 */     if (source == null) {
/*  571 */       return null;
/*      */     }
/*  573 */     StringBuilder buf = (new StringBuilder(source.length)).append(source);
/*      */     try {
/*  575 */       substitute(event, buf, 0, source.length);
/*  576 */     } catch (Throwable t) {
/*  577 */       return handleFailedReplacement(new String(source), t);
/*      */     } 
/*  579 */     return buf.toString();
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
/*      */   
/*      */   public String replace(char[] source, int offset, int length) {
/*  597 */     return replace((LogEvent)null, source, offset, length);
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
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, char[] source, int offset, int length) {
/*  616 */     if (source == null) {
/*  617 */       return null;
/*      */     }
/*  619 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*      */     try {
/*  621 */       substitute(event, buf, 0, length);
/*  622 */     } catch (Throwable t) {
/*  623 */       return handleFailedReplacement(new String(source, offset, length), t);
/*      */     } 
/*  625 */     return buf.toString();
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
/*      */   public String replace(StringBuffer source) {
/*  638 */     return replace((LogEvent)null, source);
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
/*      */   public String replace(LogEvent event, StringBuffer source) {
/*  652 */     if (source == null) {
/*  653 */       return null;
/*      */     }
/*  655 */     StringBuilder buf = (new StringBuilder(source.length())).append(source);
/*      */     try {
/*  657 */       substitute(event, buf, 0, buf.length());
/*  658 */     } catch (Throwable t) {
/*  659 */       return handleFailedReplacement(source.toString(), t);
/*      */     } 
/*  661 */     return buf.toString();
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
/*      */   
/*      */   public String replace(StringBuffer source, int offset, int length) {
/*  679 */     return replace((LogEvent)null, source, offset, length);
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
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuffer source, int offset, int length) {
/*  698 */     if (source == null) {
/*  699 */       return null;
/*      */     }
/*  701 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*      */     try {
/*  703 */       substitute(event, buf, 0, length);
/*  704 */     } catch (Throwable t) {
/*  705 */       return handleFailedReplacement(source.substring(offset, offset + length), t);
/*      */     } 
/*  707 */     return buf.toString();
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
/*      */   public String replace(StringBuilder source) {
/*  720 */     return replace((LogEvent)null, source);
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
/*      */   public String replace(LogEvent event, StringBuilder source) {
/*  734 */     if (source == null) {
/*  735 */       return null;
/*      */     }
/*  737 */     StringBuilder buf = (new StringBuilder(source.length())).append(source);
/*      */     try {
/*  739 */       substitute(event, buf, 0, buf.length());
/*  740 */     } catch (Throwable t) {
/*  741 */       return handleFailedReplacement(source.toString(), t);
/*      */     } 
/*  743 */     return buf.toString();
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
/*      */   public String replace(StringBuilder source, int offset, int length) {
/*  760 */     return replace((LogEvent)null, source, offset, length);
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
/*      */ 
/*      */   
/*      */   public String replace(LogEvent event, StringBuilder source, int offset, int length) {
/*  779 */     if (source == null) {
/*  780 */       return null;
/*      */     }
/*  782 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*      */     try {
/*  784 */       substitute(event, buf, 0, length);
/*  785 */     } catch (Throwable t) {
/*  786 */       return handleFailedReplacement(source.substring(offset, offset + length), t);
/*      */     } 
/*  788 */     return buf.toString();
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
/*      */   public String replace(Object source) {
/*  801 */     return replace((LogEvent)null, source);
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
/*      */   public String replace(LogEvent event, Object source) {
/*  814 */     if (source == null) {
/*  815 */       return null;
/*      */     }
/*  817 */     String stringValue = String.valueOf(source);
/*  818 */     StringBuilder buf = (new StringBuilder(stringValue.length())).append(stringValue);
/*      */     try {
/*  820 */       substitute(event, buf, 0, buf.length());
/*  821 */     } catch (Throwable t) {
/*  822 */       return handleFailedReplacement(stringValue, t);
/*      */     } 
/*  824 */     return buf.toString();
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
/*      */   public boolean replaceIn(StringBuffer source) {
/*  837 */     if (source == null) {
/*  838 */       return false;
/*      */     }
/*  840 */     return replaceIn(source, 0, source.length());
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
/*      */   
/*      */   public boolean replaceIn(StringBuffer source, int offset, int length) {
/*  858 */     return replaceIn((LogEvent)null, source, offset, length);
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
/*      */ 
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuffer source, int offset, int length) {
/*  877 */     if (source == null) {
/*  878 */       return false;
/*      */     }
/*  880 */     StringBuilder buf = (new StringBuilder(length)).append(source, offset, length);
/*      */     try {
/*  882 */       if (!substitute(event, buf, 0, length)) {
/*  883 */         return false;
/*      */       }
/*  885 */     } catch (Throwable t) {
/*  886 */       StatusLogger.getLogger().error("Replacement failed on {}", source, t);
/*  887 */       return false;
/*      */     } 
/*  889 */     source.replace(offset, offset + length, buf.toString());
/*  890 */     return true;
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
/*      */   public boolean replaceIn(StringBuilder source) {
/*  902 */     return replaceIn(null, source);
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
/*      */   public boolean replaceIn(LogEvent event, StringBuilder source) {
/*  915 */     if (source == null) {
/*  916 */       return false;
/*      */     }
/*  918 */     return substitute(event, source, 0, source.length());
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
/*      */   public boolean replaceIn(StringBuilder source, int offset, int length) {
/*  934 */     return replaceIn((LogEvent)null, source, offset, length);
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
/*      */   
/*      */   public boolean replaceIn(LogEvent event, StringBuilder source, int offset, int length) {
/*  952 */     if (source == null) {
/*  953 */       return false;
/*      */     }
/*  955 */     return substitute(event, source, offset, length);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean substitute(LogEvent event, StringBuilder buf, int offset, int length) {
/*  977 */     return (substitute(event, buf, offset, length, null) > 0);
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
/*      */   
/*      */   private int substitute(LogEvent event, StringBuilder buf, int offset, int length, List<String> priorVariables) {
/*  995 */     StrMatcher prefixMatcher = getVariablePrefixMatcher();
/*  996 */     StrMatcher suffixMatcher = getVariableSuffixMatcher();
/*  997 */     char escape = getEscapeChar();
/*  998 */     StrMatcher valueDelimiterMatcher = getValueDelimiterMatcher();
/*  999 */     boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
/*      */     
/* 1001 */     boolean top = (priorVariables == null);
/* 1002 */     boolean altered = false;
/* 1003 */     int lengthChange = 0;
/* 1004 */     char[] chars = getChars(buf);
/* 1005 */     int bufEnd = offset + length;
/* 1006 */     int pos = offset;
/* 1007 */     while (pos < bufEnd) {
/* 1008 */       int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
/* 1009 */       if (startMatchLen == 0) {
/* 1010 */         pos++; continue;
/*      */       } 
/* 1012 */       if (pos > offset && chars[pos - 1] == escape) {
/*      */         
/* 1014 */         buf.deleteCharAt(pos - 1);
/* 1015 */         chars = getChars(buf);
/* 1016 */         lengthChange--;
/* 1017 */         altered = true;
/* 1018 */         bufEnd--;
/*      */         continue;
/*      */       } 
/* 1021 */       int startPos = pos;
/* 1022 */       pos += startMatchLen;
/* 1023 */       int endMatchLen = 0;
/* 1024 */       int nestedVarCount = 0;
/* 1025 */       while (pos < bufEnd) {
/* 1026 */         if (substitutionInVariablesEnabled && (
/* 1027 */           endMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
/*      */           
/* 1029 */           nestedVarCount++;
/* 1030 */           pos += endMatchLen;
/*      */           
/*      */           continue;
/*      */         } 
/* 1034 */         endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
/* 1035 */         if (endMatchLen == 0) {
/* 1036 */           pos++;
/*      */           continue;
/*      */         } 
/* 1039 */         if (nestedVarCount == 0) {
/* 1040 */           String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
/* 1041 */           if (substitutionInVariablesEnabled) {
/*      */             
/* 1043 */             if (priorVariables == null) {
/* 1044 */               priorVariables = new ArrayList<>();
/*      */             }
/* 1046 */             StringBuilder bufName = new StringBuilder(varNameExpr);
/* 1047 */             substitute(event, bufName, 0, bufName.length(), priorVariables);
/* 1048 */             varNameExpr = bufName.toString();
/*      */           } 
/* 1050 */           pos += endMatchLen;
/* 1051 */           int endPos = pos;
/*      */           
/* 1053 */           String varName = varNameExpr;
/* 1054 */           String varDefaultValue = null;
/*      */           
/* 1056 */           if (valueDelimiterMatcher != null) {
/* 1057 */             char[] varNameExprChars = varNameExpr.toCharArray();
/* 1058 */             int valueDelimiterMatchLen = 0;
/* 1059 */             for (int i = 0; i < varNameExprChars.length; i++) {
/*      */               
/* 1061 */               if (!substitutionInVariablesEnabled && prefixMatcher
/* 1062 */                 .isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
/*      */                 break;
/*      */               }
/* 1065 */               if (this.valueEscapeDelimiterMatcher != null) {
/* 1066 */                 int matchLen = this.valueEscapeDelimiterMatcher.isMatch(varNameExprChars, i);
/* 1067 */                 if (matchLen != 0) {
/* 1068 */                   String varNamePrefix = varNameExpr.substring(0, i) + ':';
/* 1069 */                   varName = varNamePrefix + varNameExpr.substring(i + matchLen - 1);
/* 1070 */                   for (int j = i + matchLen; j < varNameExprChars.length; j++) {
/* 1071 */                     if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, j)) != 0) {
/* 1072 */                       varName = varNamePrefix + varNameExpr.substring(i + matchLen, j);
/* 1073 */                       varDefaultValue = varNameExpr.substring(j + valueDelimiterMatchLen); break;
/*      */                     } 
/*      */                   } 
/*      */                   break;
/*      */                 } 
/* 1078 */                 if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, i)) != 0) {
/* 1079 */                   varName = varNameExpr.substring(0, i);
/* 1080 */                   varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                   break;
/*      */                 } 
/* 1083 */               } else if ((valueDelimiterMatchLen = valueDelimiterMatcher.isMatch(varNameExprChars, i)) != 0) {
/* 1084 */                 varName = varNameExpr.substring(0, i);
/* 1085 */                 varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 1092 */           if (priorVariables == null) {
/* 1093 */             priorVariables = new ArrayList<>();
/* 1094 */             priorVariables.add(new String(chars, offset, length + lengthChange));
/*      */           } 
/*      */ 
/*      */           
/* 1098 */           boolean isCyclic = isCyclicSubstitution(varName, priorVariables);
/*      */ 
/*      */           
/* 1101 */           String varValue = isCyclic ? null : resolveVariable(event, varName, buf, startPos, endPos);
/* 1102 */           if (varValue == null) {
/* 1103 */             varValue = varDefaultValue;
/*      */           }
/* 1105 */           if (varValue != null) {
/*      */             
/* 1107 */             int varLen = varValue.length();
/* 1108 */             buf.replace(startPos, endPos, varValue);
/* 1109 */             altered = true;
/*      */             
/* 1111 */             int change = isRecursiveEvaluationAllowed() ? substitute(event, buf, startPos, varLen, priorVariables) : 0;
/*      */             
/* 1113 */             change += varLen - endPos - startPos;
/* 1114 */             pos += change;
/* 1115 */             bufEnd += change;
/* 1116 */             lengthChange += change;
/* 1117 */             chars = getChars(buf);
/*      */           } 
/*      */ 
/*      */           
/* 1121 */           if (!isCyclic) {
/* 1122 */             priorVariables.remove(priorVariables.size() - 1);
/*      */           }
/*      */           break;
/*      */         } 
/* 1126 */         nestedVarCount--;
/* 1127 */         pos += endMatchLen;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1132 */     if (top) {
/* 1133 */       return altered ? 1 : 0;
/*      */     }
/* 1135 */     return lengthChange;
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
/*      */   private boolean isCyclicSubstitution(String varName, List<String> priorVariables) {
/* 1147 */     if (!priorVariables.contains(varName)) {
/* 1148 */       priorVariables.add(varName);
/* 1149 */       return false;
/*      */     } 
/* 1151 */     StringBuilder buf = new StringBuilder(256);
/* 1152 */     buf.append("Infinite loop in property interpolation of ");
/* 1153 */     appendWithSeparators(buf, priorVariables, "->");
/* 1154 */     StatusLogger.getLogger().warn(buf);
/* 1155 */     return true;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveVariable(LogEvent event, String variableName, StringBuilder buf, int startPos, int endPos) {
/* 1180 */     StrLookup resolver = getVariableResolver();
/* 1181 */     if (resolver == null) {
/* 1182 */       return null;
/*      */     }
/*      */     try {
/* 1185 */       return resolver.lookup(event, variableName);
/* 1186 */     } catch (Throwable t) {
/* 1187 */       StatusLogger.getLogger().error("Resolver failed to lookup {}", variableName, t);
/* 1188 */       return null;
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
/*      */   public char getEscapeChar() {
/* 1200 */     return this.escapeChar;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEscapeChar(char escapeCharacter) {
/* 1211 */     this.escapeChar = escapeCharacter;
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
/*      */   public StrMatcher getVariablePrefixMatcher() {
/* 1227 */     return this.prefixMatcher;
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
/*      */   public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
/* 1243 */     if (prefixMatcher == null) {
/* 1244 */       throw new IllegalArgumentException("Variable prefix matcher must not be null!");
/*      */     }
/* 1246 */     this.prefixMatcher = prefixMatcher;
/* 1247 */     return this;
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
/*      */   public StrSubstitutor setVariablePrefix(char prefix) {
/* 1262 */     return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
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
/*      */   public StrSubstitutor setVariablePrefix(String prefix) {
/* 1277 */     if (prefix == null) {
/* 1278 */       throw new IllegalArgumentException("Variable prefix must not be null!");
/*      */     }
/* 1280 */     return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
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
/*      */   public StrMatcher getVariableSuffixMatcher() {
/* 1296 */     return this.suffixMatcher;
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
/*      */   public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
/* 1312 */     if (suffixMatcher == null) {
/* 1313 */       throw new IllegalArgumentException("Variable suffix matcher must not be null!");
/*      */     }
/* 1315 */     this.suffixMatcher = suffixMatcher;
/* 1316 */     return this;
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
/*      */   public StrSubstitutor setVariableSuffix(char suffix) {
/* 1331 */     return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
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
/*      */   public StrSubstitutor setVariableSuffix(String suffix) {
/* 1346 */     if (suffix == null) {
/* 1347 */       throw new IllegalArgumentException("Variable suffix must not be null!");
/*      */     }
/* 1349 */     return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
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
/*      */ 
/*      */   
/*      */   public StrMatcher getValueDelimiterMatcher() {
/* 1368 */     return this.valueDelimiterMatcher;
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
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiterMatcher(StrMatcher valueDelimiterMatcher) {
/* 1387 */     this.valueDelimiterMatcher = valueDelimiterMatcher;
/* 1388 */     return this;
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
/*      */   public StrSubstitutor setValueDelimiter(char valueDelimiter) {
/* 1403 */     return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
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
/*      */ 
/*      */   
/*      */   public StrSubstitutor setValueDelimiter(String valueDelimiter) {
/* 1422 */     if (Strings.isEmpty(valueDelimiter)) {
/* 1423 */       setValueDelimiterMatcher(null);
/* 1424 */       return this;
/*      */     } 
/*      */     
/* 1427 */     String escapeValue = valueDelimiter.substring(0, valueDelimiter.length() - 1) + "\\" + valueDelimiter.substring(valueDelimiter.length() - 1);
/* 1428 */     this.valueEscapeDelimiterMatcher = StrMatcher.stringMatcher(escapeValue);
/* 1429 */     return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StrLookup getVariableResolver() {
/* 1440 */     return this.variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVariableResolver(StrLookup variableResolver) {
/* 1449 */     if (variableResolver instanceof ConfigurationAware && this.configuration != null) {
/* 1450 */       ((ConfigurationAware)variableResolver).setConfiguration(this.configuration);
/*      */     }
/* 1452 */     this.variableResolver = variableResolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnableSubstitutionInVariables() {
/* 1463 */     return this.enableSubstitutionInVariables;
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
/*      */   public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
/* 1475 */     this.enableSubstitutionInVariables = enableSubstitutionInVariables;
/*      */   }
/*      */   
/*      */   boolean isRecursiveEvaluationAllowed() {
/* 1479 */     return this.recursiveEvaluationAllowed;
/*      */   }
/*      */   
/*      */   void setRecursiveEvaluationAllowed(boolean recursiveEvaluationAllowed) {
/* 1483 */     this.recursiveEvaluationAllowed = recursiveEvaluationAllowed;
/*      */   }
/*      */   
/*      */   private char[] getChars(StringBuilder sb) {
/* 1487 */     char[] chars = new char[sb.length()];
/* 1488 */     sb.getChars(0, sb.length(), chars, 0);
/* 1489 */     return chars;
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
/*      */   public void appendWithSeparators(StringBuilder sb, Iterable<?> iterable, String separator) {
/* 1502 */     if (iterable != null) {
/* 1503 */       separator = (separator == null) ? "" : separator;
/* 1504 */       Iterator<?> it = iterable.iterator();
/* 1505 */       while (it.hasNext()) {
/* 1506 */         sb.append(it.next());
/* 1507 */         if (it.hasNext()) {
/* 1508 */           sb.append(separator);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1516 */     return "StrSubstitutor(" + this.variableResolver.toString() + ')';
/*      */   }
/*      */ 
/*      */   
/*      */   public void setConfiguration(Configuration configuration) {
/* 1521 */     this.configuration = configuration;
/* 1522 */     if (this.variableResolver instanceof ConfigurationAware)
/* 1523 */       ((ConfigurationAware)this.variableResolver).setConfiguration(this.configuration); 
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\StrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */