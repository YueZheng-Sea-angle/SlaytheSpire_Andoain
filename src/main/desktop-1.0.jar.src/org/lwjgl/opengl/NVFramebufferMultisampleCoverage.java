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
/*    */ public final class NVFramebufferMultisampleCoverage
/*    */ {
/*    */   public static final int GL_RENDERBUFFER_COVERAGE_SAMPLES_NV = 36011;
/*    */   public static final int GL_RENDERBUFFER_COLOR_SAMPLES_NV = 36368;
/*    */   public static final int GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV = 36369;
/*    */   public static final int GL_MULTISAMPLE_COVERAGE_MODES_NV = 36370;
/*    */   
/*    */   public static void glRenderbufferStorageMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalformat, int width, int height) {
/* 25 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 26 */     long function_pointer = caps.glRenderbufferStorageMultisampleCoverageNV;
/* 27 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 28 */     nglRenderbufferStorageMultisampleCoverageNV(target, coverageSamples, colorSamples, internalformat, width, height, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglRenderbufferStorageMultisampleCoverageNV(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\NVFramebufferMultisampleCoverage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */