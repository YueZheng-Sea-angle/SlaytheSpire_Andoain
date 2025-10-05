/*     */ package org.lwjgl.util.glu;
/*     */ 
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.BufferUtils;
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
/*     */ public class Util
/*     */ {
/*  54 */   private static IntBuffer scratch = BufferUtils.createIntBuffer(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int ceil(int a, int b) {
/*  65 */     return (a % b == 0) ? (a / b) : (a / b + 1);
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
/*     */   protected static float[] normalize(float[] v) {
/*  78 */     float r = (float)Math.sqrt((v[0] * v[0] + v[1] * v[1] + v[2] * v[2]));
/*  79 */     if (r == 0.0D) {
/*  80 */       return v;
/*     */     }
/*  82 */     r = 1.0F / r;
/*     */     
/*  84 */     v[0] = v[0] * r;
/*  85 */     v[1] = v[1] * r;
/*  86 */     v[2] = v[2] * r;
/*     */     
/*  88 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void cross(float[] v1, float[] v2, float[] result) {
/*  99 */     result[0] = v1[1] * v2[2] - v1[2] * v2[1];
/* 100 */     result[1] = v1[2] * v2[0] - v1[0] * v2[2];
/* 101 */     result[2] = v1[0] * v2[1] - v1[1] * v2[0];
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
/*     */   protected static int compPerPix(int format) {
/* 113 */     switch (format) {
/*     */       case 6400:
/*     */       case 6401:
/*     */       case 6402:
/*     */       case 6403:
/*     */       case 6404:
/*     */       case 6405:
/*     */       case 6406:
/*     */       case 6409:
/* 122 */         return 1;
/*     */       case 6410:
/* 124 */         return 2;
/*     */       case 6407:
/*     */       case 32992:
/* 127 */         return 3;
/*     */       case 6408:
/*     */       case 32993:
/* 130 */         return 4;
/*     */     } 
/* 132 */     return -1;
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
/*     */   protected static int nearestPower(int value) {
/* 148 */     int i = 1;
/*     */ 
/*     */     
/* 151 */     if (value == 0) {
/* 152 */       return -1;
/*     */     }
/*     */     while (true) {
/* 155 */       if (value == 1)
/* 156 */         return i; 
/* 157 */       if (value == 3) {
/* 158 */         return i << 2;
/*     */       }
/* 160 */       value >>= 1;
/* 161 */       i <<= 1;
/*     */     } 
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
/*     */   protected static int bytesPerPixel(int format, int type) {
/*     */     int n;
/* 176 */     switch (format) {
/*     */       case 6400:
/*     */       case 6401:
/*     */       case 6402:
/*     */       case 6403:
/*     */       case 6404:
/*     */       case 6405:
/*     */       case 6406:
/*     */       case 6409:
/* 185 */         n = 1;
/*     */         break;
/*     */       case 6410:
/* 188 */         n = 2;
/*     */         break;
/*     */       case 6407:
/*     */       case 32992:
/* 192 */         n = 3;
/*     */         break;
/*     */       case 6408:
/*     */       case 32993:
/* 196 */         n = 4;
/*     */         break;
/*     */       default:
/* 199 */         n = 0;
/*     */         break;
/*     */     } 
/* 202 */     switch (type)
/*     */     { case 5121:
/* 204 */         m = 1;
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
/* 231 */         return n * m;case 5120: m = 1; return n * m;case 6656: m = 1; return n * m;case 5123: m = 2; return n * m;case 5122: m = 2; return n * m;case 5125: m = 4; return n * m;case 5124: m = 4; return n * m;case 5126: m = 4; return n * m; }  int m = 0; return n * m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int glGetIntegerv(int what) {
/* 242 */     scratch.rewind();
/* 243 */     GL11.glGetInteger(what, scratch);
/* 244 */     return scratch.get();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */