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
/*    */ 
/*    */ public final class ARBTextureBufferObject
/*    */ {
/*    */   public static final int GL_TEXTURE_BUFFER_ARB = 35882;
/*    */   public static final int GL_MAX_TEXTURE_BUFFER_SIZE_ARB = 35883;
/*    */   public static final int GL_TEXTURE_BINDING_BUFFER_ARB = 35884;
/*    */   public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING_ARB = 35885;
/*    */   public static final int GL_TEXTURE_BUFFER_FORMAT_ARB = 35886;
/*    */   
/*    */   public static void glTexBufferARB(int target, int internalformat, int buffer) {
/* 31 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 32 */     long function_pointer = caps.glTexBufferARB;
/* 33 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 34 */     nglTexBufferARB(target, internalformat, buffer, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglTexBufferARB(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBTextureBufferObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */