/*     */ package org.lwjgl.opencl;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.opencl.api.CLBufferRegion;
/*     */ import org.lwjgl.opencl.api.CLImageFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CLMem
/*     */   extends CLObjectChild<CLContext>
/*     */ {
/*  48 */   private static final CLMemUtil util = (CLMemUtil)CLPlatform.<CLMem>getInfoUtilInstance(CLMem.class, "CL_MEM_UTIL");
/*     */   
/*     */   CLMem(long pointer, CLContext context) {
/*  51 */     super(pointer, context);
/*  52 */     if (isValid()) {
/*  53 */       context.getCLMemRegistry().registerObject(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CLMem createImage2D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_row_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
/*  75 */     return util.createImage2D(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
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
/*     */   public static CLMem createImage3D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Buffer host_ptr, IntBuffer errcode_ret) {
/*  97 */     return util.createImage3D(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
/*     */   }
/*     */   
/*     */   public CLMem createSubBuffer(long flags, int buffer_create_type, CLBufferRegion buffer_create_info, IntBuffer errcode_ret) {
/* 101 */     return util.createSubBuffer(this, flags, buffer_create_type, buffer_create_info, errcode_ret);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInfoInt(int param_name) {
/* 112 */     return util.getInfoInt(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInfoSize(int param_name) {
/* 123 */     return util.getInfoSize(this, param_name);
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
/*     */   public long getInfoLong(int param_name) {
/* 135 */     return util.getInfoLong(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getInfoHostBuffer() {
/* 146 */     return util.getInfoHostBuffer(this);
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
/*     */   public long getImageInfoSize(int param_name) {
/* 159 */     return util.getImageInfoSize(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLImageFormat getImageFormat() {
/* 168 */     return util.getImageInfoFormat(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getImageChannelOrder() {
/* 177 */     return util.getImageInfoFormat(this, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getImageChannelType() {
/* 186 */     return util.getImageInfoFormat(this, 1);
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
/*     */   public int getGLObjectType() {
/* 198 */     return util.getGLObjectType(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGLObjectName() {
/* 208 */     return util.getGLObjectName(this);
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
/*     */   public int getGLTextureInfoInt(int param_name) {
/* 222 */     return util.getGLTextureInfoInt(this, param_name);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CLMem create(long pointer, CLContext context) {
/* 261 */     CLMem clMem = context.getCLMemRegistry().getObject(pointer);
/* 262 */     if (clMem == null) {
/* 263 */       clMem = new CLMem(pointer, context);
/*     */     } else {
/* 265 */       clMem.retain();
/*     */     } 
/* 267 */     return clMem;
/*     */   }
/*     */   
/*     */   int release() {
/*     */     try {
/* 272 */       return super.release();
/*     */     } finally {
/* 274 */       if (!isValid())
/* 275 */         getParent().getCLMemRegistry().unregisterObject(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface CLMemUtil extends InfoUtil<CLMem> {
/*     */     CLMem createImage2D(CLContext param1CLContext, long param1Long1, CLImageFormat param1CLImageFormat, long param1Long2, long param1Long3, long param1Long4, Buffer param1Buffer, IntBuffer param1IntBuffer);
/*     */     
/*     */     CLMem createImage3D(CLContext param1CLContext, long param1Long1, CLImageFormat param1CLImageFormat, long param1Long2, long param1Long3, long param1Long4, long param1Long5, long param1Long6, Buffer param1Buffer, IntBuffer param1IntBuffer);
/*     */     
/*     */     CLMem createSubBuffer(CLMem param1CLMem, long param1Long, int param1Int, CLBufferRegion param1CLBufferRegion, IntBuffer param1IntBuffer);
/*     */     
/*     */     ByteBuffer getInfoHostBuffer(CLMem param1CLMem);
/*     */     
/*     */     long getImageInfoSize(CLMem param1CLMem, int param1Int);
/*     */     
/*     */     CLImageFormat getImageInfoFormat(CLMem param1CLMem);
/*     */     
/*     */     int getImageInfoFormat(CLMem param1CLMem, int param1Int);
/*     */     
/*     */     int getGLObjectType(CLMem param1CLMem);
/*     */     
/*     */     int getGLObjectName(CLMem param1CLMem);
/*     */     
/*     */     int getGLTextureInfoInt(CLMem param1CLMem, int param1Int);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLMem.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */