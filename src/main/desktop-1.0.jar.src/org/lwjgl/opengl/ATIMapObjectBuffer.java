/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ import org.lwjgl.BufferChecks;
/*    */ import org.lwjgl.LWJGLUtil;
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
/*    */ public final class ATIMapObjectBuffer
/*    */ {
/*    */   public static ByteBuffer glMapObjectBufferATI(int buffer, ByteBuffer old_buffer) {
/* 36 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 37 */     long function_pointer = caps.glMapObjectBufferATI;
/* 38 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 39 */     if (old_buffer != null)
/* 40 */       BufferChecks.checkDirect(old_buffer); 
/* 41 */     ByteBuffer __result = nglMapObjectBufferATI(buffer, ATIVertexArrayObject.glGetObjectBufferiATI(buffer, 34660), old_buffer, function_pointer);
/* 42 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ByteBuffer glMapObjectBufferATI(int buffer, long length, ByteBuffer old_buffer) {
/* 68 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 69 */     long function_pointer = caps.glMapObjectBufferATI;
/* 70 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 71 */     if (old_buffer != null)
/* 72 */       BufferChecks.checkDirect(old_buffer); 
/* 73 */     ByteBuffer __result = nglMapObjectBufferATI(buffer, length, old_buffer, function_pointer);
/* 74 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glUnmapObjectBufferATI(int buffer) {
/* 79 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 80 */     long function_pointer = caps.glUnmapObjectBufferATI;
/* 81 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 82 */     nglUnmapObjectBufferATI(buffer, function_pointer);
/*    */   }
/*    */   
/*    */   static native ByteBuffer nglMapObjectBufferATI(int paramInt, long paramLong1, ByteBuffer paramByteBuffer, long paramLong2);
/*    */   
/*    */   static native void nglUnmapObjectBufferATI(int paramInt, long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ATIMapObjectBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */