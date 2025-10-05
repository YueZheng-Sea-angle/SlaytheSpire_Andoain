/*     */ package org.lwjgl.util.vector;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.FloatBuffer;
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
/*     */ public class Vector2f
/*     */   extends Vector
/*     */   implements Serializable, ReadableVector2f, WritableVector2f
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public float x;
/*     */   public float y;
/*     */   
/*     */   public Vector2f() {}
/*     */   
/*     */   public Vector2f(ReadableVector2f src) {
/*  65 */     set(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f(float x, float y) {
/*  72 */     set(x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(float x, float y) {
/*  79 */     this.x = x;
/*  80 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f set(ReadableVector2f src) {
/*  89 */     this.x = src.getX();
/*  90 */     this.y = src.getY();
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float lengthSquared() {
/*  98 */     return this.x * this.x + this.y * this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f translate(float x, float y) {
/* 108 */     this.x += x;
/* 109 */     this.y += y;
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector negate() {
/* 118 */     this.x = -this.x;
/* 119 */     this.y = -this.y;
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f negate(Vector2f dest) {
/* 129 */     if (dest == null)
/* 130 */       dest = new Vector2f(); 
/* 131 */     dest.x = -this.x;
/* 132 */     dest.y = -this.y;
/* 133 */     return dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector2f normalise(Vector2f dest) {
/* 143 */     float l = length();
/*     */     
/* 145 */     if (dest == null) {
/* 146 */       dest = new Vector2f(this.x / l, this.y / l);
/*     */     } else {
/* 148 */       dest.set(this.x / l, this.y / l);
/*     */     } 
/* 150 */     return dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float dot(Vector2f left, Vector2f right) {
/* 161 */     return left.x * right.x + left.y * right.y;
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
/*     */   public static float angle(Vector2f a, Vector2f b) {
/* 173 */     float dls = dot(a, b) / a.length() * b.length();
/* 174 */     if (dls < -1.0F) {
/* 175 */       dls = -1.0F;
/* 176 */     } else if (dls > 1.0F) {
/* 177 */       dls = 1.0F;
/* 178 */     }  return (float)Math.acos(dls);
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
/*     */   public static Vector2f add(Vector2f left, Vector2f right, Vector2f dest) {
/* 190 */     if (dest == null) {
/* 191 */       return new Vector2f(left.x + right.x, left.y + right.y);
/*     */     }
/* 193 */     dest.set(left.x + right.x, left.y + right.y);
/* 194 */     return dest;
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
/*     */   public static Vector2f sub(Vector2f left, Vector2f right, Vector2f dest) {
/* 207 */     if (dest == null) {
/* 208 */       return new Vector2f(left.x - right.x, left.y - right.y);
/*     */     }
/* 210 */     dest.set(left.x - right.x, left.y - right.y);
/* 211 */     return dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector store(FloatBuffer buf) {
/* 221 */     buf.put(this.x);
/* 222 */     buf.put(this.y);
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector load(FloatBuffer buf) {
/* 232 */     this.x = buf.get();
/* 233 */     this.y = buf.get();
/* 234 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector scale(float scale) {
/* 242 */     this.x *= scale;
/* 243 */     this.y *= scale;
/*     */     
/* 245 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 252 */     StringBuilder sb = new StringBuilder(64);
/*     */     
/* 254 */     sb.append("Vector2f[");
/* 255 */     sb.append(this.x);
/* 256 */     sb.append(", ");
/* 257 */     sb.append(this.y);
/* 258 */     sb.append(']');
/* 259 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getX() {
/* 266 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getY() {
/* 273 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setX(float x) {
/* 281 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setY(float y) {
/* 289 */     this.y = y;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 293 */     if (this == obj) return true; 
/* 294 */     if (obj == null) return false; 
/* 295 */     if (getClass() != obj.getClass()) return false; 
/* 296 */     Vector2f other = (Vector2f)obj;
/*     */     
/* 298 */     if (this.x == other.x && this.y == other.y) return true;
/*     */     
/* 300 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\vector\Vector2f.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */