/*    */ package org.lwjgl.opengl;
/*    */ 
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
/*    */ public final class ARBInternalformatQuery
/*    */ {
/*    */   public static final int GL_NUM_SAMPLE_COUNTS = 37760;
/*    */   
/*    */   public static void glGetInternalformat(int target, int internalformat, int pname, IntBuffer params) {
/* 18 */     GL42.glGetInternalformat(target, internalformat, pname, params);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int glGetInternalformat(int target, int internalformat, int pname) {
/* 23 */     return GL42.glGetInternalformat(target, internalformat, pname);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBInternalformatQuery.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */