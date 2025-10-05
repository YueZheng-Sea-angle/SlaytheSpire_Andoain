/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.DoubleBuffer;
/*     */ import org.lwjgl.BufferChecks;
/*     */ import org.lwjgl.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ARBGpuShaderFp64
/*     */ {
/*     */   public static final int GL_DOUBLE_VEC2 = 36860;
/*     */   public static final int GL_DOUBLE_VEC3 = 36861;
/*     */   public static final int GL_DOUBLE_VEC4 = 36862;
/*     */   public static final int GL_DOUBLE_MAT2 = 36678;
/*     */   public static final int GL_DOUBLE_MAT3 = 36679;
/*     */   public static final int GL_DOUBLE_MAT4 = 36680;
/*     */   public static final int GL_DOUBLE_MAT2x3 = 36681;
/*     */   public static final int GL_DOUBLE_MAT2x4 = 36682;
/*     */   public static final int GL_DOUBLE_MAT3x2 = 36683;
/*     */   public static final int GL_DOUBLE_MAT3x4 = 36684;
/*     */   public static final int GL_DOUBLE_MAT4x2 = 36685;
/*     */   public static final int GL_DOUBLE_MAT4x3 = 36686;
/*     */   
/*     */   public static void glUniform1d(int location, double x) {
/*  30 */     GL40.glUniform1d(location, x);
/*     */   }
/*     */   
/*     */   public static void glUniform2d(int location, double x, double y) {
/*  34 */     GL40.glUniform2d(location, x, y);
/*     */   }
/*     */   
/*     */   public static void glUniform3d(int location, double x, double y, double z) {
/*  38 */     GL40.glUniform3d(location, x, y, z);
/*     */   }
/*     */   
/*     */   public static void glUniform4d(int location, double x, double y, double z, double w) {
/*  42 */     GL40.glUniform4d(location, x, y, z, w);
/*     */   }
/*     */   
/*     */   public static void glUniform1(int location, DoubleBuffer value) {
/*  46 */     GL40.glUniform1(location, value);
/*     */   }
/*     */   
/*     */   public static void glUniform2(int location, DoubleBuffer value) {
/*  50 */     GL40.glUniform2(location, value);
/*     */   }
/*     */   
/*     */   public static void glUniform3(int location, DoubleBuffer value) {
/*  54 */     GL40.glUniform3(location, value);
/*     */   }
/*     */   
/*     */   public static void glUniform4(int location, DoubleBuffer value) {
/*  58 */     GL40.glUniform4(location, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix2(int location, boolean transpose, DoubleBuffer value) {
/*  62 */     GL40.glUniformMatrix2(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix3(int location, boolean transpose, DoubleBuffer value) {
/*  66 */     GL40.glUniformMatrix3(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix4(int location, boolean transpose, DoubleBuffer value) {
/*  70 */     GL40.glUniformMatrix4(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix2x3(int location, boolean transpose, DoubleBuffer value) {
/*  74 */     GL40.glUniformMatrix2x3(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix2x4(int location, boolean transpose, DoubleBuffer value) {
/*  78 */     GL40.glUniformMatrix2x4(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix3x2(int location, boolean transpose, DoubleBuffer value) {
/*  82 */     GL40.glUniformMatrix3x2(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix3x4(int location, boolean transpose, DoubleBuffer value) {
/*  86 */     GL40.glUniformMatrix3x4(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix4x2(int location, boolean transpose, DoubleBuffer value) {
/*  90 */     GL40.glUniformMatrix4x2(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glUniformMatrix4x3(int location, boolean transpose, DoubleBuffer value) {
/*  94 */     GL40.glUniformMatrix4x3(location, transpose, value);
/*     */   }
/*     */   
/*     */   public static void glGetUniform(int program, int location, DoubleBuffer params) {
/*  98 */     GL40.glGetUniform(program, location, params);
/*     */   }
/*     */   static native void nglProgramUniform1dEXT(int paramInt1, int paramInt2, double paramDouble, long paramLong);
/*     */   public static void glProgramUniform1dEXT(int program, int location, double x) {
/* 102 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 103 */     long function_pointer = caps.glProgramUniform1dEXT;
/* 104 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 105 */     nglProgramUniform1dEXT(program, location, x, function_pointer);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glProgramUniform2dEXT(int program, int location, double x, double y) {
/* 110 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 111 */     long function_pointer = caps.glProgramUniform2dEXT;
/* 112 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 113 */     nglProgramUniform2dEXT(program, location, x, y, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2dEXT(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, long paramLong);
/*     */   
/*     */   public static void glProgramUniform3dEXT(int program, int location, double x, double y, double z) {
/* 118 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 119 */     long function_pointer = caps.glProgramUniform3dEXT;
/* 120 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 121 */     nglProgramUniform3dEXT(program, location, x, y, z, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3dEXT(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*     */   
/*     */   public static void glProgramUniform4dEXT(int program, int location, double x, double y, double z, double w) {
/* 126 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 127 */     long function_pointer = caps.glProgramUniform4dEXT;
/* 128 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 129 */     nglProgramUniform4dEXT(program, location, x, y, z, w, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4dEXT(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*     */   
/*     */   public static void glProgramUniform1EXT(int program, int location, DoubleBuffer value) {
/* 134 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 135 */     long function_pointer = caps.glProgramUniform1dvEXT;
/* 136 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 137 */     BufferChecks.checkDirect(value);
/* 138 */     nglProgramUniform1dvEXT(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1dvEXT(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform2EXT(int program, int location, DoubleBuffer value) {
/* 143 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 144 */     long function_pointer = caps.glProgramUniform2dvEXT;
/* 145 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 146 */     BufferChecks.checkDirect(value);
/* 147 */     nglProgramUniform2dvEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2dvEXT(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform3EXT(int program, int location, DoubleBuffer value) {
/* 152 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 153 */     long function_pointer = caps.glProgramUniform3dvEXT;
/* 154 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 155 */     BufferChecks.checkDirect(value);
/* 156 */     nglProgramUniform3dvEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3dvEXT(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform4EXT(int program, int location, DoubleBuffer value) {
/* 161 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 162 */     long function_pointer = caps.glProgramUniform4dvEXT;
/* 163 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 164 */     BufferChecks.checkDirect(value);
/* 165 */     nglProgramUniform4dvEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4dvEXT(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 170 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 171 */     long function_pointer = caps.glProgramUniformMatrix2dvEXT;
/* 172 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 173 */     BufferChecks.checkDirect(value);
/* 174 */     nglProgramUniformMatrix2dvEXT(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 179 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 180 */     long function_pointer = caps.glProgramUniformMatrix3dvEXT;
/* 181 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 182 */     BufferChecks.checkDirect(value);
/* 183 */     nglProgramUniformMatrix3dvEXT(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 188 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 189 */     long function_pointer = caps.glProgramUniformMatrix4dvEXT;
/* 190 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 191 */     BufferChecks.checkDirect(value);
/* 192 */     nglProgramUniformMatrix4dvEXT(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2x3EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 197 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 198 */     long function_pointer = caps.glProgramUniformMatrix2x3dvEXT;
/* 199 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 200 */     BufferChecks.checkDirect(value);
/* 201 */     nglProgramUniformMatrix2x3dvEXT(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2x3dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2x4EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 206 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 207 */     long function_pointer = caps.glProgramUniformMatrix2x4dvEXT;
/* 208 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 209 */     BufferChecks.checkDirect(value);
/* 210 */     nglProgramUniformMatrix2x4dvEXT(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2x4dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3x2EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 215 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 216 */     long function_pointer = caps.glProgramUniformMatrix3x2dvEXT;
/* 217 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 218 */     BufferChecks.checkDirect(value);
/* 219 */     nglProgramUniformMatrix3x2dvEXT(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3x2dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3x4EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 224 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 225 */     long function_pointer = caps.glProgramUniformMatrix3x4dvEXT;
/* 226 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 227 */     BufferChecks.checkDirect(value);
/* 228 */     nglProgramUniformMatrix3x4dvEXT(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3x4dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4x2EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 233 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 234 */     long function_pointer = caps.glProgramUniformMatrix4x2dvEXT;
/* 235 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 236 */     BufferChecks.checkDirect(value);
/* 237 */     nglProgramUniformMatrix4x2dvEXT(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4x2dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4x3EXT(int program, int location, boolean transpose, DoubleBuffer value) {
/* 242 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 243 */     long function_pointer = caps.glProgramUniformMatrix4x3dvEXT;
/* 244 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 245 */     BufferChecks.checkDirect(value);
/* 246 */     nglProgramUniformMatrix4x3dvEXT(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglProgramUniformMatrix4x3dvEXT(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBGpuShaderFp64.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */