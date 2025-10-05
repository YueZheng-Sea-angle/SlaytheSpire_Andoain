/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import org.lwjgl.PointerWrapperAbstract;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class CLObject
/*    */   extends PointerWrapperAbstract
/*    */ {
/*    */   protected CLObject(long pointer) {
/* 44 */     super(pointer);
/*    */   }
/*    */   
/*    */   final long getPointerUnsafe() {
/* 48 */     return this.pointer;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */