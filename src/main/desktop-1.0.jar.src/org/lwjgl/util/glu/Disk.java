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
/*     */ public class Disk
/*     */   extends Quadric
/*     */ {
/*     */   public void draw(float innerRadius, float outerRadius, int slices, int loops) {
/*     */     float dtc;
/*     */     int l, s;
/*     */     float a;
/*     */     int i;
/*     */     float r1;
/*     */     int j;
/*  76 */     if (this.normals != 100002) {
/*  77 */       if (this.orientation == 100020) {
/*  78 */         GL11.glNormal3f(0.0F, 0.0F, 1.0F);
/*     */       } else {
/*     */         
/*  81 */         GL11.glNormal3f(0.0F, 0.0F, -1.0F);
/*     */       } 
/*     */     }
/*     */     
/*  85 */     float da = 6.2831855F / slices;
/*  86 */     float dr = (outerRadius - innerRadius) / loops;
/*     */     
/*  88 */     switch (this.drawStyle) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 100012:
/*  95 */         dtc = 2.0F * outerRadius;
/*     */         
/*  97 */         r1 = innerRadius;
/*     */         
/*  99 */         for (j = 0; j < loops; j++) {
/* 100 */           float r2 = r1 + dr;
/* 101 */           if (this.orientation == 100020) {
/*     */             
/* 103 */             GL11.glBegin(8);
/* 104 */             for (int k = 0; k <= slices; k++) {
/*     */               float f1;
/* 106 */               if (k == slices) {
/* 107 */                 f1 = 0.0F;
/*     */               } else {
/* 109 */                 f1 = k * da;
/* 110 */               }  float sa = sin(f1);
/* 111 */               float ca = cos(f1);
/* 112 */               TXTR_COORD(0.5F + sa * r2 / dtc, 0.5F + ca * r2 / dtc);
/* 113 */               GL11.glVertex2f(r2 * sa, r2 * ca);
/* 114 */               TXTR_COORD(0.5F + sa * r1 / dtc, 0.5F + ca * r1 / dtc);
/* 115 */               GL11.glVertex2f(r1 * sa, r1 * ca);
/*     */             } 
/* 117 */             GL11.glEnd();
/*     */           }
/*     */           else {
/*     */             
/* 121 */             GL11.glBegin(8);
/* 122 */             for (int k = slices; k >= 0; k--) {
/*     */               float f1;
/* 124 */               if (k == slices) {
/* 125 */                 f1 = 0.0F;
/*     */               } else {
/* 127 */                 f1 = k * da;
/* 128 */               }  float sa = sin(f1);
/* 129 */               float ca = cos(f1);
/* 130 */               TXTR_COORD(0.5F - sa * r2 / dtc, 0.5F + ca * r2 / dtc);
/* 131 */               GL11.glVertex2f(r2 * sa, r2 * ca);
/* 132 */               TXTR_COORD(0.5F - sa * r1 / dtc, 0.5F + ca * r1 / dtc);
/* 133 */               GL11.glVertex2f(r1 * sa, r1 * ca);
/*     */             } 
/* 135 */             GL11.glEnd();
/*     */           } 
/* 137 */           r1 = r2;
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 100011:
/* 145 */         for (l = 0; l <= loops; l++) {
/* 146 */           float r = innerRadius + l * dr;
/* 147 */           GL11.glBegin(2);
/* 148 */           for (int k = 0; k < slices; k++) {
/* 149 */             float f = k * da;
/* 150 */             GL11.glVertex2f(r * sin(f), r * cos(f));
/*     */           } 
/* 152 */           GL11.glEnd();
/*     */         } 
/*     */         
/* 155 */         for (i = 0; i < slices; i++) {
/* 156 */           float f1 = i * da;
/* 157 */           float x = sin(f1);
/* 158 */           float y = cos(f1);
/* 159 */           GL11.glBegin(3);
/* 160 */           for (l = 0; l <= loops; l++) {
/* 161 */             float r = innerRadius + l * dr;
/* 162 */             GL11.glVertex2f(r * x, r * y);
/*     */           } 
/* 164 */           GL11.glEnd();
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 100010:
/* 171 */         GL11.glBegin(0);
/* 172 */         for (s = 0; s < slices; s++) {
/* 173 */           float f1 = s * da;
/* 174 */           float x = sin(f1);
/* 175 */           float y = cos(f1);
/*     */           
/* 177 */           for (j = 0; j <= loops; j++) {
/* 178 */             float r = innerRadius * j * dr;
/* 179 */             GL11.glVertex2f(r * x, r * y);
/*     */           } 
/*     */         } 
/* 182 */         GL11.glEnd();
/*     */         return;
/*     */ 
/*     */       
/*     */       case 100013:
/* 187 */         if (innerRadius != 0.0D) {
/*     */           
/* 189 */           GL11.glBegin(2); float f;
/* 190 */           for (f = 0.0F; f < 6.2831854820251465D; f += da) {
/* 191 */             float x = innerRadius * sin(f);
/* 192 */             float y = innerRadius * cos(f);
/* 193 */             GL11.glVertex2f(x, y);
/*     */           } 
/* 195 */           GL11.glEnd();
/*     */         } 
/*     */ 
/*     */         
/* 199 */         GL11.glBegin(2);
/* 200 */         for (a = 0.0F; a < 6.2831855F; a += da) {
/* 201 */           float x = outerRadius * sin(a);
/* 202 */           float y = outerRadius * cos(a);
/* 203 */           GL11.glVertex2f(x, y);
/*     */         } 
/* 205 */         GL11.glEnd();
/*     */         return;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\Disk.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */