/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.MemoryUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EXTPointParameters
/*    */ {
/*    */   public static final int GL_POINT_SIZE_MIN_EXT = 33062;
/*    */   public static final int GL_POINT_SIZE_MAX_EXT = 33063;
/*    */   public static final int GL_POINT_FADE_THRESHOLD_SIZE_EXT = 33064;
/*    */   public static final int GL_DISTANCE_ATTENUATION_EXT = 33065;
/*    */   
/*    */   public static void glPointParameterfEXT(int pname, float param) {
/* 18 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 19 */     long function_pointer = caps.glPointParameterfEXT;
/* 20 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 21 */     nglPointParameterfEXT(pname, param, function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glPointParameterEXT(int pname, FloatBuffer pfParams) {
/* 26 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 27 */     long function_pointer = caps.glPointParameterfvEXT;
/* 28 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 29 */     BufferChecks.checkBuffer(pfParams, 4);
/* 30 */     nglPointParameterfvEXT(pname, MemoryUtil.getAddress(pfParams), function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglPointParameterfEXT(int paramInt, float paramFloat, long paramLong);
/*    */   
/*    */   static native void nglPointParameterfvEXT(int paramInt, long paramLong1, long paramLong2);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\EXTPointParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */