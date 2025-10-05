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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EXTDepthBoundsTest
/*    */ {
/*    */   public static final int GL_DEPTH_BOUNDS_TEST_EXT = 34960;
/*    */   public static final int GL_DEPTH_BOUNDS_EXT = 34961;
/*    */   
/*    */   public static void glDepthBoundsEXT(double zmin, double zmax) {
/* 26 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 27 */     long function_pointer = caps.glDepthBoundsEXT;
/* 28 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 29 */     nglDepthBoundsEXT(zmin, zmax, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglDepthBoundsEXT(double paramDouble1, double paramDouble2, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\EXTDepthBoundsTest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */