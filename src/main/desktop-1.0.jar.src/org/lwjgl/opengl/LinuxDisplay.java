/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.awt.Canvas;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.lwjgl.BufferUtils;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.LWJGLUtil;
/*      */ import org.lwjgl.MemoryUtil;
/*      */ import org.lwjgl.opengles.GLContext;
/*      */ import org.lwjgl.opengles.PixelFormat;
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
/*      */ final class LinuxDisplay
/*      */   implements DisplayImplementation
/*      */ {
/*      */   public static final int CurrentTime = 0;
/*      */   public static final int GrabSuccess = 0;
/*      */   public static final int AutoRepeatModeOff = 0;
/*      */   public static final int AutoRepeatModeOn = 1;
/*      */   public static final int AutoRepeatModeDefault = 2;
/*      */   public static final int None = 0;
/*      */   private static final int KeyPressMask = 1;
/*      */   private static final int KeyReleaseMask = 2;
/*      */   private static final int ButtonPressMask = 4;
/*      */   private static final int ButtonReleaseMask = 8;
/*      */   private static final int NotifyAncestor = 0;
/*      */   private static final int NotifyNonlinear = 3;
/*      */   private static final int NotifyPointer = 5;
/*      */   private static final int NotifyPointerRoot = 6;
/*      */   private static final int NotifyDetailNone = 7;
/*      */   private static final int SetModeInsert = 0;
/*      */   private static final int SaveSetRoot = 1;
/*      */   private static final int SaveSetUnmap = 1;
/*      */   private static final int X_SetInputFocus = 42;
/*      */   private static final int FULLSCREEN_LEGACY = 1;
/*      */   private static final int FULLSCREEN_NETWM = 2;
/*      */   private static final int WINDOWED = 3;
/*   98 */   private static int current_window_mode = 3;
/*      */   
/*      */   private static final int XRANDR = 10;
/*      */   
/*      */   private static final int XF86VIDMODE = 11;
/*      */   
/*      */   private static final int NONE = 12;
/*      */   
/*      */   private static long display;
/*      */   
/*      */   private static long current_window;
/*      */   
/*      */   private static long saved_error_handler;
/*      */   
/*      */   private static int display_connection_usage_count;
/*  113 */   private final LinuxEvent event_buffer = new LinuxEvent();
/*  114 */   private final LinuxEvent tmp_event_buffer = new LinuxEvent();
/*      */ 
/*      */   
/*  117 */   private int current_displaymode_extension = 12;
/*      */   
/*      */   private long delete_atom;
/*      */   
/*      */   private PeerInfo peer_info;
/*      */   
/*      */   private ByteBuffer saved_gamma;
/*      */   
/*      */   private ByteBuffer current_gamma;
/*      */   
/*      */   private DisplayMode saved_mode;
/*      */   
/*      */   private DisplayMode current_mode;
/*      */   
/*      */   private boolean keyboard_grabbed;
/*      */   
/*      */   private boolean pointer_grabbed;
/*      */   
/*      */   private boolean input_released;
/*      */   
/*      */   private boolean grab;
/*      */   
/*      */   private boolean focused;
/*      */   private boolean minimized;
/*      */   private boolean dirty;
/*      */   private boolean close_requested;
/*      */   private long current_cursor;
/*      */   private long blank_cursor;
/*      */   private boolean mouseInside = true;
/*      */   private boolean resizable;
/*      */   private boolean resized;
/*      */   private int window_x;
/*      */   private int window_y;
/*      */   private int window_width;
/*      */   private int window_height;
/*      */   private Canvas parent;
/*      */   private long parent_window;
/*      */   private static boolean xembedded;
/*      */   private long parent_proxy_focus_window;
/*      */   private boolean parent_focused;
/*      */   private boolean parent_focus_changed;
/*  158 */   private long last_window_focus = 0L;
/*      */   
/*      */   private LinuxKeyboard keyboard;
/*      */   
/*      */   private LinuxMouse mouse;
/*      */   private String wm_class;
/*      */   
/*  165 */   private final FocusListener focus_listener = new FocusListener() {
/*      */       public void focusGained(FocusEvent e) {
/*  167 */         synchronized (GlobalLock.lock) {
/*  168 */           LinuxDisplay.this.parent_focused = true;
/*  169 */           LinuxDisplay.this.parent_focus_changed = true;
/*      */         } 
/*      */       }
/*      */       public void focusLost(FocusEvent e) {
/*  173 */         synchronized (GlobalLock.lock) {
/*  174 */           LinuxDisplay.this.parent_focused = false;
/*  175 */           LinuxDisplay.this.parent_focus_changed = true;
/*      */         } 
/*      */       }
/*      */     };
/*      */   
/*      */   private static ByteBuffer getCurrentGammaRamp() throws LWJGLException {
/*  181 */     lockAWT();
/*      */     try {
/*  183 */       incDisplay();
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  193 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getBestDisplayModeExtension() {
/*      */     int result;
/*  200 */     if (isXrandrSupported()) {
/*  201 */       LWJGLUtil.log("Using Xrandr for display mode switching");
/*  202 */       result = 10;
/*  203 */     } else if (isXF86VidModeSupported()) {
/*  204 */       LWJGLUtil.log("Using XF86VidMode for display mode switching");
/*  205 */       result = 11;
/*      */     } else {
/*  207 */       LWJGLUtil.log("No display mode extensions available");
/*  208 */       result = 12;
/*      */     } 
/*  210 */     return result;
/*      */   }
/*      */   
/*      */   private static boolean isXrandrSupported() {
/*  214 */     if (Display.getPrivilegedBoolean("LWJGL_DISABLE_XRANDR"))
/*  215 */       return false; 
/*  216 */     lockAWT();
/*      */     try {
/*  218 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  224 */     catch (LWJGLException e) {
/*  225 */       LWJGLUtil.log("Got exception while querying Xrandr support: " + e);
/*  226 */       return false;
/*      */     } finally {
/*  228 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isXF86VidModeSupported() {
/*  234 */     lockAWT();
/*      */     try {
/*  236 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  242 */     catch (LWJGLException e) {
/*  243 */       LWJGLUtil.log("Got exception while querying XF86VM support: " + e);
/*  244 */       return false;
/*      */     } finally {
/*  246 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isNetWMFullscreenSupported() throws LWJGLException {
/*  252 */     if (Display.getPrivilegedBoolean("LWJGL_DISABLE_NETWM"))
/*  253 */       return false; 
/*  254 */     lockAWT();
/*      */     try {
/*  256 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  262 */     catch (LWJGLException e) {
/*  263 */       LWJGLUtil.log("Got exception while querying NetWM support: " + e);
/*  264 */       return false;
/*      */     } finally {
/*  266 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void lockAWT() {
/*      */     try {
/*  277 */       nLockAWT();
/*  278 */     } catch (LWJGLException e) {
/*  279 */       LWJGLUtil.log("Caught exception while locking AWT: " + e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void unlockAWT() {
/*      */     try {
/*  286 */       nUnlockAWT();
/*  287 */     } catch (LWJGLException e) {
/*  288 */       LWJGLUtil.log("Caught exception while unlocking AWT: " + e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void incDisplay() throws LWJGLException {
/*  297 */     if (display_connection_usage_count == 0) {
/*      */       
/*      */       try {
/*  300 */         GLContext.loadOpenGLLibrary();
/*  301 */         GLContext.loadOpenGLLibrary();
/*  302 */       } catch (Throwable t) {}
/*      */       
/*  304 */       saved_error_handler = setErrorHandler();
/*  305 */       display = openDisplay();
/*      */     } 
/*      */     
/*  308 */     display_connection_usage_count++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int globalErrorHandler(long display, long event_ptr, long error_display, long serial, long error_code, long request_code, long minor_code) throws LWJGLException {
/*  316 */     if (xembedded && request_code == 42L) return 0;
/*      */     
/*  318 */     if (display == getDisplay()) {
/*  319 */       String error_msg = getErrorText(display, error_code);
/*  320 */       throw new LWJGLException("X Error - disp: 0x" + Long.toHexString(error_display) + " serial: " + serial + " error: " + error_msg + " request_code: " + request_code + " minor_code: " + minor_code);
/*  321 */     }  if (saved_error_handler != 0L)
/*  322 */       return callErrorHandler(saved_error_handler, display, event_ptr); 
/*  323 */     return 0;
/*      */   }
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
/*      */   static void decDisplay() {}
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
/*      */   private int getWindowMode(boolean fullscreen) throws LWJGLException {
/*  348 */     if (fullscreen) {
/*  349 */       if (this.current_displaymode_extension == 10 && isNetWMFullscreenSupported()) {
/*  350 */         LWJGLUtil.log("Using NetWM for fullscreen window");
/*  351 */         return 2;
/*      */       } 
/*  353 */       LWJGLUtil.log("Using legacy mode for fullscreen window");
/*  354 */       return 1;
/*      */     } 
/*      */     
/*  357 */     return 3;
/*      */   }
/*      */   
/*      */   static long getDisplay() {
/*  361 */     if (display_connection_usage_count <= 0)
/*  362 */       throw new InternalError("display_connection_usage_count = " + display_connection_usage_count); 
/*  363 */     return display;
/*      */   }
/*      */   
/*      */   static int getDefaultScreen() {
/*  367 */     return nGetDefaultScreen(getDisplay());
/*      */   }
/*      */ 
/*      */   
/*      */   static long getWindow() {
/*  372 */     return current_window;
/*      */   }
/*      */   
/*      */   private void ungrabKeyboard() {
/*  376 */     if (this.keyboard_grabbed) {
/*  377 */       nUngrabKeyboard(getDisplay());
/*  378 */       this.keyboard_grabbed = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void grabKeyboard() {
/*  384 */     if (!this.keyboard_grabbed) {
/*  385 */       int res = nGrabKeyboard(getDisplay(), getWindow());
/*  386 */       if (res == 0) {
/*  387 */         this.keyboard_grabbed = true;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void grabPointer() {
/*  393 */     if (!this.pointer_grabbed) {
/*  394 */       int result = nGrabPointer(getDisplay(), getWindow(), 0L);
/*  395 */       if (result == 0) {
/*  396 */         this.pointer_grabbed = true;
/*      */         
/*  398 */         if (isLegacyFullscreen()) {
/*  399 */           nSetViewPort(getDisplay(), getWindow(), getDefaultScreen());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void ungrabPointer() {
/*  408 */     if (this.pointer_grabbed) {
/*  409 */       this.pointer_grabbed = false;
/*  410 */       nUngrabPointer(getDisplay());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isFullscreen() {
/*  416 */     return (current_window_mode == 1 || current_window_mode == 2);
/*      */   }
/*      */   
/*      */   private boolean shouldGrab() {
/*  420 */     return (!this.input_released && this.grab && this.mouse != null);
/*      */   }
/*      */   
/*      */   private void updatePointerGrab() {
/*  424 */     if (isFullscreen() || shouldGrab()) {
/*  425 */       grabPointer();
/*      */     } else {
/*  427 */       ungrabPointer();
/*      */     } 
/*  429 */     updateCursor();
/*      */   }
/*      */   
/*      */   private void updateCursor() {
/*      */     long cursor;
/*  434 */     if (shouldGrab()) {
/*  435 */       cursor = this.blank_cursor;
/*      */     } else {
/*  437 */       cursor = this.current_cursor;
/*      */     } 
/*  439 */     nDefineCursor(getDisplay(), getWindow(), cursor);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isLegacyFullscreen() {
/*  444 */     return (current_window_mode == 1);
/*      */   }
/*      */   
/*      */   private void updateKeyboardGrab() {
/*  448 */     if (isLegacyFullscreen()) {
/*  449 */       grabKeyboard();
/*      */     } else {
/*  451 */       ungrabKeyboard();
/*      */     } 
/*      */   }
/*      */   public void createWindow(DrawableLWJGL drawable, DisplayMode mode, Canvas parent, int x, int y) throws LWJGLException {
/*  455 */     lockAWT();
/*      */     try {
/*  457 */       incDisplay();
/*      */       try {
/*  459 */         if (drawable instanceof DrawableGLES) {
/*  460 */           this.peer_info = new LinuxDisplayPeerInfo();
/*      */         }
/*  462 */         ByteBuffer handle = this.peer_info.lockAndGetHandle();
/*      */         try {
/*  464 */           current_window_mode = getWindowMode(Display.isFullscreen());
/*      */ 
/*      */ 
/*      */           
/*  468 */           if (current_window_mode != 3) {
/*  469 */             Compiz.setLegacyFullscreenSupport(true);
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  475 */           boolean undecorated = (Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated") || (current_window_mode != 3 && Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated_fs")));
/*      */           
/*  477 */           this.parent = parent;
/*  478 */           this.parent_window = (parent != null) ? getHandle(parent) : getRootWindow(getDisplay(), getDefaultScreen());
/*  479 */           this.resizable = Display.isResizable();
/*  480 */           this.resized = false;
/*  481 */           this.window_x = x;
/*  482 */           this.window_y = y;
/*  483 */           this.window_width = mode.getWidth();
/*  484 */           this.window_height = mode.getHeight();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  489 */           if (mode.isFullscreenCapable() && this.current_displaymode_extension == 10) {
/*  490 */             XRandR.Screen primaryScreen = XRandR.DisplayModetoScreen(Display.getDisplayMode());
/*  491 */             x = primaryScreen.xPos;
/*  492 */             y = primaryScreen.yPos;
/*      */           } 
/*      */           
/*  495 */           current_window = nCreateWindow(getDisplay(), getDefaultScreen(), handle, mode, current_window_mode, x, y, undecorated, this.parent_window, this.resizable);
/*      */ 
/*      */           
/*  498 */           this.wm_class = Display.getPrivilegedString("LWJGL_WM_CLASS");
/*  499 */           if (this.wm_class == null) this.wm_class = Display.getTitle(); 
/*  500 */           setClassHint(Display.getTitle(), this.wm_class);
/*      */           
/*  502 */           mapRaised(getDisplay(), current_window);
/*  503 */           xembedded = (parent != null && isAncestorXEmbedded(this.parent_window));
/*  504 */           this.blank_cursor = createBlankCursor();
/*  505 */           this.current_cursor = 0L;
/*  506 */           this.focused = false;
/*  507 */           this.input_released = false;
/*  508 */           this.pointer_grabbed = false;
/*  509 */           this.keyboard_grabbed = false;
/*  510 */           this.close_requested = false;
/*  511 */           this.grab = false;
/*  512 */           this.minimized = false;
/*  513 */           this.dirty = true;
/*      */           
/*  515 */           if (drawable instanceof DrawableGLES) {
/*  516 */             ((DrawableGLES)drawable).initialize(current_window, getDisplay(), 4, (PixelFormat)drawable.getPixelFormat());
/*      */           }
/*  518 */           if (parent != null) {
/*  519 */             parent.addFocusListener(this.focus_listener);
/*  520 */             this.parent_focused = parent.isFocusOwner();
/*  521 */             this.parent_focus_changed = true;
/*      */           } 
/*      */         } finally {
/*  524 */           this.peer_info.unlock();
/*      */         } 
/*  526 */       } catch (LWJGLException e) {
/*  527 */         decDisplay();
/*  528 */         throw e;
/*      */       } 
/*      */     } finally {
/*  531 */       unlockAWT();
/*      */     } 
/*      */   }
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
/*      */   private static boolean isAncestorXEmbedded(long window) throws LWJGLException {
/*  550 */     long xembed_atom = internAtom("_XEMBED_INFO", true);
/*  551 */     if (xembed_atom != 0L) {
/*  552 */       long w = window;
/*  553 */       while (w != 0L) {
/*  554 */         if (hasProperty(getDisplay(), w, xembed_atom))
/*  555 */           return true; 
/*  556 */         w = getParentWindow(getDisplay(), w);
/*      */       } 
/*      */     } 
/*  559 */     return false;
/*      */   }
/*      */   
/*      */   private static long getHandle(Canvas parent) throws LWJGLException {
/*  563 */     AWTCanvasImplementation awt_impl = AWTGLCanvas.createImplementation();
/*  564 */     LinuxPeerInfo parent_peer_info = (LinuxPeerInfo)awt_impl.createPeerInfo(parent, null, null);
/*  565 */     ByteBuffer parent_peer_info_handle = parent_peer_info.lockAndGetHandle();
/*      */     try {
/*  567 */       return parent_peer_info.getDrawable();
/*      */     } finally {
/*  569 */       parent_peer_info.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateInputGrab() {
/*  574 */     updatePointerGrab();
/*  575 */     updateKeyboardGrab();
/*      */   }
/*      */   
/*      */   public void destroyWindow() {
/*  579 */     lockAWT();
/*      */     try {
/*  581 */       if (this.parent != null) {
/*  582 */         this.parent.removeFocusListener(this.focus_listener);
/*      */       }
/*      */       try {
/*  585 */         setNativeCursor(null);
/*  586 */       } catch (LWJGLException e) {
/*  587 */         LWJGLUtil.log("Failed to reset cursor: " + e.getMessage());
/*      */       } 
/*  589 */       nDestroyCursor(getDisplay(), this.blank_cursor);
/*  590 */       this.blank_cursor = 0L;
/*  591 */       ungrabKeyboard();
/*  592 */       nDestroyWindow(getDisplay(), getWindow());
/*  593 */       decDisplay();
/*      */       
/*  595 */       if (current_window_mode != 3)
/*  596 */         Compiz.setLegacyFullscreenSupport(false); 
/*      */     } finally {
/*  598 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void switchDisplayMode(DisplayMode mode) throws LWJGLException {
/*  604 */     lockAWT();
/*      */     try {
/*  606 */       switchDisplayModeOnTmpDisplay(mode);
/*  607 */       this.current_mode = mode;
/*      */     } finally {
/*  609 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void switchDisplayModeOnTmpDisplay(DisplayMode mode) throws LWJGLException {
/*  614 */     if (this.current_displaymode_extension == 10) {
/*      */       
/*  616 */       XRandR.setConfiguration(false, new XRandR.Screen[] { XRandR.DisplayModetoScreen(mode) });
/*      */     } else {
/*  618 */       incDisplay();
/*      */       try {
/*  620 */         nSwitchDisplayMode(getDisplay(), getDefaultScreen(), this.current_displaymode_extension, mode);
/*      */       } finally {
/*  622 */         decDisplay();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static long internAtom(String atom_name, boolean only_if_exists) throws LWJGLException {
/*  629 */     incDisplay();
/*      */     try {
/*  631 */       return nInternAtom(getDisplay(), atom_name, only_if_exists);
/*      */     } finally {
/*  633 */       decDisplay();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetDisplayMode() {
/*  639 */     lockAWT();
/*      */     try {
/*  641 */       if (this.current_displaymode_extension == 10) {
/*      */         
/*  643 */         AccessController.doPrivileged(new PrivilegedAction() {
/*      */               public Object run() {
/*  645 */                 XRandR.restoreConfiguration();
/*  646 */                 return null;
/*      */               }
/*      */             });
/*      */       }
/*      */       else {
/*      */         
/*  652 */         switchDisplayMode(this.saved_mode);
/*      */       } 
/*  654 */       if (isXF86VidModeSupported()) {
/*  655 */         doSetGamma(this.saved_gamma);
/*      */       }
/*  657 */       Compiz.setLegacyFullscreenSupport(false);
/*  658 */     } catch (LWJGLException e) {
/*  659 */       LWJGLUtil.log("Caught exception while resetting mode: " + e);
/*      */     } finally {
/*  661 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getGammaRampLength() {
/*  666 */     if (!isXF86VidModeSupported())
/*  667 */       return 0; 
/*  668 */     lockAWT();
/*      */     
/*      */     try {
/*  671 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  680 */     catch (LWJGLException e) {
/*  681 */       LWJGLUtil.log("Failed to get gamma ramp length: " + e);
/*  682 */       return 0;
/*      */     } finally {
/*      */       
/*  685 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException {
/*  691 */     if (!isXF86VidModeSupported())
/*  692 */       throw new LWJGLException("No gamma ramp support (Missing XF86VM extension)"); 
/*  693 */     doSetGamma(convertToNativeRamp(gammaRamp));
/*      */   }
/*      */   
/*      */   private void doSetGamma(ByteBuffer native_gamma) throws LWJGLException {
/*  697 */     lockAWT();
/*      */     try {
/*  699 */       setGammaRampOnTmpDisplay(native_gamma);
/*  700 */       this.current_gamma = native_gamma;
/*      */     } finally {
/*  702 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setGammaRampOnTmpDisplay(ByteBuffer native_gamma) throws LWJGLException {
/*  707 */     incDisplay();
/*      */     try {
/*  709 */       nSetGammaRamp(getDisplay(), getDefaultScreen(), native_gamma);
/*      */     } finally {
/*  711 */       decDisplay();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static ByteBuffer convertToNativeRamp(FloatBuffer ramp) throws LWJGLException {
/*  717 */     return nConvertToNativeRamp(ramp, ramp.position(), ramp.remaining());
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAdapter() {
/*  722 */     return null;
/*      */   }
/*      */   
/*      */   public String getVersion() {
/*  726 */     return null;
/*      */   }
/*      */   
/*      */   public DisplayMode init() throws LWJGLException {
/*  730 */     lockAWT(); 
/*      */     try { DisplayMode displayMode;
/*  732 */       Compiz.init();
/*      */       
/*  734 */       this.delete_atom = internAtom("WM_DELETE_WINDOW", false);
/*  735 */       this.current_displaymode_extension = getBestDisplayModeExtension();
/*  736 */       if (this.current_displaymode_extension == 12)
/*  737 */         throw new LWJGLException("No display mode extension is available"); 
/*  738 */       DisplayMode[] modes = getAvailableDisplayModes();
/*  739 */       if (modes == null || modes.length == 0)
/*  740 */         throw new LWJGLException("No modes available"); 
/*  741 */       switch (this.current_displaymode_extension)
/*      */       { case 10:
/*  743 */           this.saved_mode = AccessController.<DisplayMode>doPrivileged(new PrivilegedAction<DisplayMode>() {
/*      */                 public DisplayMode run() {
/*  745 */                   XRandR.saveConfiguration();
/*  746 */                   return XRandR.ScreentoDisplayMode(XRandR.getConfiguration());
/*      */                 }
/*      */               });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  756 */           this.current_mode = this.saved_mode;
/*  757 */           this.saved_gamma = getCurrentGammaRamp();
/*  758 */           this.current_gamma = this.saved_gamma;
/*  759 */           displayMode = this.saved_mode;
/*      */           
/*  761 */           return displayMode;case 11: this.saved_mode = modes[0]; this.current_mode = this.saved_mode; this.saved_gamma = getCurrentGammaRamp(); this.current_gamma = this.saved_gamma; displayMode = this.saved_mode; return displayMode; }  throw new LWJGLException("Unknown display mode extension: " + this.current_displaymode_extension); } finally { unlockAWT(); }
/*      */   
/*      */   }
/*      */   
/*      */   private static DisplayMode getCurrentXRandrMode() throws LWJGLException {
/*  766 */     lockAWT();
/*      */     try {
/*  768 */       incDisplay();
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/*  775 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTitle(String title) {
/*  783 */     lockAWT();
/*      */     try {
/*  785 */       ByteBuffer titleText = MemoryUtil.encodeUTF8(title);
/*  786 */       nSetTitle(getDisplay(), getWindow(), MemoryUtil.getAddress(titleText), titleText.remaining() - 1);
/*      */     } finally {
/*  788 */       unlockAWT();
/*      */     } 
/*      */ 
/*      */     
/*  792 */     if (Display.isCreated()) setClassHint(title, this.wm_class);
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private void setClassHint(String wm_name, String wm_class) {
/*  798 */     lockAWT();
/*      */     try {
/*  800 */       ByteBuffer nameText = MemoryUtil.encodeUTF8(wm_name);
/*  801 */       ByteBuffer classText = MemoryUtil.encodeUTF8(wm_class);
/*      */       
/*  803 */       nSetClassHint(getDisplay(), getWindow(), MemoryUtil.getAddress(nameText), MemoryUtil.getAddress(classText));
/*      */     } finally {
/*  805 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCloseRequested() {
/*  811 */     boolean result = this.close_requested;
/*  812 */     this.close_requested = false;
/*  813 */     return result;
/*      */   }
/*      */   
/*      */   public boolean isVisible() {
/*  817 */     return !this.minimized;
/*      */   }
/*      */   
/*      */   public boolean isActive() {
/*  821 */     return (this.focused || isLegacyFullscreen());
/*      */   }
/*      */   
/*      */   public boolean isDirty() {
/*  825 */     boolean result = this.dirty;
/*  826 */     this.dirty = false;
/*  827 */     return result;
/*      */   }
/*      */   
/*      */   public PeerInfo createPeerInfo(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
/*  831 */     this.peer_info = new LinuxDisplayPeerInfo(pixel_format);
/*  832 */     return this.peer_info;
/*      */   }
/*      */   
/*      */   private void relayEventToParent(LinuxEvent event_buffer, int event_mask) {
/*  836 */     this.tmp_event_buffer.copyFrom(event_buffer);
/*  837 */     this.tmp_event_buffer.setWindow(this.parent_window);
/*  838 */     this.tmp_event_buffer.sendEvent(getDisplay(), this.parent_window, true, event_mask);
/*      */   }
/*      */   
/*      */   private void relayEventToParent(LinuxEvent event_buffer) {
/*  842 */     if (this.parent == null)
/*      */       return; 
/*  844 */     switch (event_buffer.getType()) {
/*      */       case 2:
/*  846 */         relayEventToParent(event_buffer, 1);
/*      */         break;
/*      */       case 3:
/*  849 */         relayEventToParent(event_buffer, 1);
/*      */         break;
/*      */       case 4:
/*  852 */         if (xembedded || !this.focused) relayEventToParent(event_buffer, 1); 
/*      */         break;
/*      */       case 5:
/*  855 */         if (xembedded || !this.focused) relayEventToParent(event_buffer, 1);
/*      */         
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void processEvents() {
/*  863 */     while (LinuxEvent.getPending(getDisplay()) > 0) {
/*  864 */       int x, y, width, height; this.event_buffer.nextEvent(getDisplay());
/*  865 */       long event_window = this.event_buffer.getWindow();
/*  866 */       relayEventToParent(this.event_buffer);
/*  867 */       if (event_window != getWindow() || this.event_buffer.filterEvent(event_window) || (this.mouse != null && this.mouse.filterEvent(this.grab, shouldWarpPointer(), this.event_buffer)) || (this.keyboard != null && this.keyboard.filterEvent(this.event_buffer))) {
/*      */         continue;
/*      */       }
/*      */       
/*  871 */       switch (this.event_buffer.getType()) {
/*      */         case 9:
/*  873 */           setFocused(true, this.event_buffer.getFocusDetail());
/*      */         
/*      */         case 10:
/*  876 */           setFocused(false, this.event_buffer.getFocusDetail());
/*      */         
/*      */         case 33:
/*  879 */           if (this.event_buffer.getClientFormat() == 32 && this.event_buffer.getClientData(0) == this.delete_atom) {
/*  880 */             this.close_requested = true;
/*      */           }
/*      */         case 19:
/*  883 */           this.dirty = true;
/*  884 */           this.minimized = false;
/*      */         
/*      */         case 18:
/*  887 */           this.dirty = true;
/*  888 */           this.minimized = true;
/*      */         
/*      */         case 12:
/*  891 */           this.dirty = true;
/*      */         
/*      */         case 22:
/*  894 */           x = nGetX(getDisplay(), getWindow());
/*  895 */           y = nGetY(getDisplay(), getWindow());
/*      */           
/*  897 */           width = nGetWidth(getDisplay(), getWindow());
/*  898 */           height = nGetHeight(getDisplay(), getWindow());
/*      */           
/*  900 */           this.window_x = x;
/*  901 */           this.window_y = y;
/*      */           
/*  903 */           if (this.window_width != width || this.window_height != height) {
/*  904 */             this.resized = true;
/*  905 */             this.window_width = width;
/*  906 */             this.window_height = height;
/*      */           } 
/*      */ 
/*      */         
/*      */         case 7:
/*  911 */           this.mouseInside = true;
/*      */         
/*      */         case 8:
/*  914 */           this.mouseInside = false;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update() {
/*  923 */     lockAWT();
/*      */     try {
/*  925 */       processEvents();
/*  926 */       checkInput();
/*      */     } finally {
/*  928 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void reshape(int x, int y, int width, int height) {
/*  933 */     lockAWT();
/*      */     try {
/*  935 */       nReshape(getDisplay(), getWindow(), x, y, width, height);
/*      */     } finally {
/*  937 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
/*  943 */     lockAWT();
/*      */     try {
/*  945 */       incDisplay();
/*  946 */       if (this.current_displaymode_extension == 10)
/*      */       {
/*  948 */         DisplayMode[] nDisplayModes = nGetAvailableDisplayModes(getDisplay(), getDefaultScreen(), this.current_displaymode_extension);
/*  949 */         int bpp = 24;
/*  950 */         if (nDisplayModes.length > 0) {
/*  951 */           bpp = nDisplayModes[0].getBitsPerPixel();
/*      */         }
/*      */         
/*  954 */         XRandR.Screen[] resolutions = XRandR.getResolutions(XRandR.getScreenNames()[0]);
/*  955 */         DisplayMode[] modes = new DisplayMode[resolutions.length];
/*  956 */         for (int i = 0; i < modes.length; i++) {
/*  957 */           modes[i] = new DisplayMode((resolutions[i]).width, (resolutions[i]).height, bpp, (resolutions[i]).freq);
/*      */         }
/*  959 */         return modes;
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/*  969 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWheel() {
/*  976 */     return true;
/*      */   }
/*      */   
/*      */   public int getButtonCount() {
/*  980 */     return this.mouse.getButtonCount();
/*      */   }
/*      */   
/*      */   public void createMouse() throws LWJGLException {
/*  984 */     lockAWT();
/*      */     try {
/*  986 */       this.mouse = new LinuxMouse(getDisplay(), getWindow(), getWindow());
/*      */     } finally {
/*  988 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void destroyMouse() {
/*  993 */     this.mouse = null;
/*  994 */     updateInputGrab();
/*      */   }
/*      */   
/*      */   public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons) {
/*  998 */     lockAWT();
/*      */     try {
/* 1000 */       this.mouse.poll(this.grab, coord_buffer, buttons);
/*      */     } finally {
/* 1002 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void readMouse(ByteBuffer buffer) {
/* 1007 */     lockAWT();
/*      */     try {
/* 1009 */       this.mouse.read(buffer);
/*      */     } finally {
/* 1011 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setCursorPosition(int x, int y) {
/* 1016 */     lockAWT();
/*      */     try {
/* 1018 */       this.mouse.setCursorPosition(x, y);
/*      */     } finally {
/* 1020 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkInput() {
/* 1025 */     if (this.parent == null)
/*      */       return; 
/* 1027 */     if (xembedded) {
/* 1028 */       long current_focus_window = 0L;
/*      */       
/* 1030 */       if (this.last_window_focus != current_focus_window || this.parent_focused != this.focused) {
/* 1031 */         if (isParentWindowActive(current_focus_window)) {
/* 1032 */           if (this.parent_focused) {
/* 1033 */             nSetInputFocus(getDisplay(), current_window, 0L);
/* 1034 */             this.last_window_focus = current_window;
/* 1035 */             this.focused = true;
/*      */           }
/*      */           else {
/*      */             
/* 1039 */             nSetInputFocus(getDisplay(), this.parent_proxy_focus_window, 0L);
/* 1040 */             this.last_window_focus = this.parent_proxy_focus_window;
/* 1041 */             this.focused = false;
/*      */           } 
/*      */         } else {
/*      */           
/* 1045 */           this.last_window_focus = current_focus_window;
/* 1046 */           this.focused = false;
/*      */         }
/*      */       
/*      */       }
/*      */     }
/* 1051 */     else if (this.parent_focus_changed && this.parent_focused) {
/* 1052 */       setInputFocusUnsafe(getWindow());
/* 1053 */       this.parent_focus_changed = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setInputFocusUnsafe(long window) {
/*      */     try {
/* 1060 */       nSetInputFocus(getDisplay(), window, 0L);
/* 1061 */       nSync(getDisplay(), false);
/* 1062 */     } catch (LWJGLException e) {
/*      */       
/* 1064 */       LWJGLUtil.log("Got exception while trying to focus: " + e);
/*      */     } 
/*      */   }
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
/*      */   private boolean isParentWindowActive(long window) {
/*      */     try {
/* 1083 */       if (window == current_window) return true;
/*      */ 
/*      */       
/* 1086 */       if (getChildCount(getDisplay(), window) != 0) return false;
/*      */ 
/*      */       
/* 1089 */       long parent_window = getParentWindow(getDisplay(), window);
/*      */ 
/*      */       
/* 1092 */       if (parent_window == 0L) return false;
/*      */ 
/*      */       
/* 1095 */       long w = current_window;
/*      */       
/* 1097 */       while (w != 0L) {
/* 1098 */         w = getParentWindow(getDisplay(), w);
/* 1099 */         if (w == parent_window) {
/* 1100 */           this.parent_proxy_focus_window = window;
/* 1101 */           return true;
/*      */         } 
/*      */       } 
/* 1104 */     } catch (LWJGLException e) {
/* 1105 */       LWJGLUtil.log("Failed to detect if parent window is active: " + e.getMessage());
/* 1106 */       return true;
/*      */     } 
/*      */     
/* 1109 */     return false;
/*      */   }
/*      */   
/*      */   private void setFocused(boolean got_focus, int focus_detail) {
/* 1113 */     if (this.focused == got_focus || focus_detail == 7 || focus_detail == 5 || focus_detail == 6 || xembedded)
/*      */       return; 
/* 1115 */     this.focused = got_focus;
/*      */     
/* 1117 */     if (this.focused) {
/* 1118 */       acquireInput();
/*      */     } else {
/*      */       
/* 1121 */       releaseInput();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void releaseInput() {
/* 1126 */     if (isLegacyFullscreen() || this.input_released)
/*      */       return; 
/* 1128 */     if (this.keyboard != null)
/* 1129 */       this.keyboard.releaseAll(); 
/* 1130 */     this.input_released = true;
/* 1131 */     updateInputGrab();
/* 1132 */     if (current_window_mode == 2) {
/* 1133 */       nIconifyWindow(getDisplay(), getWindow(), getDefaultScreen());
/*      */       try {
/* 1135 */         if (this.current_displaymode_extension == 10) {
/*      */           
/* 1137 */           AccessController.doPrivileged(new PrivilegedAction() {
/*      */                 public Object run() {
/* 1139 */                   XRandR.restoreConfiguration();
/* 1140 */                   return null;
/*      */                 }
/*      */               });
/*      */         }
/*      */         else {
/*      */           
/* 1146 */           switchDisplayModeOnTmpDisplay(this.saved_mode);
/*      */         } 
/* 1148 */         setGammaRampOnTmpDisplay(this.saved_gamma);
/* 1149 */       } catch (LWJGLException e) {
/* 1150 */         LWJGLUtil.log("Failed to restore saved mode: " + e.getMessage());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void acquireInput() {
/* 1157 */     if (isLegacyFullscreen() || !this.input_released)
/*      */       return; 
/* 1159 */     this.input_released = false;
/* 1160 */     updateInputGrab();
/* 1161 */     if (current_window_mode == 2) {
/*      */       try {
/* 1163 */         switchDisplayModeOnTmpDisplay(this.current_mode);
/* 1164 */         setGammaRampOnTmpDisplay(this.current_gamma);
/* 1165 */       } catch (LWJGLException e) {
/* 1166 */         LWJGLUtil.log("Failed to restore mode: " + e.getMessage());
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void grabMouse(boolean new_grab) {
/* 1172 */     lockAWT();
/*      */     try {
/* 1174 */       if (new_grab != this.grab) {
/* 1175 */         this.grab = new_grab;
/* 1176 */         updateInputGrab();
/* 1177 */         this.mouse.changeGrabbed(this.grab, shouldWarpPointer());
/*      */       } 
/*      */     } finally {
/* 1180 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldWarpPointer() {
/* 1185 */     return (this.pointer_grabbed && shouldGrab());
/*      */   }
/*      */   
/*      */   public int getNativeCursorCapabilities() {
/* 1189 */     lockAWT();
/*      */     try {
/* 1191 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1197 */     catch (LWJGLException e) {
/* 1198 */       throw new RuntimeException(e);
/*      */     } finally {
/* 1200 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNativeCursor(Object handle) throws LWJGLException {
/* 1206 */     this.current_cursor = getCursorHandle(handle);
/* 1207 */     lockAWT();
/*      */     try {
/* 1209 */       updateCursor();
/*      */     } finally {
/* 1211 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getMinCursorSize() {
/* 1216 */     lockAWT();
/*      */     try {
/* 1218 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1224 */     catch (LWJGLException e) {
/* 1225 */       LWJGLUtil.log("Exception occurred in getMinCursorSize: " + e);
/* 1226 */       return 0;
/*      */     } finally {
/* 1228 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxCursorSize() {
/* 1234 */     lockAWT();
/*      */     try {
/* 1236 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1242 */     catch (LWJGLException e) {
/* 1243 */       LWJGLUtil.log("Exception occurred in getMaxCursorSize: " + e);
/* 1244 */       return 0;
/*      */     } finally {
/* 1246 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void createKeyboard() throws LWJGLException {
/* 1253 */     lockAWT();
/*      */     try {
/* 1255 */       this.keyboard = new LinuxKeyboard(getDisplay(), getWindow());
/*      */     } finally {
/* 1257 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void destroyKeyboard() {
/* 1262 */     lockAWT();
/*      */     try {
/* 1264 */       this.keyboard.destroy(getDisplay());
/* 1265 */       this.keyboard = null;
/*      */     } finally {
/* 1267 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void pollKeyboard(ByteBuffer keyDownBuffer) {
/* 1272 */     lockAWT();
/*      */     try {
/* 1274 */       this.keyboard.poll(keyDownBuffer);
/*      */     } finally {
/* 1276 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void readKeyboard(ByteBuffer buffer) {
/* 1281 */     lockAWT();
/*      */     try {
/* 1283 */       this.keyboard.read(buffer);
/*      */     } finally {
/* 1285 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static long createBlankCursor() {
/* 1292 */     return nCreateBlankCursor(getDisplay(), getWindow());
/*      */   }
/*      */ 
/*      */   
/*      */   public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
/* 1297 */     lockAWT();
/*      */     try {
/* 1299 */       incDisplay();
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */ 
/*      */       
/* 1308 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static long getCursorHandle(Object cursor_handle) {
/* 1313 */     return (cursor_handle != null) ? ((Long)cursor_handle).longValue() : 0L;
/*      */   }
/*      */   
/*      */   public void destroyCursor(Object cursorHandle) {
/* 1317 */     lockAWT();
/*      */     try {
/* 1319 */       nDestroyCursor(getDisplay(), getCursorHandle(cursorHandle));
/* 1320 */       decDisplay();
/*      */     } finally {
/* 1322 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPbufferCapabilities() {
/* 1328 */     lockAWT();
/*      */     try {
/* 1330 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1336 */     catch (LWJGLException e) {
/* 1337 */       LWJGLUtil.log("Exception occurred in getPbufferCapabilities: " + e);
/* 1338 */       return 0;
/*      */     } finally {
/* 1340 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBufferLost(PeerInfo handle) {
/* 1346 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format, ContextAttribs attribs, IntBuffer pixelFormatCaps, IntBuffer pBufferAttribs) throws LWJGLException {
/* 1352 */     return new LinuxPbufferPeerInfo(width, height, pixel_format);
/*      */   }
/*      */   
/*      */   public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
/* 1356 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
/* 1360 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */   public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
/* 1364 */     throw new UnsupportedOperationException();
/*      */   }
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
/*      */   private static ByteBuffer convertIcons(ByteBuffer[] icons) {
/* 1377 */     int bufferSize = 0;
/*      */ 
/*      */     
/* 1380 */     for (ByteBuffer icon : icons) {
/* 1381 */       int size = icon.limit() / 4;
/* 1382 */       int dimension = (int)Math.sqrt(size);
/* 1383 */       if (dimension > 0) {
/* 1384 */         bufferSize += 8;
/* 1385 */         bufferSize += dimension * dimension * 4;
/*      */       } 
/*      */     } 
/*      */     
/* 1389 */     if (bufferSize == 0) return null;
/*      */     
/* 1391 */     ByteBuffer icon_argb = BufferUtils.createByteBuffer(bufferSize);
/* 1392 */     icon_argb.order(ByteOrder.BIG_ENDIAN);
/*      */     
/* 1394 */     for (ByteBuffer icon : icons) {
/* 1395 */       int size = icon.limit() / 4;
/* 1396 */       int dimension = (int)Math.sqrt(size);
/*      */       
/* 1398 */       icon_argb.putInt(dimension);
/* 1399 */       icon_argb.putInt(dimension);
/*      */       
/* 1401 */       for (int y = 0; y < dimension; y++) {
/* 1402 */         for (int x = 0; x < dimension; x++) {
/*      */           
/* 1404 */           byte r = icon.get(x * 4 + y * dimension * 4);
/* 1405 */           byte g = icon.get(x * 4 + y * dimension * 4 + 1);
/* 1406 */           byte b = icon.get(x * 4 + y * dimension * 4 + 2);
/* 1407 */           byte a = icon.get(x * 4 + y * dimension * 4 + 3);
/*      */           
/* 1409 */           icon_argb.put(a);
/* 1410 */           icon_argb.put(r);
/* 1411 */           icon_argb.put(g);
/* 1412 */           icon_argb.put(b);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1417 */     return icon_argb;
/*      */   }
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
/*      */   public int setIcon(ByteBuffer[] icons) {
/* 1433 */     lockAWT();
/*      */     try {
/* 1435 */       incDisplay();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1445 */     catch (LWJGLException e) {
/* 1446 */       LWJGLUtil.log("Failed to set display icon: " + e);
/* 1447 */       return 0;
/*      */     } finally {
/* 1449 */       unlockAWT();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getX() {
/* 1456 */     return this.window_x;
/*      */   }
/*      */   
/*      */   public int getY() {
/* 1460 */     return this.window_y;
/*      */   }
/*      */   
/*      */   public int getWidth() {
/* 1464 */     return this.window_width;
/*      */   }
/*      */   
/*      */   public int getHeight() {
/* 1468 */     return this.window_height;
/*      */   }
/*      */   
/*      */   public boolean isInsideWindow() {
/* 1472 */     return this.mouseInside;
/*      */   }
/*      */   
/*      */   public void setResizable(boolean resizable) {
/* 1476 */     if (this.resizable == resizable) {
/*      */       return;
/*      */     }
/*      */     
/* 1480 */     this.resizable = resizable;
/* 1481 */     nSetWindowSize(getDisplay(), getWindow(), this.window_width, this.window_height, resizable);
/*      */   }
/*      */   
/*      */   public boolean wasResized() {
/* 1485 */     if (this.resized) {
/* 1486 */       this.resized = false;
/* 1487 */       return true;
/*      */     } 
/*      */     
/* 1490 */     return false;
/*      */   }
/*      */   
/*      */   public float getPixelScaleFactor() {
/* 1494 */     return 1.0F;
/*      */   } private static native ByteBuffer nGetCurrentGammaRamp(long paramLong, int paramInt) throws LWJGLException; private static native boolean nIsXrandrSupported(long paramLong) throws LWJGLException; private static native boolean nIsXF86VidModeSupported(long paramLong) throws LWJGLException; private static native boolean nIsNetWMFullscreenSupported(long paramLong, int paramInt) throws LWJGLException; private static native void nLockAWT() throws LWJGLException; private static native void nUnlockAWT() throws LWJGLException; private static native int callErrorHandler(long paramLong1, long paramLong2, long paramLong3); private static native long setErrorHandler(); private static native long resetErrorHandler(long paramLong); private static native void synchronize(long paramLong, boolean paramBoolean); private static native String getErrorText(long paramLong1, long paramLong2); static native long openDisplay() throws LWJGLException; static native void closeDisplay(long paramLong); static native int nGetDefaultScreen(long paramLong); static native int nUngrabKeyboard(long paramLong); static native int nGrabKeyboard(long paramLong1, long paramLong2); static native int nGrabPointer(long paramLong1, long paramLong2, long paramLong3); private static native void nSetViewPort(long paramLong1, long paramLong2, int paramInt); static native int nUngrabPointer(long paramLong); private static native void nDefineCursor(long paramLong1, long paramLong2, long paramLong3); private static native long nCreateWindow(long paramLong1, int paramInt1, ByteBuffer paramByteBuffer, DisplayMode paramDisplayMode, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, long paramLong2, boolean paramBoolean2) throws LWJGLException; private static native long getRootWindow(long paramLong, int paramInt); private static native boolean hasProperty(long paramLong1, long paramLong2, long paramLong3); private static native long getParentWindow(long paramLong1, long paramLong2) throws LWJGLException; private static native int getChildCount(long paramLong1, long paramLong2) throws LWJGLException; private static native void mapRaised(long paramLong1, long paramLong2); private static native void reparentWindow(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2); private static native long nGetInputFocus(long paramLong) throws LWJGLException; private static native void nSetInputFocus(long paramLong1, long paramLong2, long paramLong3); private static native void nSetWindowSize(long paramLong1, long paramLong2, int paramInt1, int paramInt2, boolean paramBoolean); private static native int nGetX(long paramLong1, long paramLong2); private static native int nGetY(long paramLong1, long paramLong2); private static native int nGetWidth(long paramLong1, long paramLong2); private static native int nGetHeight(long paramLong1, long paramLong2); static native void nDestroyWindow(long paramLong1, long paramLong2); private static native void nSwitchDisplayMode(long paramLong, int paramInt1, int paramInt2, DisplayMode paramDisplayMode) throws LWJGLException; static native long nInternAtom(long paramLong, String paramString, boolean paramBoolean); private static native int nGetGammaRampLength(long paramLong, int paramInt) throws LWJGLException;
/*      */   private static native void nSetGammaRamp(long paramLong, int paramInt, ByteBuffer paramByteBuffer) throws LWJGLException;
/*      */   private static native ByteBuffer nConvertToNativeRamp(FloatBuffer paramFloatBuffer, int paramInt1, int paramInt2) throws LWJGLException;
/*      */   private static native DisplayMode nGetCurrentXRandrMode(long paramLong, int paramInt) throws LWJGLException;
/*      */   private static native void nSetTitle(long paramLong1, long paramLong2, long paramLong3, int paramInt);
/*      */   private static native void nSetClassHint(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   private static native void nReshape(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */   private static native DisplayMode[] nGetAvailableDisplayModes(long paramLong, int paramInt1, int paramInt2) throws LWJGLException;
/*      */   private static native void nSync(long paramLong, boolean paramBoolean) throws LWJGLException;
/*      */   private static native void nIconifyWindow(long paramLong1, long paramLong2, int paramInt);
/*      */   private static native int nGetNativeCursorCapabilities(long paramLong) throws LWJGLException;
/*      */   private static native int nGetMinCursorSize(long paramLong1, long paramLong2);
/*      */   private static native int nGetMaxCursorSize(long paramLong1, long paramLong2);
/*      */   private static native long nCreateCursor(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, IntBuffer paramIntBuffer1, int paramInt6, IntBuffer paramIntBuffer2, int paramInt7) throws LWJGLException;
/*      */   static native long nCreateBlankCursor(long paramLong1, long paramLong2);
/*      */   static native void nDestroyCursor(long paramLong1, long paramLong2);
/*      */   private static native int nGetPbufferCapabilities(long paramLong, int paramInt);
/*      */   private static native void nSetWindowIcon(long paramLong1, long paramLong2, ByteBuffer paramByteBuffer, int paramInt);
/*      */   private static final class Compiz { private static boolean applyFix;
/*      */     static void init() {
/* 1515 */       if (Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.nocompiz_lfs")) {
/*      */         return;
/*      */       }
/* 1518 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */           {
/*      */             public Object run() {
/*      */               
/* 1522 */               try { if (!LinuxDisplay.Compiz.isProcessActive("compiz"))
/*      */                 {
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
/* 1613 */                   return null; }  LinuxDisplay.Compiz.provider = null; String providerName = null; if (LinuxDisplay.Compiz.isProcessActive("dbus-daemon")) { providerName = "Dbus"; LinuxDisplay.Compiz.provider = new LinuxDisplay.Compiz.Provider() { private static final String KEY = "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen"; public boolean hasLegacyFullscreenSupport() throws LWJGLException { List<String> output = LinuxDisplay.Compiz.run(new String[] { "dbus-send", "--print-reply", "--type=method_call", "--dest=org.freedesktop.compiz", "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen", "org.freedesktop.compiz.get" }); if (output == null || output.size() < 2) throw new LWJGLException("Invalid Dbus reply.");  String line = output.get(0); if (!line.startsWith("method return")) throw new LWJGLException("Invalid Dbus reply.");  line = ((String)output.get(1)).trim(); if (!line.startsWith("boolean") || line.length() < 12) throw new LWJGLException("Invalid Dbus reply.");  return "true".equalsIgnoreCase(line.substring("boolean".length() + 1)); } public void setLegacyFullscreenSupport(boolean state) throws LWJGLException { if (LinuxDisplay.Compiz.run(new String[] { "dbus-send", "--type=method_call", "--dest=org.freedesktop.compiz", "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen", "org.freedesktop.compiz.set", "boolean:" + Boolean.toString(state) }) == null) throw new LWJGLException("Failed to apply Compiz LFS workaround.");  } }; } else { try { Runtime.getRuntime().exec("gconftool"); providerName = "gconftool"; LinuxDisplay.Compiz.provider = new LinuxDisplay.Compiz.Provider() { private static final String KEY = "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen"; public boolean hasLegacyFullscreenSupport() throws LWJGLException { List<String> output = LinuxDisplay.Compiz.run(new String[] { "gconftool", "-g", "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen" }); if (output == null || output.size() == 0) throw new LWJGLException("Invalid gconftool reply.");  return Boolean.parseBoolean(((String)output.get(0)).trim()); } public void setLegacyFullscreenSupport(boolean state) throws LWJGLException { if (LinuxDisplay.Compiz.run(new String[] { "gconftool", "-s", "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen", "-s", Boolean.toString(state), "-t", "bool" }) == null) throw new LWJGLException("Failed to apply Compiz LFS workaround.");  if (state) try { Thread.sleep(200L); } catch (InterruptedException e) { e.printStackTrace(); }   } }; } catch (IOException e) {} }  return null; } catch (LWJGLException e) { return null; } finally { Exception exception = null; }
/*      */             
/*      */             }
/*      */           });
/*      */     }
/*      */     private static Provider provider;
/*      */     static void setLegacyFullscreenSupport(final boolean enabled) {
/* 1620 */       if (!applyFix) {
/*      */         return;
/*      */       }
/* 1623 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */             public Object run() {
/*      */               try {
/* 1626 */                 LinuxDisplay.Compiz.provider.setLegacyFullscreenSupport(enabled);
/* 1627 */               } catch (LWJGLException e) {
/* 1628 */                 LWJGLUtil.log("Failed to change Compiz Legacy Fullscreen Support. Reason: " + e.getMessage());
/*      */               } 
/* 1630 */               return null;
/*      */             }
/*      */           });
/*      */     }
/*      */     
/*      */     private static List<String> run(String... command) throws LWJGLException {
/* 1636 */       List<String> output = new ArrayList<String>();
/*      */       
/*      */       try {
/* 1639 */         Process p = Runtime.getRuntime().exec(command);
/*      */         try {
/* 1641 */           int exitValue = p.waitFor();
/* 1642 */           if (exitValue != 0)
/* 1643 */             return null; 
/* 1644 */         } catch (InterruptedException e) {
/* 1645 */           throw new LWJGLException("Process interrupted.", e);
/*      */         } 
/*      */         
/* 1648 */         BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*      */         
/*      */         String line;
/* 1651 */         while ((line = br.readLine()) != null) {
/* 1652 */           output.add(line);
/*      */         }
/* 1654 */         br.close();
/* 1655 */       } catch (IOException e) {
/* 1656 */         throw new LWJGLException("Process failed.", e);
/*      */       } 
/*      */       
/* 1659 */       return output;
/*      */     }
/*      */     
/*      */     private static boolean isProcessActive(String processName) throws LWJGLException {
/* 1663 */       List<String> output = run(new String[] { "ps", "-C", processName });
/* 1664 */       if (output == null) {
/* 1665 */         return false;
/*      */       }
/* 1667 */       for (String line : output) {
/* 1668 */         if (line.contains(processName)) {
/* 1669 */           return true;
/*      */         }
/*      */       } 
/* 1672 */       return false;
/*      */     }
/*      */     
/*      */     private static interface Provider {
/*      */       boolean hasLegacyFullscreenSupport() throws LWJGLException;
/*      */       
/*      */       void setLegacyFullscreenSupport(boolean param2Boolean) throws LWJGLException;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\LinuxDisplay.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */