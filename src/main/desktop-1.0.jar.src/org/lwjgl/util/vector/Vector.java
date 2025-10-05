/*    */ package org.lwjgl.util.vector;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.nio.FloatBuffer;
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
/*    */ public abstract class Vector
/*    */   implements Serializable, ReadableVector
/*    */ {
/*    */   public final float length() {
/* 58 */     return (float)Math.sqrt(lengthSquared());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract float lengthSquared();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Vector load(FloatBuffer paramFloatBuffer);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Vector negate();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Vector normalise() {
/* 86 */     float len = length();
/* 87 */     if (len != 0.0F) {
/* 88 */       float l = 1.0F / len;
/* 89 */       return scale(l);
/*    */     } 
/* 91 */     throw new IllegalStateException("Zero length vector");
/*    */   }
/*    */   
/*    */   public abstract Vector store(FloatBuffer paramFloatBuffer);
/*    */   
/*    */   public abstract Vector scale(float paramFloat);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\vector\Vector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */