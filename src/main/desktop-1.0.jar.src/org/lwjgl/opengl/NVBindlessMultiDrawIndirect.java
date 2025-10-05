/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.MemoryUtil;
/*    */ 
/*    */ 
/*    */ public final class NVBindlessMultiDrawIndirect
/*    */ {
/*    */   static native void nglMultiDrawArraysIndirectBindlessNV(int paramInt1, long paramLong1, int paramInt2, int paramInt3, int paramInt4, long paramLong2);
/*    */   
/*    */   public static void glMultiDrawArraysIndirectBindlessNV(int mode, ByteBuffer indirect, int drawCount, int stride, int vertexBufferCount) {
/* 13 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 14 */     long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
/* 15 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 16 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 17 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? (20 + 24 * vertexBufferCount) : stride) * drawCount);
/* 18 */     nglMultiDrawArraysIndirectBindlessNV(mode, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
/*    */   }
/*    */   
/*    */   public static void glMultiDrawArraysIndirectBindlessNV(int mode, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount) {
/* 22 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 23 */     long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
/* 24 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 25 */     GLChecks.ensureIndirectBOenabled(caps);
/* 26 */     nglMultiDrawArraysIndirectBindlessNVBO(mode, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glMultiDrawElementsIndirectBindlessNV(int mode, int type, ByteBuffer indirect, int drawCount, int stride, int vertexBufferCount) {
/* 31 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 32 */     long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
/* 33 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 34 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 35 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? (48 + 24 * vertexBufferCount) : stride) * drawCount);
/* 36 */     nglMultiDrawElementsIndirectBindlessNV(mode, type, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
/*    */   }
/*    */   static native void nglMultiDrawArraysIndirectBindlessNVBO(int paramInt1, long paramLong1, int paramInt2, int paramInt3, int paramInt4, long paramLong2);
/*    */   public static void glMultiDrawElementsIndirectBindlessNV(int mode, int type, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount) {
/* 40 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 41 */     long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
/* 42 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 43 */     GLChecks.ensureIndirectBOenabled(caps);
/* 44 */     nglMultiDrawElementsIndirectBindlessNVBO(mode, type, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglMultiDrawElementsIndirectBindlessNV(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, int paramInt5, long paramLong2);
/*    */   
/*    */   static native void nglMultiDrawElementsIndirectBindlessNVBO(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, int paramInt5, long paramLong2);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\NVBindlessMultiDrawIndirect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */