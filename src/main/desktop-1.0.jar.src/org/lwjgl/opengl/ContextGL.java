/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.LWJGLUtil;
/*     */ import org.lwjgl.PointerBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ContextGL
/*     */   implements Context
/*     */ {
/*     */   private static final ContextImplementation implementation;
/*  63 */   private static final ThreadLocal<ContextGL> current_context_local = new ThreadLocal<ContextGL>();
/*     */   
/*     */   private final ByteBuffer handle;
/*     */   
/*     */   private final PeerInfo peer_info;
/*     */   
/*     */   private final ContextAttribs contextAttribs;
/*     */   
/*     */   private final boolean forwardCompatible;
/*     */   
/*     */   private boolean destroyed;
/*     */   
/*     */   private boolean destroy_requested;
/*     */   
/*     */   private Thread thread;
/*     */ 
/*     */   
/*     */   static {
/*  81 */     Sys.initialize();
/*  82 */     implementation = createImplementation();
/*     */   }
/*     */   
/*     */   private static ContextImplementation createImplementation() {
/*  86 */     switch (LWJGLUtil.getPlatform()) {
/*     */       case 1:
/*  88 */         return new LinuxContextImplementation();
/*     */       case 3:
/*  90 */         return new WindowsContextImplementation();
/*     */       case 2:
/*  92 */         return new MacOSXContextImplementation();
/*     */     } 
/*  94 */     throw new IllegalStateException("Unsupported platform");
/*     */   }
/*     */ 
/*     */   
/*     */   PeerInfo getPeerInfo() {
/*  99 */     return this.peer_info;
/*     */   }
/*     */   
/*     */   ContextAttribs getContextAttribs() {
/* 103 */     return this.contextAttribs;
/*     */   }
/*     */   
/*     */   static ContextGL getCurrentContext() {
/* 107 */     return current_context_local.get();
/*     */   }
/*     */ 
/*     */   
/*     */   ContextGL(PeerInfo peer_info, ContextAttribs attribs, ContextGL shared_context) throws LWJGLException {
/* 112 */     ContextGL context_lock = (shared_context != null) ? shared_context : this;
/*     */ 
/*     */     
/* 115 */     synchronized (context_lock) {
/* 116 */       if (shared_context != null && shared_context.destroyed)
/* 117 */         throw new IllegalArgumentException("Shared context is destroyed"); 
/* 118 */       GLContext.loadOpenGLLibrary(); try {
/*     */         IntBuffer attribList;
/* 120 */         this.peer_info = peer_info;
/* 121 */         this.contextAttribs = attribs;
/*     */ 
/*     */         
/* 124 */         if (attribs != null) {
/* 125 */           attribList = attribs.getAttribList();
/* 126 */           this.forwardCompatible = attribs.isForwardCompatible();
/*     */         } else {
/* 128 */           attribList = null;
/* 129 */           this.forwardCompatible = false;
/*     */         } 
/*     */         
/* 132 */         this.handle = implementation.create(peer_info, attribList, (shared_context != null) ? shared_context.handle : null);
/* 133 */       } catch (LWJGLException e) {
/* 134 */         GLContext.unloadOpenGLLibrary();
/* 135 */         throw e;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseCurrent() throws LWJGLException {
/* 142 */     ContextGL current_context = getCurrentContext();
/* 143 */     if (current_context != null) {
/* 144 */       implementation.releaseCurrentContext();
/* 145 */       GLContext.useContext(null);
/* 146 */       current_context_local.set(null);
/* 147 */       synchronized (current_context) {
/* 148 */         current_context.thread = null;
/* 149 */         current_context.checkDestroy();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void releaseDrawable() throws LWJGLException {
/* 161 */     if (this.destroyed)
/* 162 */       throw new IllegalStateException("Context is destroyed"); 
/* 163 */     implementation.releaseDrawable(getHandle());
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void update() {
/* 168 */     if (this.destroyed)
/* 169 */       throw new IllegalStateException("Context is destroyed"); 
/* 170 */     implementation.update(getHandle());
/*     */   }
/*     */ 
/*     */   
/*     */   public static void swapBuffers() throws LWJGLException {
/* 175 */     implementation.swapBuffers();
/*     */   }
/*     */   
/*     */   private boolean canAccess() {
/* 179 */     return (this.thread == null || Thread.currentThread() == this.thread);
/*     */   }
/*     */   
/*     */   private void checkAccess() {
/* 183 */     if (!canAccess()) {
/* 184 */       throw new IllegalStateException("From thread " + Thread.currentThread() + ": " + this.thread + " already has the context current");
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void makeCurrent() throws LWJGLException {
/* 189 */     checkAccess();
/* 190 */     if (this.destroyed)
/* 191 */       throw new IllegalStateException("Context is destroyed"); 
/* 192 */     this.thread = Thread.currentThread();
/* 193 */     current_context_local.set(this);
/* 194 */     implementation.makeCurrent(this.peer_info, this.handle);
/* 195 */     GLContext.useContext(this, this.forwardCompatible);
/*     */   }
/*     */   
/*     */   ByteBuffer getHandle() {
/* 199 */     return this.handle;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isCurrent() throws LWJGLException {
/* 204 */     if (this.destroyed)
/* 205 */       throw new IllegalStateException("Context is destroyed"); 
/* 206 */     return implementation.isCurrent(this.handle);
/*     */   }
/*     */   
/*     */   private void checkDestroy() {
/* 210 */     if (!this.destroyed && this.destroy_requested) {
/*     */       try {
/* 212 */         releaseDrawable();
/* 213 */         implementation.destroy(this.peer_info, this.handle);
/* 214 */         CallbackUtil.unregisterCallbacks(this);
/* 215 */         this.destroyed = true;
/* 216 */         this.thread = null;
/* 217 */         GLContext.unloadOpenGLLibrary();
/* 218 */       } catch (LWJGLException e) {
/* 219 */         LWJGLUtil.log("Exception occurred while destroying context: " + e);
/*     */       } 
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
/*     */   public static void setSwapInterval(int value) {
/* 232 */     implementation.setSwapInterval(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void forceDestroy() throws LWJGLException {
/* 241 */     checkAccess();
/* 242 */     destroy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void destroy() throws LWJGLException {
/* 250 */     if (this.destroyed)
/*     */       return; 
/* 252 */     this.destroy_requested = true;
/* 253 */     boolean was_current = isCurrent();
/* 254 */     int error = 0;
/* 255 */     if (was_current) {
/*     */       
/*     */       try {
/* 258 */         error = GL11.glGetError();
/* 259 */       } catch (Exception e) {}
/*     */ 
/*     */       
/* 262 */       releaseCurrent();
/*     */     } 
/* 264 */     checkDestroy();
/* 265 */     if (was_current && error != 0)
/* 266 */       throw new OpenGLException(error); 
/*     */   }
/*     */   
/*     */   public synchronized void setCLSharingProperties(PointerBuffer properties) throws LWJGLException {
/* 270 */     ByteBuffer peer_handle = this.peer_info.lockAndGetHandle(); try {
/*     */       WindowsContextImplementation implWindows; LinuxContextImplementation implLinux;
/* 272 */       switch (LWJGLUtil.getPlatform()) {
/*     */         case 3:
/* 274 */           implWindows = (WindowsContextImplementation)implementation;
/* 275 */           properties.put(8200L).put(implWindows.getHGLRC(this.handle));
/* 276 */           properties.put(8203L).put(implWindows.getHDC(peer_handle));
/*     */           break;
/*     */         case 1:
/* 279 */           implLinux = (LinuxContextImplementation)implementation;
/* 280 */           properties.put(8200L).put(implLinux.getGLXContext(this.handle));
/* 281 */           properties.put(8202L).put(implLinux.getDisplay(peer_handle));
/*     */           break;
/*     */         case 2:
/* 284 */           if (LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 6)) {
/*     */             
/* 286 */             MacOSXContextImplementation implMacOSX = (MacOSXContextImplementation)implementation;
/* 287 */             long CGLShareGroup = implMacOSX.getCGLShareGroup(this.handle);
/* 288 */             properties.put(268435456L).put(CGLShareGroup);
/*     */             break;
/*     */           } 
/*     */         default:
/* 292 */           throw new UnsupportedOperationException("CL/GL context sharing is not supported on this platform.");
/*     */       } 
/*     */     } finally {
/* 295 */       this.peer_info.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\ContextGL.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */