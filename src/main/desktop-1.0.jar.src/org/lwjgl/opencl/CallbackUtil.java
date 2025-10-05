/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ final class CallbackUtil
/*    */ {
/* 44 */   private static final Map<CLContext, Long> contextUserData = new HashMap<CLContext, Long>();
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
/*    */   static long createGlobalRef(Object obj) {
/* 56 */     return (obj == null) ? 0L : ncreateGlobalRef(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static native long ncreateGlobalRef(Object paramObject);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static native void deleteGlobalRef(long paramLong);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void checkCallback(int errcode, long user_data) {
/* 82 */     if (errcode != 0 && user_data != 0L)
/* 83 */       deleteGlobalRef(user_data); 
/*    */   }
/*    */   
/*    */   static native long getContextCallback();
/*    */   
/*    */   static native long getMemObjectDestructorCallback();
/*    */   
/*    */   static native long getProgramCallback();
/*    */   
/*    */   static native long getNativeKernelCallback();
/*    */   
/*    */   static native long getEventCallback();
/*    */   
/*    */   static native long getPrintfCallback();
/*    */   
/*    */   static native long getLogMessageToSystemLogAPPLE();
/*    */   
/*    */   static native long getLogMessageToStdoutAPPLE();
/*    */   
/*    */   static native long getLogMessageToStderrAPPLE();
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CallbackUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */