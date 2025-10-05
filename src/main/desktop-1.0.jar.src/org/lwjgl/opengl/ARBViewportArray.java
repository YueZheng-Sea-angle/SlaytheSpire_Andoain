/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.DoubleBuffer;
/*    */ import java.nio.FloatBuffer;
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
/*    */ public final class ARBViewportArray
/*    */ {
/*    */   public static final int GL_MAX_VIEWPORTS = 33371;
/*    */   public static final int GL_VIEWPORT_SUBPIXEL_BITS = 33372;
/*    */   public static final int GL_VIEWPORT_BOUNDS_RANGE = 33373;
/*    */   public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
/*    */   public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 33375;
/*    */   public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
/*    */   public static final int GL_LAST_VERTEX_CONVENTION = 36430;
/*    */   public static final int GL_PROVOKING_VERTEX = 36431;
/*    */   public static final int GL_UNDEFINED_VERTEX = 33376;
/*    */   
/*    */   public static void glViewportArray(int first, FloatBuffer v) {
/* 32 */     GL41.glViewportArray(first, v);
/*    */   }
/*    */   
/*    */   public static void glViewportIndexedf(int index, float x, float y, float w, float h) {
/* 36 */     GL41.glViewportIndexedf(index, x, y, w, h);
/*    */   }
/*    */   
/*    */   public static void glViewportIndexed(int index, FloatBuffer v) {
/* 40 */     GL41.glViewportIndexed(index, v);
/*    */   }
/*    */   
/*    */   public static void glScissorArray(int first, IntBuffer v) {
/* 44 */     GL41.glScissorArray(first, v);
/*    */   }
/*    */   
/*    */   public static void glScissorIndexed(int index, int left, int bottom, int width, int height) {
/* 48 */     GL41.glScissorIndexed(index, left, bottom, width, height);
/*    */   }
/*    */   
/*    */   public static void glScissorIndexed(int index, IntBuffer v) {
/* 52 */     GL41.glScissorIndexed(index, v);
/*    */   }
/*    */   
/*    */   public static void glDepthRangeArray(int first, DoubleBuffer v) {
/* 56 */     GL41.glDepthRangeArray(first, v);
/*    */   }
/*    */   
/*    */   public static void glDepthRangeIndexed(int index, double n, double f) {
/* 60 */     GL41.glDepthRangeIndexed(index, n, f);
/*    */   }
/*    */   
/*    */   public static void glGetFloat(int target, int index, FloatBuffer data) {
/* 64 */     GL41.glGetFloat(target, index, data);
/*    */   }
/*    */ 
/*    */   
/*    */   public static float glGetFloat(int target, int index) {
/* 69 */     return GL41.glGetFloat(target, index);
/*    */   }
/*    */   
/*    */   public static void glGetDouble(int target, int index, DoubleBuffer data) {
/* 73 */     GL41.glGetDouble(target, index, data);
/*    */   }
/*    */ 
/*    */   
/*    */   public static double glGetDouble(int target, int index) {
/* 78 */     return GL41.glGetDouble(target, index);
/*    */   }
/*    */   
/*    */   public static void glGetIntegerIndexedEXT(int target, int index, IntBuffer v) {
/* 82 */     EXTDrawBuffers2.glGetIntegerIndexedEXT(target, index, v);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int glGetIntegerIndexedEXT(int target, int index) {
/* 87 */     return EXTDrawBuffers2.glGetIntegerIndexedEXT(target, index);
/*    */   }
/*    */   
/*    */   public static void glEnableIndexedEXT(int target, int index) {
/* 91 */     EXTDrawBuffers2.glEnableIndexedEXT(target, index);
/*    */   }
/*    */   
/*    */   public static void glDisableIndexedEXT(int target, int index) {
/* 95 */     EXTDrawBuffers2.glDisableIndexedEXT(target, index);
/*    */   }
/*    */   
/*    */   public static boolean glIsEnabledIndexedEXT(int target, int index) {
/* 99 */     return EXTDrawBuffers2.glIsEnabledIndexedEXT(target, index);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBViewportArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */