/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "style", category = "Converter")
/*     */ @ConverterKeys({"style"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class StyleConverter
/*     */   extends LogEventPatternConverter
/*     */   implements AnsiConverter
/*     */ {
/*     */   private final List<PatternFormatter> patternFormatters;
/*     */   private final boolean noAnsi;
/*     */   private final String style;
/*     */   private final String defaultStyle;
/*     */   
/*     */   private StyleConverter(List<PatternFormatter> patternFormatters, String style, boolean noAnsi) {
/*  61 */     super("style", "style");
/*  62 */     this.patternFormatters = patternFormatters;
/*  63 */     this.style = style;
/*  64 */     this.defaultStyle = AnsiEscape.getDefaultStyle();
/*  65 */     this.noAnsi = noAnsi;
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
/*     */   public static StyleConverter newInstance(Configuration config, String[] options) {
/*  79 */     if (options == null) {
/*  80 */       return null;
/*     */     }
/*  82 */     if (options.length < 2) {
/*  83 */       LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + options.length);
/*  84 */       return null;
/*     */     } 
/*  86 */     if (options[0] == null) {
/*  87 */       LOGGER.error("No pattern supplied for style converter");
/*  88 */       return null;
/*     */     } 
/*  90 */     if (options[1] == null) {
/*  91 */       LOGGER.error("No style attributes supplied for style converter");
/*  92 */       return null;
/*     */     } 
/*  94 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  95 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  96 */     String style = AnsiEscape.createSequence(options[1].split(Patterns.COMMA_SEPARATOR));
/*  97 */     boolean disableAnsi = Arrays.toString((Object[])options).contains("disableAnsi=true");
/*  98 */     boolean noConsoleNoAnsi = Arrays.toString((Object[])options).contains("noConsoleNoAnsi=true");
/*  99 */     boolean hideAnsi = (disableAnsi || (noConsoleNoAnsi && System.console() == null));
/* 100 */     return new StyleConverter(formatters, style, hideAnsi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 108 */     int start = 0;
/* 109 */     int end = 0;
/* 110 */     if (!this.noAnsi) {
/* 111 */       start = toAppendTo.length();
/* 112 */       toAppendTo.append(this.style);
/* 113 */       end = toAppendTo.length();
/*     */     } 
/*     */ 
/*     */     
/* 117 */     for (int i = 0, size = this.patternFormatters.size(); i < size; i++) {
/* 118 */       ((PatternFormatter)this.patternFormatters.get(i)).format(event, toAppendTo);
/*     */     }
/*     */ 
/*     */     
/* 122 */     if (!this.noAnsi) {
/* 123 */       if (toAppendTo.length() == end) {
/* 124 */         toAppendTo.setLength(start);
/*     */       } else {
/* 126 */         toAppendTo.append(this.defaultStyle);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 133 */     for (PatternFormatter formatter : this.patternFormatters) {
/* 134 */       if (formatter.handlesThrowable()) {
/* 135 */         return true;
/*     */       }
/*     */     } 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 148 */     StringBuilder sb = new StringBuilder();
/* 149 */     sb.append(super.toString());
/* 150 */     sb.append("[style=");
/* 151 */     sb.append(this.style);
/* 152 */     sb.append(", defaultStyle=");
/* 153 */     sb.append(this.defaultStyle);
/* 154 */     sb.append(", patternFormatters=");
/* 155 */     sb.append(this.patternFormatters);
/* 156 */     sb.append(", noAnsi=");
/* 157 */     sb.append(this.noAnsi);
/* 158 */     sb.append(']');
/* 159 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\StyleConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */