/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.IntBuffer;
/*    */ import org.lwjgl.PointerBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ARBMultiBind
/*    */ {
/*    */   public static void glBindBuffersBase(int target, int first, int count, IntBuffer buffers) {
/* 13 */     GL44.glBindBuffersBase(target, first, count, buffers);
/*    */   }
/*    */   
/*    */   public static void glBindBuffersRange(int target, int first, int count, IntBuffer buffers, PointerBuffer offsets, PointerBuffer sizes) {
/* 17 */     GL44.glBindBuffersRange(target, first, count, buffers, offsets, sizes);
/*    */   }
/*    */   
/*    */   public static void glBindTextures(int first, int count, IntBuffer textures) {
/* 21 */     GL44.glBindTextures(first, count, textures);
/*    */   }
/*    */   
/*    */   public static void glBindSamplers(int first, int count, IntBuffer samplers) {
/* 25 */     GL44.glBindSamplers(first, count, samplers);
/*    */   }
/*    */   
/*    */   public static void glBindImageTextures(int first, int count, IntBuffer textures) {
/* 29 */     GL44.glBindImageTextures(first, count, textures);
/*    */   }
/*    */   
/*    */   public static void glBindVertexBuffers(int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
/* 33 */     GL44.glBindVertexBuffers(first, count, buffers, offsets, strides);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBMultiBind.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */