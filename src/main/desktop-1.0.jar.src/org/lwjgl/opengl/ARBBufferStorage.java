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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ARBBufferStorage
/*     */ {
/*     */   public static final int GL_MAP_PERSISTENT_BIT = 64;
/*     */   public static final int GL_MAP_COHERENT_BIT = 128;
/*     */   public static final int GL_DYNAMIC_STORAGE_BIT = 256;
/*     */   public static final int GL_CLIENT_STORAGE_BIT = 512;
/*     */   public static final int GL_BUFFER_IMMUTABLE_STORAGE = 33311;
/*     */   public static final int GL_BUFFER_STORAGE_FLAGS = 33312;
/*     */   public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 16384;
/*     */   
/*     */   public static void glBufferStorage(int target, ByteBuffer data, int flags) {
/*  33 */     GL44.glBufferStorage(target, data, flags);
/*     */   }
/*     */   public static void glBufferStorage(int target, DoubleBuffer data, int flags) {
/*  36 */     GL44.glBufferStorage(target, data, flags);
/*     */   }
/*     */   public static void glBufferStorage(int target, FloatBuffer data, int flags) {
/*  39 */     GL44.glBufferStorage(target, data, flags);
/*     */   }
/*     */   public static void glBufferStorage(int target, IntBuffer data, int flags) {
/*  42 */     GL44.glBufferStorage(target, data, flags);
/*     */   }
/*     */   public static void glBufferStorage(int target, ShortBuffer data, int flags) {
/*  45 */     GL44.glBufferStorage(target, data, flags);
/*     */   }
/*     */   public static void glBufferStorage(int target, LongBuffer data, int flags) {
/*  48 */     GL44.glBufferStorage(target, data, flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBufferStorage(int target, long size, int flags) {
/*  53 */     GL44.glBufferStorage(target, size, flags);
/*     */   }
/*     */   
/*     */   public static void glNamedBufferStorageEXT(int buffer, ByteBuffer data, int flags) {
/*  57 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  58 */     long function_pointer = caps.glNamedBufferStorageEXT;
/*  59 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  60 */     BufferChecks.checkDirect(data);
/*  61 */     nglNamedBufferStorageEXT(buffer, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glNamedBufferStorageEXT(int buffer, DoubleBuffer data, int flags) {
/*  64 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  65 */     long function_pointer = caps.glNamedBufferStorageEXT;
/*  66 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  67 */     BufferChecks.checkDirect(data);
/*  68 */     nglNamedBufferStorageEXT(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glNamedBufferStorageEXT(int buffer, FloatBuffer data, int flags) {
/*  71 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  72 */     long function_pointer = caps.glNamedBufferStorageEXT;
/*  73 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  74 */     BufferChecks.checkDirect(data);
/*  75 */     nglNamedBufferStorageEXT(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glNamedBufferStorageEXT(int buffer, IntBuffer data, int flags) {
/*  78 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  79 */     long function_pointer = caps.glNamedBufferStorageEXT;
/*  80 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  81 */     BufferChecks.checkDirect(data);
/*  82 */     nglNamedBufferStorageEXT(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glNamedBufferStorageEXT(int buffer, ShortBuffer data, int flags) {
/*  85 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  86 */     long function_pointer = caps.glNamedBufferStorageEXT;
/*  87 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  88 */     BufferChecks.checkDirect(data);
/*  89 */     nglNamedBufferStorageEXT(buffer, (data.remaining() << 1), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   public static void glNamedBufferStorageEXT(int buffer, LongBuffer data, int flags) {
/*  92 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  93 */     long function_pointer = caps.glNamedBufferStorageEXT;
/*  94 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  95 */     BufferChecks.checkDirect(data);
/*  96 */     nglNamedBufferStorageEXT(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglNamedBufferStorageEXT(int paramInt1, long paramLong1, long paramLong2, int paramInt2, long paramLong3);
/*     */   
/*     */   public static void glNamedBufferStorageEXT(int buffer, long size, int flags) {
/* 102 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 103 */     long function_pointer = caps.glNamedBufferStorageEXT;
/* 104 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 105 */     nglNamedBufferStorageEXT(buffer, size, 0L, flags, function_pointer);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBBufferStorage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */