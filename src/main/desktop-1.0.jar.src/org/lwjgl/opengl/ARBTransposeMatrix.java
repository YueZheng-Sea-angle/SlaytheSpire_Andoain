/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.MemoryUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ARBTransposeMatrix
/*    */ {
/*    */   public static final int GL_TRANSPOSE_MODELVIEW_MATRIX_ARB = 34019;
/*    */   public static final int GL_TRANSPOSE_PROJECTION_MATRIX_ARB = 34020;
/*    */   public static final int GL_TRANSPOSE_TEXTURE_MATRIX_ARB = 34021;
/*    */   public static final int GL_TRANSPOSE_COLOR_MATRIX_ARB = 34022;
/*    */   
/*    */   public static void glLoadTransposeMatrixARB(FloatBuffer pfMtx) {
/* 18 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 19 */     long function_pointer = caps.glLoadTransposeMatrixfARB;
/* 20 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 21 */     BufferChecks.checkBuffer(pfMtx, 16);
/* 22 */     nglLoadTransposeMatrixfARB(MemoryUtil.getAddress(pfMtx), function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glMultTransposeMatrixARB(FloatBuffer pfMtx) {
/* 27 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 28 */     long function_pointer = caps.glMultTransposeMatrixfARB;
/* 29 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 30 */     BufferChecks.checkBuffer(pfMtx, 16);
/* 31 */     nglMultTransposeMatrixfARB(MemoryUtil.getAddress(pfMtx), function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglLoadTransposeMatrixfARB(long paramLong1, long paramLong2);
/*    */   
/*    */   static native void nglMultTransposeMatrixfARB(long paramLong1, long paramLong2);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBTransposeMatrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */