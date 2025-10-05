/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.BufferChecks;
/*     */ import org.lwjgl.LWJGLUtil;
/*     */ import org.lwjgl.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GL41
/*     */ {
/*     */   public static final int GL_SHADER_COMPILER = 36346;
/*     */   public static final int GL_NUM_SHADER_BINARY_FORMATS = 36345;
/*     */   public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 36347;
/*     */   public static final int GL_MAX_VARYING_VECTORS = 36348;
/*     */   public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 36349;
/*     */   public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 35738;
/*     */   public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 35739;
/*     */   public static final int GL_FIXED = 5132;
/*     */   public static final int GL_LOW_FLOAT = 36336;
/*     */   public static final int GL_MEDIUM_FLOAT = 36337;
/*     */   public static final int GL_HIGH_FLOAT = 36338;
/*     */   public static final int GL_LOW_INT = 36339;
/*     */   public static final int GL_MEDIUM_INT = 36340;
/*     */   public static final int GL_HIGH_INT = 36341;
/*     */   public static final int GL_RGB565 = 36194;
/*     */   public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 33367;
/*     */   public static final int GL_PROGRAM_BINARY_LENGTH = 34625;
/*     */   public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 34814;
/*     */   public static final int GL_PROGRAM_BINARY_FORMATS = 34815;
/*     */   public static final int GL_VERTEX_SHADER_BIT = 1;
/*     */   public static final int GL_FRAGMENT_SHADER_BIT = 2;
/*     */   public static final int GL_GEOMETRY_SHADER_BIT = 4;
/*     */   public static final int GL_TESS_CONTROL_SHADER_BIT = 8;
/*     */   public static final int GL_TESS_EVALUATION_SHADER_BIT = 16;
/*     */   public static final int GL_ALL_SHADER_BITS = -1;
/*     */   public static final int GL_PROGRAM_SEPARABLE = 33368;
/*     */   public static final int GL_ACTIVE_PROGRAM = 33369;
/*     */   public static final int GL_PROGRAM_PIPELINE_BINDING = 33370;
/*     */   public static final int GL_MAX_VIEWPORTS = 33371;
/*     */   public static final int GL_VIEWPORT_SUBPIXEL_BITS = 33372;
/*     */   public static final int GL_VIEWPORT_BOUNDS_RANGE = 33373;
/*     */   public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
/*     */   public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 33375;
/*     */   public static final int GL_UNDEFINED_VERTEX = 33376;
/*     */   
/*     */   public static void glReleaseShaderCompiler() {
/* 107 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 108 */     long function_pointer = caps.glReleaseShaderCompiler;
/* 109 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 110 */     nglReleaseShaderCompiler(function_pointer);
/*     */   }
/*     */   static native void nglReleaseShaderCompiler(long paramLong);
/*     */   
/*     */   public static void glShaderBinary(IntBuffer shaders, int binaryformat, ByteBuffer binary) {
/* 115 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 116 */     long function_pointer = caps.glShaderBinary;
/* 117 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 118 */     BufferChecks.checkDirect(shaders);
/* 119 */     BufferChecks.checkDirect(binary);
/* 120 */     nglShaderBinary(shaders.remaining(), MemoryUtil.getAddress(shaders), binaryformat, MemoryUtil.getAddress(binary), binary.remaining(), function_pointer);
/*     */   }
/*     */   static native void nglShaderBinary(int paramInt1, long paramLong1, int paramInt2, long paramLong2, int paramInt3, long paramLong3);
/*     */   
/*     */   public static void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
/* 125 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 126 */     long function_pointer = caps.glGetShaderPrecisionFormat;
/* 127 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 128 */     BufferChecks.checkBuffer(range, 2);
/* 129 */     BufferChecks.checkBuffer(precision, 1);
/* 130 */     nglGetShaderPrecisionFormat(shadertype, precisiontype, MemoryUtil.getAddress(range), MemoryUtil.getAddress(precision), function_pointer);
/*     */   }
/*     */   static native void nglGetShaderPrecisionFormat(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   public static void glDepthRangef(float n, float f) {
/* 135 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 136 */     long function_pointer = caps.glDepthRangef;
/* 137 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 138 */     nglDepthRangef(n, f, function_pointer);
/*     */   }
/*     */   static native void nglDepthRangef(float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public static void glClearDepthf(float d) {
/* 143 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 144 */     long function_pointer = caps.glClearDepthf;
/* 145 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 146 */     nglClearDepthf(d, function_pointer);
/*     */   }
/*     */   static native void nglClearDepthf(float paramFloat, long paramLong);
/*     */   
/*     */   public static void glGetProgramBinary(int program, IntBuffer length, IntBuffer binaryFormat, ByteBuffer binary) {
/* 151 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 152 */     long function_pointer = caps.glGetProgramBinary;
/* 153 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 154 */     if (length != null)
/* 155 */       BufferChecks.checkBuffer(length, 1); 
/* 156 */     BufferChecks.checkBuffer(binaryFormat, 1);
/* 157 */     BufferChecks.checkDirect(binary);
/* 158 */     nglGetProgramBinary(program, binary.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(binaryFormat), MemoryUtil.getAddress(binary), function_pointer);
/*     */   }
/*     */   static native void nglGetProgramBinary(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*     */   
/*     */   public static void glProgramBinary(int program, int binaryFormat, ByteBuffer binary) {
/* 163 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 164 */     long function_pointer = caps.glProgramBinary;
/* 165 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 166 */     BufferChecks.checkDirect(binary);
/* 167 */     nglProgramBinary(program, binaryFormat, MemoryUtil.getAddress(binary), binary.remaining(), function_pointer);
/*     */   }
/*     */   static native void nglProgramBinary(int paramInt1, int paramInt2, long paramLong1, int paramInt3, long paramLong2);
/*     */   
/*     */   public static void glProgramParameteri(int program, int pname, int value) {
/* 172 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 173 */     long function_pointer = caps.glProgramParameteri;
/* 174 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 175 */     nglProgramParameteri(program, pname, value, function_pointer);
/*     */   }
/*     */   static native void nglProgramParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glUseProgramStages(int pipeline, int stages, int program) {
/* 180 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 181 */     long function_pointer = caps.glUseProgramStages;
/* 182 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 183 */     nglUseProgramStages(pipeline, stages, program, function_pointer);
/*     */   }
/*     */   static native void nglUseProgramStages(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glActiveShaderProgram(int pipeline, int program) {
/* 188 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 189 */     long function_pointer = caps.glActiveShaderProgram;
/* 190 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 191 */     nglActiveShaderProgram(pipeline, program, function_pointer);
/*     */   }
/*     */ 
/*     */   
/*     */   static native void nglActiveShaderProgram(int paramInt1, int paramInt2, long paramLong);
/*     */ 
/*     */   
/*     */   public static int glCreateShaderProgram(int type, ByteBuffer string) {
/* 199 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 200 */     long function_pointer = caps.glCreateShaderProgramv;
/* 201 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 202 */     BufferChecks.checkDirect(string);
/* 203 */     BufferChecks.checkNullTerminated(string);
/* 204 */     int __result = nglCreateShaderProgramv(type, 1, MemoryUtil.getAddress(string), function_pointer);
/* 205 */     return __result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static native int nglCreateShaderProgramv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   public static int glCreateShaderProgram(int type, int count, ByteBuffer strings) {
/* 215 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 216 */     long function_pointer = caps.glCreateShaderProgramv;
/* 217 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 218 */     BufferChecks.checkDirect(strings);
/* 219 */     BufferChecks.checkNullTerminated(strings, count);
/* 220 */     int __result = nglCreateShaderProgramv2(type, count, MemoryUtil.getAddress(strings), function_pointer);
/* 221 */     return __result;
/*     */   }
/*     */   
/*     */   static native int nglCreateShaderProgramv2(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glCreateShaderProgram(int type, ByteBuffer[] strings) {
/* 227 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 228 */     long function_pointer = caps.glCreateShaderProgramv;
/* 229 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 230 */     BufferChecks.checkArray((Object[])strings, 1);
/* 231 */     int __result = nglCreateShaderProgramv3(type, strings.length, strings, function_pointer);
/* 232 */     return __result;
/*     */   }
/*     */   
/*     */   static native int nglCreateShaderProgramv3(int paramInt1, int paramInt2, ByteBuffer[] paramArrayOfByteBuffer, long paramLong);
/*     */   
/*     */   public static int glCreateShaderProgram(int type, CharSequence string) {
/* 238 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 239 */     long function_pointer = caps.glCreateShaderProgramv;
/* 240 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 241 */     int __result = nglCreateShaderProgramv(type, 1, APIUtil.getBufferNT(caps, string), function_pointer);
/* 242 */     return __result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateShaderProgram(int type, CharSequence[] strings) {
/* 247 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 248 */     long function_pointer = caps.glCreateShaderProgramv;
/* 249 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 250 */     BufferChecks.checkArray((Object[])strings);
/* 251 */     int __result = nglCreateShaderProgramv2(type, strings.length, APIUtil.getBufferNT(caps, strings), function_pointer);
/* 252 */     return __result;
/*     */   }
/*     */   
/*     */   public static void glBindProgramPipeline(int pipeline) {
/* 256 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 257 */     long function_pointer = caps.glBindProgramPipeline;
/* 258 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 259 */     nglBindProgramPipeline(pipeline, function_pointer);
/*     */   }
/*     */   static native void nglBindProgramPipeline(int paramInt, long paramLong);
/*     */   
/*     */   public static void glDeleteProgramPipelines(IntBuffer pipelines) {
/* 264 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 265 */     long function_pointer = caps.glDeleteProgramPipelines;
/* 266 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 267 */     BufferChecks.checkDirect(pipelines);
/* 268 */     nglDeleteProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglDeleteProgramPipelines(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDeleteProgramPipelines(int pipeline) {
/* 274 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 275 */     long function_pointer = caps.glDeleteProgramPipelines;
/* 276 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 277 */     nglDeleteProgramPipelines(1, APIUtil.getInt(caps, pipeline), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glGenProgramPipelines(IntBuffer pipelines) {
/* 281 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 282 */     long function_pointer = caps.glGenProgramPipelines;
/* 283 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 284 */     BufferChecks.checkDirect(pipelines);
/* 285 */     nglGenProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGenProgramPipelines(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGenProgramPipelines() {
/* 291 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 292 */     long function_pointer = caps.glGenProgramPipelines;
/* 293 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 294 */     IntBuffer pipelines = APIUtil.getBufferInt(caps);
/* 295 */     nglGenProgramPipelines(1, MemoryUtil.getAddress(pipelines), function_pointer);
/* 296 */     return pipelines.get(0);
/*     */   }
/*     */   
/*     */   public static boolean glIsProgramPipeline(int pipeline) {
/* 300 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 301 */     long function_pointer = caps.glIsProgramPipeline;
/* 302 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 303 */     boolean __result = nglIsProgramPipeline(pipeline, function_pointer);
/* 304 */     return __result;
/*     */   }
/*     */   static native boolean nglIsProgramPipeline(int paramInt, long paramLong);
/*     */   
/*     */   public static void glGetProgramPipeline(int pipeline, int pname, IntBuffer params) {
/* 309 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 310 */     long function_pointer = caps.glGetProgramPipelineiv;
/* 311 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 312 */     BufferChecks.checkBuffer(params, 1);
/* 313 */     nglGetProgramPipelineiv(pipeline, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetProgramPipelineiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static int glGetProgramPipelinei(int pipeline, int pname) {
/* 319 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 320 */     long function_pointer = caps.glGetProgramPipelineiv;
/* 321 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 322 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 323 */     nglGetProgramPipelineiv(pipeline, pname, MemoryUtil.getAddress(params), function_pointer);
/* 324 */     return params.get(0);
/*     */   }
/*     */   
/*     */   public static void glProgramUniform1i(int program, int location, int v0) {
/* 328 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 329 */     long function_pointer = caps.glProgramUniform1i;
/* 330 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 331 */     nglProgramUniform1i(program, location, v0, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1i(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glProgramUniform2i(int program, int location, int v0, int v1) {
/* 336 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 337 */     long function_pointer = caps.glProgramUniform2i;
/* 338 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 339 */     nglProgramUniform2i(program, location, v0, v1, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*     */   
/*     */   public static void glProgramUniform3i(int program, int location, int v0, int v1, int v2) {
/* 344 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 345 */     long function_pointer = caps.glProgramUniform3i;
/* 346 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 347 */     nglProgramUniform3i(program, location, v0, v1, v2, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*     */   
/*     */   public static void glProgramUniform4i(int program, int location, int v0, int v1, int v2, int v3) {
/* 352 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 353 */     long function_pointer = caps.glProgramUniform4i;
/* 354 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 355 */     nglProgramUniform4i(program, location, v0, v1, v2, v3, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*     */   
/*     */   public static void glProgramUniform1f(int program, int location, float v0) {
/* 360 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 361 */     long function_pointer = caps.glProgramUniform1f;
/* 362 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 363 */     nglProgramUniform1f(program, location, v0, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1f(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*     */   
/*     */   public static void glProgramUniform2f(int program, int location, float v0, float v1) {
/* 368 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 369 */     long function_pointer = caps.glProgramUniform2f;
/* 370 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 371 */     nglProgramUniform2f(program, location, v0, v1, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public static void glProgramUniform3f(int program, int location, float v0, float v1, float v2) {
/* 376 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 377 */     long function_pointer = caps.glProgramUniform3f;
/* 378 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 379 */     nglProgramUniform3f(program, location, v0, v1, v2, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*     */   
/*     */   public static void glProgramUniform4f(int program, int location, float v0, float v1, float v2, float v3) {
/* 384 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 385 */     long function_pointer = caps.glProgramUniform4f;
/* 386 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 387 */     nglProgramUniform4f(program, location, v0, v1, v2, v3, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4f(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*     */   
/*     */   public static void glProgramUniform1d(int program, int location, double v0) {
/* 392 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 393 */     long function_pointer = caps.glProgramUniform1d;
/* 394 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 395 */     nglProgramUniform1d(program, location, v0, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1d(int paramInt1, int paramInt2, double paramDouble, long paramLong);
/*     */   
/*     */   public static void glProgramUniform2d(int program, int location, double v0, double v1) {
/* 400 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 401 */     long function_pointer = caps.glProgramUniform2d;
/* 402 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 403 */     nglProgramUniform2d(program, location, v0, v1, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2d(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, long paramLong);
/*     */   
/*     */   public static void glProgramUniform3d(int program, int location, double v0, double v1, double v2) {
/* 408 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 409 */     long function_pointer = caps.glProgramUniform3d;
/* 410 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 411 */     nglProgramUniform3d(program, location, v0, v1, v2, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3d(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*     */   
/*     */   public static void glProgramUniform4d(int program, int location, double v0, double v1, double v2, double v3) {
/* 416 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 417 */     long function_pointer = caps.glProgramUniform4d;
/* 418 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 419 */     nglProgramUniform4d(program, location, v0, v1, v2, v3, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4d(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*     */   
/*     */   public static void glProgramUniform1(int program, int location, IntBuffer value) {
/* 424 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 425 */     long function_pointer = caps.glProgramUniform1iv;
/* 426 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 427 */     BufferChecks.checkDirect(value);
/* 428 */     nglProgramUniform1iv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1iv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform2(int program, int location, IntBuffer value) {
/* 433 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 434 */     long function_pointer = caps.glProgramUniform2iv;
/* 435 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 436 */     BufferChecks.checkDirect(value);
/* 437 */     nglProgramUniform2iv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2iv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform3(int program, int location, IntBuffer value) {
/* 442 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 443 */     long function_pointer = caps.glProgramUniform3iv;
/* 444 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 445 */     BufferChecks.checkDirect(value);
/* 446 */     nglProgramUniform3iv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3iv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform4(int program, int location, IntBuffer value) {
/* 451 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 452 */     long function_pointer = caps.glProgramUniform4iv;
/* 453 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 454 */     BufferChecks.checkDirect(value);
/* 455 */     nglProgramUniform4iv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4iv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform1(int program, int location, FloatBuffer value) {
/* 460 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 461 */     long function_pointer = caps.glProgramUniform1fv;
/* 462 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 463 */     BufferChecks.checkDirect(value);
/* 464 */     nglProgramUniform1fv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1fv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform2(int program, int location, FloatBuffer value) {
/* 469 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 470 */     long function_pointer = caps.glProgramUniform2fv;
/* 471 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 472 */     BufferChecks.checkDirect(value);
/* 473 */     nglProgramUniform2fv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2fv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform3(int program, int location, FloatBuffer value) {
/* 478 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 479 */     long function_pointer = caps.glProgramUniform3fv;
/* 480 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 481 */     BufferChecks.checkDirect(value);
/* 482 */     nglProgramUniform3fv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3fv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform4(int program, int location, FloatBuffer value) {
/* 487 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 488 */     long function_pointer = caps.glProgramUniform4fv;
/* 489 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 490 */     BufferChecks.checkDirect(value);
/* 491 */     nglProgramUniform4fv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4fv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform1(int program, int location, DoubleBuffer value) {
/* 496 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 497 */     long function_pointer = caps.glProgramUniform1dv;
/* 498 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 499 */     BufferChecks.checkDirect(value);
/* 500 */     nglProgramUniform1dv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1dv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform2(int program, int location, DoubleBuffer value) {
/* 505 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 506 */     long function_pointer = caps.glProgramUniform2dv;
/* 507 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 508 */     BufferChecks.checkDirect(value);
/* 509 */     nglProgramUniform2dv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2dv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform3(int program, int location, DoubleBuffer value) {
/* 514 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 515 */     long function_pointer = caps.glProgramUniform3dv;
/* 516 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 517 */     BufferChecks.checkDirect(value);
/* 518 */     nglProgramUniform3dv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3dv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform4(int program, int location, DoubleBuffer value) {
/* 523 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 524 */     long function_pointer = caps.glProgramUniform4dv;
/* 525 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 526 */     BufferChecks.checkDirect(value);
/* 527 */     nglProgramUniform4dv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4dv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform1ui(int program, int location, int v0) {
/* 532 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 533 */     long function_pointer = caps.glProgramUniform1ui;
/* 534 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 535 */     nglProgramUniform1ui(program, location, v0, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1ui(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*     */   
/*     */   public static void glProgramUniform2ui(int program, int location, int v0, int v1) {
/* 540 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 541 */     long function_pointer = caps.glProgramUniform2ui;
/* 542 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 543 */     nglProgramUniform2ui(program, location, v0, v1, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*     */   
/*     */   public static void glProgramUniform3ui(int program, int location, int v0, int v1, int v2) {
/* 548 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 549 */     long function_pointer = caps.glProgramUniform3ui;
/* 550 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 551 */     nglProgramUniform3ui(program, location, v0, v1, v2, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*     */   
/*     */   public static void glProgramUniform4ui(int program, int location, int v0, int v1, int v2, int v3) {
/* 556 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 557 */     long function_pointer = caps.glProgramUniform4ui;
/* 558 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 559 */     nglProgramUniform4ui(program, location, v0, v1, v2, v3, function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4ui(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*     */   
/*     */   public static void glProgramUniform1u(int program, int location, IntBuffer value) {
/* 564 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 565 */     long function_pointer = caps.glProgramUniform1uiv;
/* 566 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 567 */     BufferChecks.checkDirect(value);
/* 568 */     nglProgramUniform1uiv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform1uiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform2u(int program, int location, IntBuffer value) {
/* 573 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 574 */     long function_pointer = caps.glProgramUniform2uiv;
/* 575 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 576 */     BufferChecks.checkDirect(value);
/* 577 */     nglProgramUniform2uiv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform2uiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform3u(int program, int location, IntBuffer value) {
/* 582 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 583 */     long function_pointer = caps.glProgramUniform3uiv;
/* 584 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 585 */     BufferChecks.checkDirect(value);
/* 586 */     nglProgramUniform3uiv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform3uiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniform4u(int program, int location, IntBuffer value) {
/* 591 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 592 */     long function_pointer = caps.glProgramUniform4uiv;
/* 593 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 594 */     BufferChecks.checkDirect(value);
/* 595 */     nglProgramUniform4uiv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniform4uiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2(int program, int location, boolean transpose, FloatBuffer value) {
/* 600 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 601 */     long function_pointer = caps.glProgramUniformMatrix2fv;
/* 602 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 603 */     BufferChecks.checkDirect(value);
/* 604 */     nglProgramUniformMatrix2fv(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3(int program, int location, boolean transpose, FloatBuffer value) {
/* 609 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 610 */     long function_pointer = caps.glProgramUniformMatrix3fv;
/* 611 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 612 */     BufferChecks.checkDirect(value);
/* 613 */     nglProgramUniformMatrix3fv(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4(int program, int location, boolean transpose, FloatBuffer value) {
/* 618 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 619 */     long function_pointer = caps.glProgramUniformMatrix4fv;
/* 620 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 621 */     BufferChecks.checkDirect(value);
/* 622 */     nglProgramUniformMatrix4fv(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2(int program, int location, boolean transpose, DoubleBuffer value) {
/* 627 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 628 */     long function_pointer = caps.glProgramUniformMatrix2dv;
/* 629 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 630 */     BufferChecks.checkDirect(value);
/* 631 */     nglProgramUniformMatrix2dv(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3(int program, int location, boolean transpose, DoubleBuffer value) {
/* 636 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 637 */     long function_pointer = caps.glProgramUniformMatrix3dv;
/* 638 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 639 */     BufferChecks.checkDirect(value);
/* 640 */     nglProgramUniformMatrix3dv(program, location, value.remaining() / 9, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4(int program, int location, boolean transpose, DoubleBuffer value) {
/* 645 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 646 */     long function_pointer = caps.glProgramUniformMatrix4dv;
/* 647 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 648 */     BufferChecks.checkDirect(value);
/* 649 */     nglProgramUniformMatrix4dv(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, FloatBuffer value) {
/* 654 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 655 */     long function_pointer = caps.glProgramUniformMatrix2x3fv;
/* 656 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 657 */     BufferChecks.checkDirect(value);
/* 658 */     nglProgramUniformMatrix2x3fv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2x3fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, FloatBuffer value) {
/* 663 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 664 */     long function_pointer = caps.glProgramUniformMatrix3x2fv;
/* 665 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 666 */     BufferChecks.checkDirect(value);
/* 667 */     nglProgramUniformMatrix3x2fv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3x2fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, FloatBuffer value) {
/* 672 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 673 */     long function_pointer = caps.glProgramUniformMatrix2x4fv;
/* 674 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 675 */     BufferChecks.checkDirect(value);
/* 676 */     nglProgramUniformMatrix2x4fv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2x4fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, FloatBuffer value) {
/* 681 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 682 */     long function_pointer = caps.glProgramUniformMatrix4x2fv;
/* 683 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 684 */     BufferChecks.checkDirect(value);
/* 685 */     nglProgramUniformMatrix4x2fv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4x2fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, FloatBuffer value) {
/* 690 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 691 */     long function_pointer = caps.glProgramUniformMatrix3x4fv;
/* 692 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 693 */     BufferChecks.checkDirect(value);
/* 694 */     nglProgramUniformMatrix3x4fv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3x4fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, FloatBuffer value) {
/* 699 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 700 */     long function_pointer = caps.glProgramUniformMatrix4x3fv;
/* 701 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 702 */     BufferChecks.checkDirect(value);
/* 703 */     nglProgramUniformMatrix4x3fv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4x3fv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, DoubleBuffer value) {
/* 708 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 709 */     long function_pointer = caps.glProgramUniformMatrix2x3dv;
/* 710 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 711 */     BufferChecks.checkDirect(value);
/* 712 */     nglProgramUniformMatrix2x3dv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2x3dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, DoubleBuffer value) {
/* 717 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 718 */     long function_pointer = caps.glProgramUniformMatrix3x2dv;
/* 719 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 720 */     BufferChecks.checkDirect(value);
/* 721 */     nglProgramUniformMatrix3x2dv(program, location, value.remaining() / 6, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3x2dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, DoubleBuffer value) {
/* 726 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 727 */     long function_pointer = caps.glProgramUniformMatrix2x4dv;
/* 728 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 729 */     BufferChecks.checkDirect(value);
/* 730 */     nglProgramUniformMatrix2x4dv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix2x4dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, DoubleBuffer value) {
/* 735 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 736 */     long function_pointer = caps.glProgramUniformMatrix4x2dv;
/* 737 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 738 */     BufferChecks.checkDirect(value);
/* 739 */     nglProgramUniformMatrix4x2dv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4x2dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, DoubleBuffer value) {
/* 744 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 745 */     long function_pointer = caps.glProgramUniformMatrix3x4dv;
/* 746 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 747 */     BufferChecks.checkDirect(value);
/* 748 */     nglProgramUniformMatrix3x4dv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix3x4dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, DoubleBuffer value) {
/* 753 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 754 */     long function_pointer = caps.glProgramUniformMatrix4x3dv;
/* 755 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 756 */     BufferChecks.checkDirect(value);
/* 757 */     nglProgramUniformMatrix4x3dv(program, location, value.remaining() / 12, transpose, MemoryUtil.getAddress(value), function_pointer);
/*     */   }
/*     */   static native void nglProgramUniformMatrix4x3dv(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glValidateProgramPipeline(int pipeline) {
/* 762 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 763 */     long function_pointer = caps.glValidateProgramPipeline;
/* 764 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 765 */     nglValidateProgramPipeline(pipeline, function_pointer);
/*     */   }
/*     */   static native void nglValidateProgramPipeline(int paramInt, long paramLong);
/*     */   
/*     */   public static void glGetProgramPipelineInfoLog(int pipeline, IntBuffer length, ByteBuffer infoLog) {
/* 770 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 771 */     long function_pointer = caps.glGetProgramPipelineInfoLog;
/* 772 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 773 */     if (length != null)
/* 774 */       BufferChecks.checkBuffer(length, 1); 
/* 775 */     BufferChecks.checkDirect(infoLog);
/* 776 */     nglGetProgramPipelineInfoLog(pipeline, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetProgramPipelineInfoLog(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   public static String glGetProgramPipelineInfoLog(int pipeline, int bufSize) {
/* 782 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 783 */     long function_pointer = caps.glGetProgramPipelineInfoLog;
/* 784 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 785 */     IntBuffer infoLog_length = APIUtil.getLengths(caps);
/* 786 */     ByteBuffer infoLog = APIUtil.getBufferByte(caps, bufSize);
/* 787 */     nglGetProgramPipelineInfoLog(pipeline, bufSize, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
/* 788 */     infoLog.limit(infoLog_length.get(0));
/* 789 */     return APIUtil.getString(caps, infoLog);
/*     */   }
/*     */   
/*     */   public static void glVertexAttribL1d(int index, double x) {
/* 793 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 794 */     long function_pointer = caps.glVertexAttribL1d;
/* 795 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 796 */     nglVertexAttribL1d(index, x, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL1d(int paramInt, double paramDouble, long paramLong);
/*     */   
/*     */   public static void glVertexAttribL2d(int index, double x, double y) {
/* 801 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 802 */     long function_pointer = caps.glVertexAttribL2d;
/* 803 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 804 */     nglVertexAttribL2d(index, x, y, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL2d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
/*     */   
/*     */   public static void glVertexAttribL3d(int index, double x, double y, double z) {
/* 809 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 810 */     long function_pointer = caps.glVertexAttribL3d;
/* 811 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 812 */     nglVertexAttribL3d(index, x, y, z, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL3d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*     */   
/*     */   public static void glVertexAttribL4d(int index, double x, double y, double z, double w) {
/* 817 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 818 */     long function_pointer = caps.glVertexAttribL4d;
/* 819 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 820 */     nglVertexAttribL4d(index, x, y, z, w, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL4d(int paramInt, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*     */   
/*     */   public static void glVertexAttribL1(int index, DoubleBuffer v) {
/* 825 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 826 */     long function_pointer = caps.glVertexAttribL1dv;
/* 827 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 828 */     BufferChecks.checkBuffer(v, 1);
/* 829 */     nglVertexAttribL1dv(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL1dv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribL2(int index, DoubleBuffer v) {
/* 834 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 835 */     long function_pointer = caps.glVertexAttribL2dv;
/* 836 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 837 */     BufferChecks.checkBuffer(v, 2);
/* 838 */     nglVertexAttribL2dv(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL2dv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribL3(int index, DoubleBuffer v) {
/* 843 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 844 */     long function_pointer = caps.glVertexAttribL3dv;
/* 845 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 846 */     BufferChecks.checkBuffer(v, 3);
/* 847 */     nglVertexAttribL3dv(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL3dv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribL4(int index, DoubleBuffer v) {
/* 852 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 853 */     long function_pointer = caps.glVertexAttribL4dv;
/* 854 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 855 */     BufferChecks.checkBuffer(v, 4);
/* 856 */     nglVertexAttribL4dv(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribL4dv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glVertexAttribLPointer(int index, int size, int stride, DoubleBuffer pointer) {
/* 861 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 862 */     long function_pointer = caps.glVertexAttribLPointer;
/* 863 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 864 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 865 */     BufferChecks.checkDirect(pointer);
/* 866 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).glVertexAttribPointer_buffer[index] = pointer; 
/* 867 */     nglVertexAttribLPointer(index, size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*     */   }
/*     */   
/*     */   public static void glVertexAttribLPointer(int index, int size, int stride, long pointer_buffer_offset) {
/* 871 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 872 */     long function_pointer = caps.glVertexAttribLPointer;
/* 873 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 874 */     GLChecks.ensureArrayVBOenabled(caps);
/* 875 */     nglVertexAttribLPointerBO(index, size, 5130, stride, pointer_buffer_offset, function_pointer);
/*     */   }
/*     */   static native void nglVertexAttribLPointer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*     */   static native void nglVertexAttribLPointerBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*     */   public static void glGetVertexAttribL(int index, int pname, DoubleBuffer params) {
/* 880 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 881 */     long function_pointer = caps.glGetVertexAttribLdv;
/* 882 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 883 */     BufferChecks.checkBuffer(params, 4);
/* 884 */     nglGetVertexAttribLdv(index, pname, MemoryUtil.getAddress(params), function_pointer);
/*     */   }
/*     */   static native void nglGetVertexAttribLdv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glViewportArray(int first, FloatBuffer v) {
/* 889 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 890 */     long function_pointer = caps.glViewportArrayv;
/* 891 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 892 */     BufferChecks.checkDirect(v);
/* 893 */     nglViewportArrayv(first, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglViewportArrayv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glViewportIndexedf(int index, float x, float y, float w, float h) {
/* 898 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 899 */     long function_pointer = caps.glViewportIndexedf;
/* 900 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 901 */     nglViewportIndexedf(index, x, y, w, h, function_pointer);
/*     */   }
/*     */   static native void nglViewportIndexedf(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*     */   
/*     */   public static void glViewportIndexed(int index, FloatBuffer v) {
/* 906 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 907 */     long function_pointer = caps.glViewportIndexedfv;
/* 908 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 909 */     BufferChecks.checkBuffer(v, 4);
/* 910 */     nglViewportIndexedfv(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglViewportIndexedfv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glScissorArray(int first, IntBuffer v) {
/* 915 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 916 */     long function_pointer = caps.glScissorArrayv;
/* 917 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 918 */     BufferChecks.checkDirect(v);
/* 919 */     nglScissorArrayv(first, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglScissorArrayv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glScissorIndexed(int index, int left, int bottom, int width, int height) {
/* 924 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 925 */     long function_pointer = caps.glScissorIndexed;
/* 926 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 927 */     nglScissorIndexed(index, left, bottom, width, height, function_pointer);
/*     */   }
/*     */   static native void nglScissorIndexed(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*     */   
/*     */   public static void glScissorIndexed(int index, IntBuffer v) {
/* 932 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 933 */     long function_pointer = caps.glScissorIndexedv;
/* 934 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 935 */     BufferChecks.checkBuffer(v, 4);
/* 936 */     nglScissorIndexedv(index, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglScissorIndexedv(int paramInt, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDepthRangeArray(int first, DoubleBuffer v) {
/* 941 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 942 */     long function_pointer = caps.glDepthRangeArrayv;
/* 943 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 944 */     BufferChecks.checkDirect(v);
/* 945 */     nglDepthRangeArrayv(first, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
/*     */   }
/*     */   static native void nglDepthRangeArrayv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static void glDepthRangeIndexed(int index, double n, double f) {
/* 950 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 951 */     long function_pointer = caps.glDepthRangeIndexed;
/* 952 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 953 */     nglDepthRangeIndexed(index, n, f, function_pointer);
/*     */   }
/*     */   static native void nglDepthRangeIndexed(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
/*     */   
/*     */   public static void glGetFloat(int target, int index, FloatBuffer data) {
/* 958 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 959 */     long function_pointer = caps.glGetFloati_v;
/* 960 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 961 */     BufferChecks.checkDirect(data);
/* 962 */     nglGetFloati_v(target, index, MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetFloati_v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static float glGetFloat(int target, int index) {
/* 968 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 969 */     long function_pointer = caps.glGetFloati_v;
/* 970 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 971 */     FloatBuffer data = APIUtil.getBufferFloat(caps);
/* 972 */     nglGetFloati_v(target, index, MemoryUtil.getAddress(data), function_pointer);
/* 973 */     return data.get(0);
/*     */   }
/*     */   
/*     */   public static void glGetDouble(int target, int index, DoubleBuffer data) {
/* 977 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 978 */     long function_pointer = caps.glGetDoublei_v;
/* 979 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 980 */     BufferChecks.checkDirect(data);
/* 981 */     nglGetDoublei_v(target, index, MemoryUtil.getAddress(data), function_pointer);
/*     */   }
/*     */   
/*     */   static native void nglGetDoublei_v(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*     */   
/*     */   public static double glGetDouble(int target, int index) {
/* 987 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 988 */     long function_pointer = caps.glGetDoublei_v;
/* 989 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 990 */     DoubleBuffer data = APIUtil.getBufferDouble(caps);
/* 991 */     nglGetDoublei_v(target, index, MemoryUtil.getAddress(data), function_pointer);
/* 992 */     return data.get(0);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL41.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */