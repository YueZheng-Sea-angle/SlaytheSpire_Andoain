/*    */ package org.lwjgl.opencl.api;
/*    */ 
/*    */ import org.lwjgl.PointerBuffer;
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
/*    */ public final class CLBufferRegion
/*    */ {
/* 44 */   public static final int STRUCT_SIZE = 2 * PointerBuffer.getPointerSize();
/*    */   
/*    */   private final int origin;
/*    */   private final int size;
/*    */   
/*    */   public CLBufferRegion(int origin, int size) {
/* 50 */     this.origin = origin;
/* 51 */     this.size = size;
/*    */   }
/*    */   
/*    */   public int getOrigin() {
/* 55 */     return this.origin;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 59 */     return this.size;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\api\CLBufferRegion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */