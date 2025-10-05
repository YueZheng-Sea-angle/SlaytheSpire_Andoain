/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EXTStencilTwoSide
/*    */ {
/*    */   public static final int GL_STENCIL_TEST_TWO_SIDE_EXT = 35088;
/*    */   public static final int GL_ACTIVE_STENCIL_FACE_EXT = 35089;
/*    */   
/*    */   public static void glActiveStencilFaceEXT(int face) {
/* 16 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 17 */     long function_pointer = caps.glActiveStencilFaceEXT;
/* 18 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 19 */     nglActiveStencilFaceEXT(face, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglActiveStencilFaceEXT(int paramInt, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\EXTStencilTwoSide.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */