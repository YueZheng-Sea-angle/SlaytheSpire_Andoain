/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MultiformatMessage;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.MultiFormatStringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "MessagePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"m", "msg", "message"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class MessagePatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private static final String LOOKUPS = "lookups";
/*     */   private static final String NOLOOKUPS = "nolookups";
/*     */   
/*     */   private MessagePatternConverter() {
/*  47 */     super("Message", "message");
/*     */   }
/*     */   
/*     */   private static TextRenderer loadMessageRenderer(String[] options) {
/*  51 */     if (options != null) {
/*  52 */       for (String option : options) {
/*  53 */         switch (option.toUpperCase(Locale.ROOT)) {
/*     */           case "ANSI":
/*  55 */             if (Loader.isJansiAvailable()) {
/*  56 */               return new JAnsiTextRenderer(options, JAnsiTextRenderer.DefaultMessageStyleMap);
/*     */             }
/*  58 */             StatusLogger.getLogger()
/*  59 */               .warn("You requested ANSI message rendering but JANSI is not on the classpath.");
/*  60 */             return null;
/*     */           case "HTML":
/*  62 */             return new HtmlTextRenderer(options);
/*     */         } 
/*     */       } 
/*     */     }
/*  66 */     return null;
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
/*     */   public static MessagePatternConverter newInstance(Configuration config, String[] options) {
/*  79 */     String[] formats = withoutLookupOptions(options);
/*  80 */     TextRenderer textRenderer = loadMessageRenderer(formats);
/*     */     
/*  82 */     MessagePatternConverter result = (formats == null || formats.length == 0) ? SimpleMessagePatternConverter.INSTANCE : new FormattedMessagePatternConverter(formats);
/*     */     
/*  84 */     if (textRenderer != null) {
/*  85 */       result = new RenderingPatternConverter(result, textRenderer);
/*     */     }
/*  87 */     return result;
/*     */   }
/*     */   
/*     */   private static String[] withoutLookupOptions(String[] options) {
/*  91 */     if (options == null || options.length == 0) {
/*  92 */       return options;
/*     */     }
/*  94 */     List<String> results = new ArrayList<>(options.length);
/*  95 */     for (String option : options) {
/*  96 */       if ("lookups".equalsIgnoreCase(option) || "nolookups".equalsIgnoreCase(option)) {
/*  97 */         LOGGER.info("The {} option will be ignored. Message Lookups are no longer supported.", option);
/*     */       } else {
/*  99 */         results.add(option);
/*     */       } 
/*     */     } 
/* 102 */     return results.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 107 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   private static final class SimpleMessagePatternConverter extends MessagePatternConverter {
/* 111 */     private static final MessagePatternConverter INSTANCE = new SimpleMessagePatternConverter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void format(LogEvent event, StringBuilder toAppendTo) {
/* 118 */       Message msg = event.getMessage();
/* 119 */       if (msg instanceof StringBuilderFormattable) {
/* 120 */         ((StringBuilderFormattable)msg).formatTo(toAppendTo);
/* 121 */       } else if (msg != null) {
/* 122 */         toAppendTo.append(msg.getFormattedMessage());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class FormattedMessagePatternConverter
/*     */     extends MessagePatternConverter {
/*     */     private final String[] formats;
/*     */     
/*     */     FormattedMessagePatternConverter(String[] formats) {
/* 132 */       this.formats = formats;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void format(LogEvent event, StringBuilder toAppendTo) {
/* 140 */       Message msg = event.getMessage();
/* 141 */       if (msg instanceof StringBuilderFormattable) {
/* 142 */         if (msg instanceof MultiFormatStringBuilderFormattable) {
/* 143 */           ((MultiFormatStringBuilderFormattable)msg).formatTo(this.formats, toAppendTo);
/*     */         } else {
/* 145 */           ((StringBuilderFormattable)msg).formatTo(toAppendTo);
/*     */         } 
/* 147 */       } else if (msg != null) {
/* 148 */         toAppendTo.append((msg instanceof MultiformatMessage) ? ((MultiformatMessage)msg)
/* 149 */             .getFormattedMessage(this.formats) : msg
/* 150 */             .getFormattedMessage());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class RenderingPatternConverter
/*     */     extends MessagePatternConverter {
/*     */     private final MessagePatternConverter delegate;
/*     */     private final TextRenderer textRenderer;
/*     */     
/*     */     RenderingPatternConverter(MessagePatternConverter delegate, TextRenderer textRenderer) {
/* 161 */       this.delegate = delegate;
/* 162 */       this.textRenderer = textRenderer;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void format(LogEvent event, StringBuilder toAppendTo) {
/* 170 */       StringBuilder workingBuilder = new StringBuilder(80);
/* 171 */       this.delegate.format(event, workingBuilder);
/* 172 */       this.textRenderer.render(workingBuilder, toAppendTo);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MessagePatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */