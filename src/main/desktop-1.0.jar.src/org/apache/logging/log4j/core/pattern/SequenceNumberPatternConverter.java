/*    */ package org.apache.logging.log4j.core.pattern;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.apache.logging.log4j.core.LogEvent;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "SequenceNumberPatternConverter", category = "Converter")
/*    */ @ConverterKeys({"sn", "sequenceNumber"})
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public final class SequenceNumberPatternConverter
/*    */   extends LogEventPatternConverter
/*    */ {
/* 33 */   private static final AtomicLong SEQUENCE = new AtomicLong();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private SequenceNumberPatternConverter() {
/* 45 */     super("Sequence Number", "sn");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SequenceNumberPatternConverter newInstance(String[] options) {
/* 55 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 63 */     toAppendTo.append(SEQUENCE.incrementAndGet());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\pattern\SequenceNumberPatternConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */