/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.LongBuffer;
/*     */ import org.lwjgl.BufferChecks;
/*     */ import org.lwjgl.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ARBBindlessTexture
/*     */ {
/*     */   public static final int GL_UNSIGNED_INT64_ARB = 5135;
/*     */   
/*     */   static native long nglGetTextureHandleARB(int paramInt, long paramLong);
/*     */   
/*     */   public static long glGetTextureHandleARB(int texture) {
/*  18 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  19 */     long function_pointer = caps.glGetTextureHandleARB;
/*  20 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  21 */     long __result = nglGetTextureHandleARB(texture, function_pointer);
/*  22 */     return __result;
/*     */   }
/*     */   static native long nglGetTextureSamplerHandleARB(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static long glGetTextureSamplerHandleARB(int texture, int sampler) {
/*  27 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  28 */     long function_pointer = caps.glGetTextureSamplerHandleARB;
/*  29 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  30 */     long __result = nglGetTextureSamplerHandleARB(texture, sampler, function_pointer);
/*  31 */     return __result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glMakeTextureHandleResidentARB(long handle) {
/*  36 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  37 */     long function_pointer = caps.glMakeTextureHandleResidentARB;
/*  38 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  39 */     nglMakeTextureHandleResidentARB(handle, function_pointer);
/*     */   }
/*     */   static native void nglMakeTextureHandleResidentARB(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMakeTextureHandleNonResidentARB(long handle) {
/*  44 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  45 */     long function_pointer = caps.glMakeTextureHandleNonResidentARB;
/*  46 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  47 */     nglMakeTextureHandleNonResidentARB(handle, function_pointer);
/*     */   }
/*     */   static native void nglMakeTextureHandleNonResidentARB(long paramLong1, long paramLong2);
/*     */   
/*     */   public static long glGetImageHandleARB(int texture, int level, boolean layered, int layer, int format) {
/*  52 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  53 */     long function_pointer = caps.glGetImageHandleARB;
/*  54 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  55 */     long __result = nglGetImageHandleARB(texture, level, layered, layer, format, function_pointer);
/*  56 */     return __result;
/*     */   }
/*     */   static native long nglGetImageHandleARB(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4, long paramLong);
/*     */   
/*     */   public static void glMakeImageHandleResidentARB(long handle, int access) {
/*  61 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  62 */     long function_pointer = caps.glMakeImageHandleResidentARB;
/*  63 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  64 */     nglMakeImageHandleResidentARB(handle, access, function_pointer);
/*     */   }
/*     */   static native void nglMakeImageHandleResidentARB(long paramLong1, int paramInt, long paramLong2);
/*     */   
/*     */   public static void glMakeImageHandleNonResidentARB(long handle) {
/*  69 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  70 */     long function_pointer = caps.glMakeImageHandleNonResidentARB;
/*  71 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  72 */     nglMakeImageHandleNonResidentARB(handle, function_pointer);
/*     */   }
/*     */   static native void nglMakeImageHandleNonResidentARB(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformHandleui64ARB(int location, long value) {
/*  77 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  78 */     long function_pointer = caps.glUniformHandleui64ARB;
/*  79 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  80 */     nglUniformHandleui64ARB(location, value, function_pointer);
/*     */   }
/*     */   static native void nglUniformHandleui64ARB(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformHandleuARB(int location, LongBuffer value) {
/*  85 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  86 */     long function_pointer = caps.glUniformHandleui64vARB;
/*  87 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  88 */     BufferChecks.checkDirect(value);
/*  89 */     nglUniformHandleui64vARB(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformHandleui64vARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformHandleui64ARB(int program, int location, long value) {
/*  94 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  95 */     long function_pointer = caps.glProgramUniformHandleui64ARB;
/*  96 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  97 */     nglProgramUniformHandleui64ARB(program, location, value, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformHandleui64ARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformHandleuARB(int program, int location, LongBuffer values) {
/* 102 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 103 */     long function_pointer = caps.glProgramUniformHandleui64vARB;
/* 104 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 105 */     BufferChecks.checkDirect(values);
/* 106 */     nglProgramUniformHandleui64vARB(program, location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformHandleui64vARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static boolean glIsTextureHandleResidentARB(long handle) {
/* 111 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 112 */     long function_pointer = caps.glIsTextureHandleResidentARB;
/* 113 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 114 */     boolean __result = nglIsTextureHandleResidentARB(handle, function_pointer);
/* 115 */     return __result;
/*     */   }
/*     */   static native boolean nglIsTextureHandleResidentARB(long paramLong1, long paramLong2);
/*     */   
/*     */   public static boolean glIsImageHandleResidentARB(long handle) {
/* 120 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 121 */     long function_pointer = caps.glIsImageHandleResidentARB;
/* 122 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 123 */     boolean __result = nglIsImageHandleResidentARB(handle, function_pointer);
/* 124 */     return __result;
/*     */   }
/*     */   static native boolean nglIsImageHandleResidentARB(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribL1ui64ARB(int index, long x) {
/* 129 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 130 */     long function_pointer = caps.glVertexAttribL1ui64ARB;
/* 131 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 132 */     nglVertexAttribL1ui64ARB(index, x, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL1ui64ARB(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribL1uARB(int index, LongBuffer v) {
/* 137 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 138 */     long function_pointer = caps.glVertexAttribL1ui64vARB;
/* 139 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 140 */     BufferChecks.checkBuffer(v, 1);
/* 141 */     nglVertexAttribL1ui64vARB(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL1ui64vARB(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glGetVertexAttribLuARB(int index, int pname, LongBuffer params) {
/* 146 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 147 */     long function_pointer = caps.glGetVertexAttribLui64vARB;
/* 148 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 149 */     BufferChecks.checkBuffer(params, 4);
/* 150 */     nglGetVertexAttribLui64vARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetVertexAttribLui64vARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBBindlessTexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */