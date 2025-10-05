/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
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
/*     */ public final class ARBDirectStateAccess
/*     */ {
/*     */   public static final int GL_TEXTURE_TARGET = 4102;
/*     */   public static final int GL_QUERY_TARGET = 33514;
/*     */   public static final int GL_TEXTURE_BINDING = 33515;
/*     */   
/*     */   public static void glCreateTransformFeedbacks(IntBuffer ids) {
/*  29 */     GL45.glCreateTransformFeedbacks(ids);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateTransformFeedbacks() {
/*  34 */     return GL45.glCreateTransformFeedbacks();
/*     */   }
/*     */   
/*     */   public static void glTransformFeedbackBufferBase(int xfb, int index, int buffer) {
/*  38 */     GL45.glTransformFeedbackBufferBase(xfb, index, buffer);
/*     */   }
/*     */   
/*     */   public static void glTransformFeedbackBufferRange(int xfb, int index, int buffer, long offset, long size) {
/*  42 */     GL45.glTransformFeedbackBufferRange(xfb, index, buffer, offset, size);
/*     */   }
/*     */   
/*     */   public static void glGetTransformFeedback(int xfb, int pname, IntBuffer param) {
/*  46 */     GL45.glGetTransformFeedback(xfb, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetTransformFeedbacki(int xfb, int pname) {
/*  51 */     return GL45.glGetTransformFeedbacki(xfb, pname);
/*     */   }
/*     */   
/*     */   public static void glGetTransformFeedback(int xfb, int pname, int index, IntBuffer param) {
/*  55 */     GL45.glGetTransformFeedback(xfb, pname, index, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetTransformFeedbacki(int xfb, int pname, int index) {
/*  60 */     return GL45.glGetTransformFeedbacki(xfb, pname, index);
/*     */   }
/*     */   
/*     */   public static void glGetTransformFeedback(int xfb, int pname, int index, LongBuffer param) {
/*  64 */     GL45.glGetTransformFeedback(xfb, pname, index, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long glGetTransformFeedbacki64(int xfb, int pname, int index) {
/*  69 */     return GL45.glGetTransformFeedbacki64(xfb, pname, index);
/*     */   }
/*     */   
/*     */   public static void glCreateBuffers(IntBuffer buffers) {
/*  73 */     GL45.glCreateBuffers(buffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateBuffers() {
/*  78 */     return GL45.glCreateBuffers();
/*     */   }
/*     */   
/*     */   public static void glNamedBufferStorage(int buffer, ByteBuffer data, int flags) {
/*  82 */     GL45.glNamedBufferStorage(buffer, data, flags);
/*     */   }
/*     */   public static void glNamedBufferStorage(int buffer, DoubleBuffer data, int flags) {
/*  85 */     GL45.glNamedBufferStorage(buffer, data, flags);
/*     */   }
/*     */   public static void glNamedBufferStorage(int buffer, FloatBuffer data, int flags) {
/*  88 */     GL45.glNamedBufferStorage(buffer, data, flags);
/*     */   }
/*     */   public static void glNamedBufferStorage(int buffer, IntBuffer data, int flags) {
/*  91 */     GL45.glNamedBufferStorage(buffer, data, flags);
/*     */   }
/*     */   public static void glNamedBufferStorage(int buffer, ShortBuffer data, int flags) {
/*  94 */     GL45.glNamedBufferStorage(buffer, data, flags);
/*     */   }
/*     */   public static void glNamedBufferStorage(int buffer, LongBuffer data, int flags) {
/*  97 */     GL45.glNamedBufferStorage(buffer, data, flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glNamedBufferStorage(int buffer, long size, int flags) {
/* 102 */     GL45.glNamedBufferStorage(buffer, size, flags);
/*     */   }
/*     */   
/*     */   public static void glNamedBufferData(int buffer, long data_size, int usage) {
/* 106 */     GL45.glNamedBufferData(buffer, data_size, usage);
/*     */   }
/*     */   public static void glNamedBufferData(int buffer, ByteBuffer data, int usage) {
/* 109 */     GL45.glNamedBufferData(buffer, data, usage);
/*     */   }
/*     */   public static void glNamedBufferData(int buffer, DoubleBuffer data, int usage) {
/* 112 */     GL45.glNamedBufferData(buffer, data, usage);
/*     */   }
/*     */   public static void glNamedBufferData(int buffer, FloatBuffer data, int usage) {
/* 115 */     GL45.glNamedBufferData(buffer, data, usage);
/*     */   }
/*     */   public static void glNamedBufferData(int buffer, IntBuffer data, int usage) {
/* 118 */     GL45.glNamedBufferData(buffer, data, usage);
/*     */   }
/*     */   public static void glNamedBufferData(int buffer, ShortBuffer data, int usage) {
/* 121 */     GL45.glNamedBufferData(buffer, data, usage);
/*     */   }
/*     */   
/*     */   public static void glNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
/* 125 */     GL45.glNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glNamedBufferSubData(int buffer, long offset, DoubleBuffer data) {
/* 128 */     GL45.glNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glNamedBufferSubData(int buffer, long offset, FloatBuffer data) {
/* 131 */     GL45.glNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glNamedBufferSubData(int buffer, long offset, IntBuffer data) {
/* 134 */     GL45.glNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glNamedBufferSubData(int buffer, long offset, ShortBuffer data) {
/* 137 */     GL45.glNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   
/*     */   public static void glCopyNamedBufferSubData(int readBuffer, int writeBuffer, long readOffset, long writeOffset, long size) {
/* 141 */     GL45.glCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size);
/*     */   }
/*     */   
/*     */   public static void glClearNamedBufferData(int buffer, int internalformat, int format, int type, ByteBuffer data) {
/* 145 */     GL45.glClearNamedBufferData(buffer, internalformat, format, type, data);
/*     */   }
/*     */   
/*     */   public static void glClearNamedBufferSubData(int buffer, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
/* 149 */     GL45.glClearNamedBufferSubData(buffer, internalformat, offset, size, format, type, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer glMapNamedBuffer(int buffer, int access, ByteBuffer old_buffer) {
/* 165 */     return GL45.glMapNamedBuffer(buffer, access, old_buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuffer glMapNamedBuffer(int buffer, int access, long length, ByteBuffer old_buffer) {
/* 180 */     return GL45.glMapNamedBuffer(buffer, access, length, old_buffer);
/*     */   }
/*     */   
/*     */   public static ByteBuffer glMapNamedBufferRange(int buffer, long offset, long length, int access, ByteBuffer old_buffer) {
/* 184 */     return GL45.glMapNamedBufferRange(buffer, offset, length, access, old_buffer);
/*     */   }
/*     */   
/*     */   public static boolean glUnmapNamedBuffer(int buffer) {
/* 188 */     return GL45.glUnmapNamedBuffer(buffer);
/*     */   }
/*     */   
/*     */   public static void glFlushMappedNamedBufferRange(int buffer, long offset, long length) {
/* 192 */     GL45.glFlushMappedNamedBufferRange(buffer, offset, length);
/*     */   }
/*     */   
/*     */   public static void glGetNamedBufferParameter(int buffer, int pname, IntBuffer params) {
/* 196 */     GL45.glGetNamedBufferParameter(buffer, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetNamedBufferParameteri(int buffer, int pname) {
/* 201 */     return GL45.glGetNamedBufferParameteri(buffer, pname);
/*     */   }
/*     */   
/*     */   public static void glGetNamedBufferParameter(int buffer, int pname, LongBuffer params) {
/* 205 */     GL45.glGetNamedBufferParameter(buffer, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long glGetNamedBufferParameteri64(int buffer, int pname) {
/* 210 */     return GL45.glGetNamedBufferParameteri64(buffer, pname);
/*     */   }
/*     */   
/*     */   public static ByteBuffer glGetNamedBufferPointer(int buffer, int pname) {
/* 214 */     return GL45.glGetNamedBufferPointer(buffer, pname);
/*     */   }
/*     */   
/*     */   public static void glGetNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
/* 218 */     GL45.glGetNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glGetNamedBufferSubData(int buffer, long offset, DoubleBuffer data) {
/* 221 */     GL45.glGetNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glGetNamedBufferSubData(int buffer, long offset, FloatBuffer data) {
/* 224 */     GL45.glGetNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glGetNamedBufferSubData(int buffer, long offset, IntBuffer data) {
/* 227 */     GL45.glGetNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   public static void glGetNamedBufferSubData(int buffer, long offset, ShortBuffer data) {
/* 230 */     GL45.glGetNamedBufferSubData(buffer, offset, data);
/*     */   }
/*     */   
/*     */   public static void glCreateFramebuffers(IntBuffer framebuffers) {
/* 234 */     GL45.glCreateFramebuffers(framebuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateFramebuffers() {
/* 239 */     return GL45.glCreateFramebuffers();
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferRenderbuffer(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer) {
/* 243 */     GL45.glNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer);
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferParameteri(int framebuffer, int pname, int param) {
/* 247 */     GL45.glNamedFramebufferParameteri(framebuffer, pname, param);
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferTexture(int framebuffer, int attachment, int texture, int level) {
/* 251 */     GL45.glNamedFramebufferTexture(framebuffer, attachment, texture, level);
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferTextureLayer(int framebuffer, int attachment, int texture, int level, int layer) {
/* 255 */     GL45.glNamedFramebufferTextureLayer(framebuffer, attachment, texture, level, layer);
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferDrawBuffer(int framebuffer, int mode) {
/* 259 */     GL45.glNamedFramebufferDrawBuffer(framebuffer, mode);
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferDrawBuffers(int framebuffer, IntBuffer bufs) {
/* 263 */     GL45.glNamedFramebufferDrawBuffers(framebuffer, bufs);
/*     */   }
/*     */   
/*     */   public static void glNamedFramebufferReadBuffer(int framebuffer, int mode) {
/* 267 */     GL45.glNamedFramebufferReadBuffer(framebuffer, mode);
/*     */   }
/*     */   
/*     */   public static void glInvalidateNamedFramebufferData(int framebuffer, IntBuffer attachments) {
/* 271 */     GL45.glInvalidateNamedFramebufferData(framebuffer, attachments);
/*     */   }
/*     */   
/*     */   public static void glInvalidateNamedFramebufferSubData(int framebuffer, IntBuffer attachments, int x, int y, int width, int height) {
/* 275 */     GL45.glInvalidateNamedFramebufferSubData(framebuffer, attachments, x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
/* 279 */     GL45.glClearNamedFramebuffer(framebuffer, buffer, drawbuffer, value);
/*     */   }
/*     */   
/*     */   public static void glClearNamedFramebufferu(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
/* 283 */     GL45.glClearNamedFramebufferu(framebuffer, buffer, drawbuffer, value);
/*     */   }
/*     */   
/*     */   public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, FloatBuffer value) {
/* 287 */     GL45.glClearNamedFramebuffer(framebuffer, buffer, drawbuffer, value);
/*     */   }
/*     */   
/*     */   public static void glClearNamedFramebufferfi(int framebuffer, int buffer, float depth, int stencil) {
/* 291 */     GL45.glClearNamedFramebufferfi(framebuffer, buffer, depth, stencil);
/*     */   }
/*     */   
/*     */   public static void glBlitNamedFramebuffer(int readFramebuffer, int drawFramebuffer, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
/* 295 */     GL45.glBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
/*     */   }
/*     */   
/*     */   public static int glCheckNamedFramebufferStatus(int framebuffer, int target) {
/* 299 */     return GL45.glCheckNamedFramebufferStatus(framebuffer, target);
/*     */   }
/*     */   
/*     */   public static void glGetNamedFramebufferParameter(int framebuffer, int pname, IntBuffer params) {
/* 303 */     GL45.glGetNamedFramebufferParameter(framebuffer, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetNamedFramebufferParameter(int framebuffer, int pname) {
/* 308 */     return GL45.glGetNamedFramebufferParameter(framebuffer, pname);
/*     */   }
/*     */   
/*     */   public static void glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname, IntBuffer params) {
/* 312 */     GL45.glGetNamedFramebufferAttachmentParameter(framebuffer, attachment, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname) {
/* 317 */     return GL45.glGetNamedFramebufferAttachmentParameter(framebuffer, attachment, pname);
/*     */   }
/*     */   
/*     */   public static void glCreateRenderbuffers(IntBuffer renderbuffers) {
/* 321 */     GL45.glCreateRenderbuffers(renderbuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateRenderbuffers() {
/* 326 */     return GL45.glCreateRenderbuffers();
/*     */   }
/*     */   
/*     */   public static void glNamedRenderbufferStorage(int renderbuffer, int internalformat, int width, int height) {
/* 330 */     GL45.glNamedRenderbufferStorage(renderbuffer, internalformat, width, height);
/*     */   }
/*     */   
/*     */   public static void glNamedRenderbufferStorageMultisample(int renderbuffer, int samples, int internalformat, int width, int height) {
/* 334 */     GL45.glNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height);
/*     */   }
/*     */   
/*     */   public static void glGetNamedRenderbufferParameter(int renderbuffer, int pname, IntBuffer params) {
/* 338 */     GL45.glGetNamedRenderbufferParameter(renderbuffer, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetNamedRenderbufferParameter(int renderbuffer, int pname) {
/* 343 */     return GL45.glGetNamedRenderbufferParameter(renderbuffer, pname);
/*     */   }
/*     */   
/*     */   public static void glCreateTextures(int target, IntBuffer textures) {
/* 347 */     GL45.glCreateTextures(target, textures);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateTextures(int target) {
/* 352 */     return GL45.glCreateTextures(target);
/*     */   }
/*     */   
/*     */   public static void glTextureBuffer(int texture, int internalformat, int buffer) {
/* 356 */     GL45.glTextureBuffer(texture, internalformat, buffer);
/*     */   }
/*     */   
/*     */   public static void glTextureBufferRange(int texture, int internalformat, int buffer, long offset, long size) {
/* 360 */     GL45.glTextureBufferRange(texture, internalformat, buffer, offset, size);
/*     */   }
/*     */   
/*     */   public static void glTextureStorage1D(int texture, int levels, int internalformat, int width) {
/* 364 */     GL45.glTextureStorage1D(texture, levels, internalformat, width);
/*     */   }
/*     */   
/*     */   public static void glTextureStorage2D(int texture, int levels, int internalformat, int width, int height) {
/* 368 */     GL45.glTextureStorage2D(texture, levels, internalformat, width, height);
/*     */   }
/*     */   
/*     */   public static void glTextureStorage3D(int texture, int levels, int internalformat, int width, int height, int depth) {
/* 372 */     GL45.glTextureStorage3D(texture, levels, internalformat, width, height, depth);
/*     */   }
/*     */   
/*     */   public static void glTextureStorage2DMultisample(int texture, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
/* 376 */     GL45.glTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations);
/*     */   }
/*     */   
/*     */   public static void glTextureStorage3DMultisample(int texture, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
/* 380 */     GL45.glTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations);
/*     */   }
/*     */   
/*     */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
/* 384 */     GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
/* 387 */     GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
/* 390 */     GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
/* 393 */     GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
/* 396 */     GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
/* 399 */     GL45.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
/* 403 */     GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
/* 406 */     GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
/* 409 */     GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
/* 412 */     GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
/* 415 */     GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
/* 418 */     GL45.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
/* 422 */     GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
/* 425 */     GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
/* 428 */     GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
/* 431 */     GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
/* 434 */     GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
/*     */   }
/*     */   public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
/* 437 */     GL45.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, ByteBuffer data) {
/* 441 */     GL45.glCompressedTextureSubImage1D(texture, level, xoffset, width, format, data);
/*     */   }
/*     */   public static void glCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int data_imageSize, long data_buffer_offset) {
/* 444 */     GL45.glCompressedTextureSubImage1D(texture, level, xoffset, width, format, data_imageSize, data_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
/* 448 */     GL45.glCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data);
/*     */   }
/*     */   public static void glCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data_buffer_offset) {
/* 451 */     GL45.glCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data) {
/* 455 */     GL45.glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data);
/*     */   }
/*     */   public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset) {
/* 458 */     GL45.glCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glCopyTextureSubImage1D(int texture, int level, int xoffset, int x, int y, int width) {
/* 462 */     GL45.glCopyTextureSubImage1D(texture, level, xoffset, x, y, width);
/*     */   }
/*     */   
/*     */   public static void glCopyTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
/* 466 */     GL45.glCopyTextureSubImage2D(texture, level, xoffset, yoffset, x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void glCopyTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
/* 470 */     GL45.glCopyTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void glTextureParameterf(int texture, int pname, float param) {
/* 474 */     GL45.glTextureParameterf(texture, pname, param);
/*     */   }
/*     */   
/*     */   public static void glTextureParameter(int texture, int pname, FloatBuffer params) {
/* 478 */     GL45.glTextureParameter(texture, pname, params);
/*     */   }
/*     */   
/*     */   public static void glTextureParameteri(int texture, int pname, int param) {
/* 482 */     GL45.glTextureParameteri(texture, pname, param);
/*     */   }
/*     */   
/*     */   public static void glTextureParameterI(int texture, int pname, IntBuffer params) {
/* 486 */     GL45.glTextureParameterI(texture, pname, params);
/*     */   }
/*     */   
/*     */   public static void glTextureParameterIu(int texture, int pname, IntBuffer params) {
/* 490 */     GL45.glTextureParameterIu(texture, pname, params);
/*     */   }
/*     */   
/*     */   public static void glTextureParameter(int texture, int pname, IntBuffer params) {
/* 494 */     GL45.glTextureParameter(texture, pname, params);
/*     */   }
/*     */   
/*     */   public static void glGenerateTextureMipmap(int texture) {
/* 498 */     GL45.glGenerateTextureMipmap(texture);
/*     */   }
/*     */   
/*     */   public static void glBindTextureUnit(int unit, int texture) {
/* 502 */     GL45.glBindTextureUnit(unit, texture);
/*     */   }
/*     */   
/*     */   public static void glGetTextureImage(int texture, int level, int format, int type, ByteBuffer pixels) {
/* 506 */     GL45.glGetTextureImage(texture, level, format, type, pixels);
/*     */   }
/*     */   public static void glGetTextureImage(int texture, int level, int format, int type, DoubleBuffer pixels) {
/* 509 */     GL45.glGetTextureImage(texture, level, format, type, pixels);
/*     */   }
/*     */   public static void glGetTextureImage(int texture, int level, int format, int type, FloatBuffer pixels) {
/* 512 */     GL45.glGetTextureImage(texture, level, format, type, pixels);
/*     */   }
/*     */   public static void glGetTextureImage(int texture, int level, int format, int type, IntBuffer pixels) {
/* 515 */     GL45.glGetTextureImage(texture, level, format, type, pixels);
/*     */   }
/*     */   public static void glGetTextureImage(int texture, int level, int format, int type, ShortBuffer pixels) {
/* 518 */     GL45.glGetTextureImage(texture, level, format, type, pixels);
/*     */   }
/*     */   public static void glGetTextureImage(int texture, int level, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
/* 521 */     GL45.glGetTextureImage(texture, level, format, type, pixels_bufSize, pixels_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glGetCompressedTextureImage(int texture, int level, ByteBuffer pixels) {
/* 525 */     GL45.glGetCompressedTextureImage(texture, level, pixels);
/*     */   }
/*     */   public static void glGetCompressedTextureImage(int texture, int level, IntBuffer pixels) {
/* 528 */     GL45.glGetCompressedTextureImage(texture, level, pixels);
/*     */   }
/*     */   public static void glGetCompressedTextureImage(int texture, int level, ShortBuffer pixels) {
/* 531 */     GL45.glGetCompressedTextureImage(texture, level, pixels);
/*     */   }
/*     */   public static void glGetCompressedTextureImage(int texture, int level, int pixels_bufSize, long pixels_buffer_offset) {
/* 534 */     GL45.glGetCompressedTextureImage(texture, level, pixels_bufSize, pixels_buffer_offset);
/*     */   }
/*     */   
/*     */   public static void glGetTextureLevelParameter(int texture, int level, int pname, FloatBuffer params) {
/* 538 */     GL45.glGetTextureLevelParameter(texture, level, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float glGetTextureLevelParameterf(int texture, int level, int pname) {
/* 543 */     return GL45.glGetTextureLevelParameterf(texture, level, pname);
/*     */   }
/*     */   
/*     */   public static void glGetTextureLevelParameter(int texture, int level, int pname, IntBuffer params) {
/* 547 */     GL45.glGetTextureLevelParameter(texture, level, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetTextureLevelParameteri(int texture, int level, int pname) {
/* 552 */     return GL45.glGetTextureLevelParameteri(texture, level, pname);
/*     */   }
/*     */   
/*     */   public static void glGetTextureParameter(int texture, int pname, FloatBuffer params) {
/* 556 */     GL45.glGetTextureParameter(texture, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float glGetTextureParameterf(int texture, int pname) {
/* 561 */     return GL45.glGetTextureParameterf(texture, pname);
/*     */   }
/*     */   
/*     */   public static void glGetTextureParameterI(int texture, int pname, IntBuffer params) {
/* 565 */     GL45.glGetTextureParameterI(texture, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetTextureParameterIi(int texture, int pname) {
/* 570 */     return GL45.glGetTextureParameterIi(texture, pname);
/*     */   }
/*     */   
/*     */   public static void glGetTextureParameterIu(int texture, int pname, IntBuffer params) {
/* 574 */     GL45.glGetTextureParameterIu(texture, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetTextureParameterIui(int texture, int pname) {
/* 579 */     return GL45.glGetTextureParameterIui(texture, pname);
/*     */   }
/*     */   
/*     */   public static void glGetTextureParameter(int texture, int pname, IntBuffer params) {
/* 583 */     GL45.glGetTextureParameter(texture, pname, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetTextureParameteri(int texture, int pname) {
/* 588 */     return GL45.glGetTextureParameteri(texture, pname);
/*     */   }
/*     */   
/*     */   public static void glCreateVertexArrays(IntBuffer arrays) {
/* 592 */     GL45.glCreateVertexArrays(arrays);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateVertexArrays() {
/* 597 */     return GL45.glCreateVertexArrays();
/*     */   }
/*     */   
/*     */   public static void glDisableVertexArrayAttrib(int vaobj, int index) {
/* 601 */     GL45.glDisableVertexArrayAttrib(vaobj, index);
/*     */   }
/*     */   
/*     */   public static void glEnableVertexArrayAttrib(int vaobj, int index) {
/* 605 */     GL45.glEnableVertexArrayAttrib(vaobj, index);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayElementBuffer(int vaobj, int buffer) {
/* 609 */     GL45.glVertexArrayElementBuffer(vaobj, buffer);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride) {
/* 613 */     GL45.glVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayVertexBuffers(int vaobj, int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
/* 617 */     GL45.glVertexArrayVertexBuffers(vaobj, first, count, buffers, offsets, strides);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayAttribFormat(int vaobj, int attribindex, int size, int type, boolean normalized, int relativeoffset) {
/* 621 */     GL45.glVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayAttribIFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
/* 625 */     GL45.glVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayAttribLFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
/* 629 */     GL45.glVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayAttribBinding(int vaobj, int attribindex, int bindingindex) {
/* 633 */     GL45.glVertexArrayAttribBinding(vaobj, attribindex, bindingindex);
/*     */   }
/*     */   
/*     */   public static void glVertexArrayBindingDivisor(int vaobj, int bindingindex, int divisor) {
/* 637 */     GL45.glVertexArrayBindingDivisor(vaobj, bindingindex, divisor);
/*     */   }
/*     */   
/*     */   public static void glGetVertexArray(int vaobj, int pname, IntBuffer param) {
/* 641 */     GL45.glGetVertexArray(vaobj, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetVertexArray(int vaobj, int pname) {
/* 646 */     return GL45.glGetVertexArray(vaobj, pname);
/*     */   }
/*     */   
/*     */   public static void glGetVertexArrayIndexed(int vaobj, int index, int pname, IntBuffer param) {
/* 650 */     GL45.glGetVertexArrayIndexed(vaobj, index, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetVertexArrayIndexed(int vaobj, int index, int pname) {
/* 655 */     return GL45.glGetVertexArrayIndexed(vaobj, index, pname);
/*     */   }
/*     */   
/*     */   public static void glGetVertexArrayIndexed64i(int vaobj, int index, int pname, LongBuffer param) {
/* 659 */     GL45.glGetVertexArrayIndexed64i(vaobj, index, pname, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long glGetVertexArrayIndexed64i(int vaobj, int index, int pname) {
/* 664 */     return GL45.glGetVertexArrayIndexed64i(vaobj, index, pname);
/*     */   }
/*     */   
/*     */   public static void glCreateSamplers(IntBuffer samplers) {
/* 668 */     GL45.glCreateSamplers(samplers);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateSamplers() {
/* 673 */     return GL45.glCreateSamplers();
/*     */   }
/*     */   
/*     */   public static void glCreateProgramPipelines(IntBuffer pipelines) {
/* 677 */     GL45.glCreateProgramPipelines(pipelines);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateProgramPipelines() {
/* 682 */     return GL45.glCreateProgramPipelines();
/*     */   }
/*     */   
/*     */   public static void glCreateQueries(int target, IntBuffer ids) {
/* 686 */     GL45.glCreateQueries(target, ids);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateQueries(int target) {
/* 691 */     return GL45.glCreateQueries(target);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBDirectStateAccess.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */