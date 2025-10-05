/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.IntBuffer;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.MemoryUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ARBIndirectParameters
/*    */ {
/*    */   public static final int GL_PARAMETER_BUFFER_ARB = 33006;
/*    */   public static final int GL_PARAMETER_BUFFER_BINDING_ARB = 33007;
/*    */   
/*    */   public static void glMultiDrawArraysIndirectCountARB(int mode, ByteBuffer indirect, long drawcount, int maxdrawcount, int stride) {
/* 27 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 28 */     long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
/* 29 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 30 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 31 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 16 : stride) * maxdrawcount);
/* 32 */     nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
/*    */   }
/*    */   
/*    */   public static void glMultiDrawArraysIndirectCountARB(int mode, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride) {
/* 36 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 37 */     long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
/* 38 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 39 */     GLChecks.ensureIndirectBOenabled(caps);
/* 40 */     nglMultiDrawArraysIndirectCountARBBO(mode, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
/*    */   }
/*    */   static native void nglMultiDrawArraysIndirectCountARB(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, long paramLong3);
/*    */   static native void nglMultiDrawArraysIndirectCountARBBO(int paramInt1, long paramLong1, long paramLong2, int paramInt2, int paramInt3, long paramLong3);
/*    */   
/*    */   public static void glMultiDrawArraysIndirectCountARB(int mode, IntBuffer indirect, long drawcount, int maxdrawcount, int stride) {
/* 46 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 47 */     long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
/* 48 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 49 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 50 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 4 : (stride >> 2)) * maxdrawcount);
/* 51 */     nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
/*    */   }
/*    */   
/*    */   public static void glMultiDrawElementsIndirectCountARB(int mode, int type, ByteBuffer indirect, long drawcount, int maxdrawcount, int stride) {
/* 55 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 56 */     long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
/* 57 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 58 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 59 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 20 : stride) * maxdrawcount);
/* 60 */     nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
/*    */   }
/*    */   static native void nglMultiDrawElementsIndirectCountARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3);
/*    */   public static void glMultiDrawElementsIndirectCountARB(int mode, int type, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride) {
/* 64 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 65 */     long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
/* 66 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 67 */     GLChecks.ensureIndirectBOenabled(caps);
/* 68 */     nglMultiDrawElementsIndirectCountARBBO(mode, type, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglMultiDrawElementsIndirectCountARBBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3);
/*    */   
/*    */   public static void glMultiDrawElementsIndirectCountARB(int mode, int type, IntBuffer indirect, long drawcount, int maxdrawcount, int stride) {
/* 74 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 75 */     long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
/* 76 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 77 */     GLChecks.ensureIndirectBOdisabled(caps);
/* 78 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 5 : (stride >> 2)) * maxdrawcount);
/* 79 */     nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBIndirectParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */