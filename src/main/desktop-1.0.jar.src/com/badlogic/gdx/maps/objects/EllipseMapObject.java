/*    */ package com.badlogic.gdx.maps.objects;
/*    */ 
/*    */ import com.badlogic.gdx.maps.MapObject;
/*    */ import com.badlogic.gdx.math.Ellipse;
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
/*    */ public class EllipseMapObject
/*    */   extends MapObject
/*    */ {
/*    */   private Ellipse ellipse;
/*    */   
/*    */   public Ellipse getEllipse() {
/* 30 */     return this.ellipse;
/*    */   }
/*    */ 
/*    */   
/*    */   public EllipseMapObject() {
/* 35 */     this(0.0F, 0.0F, 1.0F, 1.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EllipseMapObject(float x, float y, float width, float height) {
/* 46 */     this.ellipse = new Ellipse(x, y, width, height);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\maps\objects\EllipseMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */