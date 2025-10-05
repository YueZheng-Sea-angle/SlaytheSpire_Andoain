/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NVDrawTexture
/*    */ {
/*    */   public static void glDrawTextureNV(int texture, int sampler, float x0, float y0, float x1, float y1, float z, float s0, float t0, float s1, float t1) {
/* 13 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 14 */     long function_pointer = caps.glDrawTextureNV;
/* 15 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 16 */     nglDrawTextureNV(texture, sampler, x0, y0, x1, y1, z, s0, t0, s1, t1, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglDrawTextureNV(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\NVDrawTexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */