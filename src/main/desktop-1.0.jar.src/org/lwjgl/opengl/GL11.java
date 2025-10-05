/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.DoubleBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.nio.ShortBuffer;
/*      */ import org.lwjgl.BufferChecks;
/*      */ import org.lwjgl.LWJGLUtil;
/*      */ import org.lwjgl.MemoryUtil;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class GL11
/*      */ {
/*      */   public static final int GL_ACCUM = 256;
/*      */   public static final int GL_LOAD = 257;
/*      */   public static final int GL_RETURN = 258;
/*      */   public static final int GL_MULT = 259;
/*      */   public static final int GL_ADD = 260;
/*      */   public static final int GL_NEVER = 512;
/*      */   public static final int GL_LESS = 513;
/*      */   public static final int GL_EQUAL = 514;
/*      */   public static final int GL_LEQUAL = 515;
/*      */   public static final int GL_GREATER = 516;
/*      */   public static final int GL_NOTEQUAL = 517;
/*      */   public static final int GL_GEQUAL = 518;
/*      */   public static final int GL_ALWAYS = 519;
/*      */   public static final int GL_CURRENT_BIT = 1;
/*      */   public static final int GL_POINT_BIT = 2;
/*      */   public static final int GL_LINE_BIT = 4;
/*      */   public static final int GL_POLYGON_BIT = 8;
/*      */   public static final int GL_POLYGON_STIPPLE_BIT = 16;
/*      */   public static final int GL_PIXEL_MODE_BIT = 32;
/*      */   public static final int GL_LIGHTING_BIT = 64;
/*      */   public static final int GL_FOG_BIT = 128;
/*      */   public static final int GL_DEPTH_BUFFER_BIT = 256;
/*      */   public static final int GL_ACCUM_BUFFER_BIT = 512;
/*      */   public static final int GL_STENCIL_BUFFER_BIT = 1024;
/*      */   public static final int GL_VIEWPORT_BIT = 2048;
/*      */   public static final int GL_TRANSFORM_BIT = 4096;
/*      */   public static final int GL_ENABLE_BIT = 8192;
/*      */   public static final int GL_COLOR_BUFFER_BIT = 16384;
/*      */   public static final int GL_HINT_BIT = 32768;
/*      */   public static final int GL_EVAL_BIT = 65536;
/*      */   public static final int GL_LIST_BIT = 131072;
/*      */   public static final int GL_TEXTURE_BIT = 262144;
/*      */   public static final int GL_SCISSOR_BIT = 524288;
/*      */   public static final int GL_ALL_ATTRIB_BITS = 1048575;
/*      */   public static final int GL_POINTS = 0;
/*      */   public static final int GL_LINES = 1;
/*      */   public static final int GL_LINE_LOOP = 2;
/*      */   public static final int GL_LINE_STRIP = 3;
/*      */   public static final int GL_TRIANGLES = 4;
/*      */   public static final int GL_TRIANGLE_STRIP = 5;
/*      */   public static final int GL_TRIANGLE_FAN = 6;
/*      */   public static final int GL_QUADS = 7;
/*      */   public static final int GL_QUAD_STRIP = 8;
/*      */   public static final int GL_POLYGON = 9;
/*      */   public static final int GL_ZERO = 0;
/*      */   public static final int GL_ONE = 1;
/*      */   public static final int GL_SRC_COLOR = 768;
/*      */   public static final int GL_ONE_MINUS_SRC_COLOR = 769;
/*      */   public static final int GL_SRC_ALPHA = 770;
/*      */   public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
/*      */   public static final int GL_DST_ALPHA = 772;
/*      */   public static final int GL_ONE_MINUS_DST_ALPHA = 773;
/*      */   public static final int GL_DST_COLOR = 774;
/*      */   public static final int GL_ONE_MINUS_DST_COLOR = 775;
/*      */   public static final int GL_SRC_ALPHA_SATURATE = 776;
/*      */   public static final int GL_CONSTANT_COLOR = 32769;
/*      */   public static final int GL_ONE_MINUS_CONSTANT_COLOR = 32770;
/*      */   public static final int GL_CONSTANT_ALPHA = 32771;
/*      */   public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 32772;
/*      */   public static final int GL_TRUE = 1;
/*      */   public static final int GL_FALSE = 0;
/*      */   public static final int GL_CLIP_PLANE0 = 12288;
/*      */   public static final int GL_CLIP_PLANE1 = 12289;
/*      */   public static final int GL_CLIP_PLANE2 = 12290;
/*      */   public static final int GL_CLIP_PLANE3 = 12291;
/*      */   public static final int GL_CLIP_PLANE4 = 12292;
/*      */   public static final int GL_CLIP_PLANE5 = 12293;
/*      */   public static final int GL_BYTE = 5120;
/*      */   public static final int GL_UNSIGNED_BYTE = 5121;
/*      */   public static final int GL_SHORT = 5122;
/*      */   public static final int GL_UNSIGNED_SHORT = 5123;
/*      */   public static final int GL_INT = 5124;
/*      */   public static final int GL_UNSIGNED_INT = 5125;
/*      */   public static final int GL_FLOAT = 5126;
/*      */   public static final int GL_2_BYTES = 5127;
/*      */   public static final int GL_3_BYTES = 5128;
/*      */   public static final int GL_4_BYTES = 5129;
/*      */   public static final int GL_DOUBLE = 5130;
/*      */   public static final int GL_NONE = 0;
/*      */   public static final int GL_FRONT_LEFT = 1024;
/*      */   public static final int GL_FRONT_RIGHT = 1025;
/*      */   public static final int GL_BACK_LEFT = 1026;
/*      */   public static final int GL_BACK_RIGHT = 1027;
/*      */   public static final int GL_FRONT = 1028;
/*      */   public static final int GL_BACK = 1029;
/*      */   public static final int GL_LEFT = 1030;
/*      */   public static final int GL_RIGHT = 1031;
/*      */   public static final int GL_FRONT_AND_BACK = 1032;
/*      */   public static final int GL_AUX0 = 1033;
/*      */   public static final int GL_AUX1 = 1034;
/*      */   public static final int GL_AUX2 = 1035;
/*      */   public static final int GL_AUX3 = 1036;
/*      */   public static final int GL_NO_ERROR = 0;
/*      */   public static final int GL_INVALID_ENUM = 1280;
/*      */   public static final int GL_INVALID_VALUE = 1281;
/*      */   public static final int GL_INVALID_OPERATION = 1282;
/*      */   public static final int GL_STACK_OVERFLOW = 1283;
/*      */   public static final int GL_STACK_UNDERFLOW = 1284;
/*      */   public static final int GL_OUT_OF_MEMORY = 1285;
/*      */   public static final int GL_2D = 1536;
/*      */   public static final int GL_3D = 1537;
/*      */   public static final int GL_3D_COLOR = 1538;
/*      */   public static final int GL_3D_COLOR_TEXTURE = 1539;
/*      */   public static final int GL_4D_COLOR_TEXTURE = 1540;
/*      */   public static final int GL_PASS_THROUGH_TOKEN = 1792;
/*      */   public static final int GL_POINT_TOKEN = 1793;
/*      */   public static final int GL_LINE_TOKEN = 1794;
/*      */   public static final int GL_POLYGON_TOKEN = 1795;
/*      */   public static final int GL_BITMAP_TOKEN = 1796;
/*      */   public static final int GL_DRAW_PIXEL_TOKEN = 1797;
/*      */   public static final int GL_COPY_PIXEL_TOKEN = 1798;
/*      */   public static final int GL_LINE_RESET_TOKEN = 1799;
/*      */   public static final int GL_EXP = 2048;
/*      */   public static final int GL_EXP2 = 2049;
/*      */   public static final int GL_CW = 2304;
/*      */   public static final int GL_CCW = 2305;
/*      */   public static final int GL_COEFF = 2560;
/*      */   public static final int GL_ORDER = 2561;
/*      */   public static final int GL_DOMAIN = 2562;
/*      */   public static final int GL_CURRENT_COLOR = 2816;
/*      */   public static final int GL_CURRENT_INDEX = 2817;
/*      */   public static final int GL_CURRENT_NORMAL = 2818;
/*      */   public static final int GL_CURRENT_TEXTURE_COORDS = 2819;
/*      */   public static final int GL_CURRENT_RASTER_COLOR = 2820;
/*      */   public static final int GL_CURRENT_RASTER_INDEX = 2821;
/*      */   public static final int GL_CURRENT_RASTER_TEXTURE_COORDS = 2822;
/*      */   public static final int GL_CURRENT_RASTER_POSITION = 2823;
/*      */   public static final int GL_CURRENT_RASTER_POSITION_VALID = 2824;
/*      */   public static final int GL_CURRENT_RASTER_DISTANCE = 2825;
/*      */   public static final int GL_POINT_SMOOTH = 2832;
/*      */   public static final int GL_POINT_SIZE = 2833;
/*      */   public static final int GL_POINT_SIZE_RANGE = 2834;
/*      */   public static final int GL_POINT_SIZE_GRANULARITY = 2835;
/*      */   public static final int GL_LINE_SMOOTH = 2848;
/*      */   public static final int GL_LINE_WIDTH = 2849;
/*      */   public static final int GL_LINE_WIDTH_RANGE = 2850;
/*      */   public static final int GL_LINE_WIDTH_GRANULARITY = 2851;
/*      */   public static final int GL_LINE_STIPPLE = 2852;
/*      */   public static final int GL_LINE_STIPPLE_PATTERN = 2853;
/*      */   public static final int GL_LINE_STIPPLE_REPEAT = 2854;
/*      */   public static final int GL_LIST_MODE = 2864;
/*      */   public static final int GL_MAX_LIST_NESTING = 2865;
/*      */   public static final int GL_LIST_BASE = 2866;
/*      */   public static final int GL_LIST_INDEX = 2867;
/*      */   public static final int GL_POLYGON_MODE = 2880;
/*      */   public static final int GL_POLYGON_SMOOTH = 2881;
/*      */   public static final int GL_POLYGON_STIPPLE = 2882;
/*      */   public static final int GL_EDGE_FLAG = 2883;
/*      */   public static final int GL_CULL_FACE = 2884;
/*      */   public static final int GL_CULL_FACE_MODE = 2885;
/*      */   public static final int GL_FRONT_FACE = 2886;
/*      */   public static final int GL_LIGHTING = 2896;
/*      */   public static final int GL_LIGHT_MODEL_LOCAL_VIEWER = 2897;
/*      */   public static final int GL_LIGHT_MODEL_TWO_SIDE = 2898;
/*      */   public static final int GL_LIGHT_MODEL_AMBIENT = 2899;
/*      */   public static final int GL_SHADE_MODEL = 2900;
/*      */   public static final int GL_COLOR_MATERIAL_FACE = 2901;
/*      */   public static final int GL_COLOR_MATERIAL_PARAMETER = 2902;
/*      */   public static final int GL_COLOR_MATERIAL = 2903;
/*      */   public static final int GL_FOG = 2912;
/*      */   public static final int GL_FOG_INDEX = 2913;
/*      */   public static final int GL_FOG_DENSITY = 2914;
/*      */   public static final int GL_FOG_START = 2915;
/*      */   public static final int GL_FOG_END = 2916;
/*      */   public static final int GL_FOG_MODE = 2917;
/*      */   public static final int GL_FOG_COLOR = 2918;
/*      */   public static final int GL_DEPTH_RANGE = 2928;
/*      */   public static final int GL_DEPTH_TEST = 2929;
/*      */   public static final int GL_DEPTH_WRITEMASK = 2930;
/*      */   public static final int GL_DEPTH_CLEAR_VALUE = 2931;
/*      */   public static final int GL_DEPTH_FUNC = 2932;
/*      */   public static final int GL_ACCUM_CLEAR_VALUE = 2944;
/*      */   public static final int GL_STENCIL_TEST = 2960;
/*      */   public static final int GL_STENCIL_CLEAR_VALUE = 2961;
/*      */   public static final int GL_STENCIL_FUNC = 2962;
/*      */   public static final int GL_STENCIL_VALUE_MASK = 2963;
/*      */   public static final int GL_STENCIL_FAIL = 2964;
/*      */   public static final int GL_STENCIL_PASS_DEPTH_FAIL = 2965;
/*      */   public static final int GL_STENCIL_PASS_DEPTH_PASS = 2966;
/*      */   public static final int GL_STENCIL_REF = 2967;
/*      */   public static final int GL_STENCIL_WRITEMASK = 2968;
/*      */   public static final int GL_MATRIX_MODE = 2976;
/*      */   public static final int GL_NORMALIZE = 2977;
/*      */   public static final int GL_VIEWPORT = 2978;
/*      */   public static final int GL_MODELVIEW_STACK_DEPTH = 2979;
/*      */   public static final int GL_PROJECTION_STACK_DEPTH = 2980;
/*      */   public static final int GL_TEXTURE_STACK_DEPTH = 2981;
/*      */   public static final int GL_MODELVIEW_MATRIX = 2982;
/*      */   public static final int GL_PROJECTION_MATRIX = 2983;
/*      */   public static final int GL_TEXTURE_MATRIX = 2984;
/*      */   public static final int GL_ATTRIB_STACK_DEPTH = 2992;
/*      */   public static final int GL_CLIENT_ATTRIB_STACK_DEPTH = 2993;
/*      */   public static final int GL_ALPHA_TEST = 3008;
/*      */   public static final int GL_ALPHA_TEST_FUNC = 3009;
/*      */   public static final int GL_ALPHA_TEST_REF = 3010;
/*      */   public static final int GL_DITHER = 3024;
/*      */   public static final int GL_BLEND_DST = 3040;
/*      */   public static final int GL_BLEND_SRC = 3041;
/*      */   public static final int GL_BLEND = 3042;
/*      */   public static final int GL_LOGIC_OP_MODE = 3056;
/*      */   public static final int GL_INDEX_LOGIC_OP = 3057;
/*      */   public static final int GL_COLOR_LOGIC_OP = 3058;
/*      */   public static final int GL_AUX_BUFFERS = 3072;
/*      */   public static final int GL_DRAW_BUFFER = 3073;
/*      */   public static final int GL_READ_BUFFER = 3074;
/*      */   public static final int GL_SCISSOR_BOX = 3088;
/*      */   public static final int GL_SCISSOR_TEST = 3089;
/*      */   public static final int GL_INDEX_CLEAR_VALUE = 3104;
/*      */   public static final int GL_INDEX_WRITEMASK = 3105;
/*      */   public static final int GL_COLOR_CLEAR_VALUE = 3106;
/*      */   public static final int GL_COLOR_WRITEMASK = 3107;
/*      */   public static final int GL_INDEX_MODE = 3120;
/*      */   public static final int GL_RGBA_MODE = 3121;
/*      */   public static final int GL_DOUBLEBUFFER = 3122;
/*      */   public static final int GL_STEREO = 3123;
/*      */   public static final int GL_RENDER_MODE = 3136;
/*      */   public static final int GL_PERSPECTIVE_CORRECTION_HINT = 3152;
/*      */   public static final int GL_POINT_SMOOTH_HINT = 3153;
/*      */   public static final int GL_LINE_SMOOTH_HINT = 3154;
/*      */   public static final int GL_POLYGON_SMOOTH_HINT = 3155;
/*      */   public static final int GL_FOG_HINT = 3156;
/*      */   public static final int GL_TEXTURE_GEN_S = 3168;
/*      */   public static final int GL_TEXTURE_GEN_T = 3169;
/*      */   public static final int GL_TEXTURE_GEN_R = 3170;
/*      */   public static final int GL_TEXTURE_GEN_Q = 3171;
/*      */   public static final int GL_PIXEL_MAP_I_TO_I = 3184;
/*      */   public static final int GL_PIXEL_MAP_S_TO_S = 3185;
/*      */   public static final int GL_PIXEL_MAP_I_TO_R = 3186;
/*      */   public static final int GL_PIXEL_MAP_I_TO_G = 3187;
/*      */   public static final int GL_PIXEL_MAP_I_TO_B = 3188;
/*      */   public static final int GL_PIXEL_MAP_I_TO_A = 3189;
/*      */   public static final int GL_PIXEL_MAP_R_TO_R = 3190;
/*      */   public static final int GL_PIXEL_MAP_G_TO_G = 3191;
/*      */   public static final int GL_PIXEL_MAP_B_TO_B = 3192;
/*      */   public static final int GL_PIXEL_MAP_A_TO_A = 3193;
/*      */   public static final int GL_PIXEL_MAP_I_TO_I_SIZE = 3248;
/*      */   public static final int GL_PIXEL_MAP_S_TO_S_SIZE = 3249;
/*      */   public static final int GL_PIXEL_MAP_I_TO_R_SIZE = 3250;
/*      */   public static final int GL_PIXEL_MAP_I_TO_G_SIZE = 3251;
/*      */   public static final int GL_PIXEL_MAP_I_TO_B_SIZE = 3252;
/*      */   public static final int GL_PIXEL_MAP_I_TO_A_SIZE = 3253;
/*      */   public static final int GL_PIXEL_MAP_R_TO_R_SIZE = 3254;
/*      */   public static final int GL_PIXEL_MAP_G_TO_G_SIZE = 3255;
/*      */   public static final int GL_PIXEL_MAP_B_TO_B_SIZE = 3256;
/*      */   public static final int GL_PIXEL_MAP_A_TO_A_SIZE = 3257;
/*      */   public static final int GL_UNPACK_SWAP_BYTES = 3312;
/*      */   public static final int GL_UNPACK_LSB_FIRST = 3313;
/*      */   public static final int GL_UNPACK_ROW_LENGTH = 3314;
/*      */   public static final int GL_UNPACK_SKIP_ROWS = 3315;
/*      */   public static final int GL_UNPACK_SKIP_PIXELS = 3316;
/*      */   public static final int GL_UNPACK_ALIGNMENT = 3317;
/*      */   public static final int GL_PACK_SWAP_BYTES = 3328;
/*      */   public static final int GL_PACK_LSB_FIRST = 3329;
/*      */   public static final int GL_PACK_ROW_LENGTH = 3330;
/*      */   public static final int GL_PACK_SKIP_ROWS = 3331;
/*      */   public static final int GL_PACK_SKIP_PIXELS = 3332;
/*      */   public static final int GL_PACK_ALIGNMENT = 3333;
/*      */   public static final int GL_MAP_COLOR = 3344;
/*      */   public static final int GL_MAP_STENCIL = 3345;
/*      */   public static final int GL_INDEX_SHIFT = 3346;
/*      */   public static final int GL_INDEX_OFFSET = 3347;
/*      */   public static final int GL_RED_SCALE = 3348;
/*      */   public static final int GL_RED_BIAS = 3349;
/*      */   public static final int GL_ZOOM_X = 3350;
/*      */   public static final int GL_ZOOM_Y = 3351;
/*      */   public static final int GL_GREEN_SCALE = 3352;
/*      */   public static final int GL_GREEN_BIAS = 3353;
/*      */   public static final int GL_BLUE_SCALE = 3354;
/*      */   public static final int GL_BLUE_BIAS = 3355;
/*      */   public static final int GL_ALPHA_SCALE = 3356;
/*      */   public static final int GL_ALPHA_BIAS = 3357;
/*      */   public static final int GL_DEPTH_SCALE = 3358;
/*      */   public static final int GL_DEPTH_BIAS = 3359;
/*      */   public static final int GL_MAX_EVAL_ORDER = 3376;
/*      */   public static final int GL_MAX_LIGHTS = 3377;
/*      */   public static final int GL_MAX_CLIP_PLANES = 3378;
/*      */   public static final int GL_MAX_TEXTURE_SIZE = 3379;
/*      */   public static final int GL_MAX_PIXEL_MAP_TABLE = 3380;
/*      */   public static final int GL_MAX_ATTRIB_STACK_DEPTH = 3381;
/*      */   public static final int GL_MAX_MODELVIEW_STACK_DEPTH = 3382;
/*      */   public static final int GL_MAX_NAME_STACK_DEPTH = 3383;
/*      */   public static final int GL_MAX_PROJECTION_STACK_DEPTH = 3384;
/*      */   public static final int GL_MAX_TEXTURE_STACK_DEPTH = 3385;
/*      */   public static final int GL_MAX_VIEWPORT_DIMS = 3386;
/*      */   public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 3387;
/*      */   public static final int GL_SUBPIXEL_BITS = 3408;
/*      */   public static final int GL_INDEX_BITS = 3409;
/*      */   public static final int GL_RED_BITS = 3410;
/*      */   public static final int GL_GREEN_BITS = 3411;
/*      */   public static final int GL_BLUE_BITS = 3412;
/*      */   public static final int GL_ALPHA_BITS = 3413;
/*      */   public static final int GL_DEPTH_BITS = 3414;
/*      */   public static final int GL_STENCIL_BITS = 3415;
/*      */   public static final int GL_ACCUM_RED_BITS = 3416;
/*      */   public static final int GL_ACCUM_GREEN_BITS = 3417;
/*      */   public static final int GL_ACCUM_BLUE_BITS = 3418;
/*      */   public static final int GL_ACCUM_ALPHA_BITS = 3419;
/*      */   public static final int GL_NAME_STACK_DEPTH = 3440;
/*      */   public static final int GL_AUTO_NORMAL = 3456;
/*      */   public static final int GL_MAP1_COLOR_4 = 3472;
/*      */   public static final int GL_MAP1_INDEX = 3473;
/*      */   public static final int GL_MAP1_NORMAL = 3474;
/*      */   public static final int GL_MAP1_TEXTURE_COORD_1 = 3475;
/*      */   public static final int GL_MAP1_TEXTURE_COORD_2 = 3476;
/*      */   public static final int GL_MAP1_TEXTURE_COORD_3 = 3477;
/*      */   public static final int GL_MAP1_TEXTURE_COORD_4 = 3478;
/*      */   public static final int GL_MAP1_VERTEX_3 = 3479;
/*      */   public static final int GL_MAP1_VERTEX_4 = 3480;
/*      */   public static final int GL_MAP2_COLOR_4 = 3504;
/*      */   public static final int GL_MAP2_INDEX = 3505;
/*      */   public static final int GL_MAP2_NORMAL = 3506;
/*      */   public static final int GL_MAP2_TEXTURE_COORD_1 = 3507;
/*      */   public static final int GL_MAP2_TEXTURE_COORD_2 = 3508;
/*      */   public static final int GL_MAP2_TEXTURE_COORD_3 = 3509;
/*      */   public static final int GL_MAP2_TEXTURE_COORD_4 = 3510;
/*      */   public static final int GL_MAP2_VERTEX_3 = 3511;
/*      */   public static final int GL_MAP2_VERTEX_4 = 3512;
/*      */   public static final int GL_MAP1_GRID_DOMAIN = 3536;
/*      */   public static final int GL_MAP1_GRID_SEGMENTS = 3537;
/*      */   public static final int GL_MAP2_GRID_DOMAIN = 3538;
/*      */   public static final int GL_MAP2_GRID_SEGMENTS = 3539;
/*      */   public static final int GL_TEXTURE_1D = 3552;
/*      */   public static final int GL_TEXTURE_2D = 3553;
/*      */   public static final int GL_FEEDBACK_BUFFER_POINTER = 3568;
/*      */   public static final int GL_FEEDBACK_BUFFER_SIZE = 3569;
/*      */   public static final int GL_FEEDBACK_BUFFER_TYPE = 3570;
/*      */   public static final int GL_SELECTION_BUFFER_POINTER = 3571;
/*      */   public static final int GL_SELECTION_BUFFER_SIZE = 3572;
/*      */   public static final int GL_TEXTURE_WIDTH = 4096;
/*      */   public static final int GL_TEXTURE_HEIGHT = 4097;
/*      */   public static final int GL_TEXTURE_INTERNAL_FORMAT = 4099;
/*      */   public static final int GL_TEXTURE_BORDER_COLOR = 4100;
/*      */   public static final int GL_TEXTURE_BORDER = 4101;
/*      */   public static final int GL_DONT_CARE = 4352;
/*      */   public static final int GL_FASTEST = 4353;
/*      */   public static final int GL_NICEST = 4354;
/*      */   public static final int GL_LIGHT0 = 16384;
/*      */   public static final int GL_LIGHT1 = 16385;
/*      */   public static final int GL_LIGHT2 = 16386;
/*      */   public static final int GL_LIGHT3 = 16387;
/*      */   public static final int GL_LIGHT4 = 16388;
/*      */   public static final int GL_LIGHT5 = 16389;
/*      */   public static final int GL_LIGHT6 = 16390;
/*      */   public static final int GL_LIGHT7 = 16391;
/*      */   public static final int GL_AMBIENT = 4608;
/*      */   public static final int GL_DIFFUSE = 4609;
/*      */   public static final int GL_SPECULAR = 4610;
/*      */   public static final int GL_POSITION = 4611;
/*      */   public static final int GL_SPOT_DIRECTION = 4612;
/*      */   public static final int GL_SPOT_EXPONENT = 4613;
/*      */   public static final int GL_SPOT_CUTOFF = 4614;
/*      */   public static final int GL_CONSTANT_ATTENUATION = 4615;
/*      */   public static final int GL_LINEAR_ATTENUATION = 4616;
/*      */   public static final int GL_QUADRATIC_ATTENUATION = 4617;
/*      */   public static final int GL_COMPILE = 4864;
/*      */   public static final int GL_COMPILE_AND_EXECUTE = 4865;
/*      */   public static final int GL_CLEAR = 5376;
/*      */   public static final int GL_AND = 5377;
/*      */   public static final int GL_AND_REVERSE = 5378;
/*      */   public static final int GL_COPY = 5379;
/*      */   public static final int GL_AND_INVERTED = 5380;
/*      */   public static final int GL_NOOP = 5381;
/*      */   public static final int GL_XOR = 5382;
/*      */   public static final int GL_OR = 5383;
/*      */   public static final int GL_NOR = 5384;
/*      */   public static final int GL_EQUIV = 5385;
/*      */   public static final int GL_INVERT = 5386;
/*      */   public static final int GL_OR_REVERSE = 5387;
/*      */   public static final int GL_COPY_INVERTED = 5388;
/*      */   public static final int GL_OR_INVERTED = 5389;
/*      */   public static final int GL_NAND = 5390;
/*      */   public static final int GL_SET = 5391;
/*      */   public static final int GL_EMISSION = 5632;
/*      */   public static final int GL_SHININESS = 5633;
/*      */   public static final int GL_AMBIENT_AND_DIFFUSE = 5634;
/*      */   public static final int GL_COLOR_INDEXES = 5635;
/*      */   public static final int GL_MODELVIEW = 5888;
/*      */   public static final int GL_PROJECTION = 5889;
/*      */   public static final int GL_TEXTURE = 5890;
/*      */   public static final int GL_COLOR = 6144;
/*      */   public static final int GL_DEPTH = 6145;
/*      */   public static final int GL_STENCIL = 6146;
/*      */   public static final int GL_COLOR_INDEX = 6400;
/*      */   public static final int GL_STENCIL_INDEX = 6401;
/*      */   public static final int GL_DEPTH_COMPONENT = 6402;
/*      */   public static final int GL_RED = 6403;
/*      */   public static final int GL_GREEN = 6404;
/*      */   public static final int GL_BLUE = 6405;
/*      */   public static final int GL_ALPHA = 6406;
/*      */   public static final int GL_RGB = 6407;
/*      */   public static final int GL_RGBA = 6408;
/*      */   public static final int GL_LUMINANCE = 6409;
/*      */   public static final int GL_LUMINANCE_ALPHA = 6410;
/*      */   public static final int GL_BITMAP = 6656;
/*      */   public static final int GL_POINT = 6912;
/*      */   public static final int GL_LINE = 6913;
/*      */   public static final int GL_FILL = 6914;
/*      */   public static final int GL_RENDER = 7168;
/*      */   public static final int GL_FEEDBACK = 7169;
/*      */   public static final int GL_SELECT = 7170;
/*      */   public static final int GL_FLAT = 7424;
/*      */   public static final int GL_SMOOTH = 7425;
/*      */   public static final int GL_KEEP = 7680;
/*      */   public static final int GL_REPLACE = 7681;
/*      */   public static final int GL_INCR = 7682;
/*      */   public static final int GL_DECR = 7683;
/*      */   public static final int GL_VENDOR = 7936;
/*      */   public static final int GL_RENDERER = 7937;
/*      */   public static final int GL_VERSION = 7938;
/*      */   public static final int GL_EXTENSIONS = 7939;
/*      */   public static final int GL_S = 8192;
/*      */   public static final int GL_T = 8193;
/*      */   public static final int GL_R = 8194;
/*      */   public static final int GL_Q = 8195;
/*      */   public static final int GL_MODULATE = 8448;
/*      */   public static final int GL_DECAL = 8449;
/*      */   public static final int GL_TEXTURE_ENV_MODE = 8704;
/*      */   public static final int GL_TEXTURE_ENV_COLOR = 8705;
/*      */   public static final int GL_TEXTURE_ENV = 8960;
/*      */   public static final int GL_EYE_LINEAR = 9216;
/*      */   public static final int GL_OBJECT_LINEAR = 9217;
/*      */   public static final int GL_SPHERE_MAP = 9218;
/*      */   public static final int GL_TEXTURE_GEN_MODE = 9472;
/*      */   public static final int GL_OBJECT_PLANE = 9473;
/*      */   public static final int GL_EYE_PLANE = 9474;
/*      */   public static final int GL_NEAREST = 9728;
/*      */   public static final int GL_LINEAR = 9729;
/*      */   public static final int GL_NEAREST_MIPMAP_NEAREST = 9984;
/*      */   public static final int GL_LINEAR_MIPMAP_NEAREST = 9985;
/*      */   public static final int GL_NEAREST_MIPMAP_LINEAR = 9986;
/*      */   public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
/*      */   public static final int GL_TEXTURE_MAG_FILTER = 10240;
/*      */   public static final int GL_TEXTURE_MIN_FILTER = 10241;
/*      */   public static final int GL_TEXTURE_WRAP_S = 10242;
/*      */   public static final int GL_TEXTURE_WRAP_T = 10243;
/*      */   public static final int GL_CLAMP = 10496;
/*      */   public static final int GL_REPEAT = 10497;
/*      */   public static final int GL_CLIENT_PIXEL_STORE_BIT = 1;
/*      */   public static final int GL_CLIENT_VERTEX_ARRAY_BIT = 2;
/*      */   public static final int GL_ALL_CLIENT_ATTRIB_BITS = -1;
/*      */   public static final int GL_POLYGON_OFFSET_FACTOR = 32824;
/*      */   public static final int GL_POLYGON_OFFSET_UNITS = 10752;
/*      */   public static final int GL_POLYGON_OFFSET_POINT = 10753;
/*      */   public static final int GL_POLYGON_OFFSET_LINE = 10754;
/*      */   public static final int GL_POLYGON_OFFSET_FILL = 32823;
/*      */   public static final int GL_ALPHA4 = 32827;
/*      */   public static final int GL_ALPHA8 = 32828;
/*      */   public static final int GL_ALPHA12 = 32829;
/*      */   public static final int GL_ALPHA16 = 32830;
/*      */   public static final int GL_LUMINANCE4 = 32831;
/*      */   public static final int GL_LUMINANCE8 = 32832;
/*      */   public static final int GL_LUMINANCE12 = 32833;
/*      */   public static final int GL_LUMINANCE16 = 32834;
/*      */   public static final int GL_LUMINANCE4_ALPHA4 = 32835;
/*      */   public static final int GL_LUMINANCE6_ALPHA2 = 32836;
/*      */   public static final int GL_LUMINANCE8_ALPHA8 = 32837;
/*      */   public static final int GL_LUMINANCE12_ALPHA4 = 32838;
/*      */   public static final int GL_LUMINANCE12_ALPHA12 = 32839;
/*      */   public static final int GL_LUMINANCE16_ALPHA16 = 32840;
/*      */   public static final int GL_INTENSITY = 32841;
/*      */   public static final int GL_INTENSITY4 = 32842;
/*      */   public static final int GL_INTENSITY8 = 32843;
/*      */   public static final int GL_INTENSITY12 = 32844;
/*      */   public static final int GL_INTENSITY16 = 32845;
/*      */   public static final int GL_R3_G3_B2 = 10768;
/*      */   public static final int GL_RGB4 = 32847;
/*      */   public static final int GL_RGB5 = 32848;
/*      */   public static final int GL_RGB8 = 32849;
/*      */   public static final int GL_RGB10 = 32850;
/*      */   public static final int GL_RGB12 = 32851;
/*      */   public static final int GL_RGB16 = 32852;
/*      */   public static final int GL_RGBA2 = 32853;
/*      */   public static final int GL_RGBA4 = 32854;
/*      */   public static final int GL_RGB5_A1 = 32855;
/*      */   public static final int GL_RGBA8 = 32856;
/*      */   public static final int GL_RGB10_A2 = 32857;
/*      */   public static final int GL_RGBA12 = 32858;
/*      */   public static final int GL_RGBA16 = 32859;
/*      */   public static final int GL_TEXTURE_RED_SIZE = 32860;
/*      */   public static final int GL_TEXTURE_GREEN_SIZE = 32861;
/*      */   public static final int GL_TEXTURE_BLUE_SIZE = 32862;
/*      */   public static final int GL_TEXTURE_ALPHA_SIZE = 32863;
/*      */   public static final int GL_TEXTURE_LUMINANCE_SIZE = 32864;
/*      */   public static final int GL_TEXTURE_INTENSITY_SIZE = 32865;
/*      */   public static final int GL_PROXY_TEXTURE_1D = 32867;
/*      */   public static final int GL_PROXY_TEXTURE_2D = 32868;
/*      */   public static final int GL_TEXTURE_PRIORITY = 32870;
/*      */   public static final int GL_TEXTURE_RESIDENT = 32871;
/*      */   public static final int GL_TEXTURE_BINDING_1D = 32872;
/*      */   public static final int GL_TEXTURE_BINDING_2D = 32873;
/*      */   public static final int GL_VERTEX_ARRAY = 32884;
/*      */   public static final int GL_NORMAL_ARRAY = 32885;
/*      */   public static final int GL_COLOR_ARRAY = 32886;
/*      */   public static final int GL_INDEX_ARRAY = 32887;
/*      */   public static final int GL_TEXTURE_COORD_ARRAY = 32888;
/*      */   public static final int GL_EDGE_FLAG_ARRAY = 32889;
/*      */   public static final int GL_VERTEX_ARRAY_SIZE = 32890;
/*      */   public static final int GL_VERTEX_ARRAY_TYPE = 32891;
/*      */   public static final int GL_VERTEX_ARRAY_STRIDE = 32892;
/*      */   public static final int GL_NORMAL_ARRAY_TYPE = 32894;
/*      */   public static final int GL_NORMAL_ARRAY_STRIDE = 32895;
/*      */   public static final int GL_COLOR_ARRAY_SIZE = 32897;
/*      */   public static final int GL_COLOR_ARRAY_TYPE = 32898;
/*      */   public static final int GL_COLOR_ARRAY_STRIDE = 32899;
/*      */   public static final int GL_INDEX_ARRAY_TYPE = 32901;
/*      */   public static final int GL_INDEX_ARRAY_STRIDE = 32902;
/*      */   public static final int GL_TEXTURE_COORD_ARRAY_SIZE = 32904;
/*      */   public static final int GL_TEXTURE_COORD_ARRAY_TYPE = 32905;
/*      */   public static final int GL_TEXTURE_COORD_ARRAY_STRIDE = 32906;
/*      */   public static final int GL_EDGE_FLAG_ARRAY_STRIDE = 32908;
/*      */   public static final int GL_VERTEX_ARRAY_POINTER = 32910;
/*      */   public static final int GL_NORMAL_ARRAY_POINTER = 32911;
/*      */   public static final int GL_COLOR_ARRAY_POINTER = 32912;
/*      */   public static final int GL_INDEX_ARRAY_POINTER = 32913;
/*      */   public static final int GL_TEXTURE_COORD_ARRAY_POINTER = 32914;
/*      */   public static final int GL_EDGE_FLAG_ARRAY_POINTER = 32915;
/*      */   public static final int GL_V2F = 10784;
/*      */   public static final int GL_V3F = 10785;
/*      */   public static final int GL_C4UB_V2F = 10786;
/*      */   public static final int GL_C4UB_V3F = 10787;
/*      */   public static final int GL_C3F_V3F = 10788;
/*      */   public static final int GL_N3F_V3F = 10789;
/*      */   public static final int GL_C4F_N3F_V3F = 10790;
/*      */   public static final int GL_T2F_V3F = 10791;
/*      */   public static final int GL_T4F_V4F = 10792;
/*      */   public static final int GL_T2F_C4UB_V3F = 10793;
/*      */   public static final int GL_T2F_C3F_V3F = 10794;
/*      */   public static final int GL_T2F_N3F_V3F = 10795;
/*      */   public static final int GL_T2F_C4F_N3F_V3F = 10796;
/*      */   public static final int GL_T4F_C4F_N3F_V4F = 10797;
/*      */   public static final int GL_LOGIC_OP = 3057;
/*      */   public static final int GL_TEXTURE_COMPONENTS = 4099;
/*      */   
/*      */   public static void glAccum(int op, float value) {
/*  553 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  554 */     long function_pointer = caps.glAccum;
/*  555 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  556 */     nglAccum(op, value, function_pointer);
/*      */   }
/*      */   static native void nglAccum(int paramInt, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glAlphaFunc(int func, float ref) {
/*  561 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  562 */     long function_pointer = caps.glAlphaFunc;
/*  563 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  564 */     nglAlphaFunc(func, ref, function_pointer);
/*      */   }
/*      */   static native void nglAlphaFunc(int paramInt, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glClearColor(float red, float green, float blue, float alpha) {
/*  569 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  570 */     long function_pointer = caps.glClearColor;
/*  571 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  572 */     nglClearColor(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglClearColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glClearAccum(float red, float green, float blue, float alpha) {
/*  577 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  578 */     long function_pointer = caps.glClearAccum;
/*  579 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  580 */     nglClearAccum(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglClearAccum(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glClear(int mask) {
/*  585 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  586 */     long function_pointer = caps.glClear;
/*  587 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  588 */     nglClear(mask, function_pointer);
/*      */   }
/*      */   static native void nglClear(int paramInt, long paramLong);
/*      */   
/*      */   public static void glCallLists(ByteBuffer lists) {
/*  593 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  594 */     long function_pointer = caps.glCallLists;
/*  595 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  596 */     BufferChecks.checkDirect(lists);
/*  597 */     nglCallLists(lists.remaining(), 5121, MemoryUtil.getAddress(lists), function_pointer);
/*      */   }
/*      */   public static void glCallLists(IntBuffer lists) {
/*  600 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  601 */     long function_pointer = caps.glCallLists;
/*  602 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  603 */     BufferChecks.checkDirect(lists);
/*  604 */     nglCallLists(lists.remaining(), 5125, MemoryUtil.getAddress(lists), function_pointer);
/*      */   }
/*      */   public static void glCallLists(ShortBuffer lists) {
/*  607 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  608 */     long function_pointer = caps.glCallLists;
/*  609 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  610 */     BufferChecks.checkDirect(lists);
/*  611 */     nglCallLists(lists.remaining(), 5123, MemoryUtil.getAddress(lists), function_pointer);
/*      */   }
/*      */   static native void nglCallLists(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glCallList(int list) {
/*  616 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  617 */     long function_pointer = caps.glCallList;
/*  618 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  619 */     nglCallList(list, function_pointer);
/*      */   }
/*      */   static native void nglCallList(int paramInt, long paramLong);
/*      */   
/*      */   public static void glBlendFunc(int sfactor, int dfactor) {
/*  624 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  625 */     long function_pointer = caps.glBlendFunc;
/*  626 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  627 */     nglBlendFunc(sfactor, dfactor, function_pointer);
/*      */   }
/*      */   static native void nglBlendFunc(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, ByteBuffer bitmap) {
/*  632 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  633 */     long function_pointer = caps.glBitmap;
/*  634 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  635 */     GLChecks.ensureUnpackPBOdisabled(caps);
/*  636 */     if (bitmap != null)
/*  637 */       BufferChecks.checkBuffer(bitmap, (width + 7) / 8 * height); 
/*  638 */     nglBitmap(width, height, xorig, yorig, xmove, ymove, MemoryUtil.getAddressSafe(bitmap), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glBitmap(int width, int height, float xorig, float yorig, float xmove, float ymove, long bitmap_buffer_offset) {
/*  642 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  643 */     long function_pointer = caps.glBitmap;
/*  644 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  645 */     GLChecks.ensureUnpackPBOenabled(caps);
/*  646 */     nglBitmapBO(width, height, xorig, yorig, xmove, ymove, bitmap_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglBitmap(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong1, long paramLong2);
/*      */   static native void nglBitmapBO(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong1, long paramLong2);
/*      */   public static void glBindTexture(int target, int texture) {
/*  651 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  652 */     long function_pointer = caps.glBindTexture;
/*  653 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  654 */     nglBindTexture(target, texture, function_pointer);
/*      */   }
/*      */   static native void nglBindTexture(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glPrioritizeTextures(IntBuffer textures, FloatBuffer priorities) {
/*  659 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  660 */     long function_pointer = caps.glPrioritizeTextures;
/*  661 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  662 */     BufferChecks.checkDirect(textures);
/*  663 */     BufferChecks.checkBuffer(priorities, textures.remaining());
/*  664 */     nglPrioritizeTextures(textures.remaining(), MemoryUtil.getAddress(textures), MemoryUtil.getAddress(priorities), function_pointer);
/*      */   }
/*      */   static native void nglPrioritizeTextures(int paramInt, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static boolean glAreTexturesResident(IntBuffer textures, ByteBuffer residences) {
/*  669 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  670 */     long function_pointer = caps.glAreTexturesResident;
/*  671 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  672 */     BufferChecks.checkDirect(textures);
/*  673 */     BufferChecks.checkBuffer(residences, textures.remaining());
/*  674 */     boolean __result = nglAreTexturesResident(textures.remaining(), MemoryUtil.getAddress(textures), MemoryUtil.getAddress(residences), function_pointer);
/*  675 */     return __result;
/*      */   }
/*      */   static native boolean nglAreTexturesResident(int paramInt, long paramLong1, long paramLong2, long paramLong3);
/*      */   
/*      */   public static void glBegin(int mode) {
/*  680 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  681 */     long function_pointer = caps.glBegin;
/*  682 */     BufferChecks.checkFunctionAddress(function_pointer);
/*      */     
/*  684 */     nglBegin(mode, function_pointer);
/*      */   }
/*      */   static native void nglBegin(int paramInt, long paramLong);
/*      */   
/*      */   public static void glEnd() {
/*  689 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  690 */     long function_pointer = caps.glEnd;
/*  691 */     BufferChecks.checkFunctionAddress(function_pointer);
/*      */     
/*  693 */     nglEnd(function_pointer);
/*      */   }
/*      */   static native void nglEnd(long paramLong);
/*      */   
/*      */   public static void glArrayElement(int i) {
/*  698 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  699 */     long function_pointer = caps.glArrayElement;
/*  700 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  701 */     nglArrayElement(i, function_pointer);
/*      */   }
/*      */   static native void nglArrayElement(int paramInt, long paramLong);
/*      */   
/*      */   public static void glClearDepth(double depth) {
/*  706 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  707 */     long function_pointer = caps.glClearDepth;
/*  708 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  709 */     nglClearDepth(depth, function_pointer);
/*      */   }
/*      */   static native void nglClearDepth(double paramDouble, long paramLong);
/*      */   
/*      */   public static void glDeleteLists(int list, int range) {
/*  714 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  715 */     long function_pointer = caps.glDeleteLists;
/*  716 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  717 */     nglDeleteLists(list, range, function_pointer);
/*      */   }
/*      */   static native void nglDeleteLists(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glDeleteTextures(IntBuffer textures) {
/*  722 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  723 */     long function_pointer = caps.glDeleteTextures;
/*  724 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  725 */     BufferChecks.checkDirect(textures);
/*  726 */     nglDeleteTextures(textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglDeleteTextures(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glDeleteTextures(int texture) {
/*  732 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  733 */     long function_pointer = caps.glDeleteTextures;
/*  734 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  735 */     nglDeleteTextures(1, APIUtil.getInt(caps, texture), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glCullFace(int mode) {
/*  739 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  740 */     long function_pointer = caps.glCullFace;
/*  741 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  742 */     nglCullFace(mode, function_pointer);
/*      */   }
/*      */   static native void nglCullFace(int paramInt, long paramLong);
/*      */   
/*      */   public static void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
/*  747 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  748 */     long function_pointer = caps.glCopyTexSubImage2D;
/*  749 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  750 */     nglCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglCopyTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);
/*      */   
/*      */   public static void glCopyTexSubImage1D(int target, int level, int xoffset, int x, int y, int width) {
/*  755 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  756 */     long function_pointer = caps.glCopyTexSubImage1D;
/*  757 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  758 */     nglCopyTexSubImage1D(target, level, xoffset, x, y, width, function_pointer);
/*      */   }
/*      */   static native void nglCopyTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*      */   
/*      */   public static void glCopyTexImage2D(int target, int level, int internalFormat, int x, int y, int width, int height, int border) {
/*  763 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  764 */     long function_pointer = caps.glCopyTexImage2D;
/*  765 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  766 */     nglCopyTexImage2D(target, level, internalFormat, x, y, width, height, border, function_pointer);
/*      */   }
/*      */   static native void nglCopyTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong);
/*      */   
/*      */   public static void glCopyTexImage1D(int target, int level, int internalFormat, int x, int y, int width, int border) {
/*  771 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  772 */     long function_pointer = caps.glCopyTexImage1D;
/*  773 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  774 */     nglCopyTexImage1D(target, level, internalFormat, x, y, width, border, function_pointer);
/*      */   }
/*      */   static native void nglCopyTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong);
/*      */   
/*      */   public static void glCopyPixels(int x, int y, int width, int height, int type) {
/*  779 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  780 */     long function_pointer = caps.glCopyPixels;
/*  781 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  782 */     nglCopyPixels(x, y, width, height, type, function_pointer);
/*      */   }
/*      */   static native void nglCopyPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glColorPointer(int size, int stride, DoubleBuffer pointer) {
/*  787 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  788 */     long function_pointer = caps.glColorPointer;
/*  789 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  790 */     GLChecks.ensureArrayVBOdisabled(caps);
/*  791 */     BufferChecks.checkDirect(pointer);
/*  792 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glColorPointer_pointer = pointer; 
/*  793 */     nglColorPointer(size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glColorPointer(int size, int stride, FloatBuffer pointer) {
/*  796 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  797 */     long function_pointer = caps.glColorPointer;
/*  798 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  799 */     GLChecks.ensureArrayVBOdisabled(caps);
/*  800 */     BufferChecks.checkDirect(pointer);
/*  801 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glColorPointer_pointer = pointer; 
/*  802 */     nglColorPointer(size, 5126, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glColorPointer(int size, boolean unsigned, int stride, ByteBuffer pointer) {
/*  805 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  806 */     long function_pointer = caps.glColorPointer;
/*  807 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  808 */     GLChecks.ensureArrayVBOdisabled(caps);
/*  809 */     BufferChecks.checkDirect(pointer);
/*  810 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glColorPointer_pointer = pointer; 
/*  811 */     nglColorPointer(size, unsigned ? 5121 : 5120, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   static native void nglColorPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glColorPointer(int size, int type, int stride, long pointer_buffer_offset) {
/*  815 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  816 */     long function_pointer = caps.glColorPointer;
/*  817 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  818 */     GLChecks.ensureArrayVBOenabled(caps);
/*  819 */     nglColorPointerBO(size, type, stride, pointer_buffer_offset, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglColorPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glColorPointer(int size, int type, int stride, ByteBuffer pointer) {
/*  825 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  826 */     long function_pointer = caps.glColorPointer;
/*  827 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  828 */     GLChecks.ensureArrayVBOdisabled(caps);
/*  829 */     BufferChecks.checkDirect(pointer);
/*  830 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glColorPointer_pointer = pointer; 
/*  831 */     nglColorPointer(size, type, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glColorMaterial(int face, int mode) {
/*  835 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  836 */     long function_pointer = caps.glColorMaterial;
/*  837 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  838 */     nglColorMaterial(face, mode, function_pointer);
/*      */   }
/*      */   static native void nglColorMaterial(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
/*  843 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  844 */     long function_pointer = caps.glColorMask;
/*  845 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  846 */     nglColorMask(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglColorMask(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong);
/*      */   
/*      */   public static void glColor3b(byte red, byte green, byte blue) {
/*  851 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  852 */     long function_pointer = caps.glColor3b;
/*  853 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  854 */     nglColor3b(red, green, blue, function_pointer);
/*      */   }
/*      */   static native void nglColor3b(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);
/*      */   
/*      */   public static void glColor3f(float red, float green, float blue) {
/*  859 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  860 */     long function_pointer = caps.glColor3f;
/*  861 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  862 */     nglColor3f(red, green, blue, function_pointer);
/*      */   }
/*      */   static native void nglColor3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glColor3d(double red, double green, double blue) {
/*  867 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  868 */     long function_pointer = caps.glColor3d;
/*  869 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  870 */     nglColor3d(red, green, blue, function_pointer);
/*      */   }
/*      */   static native void nglColor3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glColor3ub(byte red, byte green, byte blue) {
/*  875 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  876 */     long function_pointer = caps.glColor3ub;
/*  877 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  878 */     nglColor3ub(red, green, blue, function_pointer);
/*      */   }
/*      */   static native void nglColor3ub(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);
/*      */   
/*      */   public static void glColor4b(byte red, byte green, byte blue, byte alpha) {
/*  883 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  884 */     long function_pointer = caps.glColor4b;
/*  885 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  886 */     nglColor4b(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglColor4b(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, long paramLong);
/*      */   
/*      */   public static void glColor4f(float red, float green, float blue, float alpha) {
/*  891 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  892 */     long function_pointer = caps.glColor4f;
/*  893 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  894 */     nglColor4f(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglColor4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glColor4d(double red, double green, double blue, double alpha) {
/*  899 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  900 */     long function_pointer = caps.glColor4d;
/*  901 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  902 */     nglColor4d(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglColor4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static void glColor4ub(byte red, byte green, byte blue, byte alpha) {
/*  907 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  908 */     long function_pointer = caps.glColor4ub;
/*  909 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  910 */     nglColor4ub(red, green, blue, alpha, function_pointer);
/*      */   }
/*      */   static native void nglColor4ub(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, long paramLong);
/*      */   
/*      */   public static void glClipPlane(int plane, DoubleBuffer equation) {
/*  915 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  916 */     long function_pointer = caps.glClipPlane;
/*  917 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  918 */     BufferChecks.checkBuffer(equation, 4);
/*  919 */     nglClipPlane(plane, MemoryUtil.getAddress(equation), function_pointer);
/*      */   }
/*      */   static native void nglClipPlane(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glClearStencil(int s) {
/*  924 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  925 */     long function_pointer = caps.glClearStencil;
/*  926 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  927 */     nglClearStencil(s, function_pointer);
/*      */   }
/*      */   static native void nglClearStencil(int paramInt, long paramLong);
/*      */   
/*      */   public static void glEvalPoint1(int i) {
/*  932 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  933 */     long function_pointer = caps.glEvalPoint1;
/*  934 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  935 */     nglEvalPoint1(i, function_pointer);
/*      */   }
/*      */   static native void nglEvalPoint1(int paramInt, long paramLong);
/*      */   
/*      */   public static void glEvalPoint2(int i, int j) {
/*  940 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  941 */     long function_pointer = caps.glEvalPoint2;
/*  942 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  943 */     nglEvalPoint2(i, j, function_pointer);
/*      */   }
/*      */   static native void nglEvalPoint2(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glEvalMesh1(int mode, int i1, int i2) {
/*  948 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  949 */     long function_pointer = caps.glEvalMesh1;
/*  950 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  951 */     nglEvalMesh1(mode, i1, i2, function_pointer);
/*      */   }
/*      */   static native void nglEvalMesh1(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glEvalMesh2(int mode, int i1, int i2, int j1, int j2) {
/*  956 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  957 */     long function_pointer = caps.glEvalMesh2;
/*  958 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  959 */     nglEvalMesh2(mode, i1, i2, j1, j2, function_pointer);
/*      */   }
/*      */   static native void nglEvalMesh2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, long paramLong);
/*      */   
/*      */   public static void glEvalCoord1f(float u) {
/*  964 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  965 */     long function_pointer = caps.glEvalCoord1f;
/*  966 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  967 */     nglEvalCoord1f(u, function_pointer);
/*      */   }
/*      */   static native void nglEvalCoord1f(float paramFloat, long paramLong);
/*      */   
/*      */   public static void glEvalCoord1d(double u) {
/*  972 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  973 */     long function_pointer = caps.glEvalCoord1d;
/*  974 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  975 */     nglEvalCoord1d(u, function_pointer);
/*      */   }
/*      */   static native void nglEvalCoord1d(double paramDouble, long paramLong);
/*      */   
/*      */   public static void glEvalCoord2f(float u, float v) {
/*  980 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  981 */     long function_pointer = caps.glEvalCoord2f;
/*  982 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  983 */     nglEvalCoord2f(u, v, function_pointer);
/*      */   }
/*      */   static native void nglEvalCoord2f(float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glEvalCoord2d(double u, double v) {
/*  988 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  989 */     long function_pointer = caps.glEvalCoord2d;
/*  990 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  991 */     nglEvalCoord2d(u, v, function_pointer);
/*      */   }
/*      */   static native void nglEvalCoord2d(double paramDouble1, double paramDouble2, long paramLong);
/*      */   
/*      */   public static void glEnableClientState(int cap) {
/*  996 */     ContextCapabilities caps = GLContext.getCapabilities();
/*  997 */     long function_pointer = caps.glEnableClientState;
/*  998 */     BufferChecks.checkFunctionAddress(function_pointer);
/*  999 */     nglEnableClientState(cap, function_pointer);
/*      */   }
/*      */   static native void nglEnableClientState(int paramInt, long paramLong);
/*      */   
/*      */   public static void glDisableClientState(int cap) {
/* 1004 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1005 */     long function_pointer = caps.glDisableClientState;
/* 1006 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1007 */     nglDisableClientState(cap, function_pointer);
/*      */   }
/*      */   static native void nglDisableClientState(int paramInt, long paramLong);
/*      */   
/*      */   public static void glEnable(int cap) {
/* 1012 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1013 */     long function_pointer = caps.glEnable;
/* 1014 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1015 */     nglEnable(cap, function_pointer);
/*      */   }
/*      */   static native void nglEnable(int paramInt, long paramLong);
/*      */   
/*      */   public static void glDisable(int cap) {
/* 1020 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1021 */     long function_pointer = caps.glDisable;
/* 1022 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1023 */     nglDisable(cap, function_pointer);
/*      */   }
/*      */   static native void nglDisable(int paramInt, long paramLong);
/*      */   
/*      */   public static void glEdgeFlagPointer(int stride, ByteBuffer pointer) {
/* 1028 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1029 */     long function_pointer = caps.glEdgeFlagPointer;
/* 1030 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1031 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 1032 */     BufferChecks.checkDirect(pointer);
/* 1033 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glEdgeFlagPointer_pointer = pointer; 
/* 1034 */     nglEdgeFlagPointer(stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   static native void nglEdgeFlagPointer(int paramInt, long paramLong1, long paramLong2);
/*      */   public static void glEdgeFlagPointer(int stride, long pointer_buffer_offset) {
/* 1038 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1039 */     long function_pointer = caps.glEdgeFlagPointer;
/* 1040 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1041 */     GLChecks.ensureArrayVBOenabled(caps);
/* 1042 */     nglEdgeFlagPointerBO(stride, pointer_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglEdgeFlagPointerBO(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glEdgeFlag(boolean flag) {
/* 1047 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1048 */     long function_pointer = caps.glEdgeFlag;
/* 1049 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1050 */     nglEdgeFlag(flag, function_pointer);
/*      */   }
/*      */   static native void nglEdgeFlag(boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glDrawPixels(int width, int height, int format, int type, ByteBuffer pixels) {
/* 1055 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1056 */     long function_pointer = caps.glDrawPixels;
/* 1057 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1058 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 1059 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 1060 */     nglDrawPixels(width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glDrawPixels(int width, int height, int format, int type, IntBuffer pixels) {
/* 1063 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1064 */     long function_pointer = caps.glDrawPixels;
/* 1065 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1066 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 1067 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 1068 */     nglDrawPixels(width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glDrawPixels(int width, int height, int format, int type, ShortBuffer pixels) {
/* 1071 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1072 */     long function_pointer = caps.glDrawPixels;
/* 1073 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1074 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 1075 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 1076 */     nglDrawPixels(width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglDrawPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   public static void glDrawPixels(int width, int height, int format, int type, long pixels_buffer_offset) {
/* 1080 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1081 */     long function_pointer = caps.glDrawPixels;
/* 1082 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1083 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 1084 */     nglDrawPixelsBO(width, height, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglDrawPixelsBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glDrawElements(int mode, ByteBuffer indices) {
/* 1089 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1090 */     long function_pointer = caps.glDrawElements;
/* 1091 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1092 */     GLChecks.ensureElementVBOdisabled(caps);
/* 1093 */     BufferChecks.checkDirect(indices);
/* 1094 */     nglDrawElements(mode, indices.remaining(), 5121, MemoryUtil.getAddress(indices), function_pointer);
/*      */   }
/*      */   public static void glDrawElements(int mode, IntBuffer indices) {
/* 1097 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1098 */     long function_pointer = caps.glDrawElements;
/* 1099 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1100 */     GLChecks.ensureElementVBOdisabled(caps);
/* 1101 */     BufferChecks.checkDirect(indices);
/* 1102 */     nglDrawElements(mode, indices.remaining(), 5125, MemoryUtil.getAddress(indices), function_pointer);
/*      */   }
/*      */   public static void glDrawElements(int mode, ShortBuffer indices) {
/* 1105 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1106 */     long function_pointer = caps.glDrawElements;
/* 1107 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1108 */     GLChecks.ensureElementVBOdisabled(caps);
/* 1109 */     BufferChecks.checkDirect(indices);
/* 1110 */     nglDrawElements(mode, indices.remaining(), 5123, MemoryUtil.getAddress(indices), function_pointer);
/*      */   }
/*      */   static native void nglDrawElements(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glDrawElements(int mode, int indices_count, int type, long indices_buffer_offset) {
/* 1114 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1115 */     long function_pointer = caps.glDrawElements;
/* 1116 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1117 */     GLChecks.ensureElementVBOenabled(caps);
/* 1118 */     nglDrawElementsBO(mode, indices_count, type, indices_buffer_offset, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglDrawElementsBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
/* 1124 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1125 */     long function_pointer = caps.glDrawElements;
/* 1126 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1127 */     GLChecks.ensureElementVBOdisabled(caps);
/* 1128 */     BufferChecks.checkBuffer(indices, count);
/* 1129 */     nglDrawElements(mode, count, type, MemoryUtil.getAddress(indices), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glDrawBuffer(int mode) {
/* 1133 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1134 */     long function_pointer = caps.glDrawBuffer;
/* 1135 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1136 */     nglDrawBuffer(mode, function_pointer);
/*      */   }
/*      */   static native void nglDrawBuffer(int paramInt, long paramLong);
/*      */   
/*      */   public static void glDrawArrays(int mode, int first, int count) {
/* 1141 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1142 */     long function_pointer = caps.glDrawArrays;
/* 1143 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1144 */     nglDrawArrays(mode, first, count, function_pointer);
/*      */   }
/*      */   static native void nglDrawArrays(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glDepthRange(double zNear, double zFar) {
/* 1149 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1150 */     long function_pointer = caps.glDepthRange;
/* 1151 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1152 */     nglDepthRange(zNear, zFar, function_pointer);
/*      */   }
/*      */   static native void nglDepthRange(double paramDouble1, double paramDouble2, long paramLong);
/*      */   
/*      */   public static void glDepthMask(boolean flag) {
/* 1157 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1158 */     long function_pointer = caps.glDepthMask;
/* 1159 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1160 */     nglDepthMask(flag, function_pointer);
/*      */   }
/*      */   static native void nglDepthMask(boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static void glDepthFunc(int func) {
/* 1165 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1166 */     long function_pointer = caps.glDepthFunc;
/* 1167 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1168 */     nglDepthFunc(func, function_pointer);
/*      */   }
/*      */   static native void nglDepthFunc(int paramInt, long paramLong);
/*      */   
/*      */   public static void glFeedbackBuffer(int type, FloatBuffer buffer) {
/* 1173 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1174 */     long function_pointer = caps.glFeedbackBuffer;
/* 1175 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1176 */     BufferChecks.checkDirect(buffer);
/* 1177 */     nglFeedbackBuffer(buffer.remaining(), type, MemoryUtil.getAddress(buffer), function_pointer);
/*      */   }
/*      */   static native void nglFeedbackBuffer(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetPixelMap(int map, FloatBuffer values) {
/* 1182 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1183 */     long function_pointer = caps.glGetPixelMapfv;
/* 1184 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1185 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1186 */     BufferChecks.checkBuffer(values, 256);
/* 1187 */     nglGetPixelMapfv(map, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetPixelMapfv(int paramInt, long paramLong1, long paramLong2);
/*      */   public static void glGetPixelMapfv(int map, long values_buffer_offset) {
/* 1191 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1192 */     long function_pointer = caps.glGetPixelMapfv;
/* 1193 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1194 */     GLChecks.ensurePackPBOenabled(caps);
/* 1195 */     nglGetPixelMapfvBO(map, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetPixelMapfvBO(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetPixelMapu(int map, IntBuffer values) {
/* 1200 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1201 */     long function_pointer = caps.glGetPixelMapuiv;
/* 1202 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1203 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1204 */     BufferChecks.checkBuffer(values, 256);
/* 1205 */     nglGetPixelMapuiv(map, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetPixelMapuiv(int paramInt, long paramLong1, long paramLong2);
/*      */   public static void glGetPixelMapuiv(int map, long values_buffer_offset) {
/* 1209 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1210 */     long function_pointer = caps.glGetPixelMapuiv;
/* 1211 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1212 */     GLChecks.ensurePackPBOenabled(caps);
/* 1213 */     nglGetPixelMapuivBO(map, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetPixelMapuivBO(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetPixelMapu(int map, ShortBuffer values) {
/* 1218 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1219 */     long function_pointer = caps.glGetPixelMapusv;
/* 1220 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1221 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1222 */     BufferChecks.checkBuffer(values, 256);
/* 1223 */     nglGetPixelMapusv(map, MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglGetPixelMapusv(int paramInt, long paramLong1, long paramLong2);
/*      */   public static void glGetPixelMapusv(int map, long values_buffer_offset) {
/* 1227 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1228 */     long function_pointer = caps.glGetPixelMapusv;
/* 1229 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1230 */     GLChecks.ensurePackPBOenabled(caps);
/* 1231 */     nglGetPixelMapusvBO(map, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetPixelMapusvBO(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMaterial(int face, int pname, FloatBuffer params) {
/* 1236 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1237 */     long function_pointer = caps.glGetMaterialfv;
/* 1238 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1239 */     BufferChecks.checkBuffer(params, 4);
/* 1240 */     nglGetMaterialfv(face, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetMaterialfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMaterial(int face, int pname, IntBuffer params) {
/* 1245 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1246 */     long function_pointer = caps.glGetMaterialiv;
/* 1247 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1248 */     BufferChecks.checkBuffer(params, 4);
/* 1249 */     nglGetMaterialiv(face, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetMaterialiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMap(int target, int query, FloatBuffer v) {
/* 1254 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1255 */     long function_pointer = caps.glGetMapfv;
/* 1256 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1257 */     BufferChecks.checkBuffer(v, 256);
/* 1258 */     nglGetMapfv(target, query, MemoryUtil.getAddress(v), function_pointer);
/*      */   }
/*      */   static native void nglGetMapfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMap(int target, int query, DoubleBuffer v) {
/* 1263 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1264 */     long function_pointer = caps.glGetMapdv;
/* 1265 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1266 */     BufferChecks.checkBuffer(v, 256);
/* 1267 */     nglGetMapdv(target, query, MemoryUtil.getAddress(v), function_pointer);
/*      */   }
/*      */   static native void nglGetMapdv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetMap(int target, int query, IntBuffer v) {
/* 1272 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1273 */     long function_pointer = caps.glGetMapiv;
/* 1274 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1275 */     BufferChecks.checkBuffer(v, 256);
/* 1276 */     nglGetMapiv(target, query, MemoryUtil.getAddress(v), function_pointer);
/*      */   }
/*      */   static native void nglGetMapiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetLight(int light, int pname, FloatBuffer params) {
/* 1281 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1282 */     long function_pointer = caps.glGetLightfv;
/* 1283 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1284 */     BufferChecks.checkBuffer(params, 4);
/* 1285 */     nglGetLightfv(light, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetLightfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetLight(int light, int pname, IntBuffer params) {
/* 1290 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1291 */     long function_pointer = caps.glGetLightiv;
/* 1292 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1293 */     BufferChecks.checkBuffer(params, 4);
/* 1294 */     nglGetLightiv(light, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglGetLightiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetError() {
/* 1299 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1300 */     long function_pointer = caps.glGetError;
/* 1301 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1302 */     int __result = nglGetError(function_pointer);
/* 1303 */     return __result;
/*      */   }
/*      */   static native int nglGetError(long paramLong);
/*      */   
/*      */   public static void glGetClipPlane(int plane, DoubleBuffer equation) {
/* 1308 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1309 */     long function_pointer = caps.glGetClipPlane;
/* 1310 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1311 */     BufferChecks.checkBuffer(equation, 4);
/* 1312 */     nglGetClipPlane(plane, MemoryUtil.getAddress(equation), function_pointer);
/*      */   }
/*      */   static native void nglGetClipPlane(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetBoolean(int pname, ByteBuffer params) {
/* 1317 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1318 */     long function_pointer = caps.glGetBooleanv;
/* 1319 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1320 */     BufferChecks.checkBuffer(params, 16);
/* 1321 */     nglGetBooleanv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetBooleanv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static boolean glGetBoolean(int pname) {
/* 1327 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1328 */     long function_pointer = caps.glGetBooleanv;
/* 1329 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1330 */     ByteBuffer params = APIUtil.getBufferByte(caps, 1);
/* 1331 */     nglGetBooleanv(pname, MemoryUtil.getAddress(params), function_pointer);
/* 1332 */     return (params.get(0) == 1);
/*      */   }
/*      */   
/*      */   public static void glGetDouble(int pname, DoubleBuffer params) {
/* 1336 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1337 */     long function_pointer = caps.glGetDoublev;
/* 1338 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1339 */     BufferChecks.checkBuffer(params, 16);
/* 1340 */     nglGetDoublev(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetDoublev(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static double glGetDouble(int pname) {
/* 1346 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1347 */     long function_pointer = caps.glGetDoublev;
/* 1348 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1349 */     DoubleBuffer params = APIUtil.getBufferDouble(caps);
/* 1350 */     nglGetDoublev(pname, MemoryUtil.getAddress(params), function_pointer);
/* 1351 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetFloat(int pname, FloatBuffer params) {
/* 1355 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1356 */     long function_pointer = caps.glGetFloatv;
/* 1357 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1358 */     BufferChecks.checkBuffer(params, 16);
/* 1359 */     nglGetFloatv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetFloatv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetFloat(int pname) {
/* 1365 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1366 */     long function_pointer = caps.glGetFloatv;
/* 1367 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1368 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1369 */     nglGetFloatv(pname, MemoryUtil.getAddress(params), function_pointer);
/* 1370 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetInteger(int pname, IntBuffer params) {
/* 1374 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1375 */     long function_pointer = caps.glGetIntegerv;
/* 1376 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1377 */     BufferChecks.checkBuffer(params, 16);
/* 1378 */     nglGetIntegerv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetIntegerv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetInteger(int pname) {
/* 1384 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1385 */     long function_pointer = caps.glGetIntegerv;
/* 1386 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1387 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1388 */     nglGetIntegerv(pname, MemoryUtil.getAddress(params), function_pointer);
/* 1389 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGenTextures(IntBuffer textures) {
/* 1393 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1394 */     long function_pointer = caps.glGenTextures;
/* 1395 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1396 */     BufferChecks.checkDirect(textures);
/* 1397 */     nglGenTextures(textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGenTextures(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGenTextures() {
/* 1403 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1404 */     long function_pointer = caps.glGenTextures;
/* 1405 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1406 */     IntBuffer textures = APIUtil.getBufferInt(caps);
/* 1407 */     nglGenTextures(1, MemoryUtil.getAddress(textures), function_pointer);
/* 1408 */     return textures.get(0);
/*      */   }
/*      */   
/*      */   public static int glGenLists(int range) {
/* 1412 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1413 */     long function_pointer = caps.glGenLists;
/* 1414 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1415 */     int __result = nglGenLists(range, function_pointer);
/* 1416 */     return __result;
/*      */   }
/*      */   static native int nglGenLists(int paramInt, long paramLong);
/*      */   
/*      */   public static void glFrustum(double left, double right, double bottom, double top, double zNear, double zFar) {
/* 1421 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1422 */     long function_pointer = caps.glFrustum;
/* 1423 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1424 */     nglFrustum(left, right, bottom, top, zNear, zFar, function_pointer);
/*      */   }
/*      */   static native void nglFrustum(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, long paramLong);
/*      */   
/*      */   public static void glFrontFace(int mode) {
/* 1429 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1430 */     long function_pointer = caps.glFrontFace;
/* 1431 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1432 */     nglFrontFace(mode, function_pointer);
/*      */   }
/*      */   static native void nglFrontFace(int paramInt, long paramLong);
/*      */   
/*      */   public static void glFogf(int pname, float param) {
/* 1437 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1438 */     long function_pointer = caps.glFogf;
/* 1439 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1440 */     nglFogf(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglFogf(int paramInt, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glFogi(int pname, int param) {
/* 1445 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1446 */     long function_pointer = caps.glFogi;
/* 1447 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1448 */     nglFogi(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglFogi(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glFog(int pname, FloatBuffer params) {
/* 1453 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1454 */     long function_pointer = caps.glFogfv;
/* 1455 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1456 */     BufferChecks.checkBuffer(params, 4);
/* 1457 */     nglFogfv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglFogfv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glFog(int pname, IntBuffer params) {
/* 1462 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1463 */     long function_pointer = caps.glFogiv;
/* 1464 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1465 */     BufferChecks.checkBuffer(params, 4);
/* 1466 */     nglFogiv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglFogiv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glFlush() {
/* 1471 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1472 */     long function_pointer = caps.glFlush;
/* 1473 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1474 */     nglFlush(function_pointer);
/*      */   }
/*      */   static native void nglFlush(long paramLong);
/*      */   
/*      */   public static void glFinish() {
/* 1479 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1480 */     long function_pointer = caps.glFinish;
/* 1481 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1482 */     nglFinish(function_pointer);
/*      */   }
/*      */   static native void nglFinish(long paramLong);
/*      */   
/*      */   public static ByteBuffer glGetPointer(int pname, long result_size) {
/* 1487 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1488 */     long function_pointer = caps.glGetPointerv;
/* 1489 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1490 */     ByteBuffer __result = nglGetPointerv(pname, result_size, function_pointer);
/* 1491 */     return (LWJGLUtil.CHECKS && __result == null) ? null : __result.order(ByteOrder.nativeOrder());
/*      */   }
/*      */   static native ByteBuffer nglGetPointerv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static boolean glIsEnabled(int cap) {
/* 1496 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1497 */     long function_pointer = caps.glIsEnabled;
/* 1498 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1499 */     boolean __result = nglIsEnabled(cap, function_pointer);
/* 1500 */     return __result;
/*      */   }
/*      */   static native boolean nglIsEnabled(int paramInt, long paramLong);
/*      */   
/*      */   public static void glInterleavedArrays(int format, int stride, ByteBuffer pointer) {
/* 1505 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1506 */     long function_pointer = caps.glInterleavedArrays;
/* 1507 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1508 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 1509 */     BufferChecks.checkDirect(pointer);
/* 1510 */     nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glInterleavedArrays(int format, int stride, DoubleBuffer pointer) {
/* 1513 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1514 */     long function_pointer = caps.glInterleavedArrays;
/* 1515 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1516 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 1517 */     BufferChecks.checkDirect(pointer);
/* 1518 */     nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glInterleavedArrays(int format, int stride, FloatBuffer pointer) {
/* 1521 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1522 */     long function_pointer = caps.glInterleavedArrays;
/* 1523 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1524 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 1525 */     BufferChecks.checkDirect(pointer);
/* 1526 */     nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glInterleavedArrays(int format, int stride, IntBuffer pointer) {
/* 1529 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1530 */     long function_pointer = caps.glInterleavedArrays;
/* 1531 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1532 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 1533 */     BufferChecks.checkDirect(pointer);
/* 1534 */     nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glInterleavedArrays(int format, int stride, ShortBuffer pointer) {
/* 1537 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1538 */     long function_pointer = caps.glInterleavedArrays;
/* 1539 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1540 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 1541 */     BufferChecks.checkDirect(pointer);
/* 1542 */     nglInterleavedArrays(format, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   static native void nglInterleavedArrays(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static void glInterleavedArrays(int format, int stride, long pointer_buffer_offset) {
/* 1546 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1547 */     long function_pointer = caps.glInterleavedArrays;
/* 1548 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1549 */     GLChecks.ensureArrayVBOenabled(caps);
/* 1550 */     nglInterleavedArraysBO(format, stride, pointer_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglInterleavedArraysBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glInitNames() {
/* 1555 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1556 */     long function_pointer = caps.glInitNames;
/* 1557 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1558 */     nglInitNames(function_pointer);
/*      */   }
/*      */   static native void nglInitNames(long paramLong);
/*      */   
/*      */   public static void glHint(int target, int mode) {
/* 1563 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1564 */     long function_pointer = caps.glHint;
/* 1565 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1566 */     nglHint(target, mode, function_pointer);
/*      */   }
/*      */   static native void nglHint(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glGetTexParameter(int target, int pname, FloatBuffer params) {
/* 1571 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1572 */     long function_pointer = caps.glGetTexParameterfv;
/* 1573 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1574 */     BufferChecks.checkBuffer(params, 4);
/* 1575 */     nglGetTexParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetTexParameterf(int target, int pname) {
/* 1581 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1582 */     long function_pointer = caps.glGetTexParameterfv;
/* 1583 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1584 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1585 */     nglGetTexParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1586 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexParameter(int target, int pname, IntBuffer params) {
/* 1590 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1591 */     long function_pointer = caps.glGetTexParameteriv;
/* 1592 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1593 */     BufferChecks.checkBuffer(params, 4);
/* 1594 */     nglGetTexParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTexParameteri(int target, int pname) {
/* 1600 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1601 */     long function_pointer = caps.glGetTexParameteriv;
/* 1602 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1603 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1604 */     nglGetTexParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1605 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexLevelParameter(int target, int level, int pname, FloatBuffer params) {
/* 1609 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1610 */     long function_pointer = caps.glGetTexLevelParameterfv;
/* 1611 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1612 */     BufferChecks.checkBuffer(params, 4);
/* 1613 */     nglGetTexLevelParameterfv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexLevelParameterfv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetTexLevelParameterf(int target, int level, int pname) {
/* 1619 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1620 */     long function_pointer = caps.glGetTexLevelParameterfv;
/* 1621 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1622 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1623 */     nglGetTexLevelParameterfv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1624 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexLevelParameter(int target, int level, int pname, IntBuffer params) {
/* 1628 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1629 */     long function_pointer = caps.glGetTexLevelParameteriv;
/* 1630 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1631 */     BufferChecks.checkBuffer(params, 4);
/* 1632 */     nglGetTexLevelParameteriv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexLevelParameteriv(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTexLevelParameteri(int target, int level, int pname) {
/* 1638 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1639 */     long function_pointer = caps.glGetTexLevelParameteriv;
/* 1640 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1641 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1642 */     nglGetTexLevelParameteriv(target, level, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1643 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexImage(int target, int level, int format, int type, ByteBuffer pixels) {
/* 1647 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1648 */     long function_pointer = caps.glGetTexImage;
/* 1649 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1650 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1651 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
/* 1652 */     nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTexImage(int target, int level, int format, int type, DoubleBuffer pixels) {
/* 1655 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1656 */     long function_pointer = caps.glGetTexImage;
/* 1657 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1658 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1659 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
/* 1660 */     nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTexImage(int target, int level, int format, int type, FloatBuffer pixels) {
/* 1663 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1664 */     long function_pointer = caps.glGetTexImage;
/* 1665 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1666 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1667 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
/* 1668 */     nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTexImage(int target, int level, int format, int type, IntBuffer pixels) {
/* 1671 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1672 */     long function_pointer = caps.glGetTexImage;
/* 1673 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1674 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1675 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
/* 1676 */     nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glGetTexImage(int target, int level, int format, int type, ShortBuffer pixels) {
/* 1679 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1680 */     long function_pointer = caps.glGetTexImage;
/* 1681 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1682 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1683 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, 1, 1, 1));
/* 1684 */     nglGetTexImage(target, level, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglGetTexImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   public static void glGetTexImage(int target, int level, int format, int type, long pixels_buffer_offset) {
/* 1688 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1689 */     long function_pointer = caps.glGetTexImage;
/* 1690 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1691 */     GLChecks.ensurePackPBOenabled(caps);
/* 1692 */     nglGetTexImageBO(target, level, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetTexImageBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glGetTexGen(int coord, int pname, IntBuffer params) {
/* 1697 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1698 */     long function_pointer = caps.glGetTexGeniv;
/* 1699 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1700 */     BufferChecks.checkBuffer(params, 4);
/* 1701 */     nglGetTexGeniv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexGeniv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTexGeni(int coord, int pname) {
/* 1707 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1708 */     long function_pointer = caps.glGetTexGeniv;
/* 1709 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1710 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1711 */     nglGetTexGeniv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1712 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexGen(int coord, int pname, FloatBuffer params) {
/* 1716 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1717 */     long function_pointer = caps.glGetTexGenfv;
/* 1718 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1719 */     BufferChecks.checkBuffer(params, 4);
/* 1720 */     nglGetTexGenfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexGenfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetTexGenf(int coord, int pname) {
/* 1726 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1727 */     long function_pointer = caps.glGetTexGenfv;
/* 1728 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1729 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1730 */     nglGetTexGenfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1731 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexGen(int coord, int pname, DoubleBuffer params) {
/* 1735 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1736 */     long function_pointer = caps.glGetTexGendv;
/* 1737 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1738 */     BufferChecks.checkBuffer(params, 4);
/* 1739 */     nglGetTexGendv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexGendv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static double glGetTexGend(int coord, int pname) {
/* 1745 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1746 */     long function_pointer = caps.glGetTexGendv;
/* 1747 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1748 */     DoubleBuffer params = APIUtil.getBufferDouble(caps);
/* 1749 */     nglGetTexGendv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1750 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexEnv(int coord, int pname, IntBuffer params) {
/* 1754 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1755 */     long function_pointer = caps.glGetTexEnviv;
/* 1756 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1757 */     BufferChecks.checkBuffer(params, 4);
/* 1758 */     nglGetTexEnviv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexEnviv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static int glGetTexEnvi(int coord, int pname) {
/* 1764 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1765 */     long function_pointer = caps.glGetTexEnviv;
/* 1766 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1767 */     IntBuffer params = APIUtil.getBufferInt(caps);
/* 1768 */     nglGetTexEnviv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1769 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static void glGetTexEnv(int coord, int pname, FloatBuffer params) {
/* 1773 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1774 */     long function_pointer = caps.glGetTexEnvfv;
/* 1775 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1776 */     BufferChecks.checkBuffer(params, 4);
/* 1777 */     nglGetTexEnvfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglGetTexEnvfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static float glGetTexEnvf(int coord, int pname) {
/* 1783 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1784 */     long function_pointer = caps.glGetTexEnvfv;
/* 1785 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1786 */     FloatBuffer params = APIUtil.getBufferFloat(caps);
/* 1787 */     nglGetTexEnvfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/* 1788 */     return params.get(0);
/*      */   }
/*      */   
/*      */   public static String glGetString(int name) {
/* 1792 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1793 */     long function_pointer = caps.glGetString;
/* 1794 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1795 */     String __result = nglGetString(name, function_pointer);
/* 1796 */     return __result;
/*      */   }
/*      */   static native String nglGetString(int paramInt, long paramLong);
/*      */   
/*      */   public static void glGetPolygonStipple(ByteBuffer mask) {
/* 1801 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1802 */     long function_pointer = caps.glGetPolygonStipple;
/* 1803 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1804 */     GLChecks.ensurePackPBOdisabled(caps);
/* 1805 */     BufferChecks.checkBuffer(mask, 128);
/* 1806 */     nglGetPolygonStipple(MemoryUtil.getAddress(mask), function_pointer);
/*      */   }
/*      */   static native void nglGetPolygonStipple(long paramLong1, long paramLong2);
/*      */   public static void glGetPolygonStipple(long mask_buffer_offset) {
/* 1810 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1811 */     long function_pointer = caps.glGetPolygonStipple;
/* 1812 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1813 */     GLChecks.ensurePackPBOenabled(caps);
/* 1814 */     nglGetPolygonStippleBO(mask_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglGetPolygonStippleBO(long paramLong1, long paramLong2);
/*      */   
/*      */   public static boolean glIsList(int list) {
/* 1819 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1820 */     long function_pointer = caps.glIsList;
/* 1821 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1822 */     boolean __result = nglIsList(list, function_pointer);
/* 1823 */     return __result;
/*      */   }
/*      */   static native boolean nglIsList(int paramInt, long paramLong);
/*      */   
/*      */   public static void glMaterialf(int face, int pname, float param) {
/* 1828 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1829 */     long function_pointer = caps.glMaterialf;
/* 1830 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1831 */     nglMaterialf(face, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglMaterialf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glMateriali(int face, int pname, int param) {
/* 1836 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1837 */     long function_pointer = caps.glMateriali;
/* 1838 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1839 */     nglMateriali(face, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglMateriali(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glMaterial(int face, int pname, FloatBuffer params) {
/* 1844 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1845 */     long function_pointer = caps.glMaterialfv;
/* 1846 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1847 */     BufferChecks.checkBuffer(params, 4);
/* 1848 */     nglMaterialfv(face, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglMaterialfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMaterial(int face, int pname, IntBuffer params) {
/* 1853 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1854 */     long function_pointer = caps.glMaterialiv;
/* 1855 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1856 */     BufferChecks.checkBuffer(params, 4);
/* 1857 */     nglMaterialiv(face, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglMaterialiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMapGrid1f(int un, float u1, float u2) {
/* 1862 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1863 */     long function_pointer = caps.glMapGrid1f;
/* 1864 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1865 */     nglMapGrid1f(un, u1, u2, function_pointer);
/*      */   }
/*      */   static native void nglMapGrid1f(int paramInt, float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glMapGrid1d(int un, double u1, double u2) {
/* 1870 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1871 */     long function_pointer = caps.glMapGrid1d;
/* 1872 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1873 */     nglMapGrid1d(un, u1, u2, function_pointer);
/*      */   }
/*      */   static native void nglMapGrid1d(int paramInt, double paramDouble1, double paramDouble2, long paramLong);
/*      */   
/*      */   public static void glMapGrid2f(int un, float u1, float u2, int vn, float v1, float v2) {
/* 1878 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1879 */     long function_pointer = caps.glMapGrid2f;
/* 1880 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1881 */     nglMapGrid2f(un, u1, u2, vn, v1, v2, function_pointer);
/*      */   }
/*      */   static native void nglMapGrid2f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glMapGrid2d(int un, double u1, double u2, int vn, double v1, double v2) {
/* 1886 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1887 */     long function_pointer = caps.glMapGrid2d;
/* 1888 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1889 */     nglMapGrid2d(un, u1, u2, vn, v1, v2, function_pointer);
/*      */   }
/*      */   static native void nglMapGrid2d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static void glMap2f(int target, float u1, float u2, int ustride, int uorder, float v1, float v2, int vstride, int vorder, FloatBuffer points) {
/* 1894 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1895 */     long function_pointer = caps.glMap2f;
/* 1896 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1897 */     BufferChecks.checkDirect(points);
/* 1898 */     nglMap2f(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, MemoryUtil.getAddress(points), function_pointer);
/*      */   }
/*      */   static native void nglMap2f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, float paramFloat3, float paramFloat4, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMap2d(int target, double u1, double u2, int ustride, int uorder, double v1, double v2, int vstride, int vorder, DoubleBuffer points) {
/* 1903 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1904 */     long function_pointer = caps.glMap2d;
/* 1905 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1906 */     BufferChecks.checkDirect(points);
/* 1907 */     nglMap2d(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, MemoryUtil.getAddress(points), function_pointer);
/*      */   }
/*      */   static native void nglMap2d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, double paramDouble3, double paramDouble4, int paramInt4, int paramInt5, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMap1f(int target, float u1, float u2, int stride, int order, FloatBuffer points) {
/* 1912 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1913 */     long function_pointer = caps.glMap1f;
/* 1914 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1915 */     BufferChecks.checkDirect(points);
/* 1916 */     nglMap1f(target, u1, u2, stride, order, MemoryUtil.getAddress(points), function_pointer);
/*      */   }
/*      */   static native void nglMap1f(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMap1d(int target, double u1, double u2, int stride, int order, DoubleBuffer points) {
/* 1921 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1922 */     long function_pointer = caps.glMap1d;
/* 1923 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1924 */     BufferChecks.checkDirect(points);
/* 1925 */     nglMap1d(target, u1, u2, stride, order, MemoryUtil.getAddress(points), function_pointer);
/*      */   }
/*      */   static native void nglMap1d(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glLogicOp(int opcode) {
/* 1930 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1931 */     long function_pointer = caps.glLogicOp;
/* 1932 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1933 */     nglLogicOp(opcode, function_pointer);
/*      */   }
/*      */   static native void nglLogicOp(int paramInt, long paramLong);
/*      */   
/*      */   public static void glLoadName(int name) {
/* 1938 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1939 */     long function_pointer = caps.glLoadName;
/* 1940 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1941 */     nglLoadName(name, function_pointer);
/*      */   }
/*      */   static native void nglLoadName(int paramInt, long paramLong);
/*      */   
/*      */   public static void glLoadMatrix(FloatBuffer m) {
/* 1946 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1947 */     long function_pointer = caps.glLoadMatrixf;
/* 1948 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1949 */     BufferChecks.checkBuffer(m, 16);
/* 1950 */     nglLoadMatrixf(MemoryUtil.getAddress(m), function_pointer);
/*      */   }
/*      */   static native void nglLoadMatrixf(long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glLoadMatrix(DoubleBuffer m) {
/* 1955 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1956 */     long function_pointer = caps.glLoadMatrixd;
/* 1957 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1958 */     BufferChecks.checkBuffer(m, 16);
/* 1959 */     nglLoadMatrixd(MemoryUtil.getAddress(m), function_pointer);
/*      */   }
/*      */   static native void nglLoadMatrixd(long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glLoadIdentity() {
/* 1964 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1965 */     long function_pointer = caps.glLoadIdentity;
/* 1966 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1967 */     nglLoadIdentity(function_pointer);
/*      */   }
/*      */   static native void nglLoadIdentity(long paramLong);
/*      */   
/*      */   public static void glListBase(int base) {
/* 1972 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1973 */     long function_pointer = caps.glListBase;
/* 1974 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1975 */     nglListBase(base, function_pointer);
/*      */   }
/*      */   static native void nglListBase(int paramInt, long paramLong);
/*      */   
/*      */   public static void glLineWidth(float width) {
/* 1980 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1981 */     long function_pointer = caps.glLineWidth;
/* 1982 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1983 */     nglLineWidth(width, function_pointer);
/*      */   }
/*      */   static native void nglLineWidth(float paramFloat, long paramLong);
/*      */   
/*      */   public static void glLineStipple(int factor, short pattern) {
/* 1988 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1989 */     long function_pointer = caps.glLineStipple;
/* 1990 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1991 */     nglLineStipple(factor, pattern, function_pointer);
/*      */   }
/*      */   static native void nglLineStipple(int paramInt, short paramShort, long paramLong);
/*      */   
/*      */   public static void glLightModelf(int pname, float param) {
/* 1996 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 1997 */     long function_pointer = caps.glLightModelf;
/* 1998 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 1999 */     nglLightModelf(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglLightModelf(int paramInt, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glLightModeli(int pname, int param) {
/* 2004 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2005 */     long function_pointer = caps.glLightModeli;
/* 2006 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2007 */     nglLightModeli(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglLightModeli(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glLightModel(int pname, FloatBuffer params) {
/* 2012 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2013 */     long function_pointer = caps.glLightModelfv;
/* 2014 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2015 */     BufferChecks.checkBuffer(params, 4);
/* 2016 */     nglLightModelfv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglLightModelfv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glLightModel(int pname, IntBuffer params) {
/* 2021 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2022 */     long function_pointer = caps.glLightModeliv;
/* 2023 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2024 */     BufferChecks.checkBuffer(params, 4);
/* 2025 */     nglLightModeliv(pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglLightModeliv(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glLightf(int light, int pname, float param) {
/* 2030 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2031 */     long function_pointer = caps.glLightf;
/* 2032 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2033 */     nglLightf(light, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglLightf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glLighti(int light, int pname, int param) {
/* 2038 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2039 */     long function_pointer = caps.glLighti;
/* 2040 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2041 */     nglLighti(light, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglLighti(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glLight(int light, int pname, FloatBuffer params) {
/* 2046 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2047 */     long function_pointer = caps.glLightfv;
/* 2048 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2049 */     BufferChecks.checkBuffer(params, 4);
/* 2050 */     nglLightfv(light, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglLightfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glLight(int light, int pname, IntBuffer params) {
/* 2055 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2056 */     long function_pointer = caps.glLightiv;
/* 2057 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2058 */     BufferChecks.checkBuffer(params, 4);
/* 2059 */     nglLightiv(light, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglLightiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static boolean glIsTexture(int texture) {
/* 2064 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2065 */     long function_pointer = caps.glIsTexture;
/* 2066 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2067 */     boolean __result = nglIsTexture(texture, function_pointer);
/* 2068 */     return __result;
/*      */   }
/*      */   static native boolean nglIsTexture(int paramInt, long paramLong);
/*      */   
/*      */   public static void glMatrixMode(int mode) {
/* 2073 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2074 */     long function_pointer = caps.glMatrixMode;
/* 2075 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2076 */     nglMatrixMode(mode, function_pointer);
/*      */   }
/*      */   static native void nglMatrixMode(int paramInt, long paramLong);
/*      */   
/*      */   public static void glPolygonStipple(ByteBuffer mask) {
/* 2081 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2082 */     long function_pointer = caps.glPolygonStipple;
/* 2083 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2084 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2085 */     BufferChecks.checkBuffer(mask, 128);
/* 2086 */     nglPolygonStipple(MemoryUtil.getAddress(mask), function_pointer);
/*      */   }
/*      */   static native void nglPolygonStipple(long paramLong1, long paramLong2);
/*      */   public static void glPolygonStipple(long mask_buffer_offset) {
/* 2090 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2091 */     long function_pointer = caps.glPolygonStipple;
/* 2092 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2093 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2094 */     nglPolygonStippleBO(mask_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglPolygonStippleBO(long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glPolygonOffset(float factor, float units) {
/* 2099 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2100 */     long function_pointer = caps.glPolygonOffset;
/* 2101 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2102 */     nglPolygonOffset(factor, units, function_pointer);
/*      */   }
/*      */   static native void nglPolygonOffset(float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glPolygonMode(int face, int mode) {
/* 2107 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2108 */     long function_pointer = caps.glPolygonMode;
/* 2109 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2110 */     nglPolygonMode(face, mode, function_pointer);
/*      */   }
/*      */   static native void nglPolygonMode(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glPointSize(float size) {
/* 2115 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2116 */     long function_pointer = caps.glPointSize;
/* 2117 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2118 */     nglPointSize(size, function_pointer);
/*      */   }
/*      */   static native void nglPointSize(float paramFloat, long paramLong);
/*      */   
/*      */   public static void glPixelZoom(float xfactor, float yfactor) {
/* 2123 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2124 */     long function_pointer = caps.glPixelZoom;
/* 2125 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2126 */     nglPixelZoom(xfactor, yfactor, function_pointer);
/*      */   }
/*      */   static native void nglPixelZoom(float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glPixelTransferf(int pname, float param) {
/* 2131 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2132 */     long function_pointer = caps.glPixelTransferf;
/* 2133 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2134 */     nglPixelTransferf(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglPixelTransferf(int paramInt, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glPixelTransferi(int pname, int param) {
/* 2139 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2140 */     long function_pointer = caps.glPixelTransferi;
/* 2141 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2142 */     nglPixelTransferi(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglPixelTransferi(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glPixelStoref(int pname, float param) {
/* 2147 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2148 */     long function_pointer = caps.glPixelStoref;
/* 2149 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2150 */     nglPixelStoref(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglPixelStoref(int paramInt, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glPixelStorei(int pname, int param) {
/* 2155 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2156 */     long function_pointer = caps.glPixelStorei;
/* 2157 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2158 */     nglPixelStorei(pname, param, function_pointer);
/*      */   }
/*      */   static native void nglPixelStorei(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glPixelMap(int map, FloatBuffer values) {
/* 2163 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2164 */     long function_pointer = caps.glPixelMapfv;
/* 2165 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2166 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2167 */     BufferChecks.checkDirect(values);
/* 2168 */     nglPixelMapfv(map, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglPixelMapfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static void glPixelMapfv(int map, int values_mapsize, long values_buffer_offset) {
/* 2172 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2173 */     long function_pointer = caps.glPixelMapfv;
/* 2174 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2175 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2176 */     nglPixelMapfvBO(map, values_mapsize, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglPixelMapfvBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glPixelMapu(int map, IntBuffer values) {
/* 2181 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2182 */     long function_pointer = caps.glPixelMapuiv;
/* 2183 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2184 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2185 */     BufferChecks.checkDirect(values);
/* 2186 */     nglPixelMapuiv(map, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglPixelMapuiv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static void glPixelMapuiv(int map, int values_mapsize, long values_buffer_offset) {
/* 2190 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2191 */     long function_pointer = caps.glPixelMapuiv;
/* 2192 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2193 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2194 */     nglPixelMapuivBO(map, values_mapsize, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglPixelMapuivBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glPixelMapu(int map, ShortBuffer values) {
/* 2199 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2200 */     long function_pointer = caps.glPixelMapusv;
/* 2201 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2202 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2203 */     BufferChecks.checkDirect(values);
/* 2204 */     nglPixelMapusv(map, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
/*      */   }
/*      */   static native void nglPixelMapusv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static void glPixelMapusv(int map, int values_mapsize, long values_buffer_offset) {
/* 2208 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2209 */     long function_pointer = caps.glPixelMapusv;
/* 2210 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2211 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2212 */     nglPixelMapusvBO(map, values_mapsize, values_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglPixelMapusvBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glPassThrough(float token) {
/* 2217 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2218 */     long function_pointer = caps.glPassThrough;
/* 2219 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2220 */     nglPassThrough(token, function_pointer);
/*      */   }
/*      */   static native void nglPassThrough(float paramFloat, long paramLong);
/*      */   
/*      */   public static void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
/* 2225 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2226 */     long function_pointer = caps.glOrtho;
/* 2227 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2228 */     nglOrtho(left, right, bottom, top, zNear, zFar, function_pointer);
/*      */   }
/*      */   static native void nglOrtho(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, long paramLong);
/*      */   
/*      */   public static void glNormalPointer(int stride, ByteBuffer pointer) {
/* 2233 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2234 */     long function_pointer = caps.glNormalPointer;
/* 2235 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2236 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2237 */     BufferChecks.checkDirect(pointer);
/* 2238 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glNormalPointer_pointer = pointer; 
/* 2239 */     nglNormalPointer(5120, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glNormalPointer(int stride, DoubleBuffer pointer) {
/* 2242 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2243 */     long function_pointer = caps.glNormalPointer;
/* 2244 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2245 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2246 */     BufferChecks.checkDirect(pointer);
/* 2247 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glNormalPointer_pointer = pointer; 
/* 2248 */     nglNormalPointer(5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glNormalPointer(int stride, FloatBuffer pointer) {
/* 2251 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2252 */     long function_pointer = caps.glNormalPointer;
/* 2253 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2254 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2255 */     BufferChecks.checkDirect(pointer);
/* 2256 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glNormalPointer_pointer = pointer; 
/* 2257 */     nglNormalPointer(5126, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glNormalPointer(int stride, IntBuffer pointer) {
/* 2260 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2261 */     long function_pointer = caps.glNormalPointer;
/* 2262 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2263 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2264 */     BufferChecks.checkDirect(pointer);
/* 2265 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glNormalPointer_pointer = pointer; 
/* 2266 */     nglNormalPointer(5124, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   static native void nglNormalPointer(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static void glNormalPointer(int type, int stride, long pointer_buffer_offset) {
/* 2270 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2271 */     long function_pointer = caps.glNormalPointer;
/* 2272 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2273 */     GLChecks.ensureArrayVBOenabled(caps);
/* 2274 */     nglNormalPointerBO(type, stride, pointer_buffer_offset, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglNormalPointerBO(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glNormalPointer(int type, int stride, ByteBuffer pointer) {
/* 2280 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2281 */     long function_pointer = caps.glNormalPointer;
/* 2282 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2283 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2284 */     BufferChecks.checkDirect(pointer);
/* 2285 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glNormalPointer_pointer = pointer; 
/* 2286 */     nglNormalPointer(type, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glNormal3b(byte nx, byte ny, byte nz) {
/* 2290 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2291 */     long function_pointer = caps.glNormal3b;
/* 2292 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2293 */     nglNormal3b(nx, ny, nz, function_pointer);
/*      */   }
/*      */   static native void nglNormal3b(byte paramByte1, byte paramByte2, byte paramByte3, long paramLong);
/*      */   
/*      */   public static void glNormal3f(float nx, float ny, float nz) {
/* 2298 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2299 */     long function_pointer = caps.glNormal3f;
/* 2300 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2301 */     nglNormal3f(nx, ny, nz, function_pointer);
/*      */   }
/*      */   static native void nglNormal3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glNormal3d(double nx, double ny, double nz) {
/* 2306 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2307 */     long function_pointer = caps.glNormal3d;
/* 2308 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2309 */     nglNormal3d(nx, ny, nz, function_pointer);
/*      */   }
/*      */   static native void nglNormal3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glNormal3i(int nx, int ny, int nz) {
/* 2314 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2315 */     long function_pointer = caps.glNormal3i;
/* 2316 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2317 */     nglNormal3i(nx, ny, nz, function_pointer);
/*      */   }
/*      */   static native void nglNormal3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glNewList(int list, int mode) {
/* 2322 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2323 */     long function_pointer = caps.glNewList;
/* 2324 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2325 */     nglNewList(list, mode, function_pointer);
/*      */   }
/*      */   static native void nglNewList(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glEndList() {
/* 2330 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2331 */     long function_pointer = caps.glEndList;
/* 2332 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2333 */     nglEndList(function_pointer);
/*      */   }
/*      */   static native void nglEndList(long paramLong);
/*      */   
/*      */   public static void glMultMatrix(FloatBuffer m) {
/* 2338 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2339 */     long function_pointer = caps.glMultMatrixf;
/* 2340 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2341 */     BufferChecks.checkBuffer(m, 16);
/* 2342 */     nglMultMatrixf(MemoryUtil.getAddress(m), function_pointer);
/*      */   }
/*      */   static native void nglMultMatrixf(long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glMultMatrix(DoubleBuffer m) {
/* 2347 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2348 */     long function_pointer = caps.glMultMatrixd;
/* 2349 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2350 */     BufferChecks.checkBuffer(m, 16);
/* 2351 */     nglMultMatrixd(MemoryUtil.getAddress(m), function_pointer);
/*      */   }
/*      */   static native void nglMultMatrixd(long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glShadeModel(int mode) {
/* 2356 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2357 */     long function_pointer = caps.glShadeModel;
/* 2358 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2359 */     nglShadeModel(mode, function_pointer);
/*      */   }
/*      */   static native void nglShadeModel(int paramInt, long paramLong);
/*      */   
/*      */   public static void glSelectBuffer(IntBuffer buffer) {
/* 2364 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2365 */     long function_pointer = caps.glSelectBuffer;
/* 2366 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2367 */     BufferChecks.checkDirect(buffer);
/* 2368 */     nglSelectBuffer(buffer.remaining(), MemoryUtil.getAddress(buffer), function_pointer);
/*      */   }
/*      */   static native void nglSelectBuffer(int paramInt, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glScissor(int x, int y, int width, int height) {
/* 2373 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2374 */     long function_pointer = caps.glScissor;
/* 2375 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2376 */     nglScissor(x, y, width, height, function_pointer);
/*      */   }
/*      */   static native void nglScissor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glScalef(float x, float y, float z) {
/* 2381 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2382 */     long function_pointer = caps.glScalef;
/* 2383 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2384 */     nglScalef(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglScalef(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glScaled(double x, double y, double z) {
/* 2389 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2390 */     long function_pointer = caps.glScaled;
/* 2391 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2392 */     nglScaled(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglScaled(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glRotatef(float angle, float x, float y, float z) {
/* 2397 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2398 */     long function_pointer = caps.glRotatef;
/* 2399 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2400 */     nglRotatef(angle, x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglRotatef(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glRotated(double angle, double x, double y, double z) {
/* 2405 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2406 */     long function_pointer = caps.glRotated;
/* 2407 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2408 */     nglRotated(angle, x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglRotated(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static int glRenderMode(int mode) {
/* 2413 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2414 */     long function_pointer = caps.glRenderMode;
/* 2415 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2416 */     int __result = nglRenderMode(mode, function_pointer);
/* 2417 */     return __result;
/*      */   }
/*      */   static native int nglRenderMode(int paramInt, long paramLong);
/*      */   
/*      */   public static void glRectf(float x1, float y1, float x2, float y2) {
/* 2422 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2423 */     long function_pointer = caps.glRectf;
/* 2424 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2425 */     nglRectf(x1, y1, x2, y2, function_pointer);
/*      */   }
/*      */   static native void nglRectf(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glRectd(double x1, double y1, double x2, double y2) {
/* 2430 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2431 */     long function_pointer = caps.glRectd;
/* 2432 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2433 */     nglRectd(x1, y1, x2, y2, function_pointer);
/*      */   }
/*      */   static native void nglRectd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static void glRecti(int x1, int y1, int x2, int y2) {
/* 2438 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2439 */     long function_pointer = caps.glRecti;
/* 2440 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2441 */     nglRecti(x1, y1, x2, y2, function_pointer);
/*      */   }
/*      */   static native void nglRecti(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
/* 2446 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2447 */     long function_pointer = caps.glReadPixels;
/* 2448 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2449 */     GLChecks.ensurePackPBOdisabled(caps);
/* 2450 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2451 */     nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
/* 2454 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2455 */     long function_pointer = caps.glReadPixels;
/* 2456 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2457 */     GLChecks.ensurePackPBOdisabled(caps);
/* 2458 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2459 */     nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
/* 2462 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2463 */     long function_pointer = caps.glReadPixels;
/* 2464 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2465 */     GLChecks.ensurePackPBOdisabled(caps);
/* 2466 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2467 */     nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
/* 2470 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2471 */     long function_pointer = caps.glReadPixels;
/* 2472 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2473 */     GLChecks.ensurePackPBOdisabled(caps);
/* 2474 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2475 */     nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glReadPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
/* 2478 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2479 */     long function_pointer = caps.glReadPixels;
/* 2480 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2481 */     GLChecks.ensurePackPBOdisabled(caps);
/* 2482 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2483 */     nglReadPixels(x, y, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglReadPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   public static void glReadPixels(int x, int y, int width, int height, int format, int type, long pixels_buffer_offset) {
/* 2487 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2488 */     long function_pointer = caps.glReadPixels;
/* 2489 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2490 */     GLChecks.ensurePackPBOenabled(caps);
/* 2491 */     nglReadPixelsBO(x, y, width, height, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglReadPixelsBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glReadBuffer(int mode) {
/* 2496 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2497 */     long function_pointer = caps.glReadBuffer;
/* 2498 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2499 */     nglReadBuffer(mode, function_pointer);
/*      */   }
/*      */   static native void nglReadBuffer(int paramInt, long paramLong);
/*      */   
/*      */   public static void glRasterPos2f(float x, float y) {
/* 2504 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2505 */     long function_pointer = caps.glRasterPos2f;
/* 2506 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2507 */     nglRasterPos2f(x, y, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos2f(float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glRasterPos2d(double x, double y) {
/* 2512 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2513 */     long function_pointer = caps.glRasterPos2d;
/* 2514 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2515 */     nglRasterPos2d(x, y, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos2d(double paramDouble1, double paramDouble2, long paramLong);
/*      */   
/*      */   public static void glRasterPos2i(int x, int y) {
/* 2520 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2521 */     long function_pointer = caps.glRasterPos2i;
/* 2522 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2523 */     nglRasterPos2i(x, y, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos2i(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glRasterPos3f(float x, float y, float z) {
/* 2528 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2529 */     long function_pointer = caps.glRasterPos3f;
/* 2530 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2531 */     nglRasterPos3f(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glRasterPos3d(double x, double y, double z) {
/* 2536 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2537 */     long function_pointer = caps.glRasterPos3d;
/* 2538 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2539 */     nglRasterPos3d(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glRasterPos3i(int x, int y, int z) {
/* 2544 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2545 */     long function_pointer = caps.glRasterPos3i;
/* 2546 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2547 */     nglRasterPos3i(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glRasterPos4f(float x, float y, float z, float w) {
/* 2552 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2553 */     long function_pointer = caps.glRasterPos4f;
/* 2554 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2555 */     nglRasterPos4f(x, y, z, w, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glRasterPos4d(double x, double y, double z, double w) {
/* 2560 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2561 */     long function_pointer = caps.glRasterPos4d;
/* 2562 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2563 */     nglRasterPos4d(x, y, z, w, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static void glRasterPos4i(int x, int y, int z, int w) {
/* 2568 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2569 */     long function_pointer = caps.glRasterPos4i;
/* 2570 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2571 */     nglRasterPos4i(x, y, z, w, function_pointer);
/*      */   }
/*      */   static native void nglRasterPos4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glPushName(int name) {
/* 2576 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2577 */     long function_pointer = caps.glPushName;
/* 2578 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2579 */     nglPushName(name, function_pointer);
/*      */   }
/*      */   static native void nglPushName(int paramInt, long paramLong);
/*      */   
/*      */   public static void glPopName() {
/* 2584 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2585 */     long function_pointer = caps.glPopName;
/* 2586 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2587 */     nglPopName(function_pointer);
/*      */   }
/*      */   static native void nglPopName(long paramLong);
/*      */   
/*      */   public static void glPushMatrix() {
/* 2592 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2593 */     long function_pointer = caps.glPushMatrix;
/* 2594 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2595 */     nglPushMatrix(function_pointer);
/*      */   }
/*      */   static native void nglPushMatrix(long paramLong);
/*      */   
/*      */   public static void glPopMatrix() {
/* 2600 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2601 */     long function_pointer = caps.glPopMatrix;
/* 2602 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2603 */     nglPopMatrix(function_pointer);
/*      */   }
/*      */   static native void nglPopMatrix(long paramLong);
/*      */   
/*      */   public static void glPushClientAttrib(int mask) {
/* 2608 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2609 */     long function_pointer = caps.glPushClientAttrib;
/* 2610 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2611 */     StateTracker.pushAttrib(caps, mask);
/* 2612 */     nglPushClientAttrib(mask, function_pointer);
/*      */   }
/*      */   static native void nglPushClientAttrib(int paramInt, long paramLong);
/*      */   
/*      */   public static void glPopClientAttrib() {
/* 2617 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2618 */     long function_pointer = caps.glPopClientAttrib;
/* 2619 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2620 */     StateTracker.popAttrib(caps);
/* 2621 */     nglPopClientAttrib(function_pointer);
/*      */   }
/*      */   static native void nglPopClientAttrib(long paramLong);
/*      */   
/*      */   public static void glPushAttrib(int mask) {
/* 2626 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2627 */     long function_pointer = caps.glPushAttrib;
/* 2628 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2629 */     nglPushAttrib(mask, function_pointer);
/*      */   }
/*      */   static native void nglPushAttrib(int paramInt, long paramLong);
/*      */   
/*      */   public static void glPopAttrib() {
/* 2634 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2635 */     long function_pointer = caps.glPopAttrib;
/* 2636 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2637 */     nglPopAttrib(function_pointer);
/*      */   }
/*      */   static native void nglPopAttrib(long paramLong);
/*      */   
/*      */   public static void glStencilFunc(int func, int ref, int mask) {
/* 2642 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2643 */     long function_pointer = caps.glStencilFunc;
/* 2644 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2645 */     nglStencilFunc(func, ref, mask, function_pointer);
/*      */   }
/*      */   static native void nglStencilFunc(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glVertexPointer(int size, int stride, DoubleBuffer pointer) {
/* 2650 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2651 */     long function_pointer = caps.glVertexPointer;
/* 2652 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2653 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2654 */     BufferChecks.checkDirect(pointer);
/* 2655 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glVertexPointer_pointer = pointer; 
/* 2656 */     nglVertexPointer(size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glVertexPointer(int size, int stride, FloatBuffer pointer) {
/* 2659 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2660 */     long function_pointer = caps.glVertexPointer;
/* 2661 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2662 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2663 */     BufferChecks.checkDirect(pointer);
/* 2664 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glVertexPointer_pointer = pointer; 
/* 2665 */     nglVertexPointer(size, 5126, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glVertexPointer(int size, int stride, IntBuffer pointer) {
/* 2668 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2669 */     long function_pointer = caps.glVertexPointer;
/* 2670 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2671 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2672 */     BufferChecks.checkDirect(pointer);
/* 2673 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glVertexPointer_pointer = pointer; 
/* 2674 */     nglVertexPointer(size, 5124, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glVertexPointer(int size, int stride, ShortBuffer pointer) {
/* 2677 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2678 */     long function_pointer = caps.glVertexPointer;
/* 2679 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2680 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2681 */     BufferChecks.checkDirect(pointer);
/* 2682 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glVertexPointer_pointer = pointer; 
/* 2683 */     nglVertexPointer(size, 5122, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   static native void nglVertexPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glVertexPointer(int size, int type, int stride, long pointer_buffer_offset) {
/* 2687 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2688 */     long function_pointer = caps.glVertexPointer;
/* 2689 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2690 */     GLChecks.ensureArrayVBOenabled(caps);
/* 2691 */     nglVertexPointerBO(size, type, stride, pointer_buffer_offset, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglVertexPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glVertexPointer(int size, int type, int stride, ByteBuffer pointer) {
/* 2697 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2698 */     long function_pointer = caps.glVertexPointer;
/* 2699 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2700 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 2701 */     BufferChecks.checkDirect(pointer);
/* 2702 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).GL11_glVertexPointer_pointer = pointer; 
/* 2703 */     nglVertexPointer(size, type, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glVertex2f(float x, float y) {
/* 2707 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2708 */     long function_pointer = caps.glVertex2f;
/* 2709 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2710 */     nglVertex2f(x, y, function_pointer);
/*      */   }
/*      */   static native void nglVertex2f(float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glVertex2d(double x, double y) {
/* 2715 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2716 */     long function_pointer = caps.glVertex2d;
/* 2717 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2718 */     nglVertex2d(x, y, function_pointer);
/*      */   }
/*      */   static native void nglVertex2d(double paramDouble1, double paramDouble2, long paramLong);
/*      */   
/*      */   public static void glVertex2i(int x, int y) {
/* 2723 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2724 */     long function_pointer = caps.glVertex2i;
/* 2725 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2726 */     nglVertex2i(x, y, function_pointer);
/*      */   }
/*      */   static native void nglVertex2i(int paramInt1, int paramInt2, long paramLong);
/*      */   
/*      */   public static void glVertex3f(float x, float y, float z) {
/* 2731 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2732 */     long function_pointer = caps.glVertex3f;
/* 2733 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2734 */     nglVertex3f(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglVertex3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glVertex3d(double x, double y, double z) {
/* 2739 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2740 */     long function_pointer = caps.glVertex3d;
/* 2741 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2742 */     nglVertex3d(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglVertex3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glVertex3i(int x, int y, int z) {
/* 2747 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2748 */     long function_pointer = caps.glVertex3i;
/* 2749 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2750 */     nglVertex3i(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglVertex3i(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glVertex4f(float x, float y, float z, float w) {
/* 2755 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2756 */     long function_pointer = caps.glVertex4f;
/* 2757 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2758 */     nglVertex4f(x, y, z, w, function_pointer);
/*      */   }
/*      */   static native void nglVertex4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glVertex4d(double x, double y, double z, double w) {
/* 2763 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2764 */     long function_pointer = caps.glVertex4d;
/* 2765 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2766 */     nglVertex4d(x, y, z, w, function_pointer);
/*      */   }
/*      */   static native void nglVertex4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static void glVertex4i(int x, int y, int z, int w) {
/* 2771 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2772 */     long function_pointer = caps.glVertex4i;
/* 2773 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2774 */     nglVertex4i(x, y, z, w, function_pointer);
/*      */   }
/*      */   static native void nglVertex4i(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */   
/*      */   public static void glTranslatef(float x, float y, float z) {
/* 2779 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2780 */     long function_pointer = caps.glTranslatef;
/* 2781 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2782 */     nglTranslatef(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglTranslatef(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glTranslated(double x, double y, double z) {
/* 2787 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2788 */     long function_pointer = caps.glTranslated;
/* 2789 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2790 */     nglTranslated(x, y, z, function_pointer);
/*      */   }
/*      */   static native void nglTranslated(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, ByteBuffer pixels) {
/* 2795 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2796 */     long function_pointer = caps.glTexImage1D;
/* 2797 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2798 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2799 */     if (pixels != null)
/* 2800 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width)); 
/* 2801 */     nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, DoubleBuffer pixels) {
/* 2804 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2805 */     long function_pointer = caps.glTexImage1D;
/* 2806 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2807 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2808 */     if (pixels != null)
/* 2809 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width)); 
/* 2810 */     nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, FloatBuffer pixels) {
/* 2813 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2814 */     long function_pointer = caps.glTexImage1D;
/* 2815 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2816 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2817 */     if (pixels != null)
/* 2818 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width)); 
/* 2819 */     nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, IntBuffer pixels) {
/* 2822 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2823 */     long function_pointer = caps.glTexImage1D;
/* 2824 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2825 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2826 */     if (pixels != null)
/* 2827 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width)); 
/* 2828 */     nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, ShortBuffer pixels) {
/* 2831 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2832 */     long function_pointer = caps.glTexImage1D;
/* 2833 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2834 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2835 */     if (pixels != null)
/* 2836 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage1DStorage(pixels, format, type, width)); 
/* 2837 */     nglTexImage1D(target, level, internalformat, width, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   static native void nglTexImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*      */   public static void glTexImage1D(int target, int level, int internalformat, int width, int border, int format, int type, long pixels_buffer_offset) {
/* 2841 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2842 */     long function_pointer = caps.glTexImage1D;
/* 2843 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2844 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2845 */     nglTexImage1DBO(target, level, internalformat, width, border, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTexImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
/* 2850 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2851 */     long function_pointer = caps.glTexImage2D;
/* 2852 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2853 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2854 */     if (pixels != null)
/* 2855 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)); 
/* 2856 */     nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, DoubleBuffer pixels) {
/* 2859 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2860 */     long function_pointer = caps.glTexImage2D;
/* 2861 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2862 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2863 */     if (pixels != null)
/* 2864 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)); 
/* 2865 */     nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
/* 2868 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2869 */     long function_pointer = caps.glTexImage2D;
/* 2870 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2871 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2872 */     if (pixels != null)
/* 2873 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)); 
/* 2874 */     nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
/* 2877 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2878 */     long function_pointer = caps.glTexImage2D;
/* 2879 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2880 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2881 */     if (pixels != null)
/* 2882 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)); 
/* 2883 */     nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ShortBuffer pixels) {
/* 2886 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2887 */     long function_pointer = caps.glTexImage2D;
/* 2888 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2889 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2890 */     if (pixels != null)
/* 2891 */       BufferChecks.checkBuffer(pixels, GLChecks.calculateTexImage2DStorage(pixels, format, type, width, height)); 
/* 2892 */     nglTexImage2D(target, level, internalformat, width, height, border, format, type, MemoryUtil.getAddressSafe(pixels), function_pointer);
/*      */   }
/*      */   static native void nglTexImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, long pixels_buffer_offset) {
/* 2896 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2897 */     long function_pointer = caps.glTexImage2D;
/* 2898 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2899 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2900 */     nglTexImage2DBO(target, level, internalformat, width, height, border, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTexImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
/* 2905 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2906 */     long function_pointer = caps.glTexSubImage1D;
/* 2907 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2908 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2909 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/* 2910 */     nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
/* 2913 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2914 */     long function_pointer = caps.glTexSubImage1D;
/* 2915 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2916 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2917 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/* 2918 */     nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
/* 2921 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2922 */     long function_pointer = caps.glTexSubImage1D;
/* 2923 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2924 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2925 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/* 2926 */     nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
/* 2929 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2930 */     long function_pointer = caps.glTexSubImage1D;
/* 2931 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2932 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2933 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/* 2934 */     nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
/* 2937 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2938 */     long function_pointer = caps.glTexSubImage1D;
/* 2939 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2940 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2941 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
/* 2942 */     nglTexSubImage1D(target, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglTexSubImage1D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   public static void glTexSubImage1D(int target, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
/* 2946 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2947 */     long function_pointer = caps.glTexSubImage1D;
/* 2948 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2949 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 2950 */     nglTexSubImage1DBO(target, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTexSubImage1DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
/* 2955 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2956 */     long function_pointer = caps.glTexSubImage2D;
/* 2957 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2958 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2959 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2960 */     nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
/* 2963 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2964 */     long function_pointer = caps.glTexSubImage2D;
/* 2965 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2966 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2967 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2968 */     nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
/* 2971 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2972 */     long function_pointer = caps.glTexSubImage2D;
/* 2973 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2974 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2975 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2976 */     nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
/* 2979 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2980 */     long function_pointer = caps.glTexSubImage2D;
/* 2981 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2982 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2983 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2984 */     nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
/* 2987 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2988 */     long function_pointer = caps.glTexSubImage2D;
/* 2989 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2990 */     GLChecks.ensureUnpackPBOdisabled(caps);
/* 2991 */     BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
/* 2992 */     nglTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
/*      */   }
/*      */   static native void nglTexSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
/* 2996 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 2997 */     long function_pointer = caps.glTexSubImage2D;
/* 2998 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 2999 */     GLChecks.ensureUnpackPBOenabled(caps);
/* 3000 */     nglTexSubImage2DBO(target, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
/*      */   }
/*      */   static native void nglTexSubImage2DBO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexParameterf(int target, int pname, float param) {
/* 3005 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3006 */     long function_pointer = caps.glTexParameterf;
/* 3007 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3008 */     nglTexParameterf(target, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexParameterf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glTexParameteri(int target, int pname, int param) {
/* 3013 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3014 */     long function_pointer = caps.glTexParameteri;
/* 3015 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3016 */     nglTexParameteri(target, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexParameteri(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTexParameter(int target, int pname, FloatBuffer param) {
/* 3021 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3022 */     long function_pointer = caps.glTexParameterfv;
/* 3023 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3024 */     BufferChecks.checkBuffer(param, 4);
/* 3025 */     nglTexParameterfv(target, pname, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   static native void nglTexParameterfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexParameter(int target, int pname, IntBuffer param) {
/* 3030 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3031 */     long function_pointer = caps.glTexParameteriv;
/* 3032 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3033 */     BufferChecks.checkBuffer(param, 4);
/* 3034 */     nglTexParameteriv(target, pname, MemoryUtil.getAddress(param), function_pointer);
/*      */   }
/*      */   static native void nglTexParameteriv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexGenf(int coord, int pname, float param) {
/* 3039 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3040 */     long function_pointer = caps.glTexGenf;
/* 3041 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3042 */     nglTexGenf(coord, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexGenf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glTexGend(int coord, int pname, double param) {
/* 3047 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3048 */     long function_pointer = caps.glTexGend;
/* 3049 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3050 */     nglTexGend(coord, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexGend(int paramInt1, int paramInt2, double paramDouble, long paramLong);
/*      */   
/*      */   public static void glTexGen(int coord, int pname, FloatBuffer params) {
/* 3055 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3056 */     long function_pointer = caps.glTexGenfv;
/* 3057 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3058 */     BufferChecks.checkBuffer(params, 4);
/* 3059 */     nglTexGenfv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTexGenfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexGen(int coord, int pname, DoubleBuffer params) {
/* 3064 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3065 */     long function_pointer = caps.glTexGendv;
/* 3066 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3067 */     BufferChecks.checkBuffer(params, 4);
/* 3068 */     nglTexGendv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTexGendv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexGeni(int coord, int pname, int param) {
/* 3073 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3074 */     long function_pointer = caps.glTexGeni;
/* 3075 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3076 */     nglTexGeni(coord, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexGeni(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTexGen(int coord, int pname, IntBuffer params) {
/* 3081 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3082 */     long function_pointer = caps.glTexGeniv;
/* 3083 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3084 */     BufferChecks.checkBuffer(params, 4);
/* 3085 */     nglTexGeniv(coord, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTexGeniv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexEnvf(int target, int pname, float param) {
/* 3090 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3091 */     long function_pointer = caps.glTexEnvf;
/* 3092 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3093 */     nglTexEnvf(target, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexEnvf(int paramInt1, int paramInt2, float paramFloat, long paramLong);
/*      */   
/*      */   public static void glTexEnvi(int target, int pname, int param) {
/* 3098 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3099 */     long function_pointer = caps.glTexEnvi;
/* 3100 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3101 */     nglTexEnvi(target, pname, param, function_pointer);
/*      */   }
/*      */   static native void nglTexEnvi(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glTexEnv(int target, int pname, FloatBuffer params) {
/* 3106 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3107 */     long function_pointer = caps.glTexEnvfv;
/* 3108 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3109 */     BufferChecks.checkBuffer(params, 4);
/* 3110 */     nglTexEnvfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTexEnvfv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexEnv(int target, int pname, IntBuffer params) {
/* 3115 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3116 */     long function_pointer = caps.glTexEnviv;
/* 3117 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3118 */     BufferChecks.checkBuffer(params, 4);
/* 3119 */     nglTexEnviv(target, pname, MemoryUtil.getAddress(params), function_pointer);
/*      */   }
/*      */   static native void nglTexEnviv(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexCoordPointer(int size, int stride, DoubleBuffer pointer) {
/* 3124 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3125 */     long function_pointer = caps.glTexCoordPointer;
/* 3126 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3127 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 3128 */     BufferChecks.checkDirect(pointer);
/* 3129 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).glTexCoordPointer_buffer[(StateTracker.getReferences(caps)).glClientActiveTexture] = pointer; 
/* 3130 */     nglTexCoordPointer(size, 5130, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glTexCoordPointer(int size, int stride, FloatBuffer pointer) {
/* 3133 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3134 */     long function_pointer = caps.glTexCoordPointer;
/* 3135 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3136 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 3137 */     BufferChecks.checkDirect(pointer);
/* 3138 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).glTexCoordPointer_buffer[(StateTracker.getReferences(caps)).glClientActiveTexture] = pointer; 
/* 3139 */     nglTexCoordPointer(size, 5126, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glTexCoordPointer(int size, int stride, IntBuffer pointer) {
/* 3142 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3143 */     long function_pointer = caps.glTexCoordPointer;
/* 3144 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3145 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 3146 */     BufferChecks.checkDirect(pointer);
/* 3147 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).glTexCoordPointer_buffer[(StateTracker.getReferences(caps)).glClientActiveTexture] = pointer; 
/* 3148 */     nglTexCoordPointer(size, 5124, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   public static void glTexCoordPointer(int size, int stride, ShortBuffer pointer) {
/* 3151 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3152 */     long function_pointer = caps.glTexCoordPointer;
/* 3153 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3154 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 3155 */     BufferChecks.checkDirect(pointer);
/* 3156 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).glTexCoordPointer_buffer[(StateTracker.getReferences(caps)).glClientActiveTexture] = pointer; 
/* 3157 */     nglTexCoordPointer(size, 5122, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   static native void nglTexCoordPointer(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   public static void glTexCoordPointer(int size, int type, int stride, long pointer_buffer_offset) {
/* 3161 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3162 */     long function_pointer = caps.glTexCoordPointer;
/* 3163 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3164 */     GLChecks.ensureArrayVBOenabled(caps);
/* 3165 */     nglTexCoordPointerBO(size, type, stride, pointer_buffer_offset, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglTexCoordPointerBO(int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2);
/*      */   
/*      */   public static void glTexCoordPointer(int size, int type, int stride, ByteBuffer pointer) {
/* 3171 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3172 */     long function_pointer = caps.glTexCoordPointer;
/* 3173 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3174 */     GLChecks.ensureArrayVBOdisabled(caps);
/* 3175 */     BufferChecks.checkDirect(pointer);
/* 3176 */     if (LWJGLUtil.CHECKS) (StateTracker.getReferences(caps)).glTexCoordPointer_buffer[(StateTracker.getReferences(caps)).glClientActiveTexture] = pointer; 
/* 3177 */     nglTexCoordPointer(size, type, stride, MemoryUtil.getAddress(pointer), function_pointer);
/*      */   }
/*      */   
/*      */   public static void glTexCoord1f(float s) {
/* 3181 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3182 */     long function_pointer = caps.glTexCoord1f;
/* 3183 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3184 */     nglTexCoord1f(s, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord1f(float paramFloat, long paramLong);
/*      */   
/*      */   public static void glTexCoord1d(double s) {
/* 3189 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3190 */     long function_pointer = caps.glTexCoord1d;
/* 3191 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3192 */     nglTexCoord1d(s, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord1d(double paramDouble, long paramLong);
/*      */   
/*      */   public static void glTexCoord2f(float s, float t) {
/* 3197 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3198 */     long function_pointer = caps.glTexCoord2f;
/* 3199 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3200 */     nglTexCoord2f(s, t, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord2f(float paramFloat1, float paramFloat2, long paramLong);
/*      */   
/*      */   public static void glTexCoord2d(double s, double t) {
/* 3205 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3206 */     long function_pointer = caps.glTexCoord2d;
/* 3207 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3208 */     nglTexCoord2d(s, t, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord2d(double paramDouble1, double paramDouble2, long paramLong);
/*      */   
/*      */   public static void glTexCoord3f(float s, float t, float r) {
/* 3213 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3214 */     long function_pointer = caps.glTexCoord3f;
/* 3215 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3216 */     nglTexCoord3f(s, t, r, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord3f(float paramFloat1, float paramFloat2, float paramFloat3, long paramLong);
/*      */   
/*      */   public static void glTexCoord3d(double s, double t, double r) {
/* 3221 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3222 */     long function_pointer = caps.glTexCoord3d;
/* 3223 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3224 */     nglTexCoord3d(s, t, r, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord3d(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong);
/*      */   
/*      */   public static void glTexCoord4f(float s, float t, float r, float q) {
/* 3229 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3230 */     long function_pointer = caps.glTexCoord4f;
/* 3231 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3232 */     nglTexCoord4f(s, t, r, q, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord4f(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
/*      */   
/*      */   public static void glTexCoord4d(double s, double t, double r, double q) {
/* 3237 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3238 */     long function_pointer = caps.glTexCoord4d;
/* 3239 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3240 */     nglTexCoord4d(s, t, r, q, function_pointer);
/*      */   }
/*      */   static native void nglTexCoord4d(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, long paramLong);
/*      */   
/*      */   public static void glStencilOp(int fail, int zfail, int zpass) {
/* 3245 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3246 */     long function_pointer = caps.glStencilOp;
/* 3247 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3248 */     nglStencilOp(fail, zfail, zpass, function_pointer);
/*      */   }
/*      */   static native void nglStencilOp(int paramInt1, int paramInt2, int paramInt3, long paramLong);
/*      */   
/*      */   public static void glStencilMask(int mask) {
/* 3253 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3254 */     long function_pointer = caps.glStencilMask;
/* 3255 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3256 */     nglStencilMask(mask, function_pointer);
/*      */   }
/*      */   static native void nglStencilMask(int paramInt, long paramLong);
/*      */   
/*      */   public static void glViewport(int x, int y, int width, int height) {
/* 3261 */     ContextCapabilities caps = GLContext.getCapabilities();
/* 3262 */     long function_pointer = caps.glViewport;
/* 3263 */     BufferChecks.checkFunctionAddress(function_pointer);
/* 3264 */     nglViewport(x, y, width, height, function_pointer);
/*      */   }
/*      */   
/*      */   static native void nglViewport(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong);
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\GL11.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */