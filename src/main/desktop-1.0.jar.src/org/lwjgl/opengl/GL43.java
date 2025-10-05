/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.LongBuffer;
/*      */ import org.lwjgl.BufferChecks;
/*      */ import org.lwjgl.MemoryUtil;
/*      */ import org.lwjgl.PointerWrapper;
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
/*      */ public final class GL43
/*      */ {
/*      */   public static final int GL_NUM_SHADING_LANGUAGE_VERSIONS = 33513;
/*      */   public static final int GL_VERTEX_ATTRIB_ARRAY_LONG = 34638;
/*      */   public static final int GL_COMPRESSED_RGB8_ETC2 = 37492;
/*      */   public static final int GL_COMPRESSED_SRGB8_ETC2 = 37493;
/*      */   public static final int GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 37494;
/*      */   public static final int GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 37495;
/*      */   public static final int GL_COMPRESSED_RGBA8_ETC2_EAC = 37496;
/*      */   public static final int GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 37497;
/*      */   public static final int GL_COMPRESSED_R11_EAC = 37488;
/*      */   public static final int GL_COMPRESSED_SIGNED_R11_EAC = 37489;
/*      */   public static final int GL_COMPRESSED_RG11_EAC = 37490;
/*      */   public static final int GL_COMPRESSED_SIGNED_RG11_EAC = 37491;
/*      */   public static final int GL_PRIMITIVE_RESTART_FIXED_INDEX = 36201;
/*      */   public static final int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 36202;
/*      */   public static final int GL_MAX_ELEMENT_INDEX = 36203;
/*      */   public static final int GL_COMPUTE_SHADER = 37305;
/*      */   public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 37307;
/*      */   public static final int GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 37308;
/*      */   public static final int GL_MAX_COMPUTE_IMAGE_UNIFORMS = 37309;
/*      */   public static final int GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 33378;
/*      */   public static final int GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 33379;
/*      */   public static final int GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 33380;
/*      */   public static final int GL_MAX_COMPUTE_ATOMIC_COUNTERS = 33381;
/*      */   public static final int GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 33382;
/*      */   public static final int GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 37099;
/*      */   public static final int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 37310;
/*      */   public static final int GL_MAX_COMPUTE_WORK_GROUP_SIZE = 37311;
/*      */   public static final int GL_COMPUTE_WORK_GROUP_SIZE = 33383;
/*      */   public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 37100;
/*      */   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 37101;
/*      */   public static final int GL_DISPATCH_INDIRECT_BUFFER = 37102;
/*      */   public static final int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 37103;
/*      */   public static final int GL_COMPUTE_SHADER_BIT = 32;
/*      */   public static final int GL_DEBUG_OUTPUT = 37600;
/*      */   public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 33346;
/*      */   public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 2;
/*      */   public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 37187;
/*      */   public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 37188;
/*      */   public static final int GL_DEBUG_LOGGED_MESSAGES = 37189;
/*      */   public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 33347;
/*      */   public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 33388;
/*      */   public static final int GL_DEBUG_GROUP_STACK_DEPTH = 33389;
/*      */   public static final int GL_MAX_LABEL_LENGTH = 33512;
/*      */   public static final int GL_DEBUG_CALLBACK_FUNCTION = 33348;
/*      */   public static final int GL_DEBUG_CALLBACK_USER_PARAM = 33349;
/*      */   public static final int GL_DEBUG_SOURCE_API = 33350;
/*      */   public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
/*      */   public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
/*      */   public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
/*      */   public static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
/*      */   public static final int GL_DEBUG_SOURCE_OTHER = 33355;
/*      */   public static final int GL_DEBUG_TYPE_ERROR = 33356;
/*      */   public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
/*      */   public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
/*      */   public static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
/*      */   public static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
/*      */   public static final int GL_DEBUG_TYPE_OTHER = 33361;
/*      */   public static final int GL_DEBUG_TYPE_MARKER = 33384;
/*      */   public static final int GL_DEBUG_TYPE_PUSH_GROUP = 33385;
/*      */   public static final int GL_DEBUG_TYPE_POP_GROUP = 33386;
/*      */   public static final int GL_DEBUG_SEVERITY_HIGH = 37190;
/*      */   public static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
/*      */   public static final int GL_DEBUG_SEVERITY_LOW = 37192;
/*      */   public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
/*      */   public static final int GL_BUFFER = 33504;
/*      */   public static final int GL_SHADER = 33505;
/*      */   public static final int GL_PROGRAM = 33506;
/*      */   public static final int GL_QUERY = 33507;
/*      */   public static final int GL_PROGRAM_PIPELINE = 33508;
/*      */   public static final int GL_SAMPLER = 33510;
/*      */   public static final int GL_DISPLAY_LIST = 33511;
/*      */   public static final int GL_MAX_UNIFORM_LOCATIONS = 33390;
/*      */   public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 37648;
/*      */   public static final int GL_FRAMEBUFFER_DEFAULT_HEIGHT = 37649;
/*      */   public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 37650;
/*      */   public static final int GL_FRAMEBUFFER_DEFAULT_SAMPLES = 37651;
/*      */   public static final int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 37652;
/*      */   public static final int GL_MAX_FRAMEBUFFER_WIDTH = 37653;
/*      */   public static final int GL_MAX_FRAMEBUFFER_HEIGHT = 37654;
/*      */   public static final int GL_MAX_FRAMEBUFFER_LAYERS = 37655;
/*      */   public static final int GL_MAX_FRAMEBUFFER_SAMPLES = 37656;
/*      */   public static final int GL_INTERNALFORMAT_SUPPORTED = 33391;
/*      */   public static final int GL_INTERNALFORMAT_PREFERRED = 33392;
/*      */   public static final int GL_INTERNALFORMAT_RED_SIZE = 33393;
/*      */   public static final int GL_INTERNALFORMAT_GREEN_SIZE = 33394;
/*      */   public static final int GL_INTERNALFORMAT_BLUE_SIZE = 33395;
/*      */   public static final int GL_INTERNALFORMAT_ALPHA_SIZE = 33396;
/*      */   public static final int GL_INTERNALFORMAT_DEPTH_SIZE = 33397;
/*      */   public static final int GL_INTERNALFORMAT_STENCIL_SIZE = 33398;
/*      */   public static final int GL_INTERNALFORMAT_SHARED_SIZE = 33399;
/*      */   public static final int GL_INTERNALFORMAT_RED_TYPE = 33400;
/*      */   public static final int GL_INTERNALFORMAT_GREEN_TYPE = 33401;
/*      */   public static final int GL_INTERNALFORMAT_BLUE_TYPE = 33402;
/*      */   public static final int GL_INTERNALFORMAT_ALPHA_TYPE = 33403;
/*      */   public static final int GL_INTERNALFORMAT_DEPTH_TYPE = 33404;
/*      */   public static final int GL_INTERNALFORMAT_STENCIL_TYPE = 33405;
/*      */   public static final int GL_MAX_WIDTH = 33406;
/*      */   public static final int GL_MAX_HEIGHT = 33407;
/*      */   public static final int GL_MAX_DEPTH = 33408;
/*      */   public static final int GL_MAX_LAYERS = 33409;
/*      */   public static final int GL_MAX_COMBINED_DIMENSIONS = 33410;
/*      */   public static final int GL_COLOR_COMPONENTS = 33411;
/*      */   public static final int GL_DEPTH_COMPONENTS = 33412;
/*      */   public static final int GL_STENCIL_COMPONENTS = 33413;
/*      */   public static final int GL_COLOR_RENDERABLE = 33414;
/*      */   public static final int GL_DEPTH_RENDERABLE = 33415;
/*      */   public static final int GL_STENCIL_RENDERABLE = 33416;
/*      */   public static final int GL_FRAMEBUFFER_RENDERABLE = 33417;
/*      */   public static final int GL_FRAMEBUFFER_RENDERABLE_LAYERED = 33418;
/*      */   public static final int GL_FRAMEBUFFER_BLEND = 33419;
/*      */   public static final int GL_READ_PIXELS = 33420;
/*      */   public static final int GL_READ_PIXELS_FORMAT = 33421;
/*      */   public static final int GL_READ_PIXELS_TYPE = 33422;
/*      */   public static final int GL_TEXTURE_IMAGE_FORMAT = 33423;
/*      */   public static final int GL_TEXTURE_IMAGE_TYPE = 33424;
/*      */   public static final int GL_GET_TEXTURE_IMAGE_FORMAT = 33425;
/*      */   public static final int GL_GET_TEXTURE_IMAGE_TYPE = 33426;
/*      */   public static final int GL_MIPMAP = 33427;
/*      */   public static final int GL_MANUAL_GENERATE_MIPMAP = 33428;
/*      */   public static final int GL_AUTO_GENERATE_MIPMAP = 33429;
/*      */   public static final int GL_COLOR_ENCODING = 33430;
/*      */   public static final int GL_SRGB_READ = 33431;
/*      */   public static final int GL_SRGB_WRITE = 33432;
/*      */   public static final int GL_SRGB_DECODE_ARB = 33433;
/*      */   public static final int GL_FILTER = 33434;
/*      */   public static final int GL_VERTEX_TEXTURE = 33435;
/*      */   public static final int GL_TESS_CONTROL_TEXTURE = 33436;
/*      */   public static final int GL_TESS_EVALUATION_TEXTURE = 33437;
/*      */   public static final int GL_GEOMETRY_TEXTURE = 33438;
/*      */   public static final int GL_FRAGMENT_TEXTURE = 33439;
/*      */   public static final int GL_COMPUTE_TEXTURE = 33440;
/*      */   public static final int GL_TEXTURE_SHADOW = 33441;
/*      */   public static final int GL_TEXTURE_GATHER = 33442;
/*      */   public static final int GL_TEXTURE_GATHER_SHADOW = 33443;
/*      */   public static final int GL_SHADER_IMAGE_LOAD = 33444;
/*      */   public static final int GL_SHADER_IMAGE_STORE = 33445;
/*      */   public static final int GL_SHADER_IMAGE_ATOMIC = 33446;
/*      */   public static final int GL_IMAGE_TEXEL_SIZE = 33447;
/*      */   public static final int GL_IMAGE_COMPATIBILITY_CLASS = 33448;
/*      */   public static final int GL_IMAGE_PIXEL_FORMAT = 33449;
/*      */   public static final int GL_IMAGE_PIXEL_TYPE = 33450;
/*      */   public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST = 33452;
/*      */   public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST = 33453;
/*      */   public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE = 33454;
/*      */   public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 33455;
/*      */   public static final int GL_TEXTURE_COMPRESSED_BLOCK_WIDTH = 33457;
/*      */   public static final int GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT = 33458;
/*      */   public static final int GL_TEXTURE_COMPRESSED_BLOCK_SIZE = 33459;
/*      */   public static final int GL_CLEAR_BUFFER = 33460;
/*      */   public static final int GL_TEXTURE_VIEW = 33461;
/*      */   public static final int GL_VIEW_COMPATIBILITY_CLASS = 33462;
/*      */   public static final int GL_FULL_SUPPORT = 33463;
/*      */   public static final int GL_CAVEAT_SUPPORT = 33464;
/*      */   public static final int GL_IMAGE_CLASS_4_X_32 = 33465;
/*      */   public static final int GL_IMAGE_CLASS_2_X_32 = 33466;
/*      */   public static final int GL_IMAGE_CLASS_1_X_32 = 33467;
/*      */   public static final int GL_IMAGE_CLASS_4_X_16 = 33468;
/*      */   public static final int GL_IMAGE_CLASS_2_X_16 = 33469;
/*      */   public static final int GL_IMAGE_CLASS_1_X_16 = 33470;
/*      */   public static final int GL_IMAGE_CLASS_4_X_8 = 33471;
/*      */   public static final int GL_IMAGE_CLASS_2_X_8 = 33472;
/*      */   public static final int GL_IMAGE_CLASS_1_X_8 = 33473;
/*      */   public static final int GL_IMAGE_CLASS_11_11_10 = 33474;
/*      */   public static final int GL_IMAGE_CLASS_10_10_10_2 = 33475;
/*      */   public static final int GL_VIEW_CLASS_128_BITS = 33476;
/*      */   public static final int GL_VIEW_CLASS_96_BITS = 33477;
/*      */   public static final int GL_VIEW_CLASS_64_BITS = 33478;
/*      */   public static final int GL_VIEW_CLASS_48_BITS = 33479;
/*      */   public static final int GL_VIEW_CLASS_32_BITS = 33480;
/*      */   public static final int GL_VIEW_CLASS_24_BITS = 33481;
/*      */   public static final int GL_VIEW_CLASS_16_BITS = 33482;
/*      */   public static final int GL_VIEW_CLASS_8_BITS = 33483;
/*      */   public static final int GL_VIEW_CLASS_S3TC_DXT1_RGB = 33484;
/*      */   public static final int GL_VIEW_CLASS_S3TC_DXT1_RGBA = 33485;
/*      */   public static final int GL_VIEW_CLASS_S3TC_DXT3_RGBA = 33486;
/*      */   public static final int GL_VIEW_CLASS_S3TC_DXT5_RGBA = 33487;
/*      */   public static final int GL_VIEW_CLASS_RGTC1_RED = 33488;
/*      */   public static final int GL_VIEW_CLASS_RGTC2_RG = 33489;
/*      */   public static final int GL_VIEW_CLASS_BPTC_UNORM = 33490;
/*      */   public static final int GL_VIEW_CLASS_BPTC_FLOAT = 33491;
/*      */   public static final int GL_UNIFORM = 37601;
/*      */   public static final int GL_UNIFORM_BLOCK = 37602;
/*      */   public static final int GL_PROGRAM_INPUT = 37603;
/*      */   public static final int GL_PROGRAM_OUTPUT = 37604;
/*      */   public static final int GL_BUFFER_VARIABLE = 37605;
/*      */   public static final int GL_SHADER_STORAGE_BLOCK = 37606;
/*      */   public static final int GL_VERTEX_SUBROUTINE = 37608;
/*      */   public static final int GL_TESS_CONTROL_SUBROUTINE = 37609;
/*      */   public static final int GL_TESS_EVALUATION_SUBROUTINE = 37610;
/*      */   public static final int GL_GEOMETRY_SUBROUTINE = 37611;
/*      */   public static final int GL_FRAGMENT_SUBROUTINE = 37612;
/*      */   public static final int GL_COMPUTE_SUBROUTINE = 37613;
/*      */   public static final int GL_VERTEX_SUBROUTINE_UNIFORM = 37614;
/*      */   public static final int GL_TESS_CONTROL_SUBROUTINE_UNIFORM = 37615;
/*      */   public static final int GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 37616;
/*      */   public static final int GL_GEOMETRY_SUBROUTINE_UNIFORM = 37617;
/*      */   public static final int GL_FRAGMENT_SUBROUTINE_UNIFORM = 37618;
/*      */   public static final int GL_COMPUTE_SUBROUTINE_UNIFORM = 37619;
/*      */   public static final int GL_TRANSFORM_FEEDBACK_VARYING = 37620;
/*      */   public static final int GL_ACTIVE_RESOURCES = 37621;
/*      */   public static final int GL_MAX_NAME_LENGTH = 37622;
/*      */   public static final int GL_MAX_NUM_ACTIVE_VARIABLES = 37623;
/*      */   public static final int GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 37624;
/*      */   public static final int GL_NAME_LENGTH = 37625;
/*      */   public static final int GL_TYPE = 37626;
/*      */   public static final int GL_ARRAY_SIZE = 37627;
/*      */   public static final int GL_OFFSET = 37628;
/*      */   public static final int GL_BLOCK_INDEX = 37629;
/*      */   public static final int GL_ARRAY_STRIDE = 37630;
/*      */   public static final int GL_MATRIX_STRIDE = 37631;
/*      */   public static final int GL_IS_ROW_MAJOR = 37632;
/*      */   public static final int GL_ATOMIC_COUNTER_BUFFER_INDEX = 37633;
/*      */   public static final int GL_BUFFER_BINDING = 37634;
/*      */   public static final int GL_BUFFER_DATA_SIZE = 37635;
/*      */   public static final int GL_NUM_ACTIVE_VARIABLES = 37636;
/*      */   public static final int GL_ACTIVE_VARIABLES = 37637;
/*      */   public static final int GL_REFERENCED_BY_VERTEX_SHADER = 37638;
/*      */   public static final int GL_REFERENCED_BY_TESS_CONTROL_SHADER = 37639;
/*      */   public static final int GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 37640;
/*      */   public static final int GL_REFERENCED_BY_GEOMETRY_SHADER = 37641;
/*      */   public static final int GL_REFERENCED_BY_FRAGMENT_SHADER = 37642;
/*      */   public static final int GL_REFERENCED_BY_COMPUTE_SHADER = 37643;
/*      */   public static final int GL_TOP_LEVEL_ARRAY_SIZE = 37644;
/*      */   public static final int GL_TOP_LEVEL_ARRAY_STRIDE = 37645;
/*      */   public static final int GL_LOCATION = 37646;
/*      */   public static final int GL_LOCATION_INDEX = 37647;
/*      */   public static final int GL_IS_PER_PATCH = 37607;
/*      */   public static final int GL_SHADER_STORAGE_BUFFER = 37074;
/*      */   public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 37075;
/*      */   public static final int GL_SHADER_STORAGE_BUFFER_START = 37076;
/*      */   public static final int GL_SHADER_STORAGE_BUFFER_SIZE = 37077;
/*      */   public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 37078;
/*      */   public static final int GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 37079;
/*      */   public static final int GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 37080;
/*      */   public static final int GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 37081;
/*      */   public static final int GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 37082;
/*      */   public static final int GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 37083;
/*      */   public static final int GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 37084;
/*      */   public static final int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 37085;
/*      */   public static final int GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 37086;
/*      */   public static final int GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 37087;
/*      */   public static final int GL_SHADER_STORAGE_BARRIER_BIT = 8192;
/*      */   public static final int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 36665;
/*      */   public static final int GL_DEPTH_STENCIL_TEXTURE_MODE = 37098;
/*      */   public static final int GL_TEXTURE_BUFFER_OFFSET = 37277;
/*      */   public static final int GL_TEXTURE_BUFFER_SIZE = 37278;
/*      */   public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 37279;
/*      */   public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 33499;
/*      */   public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 33500;
/*      */   public static final int GL_TEXTURE_VIEW_MIN_LAYER = 33501;
/*      */   public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 33502;
/*      */   public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 33503;
/*      */   public static final int GL_VERTEX_ATTRIB_BINDING = 33492;
/*      */   public static final int GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 33493;
/*      */   public static final int GL_VERTEX_BINDING_DIVISOR = 33494;
/*      */   public static final int GL_VERTEX_BINDING_OFFSET = 33495;
/*      */   public static final int GL_VERTEX_BINDING_STRIDE = 33496;
/*      */   public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 33497;
/*      */   public static final int GL_MAX_VERTEX_ATTRIB_BINDINGS = 33498;
/*      */   
/*      */   public static void glClearBufferData(int target, int internalformat, int format, int type, ByteBuffer data) {
/*  488 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  489 */     long function_pointer = caps.glClearBufferData;
/*  490 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  491 */     BufferChecks.checkBuffer(data, 1);
/*  492 */     nglClearBufferData(target, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglClearBufferData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glClearBufferSubData(int target, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
/*  497 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  498 */     long function_pointer = caps.glClearBufferSubData;
/*  499 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  500 */     BufferChecks.checkBuffer(data, 1);
/*  501 */     nglClearBufferSubData(target, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
/*      */   }
/*      */   static native void nglClearBufferSubData(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, long paramLong3, long paramLong4);
/*      */   
/*      */   public static void glDispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
/*  506 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  507 */     long function_pointer = caps.glDispatchCompute;
/*  508 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  509 */     nglDispatchCompute(num_groups_x, num_groups_y, num_groups_z, function_pointer);
/*      */   }
/*      */   static native void nglDispatchCompute(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glDispatchComputeIndirect(long indirect) {
/*  514 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  515 */     long function_pointer = caps.glDispatchComputeIndirect;
/*  516 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  517 */     nglDispatchComputeIndirect(indirect, function_pointer);
/*      */   }
/*      */   static native void nglDispatchComputeIndirect(long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int srcWidth, int srcHeight, int srcDepth) {
/*  522 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  523 */     long function_pointer = caps.glCopyImageSubData;
/*  524 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  525 */     nglCopyImageSubData(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth, function_pointer);
/*      */   }
/*      */   static native void nglCopyImageSubData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, long paramLong);
/*      */   
/*      */   public static void glDebugMessageControl(int source, int type, int severity, IntBuffer ids, boolean enabled) {
/*  530 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  531 */     long function_pointer = caps.glDebugMessageControl;
/*  532 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  533 */     if (ids != null)
/*  534 */       BufferChecks.checkDirect(ids); 
/*  535 */     nglDebugMessageControl(source, type, severity, (ids == null) ? 0 : ids.remaining(), MemoryUtil.getAddressSafe(ids), enabled, function_pointer);
/*      */   }
/*      */   static native void nglDebugMessageControl(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, boolean paramBoolean, long paramLong2);
/*      */   
/*      */   public static void glDebugMessageInsert(int source, int type, int id, int severity, ByteBuffer buf) {
/*  540 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  541 */     long function_pointer = caps.glDebugMessageInsert;
/*  542 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  543 */     BufferChecks.checkDirect(buf);
/*  544 */     nglDebugMessageInsert(source, type, id, severity, buf.remaining(), MemoryUtil.getAddress(buf), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglDebugMessageInsert(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glDebugMessageInsert(int source, int type, int id, int severity, CharSequence buf) {
/*  550 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  551 */     long function_pointer = caps.glDebugMessageInsert;
/*  552 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  553 */     nglDebugMessageInsert(source, type, id, severity, buf.length(), APIUtil.getBuffer(caps, buf), function_pointer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void glDebugMessageCallback(KHRDebugCallback callback) {
/*  564 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  565 */     long function_pointer = caps.glDebugMessageCallback;
/*  566 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  567 */     long userParam = (callback == null) ? 0L : CallbackUtil.createGlobalRef(callback.getHandler());
/*  568 */     CallbackUtil.registerContextCallbackKHR(userParam);
/*  569 */     nglDebugMessageCallback((callback == null) ? 0L : callback.getPointer(), userParam, function_pointer);
/*      */   }
/*      */   static native void nglDebugMessageCallback(long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static int glGetDebugMessageLog(int count, IntBuffer sources, IntBuffer types, IntBuffer ids, IntBuffer severities, IntBuffer lengths, ByteBuffer messageLog) {
/*  574 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  575 */     long function_pointer = caps.glGetDebugMessageLog;
/*  576 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  577 */     if (sources != null)
/*  578 */       BufferChecks.checkBuffer(sources, count); 
/*  579 */     if (types != null)
/*  580 */       BufferChecks.checkBuffer(types, count); 
/*  581 */     if (ids != null)
/*  582 */       BufferChecks.checkBuffer(ids, count); 
/*  583 */     if (severities != null)
/*  584 */       BufferChecks.checkBuffer(severities, count); 
/*  585 */     if (lengths != null)
/*  586 */       BufferChecks.checkBuffer(lengths, count); 
/*  587 */     if (messageLog != null)
/*  588 */       BufferChecks.checkDirect(messageLog); 
/*  589 */     int __result = nglGetDebugMessageLog(count, (messageLog == null) ? 0 : messageLog.remaining(), MemoryUtil.getAddressSafe(sources), MemoryUtil.getAddressSafe(types), MemoryUtil.getAddressSafe(ids), MemoryUtil.getAddressSafe(severities), MemoryUtil.getAddressSafe(lengths), MemoryUtil.getAddressSafe(messageLog), function_pointer);
/*  590 */     return __result;
/*      */   }
/*      */   static native int nglGetDebugMessageLog(int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7);
/*      */   
/*      */   public static void glPushDebugGroup(int source, int id, ByteBuffer message) {
/*  595 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  596 */     long function_pointer = caps.glPushDebugGroup;
/*  597 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  598 */     BufferChecks.checkDirect(message);
/*  599 */     nglPushDebugGroup(source, id, message.remaining(), MemoryUtil.getAddress(message), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglPushDebugGroup(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glPushDebugGroup(int source, int id, CharSequence message) {
/*  605 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  606 */     long function_pointer = caps.glPushDebugGroup;
/*  607 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  608 */     nglPushDebugGroup(source, id, message.length(), APIUtil.getBuffer(caps, message), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glPopDebugGroup() {
/*  612 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  613 */     long function_pointer = caps.glPopDebugGroup;
/*  614 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  615 */     nglPopDebugGroup(function_pointer);
/*      */   }
/*      */   static native void nglPopDebugGroup(long paramLong);
/*      */   
/*      */   public static void glObjectLabel(int identifier, int name, ByteBuffer label) {
/*  620 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  621 */     long function_pointer = caps.glObjectLabel;
/*  622 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  623 */     if (label != null)
/*  624 */       BufferChecks.checkDirect(label); 
/*  625 */     nglObjectLabel(identifier, name, (label == null) ? 0 : label.remaining(), MemoryUtil.getAddressSafe(label), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglObjectLabel(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glObjectLabel(int identifier, int name, CharSequence label) {
/*  631 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  632 */     long function_pointer = caps.glObjectLabel;
/*  633 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  634 */     nglObjectLabel(identifier, name, label.length(), APIUtil.getBuffer(caps, label), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glGetObjectLabel(int identifier, int name, IntBuffer length, ByteBuffer label) {
/*  638 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  639 */     long function_pointer = caps.glGetObjectLabel;
/*  640 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  641 */     if (length != null)
/*  642 */       BufferChecks.checkBuffer(length, 1); 
/*  643 */     BufferChecks.checkDirect(label);
/*  644 */     nglGetObjectLabel(identifier, name, label.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(label), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetObjectLabel(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static String glGetObjectLabel(int identifier, int name, int bufSize) {
/*  650 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  651 */     long function_pointer = caps.glGetObjectLabel;
/*  652 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  653 */     IntBuffer label_length = APIUtil.getLengths(caps);
/*  654 */     ByteBuffer label = APIUtil.getBufferByte(caps, bufSize);
/*  655 */     nglGetObjectLabel(identifier, name, bufSize, MemoryUtil.getAddress0(label_length), MemoryUtil.getAddress(label), function_pointer);
/*  656 */     label.limit(label_length.get(0));
/*  657 */     return APIUtil.getString(caps, label);
/*      */   }
/*      */   
/*      */   public static void glObjectPtrLabel(PointerWrapper ptr, ByteBuffer label) {
/*  661 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  662 */     long function_pointer = caps.glObjectPtrLabel;
/*  663 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  664 */     if (label != null)
/*  665 */       BufferChecks.checkDirect(label); 
/*  666 */     nglObjectPtrLabel(ptr.getPointer(), (label == null) ? 0 : label.remaining(), MemoryUtil.getAddressSafe(label), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglObjectPtrLabel(long paramLong1, int paramInt, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glObjectPtrLabel(PointerWrapper ptr, CharSequence label) {
/*  672 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  673 */     long function_pointer = caps.glObjectPtrLabel;
/*  674 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  675 */     nglObjectPtrLabel(ptr.getPointer(), label.length(), APIUtil.getBuffer(caps, label), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glGetObjectPtrLabel(PointerWrapper ptr, IntBuffer length, ByteBuffer label) {
/*  679 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  680 */     long function_pointer = caps.glGetObjectPtrLabel;
/*  681 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  682 */     if (length != null)
/*  683 */       BufferChecks.checkBuffer(length, 1); 
/*  684 */     BufferChecks.checkDirect(label);
/*  685 */     nglGetObjectPtrLabel(ptr.getPointer(), label.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(label), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetObjectPtrLabel(long paramLong1, int paramInt, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static String glGetObjectPtrLabel(PointerWrapper ptr, int bufSize) {
/*  691 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  692 */     long function_pointer = caps.glGetObjectPtrLabel;
/*  693 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  694 */     IntBuffer label_length = APIUtil.getLengths(caps);
/*  695 */     ByteBuffer label = APIUtil.getBufferByte(caps, bufSize);
/*  696 */     nglGetObjectPtrLabel(ptr.getPointer(), bufSize, MemoryUtil.getAddress0(label_length), MemoryUtil.getAddress(label), function_pointer);
/*  697 */     label.limit(label_length.get(0));
/*  698 */     return APIUtil.getString(caps, label);
/*      */   }
/*      */   
/*      */   public static void glFramebufferParameteri(int target, int pname, int param) {
/*  702 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  703 */     long function_pointer = caps.glFramebufferParameteri;
/*  704 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  705 */     nglFramebufferParameteri(target, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglFramebufferParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glGetFramebufferParameter(int target, int pname, IntBuffer params) {
/*  710 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  711 */     long function_pointer = caps.glGetFramebufferParameteriv;
/*  712 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  713 */     BufferChecks.checkBuffer(params, 1);
/*  714 */     nglGetFramebufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetFramebufferParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetFramebufferParameteri(int target, int pname) {
/*  720 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  721 */     long function_pointer = caps.glGetFramebufferParameteriv;
/*  722 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  723 */     IntBuffer params = APIUtil.getBufferInt(caps);
/*  724 */     nglGetFramebufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*  725 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetInternalformat(int target, int internalformat, int pname, LongBuffer params) {
/*  729 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  730 */     long function_pointer = caps.glGetInternalformati64v;
/*  731 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  732 */     BufferChecks.checkDirect(params);
/*  733 */     nglGetInternalformati64v(target, internalformat, pname, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetInternalformati64v(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static long glGetInternalformati64(int target, int internalformat, int pname) {
/*  739 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  740 */     long function_pointer = caps.glGetInternalformati64v;
/*  741 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  742 */     LongBuffer params = APIUtil.getBufferLong(caps);
/*  743 */     nglGetInternalformati64v(target, internalformat, pname, 1, MemoryUtil.getAddress(params), function_pointer);
/*  744 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glInvalidateTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth) {
/*  748 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  749 */     long function_pointer = caps.glInvalidateTexSubImage;
/*  750 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  751 */     nglInvalidateTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, function_pointer);
/*      */   }
/*      */   static native void nglInvalidateTexSubImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);
/*      */   
/*      */   public static void glInvalidateTexImage(int texture, int level) {
/*  756 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  757 */     long function_pointer = caps.glInvalidateTexImage;
/*  758 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  759 */     nglInvalidateTexImage(texture, level, function_pointer);
/*      */   }
/*      */   static native void nglInvalidateTexImage(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glInvalidateBufferSubData(int buffer, long offset, long length) {
/*  764 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  765 */     long function_pointer = caps.glInvalidateBufferSubData;
/*  766 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  767 */     nglInvalidateBufferSubData(buffer, offset, length, function_pointer);
/*      */   }
/*      */   static native void nglInvalidateBufferSubData(int paramInt, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glInvalidateBufferData(int buffer) {
/*  772 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  773 */     long function_pointer = caps.glInvalidateBufferData;
/*  774 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  775 */     nglInvalidateBufferData(buffer, function_pointer);
/*      */   }
/*      */   static native void nglInvalidateBufferData(int paramInt, long paramLong);
/*      */   
/*      */   public static void glInvalidateFramebuffer(int target, IntBuffer attachments) {
/*  780 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  781 */     long function_pointer = caps.glInvalidateFramebuffer;
/*  782 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  783 */     BufferChecks.checkDirect(attachments);
/*  784 */     nglInvalidateFramebuffer(target, attachments.remaining(), MemoryUtil.getAddress(attachments), function_pointer);
/*      */   }
/*      */   static native void nglInvalidateFramebuffer(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glInvalidateSubFramebuffer(int target, IntBuffer attachments, int x, int y, int width, int height) {
/*  789 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  790 */     long function_pointer = caps.glInvalidateSubFramebuffer;
/*  791 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  792 */     BufferChecks.checkDirect(attachments);
/*  793 */     nglInvalidateSubFramebuffer(target, attachments.remaining(), MemoryUtil.getAddress(attachments), x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglInvalidateSubFramebuffer(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong2);
/*      */   
/*      */   public static void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
/*  798 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  799 */     long function_pointer = caps.glMultiDrawArraysIndirect;
/*  800 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  801 */     GLChecks.ensureIndirectBOdisabled(caps);
/*  802 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 16 : stride) * primcount);
/*  803 */     nglMultiDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
/*      */   }
/*      */   
/*      */   public static void glMultiDrawArraysIndirect(int mode, long indirect_buffer_offset, int primcount, int stride) {
/*  807 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  808 */     long function_pointer = caps.glMultiDrawArraysIndirect;
/*  809 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  810 */     GLChecks.ensureIndirectBOenabled(caps);
/*  811 */     nglMultiDrawArraysIndirectBO(mode, indirect_buffer_offset, primcount, stride, function_pointer);
/*      */   }
/*      */   static native void nglMultiDrawArraysIndirect(int paramInt1, long paramLong1, int paramInt2, int paramInt3, long paramLong2);
/*      */   static native void nglMultiDrawArraysIndirectBO(int paramInt1, long paramLong1, int paramInt2, int paramInt3, long paramLong2);
/*      */   
/*      */   public static void glMultiDrawArraysIndirect(int mode, IntBuffer indirect, int primcount, int stride) {
/*  817 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  818 */     long function_pointer = caps.glMultiDrawArraysIndirect;
/*  819 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  820 */     GLChecks.ensureIndirectBOdisabled(caps);
/*  821 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 4 : (stride >> 2)) * primcount);
/*  822 */     nglMultiDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
/*      */   }
/*      */   
/*      */   public static void glMultiDrawElementsIndirect(int mode, int type, ByteBuffer indirect, int primcount, int stride) {
/*  826 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  827 */     long function_pointer = caps.glMultiDrawElementsIndirect;
/*  828 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  829 */     GLChecks.ensureIndirectBOdisabled(caps);
/*  830 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 20 : stride) * primcount);
/*  831 */     nglMultiDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
/*      */   }
/*      */   static native void nglMultiDrawElementsIndirect(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, long paramLong2);
/*      */   public static void glMultiDrawElementsIndirect(int mode, int type, long indirect_buffer_offset, int primcount, int stride) {
/*  835 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  836 */     long function_pointer = caps.glMultiDrawElementsIndirect;
/*  837 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  838 */     GLChecks.ensureIndirectBOenabled(caps);
/*  839 */     nglMultiDrawElementsIndirectBO(mode, type, indirect_buffer_offset, primcount, stride, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglMultiDrawElementsIndirectBO(int paramInt1, int paramInt2, long paramLong1, int paramInt3, int paramInt4, long paramLong2);
/*      */   
/*      */   public static void glMultiDrawElementsIndirect(int mode, int type, IntBuffer indirect, int primcount, int stride) {
/*  845 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  846 */     long function_pointer = caps.glMultiDrawElementsIndirect;
/*  847 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  848 */     GLChecks.ensureIndirectBOdisabled(caps);
/*  849 */     BufferChecks.checkBuffer(indirect, ((stride == 0) ? 5 : (stride >> 2)) * primcount);
/*  850 */     nglMultiDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), primcount, stride, function_pointer);
/*      */   }
/*      */   
/*      */   public static void glGetProgramInterface(int program, int programInterface, int pname, IntBuffer params) {
/*  854 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  855 */     long function_pointer = caps.glGetProgramInterfaceiv;
/*  856 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  857 */     BufferChecks.checkBuffer(params, 1);
/*  858 */     nglGetProgramInterfaceiv(program, programInterface, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetProgramInterfaceiv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetProgramInterfacei(int program, int programInterface, int pname) {
/*  864 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  865 */     long function_pointer = caps.glGetProgramInterfaceiv;
/*  866 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  867 */     IntBuffer params = APIUtil.getBufferInt(caps);
/*  868 */     nglGetProgramInterfaceiv(program, programInterface, pname, MemoryUtil.getAddress(params), function_pointer);
/*  869 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static int glGetProgramResourceIndex(int program, int programInterface, ByteBuffer name) {
/*  873 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  874 */     long function_pointer = caps.glGetProgramResourceIndex;
/*  875 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  876 */     BufferChecks.checkDirect(name);
/*  877 */     BufferChecks.checkNullTerminated(name);
/*  878 */     int __result = nglGetProgramResourceIndex(program, programInterface, MemoryUtil.getAddress(name), function_pointer);
/*  879 */     return __result;
/*      */   }
/*      */   
/*      */   static native int nglGetProgramResourceIndex(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetProgramResourceIndex(int program, int programInterface, CharSequence name) {
/*  885 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  886 */     long function_pointer = caps.glGetProgramResourceIndex;
/*  887 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  888 */     int __result = nglGetProgramResourceIndex(program, programInterface, APIUtil.getBufferNT(caps, name), function_pointer);
/*  889 */     return __result;
/*      */   }
/*      */   
/*      */   public static void glGetProgramResourceName(int program, int programInterface, int index, IntBuffer length, ByteBuffer name) {
/*  893 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  894 */     long function_pointer = caps.glGetProgramResourceName;
/*  895 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  896 */     if (length != null)
/*  897 */       BufferChecks.checkBuffer(length, 1); 
/*  898 */     if (name != null)
/*  899 */       BufferChecks.checkDirect(name); 
/*  900 */     nglGetProgramResourceName(program, programInterface, index, (name == null) ? 0 : name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddressSafe(name), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetProgramResourceName(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static String glGetProgramResourceName(int program, int programInterface, int index, int bufSize) {
/*  906 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  907 */     long function_pointer = caps.glGetProgramResourceName;
/*  908 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  909 */     IntBuffer name_length = APIUtil.getLengths(caps);
/*  910 */     ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
/*  911 */     nglGetProgramResourceName(program, programInterface, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
/*  912 */     name.limit(name_length.get(0));
/*  913 */     return APIUtil.getString(caps, name);
/*      */   }
/*      */   
/*      */   public static void glGetProgramResource(int program, int programInterface, int index, IntBuffer props, IntBuffer length, IntBuffer params) {
/*  917 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  918 */     long function_pointer = caps.glGetProgramResourceiv;
/*  919 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  920 */     BufferChecks.checkDirect(props);
/*  921 */     if (length != null)
/*  922 */       BufferChecks.checkBuffer(length, 1); 
/*  923 */     BufferChecks.checkDirect(params);
/*  924 */     nglGetProgramResourceiv(program, programInterface, index, props.remaining(), MemoryUtil.getAddress(props), params.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetProgramResourceiv(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, int paramInt5, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static int glGetProgramResourceLocation(int program, int programInterface, ByteBuffer name) {
/*  929 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  930 */     long function_pointer = caps.glGetProgramResourceLocation;
/*  931 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  932 */     BufferChecks.checkDirect(name);
/*  933 */     BufferChecks.checkNullTerminated(name);
/*  934 */     int __result = nglGetProgramResourceLocation(program, programInterface, MemoryUtil.getAddress(name), function_pointer);
/*  935 */     return __result;
/*      */   }
/*      */   
/*      */   static native int nglGetProgramResourceLocation(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetProgramResourceLocation(int program, int programInterface, CharSequence name) {
/*  941 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  942 */     long function_pointer = caps.glGetProgramResourceLocation;
/*  943 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  944 */     int __result = nglGetProgramResourceLocation(program, programInterface, APIUtil.getBufferNT(caps, name), function_pointer);
/*  945 */     return __result;
/*      */   }
/*      */   
/*      */   public static int glGetProgramResourceLocationIndex(int program, int programInterface, ByteBuffer name) {
/*  949 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  950 */     long function_pointer = caps.glGetProgramResourceLocationIndex;
/*  951 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  952 */     BufferChecks.checkDirect(name);
/*  953 */     BufferChecks.checkNullTerminated(name);
/*  954 */     int __result = nglGetProgramResourceLocationIndex(program, programInterface, MemoryUtil.getAddress(name), function_pointer);
/*  955 */     return __result;
/*      */   }
/*      */   
/*      */   static native int nglGetProgramResourceLocationIndex(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetProgramResourceLocationIndex(int program, int programInterface, CharSequence name) {
/*  961 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  962 */     long function_pointer = caps.glGetProgramResourceLocationIndex;
/*  963 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  964 */     int __result = nglGetProgramResourceLocationIndex(program, programInterface, APIUtil.getBufferNT(caps, name), function_pointer);
/*  965 */     return __result;
/*      */   }
/*      */   
/*      */   public static void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
/*  969 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  970 */     long function_pointer = caps.glShaderStorageBlockBinding;
/*  971 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  972 */     nglShaderStorageBlockBinding(program, storageBlockIndex, storageBlockBinding, function_pointer);
/*      */   }
/*      */   static native void nglShaderStorageBlockBinding(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTexBufferRange(int target, int internalformat, int buffer, long offset, long size) {
/*  977 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  978 */     long function_pointer = caps.glTexBufferRange;
/*  979 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  980 */     nglTexBufferRange(target, internalformat, buffer, offset, size, function_pointer);
/*      */   }
/*      */   static native void nglTexBufferRange(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glTexStorage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
/*  985 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  986 */     long function_pointer = caps.glTexStorage2DMultisample;
/*  987 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  988 */     nglTexStorage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
/*      */   }
/*      */   static native void nglTexStorage2DMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glTexStorage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
/*  993 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  994 */     long function_pointer = caps.glTexStorage3DMultisample;
/*  995 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  996 */     nglTexStorage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
/*      */   }
/*      */   static native void nglTexStorage3DMultisample(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glTextureView(int texture, int target, int origtexture, int internalformat, int minlevel, int numlevels, int minlayer, int numlayers) {
/* 1001 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1002 */     long function_pointer = caps.glTextureView;
/* 1003 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1004 */     nglTextureView(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers, function_pointer);
/*      */   }
/*      */   static native void nglTextureView(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);
/*      */   
/*      */   public static void glBindVertexBuffer(int bindingindex, int buffer, long offset, int stride) {
/* 1009 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1010 */     long function_pointer = caps.glBindVertexBuffer;
/* 1011 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1012 */     nglBindVertexBuffer(bindingindex, buffer, offset, stride, function_pointer);
/*      */   }
/*      */   static native void nglBindVertexBuffer(int paramInt1, int paramInt2, long paramLong1, int paramInt3, long paramLong2);
/*      */   
/*      */   public static void glVertexAttribFormat(int attribindex, int size, int type, boolean normalized, int relativeoffset) {
/* 1017 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1018 */     long function_pointer = caps.glVertexAttribFormat;
/* 1019 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1020 */     nglVertexAttribFormat(attribindex, size, type, normalized, relativeoffset, function_pointer);
/*      */   }
/*      */   static native void nglVertexAttribFormat(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glVertexAttribIFormat(int attribindex, int size, int type, int relativeoffset) {
/* 1025 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1026 */     long function_pointer = caps.glVertexAttribIFormat;
/* 1027 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1028 */     nglVertexAttribIFormat(attribindex, size, type, relativeoffset, function_pointer);
/*      */   }
/*      */   static native void nglVertexAttribIFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glVertexAttribLFormat(int attribindex, int size, int type, int relativeoffset) {
/* 1033 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1034 */     long function_pointer = caps.glVertexAttribLFormat;
/* 1035 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1036 */     nglVertexAttribLFormat(attribindex, size, type, relativeoffset, function_pointer);
/*      */   }
/*      */   static native void nglVertexAttribLFormat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glVertexAttribBinding(int attribindex, int bindingindex) {
/* 1041 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1042 */     long function_pointer = caps.glVertexAttribBinding;
/* 1043 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1044 */     nglVertexAttribBinding(attribindex, bindingindex, function_pointer);
/*      */   }
/*      */   static native void nglVertexAttribBinding(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glVertexBindingDivisor(int bindingindex, int divisor) {
/* 1049 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1050 */     long function_pointer = caps.glVertexBindingDivisor;
/* 1051 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1052 */     nglVertexBindingDivisor(bindingindex, divisor, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglVertexBindingDivisor(int paramInt1, int paramInt2, long paramLong);
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL43.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */