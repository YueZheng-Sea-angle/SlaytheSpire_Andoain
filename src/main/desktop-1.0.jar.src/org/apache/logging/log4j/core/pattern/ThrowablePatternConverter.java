/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.impl.ThrowableFormatOptions;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.core.util.StringBuilderWriter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "ThrowablePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"ex", "throwable", "exception"})
/*     */ public class ThrowablePatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   protected final List<PatternFormatter> formatters;
/*     */   private String rawOption;
/*     */   private final boolean subShortOption;
/*     */   private final boolean nonStandardLineSeparator;
/*     */   protected final ThrowableFormatOptions options;
/*     */   
/*     */   @Deprecated
/*     */   protected ThrowablePatternConverter(String name, String style, String[] options) {
/*  65 */     this(name, style, options, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ThrowablePatternConverter(String name, String style, String[] options, Configuration config) {
/*  76 */     super(name, style);
/*  77 */     this.options = ThrowableFormatOptions.newInstance(options);
/*  78 */     if (options != null && options.length > 0) {
/*  79 */       this.rawOption = options[0];
/*     */     }
/*  81 */     if (this.options.getSuffix() != null) {
/*  82 */       PatternParser parser = PatternLayout.createPatternParser(config);
/*  83 */       List<PatternFormatter> parsedSuffixFormatters = parser.parse(this.options.getSuffix());
/*     */       
/*  85 */       boolean hasThrowableSuffixFormatter = false;
/*  86 */       for (PatternFormatter suffixFormatter : parsedSuffixFormatters) {
/*  87 */         if (suffixFormatter.handlesThrowable()) {
/*  88 */           hasThrowableSuffixFormatter = true;
/*     */         }
/*     */       } 
/*  91 */       if (!hasThrowableSuffixFormatter) {
/*  92 */         this.formatters = parsedSuffixFormatters;
/*     */       } else {
/*  94 */         List<PatternFormatter> suffixFormatters = new ArrayList<>();
/*  95 */         for (PatternFormatter suffixFormatter : parsedSuffixFormatters) {
/*  96 */           if (!suffixFormatter.handlesThrowable()) {
/*  97 */             suffixFormatters.add(suffixFormatter);
/*     */           }
/*     */         } 
/* 100 */         this.formatters = suffixFormatters;
/*     */       } 
/*     */     } else {
/* 103 */       this.formatters = Collections.emptyList();
/*     */     } 
/* 105 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       .subShortOption = ("short.message".equalsIgnoreCase(this.rawOption) || "short.localizedMessage".equalsIgnoreCase(this.rawOption) || "short.fileName".equalsIgnoreCase(this.rawOption) || "short.lineNumber".equalsIgnoreCase(this.rawOption) || "short.methodName".equalsIgnoreCase(this.rawOption) || "short.className".equalsIgnoreCase(this.rawOption));
/* 111 */     this.nonStandardLineSeparator = !Strings.LINE_SEPARATOR.equals(this.options.getSeparator());
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
/*     */   public static ThrowablePatternConverter newInstance(Configuration config, String[] options) {
/* 123 */     return new ThrowablePatternConverter("Throwable", "throwable", options, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder buffer) {
/* 131 */     Throwable t = event.getThrown();
/*     */     
/* 133 */     if (this.subShortOption) {
/* 134 */       formatSubShortOption(t, getSuffix(event), buffer);
/*     */     }
/* 136 */     else if (t != null && this.options.anyLines()) {
/* 137 */       formatOption(t, getSuffix(event), buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void formatSubShortOption(Throwable t, String suffix, StringBuilder buffer) {
/* 143 */     StackTraceElement throwingMethod = null;
/*     */ 
/*     */     
/* 146 */     if (t != null) {
/* 147 */       StackTraceElement[] trace = t.getStackTrace();
/* 148 */       if (trace != null && trace.length > 0) {
/* 149 */         throwingMethod = trace[0];
/*     */       }
/*     */     } 
/*     */     
/* 153 */     if (t != null && throwingMethod != null) {
/* 154 */       String toAppend = "";
/*     */       
/* 156 */       if ("short.className".equalsIgnoreCase(this.rawOption)) {
/* 157 */         toAppend = throwingMethod.getClassName();
/*     */       }
/* 159 */       else if ("short.methodName".equalsIgnoreCase(this.rawOption)) {
/* 160 */         toAppend = throwingMethod.getMethodName();
/*     */       }
/* 162 */       else if ("short.lineNumber".equalsIgnoreCase(this.rawOption)) {
/* 163 */         toAppend = String.valueOf(throwingMethod.getLineNumber());
/*     */       }
/* 165 */       else if ("short.message".equalsIgnoreCase(this.rawOption)) {
/* 166 */         toAppend = t.getMessage();
/*     */       }
/* 168 */       else if ("short.localizedMessage".equalsIgnoreCase(this.rawOption)) {
/* 169 */         toAppend = t.getLocalizedMessage();
/*     */       }
/* 171 */       else if ("short.fileName".equalsIgnoreCase(this.rawOption)) {
/* 172 */         toAppend = throwingMethod.getFileName();
/*     */       } 
/*     */       
/* 175 */       int len = buffer.length();
/* 176 */       if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
/* 177 */         buffer.append(' ');
/*     */       }
/* 179 */       buffer.append(toAppend);
/*     */       
/* 181 */       if (Strings.isNotBlank(suffix)) {
/* 182 */         buffer.append(' ');
/* 183 */         buffer.append(suffix);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void formatOption(Throwable throwable, String suffix, StringBuilder buffer) {
/* 189 */     int len = buffer.length();
/* 190 */     if (len > 0 && !Character.isWhitespace(buffer.charAt(len - 1))) {
/* 191 */       buffer.append(' ');
/*     */     }
/* 193 */     if (!this.options.allLines() || this.nonStandardLineSeparator || Strings.isNotBlank(suffix)) {
/* 194 */       StringWriter w = new StringWriter();
/* 195 */       throwable.printStackTrace(new PrintWriter(w));
/*     */       
/* 197 */       String[] array = w.toString().split(Strings.LINE_SEPARATOR);
/* 198 */       int limit = this.options.minLines(array.length) - 1;
/* 199 */       boolean suffixNotBlank = Strings.isNotBlank(suffix);
/* 200 */       for (int i = 0; i <= limit; i++) {
/* 201 */         buffer.append(array[i]);
/* 202 */         if (suffixNotBlank) {
/* 203 */           buffer.append(' ');
/* 204 */           buffer.append(suffix);
/*     */         } 
/* 206 */         if (i < limit) {
/* 207 */           buffer.append(this.options.getSeparator());
/*     */         }
/*     */       } 
/*     */     } else {
/* 211 */       throwable.printStackTrace(new PrintWriter((Writer)new StringBuilderWriter(buffer)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 222 */     return true;
/*     */   }
/*     */   
/*     */   protected String getSuffix(LogEvent event) {
/* 226 */     if (this.formatters.isEmpty()) {
/* 227 */       return "";
/*     */     }
/*     */     
/* 230 */     StringBuilder toAppendTo = new StringBuilder();
/* 231 */     for (int i = 0, size = this.formatters.size(); i < size; i++) {
/* 232 */       ((PatternFormatter)this.formatters.get(i)).format(event, toAppendTo);
/*     */     }
/* 234 */     return toAppendTo.toString();
/*     */   }
/*     */   
/*     */   public ThrowableFormatOptions getOptions() {
/* 238 */     return this.options;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\ThrowablePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */