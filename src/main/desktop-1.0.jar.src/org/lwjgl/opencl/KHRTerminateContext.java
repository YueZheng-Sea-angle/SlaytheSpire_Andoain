/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import org.lwjgl.BufferChecks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class KHRTerminateContext
/*    */ {
/*    */   public static final int CL_DEVICE_TERMINATE_CAPABILITY_KHR = 8207;
/*    */   public static final int CL_CONTEXT_TERMINATE_KHR = 8208;
/*    */   
/*    */   public static int clTerminateContextKHR(CLContext context) {
/* 16 */     long function_pointer = CLCapabilities.clTerminateContextKHR;
/* 17 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 18 */     int __result = nclTerminateContextKHR(context.getPointer(), function_pointer);
/* 19 */     return __result;
/*    */   }
/*    */   
/*    */   static native int nclTerminateContextKHR(long paramLong1, long paramLong2);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\KHRTerminateContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */