/*     */ package org.lwjgl.opencl;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CLCapabilities
/*     */ {
/*     */   static final boolean CL_APPLE_ContextLoggingFunctions;
/*   8 */   static final long clLogMessagesToSystemLogAPPLE = CL.getFunctionAddress("clLogMessagesToSystemLogAPPLE");
/*   9 */   static final long clLogMessagesToStdoutAPPLE = CL.getFunctionAddress("clLogMessagesToStdoutAPPLE");
/*  10 */   static final long clLogMessagesToStderrAPPLE = CL.getFunctionAddress("clLogMessagesToStderrAPPLE");
/*     */   
/*     */   static final boolean CL_APPLE_SetMemObjectDestructor;
/*  13 */   static final long clSetMemObjectDestructorAPPLE = CL.getFunctionAddress("clSetMemObjectDestructorAPPLE");
/*     */   
/*     */   static final boolean CL_APPLE_gl_sharing;
/*  16 */   static final long clGetGLContextInfoAPPLE = CL.getFunctionAddress("clGetGLContextInfoAPPLE");
/*     */   
/*     */   static final boolean OpenCL10;
/*  19 */   static final long clGetPlatformIDs = CL.getFunctionAddress("clGetPlatformIDs");
/*  20 */   static final long clGetPlatformInfo = CL.getFunctionAddress("clGetPlatformInfo");
/*  21 */   static final long clGetDeviceIDs = CL.getFunctionAddress("clGetDeviceIDs");
/*  22 */   static final long clGetDeviceInfo = CL.getFunctionAddress("clGetDeviceInfo");
/*  23 */   static final long clCreateContext = CL.getFunctionAddress("clCreateContext");
/*  24 */   static final long clCreateContextFromType = CL.getFunctionAddress("clCreateContextFromType");
/*  25 */   static final long clRetainContext = CL.getFunctionAddress("clRetainContext");
/*  26 */   static final long clReleaseContext = CL.getFunctionAddress("clReleaseContext");
/*  27 */   static final long clGetContextInfo = CL.getFunctionAddress("clGetContextInfo");
/*  28 */   static final long clCreateCommandQueue = CL.getFunctionAddress("clCreateCommandQueue");
/*  29 */   static final long clRetainCommandQueue = CL.getFunctionAddress("clRetainCommandQueue");
/*  30 */   static final long clReleaseCommandQueue = CL.getFunctionAddress("clReleaseCommandQueue");
/*  31 */   static final long clGetCommandQueueInfo = CL.getFunctionAddress("clGetCommandQueueInfo");
/*  32 */   static final long clCreateBuffer = CL.getFunctionAddress("clCreateBuffer");
/*  33 */   static final long clEnqueueReadBuffer = CL.getFunctionAddress("clEnqueueReadBuffer");
/*  34 */   static final long clEnqueueWriteBuffer = CL.getFunctionAddress("clEnqueueWriteBuffer");
/*  35 */   static final long clEnqueueCopyBuffer = CL.getFunctionAddress("clEnqueueCopyBuffer");
/*  36 */   static final long clEnqueueMapBuffer = CL.getFunctionAddress("clEnqueueMapBuffer");
/*  37 */   static final long clCreateImage2D = CL.getFunctionAddress("clCreateImage2D");
/*  38 */   static final long clCreateImage3D = CL.getFunctionAddress("clCreateImage3D");
/*  39 */   static final long clGetSupportedImageFormats = CL.getFunctionAddress("clGetSupportedImageFormats");
/*  40 */   static final long clEnqueueReadImage = CL.getFunctionAddress("clEnqueueReadImage");
/*  41 */   static final long clEnqueueWriteImage = CL.getFunctionAddress("clEnqueueWriteImage");
/*  42 */   static final long clEnqueueCopyImage = CL.getFunctionAddress("clEnqueueCopyImage");
/*  43 */   static final long clEnqueueCopyImageToBuffer = CL.getFunctionAddress("clEnqueueCopyImageToBuffer");
/*  44 */   static final long clEnqueueCopyBufferToImage = CL.getFunctionAddress("clEnqueueCopyBufferToImage");
/*  45 */   static final long clEnqueueMapImage = CL.getFunctionAddress("clEnqueueMapImage");
/*  46 */   static final long clGetImageInfo = CL.getFunctionAddress("clGetImageInfo");
/*  47 */   static final long clRetainMemObject = CL.getFunctionAddress("clRetainMemObject");
/*  48 */   static final long clReleaseMemObject = CL.getFunctionAddress("clReleaseMemObject");
/*  49 */   static final long clEnqueueUnmapMemObject = CL.getFunctionAddress("clEnqueueUnmapMemObject");
/*  50 */   static final long clGetMemObjectInfo = CL.getFunctionAddress("clGetMemObjectInfo");
/*  51 */   static final long clCreateSampler = CL.getFunctionAddress("clCreateSampler");
/*  52 */   static final long clRetainSampler = CL.getFunctionAddress("clRetainSampler");
/*  53 */   static final long clReleaseSampler = CL.getFunctionAddress("clReleaseSampler");
/*  54 */   static final long clGetSamplerInfo = CL.getFunctionAddress("clGetSamplerInfo");
/*  55 */   static final long clCreateProgramWithSource = CL.getFunctionAddress("clCreateProgramWithSource");
/*  56 */   static final long clCreateProgramWithBinary = CL.getFunctionAddress("clCreateProgramWithBinary");
/*  57 */   static final long clRetainProgram = CL.getFunctionAddress("clRetainProgram");
/*  58 */   static final long clReleaseProgram = CL.getFunctionAddress("clReleaseProgram");
/*  59 */   static final long clBuildProgram = CL.getFunctionAddress("clBuildProgram");
/*  60 */   static final long clUnloadCompiler = CL.getFunctionAddress("clUnloadCompiler");
/*  61 */   static final long clGetProgramInfo = CL.getFunctionAddress("clGetProgramInfo");
/*  62 */   static final long clGetProgramBuildInfo = CL.getFunctionAddress("clGetProgramBuildInfo");
/*  63 */   static final long clCreateKernel = CL.getFunctionAddress("clCreateKernel");
/*  64 */   static final long clCreateKernelsInProgram = CL.getFunctionAddress("clCreateKernelsInProgram");
/*  65 */   static final long clRetainKernel = CL.getFunctionAddress("clRetainKernel");
/*  66 */   static final long clReleaseKernel = CL.getFunctionAddress("clReleaseKernel");
/*  67 */   static final long clSetKernelArg = CL.getFunctionAddress("clSetKernelArg");
/*  68 */   static final long clGetKernelInfo = CL.getFunctionAddress("clGetKernelInfo");
/*  69 */   static final long clGetKernelWorkGroupInfo = CL.getFunctionAddress("clGetKernelWorkGroupInfo");
/*  70 */   static final long clEnqueueNDRangeKernel = CL.getFunctionAddress("clEnqueueNDRangeKernel");
/*  71 */   static final long clEnqueueTask = CL.getFunctionAddress("clEnqueueTask");
/*  72 */   static final long clEnqueueNativeKernel = CL.getFunctionAddress("clEnqueueNativeKernel");
/*  73 */   static final long clWaitForEvents = CL.getFunctionAddress("clWaitForEvents");
/*  74 */   static final long clGetEventInfo = CL.getFunctionAddress("clGetEventInfo");
/*  75 */   static final long clRetainEvent = CL.getFunctionAddress("clRetainEvent");
/*  76 */   static final long clReleaseEvent = CL.getFunctionAddress("clReleaseEvent");
/*  77 */   static final long clEnqueueMarker = CL.getFunctionAddress("clEnqueueMarker");
/*  78 */   static final long clEnqueueBarrier = CL.getFunctionAddress("clEnqueueBarrier");
/*  79 */   static final long clEnqueueWaitForEvents = CL.getFunctionAddress("clEnqueueWaitForEvents");
/*  80 */   static final long clGetEventProfilingInfo = CL.getFunctionAddress("clGetEventProfilingInfo");
/*  81 */   static final long clFlush = CL.getFunctionAddress("clFlush");
/*  82 */   static final long clFinish = CL.getFunctionAddress("clFinish");
/*  83 */   static final long clGetExtensionFunctionAddress = CL.getFunctionAddress("clGetExtensionFunctionAddress");
/*     */   
/*     */   static final boolean OpenCL10GL;
/*  86 */   static final long clCreateFromGLBuffer = CL.getFunctionAddress("clCreateFromGLBuffer");
/*  87 */   static final long clCreateFromGLTexture2D = CL.getFunctionAddress("clCreateFromGLTexture2D");
/*  88 */   static final long clCreateFromGLTexture3D = CL.getFunctionAddress("clCreateFromGLTexture3D");
/*  89 */   static final long clCreateFromGLRenderbuffer = CL.getFunctionAddress("clCreateFromGLRenderbuffer");
/*  90 */   static final long clGetGLObjectInfo = CL.getFunctionAddress("clGetGLObjectInfo");
/*  91 */   static final long clGetGLTextureInfo = CL.getFunctionAddress("clGetGLTextureInfo");
/*  92 */   static final long clEnqueueAcquireGLObjects = CL.getFunctionAddress("clEnqueueAcquireGLObjects");
/*  93 */   static final long clEnqueueReleaseGLObjects = CL.getFunctionAddress("clEnqueueReleaseGLObjects");
/*     */   
/*     */   static final boolean OpenCL11;
/*  96 */   static final long clCreateSubBuffer = CL.getFunctionAddress("clCreateSubBuffer");
/*  97 */   static final long clSetMemObjectDestructorCallback = CL.getFunctionAddress("clSetMemObjectDestructorCallback");
/*  98 */   static final long clEnqueueReadBufferRect = CL.getFunctionAddress("clEnqueueReadBufferRect");
/*  99 */   static final long clEnqueueWriteBufferRect = CL.getFunctionAddress("clEnqueueWriteBufferRect");
/* 100 */   static final long clEnqueueCopyBufferRect = CL.getFunctionAddress("clEnqueueCopyBufferRect");
/* 101 */   static final long clCreateUserEvent = CL.getFunctionAddress("clCreateUserEvent");
/* 102 */   static final long clSetUserEventStatus = CL.getFunctionAddress("clSetUserEventStatus");
/* 103 */   static final long clSetEventCallback = CL.getFunctionAddress("clSetEventCallback");
/*     */   
/*     */   static final boolean OpenCL12;
/* 106 */   static final long clRetainDevice = CL.getFunctionAddress("clRetainDevice");
/* 107 */   static final long clReleaseDevice = CL.getFunctionAddress("clReleaseDevice");
/* 108 */   static final long clCreateSubDevices = CL.getFunctionAddress("clCreateSubDevices");
/* 109 */   static final long clCreateImage = CL.getFunctionAddress("clCreateImage");
/* 110 */   static final long clCreateProgramWithBuiltInKernels = CL.getFunctionAddress("clCreateProgramWithBuiltInKernels");
/* 111 */   static final long clCompileProgram = CL.getFunctionAddress("clCompileProgram");
/* 112 */   static final long clLinkProgram = CL.getFunctionAddress("clLinkProgram");
/* 113 */   static final long clUnloadPlatformCompiler = CL.getFunctionAddress("clUnloadPlatformCompiler");
/* 114 */   static final long clGetKernelArgInfo = CL.getFunctionAddress("clGetKernelArgInfo");
/* 115 */   static final long clEnqueueFillBuffer = CL.getFunctionAddress("clEnqueueFillBuffer");
/* 116 */   static final long clEnqueueFillImage = CL.getFunctionAddress("clEnqueueFillImage");
/* 117 */   static final long clEnqueueMigrateMemObjects = CL.getFunctionAddress("clEnqueueMigrateMemObjects");
/* 118 */   static final long clEnqueueMarkerWithWaitList = CL.getFunctionAddress("clEnqueueMarkerWithWaitList");
/* 119 */   static final long clEnqueueBarrierWithWaitList = CL.getFunctionAddress("clEnqueueBarrierWithWaitList");
/* 120 */   static final long clSetPrintfCallback = CL.getFunctionAddress("clSetPrintfCallback");
/* 121 */   static final long clGetExtensionFunctionAddressForPlatform = CL.getFunctionAddress("clGetExtensionFunctionAddressForPlatform");
/*     */   
/*     */   static final boolean OpenCL12GL;
/* 124 */   static final long clCreateFromGLTexture = CL.getFunctionAddress("clCreateFromGLTexture");
/*     */   
/*     */   static final boolean CL_EXT_device_fission;
/* 127 */   static final long clRetainDeviceEXT = CL.getFunctionAddress("clRetainDeviceEXT");
/* 128 */   static final long clReleaseDeviceEXT = CL.getFunctionAddress("clReleaseDeviceEXT");
/* 129 */   static final long clCreateSubDevicesEXT = CL.getFunctionAddress("clCreateSubDevicesEXT");
/*     */   
/*     */   static final boolean CL_EXT_migrate_memobject;
/* 132 */   static final long clEnqueueMigrateMemObjectEXT = CL.getFunctionAddress("clEnqueueMigrateMemObjectEXT");
/*     */   
/*     */   static final boolean CL_KHR_gl_event;
/* 135 */   static final long clCreateEventFromGLsyncKHR = CL.getFunctionAddress("clCreateEventFromGLsyncKHR");
/*     */   
/*     */   static final boolean CL_KHR_gl_sharing;
/* 138 */   static final long clGetGLContextInfoKHR = CL.getFunctionAddress("clGetGLContextInfoKHR");
/*     */   
/*     */   static final boolean CL_KHR_icd;
/* 141 */   static final long clIcdGetPlatformIDsKHR = CL.getFunctionAddress("clIcdGetPlatformIDsKHR");
/*     */   
/*     */   static final boolean CL_KHR_subgroups;
/* 144 */   static final long clGetKernelSubGroupInfoKHR = CL.getFunctionAddress("clGetKernelSubGroupInfoKHR");
/*     */   
/*     */   static final boolean CL_KHR_terminate_context;
/* 147 */   static final long clTerminateContextKHR = CL.getFunctionAddress("clTerminateContextKHR");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 153 */     CL_APPLE_ContextLoggingFunctions = isAPPLE_ContextLoggingFunctionsSupported();
/* 154 */     CL_APPLE_SetMemObjectDestructor = isAPPLE_SetMemObjectDestructorSupported();
/* 155 */     CL_APPLE_gl_sharing = isAPPLE_gl_sharingSupported();
/* 156 */     OpenCL10 = isCL10Supported();
/* 157 */     OpenCL10GL = isCL10GLSupported();
/* 158 */     OpenCL11 = isCL11Supported();
/* 159 */     OpenCL12 = isCL12Supported();
/* 160 */     OpenCL12GL = isCL12GLSupported();
/* 161 */     CL_EXT_device_fission = isEXT_device_fissionSupported();
/* 162 */     CL_EXT_migrate_memobject = isEXT_migrate_memobjectSupported();
/* 163 */     CL_KHR_gl_event = isKHR_gl_eventSupported();
/* 164 */     CL_KHR_gl_sharing = isKHR_gl_sharingSupported();
/* 165 */     CL_KHR_icd = isKHR_icdSupported();
/* 166 */     CL_KHR_subgroups = isKHR_subgroupsSupported();
/* 167 */     CL_KHR_terminate_context = isKHR_terminate_contextSupported();
/*     */   }
/*     */   
/*     */   public static CLPlatformCapabilities getPlatformCapabilities(CLPlatform platform) {
/* 171 */     platform.checkValid();
/*     */     
/* 173 */     CLPlatformCapabilities caps = (CLPlatformCapabilities)platform.getCapabilities();
/* 174 */     if (caps == null) {
/* 175 */       platform.setCapabilities(caps = new CLPlatformCapabilities(platform));
/*     */     }
/* 177 */     return caps;
/*     */   }
/*     */   
/*     */   public static CLDeviceCapabilities getDeviceCapabilities(CLDevice device) {
/* 181 */     device.checkValid();
/*     */     
/* 183 */     CLDeviceCapabilities caps = (CLDeviceCapabilities)device.getCapabilities();
/* 184 */     if (caps == null) {
/* 185 */       device.setCapabilities(caps = new CLDeviceCapabilities(device));
/*     */     }
/* 187 */     return caps;
/*     */   }
/*     */   
/*     */   private static boolean isAPPLE_ContextLoggingFunctionsSupported() {
/* 191 */     return ((clLogMessagesToSystemLogAPPLE != 0L)) & ((clLogMessagesToStdoutAPPLE != 0L)) & ((clLogMessagesToStderrAPPLE != 0L) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAPPLE_SetMemObjectDestructorSupported() {
/* 198 */     return (clSetMemObjectDestructorAPPLE != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isAPPLE_gl_sharingSupported() {
/* 203 */     return (clGetGLContextInfoAPPLE != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isCL10Supported() {
/* 208 */     return ((clGetPlatformIDs != 0L)) & ((clGetPlatformInfo != 0L)) & ((clGetDeviceIDs != 0L) ? 1 : 0) & ((clGetDeviceInfo != 0L) ? 1 : 0) & ((clCreateContext != 0L) ? 1 : 0) & ((clCreateContextFromType != 0L) ? 1 : 0) & ((clRetainContext != 0L) ? 1 : 0) & ((clReleaseContext != 0L) ? 1 : 0) & ((clGetContextInfo != 0L) ? 1 : 0) & ((clCreateCommandQueue != 0L) ? 1 : 0) & ((clRetainCommandQueue != 0L) ? 1 : 0) & ((clReleaseCommandQueue != 0L) ? 1 : 0) & ((clGetCommandQueueInfo != 0L) ? 1 : 0) & ((clCreateBuffer != 0L) ? 1 : 0) & ((clEnqueueReadBuffer != 0L) ? 1 : 0) & ((clEnqueueWriteBuffer != 0L) ? 1 : 0) & ((clEnqueueCopyBuffer != 0L) ? 1 : 0) & ((clEnqueueMapBuffer != 0L) ? 1 : 0) & ((clCreateImage2D != 0L) ? 1 : 0) & ((clCreateImage3D != 0L) ? 1 : 0) & ((clGetSupportedImageFormats != 0L) ? 1 : 0) & ((clEnqueueReadImage != 0L) ? 1 : 0) & ((clEnqueueWriteImage != 0L) ? 1 : 0) & ((clEnqueueCopyImage != 0L) ? 1 : 0) & ((clEnqueueCopyImageToBuffer != 0L) ? 1 : 0) & ((clEnqueueCopyBufferToImage != 0L) ? 1 : 0) & ((clEnqueueMapImage != 0L) ? 1 : 0) & ((clGetImageInfo != 0L) ? 1 : 0) & ((clRetainMemObject != 0L) ? 1 : 0) & ((clReleaseMemObject != 0L) ? 1 : 0) & ((clEnqueueUnmapMemObject != 0L) ? 1 : 0) & ((clGetMemObjectInfo != 0L) ? 1 : 0) & ((clCreateSampler != 0L) ? 1 : 0) & ((clRetainSampler != 0L) ? 1 : 0) & ((clReleaseSampler != 0L) ? 1 : 0) & ((clGetSamplerInfo != 0L) ? 1 : 0) & ((clCreateProgramWithSource != 0L) ? 1 : 0) & ((clCreateProgramWithBinary != 0L) ? 1 : 0) & ((clRetainProgram != 0L) ? 1 : 0) & ((clReleaseProgram != 0L) ? 1 : 0) & ((clBuildProgram != 0L) ? 1 : 0) & ((clUnloadCompiler != 0L) ? 1 : 0) & ((clGetProgramInfo != 0L) ? 1 : 0) & ((clGetProgramBuildInfo != 0L) ? 1 : 0) & ((clCreateKernel != 0L) ? 1 : 0) & ((clCreateKernelsInProgram != 0L) ? 1 : 0) & ((clRetainKernel != 0L) ? 1 : 0) & ((clReleaseKernel != 0L) ? 1 : 0) & ((clSetKernelArg != 0L) ? 1 : 0) & ((clGetKernelInfo != 0L) ? 1 : 0) & ((clGetKernelWorkGroupInfo != 0L) ? 1 : 0) & ((clEnqueueNDRangeKernel != 0L) ? 1 : 0) & ((clEnqueueTask != 0L) ? 1 : 0) & ((clEnqueueNativeKernel != 0L) ? 1 : 0) & ((clWaitForEvents != 0L) ? 1 : 0) & ((clGetEventInfo != 0L) ? 1 : 0) & ((clRetainEvent != 0L) ? 1 : 0) & ((clReleaseEvent != 0L) ? 1 : 0) & ((clEnqueueMarker != 0L) ? 1 : 0) & ((clEnqueueBarrier != 0L) ? 1 : 0) & ((clEnqueueWaitForEvents != 0L) ? 1 : 0) & ((clGetEventProfilingInfo != 0L) ? 1 : 0) & ((clFlush != 0L) ? 1 : 0) & ((clFinish != 0L) ? 1 : 0) & ((clGetExtensionFunctionAddress != 0L) ? 1 : 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isCL10GLSupported() {
/* 277 */     return ((clCreateFromGLBuffer != 0L)) & ((clCreateFromGLTexture2D != 0L)) & ((clCreateFromGLTexture3D != 0L) ? 1 : 0) & ((clCreateFromGLRenderbuffer != 0L) ? 1 : 0) & ((clGetGLObjectInfo != 0L) ? 1 : 0) & ((clGetGLTextureInfo != 0L) ? 1 : 0) & ((clEnqueueAcquireGLObjects != 0L) ? 1 : 0) & ((clEnqueueReleaseGLObjects != 0L) ? 1 : 0);
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
/*     */   private static boolean isCL11Supported() {
/* 289 */     return ((clCreateSubBuffer != 0L)) & ((clSetMemObjectDestructorCallback != 0L)) & ((clEnqueueReadBufferRect != 0L) ? 1 : 0) & ((clEnqueueWriteBufferRect != 0L) ? 1 : 0) & ((clEnqueueCopyBufferRect != 0L) ? 1 : 0) & ((clCreateUserEvent != 0L) ? 1 : 0) & ((clSetUserEventStatus != 0L) ? 1 : 0) & ((clSetEventCallback != 0L) ? 1 : 0);
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
/*     */   private static boolean isCL12Supported() {
/*     */     // Byte code:
/*     */     //   0: getstatic org/lwjgl/opencl/CLCapabilities.clRetainDevice : J
/*     */     //   3: lconst_0
/*     */     //   4: lcmp
/*     */     //   5: ifeq -> 12
/*     */     //   8: iconst_1
/*     */     //   9: goto -> 13
/*     */     //   12: iconst_0
/*     */     //   13: getstatic org/lwjgl/opencl/CLCapabilities.clReleaseDevice : J
/*     */     //   16: lconst_0
/*     */     //   17: lcmp
/*     */     //   18: ifeq -> 25
/*     */     //   21: iconst_1
/*     */     //   22: goto -> 26
/*     */     //   25: iconst_0
/*     */     //   26: iand
/*     */     //   27: getstatic org/lwjgl/opencl/CLCapabilities.clCreateSubDevices : J
/*     */     //   30: lconst_0
/*     */     //   31: lcmp
/*     */     //   32: ifeq -> 39
/*     */     //   35: iconst_1
/*     */     //   36: goto -> 40
/*     */     //   39: iconst_0
/*     */     //   40: iand
/*     */     //   41: getstatic org/lwjgl/opencl/CLCapabilities.clCreateImage : J
/*     */     //   44: lconst_0
/*     */     //   45: lcmp
/*     */     //   46: ifeq -> 53
/*     */     //   49: iconst_1
/*     */     //   50: goto -> 54
/*     */     //   53: iconst_0
/*     */     //   54: iand
/*     */     //   55: getstatic org/lwjgl/opencl/CLCapabilities.clCreateProgramWithBuiltInKernels : J
/*     */     //   58: lconst_0
/*     */     //   59: lcmp
/*     */     //   60: ifeq -> 67
/*     */     //   63: iconst_1
/*     */     //   64: goto -> 68
/*     */     //   67: iconst_0
/*     */     //   68: iand
/*     */     //   69: getstatic org/lwjgl/opencl/CLCapabilities.clCompileProgram : J
/*     */     //   72: lconst_0
/*     */     //   73: lcmp
/*     */     //   74: ifeq -> 81
/*     */     //   77: iconst_1
/*     */     //   78: goto -> 82
/*     */     //   81: iconst_0
/*     */     //   82: iand
/*     */     //   83: getstatic org/lwjgl/opencl/CLCapabilities.clLinkProgram : J
/*     */     //   86: lconst_0
/*     */     //   87: lcmp
/*     */     //   88: ifeq -> 95
/*     */     //   91: iconst_1
/*     */     //   92: goto -> 96
/*     */     //   95: iconst_0
/*     */     //   96: iand
/*     */     //   97: getstatic org/lwjgl/opencl/CLCapabilities.clUnloadPlatformCompiler : J
/*     */     //   100: lconst_0
/*     */     //   101: lcmp
/*     */     //   102: ifeq -> 109
/*     */     //   105: iconst_1
/*     */     //   106: goto -> 110
/*     */     //   109: iconst_0
/*     */     //   110: iand
/*     */     //   111: getstatic org/lwjgl/opencl/CLCapabilities.clGetKernelArgInfo : J
/*     */     //   114: lconst_0
/*     */     //   115: lcmp
/*     */     //   116: ifeq -> 123
/*     */     //   119: iconst_1
/*     */     //   120: goto -> 124
/*     */     //   123: iconst_0
/*     */     //   124: iand
/*     */     //   125: getstatic org/lwjgl/opencl/CLCapabilities.clEnqueueFillBuffer : J
/*     */     //   128: lconst_0
/*     */     //   129: lcmp
/*     */     //   130: ifeq -> 137
/*     */     //   133: iconst_1
/*     */     //   134: goto -> 138
/*     */     //   137: iconst_0
/*     */     //   138: iand
/*     */     //   139: getstatic org/lwjgl/opencl/CLCapabilities.clEnqueueFillImage : J
/*     */     //   142: lconst_0
/*     */     //   143: lcmp
/*     */     //   144: ifeq -> 151
/*     */     //   147: iconst_1
/*     */     //   148: goto -> 152
/*     */     //   151: iconst_0
/*     */     //   152: iand
/*     */     //   153: getstatic org/lwjgl/opencl/CLCapabilities.clEnqueueMigrateMemObjects : J
/*     */     //   156: lconst_0
/*     */     //   157: lcmp
/*     */     //   158: ifeq -> 165
/*     */     //   161: iconst_1
/*     */     //   162: goto -> 166
/*     */     //   165: iconst_0
/*     */     //   166: iand
/*     */     //   167: getstatic org/lwjgl/opencl/CLCapabilities.clEnqueueMarkerWithWaitList : J
/*     */     //   170: lconst_0
/*     */     //   171: lcmp
/*     */     //   172: ifeq -> 179
/*     */     //   175: iconst_1
/*     */     //   176: goto -> 180
/*     */     //   179: iconst_0
/*     */     //   180: iand
/*     */     //   181: getstatic org/lwjgl/opencl/CLCapabilities.clEnqueueBarrierWithWaitList : J
/*     */     //   184: lconst_0
/*     */     //   185: lcmp
/*     */     //   186: ifeq -> 193
/*     */     //   189: iconst_1
/*     */     //   190: goto -> 194
/*     */     //   193: iconst_0
/*     */     //   194: iand
/*     */     //   195: getstatic org/lwjgl/opencl/CLCapabilities.clSetPrintfCallback : J
/*     */     //   198: lconst_0
/*     */     //   199: lcmp
/*     */     //   200: ifne -> 203
/*     */     //   203: iconst_1
/*     */     //   204: iand
/*     */     //   205: getstatic org/lwjgl/opencl/CLCapabilities.clGetExtensionFunctionAddressForPlatform : J
/*     */     //   208: lconst_0
/*     */     //   209: lcmp
/*     */     //   210: ifeq -> 217
/*     */     //   213: iconst_1
/*     */     //   214: goto -> 218
/*     */     //   217: iconst_0
/*     */     //   218: iand
/*     */     //   219: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #301	-> 0
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
/*     */   private static boolean isCL12GLSupported() {
/* 321 */     return (clCreateFromGLTexture != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isEXT_device_fissionSupported() {
/* 326 */     return ((clRetainDeviceEXT != 0L)) & ((clReleaseDeviceEXT != 0L)) & ((clCreateSubDevicesEXT != 0L) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEXT_migrate_memobjectSupported() {
/* 333 */     return (clEnqueueMigrateMemObjectEXT != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isKHR_gl_eventSupported() {
/* 338 */     return (clCreateEventFromGLsyncKHR != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isKHR_gl_sharingSupported() {
/* 343 */     return (clGetGLContextInfoKHR != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isKHR_icdSupported() {
/* 348 */     if (clIcdGetPlatformIDsKHR == 0L); return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isKHR_subgroupsSupported() {
/* 353 */     return (clGetKernelSubGroupInfoKHR != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isKHR_terminate_contextSupported() {
/* 358 */     return (clTerminateContextKHR != 0L);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLCapabilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */