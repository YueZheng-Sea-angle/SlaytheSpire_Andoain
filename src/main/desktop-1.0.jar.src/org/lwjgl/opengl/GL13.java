/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import org.lwjgl.BufferChecks;
/*     */ import org.lwjgl.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GL13
/*     */ {
/*     */   public static final int GL_TEXTURE0 = 33984;
/*     */   public static final int GL_TEXTURE1 = 33985;
/*     */   public static final int GL_TEXTURE2 = 33986;
/*     */   public static final int GL_TEXTURE3 = 33987;
/*     */   public static final int GL_TEXTURE4 = 33988;
/*     */   public static final int GL_TEXTURE5 = 33989;
/*     */   public static final int GL_TEXTURE6 = 33990;
/*     */   public static final int GL_TEXTURE7 = 33991;
/*     */   public static final int GL_TEXTURE8 = 33992;
/*     */   public static final int GL_TEXTURE9 = 33993;
/*     */   public static final int GL_TEXTURE10 = 33994;
/*     */   public static final int GL_TEXTURE11 = 33995;
/*     */   public static final int GL_TEXTURE12 = 33996;
/*     */   public static final int GL_TEXTURE13 = 33997;
/*     */   public static final int GL_TEXTURE14 = 33998;
/*     */   public static final int GL_TEXTURE15 = 33999;
/*     */   public static final int GL_TEXTURE16 = 34000;
/*     */   public static final int GL_TEXTURE17 = 34001;
/*     */   public static final int GL_TEXTURE18 = 34002;
/*     */   public static final int GL_TEXTURE19 = 34003;
/*     */   public static final int GL_TEXTURE20 = 34004;
/*     */   public static final int GL_TEXTURE21 = 34005;
/*     */   public static final int GL_TEXTURE22 = 34006;
/*     */   public static final int GL_TEXTURE23 = 34007;
/*     */   public static final int GL_TEXTURE24 = 34008;
/*     */   public static final int GL_TEXTURE25 = 34009;
/*     */   public static final int GL_TEXTURE26 = 34010;
/*     */   public static final int GL_TEXTURE27 = 34011;
/*     */   public static final int GL_TEXTURE28 = 34012;
/*     */   public static final int GL_TEXTURE29 = 34013;
/*     */   public static final int GL_TEXTURE30 = 34014;
/*     */   public static final int GL_TEXTURE31 = 34015;
/*     */   public static final int GL_ACTIVE_TEXTURE = 34016;
/*     */   public static final int GL_CLIENT_ACTIVE_TEXTURE = 34017;
/*     */   public static final int GL_MAX_TEXTURE_UNITS = 34018;
/*     */   public static final int GL_NORMAL_MAP = 34065;
/*     */   public static final int GL_REFLECTION_MAP = 34066;
/*     */   public static final int GL_TEXTURE_CUBE_MAP = 34067;
/*     */   public static final int GL_TEXTURE_BINDING_CUBE_MAP = 34068;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
/*     */   public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
/*     */   public static final int GL_PROXY_TEXTURE_CUBE_MAP = 34075;
/*     */   public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 34076;
/*     */   public static final int GL_COMPRESSED_ALPHA = 34025;
/*     */   public static final int GL_COMPRESSED_LUMINANCE = 34026;
/*     */   public static final int GL_COMPRESSED_LUMINANCE_ALPHA = 34027;
/*     */   public static final int GL_COMPRESSED_INTENSITY = 34028;
/*     */   public static final int GL_COMPRESSED_RGB = 34029;
/*     */   public static final int GL_COMPRESSED_RGBA = 34030;
/*     */   public static final int GL_TEXTURE_COMPRESSION_HINT = 34031;
/*     */   public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE = 34464;
/*     */   public static final int GL_TEXTURE_COMPRESSED = 34465;
/*     */   public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 34466;
/*     */   public static final int GL_COMPRESSED_TEXTURE_FORMATS = 34467;
/*     */   public static final int GL_MULTISAMPLE = 32925;
/*     */   public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 32926;
/*     */   public static final int GL_SAMPLE_ALPHA_TO_ONE = 32927;
/*     */   public static final int GL_SAMPLE_COVERAGE = 32928;
/*     */   public static final int GL_SAMPLE_BUFFERS = 32936;
/*     */   public static final int GL_SAMPLES = 32937;
/*     */   public static final int GL_SAMPLE_COVERAGE_VALUE = 32938;
/*     */   public static final int GL_SAMPLE_COVERAGE_INVERT = 32939;
/*     */   public static final int GL_MULTISAMPLE_BIT = 536870912;
/*     */   public static final int GL_TRANSPOSE_MODELVIEW_MATRIX = 34019;
/*     */   public static final int GL_TRANSPOSE_PROJECTION_MATRIX = 34020;
/*     */   public static final int GL_TRANSPOSE_TEXTURE_MATRIX = 34021;
/*     */   public static final int GL_TRANSPOSE_COLOR_MATRIX = 34022;
/*     */   public static final int GL_COMBINE = 34160;
/*     */   public static final int GL_COMBINE_RGB = 34161;
/*     */   public static final int GL_COMBINE_ALPHA = 34162;
/*     */   public static final int GL_SOURCE0_RGB = 34176;
/*     */   public static final int GL_SOURCE1_RGB = 34177;
/*     */   public static final int GL_SOURCE2_RGB = 34178;
/*     */   public static final int GL_SOURCE0_ALPHA = 34184;
/*     */   public static final int GL_SOURCE1_ALPHA = 34185;
/*     */   public static final int GL_SOURCE2_ALPHA = 34186;
/*     */   public static final int GL_OPERAND0_RGB = 34192;
/*     */   public static final int GL_OPERAND1_RGB = 34193;
/*     */   public static final int GL_OPERAND2_RGB = 34194;
/*     */   public static final int GL_OPERAND0_ALPHA = 34200;
/*     */   public static final int GL_OPERAND1_ALPHA = 34201;
/*     */   public static final int GL_OPERAND2_ALPHA = 34202;
/*     */   public static final int GL_RGB_SCALE = 34163;
/*     */   public static final int GL_ADD_SIGNED = 34164;
/*     */   public static final int GL_INTERPOLATE = 34165;
/*     */   public static final int GL_SUBTRACT = 34023;
/*     */   public static final int GL_CONSTANT = 34166;
/*     */   public static final int GL_PRIMARY_COLOR = 34167;
/*     */   public static final int GL_PREVIOUS = 34168;
/*     */   public static final int GL_DOT3_RGB = 34478;
/*     */   public static final int GL_DOT3_RGBA = 34479;
/*     */   public static final int GL_CLAMP_TO_BORDER = 33069;
/*     */   
/*     */   public static void glActiveTexture(int texture) {
/* 118 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 119 */     long function_pointer = caps.glActiveTexture;
/* 120 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 121 */     nglActiveTexture(texture, function_pointer);
/*     */   }
/*     */   static native void nglActiveTexture(int paramInt, long paramLong);
/*     */   
/*     */   public static void glClientActiveTexture(int texture) {
/* 126 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 127 */     long function_pointer = caps.glClientActiveTexture;
/* 128 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 129 */     (StateTracker.getReferences(caps)).glClientActiveTexture = texture - 33984;
/* 130 */     nglClientActiveTexture(texture, function_pointer);
/*     */   }
/*     */   static native void nglClientActiveTexture(int paramInt, long paramLong);
/*     */   
/*     */   public static void glCompressedTexImage1D(int target, int level, int internalformat, int width, int border, ByteBuffer data) {
/* 135 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 136 */     long function_pointer = caps.glCompressedTexImage1D;
/* 137 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 138 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 139 */     BufferChecks.checkDirect(data);
/* 140 */     nglCompressedTexImage1D(target, level, internalformat, width, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glCompressedTexImage1D(int target, int level, int internalformat, int width, int border, int data_imageSize, long data_buffer_offset) {
/* 144 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 145 */     long function_pointer = caps.glCompressedTexImage1D;
/* 146 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 147 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 148 */     nglCompressedTexImage1DBO(target, level, internalformat, width, border, data_imageSize, data_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*     */   static native void nglCompressedTexImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glCompressedTexImage1D(int target, int level, int internalformat, int width, int border, int imageSize) {
/* 154 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 155 */     long function_pointer = caps.glCompressedTexImage1D;
/* 156 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 157 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 158 */     nglCompressedTexImage1D(target, level, internalformat, width, border, imageSize, 0L, function_pointer);
/*     */   }
/*     */   
/*     */   public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, ByteBuffer data) {
/* 162 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 163 */     long function_pointer = caps.glCompressedTexImage2D;
/* 164 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 165 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 166 */     BufferChecks.checkDirect(data);
/* 167 */     nglCompressedTexImage2D(target, level, internalformat, width, height, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*     */   public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int data_imageSize, long data_buffer_offset) {
/* 171 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 172 */     long function_pointer = caps.glCompressedTexImage2D;
/* 173 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 174 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 175 */     nglCompressedTexImage2DBO(target, level, internalformat, width, height, border, data_imageSize, data_buffer_offset, function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglCompressedTexImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize) {
/* 181 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 182 */     long function_pointer = caps.glCompressedTexImage2D;
/* 183 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 184 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 185 */     nglCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, 0L, function_pointer);
/*     */   }
/*     */   
/*     */   public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, ByteBuffer data) {
/* 189 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 190 */     long function_pointer = caps.glCompressedTexImage3D;
/* 191 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 192 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 193 */     BufferChecks.checkDirect(data);
/* 194 */     nglCompressedTexImage3D(target, level, internalformat, width, height, depth, border, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*     */   public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int data_imageSize, long data_buffer_offset) {
/* 198 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 199 */     long function_pointer = caps.glCompressedTexImage3D;
/* 200 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 201 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 202 */     nglCompressedTexImage3DBO(target, level, internalformat, width, height, depth, border, data_imageSize, data_buffer_offset, function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglCompressedTexImage3DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glCompressedTexImage3D(int target, int level, int internalformat, int width, int height, int depth, int border, int imageSize) {
/* 208 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 209 */     long function_pointer = caps.glCompressedTexImage3D;
/* 210 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 211 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 212 */     nglCompressedTexImage3D(target, level, internalformat, width, height, depth, border, imageSize, 0L, function_pointer);
/*     */   }
/*     */   
/*     */   public static void glCompressedTexSubImage1D(int target, int level, int xoffset, int width, int format, ByteBuffer data) {
/* 216 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 217 */     long function_pointer = caps.glCompressedTexSubImage1D;
/* 218 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 219 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 220 */     BufferChecks.checkDirect(data);
/* 221 */     nglCompressedTexSubImage1D(target, level, xoffset, width, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*     */   public static void glCompressedTexSubImage1D(int target, int level, int xoffset, int width, int format, int data_imageSize, long data_buffer_offset) {
/* 225 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 226 */     long function_pointer = caps.glCompressedTexSubImage1D;
/* 227 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 228 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 229 */     nglCompressedTexSubImage1DBO(target, level, xoffset, width, format, data_imageSize, data_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexSubImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
/* 234 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 235 */     long function_pointer = caps.glCompressedTexSubImage2D;
/* 236 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 237 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 238 */     BufferChecks.checkDirect(data);
/* 239 */     nglCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*     */   public static void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data_buffer_offset) {
/* 243 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 244 */     long function_pointer = caps.glCompressedTexSubImage2D;
/* 245 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 246 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 247 */     nglCompressedTexSubImage2DBO(target, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexSubImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, ByteBuffer data) {
/* 252 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 253 */     long function_pointer = caps.glCompressedTexSubImage3D;
/* 254 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 255 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 256 */     BufferChecks.checkDirect(data);
/* 257 */     nglCompressedTexSubImage3D(target, level, xoffset, yoffset, zoffset, width, height, depth, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexSubImage3D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*     */   public static void glCompressedTexSubImage3D(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int data_imageSize, long data_buffer_offset) {
/* 261 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 262 */     long function_pointer = caps.glCompressedTexSubImage3D;
/* 263 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 264 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 265 */     nglCompressedTexSubImage3DBO(target, level, xoffset, yoffset, zoffset, width, height, depth, format, data_imageSize, data_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglCompressedTexSubImage3DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glGetCompressedTexImage(int target, int lod, ByteBuffer img) {
/* 270 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 271 */     long function_pointer = caps.glGetCompressedTexImage;
/* 272 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 273 */     GLChecks.ensurePackPBOdisabled(caps);
/* 274 */     BufferChecks.checkDirect(img);
/* 275 */     nglGetCompressedTexImage(target, lod, MemoryUtil.getAddress(img), function_pointer);
/*     */   }
/*     */   public static void glGetCompressedTexImage(int target, int lod, IntBuffer img) {
/* 278 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 279 */     long function_pointer = caps.glGetCompressedTexImage;
/* 280 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 281 */     GLChecks.ensurePackPBOdisabled(caps);
/* 282 */     BufferChecks.checkDirect(img);
/* 283 */     nglGetCompressedTexImage(target, lod, MemoryUtil.getAddress(img), function_pointer);
/*     */   }
/*     */   public static void glGetCompressedTexImage(int target, int lod, ShortBuffer img) {
/* 286 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 287 */     long function_pointer = caps.glGetCompressedTexImage;
/* 288 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 289 */     GLChecks.ensurePackPBOdisabled(caps);
/* 290 */     BufferChecks.checkDirect(img);
/* 291 */     nglGetCompressedTexImage(target, lod, MemoryUtil.getAddress(img), function_pointer);
/*     */   }
/*     */   static native void nglGetCompressedTexImage(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   public static void glGetCompressedTexImage(int target, int lod, long img_buffer_offset) {
/* 295 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 296 */     long function_pointer = caps.glGetCompressedTexImage;
/* 297 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 298 */     GLChecks.ensurePackPBOenabled(caps);
/* 299 */     nglGetCompressedTexImageBO(target, lod, img_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglGetCompressedTexImageBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultiTexCoord1f(int target, float s) {
/* 304 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 305 */     long function_pointer = caps.glMultiTexCoord1f;
/* 306 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 307 */     nglMultiTexCoord1f(target, s, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord1f(int paramInt, float paramFloat, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord1d(int target, double s) {
/* 312 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 313 */     long function_pointer = caps.glMultiTexCoord1d;
/* 314 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 315 */     nglMultiTexCoord1d(target, s, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord1d(int paramInt, double paramDouble, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord2f(int target, float s, float t) {
/* 320 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 321 */     long function_pointer = caps.glMultiTexCoord2f;
/* 322 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 323 */     nglMultiTexCoord2f(target, s, t, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord2f(int paramInt, float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord2d(int target, double s, double t) {
/* 328 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 329 */     long function_pointer = caps.glMultiTexCoord2d;
/* 330 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 331 */     nglMultiTexCoord2d(target, s, t, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord2d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord3f(int target, float s, float t, float r) {
/* 336 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 337 */     long function_pointer = caps.glMultiTexCoord3f;
/* 338 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 339 */     nglMultiTexCoord3f(target, s, t, r, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord3f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord3d(int target, double s, double t, double r) {
/* 344 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 345 */     long function_pointer = caps.glMultiTexCoord3d;
/* 346 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 347 */     nglMultiTexCoord3d(target, s, t, r, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
/* 352 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 353 */     long function_pointer = caps.glMultiTexCoord4f;
/* 354 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 355 */     nglMultiTexCoord4f(target, s, t, r, q, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord4f(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*     */   
/*     */   public static void glMultiTexCoord4d(int target, double s, double t, double r, double q) {
/* 360 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 361 */     long function_pointer = caps.glMultiTexCoord4d;
/* 362 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 363 */     nglMultiTexCoord4d(target, s, t, r, q, function_pointer);
/*     */   }
/*     */   static native void nglMultiTexCoord4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*     */   
/*     */   public static void glLoadTransposeMatrix(FloatBuffer m) {
/* 368 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 369 */     long function_pointer = caps.glLoadTransposeMatrixf;
/* 370 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 371 */     BufferChecks.checkBuffer(m, 16);
/* 372 */     nglLoadTransposeMatrixf(MemoryUtil.getAddress(m), function_pointer);
/*     */   }
/*     */   static native void nglLoadTransposeMatrixf(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glLoadTransposeMatrix(DoubleBuffer m) {
/* 377 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 378 */     long function_pointer = caps.glLoadTransposeMatrixd;
/* 379 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 380 */     BufferChecks.checkBuffer(m, 16);
/* 381 */     nglLoadTransposeMatrixd(MemoryUtil.getAddress(m), function_pointer);
/*     */   }
/*     */   static native void nglLoadTransposeMatrixd(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultTransposeMatrix(FloatBuffer m) {
/* 386 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 387 */     long function_pointer = caps.glMultTransposeMatrixf;
/* 388 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 389 */     BufferChecks.checkBuffer(m, 16);
/* 390 */     nglMultTransposeMatrixf(MemoryUtil.getAddress(m), function_pointer);
/*     */   }
/*     */   static native void nglMultTransposeMatrixf(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glMultTransposeMatrix(DoubleBuffer m) {
/* 395 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 396 */     long function_pointer = caps.glMultTransposeMatrixd;
/* 397 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 398 */     BufferChecks.checkBuffer(m, 16);
/* 399 */     nglMultTransposeMatrixd(MemoryUtil.getAddress(m), function_pointer);
/*     */   }
/*     */   static native void nglMultTransposeMatrixd(long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glSampleCoverage(float value, boolean invert) {
/* 404 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 405 */     long function_pointer = caps.glSampleCoverage;
/* 406 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 407 */     nglSampleCoverage(value, invert, function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglSampleCoverage(float paramFloat, boolean paramBoolean, long paramLong);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL13.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */