/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.message.MapMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "MapPatternConverter", category = "Converter")
/*     */ @ConverterKeys({"K", "map", "MAP"})
/*     */ public final class MapPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*  36 */   private static final String JAVA_UNQUOTED = MapMessage.MapFormat.JAVA_UNQUOTED.name();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String key;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String[] format;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MapPatternConverter(String[] options, String... format) {
/*  57 */     super((options != null && options.length > 0) ? ("MAP{" + options[0] + '}') : "MAP", "map");
/*  58 */     this.key = (options != null && options.length > 0) ? options[0] : null;
/*  59 */     this.format = format;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MapPatternConverter newInstance(String[] options) {
/*  69 */     return new MapPatternConverter(options, new String[] { JAVA_UNQUOTED });
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
/*     */   public static MapPatternConverter newInstance(String[] options, MapMessage.MapFormat format) {
/*  81 */     return new MapPatternConverter(options, new String[] { Objects.toString(format, JAVA_UNQUOTED) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/*     */     MapMessage msg;
/*  90 */     if (event.getMessage() instanceof MapMessage) {
/*  91 */       msg = (MapMessage)event.getMessage();
/*     */     } else {
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/*  97 */     if (this.key == null) {
/*  98 */       msg.formatTo(this.format, toAppendTo);
/*     */     } else {
/*     */       
/* 101 */       String val = msg.get(this.key);
/*     */       
/* 103 */       if (val != null)
/* 104 */         toAppendTo.append(val); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\MapPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */