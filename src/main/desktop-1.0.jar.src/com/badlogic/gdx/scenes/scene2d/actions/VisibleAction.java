/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Action;
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
/*    */ public class VisibleAction
/*    */   extends Action
/*    */ {
/*    */   private boolean visible;
/*    */   
/*    */   public boolean act(float delta) {
/* 28 */     this.target.setVisible(this.visible);
/* 29 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isVisible() {
/* 33 */     return this.visible;
/*    */   }
/*    */   
/*    */   public void setVisible(boolean visible) {
/* 37 */     this.visible = visible;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\scenes\scene2d\actions\VisibleAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */