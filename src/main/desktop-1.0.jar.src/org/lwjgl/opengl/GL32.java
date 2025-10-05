/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
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
/*     */ public final class GL32
/*     */ {
/*     */   public static final int GL_CONTEXT_PROFILE_MASK = 37158;
/*     */   public static final int GL_CONTEXT_CORE_PROFILE_BIT = 1;
/*     */   public static final int GL_CONTEXT_COMPATIBILITY_PROFILE_BIT = 2;
/*     */   public static final int GL_MAX_VERTEX_OUTPUT_COMPONENTS = 37154;
/*     */   public static final int GL_MAX_GEOMETRY_INPUT_COMPONENTS = 37155;
/*     */   public static final int GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 37156;
/*     */   public static final int GL_MAX_FRAGMENT_INPUT_COMPONENTS = 37157;
/*     */   public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
/*     */   public static final int GL_LAST_VERTEX_CONVENTION = 36430;
/*     */   public static final int GL_PROVOKING_VERTEX = 36431;
/*     */   public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 36428;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 34895;
/*     */   public static final int GL_SAMPLE_POSITION = 36432;
/*     */   public static final int GL_SAMPLE_MASK = 36433;
/*     */   public static final int GL_SAMPLE_MASK_VALUE = 36434;
/*     */   public static final int GL_TEXTURE_2D_MULTISAMPLE = 37120;
/*     */   public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 37121;
/*     */   public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 37122;
/*     */   public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 37123;
/*     */   public static final int GL_MAX_SAMPLE_MASK_WORDS = 36441;
/*     */   public static final int GL_MAX_COLOR_TEXTURE_SAMPLES = 37134;
/*     */   public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES = 37135;
/*     */   public static final int GL_MAX_INTEGER_SAMPLES = 37136;
/*     */   public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 37124;
/*     */   public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 37125;
/*     */   public static final int GL_TEXTURE_SAMPLES = 37126;
/*     */   public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 37127;
/*     */   public static final int GL_SAMPLER_2D_MULTISAMPLE = 37128;
/*     */   public static final int GL_INT_SAMPLER_2D_MULTISAMPLE = 37129;
/*     */   public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 37130;
/*     */   public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 37131;
/*     */   public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37132;
/*     */   public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37133;
/*     */   public static final int GL_DEPTH_CLAMP = 34383;
/*     */   public static final int GL_GEOMETRY_SHADER = 36313;
/*     */   public static final int GL_GEOMETRY_VERTICES_OUT = 36314;
/*     */   public static final int GL_GEOMETRY_INPUT_TYPE = 36315;
/*     */   public static final int GL_GEOMETRY_OUTPUT_TYPE = 36316;
/*     */   public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 35881;
/*     */   public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 36319;
/*     */   public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 36320;
/*     */   public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 36321;
/*     */   public static final int GL_LINES_ADJACENCY = 10;
/*     */   public static final int GL_LINE_STRIP_ADJACENCY = 11;
/*     */   public static final int GL_TRIANGLES_ADJACENCY = 12;
/*     */   public static final int GL_TRIANGLE_STRIP_ADJACENCY = 13;
/*     */   public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 36264;
/*     */   public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 36263;
/*     */   public static final int GL_PROGRAM_POINT_SIZE = 34370;
/*     */   public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 37137;
/*     */   public static final int GL_OBJECT_TYPE = 37138;
/*     */   public static final int GL_SYNC_CONDITION = 37139;
/*     */   public static final int GL_SYNC_STATUS = 37140;
/*     */   public static final int GL_SYNC_FLAGS = 37141;
/*     */   public static final int GL_SYNC_FENCE = 37142;
/*     */   public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 37143;
/*     */   public static final int GL_UNSIGNALED = 37144;
/*     */   public static final int GL_SIGNALED = 37145;
/*     */   public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 1;
/*     */   public static final long GL_TIMEOUT_IGNORED = -1L;
/*     */   public static final int GL_ALREADY_SIGNALED = 37146;
/*     */   public static final int GL_TIMEOUT_EXPIRED = 37147;
/*     */   public static final int GL_CONDITION_SATISFIED = 37148;
/*     */   public static final int GL_WAIT_FAILED = 37149;
/*     */   
/*     */   public static void glGetBufferParameter(int target, int pname, LongBuffer params) {
/* 213 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 214 */     long function_pointer = caps.glGetBufferParameteri64v;
/* 215 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 216 */     BufferChecks.checkBuffer(params, 4);
/* 217 */     nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetBufferParameteri64v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static long glGetBufferParameter(int target, int pname) {
/* 228 */     return glGetBufferParameteri64(target, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long glGetBufferParameteri64(int target, int pname) {
/* 233 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 234 */     long function_pointer = caps.glGetBufferParameteri64v;
/* 235 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 236 */     LongBuffer params = APIUtil.getBufferLong(caps);
/* 237 */     nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params), function_pointer);
/* 238 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glDrawElementsBaseVertex(int mode, ByteBuffer indices, int basevertex) {
/* 242 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 243 */     long function_pointer = caps.glDrawElementsBaseVertex;
/* 244 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 245 */     GLChecks.ensureElementVBOdisabled(caps);
/* 246 */     BufferChecks.checkDirect(indices);
/* 247 */     nglDrawElementsBaseVertex(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), basevertex, function_pointer);
/*     */   }
/*     */   public static void glDrawElementsBaseVertex(int mode, IntBuffer indices, int basevertex) {
/* 250 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 251 */     long function_pointer = caps.glDrawElementsBaseVertex;
/* 252 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 253 */     GLChecks.ensureElementVBOdisabled(caps);
/* 254 */     BufferChecks.checkDirect(indices);
/* 255 */     nglDrawElementsBaseVertex(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), basevertex, function_pointer);
/*     */   }
/*     */   public static void glDrawElementsBaseVertex(int mode, ShortBuffer indices, int basevertex) {
/* 258 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 259 */     long function_pointer = caps.glDrawElementsBaseVertex;
/* 260 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 261 */     GLChecks.ensureElementVBOdisabled(caps);
/* 262 */     BufferChecks.checkDirect(indices);
/* 263 */     nglDrawElementsBaseVertex(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), basevertex, function_pointer);
/*     */   }
/*     */   
/*     */   public static void glDrawElementsBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int basevertex) {
/* 267 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 268 */     long function_pointer = caps.glDrawElementsBaseVertex;
/* 269 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 270 */     GLChecks.ensureElementVBOenabled(caps);
/* 271 */     nglDrawElementsBaseVertexBO(mode, indices_count, type, indices_buffer_offset, basevertex, function_pointer);
/*     */   }
/*     */   static native void nglDrawElementsBaseVertex(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, long paramLong2);
/*     */   static native void nglDrawElementsBaseVertexBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, long paramLong2);
/*     */   public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ByteBuffer indices, int basevertex) {
/* 276 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 277 */     long function_pointer = caps.glDrawRangeElementsBaseVertex;
/* 278 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 279 */     GLChecks.ensureElementVBOdisabled(caps);
/* 280 */     BufferChecks.checkDirect(indices);
/* 281 */     nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), 5121, MemoryUtil.getAddress(indices), basevertex, function_pointer);
/*     */   }
/*     */   public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, IntBuffer indices, int basevertex) {
/* 284 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 285 */     long function_pointer = caps.glDrawRangeElementsBaseVertex;
/* 286 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 287 */     GLChecks.ensureElementVBOdisabled(caps);
/* 288 */     BufferChecks.checkDirect(indices);
/* 289 */     nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), 5125, MemoryUtil.getAddress(indices), basevertex, function_pointer);
/*     */   }
/*     */   public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ShortBuffer indices, int basevertex) {
/* 292 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 293 */     long function_pointer = caps.glDrawRangeElementsBaseVertex;
/* 294 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 295 */     GLChecks.ensureElementVBOdisabled(caps);
/* 296 */     BufferChecks.checkDirect(indices);
/* 297 */     nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), 5123, MemoryUtil.getAddress(indices), basevertex, function_pointer);
/*     */   }
/*     */   static native void nglDrawRangeElementsBaseVertex(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, int paramInt6, long paramLong2);
/*     */   public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset, int basevertex) {
/* 301 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 302 */     long function_pointer = caps.glDrawRangeElementsBaseVertex;
/* 303 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 304 */     GLChecks.ensureElementVBOenabled(caps);
/* 305 */     nglDrawRangeElementsBaseVertexBO(mode, start, end, indices_count, type, indices_buffer_offset, basevertex, function_pointer);
/*     */   }
/*     */   static native void nglDrawRangeElementsBaseVertexBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, int paramInt6, long paramLong2);
/*     */   
/*     */   public static void glDrawElementsInstancedBaseVertex(int mode, ByteBuffer indices, int primcount, int basevertex) {
/* 310 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 311 */     long function_pointer = caps.glDrawElementsInstancedBaseVertex;
/* 312 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 313 */     GLChecks.ensureElementVBOdisabled(caps);
/* 314 */     BufferChecks.checkDirect(indices);
/* 315 */     nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
/*     */   }
/*     */   public static void glDrawElementsInstancedBaseVertex(int mode, IntBuffer indices, int primcount, int basevertex) {
/* 318 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 319 */     long function_pointer = caps.glDrawElementsInstancedBaseVertex;
/* 320 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 321 */     GLChecks.ensureElementVBOdisabled(caps);
/* 322 */     BufferChecks.checkDirect(indices);
/* 323 */     nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
/*     */   }
/*     */   public static void glDrawElementsInstancedBaseVertex(int mode, ShortBuffer indices, int primcount, int basevertex) {
/* 326 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 327 */     long function_pointer = caps.glDrawElementsInstancedBaseVertex;
/* 328 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 329 */     GLChecks.ensureElementVBOdisabled(caps);
/* 330 */     BufferChecks.checkDirect(indices);
/* 331 */     nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
/*     */   }
/*     */   static native void nglDrawElementsInstancedBaseVertex(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, int paramInt5, long paramLong2);
/*     */   public static void glDrawElementsInstancedBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex) {
/* 335 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 336 */     long function_pointer = caps.glDrawElementsInstancedBaseVertex;
/* 337 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 338 */     GLChecks.ensureElementVBOenabled(caps);
/* 339 */     nglDrawElementsInstancedBaseVertexBO(mode, indices_count, type, indices_buffer_offset, primcount, basevertex, function_pointer);
/*     */   }
/*     */   static native void nglDrawElementsInstancedBaseVertexBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, int paramInt5, long paramLong2);
/*     */   
/*     */   public static void glProvokingVertex(int mode) {
/* 344 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 345 */     long function_pointer = caps.glProvokingVertex;
/* 346 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 347 */     nglProvokingVertex(mode, function_pointer);
/*     */   }
/*     */   static native void nglProvokingVertex(int paramInt, long paramLong);
/*     */   
/*     */   public static void glTexImage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
/* 352 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 353 */     long function_pointer = caps.glTexImage2DMultisample;
/* 354 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 355 */     nglTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
/*     */   }
/*     */   static native void nglTexImage2DMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, long paramLong);
/*     */   
/*     */   public static void glTexImage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
/* 360 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 361 */     long function_pointer = caps.glTexImage3DMultisample;
/* 362 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 363 */     nglTexImage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
/*     */   }
/*     */   static native void nglTexImage3DMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, long paramLong);
/*     */   
/*     */   public static void glGetMultisample(int pname, int index, FloatBuffer val) {
/* 368 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 369 */     long function_pointer = caps.glGetMultisamplefv;
/* 370 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 371 */     BufferChecks.checkBuffer(val, 2);
/* 372 */     nglGetMultisamplefv(pname, index, MemoryUtil.getAddress(val), function_pointer);
/*     */   }
/*     */   static native void nglGetMultisamplefv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glSampleMaski(int index, int mask) {
/* 377 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 378 */     long function_pointer = caps.glSampleMaski;
/* 379 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 380 */     nglSampleMaski(index, mask, function_pointer);
/*     */   }
/*     */   static native void nglSampleMaski(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static void glFramebufferTexture(int target, int attachment, int texture, int level) {
/* 385 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 386 */     long function_pointer = caps.glFramebufferTexture;
/* 387 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 388 */     nglFramebufferTexture(target, attachment, texture, level, function_pointer);
/*     */   }
/*     */   static native void nglFramebufferTexture(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*     */   
/*     */   public static GLSync glFenceSync(int condition, int flags) {
/* 393 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 394 */     long function_pointer = caps.glFenceSync;
/* 395 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 396 */     GLSync __result = new GLSync(nglFenceSync(condition, flags, function_pointer));
/* 397 */     return __result;
/*     */   }
/*     */   static native long nglFenceSync(int paramInt1, int paramInt2, long paramLong);
/*     */   
/*     */   public static boolean glIsSync(GLSync sync) {
/* 402 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 403 */     long function_pointer = caps.glIsSync;
/* 404 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 405 */     boolean __result = nglIsSync(sync.getPointer(), function_pointer);
/* 406 */     return __result;
/*     */   }
/*     */   static native boolean nglIsSync(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDeleteSync(GLSync sync) {
/* 411 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 412 */     long function_pointer = caps.glDeleteSync;
/* 413 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 414 */     nglDeleteSync(sync.getPointer(), function_pointer);
/*     */   }
/*     */   static native void nglDeleteSync(long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glClientWaitSync(GLSync sync, int flags, long timeout) {
/* 419 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 420 */     long function_pointer = caps.glClientWaitSync;
/* 421 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 422 */     int __result = nglClientWaitSync(sync.getPointer(), flags, timeout, function_pointer);
/* 423 */     return __result;
/*     */   }
/*     */   static native int nglClientWaitSync(long paramLong1, int paramInt, long paramLong2, long paramLong3);
/*     */   
/*     */   public static void glWaitSync(GLSync sync, int flags, long timeout) {
/* 428 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 429 */     long function_pointer = caps.glWaitSync;
/* 430 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 431 */     nglWaitSync(sync.getPointer(), flags, timeout, function_pointer);
/*     */   }
/*     */   static native void nglWaitSync(long paramLong1, int paramInt, long paramLong2, long paramLong3);
/*     */   
/*     */   public static void glGetInteger64(int pname, LongBuffer data) {
/* 436 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 437 */     long function_pointer = caps.glGetInteger64v;
/* 438 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 439 */     BufferChecks.checkBuffer(data, 1);
/* 440 */     nglGetInteger64v(pname, MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetInteger64v(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static long glGetInteger64(int pname) {
/* 446 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 447 */     long function_pointer = caps.glGetInteger64v;
/* 448 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 449 */     LongBuffer data = APIUtil.getBufferLong(caps);
/* 450 */     nglGetInteger64v(pname, MemoryUtil.getAddress(data), function_pointer);
/* 451 */     return data.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetInteger64(int value, int index, LongBuffer data) {
/* 455 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 456 */     long function_pointer = caps.glGetInteger64i_v;
/* 457 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 458 */     BufferChecks.checkBuffer(data, 4);
/* 459 */     nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetInteger64i_v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static long glGetInteger64(int value, int index) {
/* 465 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 466 */     long function_pointer = caps.glGetInteger64i_v;
/* 467 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 468 */     LongBuffer data = APIUtil.getBufferLong(caps);
/* 469 */     nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data), function_pointer);
/* 470 */     return data.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetSync(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
/* 474 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 475 */     long function_pointer = caps.glGetSynciv;
/* 476 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 477 */     if (length != null)
/* 478 */       BufferChecks.checkBuffer(length, 1); 
/* 479 */     BufferChecks.checkDirect(values);
/* 480 */     nglGetSynciv(sync.getPointer(), pname, values.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(values), function_pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native void nglGetSynciv(long paramLong1, int paramInt1, int paramInt2, long paramLong2, long paramLong3, long paramLong4);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static int glGetSync(GLSync sync, int pname) {
/* 491 */     return glGetSynci(sync, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetSynci(GLSync sync, int pname) {
/* 496 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 497 */     long function_pointer = caps.glGetSynciv;
/* 498 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 499 */     IntBuffer values = APIUtil.getBufferInt(caps);
/* 500 */     nglGetSynciv(sync.getPointer(), pname, 1, 0L, MemoryUtil.getAddress(values), function_pointer);
/* 501 */     return values.get(0);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */