/*    */ package com.badlogic.gdx.scenes.scene2d.actions;
/*    */ 
/*    */ import com.badlogic.gdx.scenes.scene2d.Event;
/*    */ 
/*    */ 
/*    */ public class CountdownEventAction<T extends Event>
/*    */   extends EventAction<T>
/*    */ {
/*    */   int count;
/*    */   int current;
/*    */   
/*    */   public CountdownEventAction(Class<? extends T> eventClass, int count) {
/* 13 */     super(eventClass);
/* 14 */     this.count = count;
/*    */   }
/*    */   
/*    */   public boolean handle(T event) {
/* 18 */     this.current++;
/* 19 */     return (this.current >= this.count);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\badlogic\gdx\scenes\scene2d\actions\CountdownEventAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */