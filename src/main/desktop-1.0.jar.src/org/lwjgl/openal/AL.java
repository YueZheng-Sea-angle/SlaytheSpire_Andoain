/*     */ package org.lwjgl.openal;
/*     */ 
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.LWJGLUtil;
/*     */ import org.lwjgl.Sys;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AL
/*     */ {
/*     */   static ALCdevice device;
/*     */   static ALCcontext context;
/*     */   private static boolean created;
/*     */   
/*     */   static {
/*  59 */     Sys.initialize();
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
/*     */   public static boolean isCreated() {
/*  87 */     return created;
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
/*     */   public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized) throws LWJGLException {
/* 102 */     create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
/*     */     String libname, library_names[];
/* 112 */     if (created) {
/* 113 */       throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
/*     */     }
/*     */     
/* 116 */     switch (LWJGLUtil.getPlatform()) {
/*     */       case 3:
/* 118 */         if (Sys.is64Bit()) {
/* 119 */           String str = "OpenAL64";
/* 120 */           String[] arrayOfString = { "OpenAL64.dll" }; break;
/*     */         } 
/* 122 */         libname = "OpenAL32";
/* 123 */         library_names = new String[] { "OpenAL32.dll" };
/*     */         break;
/*     */       
/*     */       case 1:
/* 127 */         libname = "openal";
/* 128 */         library_names = new String[] { "libopenal64.so", "libopenal.so", "libopenal.so.0" };
/*     */         break;
/*     */       case 2:
/* 131 */         libname = "openal";
/* 132 */         library_names = new String[] { "openal.dylib" };
/*     */         break;
/*     */       default:
/* 135 */         throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
/*     */     } 
/* 137 */     String[] oalPaths = LWJGLUtil.getLibraryPaths(libname, library_names, AL.class.getClassLoader());
/* 138 */     LWJGLUtil.log("Found " + oalPaths.length + " OpenAL paths");
/* 139 */     for (String oalPath : oalPaths) {
/*     */       try {
/* 141 */         nCreate(oalPath);
/* 142 */         created = true;
/* 143 */         init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
/*     */         break;
/* 145 */       } catch (LWJGLException e) {
/* 146 */         LWJGLUtil.log("Failed to load " + oalPath + ": " + e.getMessage());
/*     */       } 
/*     */     } 
/* 149 */     if (!created && LWJGLUtil.getPlatform() == 2) {
/*     */       
/* 151 */       nCreateDefault();
/* 152 */       created = true;
/* 153 */       init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
/*     */     } 
/* 155 */     if (!created)
/* 156 */       throw new LWJGLException("Could not locate OpenAL library."); 
/*     */   }
/*     */   
/*     */   private static void init(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
/*     */     try {
/* 161 */       AL10.initNativeStubs();
/* 162 */       ALC10.initNativeStubs();
/*     */       
/* 164 */       if (openDevice) {
/* 165 */         device = ALC10.alcOpenDevice(deviceArguments);
/* 166 */         if (device == null) {
/* 167 */           throw new LWJGLException("Could not open ALC device");
/*     */         }
/*     */         
/* 170 */         if (contextFrequency == -1) {
/* 171 */           context = ALC10.alcCreateContext(device, null);
/*     */         } else {
/* 173 */           context = ALC10.alcCreateContext(device, ALCcontext.createAttributeList(contextFrequency, contextRefresh, contextSynchronized ? 1 : 0));
/*     */         } 
/*     */ 
/*     */         
/* 177 */         ALC10.alcMakeContextCurrent(context);
/*     */       } 
/* 179 */     } catch (LWJGLException e) {
/* 180 */       destroy();
/* 181 */       throw e;
/*     */     } 
/*     */     
/* 184 */     ALC11.initialize();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     if (ALC10.alcIsExtensionPresent(device, "ALC_EXT_EFX")) {
/* 194 */       EFX10.initNativeStubs();
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
/*     */   public static void create() throws LWJGLException {
/* 206 */     create(null, 44100, 60, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void destroy() {
/* 213 */     if (context != null) {
/* 214 */       ALC10.alcMakeContextCurrent(null);
/* 215 */       ALC10.alcDestroyContext(context);
/* 216 */       context = null;
/*     */     } 
/* 218 */     if (device != null) {
/* 219 */       boolean result = ALC10.alcCloseDevice(device);
/* 220 */       device = null;
/*     */     } 
/* 222 */     resetNativeStubs(AL10.class);
/* 223 */     resetNativeStubs(AL11.class);
/* 224 */     resetNativeStubs(ALC10.class);
/* 225 */     resetNativeStubs(ALC11.class);
/* 226 */     resetNativeStubs(EFX10.class);
/*     */     
/* 228 */     if (created)
/* 229 */       nDestroy(); 
/* 230 */     created = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ALCcontext getContext() {
/* 239 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ALCdevice getDevice() {
/* 246 */     return device;
/*     */   }
/*     */   
/*     */   private static native void nCreate(String paramString) throws LWJGLException;
/*     */   
/*     */   private static native void nCreateDefault() throws LWJGLException;
/*     */   
/*     */   private static native void nDestroy();
/*     */   
/*     */   private static native void resetNativeStubs(Class paramClass);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\openal\AL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */