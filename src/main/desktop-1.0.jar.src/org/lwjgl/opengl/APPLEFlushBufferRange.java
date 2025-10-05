/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
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
/*    */ public final class APPLEFlushBufferRange
/*    */ {
/*    */   public static final int GL_BUFFER_SERIALIZED_MODIFY_APPLE = 35346;
/*    */   public static final int GL_BUFFER_FLUSHING_UNMAP_APPLE = 35347;
/*    */   
/*    */   public static void glBufferParameteriAPPLE(int target, int pname, int param) {
/* 20 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 21 */     long function_pointer = caps.glBufferParameteriAPPLE;
/* 22 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 23 */     nglBufferParameteriAPPLE(target, pname, param, function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glFlushMappedBufferRangeAPPLE(int target, long offset, long size) {
/* 28 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 29 */     long function_pointer = caps.glFlushMappedBufferRangeAPPLE;
/* 30 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 31 */     nglFlushMappedBufferRangeAPPLE(target, offset, size, function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglBufferParameteriAPPLE(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*    */   
/*    */   static native void nglFlushMappedBufferRangeAPPLE(int paramInt, long paramLong1, long paramLong2, long paramLong3);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\APPLEFlushBufferRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */