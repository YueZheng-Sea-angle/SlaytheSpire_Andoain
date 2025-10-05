/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
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
/*     */ public final class GL33
/*     */ {
/*     */   public static final int GL_SRC1_COLOR = 35065;
/*     */   public static final int GL_ONE_MINUS_SRC1_COLOR = 35066;
/*     */   public static final int GL_ONE_MINUS_SRC1_ALPHA = 35067;
/*     */   public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 35068;
/*     */   public static final int GL_ANY_SAMPLES_PASSED = 35887;
/*     */   public static final int GL_SAMPLER_BINDING = 35097;
/*     */   public static final int GL_RGB10_A2UI = 36975;
/*     */   public static final int GL_TEXTURE_SWIZZLE_R = 36418;
/*     */   public static final int GL_TEXTURE_SWIZZLE_G = 36419;
/*     */   public static final int GL_TEXTURE_SWIZZLE_B = 36420;
/*     */   public static final int GL_TEXTURE_SWIZZLE_A = 36421;
/*     */   public static final int GL_TEXTURE_SWIZZLE_RGBA = 36422;
/*     */   public static final int GL_TIME_ELAPSED = 35007;
/*     */   public static final int GL_TIMESTAMP = 36392;
/*     */   public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 35070;
/*     */   public static final int GL_INT_2_10_10_10_REV = 36255;
/*     */   
/*     */   public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, ByteBuffer name) {
/*  90 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  91 */     long function_pointer = caps.glBindFragDataLocationIndexed;
/*  92 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  93 */     BufferChecks.checkDirect(name);
/*  94 */     BufferChecks.checkNullTerminated(name);
/*  95 */     nglBindFragDataLocationIndexed(program, colorNumber, index, MemoryUtil.getAddress(name), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglBindFragDataLocationIndexed(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, CharSequence name) {
/* 101 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 102 */     long function_pointer = caps.glBindFragDataLocationIndexed;
/* 103 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 104 */     nglBindFragDataLocationIndexed(program, colorNumber, index, APIUtil.getBufferNT(caps, name), function_pointer);
/*     */   }
/*     */   
/*     */   public static int glGetFragDataIndex(int program, ByteBuffer name) {
/* 108 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 109 */     long function_pointer = caps.glGetFragDataIndex;
/* 110 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 111 */     BufferChecks.checkDirect(name);
/* 112 */     BufferChecks.checkNullTerminated(name);
/* 113 */     int __result = nglGetFragDataIndex(program, MemoryUtil.getAddress(name), function_pointer);
/* 114 */     return __result;
/*     */   }
/*     */   
/*     */   static native int nglGetFragDataIndex(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetFragDataIndex(int program, CharSequence name) {
/* 120 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 121 */     long function_pointer = caps.glGetFragDataIndex;
/* 122 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 123 */     int __result = nglGetFragDataIndex(program, APIUtil.getBufferNT(caps, name), function_pointer);
/* 124 */     return __result;
/*     */   }
/*     */   
/*     */   public static void glGenSamplers(IntBuffer samplers) {
/* 128 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 129 */     long function_pointer = caps.glGenSamplers;
/* 130 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 131 */     BufferChecks.checkDirect(samplers);
/* 132 */     nglGenSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGenSamplers(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGenSamplers() {
/* 138 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 139 */     long function_pointer = caps.glGenSamplers;
/* 140 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 141 */     IntBuffer samplers = APIUtil.getBufferInt(caps);
/* 142 */     nglGenSamplers(1, MemoryUtil.getAddress(samplers), function_pointer);
/* 143 */     return samplers.get(0);
/*     */   }
/*     */   
/*     */   public static void glDeleteSamplers(IntBuffer samplers) {
/* 147 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 148 */     long function_pointer = caps.glDeleteSamplers;
/* 149 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 150 */     BufferChecks.checkDirect(samplers);
/* 151 */     nglDeleteSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglDeleteSamplers(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDeleteSamplers(int sampler) {
/* 157 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 158 */     long function_pointer = caps.glDeleteSamplers;
/* 159 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 160 */     nglDeleteSamplers(1, APIUtil.getInt(caps, sampler), function_pointer);
/*     */   }
/*     */   static native boolean nglIsSampler(int paramInt, long paramLong);
/*     */   public static boolean glIsSampler(int sampler) {
/* 164 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 165 */     long function_pointer = caps.glIsSampler;
/* 166 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 167 */     boolean __result = nglIsSampler(sampler, function_pointer);
/* 168 */     return __result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBindSampler(int unit, int sampler) {
/* 173 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 174 */     long function_pointer = caps.glBindSampler;
/* 175 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 176 */     nglBindSampler(unit, sampler, function_pointer);
/*     */   }
/*     */   static native void nglBindSampler(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glSamplerParameteri(int sampler, int pname, int param) {
/* 181 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 182 */     long function_pointer = caps.glSamplerParameteri;
/* 183 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 184 */     nglSamplerParameteri(sampler, pname, param, function_pointer);
/*     */   }
/*     */   static native void nglSamplerParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glSamplerParameterf(int sampler, int pname, float param) {
/* 189 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 190 */     long function_pointer = caps.glSamplerParameterf;
/* 191 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 192 */     nglSamplerParameterf(sampler, pname, param, function_pointer);
/*     */   }
/*     */   static native void nglSamplerParameterf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*     */   
/*     */   public static void glSamplerParameter(int sampler, int pname, IntBuffer params) {
/* 197 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 198 */     long function_pointer = caps.glSamplerParameteriv;
/* 199 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 200 */     BufferChecks.checkBuffer(params, 4);
/* 201 */     nglSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   static native void nglSamplerParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glSamplerParameter(int sampler, int pname, FloatBuffer params) {
/* 206 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 207 */     long function_pointer = caps.glSamplerParameterfv;
/* 208 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 209 */     BufferChecks.checkBuffer(params, 4);
/* 210 */     nglSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   static native void nglSamplerParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glSamplerParameterI(int sampler, int pname, IntBuffer params) {
/* 215 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 216 */     long function_pointer = caps.glSamplerParameterIiv;
/* 217 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 218 */     BufferChecks.checkBuffer(params, 4);
/* 219 */     nglSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   static native void nglSamplerParameterIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glSamplerParameterIu(int sampler, int pname, IntBuffer params) {
/* 224 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 225 */     long function_pointer = caps.glSamplerParameterIuiv;
/* 226 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 227 */     BufferChecks.checkBuffer(params, 4);
/* 228 */     nglSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   static native void nglSamplerParameterIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glGetSamplerParameter(int sampler, int pname, IntBuffer params) {
/* 233 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 234 */     long function_pointer = caps.glGetSamplerParameteriv;
/* 235 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 236 */     BufferChecks.checkBuffer(params, 4);
/* 237 */     nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetSamplerParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetSamplerParameteri(int sampler, int pname) {
/* 243 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 244 */     long function_pointer = caps.glGetSamplerParameteriv;
/* 245 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 246 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 247 */     nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/* 248 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetSamplerParameter(int sampler, int pname, FloatBuffer params) {
/* 252 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 253 */     long function_pointer = caps.glGetSamplerParameterfv;
/* 254 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 255 */     BufferChecks.checkBuffer(params, 4);
/* 256 */     nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetSamplerParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static float glGetSamplerParameterf(int sampler, int pname) {
/* 262 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 263 */     long function_pointer = caps.glGetSamplerParameterfv;
/* 264 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 265 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 266 */     nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/* 267 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetSamplerParameterI(int sampler, int pname, IntBuffer params) {
/* 271 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 272 */     long function_pointer = caps.glGetSamplerParameterIiv;
/* 273 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 274 */     BufferChecks.checkBuffer(params, 4);
/* 275 */     nglGetSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetSamplerParameterIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetSamplerParameterIi(int sampler, int pname) {
/* 281 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 282 */     long function_pointer = caps.glGetSamplerParameterIiv;
/* 283 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 284 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 285 */     nglGetSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/* 286 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetSamplerParameterIu(int sampler, int pname, IntBuffer params) {
/* 290 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 291 */     long function_pointer = caps.glGetSamplerParameterIuiv;
/* 292 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 293 */     BufferChecks.checkBuffer(params, 4);
/* 294 */     nglGetSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetSamplerParameterIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetSamplerParameterIui(int sampler, int pname) {
/* 300 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 301 */     long function_pointer = caps.glGetSamplerParameterIuiv;
/* 302 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 303 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 304 */     nglGetSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
/* 305 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glQueryCounter(int id, int target) {
/* 309 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 310 */     long function_pointer = caps.glQueryCounter;
/* 311 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 312 */     nglQueryCounter(id, target, function_pointer);
/*     */   }
/*     */   static native void nglQueryCounter(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glGetQueryObject(int id, int pname, LongBuffer params) {
/* 317 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 318 */     long function_pointer = caps.glGetQueryObjecti64v;
/* 319 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 320 */     BufferChecks.checkBuffer(params, 1);
/* 321 */     nglGetQueryObjecti64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetQueryObjecti64v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long glGetQueryObject(int id, int pname) {
/* 332 */     return glGetQueryObjecti64(id, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long glGetQueryObjecti64(int id, int pname) {
/* 337 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 338 */     long function_pointer = caps.glGetQueryObjecti64v;
/* 339 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 340 */     LongBuffer params = APIUtil.getBufferLong(caps);
/* 341 */     nglGetQueryObjecti64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
/* 342 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetQueryObjectu(int id, int pname, LongBuffer params) {
/* 346 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 347 */     long function_pointer = caps.glGetQueryObjectui64v;
/* 348 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 349 */     BufferChecks.checkBuffer(params, 1);
/* 350 */     nglGetQueryObjectui64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetQueryObjectui64v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long glGetQueryObjectu(int id, int pname) {
/* 361 */     return glGetQueryObjectui64(id, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long glGetQueryObjectui64(int id, int pname) {
/* 366 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 367 */     long function_pointer = caps.glGetQueryObjectui64v;
/* 368 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 369 */     LongBuffer params = APIUtil.getBufferLong(caps);
/* 370 */     nglGetQueryObjectui64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
/* 371 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glVertexAttribDivisor(int index, int divisor) {
/* 375 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 376 */     long function_pointer = caps.glVertexAttribDivisor;
/* 377 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 378 */     nglVertexAttribDivisor(index, divisor, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribDivisor(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glVertexP2ui(int type, int value) {
/* 383 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 384 */     long function_pointer = caps.glVertexP2ui;
/* 385 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 386 */     nglVertexP2ui(type, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexP2ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glVertexP3ui(int type, int value) {
/* 391 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 392 */     long function_pointer = caps.glVertexP3ui;
/* 393 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 394 */     nglVertexP3ui(type, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexP3ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glVertexP4ui(int type, int value) {
/* 399 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 400 */     long function_pointer = caps.glVertexP4ui;
/* 401 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 402 */     nglVertexP4ui(type, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexP4ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glVertexP2u(int type, IntBuffer value) {
/* 407 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 408 */     long function_pointer = caps.glVertexP2uiv;
/* 409 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 410 */     BufferChecks.checkBuffer(value, 2);
/* 411 */     nglVertexP2uiv(type, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglVertexP2uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexP3u(int type, IntBuffer value) {
/* 416 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 417 */     long function_pointer = caps.glVertexP3uiv;
/* 418 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 419 */     BufferChecks.checkBuffer(value, 3);
/* 420 */     nglVertexP3uiv(type, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglVertexP3uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexP4u(int type, IntBuffer value) {
/* 425 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 426 */     long function_pointer = caps.glVertexP4uiv;
/* 427 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 428 */     BufferChecks.checkBuffer(value, 4);
/* 429 */     nglVertexP4uiv(type, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglVertexP4uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glTexCoordP1ui(int type, int coords) {
/* 434 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 435 */     long function_pointer = caps.glTexCoordP1ui;
/* 436 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 437 */     nglTexCoordP1ui(type, coords, function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP1ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glTexCoordP2ui(int type, int coords) {
/* 442 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 443 */     long function_pointer = caps.glTexCoordP2ui;
/* 444 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 445 */     nglTexCoordP2ui(type, coords, function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP2ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glTexCoordP3ui(int type, int coords) {
/* 450 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 451 */     long function_pointer = caps.glTexCoordP3ui;
/* 452 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 453 */     nglTexCoordP3ui(type, coords, function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP3ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glTexCoordP4ui(int type, int coords) {
/* 458 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 459 */     long function_pointer = caps.glTexCoordP4ui;
/* 460 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 461 */     nglTexCoordP4ui(type, coords, function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP4ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glTexCoordP1u(int type, IntBuffer coords) {
/* 466 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 467 */     long function_pointer = caps.glTexCoordP1uiv;
/* 468 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 469 */     BufferChecks.checkBuffer(coords, 1);
/* 470 */     nglTexCoordP1uiv(type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP1uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glTexCoordP2u(int type, IntBuffer coords) {
/* 475 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 476 */     long function_pointer = caps.glTexCoordP2uiv;
/* 477 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 478 */     BufferChecks.checkBuffer(coords, 2);
/* 479 */     nglTexCoordP2uiv(type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP2uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glTexCoordP3u(int type, IntBuffer coords) {
/* 484 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 485 */     long function_pointer = caps.glTexCoordP3uiv;
/* 486 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 487 */     BufferChecks.checkBuffer(coords, 3);
/* 488 */     nglTexCoordP3uiv(type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP3uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glTexCoordP4u(int type, IntBuffer coords) {
/* 493 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 494 */     long function_pointer = caps.glTexCoordP4uiv;
/* 495 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 496 */     BufferChecks.checkBuffer(coords, 4);
/* 497 */     nglTexCoordP4uiv(type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglTexCoordP4uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultiTexCoordP1ui(int texture, int type, int coords) {
/* 502 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 503 */     long function_pointer = caps.glMultiTexCoordP1ui;
/* 504 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 505 */     nglMultiTexCoordP1ui(texture, type, coords, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP1ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoordP2ui(int texture, int type, int coords) {
/* 510 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 511 */     long function_pointer = caps.glMultiTexCoordP2ui;
/* 512 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 513 */     nglMultiTexCoordP2ui(texture, type, coords, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP2ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoordP3ui(int texture, int type, int coords) {
/* 518 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 519 */     long function_pointer = caps.glMultiTexCoordP3ui;
/* 520 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 521 */     nglMultiTexCoordP3ui(texture, type, coords, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP3ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoordP4ui(int texture, int type, int coords) {
/* 526 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 527 */     long function_pointer = caps.glMultiTexCoordP4ui;
/* 528 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 529 */     nglMultiTexCoordP4ui(texture, type, coords, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP4ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoordP1u(int texture, int type, IntBuffer coords) {
/* 534 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 535 */     long function_pointer = caps.glMultiTexCoordP1uiv;
/* 536 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 537 */     BufferChecks.checkBuffer(coords, 1);
/* 538 */     nglMultiTexCoordP1uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP1uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultiTexCoordP2u(int texture, int type, IntBuffer coords) {
/* 543 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 544 */     long function_pointer = caps.glMultiTexCoordP2uiv;
/* 545 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 546 */     BufferChecks.checkBuffer(coords, 2);
/* 547 */     nglMultiTexCoordP2uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP2uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultiTexCoordP3u(int texture, int type, IntBuffer coords) {
/* 552 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 553 */     long function_pointer = caps.glMultiTexCoordP3uiv;
/* 554 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 555 */     BufferChecks.checkBuffer(coords, 3);
/* 556 */     nglMultiTexCoordP3uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP3uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultiTexCoordP4u(int texture, int type, IntBuffer coords) {
/* 561 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 562 */     long function_pointer = caps.glMultiTexCoordP4uiv;
/* 563 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 564 */     BufferChecks.checkBuffer(coords, 4);
/* 565 */     nglMultiTexCoordP4uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoordP4uiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glNormalP3ui(int type, int coords) {
/* 570 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 571 */     long function_pointer = caps.glNormalP3ui;
/* 572 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 573 */     nglNormalP3ui(type, coords, function_pointer);
/*     */   }
/*     */   static native void nglNormalP3ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glNormalP3u(int type, IntBuffer coords) {
/* 578 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 579 */     long function_pointer = caps.glNormalP3uiv;
/* 580 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 581 */     BufferChecks.checkBuffer(coords, 3);
/* 582 */     nglNormalP3uiv(type, MemoryUtil.getAddress(coords), function_pointer);
/*     */   }
/*     */   static native void nglNormalP3uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glColorP3ui(int type, int color) {
/* 587 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 588 */     long function_pointer = caps.glColorP3ui;
/* 589 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 590 */     nglColorP3ui(type, color, function_pointer);
/*     */   }
/*     */   static native void nglColorP3ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glColorP4ui(int type, int color) {
/* 595 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 596 */     long function_pointer = caps.glColorP4ui;
/* 597 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 598 */     nglColorP4ui(type, color, function_pointer);
/*     */   }
/*     */   static native void nglColorP4ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glColorP3u(int type, IntBuffer color) {
/* 603 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 604 */     long function_pointer = caps.glColorP3uiv;
/* 605 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 606 */     BufferChecks.checkBuffer(color, 3);
/* 607 */     nglColorP3uiv(type, MemoryUtil.getAddress(color), function_pointer);
/*     */   }
/*     */   static native void nglColorP3uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glColorP4u(int type, IntBuffer color) {
/* 612 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 613 */     long function_pointer = caps.glColorP4uiv;
/* 614 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 615 */     BufferChecks.checkBuffer(color, 4);
/* 616 */     nglColorP4uiv(type, MemoryUtil.getAddress(color), function_pointer);
/*     */   }
/*     */   static native void nglColorP4uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glSecondaryColorP3ui(int type, int color) {
/* 621 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 622 */     long function_pointer = caps.glSecondaryColorP3ui;
/* 623 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 624 */     nglSecondaryColorP3ui(type, color, function_pointer);
/*     */   }
/*     */   static native void nglSecondaryColorP3ui(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glSecondaryColorP3u(int type, IntBuffer color) {
/* 629 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 630 */     long function_pointer = caps.glSecondaryColorP3uiv;
/* 631 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 632 */     BufferChecks.checkBuffer(color, 3);
/* 633 */     nglSecondaryColorP3uiv(type, MemoryUtil.getAddress(color), function_pointer);
/*     */   }
/*     */   static native void nglSecondaryColorP3uiv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribP1ui(int index, int type, boolean normalized, int value) {
/* 638 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 639 */     long function_pointer = caps.glVertexAttribP1ui;
/* 640 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 641 */     nglVertexAttribP1ui(index, type, normalized, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP1ui(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glVertexAttribP2ui(int index, int type, boolean normalized, int value) {
/* 646 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 647 */     long function_pointer = caps.glVertexAttribP2ui;
/* 648 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 649 */     nglVertexAttribP2ui(index, type, normalized, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP2ui(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glVertexAttribP3ui(int index, int type, boolean normalized, int value) {
/* 654 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 655 */     long function_pointer = caps.glVertexAttribP3ui;
/* 656 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 657 */     nglVertexAttribP3ui(index, type, normalized, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP3ui(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glVertexAttribP4ui(int index, int type, boolean normalized, int value) {
/* 662 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 663 */     long function_pointer = caps.glVertexAttribP4ui;
/* 664 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 665 */     nglVertexAttribP4ui(index, type, normalized, value, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP4ui(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glVertexAttribP1u(int index, int type, boolean normalized, IntBuffer value) {
/* 670 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 671 */     long function_pointer = caps.glVertexAttribP1uiv;
/* 672 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 673 */     BufferChecks.checkBuffer(value, 1);
/* 674 */     nglVertexAttribP1uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP1uiv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribP2u(int index, int type, boolean normalized, IntBuffer value) {
/* 679 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 680 */     long function_pointer = caps.glVertexAttribP2uiv;
/* 681 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 682 */     BufferChecks.checkBuffer(value, 2);
/* 683 */     nglVertexAttribP2uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP2uiv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribP3u(int index, int type, boolean normalized, IntBuffer value) {
/* 688 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 689 */     long function_pointer = caps.glVertexAttribP3uiv;
/* 690 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 691 */     BufferChecks.checkBuffer(value, 3);
/* 692 */     nglVertexAttribP3uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribP3uiv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribP4u(int index, int type, boolean normalized, IntBuffer value) {
/* 697 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 698 */     long function_pointer = caps.glVertexAttribP4uiv;
/* 699 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 700 */     BufferChecks.checkBuffer(value, 4);
/* 701 */     nglVertexAttribP4uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglVertexAttribP4uiv(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL33.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */