/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BaseReferences
/*     */ {
/*     */   int elementArrayBuffer;
/*     */   int arrayBuffer;
/*     */   final Buffer[] glVertexAttribPointer_buffer;
/*     */   final Buffer[] glTexCoordPointer_buffer;
/*     */   int glClientActiveTexture;
/*     */   int vertexArrayObject;
/*     */   int pixelPackBuffer;
/*     */   int pixelUnpackBuffer;
/*     */   int indirectBuffer;
/*     */   
/*     */   BaseReferences(ContextCapabilities caps) {
/*     */     int max_vertex_attribs, max_texture_units;
/*  58 */     if (caps.OpenGL20 || caps.GL_ARB_vertex_shader) {
/*  59 */       max_vertex_attribs = GL11.glGetInteger(34921);
/*     */     } else {
/*  61 */       max_vertex_attribs = 0;
/*  62 */     }  this.glVertexAttribPointer_buffer = new Buffer[max_vertex_attribs];
/*     */ 
/*     */     
/*  65 */     if (caps.OpenGL20) {
/*  66 */       max_texture_units = GL11.glGetInteger(34930);
/*  67 */     } else if (caps.OpenGL13 || caps.GL_ARB_multitexture) {
/*  68 */       max_texture_units = GL11.glGetInteger(34018);
/*     */     } else {
/*  70 */       max_texture_units = 1;
/*  71 */     }  this.glTexCoordPointer_buffer = new Buffer[max_texture_units];
/*     */   }
/*     */   
/*     */   void clear() {
/*  75 */     this.elementArrayBuffer = 0;
/*  76 */     this.arrayBuffer = 0;
/*  77 */     this.glClientActiveTexture = 0;
/*  78 */     Arrays.fill((Object[])this.glVertexAttribPointer_buffer, (Object)null);
/*  79 */     Arrays.fill((Object[])this.glTexCoordPointer_buffer, (Object)null);
/*     */     
/*  81 */     this.vertexArrayObject = 0;
/*     */     
/*  83 */     this.pixelPackBuffer = 0;
/*  84 */     this.pixelUnpackBuffer = 0;
/*     */     
/*  86 */     this.indirectBuffer = 0;
/*     */   }
/*     */   
/*     */   void copy(BaseReferences references, int mask) {
/*  90 */     if ((mask & 0x2) != 0) {
/*  91 */       this.elementArrayBuffer = references.elementArrayBuffer;
/*  92 */       this.arrayBuffer = references.arrayBuffer;
/*  93 */       this.glClientActiveTexture = references.glClientActiveTexture;
/*  94 */       System.arraycopy(references.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer, 0, this.glVertexAttribPointer_buffer.length);
/*  95 */       System.arraycopy(references.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer, 0, this.glTexCoordPointer_buffer.length);
/*     */       
/*  97 */       this.vertexArrayObject = references.vertexArrayObject;
/*     */       
/*  99 */       this.indirectBuffer = references.indirectBuffer;
/*     */     } 
/*     */     
/* 102 */     if ((mask & 0x1) != 0) {
/* 103 */       this.pixelPackBuffer = references.pixelPackBuffer;
/* 104 */       this.pixelUnpackBuffer = references.pixelUnpackBuffer;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\BaseReferences.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */