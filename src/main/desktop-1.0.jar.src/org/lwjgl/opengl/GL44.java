/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import org.lwjgl.BufferChecks;
/*     */ import org.lwjgl.MemoryUtil;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GL44
/*     */ {
/*     */   public static final int GL_MAX_VERTEX_ATTRIB_STRIDE = 33509;
/*     */   public static final int GL_MAP_PERSISTENT_BIT = 64;
/*     */   public static final int GL_MAP_COHERENT_BIT = 128;
/*     */   public static final int GL_DYNAMIC_STORAGE_BIT = 256;
/*     */   public static final int GL_CLIENT_STORAGE_BIT = 512;
/*     */   public static final int GL_BUFFER_IMMUTABLE_STORAGE = 33311;
/*     */   public static final int GL_BUFFER_STORAGE_FLAGS = 33312;
/*     */   public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 16384;
/*     */   public static final int GL_CLEAR_TEXTURE = 37733;
/*     */   public static final int GL_LOCATION_COMPONENT = 37706;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_INDEX = 37707;
/*     */   public static final int GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE = 37708;
/*     */   public static final int GL_QUERY_RESULT_NO_WAIT = 37268;
/*     */   public static final int GL_QUERY_BUFFER = 37266;
/*     */   public static final int GL_QUERY_BUFFER_BINDING = 37267;
/*     */   public static final int GL_QUERY_BUFFER_BARRIER_BIT = 32768;
/*     */   public static final int GL_MIRROR_CLAMP_TO_EDGE = 34627;
/*     */   
/*     */   public static void glBufferStorage(int target, ByteBuffer data, int flags) {
/*  86 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  87 */     long function_pointer = caps.glBufferStorage;
/*  88 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  89 */     BufferChecks.checkDirect(data);
/*  90 */     nglBufferStorage(target, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glBufferStorage(int target, DoubleBuffer data, int flags) {
/*  93 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  94 */     long function_pointer = caps.glBufferStorage;
/*  95 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  96 */     BufferChecks.checkDirect(data);
/*  97 */     nglBufferStorage(target, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glBufferStorage(int target, FloatBuffer data, int flags) {
/* 100 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 101 */     long function_pointer = caps.glBufferStorage;
/* 102 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 103 */     BufferChecks.checkDirect(data);
/* 104 */     nglBufferStorage(target, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glBufferStorage(int target, IntBuffer data, int flags) {
/* 107 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 108 */     long function_pointer = caps.glBufferStorage;
/* 109 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 110 */     BufferChecks.checkDirect(data);
/* 111 */     nglBufferStorage(target, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glBufferStorage(int target, ShortBuffer data, int flags) {
/* 114 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 115 */     long function_pointer = caps.glBufferStorage;
/* 116 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 117 */     BufferChecks.checkDirect(data);
/* 118 */     nglBufferStorage(target, (data.remaining() << 1), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glBufferStorage(int target, LongBuffer data, int flags) {
/* 121 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 122 */     long function_pointer = caps.glBufferStorage;
/* 123 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 124 */     BufferChecks.checkDirect(data);
/* 125 */     nglBufferStorage(target, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglBufferStorage(int paramInt1, long paramLong1, long paramLong2, int paramInt2, long paramLong3);
/*     */   
/*     */   public static void glBufferStorage(int target, long size, int flags) {
/* 131 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 132 */     long function_pointer = caps.glBufferStorage;
/* 133 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 134 */     nglBufferStorage(target, size, 0L, flags, function_pointer);
/*     */   }
/*     */   
/*     */   public static void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
/* 138 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 139 */     long function_pointer = caps.glClearTexImage;
/* 140 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 141 */     if (data != null)
/* 142 */       BufferChecks.checkBuffer(data, 1); 
/* 143 */     nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexImage(int texture, int level, int format, int type, DoubleBuffer data) {
/* 146 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 147 */     long function_pointer = caps.glClearTexImage;
/* 148 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 149 */     if (data != null)
/* 150 */       BufferChecks.checkBuffer(data, 1); 
/* 151 */     nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexImage(int texture, int level, int format, int type, FloatBuffer data) {
/* 154 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 155 */     long function_pointer = caps.glClearTexImage;
/* 156 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 157 */     if (data != null)
/* 158 */       BufferChecks.checkBuffer(data, 1); 
/* 159 */     nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexImage(int texture, int level, int format, int type, IntBuffer data) {
/* 162 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 163 */     long function_pointer = caps.glClearTexImage;
/* 164 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 165 */     if (data != null)
/* 166 */       BufferChecks.checkBuffer(data, 1); 
/* 167 */     nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexImage(int texture, int level, int format, int type, ShortBuffer data) {
/* 170 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 171 */     long function_pointer = caps.glClearTexImage;
/* 172 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 173 */     if (data != null)
/* 174 */       BufferChecks.checkBuffer(data, 1); 
/* 175 */     nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexImage(int texture, int level, int format, int type, LongBuffer data) {
/* 178 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 179 */     long function_pointer = caps.glClearTexImage;
/* 180 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 181 */     if (data != null)
/* 182 */       BufferChecks.checkBuffer(data, 1); 
/* 183 */     nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   static native void nglClearTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
/* 188 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 189 */     long function_pointer = caps.glClearTexSubImage;
/* 190 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 191 */     if (data != null)
/* 192 */       BufferChecks.checkBuffer(data, 1); 
/* 193 */     nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer data) {
/* 196 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 197 */     long function_pointer = caps.glClearTexSubImage;
/* 198 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 199 */     if (data != null)
/* 200 */       BufferChecks.checkBuffer(data, 1); 
/* 201 */     nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer data) {
/* 204 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 205 */     long function_pointer = caps.glClearTexSubImage;
/* 206 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 207 */     if (data != null)
/* 208 */       BufferChecks.checkBuffer(data, 1); 
/* 209 */     nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer data) {
/* 212 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 213 */     long function_pointer = caps.glClearTexSubImage;
/* 214 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 215 */     if (data != null)
/* 216 */       BufferChecks.checkBuffer(data, 1); 
/* 217 */     nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer data) {
/* 220 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 221 */     long function_pointer = caps.glClearTexSubImage;
/* 222 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 223 */     if (data != null)
/* 224 */       BufferChecks.checkBuffer(data, 1); 
/* 225 */     nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, LongBuffer data) {
/* 228 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 229 */     long function_pointer = caps.glClearTexSubImage;
/* 230 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 231 */     if (data != null)
/* 232 */       BufferChecks.checkBuffer(data, 1); 
/* 233 */     nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
/*     */   }
/*     */   static native void nglClearTexSubImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindBuffersBase(int target, int first, int count, IntBuffer buffers) {
/* 238 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 239 */     long function_pointer = caps.glBindBuffersBase;
/* 240 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 241 */     if (buffers != null)
/* 242 */       BufferChecks.checkBuffer(buffers, count); 
/* 243 */     nglBindBuffersBase(target, first, count, MemoryUtil.getAddressSafe(buffers), function_pointer);
/*     */   }
/*     */   static native void nglBindBuffersBase(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindBuffersRange(int target, int first, int count, IntBuffer buffers, PointerBuffer offsets, PointerBuffer sizes) {
/* 248 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 249 */     long function_pointer = caps.glBindBuffersRange;
/* 250 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 251 */     if (buffers != null)
/* 252 */       BufferChecks.checkBuffer(buffers, count); 
/* 253 */     if (offsets != null)
/* 254 */       BufferChecks.checkBuffer(offsets, count); 
/* 255 */     if (sizes != null)
/* 256 */       BufferChecks.checkBuffer(sizes, count); 
/* 257 */     nglBindBuffersRange(target, first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(sizes), function_pointer);
/*     */   }
/*     */   static native void nglBindBuffersRange(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*     */   
/*     */   public static void glBindTextures(int first, int count, IntBuffer textures) {
/* 262 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 263 */     long function_pointer = caps.glBindTextures;
/* 264 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 265 */     if (textures != null)
/* 266 */       BufferChecks.checkBuffer(textures, count); 
/* 267 */     nglBindTextures(first, count, MemoryUtil.getAddressSafe(textures), function_pointer);
/*     */   }
/*     */   static native void nglBindTextures(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindSamplers(int first, int count, IntBuffer samplers) {
/* 272 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 273 */     long function_pointer = caps.glBindSamplers;
/* 274 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 275 */     if (samplers != null)
/* 276 */       BufferChecks.checkBuffer(samplers, count); 
/* 277 */     nglBindSamplers(first, count, MemoryUtil.getAddressSafe(samplers), function_pointer);
/*     */   }
/*     */   static native void nglBindSamplers(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindImageTextures(int first, int count, IntBuffer textures) {
/* 282 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 283 */     long function_pointer = caps.glBindImageTextures;
/* 284 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 285 */     if (textures != null)
/* 286 */       BufferChecks.checkBuffer(textures, count); 
/* 287 */     nglBindImageTextures(first, count, MemoryUtil.getAddressSafe(textures), function_pointer);
/*     */   }
/*     */   static native void nglBindImageTextures(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glBindVertexBuffers(int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
/* 292 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 293 */     long function_pointer = caps.glBindVertexBuffers;
/* 294 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 295 */     if (buffers != null)
/* 296 */       BufferChecks.checkBuffer(buffers, count); 
/* 297 */     if (offsets != null)
/* 298 */       BufferChecks.checkBuffer(offsets, count); 
/* 299 */     if (strides != null)
/* 300 */       BufferChecks.checkBuffer(strides, count); 
/* 301 */     nglBindVertexBuffers(first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(strides), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglBindVertexBuffers(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL44.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */