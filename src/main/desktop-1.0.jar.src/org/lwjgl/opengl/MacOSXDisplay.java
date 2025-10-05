/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Robot;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.LWJGLException;
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
/*     */ final class MacOSXDisplay
/*     */   implements DisplayImplementation
/*     */ {
/*     */   private static final int PBUFFER_HANDLE_SIZE = 24;
/*     */   private static final int GAMMA_LENGTH = 256;
/*     */   private Canvas canvas;
/*     */   private Robot robot;
/*     */   private MacOSXMouseEventQueue mouse_queue;
/*     */   private KeyboardEventQueue keyboard_queue;
/*     */   private DisplayMode requested_mode;
/*     */   private MacOSXNativeMouse mouse;
/*     */   private MacOSXNativeKeyboard keyboard;
/*     */   private ByteBuffer window;
/*     */   private ByteBuffer context;
/*     */   private boolean skipViewportValue = false;
/*  83 */   private static final IntBuffer current_viewport = BufferUtils.createIntBuffer(16);
/*     */   
/*     */   private boolean mouseInsideWindow;
/*     */   
/*     */   private boolean close_requested;
/*     */   
/*     */   private boolean native_mode = true;
/*     */   
/*     */   private boolean updateNativeCursor = false;
/*     */   
/*  93 */   private long currentNativeCursor = 0L;
/*     */   
/*     */   private boolean enableHighDPI = false;
/*     */   
/*  97 */   private float scaleFactor = 1.0F;
/*     */ 
/*     */   
/*     */   private native ByteBuffer nCreateWindow(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws LWJGLException;
/*     */ 
/*     */   
/*     */   private native Object nGetCurrentDisplayMode();
/*     */ 
/*     */   
/*     */   private native void nGetDisplayModes(Object paramObject);
/*     */ 
/*     */   
/*     */   private native boolean nIsMiniaturized(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native boolean nIsFocused(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native void nSetResizable(ByteBuffer paramByteBuffer, boolean paramBoolean);
/*     */   
/*     */   private native void nResizeWindow(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   private native boolean nWasResized(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native int nGetX(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native int nGetY(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native int nGetWidth(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native int nGetHeight(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private native boolean nIsNativeMode(ByteBuffer paramByteBuffer);
/*     */   
/*     */   private static boolean isUndecorated() {
/* 130 */     return Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated");
/*     */   }
/*     */   
/*     */   public void createWindow(DrawableLWJGL drawable, DisplayMode mode, Canvas parent, int x, int y) throws LWJGLException {
/* 134 */     boolean fullscreen = Display.isFullscreen();
/* 135 */     boolean resizable = Display.isResizable();
/* 136 */     boolean parented = (parent != null && !fullscreen);
/*     */ 
/*     */     
/* 139 */     boolean enableFullscreenModeAPI = (LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7) && parent == null && !Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.disableOSXFullscreenModeAPI"));
/*     */ 
/*     */ 
/*     */     
/* 143 */     this.enableHighDPI = (LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7) && parent == null && (Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.enableHighDPI") || fullscreen));
/*     */ 
/*     */     
/* 146 */     if (parented) { this.canvas = parent; }
/* 147 */     else { this.canvas = null; }
/*     */     
/* 149 */     this.close_requested = false;
/*     */     
/* 151 */     DrawableGL gl_drawable = (DrawableGL)Display.getDrawable();
/* 152 */     PeerInfo peer_info = gl_drawable.peer_info;
/* 153 */     ByteBuffer peer_handle = peer_info.lockAndGetHandle();
/* 154 */     ByteBuffer window_handle = parented ? ((MacOSXCanvasPeerInfo)peer_info).window_handle : this.window;
/*     */ 
/*     */     
/*     */     try {
/* 158 */       this.window = nCreateWindow(x, y, mode.getWidth(), mode.getHeight(), fullscreen, isUndecorated(), resizable, parented, enableFullscreenModeAPI, this.enableHighDPI, peer_handle, window_handle);
/*     */ 
/*     */ 
/*     */       
/* 162 */       if (fullscreen) {
/*     */         
/* 164 */         this.skipViewportValue = true;
/*     */         
/* 166 */         current_viewport.put(2, mode.getWidth());
/* 167 */         current_viewport.put(3, mode.getHeight());
/*     */       } 
/*     */       
/* 170 */       this.native_mode = nIsNativeMode(peer_handle);
/*     */       
/* 172 */       if (!this.native_mode) {
/* 173 */         this.robot = AWTUtil.createRobot(this.canvas);
/*     */       }
/*     */     }
/* 176 */     catch (LWJGLException e) {
/* 177 */       destroyWindow();
/* 178 */       throw e;
/*     */     } finally {
/* 180 */       peer_info.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void doHandleQuit() {
/* 185 */     synchronized (this) {
/* 186 */       this.close_requested = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mouseInsideWindow(boolean inside) {
/* 191 */     synchronized (this) {
/* 192 */       this.mouseInsideWindow = inside;
/*     */     } 
/* 194 */     this.updateNativeCursor = true;
/*     */   }
/*     */   
/*     */   public void setScaleFactor(float scale) {
/* 198 */     synchronized (this) {
/* 199 */       this.scaleFactor = scale;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public native void nDestroyCALayer(ByteBuffer paramByteBuffer);
/*     */   
/*     */   public native void nDestroyWindow(ByteBuffer paramByteBuffer);
/*     */   
/*     */   public void destroyWindow() {
/* 209 */     if (!this.native_mode) {
/* 210 */       DrawableGL gl_drawable = (DrawableGL)Display.getDrawable();
/* 211 */       PeerInfo peer_info = gl_drawable.peer_info;
/* 212 */       if (peer_info != null) {
/* 213 */         ByteBuffer peer_handle = peer_info.getHandle();
/* 214 */         nDestroyCALayer(peer_handle);
/*     */       } 
/* 216 */       this.robot = null;
/*     */     } 
/*     */     
/* 219 */     nDestroyWindow(this.window);
/*     */   }
/*     */   
/*     */   public int getGammaRampLength() {
/* 223 */     return 256;
/*     */   }
/*     */   
/*     */   public native void setGammaRamp(FloatBuffer paramFloatBuffer) throws LWJGLException;
/*     */   
/*     */   public String getAdapter() {
/* 229 */     return null;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean equals(DisplayMode mode1, DisplayMode mode2) {
/* 237 */     return (mode1.getWidth() == mode2.getWidth() && mode1.getHeight() == mode2.getHeight() && mode1.getBitsPerPixel() == mode2.getBitsPerPixel() && mode1.getFrequency() == mode2.getFrequency());
/*     */   }
/*     */ 
/*     */   
/*     */   public void switchDisplayMode(DisplayMode mode) throws LWJGLException {
/* 242 */     DisplayMode[] modes = getAvailableDisplayModes();
/*     */     
/* 244 */     for (DisplayMode available_mode : modes) {
/* 245 */       if (equals(available_mode, mode)) {
/* 246 */         this.requested_mode = available_mode;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 251 */     throw new LWJGLException(mode + " is not supported");
/*     */   }
/*     */   
/*     */   public void resetDisplayMode() {
/* 255 */     this.requested_mode = null;
/* 256 */     restoreGamma();
/*     */   }
/*     */   
/*     */   private native void restoreGamma();
/*     */   
/*     */   public Object createDisplayMode(int width, int height, int bitsPerPixel, int refreshRate) {
/* 262 */     return new DisplayMode(width, height, bitsPerPixel, refreshRate);
/*     */   }
/*     */   
/*     */   public DisplayMode init() throws LWJGLException {
/* 266 */     return (DisplayMode)nGetCurrentDisplayMode();
/*     */   }
/*     */   
/*     */   public void addDisplayMode(Object modesList, int width, int height, int bitsPerPixel, int refreshRate) {
/* 270 */     List<DisplayMode> modes = (List<DisplayMode>)modesList;
/* 271 */     DisplayMode displayMode = new DisplayMode(width, height, bitsPerPixel, refreshRate);
/* 272 */     modes.add(displayMode);
/*     */   }
/*     */   
/*     */   public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
/* 276 */     List<DisplayMode> modes = new ArrayList<DisplayMode>();
/* 277 */     nGetDisplayModes(modes);
/* 278 */     modes.add(Display.getDesktopDisplayMode());
/* 279 */     return modes.<DisplayMode>toArray(new DisplayMode[modes.size()]);
/*     */   }
/*     */   
/*     */   private native void nSetTitle(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2);
/*     */   
/*     */   public void setTitle(String title) {
/* 285 */     ByteBuffer buffer = MemoryUtil.encodeUTF8(title);
/* 286 */     nSetTitle(this.window, buffer);
/*     */   }
/*     */   
/*     */   public boolean isCloseRequested() {
/*     */     boolean result;
/* 291 */     synchronized (this) {
/* 292 */       result = this.close_requested;
/* 293 */       this.close_requested = false;
/*     */     } 
/* 295 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isVisible() {
/* 299 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 303 */     if (this.native_mode) {
/* 304 */       return nIsFocused(this.window);
/*     */     }
/*     */     
/* 307 */     return Display.getParent().hasFocus();
/*     */   }
/*     */ 
/*     */   
/*     */   public Canvas getCanvas() {
/* 312 */     return this.canvas;
/*     */   }
/*     */   
/*     */   public boolean isDirty() {
/* 316 */     return false;
/*     */   }
/*     */   
/*     */   public PeerInfo createPeerInfo(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
/*     */     try {
/* 321 */       return new MacOSXDisplayPeerInfo(pixel_format, attribs, true);
/* 322 */     } catch (LWJGLException e) {
/* 323 */       return new MacOSXDisplayPeerInfo(pixel_format, attribs, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/* 328 */     boolean should_update = true;
/*     */     
/* 330 */     DrawableGL drawable = (DrawableGL)Display.getDrawable();
/* 331 */     if (should_update) {
/*     */ 
/*     */ 
/*     */       
/* 335 */       if (this.skipViewportValue) { this.skipViewportValue = false; }
/* 336 */       else { GL11.glGetInteger(2978, current_viewport); }
/*     */       
/* 338 */       drawable.context.update();
/*     */ 
/*     */       
/* 341 */       GL11.glViewport(current_viewport.get(0), current_viewport.get(1), current_viewport.get(2), current_viewport.get(3));
/*     */     } 
/*     */     
/* 344 */     if (this.native_mode && this.updateNativeCursor) {
/* 345 */       this.updateNativeCursor = false;
/*     */       try {
/* 347 */         setNativeCursor(Long.valueOf(this.currentNativeCursor));
/* 348 */       } catch (LWJGLException e) {
/* 349 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reshape(int x, int y, int width, int height) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasWheel() {
/* 362 */     return AWTUtil.hasWheel();
/*     */   }
/*     */   
/*     */   public int getButtonCount() {
/* 366 */     return AWTUtil.getButtonCount();
/*     */   }
/*     */   
/*     */   public void createMouse() throws LWJGLException {
/* 370 */     if (this.native_mode) {
/* 371 */       this.mouse = new MacOSXNativeMouse(this, this.window);
/* 372 */       this.mouse.register();
/*     */     } else {
/*     */       
/* 375 */       this.mouse_queue = new MacOSXMouseEventQueue(this.canvas);
/* 376 */       this.mouse_queue.register();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void destroyMouse() {
/* 381 */     if (this.native_mode) {
/*     */       
/*     */       try {
/* 384 */         MacOSXNativeMouse.setCursor(0L);
/* 385 */       } catch (LWJGLException e) {}
/*     */ 
/*     */       
/* 388 */       grabMouse(false);
/*     */       
/* 390 */       if (this.mouse != null) {
/* 391 */         this.mouse.unregister();
/*     */       }
/* 393 */       this.mouse = null;
/*     */     } else {
/*     */       
/* 396 */       if (this.mouse_queue != null) {
/* 397 */         MacOSXMouseEventQueue.nGrabMouse(false);
/* 398 */         this.mouse_queue.unregister();
/*     */       } 
/* 400 */       this.mouse_queue = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons_buffer) {
/* 405 */     if (this.native_mode) {
/* 406 */       this.mouse.poll(coord_buffer, buttons_buffer);
/*     */     } else {
/*     */       
/* 409 */       this.mouse_queue.poll(coord_buffer, buttons_buffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readMouse(ByteBuffer buffer) {
/* 414 */     if (this.native_mode) {
/* 415 */       this.mouse.copyEvents(buffer);
/*     */     } else {
/*     */       
/* 418 */       this.mouse_queue.copyEvents(buffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void grabMouse(boolean grab) {
/* 423 */     if (this.native_mode) {
/* 424 */       this.mouse.setGrabbed(grab);
/*     */     } else {
/*     */       
/* 427 */       this.mouse_queue.setGrabbed(grab);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getNativeCursorCapabilities() {
/* 432 */     if (this.native_mode) {
/* 433 */       return 7;
/*     */     }
/*     */     
/* 436 */     return AWTUtil.getNativeCursorCapabilities();
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int x, int y) {
/* 440 */     if (this.native_mode && 
/* 441 */       this.mouse != null) {
/* 442 */       this.mouse.setCursorPosition(x, y);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNativeCursor(Object handle) throws LWJGLException {
/* 451 */     if (this.native_mode) {
/* 452 */       this.currentNativeCursor = getCursorHandle(handle);
/* 453 */       if (Display.isCreated())
/* 454 */         if (this.mouseInsideWindow) { MacOSXNativeMouse.setCursor(this.currentNativeCursor); }
/* 455 */         else { MacOSXNativeMouse.setCursor(0L); }
/*     */          
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMinCursorSize() {
/* 461 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxCursorSize() {
/* 467 */     DisplayMode dm = Display.getDesktopDisplayMode();
/* 468 */     return Math.min(dm.getWidth(), dm.getHeight()) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void createKeyboard() throws LWJGLException {
/* 473 */     if (this.native_mode) {
/* 474 */       this.keyboard = new MacOSXNativeKeyboard(this.window);
/* 475 */       this.keyboard.register();
/*     */     } else {
/*     */       
/* 478 */       this.keyboard_queue = new KeyboardEventQueue(this.canvas);
/* 479 */       this.keyboard_queue.register();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void destroyKeyboard() {
/* 484 */     if (this.native_mode) {
/* 485 */       if (this.keyboard != null) {
/* 486 */         this.keyboard.unregister();
/*     */       }
/* 488 */       this.keyboard = null;
/*     */     } else {
/*     */       
/* 491 */       if (this.keyboard_queue != null) {
/* 492 */         this.keyboard_queue.unregister();
/*     */       }
/* 494 */       this.keyboard_queue = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pollKeyboard(ByteBuffer keyDownBuffer) {
/* 499 */     if (this.native_mode) {
/* 500 */       this.keyboard.poll(keyDownBuffer);
/*     */     } else {
/*     */       
/* 503 */       this.keyboard_queue.poll(keyDownBuffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void readKeyboard(ByteBuffer buffer) {
/* 508 */     if (this.native_mode) {
/* 509 */       this.keyboard.copyEvents(buffer);
/*     */     } else {
/*     */       
/* 512 */       this.keyboard_queue.copyEvents(buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
/* 518 */     if (this.native_mode) {
/* 519 */       long cursor = MacOSXNativeMouse.createCursor(width, height, xHotspot, yHotspot, numImages, images, delays);
/* 520 */       return Long.valueOf(cursor);
/*     */     } 
/*     */     
/* 523 */     return AWTUtil.createCursor(width, height, xHotspot, yHotspot, numImages, images, delays);
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroyCursor(Object cursor_handle) {
/* 528 */     long handle = getCursorHandle(cursor_handle);
/*     */ 
/*     */     
/* 531 */     if (this.currentNativeCursor == handle) {
/* 532 */       this.currentNativeCursor = 0L;
/*     */     }
/*     */     
/* 535 */     MacOSXNativeMouse.destroyCursor(handle);
/*     */   }
/*     */   
/*     */   private static long getCursorHandle(Object cursor_handle) {
/* 539 */     return (cursor_handle != null) ? ((Long)cursor_handle).longValue() : 0L;
/*     */   }
/*     */   
/*     */   public int getPbufferCapabilities() {
/* 543 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isBufferLost(PeerInfo handle) {
/* 547 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format, ContextAttribs attribs, IntBuffer pixelFormatCaps, IntBuffer pBufferAttribs) throws LWJGLException {
/* 553 */     return new MacOSXPbufferPeerInfo(width, height, pixel_format, attribs);
/*     */   }
/*     */   
/*     */   public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
/* 557 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
/* 561 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
/* 565 */     throw new UnsupportedOperationException();
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
/*     */   public int setIcon(ByteBuffer[] icons) {
/* 609 */     return 0;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 613 */     return nGetX(this.window);
/*     */   }
/*     */   
/*     */   public int getY() {
/* 617 */     return nGetY(this.window);
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 621 */     return nGetWidth(this.window);
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 625 */     return nGetHeight(this.window);
/*     */   }
/*     */   
/*     */   public boolean isInsideWindow() {
/* 629 */     return this.mouseInsideWindow;
/*     */   }
/*     */   
/*     */   public void setResizable(boolean resizable) {
/* 633 */     nSetResizable(this.window, resizable);
/*     */   }
/*     */   
/*     */   public boolean wasResized() {
/* 637 */     return nWasResized(this.window);
/*     */   }
/*     */   
/*     */   public float getPixelScaleFactor() {
/* 641 */     return (this.enableHighDPI && !Display.isFullscreen()) ? this.scaleFactor : 1.0F;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\MacOSXDisplay.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */