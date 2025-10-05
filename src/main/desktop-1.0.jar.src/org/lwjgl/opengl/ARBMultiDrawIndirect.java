/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.IntBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ARBMultiDrawIndirect
/*    */ {
/*    */   public static void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
/* 13 */     GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
/*    */   }
/*    */   public static void glMultiDrawArraysIndirect(int mode, long indirect_buffer_offset, int primcount, int stride) {
/* 16 */     GL43.glMultiDrawArraysIndirect(mode, indirect_buffer_offset, primcount, stride);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glMultiDrawArraysIndirect(int mode, IntBuffer indirect, int primcount, int stride) {
/* 21 */     GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
/*    */   }
/*    */   
/*    */   public static void glMultiDrawElementsIndirect(int mode, int type, ByteBuffer indirect, int primcount, int stride) {
/* 25 */     GL43.glMultiDrawElementsIndirect(mode, type, indirect, primcount, stride);
/*    */   }
/*    */   public static void glMultiDrawElementsIndirect(int mode, int type, long indirect_buffer_offset, int primcount, int stride) {
/* 28 */     GL43.glMultiDrawElementsIndirect(mode, type, indirect_buffer_offset, primcount, stride);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glMultiDrawElementsIndirect(int mode, int type, IntBuffer indirect, int primcount, int stride) {
/* 33 */     GL43.glMultiDrawElementsIndirect(mode, type, indirect, primcount, stride);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBMultiDrawIndirect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */