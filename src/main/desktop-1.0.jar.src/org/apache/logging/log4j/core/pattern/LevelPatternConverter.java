/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ @Plugin(name = "LevelPatternConverter", category = "Converter")
/*     */ @ConverterKeys({"p", "level"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class LevelPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private static final String OPTION_LENGTH = "length";
/*     */   private static final String OPTION_LOWER = "lowerCase";
/*  42 */   private static final LevelPatternConverter INSTANCE = new SimpleLevelPatternConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelPatternConverter() {
/*  48 */     super("Level", "level");
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
/*     */   public static LevelPatternConverter newInstance(String[] options) {
/*  60 */     if (options == null || options.length == 0) {
/*  61 */       return INSTANCE;
/*     */     }
/*  63 */     Map<Level, String> levelMap = new HashMap<>();
/*  64 */     int length = Integer.MAX_VALUE;
/*  65 */     boolean lowerCase = false;
/*  66 */     String[] definitions = options[0].split(Patterns.COMMA_SEPARATOR);
/*  67 */     for (String def : definitions) {
/*  68 */       String[] pair = def.split("=");
/*  69 */       if (pair == null || pair.length != 2) {
/*  70 */         LOGGER.error("Invalid option {}", def);
/*     */       } else {
/*     */         
/*  73 */         String key = pair[0].trim();
/*  74 */         String value = pair[1].trim();
/*  75 */         if ("length".equalsIgnoreCase(key)) {
/*  76 */           length = Integer.parseInt(value);
/*  77 */         } else if ("lowerCase".equalsIgnoreCase(key)) {
/*  78 */           lowerCase = Boolean.parseBoolean(value);
/*     */         } else {
/*  80 */           Level level = Level.toLevel(key, null);
/*  81 */           if (level == null) {
/*  82 */             LOGGER.error("Invalid Level {}", key);
/*     */           } else {
/*  84 */             levelMap.put(level, value);
/*     */           } 
/*     */         } 
/*     */       } 
/*  88 */     }  if (levelMap.isEmpty() && length == Integer.MAX_VALUE && !lowerCase) {
/*  89 */       return INSTANCE;
/*     */     }
/*  91 */     for (Level level : Level.values()) {
/*  92 */       if (!levelMap.containsKey(level)) {
/*  93 */         String left = left(level, length);
/*  94 */         levelMap.put(level, lowerCase ? left.toLowerCase(Locale.US) : left);
/*     */       } 
/*     */     } 
/*  97 */     return new LevelMapLevelPatternConverter(levelMap);
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
/*     */   private static String left(Level level, int length) {
/* 111 */     String string = level.toString();
/* 112 */     if (length >= string.length()) {
/* 113 */       return string;
/*     */     }
/* 115 */     return string.substring(0, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder output) {
/* 123 */     throw new UnsupportedOperationException("Overridden by subclasses");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStyleClass(Object e) {
/* 131 */     if (e instanceof LogEvent) {
/* 132 */       return "level " + ((LogEvent)e).getLevel().name().toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */     
/* 135 */     return "level";
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class SimpleLevelPatternConverter
/*     */     extends LevelPatternConverter
/*     */   {
/*     */     private SimpleLevelPatternConverter() {}
/*     */     
/*     */     public void format(LogEvent event, StringBuilder output) {
/* 145 */       output.append(event.getLevel());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LevelMapLevelPatternConverter
/*     */     extends LevelPatternConverter {
/*     */     private final Map<Level, String> levelMap;
/*     */     
/*     */     private LevelMapLevelPatternConverter(Map<Level, String> levelMap) {
/* 154 */       this.levelMap = levelMap;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void format(LogEvent event, StringBuilder output) {
/* 162 */       output.append(this.levelMap.get(event.getLevel()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\LevelPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */