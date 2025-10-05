/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public abstract class AbstractStyleNameConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private final List<PatternFormatter> formatters;
/*     */   private final String style;
/*     */   
/*     */   protected AbstractStyleNameConverter(String name, List<PatternFormatter> formatters, String styling) {
/*  47 */     super(name, "style");
/*  48 */     this.formatters = formatters;
/*  49 */     this.style = styling;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "black", category = "Converter")
/*     */   @ConverterKeys({"black"})
/*     */   public static final class Black
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "black";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Black(List<PatternFormatter> formatters, String styling) {
/*  69 */       super("black", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Black newInstance(Configuration config, String[] options) {
/*  81 */       return newInstance(Black.class, "black", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "blue", category = "Converter")
/*     */   @ConverterKeys({"blue"})
/*     */   public static final class Blue
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "blue";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Blue(List<PatternFormatter> formatters, String styling) {
/* 102 */       super("blue", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Blue newInstance(Configuration config, String[] options) {
/* 114 */       return newInstance(Blue.class, "blue", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "cyan", category = "Converter")
/*     */   @ConverterKeys({"cyan"})
/*     */   public static final class Cyan
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "cyan";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Cyan(List<PatternFormatter> formatters, String styling) {
/* 135 */       super("cyan", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Cyan newInstance(Configuration config, String[] options) {
/* 147 */       return newInstance(Cyan.class, "cyan", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "green", category = "Converter")
/*     */   @ConverterKeys({"green"})
/*     */   public static final class Green
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "green";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Green(List<PatternFormatter> formatters, String styling) {
/* 168 */       super("green", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Green newInstance(Configuration config, String[] options) {
/* 180 */       return newInstance(Green.class, "green", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "magenta", category = "Converter")
/*     */   @ConverterKeys({"magenta"})
/*     */   public static final class Magenta
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "magenta";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Magenta(List<PatternFormatter> formatters, String styling) {
/* 201 */       super("magenta", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Magenta newInstance(Configuration config, String[] options) {
/* 213 */       return newInstance(Magenta.class, "magenta", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "red", category = "Converter")
/*     */   @ConverterKeys({"red"})
/*     */   public static final class Red
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "red";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Red(List<PatternFormatter> formatters, String styling) {
/* 234 */       super("red", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Red newInstance(Configuration config, String[] options) {
/* 246 */       return newInstance(Red.class, "red", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "white", category = "Converter")
/*     */   @ConverterKeys({"white"})
/*     */   public static final class White
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "white";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public White(List<PatternFormatter> formatters, String styling) {
/* 267 */       super("white", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static White newInstance(Configuration config, String[] options) {
/* 279 */       return newInstance(White.class, "white", config, options);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Plugin(name = "yellow", category = "Converter")
/*     */   @ConverterKeys({"yellow"})
/*     */   public static final class Yellow
/*     */     extends AbstractStyleNameConverter
/*     */   {
/*     */     protected static final String NAME = "yellow";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Yellow(List<PatternFormatter> formatters, String styling) {
/* 300 */       super("yellow", formatters, styling);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Yellow newInstance(Configuration config, String[] options) {
/* 312 */       return newInstance(Yellow.class, "yellow", config, options);
/*     */     }
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
/*     */   protected static <T extends AbstractStyleNameConverter> T newInstance(Class<T> asnConverterClass, String name, Configuration config, String[] options) {
/* 327 */     List<PatternFormatter> formatters = toPatternFormatterList(config, options);
/* 328 */     if (formatters == null) {
/* 329 */       return null;
/*     */     }
/*     */     try {
/* 332 */       Constructor<T> constructor = asnConverterClass.getConstructor(new Class[] { List.class, String.class });
/* 333 */       return constructor.newInstance(new Object[] { formatters, AnsiEscape.createSequence(new String[] { name }) });
/* 334 */     } catch (SecurityException|NoSuchMethodException|IllegalArgumentException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*     */       
/* 336 */       LOGGER.error(e.toString(), e);
/*     */       
/* 338 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<PatternFormatter> toPatternFormatterList(Configuration config, String[] options) {
/* 349 */     if (options.length == 0 || options[0] == null) {
/* 350 */       LOGGER.error("No pattern supplied on style for config=" + config);
/* 351 */       return null;
/*     */     } 
/* 353 */     PatternParser parser = PatternLayout.createPatternParser(config);
/* 354 */     if (parser == null) {
/* 355 */       LOGGER.error("No PatternParser created for config=" + config + ", options=" + Arrays.toString((Object[])options));
/* 356 */       return null;
/*     */     } 
/* 358 */     return parser.parse(options[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 367 */     int start = toAppendTo.length();
/* 368 */     for (int i = 0; i < this.formatters.size(); i++) {
/* 369 */       PatternFormatter formatter = this.formatters.get(i);
/* 370 */       formatter.format(event, toAppendTo);
/*     */     } 
/* 372 */     if (toAppendTo.length() > start) {
/* 373 */       toAppendTo.insert(start, this.style);
/* 374 */       toAppendTo.append(AnsiEscape.getDefaultStyle());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\AbstractStyleNameConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */