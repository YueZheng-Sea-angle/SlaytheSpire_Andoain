/*    */ package org.apache.logging.log4j;
/*    */ 
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.util.Supplier;
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
/*    */ public interface LogBuilder
/*    */ {
/* 29 */   public static final LogBuilder NOOP = new LogBuilder()
/*    */     {
/*    */     
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   default LogBuilder withMarker(Marker marker) {
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default LogBuilder withThrowable(Throwable throwable) {
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default LogBuilder withLocation() {
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default LogBuilder withLocation(StackTraceElement location) {
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   default void log(CharSequence message) {}
/*    */   
/*    */   default void log(String message) {}
/*    */   
/*    */   void log(String message, Object... params) {}
/*    */   
/*    */   void log(String message, Supplier<?>... params) {}
/*    */   
/*    */   default void log(Message message) {}
/*    */   
/*    */   default void log(Supplier<Message> messageSupplier) {}
/*    */   
/*    */   default void log(Object message) {}
/*    */   
/*    */   default void log(String message, Object p0) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {}
/*    */   
/*    */   default void log(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {}
/*    */   
/*    */   default void log() {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\LogBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */