/*    */ package com.badlogic.gdx.maps.objects;
/*    */ 
/*    */ import com.badlogic.gdx.maps.MapObject;
/*    */ import com.badlogic.gdx.math.Circle;
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
/*    */ public class CircleMapObject
/*    */   extends MapObject
/*    */ {
/*    */   private Circle circle;
/*    */   
/*    */   public Circle getCircle() {
/* 29 */     return this.circle;
/*    */   }
/*    */ 
/*    */   
/*    */   public CircleMapObject() {
/* 34 */     this(0.0F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CircleMapObject(float x, float y, float radius) {
/* 44 */     this.circle = new Circle(x, y, radius);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\maps\objects\CircleMapObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */