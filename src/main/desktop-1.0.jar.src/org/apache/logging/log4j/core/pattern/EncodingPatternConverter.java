/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.layout.PatternLayout;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @Plugin(name = "encode", category = "Converter")
/*     */ @ConverterKeys({"enc", "encode"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class EncodingPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final List<PatternFormatter> formatters;
/*     */   private final EscapeFormat escapeFormat;
/*     */   
/*     */   private EncodingPatternConverter(List<PatternFormatter> formatters, EscapeFormat escapeFormat) {
/*  51 */     super("encode", "encode");
/*  52 */     this.formatters = formatters;
/*  53 */     this.escapeFormat = escapeFormat;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/*  58 */     return (this.formatters != null && this.formatters.stream()
/*  59 */       .map(PatternFormatter::getConverter)
/*  60 */       .anyMatch(LogEventPatternConverter::handlesThrowable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static EncodingPatternConverter newInstance(Configuration config, String[] options) {
/*  71 */     if (options.length > 2 || options.length == 0) {
/*  72 */       LOGGER.error("Incorrect number of options on escape. Expected 1 or 2, but received {}", 
/*  73 */           Integer.valueOf(options.length));
/*  74 */       return null;
/*     */     } 
/*  76 */     if (options[0] == null) {
/*  77 */       LOGGER.error("No pattern supplied on escape");
/*  78 */       return null;
/*     */     } 
/*     */     
/*  81 */     EscapeFormat escapeFormat = (options.length < 2) ? EscapeFormat.HTML : (EscapeFormat)EnglishEnums.valueOf(EscapeFormat.class, options[1], EscapeFormat.HTML);
/*  82 */     PatternParser parser = PatternLayout.createPatternParser(config);
/*  83 */     List<PatternFormatter> formatters = parser.parse(options[0]);
/*  84 */     return new EncodingPatternConverter(formatters, escapeFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  92 */     int start = toAppendTo.length();
/*  93 */     for (int i = 0; i < this.formatters.size(); i++) {
/*  94 */       ((PatternFormatter)this.formatters.get(i)).format(event, toAppendTo);
/*     */     }
/*  96 */     this.escapeFormat.escape(toAppendTo, start);
/*     */   }
/*     */   
/*     */   private enum EscapeFormat {
/* 100 */     HTML
/*     */     {
/*     */ 
/*     */       
/*     */       void escape(StringBuilder toAppendTo, int start)
/*     */       {
/* 106 */         int origLength = toAppendTo.length();
/* 107 */         int firstSpecialChar = origLength;
/*     */         int i;
/* 109 */         for (i = origLength - 1; i >= start; i--) {
/* 110 */           char c = toAppendTo.charAt(i);
/* 111 */           String escaped = escapeChar(c);
/* 112 */           if (escaped != null) {
/* 113 */             firstSpecialChar = i;
/* 114 */             for (int k = 0; k < escaped.length() - 1; k++) {
/* 115 */               toAppendTo.append(' ');
/*     */             }
/*     */           } 
/*     */         } 
/*     */         int j;
/* 120 */         for (i = origLength - 1, j = toAppendTo.length(); i >= firstSpecialChar; i--) {
/* 121 */           char c = toAppendTo.charAt(i);
/* 122 */           String escaped = escapeChar(c);
/* 123 */           if (escaped == null) {
/* 124 */             toAppendTo.setCharAt(--j, c);
/*     */           } else {
/* 126 */             toAppendTo.replace(j - escaped.length(), j, escaped);
/* 127 */             j -= escaped.length();
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       private String escapeChar(char c) {
/* 133 */         switch (c) {
/*     */           case '\r':
/* 135 */             return "\\r";
/*     */           case '\n':
/* 137 */             return "\\n";
/*     */           case '&':
/* 139 */             return "&amp;";
/*     */           case '<':
/* 141 */             return "&lt;";
/*     */           case '>':
/* 143 */             return "&gt;";
/*     */           case '"':
/* 145 */             return "&quot;";
/*     */           case '\'':
/* 147 */             return "&apos;";
/*     */           case '/':
/* 149 */             return "&#x2F;";
/*     */         } 
/* 151 */         return null;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     JSON
/*     */     {
/*     */       void escape(StringBuilder toAppendTo, int start) {
/* 164 */         StringBuilders.escapeJson(toAppendTo, start);
/*     */       }
/*     */     },
/*     */     
/* 168 */     CRLF
/*     */     {
/*     */ 
/*     */       
/*     */       void escape(StringBuilder toAppendTo, int start)
/*     */       {
/* 174 */         int origLength = toAppendTo.length();
/* 175 */         int firstSpecialChar = origLength;
/*     */         int i;
/* 177 */         for (i = origLength - 1; i >= start; i--) {
/* 178 */           char c = toAppendTo.charAt(i);
/* 179 */           if (c == '\r' || c == '\n') {
/* 180 */             firstSpecialChar = i;
/* 181 */             toAppendTo.append(' ');
/*     */           } 
/*     */         } 
/*     */         int j;
/* 185 */         for (i = origLength - 1, j = toAppendTo.length(); i >= firstSpecialChar; i--) {
/* 186 */           char c = toAppendTo.charAt(i);
/* 187 */           switch (c) {
/*     */             case '\r':
/* 189 */               toAppendTo.setCharAt(--j, 'r');
/* 190 */               toAppendTo.setCharAt(--j, '\\');
/*     */               break;
/*     */             case '\n':
/* 193 */               toAppendTo.setCharAt(--j, 'n');
/* 194 */               toAppendTo.setCharAt(--j, '\\');
/*     */               break;
/*     */             default:
/* 197 */               toAppendTo.setCharAt(--j, c);
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         } 
/*     */       }
/*     */     },
/* 208 */     XML
/*     */     {
/*     */       void escape(StringBuilder toAppendTo, int start) {
/* 211 */         StringBuilders.escapeXml(toAppendTo, start);
/*     */       }
/*     */     };
/*     */     
/*     */     abstract void escape(StringBuilder param1StringBuilder, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\EncodingPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */