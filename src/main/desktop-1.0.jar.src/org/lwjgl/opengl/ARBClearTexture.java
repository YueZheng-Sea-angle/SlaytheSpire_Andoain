/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.DoubleBuffer;
/*    */ import java.nio.FloatBuffer;
/*    */ import java.nio.IntBuffer;
/*    */ import java.nio.LongBuffer;
/*    */ import java.nio.ShortBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ARBClearTexture
/*    */ {
/*    */   public static final int GL_CLEAR_TEXTURE = 37733;
/*    */   
/*    */   public static void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
/* 19 */     GL44.glClearTexImage(texture, level, format, type, data);
/*    */   }
/*    */   public static void glClearTexImage(int texture, int level, int format, int type, DoubleBuffer data) {
/* 22 */     GL44.glClearTexImage(texture, level, format, type, data);
/*    */   }
/*    */   public static void glClearTexImage(int texture, int level, int format, int type, FloatBuffer data) {
/* 25 */     GL44.glClearTexImage(texture, level, format, type, data);
/*    */   }
/*    */   public static void glClearTexImage(int texture, int level, int format, int type, IntBuffer data) {
/* 28 */     GL44.glClearTexImage(texture, level, format, type, data);
/*    */   }
/*    */   public static void glClearTexImage(int texture, int level, int format, int type, ShortBuffer data) {
/* 31 */     GL44.glClearTexImage(texture, level, format, type, data);
/*    */   }
/*    */   public static void glClearTexImage(int texture, int level, int format, int type, LongBuffer data) {
/* 34 */     GL44.glClearTexImage(texture, level, format, type, data);
/*    */   }
/*    */   
/*    */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
/* 38 */     GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
/*    */   }
/*    */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer data) {
/* 41 */     GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
/*    */   }
/*    */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer data) {
/* 44 */     GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
/*    */   }
/*    */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer data) {
/* 47 */     GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
/*    */   }
/*    */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer data) {
/* 50 */     GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
/*    */   }
/*    */   public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, LongBuffer data) {
/* 53 */     GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBClearTexture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */