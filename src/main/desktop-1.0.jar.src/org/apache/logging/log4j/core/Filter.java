/*    */ package org.apache.logging.log4j.core;
/*    */ 
/*    */ import org.apache.logging.log4j.Level;
/*    */ import org.apache.logging.log4j.Marker;
/*    */ import org.apache.logging.log4j.message.Message;
/*    */ import org.apache.logging.log4j.util.EnglishEnums;
/*    */ 
/*    */ 
/*    */ public interface Filter
/*    */   extends LifeCycle
/*    */ {
/*    */   public static final String ELEMENT_TYPE = "filter";
/*    */   
/*    */   Result getOnMismatch();
/*    */   
/*    */   Result getOnMatch();
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
/*    */   
/* 38 */   public static final Filter[] EMPTY_ARRAY = new Filter[0];
/*    */ 
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
/*    */ 
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
/*    */ 
/*    */   
/*    */   Result filter(Logger paramLogger, Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
/*    */ 
/*    */   
/*    */   Result filter(LogEvent paramLogEvent);
/*    */ 
/*    */   
/*    */   public enum Result
/*    */   {
/* 55 */     ACCEPT,
/*    */ 
/*    */ 
/*    */     
/* 59 */     NEUTRAL,
/*    */ 
/*    */ 
/*    */     
/* 63 */     DENY;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static Result toResult(String name) {
/* 72 */       return toResult(name, null);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public static Result toResult(String name, Result defaultResult) {
/* 83 */       return (Result)EnglishEnums.valueOf(Result.class, name, defaultResult);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\Filter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */