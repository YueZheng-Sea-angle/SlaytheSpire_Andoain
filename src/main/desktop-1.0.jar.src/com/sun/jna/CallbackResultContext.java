/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class CallbackResultContext
/*    */   extends ToNativeContext
/*    */ {
/*    */   private Method method;
/*    */   
/*    */   CallbackResultContext(Method callbackMethod) {
/* 30 */     this.method = callbackMethod;
/*    */   } public Method getMethod() {
/* 32 */     return this.method;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\com\sun\jna\CallbackResultContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */