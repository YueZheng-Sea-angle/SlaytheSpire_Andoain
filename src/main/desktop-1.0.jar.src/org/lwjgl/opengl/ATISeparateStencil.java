/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ATISeparateStencil
/*    */ {
/*    */   public static final int GL_STENCIL_BACK_FUNC_ATI = 34816;
/*    */   public static final int GL_STENCIL_BACK_FAIL_ATI = 34817;
/*    */   public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL_ATI = 34818;
/*    */   public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS_ATI = 34819;
/*    */   
/*    */   public static void glStencilOpSeparateATI(int face, int sfail, int dpfail, int dppass) {
/* 18 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 19 */     long function_pointer = caps.glStencilOpSeparateATI;
/* 20 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 21 */     nglStencilOpSeparateATI(face, sfail, dpfail, dppass, function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glStencilFuncSeparateATI(int frontfunc, int backfunc, int ref, int mask) {
/* 26 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 27 */     long function_pointer = caps.glStencilFuncSeparateATI;
/* 28 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 29 */     nglStencilFuncSeparateATI(frontfunc, backfunc, ref, mask, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglStencilOpSeparateATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*    */   
/*    */   static native void nglStencilFuncSeparateATI(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ATISeparateStencil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */