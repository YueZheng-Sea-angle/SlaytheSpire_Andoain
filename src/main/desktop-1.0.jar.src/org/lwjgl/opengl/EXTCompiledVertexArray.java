/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EXTCompiledVertexArray
/*    */ {
/*    */   public static final int GL_ARRAY_ELEMENT_LOCK_FIRST_EXT = 33192;
/*    */   public static final int GL_ARRAY_ELEMENT_LOCK_COUNT_EXT = 33193;
/*    */   
/*    */   public static void glLockArraysEXT(int first, int count) {
/* 16 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 17 */     long function_pointer = caps.glLockArraysEXT;
/* 18 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 19 */     nglLockArraysEXT(first, count, function_pointer);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glUnlockArraysEXT() {
/* 24 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 25 */     long function_pointer = caps.glUnlockArraysEXT;
/* 26 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 27 */     nglUnlockArraysEXT(function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglLockArraysEXT(int paramInt1, int paramInt2, long paramLong);
/*    */   
/*    */   static native void nglUnlockArraysEXT(long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\EXTCompiledVertexArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */