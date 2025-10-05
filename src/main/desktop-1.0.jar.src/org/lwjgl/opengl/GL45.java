/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.DoubleBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.LongBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ import org.lwjgl.BufferChecks;
/*      */ import org.lwjgl.LWJGLUtil;
/*      */ import org.lwjgl.MemoryUtil;
/*      */ import org.lwjgl.PointerBuffer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class GL45
/*      */ {
/*      */   public static final int GL_NEGATIVE_ONE_TO_ONE = 37726;
/*      */   public static final int GL_ZERO_TO_ONE = 37727;
/*      */   public static final int GL_CLIP_ORIGIN = 37724;
/*      */   public static final int GL_CLIP_DEPTH_MODE = 37725;
/*      */   public static final int GL_QUERY_WAIT_INVERTED = 36375;
/*      */   public static final int GL_QUERY_NO_WAIT_INVERTED = 36376;
/*      */   public static final int GL_QUERY_BY_REGION_WAIT_INVERTED = 36377;
/*      */   public static final int GL_QUERY_BY_REGION_NO_WAIT_INVERTED = 36378;
/*      */   public static final int GL_MAX_CULL_DISTANCES = 33529;
/*      */   public static final int GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 33530;
/*      */   public static final int GL_TEXTURE_TARGET = 4102;
/*      */   public static final int GL_QUERY_TARGET = 33514;
/*      */   public static final int GL_TEXTURE_BINDING = 33515;
/*      */   public static final int GL_CONTEXT_RELEASE_BEHAVIOR = 33531;
/*      */   public static final int GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH = 33532;
/*      */   public static final int GL_GUILTY_CONTEXT_RESET = 33363;
/*      */   public static final int GL_INNOCENT_CONTEXT_RESET = 33364;
/*      */   public static final int GL_UNKNOWN_CONTEXT_RESET = 33365;
/*      */   public static final int GL_CONTEXT_ROBUST_ACCESS = 37107;
/*      */   public static final int GL_RESET_NOTIFICATION_STRATEGY = 33366;
/*      */   public static final int GL_LOSE_CONTEXT_ON_RESET = 33362;
/*      */   public static final int GL_NO_RESET_NOTIFICATION = 33377;
/*      */   public static final int GL_CONTEXT_LOST = 1287;
/*      */   
/*      */   public static void glClipControl(int origin, int depth) {
/*   95 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   96 */     long function_pointer = caps.glClipControl;
/*   97 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   98 */     nglClipControl(origin, depth, function_pointer);
/*      */   }
/*      */   static native void nglClipControl(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glCreateTransformFeedbacks(IntBuffer ids) {
/*  103 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  104 */     long function_pointer = caps.glCreateTransformFeedbacks;
/*  105 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  106 */     BufferChecks.checkDirect(ids);
/*  107 */     nglCreateTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateTransformFeedbacks(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateTransformFeedbacks() {
/*  113 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  114 */     long function_pointer = caps.glCreateTransformFeedbacks;
/*  115 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  116 */     IntBuffer ids = APIUtil.getBufferInt(caps);
/*  117 */     nglCreateTransformFeedbacks(1, MemoryUtil.getAddress(ids), function_pointer);
/*  118 */     return ids.get(0);
/*      */   }
/*      */   
/*      */   public static void glTransformFeedbackBufferBase(int xfb, int index, int buffer) {
/*  122 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  123 */     long function_pointer = caps.glTransformFeedbackBufferBase;
/*  124 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  125 */     nglTransformFeedbackBufferBase(xfb, index, buffer, function_pointer);
/*      */   }
/*      */   static native void nglTransformFeedbackBufferBase(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTransformFeedbackBufferRange(int xfb, int index, int buffer, long offset, long size) {
/*  130 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  131 */     long function_pointer = caps.glTransformFeedbackBufferRange;
/*  132 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  133 */     nglTransformFeedbackBufferRange(xfb, index, buffer, offset, size, function_pointer);
/*      */   }
/*      */   static native void nglTransformFeedbackBufferRange(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glGetTransformFeedback(int xfb, int pname, IntBuffer param) {
/*  138 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  139 */     long function_pointer = caps.glGetTransformFeedbackiv;
/*  140 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  141 */     BufferChecks.checkBuffer(param, 1);
/*  142 */     nglGetTransformFeedbackiv(xfb, pname, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTransformFeedbackiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTransformFeedbacki(int xfb, int pname) {
/*  148 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  149 */     long function_pointer = caps.glGetTransformFeedbackiv;
/*  150 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  151 */     IntBuffer param = APIUtil.getBufferInt(caps);
/*  152 */     nglGetTransformFeedbackiv(xfb, pname, MemoryUtil.getAddress(param), function_pointer);
/*  153 */     return param.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTransformFeedback(int xfb, int pname, int index, IntBuffer param) {
/*  157 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  158 */     long function_pointer = caps.glGetTransformFeedbacki_v;
/*  159 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  160 */     BufferChecks.checkBuffer(param, 1);
/*  161 */     nglGetTransformFeedbacki_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTransformFeedbacki_v(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTransformFeedbacki(int xfb, int pname, int index) {
/*  167 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  168 */     long function_pointer = caps.glGetTransformFeedbacki_v;
/*  169 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  170 */     IntBuffer param = APIUtil.getBufferInt(caps);
/*  171 */     nglGetTransformFeedbacki_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
/*  172 */     return param.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTransformFeedback(int xfb, int pname, int index, LongBuffer param) {
/*  176 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  177 */     long function_pointer = caps.glGetTransformFeedbacki64_v;
/*  178 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  179 */     BufferChecks.checkBuffer(param, 1);
/*  180 */     nglGetTransformFeedbacki64_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTransformFeedbacki64_v(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static long glGetTransformFeedbacki64(int xfb, int pname, int index) {
/*  186 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  187 */     long function_pointer = caps.glGetTransformFeedbacki64_v;
/*  188 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  189 */     LongBuffer param = APIUtil.getBufferLong(caps);
/*  190 */     nglGetTransformFeedbacki64_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
/*  191 */     return param.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateBuffers(IntBuffer buffers) {
/*  195 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  196 */     long function_pointer = caps.glCreateBuffers;
/*  197 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  198 */     BufferChecks.checkDirect(buffers);
/*  199 */     nglCreateBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateBuffers(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateBuffers() {
/*  205 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  206 */     long function_pointer = caps.glCreateBuffers;
/*  207 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  208 */     IntBuffer buffers = APIUtil.getBufferInt(caps);
/*  209 */     nglCreateBuffers(1, MemoryUtil.getAddress(buffers), function_pointer);
/*  210 */     return buffers.get(0);
/*      */   }
/*      */   
/*      */   public static void glNamedBufferStorage(int buffer, ByteBuffer data, int flags) {
/*  214 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  215 */     long function_pointer = caps.glNamedBufferStorage;
/*  216 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  217 */     BufferChecks.checkDirect(data);
/*  218 */     nglNamedBufferStorage(buffer, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferStorage(int buffer, DoubleBuffer data, int flags) {
/*  221 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  222 */     long function_pointer = caps.glNamedBufferStorage;
/*  223 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  224 */     BufferChecks.checkDirect(data);
/*  225 */     nglNamedBufferStorage(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferStorage(int buffer, FloatBuffer data, int flags) {
/*  228 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  229 */     long function_pointer = caps.glNamedBufferStorage;
/*  230 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  231 */     BufferChecks.checkDirect(data);
/*  232 */     nglNamedBufferStorage(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferStorage(int buffer, IntBuffer data, int flags) {
/*  235 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  236 */     long function_pointer = caps.glNamedBufferStorage;
/*  237 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  238 */     BufferChecks.checkDirect(data);
/*  239 */     nglNamedBufferStorage(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferStorage(int buffer, ShortBuffer data, int flags) {
/*  242 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  243 */     long function_pointer = caps.glNamedBufferStorage;
/*  244 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  245 */     BufferChecks.checkDirect(data);
/*  246 */     nglNamedBufferStorage(buffer, (data.remaining() << 1), MemoryUtil.getAddress(data), flags, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferStorage(int buffer, LongBuffer data, int flags) {
/*  249 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  250 */     long function_pointer = caps.glNamedBufferStorage;
/*  251 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  252 */     BufferChecks.checkDirect(data);
/*  253 */     nglNamedBufferStorage(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglNamedBufferStorage(int paramInt1, long paramLong1, long paramLong2, int paramInt2, long paramLong3);
/*      */   
/*      */   public static void glNamedBufferStorage(int buffer, long size, int flags) {
/*  259 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  260 */     long function_pointer = caps.glNamedBufferStorage;
/*  261 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  262 */     nglNamedBufferStorage(buffer, size, 0L, flags, function_pointer);
/*      */   }
/*      */   
/*      */   public static void glNamedBufferData(int buffer, long data_size, int usage) {
/*  266 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  267 */     long function_pointer = caps.glNamedBufferData;
/*  268 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  269 */     nglNamedBufferData(buffer, data_size, 0L, usage, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferData(int buffer, ByteBuffer data, int usage) {
/*  272 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  273 */     long function_pointer = caps.glNamedBufferData;
/*  274 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  275 */     BufferChecks.checkDirect(data);
/*  276 */     nglNamedBufferData(buffer, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferData(int buffer, DoubleBuffer data, int usage) {
/*  279 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  280 */     long function_pointer = caps.glNamedBufferData;
/*  281 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  282 */     BufferChecks.checkDirect(data);
/*  283 */     nglNamedBufferData(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), usage, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferData(int buffer, FloatBuffer data, int usage) {
/*  286 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  287 */     long function_pointer = caps.glNamedBufferData;
/*  288 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  289 */     BufferChecks.checkDirect(data);
/*  290 */     nglNamedBufferData(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferData(int buffer, IntBuffer data, int usage) {
/*  293 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  294 */     long function_pointer = caps.glNamedBufferData;
/*  295 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  296 */     BufferChecks.checkDirect(data);
/*  297 */     nglNamedBufferData(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
/*      */   }
/*      */   public static void glNamedBufferData(int buffer, ShortBuffer data, int usage) {
/*  300 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  301 */     long function_pointer = caps.glNamedBufferData;
/*  302 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  303 */     BufferChecks.checkDirect(data);
/*  304 */     nglNamedBufferData(buffer, (data.remaining() << 1), MemoryUtil.getAddress(data), usage, function_pointer);
/*      */   }
/*      */   static native void nglNamedBufferData(int paramInt1, long paramLong1, long paramLong2, int paramInt2, long paramLong3);
/*      */   
/*      */   public static void glNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
/*  309 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  310 */     long function_pointer = caps.glNamedBufferSubData;
/*  311 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  312 */     BufferChecks.checkDirect(data);
/*  313 */     nglNamedBufferSubData(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glNamedBufferSubData(int buffer, long offset, DoubleBuffer data) {
/*  316 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  317 */     long function_pointer = caps.glNamedBufferSubData;
/*  318 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  319 */     BufferChecks.checkDirect(data);
/*  320 */     nglNamedBufferSubData(buffer, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glNamedBufferSubData(int buffer, long offset, FloatBuffer data) {
/*  323 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  324 */     long function_pointer = caps.glNamedBufferSubData;
/*  325 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  326 */     BufferChecks.checkDirect(data);
/*  327 */     nglNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glNamedBufferSubData(int buffer, long offset, IntBuffer data) {
/*  330 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  331 */     long function_pointer = caps.glNamedBufferSubData;
/*  332 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  333 */     BufferChecks.checkDirect(data);
/*  334 */     nglNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glNamedBufferSubData(int buffer, long offset, ShortBuffer data) {
/*  337 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  338 */     long function_pointer = caps.glNamedBufferSubData;
/*  339 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  340 */     BufferChecks.checkDirect(data);
/*  341 */     nglNamedBufferSubData(buffer, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglNamedBufferSubData(int paramInt, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static void glCopyNamedBufferSubData(int readBuffer, int writeBuffer, long readOffset, long writeOffset, long size) {
/*  346 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  347 */     long function_pointer = caps.glCopyNamedBufferSubData;
/*  348 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  349 */     nglCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size, function_pointer);
/*      */   }
/*      */   static native void nglCopyNamedBufferSubData(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static void glClearNamedBufferData(int buffer, int internalformat, int format, int type, ByteBuffer data) {
/*  354 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  355 */     long function_pointer = caps.glClearNamedBufferData;
/*  356 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  357 */     BufferChecks.checkBuffer(data, 1);
/*  358 */     nglClearNamedBufferData(buffer, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglClearNamedBufferData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glClearNamedBufferSubData(int buffer, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
/*  363 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  364 */     long function_pointer = caps.glClearNamedBufferSubData;
/*  365 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  366 */     BufferChecks.checkBuffer(data, 1);
/*  367 */     nglClearNamedBufferSubData(buffer, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static native void nglClearNamedBufferSubData(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3, long paramLong4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteBuffer glMapNamedBuffer(int buffer, int access, ByteBuffer old_buffer) {
/*  384 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  385 */     long function_pointer = caps.glMapNamedBuffer;
/*  386 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  387 */     if (old_buffer != null)
/*  388 */       BufferChecks.checkDirect(old_buffer); 
/*  389 */     ByteBuffer __result = nglMapNamedBuffer(buffer, access, glGetNamedBufferParameteri(buffer, 34660), old_buffer, function_pointer);
/*  390 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ByteBuffer glMapNamedBuffer(int buffer, int access, long length, ByteBuffer old_buffer) {
/*  405 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  406 */     long function_pointer = caps.glMapNamedBuffer;
/*  407 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  408 */     if (old_buffer != null)
/*  409 */       BufferChecks.checkDirect(old_buffer); 
/*  410 */     ByteBuffer __result = nglMapNamedBuffer(buffer, access, length, old_buffer, function_pointer);
/*  411 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*      */   }
/*      */   static native ByteBuffer nglMapNamedBuffer(int paramInt1, int paramInt2, long paramLong1, ByteBuffer paramByteBuffer, long paramLong2);
/*      */   
/*      */   public static ByteBuffer glMapNamedBufferRange(int buffer, long offset, long length, int access, ByteBuffer old_buffer) {
/*  416 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  417 */     long function_pointer = caps.glMapNamedBufferRange;
/*  418 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  419 */     if (old_buffer != null)
/*  420 */       BufferChecks.checkDirect(old_buffer); 
/*  421 */     ByteBuffer __result = nglMapNamedBufferRange(buffer, offset, length, access, old_buffer, function_pointer);
/*  422 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*      */   }
/*      */   static native ByteBuffer nglMapNamedBufferRange(int paramInt1, long paramLong1, long paramLong2, int paramInt2, ByteBuffer paramByteBuffer, long paramLong3);
/*      */   
/*      */   public static boolean glUnmapNamedBuffer(int buffer) {
/*  427 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  428 */     long function_pointer = caps.glUnmapNamedBuffer;
/*  429 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  430 */     boolean __result = nglUnmapNamedBuffer(buffer, function_pointer);
/*  431 */     return __result;
/*      */   }
/*      */   static native boolean nglUnmapNamedBuffer(int paramInt, long paramLong);
/*      */   
/*      */   public static void glFlushMappedNamedBufferRange(int buffer, long offset, long length) {
/*  436 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  437 */     long function_pointer = caps.glFlushMappedNamedBufferRange;
/*  438 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  439 */     nglFlushMappedNamedBufferRange(buffer, offset, length, function_pointer);
/*      */   }
/*      */   static native void nglFlushMappedNamedBufferRange(int paramInt, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glGetNamedBufferParameter(int buffer, int pname, IntBuffer params) {
/*  444 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  445 */     long function_pointer = caps.glGetNamedBufferParameteriv;
/*  446 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  447 */     BufferChecks.checkDirect(params);
/*  448 */     nglGetNamedBufferParameteriv(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetNamedBufferParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetNamedBufferParameteri(int buffer, int pname) {
/*  454 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  455 */     long function_pointer = caps.glGetNamedBufferParameteriv;
/*  456 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  457 */     IntBuffer params = APIUtil.getBufferInt(caps);
/*  458 */     nglGetNamedBufferParameteriv(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*  459 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetNamedBufferParameter(int buffer, int pname, LongBuffer params) {
/*  463 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  464 */     long function_pointer = caps.glGetNamedBufferParameteri64v;
/*  465 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  466 */     BufferChecks.checkBuffer(params, 1);
/*  467 */     nglGetNamedBufferParameteri64v(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetNamedBufferParameteri64v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static long glGetNamedBufferParameteri64(int buffer, int pname) {
/*  473 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  474 */     long function_pointer = caps.glGetNamedBufferParameteri64v;
/*  475 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  476 */     LongBuffer params = APIUtil.getBufferLong(caps);
/*  477 */     nglGetNamedBufferParameteri64v(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*  478 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static ByteBuffer glGetNamedBufferPointer(int buffer, int pname) {
/*  482 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  483 */     long function_pointer = caps.glGetNamedBufferPointerv;
/*  484 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  485 */     ByteBuffer __result = nglGetNamedBufferPointerv(buffer, pname, glGetNamedBufferParameteri(buffer, 34660), function_pointer);
/*  486 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*      */   }
/*      */   static native ByteBuffer nglGetNamedBufferPointerv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
/*  491 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  492 */     long function_pointer = caps.glGetNamedBufferSubData;
/*  493 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  494 */     BufferChecks.checkDirect(data);
/*  495 */     nglGetNamedBufferSubData(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glGetNamedBufferSubData(int buffer, long offset, DoubleBuffer data) {
/*  498 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  499 */     long function_pointer = caps.glGetNamedBufferSubData;
/*  500 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  501 */     BufferChecks.checkDirect(data);
/*  502 */     nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glGetNamedBufferSubData(int buffer, long offset, FloatBuffer data) {
/*  505 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  506 */     long function_pointer = caps.glGetNamedBufferSubData;
/*  507 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  508 */     BufferChecks.checkDirect(data);
/*  509 */     nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glGetNamedBufferSubData(int buffer, long offset, IntBuffer data) {
/*  512 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  513 */     long function_pointer = caps.glGetNamedBufferSubData;
/*  514 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  515 */     BufferChecks.checkDirect(data);
/*  516 */     nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glGetNamedBufferSubData(int buffer, long offset, ShortBuffer data) {
/*  519 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  520 */     long function_pointer = caps.glGetNamedBufferSubData;
/*  521 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  522 */     BufferChecks.checkDirect(data);
/*  523 */     nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglGetNamedBufferSubData(int paramInt, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static void glCreateFramebuffers(IntBuffer framebuffers) {
/*  528 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  529 */     long function_pointer = caps.glCreateFramebuffers;
/*  530 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  531 */     BufferChecks.checkDirect(framebuffers);
/*  532 */     nglCreateFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateFramebuffers(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateFramebuffers() {
/*  538 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  539 */     long function_pointer = caps.glCreateFramebuffers;
/*  540 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  541 */     IntBuffer framebuffers = APIUtil.getBufferInt(caps);
/*  542 */     nglCreateFramebuffers(1, MemoryUtil.getAddress(framebuffers), function_pointer);
/*  543 */     return framebuffers.get(0);
/*      */   }
/*      */   
/*      */   public static void glNamedFramebufferRenderbuffer(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer) {
/*  547 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  548 */     long function_pointer = caps.glNamedFramebufferRenderbuffer;
/*  549 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  550 */     nglNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer, function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferRenderbuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glNamedFramebufferParameteri(int framebuffer, int pname, int param) {
/*  555 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  556 */     long function_pointer = caps.glNamedFramebufferParameteri;
/*  557 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  558 */     nglNamedFramebufferParameteri(framebuffer, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glNamedFramebufferTexture(int framebuffer, int attachment, int texture, int level) {
/*  563 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  564 */     long function_pointer = caps.glNamedFramebufferTexture;
/*  565 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  566 */     nglNamedFramebufferTexture(framebuffer, attachment, texture, level, function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferTexture(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glNamedFramebufferTextureLayer(int framebuffer, int attachment, int texture, int level, int layer) {
/*  571 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  572 */     long function_pointer = caps.glNamedFramebufferTextureLayer;
/*  573 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  574 */     nglNamedFramebufferTextureLayer(framebuffer, attachment, texture, level, layer, function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferTextureLayer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glNamedFramebufferDrawBuffer(int framebuffer, int mode) {
/*  579 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  580 */     long function_pointer = caps.glNamedFramebufferDrawBuffer;
/*  581 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  582 */     nglNamedFramebufferDrawBuffer(framebuffer, mode, function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferDrawBuffer(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glNamedFramebufferDrawBuffers(int framebuffer, IntBuffer bufs) {
/*  587 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  588 */     long function_pointer = caps.glNamedFramebufferDrawBuffers;
/*  589 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  590 */     BufferChecks.checkDirect(bufs);
/*  591 */     nglNamedFramebufferDrawBuffers(framebuffer, bufs.remaining(), MemoryUtil.getAddress(bufs), function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferDrawBuffers(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glNamedFramebufferReadBuffer(int framebuffer, int mode) {
/*  596 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  597 */     long function_pointer = caps.glNamedFramebufferReadBuffer;
/*  598 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  599 */     nglNamedFramebufferReadBuffer(framebuffer, mode, function_pointer);
/*      */   }
/*      */   static native void nglNamedFramebufferReadBuffer(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glInvalidateNamedFramebufferData(int framebuffer, IntBuffer attachments) {
/*  604 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  605 */     long function_pointer = caps.glInvalidateNamedFramebufferData;
/*  606 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  607 */     BufferChecks.checkDirect(attachments);
/*  608 */     nglInvalidateNamedFramebufferData(framebuffer, attachments.remaining(), MemoryUtil.getAddress(attachments), function_pointer);
/*      */   }
/*      */   static native void nglInvalidateNamedFramebufferData(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glInvalidateNamedFramebufferSubData(int framebuffer, IntBuffer attachments, int x, int y, int width, int height) {
/*  613 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  614 */     long function_pointer = caps.glInvalidateNamedFramebufferSubData;
/*  615 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  616 */     BufferChecks.checkDirect(attachments);
/*  617 */     nglInvalidateNamedFramebufferSubData(framebuffer, attachments.remaining(), MemoryUtil.getAddress(attachments), x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglInvalidateNamedFramebufferSubData(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong2);
/*      */   
/*      */   public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
/*  622 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  623 */     long function_pointer = caps.glClearNamedFramebufferiv;
/*  624 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  625 */     BufferChecks.checkBuffer(value, 1);
/*  626 */     nglClearNamedFramebufferiv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
/*      */   }
/*      */   static native void nglClearNamedFramebufferiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glClearNamedFramebufferu(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
/*  631 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  632 */     long function_pointer = caps.glClearNamedFramebufferuiv;
/*  633 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  634 */     BufferChecks.checkBuffer(value, 4);
/*  635 */     nglClearNamedFramebufferuiv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
/*      */   }
/*      */   static native void nglClearNamedFramebufferuiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, FloatBuffer value) {
/*  640 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  641 */     long function_pointer = caps.glClearNamedFramebufferfv;
/*  642 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  643 */     BufferChecks.checkBuffer(value, 1);
/*  644 */     nglClearNamedFramebufferfv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
/*      */   }
/*      */   static native void nglClearNamedFramebufferfv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glClearNamedFramebufferfi(int framebuffer, int buffer, float depth, int stencil) {
/*  649 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  650 */     long function_pointer = caps.glClearNamedFramebufferfi;
/*  651 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  652 */     nglClearNamedFramebufferfi(framebuffer, buffer, depth, stencil, function_pointer);
/*      */   }
/*      */   static native void nglClearNamedFramebufferfi(int paramInt1, int paramInt2, float paramFloat, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glBlitNamedFramebuffer(int readFramebuffer, int drawFramebuffer, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/*  657 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  658 */     long function_pointer = caps.glBlitNamedFramebuffer;
/*  659 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  660 */     nglBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
/*      */   }
/*      */   static native void nglBlitNamedFramebuffer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, long paramLong);
/*      */   
/*      */   public static int glCheckNamedFramebufferStatus(int framebuffer, int target) {
/*  665 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  666 */     long function_pointer = caps.glCheckNamedFramebufferStatus;
/*  667 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  668 */     int __result = nglCheckNamedFramebufferStatus(framebuffer, target, function_pointer);
/*  669 */     return __result;
/*      */   }
/*      */   static native int nglCheckNamedFramebufferStatus(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glGetNamedFramebufferParameter(int framebuffer, int pname, IntBuffer params) {
/*  674 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  675 */     long function_pointer = caps.glGetNamedFramebufferParameteriv;
/*  676 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  677 */     BufferChecks.checkBuffer(params, 1);
/*  678 */     nglGetNamedFramebufferParameteriv(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetNamedFramebufferParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetNamedFramebufferParameter(int framebuffer, int pname) {
/*  684 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  685 */     long function_pointer = caps.glGetNamedFramebufferParameteriv;
/*  686 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  687 */     IntBuffer params = APIUtil.getBufferInt(caps);
/*  688 */     nglGetNamedFramebufferParameteriv(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*  689 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname, IntBuffer params) {
/*  693 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  694 */     long function_pointer = caps.glGetNamedFramebufferAttachmentParameteriv;
/*  695 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  696 */     BufferChecks.checkBuffer(params, 1);
/*  697 */     nglGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetNamedFramebufferAttachmentParameteriv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname) {
/*  703 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  704 */     long function_pointer = caps.glGetNamedFramebufferAttachmentParameteriv;
/*  705 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  706 */     IntBuffer params = APIUtil.getBufferInt(caps);
/*  707 */     nglGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
/*  708 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateRenderbuffers(IntBuffer renderbuffers) {
/*  712 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  713 */     long function_pointer = caps.glCreateRenderbuffers;
/*  714 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  715 */     BufferChecks.checkDirect(renderbuffers);
/*  716 */     nglCreateRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateRenderbuffers(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateRenderbuffers() {
/*  722 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  723 */     long function_pointer = caps.glCreateRenderbuffers;
/*  724 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  725 */     IntBuffer renderbuffers = APIUtil.getBufferInt(caps);
/*  726 */     nglCreateRenderbuffers(1, MemoryUtil.getAddress(renderbuffers), function_pointer);
/*  727 */     return renderbuffers.get(0);
/*      */   }
/*      */   
/*      */   public static void glNamedRenderbufferStorage(int renderbuffer, int internalformat, int width, int height) {
/*  731 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  732 */     long function_pointer = caps.glNamedRenderbufferStorage;
/*  733 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  734 */     nglNamedRenderbufferStorage(renderbuffer, internalformat, width, height, function_pointer);
/*      */   }
/*      */   static native void nglNamedRenderbufferStorage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glNamedRenderbufferStorageMultisample(int renderbuffer, int samples, int internalformat, int width, int height) {
/*  739 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  740 */     long function_pointer = caps.glNamedRenderbufferStorageMultisample;
/*  741 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  742 */     nglNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height, function_pointer);
/*      */   }
/*      */   static native void nglNamedRenderbufferStorageMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glGetNamedRenderbufferParameter(int renderbuffer, int pname, IntBuffer params) {
/*  747 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  748 */     long function_pointer = caps.glGetNamedRenderbufferParameteriv;
/*  749 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  750 */     BufferChecks.checkBuffer(params, 1);
/*  751 */     nglGetNamedRenderbufferParameteriv(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetNamedRenderbufferParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetNamedRenderbufferParameter(int renderbuffer, int pname) {
/*  757 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  758 */     long function_pointer = caps.glGetNamedRenderbufferParameteriv;
/*  759 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  760 */     IntBuffer params = APIUtil.getBufferInt(caps);
/*  761 */     nglGetNamedRenderbufferParameteriv(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
/*  762 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateTextures(int target, IntBuffer textures) {
/*  766 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  767 */     long function_pointer = caps.glCreateTextures;
/*  768 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  769 */     BufferChecks.checkDirect(textures);
/*  770 */     nglCreateTextures(target, textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateTextures(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateTextures(int target) {
/*  776 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  777 */     long function_pointer = caps.glCreateTextures;
/*  778 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  779 */     IntBuffer textures = APIUtil.getBufferInt(caps);
/*  780 */     nglCreateTextures(target, 1, MemoryUtil.getAddress(textures), function_pointer);
/*  781 */     return textures.get(0);
/*      */   }
/*      */   
/*      */   public static void glTextureBuffer(int texture, int internalformat, int buffer) {
/*  785 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  786 */     long function_pointer = caps.glTextureBuffer;
/*  787 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  788 */     nglTextureBuffer(texture, internalformat, buffer, function_pointer);
/*      */   }
/*      */   static native void nglTextureBuffer(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTextureBufferRange(int texture, int internalformat, int buffer, long offset, long size) {
/*  793 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  794 */     long function_pointer = caps.glTextureBufferRange;
/*  795 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  796 */     nglTextureBufferRange(texture, internalformat, buffer, offset, size, function_pointer);
/*      */   }
/*      */   static native void nglTextureBufferRange(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glTextureStorage1D(int texture, int levels, int internalformat, int width) {
/*  801 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  802 */     long function_pointer = caps.glTextureStorage1D;
/*  803 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  804 */     nglTextureStorage1D(texture, levels, internalformat, width, function_pointer);
/*      */   }
/*      */   static native void nglTextureStorage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glTextureStorage2D(int texture, int levels, int internalformat, int width, int height) {
/*  809 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  810 */     long function_pointer = caps.glTextureStorage2D;
/*  811 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  812 */     nglTextureStorage2D(texture, levels, internalformat, width, height, function_pointer);
/*      */   }
/*      */   static native void nglTextureStorage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glTextureStorage3D(int texture, int levels, int internalformat, int width, int height, int depth) {
/*  817 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  818 */     long function_pointer = caps.glTextureStorage3D;
/*  819 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  820 */     nglTextureStorage3D(texture, levels, internalformat, width, height, depth, function_pointer);
/*      */   }
/*      */   static native void nglTextureStorage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*      */   
/*      */   public static void glTextureStorage2DMultisample(int texture, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
/*  825 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  826 */     long function_pointer = caps.glTextureStorage2DMultisample;
/*  827 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  828 */     nglTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
/*      */   }
/*      */   static native void nglTextureStorage2DMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glTextureStorage3DMultisample(int texture, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
/*  833 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  834 */     long function_pointer = caps.glTextureStorage3DMultisample;
/*  835 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  836 */     nglTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
/*      */   }
/*      */   static native void nglTextureStorage3DMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
/*  841 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  842 */     long function_pointer = caps.glTextureSubImage1D;
/*  843 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  844 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  845 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/*  846 */     nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
/*  849 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  850 */     long function_pointer = caps.glTextureSubImage1D;
/*  851 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  852 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  853 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/*  854 */     nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
/*  857 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  858 */     long function_pointer = caps.glTextureSubImage1D;
/*  859 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  860 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  861 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/*  862 */     nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
/*  865 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  866 */     long function_pointer = caps.glTextureSubImage1D;
/*  867 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  868 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  869 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/*  870 */     nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
/*  873 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  874 */     long function_pointer = caps.glTextureSubImage1D;
/*  875 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  876 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  877 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/*  878 */     nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
/*  882 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  883 */     long function_pointer = caps.glTextureSubImage1D;
/*  884 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  885 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  886 */     nglTextureSubImage1DBO(texture, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTextureSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   static native void nglTextureSubImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
/*  891 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  892 */     long function_pointer = caps.glTextureSubImage2D;
/*  893 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  894 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  895 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/*  896 */     nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
/*  899 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  900 */     long function_pointer = caps.glTextureSubImage2D;
/*  901 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  902 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  903 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/*  904 */     nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
/*  907 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  908 */     long function_pointer = caps.glTextureSubImage2D;
/*  909 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  910 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  911 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/*  912 */     nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
/*  915 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  916 */     long function_pointer = caps.glTextureSubImage2D;
/*  917 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  918 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  919 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/*  920 */     nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
/*  923 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  924 */     long function_pointer = caps.glTextureSubImage2D;
/*  925 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  926 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  927 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/*  928 */     nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglTextureSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
/*  932 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  933 */     long function_pointer = caps.glTextureSubImage2D;
/*  934 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  935 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  936 */     nglTextureSubImage2DBO(texture, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTextureSubImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
/*  941 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  942 */     long function_pointer = caps.glTextureSubImage3D;
/*  943 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  944 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  945 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
/*  946 */     nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
/*  949 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  950 */     long function_pointer = caps.glTextureSubImage3D;
/*  951 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  952 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  953 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
/*  954 */     nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
/*  957 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  958 */     long function_pointer = caps.glTextureSubImage3D;
/*  959 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  960 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  961 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
/*  962 */     nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
/*  965 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  966 */     long function_pointer = caps.glTextureSubImage3D;
/*  967 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  968 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  969 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
/*  970 */     nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
/*  973 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  974 */     long function_pointer = caps.glTextureSubImage3D;
/*  975 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  976 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  977 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
/*  978 */     nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglTextureSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*      */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
/*  982 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  983 */     long function_pointer = caps.glTextureSubImage3D;
/*  984 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  985 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  986 */     nglTextureSubImage3DBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTextureSubImage3DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, ByteBuffer data) {
/*  991 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  992 */     long function_pointer = caps.glCompressedTextureSubImage1D;
/*  993 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  994 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  995 */     BufferChecks.checkDirect(data);
/*  996 */     nglCompressedTextureSubImage1D(texture, level, xoffset, width, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglCompressedTextureSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   public static void glCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int data_imageSize, long data_buffer_offset) {
/* 1000 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1001 */     long function_pointer = caps.glCompressedTextureSubImage1D;
/* 1002 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1003 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 1004 */     nglCompressedTextureSubImage1DBO(texture, level, xoffset, width, format, data_imageSize, data_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglCompressedTextureSubImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
/* 1009 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1010 */     long function_pointer = caps.glCompressedTextureSubImage2D;
/* 1011 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1012 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 1013 */     BufferChecks.checkDirect(data);
/* 1014 */     nglCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglCompressedTextureSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   public static void glCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data_buffer_offset) {
/* 1018 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1019 */     long function_pointer = caps.glCompressedTextureSubImage2D;
/* 1020 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1021 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 1022 */     nglCompressedTextureSubImage2DBO(texture, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglCompressedTextureSubImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data) {
/* 1027 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1028 */     long function_pointer = caps.glCompressedTextureSubImage3D;
/* 1029 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1030 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 1031 */     BufferChecks.checkDirect(data);
/* 1032 */     nglCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglCompressedTextureSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*      */   public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset) {
/* 1036 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1037 */     long function_pointer = caps.glCompressedTextureSubImage3D;
/* 1038 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1039 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 1040 */     nglCompressedTextureSubImage3DBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglCompressedTextureSubImage3DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCopyTextureSubImage1D(int texture, int level, int xoffset, int x, int y, int width) {
/* 1045 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1046 */     long function_pointer = caps.glCopyTextureSubImage1D;
/* 1047 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1048 */     nglCopyTextureSubImage1D(texture, level, xoffset, x, y, width, function_pointer);
/*      */   }
/*      */   static native void nglCopyTextureSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*      */   
/*      */   public static void glCopyTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
/* 1053 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1054 */     long function_pointer = caps.glCopyTextureSubImage2D;
/* 1055 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1056 */     nglCopyTextureSubImage2D(texture, level, xoffset, yoffset, x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglCopyTextureSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);
/*      */   
/*      */   public static void glCopyTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
/* 1061 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1062 */     long function_pointer = caps.glCopyTextureSubImage3D;
/* 1063 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1064 */     nglCopyTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglCopyTextureSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, long paramLong);
/*      */   
/*      */   public static void glTextureParameterf(int texture, int pname, float param) {
/* 1069 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1070 */     long function_pointer = caps.glTextureParameterf;
/* 1071 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1072 */     nglTextureParameterf(texture, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTextureParameterf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glTextureParameter(int texture, int pname, FloatBuffer params) {
/* 1077 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1078 */     long function_pointer = caps.glTextureParameterfv;
/* 1079 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1080 */     BufferChecks.checkBuffer(params, 4);
/* 1081 */     nglTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTextureParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTextureParameteri(int texture, int pname, int param) {
/* 1086 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1087 */     long function_pointer = caps.glTextureParameteri;
/* 1088 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1089 */     nglTextureParameteri(texture, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTextureParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTextureParameterI(int texture, int pname, IntBuffer params) {
/* 1094 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1095 */     long function_pointer = caps.glTextureParameterIiv;
/* 1096 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1097 */     BufferChecks.checkBuffer(params, 1);
/* 1098 */     nglTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTextureParameterIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTextureParameterIu(int texture, int pname, IntBuffer params) {
/* 1103 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1104 */     long function_pointer = caps.glTextureParameterIuiv;
/* 1105 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1106 */     BufferChecks.checkBuffer(params, 1);
/* 1107 */     nglTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTextureParameterIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTextureParameter(int texture, int pname, IntBuffer params) {
/* 1112 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1113 */     long function_pointer = caps.glTextureParameteriv;
/* 1114 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1115 */     BufferChecks.checkBuffer(params, 4);
/* 1116 */     nglTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTextureParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGenerateTextureMipmap(int texture) {
/* 1121 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1122 */     long function_pointer = caps.glGenerateTextureMipmap;
/* 1123 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1124 */     nglGenerateTextureMipmap(texture, function_pointer);
/*      */   }
/*      */   static native void nglGenerateTextureMipmap(int paramInt, long paramLong);
/*      */   
/*      */   public static void glBindTextureUnit(int unit, int texture) {
/* 1129 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1130 */     long function_pointer = caps.glBindTextureUnit;
/* 1131 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1132 */     nglBindTextureUnit(unit, texture, function_pointer);
/*      */   }
/*      */   static native void nglBindTextureUnit(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glGetTextureImage(int texture, int level, int format, int type, ByteBuffer pixels) {
/* 1137 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1138 */     long function_pointer = caps.glGetTextureImage;
/* 1139 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1140 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1141 */     BufferChecks.checkDirect(pixels);
/* 1142 */     nglGetTextureImage(texture, level, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureImage(int texture, int level, int format, int type, DoubleBuffer pixels) {
/* 1145 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1146 */     long function_pointer = caps.glGetTextureImage;
/* 1147 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1148 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1149 */     BufferChecks.checkDirect(pixels);
/* 1150 */     nglGetTextureImage(texture, level, format, type, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureImage(int texture, int level, int format, int type, FloatBuffer pixels) {
/* 1153 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1154 */     long function_pointer = caps.glGetTextureImage;
/* 1155 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1156 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1157 */     BufferChecks.checkDirect(pixels);
/* 1158 */     nglGetTextureImage(texture, level, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureImage(int texture, int level, int format, int type, IntBuffer pixels) {
/* 1161 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1162 */     long function_pointer = caps.glGetTextureImage;
/* 1163 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1164 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1165 */     BufferChecks.checkDirect(pixels);
/* 1166 */     nglGetTextureImage(texture, level, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureImage(int texture, int level, int format, int type, ShortBuffer pixels) {
/* 1169 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1170 */     long function_pointer = caps.glGetTextureImage;
/* 1171 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1172 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1173 */     BufferChecks.checkDirect(pixels);
/* 1174 */     nglGetTextureImage(texture, level, format, type, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglGetTextureImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   public static void glGetTextureImage(int texture, int level, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
/* 1178 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1179 */     long function_pointer = caps.glGetTextureImage;
/* 1180 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1181 */     GLChecks.ensurePackPBOenabled(caps);
/* 1182 */     nglGetTextureImageBO(texture, level, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetTextureImageBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetCompressedTextureImage(int texture, int level, ByteBuffer pixels) {
/* 1187 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1188 */     long function_pointer = caps.glGetCompressedTextureImage;
/* 1189 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1190 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1191 */     BufferChecks.checkDirect(pixels);
/* 1192 */     nglGetCompressedTextureImage(texture, level, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetCompressedTextureImage(int texture, int level, IntBuffer pixels) {
/* 1195 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1196 */     long function_pointer = caps.glGetCompressedTextureImage;
/* 1197 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1198 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1199 */     BufferChecks.checkDirect(pixels);
/* 1200 */     nglGetCompressedTextureImage(texture, level, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetCompressedTextureImage(int texture, int level, ShortBuffer pixels) {
/* 1203 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1204 */     long function_pointer = caps.glGetCompressedTextureImage;
/* 1205 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1206 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1207 */     BufferChecks.checkDirect(pixels);
/* 1208 */     nglGetCompressedTextureImage(texture, level, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglGetCompressedTextureImage(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glGetCompressedTextureImage(int texture, int level, int pixels_bufSize, long pixels_buffer_offset) {
/* 1212 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1213 */     long function_pointer = caps.glGetCompressedTextureImage;
/* 1214 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1215 */     GLChecks.ensurePackPBOenabled(caps);
/* 1216 */     nglGetCompressedTextureImageBO(texture, level, pixels_bufSize, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetCompressedTextureImageBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetTextureLevelParameter(int texture, int level, int pname, FloatBuffer params) {
/* 1221 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1222 */     long function_pointer = caps.glGetTextureLevelParameterfv;
/* 1223 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1224 */     BufferChecks.checkBuffer(params, 1);
/* 1225 */     nglGetTextureLevelParameterfv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTextureLevelParameterfv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetTextureLevelParameterf(int texture, int level, int pname) {
/* 1231 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1232 */     long function_pointer = caps.glGetTextureLevelParameterfv;
/* 1233 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1234 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1235 */     nglGetTextureLevelParameterfv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1236 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTextureLevelParameter(int texture, int level, int pname, IntBuffer params) {
/* 1240 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1241 */     long function_pointer = caps.glGetTextureLevelParameteriv;
/* 1242 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1243 */     BufferChecks.checkBuffer(params, 1);
/* 1244 */     nglGetTextureLevelParameteriv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTextureLevelParameteriv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTextureLevelParameteri(int texture, int level, int pname) {
/* 1250 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1251 */     long function_pointer = caps.glGetTextureLevelParameteriv;
/* 1252 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1253 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1254 */     nglGetTextureLevelParameteriv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1255 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTextureParameter(int texture, int pname, FloatBuffer params) {
/* 1259 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1260 */     long function_pointer = caps.glGetTextureParameterfv;
/* 1261 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1262 */     BufferChecks.checkBuffer(params, 1);
/* 1263 */     nglGetTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTextureParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetTextureParameterf(int texture, int pname) {
/* 1269 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1270 */     long function_pointer = caps.glGetTextureParameterfv;
/* 1271 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1272 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1273 */     nglGetTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1274 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTextureParameterI(int texture, int pname, IntBuffer params) {
/* 1278 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1279 */     long function_pointer = caps.glGetTextureParameterIiv;
/* 1280 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1281 */     BufferChecks.checkBuffer(params, 1);
/* 1282 */     nglGetTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTextureParameterIiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTextureParameterIi(int texture, int pname) {
/* 1288 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1289 */     long function_pointer = caps.glGetTextureParameterIiv;
/* 1290 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1291 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1292 */     nglGetTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1293 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTextureParameterIu(int texture, int pname, IntBuffer params) {
/* 1297 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1298 */     long function_pointer = caps.glGetTextureParameterIuiv;
/* 1299 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1300 */     BufferChecks.checkBuffer(params, 1);
/* 1301 */     nglGetTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTextureParameterIuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTextureParameterIui(int texture, int pname) {
/* 1307 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1308 */     long function_pointer = caps.glGetTextureParameterIuiv;
/* 1309 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1310 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1311 */     nglGetTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1312 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTextureParameter(int texture, int pname, IntBuffer params) {
/* 1316 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1317 */     long function_pointer = caps.glGetTextureParameteriv;
/* 1318 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1319 */     BufferChecks.checkBuffer(params, 1);
/* 1320 */     nglGetTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTextureParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTextureParameteri(int texture, int pname) {
/* 1326 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1327 */     long function_pointer = caps.glGetTextureParameteriv;
/* 1328 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1329 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1330 */     nglGetTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1331 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateVertexArrays(IntBuffer arrays) {
/* 1335 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1336 */     long function_pointer = caps.glCreateVertexArrays;
/* 1337 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1338 */     BufferChecks.checkDirect(arrays);
/* 1339 */     nglCreateVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateVertexArrays(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateVertexArrays() {
/* 1345 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1346 */     long function_pointer = caps.glCreateVertexArrays;
/* 1347 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1348 */     IntBuffer arrays = APIUtil.getBufferInt(caps);
/* 1349 */     nglCreateVertexArrays(1, MemoryUtil.getAddress(arrays), function_pointer);
/* 1350 */     return arrays.get(0);
/*      */   }
/*      */   
/*      */   public static void glDisableVertexArrayAttrib(int vaobj, int index) {
/* 1354 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1355 */     long function_pointer = caps.glDisableVertexArrayAttrib;
/* 1356 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1357 */     nglDisableVertexArrayAttrib(vaobj, index, function_pointer);
/*      */   }
/*      */   static native void nglDisableVertexArrayAttrib(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glEnableVertexArrayAttrib(int vaobj, int index) {
/* 1362 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1363 */     long function_pointer = caps.glEnableVertexArrayAttrib;
/* 1364 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1365 */     nglEnableVertexArrayAttrib(vaobj, index, function_pointer);
/*      */   }
/*      */   static native void nglEnableVertexArrayAttrib(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glVertexArrayElementBuffer(int vaobj, int buffer) {
/* 1370 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1371 */     long function_pointer = caps.glVertexArrayElementBuffer;
/* 1372 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1373 */     nglVertexArrayElementBuffer(vaobj, buffer, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayElementBuffer(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride) {
/* 1378 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1379 */     long function_pointer = caps.glVertexArrayVertexBuffer;
/* 1380 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1381 */     nglVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayVertexBuffer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, int paramInt4, long paramLong2);
/*      */   
/*      */   public static void glVertexArrayVertexBuffers(int vaobj, int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
/* 1386 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1387 */     long function_pointer = caps.glVertexArrayVertexBuffers;
/* 1388 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1389 */     if (buffers != null)
/* 1390 */       BufferChecks.checkBuffer(buffers, count); 
/* 1391 */     if (offsets != null)
/* 1392 */       BufferChecks.checkBuffer(offsets, count); 
/* 1393 */     if (strides != null)
/* 1394 */       BufferChecks.checkBuffer(strides, count); 
/* 1395 */     nglVertexArrayVertexBuffers(vaobj, first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(strides), function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayVertexBuffers(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static void glVertexArrayAttribFormat(int vaobj, int attribindex, int size, int type, boolean normalized, int relativeoffset) {
/* 1400 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1401 */     long function_pointer = caps.glVertexArrayAttribFormat;
/* 1402 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1403 */     nglVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayAttribFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glVertexArrayAttribIFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
/* 1408 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1409 */     long function_pointer = caps.glVertexArrayAttribIFormat;
/* 1410 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1411 */     nglVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayAttribIFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glVertexArrayAttribLFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
/* 1416 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1417 */     long function_pointer = caps.glVertexArrayAttribLFormat;
/* 1418 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1419 */     nglVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayAttribLFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glVertexArrayAttribBinding(int vaobj, int attribindex, int bindingindex) {
/* 1424 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1425 */     long function_pointer = caps.glVertexArrayAttribBinding;
/* 1426 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1427 */     nglVertexArrayAttribBinding(vaobj, attribindex, bindingindex, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayAttribBinding(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glVertexArrayBindingDivisor(int vaobj, int bindingindex, int divisor) {
/* 1432 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1433 */     long function_pointer = caps.glVertexArrayBindingDivisor;
/* 1434 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1435 */     nglVertexArrayBindingDivisor(vaobj, bindingindex, divisor, function_pointer);
/*      */   }
/*      */   static native void nglVertexArrayBindingDivisor(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glGetVertexArray(int vaobj, int pname, IntBuffer param) {
/* 1440 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1441 */     long function_pointer = caps.glGetVertexArrayiv;
/* 1442 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1443 */     BufferChecks.checkBuffer(param, 1);
/* 1444 */     nglGetVertexArrayiv(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetVertexArrayiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetVertexArray(int vaobj, int pname) {
/* 1450 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1451 */     long function_pointer = caps.glGetVertexArrayiv;
/* 1452 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1453 */     IntBuffer param = APIUtil.getBufferInt(caps);
/* 1454 */     nglGetVertexArrayiv(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
/* 1455 */     return param.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetVertexArrayIndexed(int vaobj, int index, int pname, IntBuffer param) {
/* 1459 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1460 */     long function_pointer = caps.glGetVertexArrayIndexediv;
/* 1461 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1462 */     BufferChecks.checkBuffer(param, 1);
/* 1463 */     nglGetVertexArrayIndexediv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetVertexArrayIndexediv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetVertexArrayIndexed(int vaobj, int index, int pname) {
/* 1469 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1470 */     long function_pointer = caps.glGetVertexArrayIndexediv;
/* 1471 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1472 */     IntBuffer param = APIUtil.getBufferInt(caps);
/* 1473 */     nglGetVertexArrayIndexediv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
/* 1474 */     return param.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetVertexArrayIndexed64i(int vaobj, int index, int pname, LongBuffer param) {
/* 1478 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1479 */     long function_pointer = caps.glGetVertexArrayIndexed64iv;
/* 1480 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1481 */     BufferChecks.checkBuffer(param, 1);
/* 1482 */     nglGetVertexArrayIndexed64iv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetVertexArrayIndexed64iv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static long glGetVertexArrayIndexed64i(int vaobj, int index, int pname) {
/* 1488 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1489 */     long function_pointer = caps.glGetVertexArrayIndexed64iv;
/* 1490 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1491 */     LongBuffer param = APIUtil.getBufferLong(caps);
/* 1492 */     nglGetVertexArrayIndexed64iv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
/* 1493 */     return param.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateSamplers(IntBuffer samplers) {
/* 1497 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1498 */     long function_pointer = caps.glCreateSamplers;
/* 1499 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1500 */     BufferChecks.checkDirect(samplers);
/* 1501 */     nglCreateSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateSamplers(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateSamplers() {
/* 1507 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1508 */     long function_pointer = caps.glCreateSamplers;
/* 1509 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1510 */     IntBuffer samplers = APIUtil.getBufferInt(caps);
/* 1511 */     nglCreateSamplers(1, MemoryUtil.getAddress(samplers), function_pointer);
/* 1512 */     return samplers.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateProgramPipelines(IntBuffer pipelines) {
/* 1516 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1517 */     long function_pointer = caps.glCreateProgramPipelines;
/* 1518 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1519 */     BufferChecks.checkDirect(pipelines);
/* 1520 */     nglCreateProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateProgramPipelines(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateProgramPipelines() {
/* 1526 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1527 */     long function_pointer = caps.glCreateProgramPipelines;
/* 1528 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1529 */     IntBuffer pipelines = APIUtil.getBufferInt(caps);
/* 1530 */     nglCreateProgramPipelines(1, MemoryUtil.getAddress(pipelines), function_pointer);
/* 1531 */     return pipelines.get(0);
/*      */   }
/*      */   
/*      */   public static void glCreateQueries(int target, IntBuffer ids) {
/* 1535 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1536 */     long function_pointer = caps.glCreateQueries;
/* 1537 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1538 */     BufferChecks.checkDirect(ids);
/* 1539 */     nglCreateQueries(target, ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglCreateQueries(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glCreateQueries(int target) {
/* 1545 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1546 */     long function_pointer = caps.glCreateQueries;
/* 1547 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1548 */     IntBuffer ids = APIUtil.getBufferInt(caps);
/* 1549 */     nglCreateQueries(target, 1, MemoryUtil.getAddress(ids), function_pointer);
/* 1550 */     return ids.get(0);
/*      */   }
/*      */   
/*      */   public static void glMemoryBarrierByRegion(int barriers) {
/* 1554 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1555 */     long function_pointer = caps.glMemoryBarrierByRegion;
/* 1556 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1557 */     nglMemoryBarrierByRegion(barriers, function_pointer);
/*      */   }
/*      */   static native void nglMemoryBarrierByRegion(int paramInt, long paramLong);
/*      */   
/*      */   public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
/* 1562 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1563 */     long function_pointer = caps.glGetTextureSubImage;
/* 1564 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1565 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1566 */     BufferChecks.checkDirect(pixels);
/* 1567 */     nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
/* 1570 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1571 */     long function_pointer = caps.glGetTextureSubImage;
/* 1572 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1573 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1574 */     BufferChecks.checkDirect(pixels);
/* 1575 */     nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
/* 1578 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1579 */     long function_pointer = caps.glGetTextureSubImage;
/* 1580 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1581 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1582 */     BufferChecks.checkDirect(pixels);
/* 1583 */     nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
/* 1586 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1587 */     long function_pointer = caps.glGetTextureSubImage;
/* 1588 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1589 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1590 */     BufferChecks.checkDirect(pixels);
/* 1591 */     nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
/* 1594 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1595 */     long function_pointer = caps.glGetTextureSubImage;
/* 1596 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1597 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1598 */     BufferChecks.checkDirect(pixels);
/* 1599 */     nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglGetTextureSubImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, long paramLong1, long paramLong2);
/*      */   public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
/* 1603 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1604 */     long function_pointer = caps.glGetTextureSubImage;
/* 1605 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1606 */     GLChecks.ensurePackPBOenabled(caps);
/* 1607 */     nglGetTextureSubImageBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetTextureSubImageBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, ByteBuffer pixels) {
/* 1612 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1613 */     long function_pointer = caps.glGetCompressedTextureSubImage;
/* 1614 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1615 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1616 */     BufferChecks.checkDirect(pixels);
/* 1617 */     nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, DoubleBuffer pixels) {
/* 1620 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1621 */     long function_pointer = caps.glGetCompressedTextureSubImage;
/* 1622 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1623 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1624 */     BufferChecks.checkDirect(pixels);
/* 1625 */     nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, FloatBuffer pixels) {
/* 1628 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1629 */     long function_pointer = caps.glGetCompressedTextureSubImage;
/* 1630 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1631 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1632 */     BufferChecks.checkDirect(pixels);
/* 1633 */     nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, IntBuffer pixels) {
/* 1636 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1637 */     long function_pointer = caps.glGetCompressedTextureSubImage;
/* 1638 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1639 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1640 */     BufferChecks.checkDirect(pixels);
/* 1641 */     nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, ShortBuffer pixels) {
/* 1644 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1645 */     long function_pointer = caps.glGetCompressedTextureSubImage;
/* 1646 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1647 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1648 */     BufferChecks.checkDirect(pixels);
/* 1649 */     nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglGetCompressedTextureSubImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, long paramLong1, long paramLong2);
/*      */   public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int pixels_bufSize, long pixels_buffer_offset) {
/* 1653 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1654 */     long function_pointer = caps.glGetCompressedTextureSubImage;
/* 1655 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1656 */     GLChecks.ensurePackPBOenabled(caps);
/* 1657 */     nglGetCompressedTextureSubImageBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels_bufSize, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetCompressedTextureSubImageBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTextureBarrier() {
/* 1662 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1663 */     long function_pointer = caps.glTextureBarrier;
/* 1664 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1665 */     nglTextureBarrier(function_pointer);
/*      */   }
/*      */   static native void nglTextureBarrier(long paramLong);
/*      */   
/*      */   public static int glGetGraphicsResetStatus() {
/* 1670 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1671 */     long function_pointer = caps.glGetGraphicsResetStatus;
/* 1672 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1673 */     int __result = nglGetGraphicsResetStatus(function_pointer);
/* 1674 */     return __result;
/*      */   }
/*      */   static native int nglGetGraphicsResetStatus(long paramLong);
/*      */   
/*      */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
/* 1679 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1680 */     long function_pointer = caps.glReadnPixels;
/* 1681 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1682 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1683 */     BufferChecks.checkDirect(pixels);
/* 1684 */     nglReadnPixels(x, y, width, height, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
/* 1687 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1688 */     long function_pointer = caps.glReadnPixels;
/* 1689 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1690 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1691 */     BufferChecks.checkDirect(pixels);
/* 1692 */     nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 3, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
/* 1695 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1696 */     long function_pointer = caps.glReadnPixels;
/* 1697 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1698 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1699 */     BufferChecks.checkDirect(pixels);
/* 1700 */     nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
/* 1703 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1704 */     long function_pointer = caps.glReadnPixels;
/* 1705 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1706 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1707 */     BufferChecks.checkDirect(pixels);
/* 1708 */     nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 2, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
/* 1711 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1712 */     long function_pointer = caps.glReadnPixels;
/* 1713 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1714 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1715 */     BufferChecks.checkDirect(pixels);
/* 1716 */     nglReadnPixels(x, y, width, height, format, type, pixels.remaining() << 1, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglReadnPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*      */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
/* 1720 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1721 */     long function_pointer = caps.glReadnPixels;
/* 1722 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1723 */     GLChecks.ensurePackPBOenabled(caps);
/* 1724 */     nglReadnPixelsBO(x, y, width, height, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglReadnPixelsBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniform(int program, int location, FloatBuffer params) {
/* 1729 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1730 */     long function_pointer = caps.glGetnUniformfv;
/* 1731 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1732 */     BufferChecks.checkDirect(params);
/* 1733 */     nglGetnUniformfv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetnUniformfv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniform(int program, int location, IntBuffer params) {
/* 1738 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1739 */     long function_pointer = caps.glGetnUniformiv;
/* 1740 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1741 */     BufferChecks.checkDirect(params);
/* 1742 */     nglGetnUniformiv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetnUniformiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniformu(int program, int location, IntBuffer params) {
/* 1747 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1748 */     long function_pointer = caps.glGetnUniformuiv;
/* 1749 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1750 */     BufferChecks.checkDirect(params);
/* 1751 */     nglGetnUniformuiv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetnUniformuiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL45.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */