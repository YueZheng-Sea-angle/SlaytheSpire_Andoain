/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NVTextureBarrier
/*    */ {
/*    */   public static void glTextureBarrierNV() {
/* 13 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 14 */     long function_pointer = caps.glTextureBarrierNV;
/* 15 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 16 */     nglTextureBarrierNV(function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglTextureBarrierNV(long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\NVTextureBarrier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */