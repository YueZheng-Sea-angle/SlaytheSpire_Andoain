/*    */ package com.badlogic.gdx.utils;
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
/*    */ public final class TimeUtils
/*    */ {
/*    */   private static final long nanosPerMilli = 1000000L;
/*    */   
/*    */   public static long nanoTime() {
/* 24 */     return System.nanoTime();
/*    */   }
/*    */ 
/*    */   
/*    */   public static long millis() {
/* 29 */     return System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long nanosToMillis(long nanos) {
/* 38 */     return nanos / 1000000L;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long millisToNanos(long millis) {
/* 45 */     return millis * 1000000L;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long timeSinceNanos(long prevTime) {
/* 52 */     return nanoTime() - prevTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long timeSinceMillis(long prevTime) {
/* 59 */     return millis() - prevTime;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gd\\utils\TimeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */