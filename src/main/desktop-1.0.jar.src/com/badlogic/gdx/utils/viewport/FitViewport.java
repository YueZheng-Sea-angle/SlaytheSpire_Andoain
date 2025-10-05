/*    */ package com.badlogic.gdx.utils.viewport;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Camera;
/*    */ import com.badlogic.gdx.utils.Scaling;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FitViewport
/*    */   extends ScalingViewport
/*    */ {
/*    */   public FitViewport(float worldWidth, float worldHeight) {
/* 15 */     super(Scaling.fit, worldWidth, worldHeight);
/*    */   }
/*    */   
/*    */   public FitViewport(float worldWidth, float worldHeight, Camera camera) {
/* 19 */     super(Scaling.fit, worldWidth, worldHeight, camera);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gd\\utils\viewport\FitViewport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */