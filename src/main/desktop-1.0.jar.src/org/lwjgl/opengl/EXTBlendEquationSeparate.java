/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
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
/*    */ public final class EXTBlendEquationSeparate
/*    */ {
/*    */   public static final int GL_BLEND_EQUATION_RGB_EXT = 32777;
/*    */   public static final int GL_BLEND_EQUATION_ALPHA_EXT = 34877;
/*    */   
/*    */   public static void glBlendEquationSeparateEXT(int modeRGB, int modeAlpha) {
/* 20 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 21 */     long function_pointer = caps.glBlendEquationSeparateEXT;
/* 22 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 23 */     nglBlendEquationSeparateEXT(modeRGB, modeAlpha, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglBlendEquationSeparateEXT(int paramInt1, int paramInt2, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\EXTBlendEquationSeparate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */