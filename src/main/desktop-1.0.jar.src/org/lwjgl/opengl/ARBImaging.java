/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.DoubleBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ import org.lwjgl.BufferChecks;
/*      */ import org.lwjgl.MemoryUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ARBImaging
/*      */ {
/*      */   public static final int GL_BLEND_COLOR = 32773;
/*      */   public static final int GL_FUNC_ADD = 32774;
/*      */   public static final int GL_MIN = 32775;
/*      */   public static final int GL_MAX = 32776;
/*      */   public static final int GL_BLEND_EQUATION = 32777;
/*      */   public static final int GL_FUNC_SUBTRACT = 32778;
/*      */   public static final int GL_FUNC_REVERSE_SUBTRACT = 32779;
/*      */   public static final int GL_COLOR_MATRIX = 32945;
/*      */   public static final int GL_COLOR_MATRIX_STACK_DEPTH = 32946;
/*      */   public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH = 32947;
/*      */   public static final int GL_POST_COLOR_MATRIX_RED_SCALE = 32948;
/*      */   public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE = 32949;
/*      */   public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE = 32950;
/*      */   public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE = 32951;
/*      */   public static final int GL_POST_COLOR_MATRIX_RED_BIAS = 32952;
/*      */   public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS = 32953;
/*      */   public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS = 32954;
/*      */   public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS = 32955;
/*      */   public static final int GL_COLOR_TABLE = 32976;
/*      */   public static final int GL_POST_CONVOLUTION_COLOR_TABLE = 32977;
/*      */   public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE = 32978;
/*      */   public static final int GL_PROXY_COLOR_TABLE = 32979;
/*      */   public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 32980;
/*      */   public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 32981;
/*      */   public static final int GL_COLOR_TABLE_SCALE = 32982;
/*      */   public static final int GL_COLOR_TABLE_BIAS = 32983;
/*      */   public static final int GL_COLOR_TABLE_FORMAT = 32984;
/*      */   public static final int GL_COLOR_TABLE_WIDTH = 32985;
/*      */   public static final int GL_COLOR_TABLE_RED_SIZE = 32986;
/*      */   public static final int GL_COLOR_TABLE_GREEN_SIZE = 32987;
/*      */   public static final int GL_COLOR_TABLE_BLUE_SIZE = 32988;
/*      */   public static final int GL_COLOR_TABLE_ALPHA_SIZE = 32989;
/*      */   public static final int GL_COLOR_TABLE_LUMINANCE_SIZE = 32990;
/*      */   public static final int GL_COLOR_TABLE_INTENSITY_SIZE = 32991;
/*      */   public static final int GL_CONVOLUTION_1D = 32784;
/*      */   public static final int GL_CONVOLUTION_2D = 32785;
/*      */   public static final int GL_SEPARABLE_2D = 32786;
/*      */   public static final int GL_CONVOLUTION_BORDER_MODE = 32787;
/*      */   public static final int GL_CONVOLUTION_FILTER_SCALE = 32788;
/*      */   public static final int GL_CONVOLUTION_FILTER_BIAS = 32789;
/*      */   public static final int GL_REDUCE = 32790;
/*      */   public static final int GL_CONVOLUTION_FORMAT = 32791;
/*      */   public static final int GL_CONVOLUTION_WIDTH = 32792;
/*      */   public static final int GL_CONVOLUTION_HEIGHT = 32793;
/*      */   public static final int GL_MAX_CONVOLUTION_WIDTH = 32794;
/*      */   public static final int GL_MAX_CONVOLUTION_HEIGHT = 32795;
/*      */   public static final int GL_POST_CONVOLUTION_RED_SCALE = 32796;
/*      */   public static final int GL_POST_CONVOLUTION_GREEN_SCALE = 32797;
/*      */   public static final int GL_POST_CONVOLUTION_BLUE_SCALE = 32798;
/*      */   public static final int GL_POST_CONVOLUTION_ALPHA_SCALE = 32799;
/*      */   public static final int GL_POST_CONVOLUTION_RED_BIAS = 32800;
/*      */   public static final int GL_POST_CONVOLUTION_GREEN_BIAS = 32801;
/*      */   public static final int GL_POST_CONVOLUTION_BLUE_BIAS = 32802;
/*      */   public static final int GL_POST_CONVOLUTION_ALPHA_BIAS = 32803;
/*      */   public static final int GL_IGNORE_BORDER = 33104;
/*      */   public static final int GL_CONSTANT_BORDER = 33105;
/*      */   public static final int GL_REPLICATE_BORDER = 33107;
/*      */   public static final int GL_CONVOLUTION_BORDER_COLOR = 33108;
/*      */   public static final int GL_HISTOGRAM = 32804;
/*      */   public static final int GL_PROXY_HISTOGRAM = 32805;
/*      */   public static final int GL_HISTOGRAM_WIDTH = 32806;
/*      */   public static final int GL_HISTOGRAM_FORMAT = 32807;
/*      */   public static final int GL_HISTOGRAM_RED_SIZE = 32808;
/*      */   public static final int GL_HISTOGRAM_GREEN_SIZE = 32809;
/*      */   public static final int GL_HISTOGRAM_BLUE_SIZE = 32810;
/*      */   public static final int GL_HISTOGRAM_ALPHA_SIZE = 32811;
/*      */   public static final int GL_HISTOGRAM_LUMINANCE_SIZE = 32812;
/*      */   public static final int GL_HISTOGRAM_SINK = 32813;
/*      */   public static final int GL_MINMAX = 32814;
/*      */   public static final int GL_MINMAX_FORMAT = 32815;
/*      */   public static final int GL_MINMAX_SINK = 32816;
/*      */   public static final int GL_TABLE_TOO_LARGE = 32817;
/*      */   
/*      */   public static void glColorTable(int target, int internalFormat, int width, int format, int type, ByteBuffer data) {
/*   94 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   95 */     long function_pointer = caps.glColorTable;
/*   96 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   97 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*   98 */     BufferChecks.checkBuffer(data, 256);
/*   99 */     nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glColorTable(int target, int internalFormat, int width, int format, int type, DoubleBuffer data) {
/*  102 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  103 */     long function_pointer = caps.glColorTable;
/*  104 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  105 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  106 */     BufferChecks.checkBuffer(data, 256);
/*  107 */     nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glColorTable(int target, int internalFormat, int width, int format, int type, FloatBuffer data) {
/*  110 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  111 */     long function_pointer = caps.glColorTable;
/*  112 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  113 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  114 */     BufferChecks.checkBuffer(data, 256);
/*  115 */     nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glColorTable(int target, int internalFormat, int width, int format, int type, long data_buffer_offset) {
/*  119 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  120 */     long function_pointer = caps.glColorTable;
/*  121 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  122 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  123 */     nglColorTableBO(target, internalFormat, width, format, type, data_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglColorTable(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   static native void nglColorTableBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   public static void glColorSubTable(int target, int start, int count, int format, int type, ByteBuffer data) {
/*  128 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  129 */     long function_pointer = caps.glColorSubTable;
/*  130 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  131 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  132 */     BufferChecks.checkBuffer(data, 256);
/*  133 */     nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glColorSubTable(int target, int start, int count, int format, int type, DoubleBuffer data) {
/*  136 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  137 */     long function_pointer = caps.glColorSubTable;
/*  138 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  139 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  140 */     BufferChecks.checkBuffer(data, 256);
/*  141 */     nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glColorSubTable(int target, int start, int count, int format, int type, FloatBuffer data) {
/*  144 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  145 */     long function_pointer = caps.glColorSubTable;
/*  146 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  147 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  148 */     BufferChecks.checkBuffer(data, 256);
/*  149 */     nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglColorSubTable(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   public static void glColorSubTable(int target, int start, int count, int format, int type, long data_buffer_offset) {
/*  153 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  154 */     long function_pointer = caps.glColorSubTable;
/*  155 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  156 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  157 */     nglColorSubTableBO(target, start, count, format, type, data_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglColorSubTableBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glColorTableParameter(int target, int pname, IntBuffer params) {
/*  162 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  163 */     long function_pointer = caps.glColorTableParameteriv;
/*  164 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  165 */     BufferChecks.checkBuffer(params, 4);
/*  166 */     nglColorTableParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglColorTableParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glColorTableParameter(int target, int pname, FloatBuffer params) {
/*  171 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  172 */     long function_pointer = caps.glColorTableParameterfv;
/*  173 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  174 */     BufferChecks.checkBuffer(params, 4);
/*  175 */     nglColorTableParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglColorTableParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCopyColorSubTable(int target, int start, int x, int y, int width) {
/*  180 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  181 */     long function_pointer = caps.glCopyColorSubTable;
/*  182 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  183 */     nglCopyColorSubTable(target, start, x, y, width, function_pointer);
/*      */   }
/*      */   static native void nglCopyColorSubTable(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glCopyColorTable(int target, int internalformat, int x, int y, int width) {
/*  188 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  189 */     long function_pointer = caps.glCopyColorTable;
/*  190 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  191 */     nglCopyColorTable(target, internalformat, x, y, width, function_pointer);
/*      */   }
/*      */   static native void nglCopyColorTable(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glGetColorTable(int target, int format, int type, ByteBuffer data) {
/*  196 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  197 */     long function_pointer = caps.glGetColorTable;
/*  198 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  199 */     BufferChecks.checkBuffer(data, 256);
/*  200 */     nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glGetColorTable(int target, int format, int type, DoubleBuffer data) {
/*  203 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  204 */     long function_pointer = caps.glGetColorTable;
/*  205 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  206 */     BufferChecks.checkBuffer(data, 256);
/*  207 */     nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glGetColorTable(int target, int format, int type, FloatBuffer data) {
/*  210 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  211 */     long function_pointer = caps.glGetColorTable;
/*  212 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  213 */     BufferChecks.checkBuffer(data, 256);
/*  214 */     nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglGetColorTable(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetColorTableParameter(int target, int pname, IntBuffer params) {
/*  219 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  220 */     long function_pointer = caps.glGetColorTableParameteriv;
/*  221 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  222 */     BufferChecks.checkBuffer(params, 4);
/*  223 */     nglGetColorTableParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetColorTableParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetColorTableParameter(int target, int pname, FloatBuffer params) {
/*  228 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  229 */     long function_pointer = caps.glGetColorTableParameterfv;
/*  230 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  231 */     BufferChecks.checkBuffer(params, 4);
/*  232 */     nglGetColorTableParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetColorTableParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glBlendEquation(int mode) {
/*  237 */     GL14.glBlendEquation(mode);
/*      */   }
/*      */   
/*      */   public static void glBlendColor(float red, float green, float blue, float alpha) {
/*  241 */     GL14.glBlendColor(red, green, blue, alpha);
/*      */   }
/*      */   
/*      */   public static void glHistogram(int target, int width, int internalformat, boolean sink) {
/*  245 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  246 */     long function_pointer = caps.glHistogram;
/*  247 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  248 */     nglHistogram(target, width, internalformat, sink, function_pointer);
/*      */   }
/*      */   static native void nglHistogram(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glResetHistogram(int target) {
/*  253 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  254 */     long function_pointer = caps.glResetHistogram;
/*  255 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  256 */     nglResetHistogram(target, function_pointer);
/*      */   }
/*      */   static native void nglResetHistogram(int paramInt, long paramLong);
/*      */   
/*      */   public static void glGetHistogram(int target, boolean reset, int format, int type, ByteBuffer values) {
/*  261 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  262 */     long function_pointer = caps.glGetHistogram;
/*  263 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  264 */     GLChecks.ensurePackPBOdisabled(caps);
/*  265 */     BufferChecks.checkBuffer(values, 256);
/*  266 */     nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetHistogram(int target, boolean reset, int format, int type, DoubleBuffer values) {
/*  269 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  270 */     long function_pointer = caps.glGetHistogram;
/*  271 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  272 */     GLChecks.ensurePackPBOdisabled(caps);
/*  273 */     BufferChecks.checkBuffer(values, 256);
/*  274 */     nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetHistogram(int target, boolean reset, int format, int type, FloatBuffer values) {
/*  277 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  278 */     long function_pointer = caps.glGetHistogram;
/*  279 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  280 */     GLChecks.ensurePackPBOdisabled(caps);
/*  281 */     BufferChecks.checkBuffer(values, 256);
/*  282 */     nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetHistogram(int target, boolean reset, int format, int type, IntBuffer values) {
/*  285 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  286 */     long function_pointer = caps.glGetHistogram;
/*  287 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  288 */     GLChecks.ensurePackPBOdisabled(caps);
/*  289 */     BufferChecks.checkBuffer(values, 256);
/*  290 */     nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetHistogram(int target, boolean reset, int format, int type, ShortBuffer values) {
/*  293 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  294 */     long function_pointer = caps.glGetHistogram;
/*  295 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  296 */     GLChecks.ensurePackPBOdisabled(caps);
/*  297 */     BufferChecks.checkBuffer(values, 256);
/*  298 */     nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetHistogram(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glGetHistogram(int target, boolean reset, int format, int type, long values_buffer_offset) {
/*  302 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  303 */     long function_pointer = caps.glGetHistogram;
/*  304 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  305 */     GLChecks.ensurePackPBOenabled(caps);
/*  306 */     nglGetHistogramBO(target, reset, format, type, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetHistogramBO(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetHistogramParameter(int target, int pname, FloatBuffer params) {
/*  311 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  312 */     long function_pointer = caps.glGetHistogramParameterfv;
/*  313 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  314 */     BufferChecks.checkBuffer(params, 256);
/*  315 */     nglGetHistogramParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetHistogramParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetHistogramParameter(int target, int pname, IntBuffer params) {
/*  320 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  321 */     long function_pointer = caps.glGetHistogramParameteriv;
/*  322 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  323 */     BufferChecks.checkBuffer(params, 256);
/*  324 */     nglGetHistogramParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetHistogramParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMinmax(int target, int internalformat, boolean sink) {
/*  329 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  330 */     long function_pointer = caps.glMinmax;
/*  331 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  332 */     nglMinmax(target, internalformat, sink, function_pointer);
/*      */   }
/*      */   static native void nglMinmax(int paramInt1, int paramInt2, boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glResetMinmax(int target) {
/*  337 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  338 */     long function_pointer = caps.glResetMinmax;
/*  339 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  340 */     nglResetMinmax(target, function_pointer);
/*      */   }
/*      */   static native void nglResetMinmax(int paramInt, long paramLong);
/*      */   
/*      */   public static void glGetMinmax(int target, boolean reset, int format, int types, ByteBuffer values) {
/*  345 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  346 */     long function_pointer = caps.glGetMinmax;
/*  347 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  348 */     GLChecks.ensurePackPBOdisabled(caps);
/*  349 */     BufferChecks.checkBuffer(values, 4);
/*  350 */     nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetMinmax(int target, boolean reset, int format, int types, DoubleBuffer values) {
/*  353 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  354 */     long function_pointer = caps.glGetMinmax;
/*  355 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  356 */     GLChecks.ensurePackPBOdisabled(caps);
/*  357 */     BufferChecks.checkBuffer(values, 4);
/*  358 */     nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetMinmax(int target, boolean reset, int format, int types, FloatBuffer values) {
/*  361 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  362 */     long function_pointer = caps.glGetMinmax;
/*  363 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  364 */     GLChecks.ensurePackPBOdisabled(caps);
/*  365 */     BufferChecks.checkBuffer(values, 4);
/*  366 */     nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetMinmax(int target, boolean reset, int format, int types, IntBuffer values) {
/*  369 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  370 */     long function_pointer = caps.glGetMinmax;
/*  371 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  372 */     GLChecks.ensurePackPBOdisabled(caps);
/*  373 */     BufferChecks.checkBuffer(values, 4);
/*  374 */     nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetMinmax(int target, boolean reset, int format, int types, ShortBuffer values) {
/*  377 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  378 */     long function_pointer = caps.glGetMinmax;
/*  379 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  380 */     GLChecks.ensurePackPBOdisabled(caps);
/*  381 */     BufferChecks.checkBuffer(values, 4);
/*  382 */     nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetMinmax(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glGetMinmax(int target, boolean reset, int format, int types, long values_buffer_offset) {
/*  386 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  387 */     long function_pointer = caps.glGetMinmax;
/*  388 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  389 */     GLChecks.ensurePackPBOenabled(caps);
/*  390 */     nglGetMinmaxBO(target, reset, format, types, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetMinmaxBO(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMinmaxParameter(int target, int pname, FloatBuffer params) {
/*  395 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  396 */     long function_pointer = caps.glGetMinmaxParameterfv;
/*  397 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  398 */     BufferChecks.checkBuffer(params, 4);
/*  399 */     nglGetMinmaxParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetMinmaxParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMinmaxParameter(int target, int pname, IntBuffer params) {
/*  404 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  405 */     long function_pointer = caps.glGetMinmaxParameteriv;
/*  406 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  407 */     BufferChecks.checkBuffer(params, 4);
/*  408 */     nglGetMinmaxParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetMinmaxParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, ByteBuffer image) {
/*  413 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  414 */     long function_pointer = caps.glConvolutionFilter1D;
/*  415 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  416 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  417 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
/*  418 */     nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, DoubleBuffer image) {
/*  421 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  422 */     long function_pointer = caps.glConvolutionFilter1D;
/*  423 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  424 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  425 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
/*  426 */     nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, FloatBuffer image) {
/*  429 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  430 */     long function_pointer = caps.glConvolutionFilter1D;
/*  431 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  432 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  433 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
/*  434 */     nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, IntBuffer image) {
/*  437 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  438 */     long function_pointer = caps.glConvolutionFilter1D;
/*  439 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  440 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  441 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
/*  442 */     nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, ShortBuffer image) {
/*  445 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  446 */     long function_pointer = caps.glConvolutionFilter1D;
/*  447 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  448 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  449 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
/*  450 */     nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   static native void nglConvolutionFilter1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, long image_buffer_offset) {
/*  454 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  455 */     long function_pointer = caps.glConvolutionFilter1D;
/*  456 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  457 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  458 */     nglConvolutionFilter1DBO(target, internalformat, width, format, type, image_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglConvolutionFilter1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer image) {
/*  463 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  464 */     long function_pointer = caps.glConvolutionFilter2D;
/*  465 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  466 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  467 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
/*  468 */     nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer image) {
/*  471 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  472 */     long function_pointer = caps.glConvolutionFilter2D;
/*  473 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  474 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  475 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
/*  476 */     nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer image) {
/*  479 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  480 */     long function_pointer = caps.glConvolutionFilter2D;
/*  481 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  482 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  483 */     BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
/*  484 */     nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   static native void nglConvolutionFilter2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, long image_buffer_offset) {
/*  488 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  489 */     long function_pointer = caps.glConvolutionFilter2D;
/*  490 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  491 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  492 */     nglConvolutionFilter2DBO(target, internalformat, width, height, format, type, image_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglConvolutionFilter2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glConvolutionParameterf(int target, int pname, float params) {
/*  497 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  498 */     long function_pointer = caps.glConvolutionParameterf;
/*  499 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  500 */     nglConvolutionParameterf(target, pname, params, function_pointer);
/*      */   }
/*      */   static native void nglConvolutionParameterf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glConvolutionParameter(int target, int pname, FloatBuffer params) {
/*  505 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  506 */     long function_pointer = caps.glConvolutionParameterfv;
/*  507 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  508 */     BufferChecks.checkBuffer(params, 4);
/*  509 */     nglConvolutionParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglConvolutionParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glConvolutionParameteri(int target, int pname, int params) {
/*  514 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  515 */     long function_pointer = caps.glConvolutionParameteri;
/*  516 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  517 */     nglConvolutionParameteri(target, pname, params, function_pointer);
/*      */   }
/*      */   static native void nglConvolutionParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glConvolutionParameter(int target, int pname, IntBuffer params) {
/*  522 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  523 */     long function_pointer = caps.glConvolutionParameteriv;
/*  524 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  525 */     BufferChecks.checkBuffer(params, 4);
/*  526 */     nglConvolutionParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglConvolutionParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCopyConvolutionFilter1D(int target, int internalformat, int x, int y, int width) {
/*  531 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  532 */     long function_pointer = caps.glCopyConvolutionFilter1D;
/*  533 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  534 */     nglCopyConvolutionFilter1D(target, internalformat, x, y, width, function_pointer);
/*      */   }
/*      */   static native void nglCopyConvolutionFilter1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glCopyConvolutionFilter2D(int target, int internalformat, int x, int y, int width, int height) {
/*  539 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  540 */     long function_pointer = caps.glCopyConvolutionFilter2D;
/*  541 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  542 */     nglCopyConvolutionFilter2D(target, internalformat, x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglCopyConvolutionFilter2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*      */   
/*      */   public static void glGetConvolutionFilter(int target, int format, int type, ByteBuffer image) {
/*  547 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  548 */     long function_pointer = caps.glGetConvolutionFilter;
/*  549 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  550 */     GLChecks.ensurePackPBOdisabled(caps);
/*  551 */     BufferChecks.checkDirect(image);
/*  552 */     nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetConvolutionFilter(int target, int format, int type, DoubleBuffer image) {
/*  555 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  556 */     long function_pointer = caps.glGetConvolutionFilter;
/*  557 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  558 */     GLChecks.ensurePackPBOdisabled(caps);
/*  559 */     BufferChecks.checkDirect(image);
/*  560 */     nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetConvolutionFilter(int target, int format, int type, FloatBuffer image) {
/*  563 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  564 */     long function_pointer = caps.glGetConvolutionFilter;
/*  565 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  566 */     GLChecks.ensurePackPBOdisabled(caps);
/*  567 */     BufferChecks.checkDirect(image);
/*  568 */     nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetConvolutionFilter(int target, int format, int type, IntBuffer image) {
/*  571 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  572 */     long function_pointer = caps.glGetConvolutionFilter;
/*  573 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  574 */     GLChecks.ensurePackPBOdisabled(caps);
/*  575 */     BufferChecks.checkDirect(image);
/*  576 */     nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetConvolutionFilter(int target, int format, int type, ShortBuffer image) {
/*  579 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  580 */     long function_pointer = caps.glGetConvolutionFilter;
/*  581 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  582 */     GLChecks.ensurePackPBOdisabled(caps);
/*  583 */     BufferChecks.checkDirect(image);
/*  584 */     nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   static native void nglGetConvolutionFilter(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glGetConvolutionFilter(int target, int format, int type, long image_buffer_offset) {
/*  588 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  589 */     long function_pointer = caps.glGetConvolutionFilter;
/*  590 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  591 */     GLChecks.ensurePackPBOenabled(caps);
/*  592 */     nglGetConvolutionFilterBO(target, format, type, image_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetConvolutionFilterBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetConvolutionParameter(int target, int pname, FloatBuffer params) {
/*  597 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  598 */     long function_pointer = caps.glGetConvolutionParameterfv;
/*  599 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  600 */     BufferChecks.checkBuffer(params, 4);
/*  601 */     nglGetConvolutionParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetConvolutionParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetConvolutionParameter(int target, int pname, IntBuffer params) {
/*  606 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  607 */     long function_pointer = caps.glGetConvolutionParameteriv;
/*  608 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  609 */     BufferChecks.checkBuffer(params, 4);
/*  610 */     nglGetConvolutionParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetConvolutionParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, ByteBuffer column) {
/*  615 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  616 */     long function_pointer = caps.glSeparableFilter2D;
/*  617 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  618 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  619 */     BufferChecks.checkDirect(row);
/*  620 */     BufferChecks.checkDirect(column);
/*  621 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, DoubleBuffer column) {
/*  624 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  625 */     long function_pointer = caps.glSeparableFilter2D;
/*  626 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  627 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  628 */     BufferChecks.checkDirect(row);
/*  629 */     BufferChecks.checkDirect(column);
/*  630 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, FloatBuffer column) {
/*  633 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  634 */     long function_pointer = caps.glSeparableFilter2D;
/*  635 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  636 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  637 */     BufferChecks.checkDirect(row);
/*  638 */     BufferChecks.checkDirect(column);
/*  639 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, IntBuffer column) {
/*  642 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  643 */     long function_pointer = caps.glSeparableFilter2D;
/*  644 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  645 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  646 */     BufferChecks.checkDirect(row);
/*  647 */     BufferChecks.checkDirect(column);
/*  648 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, ShortBuffer column) {
/*  651 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  652 */     long function_pointer = caps.glSeparableFilter2D;
/*  653 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  654 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  655 */     BufferChecks.checkDirect(row);
/*  656 */     BufferChecks.checkDirect(column);
/*  657 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, ByteBuffer column) {
/*  660 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  661 */     long function_pointer = caps.glSeparableFilter2D;
/*  662 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  663 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  664 */     BufferChecks.checkDirect(row);
/*  665 */     BufferChecks.checkDirect(column);
/*  666 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, DoubleBuffer column) {
/*  669 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  670 */     long function_pointer = caps.glSeparableFilter2D;
/*  671 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  672 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  673 */     BufferChecks.checkDirect(row);
/*  674 */     BufferChecks.checkDirect(column);
/*  675 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, FloatBuffer column) {
/*  678 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  679 */     long function_pointer = caps.glSeparableFilter2D;
/*  680 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  681 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  682 */     BufferChecks.checkDirect(row);
/*  683 */     BufferChecks.checkDirect(column);
/*  684 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, IntBuffer column) {
/*  687 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  688 */     long function_pointer = caps.glSeparableFilter2D;
/*  689 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  690 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  691 */     BufferChecks.checkDirect(row);
/*  692 */     BufferChecks.checkDirect(column);
/*  693 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, ShortBuffer column) {
/*  696 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  697 */     long function_pointer = caps.glSeparableFilter2D;
/*  698 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  699 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  700 */     BufferChecks.checkDirect(row);
/*  701 */     BufferChecks.checkDirect(column);
/*  702 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, ByteBuffer column) {
/*  705 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  706 */     long function_pointer = caps.glSeparableFilter2D;
/*  707 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  708 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  709 */     BufferChecks.checkDirect(row);
/*  710 */     BufferChecks.checkDirect(column);
/*  711 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, DoubleBuffer column) {
/*  714 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  715 */     long function_pointer = caps.glSeparableFilter2D;
/*  716 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  717 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  718 */     BufferChecks.checkDirect(row);
/*  719 */     BufferChecks.checkDirect(column);
/*  720 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, FloatBuffer column) {
/*  723 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  724 */     long function_pointer = caps.glSeparableFilter2D;
/*  725 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  726 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  727 */     BufferChecks.checkDirect(row);
/*  728 */     BufferChecks.checkDirect(column);
/*  729 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, IntBuffer column) {
/*  732 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  733 */     long function_pointer = caps.glSeparableFilter2D;
/*  734 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  735 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  736 */     BufferChecks.checkDirect(row);
/*  737 */     BufferChecks.checkDirect(column);
/*  738 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, ShortBuffer column) {
/*  741 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  742 */     long function_pointer = caps.glSeparableFilter2D;
/*  743 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  744 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  745 */     BufferChecks.checkDirect(row);
/*  746 */     BufferChecks.checkDirect(column);
/*  747 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, ByteBuffer column) {
/*  750 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  751 */     long function_pointer = caps.glSeparableFilter2D;
/*  752 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  753 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  754 */     BufferChecks.checkDirect(row);
/*  755 */     BufferChecks.checkDirect(column);
/*  756 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, DoubleBuffer column) {
/*  759 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  760 */     long function_pointer = caps.glSeparableFilter2D;
/*  761 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  762 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  763 */     BufferChecks.checkDirect(row);
/*  764 */     BufferChecks.checkDirect(column);
/*  765 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, FloatBuffer column) {
/*  768 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  769 */     long function_pointer = caps.glSeparableFilter2D;
/*  770 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  771 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  772 */     BufferChecks.checkDirect(row);
/*  773 */     BufferChecks.checkDirect(column);
/*  774 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, IntBuffer column) {
/*  777 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  778 */     long function_pointer = caps.glSeparableFilter2D;
/*  779 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  780 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  781 */     BufferChecks.checkDirect(row);
/*  782 */     BufferChecks.checkDirect(column);
/*  783 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, ShortBuffer column) {
/*  786 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  787 */     long function_pointer = caps.glSeparableFilter2D;
/*  788 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  789 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  790 */     BufferChecks.checkDirect(row);
/*  791 */     BufferChecks.checkDirect(column);
/*  792 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, ByteBuffer column) {
/*  795 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  796 */     long function_pointer = caps.glSeparableFilter2D;
/*  797 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  798 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  799 */     BufferChecks.checkDirect(row);
/*  800 */     BufferChecks.checkDirect(column);
/*  801 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, DoubleBuffer column) {
/*  804 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  805 */     long function_pointer = caps.glSeparableFilter2D;
/*  806 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  807 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  808 */     BufferChecks.checkDirect(row);
/*  809 */     BufferChecks.checkDirect(column);
/*  810 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, FloatBuffer column) {
/*  813 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  814 */     long function_pointer = caps.glSeparableFilter2D;
/*  815 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  816 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  817 */     BufferChecks.checkDirect(row);
/*  818 */     BufferChecks.checkDirect(column);
/*  819 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, IntBuffer column) {
/*  822 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  823 */     long function_pointer = caps.glSeparableFilter2D;
/*  824 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  825 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  826 */     BufferChecks.checkDirect(row);
/*  827 */     BufferChecks.checkDirect(column);
/*  828 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, ShortBuffer column) {
/*  831 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  832 */     long function_pointer = caps.glSeparableFilter2D;
/*  833 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  834 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  835 */     BufferChecks.checkDirect(row);
/*  836 */     BufferChecks.checkDirect(column);
/*  837 */     nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
/*      */   }
/*      */   static native void nglSeparableFilter2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2, long paramLong3);
/*      */   public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, long row_buffer_offset, long column_buffer_offset) {
/*  841 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  842 */     long function_pointer = caps.glSeparableFilter2D;
/*  843 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  844 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  845 */     nglSeparableFilter2DBO(target, internalformat, width, height, format, type, row_buffer_offset, column_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglSeparableFilter2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, ByteBuffer span) {
/*  850 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  851 */     long function_pointer = caps.glGetSeparableFilter;
/*  852 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  853 */     GLChecks.ensurePackPBOdisabled(caps);
/*  854 */     BufferChecks.checkDirect(row);
/*  855 */     BufferChecks.checkDirect(column);
/*  856 */     BufferChecks.checkDirect(span);
/*  857 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, DoubleBuffer span) {
/*  860 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  861 */     long function_pointer = caps.glGetSeparableFilter;
/*  862 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  863 */     GLChecks.ensurePackPBOdisabled(caps);
/*  864 */     BufferChecks.checkDirect(row);
/*  865 */     BufferChecks.checkDirect(column);
/*  866 */     BufferChecks.checkDirect(span);
/*  867 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, IntBuffer span) {
/*  870 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  871 */     long function_pointer = caps.glGetSeparableFilter;
/*  872 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  873 */     GLChecks.ensurePackPBOdisabled(caps);
/*  874 */     BufferChecks.checkDirect(row);
/*  875 */     BufferChecks.checkDirect(column);
/*  876 */     BufferChecks.checkDirect(span);
/*  877 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, ShortBuffer span) {
/*  880 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  881 */     long function_pointer = caps.glGetSeparableFilter;
/*  882 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  883 */     GLChecks.ensurePackPBOdisabled(caps);
/*  884 */     BufferChecks.checkDirect(row);
/*  885 */     BufferChecks.checkDirect(column);
/*  886 */     BufferChecks.checkDirect(span);
/*  887 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ByteBuffer span) {
/*  890 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  891 */     long function_pointer = caps.glGetSeparableFilter;
/*  892 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  893 */     GLChecks.ensurePackPBOdisabled(caps);
/*  894 */     BufferChecks.checkDirect(row);
/*  895 */     BufferChecks.checkDirect(column);
/*  896 */     BufferChecks.checkDirect(span);
/*  897 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, DoubleBuffer span) {
/*  900 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  901 */     long function_pointer = caps.glGetSeparableFilter;
/*  902 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  903 */     GLChecks.ensurePackPBOdisabled(caps);
/*  904 */     BufferChecks.checkDirect(row);
/*  905 */     BufferChecks.checkDirect(column);
/*  906 */     BufferChecks.checkDirect(span);
/*  907 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, IntBuffer span) {
/*  910 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  911 */     long function_pointer = caps.glGetSeparableFilter;
/*  912 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  913 */     GLChecks.ensurePackPBOdisabled(caps);
/*  914 */     BufferChecks.checkDirect(row);
/*  915 */     BufferChecks.checkDirect(column);
/*  916 */     BufferChecks.checkDirect(span);
/*  917 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ShortBuffer span) {
/*  920 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  921 */     long function_pointer = caps.glGetSeparableFilter;
/*  922 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  923 */     GLChecks.ensurePackPBOdisabled(caps);
/*  924 */     BufferChecks.checkDirect(row);
/*  925 */     BufferChecks.checkDirect(column);
/*  926 */     BufferChecks.checkDirect(span);
/*  927 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, ByteBuffer span) {
/*  930 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  931 */     long function_pointer = caps.glGetSeparableFilter;
/*  932 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  933 */     GLChecks.ensurePackPBOdisabled(caps);
/*  934 */     BufferChecks.checkDirect(row);
/*  935 */     BufferChecks.checkDirect(column);
/*  936 */     BufferChecks.checkDirect(span);
/*  937 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, DoubleBuffer span) {
/*  940 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  941 */     long function_pointer = caps.glGetSeparableFilter;
/*  942 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  943 */     GLChecks.ensurePackPBOdisabled(caps);
/*  944 */     BufferChecks.checkDirect(row);
/*  945 */     BufferChecks.checkDirect(column);
/*  946 */     BufferChecks.checkDirect(span);
/*  947 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, IntBuffer span) {
/*  950 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  951 */     long function_pointer = caps.glGetSeparableFilter;
/*  952 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  953 */     GLChecks.ensurePackPBOdisabled(caps);
/*  954 */     BufferChecks.checkDirect(row);
/*  955 */     BufferChecks.checkDirect(column);
/*  956 */     BufferChecks.checkDirect(span);
/*  957 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, ShortBuffer span) {
/*  960 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  961 */     long function_pointer = caps.glGetSeparableFilter;
/*  962 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  963 */     GLChecks.ensurePackPBOdisabled(caps);
/*  964 */     BufferChecks.checkDirect(row);
/*  965 */     BufferChecks.checkDirect(column);
/*  966 */     BufferChecks.checkDirect(span);
/*  967 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, ByteBuffer span) {
/*  970 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  971 */     long function_pointer = caps.glGetSeparableFilter;
/*  972 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  973 */     GLChecks.ensurePackPBOdisabled(caps);
/*  974 */     BufferChecks.checkDirect(row);
/*  975 */     BufferChecks.checkDirect(column);
/*  976 */     BufferChecks.checkDirect(span);
/*  977 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, DoubleBuffer span) {
/*  980 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  981 */     long function_pointer = caps.glGetSeparableFilter;
/*  982 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  983 */     GLChecks.ensurePackPBOdisabled(caps);
/*  984 */     BufferChecks.checkDirect(row);
/*  985 */     BufferChecks.checkDirect(column);
/*  986 */     BufferChecks.checkDirect(span);
/*  987 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, IntBuffer span) {
/*  990 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  991 */     long function_pointer = caps.glGetSeparableFilter;
/*  992 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  993 */     GLChecks.ensurePackPBOdisabled(caps);
/*  994 */     BufferChecks.checkDirect(row);
/*  995 */     BufferChecks.checkDirect(column);
/*  996 */     BufferChecks.checkDirect(span);
/*  997 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1000 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1001 */     long function_pointer = caps.glGetSeparableFilter;
/* 1002 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1003 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1004 */     BufferChecks.checkDirect(row);
/* 1005 */     BufferChecks.checkDirect(column);
/* 1006 */     BufferChecks.checkDirect(span);
/* 1007 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ByteBuffer span) {
/* 1010 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1011 */     long function_pointer = caps.glGetSeparableFilter;
/* 1012 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1013 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1014 */     BufferChecks.checkDirect(row);
/* 1015 */     BufferChecks.checkDirect(column);
/* 1016 */     BufferChecks.checkDirect(span);
/* 1017 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, DoubleBuffer span) {
/* 1020 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1021 */     long function_pointer = caps.glGetSeparableFilter;
/* 1022 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1023 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1024 */     BufferChecks.checkDirect(row);
/* 1025 */     BufferChecks.checkDirect(column);
/* 1026 */     BufferChecks.checkDirect(span);
/* 1027 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, IntBuffer span) {
/* 1030 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1031 */     long function_pointer = caps.glGetSeparableFilter;
/* 1032 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1033 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1034 */     BufferChecks.checkDirect(row);
/* 1035 */     BufferChecks.checkDirect(column);
/* 1036 */     BufferChecks.checkDirect(span);
/* 1037 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ShortBuffer span) {
/* 1040 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1041 */     long function_pointer = caps.glGetSeparableFilter;
/* 1042 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1043 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1044 */     BufferChecks.checkDirect(row);
/* 1045 */     BufferChecks.checkDirect(column);
/* 1046 */     BufferChecks.checkDirect(span);
/* 1047 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ByteBuffer span) {
/* 1050 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1051 */     long function_pointer = caps.glGetSeparableFilter;
/* 1052 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1053 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1054 */     BufferChecks.checkDirect(row);
/* 1055 */     BufferChecks.checkDirect(column);
/* 1056 */     BufferChecks.checkDirect(span);
/* 1057 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, DoubleBuffer span) {
/* 1060 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1061 */     long function_pointer = caps.glGetSeparableFilter;
/* 1062 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1063 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1064 */     BufferChecks.checkDirect(row);
/* 1065 */     BufferChecks.checkDirect(column);
/* 1066 */     BufferChecks.checkDirect(span);
/* 1067 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, IntBuffer span) {
/* 1070 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1071 */     long function_pointer = caps.glGetSeparableFilter;
/* 1072 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1073 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1074 */     BufferChecks.checkDirect(row);
/* 1075 */     BufferChecks.checkDirect(column);
/* 1076 */     BufferChecks.checkDirect(span);
/* 1077 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ShortBuffer span) {
/* 1080 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1081 */     long function_pointer = caps.glGetSeparableFilter;
/* 1082 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1083 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1084 */     BufferChecks.checkDirect(row);
/* 1085 */     BufferChecks.checkDirect(column);
/* 1086 */     BufferChecks.checkDirect(span);
/* 1087 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, ByteBuffer span) {
/* 1090 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1091 */     long function_pointer = caps.glGetSeparableFilter;
/* 1092 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1093 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1094 */     BufferChecks.checkDirect(row);
/* 1095 */     BufferChecks.checkDirect(column);
/* 1096 */     BufferChecks.checkDirect(span);
/* 1097 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, DoubleBuffer span) {
/* 1100 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1101 */     long function_pointer = caps.glGetSeparableFilter;
/* 1102 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1103 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1104 */     BufferChecks.checkDirect(row);
/* 1105 */     BufferChecks.checkDirect(column);
/* 1106 */     BufferChecks.checkDirect(span);
/* 1107 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, IntBuffer span) {
/* 1110 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1111 */     long function_pointer = caps.glGetSeparableFilter;
/* 1112 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1113 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1114 */     BufferChecks.checkDirect(row);
/* 1115 */     BufferChecks.checkDirect(column);
/* 1116 */     BufferChecks.checkDirect(span);
/* 1117 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, ShortBuffer span) {
/* 1120 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1121 */     long function_pointer = caps.glGetSeparableFilter;
/* 1122 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1123 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1124 */     BufferChecks.checkDirect(row);
/* 1125 */     BufferChecks.checkDirect(column);
/* 1126 */     BufferChecks.checkDirect(span);
/* 1127 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ByteBuffer span) {
/* 1130 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1131 */     long function_pointer = caps.glGetSeparableFilter;
/* 1132 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1133 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1134 */     BufferChecks.checkDirect(row);
/* 1135 */     BufferChecks.checkDirect(column);
/* 1136 */     BufferChecks.checkDirect(span);
/* 1137 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, DoubleBuffer span) {
/* 1140 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1141 */     long function_pointer = caps.glGetSeparableFilter;
/* 1142 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1143 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1144 */     BufferChecks.checkDirect(row);
/* 1145 */     BufferChecks.checkDirect(column);
/* 1146 */     BufferChecks.checkDirect(span);
/* 1147 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, IntBuffer span) {
/* 1150 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1151 */     long function_pointer = caps.glGetSeparableFilter;
/* 1152 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1153 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1154 */     BufferChecks.checkDirect(row);
/* 1155 */     BufferChecks.checkDirect(column);
/* 1156 */     BufferChecks.checkDirect(span);
/* 1157 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1160 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1161 */     long function_pointer = caps.glGetSeparableFilter;
/* 1162 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1163 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1164 */     BufferChecks.checkDirect(row);
/* 1165 */     BufferChecks.checkDirect(column);
/* 1166 */     BufferChecks.checkDirect(span);
/* 1167 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, ByteBuffer span) {
/* 1170 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1171 */     long function_pointer = caps.glGetSeparableFilter;
/* 1172 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1173 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1174 */     BufferChecks.checkDirect(row);
/* 1175 */     BufferChecks.checkDirect(column);
/* 1176 */     BufferChecks.checkDirect(span);
/* 1177 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, DoubleBuffer span) {
/* 1180 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1181 */     long function_pointer = caps.glGetSeparableFilter;
/* 1182 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1183 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1184 */     BufferChecks.checkDirect(row);
/* 1185 */     BufferChecks.checkDirect(column);
/* 1186 */     BufferChecks.checkDirect(span);
/* 1187 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, IntBuffer span) {
/* 1190 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1191 */     long function_pointer = caps.glGetSeparableFilter;
/* 1192 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1193 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1194 */     BufferChecks.checkDirect(row);
/* 1195 */     BufferChecks.checkDirect(column);
/* 1196 */     BufferChecks.checkDirect(span);
/* 1197 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, ShortBuffer span) {
/* 1200 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1201 */     long function_pointer = caps.glGetSeparableFilter;
/* 1202 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1203 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1204 */     BufferChecks.checkDirect(row);
/* 1205 */     BufferChecks.checkDirect(column);
/* 1206 */     BufferChecks.checkDirect(span);
/* 1207 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ByteBuffer span) {
/* 1210 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1211 */     long function_pointer = caps.glGetSeparableFilter;
/* 1212 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1213 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1214 */     BufferChecks.checkDirect(row);
/* 1215 */     BufferChecks.checkDirect(column);
/* 1216 */     BufferChecks.checkDirect(span);
/* 1217 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, DoubleBuffer span) {
/* 1220 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1221 */     long function_pointer = caps.glGetSeparableFilter;
/* 1222 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1223 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1224 */     BufferChecks.checkDirect(row);
/* 1225 */     BufferChecks.checkDirect(column);
/* 1226 */     BufferChecks.checkDirect(span);
/* 1227 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, IntBuffer span) {
/* 1230 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1231 */     long function_pointer = caps.glGetSeparableFilter;
/* 1232 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1233 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1234 */     BufferChecks.checkDirect(row);
/* 1235 */     BufferChecks.checkDirect(column);
/* 1236 */     BufferChecks.checkDirect(span);
/* 1237 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ShortBuffer span) {
/* 1240 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1241 */     long function_pointer = caps.glGetSeparableFilter;
/* 1242 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1243 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1244 */     BufferChecks.checkDirect(row);
/* 1245 */     BufferChecks.checkDirect(column);
/* 1246 */     BufferChecks.checkDirect(span);
/* 1247 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, ByteBuffer span) {
/* 1250 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1251 */     long function_pointer = caps.glGetSeparableFilter;
/* 1252 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1253 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1254 */     BufferChecks.checkDirect(row);
/* 1255 */     BufferChecks.checkDirect(column);
/* 1256 */     BufferChecks.checkDirect(span);
/* 1257 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, DoubleBuffer span) {
/* 1260 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1261 */     long function_pointer = caps.glGetSeparableFilter;
/* 1262 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1263 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1264 */     BufferChecks.checkDirect(row);
/* 1265 */     BufferChecks.checkDirect(column);
/* 1266 */     BufferChecks.checkDirect(span);
/* 1267 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, IntBuffer span) {
/* 1270 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1271 */     long function_pointer = caps.glGetSeparableFilter;
/* 1272 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1273 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1274 */     BufferChecks.checkDirect(row);
/* 1275 */     BufferChecks.checkDirect(column);
/* 1276 */     BufferChecks.checkDirect(span);
/* 1277 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, ShortBuffer span) {
/* 1280 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1281 */     long function_pointer = caps.glGetSeparableFilter;
/* 1282 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1283 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1284 */     BufferChecks.checkDirect(row);
/* 1285 */     BufferChecks.checkDirect(column);
/* 1286 */     BufferChecks.checkDirect(span);
/* 1287 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, ByteBuffer span) {
/* 1290 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1291 */     long function_pointer = caps.glGetSeparableFilter;
/* 1292 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1293 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1294 */     BufferChecks.checkDirect(row);
/* 1295 */     BufferChecks.checkDirect(column);
/* 1296 */     BufferChecks.checkDirect(span);
/* 1297 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, DoubleBuffer span) {
/* 1300 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1301 */     long function_pointer = caps.glGetSeparableFilter;
/* 1302 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1303 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1304 */     BufferChecks.checkDirect(row);
/* 1305 */     BufferChecks.checkDirect(column);
/* 1306 */     BufferChecks.checkDirect(span);
/* 1307 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, IntBuffer span) {
/* 1310 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1311 */     long function_pointer = caps.glGetSeparableFilter;
/* 1312 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1313 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1314 */     BufferChecks.checkDirect(row);
/* 1315 */     BufferChecks.checkDirect(column);
/* 1316 */     BufferChecks.checkDirect(span);
/* 1317 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1320 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1321 */     long function_pointer = caps.glGetSeparableFilter;
/* 1322 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1323 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1324 */     BufferChecks.checkDirect(row);
/* 1325 */     BufferChecks.checkDirect(column);
/* 1326 */     BufferChecks.checkDirect(span);
/* 1327 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, ByteBuffer span) {
/* 1330 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1331 */     long function_pointer = caps.glGetSeparableFilter;
/* 1332 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1333 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1334 */     BufferChecks.checkDirect(row);
/* 1335 */     BufferChecks.checkDirect(column);
/* 1336 */     BufferChecks.checkDirect(span);
/* 1337 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, DoubleBuffer span) {
/* 1340 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1341 */     long function_pointer = caps.glGetSeparableFilter;
/* 1342 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1343 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1344 */     BufferChecks.checkDirect(row);
/* 1345 */     BufferChecks.checkDirect(column);
/* 1346 */     BufferChecks.checkDirect(span);
/* 1347 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, IntBuffer span) {
/* 1350 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1351 */     long function_pointer = caps.glGetSeparableFilter;
/* 1352 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1353 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1354 */     BufferChecks.checkDirect(row);
/* 1355 */     BufferChecks.checkDirect(column);
/* 1356 */     BufferChecks.checkDirect(span);
/* 1357 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, ShortBuffer span) {
/* 1360 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1361 */     long function_pointer = caps.glGetSeparableFilter;
/* 1362 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1363 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1364 */     BufferChecks.checkDirect(row);
/* 1365 */     BufferChecks.checkDirect(column);
/* 1366 */     BufferChecks.checkDirect(span);
/* 1367 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, ByteBuffer span) {
/* 1370 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1371 */     long function_pointer = caps.glGetSeparableFilter;
/* 1372 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1373 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1374 */     BufferChecks.checkDirect(row);
/* 1375 */     BufferChecks.checkDirect(column);
/* 1376 */     BufferChecks.checkDirect(span);
/* 1377 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, DoubleBuffer span) {
/* 1380 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1381 */     long function_pointer = caps.glGetSeparableFilter;
/* 1382 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1383 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1384 */     BufferChecks.checkDirect(row);
/* 1385 */     BufferChecks.checkDirect(column);
/* 1386 */     BufferChecks.checkDirect(span);
/* 1387 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, IntBuffer span) {
/* 1390 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1391 */     long function_pointer = caps.glGetSeparableFilter;
/* 1392 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1393 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1394 */     BufferChecks.checkDirect(row);
/* 1395 */     BufferChecks.checkDirect(column);
/* 1396 */     BufferChecks.checkDirect(span);
/* 1397 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, ShortBuffer span) {
/* 1400 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1401 */     long function_pointer = caps.glGetSeparableFilter;
/* 1402 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1403 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1404 */     BufferChecks.checkDirect(row);
/* 1405 */     BufferChecks.checkDirect(column);
/* 1406 */     BufferChecks.checkDirect(span);
/* 1407 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, ByteBuffer span) {
/* 1410 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1411 */     long function_pointer = caps.glGetSeparableFilter;
/* 1412 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1413 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1414 */     BufferChecks.checkDirect(row);
/* 1415 */     BufferChecks.checkDirect(column);
/* 1416 */     BufferChecks.checkDirect(span);
/* 1417 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, DoubleBuffer span) {
/* 1420 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1421 */     long function_pointer = caps.glGetSeparableFilter;
/* 1422 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1423 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1424 */     BufferChecks.checkDirect(row);
/* 1425 */     BufferChecks.checkDirect(column);
/* 1426 */     BufferChecks.checkDirect(span);
/* 1427 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, IntBuffer span) {
/* 1430 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1431 */     long function_pointer = caps.glGetSeparableFilter;
/* 1432 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1433 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1434 */     BufferChecks.checkDirect(row);
/* 1435 */     BufferChecks.checkDirect(column);
/* 1436 */     BufferChecks.checkDirect(span);
/* 1437 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, ShortBuffer span) {
/* 1440 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1441 */     long function_pointer = caps.glGetSeparableFilter;
/* 1442 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1443 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1444 */     BufferChecks.checkDirect(row);
/* 1445 */     BufferChecks.checkDirect(column);
/* 1446 */     BufferChecks.checkDirect(span);
/* 1447 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, ByteBuffer span) {
/* 1450 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1451 */     long function_pointer = caps.glGetSeparableFilter;
/* 1452 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1453 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1454 */     BufferChecks.checkDirect(row);
/* 1455 */     BufferChecks.checkDirect(column);
/* 1456 */     BufferChecks.checkDirect(span);
/* 1457 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, DoubleBuffer span) {
/* 1460 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1461 */     long function_pointer = caps.glGetSeparableFilter;
/* 1462 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1463 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1464 */     BufferChecks.checkDirect(row);
/* 1465 */     BufferChecks.checkDirect(column);
/* 1466 */     BufferChecks.checkDirect(span);
/* 1467 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, IntBuffer span) {
/* 1470 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1471 */     long function_pointer = caps.glGetSeparableFilter;
/* 1472 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1473 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1474 */     BufferChecks.checkDirect(row);
/* 1475 */     BufferChecks.checkDirect(column);
/* 1476 */     BufferChecks.checkDirect(span);
/* 1477 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1480 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1481 */     long function_pointer = caps.glGetSeparableFilter;
/* 1482 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1483 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1484 */     BufferChecks.checkDirect(row);
/* 1485 */     BufferChecks.checkDirect(column);
/* 1486 */     BufferChecks.checkDirect(span);
/* 1487 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, ByteBuffer span) {
/* 1490 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1491 */     long function_pointer = caps.glGetSeparableFilter;
/* 1492 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1493 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1494 */     BufferChecks.checkDirect(row);
/* 1495 */     BufferChecks.checkDirect(column);
/* 1496 */     BufferChecks.checkDirect(span);
/* 1497 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, DoubleBuffer span) {
/* 1500 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1501 */     long function_pointer = caps.glGetSeparableFilter;
/* 1502 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1503 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1504 */     BufferChecks.checkDirect(row);
/* 1505 */     BufferChecks.checkDirect(column);
/* 1506 */     BufferChecks.checkDirect(span);
/* 1507 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, IntBuffer span) {
/* 1510 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1511 */     long function_pointer = caps.glGetSeparableFilter;
/* 1512 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1513 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1514 */     BufferChecks.checkDirect(row);
/* 1515 */     BufferChecks.checkDirect(column);
/* 1516 */     BufferChecks.checkDirect(span);
/* 1517 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, ShortBuffer span) {
/* 1520 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1521 */     long function_pointer = caps.glGetSeparableFilter;
/* 1522 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1523 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1524 */     BufferChecks.checkDirect(row);
/* 1525 */     BufferChecks.checkDirect(column);
/* 1526 */     BufferChecks.checkDirect(span);
/* 1527 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ByteBuffer span) {
/* 1530 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1531 */     long function_pointer = caps.glGetSeparableFilter;
/* 1532 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1533 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1534 */     BufferChecks.checkDirect(row);
/* 1535 */     BufferChecks.checkDirect(column);
/* 1536 */     BufferChecks.checkDirect(span);
/* 1537 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, DoubleBuffer span) {
/* 1540 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1541 */     long function_pointer = caps.glGetSeparableFilter;
/* 1542 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1543 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1544 */     BufferChecks.checkDirect(row);
/* 1545 */     BufferChecks.checkDirect(column);
/* 1546 */     BufferChecks.checkDirect(span);
/* 1547 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, IntBuffer span) {
/* 1550 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1551 */     long function_pointer = caps.glGetSeparableFilter;
/* 1552 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1553 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1554 */     BufferChecks.checkDirect(row);
/* 1555 */     BufferChecks.checkDirect(column);
/* 1556 */     BufferChecks.checkDirect(span);
/* 1557 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ShortBuffer span) {
/* 1560 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1561 */     long function_pointer = caps.glGetSeparableFilter;
/* 1562 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1563 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1564 */     BufferChecks.checkDirect(row);
/* 1565 */     BufferChecks.checkDirect(column);
/* 1566 */     BufferChecks.checkDirect(span);
/* 1567 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, ByteBuffer span) {
/* 1570 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1571 */     long function_pointer = caps.glGetSeparableFilter;
/* 1572 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1573 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1574 */     BufferChecks.checkDirect(row);
/* 1575 */     BufferChecks.checkDirect(column);
/* 1576 */     BufferChecks.checkDirect(span);
/* 1577 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, DoubleBuffer span) {
/* 1580 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1581 */     long function_pointer = caps.glGetSeparableFilter;
/* 1582 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1583 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1584 */     BufferChecks.checkDirect(row);
/* 1585 */     BufferChecks.checkDirect(column);
/* 1586 */     BufferChecks.checkDirect(span);
/* 1587 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, IntBuffer span) {
/* 1590 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1591 */     long function_pointer = caps.glGetSeparableFilter;
/* 1592 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1593 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1594 */     BufferChecks.checkDirect(row);
/* 1595 */     BufferChecks.checkDirect(column);
/* 1596 */     BufferChecks.checkDirect(span);
/* 1597 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, ShortBuffer span) {
/* 1600 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1601 */     long function_pointer = caps.glGetSeparableFilter;
/* 1602 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1603 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1604 */     BufferChecks.checkDirect(row);
/* 1605 */     BufferChecks.checkDirect(column);
/* 1606 */     BufferChecks.checkDirect(span);
/* 1607 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, ByteBuffer span) {
/* 1610 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1611 */     long function_pointer = caps.glGetSeparableFilter;
/* 1612 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1613 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1614 */     BufferChecks.checkDirect(row);
/* 1615 */     BufferChecks.checkDirect(column);
/* 1616 */     BufferChecks.checkDirect(span);
/* 1617 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, DoubleBuffer span) {
/* 1620 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1621 */     long function_pointer = caps.glGetSeparableFilter;
/* 1622 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1623 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1624 */     BufferChecks.checkDirect(row);
/* 1625 */     BufferChecks.checkDirect(column);
/* 1626 */     BufferChecks.checkDirect(span);
/* 1627 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, IntBuffer span) {
/* 1630 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1631 */     long function_pointer = caps.glGetSeparableFilter;
/* 1632 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1633 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1634 */     BufferChecks.checkDirect(row);
/* 1635 */     BufferChecks.checkDirect(column);
/* 1636 */     BufferChecks.checkDirect(span);
/* 1637 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1640 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1641 */     long function_pointer = caps.glGetSeparableFilter;
/* 1642 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1643 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1644 */     BufferChecks.checkDirect(row);
/* 1645 */     BufferChecks.checkDirect(column);
/* 1646 */     BufferChecks.checkDirect(span);
/* 1647 */     nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   static native void nglGetSeparableFilter(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   public static void glGetSeparableFilter(int target, int format, int type, long row_buffer_offset, long column_buffer_offset, long span_buffer_offset) {
/* 1651 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1652 */     long function_pointer = caps.glGetSeparableFilter;
/* 1653 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1654 */     GLChecks.ensurePackPBOenabled(caps);
/* 1655 */     nglGetSeparableFilterBO(target, format, type, row_buffer_offset, column_buffer_offset, span_buffer_offset, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetSeparableFilterBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBImaging.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */