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
/*    */ abstract class CLProgramCallback
/*    */   extends PointerWrapperAbstract
/*    */ {
/*    */   private CLContext context;
/*    */   
/*    */   protected CLProgramCallback() {
/* 46 */     super(CallbackUtil.getProgramCallback());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final void setContext(CLContext context) {
/* 55 */     this.context = context;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void handleMessage(long program_address) {
/* 64 */     handleMessage(this.context.getCLProgram(program_address));
/*    */   }
/*    */   
/*    */   protected abstract void handleMessage(CLProgram paramCLProgram);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLProgramCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */