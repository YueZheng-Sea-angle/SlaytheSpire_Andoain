/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.MemoryUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AMDSamplePositions
/*    */ {
/*    */   public static final int GL_SUBSAMPLE_DISTANCE_AMD = 34879;
/*    */   
/*    */   public static void glSetMultisampleAMD(int pname, int index, FloatBuffer val) {
/* 18 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 19 */     long function_pointer = caps.glSetMultisamplefvAMD;
/* 20 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 21 */     BufferChecks.checkBuffer(val, 2);
/* 22 */     nglSetMultisamplefvAMD(pname, index, MemoryUtil.getAddress(val), function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglSetMultisamplefvAMD(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\AMDSamplePositions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */