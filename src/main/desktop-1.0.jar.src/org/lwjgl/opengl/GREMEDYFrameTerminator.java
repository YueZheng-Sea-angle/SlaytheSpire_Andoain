/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GREMEDYFrameTerminator
/*    */ {
/*    */   public static void glFrameTerminatorGREMEDY() {
/* 13 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 14 */     long function_pointer = caps.glFrameTerminatorGREMEDY;
/* 15 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 16 */     nglFrameTerminatorGREMEDY(function_pointer);
/*    */   }
/*    */   
/*    */   static native void nglFrameTerminatorGREMEDY(long paramLong);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GREMEDYFrameTerminator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */