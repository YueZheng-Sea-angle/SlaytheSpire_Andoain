/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
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
/*     */ public enum AnsiEscape
/*     */ {
/*  43 */   CSI("\033["),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   SUFFIX("m"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   SEPARATOR(";"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   NORMAL("0"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   BRIGHT("1"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   DIM("2"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   UNDERLINE("3"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   BLINK("5"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   REVERSE("7"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   HIDDEN("8"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   BLACK("30"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   FG_BLACK("30"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   RED("31"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   FG_RED("31"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   GREEN("32"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   FG_GREEN("32"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   YELLOW("33"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   FG_YELLOW("33"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   BLUE("34"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   FG_BLUE("34"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   MAGENTA("35"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   FG_MAGENTA("35"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   CYAN("36"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   FG_CYAN("36"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 163 */   WHITE("37"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   FG_WHITE("37"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   DEFAULT("39"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   FG_DEFAULT("39"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   BG_BLACK("40"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   BG_RED("41"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   BG_GREEN("42"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 198 */   BG_YELLOW("43"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   BG_BLUE("44"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   BG_MAGENTA("45"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   BG_CYAN("46"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   BG_WHITE("47"); private static final String DEFAULT_STYLE;
/*     */   static {
/* 220 */     DEFAULT_STYLE = CSI.getCode() + SUFFIX.getCode();
/*     */   }
/*     */   private final String code;
/*     */   
/*     */   AnsiEscape(String code) {
/* 225 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDefaultStyle() {
/* 234 */     return DEFAULT_STYLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCode() {
/* 243 */     return this.code;
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
/*     */   public static Map<String, String> createMap(String values, String[] dontEscapeKeys) {
/* 267 */     return createMap(values.split(Patterns.COMMA_SEPARATOR), dontEscapeKeys);
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
/*     */   public static Map<String, String> createMap(String[] values, String[] dontEscapeKeys) {
/* 293 */     String[] sortedIgnoreKeys = (dontEscapeKeys != null) ? (String[])dontEscapeKeys.clone() : Strings.EMPTY_ARRAY;
/* 294 */     Arrays.sort((Object[])sortedIgnoreKeys);
/* 295 */     Map<String, String> map = new HashMap<>();
/* 296 */     for (String string : values) {
/* 297 */       String[] keyValue = string.split(Patterns.toWhitespaceSeparator("="));
/* 298 */       if (keyValue.length > 1) {
/* 299 */         String key = keyValue[0].toUpperCase(Locale.ENGLISH);
/* 300 */         String value = keyValue[1];
/* 301 */         boolean escape = (Arrays.binarySearch((Object[])sortedIgnoreKeys, key) < 0);
/* 302 */         map.put(key, escape ? createSequence(value.split("\\s")) : value);
/*     */       } 
/*     */     } 
/* 305 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createSequence(String... names) {
/* 316 */     if (names == null) {
/* 317 */       return getDefaultStyle();
/*     */     }
/* 319 */     StringBuilder sb = new StringBuilder(CSI.getCode());
/* 320 */     boolean first = true;
/* 321 */     for (String name : names) {
/*     */       try {
/* 323 */         AnsiEscape escape = (AnsiEscape)EnglishEnums.valueOf(AnsiEscape.class, name.trim());
/* 324 */         if (!first) {
/* 325 */           sb.append(SEPARATOR.getCode());
/*     */         }
/* 327 */         first = false;
/* 328 */         sb.append(escape.getCode());
/* 329 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 333 */     sb.append(SUFFIX.getCode());
/* 334 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\AnsiEscape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */