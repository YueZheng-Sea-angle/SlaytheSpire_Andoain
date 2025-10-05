/*    */ package org.lwjgl.util.mapped;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MappedSet2
/*    */ {
/*    */   private final MappedObject a;
/*    */   private final MappedObject b;
/*    */   public int view;
/*    */   
/*    */   MappedSet2(MappedObject a, MappedObject b) {
/* 40 */     this.a = a;
/* 41 */     this.b = b;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void view(int view) {
/* 47 */     this.a.setViewAddress(this.a.getViewAddress(view));
/* 48 */     this.b.setViewAddress(this.b.getViewAddress(view));
/*    */   }
/*    */   
/*    */   public void next() {
/* 52 */     this.a.next();
/* 53 */     this.b.next();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\mapped\MappedSet2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */