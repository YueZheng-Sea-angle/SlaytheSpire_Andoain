/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import org.lwjgl.LWJGLUtil;
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
/*    */ abstract class CLObjectChild<P extends CLObject>
/*    */   extends CLObjectRetainable
/*    */ {
/*    */   private final P parent;
/*    */   
/*    */   protected CLObjectChild(long pointer, P parent) {
/* 46 */     super(pointer);
/*    */     
/* 48 */     if (LWJGLUtil.DEBUG && parent != null && !parent.isValid()) {
/* 49 */       throw new IllegalStateException("The parent specified is not a valid CL object.");
/*    */     }
/* 51 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public P getParent() {
/* 55 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLObjectChild.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */