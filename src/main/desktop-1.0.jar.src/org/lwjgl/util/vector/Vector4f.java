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
/*     */ public class Vector4f
/*     */   extends Vector
/*     */   implements Serializable, ReadableVector4f, WritableVector4f
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public float x;
/*     */   public float y;
/*     */   public float z;
/*     */   public float w;
/*     */   
/*     */   public Vector4f() {}
/*     */   
/*     */   public Vector4f(ReadableVector4f src) {
/*  65 */     set(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector4f(float x, float y, float z, float w) {
/*  72 */     set(x, y, z, w);
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
/*     */   public void set(float x, float y, float z) {
/*  87 */     this.x = x;
/*  88 */     this.y = y;
/*  89 */     this.z = z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(float x, float y, float z, float w) {
/*  96 */     this.x = x;
/*  97 */     this.y = y;
/*  98 */     this.z = z;
/*  99 */     this.w = w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector4f set(ReadableVector4f src) {
/* 108 */     this.x = src.getX();
/* 109 */     this.y = src.getY();
/* 110 */     this.z = src.getZ();
/* 111 */     this.w = src.getW();
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float lengthSquared() {
/* 119 */     return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector4f translate(float x, float y, float z, float w) {
/* 129 */     this.x += x;
/* 130 */     this.y += y;
/* 131 */     this.z += z;
/* 132 */     this.w += w;
/* 133 */     return this;
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
/*     */   public static Vector4f add(Vector4f left, Vector4f right, Vector4f dest) {
/* 145 */     if (dest == null) {
/* 146 */       return new Vector4f(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
/*     */     }
/* 148 */     dest.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
/* 149 */     return dest;
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
/*     */   public static Vector4f sub(Vector4f left, Vector4f right, Vector4f dest) {
/* 162 */     if (dest == null) {
/* 163 */       return new Vector4f(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
/*     */     }
/* 165 */     dest.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
/* 166 */     return dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector negate() {
/* 176 */     this.x = -this.x;
/* 177 */     this.y = -this.y;
/* 178 */     this.z = -this.z;
/* 179 */     this.w = -this.w;
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector4f negate(Vector4f dest) {
/* 189 */     if (dest == null)
/* 190 */       dest = new Vector4f(); 
/* 191 */     dest.x = -this.x;
/* 192 */     dest.y = -this.y;
/* 193 */     dest.z = -this.z;
/* 194 */     dest.w = -this.w;
/* 195 */     return dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector4f normalise(Vector4f dest) {
/* 205 */     float l = length();
/*     */     
/* 207 */     if (dest == null) {
/* 208 */       dest = new Vector4f(this.x / l, this.y / l, this.z / l, this.w / l);
/*     */     } else {
/* 210 */       dest.set(this.x / l, this.y / l, this.z / l, this.w / l);
/*     */     } 
/* 212 */     return dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float dot(Vector4f left, Vector4f right) {
/* 223 */     return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float angle(Vector4f a, Vector4f b) {
/* 233 */     float dls = dot(a, b) / a.length() * b.length();
/* 234 */     if (dls < -1.0F) {
/* 235 */       dls = -1.0F;
/* 236 */     } else if (dls > 1.0F) {
/* 237 */       dls = 1.0F;
/* 238 */     }  return (float)Math.acos(dls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector load(FloatBuffer buf) {
/* 245 */     this.x = buf.get();
/* 246 */     this.y = buf.get();
/* 247 */     this.z = buf.get();
/* 248 */     this.w = buf.get();
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector scale(float scale) {
/* 256 */     this.x *= scale;
/* 257 */     this.y *= scale;
/* 258 */     this.z *= scale;
/* 259 */     this.w *= scale;
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector store(FloatBuffer buf) {
/* 268 */     buf.put(this.x);
/* 269 */     buf.put(this.y);
/* 270 */     buf.put(this.z);
/* 271 */     buf.put(this.w);
/*     */     
/* 273 */     return this;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 277 */     return "Vector4f: " + this.x + " " + this.y + " " + this.z + " " + this.w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getX() {
/* 284 */     return this.x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final float getY() {
/* 291 */     return this.y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setX(float x) {
/* 299 */     this.x = x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setY(float y) {
/* 307 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZ(float z) {
/* 315 */     this.z = z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getZ() {
/* 323 */     return this.z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setW(float w) {
/* 331 */     this.w = w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getW() {
/* 338 */     return this.w;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 342 */     if (this == obj) return true; 
/* 343 */     if (obj == null) return false; 
/* 344 */     if (getClass() != obj.getClass()) return false; 
/* 345 */     Vector4f other = (Vector4f)obj;
/*     */     
/* 347 */     if (this.x == other.x && this.y == other.y && this.z == other.z && this.w == other.w) return true;
/*     */     
/* 349 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\vector\Vector4f.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */