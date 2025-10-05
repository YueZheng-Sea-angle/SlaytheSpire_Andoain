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
/*     */ public class Cylinder
/*     */   extends Quadric
/*     */ {
/*     */   public void draw(float baseRadius, float topRadius, float height, int slices, int stacks) {
/*     */     float nsign;
/*  84 */     if (this.orientation == 100021) {
/*  85 */       nsign = -1.0F;
/*     */     } else {
/*  87 */       nsign = 1.0F;
/*     */     } 
/*     */     
/*  90 */     float da = 6.2831855F / slices;
/*  91 */     float dr = (topRadius - baseRadius) / stacks;
/*  92 */     float dz = height / stacks;
/*  93 */     float nz = (baseRadius - topRadius) / height;
/*     */ 
/*     */     
/*  96 */     if (this.drawStyle == 100010) {
/*  97 */       GL11.glBegin(0);
/*  98 */       for (int i = 0; i < slices; i++) {
/*  99 */         float x = cos(i * da);
/* 100 */         float y = sin(i * da);
/* 101 */         normal3f(x * nsign, y * nsign, nz * nsign);
/*     */         
/* 103 */         float z = 0.0F;
/* 104 */         float r = baseRadius;
/* 105 */         for (int j = 0; j <= stacks; j++) {
/* 106 */           GL11.glVertex3f(x * r, y * r, z);
/* 107 */           z += dz;
/* 108 */           r += dr;
/*     */         } 
/*     */       } 
/* 111 */       GL11.glEnd();
/* 112 */     } else if (this.drawStyle == 100011 || this.drawStyle == 100013) {
/*     */       
/* 114 */       if (this.drawStyle == 100011) {
/* 115 */         float z = 0.0F;
/* 116 */         float r = baseRadius;
/* 117 */         for (int j = 0; j <= stacks; j++) {
/* 118 */           GL11.glBegin(2);
/* 119 */           for (int k = 0; k < slices; k++) {
/* 120 */             float x = cos(k * da);
/* 121 */             float y = sin(k * da);
/* 122 */             normal3f(x * nsign, y * nsign, nz * nsign);
/* 123 */             GL11.glVertex3f(x * r, y * r, z);
/*     */           } 
/* 125 */           GL11.glEnd();
/* 126 */           z += dz;
/* 127 */           r += dr;
/*     */         }
/*     */       
/*     */       }
/* 131 */       else if (baseRadius != 0.0D) {
/* 132 */         GL11.glBegin(2); int j;
/* 133 */         for (j = 0; j < slices; j++) {
/* 134 */           float x = cos(j * da);
/* 135 */           float y = sin(j * da);
/* 136 */           normal3f(x * nsign, y * nsign, nz * nsign);
/* 137 */           GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0F);
/*     */         } 
/* 139 */         GL11.glEnd();
/* 140 */         GL11.glBegin(2);
/* 141 */         for (j = 0; j < slices; j++) {
/* 142 */           float x = cos(j * da);
/* 143 */           float y = sin(j * da);
/* 144 */           normal3f(x * nsign, y * nsign, nz * nsign);
/* 145 */           GL11.glVertex3f(x * topRadius, y * topRadius, height);
/*     */         } 
/* 147 */         GL11.glEnd();
/*     */       } 
/*     */ 
/*     */       
/* 151 */       GL11.glBegin(1);
/* 152 */       for (int i = 0; i < slices; i++) {
/* 153 */         float x = cos(i * da);
/* 154 */         float y = sin(i * da);
/* 155 */         normal3f(x * nsign, y * nsign, nz * nsign);
/* 156 */         GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0F);
/* 157 */         GL11.glVertex3f(x * topRadius, y * topRadius, height);
/*     */       } 
/* 159 */       GL11.glEnd();
/* 160 */     } else if (this.drawStyle == 100012) {
/* 161 */       float ds = 1.0F / slices;
/* 162 */       float dt = 1.0F / stacks;
/* 163 */       float t = 0.0F;
/* 164 */       float z = 0.0F;
/* 165 */       float r = baseRadius;
/* 166 */       for (int j = 0; j < stacks; j++) {
/* 167 */         float s = 0.0F;
/* 168 */         GL11.glBegin(8);
/* 169 */         for (int i = 0; i <= slices; i++) {
/* 170 */           float x, y; if (i == slices) {
/* 171 */             x = sin(0.0F);
/* 172 */             y = cos(0.0F);
/*     */           } else {
/* 174 */             x = sin(i * da);
/* 175 */             y = cos(i * da);
/*     */           } 
/* 177 */           if (nsign == 1.0F) {
/* 178 */             normal3f(x * nsign, y * nsign, nz * nsign);
/* 179 */             TXTR_COORD(s, t);
/* 180 */             GL11.glVertex3f(x * r, y * r, z);
/* 181 */             normal3f(x * nsign, y * nsign, nz * nsign);
/* 182 */             TXTR_COORD(s, t + dt);
/* 183 */             GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
/*     */           } else {
/* 185 */             normal3f(x * nsign, y * nsign, nz * nsign);
/* 186 */             TXTR_COORD(s, t);
/* 187 */             GL11.glVertex3f(x * r, y * r, z);
/* 188 */             normal3f(x * nsign, y * nsign, nz * nsign);
/* 189 */             TXTR_COORD(s, t + dt);
/* 190 */             GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
/*     */           } 
/* 192 */           s += ds;
/*     */         } 
/* 194 */         GL11.glEnd();
/* 195 */         r += dr;
/* 196 */         t += dt;
/* 197 */         z += dz;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\Cylinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */