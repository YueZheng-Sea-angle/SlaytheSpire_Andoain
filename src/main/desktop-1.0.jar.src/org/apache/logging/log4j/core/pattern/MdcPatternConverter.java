/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ import org.apache.logging.log4j.util.TriConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "MdcPatternConverter", category = "Converter")
/*     */ @ConverterKeys({"X", "mdc", "MDC"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class MdcPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final String key;
/*     */   private final String[] keys;
/*     */   private final boolean full;
/*     */   private static final TriConsumer<String, Object, StringBuilder> WRITE_KEY_VALUES_INTO;
/*     */   
/*     */   private MdcPatternConverter(String[] options) {
/*  51 */     super((options != null && options.length > 0) ? ("MDC{" + options[0] + '}') : "MDC", "mdc");
/*  52 */     if (options != null && options.length > 0) {
/*  53 */       this.full = false;
/*  54 */       if (options[0].indexOf(',') > 0) {
/*  55 */         this.keys = options[0].split(",");
/*  56 */         for (int i = 0; i < this.keys.length; i++) {
/*  57 */           this.keys[i] = this.keys[i].trim();
/*     */         }
/*  59 */         this.key = null;
/*     */       } else {
/*  61 */         this.keys = null;
/*  62 */         this.key = options[0];
/*     */       } 
/*     */     } else {
/*  65 */       this.full = true;
/*  66 */       this.key = null;
/*  67 */       this.keys = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MdcPatternConverter newInstance(String[] options) {
/*  78 */     return new MdcPatternConverter(options);
/*     */   }
/*     */   static {
/*  81 */     WRITE_KEY_VALUES_INTO = ((key, value, sb) -> {
/*     */         sb.append(key).append('=');
/*     */         StringBuilders.appendValue(sb, value);
/*     */         sb.append(", ");
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*  92 */     ReadOnlyStringMap contextData = event.getContextData();
/*     */ 
/*     */     
/*  95 */     if (this.full) {
/*  96 */       if (contextData == null || contextData.isEmpty()) {
/*  97 */         toAppendTo.append("{}");
/*     */         return;
/*     */       } 
/* 100 */       appendFully(contextData, toAppendTo);
/* 101 */     } else if (this.keys != null) {
/* 102 */       if (contextData == null || contextData.isEmpty()) {
/* 103 */         toAppendTo.append("{}");
/*     */         return;
/*     */       } 
/* 106 */       appendSelectedKeys(this.keys, contextData, toAppendTo);
/* 107 */     } else if (contextData != null) {
/*     */       
/* 109 */       Object value = contextData.getValue(this.key);
/* 110 */       if (value != null) {
/* 111 */         StringBuilders.appendValue(toAppendTo, value);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void appendFully(ReadOnlyStringMap contextData, StringBuilder toAppendTo) {
/* 117 */     toAppendTo.append("{");
/* 118 */     int start = toAppendTo.length();
/* 119 */     contextData.forEach(WRITE_KEY_VALUES_INTO, toAppendTo);
/* 120 */     int end = toAppendTo.length();
/* 121 */     if (end > start) {
/* 122 */       toAppendTo.setCharAt(end - 2, '}');
/* 123 */       toAppendTo.deleteCharAt(end - 1);
/*     */     } else {
/* 125 */       toAppendTo.append('}');
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendSelectedKeys(String[] keys, ReadOnlyStringMap contextData, StringBuilder sb) {
/* 131 */     int start = sb.length();
/* 132 */     sb.append('{');
/* 133 */     for (int i = 0; i < keys.length; i++) {
/* 134 */       String theKey = keys[i];
/* 135 */       Object value = contextData.getValue(theKey);
/* 136 */       if (value != null) {
/* 137 */         if (sb.length() - start > 1) {
/* 138 */           sb.append(", ");
/*     */         }
/* 140 */         sb.append(theKey).append('=');
/* 141 */         StringBuilders.appendValue(sb, value);
/*     */       } 
/*     */     } 
/* 144 */     sb.append('}');
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MdcPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */