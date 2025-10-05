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
/*    */ 
/*    */ public abstract class CLEventCallback
/*    */   extends PointerWrapperAbstract
/*    */ {
/*    */   private CLObjectRegistry<CLEvent> eventRegistry;
/*    */   
/*    */   protected CLEventCallback() {
/* 48 */     super(CallbackUtil.getEventCallback());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setRegistry(CLObjectRegistry<CLEvent> eventRegistry) {
/* 57 */     this.eventRegistry = eventRegistry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void handleMessage(long event_address, int event_command_exec_status) {
/* 66 */     handleMessage(this.eventRegistry.getObject(event_address), event_command_exec_status);
/*    */   }
/*    */   
/*    */   protected abstract void handleMessage(CLEvent paramCLEvent, int paramInt);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLEventCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */