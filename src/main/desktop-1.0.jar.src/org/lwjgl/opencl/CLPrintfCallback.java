/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import org.lwjgl.PointerWrapperAbstract;
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
/*    */ public abstract class CLPrintfCallback
/*    */   extends PointerWrapperAbstract
/*    */ {
/*    */   protected CLPrintfCallback() {
/* 45 */     super(CallbackUtil.getPrintfCallback());
/*    */   }
/*    */   
/*    */   protected abstract void handleMessage(String paramString);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLPrintfCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */