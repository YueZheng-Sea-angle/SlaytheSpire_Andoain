/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "highlight", category = "Converter")
/*     */ @ConverterKeys({"highlight"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class HighlightConverter
/*     */   extends LogEventPatternConverter
/*     */   implements AnsiConverter
/*     */ {
/*  83 */   private static final Map<String, String> DEFAULT_STYLES = new HashMap<>();
/*     */   
/*  85 */   private static final Map<String, String> LOGBACK_STYLES = new HashMap<>();
/*     */   
/*     */   private static final String STYLE_KEY = "STYLE";
/*     */   
/*     */   private static final String STYLE_KEY_DEFAULT = "DEFAULT";
/*     */   
/*     */   private static final String STYLE_KEY_LOGBACK = "LOGBACK";
/*     */   
/*  93 */   private static final Map<String, Map<String, String>> STYLES = new HashMap<>();
/*     */   private final Map<String, String> levelStyles;
/*     */   
/*     */   static {
/*  97 */     DEFAULT_STYLES.put(Level.FATAL.name(), AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  98 */     DEFAULT_STYLES.put(Level.ERROR.name(), AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/*  99 */     DEFAULT_STYLES.put(Level.WARN.name(), AnsiEscape.createSequence(new String[] { "YELLOW" }));
/* 100 */     DEFAULT_STYLES.put(Level.INFO.name(), AnsiEscape.createSequence(new String[] { "GREEN" }));
/* 101 */     DEFAULT_STYLES.put(Level.DEBUG.name(), AnsiEscape.createSequence(new String[] { "CYAN" }));
/* 102 */     DEFAULT_STYLES.put(Level.TRACE.name(), AnsiEscape.createSequence(new String[] { "BLACK" }));
/*     */     
/* 104 */     LOGBACK_STYLES.put(Level.FATAL.name(), AnsiEscape.createSequence(new String[] { "BLINK", "BRIGHT", "RED" }));
/* 105 */     LOGBACK_STYLES.put(Level.ERROR.name(), AnsiEscape.createSequence(new String[] { "BRIGHT", "RED" }));
/* 106 */     LOGBACK_STYLES.put(Level.WARN.name(), AnsiEscape.createSequence(new String[] { "RED" }));
/* 107 */     LOGBACK_STYLES.put(Level.INFO.name(), AnsiEscape.createSequence(new String[] { "BLUE" }));
/* 108 */     LOGBACK_STYLES.put(Level.DEBUG.name(), AnsiEscape.createSequence((String[])null));
/* 109 */     LOGBACK_STYLES.put(Level.TRACE.name(), AnsiEscape.createSequence((String[])null));
/*     */     
/* 111 */     STYLES.put("DEFAULT", DEFAULT_STYLES);
/* 112 */     STYLES.put("LOGBACK", LOGBACK_STYLES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<PatternFormatter> patternFormatters;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean noAnsi;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String defaultStyle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<String, String> createLevelStyleMap(String[] options) {
/* 144 */     if (options.length < 2) {
/* 145 */       return DEFAULT_STYLES;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 150 */     String string = options[1].replaceAll("disableAnsi=(true|false)", "").replaceAll("noConsoleNoAnsi=(true|false)", "");
/*     */     
/* 152 */     Map<String, String> styles = AnsiEscape.createMap(string, new String[] { "STYLE" });
/* 153 */     Map<String, String> levelStyles = new HashMap<>(DEFAULT_STYLES);
/* 154 */     for (Map.Entry<String, String> entry : styles.entrySet()) {
/* 155 */       String key = ((String)entry.getKey()).toUpperCase(Locale.ENGLISH);
/* 156 */       String value = entry.getValue();
/* 157 */       if ("STYLE".equalsIgnoreCase(key)) {
/* 158 */         Map<String, String> enumMap = STYLES.get(value.toUpperCase(Locale.ENGLISH));
/* 159 */         if (enumMap == null) {
/* 160 */           LOGGER.error("Unknown level style: " + value + ". Use one of " + 
/* 161 */               Arrays.toString(STYLES.keySet().toArray())); continue;
/*     */         } 
/* 163 */         levelStyles.putAll(enumMap);
/*     */         continue;
/*     */       } 
/* 166 */       Level level = Level.toLevel(key, null);
/* 167 */       if (level == null) {
/* 168 */         LOGGER.warn("Setting style for yet unknown level name {}", key);
/* 169 */         levelStyles.put(key, value); continue;
/*     */       } 
/* 171 */       levelStyles.put(level.name(), value);
/*     */     } 
/*     */ 
/*     */     
/* 175 */     return levelStyles;
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
/*     */   public static HighlightConverter newInstance(Configuration config, String[] options) {
/* 187 */     if (options.length < 1) {
/* 188 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
/* 189 */       return null;
/*     */     } 
/* 191 */     if (options[0] == null) {
/* 192 */       LOGGER.error("No pattern supplied on style");
/* 193 */       return null;
/*     */     } 
/* 195 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 196 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/* 197 */     boolean disableAnsi = Arrays.toString((Object[])options).contains("disableAnsi=true");
/* 198 */     boolean noConsoleNoAnsi = Arrays.toString((Object[])options).contains("noConsoleNoAnsi=true");
/* 199 */     boolean hideAnsi = (disableAnsi || (noConsoleNoAnsi && System.console() == null));
/* 200 */     return new HighlightConverter(formatters, createLevelStyleMap(options), hideAnsi);
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
/*     */   private HighlightConverter(List<PatternFormatter> patternFormatters, Map<String, String> levelStyles, boolean noAnsi) {
/* 220 */     super("style", "style");
/* 221 */     this.patternFormatters = patternFormatters;
/* 222 */     this.levelStyles = levelStyles;
/* 223 */     this.defaultStyle = AnsiEscape.getDefaultStyle();
/* 224 */     this.noAnsi = noAnsi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 232 */     int start = 0;
/* 233 */     int end = 0;
/* 234 */     String levelStyle = this.levelStyles.get(event.getLevel().name());
/* 235 */     if (!this.noAnsi) {
/* 236 */       start = toAppendTo.length();
/* 237 */       if (levelStyle != null) {
/* 238 */         toAppendTo.append(levelStyle);
/*     */       }
/* 240 */       end = toAppendTo.length();
/*     */     } 
/*     */ 
/*     */     
/* 244 */     for (int i = 0, size = this.patternFormatters.size(); i < size; i++) {
/* 245 */       ((PatternFormatter)this.patternFormatters.get(i)).format(event, toAppendTo);
/*     */     }
/*     */ 
/*     */     
/* 249 */     boolean empty = (toAppendTo.length() == end);
/* 250 */     if (!this.noAnsi) {
/* 251 */       if (empty) {
/* 252 */         toAppendTo.setLength(start);
/* 253 */       } else if (levelStyle != null) {
/* 254 */         toAppendTo.append(this.defaultStyle);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   String getLevelStyle(Level level) {
/* 260 */     return this.levelStyles.get(level.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 265 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 266 */       if (formatter.handlesThrowable()) {
/* 267 */         return true;
/*     */       }
/*     */     } 
/* 270 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\HighlightConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */