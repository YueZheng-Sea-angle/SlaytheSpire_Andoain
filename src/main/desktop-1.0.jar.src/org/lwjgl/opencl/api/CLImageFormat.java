/*    */ package org.lwjgl.opencl.api;
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
/*    */ public final class CLImageFormat
/*    */ {
/*    */   public static final int STRUCT_SIZE = 8;
/*    */   private final int channelOrder;
/*    */   private final int channelType;
/*    */   
/*    */   public CLImageFormat(int channelOrder, int channelType) {
/* 48 */     this.channelOrder = channelOrder;
/* 49 */     this.channelType = channelType;
/*    */   }
/*    */   
/*    */   public int getChannelOrder() {
/* 53 */     return this.channelOrder;
/*    */   }
/*    */   
/*    */   public int getChannelType() {
/* 57 */     return this.channelType;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\api\CLImageFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */