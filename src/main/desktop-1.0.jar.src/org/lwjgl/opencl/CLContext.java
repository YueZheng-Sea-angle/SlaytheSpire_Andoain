/*     */ package org.lwjgl.opencl;
/*     */ 
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.List;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.opencl.api.CLImageFormat;
/*     */ import org.lwjgl.opencl.api.Filter;
/*     */ import org.lwjgl.opengl.Drawable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CLContext
/*     */   extends CLObjectChild<CLPlatform>
/*     */ {
/*  49 */   private static final CLContextUtil util = (CLContextUtil)CLPlatform.<CLContext>getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");
/*     */   
/*     */   private final CLObjectRegistry<CLCommandQueue> clCommandQueues;
/*     */   
/*     */   private final CLObjectRegistry<CLMem> clMems;
/*     */   
/*     */   private final CLObjectRegistry<CLSampler> clSamplers;
/*     */   private final CLObjectRegistry<CLProgram> clPrograms;
/*     */   private final CLObjectRegistry<CLEvent> clEvents;
/*     */   private long contextCallback;
/*     */   private long printfCallback;
/*     */   
/*     */   CLContext(long pointer, CLPlatform platform) {
/*  62 */     super(pointer, platform);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     if (isValid()) {
/*  68 */       this.clCommandQueues = new CLObjectRegistry<CLCommandQueue>();
/*  69 */       this.clMems = new CLObjectRegistry<CLMem>();
/*  70 */       this.clSamplers = new CLObjectRegistry<CLSampler>();
/*  71 */       this.clPrograms = new CLObjectRegistry<CLProgram>();
/*  72 */       this.clEvents = new CLObjectRegistry<CLEvent>();
/*     */     } else {
/*  74 */       this.clCommandQueues = null;
/*  75 */       this.clMems = null;
/*  76 */       this.clSamplers = null;
/*  77 */       this.clPrograms = null;
/*  78 */       this.clEvents = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLCommandQueue getCLCommandQueue(long id) {
/*  89 */     return this.clCommandQueues.getObject(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLMem getCLMem(long id) {
/*  98 */     return this.clMems.getObject(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLSampler getCLSampler(long id) {
/* 107 */     return this.clSamplers.getObject(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLProgram getCLProgram(long id) {
/* 116 */     return this.clPrograms.getObject(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CLEvent getCLEvent(long id) {
/* 125 */     return this.clEvents.getObject(id);
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
/*     */   public static CLContext create(CLPlatform platform, List<CLDevice> devices, IntBuffer errcode_ret) throws LWJGLException {
/* 141 */     return create(platform, devices, (CLContextCallback)null, (Drawable)null, errcode_ret);
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
/*     */   public static CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, IntBuffer errcode_ret) throws LWJGLException {
/* 157 */     return create(platform, devices, pfn_notify, (Drawable)null, errcode_ret);
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
/*     */   public static CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
/* 173 */     return util.create(platform, devices, pfn_notify, share_drawable, errcode_ret);
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
/*     */   public static CLContext createFromType(CLPlatform platform, long device_type, IntBuffer errcode_ret) throws LWJGLException {
/* 188 */     return util.createFromType(platform, device_type, null, null, errcode_ret);
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
/*     */   public static CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, IntBuffer errcode_ret) throws LWJGLException {
/* 204 */     return util.createFromType(platform, device_type, pfn_notify, null, errcode_ret);
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
/*     */   public static CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
/* 220 */     return util.createFromType(platform, device_type, pfn_notify, share_drawable, errcode_ret);
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
/* 231 */     return util.getInfoInt(this, param_name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<CLDevice> getInfoDevices() {
/* 240 */     return util.getInfoDevices(this);
/*     */   }
/*     */   
/*     */   public List<CLImageFormat> getSupportedImageFormats(long flags, int image_type) {
/* 244 */     return getSupportedImageFormats(flags, image_type, (Filter<CLImageFormat>)null);
/*     */   }
/*     */   
/*     */   public List<CLImageFormat> getSupportedImageFormats(long flags, int image_type, Filter<CLImageFormat> filter) {
/* 248 */     return util.getSupportedImageFormats(this, flags, image_type, filter);
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
/*     */   CLObjectRegistry<CLCommandQueue> getCLCommandQueueRegistry() {
/* 266 */     return this.clCommandQueues;
/*     */   } CLObjectRegistry<CLMem> getCLMemRegistry() {
/* 268 */     return this.clMems;
/*     */   } CLObjectRegistry<CLSampler> getCLSamplerRegistry() {
/* 270 */     return this.clSamplers;
/*     */   } CLObjectRegistry<CLProgram> getCLProgramRegistry() {
/* 272 */     return this.clPrograms;
/*     */   } CLObjectRegistry<CLEvent> getCLEventRegistry() {
/* 274 */     return this.clEvents;
/*     */   }
/*     */   private boolean checkCallback(long callback, int result) {
/* 277 */     if (result == 0 && (callback == 0L || isValid())) {
/* 278 */       return true;
/*     */     }
/* 280 */     if (callback != 0L)
/* 281 */       CallbackUtil.deleteGlobalRef(callback); 
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setContextCallback(long callback) {
/* 292 */     if (checkCallback(callback, 0)) {
/* 293 */       this.contextCallback = callback;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setPrintfCallback(long callback, int result) {
/* 303 */     if (checkCallback(callback, result)) {
/* 304 */       this.printfCallback = callback;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void releaseImpl() {
/* 313 */     if (release() > 0) {
/*     */       return;
/*     */     }
/* 316 */     if (this.contextCallback != 0L)
/* 317 */       CallbackUtil.deleteGlobalRef(this.contextCallback); 
/* 318 */     if (this.printfCallback != 0L)
/* 319 */       CallbackUtil.deleteGlobalRef(this.printfCallback); 
/*     */   }
/*     */   
/*     */   static interface CLContextUtil extends InfoUtil<CLContext> {
/*     */     List<CLDevice> getInfoDevices(CLContext param1CLContext);
/*     */     
/*     */     CLContext create(CLPlatform param1CLPlatform, List<CLDevice> param1List, CLContextCallback param1CLContextCallback, Drawable param1Drawable, IntBuffer param1IntBuffer) throws LWJGLException;
/*     */     
/*     */     CLContext createFromType(CLPlatform param1CLPlatform, long param1Long, CLContextCallback param1CLContextCallback, Drawable param1Drawable, IntBuffer param1IntBuffer) throws LWJGLException;
/*     */     
/*     */     List<CLImageFormat> getSupportedImageFormats(CLContext param1CLContext, long param1Long, int param1Int, Filter<CLImageFormat> param1Filter);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\CLContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */