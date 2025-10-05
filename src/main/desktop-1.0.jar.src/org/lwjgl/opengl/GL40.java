/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GL40
/*     */ {
/*     */   public static final int GL_DRAW_INDIRECT_BUFFER = 36671;
/*     */   public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 36675;
/*     */   public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 34943;
/*     */   public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 36442;
/*     */   public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 36443;
/*     */   public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 36444;
/*     */   public static final int GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 36445;
/*     */   public static final int GL_MAX_VERTEX_STREAMS = 36465;
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
/*     */   public static final int GL_SAMPLE_SHADING = 35894;
/*     */   public static final int GL_MIN_SAMPLE_SHADING_VALUE = 35895;
/*     */   public static final int GL_ACTIVE_SUBROUTINES = 36325;
/*     */   public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 36326;
/*     */   public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 36423;
/*     */   public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 36424;
/*     */   public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 36425;
/*     */   public static final int GL_MAX_SUBROUTINES = 36327;
/*     */   public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 36328;
/*     */   public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 36426;
/*     */   public static final int GL_COMPATIBLE_SUBROUTINES = 36427;
/*     */   public static final int GL_PATCHES = 14;
/*     */   public static final int GL_PATCH_VERTICES = 36466;
/*     */   public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 36467;
/*     */   public static final int GL_PATCH_DEFAULT_OUTER_LEVEL = 36468;
/*     */   public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 36469;
/*     */   public static final int GL_TESS_GEN_MODE = 36470;
/*     */   public static final int GL_TESS_GEN_SPACING = 36471;
/*     */   public static final int GL_TESS_GEN_VERTEX_ORDER = 36472;
/*     */   public static final int GL_TESS_GEN_POINT_MODE = 36473;
/*     */   public static final int GL_ISOLINES = 36474;
/*     */   public static final int GL_FRACTIONAL_ODD = 36475;
/*     */   public static final int GL_FRACTIONAL_EVEN = 36476;
/*     */   public static final int GL_MAX_PATCH_VERTICES = 36477;
/*     */   public static final int GL_MAX_TESS_GEN_LEVEL = 36478;
/*     */   public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 36479;
/*     */   public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 36480;
/*     */   public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 36481;
/*     */   public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 36482;
/*     */   public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 36483;
/*     */   public static final int GL_MAX_TESS_PATCH_COMPONENTS = 36484;
/*     */   public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 36485;
/*     */   public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 36486;
/*     */   public static final int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 36489;
/*     */   public static final int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 36490;
/*     */   public static final int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 34924;
/*     */   public static final int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 34925;
/*     */   public static final int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 36382;
/*     */   public static final int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 36383;
/*     */   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 34032;
/*     */   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 34033;
/*     */   public static final int GL_TESS_EVALUATION_SHADER = 36487;
/*     */   public static final int GL_TESS_CONTROL_SHADER = 36488;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 36873;
/*     */   public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 36874;
/*     */   public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 36875;
/*     */   public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 36876;
/*     */   public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 36877;
/*     */   public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY = 36878;
/*     */   public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 36879;
/*     */   public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 36446;
/*     */   public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 36447;
/*     */   public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_COMPONENTS_ARB = 36767;
/*     */   public static final int GL_TRANSFORM_FEEDBACK = 36386;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_PAUSED = 36387;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_ACTIVE = 36388;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 36387;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 36388;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_BINDING = 36389;
/*     */   public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 36464;
/*     */   
/*     */   public static void glBlendEquationi(int buf, int mode) {
/* 228 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 229 */     long function_pointer = caps.glBlendEquationi;
/* 230 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 231 */     nglBlendEquationi(buf, mode, function_pointer);
/*     */   }
/*     */   static native void nglBlendEquationi(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glBlendEquationSeparatei(int buf, int modeRGB, int modeAlpha) {
/* 236 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 237 */     long function_pointer = caps.glBlendEquationSeparatei;
/* 238 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 239 */     nglBlendEquationSeparatei(buf, modeRGB, modeAlpha, function_pointer);
/*     */   }
/*     */   static native void nglBlendEquationSeparatei(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glBlendFunci(int buf, int src, int dst) {
/* 244 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 245 */     long function_pointer = caps.glBlendFunci;
/* 246 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 247 */     nglBlendFunci(buf, src, dst, function_pointer);
/*     */   }
/*     */   static native void nglBlendFunci(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glBlendFuncSeparatei(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
/* 252 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 253 */     long function_pointer = caps.glBlendFuncSeparatei;
/* 254 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 255 */     nglBlendFuncSeparatei(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
/*     */   }
/*     */   static native void nglBlendFuncSeparatei(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*     */   
/*     */   public static void glDrawArraysIndirect(int mode, ByteBuffer indirect) {
/* 260 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 261 */     long function_pointer = caps.glDrawArraysIndirect;
/* 262 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 263 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 264 */     BufferChecks.checkBuffer(indirect, 16);
/* 265 */     nglDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glDrawArraysIndirect(int mode, long indirect_buffer_offset) {
/* 269 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 270 */     long function_pointer = caps.glDrawArraysIndirect;
/* 271 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 272 */     GLChecks.ensureIndirectBOenabled(caps);
/* 273 */     nglDrawArraysIndirectBO(mode, indirect_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglDrawArraysIndirect(int paramInt, long paramLong1, long paramLong2);
/*     */   static native void nglDrawArraysIndirectBO(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDrawArraysIndirect(int mode, IntBuffer indirect) {
/* 279 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 280 */     long function_pointer = caps.glDrawArraysIndirect;
/* 281 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 282 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 283 */     BufferChecks.checkBuffer(indirect, 4);
/* 284 */     nglDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glDrawElementsIndirect(int mode, int type, ByteBuffer indirect) {
/* 288 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 289 */     long function_pointer = caps.glDrawElementsIndirect;
/* 290 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 291 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 292 */     BufferChecks.checkBuffer(indirect, 20);
/* 293 */     nglDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), function_pointer);
/*     */   }
/*     */   static native void nglDrawElementsIndirect(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   public static void glDrawElementsIndirect(int mode, int type, long indirect_buffer_offset) {
/* 297 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 298 */     long function_pointer = caps.glDrawElementsIndirect;
/* 299 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 300 */     GLChecks.ensureIndirectBOenabled(caps);
/* 301 */     nglDrawElementsIndirectBO(mode, type, indirect_buffer_offset, function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglDrawElementsIndirectBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDrawElementsIndirect(int mode, int type, IntBuffer indirect) {
/* 307 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 308 */     long function_pointer = caps.glDrawElementsIndirect;
/* 309 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 310 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 311 */     BufferChecks.checkBuffer(indirect, 5);
/* 312 */     nglDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glUniform1d(int location, double x) {
/* 316 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 317 */     long function_pointer = caps.glUniform1d;
/* 318 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 319 */     nglUniform1d(location, x, function_pointer);
/*     */   }
/*     */   static native void nglUniform1d(int paramInt, double paramDouble, long paramLong);
/*     */   
/*     */   public static void glUniform2d(int location, double x, double y) {
/* 324 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 325 */     long function_pointer = caps.glUniform2d;
/* 326 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 327 */     nglUniform2d(location, x, y, function_pointer);
/*     */   }
/*     */   static native void nglUniform2d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
/*     */   
/*     */   public static void glUniform3d(int location, double x, double y, double z) {
/* 332 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 333 */     long function_pointer = caps.glUniform3d;
/* 334 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 335 */     nglUniform3d(location, x, y, z, function_pointer);
/*     */   }
/*     */   static native void nglUniform3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*     */   
/*     */   public static void glUniform4d(int location, double x, double y, double z, double w) {
/* 340 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 341 */     long function_pointer = caps.glUniform4d;
/* 342 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 343 */     nglUniform4d(location, x, y, z, w, function_pointer);
/*     */   }
/*     */   static native void nglUniform4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*     */   
/*     */   public static void glUniform1(int location, DoubleBuffer value) {
/* 348 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 349 */     long function_pointer = caps.glUniform1dv;
/* 350 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 351 */     BufferChecks.checkDirect(value);
/* 352 */     nglUniform1dv(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniform1dv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniform2(int location, DoubleBuffer value) {
/* 357 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 358 */     long function_pointer = caps.glUniform2dv;
/* 359 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 360 */     BufferChecks.checkDirect(value);
/* 361 */     nglUniform2dv(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniform2dv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniform3(int location, DoubleBuffer value) {
/* 366 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 367 */     long function_pointer = caps.glUniform3dv;
/* 368 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 369 */     BufferChecks.checkDirect(value);
/* 370 */     nglUniform3dv(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniform3dv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniform4(int location, DoubleBuffer value) {
/* 375 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 376 */     long function_pointer = caps.glUniform4dv;
/* 377 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 378 */     BufferChecks.checkDirect(value);
/* 379 */     nglUniform4dv(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniform4dv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix2(int location, boolean transpose, DoubleBuffer value) {
/* 384 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 385 */     long function_pointer = caps.glUniformMatrix2dv;
/* 386 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 387 */     BufferChecks.checkDirect(value);
/* 388 */     nglUniformMatrix2dv(location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix2dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix3(int location, boolean transpose, DoubleBuffer value) {
/* 393 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 394 */     long function_pointer = caps.glUniformMatrix3dv;
/* 395 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 396 */     BufferChecks.checkDirect(value);
/* 397 */     nglUniformMatrix3dv(location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix3dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix4(int location, boolean transpose, DoubleBuffer value) {
/* 402 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 403 */     long function_pointer = caps.glUniformMatrix4dv;
/* 404 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 405 */     BufferChecks.checkDirect(value);
/* 406 */     nglUniformMatrix4dv(location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix4dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix2x3(int location, boolean transpose, DoubleBuffer value) {
/* 411 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 412 */     long function_pointer = caps.glUniformMatrix2x3dv;
/* 413 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 414 */     BufferChecks.checkDirect(value);
/* 415 */     nglUniformMatrix2x3dv(location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix2x3dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix2x4(int location, boolean transpose, DoubleBuffer value) {
/* 420 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 421 */     long function_pointer = caps.glUniformMatrix2x4dv;
/* 422 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 423 */     BufferChecks.checkDirect(value);
/* 424 */     nglUniformMatrix2x4dv(location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix2x4dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix3x2(int location, boolean transpose, DoubleBuffer value) {
/* 429 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 430 */     long function_pointer = caps.glUniformMatrix3x2dv;
/* 431 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 432 */     BufferChecks.checkDirect(value);
/* 433 */     nglUniformMatrix3x2dv(location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix3x2dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix3x4(int location, boolean transpose, DoubleBuffer value) {
/* 438 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 439 */     long function_pointer = caps.glUniformMatrix3x4dv;
/* 440 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 441 */     BufferChecks.checkDirect(value);
/* 442 */     nglUniformMatrix3x4dv(location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix3x4dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix4x2(int location, boolean transpose, DoubleBuffer value) {
/* 447 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 448 */     long function_pointer = caps.glUniformMatrix4x2dv;
/* 449 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 450 */     BufferChecks.checkDirect(value);
/* 451 */     nglUniformMatrix4x2dv(location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix4x2dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glUniformMatrix4x3(int location, boolean transpose, DoubleBuffer value) {
/* 456 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 457 */     long function_pointer = caps.glUniformMatrix4x3dv;
/* 458 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 459 */     BufferChecks.checkDirect(value);
/* 460 */     nglUniformMatrix4x3dv(location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglUniformMatrix4x3dv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glGetUniform(int program, int location, DoubleBuffer params) {
/* 465 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 466 */     long function_pointer = caps.glGetUniformdv;
/* 467 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 468 */     BufferChecks.checkDirect(params);
/* 469 */     nglGetUniformdv(program, location, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   static native void nglGetUniformdv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMinSampleShading(float value) {
/* 474 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 475 */     long function_pointer = caps.glMinSampleShading;
/* 476 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 477 */     nglMinSampleShading(value, function_pointer);
/*     */   }
/*     */   static native void nglMinSampleShading(float paramFloat, long paramLong);
/*     */   
/*     */   public static int glGetSubroutineUniformLocation(int program, int shadertype, ByteBuffer name) {
/* 482 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 483 */     long function_pointer = caps.glGetSubroutineUniformLocation;
/* 484 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 485 */     BufferChecks.checkDirect(name);
/* 486 */     BufferChecks.checkNullTerminated(name);
/* 487 */     int __result = nglGetSubroutineUniformLocation(program, shadertype, MemoryUtil.getAddress(name), function_pointer);
/* 488 */     return __result;
/*     */   }
/*     */   
/*     */   static native int nglGetSubroutineUniformLocation(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetSubroutineUniformLocation(int program, int shadertype, CharSequence name) {
/* 494 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 495 */     long function_pointer = caps.glGetSubroutineUniformLocation;
/* 496 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 497 */     int __result = nglGetSubroutineUniformLocation(program, shadertype, APIUtil.getBufferNT(caps, name), function_pointer);
/* 498 */     return __result;
/*     */   }
/*     */   
/*     */   public static int glGetSubroutineIndex(int program, int shadertype, ByteBuffer name) {
/* 502 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 503 */     long function_pointer = caps.glGetSubroutineIndex;
/* 504 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 505 */     BufferChecks.checkDirect(name);
/* 506 */     BufferChecks.checkNullTerminated(name);
/* 507 */     int __result = nglGetSubroutineIndex(program, shadertype, MemoryUtil.getAddress(name), function_pointer);
/* 508 */     return __result;
/*     */   }
/*     */   
/*     */   static native int nglGetSubroutineIndex(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetSubroutineIndex(int program, int shadertype, CharSequence name) {
/* 514 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 515 */     long function_pointer = caps.glGetSubroutineIndex;
/* 516 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 517 */     int __result = nglGetSubroutineIndex(program, shadertype, APIUtil.getBufferNT(caps, name), function_pointer);
/* 518 */     return __result;
/*     */   }
/*     */   
/*     */   public static void glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname, IntBuffer values) {
/* 522 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 523 */     long function_pointer = caps.glGetActiveSubroutineUniformiv;
/* 524 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 525 */     BufferChecks.checkBuffer(values, 1);
/* 526 */     nglGetActiveSubroutineUniformiv(program, shadertype, index, pname, MemoryUtil.getAddress(values), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetActiveSubroutineUniformiv(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname) {
/* 537 */     return glGetActiveSubroutineUniformi(program, shadertype, index, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetActiveSubroutineUniformi(int program, int shadertype, int index, int pname) {
/* 542 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 543 */     long function_pointer = caps.glGetActiveSubroutineUniformiv;
/* 544 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 545 */     IntBuffer values = APIUtil.getBufferInt(caps);
/* 546 */     nglGetActiveSubroutineUniformiv(program, shadertype, index, pname, MemoryUtil.getAddress(values), function_pointer);
/* 547 */     return values.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetActiveSubroutineUniformName(int program, int shadertype, int index, IntBuffer length, ByteBuffer name) {
/* 551 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 552 */     long function_pointer = caps.glGetActiveSubroutineUniformName;
/* 553 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 554 */     if (length != null)
/* 555 */       BufferChecks.checkBuffer(length, 1); 
/* 556 */     BufferChecks.checkDirect(name);
/* 557 */     nglGetActiveSubroutineUniformName(program, shadertype, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(name), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetActiveSubroutineUniformName(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   public static String glGetActiveSubroutineUniformName(int program, int shadertype, int index, int bufsize) {
/* 563 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 564 */     long function_pointer = caps.glGetActiveSubroutineUniformName;
/* 565 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 566 */     IntBuffer name_length = APIUtil.getLengths(caps);
/* 567 */     ByteBuffer name = APIUtil.getBufferByte(caps, bufsize);
/* 568 */     nglGetActiveSubroutineUniformName(program, shadertype, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
/* 569 */     name.limit(name_length.get(0));
/* 570 */     return APIUtil.getString(caps, name);
/*     */   }
/*     */   
/*     */   public static void glGetActiveSubroutineName(int program, int shadertype, int index, IntBuffer length, ByteBuffer name) {
/* 574 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 575 */     long function_pointer = caps.glGetActiveSubroutineName;
/* 576 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 577 */     if (length != null)
/* 578 */       BufferChecks.checkBuffer(length, 1); 
/* 579 */     BufferChecks.checkDirect(name);
/* 580 */     nglGetActiveSubroutineName(program, shadertype, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(name), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetActiveSubroutineName(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   public static String glGetActiveSubroutineName(int program, int shadertype, int index, int bufsize) {
/* 586 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 587 */     long function_pointer = caps.glGetActiveSubroutineName;
/* 588 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 589 */     IntBuffer name_length = APIUtil.getLengths(caps);
/* 590 */     ByteBuffer name = APIUtil.getBufferByte(caps, bufsize);
/* 591 */     nglGetActiveSubroutineName(program, shadertype, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
/* 592 */     name.limit(name_length.get(0));
/* 593 */     return APIUtil.getString(caps, name);
/*     */   }
/*     */   
/*     */   public static void glUniformSubroutinesu(int shadertype, IntBuffer indices) {
/* 597 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 598 */     long function_pointer = caps.glUniformSubroutinesuiv;
/* 599 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 600 */     BufferChecks.checkDirect(indices);
/* 601 */     nglUniformSubroutinesuiv(shadertype, indices.remaining(), MemoryUtil.getAddress(indices), function_pointer);
/*     */   }
/*     */   static native void nglUniformSubroutinesuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glGetUniformSubroutineu(int shadertype, int location, IntBuffer params) {
/* 606 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 607 */     long function_pointer = caps.glGetUniformSubroutineuiv;
/* 608 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 609 */     BufferChecks.checkBuffer(params, 1);
/* 610 */     nglGetUniformSubroutineuiv(shadertype, location, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetUniformSubroutineuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int glGetUniformSubroutineu(int shadertype, int location) {
/* 621 */     return glGetUniformSubroutineui(shadertype, location);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetUniformSubroutineui(int shadertype, int location) {
/* 626 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 627 */     long function_pointer = caps.glGetUniformSubroutineuiv;
/* 628 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 629 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 630 */     nglGetUniformSubroutineuiv(shadertype, location, MemoryUtil.getAddress(params), function_pointer);
/* 631 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetProgramStage(int program, int shadertype, int pname, IntBuffer values) {
/* 635 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 636 */     long function_pointer = caps.glGetProgramStageiv;
/* 637 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 638 */     BufferChecks.checkBuffer(values, 1);
/* 639 */     nglGetProgramStageiv(program, shadertype, pname, MemoryUtil.getAddress(values), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetProgramStageiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int glGetProgramStage(int program, int shadertype, int pname) {
/* 650 */     return glGetProgramStagei(program, shadertype, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetProgramStagei(int program, int shadertype, int pname) {
/* 655 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 656 */     long function_pointer = caps.glGetProgramStageiv;
/* 657 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 658 */     IntBuffer values = APIUtil.getBufferInt(caps);
/* 659 */     nglGetProgramStageiv(program, shadertype, pname, MemoryUtil.getAddress(values), function_pointer);
/* 660 */     return values.get(0);
/*     */   }
/*     */   
/*     */   public static void glPatchParameteri(int pname, int value) {
/* 664 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 665 */     long function_pointer = caps.glPatchParameteri;
/* 666 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 667 */     nglPatchParameteri(pname, value, function_pointer);
/*     */   }
/*     */   static native void nglPatchParameteri(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glPatchParameter(int pname, FloatBuffer values) {
/* 672 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 673 */     long function_pointer = caps.glPatchParameterfv;
/* 674 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 675 */     BufferChecks.checkBuffer(values, 4);
/* 676 */     nglPatchParameterfv(pname, MemoryUtil.getAddress(values), function_pointer);
/*     */   }
/*     */   static native void nglPatchParameterfv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindTransformFeedback(int target, int id) {
/* 681 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 682 */     long function_pointer = caps.glBindTransformFeedback;
/* 683 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 684 */     nglBindTransformFeedback(target, id, function_pointer);
/*     */   }
/*     */   static native void nglBindTransformFeedback(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glDeleteTransformFeedbacks(IntBuffer ids) {
/* 689 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 690 */     long function_pointer = caps.glDeleteTransformFeedbacks;
/* 691 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 692 */     BufferChecks.checkDirect(ids);
/* 693 */     nglDeleteTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglDeleteTransformFeedbacks(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDeleteTransformFeedbacks(int id) {
/* 699 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 700 */     long function_pointer = caps.glDeleteTransformFeedbacks;
/* 701 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 702 */     nglDeleteTransformFeedbacks(1, APIUtil.getInt(caps, id), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glGenTransformFeedbacks(IntBuffer ids) {
/* 706 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 707 */     long function_pointer = caps.glGenTransformFeedbacks;
/* 708 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 709 */     BufferChecks.checkDirect(ids);
/* 710 */     nglGenTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGenTransformFeedbacks(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGenTransformFeedbacks() {
/* 716 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 717 */     long function_pointer = caps.glGenTransformFeedbacks;
/* 718 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 719 */     IntBuffer ids = APIUtil.getBufferInt(caps);
/* 720 */     nglGenTransformFeedbacks(1, MemoryUtil.getAddress(ids), function_pointer);
/* 721 */     return ids.get(0);
/*     */   }
/*     */   
/*     */   public static boolean glIsTransformFeedback(int id) {
/* 725 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 726 */     long function_pointer = caps.glIsTransformFeedback;
/* 727 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 728 */     boolean __result = nglIsTransformFeedback(id, function_pointer);
/* 729 */     return __result;
/*     */   }
/*     */   static native boolean nglIsTransformFeedback(int paramInt, long paramLong);
/*     */   
/*     */   public static void glPauseTransformFeedback() {
/* 734 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 735 */     long function_pointer = caps.glPauseTransformFeedback;
/* 736 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 737 */     nglPauseTransformFeedback(function_pointer);
/*     */   }
/*     */   static native void nglPauseTransformFeedback(long paramLong);
/*     */   
/*     */   public static void glResumeTransformFeedback() {
/* 742 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 743 */     long function_pointer = caps.glResumeTransformFeedback;
/* 744 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 745 */     nglResumeTransformFeedback(function_pointer);
/*     */   }
/*     */   static native void nglResumeTransformFeedback(long paramLong);
/*     */   
/*     */   public static void glDrawTransformFeedback(int mode, int id) {
/* 750 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 751 */     long function_pointer = caps.glDrawTransformFeedback;
/* 752 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 753 */     nglDrawTransformFeedback(mode, id, function_pointer);
/*     */   }
/*     */   static native void nglDrawTransformFeedback(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glDrawTransformFeedbackStream(int mode, int id, int stream) {
/* 758 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 759 */     long function_pointer = caps.glDrawTransformFeedbackStream;
/* 760 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 761 */     nglDrawTransformFeedbackStream(mode, id, stream, function_pointer);
/*     */   }
/*     */   static native void nglDrawTransformFeedbackStream(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glBeginQueryIndexed(int target, int index, int id) {
/* 766 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 767 */     long function_pointer = caps.glBeginQueryIndexed;
/* 768 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 769 */     nglBeginQueryIndexed(target, index, id, function_pointer);
/*     */   }
/*     */   static native void nglBeginQueryIndexed(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glEndQueryIndexed(int target, int index) {
/* 774 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 775 */     long function_pointer = caps.glEndQueryIndexed;
/* 776 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 777 */     nglEndQueryIndexed(target, index, function_pointer);
/*     */   }
/*     */   static native void nglEndQueryIndexed(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glGetQueryIndexed(int target, int index, int pname, IntBuffer params) {
/* 782 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 783 */     long function_pointer = caps.glGetQueryIndexediv;
/* 784 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 785 */     BufferChecks.checkBuffer(params, 1);
/* 786 */     nglGetQueryIndexediv(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetQueryIndexediv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int glGetQueryIndexed(int target, int index, int pname) {
/* 797 */     return glGetQueryIndexedi(target, index, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetQueryIndexedi(int target, int index, int pname) {
/* 802 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 803 */     long function_pointer = caps.glGetQueryIndexediv;
/* 804 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 805 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 806 */     nglGetQueryIndexediv(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
/* 807 */     return params.get(0);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL40.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */