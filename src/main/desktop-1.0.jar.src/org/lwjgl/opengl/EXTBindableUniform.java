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
/*    */ public final class EXTBindableUniform
/*    */ {
/*    */   public static final int GL_MAX_VERTEX_BINDABLE_UNIFORMS_EXT = 36322;
/*    */   public static final int GL_MAX_FRAGMENT_BINDABLE_UNIFORMS_EXT = 36323;
/*    */   public static final int GL_MAX_GEOMETRY_BINDABLE_UNIFORMS_EXT = 36324;
/*    */   public static final int GL_MAX_BINDABLE_UNIFORM_SIZE_EXT = 36333;
/*    */   public static final int GL_UNIFORM_BUFFER_BINDING_EXT = 36335;
/*    */   public static final int GL_UNIFORM_BUFFER_EXT = 36334;
/*    */   
/*    */   static native void nglUniformBufferEXT(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*    */   
/*    */   public static void glUniformBufferEXT(int program, int location, int buffer) {
/* 30 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 31 */     long function_pointer = caps.glUniformBufferEXT;
/* 32 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 33 */     nglUniformBufferEXT(program, location, buffer, function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int glGetUniformBufferSizeEXT(int program, int location) {
/* 38 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 39 */     long function_pointer = caps.glGetUniformBufferSizeEXT;
/* 40 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 41 */     int __result = nglGetUniformBufferSizeEXT(program, location, function_pointer);
/* 42 */     return __result;
/*    */   }
/*    */ 
/*    */   
/*    */   public static long glGetUniformOffsetEXT(int program, int location) {
/* 47 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 48 */     long function_pointer = caps.glGetUniformOffsetEXT;
/* 49 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 50 */     long __result = nglGetUniformOffsetEXT(program, location, function_pointer);
/* 51 */     return __result;
/*    */   }
/*    */   
/*    */   static native int nglGetUniformBufferSizeEXT(int paramInt1, int paramInt2, long paramLong);
/*    */   
/*    */   static native long nglGetUniformOffsetEXT(int paramInt1, int paramInt2, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\EXTBindableUniform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */