/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.DoubleBuffer;
/*    */ import java.nio.FloatBuffer;
/*    */ import java.nio.IntBuffer;
/*    */ import java.nio.ShortBuffer;
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
/*    */ public final class KHRRobustness
/*    */ {
/*    */   public static final int GL_GUILTY_CONTEXT_RESET = 33363;
/*    */   public static final int GL_INNOCENT_CONTEXT_RESET = 33364;
/*    */   public static final int GL_UNKNOWN_CONTEXT_RESET = 33365;
/*    */   public static final int GL_CONTEXT_ROBUST_ACCESS = 37107;
/*    */   public static final int GL_RESET_NOTIFICATION_STRATEGY = 33366;
/*    */   public static final int GL_LOSE_CONTEXT_ON_RESET = 33362;
/*    */   public static final int GL_NO_RESET_NOTIFICATION = 33377;
/*    */   public static final int GL_CONTEXT_LOST = 1287;
/*    */   
/*    */   public static int glGetGraphicsResetStatus() {
/* 39 */     return GL45.glGetGraphicsResetStatus();
/*    */   }
/*    */   
/*    */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
/* 43 */     GL45.glReadnPixels(x, y, width, height, format, type, pixels);
/*    */   }
/*    */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
/* 46 */     GL45.glReadnPixels(x, y, width, height, format, type, pixels);
/*    */   }
/*    */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
/* 49 */     GL45.glReadnPixels(x, y, width, height, format, type, pixels);
/*    */   }
/*    */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
/* 52 */     GL45.glReadnPixels(x, y, width, height, format, type, pixels);
/*    */   }
/*    */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
/* 55 */     GL45.glReadnPixels(x, y, width, height, format, type, pixels);
/*    */   }
/*    */   public static void glReadnPixels(int x, int y, int width, int height, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
/* 58 */     GL45.glReadnPixels(x, y, width, height, format, type, pixels_bufSize, pixels_buffer_offset);
/*    */   }
/*    */   
/*    */   public static void glGetnUniform(int program, int location, FloatBuffer params) {
/* 62 */     GL45.glGetnUniform(program, location, params);
/*    */   }
/*    */   
/*    */   public static void glGetnUniform(int program, int location, IntBuffer params) {
/* 66 */     GL45.glGetnUniform(program, location, params);
/*    */   }
/*    */   
/*    */   public static void glGetnUniformu(int program, int location, IntBuffer params) {
/* 70 */     GL45.glGetnUniformu(program, location, params);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\KHRRobustness.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */