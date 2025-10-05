/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.LWJGLUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GLChecks
/*     */ {
/*     */   static void ensureArrayVBOdisabled(ContextCapabilities caps) {
/*  70 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).arrayBuffer != 0) {
/*  71 */       throw new OpenGLException("Cannot use Buffers when Array Buffer Object is enabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureArrayVBOenabled(ContextCapabilities caps) {
/*  76 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).arrayBuffer == 0) {
/*  77 */       throw new OpenGLException("Cannot use offsets when Array Buffer Object is disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureElementVBOdisabled(ContextCapabilities caps) {
/*  82 */     if (LWJGLUtil.CHECKS && StateTracker.getElementArrayBufferBound(caps) != 0) {
/*  83 */       throw new OpenGLException("Cannot use Buffers when Element Array Buffer Object is enabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureElementVBOenabled(ContextCapabilities caps) {
/*  88 */     if (LWJGLUtil.CHECKS && StateTracker.getElementArrayBufferBound(caps) == 0) {
/*  89 */       throw new OpenGLException("Cannot use offsets when Element Array Buffer Object is disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureIndirectBOdisabled(ContextCapabilities caps) {
/*  94 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).indirectBuffer != 0) {
/*  95 */       throw new OpenGLException("Cannot use Buffers when Draw Indirect Object is enabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureIndirectBOenabled(ContextCapabilities caps) {
/* 100 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).indirectBuffer == 0) {
/* 101 */       throw new OpenGLException("Cannot use offsets when Draw Indirect Object is disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensurePackPBOdisabled(ContextCapabilities caps) {
/* 106 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).pixelPackBuffer != 0) {
/* 107 */       throw new OpenGLException("Cannot use Buffers when Pixel Pack Buffer Object is enabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensurePackPBOenabled(ContextCapabilities caps) {
/* 112 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).pixelPackBuffer == 0) {
/* 113 */       throw new OpenGLException("Cannot use offsets when Pixel Pack Buffer Object is disabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureUnpackPBOdisabled(ContextCapabilities caps) {
/* 118 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).pixelUnpackBuffer != 0) {
/* 119 */       throw new OpenGLException("Cannot use Buffers when Pixel Unpack Buffer Object is enabled");
/*     */     }
/*     */   }
/*     */   
/*     */   static void ensureUnpackPBOenabled(ContextCapabilities caps) {
/* 124 */     if (LWJGLUtil.CHECKS && (StateTracker.getReferences(caps)).pixelUnpackBuffer == 0) {
/* 125 */       throw new OpenGLException("Cannot use offsets when Pixel Unpack Buffer Object is disabled");
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
/*     */   static int calculateImageStorage(Buffer buffer, int format, int type, int width, int height, int depth) {
/* 140 */     return LWJGLUtil.CHECKS ? (calculateImageStorage(format, type, width, height, depth) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
/*     */   }
/*     */   
/*     */   static int calculateTexImage1DStorage(Buffer buffer, int format, int type, int width) {
/* 144 */     return LWJGLUtil.CHECKS ? (calculateTexImage1DStorage(format, type, width) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
/*     */   }
/*     */   
/*     */   static int calculateTexImage2DStorage(Buffer buffer, int format, int type, int width, int height) {
/* 148 */     return LWJGLUtil.CHECKS ? (calculateTexImage2DStorage(format, type, width, height) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
/*     */   }
/*     */   
/*     */   static int calculateTexImage3DStorage(Buffer buffer, int format, int type, int width, int height, int depth) {
/* 152 */     return LWJGLUtil.CHECKS ? (calculateTexImage3DStorage(format, type, width, height, depth) >> BufferUtils.getElementSizeExponent(buffer)) : 0;
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
/*     */   private static int calculateImageStorage(int format, int type, int width, int height, int depth) {
/* 167 */     return calculateBytesPerPixel(format, type) * width * height * depth;
/*     */   }
/*     */   
/*     */   private static int calculateTexImage1DStorage(int format, int type, int width) {
/* 171 */     return calculateBytesPerPixel(format, type) * width;
/*     */   }
/*     */   
/*     */   private static int calculateTexImage2DStorage(int format, int type, int width, int height) {
/* 175 */     return calculateTexImage1DStorage(format, type, width) * height;
/*     */   }
/*     */   
/*     */   private static int calculateTexImage3DStorage(int format, int type, int width, int height, int depth) {
/* 179 */     return calculateTexImage2DStorage(format, type, width, height) * depth;
/*     */   }
/*     */   private static int calculateBytesPerPixel(int format, int type) {
/*     */     int bpe;
/*     */     int epp;
/* 184 */     switch (type) {
/*     */       case 5120:
/*     */       case 5121:
/* 187 */         bpe = 1;
/*     */         break;
/*     */       case 5122:
/*     */       case 5123:
/* 191 */         bpe = 2;
/*     */         break;
/*     */       case 5124:
/*     */       case 5125:
/*     */       case 5126:
/* 196 */         bpe = 4;
/*     */         break;
/*     */       
/*     */       default:
/* 200 */         return 0;
/*     */     } 
/*     */ 
/*     */     
/* 204 */     switch (format) {
/*     */       case 6406:
/*     */       case 6409:
/* 207 */         epp = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 229 */         return bpe * epp;case 6410: epp = 2; return bpe * epp;case 6407: case 32992: epp = 3; return bpe * epp;case 6408: case 32768: case 32993: epp = 4; return bpe * epp;
/*     */     } 
/*     */     return 0;
/*     */   }
/*     */   
/*     */   static int calculateBytesPerCharCode(int type) {
/* 235 */     switch (type) {
/*     */       case 5121:
/*     */       case 37018:
/* 238 */         return 1;
/*     */       case 5123:
/*     */       case 5127:
/*     */       case 37019:
/* 242 */         return 2;
/*     */       case 5128:
/* 244 */         return 3;
/*     */       case 5129:
/* 246 */         return 4;
/*     */     } 
/* 248 */     throw new IllegalArgumentException("Unsupported charcode type: " + type);
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculateBytesPerPathName(int pathNameType) {
/* 253 */     switch (pathNameType) {
/*     */       case 5120:
/*     */       case 5121:
/*     */       case 37018:
/* 257 */         return 1;
/*     */       case 5122:
/*     */       case 5123:
/*     */       case 5127:
/*     */       case 37019:
/* 262 */         return 2;
/*     */       case 5128:
/* 264 */         return 3;
/*     */       case 5124:
/*     */       case 5125:
/*     */       case 5126:
/*     */       case 5129:
/* 269 */         return 4;
/*     */     } 
/* 271 */     throw new IllegalArgumentException("Unsupported path name type: " + pathNameType);
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculateTransformPathValues(int transformType) {
/* 276 */     switch (transformType) {
/*     */       case 0:
/* 278 */         return 0;
/*     */       case 37006:
/*     */       case 37007:
/* 281 */         return 1;
/*     */       case 37008:
/* 283 */         return 2;
/*     */       case 37009:
/* 285 */         return 3;
/*     */       case 37010:
/*     */       case 37014:
/* 288 */         return 6;
/*     */       case 37012:
/*     */       case 37016:
/* 291 */         return 12;
/*     */     } 
/* 293 */     throw new IllegalArgumentException("Unsupported transform type: " + transformType);
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculatePathColorGenCoeffsCount(int genMode, int colorFormat) {
/* 298 */     int coeffsPerComponent = calculatePathGenCoeffsPerComponent(genMode);
/*     */     
/* 300 */     switch (colorFormat) {
/*     */       case 6407:
/* 302 */         return 3 * coeffsPerComponent;
/*     */       case 6408:
/* 304 */         return 4 * coeffsPerComponent;
/*     */     } 
/* 306 */     return coeffsPerComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculatePathTextGenCoeffsPerComponent(FloatBuffer coeffs, int genMode) {
/* 311 */     if (genMode == 0) {
/* 312 */       return 0;
/*     */     }
/* 314 */     return coeffs.remaining() / calculatePathGenCoeffsPerComponent(genMode);
/*     */   }
/*     */   
/*     */   private static int calculatePathGenCoeffsPerComponent(int genMode) {
/* 318 */     switch (genMode) {
/*     */       case 0:
/* 320 */         return 0;
/*     */       case 9217:
/*     */       case 37002:
/* 323 */         return 3;
/*     */       case 9216:
/* 325 */         return 4;
/*     */     } 
/* 327 */     throw new IllegalArgumentException("Unsupported gen mode: " + genMode);
/*     */   }
/*     */ 
/*     */   
/*     */   static int calculateMetricsSize(int metricQueryMask, int stride) {
/* 332 */     if (LWJGLUtil.DEBUG && (stride < 0 || stride % 4 != 0)) {
/* 333 */       throw new IllegalArgumentException("Invalid stride value: " + stride);
/*     */     }
/* 335 */     int metrics = Integer.bitCount(metricQueryMask);
/*     */     
/* 337 */     if (LWJGLUtil.DEBUG && stride >> 2 < metrics) {
/* 338 */       throw new IllegalArgumentException("The queried metrics do not fit in the specified stride: " + stride);
/*     */     }
/* 340 */     return (stride == 0) ? metrics : (stride >> 2);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GLChecks.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */