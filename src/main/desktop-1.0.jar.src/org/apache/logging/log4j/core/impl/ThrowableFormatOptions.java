/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import org.apache.logging.log4j.core.pattern.JAnsiTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
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
/*     */ public final class ThrowableFormatOptions
/*     */ {
/*     */   private static final int DEFAULT_LINES = 2147483647;
/*  41 */   protected static final ThrowableFormatOptions DEFAULT = new ThrowableFormatOptions();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FULL = "full";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String NONE = "none";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SHORT = "short";
/*     */ 
/*     */ 
/*     */   
/*     */   private final TextRenderer textRenderer;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int lines;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String separator;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String suffix;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<String> ignorePackages;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String CLASS_NAME = "short.className";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String METHOD_NAME = "short.methodName";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LINE_NUMBER = "short.lineNumber";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FILE_NAME = "short.fileName";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String MESSAGE = "short.message";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LOCALIZED_MESSAGE = "short.localizedMessage";
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions(int lines, String separator, List<String> ignorePackages, TextRenderer textRenderer, String suffix) {
/* 102 */     this.lines = lines;
/* 103 */     this.separator = (separator == null) ? Strings.LINE_SEPARATOR : separator;
/* 104 */     this.ignorePackages = ignorePackages;
/* 105 */     this.textRenderer = (textRenderer == null) ? (TextRenderer)PlainTextRenderer.getInstance() : textRenderer;
/* 106 */     this.suffix = suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions(List<String> packages) {
/* 116 */     this(2147483647, null, packages, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowableFormatOptions() {
/* 123 */     this(2147483647, null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLines() {
/* 132 */     return this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSeparator() {
/* 141 */     return this.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextRenderer getTextRenderer() {
/* 150 */     return this.textRenderer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getIgnorePackages() {
/* 159 */     return this.ignorePackages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean allLines() {
/* 168 */     return (this.lines == Integer.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean anyLines() {
/* 177 */     return (this.lines > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int minLines(int maxLines) {
/* 188 */     return (this.lines > maxLines) ? maxLines : this.lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPackages() {
/* 197 */     return (this.ignorePackages != null && !this.ignorePackages.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 205 */     StringBuilder s = new StringBuilder();
/* 206 */     s.append('{')
/* 207 */       .append(allLines() ? "full" : ((this.lines == 2) ? "short" : (anyLines() ? String.valueOf(this.lines) : "none")))
/* 208 */       .append('}');
/* 209 */     s.append("{separator(").append(this.separator).append(")}");
/* 210 */     if (hasPackages()) {
/* 211 */       s.append("{filters(");
/* 212 */       for (String p : this.ignorePackages) {
/* 213 */         s.append(p).append(',');
/*     */       }
/* 215 */       s.deleteCharAt(s.length() - 1);
/* 216 */       s.append(")}");
/*     */     } 
/* 218 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ThrowableFormatOptions newInstance(String[] options) {
/*     */     JAnsiTextRenderer jAnsiTextRenderer;
/* 229 */     if (options == null || options.length == 0) {
/* 230 */       return DEFAULT;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     if (options.length == 1 && Strings.isNotEmpty(options[0])) {
/* 239 */       String[] opts = options[0].split(Patterns.COMMA_SEPARATOR, 2);
/* 240 */       String first = opts[0].trim();
/* 241 */       try (Scanner scanner = new Scanner(first)) {
/* 242 */         if (opts.length > 1 && (first.equalsIgnoreCase("full") || first.equalsIgnoreCase("short") || first
/* 243 */           .equalsIgnoreCase("none") || scanner.hasNextInt())) {
/* 244 */           options = new String[] { first, opts[1].trim() };
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 249 */     int lines = DEFAULT.lines;
/* 250 */     String separator = DEFAULT.separator;
/* 251 */     List<String> packages = DEFAULT.ignorePackages;
/* 252 */     TextRenderer ansiRenderer = DEFAULT.textRenderer;
/* 253 */     String suffix = DEFAULT.getSuffix();
/* 254 */     for (String rawOption : options) {
/* 255 */       if (rawOption != null) {
/* 256 */         String option = rawOption.trim();
/* 257 */         if (!option.isEmpty())
/*     */         {
/* 259 */           if (option.startsWith("separator(") && option.endsWith(")")) {
/* 260 */             separator = option.substring("separator(".length(), option.length() - 1);
/* 261 */           } else if (option.startsWith("filters(") && option.endsWith(")")) {
/* 262 */             String filterStr = option.substring("filters(".length(), option.length() - 1);
/* 263 */             if (filterStr.length() > 0) {
/* 264 */               String[] array = filterStr.split(Patterns.COMMA_SEPARATOR);
/* 265 */               if (array.length > 0) {
/* 266 */                 packages = new ArrayList<>(array.length);
/* 267 */                 for (String token : array) {
/* 268 */                   token = token.trim();
/* 269 */                   if (token.length() > 0) {
/* 270 */                     packages.add(token);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/* 275 */           } else if (option.equalsIgnoreCase("none")) {
/* 276 */             lines = 0;
/* 277 */           } else if (option.equalsIgnoreCase("short") || option.equalsIgnoreCase("short.className") || option
/* 278 */             .equalsIgnoreCase("short.methodName") || option.equalsIgnoreCase("short.lineNumber") || option
/* 279 */             .equalsIgnoreCase("short.fileName") || option.equalsIgnoreCase("short.message") || option
/* 280 */             .equalsIgnoreCase("short.localizedMessage")) {
/* 281 */             lines = 2;
/* 282 */           } else if ((option.startsWith("ansi(") && option.endsWith(")")) || option.equals("ansi")) {
/* 283 */             if (Loader.isJansiAvailable()) {
/*     */               
/* 285 */               String styleMapStr = option.equals("ansi") ? "" : option.substring("ansi(".length(), option.length() - 1);
/* 286 */               jAnsiTextRenderer = new JAnsiTextRenderer(new String[] { null, styleMapStr }, JAnsiTextRenderer.DefaultExceptionStyleMap);
/*     */             } else {
/*     */               
/* 289 */               StatusLogger.getLogger().warn("You requested ANSI exception rendering but JANSI is not on the classpath. Please see https://logging.apache.org/log4j/2.x/runtime-dependencies.html");
/*     */             }
/*     */           
/* 292 */           } else if (option.startsWith("S(") && option.endsWith(")")) {
/* 293 */             suffix = option.substring("S(".length(), option.length() - 1);
/* 294 */           } else if (option.startsWith("suffix(") && option.endsWith(")")) {
/* 295 */             suffix = option.substring("suffix(".length(), option.length() - 1);
/* 296 */           } else if (!option.equalsIgnoreCase("full")) {
/* 297 */             lines = Integer.parseInt(option);
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 301 */     return new ThrowableFormatOptions(lines, separator, packages, (TextRenderer)jAnsiTextRenderer, suffix);
/*     */   }
/*     */   
/*     */   public String getSuffix() {
/* 305 */     return this.suffix;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ThrowableFormatOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */