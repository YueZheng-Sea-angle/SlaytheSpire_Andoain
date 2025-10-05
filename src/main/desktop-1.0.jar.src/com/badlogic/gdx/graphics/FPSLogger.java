/*    */ package com.badlogic.gdx.graphics;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.utils.TimeUtils;
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
/*    */ public class FPSLogger
/*    */ {
/* 30 */   long startTime = TimeUtils.nanoTime();
/*    */ 
/*    */ 
/*    */   
/*    */   public void log() {
/* 35 */     if (TimeUtils.nanoTime() - this.startTime > 1000000000L) {
/* 36 */       Gdx.app.log("FPSLogger", "fps: " + Gdx.graphics.getFramesPerSecond());
/* 37 */       this.startTime = TimeUtils.nanoTime();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\graphics\FPSLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */