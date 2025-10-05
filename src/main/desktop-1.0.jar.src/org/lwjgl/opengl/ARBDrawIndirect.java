/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.IntBuffer;
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
/*    */ public final class ARBDrawIndirect
/*    */ {
/*    */   public static final int GL_DRAW_INDIRECT_BUFFER = 36671;
/*    */   public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 36675;
/*    */   
/*    */   public static void glDrawArraysIndirect(int mode, ByteBuffer indirect) {
/* 28 */     GL40.glDrawArraysIndirect(mode, indirect);
/*    */   }
/*    */   public static void glDrawArraysIndirect(int mode, long indirect_buffer_offset) {
/* 31 */     GL40.glDrawArraysIndirect(mode, indirect_buffer_offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glDrawArraysIndirect(int mode, IntBuffer indirect) {
/* 36 */     GL40.glDrawArraysIndirect(mode, indirect);
/*    */   }
/*    */   
/*    */   public static void glDrawElementsIndirect(int mode, int type, ByteBuffer indirect) {
/* 40 */     GL40.glDrawElementsIndirect(mode, type, indirect);
/*    */   }
/*    */   public static void glDrawElementsIndirect(int mode, int type, long indirect_buffer_offset) {
/* 43 */     GL40.glDrawElementsIndirect(mode, type, indirect_buffer_offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void glDrawElementsIndirect(int mode, int type, IntBuffer indirect) {
/* 48 */     GL40.glDrawElementsIndirect(mode, type, indirect);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBDrawIndirect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */