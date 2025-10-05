/*    */ package org.lwjgl.opencl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CLNativeKernel
/*    */   extends PointerWrapperAbstract
/*    */ {
/*    */   protected CLNativeKernel() {
/* 51 */     super(CallbackUtil.getNativeKernelCallback());
/*    */   }
/*    */   
/*    */   protected abstract void execute(ByteBuffer[] paramArrayOfByteBuffer);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLNativeKernel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */