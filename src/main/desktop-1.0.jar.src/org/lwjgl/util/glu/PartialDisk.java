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
/*     */ public class PartialDisk
/*     */   extends Quadric
/*     */ {
/*     */   private static final int CACHE_SIZE = 240;
/*     */   
/*     */   public void draw(float innerRadius, float outerRadius, int slices, int loops, float startAngle, float sweepAngle) {
/*     */     int j, slices2, finish;
/*  88 */     float[] sinCache = new float[240];
/*  89 */     float[] cosCache = new float[240];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     float texLow = 0.0F, texHigh = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     if (slices >= 240)
/* 100 */       slices = 239; 
/* 101 */     if (slices < 2 || loops < 1 || outerRadius <= 0.0F || innerRadius < 0.0F || innerRadius > outerRadius) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       System.err.println("PartialDisk: GLU_INVALID_VALUE");
/*     */       
/*     */       return;
/*     */     } 
/* 111 */     if (sweepAngle < -360.0F)
/* 112 */       sweepAngle = 360.0F; 
/* 113 */     if (sweepAngle > 360.0F)
/* 114 */       sweepAngle = 360.0F; 
/* 115 */     if (sweepAngle < 0.0F) {
/* 116 */       startAngle += sweepAngle;
/* 117 */       sweepAngle = -sweepAngle;
/*     */     } 
/*     */     
/* 120 */     if (sweepAngle == 360.0F) {
/* 121 */       slices2 = slices;
/*     */     } else {
/* 123 */       slices2 = slices + 1;
/*     */     } 
/*     */ 
/*     */     
/* 127 */     float deltaRadius = outerRadius - innerRadius;
/*     */ 
/*     */ 
/*     */     
/* 131 */     float angleOffset = startAngle / 180.0F * 3.1415927F; int i;
/* 132 */     for (i = 0; i <= slices; i++) {
/* 133 */       float angle = angleOffset + 3.1415927F * sweepAngle / 180.0F * i / slices;
/* 134 */       sinCache[i] = sin(angle);
/* 135 */       cosCache[i] = cos(angle);
/*     */     } 
/*     */     
/* 138 */     if (sweepAngle == 360.0F) {
/* 139 */       sinCache[slices] = sinCache[0];
/* 140 */       cosCache[slices] = cosCache[0];
/*     */     } 
/*     */     
/* 143 */     switch (this.normals) {
/*     */       case 100000:
/*     */       case 100001:
/* 146 */         if (this.orientation == 100020) {
/* 147 */           GL11.glNormal3f(0.0F, 0.0F, 1.0F); break;
/*     */         } 
/* 149 */         GL11.glNormal3f(0.0F, 0.0F, -1.0F);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     switch (this.drawStyle) {
/*     */       case 100012:
/* 159 */         if (innerRadius == 0.0F) {
/* 160 */           finish = loops - 1;
/*     */           
/* 162 */           GL11.glBegin(6);
/* 163 */           if (this.textureFlag) {
/* 164 */             GL11.glTexCoord2f(0.5F, 0.5F);
/*     */           }
/* 166 */           GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 167 */           float radiusLow = outerRadius - deltaRadius * (loops - 1) / loops;
/* 168 */           if (this.textureFlag) {
/* 169 */             texLow = radiusLow / outerRadius / 2.0F;
/*     */           }
/*     */           
/* 172 */           if (this.orientation == 100020) {
/* 173 */             for (i = slices; i >= 0; i--) {
/* 174 */               if (this.textureFlag) {
/* 175 */                 GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */               }
/*     */ 
/*     */               
/* 179 */               GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0F);
/*     */             } 
/*     */           } else {
/* 182 */             for (i = 0; i <= slices; i++) {
/* 183 */               if (this.textureFlag) {
/* 184 */                 GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */               }
/*     */ 
/*     */               
/* 188 */               GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0F);
/*     */             } 
/*     */           } 
/* 191 */           GL11.glEnd();
/*     */         } else {
/* 193 */           finish = loops;
/*     */         } 
/* 195 */         for (j = 0; j < finish; j++) {
/* 196 */           float radiusLow = outerRadius - deltaRadius * j / loops;
/* 197 */           float radiusHigh = outerRadius - deltaRadius * (j + 1) / loops;
/* 198 */           if (this.textureFlag) {
/* 199 */             texLow = radiusLow / outerRadius / 2.0F;
/* 200 */             texHigh = radiusHigh / outerRadius / 2.0F;
/*     */           } 
/*     */           
/* 203 */           GL11.glBegin(8);
/* 204 */           for (i = 0; i <= slices; i++) {
/* 205 */             if (this.orientation == 100020) {
/* 206 */               if (this.textureFlag) {
/* 207 */                 GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */               }
/*     */ 
/*     */               
/* 211 */               GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0F);
/*     */               
/* 213 */               if (this.textureFlag) {
/* 214 */                 GL11.glTexCoord2f(texHigh * sinCache[i] + 0.5F, texHigh * cosCache[i] + 0.5F);
/*     */               }
/*     */ 
/*     */               
/* 218 */               GL11.glVertex3f(radiusHigh * sinCache[i], radiusHigh * cosCache[i], 0.0F);
/*     */             
/*     */             }
/*     */             else {
/*     */               
/* 223 */               if (this.textureFlag) {
/* 224 */                 GL11.glTexCoord2f(texHigh * sinCache[i] + 0.5F, texHigh * cosCache[i] + 0.5F);
/*     */               }
/*     */ 
/*     */               
/* 228 */               GL11.glVertex3f(radiusHigh * sinCache[i], radiusHigh * cosCache[i], 0.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 233 */               if (this.textureFlag) {
/* 234 */                 GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */               }
/*     */ 
/*     */               
/* 238 */               GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0F);
/*     */             } 
/*     */           } 
/* 241 */           GL11.glEnd();
/*     */         } 
/*     */         break;
/*     */       case 100010:
/* 245 */         GL11.glBegin(0);
/* 246 */         for (i = 0; i < slices2; i++) {
/* 247 */           float sintemp = sinCache[i];
/* 248 */           float costemp = cosCache[i];
/* 249 */           for (j = 0; j <= loops; j++) {
/* 250 */             float f = outerRadius - deltaRadius * j / loops;
/*     */             
/* 252 */             if (this.textureFlag) {
/* 253 */               texLow = f / outerRadius / 2.0F;
/*     */               
/* 255 */               GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */             } 
/*     */ 
/*     */             
/* 259 */             GL11.glVertex3f(f * sintemp, f * costemp, 0.0F);
/*     */           } 
/*     */         } 
/* 262 */         GL11.glEnd();
/*     */         break;
/*     */       case 100011:
/* 265 */         if (innerRadius == outerRadius) {
/* 266 */           GL11.glBegin(3);
/*     */           
/* 268 */           for (i = 0; i <= slices; i++) {
/* 269 */             if (this.textureFlag) {
/* 270 */               GL11.glTexCoord2f(sinCache[i] / 2.0F + 0.5F, cosCache[i] / 2.0F + 0.5F);
/*     */             }
/* 272 */             GL11.glVertex3f(innerRadius * sinCache[i], innerRadius * cosCache[i], 0.0F);
/*     */           } 
/* 274 */           GL11.glEnd();
/*     */           break;
/*     */         } 
/* 277 */         for (j = 0; j <= loops; j++) {
/* 278 */           float radiusLow = outerRadius - deltaRadius * j / loops;
/* 279 */           if (this.textureFlag) {
/* 280 */             texLow = radiusLow / outerRadius / 2.0F;
/*     */           }
/*     */           
/* 283 */           GL11.glBegin(3);
/* 284 */           for (i = 0; i <= slices; i++) {
/* 285 */             if (this.textureFlag) {
/* 286 */               GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */             }
/*     */ 
/*     */             
/* 290 */             GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0F);
/*     */           } 
/* 292 */           GL11.glEnd();
/*     */         } 
/* 294 */         for (i = 0; i < slices2; i++) {
/* 295 */           float sintemp = sinCache[i];
/* 296 */           float costemp = cosCache[i];
/* 297 */           GL11.glBegin(3);
/* 298 */           for (j = 0; j <= loops; j++) {
/* 299 */             float radiusLow = outerRadius - deltaRadius * j / loops;
/* 300 */             if (this.textureFlag) {
/* 301 */               texLow = radiusLow / outerRadius / 2.0F;
/*     */             }
/*     */             
/* 304 */             if (this.textureFlag) {
/* 305 */               GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */             }
/*     */ 
/*     */             
/* 309 */             GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0F);
/*     */           } 
/* 311 */           GL11.glEnd();
/*     */         } 
/*     */         break;
/*     */       case 100013:
/* 315 */         if (sweepAngle < 360.0F) {
/* 316 */           for (i = 0; i <= slices; i += slices) {
/* 317 */             float f1 = sinCache[i];
/* 318 */             float f2 = cosCache[i];
/* 319 */             GL11.glBegin(3);
/* 320 */             for (j = 0; j <= loops; j++) {
/* 321 */               float radiusLow = outerRadius - deltaRadius * j / loops;
/*     */               
/* 323 */               if (this.textureFlag) {
/* 324 */                 texLow = radiusLow / outerRadius / 2.0F;
/* 325 */                 GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */               } 
/*     */ 
/*     */               
/* 329 */               GL11.glVertex3f(radiusLow * f1, radiusLow * f2, 0.0F);
/*     */             } 
/* 331 */             GL11.glEnd();
/*     */           } 
/*     */         }
/* 334 */         for (j = 0; j <= loops; j += loops) {
/* 335 */           float radiusLow = outerRadius - deltaRadius * j / loops;
/* 336 */           if (this.textureFlag) {
/* 337 */             texLow = radiusLow / outerRadius / 2.0F;
/*     */           }
/*     */           
/* 340 */           GL11.glBegin(3);
/* 341 */           for (i = 0; i <= slices; i++) {
/* 342 */             if (this.textureFlag) {
/* 343 */               GL11.glTexCoord2f(texLow * sinCache[i] + 0.5F, texLow * cosCache[i] + 0.5F);
/*     */             }
/*     */ 
/*     */             
/* 347 */             GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0F);
/*     */           } 
/* 349 */           GL11.glEnd();
/* 350 */           if (innerRadius == outerRadius)
/*     */             break; 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\PartialDisk.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */