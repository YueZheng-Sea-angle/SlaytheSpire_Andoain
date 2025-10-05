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
/*    */ 
/*    */ public final class NVPrimitiveRestart
/*    */ {
/*    */   public static final int GL_PRIMITIVE_RESTART_NV = 34136;
/*    */   public static final int GL_PRIMITIVE_RESTART_INDEX_NV = 34137;
/*    */   
/*    */   public static void glPrimitiveRestartNV() {
/* 27 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 28 */     long function_pointer = caps.glPrimitiveRestartNV;
/* 29 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 30 */     nglPrimitiveRestartNV(function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glPrimitiveRestartIndexNV(int index) {
/* 35 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 36 */     long function_pointer = caps.glPrimitiveRestartIndexNV;
/* 37 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 38 */     nglPrimitiveRestartIndexNV(index, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglPrimitiveRestartNV(long paramLong);
/*    */   
/*    */   static native void nglPrimitiveRestartIndexNV(int paramInt, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\NVPrimitiveRestart.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */