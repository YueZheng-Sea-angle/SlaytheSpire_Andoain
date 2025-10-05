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
/*    */ public class RemoveActorAction
/*    */   extends Action
/*    */ {
/*    */   private boolean removed;
/*    */   
/*    */   public boolean act(float delta) {
/* 28 */     if (!this.removed) {
/* 29 */       this.removed = true;
/* 30 */       this.target.remove();
/*    */     } 
/* 32 */     return true;
/*    */   }
/*    */   
/*    */   public void restart() {
/* 36 */     this.removed = false;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\scenes\scene2d\actions\RemoveActorAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */