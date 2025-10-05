/*    */ package org.apache.logging.log4j.message;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ @AsynchronouslyFormattable
/*    */ @PerformanceSensitive({"allocation"})
/*    */ public class StringMapMessage
/*    */   extends MapMessage<StringMapMessage, String>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public StringMapMessage() {}
/*    */   
/*    */   public StringMapMessage(int initialCapacity) {
/* 48 */     super(initialCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringMapMessage(Map<String, String> map) {
/* 58 */     super(map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringMapMessage newInstance(Map<String, String> map) {
/* 68 */     return new StringMapMessage(map);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\StringMapMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */