/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.MemoryUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ARBClearBufferObject
/*    */ {
/*    */   public static void glClearBufferData(int target, int internalformat, int format, int type, ByteBuffer data) {
/* 13 */     GL43.glClearBufferData(target, internalformat, format, type, data);
/*    */   }
/*    */   
/*    */   public static void glClearBufferSubData(int target, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
/* 17 */     GL43.glClearBufferSubData(target, internalformat, offset, size, format, type, data);
/*    */   }
/*    */   
/*    */   public static void glClearNamedBufferDataEXT(int buffer, int internalformat, int format, int type, ByteBuffer data) {
/* 21 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 22 */     long function_pointer = caps.glClearNamedBufferDataEXT;
/* 23 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 24 */     BufferChecks.checkBuffer(data, 1);
/* 25 */     nglClearNamedBufferDataEXT(buffer, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glClearNamedBufferSubDataEXT(int buffer, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
/* 30 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 31 */     long function_pointer = caps.glClearNamedBufferSubDataEXT;
/* 32 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 33 */     BufferChecks.checkBuffer(data, 1);
/* 34 */     nglClearNamedBufferSubDataEXT(buffer, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglClearNamedBufferDataEXT(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*    */   
/*    */   static native void nglClearNamedBufferSubDataEXT(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3, long paramLong4);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBClearBufferObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */