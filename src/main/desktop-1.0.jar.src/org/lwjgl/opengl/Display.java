/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.awt.Canvas;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import org.lwjgl.BufferUtils;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.LWJGLUtil;
/*      */ import org.lwjgl.Sys;
/*      */ import org.lwjgl.input.Controllers;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengles.ContextAttribs;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Display
/*      */ {
/*   67 */   private static final Thread shutdown_hook = new Thread() {
/*      */       public void run() {
/*   69 */         Display.reset();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*      */   private static final DisplayImplementation display_impl;
/*      */ 
/*      */   
/*      */   private static final DisplayMode initial_mode;
/*      */ 
/*      */   
/*      */   private static Canvas parent;
/*      */ 
/*      */   
/*      */   private static DisplayMode current_mode;
/*      */   
/*   86 */   private static int x = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ByteBuffer[] cached_icons;
/*      */ 
/*      */ 
/*      */   
/*   95 */   private static int y = -1;
/*      */ 
/*      */   
/*   98 */   private static int width = 0;
/*      */ 
/*      */   
/*  101 */   private static int height = 0;
/*      */ 
/*      */   
/*  104 */   private static String title = "Game";
/*      */   
/*      */   private static boolean fullscreen;
/*      */   
/*      */   private static int swap_interval;
/*      */   
/*      */   private static DrawableLWJGL drawable;
/*      */   
/*      */   private static boolean window_created;
/*      */   
/*      */   private static boolean parent_resized;
/*      */   
/*      */   private static boolean window_resized;
/*      */   
/*      */   private static boolean window_resizable;
/*      */   
/*      */   private static float r;
/*      */   
/*      */   private static float g;
/*      */   
/*      */   private static float b;
/*      */   
/*  126 */   private static final ComponentListener component_listener = new ComponentAdapter() {
/*      */       public void componentResized(ComponentEvent e) {
/*  128 */         synchronized (GlobalLock.lock) {
/*  129 */           Display.parent_resized = true;
/*      */         } 
/*      */       }
/*      */     };
/*      */   
/*      */   static {
/*  135 */     Sys.initialize();
/*  136 */     display_impl = createDisplayImplementation();
/*      */     try {
/*  138 */       current_mode = initial_mode = display_impl.init();
/*  139 */       LWJGLUtil.log("Initial mode: " + initial_mode);
/*  140 */     } catch (LWJGLException e) {
/*  141 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Drawable getDrawable() {
/*  151 */     return drawable;
/*      */   }
/*      */   
/*      */   private static DisplayImplementation createDisplayImplementation() {
/*  155 */     switch (LWJGLUtil.getPlatform()) {
/*      */       case 1:
/*  157 */         return new LinuxDisplay();
/*      */       case 3:
/*  159 */         return new WindowsDisplay();
/*      */       case 2:
/*  161 */         return new MacOSXDisplay();
/*      */     } 
/*  163 */     throw new IllegalStateException("Unsupported platform");
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
/*  187 */     synchronized (GlobalLock.lock) {
/*  188 */       DisplayMode[] unfilteredModes = display_impl.getAvailableDisplayModes();
/*      */       
/*  190 */       if (unfilteredModes == null) {
/*  191 */         return new DisplayMode[0];
/*      */       }
/*      */ 
/*      */       
/*  195 */       HashSet<DisplayMode> modes = new HashSet<DisplayMode>(unfilteredModes.length);
/*      */       
/*  197 */       modes.addAll(Arrays.asList(unfilteredModes));
/*  198 */       DisplayMode[] filteredModes = new DisplayMode[modes.size()];
/*  199 */       modes.toArray(filteredModes);
/*      */       
/*  201 */       LWJGLUtil.log("Removed " + (unfilteredModes.length - filteredModes.length) + " duplicate displaymodes");
/*      */       
/*  203 */       return filteredModes;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DisplayMode getDesktopDisplayMode() {
/*  213 */     return initial_mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DisplayMode getDisplayMode() {
/*  222 */     return current_mode;
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
/*      */   public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
/*  237 */     synchronized (GlobalLock.lock) {
/*  238 */       if (mode == null)
/*  239 */         throw new NullPointerException("mode must be non-null"); 
/*  240 */       boolean was_fullscreen = isFullscreen();
/*  241 */       current_mode = mode;
/*  242 */       if (isCreated()) {
/*  243 */         destroyWindow();
/*      */         
/*      */         try {
/*  246 */           if (was_fullscreen && !isFullscreen()) {
/*  247 */             display_impl.resetDisplayMode();
/*  248 */           } else if (isFullscreen()) {
/*  249 */             switchDisplayMode();
/*  250 */           }  createWindow();
/*  251 */           makeCurrentAndSetSwapInterval();
/*  252 */         } catch (LWJGLException e) {
/*  253 */           drawable.destroy();
/*  254 */           display_impl.resetDisplayMode();
/*  255 */           throw e;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static DisplayMode getEffectiveMode() {
/*  262 */     return (!isFullscreen() && parent != null) ? new DisplayMode(parent.getWidth(), parent.getHeight()) : current_mode;
/*      */   }
/*      */   
/*      */   private static int getWindowX() {
/*  266 */     if (!isFullscreen() && parent == null) {
/*      */       
/*  268 */       if (x == -1) {
/*  269 */         return Math.max(0, (initial_mode.getWidth() - current_mode.getWidth()) / 2);
/*      */       }
/*  271 */       return x;
/*      */     } 
/*      */     
/*  274 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getWindowY() {
/*  279 */     if (!isFullscreen() && parent == null) {
/*      */       
/*  281 */       if (y == -1) {
/*  282 */         return Math.max(0, (initial_mode.getHeight() - current_mode.getHeight()) / 2);
/*      */       }
/*  284 */       return y;
/*      */     } 
/*      */     
/*  287 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createWindow() throws LWJGLException {
/*  296 */     if (window_created) {
/*      */       return;
/*      */     }
/*  299 */     Canvas tmp_parent = isFullscreen() ? null : parent;
/*  300 */     if (tmp_parent != null && !tmp_parent.isDisplayable())
/*  301 */       throw new LWJGLException("Parent.isDisplayable() must be true"); 
/*  302 */     if (tmp_parent != null) {
/*  303 */       tmp_parent.addComponentListener(component_listener);
/*      */     }
/*  305 */     DisplayMode mode = getEffectiveMode();
/*  306 */     display_impl.createWindow(drawable, mode, tmp_parent, getWindowX(), getWindowY());
/*  307 */     window_created = true;
/*      */     
/*  309 */     width = getDisplayMode().getWidth();
/*  310 */     height = getDisplayMode().getHeight();
/*      */     
/*  312 */     setTitle(title);
/*  313 */     initControls();
/*      */ 
/*      */     
/*  316 */     if (cached_icons != null) {
/*  317 */       setIcon(cached_icons);
/*      */     } else {
/*  319 */       setIcon(new ByteBuffer[] { LWJGLUtil.LWJGLIcon32x32, LWJGLUtil.LWJGLIcon16x16 });
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void releaseDrawable() {
/*      */     try {
/*  325 */       Context context = drawable.getContext();
/*  326 */       if (context != null && context.isCurrent()) {
/*  327 */         context.releaseCurrent();
/*  328 */         context.releaseDrawable();
/*      */       } 
/*  330 */     } catch (LWJGLException e) {
/*  331 */       LWJGLUtil.log("Exception occurred while trying to release context: " + e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void destroyWindow() {
/*  336 */     if (!window_created) {
/*      */       return;
/*      */     }
/*  339 */     if (parent != null) {
/*  340 */       parent.removeComponentListener(component_listener);
/*      */     }
/*  342 */     releaseDrawable();
/*      */ 
/*      */     
/*  345 */     if (Mouse.isCreated()) {
/*  346 */       Mouse.destroy();
/*      */     }
/*  348 */     if (Keyboard.isCreated()) {
/*  349 */       Keyboard.destroy();
/*      */     }
/*  351 */     display_impl.destroyWindow();
/*  352 */     window_created = false;
/*      */   }
/*      */   
/*      */   private static void switchDisplayMode() throws LWJGLException {
/*  356 */     if (!current_mode.isFullscreenCapable()) {
/*  357 */       throw new IllegalStateException("Only modes acquired from getAvailableDisplayModes() can be used for fullscreen display");
/*      */     }
/*  359 */     display_impl.switchDisplayMode(current_mode);
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
/*      */   public static void setDisplayConfiguration(float gamma, float brightness, float contrast) throws LWJGLException {
/*  371 */     synchronized (GlobalLock.lock) {
/*  372 */       if (!isCreated()) {
/*  373 */         throw new LWJGLException("Display not yet created.");
/*      */       }
/*  375 */       if (brightness < -1.0F || brightness > 1.0F)
/*  376 */         throw new IllegalArgumentException("Invalid brightness value"); 
/*  377 */       if (contrast < 0.0F)
/*  378 */         throw new IllegalArgumentException("Invalid contrast value"); 
/*  379 */       int rampSize = display_impl.getGammaRampLength();
/*  380 */       if (rampSize == 0) {
/*  381 */         throw new LWJGLException("Display configuration not supported");
/*      */       }
/*  383 */       FloatBuffer gammaRamp = BufferUtils.createFloatBuffer(rampSize);
/*  384 */       for (int i = 0; i < rampSize; i++) {
/*  385 */         float intensity = i / (rampSize - 1);
/*      */         
/*  387 */         float rampEntry = (float)Math.pow(intensity, gamma);
/*      */         
/*  389 */         rampEntry += brightness;
/*      */         
/*  391 */         rampEntry = (rampEntry - 0.5F) * contrast + 0.5F;
/*      */         
/*  393 */         if (rampEntry > 1.0F) {
/*  394 */           rampEntry = 1.0F;
/*  395 */         } else if (rampEntry < 0.0F) {
/*  396 */           rampEntry = 0.0F;
/*  397 */         }  gammaRamp.put(i, rampEntry);
/*      */       } 
/*  399 */       display_impl.setGammaRamp(gammaRamp);
/*  400 */       LWJGLUtil.log("Gamma set, gamma = " + gamma + ", brightness = " + brightness + ", contrast = " + contrast);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sync(int fps) {
/*  411 */     Sync.sync(fps);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getTitle() {
/*  416 */     synchronized (GlobalLock.lock) {
/*  417 */       return title;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static Canvas getParent() {
/*  423 */     synchronized (GlobalLock.lock) {
/*  424 */       return parent;
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
/*      */   public static void setParent(Canvas parent) throws LWJGLException {
/*  439 */     synchronized (GlobalLock.lock) {
/*  440 */       if (Display.parent != parent) {
/*  441 */         Display.parent = parent;
/*  442 */         if (!isCreated())
/*      */           return; 
/*  444 */         destroyWindow();
/*      */         try {
/*  446 */           if (isFullscreen()) {
/*  447 */             switchDisplayMode();
/*      */           } else {
/*  449 */             display_impl.resetDisplayMode();
/*      */           } 
/*  451 */           createWindow();
/*  452 */           makeCurrentAndSetSwapInterval();
/*  453 */         } catch (LWJGLException e) {
/*  454 */           drawable.destroy();
/*  455 */           display_impl.resetDisplayMode();
/*  456 */           throw e;
/*      */         } 
/*      */       } 
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
/*      */   public static void setFullscreen(boolean fullscreen) throws LWJGLException {
/*  475 */     setDisplayModeAndFullscreenInternal(fullscreen, current_mode);
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
/*      */   public static void setDisplayModeAndFullscreen(DisplayMode mode) throws LWJGLException {
/*  490 */     setDisplayModeAndFullscreenInternal(mode.isFullscreenCapable(), mode);
/*      */   }
/*      */   
/*      */   private static void setDisplayModeAndFullscreenInternal(boolean fullscreen, DisplayMode mode) throws LWJGLException {
/*  494 */     synchronized (GlobalLock.lock) {
/*  495 */       if (mode == null)
/*  496 */         throw new NullPointerException("mode must be non-null"); 
/*  497 */       DisplayMode old_mode = current_mode;
/*  498 */       current_mode = mode;
/*  499 */       boolean was_fullscreen = isFullscreen();
/*  500 */       Display.fullscreen = fullscreen;
/*  501 */       if (was_fullscreen != isFullscreen() || !mode.equals(old_mode)) {
/*  502 */         if (!isCreated())
/*      */           return; 
/*  504 */         destroyWindow();
/*      */         try {
/*  506 */           if (isFullscreen()) {
/*  507 */             switchDisplayMode();
/*      */           } else {
/*  509 */             display_impl.resetDisplayMode();
/*      */           } 
/*  511 */           createWindow();
/*  512 */           makeCurrentAndSetSwapInterval();
/*  513 */         } catch (LWJGLException e) {
/*  514 */           drawable.destroy();
/*  515 */           display_impl.resetDisplayMode();
/*  516 */           throw e;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFullscreen() {
/*  524 */     synchronized (GlobalLock.lock) {
/*  525 */       return (fullscreen && current_mode.isFullscreenCapable());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setTitle(String newTitle) {
/*  535 */     synchronized (GlobalLock.lock) {
/*  536 */       if (newTitle == null) {
/*  537 */         newTitle = "";
/*      */       }
/*  539 */       title = newTitle;
/*  540 */       if (isCreated()) {
/*  541 */         display_impl.setTitle(title);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static boolean isCloseRequested() {
/*  547 */     synchronized (GlobalLock.lock) {
/*  548 */       if (!isCreated())
/*  549 */         throw new IllegalStateException("Cannot determine close requested state of uncreated window"); 
/*  550 */       return display_impl.isCloseRequested();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isVisible() {
/*  556 */     synchronized (GlobalLock.lock) {
/*  557 */       if (!isCreated())
/*  558 */         throw new IllegalStateException("Cannot determine minimized state of uncreated window"); 
/*  559 */       return display_impl.isVisible();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isActive() {
/*  565 */     synchronized (GlobalLock.lock) {
/*  566 */       if (!isCreated())
/*  567 */         throw new IllegalStateException("Cannot determine focused state of uncreated window"); 
/*  568 */       return display_impl.isActive();
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
/*      */   public static boolean isDirty() {
/*  583 */     synchronized (GlobalLock.lock) {
/*  584 */       if (!isCreated())
/*  585 */         throw new IllegalStateException("Cannot determine dirty state of uncreated window"); 
/*  586 */       return display_impl.isDirty();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void processMessages() {
/*  596 */     synchronized (GlobalLock.lock) {
/*  597 */       if (!isCreated()) {
/*  598 */         throw new IllegalStateException("Display not created");
/*      */       }
/*  600 */       display_impl.update();
/*      */     } 
/*  602 */     pollDevices();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void swapBuffers() throws LWJGLException {
/*  612 */     synchronized (GlobalLock.lock) {
/*  613 */       if (!isCreated()) {
/*  614 */         throw new IllegalStateException("Display not created");
/*      */       }
/*  616 */       if (LWJGLUtil.DEBUG)
/*  617 */         drawable.checkGLError(); 
/*  618 */       drawable.swapBuffers();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void update() {
/*  628 */     update(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void update(boolean processMessages) {
/*  639 */     synchronized (GlobalLock.lock) {
/*  640 */       if (!isCreated()) {
/*  641 */         throw new IllegalStateException("Display not created");
/*      */       }
/*      */       
/*  644 */       if (display_impl.isVisible() || display_impl.isDirty()) {
/*      */         try {
/*  646 */           swapBuffers();
/*  647 */         } catch (LWJGLException e) {
/*  648 */           throw new RuntimeException(e);
/*      */         } 
/*      */       }
/*      */       
/*  652 */       window_resized = (!isFullscreen() && parent == null && display_impl.wasResized());
/*      */       
/*  654 */       if (window_resized) {
/*  655 */         width = display_impl.getWidth();
/*  656 */         height = display_impl.getHeight();
/*      */       } 
/*      */       
/*  659 */       if (parent_resized) {
/*  660 */         reshape();
/*  661 */         parent_resized = false;
/*  662 */         window_resized = true;
/*      */       } 
/*      */       
/*  665 */       if (processMessages) {
/*  666 */         processMessages();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   static void pollDevices() {
/*  672 */     if (Mouse.isCreated()) {
/*  673 */       Mouse.poll();
/*  674 */       Mouse.updateCursor();
/*      */     } 
/*      */     
/*  677 */     if (Keyboard.isCreated()) {
/*  678 */       Keyboard.poll();
/*      */     }
/*      */     
/*  681 */     if (Controllers.isCreated()) {
/*  682 */       Controllers.poll();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void releaseContext() throws LWJGLException {
/*  692 */     drawable.releaseContext();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCurrent() throws LWJGLException {
/*  697 */     return drawable.isCurrent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void makeCurrent() throws LWJGLException {
/*  706 */     drawable.makeCurrent();
/*      */   }
/*      */   
/*      */   private static void removeShutdownHook() {
/*  710 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */           public Object run() {
/*  712 */             Runtime.getRuntime().removeShutdownHook(Display.shutdown_hook);
/*  713 */             return null;
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private static void registerShutdownHook() {
/*  719 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */           public Object run() {
/*  721 */             Runtime.getRuntime().addShutdownHook(Display.shutdown_hook);
/*  722 */             return null;
/*      */           }
/*      */         });
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
/*      */   public static void create() throws LWJGLException {
/*  739 */     create(new PixelFormat());
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
/*      */   public static void create(PixelFormat pixel_format) throws LWJGLException {
/*  756 */     synchronized (GlobalLock.lock) {
/*  757 */       create(pixel_format, (Drawable)null, (ContextAttribs)null);
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
/*      */   public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
/*  776 */     synchronized (GlobalLock.lock) {
/*  777 */       create(pixel_format, shared_drawable, (ContextAttribs)null);
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
/*      */   public static void create(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
/*  796 */     synchronized (GlobalLock.lock) {
/*  797 */       create(pixel_format, (Drawable)null, attribs);
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
/*      */   
/*      */   public static void create(PixelFormat pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
/*  817 */     synchronized (GlobalLock.lock) {
/*  818 */       if (isCreated())
/*  819 */         throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time."); 
/*  820 */       if (pixel_format == null)
/*  821 */         throw new NullPointerException("pixel_format cannot be null"); 
/*  822 */       removeShutdownHook();
/*  823 */       registerShutdownHook();
/*  824 */       if (isFullscreen()) {
/*  825 */         switchDisplayMode();
/*      */       }
/*  827 */       DrawableGL drawable = new DrawableGL() {
/*      */           public void destroy() {
/*  829 */             synchronized (GlobalLock.lock) {
/*  830 */               if (!Display.isCreated()) {
/*      */                 return;
/*      */               }
/*  833 */               Display.releaseDrawable();
/*  834 */               super.destroy();
/*  835 */               Display.destroyWindow();
/*  836 */               Display.x = Display.y = -1;
/*  837 */               Display.cached_icons = null;
/*  838 */               Display.reset();
/*  839 */               Display.removeShutdownHook();
/*      */             } 
/*      */           }
/*      */         };
/*  843 */       Display.drawable = drawable;
/*      */       
/*      */       try {
/*  846 */         drawable.setPixelFormat(pixel_format, attribs);
/*      */         try {
/*  848 */           createWindow();
/*      */           try {
/*  850 */             drawable.context = new ContextGL(drawable.peer_info, attribs, (shared_drawable != null) ? ((DrawableGL)shared_drawable).getContext() : null);
/*      */             try {
/*  852 */               makeCurrentAndSetSwapInterval();
/*  853 */               initContext();
/*  854 */             } catch (LWJGLException e) {
/*  855 */               drawable.destroy();
/*  856 */               throw e;
/*      */             } 
/*  858 */           } catch (LWJGLException e) {
/*  859 */             destroyWindow();
/*  860 */             throw e;
/*      */           } 
/*  862 */         } catch (LWJGLException e) {
/*  863 */           drawable.destroy();
/*  864 */           throw e;
/*      */         } 
/*  866 */       } catch (LWJGLException e) {
/*  867 */         display_impl.resetDisplayMode();
/*  868 */         throw e;
/*      */       } 
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
/*      */   public static void create(PixelFormatLWJGL pixel_format) throws LWJGLException {
/*  888 */     synchronized (GlobalLock.lock) {
/*  889 */       create(pixel_format, (Drawable)null, (ContextAttribs)null);
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
/*      */   public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable) throws LWJGLException {
/*  908 */     synchronized (GlobalLock.lock) {
/*  909 */       create(pixel_format, shared_drawable, (ContextAttribs)null);
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
/*      */   public static void create(PixelFormatLWJGL pixel_format, ContextAttribs attribs) throws LWJGLException {
/*  928 */     synchronized (GlobalLock.lock) {
/*  929 */       create(pixel_format, (Drawable)null, attribs);
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
/*      */   
/*      */   public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
/*  949 */     synchronized (GlobalLock.lock) {
/*  950 */       if (isCreated())
/*  951 */         throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time."); 
/*  952 */       if (pixel_format == null)
/*  953 */         throw new NullPointerException("pixel_format cannot be null"); 
/*  954 */       removeShutdownHook();
/*  955 */       registerShutdownHook();
/*  956 */       if (isFullscreen()) {
/*  957 */         switchDisplayMode();
/*      */       }
/*  959 */       DrawableGLES drawable = new DrawableGLES()
/*      */         {
/*      */           public void setPixelFormat(PixelFormatLWJGL pf, ContextAttribs attribs) throws LWJGLException {
/*  962 */             throw new UnsupportedOperationException();
/*      */           }
/*      */           
/*      */           public void destroy() {
/*  966 */             synchronized (GlobalLock.lock) {
/*  967 */               if (!Display.isCreated()) {
/*      */                 return;
/*      */               }
/*  970 */               Display.releaseDrawable();
/*  971 */               super.destroy();
/*  972 */               Display.destroyWindow();
/*  973 */               Display.x = Display.y = -1;
/*  974 */               Display.cached_icons = null;
/*  975 */               Display.reset();
/*  976 */               Display.removeShutdownHook();
/*      */             } 
/*      */           }
/*      */         };
/*  980 */       Display.drawable = drawable;
/*      */       
/*      */       try {
/*  983 */         drawable.setPixelFormat(pixel_format);
/*      */         try {
/*  985 */           createWindow();
/*      */           try {
/*  987 */             drawable.createContext(attribs, shared_drawable);
/*      */             try {
/*  989 */               makeCurrentAndSetSwapInterval();
/*  990 */               initContext();
/*  991 */             } catch (LWJGLException e) {
/*  992 */               drawable.destroy();
/*  993 */               throw e;
/*      */             } 
/*  995 */           } catch (LWJGLException e) {
/*  996 */             destroyWindow();
/*  997 */             throw e;
/*      */           } 
/*  999 */         } catch (LWJGLException e) {
/* 1000 */           drawable.destroy();
/* 1001 */           throw e;
/*      */         } 
/* 1003 */       } catch (LWJGLException e) {
/* 1004 */         display_impl.resetDisplayMode();
/* 1005 */         throw e;
/*      */       } 
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
/*      */   public static void setInitialBackground(float red, float green, float blue) {
/* 1019 */     r = red;
/* 1020 */     g = green;
/* 1021 */     b = blue;
/*      */   }
/*      */   
/*      */   private static void makeCurrentAndSetSwapInterval() throws LWJGLException {
/* 1025 */     makeCurrent();
/*      */     try {
/* 1027 */       drawable.checkGLError();
/* 1028 */     } catch (OpenGLException e) {
/* 1029 */       LWJGLUtil.log("OpenGL error during context creation: " + e.getMessage());
/*      */     } 
/* 1031 */     setSwapInterval(swap_interval);
/*      */   }
/*      */   
/*      */   private static void initContext() {
/* 1035 */     drawable.initContext(r, g, b);
/* 1036 */     update();
/*      */   }
/*      */   
/*      */   static DisplayImplementation getImplementation() {
/* 1040 */     return display_impl;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean getPrivilegedBoolean(final String property_name) {
/* 1045 */     return ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>() {
/*      */           public Boolean run() {
/* 1047 */             return Boolean.valueOf(Boolean.getBoolean(property_name));
/*      */           }
/*      */         })).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   static String getPrivilegedString(final String property_name) {
/* 1054 */     return AccessController.<String>doPrivileged(new PrivilegedAction<String>() {
/*      */           public String run() {
/* 1056 */             return System.getProperty(property_name);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initControls() {
/* 1063 */     if (!getPrivilegedBoolean("org.lwjgl.opengl.Display.noinput")) {
/* 1064 */       if (!Mouse.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nomouse")) {
/*      */         try {
/* 1066 */           Mouse.create();
/* 1067 */         } catch (LWJGLException e) {
/* 1068 */           if (LWJGLUtil.DEBUG) {
/* 1069 */             e.printStackTrace(System.err);
/*      */           } else {
/* 1071 */             LWJGLUtil.log("Failed to create Mouse: " + e);
/*      */           } 
/*      */         } 
/*      */       }
/* 1075 */       if (!Keyboard.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nokeyboard")) {
/*      */         try {
/* 1077 */           Keyboard.create();
/* 1078 */         } catch (LWJGLException e) {
/* 1079 */           if (LWJGLUtil.DEBUG) {
/* 1080 */             e.printStackTrace(System.err);
/*      */           } else {
/* 1082 */             LWJGLUtil.log("Failed to create Keyboard: " + e);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void destroy() {
/* 1094 */     if (isCreated()) {
/* 1095 */       drawable.destroy();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void reset() {
/* 1105 */     display_impl.resetDisplayMode();
/* 1106 */     current_mode = initial_mode;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCreated() {
/* 1111 */     synchronized (GlobalLock.lock) {
/* 1112 */       return window_created;
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
/*      */   public static void setSwapInterval(int value) {
/* 1126 */     synchronized (GlobalLock.lock) {
/* 1127 */       swap_interval = value;
/* 1128 */       if (isCreated()) {
/* 1129 */         drawable.setSwapInterval(swap_interval);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setVSyncEnabled(boolean sync) {
/* 1141 */     synchronized (GlobalLock.lock) {
/* 1142 */       setSwapInterval(sync ? 1 : 0);
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
/*      */   public static void setLocation(int new_x, int new_y) {
/* 1157 */     synchronized (GlobalLock.lock) {
/*      */       
/* 1159 */       x = new_x;
/* 1160 */       y = new_y;
/*      */ 
/*      */       
/* 1163 */       if (isCreated() && !isFullscreen()) {
/* 1164 */         reshape();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void reshape() {
/* 1170 */     DisplayMode mode = getEffectiveMode();
/* 1171 */     display_impl.reshape(getWindowX(), getWindowY(), mode.getWidth(), mode.getHeight());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getAdapter() {
/* 1181 */     synchronized (GlobalLock.lock) {
/* 1182 */       return display_impl.getAdapter();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getVersion() {
/* 1193 */     synchronized (GlobalLock.lock) {
/* 1194 */       return display_impl.getVersion();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static int setIcon(ByteBuffer[] icons) {
/* 1216 */     synchronized (GlobalLock.lock) {
/*      */ 
/*      */       
/* 1219 */       if (cached_icons != icons) {
/* 1220 */         cached_icons = new ByteBuffer[icons.length];
/* 1221 */         for (int i = 0; i < icons.length; i++) {
/* 1222 */           cached_icons[i] = BufferUtils.createByteBuffer(icons[i].capacity());
/* 1223 */           int old_position = icons[i].position();
/* 1224 */           cached_icons[i].put(icons[i]);
/* 1225 */           icons[i].position(old_position);
/* 1226 */           cached_icons[i].flip();
/*      */         } 
/*      */       } 
/*      */       
/* 1230 */       if (isCreated() && parent == null) {
/* 1231 */         return display_impl.setIcon(cached_icons);
/*      */       }
/* 1233 */       return 0;
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
/*      */   public static void setResizable(boolean resizable) {
/* 1245 */     window_resizable = resizable;
/* 1246 */     if (isCreated()) {
/* 1247 */       display_impl.setResizable(resizable);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isResizable() {
/* 1255 */     return window_resizable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean wasResized() {
/* 1265 */     return window_resized;
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
/*      */   public static int getX() {
/* 1277 */     if (isFullscreen()) {
/* 1278 */       return 0;
/*      */     }
/*      */     
/* 1281 */     if (parent != null) {
/* 1282 */       return parent.getX();
/*      */     }
/*      */     
/* 1285 */     return display_impl.getX();
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
/*      */   public static int getY() {
/* 1297 */     if (isFullscreen()) {
/* 1298 */       return 0;
/*      */     }
/*      */     
/* 1301 */     if (parent != null) {
/* 1302 */       return parent.getY();
/*      */     }
/*      */     
/* 1305 */     return display_impl.getY();
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
/*      */   public static int getWidth() {
/* 1319 */     if (isFullscreen()) {
/* 1320 */       return getDisplayMode().getWidth();
/*      */     }
/*      */     
/* 1323 */     if (parent != null) {
/* 1324 */       return parent.getWidth();
/*      */     }
/*      */     
/* 1327 */     return width;
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
/*      */   public static int getHeight() {
/* 1341 */     if (isFullscreen()) {
/* 1342 */       return getDisplayMode().getHeight();
/*      */     }
/*      */     
/* 1345 */     if (parent != null) {
/* 1346 */       return parent.getHeight();
/*      */     }
/*      */     
/* 1349 */     return height;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float getPixelScaleFactor() {
/* 1373 */     return display_impl.getPixelScaleFactor();
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\Display.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */