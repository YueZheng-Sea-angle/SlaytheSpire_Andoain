/*     */ package org.lwjgl.util.glu;
/*     */ 
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Quadric
/*     */ {
/*  57 */   protected int drawStyle = 100012;
/*  58 */   protected int orientation = 100020;
/*     */   protected boolean textureFlag = false;
/*  60 */   protected int normals = 100000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void normal3f(float x, float y, float z) {
/*  73 */     float mag = (float)Math.sqrt((x * x + y * y + z * z));
/*  74 */     if (mag > 1.0E-5F) {
/*  75 */       x /= mag;
/*  76 */       y /= mag;
/*  77 */       z /= mag;
/*     */     } 
/*  79 */     GL11.glNormal3f(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDrawStyle(int drawStyle) {
/* 101 */     this.drawStyle = drawStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNormals(int normals) {
/* 118 */     this.normals = normals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrientation(int orientation) {
/* 135 */     this.orientation = orientation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextureFlag(boolean textureFlag) {
/* 150 */     this.textureFlag = textureFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDrawStyle() {
/* 159 */     return this.drawStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNormals() {
/* 167 */     return this.normals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOrientation() {
/* 175 */     return this.orientation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTextureFlag() {
/* 183 */     return this.textureFlag;
/*     */   }
/*     */   
/*     */   protected void TXTR_COORD(float x, float y) {
/* 187 */     if (this.textureFlag) GL11.glTexCoord2f(x, y);
/*     */   
/*     */   }
/*     */   
/*     */   protected float sin(float r) {
/* 192 */     return (float)Math.sin(r);
/*     */   }
/*     */   
/*     */   protected float cos(float r) {
/* 196 */     return (float)Math.cos(r);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\Quadric.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */