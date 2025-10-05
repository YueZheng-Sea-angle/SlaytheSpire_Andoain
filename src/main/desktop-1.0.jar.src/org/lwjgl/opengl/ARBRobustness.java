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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ARBRobustness
/*      */ {
/*      */   public static final int GL_GUILTY_CONTEXT_RESET_ARB = 33363;
/*      */   public static final int GL_INNOCENT_CONTEXT_RESET_ARB = 33364;
/*      */   public static final int GL_UNKNOWN_CONTEXT_RESET_ARB = 33365;
/*      */   public static final int GL_RESET_NOTIFICATION_STRATEGY_ARB = 33366;
/*      */   public static final int GL_LOSE_CONTEXT_ON_RESET_ARB = 33362;
/*      */   public static final int GL_NO_RESET_NOTIFICATION_ARB = 33377;
/*      */   public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB = 4;
/*      */   
/*      */   public static int glGetGraphicsResetStatusARB() {
/*   38 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   39 */     long function_pointer = caps.glGetGraphicsResetStatusARB;
/*   40 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   41 */     int __result = nglGetGraphicsResetStatusARB(function_pointer);
/*   42 */     return __result;
/*      */   }
/*      */   static native int nglGetGraphicsResetStatusARB(long paramLong);
/*      */   
/*      */   public static void glGetnMapdvARB(int target, int query, DoubleBuffer v) {
/*   47 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   48 */     long function_pointer = caps.glGetnMapdvARB;
/*   49 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   50 */     BufferChecks.checkDirect(v);
/*   51 */     nglGetnMapdvARB(target, query, v.remaining() << 3, MemoryUtil.getAddress(v), function_pointer);
/*      */   }
/*      */   static native void nglGetnMapdvARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnMapfvARB(int target, int query, FloatBuffer v) {
/*   56 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   57 */     long function_pointer = caps.glGetnMapfvARB;
/*   58 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   59 */     BufferChecks.checkDirect(v);
/*   60 */     nglGetnMapfvARB(target, query, v.remaining() << 2, MemoryUtil.getAddress(v), function_pointer);
/*      */   }
/*      */   static native void nglGetnMapfvARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnMapivARB(int target, int query, IntBuffer v) {
/*   65 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   66 */     long function_pointer = caps.glGetnMapivARB;
/*   67 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   68 */     BufferChecks.checkDirect(v);
/*   69 */     nglGetnMapivARB(target, query, v.remaining() << 2, MemoryUtil.getAddress(v), function_pointer);
/*      */   }
/*      */   static native void nglGetnMapivARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnPixelMapfvARB(int map, FloatBuffer values) {
/*   74 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   75 */     long function_pointer = caps.glGetnPixelMapfvARB;
/*   76 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   77 */     BufferChecks.checkDirect(values);
/*   78 */     nglGetnPixelMapfvARB(map, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetnPixelMapfvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnPixelMapuivARB(int map, IntBuffer values) {
/*   83 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   84 */     long function_pointer = caps.glGetnPixelMapuivARB;
/*   85 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   86 */     BufferChecks.checkDirect(values);
/*   87 */     nglGetnPixelMapuivARB(map, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetnPixelMapuivARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnPixelMapusvARB(int map, ShortBuffer values) {
/*   92 */     ContextCapabilities caps = GLContext.getCapabilities();
/*   93 */     long function_pointer = caps.glGetnPixelMapusvARB;
/*   94 */     BufferChecks.checkFunctionAddress(function_pointer);
/*   95 */     BufferChecks.checkDirect(values);
/*   96 */     nglGetnPixelMapusvARB(map, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetnPixelMapusvARB(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnPolygonStippleARB(ByteBuffer pattern) {
/*  101 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  102 */     long function_pointer = caps.glGetnPolygonStippleARB;
/*  103 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  104 */     BufferChecks.checkDirect(pattern);
/*  105 */     nglGetnPolygonStippleARB(pattern.remaining(), MemoryUtil.getAddress(pattern), function_pointer);
/*      */   }
/*      */   static native void nglGetnPolygonStippleARB(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnTexImageARB(int target, int level, int format, int type, ByteBuffer img) {
/*  110 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  111 */     long function_pointer = caps.glGetnTexImageARB;
/*  112 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  113 */     GLChecks.ensurePackPBOdisabled(caps);
/*  114 */     BufferChecks.checkDirect(img);
/*  115 */     nglGetnTexImageARB(target, level, format, type, img.remaining(), MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   public static void glGetnTexImageARB(int target, int level, int format, int type, DoubleBuffer img) {
/*  118 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  119 */     long function_pointer = caps.glGetnTexImageARB;
/*  120 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  121 */     GLChecks.ensurePackPBOdisabled(caps);
/*  122 */     BufferChecks.checkDirect(img);
/*  123 */     nglGetnTexImageARB(target, level, format, type, img.remaining() << 3, MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   public static void glGetnTexImageARB(int target, int level, int format, int type, FloatBuffer img) {
/*  126 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  127 */     long function_pointer = caps.glGetnTexImageARB;
/*  128 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  129 */     GLChecks.ensurePackPBOdisabled(caps);
/*  130 */     BufferChecks.checkDirect(img);
/*  131 */     nglGetnTexImageARB(target, level, format, type, img.remaining() << 2, MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   public static void glGetnTexImageARB(int target, int level, int format, int type, IntBuffer img) {
/*  134 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  135 */     long function_pointer = caps.glGetnTexImageARB;
/*  136 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  137 */     GLChecks.ensurePackPBOdisabled(caps);
/*  138 */     BufferChecks.checkDirect(img);
/*  139 */     nglGetnTexImageARB(target, level, format, type, img.remaining() << 2, MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   public static void glGetnTexImageARB(int target, int level, int format, int type, ShortBuffer img) {
/*  142 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  143 */     long function_pointer = caps.glGetnTexImageARB;
/*  144 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  145 */     GLChecks.ensurePackPBOdisabled(caps);
/*  146 */     BufferChecks.checkDirect(img);
/*  147 */     nglGetnTexImageARB(target, level, format, type, img.remaining() << 1, MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glGetnTexImageARB(int target, int level, int format, int type, int img_bufSize, long img_buffer_offset) {
/*  151 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  152 */     long function_pointer = caps.glGetnTexImageARB;
/*  153 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  154 */     GLChecks.ensurePackPBOenabled(caps);
/*  155 */     nglGetnTexImageARBBO(target, level, format, type, img_bufSize, img_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetnTexImageARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   static native void nglGetnTexImageARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
/*  160 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  161 */     long function_pointer = caps.glReadnPixelsARB;
/*  162 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  163 */     GLChecks.ensurePackPBOdisabled(caps);
/*  164 */     BufferChecks.checkDirect(data);
/*  165 */     nglReadnPixelsARB(x, y, width, height, format, type, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, DoubleBuffer data) {
/*  168 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  169 */     long function_pointer = caps.glReadnPixelsARB;
/*  170 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  171 */     GLChecks.ensurePackPBOdisabled(caps);
/*  172 */     BufferChecks.checkDirect(data);
/*  173 */     nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 3, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, FloatBuffer data) {
/*  176 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  177 */     long function_pointer = caps.glReadnPixelsARB;
/*  178 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  179 */     GLChecks.ensurePackPBOdisabled(caps);
/*  180 */     BufferChecks.checkDirect(data);
/*  181 */     nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, IntBuffer data) {
/*  184 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  185 */     long function_pointer = caps.glReadnPixelsARB;
/*  186 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  187 */     GLChecks.ensurePackPBOdisabled(caps);
/*  188 */     BufferChecks.checkDirect(data);
/*  189 */     nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 2, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, ShortBuffer data) {
/*  192 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  193 */     long function_pointer = caps.glReadnPixelsARB;
/*  194 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  195 */     GLChecks.ensurePackPBOdisabled(caps);
/*  196 */     BufferChecks.checkDirect(data);
/*  197 */     nglReadnPixelsARB(x, y, width, height, format, type, data.remaining() << 1, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglReadnPixelsARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*      */   public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, int data_bufSize, long data_buffer_offset) {
/*  201 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  202 */     long function_pointer = caps.glReadnPixelsARB;
/*  203 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  204 */     GLChecks.ensurePackPBOenabled(caps);
/*  205 */     nglReadnPixelsARBBO(x, y, width, height, format, type, data_bufSize, data_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglReadnPixelsARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnColorTableARB(int target, int format, int type, ByteBuffer table) {
/*  210 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  211 */     long function_pointer = caps.glGetnColorTableARB;
/*  212 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  213 */     BufferChecks.checkDirect(table);
/*  214 */     nglGetnColorTableARB(target, format, type, table.remaining(), MemoryUtil.getAddress(table), function_pointer);
/*      */   }
/*      */   public static void glGetnColorTableARB(int target, int format, int type, DoubleBuffer table) {
/*  217 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  218 */     long function_pointer = caps.glGetnColorTableARB;
/*  219 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  220 */     BufferChecks.checkDirect(table);
/*  221 */     nglGetnColorTableARB(target, format, type, table.remaining() << 3, MemoryUtil.getAddress(table), function_pointer);
/*      */   }
/*      */   public static void glGetnColorTableARB(int target, int format, int type, FloatBuffer table) {
/*  224 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  225 */     long function_pointer = caps.glGetnColorTableARB;
/*  226 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  227 */     BufferChecks.checkDirect(table);
/*  228 */     nglGetnColorTableARB(target, format, type, table.remaining() << 2, MemoryUtil.getAddress(table), function_pointer);
/*      */   }
/*      */   static native void nglGetnColorTableARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnConvolutionFilterARB(int target, int format, int type, ByteBuffer image) {
/*  233 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  234 */     long function_pointer = caps.glGetnConvolutionFilterARB;
/*  235 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  236 */     GLChecks.ensurePackPBOdisabled(caps);
/*  237 */     BufferChecks.checkDirect(image);
/*  238 */     nglGetnConvolutionFilterARB(target, format, type, image.remaining(), MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetnConvolutionFilterARB(int target, int format, int type, DoubleBuffer image) {
/*  241 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  242 */     long function_pointer = caps.glGetnConvolutionFilterARB;
/*  243 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  244 */     GLChecks.ensurePackPBOdisabled(caps);
/*  245 */     BufferChecks.checkDirect(image);
/*  246 */     nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 3, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetnConvolutionFilterARB(int target, int format, int type, FloatBuffer image) {
/*  249 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  250 */     long function_pointer = caps.glGetnConvolutionFilterARB;
/*  251 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  252 */     GLChecks.ensurePackPBOdisabled(caps);
/*  253 */     BufferChecks.checkDirect(image);
/*  254 */     nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 2, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetnConvolutionFilterARB(int target, int format, int type, IntBuffer image) {
/*  257 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  258 */     long function_pointer = caps.glGetnConvolutionFilterARB;
/*  259 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  260 */     GLChecks.ensurePackPBOdisabled(caps);
/*  261 */     BufferChecks.checkDirect(image);
/*  262 */     nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 2, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   public static void glGetnConvolutionFilterARB(int target, int format, int type, ShortBuffer image) {
/*  265 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  266 */     long function_pointer = caps.glGetnConvolutionFilterARB;
/*  267 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  268 */     GLChecks.ensurePackPBOdisabled(caps);
/*  269 */     BufferChecks.checkDirect(image);
/*  270 */     nglGetnConvolutionFilterARB(target, format, type, image.remaining() << 1, MemoryUtil.getAddress(image), function_pointer);
/*      */   }
/*      */   static native void nglGetnConvolutionFilterARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   public static void glGetnConvolutionFilterARB(int target, int format, int type, int image_bufSize, long image_buffer_offset) {
/*  274 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  275 */     long function_pointer = caps.glGetnConvolutionFilterARB;
/*  276 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  277 */     GLChecks.ensurePackPBOenabled(caps);
/*  278 */     nglGetnConvolutionFilterARBBO(target, format, type, image_bufSize, image_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetnConvolutionFilterARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, ByteBuffer span) {
/*  283 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  284 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  285 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  286 */     GLChecks.ensurePackPBOdisabled(caps);
/*  287 */     BufferChecks.checkDirect(row);
/*  288 */     BufferChecks.checkDirect(column);
/*  289 */     BufferChecks.checkDirect(span);
/*  290 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, DoubleBuffer span) {
/*  293 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  294 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  295 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  296 */     GLChecks.ensurePackPBOdisabled(caps);
/*  297 */     BufferChecks.checkDirect(row);
/*  298 */     BufferChecks.checkDirect(column);
/*  299 */     BufferChecks.checkDirect(span);
/*  300 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, FloatBuffer span) {
/*  303 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  304 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  305 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  306 */     GLChecks.ensurePackPBOdisabled(caps);
/*  307 */     BufferChecks.checkDirect(row);
/*  308 */     BufferChecks.checkDirect(column);
/*  309 */     BufferChecks.checkDirect(span);
/*  310 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, IntBuffer span) {
/*  313 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  314 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  315 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  316 */     GLChecks.ensurePackPBOdisabled(caps);
/*  317 */     BufferChecks.checkDirect(row);
/*  318 */     BufferChecks.checkDirect(column);
/*  319 */     BufferChecks.checkDirect(span);
/*  320 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, ShortBuffer span) {
/*  323 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  324 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  325 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  326 */     GLChecks.ensurePackPBOdisabled(caps);
/*  327 */     BufferChecks.checkDirect(row);
/*  328 */     BufferChecks.checkDirect(column);
/*  329 */     BufferChecks.checkDirect(span);
/*  330 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ByteBuffer span) {
/*  333 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  334 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  335 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  336 */     GLChecks.ensurePackPBOdisabled(caps);
/*  337 */     BufferChecks.checkDirect(row);
/*  338 */     BufferChecks.checkDirect(column);
/*  339 */     BufferChecks.checkDirect(span);
/*  340 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, DoubleBuffer span) {
/*  343 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  344 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  345 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  346 */     GLChecks.ensurePackPBOdisabled(caps);
/*  347 */     BufferChecks.checkDirect(row);
/*  348 */     BufferChecks.checkDirect(column);
/*  349 */     BufferChecks.checkDirect(span);
/*  350 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, FloatBuffer span) {
/*  353 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  354 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  355 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  356 */     GLChecks.ensurePackPBOdisabled(caps);
/*  357 */     BufferChecks.checkDirect(row);
/*  358 */     BufferChecks.checkDirect(column);
/*  359 */     BufferChecks.checkDirect(span);
/*  360 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, IntBuffer span) {
/*  363 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  364 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  365 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  366 */     GLChecks.ensurePackPBOdisabled(caps);
/*  367 */     BufferChecks.checkDirect(row);
/*  368 */     BufferChecks.checkDirect(column);
/*  369 */     BufferChecks.checkDirect(span);
/*  370 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ShortBuffer span) {
/*  373 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  374 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  375 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  376 */     GLChecks.ensurePackPBOdisabled(caps);
/*  377 */     BufferChecks.checkDirect(row);
/*  378 */     BufferChecks.checkDirect(column);
/*  379 */     BufferChecks.checkDirect(span);
/*  380 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, ByteBuffer span) {
/*  383 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  384 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  385 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  386 */     GLChecks.ensurePackPBOdisabled(caps);
/*  387 */     BufferChecks.checkDirect(row);
/*  388 */     BufferChecks.checkDirect(column);
/*  389 */     BufferChecks.checkDirect(span);
/*  390 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, DoubleBuffer span) {
/*  393 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  394 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  395 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  396 */     GLChecks.ensurePackPBOdisabled(caps);
/*  397 */     BufferChecks.checkDirect(row);
/*  398 */     BufferChecks.checkDirect(column);
/*  399 */     BufferChecks.checkDirect(span);
/*  400 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, FloatBuffer span) {
/*  403 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  404 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  405 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  406 */     GLChecks.ensurePackPBOdisabled(caps);
/*  407 */     BufferChecks.checkDirect(row);
/*  408 */     BufferChecks.checkDirect(column);
/*  409 */     BufferChecks.checkDirect(span);
/*  410 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, IntBuffer span) {
/*  413 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  414 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  415 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  416 */     GLChecks.ensurePackPBOdisabled(caps);
/*  417 */     BufferChecks.checkDirect(row);
/*  418 */     BufferChecks.checkDirect(column);
/*  419 */     BufferChecks.checkDirect(span);
/*  420 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, ShortBuffer span) {
/*  423 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  424 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  425 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  426 */     GLChecks.ensurePackPBOdisabled(caps);
/*  427 */     BufferChecks.checkDirect(row);
/*  428 */     BufferChecks.checkDirect(column);
/*  429 */     BufferChecks.checkDirect(span);
/*  430 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, ByteBuffer span) {
/*  433 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  434 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  435 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  436 */     GLChecks.ensurePackPBOdisabled(caps);
/*  437 */     BufferChecks.checkDirect(row);
/*  438 */     BufferChecks.checkDirect(column);
/*  439 */     BufferChecks.checkDirect(span);
/*  440 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, DoubleBuffer span) {
/*  443 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  444 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  445 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  446 */     GLChecks.ensurePackPBOdisabled(caps);
/*  447 */     BufferChecks.checkDirect(row);
/*  448 */     BufferChecks.checkDirect(column);
/*  449 */     BufferChecks.checkDirect(span);
/*  450 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, FloatBuffer span) {
/*  453 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  454 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  455 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  456 */     GLChecks.ensurePackPBOdisabled(caps);
/*  457 */     BufferChecks.checkDirect(row);
/*  458 */     BufferChecks.checkDirect(column);
/*  459 */     BufferChecks.checkDirect(span);
/*  460 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, IntBuffer span) {
/*  463 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  464 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  465 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  466 */     GLChecks.ensurePackPBOdisabled(caps);
/*  467 */     BufferChecks.checkDirect(row);
/*  468 */     BufferChecks.checkDirect(column);
/*  469 */     BufferChecks.checkDirect(span);
/*  470 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, ShortBuffer span) {
/*  473 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  474 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  475 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  476 */     GLChecks.ensurePackPBOdisabled(caps);
/*  477 */     BufferChecks.checkDirect(row);
/*  478 */     BufferChecks.checkDirect(column);
/*  479 */     BufferChecks.checkDirect(span);
/*  480 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, ByteBuffer span) {
/*  483 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  484 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  485 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  486 */     GLChecks.ensurePackPBOdisabled(caps);
/*  487 */     BufferChecks.checkDirect(row);
/*  488 */     BufferChecks.checkDirect(column);
/*  489 */     BufferChecks.checkDirect(span);
/*  490 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, DoubleBuffer span) {
/*  493 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  494 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  495 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  496 */     GLChecks.ensurePackPBOdisabled(caps);
/*  497 */     BufferChecks.checkDirect(row);
/*  498 */     BufferChecks.checkDirect(column);
/*  499 */     BufferChecks.checkDirect(span);
/*  500 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, FloatBuffer span) {
/*  503 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  504 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  505 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  506 */     GLChecks.ensurePackPBOdisabled(caps);
/*  507 */     BufferChecks.checkDirect(row);
/*  508 */     BufferChecks.checkDirect(column);
/*  509 */     BufferChecks.checkDirect(span);
/*  510 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, IntBuffer span) {
/*  513 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  514 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  515 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  516 */     GLChecks.ensurePackPBOdisabled(caps);
/*  517 */     BufferChecks.checkDirect(row);
/*  518 */     BufferChecks.checkDirect(column);
/*  519 */     BufferChecks.checkDirect(span);
/*  520 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, ShortBuffer span) {
/*  523 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  524 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  525 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  526 */     GLChecks.ensurePackPBOdisabled(caps);
/*  527 */     BufferChecks.checkDirect(row);
/*  528 */     BufferChecks.checkDirect(column);
/*  529 */     BufferChecks.checkDirect(span);
/*  530 */     nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ByteBuffer span) {
/*  533 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  534 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  535 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  536 */     GLChecks.ensurePackPBOdisabled(caps);
/*  537 */     BufferChecks.checkDirect(row);
/*  538 */     BufferChecks.checkDirect(column);
/*  539 */     BufferChecks.checkDirect(span);
/*  540 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, DoubleBuffer span) {
/*  543 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  544 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  545 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  546 */     GLChecks.ensurePackPBOdisabled(caps);
/*  547 */     BufferChecks.checkDirect(row);
/*  548 */     BufferChecks.checkDirect(column);
/*  549 */     BufferChecks.checkDirect(span);
/*  550 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, FloatBuffer span) {
/*  553 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  554 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  555 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  556 */     GLChecks.ensurePackPBOdisabled(caps);
/*  557 */     BufferChecks.checkDirect(row);
/*  558 */     BufferChecks.checkDirect(column);
/*  559 */     BufferChecks.checkDirect(span);
/*  560 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, IntBuffer span) {
/*  563 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  564 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  565 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  566 */     GLChecks.ensurePackPBOdisabled(caps);
/*  567 */     BufferChecks.checkDirect(row);
/*  568 */     BufferChecks.checkDirect(column);
/*  569 */     BufferChecks.checkDirect(span);
/*  570 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ShortBuffer span) {
/*  573 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  574 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  575 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  576 */     GLChecks.ensurePackPBOdisabled(caps);
/*  577 */     BufferChecks.checkDirect(row);
/*  578 */     BufferChecks.checkDirect(column);
/*  579 */     BufferChecks.checkDirect(span);
/*  580 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ByteBuffer span) {
/*  583 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  584 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  585 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  586 */     GLChecks.ensurePackPBOdisabled(caps);
/*  587 */     BufferChecks.checkDirect(row);
/*  588 */     BufferChecks.checkDirect(column);
/*  589 */     BufferChecks.checkDirect(span);
/*  590 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, DoubleBuffer span) {
/*  593 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  594 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  595 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  596 */     GLChecks.ensurePackPBOdisabled(caps);
/*  597 */     BufferChecks.checkDirect(row);
/*  598 */     BufferChecks.checkDirect(column);
/*  599 */     BufferChecks.checkDirect(span);
/*  600 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, FloatBuffer span) {
/*  603 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  604 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  605 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  606 */     GLChecks.ensurePackPBOdisabled(caps);
/*  607 */     BufferChecks.checkDirect(row);
/*  608 */     BufferChecks.checkDirect(column);
/*  609 */     BufferChecks.checkDirect(span);
/*  610 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, IntBuffer span) {
/*  613 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  614 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  615 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  616 */     GLChecks.ensurePackPBOdisabled(caps);
/*  617 */     BufferChecks.checkDirect(row);
/*  618 */     BufferChecks.checkDirect(column);
/*  619 */     BufferChecks.checkDirect(span);
/*  620 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ShortBuffer span) {
/*  623 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  624 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  625 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  626 */     GLChecks.ensurePackPBOdisabled(caps);
/*  627 */     BufferChecks.checkDirect(row);
/*  628 */     BufferChecks.checkDirect(column);
/*  629 */     BufferChecks.checkDirect(span);
/*  630 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, ByteBuffer span) {
/*  633 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  634 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  635 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  636 */     GLChecks.ensurePackPBOdisabled(caps);
/*  637 */     BufferChecks.checkDirect(row);
/*  638 */     BufferChecks.checkDirect(column);
/*  639 */     BufferChecks.checkDirect(span);
/*  640 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, DoubleBuffer span) {
/*  643 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  644 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  645 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  646 */     GLChecks.ensurePackPBOdisabled(caps);
/*  647 */     BufferChecks.checkDirect(row);
/*  648 */     BufferChecks.checkDirect(column);
/*  649 */     BufferChecks.checkDirect(span);
/*  650 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, FloatBuffer span) {
/*  653 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  654 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  655 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  656 */     GLChecks.ensurePackPBOdisabled(caps);
/*  657 */     BufferChecks.checkDirect(row);
/*  658 */     BufferChecks.checkDirect(column);
/*  659 */     BufferChecks.checkDirect(span);
/*  660 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, IntBuffer span) {
/*  663 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  664 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  665 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  666 */     GLChecks.ensurePackPBOdisabled(caps);
/*  667 */     BufferChecks.checkDirect(row);
/*  668 */     BufferChecks.checkDirect(column);
/*  669 */     BufferChecks.checkDirect(span);
/*  670 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, ShortBuffer span) {
/*  673 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  674 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  675 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  676 */     GLChecks.ensurePackPBOdisabled(caps);
/*  677 */     BufferChecks.checkDirect(row);
/*  678 */     BufferChecks.checkDirect(column);
/*  679 */     BufferChecks.checkDirect(span);
/*  680 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, ByteBuffer span) {
/*  683 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  684 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  685 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  686 */     GLChecks.ensurePackPBOdisabled(caps);
/*  687 */     BufferChecks.checkDirect(row);
/*  688 */     BufferChecks.checkDirect(column);
/*  689 */     BufferChecks.checkDirect(span);
/*  690 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, DoubleBuffer span) {
/*  693 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  694 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  695 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  696 */     GLChecks.ensurePackPBOdisabled(caps);
/*  697 */     BufferChecks.checkDirect(row);
/*  698 */     BufferChecks.checkDirect(column);
/*  699 */     BufferChecks.checkDirect(span);
/*  700 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, FloatBuffer span) {
/*  703 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  704 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  705 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  706 */     GLChecks.ensurePackPBOdisabled(caps);
/*  707 */     BufferChecks.checkDirect(row);
/*  708 */     BufferChecks.checkDirect(column);
/*  709 */     BufferChecks.checkDirect(span);
/*  710 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, IntBuffer span) {
/*  713 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  714 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  715 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  716 */     GLChecks.ensurePackPBOdisabled(caps);
/*  717 */     BufferChecks.checkDirect(row);
/*  718 */     BufferChecks.checkDirect(column);
/*  719 */     BufferChecks.checkDirect(span);
/*  720 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, ShortBuffer span) {
/*  723 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  724 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  725 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  726 */     GLChecks.ensurePackPBOdisabled(caps);
/*  727 */     BufferChecks.checkDirect(row);
/*  728 */     BufferChecks.checkDirect(column);
/*  729 */     BufferChecks.checkDirect(span);
/*  730 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ByteBuffer span) {
/*  733 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  734 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  735 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  736 */     GLChecks.ensurePackPBOdisabled(caps);
/*  737 */     BufferChecks.checkDirect(row);
/*  738 */     BufferChecks.checkDirect(column);
/*  739 */     BufferChecks.checkDirect(span);
/*  740 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, DoubleBuffer span) {
/*  743 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  744 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  745 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  746 */     GLChecks.ensurePackPBOdisabled(caps);
/*  747 */     BufferChecks.checkDirect(row);
/*  748 */     BufferChecks.checkDirect(column);
/*  749 */     BufferChecks.checkDirect(span);
/*  750 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, FloatBuffer span) {
/*  753 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  754 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  755 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  756 */     GLChecks.ensurePackPBOdisabled(caps);
/*  757 */     BufferChecks.checkDirect(row);
/*  758 */     BufferChecks.checkDirect(column);
/*  759 */     BufferChecks.checkDirect(span);
/*  760 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, IntBuffer span) {
/*  763 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  764 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  765 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  766 */     GLChecks.ensurePackPBOdisabled(caps);
/*  767 */     BufferChecks.checkDirect(row);
/*  768 */     BufferChecks.checkDirect(column);
/*  769 */     BufferChecks.checkDirect(span);
/*  770 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ShortBuffer span) {
/*  773 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  774 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  775 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  776 */     GLChecks.ensurePackPBOdisabled(caps);
/*  777 */     BufferChecks.checkDirect(row);
/*  778 */     BufferChecks.checkDirect(column);
/*  779 */     BufferChecks.checkDirect(span);
/*  780 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 3, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, ByteBuffer span) {
/*  783 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  784 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  785 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  786 */     GLChecks.ensurePackPBOdisabled(caps);
/*  787 */     BufferChecks.checkDirect(row);
/*  788 */     BufferChecks.checkDirect(column);
/*  789 */     BufferChecks.checkDirect(span);
/*  790 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, DoubleBuffer span) {
/*  793 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  794 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  795 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  796 */     GLChecks.ensurePackPBOdisabled(caps);
/*  797 */     BufferChecks.checkDirect(row);
/*  798 */     BufferChecks.checkDirect(column);
/*  799 */     BufferChecks.checkDirect(span);
/*  800 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, FloatBuffer span) {
/*  803 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  804 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  805 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  806 */     GLChecks.ensurePackPBOdisabled(caps);
/*  807 */     BufferChecks.checkDirect(row);
/*  808 */     BufferChecks.checkDirect(column);
/*  809 */     BufferChecks.checkDirect(span);
/*  810 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, IntBuffer span) {
/*  813 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  814 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  815 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  816 */     GLChecks.ensurePackPBOdisabled(caps);
/*  817 */     BufferChecks.checkDirect(row);
/*  818 */     BufferChecks.checkDirect(column);
/*  819 */     BufferChecks.checkDirect(span);
/*  820 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, ShortBuffer span) {
/*  823 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  824 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  825 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  826 */     GLChecks.ensurePackPBOdisabled(caps);
/*  827 */     BufferChecks.checkDirect(row);
/*  828 */     BufferChecks.checkDirect(column);
/*  829 */     BufferChecks.checkDirect(span);
/*  830 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ByteBuffer span) {
/*  833 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  834 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  835 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  836 */     GLChecks.ensurePackPBOdisabled(caps);
/*  837 */     BufferChecks.checkDirect(row);
/*  838 */     BufferChecks.checkDirect(column);
/*  839 */     BufferChecks.checkDirect(span);
/*  840 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, DoubleBuffer span) {
/*  843 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  844 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  845 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  846 */     GLChecks.ensurePackPBOdisabled(caps);
/*  847 */     BufferChecks.checkDirect(row);
/*  848 */     BufferChecks.checkDirect(column);
/*  849 */     BufferChecks.checkDirect(span);
/*  850 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, FloatBuffer span) {
/*  853 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  854 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  855 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  856 */     GLChecks.ensurePackPBOdisabled(caps);
/*  857 */     BufferChecks.checkDirect(row);
/*  858 */     BufferChecks.checkDirect(column);
/*  859 */     BufferChecks.checkDirect(span);
/*  860 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, IntBuffer span) {
/*  863 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  864 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  865 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  866 */     GLChecks.ensurePackPBOdisabled(caps);
/*  867 */     BufferChecks.checkDirect(row);
/*  868 */     BufferChecks.checkDirect(column);
/*  869 */     BufferChecks.checkDirect(span);
/*  870 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ShortBuffer span) {
/*  873 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  874 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  875 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  876 */     GLChecks.ensurePackPBOdisabled(caps);
/*  877 */     BufferChecks.checkDirect(row);
/*  878 */     BufferChecks.checkDirect(column);
/*  879 */     BufferChecks.checkDirect(span);
/*  880 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, ByteBuffer span) {
/*  883 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  884 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  885 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  886 */     GLChecks.ensurePackPBOdisabled(caps);
/*  887 */     BufferChecks.checkDirect(row);
/*  888 */     BufferChecks.checkDirect(column);
/*  889 */     BufferChecks.checkDirect(span);
/*  890 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, DoubleBuffer span) {
/*  893 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  894 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  895 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  896 */     GLChecks.ensurePackPBOdisabled(caps);
/*  897 */     BufferChecks.checkDirect(row);
/*  898 */     BufferChecks.checkDirect(column);
/*  899 */     BufferChecks.checkDirect(span);
/*  900 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, FloatBuffer span) {
/*  903 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  904 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  905 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  906 */     GLChecks.ensurePackPBOdisabled(caps);
/*  907 */     BufferChecks.checkDirect(row);
/*  908 */     BufferChecks.checkDirect(column);
/*  909 */     BufferChecks.checkDirect(span);
/*  910 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, IntBuffer span) {
/*  913 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  914 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  915 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  916 */     GLChecks.ensurePackPBOdisabled(caps);
/*  917 */     BufferChecks.checkDirect(row);
/*  918 */     BufferChecks.checkDirect(column);
/*  919 */     BufferChecks.checkDirect(span);
/*  920 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, ShortBuffer span) {
/*  923 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  924 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  925 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  926 */     GLChecks.ensurePackPBOdisabled(caps);
/*  927 */     BufferChecks.checkDirect(row);
/*  928 */     BufferChecks.checkDirect(column);
/*  929 */     BufferChecks.checkDirect(span);
/*  930 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, ByteBuffer span) {
/*  933 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  934 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  935 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  936 */     GLChecks.ensurePackPBOdisabled(caps);
/*  937 */     BufferChecks.checkDirect(row);
/*  938 */     BufferChecks.checkDirect(column);
/*  939 */     BufferChecks.checkDirect(span);
/*  940 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, DoubleBuffer span) {
/*  943 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  944 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  945 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  946 */     GLChecks.ensurePackPBOdisabled(caps);
/*  947 */     BufferChecks.checkDirect(row);
/*  948 */     BufferChecks.checkDirect(column);
/*  949 */     BufferChecks.checkDirect(span);
/*  950 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, FloatBuffer span) {
/*  953 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  954 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  955 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  956 */     GLChecks.ensurePackPBOdisabled(caps);
/*  957 */     BufferChecks.checkDirect(row);
/*  958 */     BufferChecks.checkDirect(column);
/*  959 */     BufferChecks.checkDirect(span);
/*  960 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, IntBuffer span) {
/*  963 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  964 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  965 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  966 */     GLChecks.ensurePackPBOdisabled(caps);
/*  967 */     BufferChecks.checkDirect(row);
/*  968 */     BufferChecks.checkDirect(column);
/*  969 */     BufferChecks.checkDirect(span);
/*  970 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, ShortBuffer span) {
/*  973 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  974 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  975 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  976 */     GLChecks.ensurePackPBOdisabled(caps);
/*  977 */     BufferChecks.checkDirect(row);
/*  978 */     BufferChecks.checkDirect(column);
/*  979 */     BufferChecks.checkDirect(span);
/*  980 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, ByteBuffer span) {
/*  983 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  984 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  985 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  986 */     GLChecks.ensurePackPBOdisabled(caps);
/*  987 */     BufferChecks.checkDirect(row);
/*  988 */     BufferChecks.checkDirect(column);
/*  989 */     BufferChecks.checkDirect(span);
/*  990 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, DoubleBuffer span) {
/*  993 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  994 */     long function_pointer = caps.glGetnSeparableFilterARB;
/*  995 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  996 */     GLChecks.ensurePackPBOdisabled(caps);
/*  997 */     BufferChecks.checkDirect(row);
/*  998 */     BufferChecks.checkDirect(column);
/*  999 */     BufferChecks.checkDirect(span);
/* 1000 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, FloatBuffer span) {
/* 1003 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1004 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1005 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1006 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1007 */     BufferChecks.checkDirect(row);
/* 1008 */     BufferChecks.checkDirect(column);
/* 1009 */     BufferChecks.checkDirect(span);
/* 1010 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, IntBuffer span) {
/* 1013 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1014 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1015 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1016 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1017 */     BufferChecks.checkDirect(row);
/* 1018 */     BufferChecks.checkDirect(column);
/* 1019 */     BufferChecks.checkDirect(span);
/* 1020 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1023 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1024 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1025 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1026 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1027 */     BufferChecks.checkDirect(row);
/* 1028 */     BufferChecks.checkDirect(column);
/* 1029 */     BufferChecks.checkDirect(span);
/* 1030 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, ByteBuffer span) {
/* 1033 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1034 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1035 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1036 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1037 */     BufferChecks.checkDirect(row);
/* 1038 */     BufferChecks.checkDirect(column);
/* 1039 */     BufferChecks.checkDirect(span);
/* 1040 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, DoubleBuffer span) {
/* 1043 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1044 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1045 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1046 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1047 */     BufferChecks.checkDirect(row);
/* 1048 */     BufferChecks.checkDirect(column);
/* 1049 */     BufferChecks.checkDirect(span);
/* 1050 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, FloatBuffer span) {
/* 1053 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1054 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1055 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1056 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1057 */     BufferChecks.checkDirect(row);
/* 1058 */     BufferChecks.checkDirect(column);
/* 1059 */     BufferChecks.checkDirect(span);
/* 1060 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, IntBuffer span) {
/* 1063 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1064 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1065 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1066 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1067 */     BufferChecks.checkDirect(row);
/* 1068 */     BufferChecks.checkDirect(column);
/* 1069 */     BufferChecks.checkDirect(span);
/* 1070 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, ShortBuffer span) {
/* 1073 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1074 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1075 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1076 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1077 */     BufferChecks.checkDirect(row);
/* 1078 */     BufferChecks.checkDirect(column);
/* 1079 */     BufferChecks.checkDirect(span);
/* 1080 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, ByteBuffer span) {
/* 1083 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1084 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1085 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1086 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1087 */     BufferChecks.checkDirect(row);
/* 1088 */     BufferChecks.checkDirect(column);
/* 1089 */     BufferChecks.checkDirect(span);
/* 1090 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, DoubleBuffer span) {
/* 1093 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1094 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1095 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1096 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1097 */     BufferChecks.checkDirect(row);
/* 1098 */     BufferChecks.checkDirect(column);
/* 1099 */     BufferChecks.checkDirect(span);
/* 1100 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, FloatBuffer span) {
/* 1103 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1104 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1105 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1106 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1107 */     BufferChecks.checkDirect(row);
/* 1108 */     BufferChecks.checkDirect(column);
/* 1109 */     BufferChecks.checkDirect(span);
/* 1110 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, IntBuffer span) {
/* 1113 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1114 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1115 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1116 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1117 */     BufferChecks.checkDirect(row);
/* 1118 */     BufferChecks.checkDirect(column);
/* 1119 */     BufferChecks.checkDirect(span);
/* 1120 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, ShortBuffer span) {
/* 1123 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1124 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1125 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1126 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1127 */     BufferChecks.checkDirect(row);
/* 1128 */     BufferChecks.checkDirect(column);
/* 1129 */     BufferChecks.checkDirect(span);
/* 1130 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, ByteBuffer span) {
/* 1133 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1134 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1135 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1136 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1137 */     BufferChecks.checkDirect(row);
/* 1138 */     BufferChecks.checkDirect(column);
/* 1139 */     BufferChecks.checkDirect(span);
/* 1140 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, DoubleBuffer span) {
/* 1143 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1144 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1145 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1146 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1147 */     BufferChecks.checkDirect(row);
/* 1148 */     BufferChecks.checkDirect(column);
/* 1149 */     BufferChecks.checkDirect(span);
/* 1150 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, FloatBuffer span) {
/* 1153 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1154 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1155 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1156 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1157 */     BufferChecks.checkDirect(row);
/* 1158 */     BufferChecks.checkDirect(column);
/* 1159 */     BufferChecks.checkDirect(span);
/* 1160 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, IntBuffer span) {
/* 1163 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1164 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1165 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1166 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1167 */     BufferChecks.checkDirect(row);
/* 1168 */     BufferChecks.checkDirect(column);
/* 1169 */     BufferChecks.checkDirect(span);
/* 1170 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, ShortBuffer span) {
/* 1173 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1174 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1175 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1176 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1177 */     BufferChecks.checkDirect(row);
/* 1178 */     BufferChecks.checkDirect(column);
/* 1179 */     BufferChecks.checkDirect(span);
/* 1180 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, ByteBuffer span) {
/* 1183 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1184 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1185 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1186 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1187 */     BufferChecks.checkDirect(row);
/* 1188 */     BufferChecks.checkDirect(column);
/* 1189 */     BufferChecks.checkDirect(span);
/* 1190 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, DoubleBuffer span) {
/* 1193 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1194 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1195 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1196 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1197 */     BufferChecks.checkDirect(row);
/* 1198 */     BufferChecks.checkDirect(column);
/* 1199 */     BufferChecks.checkDirect(span);
/* 1200 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, FloatBuffer span) {
/* 1203 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1204 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1205 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1206 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1207 */     BufferChecks.checkDirect(row);
/* 1208 */     BufferChecks.checkDirect(column);
/* 1209 */     BufferChecks.checkDirect(span);
/* 1210 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, IntBuffer span) {
/* 1213 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1214 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1215 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1216 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1217 */     BufferChecks.checkDirect(row);
/* 1218 */     BufferChecks.checkDirect(column);
/* 1219 */     BufferChecks.checkDirect(span);
/* 1220 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, ShortBuffer span) {
/* 1223 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1224 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1225 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1226 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1227 */     BufferChecks.checkDirect(row);
/* 1228 */     BufferChecks.checkDirect(column);
/* 1229 */     BufferChecks.checkDirect(span);
/* 1230 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, ByteBuffer span) {
/* 1233 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1234 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1235 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1236 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1237 */     BufferChecks.checkDirect(row);
/* 1238 */     BufferChecks.checkDirect(column);
/* 1239 */     BufferChecks.checkDirect(span);
/* 1240 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, DoubleBuffer span) {
/* 1243 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1244 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1245 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1246 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1247 */     BufferChecks.checkDirect(row);
/* 1248 */     BufferChecks.checkDirect(column);
/* 1249 */     BufferChecks.checkDirect(span);
/* 1250 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, FloatBuffer span) {
/* 1253 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1254 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1255 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1256 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1257 */     BufferChecks.checkDirect(row);
/* 1258 */     BufferChecks.checkDirect(column);
/* 1259 */     BufferChecks.checkDirect(span);
/* 1260 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, IntBuffer span) {
/* 1263 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1264 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1265 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1266 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1267 */     BufferChecks.checkDirect(row);
/* 1268 */     BufferChecks.checkDirect(column);
/* 1269 */     BufferChecks.checkDirect(span);
/* 1270 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1273 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1274 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1275 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1276 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1277 */     BufferChecks.checkDirect(row);
/* 1278 */     BufferChecks.checkDirect(column);
/* 1279 */     BufferChecks.checkDirect(span);
/* 1280 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 2, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, ByteBuffer span) {
/* 1283 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1284 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1285 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1286 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1287 */     BufferChecks.checkDirect(row);
/* 1288 */     BufferChecks.checkDirect(column);
/* 1289 */     BufferChecks.checkDirect(span);
/* 1290 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, DoubleBuffer span) {
/* 1293 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1294 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1295 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1296 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1297 */     BufferChecks.checkDirect(row);
/* 1298 */     BufferChecks.checkDirect(column);
/* 1299 */     BufferChecks.checkDirect(span);
/* 1300 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, FloatBuffer span) {
/* 1303 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1304 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1305 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1306 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1307 */     BufferChecks.checkDirect(row);
/* 1308 */     BufferChecks.checkDirect(column);
/* 1309 */     BufferChecks.checkDirect(span);
/* 1310 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, IntBuffer span) {
/* 1313 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1314 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1315 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1316 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1317 */     BufferChecks.checkDirect(row);
/* 1318 */     BufferChecks.checkDirect(column);
/* 1319 */     BufferChecks.checkDirect(span);
/* 1320 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, ShortBuffer span) {
/* 1323 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1324 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1325 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1326 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1327 */     BufferChecks.checkDirect(row);
/* 1328 */     BufferChecks.checkDirect(column);
/* 1329 */     BufferChecks.checkDirect(span);
/* 1330 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ByteBuffer span) {
/* 1333 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1334 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1335 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1336 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1337 */     BufferChecks.checkDirect(row);
/* 1338 */     BufferChecks.checkDirect(column);
/* 1339 */     BufferChecks.checkDirect(span);
/* 1340 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, DoubleBuffer span) {
/* 1343 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1344 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1345 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1346 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1347 */     BufferChecks.checkDirect(row);
/* 1348 */     BufferChecks.checkDirect(column);
/* 1349 */     BufferChecks.checkDirect(span);
/* 1350 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, FloatBuffer span) {
/* 1353 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1354 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1355 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1356 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1357 */     BufferChecks.checkDirect(row);
/* 1358 */     BufferChecks.checkDirect(column);
/* 1359 */     BufferChecks.checkDirect(span);
/* 1360 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, IntBuffer span) {
/* 1363 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1364 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1365 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1366 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1367 */     BufferChecks.checkDirect(row);
/* 1368 */     BufferChecks.checkDirect(column);
/* 1369 */     BufferChecks.checkDirect(span);
/* 1370 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ShortBuffer span) {
/* 1373 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1374 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1375 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1376 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1377 */     BufferChecks.checkDirect(row);
/* 1378 */     BufferChecks.checkDirect(column);
/* 1379 */     BufferChecks.checkDirect(span);
/* 1380 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 3, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, ByteBuffer span) {
/* 1383 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1384 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1385 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1386 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1387 */     BufferChecks.checkDirect(row);
/* 1388 */     BufferChecks.checkDirect(column);
/* 1389 */     BufferChecks.checkDirect(span);
/* 1390 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, DoubleBuffer span) {
/* 1393 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1394 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1395 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1396 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1397 */     BufferChecks.checkDirect(row);
/* 1398 */     BufferChecks.checkDirect(column);
/* 1399 */     BufferChecks.checkDirect(span);
/* 1400 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, FloatBuffer span) {
/* 1403 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1404 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1405 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1406 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1407 */     BufferChecks.checkDirect(row);
/* 1408 */     BufferChecks.checkDirect(column);
/* 1409 */     BufferChecks.checkDirect(span);
/* 1410 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, IntBuffer span) {
/* 1413 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1414 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1415 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1416 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1417 */     BufferChecks.checkDirect(row);
/* 1418 */     BufferChecks.checkDirect(column);
/* 1419 */     BufferChecks.checkDirect(span);
/* 1420 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, ShortBuffer span) {
/* 1423 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1424 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1425 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1426 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1427 */     BufferChecks.checkDirect(row);
/* 1428 */     BufferChecks.checkDirect(column);
/* 1429 */     BufferChecks.checkDirect(span);
/* 1430 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, ByteBuffer span) {
/* 1433 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1434 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1435 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1436 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1437 */     BufferChecks.checkDirect(row);
/* 1438 */     BufferChecks.checkDirect(column);
/* 1439 */     BufferChecks.checkDirect(span);
/* 1440 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, DoubleBuffer span) {
/* 1443 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1444 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1445 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1446 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1447 */     BufferChecks.checkDirect(row);
/* 1448 */     BufferChecks.checkDirect(column);
/* 1449 */     BufferChecks.checkDirect(span);
/* 1450 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, FloatBuffer span) {
/* 1453 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1454 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1455 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1456 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1457 */     BufferChecks.checkDirect(row);
/* 1458 */     BufferChecks.checkDirect(column);
/* 1459 */     BufferChecks.checkDirect(span);
/* 1460 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, IntBuffer span) {
/* 1463 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1464 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1465 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1466 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1467 */     BufferChecks.checkDirect(row);
/* 1468 */     BufferChecks.checkDirect(column);
/* 1469 */     BufferChecks.checkDirect(span);
/* 1470 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, ShortBuffer span) {
/* 1473 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1474 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1475 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1476 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1477 */     BufferChecks.checkDirect(row);
/* 1478 */     BufferChecks.checkDirect(column);
/* 1479 */     BufferChecks.checkDirect(span);
/* 1480 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 2, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, ByteBuffer span) {
/* 1483 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1484 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1485 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1486 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1487 */     BufferChecks.checkDirect(row);
/* 1488 */     BufferChecks.checkDirect(column);
/* 1489 */     BufferChecks.checkDirect(span);
/* 1490 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, DoubleBuffer span) {
/* 1493 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1494 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1495 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1496 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1497 */     BufferChecks.checkDirect(row);
/* 1498 */     BufferChecks.checkDirect(column);
/* 1499 */     BufferChecks.checkDirect(span);
/* 1500 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, FloatBuffer span) {
/* 1503 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1504 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1505 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1506 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1507 */     BufferChecks.checkDirect(row);
/* 1508 */     BufferChecks.checkDirect(column);
/* 1509 */     BufferChecks.checkDirect(span);
/* 1510 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, IntBuffer span) {
/* 1513 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1514 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1515 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1516 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1517 */     BufferChecks.checkDirect(row);
/* 1518 */     BufferChecks.checkDirect(column);
/* 1519 */     BufferChecks.checkDirect(span);
/* 1520 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, ShortBuffer span) {
/* 1523 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1524 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1525 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1526 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1527 */     BufferChecks.checkDirect(row);
/* 1528 */     BufferChecks.checkDirect(column);
/* 1529 */     BufferChecks.checkDirect(span);
/* 1530 */     nglGetnSeparableFilterARB(target, format, type, row.remaining() << 1, MemoryUtil.getAddress(row), column.remaining() << 1, MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
/*      */   }
/*      */   static native void nglGetnSeparableFilterARB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, int paramInt5, long paramLong2, long paramLong3, long paramLong4);
/*      */   public static void glGetnSeparableFilterARB(int target, int format, int type, int row_rowBufSize, long row_buffer_offset, int column_columnBufSize, long column_buffer_offset, long span_buffer_offset) {
/* 1534 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1535 */     long function_pointer = caps.glGetnSeparableFilterARB;
/* 1536 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1537 */     GLChecks.ensurePackPBOenabled(caps);
/* 1538 */     nglGetnSeparableFilterARBBO(target, format, type, row_rowBufSize, row_buffer_offset, column_columnBufSize, column_buffer_offset, span_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetnSeparableFilterARBBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, int paramInt5, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static void glGetnHistogramARB(int target, boolean reset, int format, int type, ByteBuffer values) {
/* 1543 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1544 */     long function_pointer = caps.glGetnHistogramARB;
/* 1545 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1546 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1547 */     BufferChecks.checkDirect(values);
/* 1548 */     nglGetnHistogramARB(target, reset, format, type, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnHistogramARB(int target, boolean reset, int format, int type, DoubleBuffer values) {
/* 1551 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1552 */     long function_pointer = caps.glGetnHistogramARB;
/* 1553 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1554 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1555 */     BufferChecks.checkDirect(values);
/* 1556 */     nglGetnHistogramARB(target, reset, format, type, values.remaining() << 3, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnHistogramARB(int target, boolean reset, int format, int type, FloatBuffer values) {
/* 1559 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1560 */     long function_pointer = caps.glGetnHistogramARB;
/* 1561 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1562 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1563 */     BufferChecks.checkDirect(values);
/* 1564 */     nglGetnHistogramARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnHistogramARB(int target, boolean reset, int format, int type, IntBuffer values) {
/* 1567 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1568 */     long function_pointer = caps.glGetnHistogramARB;
/* 1569 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1570 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1571 */     BufferChecks.checkDirect(values);
/* 1572 */     nglGetnHistogramARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnHistogramARB(int target, boolean reset, int format, int type, ShortBuffer values) {
/* 1575 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1576 */     long function_pointer = caps.glGetnHistogramARB;
/* 1577 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1578 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1579 */     BufferChecks.checkDirect(values);
/* 1580 */     nglGetnHistogramARB(target, reset, format, type, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetnHistogramARB(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   public static void glGetnHistogramARB(int target, boolean reset, int format, int type, int values_bufSize, long values_buffer_offset) {
/* 1584 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1585 */     long function_pointer = caps.glGetnHistogramARB;
/* 1586 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1587 */     GLChecks.ensurePackPBOenabled(caps);
/* 1588 */     nglGetnHistogramARBBO(target, reset, format, type, values_bufSize, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetnHistogramARBBO(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, ByteBuffer values) {
/* 1593 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1594 */     long function_pointer = caps.glGetnMinmaxARB;
/* 1595 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1596 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1597 */     BufferChecks.checkDirect(values);
/* 1598 */     nglGetnMinmaxARB(target, reset, format, type, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, DoubleBuffer values) {
/* 1601 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1602 */     long function_pointer = caps.glGetnMinmaxARB;
/* 1603 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1604 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1605 */     BufferChecks.checkDirect(values);
/* 1606 */     nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 3, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, FloatBuffer values) {
/* 1609 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1610 */     long function_pointer = caps.glGetnMinmaxARB;
/* 1611 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1612 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1613 */     BufferChecks.checkDirect(values);
/* 1614 */     nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, IntBuffer values) {
/* 1617 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1618 */     long function_pointer = caps.glGetnMinmaxARB;
/* 1619 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1620 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1621 */     BufferChecks.checkDirect(values);
/* 1622 */     nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, ShortBuffer values) {
/* 1625 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1626 */     long function_pointer = caps.glGetnMinmaxARB;
/* 1627 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1628 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1629 */     BufferChecks.checkDirect(values);
/* 1630 */     nglGetnMinmaxARB(target, reset, format, type, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetnMinmaxARB(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, int values_bufSize, long values_buffer_offset) {
/* 1634 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1635 */     long function_pointer = caps.glGetnMinmaxARB;
/* 1636 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1637 */     GLChecks.ensurePackPBOenabled(caps);
/* 1638 */     nglGetnMinmaxARBBO(target, reset, format, type, values_bufSize, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetnMinmaxARBBO(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnCompressedTexImageARB(int target, int lod, ByteBuffer img) {
/* 1643 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1644 */     long function_pointer = caps.glGetnCompressedTexImageARB;
/* 1645 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1646 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1647 */     BufferChecks.checkDirect(img);
/* 1648 */     nglGetnCompressedTexImageARB(target, lod, img.remaining(), MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   public static void glGetnCompressedTexImageARB(int target, int lod, IntBuffer img) {
/* 1651 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1652 */     long function_pointer = caps.glGetnCompressedTexImageARB;
/* 1653 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1654 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1655 */     BufferChecks.checkDirect(img);
/* 1656 */     nglGetnCompressedTexImageARB(target, lod, img.remaining() << 2, MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   public static void glGetnCompressedTexImageARB(int target, int lod, ShortBuffer img) {
/* 1659 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1660 */     long function_pointer = caps.glGetnCompressedTexImageARB;
/* 1661 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1662 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1663 */     BufferChecks.checkDirect(img);
/* 1664 */     nglGetnCompressedTexImageARB(target, lod, img.remaining() << 1, MemoryUtil.getAddress(img), function_pointer);
/*      */   }
/*      */   static native void nglGetnCompressedTexImageARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glGetnCompressedTexImageARB(int target, int lod, int img_bufSize, long img_buffer_offset) {
/* 1668 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1669 */     long function_pointer = caps.glGetnCompressedTexImageARB;
/* 1670 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1671 */     GLChecks.ensurePackPBOenabled(caps);
/* 1672 */     nglGetnCompressedTexImageARBBO(target, lod, img_bufSize, img_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetnCompressedTexImageARBBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniformfvARB(int program, int location, FloatBuffer params) {
/* 1677 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1678 */     long function_pointer = caps.glGetnUniformfvARB;
/* 1679 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1680 */     BufferChecks.checkDirect(params);
/* 1681 */     nglGetnUniformfvARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetnUniformfvARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniformivARB(int program, int location, IntBuffer params) {
/* 1686 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1687 */     long function_pointer = caps.glGetnUniformivARB;
/* 1688 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1689 */     BufferChecks.checkDirect(params);
/* 1690 */     nglGetnUniformivARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetnUniformivARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniformuivARB(int program, int location, IntBuffer params) {
/* 1695 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1696 */     long function_pointer = caps.glGetnUniformuivARB;
/* 1697 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1698 */     BufferChecks.checkDirect(params);
/* 1699 */     nglGetnUniformuivARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetnUniformuivARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetnUniformdvARB(int program, int location, DoubleBuffer params) {
/* 1704 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1705 */     long function_pointer = caps.glGetnUniformdvARB;
/* 1706 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1707 */     BufferChecks.checkDirect(params);
/* 1708 */     nglGetnUniformdvARB(program, location, params.remaining() << 3, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetnUniformdvARB(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ARBRobustness.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */