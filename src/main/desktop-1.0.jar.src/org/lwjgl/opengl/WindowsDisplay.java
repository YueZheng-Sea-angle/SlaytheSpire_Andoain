/*      */ package org.lwjgl.opengl;
/*      */ 
/*      */ import java.awt.Canvas;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import javax.swing.SwingUtilities;
/*      */ import org.lwjgl.BufferUtils;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.LWJGLUtil;
/*      */ import org.lwjgl.MemoryUtil;
/*      */ import org.lwjgl.input.Mouse;
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
/*      */ final class WindowsDisplay
/*      */   implements DisplayImplementation
/*      */ {
/*      */   private static final int GAMMA_LENGTH = 256;
/*      */   private static final int WM_WINDOWPOSCHANGED = 71;
/*      */   private static final int WM_MOVE = 3;
/*      */   private static final int WM_CANCELMODE = 31;
/*      */   private static final int WM_MOUSEMOVE = 512;
/*      */   private static final int WM_LBUTTONDOWN = 513;
/*      */   private static final int WM_LBUTTONUP = 514;
/*      */   private static final int WM_LBUTTONDBLCLK = 515;
/*      */   private static final int WM_RBUTTONDOWN = 516;
/*      */   private static final int WM_RBUTTONUP = 517;
/*      */   private static final int WM_RBUTTONDBLCLK = 518;
/*      */   private static final int WM_MBUTTONDOWN = 519;
/*      */   private static final int WM_MBUTTONUP = 520;
/*      */   private static final int WM_MBUTTONDBLCLK = 521;
/*      */   private static final int WM_XBUTTONDOWN = 523;
/*      */   private static final int WM_XBUTTONUP = 524;
/*      */   private static final int WM_XBUTTONDBLCLK = 525;
/*      */   private static final int WM_MOUSEWHEEL = 522;
/*      */   private static final int WM_CAPTURECHANGED = 533;
/*      */   private static final int WM_MOUSELEAVE = 675;
/*      */   private static final int WM_ENTERSIZEMOVE = 561;
/*      */   private static final int WM_EXITSIZEMOVE = 562;
/*      */   private static final int WM_SIZING = 532;
/*      */   private static final int WM_KEYDOWN = 256;
/*      */   private static final int WM_KEYUP = 257;
/*      */   private static final int WM_SYSKEYUP = 261;
/*      */   private static final int WM_SYSKEYDOWN = 260;
/*      */   private static final int WM_SYSCHAR = 262;
/*      */   private static final int WM_CHAR = 258;
/*      */   private static final int WM_GETICON = 127;
/*      */   private static final int WM_SETICON = 128;
/*      */   private static final int WM_SETCURSOR = 32;
/*      */   private static final int WM_MOUSEACTIVATE = 33;
/*      */   private static final int WM_QUIT = 18;
/*      */   private static final int WM_SYSCOMMAND = 274;
/*      */   private static final int WM_PAINT = 15;
/*      */   private static final int WM_KILLFOCUS = 8;
/*      */   private static final int WM_SETFOCUS = 7;
/*      */   private static final int SC_SIZE = 61440;
/*      */   private static final int SC_MOVE = 61456;
/*      */   private static final int SC_MINIMIZE = 61472;
/*      */   private static final int SC_MAXIMIZE = 61488;
/*      */   private static final int SC_NEXTWINDOW = 61504;
/*      */   private static final int SC_PREVWINDOW = 61520;
/*      */   private static final int SC_CLOSE = 61536;
/*      */   private static final int SC_VSCROLL = 61552;
/*      */   private static final int SC_HSCROLL = 61568;
/*      */   private static final int SC_MOUSEMENU = 61584;
/*      */   private static final int SC_KEYMENU = 61696;
/*      */   private static final int SC_ARRANGE = 61712;
/*      */   private static final int SC_RESTORE = 61728;
/*      */   private static final int SC_TASKLIST = 61744;
/*      */   private static final int SC_SCREENSAVE = 61760;
/*      */   private static final int SC_HOTKEY = 61776;
/*      */   private static final int SC_DEFAULT = 61792;
/*      */   private static final int SC_MONITORPOWER = 61808;
/*      */   private static final int SC_CONTEXTHELP = 61824;
/*      */   private static final int SC_SEPARATOR = 61455;
/*      */   static final int SM_CXCURSOR = 13;
/*      */   static final int SM_CYCURSOR = 14;
/*      */   static final int SM_CMOUSEBUTTONS = 43;
/*      */   static final int SM_MOUSEWHEELPRESENT = 75;
/*      */   private static final int SIZE_RESTORED = 0;
/*      */   private static final int SIZE_MINIMIZED = 1;
/*      */   private static final int SIZE_MAXIMIZED = 2;
/*      */   private static final int WM_SIZE = 5;
/*      */   private static final int WM_ACTIVATE = 6;
/*      */   private static final int WA_INACTIVE = 0;
/*      */   private static final int WA_ACTIVE = 1;
/*      */   private static final int WA_CLICKACTIVE = 2;
/*      */   private static final int SW_NORMAL = 1;
/*      */   private static final int SW_SHOWMINNOACTIVE = 7;
/*      */   private static final int SW_SHOWDEFAULT = 10;
/*      */   private static final int SW_RESTORE = 9;
/*      */   private static final int SW_MAXIMIZE = 3;
/*      */   private static final int ICON_SMALL = 0;
/*      */   private static final int ICON_BIG = 1;
/*  143 */   private static final IntBuffer rect_buffer = BufferUtils.createIntBuffer(4);
/*  144 */   private static final Rect rect = new Rect();
/*      */   
/*      */   private static final long HWND_TOP = 0L;
/*      */   
/*      */   private static final long HWND_BOTTOM = 1L;
/*      */   
/*      */   private static final long HWND_TOPMOST = -1L;
/*      */   
/*      */   private static final long HWND_NOTOPMOST = -2L;
/*      */   
/*      */   private static final int SWP_NOSIZE = 1;
/*      */   
/*      */   private static final int SWP_NOMOVE = 2;
/*      */   
/*      */   private static final int SWP_NOZORDER = 4;
/*      */   
/*      */   private static final int SWP_FRAMECHANGED = 32;
/*      */   
/*      */   private static final int GWL_STYLE = -16;
/*      */   
/*      */   private static final int GWL_EXSTYLE = -20;
/*      */   
/*      */   private static final int WS_THICKFRAME = 262144;
/*      */   
/*      */   private static final int WS_MAXIMIZEBOX = 65536;
/*      */   
/*      */   private static final int HTCLIENT = 1;
/*      */   
/*      */   private static final int MK_XBUTTON1 = 32;
/*      */   
/*      */   private static final int MK_XBUTTON2 = 64;
/*      */   
/*      */   private static final int XBUTTON1 = 1;
/*      */   
/*      */   private static final int XBUTTON2 = 2;
/*      */   private static WindowsDisplay current_display;
/*      */   private static boolean cursor_clipped;
/*      */   private WindowsDisplayPeerInfo peer_info;
/*      */   private Object current_cursor;
/*      */   private static boolean hasParent;
/*      */   private Canvas parent;
/*      */   private long parent_hwnd;
/*      */   private FocusAdapter parent_focus_tracker;
/*      */   private AtomicBoolean parent_focused;
/*      */   private WindowsKeyboard keyboard;
/*      */   private WindowsMouse mouse;
/*      */   private boolean close_requested;
/*      */   private boolean is_dirty;
/*      */   private ByteBuffer current_gamma;
/*      */   private ByteBuffer saved_gamma;
/*      */   private DisplayMode current_mode;
/*      */   private boolean mode_set;
/*      */   private boolean isMinimized;
/*      */   private boolean isFocused;
/*      */   private boolean redoMakeContextCurrent;
/*      */   private boolean inAppActivate;
/*      */   private boolean resized;
/*      */   private boolean resizable;
/*      */   private int x;
/*      */   private int y;
/*      */   private int width;
/*      */   private int height;
/*      */   private long hwnd;
/*      */   private long hdc;
/*      */   private long small_icon;
/*      */   private long large_icon;
/*      */   private boolean iconsLoaded;
/*  211 */   private int captureMouse = -1;
/*      */   private boolean trackingMouse;
/*      */   private boolean mouseInside;
/*      */   
/*      */   static {
/*      */     try {
/*  217 */       Method windowProc = WindowsDisplay.class.getDeclaredMethod("handleMessage", new Class[] { long.class, int.class, long.class, long.class, long.class });
/*  218 */       setWindowProc(windowProc);
/*  219 */     } catch (NoSuchMethodException e) {
/*  220 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   WindowsDisplay() {
/*  225 */     current_display = this;
/*      */   }
/*      */   
/*      */   public void createWindow(DrawableLWJGL drawable, DisplayMode mode, Canvas parent, int x, int y) throws LWJGLException {
/*  229 */     this.close_requested = false;
/*  230 */     this.is_dirty = false;
/*  231 */     this.isMinimized = false;
/*  232 */     this.isFocused = false;
/*  233 */     this.redoMakeContextCurrent = false;
/*  234 */     this.parent = parent;
/*  235 */     hasParent = (parent != null);
/*  236 */     this.parent_hwnd = (parent != null) ? getHwnd(parent) : 0L;
/*  237 */     this.hwnd = nCreateWindow(x, y, mode.getWidth(), mode.getHeight(), (Display.isFullscreen() || isUndecorated()), (parent != null), this.parent_hwnd);
/*  238 */     if (Display.isResizable() && parent == null) {
/*  239 */       setResizable(true);
/*      */     }
/*      */     
/*  242 */     if (this.hwnd == 0L) {
/*  243 */       throw new LWJGLException("Failed to create window");
/*      */     }
/*  245 */     this.hdc = getDC(this.hwnd);
/*  246 */     if (this.hdc == 0L) {
/*  247 */       nDestroyWindow(this.hwnd);
/*  248 */       throw new LWJGLException("Failed to get dc");
/*      */     } 
/*      */     
/*      */     try {
/*  252 */       if (drawable instanceof DrawableGL) {
/*  253 */         int format = WindowsPeerInfo.choosePixelFormat(getHdc(), 0, 0, (PixelFormat)drawable.getPixelFormat(), (IntBuffer)null, true, true, false, true);
/*  254 */         WindowsPeerInfo.setPixelFormat(getHdc(), format);
/*      */       } else {
/*  256 */         this.peer_info = new WindowsDisplayPeerInfo(true);
/*  257 */         ((DrawableGLES)drawable).initialize(this.hwnd, this.hdc, 4, (PixelFormat)drawable.getPixelFormat());
/*      */       } 
/*  259 */       this.peer_info.initDC(getHwnd(), getHdc());
/*  260 */       showWindow(getHwnd(), 10);
/*      */       
/*  262 */       updateWidthAndHeight();
/*      */       
/*  264 */       if (parent == null) {
/*  265 */         setForegroundWindow(getHwnd());
/*      */       } else {
/*  267 */         this.parent_focused = new AtomicBoolean(false);
/*  268 */         parent.addFocusListener(this.parent_focus_tracker = new FocusAdapter() {
/*      */               public void focusGained(FocusEvent e) {
/*  270 */                 WindowsDisplay.this.parent_focused.set(true);
/*  271 */                 WindowsDisplay.this.clearAWTFocus();
/*      */               }
/*      */             });
/*  274 */         SwingUtilities.invokeLater(new Runnable() {
/*      */               public void run() {
/*  276 */                 WindowsDisplay.this.clearAWTFocus();
/*      */               }
/*      */             });
/*      */       } 
/*  280 */       grabFocus();
/*  281 */     } catch (LWJGLException e) {
/*  282 */       nReleaseDC(this.hwnd, this.hdc);
/*  283 */       nDestroyWindow(this.hwnd);
/*  284 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateWidthAndHeight() {
/*  289 */     getClientRect(this.hwnd, rect_buffer);
/*  290 */     rect.copyFromBuffer(rect_buffer);
/*  291 */     this.width = rect.right - rect.left;
/*  292 */     this.height = rect.bottom - rect.top;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isUndecorated() {
/*  298 */     return Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated");
/*      */   }
/*      */   
/*      */   private static long getHwnd(Canvas parent) throws LWJGLException {
/*  302 */     AWTCanvasImplementation awt_impl = AWTGLCanvas.createImplementation();
/*  303 */     WindowsPeerInfo parent_peer_info = (WindowsPeerInfo)awt_impl.createPeerInfo(parent, null, null);
/*  304 */     ByteBuffer parent_peer_info_handle = parent_peer_info.lockAndGetHandle();
/*      */     try {
/*  306 */       return parent_peer_info.getHwnd();
/*      */     } finally {
/*  308 */       parent_peer_info.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void destroyWindow() {
/*  313 */     if (this.parent != null) {
/*  314 */       this.parent.removeFocusListener(this.parent_focus_tracker);
/*  315 */       this.parent_focus_tracker = null;
/*      */     } 
/*      */     
/*  318 */     nReleaseDC(this.hwnd, this.hdc);
/*  319 */     nDestroyWindow(this.hwnd);
/*  320 */     freeLargeIcon();
/*  321 */     freeSmallIcon();
/*  322 */     resetCursorClipping();
/*      */   }
/*      */ 
/*      */   
/*      */   static void resetCursorClipping() {
/*  327 */     if (cursor_clipped) {
/*      */       try {
/*  329 */         clipCursor(null);
/*  330 */       } catch (LWJGLException e) {
/*  331 */         LWJGLUtil.log("Failed to reset cursor clipping: " + e);
/*      */       } 
/*  333 */       cursor_clipped = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void getGlobalClientRect(long hwnd, Rect rect) {
/*  338 */     rect_buffer.put(0, 0).put(1, 0);
/*  339 */     clientToScreen(hwnd, rect_buffer);
/*  340 */     int offset_x = rect_buffer.get(0);
/*  341 */     int offset_y = rect_buffer.get(1);
/*  342 */     getClientRect(hwnd, rect_buffer);
/*  343 */     rect.copyFromBuffer(rect_buffer);
/*  344 */     rect.offset(offset_x, offset_y);
/*      */   }
/*      */   
/*      */   static void setupCursorClipping(long hwnd) throws LWJGLException {
/*  348 */     cursor_clipped = true;
/*  349 */     getGlobalClientRect(hwnd, rect);
/*  350 */     rect.copyToBuffer(rect_buffer);
/*  351 */     clipCursor(rect_buffer);
/*      */   }
/*      */ 
/*      */   
/*      */   public void switchDisplayMode(DisplayMode mode) throws LWJGLException {
/*  356 */     nSwitchDisplayMode(mode);
/*  357 */     this.current_mode = mode;
/*  358 */     this.mode_set = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void appActivate(boolean active, long millis) {
/*  366 */     if (this.inAppActivate) {
/*      */       return;
/*      */     }
/*  369 */     this.inAppActivate = true;
/*  370 */     this.isFocused = active;
/*  371 */     if (active) {
/*  372 */       if (Display.isFullscreen()) {
/*  373 */         restoreDisplayMode();
/*      */       }
/*  375 */       if (this.parent == null) {
/*  376 */         setForegroundWindow(getHwnd());
/*      */       }
/*  378 */       setFocus(getHwnd());
/*  379 */       this.redoMakeContextCurrent = true;
/*  380 */       if (Display.isFullscreen())
/*  381 */         updateClipping(); 
/*      */     } else {
/*  383 */       if (this.keyboard != null)
/*  384 */         this.keyboard.releaseAll(millis); 
/*  385 */       if (Display.isFullscreen()) {
/*  386 */         showWindow(getHwnd(), 7);
/*  387 */         resetDisplayMode();
/*      */       } else {
/*  389 */         updateClipping();
/*      */       } 
/*  391 */     }  updateCursor();
/*  392 */     this.inAppActivate = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void clearAWTFocus() {
/*  400 */     this.parent.setFocusable(false);
/*  401 */     this.parent.setFocusable(true);
/*      */ 
/*      */     
/*  404 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
/*      */   }
/*      */   
/*      */   private void grabFocus() {
/*  408 */     if (this.parent == null) {
/*  409 */       setFocus(getHwnd());
/*      */     } else {
/*  411 */       SwingUtilities.invokeLater(new Runnable() {
/*      */             public void run() {
/*  413 */               WindowsDisplay.this.parent.requestFocus();
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */   private void restoreDisplayMode() {
/*      */     try {
/*  420 */       doSetGammaRamp(this.current_gamma);
/*  421 */     } catch (LWJGLException e) {
/*  422 */       LWJGLUtil.log("Failed to restore gamma: " + e.getMessage());
/*      */     } 
/*      */     
/*  425 */     if (!this.mode_set) {
/*  426 */       this.mode_set = true;
/*      */       try {
/*  428 */         nSwitchDisplayMode(this.current_mode);
/*  429 */       } catch (LWJGLException e) {
/*  430 */         LWJGLUtil.log("Failed to restore display mode: " + e.getMessage());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void resetDisplayMode() {
/*      */     try {
/*  437 */       doSetGammaRamp(this.saved_gamma);
/*  438 */     } catch (LWJGLException e) {
/*  439 */       LWJGLUtil.log("Failed to reset gamma ramp: " + e.getMessage());
/*      */     } 
/*  441 */     this.current_gamma = this.saved_gamma;
/*  442 */     if (this.mode_set) {
/*  443 */       this.mode_set = false;
/*  444 */       nResetDisplayMode();
/*      */     } 
/*  446 */     resetCursorClipping();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getGammaRampLength() {
/*  451 */     return 256;
/*      */   }
/*      */   
/*      */   public void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException {
/*  455 */     doSetGammaRamp(convertToNativeRamp(gammaRamp));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void doSetGammaRamp(ByteBuffer native_gamma) throws LWJGLException {
/*  461 */     nSetGammaRamp(native_gamma);
/*  462 */     this.current_gamma = native_gamma;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAdapter() {
/*      */     try {
/*  468 */       String maxObjNo = WindowsRegistry.queryRegistrationKey(3, "HARDWARE\\DeviceMap\\Video", "MaxObjectNumber");
/*      */ 
/*      */ 
/*      */       
/*  472 */       int maxObjectNumber = maxObjNo.charAt(0);
/*  473 */       String vga_driver_value = "";
/*  474 */       for (int i = 0; i < maxObjectNumber; i++) {
/*  475 */         String adapter_string = WindowsRegistry.queryRegistrationKey(3, "HARDWARE\\DeviceMap\\Video", "\\Device\\Video" + i);
/*      */ 
/*      */ 
/*      */         
/*  479 */         String root_key = "\\registry\\machine\\";
/*  480 */         if (adapter_string.toLowerCase().startsWith(root_key)) {
/*  481 */           String driver_value = WindowsRegistry.queryRegistrationKey(3, adapter_string.substring(root_key.length()), "InstalledDisplayDrivers");
/*      */ 
/*      */ 
/*      */           
/*  485 */           if (driver_value.toUpperCase().startsWith("VGA")) {
/*  486 */             vga_driver_value = driver_value;
/*  487 */           } else if (!driver_value.toUpperCase().startsWith("RDP") && !driver_value.toUpperCase().startsWith("NMNDD")) {
/*  488 */             return driver_value;
/*      */           } 
/*      */         } 
/*      */       } 
/*  492 */       if (!vga_driver_value.equals("")) {
/*  493 */         return vga_driver_value;
/*      */       }
/*  495 */     } catch (LWJGLException e) {
/*  496 */       LWJGLUtil.log("Exception occurred while querying registry: " + e);
/*      */     } 
/*  498 */     return null;
/*      */   }
/*      */   
/*      */   public String getVersion() {
/*  502 */     String driver = getAdapter();
/*  503 */     if (driver != null) {
/*  504 */       String[] drivers = driver.split(",");
/*  505 */       if (drivers.length > 0) {
/*  506 */         WindowsFileVersion version = nGetVersion(drivers[0] + ".dll");
/*  507 */         if (version != null)
/*  508 */           return version.toString(); 
/*      */       } 
/*      */     } 
/*  511 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public DisplayMode init() throws LWJGLException {
/*  516 */     this.current_gamma = this.saved_gamma = getCurrentGammaRamp();
/*  517 */     return this.current_mode = getCurrentDisplayMode();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTitle(String title) {
/*  522 */     ByteBuffer buffer = MemoryUtil.encodeUTF16(title);
/*  523 */     nSetTitle(this.hwnd, MemoryUtil.getAddress0(buffer));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCloseRequested() {
/*  528 */     boolean saved = this.close_requested;
/*  529 */     this.close_requested = false;
/*  530 */     return saved;
/*      */   }
/*      */   
/*      */   public boolean isVisible() {
/*  534 */     return !this.isMinimized;
/*      */   }
/*      */   
/*      */   public boolean isActive() {
/*  538 */     return this.isFocused;
/*      */   }
/*      */   
/*      */   public boolean isDirty() {
/*  542 */     boolean saved = this.is_dirty;
/*  543 */     this.is_dirty = false;
/*  544 */     return saved;
/*      */   }
/*      */   
/*      */   public PeerInfo createPeerInfo(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
/*  548 */     this.peer_info = new WindowsDisplayPeerInfo(false);
/*  549 */     return this.peer_info;
/*      */   }
/*      */   
/*      */   public void update() {
/*  553 */     nUpdate();
/*      */     
/*  555 */     if (!this.isFocused && this.parent != null && this.parent_focused.compareAndSet(true, false)) {
/*  556 */       setFocus(getHwnd());
/*      */     }
/*      */     
/*  559 */     if (this.redoMakeContextCurrent) {
/*  560 */       this.redoMakeContextCurrent = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  567 */         Context context = ((DrawableLWJGL)Display.getDrawable()).getContext();
/*  568 */         if (context != null && context.isCurrent())
/*  569 */           context.makeCurrent(); 
/*  570 */       } catch (LWJGLException e) {
/*  571 */         LWJGLUtil.log("Exception occurred while trying to make context current: " + e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void reshape(int x, int y, int width, int height) {
/*  578 */     nReshape(getHwnd(), x, y, width, height, (Display.isFullscreen() || isUndecorated()), (this.parent != null));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasWheel() {
/*  585 */     return this.mouse.hasWheel();
/*      */   }
/*      */   
/*      */   public int getButtonCount() {
/*  589 */     return this.mouse.getButtonCount();
/*      */   }
/*      */   
/*      */   public void createMouse() throws LWJGLException {
/*  593 */     this.mouse = new WindowsMouse(getHwnd());
/*      */   }
/*      */   
/*      */   public void destroyMouse() {
/*  597 */     if (this.mouse != null)
/*  598 */       this.mouse.destroy(); 
/*  599 */     this.mouse = null;
/*      */   }
/*      */   
/*      */   public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons) {
/*  603 */     this.mouse.poll(coord_buffer, buttons);
/*      */   }
/*      */   
/*      */   public void readMouse(ByteBuffer buffer) {
/*  607 */     this.mouse.read(buffer);
/*      */   }
/*      */   
/*      */   public void grabMouse(boolean grab) {
/*  611 */     this.mouse.grab(grab, shouldGrab());
/*  612 */     updateCursor();
/*      */   }
/*      */   
/*      */   public int getNativeCursorCapabilities() {
/*  616 */     return 1;
/*      */   }
/*      */   
/*      */   public void setCursorPosition(int x, int y) {
/*  620 */     getGlobalClientRect(getHwnd(), rect);
/*  621 */     int transformed_x = rect.left + x;
/*  622 */     int transformed_y = rect.bottom - 1 - y;
/*  623 */     nSetCursorPosition(transformed_x, transformed_y);
/*  624 */     setMousePosition(x, y);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNativeCursor(Object handle) throws LWJGLException {
/*  629 */     this.current_cursor = handle;
/*  630 */     updateCursor();
/*      */   }
/*      */   
/*      */   private void updateCursor() {
/*      */     try {
/*  635 */       if (this.mouse != null && shouldGrab())
/*  636 */       { nSetNativeCursor(getHwnd(), this.mouse.getBlankCursor()); }
/*      */       else
/*  638 */       { nSetNativeCursor(getHwnd(), this.current_cursor); } 
/*  639 */     } catch (LWJGLException e) {
/*  640 */       LWJGLUtil.log("Failed to update cursor: " + e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMinCursorSize() {
/*  646 */     return getSystemMetrics(13);
/*      */   }
/*      */   
/*      */   public int getMaxCursorSize() {
/*  650 */     return getSystemMetrics(13);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long getHwnd() {
/*  658 */     return this.hwnd;
/*      */   }
/*      */   
/*      */   private long getHdc() {
/*  662 */     return this.hdc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void centerCursor(long hwnd) {
/*  670 */     if (getForegroundWindow() != hwnd && !hasParent)
/*      */       return; 
/*  672 */     getGlobalClientRect(hwnd, rect);
/*  673 */     int local_offset_x = rect.left;
/*  674 */     int local_offset_y = rect.top;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  679 */     int center_x = (rect.left + rect.right) / 2;
/*  680 */     int center_y = (rect.top + rect.bottom) / 2;
/*  681 */     nSetCursorPosition(center_x, center_y);
/*  682 */     int local_x = center_x - local_offset_x;
/*  683 */     int local_y = center_y - local_offset_y;
/*  684 */     if (current_display != null)
/*  685 */       current_display.setMousePosition(local_x, transformY(hwnd, local_y)); 
/*      */   }
/*      */   
/*      */   private void setMousePosition(int x, int y) {
/*  689 */     if (this.mouse != null) {
/*  690 */       this.mouse.setPosition(x, y);
/*      */     }
/*      */   }
/*      */   
/*      */   public void createKeyboard() throws LWJGLException {
/*  695 */     this.keyboard = new WindowsKeyboard();
/*      */   }
/*      */   
/*      */   public void destroyKeyboard() {
/*  699 */     this.keyboard = null;
/*      */   }
/*      */   
/*      */   public void pollKeyboard(ByteBuffer keyDownBuffer) {
/*  703 */     this.keyboard.poll(keyDownBuffer);
/*      */   }
/*      */   
/*      */   public void readKeyboard(ByteBuffer buffer) {
/*  707 */     this.keyboard.read(buffer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
/*  715 */     return doCreateCursor(width, height, xHotspot, yHotspot, numImages, images, delays);
/*      */   }
/*      */   
/*      */   static Object doCreateCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
/*  719 */     return nCreateCursor(width, height, xHotspot, yHotspot, numImages, images, images.position(), delays, (delays != null) ? delays.position() : -1);
/*      */   }
/*      */   
/*      */   public void destroyCursor(Object cursorHandle) {
/*  723 */     doDestroyCursor(cursorHandle);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getPbufferCapabilities() {
/*      */     try {
/*  730 */       return nGetPbufferCapabilities(new PixelFormat(0, 0, 0, 0, 0, 0, 0, 0, false));
/*  731 */     } catch (LWJGLException e) {
/*  732 */       LWJGLUtil.log("Exception occurred while determining pbuffer capabilities: " + e);
/*  733 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBufferLost(PeerInfo handle) {
/*  739 */     return ((WindowsPbufferPeerInfo)handle).isBufferLost();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format, ContextAttribs attribs, IntBuffer pixelFormatCaps, IntBuffer pBufferAttribs) throws LWJGLException {
/*  745 */     return new WindowsPbufferPeerInfo(width, height, pixel_format, pixelFormatCaps, pBufferAttribs);
/*      */   }
/*      */   
/*      */   public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
/*  749 */     ((WindowsPbufferPeerInfo)handle).setPbufferAttrib(attrib, value);
/*      */   }
/*      */   
/*      */   public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
/*  753 */     ((WindowsPbufferPeerInfo)handle).bindTexImageToPbuffer(buffer);
/*      */   }
/*      */   
/*      */   public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
/*  757 */     ((WindowsPbufferPeerInfo)handle).releaseTexImageFromPbuffer(buffer);
/*      */   }
/*      */   
/*      */   private void freeSmallIcon() {
/*  761 */     if (this.small_icon != 0L) {
/*  762 */       destroyIcon(this.small_icon);
/*  763 */       this.small_icon = 0L;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void freeLargeIcon() {
/*  768 */     if (this.large_icon != 0L) {
/*  769 */       destroyIcon(this.large_icon);
/*  770 */       this.large_icon = 0L;
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
/*      */   public int setIcon(ByteBuffer[] icons) {
/*  787 */     boolean done_small = false;
/*  788 */     boolean done_large = false;
/*  789 */     int used = 0;
/*      */     
/*  791 */     int small_icon_size = 16;
/*  792 */     int large_icon_size = 32;
/*  793 */     for (ByteBuffer icon : icons) {
/*  794 */       int size = icon.limit() / 4;
/*      */       
/*  796 */       if ((int)Math.sqrt(size) == small_icon_size && !done_small) {
/*  797 */         long small_new_icon = createIcon(small_icon_size, small_icon_size, icon.asIntBuffer());
/*  798 */         sendMessage(this.hwnd, 128L, 0L, small_new_icon);
/*  799 */         freeSmallIcon();
/*  800 */         this.small_icon = small_new_icon;
/*  801 */         used++;
/*  802 */         done_small = true;
/*      */       } 
/*  804 */       if ((int)Math.sqrt(size) == large_icon_size && !done_large) {
/*  805 */         long large_new_icon = createIcon(large_icon_size, large_icon_size, icon.asIntBuffer());
/*  806 */         sendMessage(this.hwnd, 128L, 1L, large_new_icon);
/*  807 */         freeLargeIcon();
/*  808 */         this.large_icon = large_new_icon;
/*  809 */         used++;
/*  810 */         done_large = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  816 */         this.iconsLoaded = false;
/*      */ 
/*      */         
/*  819 */         long time = System.nanoTime();
/*  820 */         long MAX_WAIT = 500000000L;
/*      */         while (true) {
/*  822 */           nUpdate();
/*  823 */           if (this.iconsLoaded || MAX_WAIT < System.nanoTime() - time) {
/*      */             break;
/*      */           }
/*  826 */           Thread.yield();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  831 */     return used;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleMouseButton(int button, int state, long millis) {
/*  841 */     if (this.mouse != null) {
/*  842 */       this.mouse.handleMouseButton((byte)button, (byte)state, millis);
/*      */ 
/*      */       
/*  845 */       if (this.captureMouse == -1 && button != -1 && state == 1) {
/*  846 */         this.captureMouse = button;
/*  847 */         nSetCapture(this.hwnd);
/*      */       } 
/*      */ 
/*      */       
/*  851 */       if (this.captureMouse != -1 && button == this.captureMouse && state == 0) {
/*  852 */         this.captureMouse = -1;
/*  853 */         nReleaseCapture();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean shouldGrab() {
/*  859 */     return (!this.isMinimized && this.isFocused && Mouse.isGrabbed());
/*      */   }
/*      */   
/*      */   private void handleMouseMoved(int x, int y, long millis) {
/*  863 */     if (this.mouse != null) {
/*  864 */       this.mouse.handleMouseMoved(x, y, millis, shouldGrab());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleMouseScrolled(int amount, long millis) {
/*  872 */     if (this.mouse != null) {
/*  873 */       this.mouse.handleMouseScrolled(amount, millis);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void handleChar(long wParam, long lParam, long millis) {
/*  879 */     byte previous_state = (byte)(int)(lParam >>> 30L & 0x1L);
/*  880 */     byte state = (byte)(int)(1L - (lParam >>> 31L & 0x1L));
/*  881 */     boolean repeat = (state == previous_state);
/*  882 */     if (this.keyboard != null)
/*  883 */       this.keyboard.handleChar((int)(wParam & 0xFFFFL), millis, repeat); 
/*      */   }
/*      */   
/*      */   private void handleKeyButton(long wParam, long lParam, long millis) {
/*  887 */     if (this.keyboard == null) {
/*      */       return;
/*      */     }
/*  890 */     byte previous_state = (byte)(int)(lParam >>> 30L & 0x1L);
/*  891 */     byte state = (byte)(int)(1L - (lParam >>> 31L & 0x1L));
/*  892 */     boolean repeat = (state == previous_state);
/*  893 */     byte extended = (byte)(int)(lParam >>> 24L & 0x1L);
/*  894 */     int scan_code = (int)(lParam >>> 16L & 0xFFL);
/*      */     
/*  896 */     this.keyboard.handleKey((int)wParam, scan_code, (extended != 0), state, millis, repeat);
/*      */   }
/*      */   
/*      */   private static int transformY(long hwnd, int y) {
/*  900 */     getClientRect(hwnd, rect_buffer);
/*  901 */     rect.copyFromBuffer(rect_buffer);
/*  902 */     return rect.bottom - rect.top - 1 - y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long handleMessage(long hwnd, int msg, long wParam, long lParam, long millis) {
/*  910 */     if (current_display != null) {
/*  911 */       return current_display.doHandleMessage(hwnd, msg, wParam, lParam, millis);
/*      */     }
/*  913 */     return defWindowProc(hwnd, msg, wParam, lParam);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkCursorState() {
/*  919 */     updateClipping();
/*      */   }
/*      */   
/*      */   private void updateClipping() {
/*  923 */     if ((Display.isFullscreen() || (this.mouse != null && this.mouse.isGrabbed())) && !this.isMinimized && this.isFocused && (getForegroundWindow() == getHwnd() || hasParent)) {
/*      */       try {
/*  925 */         setupCursorClipping(getHwnd());
/*  926 */       } catch (LWJGLException e) {
/*  927 */         LWJGLUtil.log("setupCursorClipping failed: " + e.getMessage());
/*      */       } 
/*      */     } else {
/*  930 */       resetCursorClipping();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setMinimized(boolean m) {
/*  935 */     this.isMinimized = m;
/*  936 */     checkCursorState();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long doHandleMessage(long hwnd, int msg, long wParam, long lParam, long millis) {
/*      */     int xPos;
/*      */     int yPos;
/*      */     int dwheel;
/*  950 */     if (this.parent != null && !this.isFocused) {
/*  951 */       switch (msg) {
/*      */         case 513:
/*      */         case 516:
/*      */         case 519:
/*      */         case 523:
/*  956 */           sendMessage(this.parent_hwnd, msg, wParam, lParam);
/*      */           break;
/*      */       } 
/*      */     }
/*  960 */     switch (msg) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*  972 */         return 0L;
/*      */       case 5:
/*  974 */         switch ((int)wParam) {
/*      */           case 0:
/*      */           case 2:
/*  977 */             this.resized = true;
/*  978 */             updateWidthAndHeight();
/*  979 */             setMinimized(false);
/*      */             break;
/*      */           case 1:
/*  982 */             setMinimized(true);
/*      */             break;
/*      */         } 
/*      */         break;
/*      */       case 532:
/*  987 */         this.resized = true;
/*  988 */         updateWidthAndHeight();
/*      */         break;
/*      */       case 32:
/*  991 */         if ((lParam & 0xFFFFL) == 1L) {
/*      */ 
/*      */           
/*  994 */           updateCursor();
/*  995 */           return -1L;
/*      */         } 
/*      */         
/*  998 */         return defWindowProc(hwnd, msg, wParam, lParam);
/*      */       
/*      */       case 8:
/* 1001 */         appActivate(false, millis);
/* 1002 */         return 0L;
/*      */       case 7:
/* 1004 */         appActivate(true, millis);
/* 1005 */         return 0L;
/*      */       case 33:
/* 1007 */         if (this.parent != null) {
/* 1008 */           if (!this.isFocused)
/* 1009 */             grabFocus(); 
/* 1010 */           return 3L;
/*      */         } 
/*      */         break;
/*      */       case 512:
/* 1014 */         xPos = (short)(int)(lParam & 0xFFFFL);
/* 1015 */         yPos = transformY(getHwnd(), (short)(int)(lParam >> 16L & 0xFFFFL));
/* 1016 */         handleMouseMoved(xPos, yPos, millis);
/* 1017 */         checkCursorState();
/* 1018 */         this.mouseInside = true;
/* 1019 */         if (!this.trackingMouse) {
/* 1020 */           this.trackingMouse = nTrackMouseEvent(hwnd);
/*      */         }
/* 1022 */         return 0L;
/*      */       case 522:
/* 1024 */         dwheel = (short)(int)(wParam >> 16L & 0xFFFFL);
/* 1025 */         handleMouseScrolled(dwheel, millis);
/* 1026 */         return 0L;
/*      */       case 513:
/* 1028 */         handleMouseButton(0, 1, millis);
/* 1029 */         return 0L;
/*      */       case 514:
/* 1031 */         handleMouseButton(0, 0, millis);
/* 1032 */         return 0L;
/*      */       case 516:
/* 1034 */         handleMouseButton(1, 1, millis);
/* 1035 */         return 0L;
/*      */       case 517:
/* 1037 */         handleMouseButton(1, 0, millis);
/* 1038 */         return 0L;
/*      */       case 519:
/* 1040 */         handleMouseButton(2, 1, millis);
/* 1041 */         return 0L;
/*      */       case 520:
/* 1043 */         handleMouseButton(2, 0, millis);
/* 1044 */         return 0L;
/*      */       case 524:
/* 1046 */         if (wParam >> 16L == 1L) {
/* 1047 */           handleMouseButton(3, 0, millis);
/*      */         } else {
/* 1049 */           handleMouseButton(4, 0, millis);
/*      */         } 
/* 1051 */         return 1L;
/*      */       case 523:
/* 1053 */         if ((wParam & 0xFFL) == 32L) {
/* 1054 */           handleMouseButton(3, 1, millis);
/*      */         } else {
/* 1056 */           handleMouseButton(4, 1, millis);
/*      */         } 
/* 1058 */         return 1L;
/*      */       case 258:
/*      */       case 262:
/* 1061 */         handleChar(wParam, lParam, millis);
/* 1062 */         return 0L;
/*      */       
/*      */       case 261:
/* 1065 */         if (wParam == 18L || wParam == 121L) {
/* 1066 */           handleKeyButton(wParam, lParam, millis);
/* 1067 */           return 0L;
/*      */         } 
/*      */ 
/*      */       
/*      */       case 257:
/* 1072 */         if (wParam == 44L && this.keyboard != null && !this.keyboard.isKeyDown(183)) {
/*      */ 
/*      */           
/* 1075 */           long fake_lparam = lParam & 0x7FFFFFFFL;
/*      */           
/* 1077 */           fake_lparam &= 0xFFFFFFFFBFFFFFFFL;
/* 1078 */           handleKeyButton(wParam, fake_lparam, millis);
/*      */         } 
/*      */ 
/*      */       
/*      */       case 256:
/*      */       case 260:
/* 1084 */         handleKeyButton(wParam, lParam, millis);
/*      */         break;
/*      */       case 18:
/* 1087 */         this.close_requested = true;
/* 1088 */         return 0L;
/*      */       case 274:
/* 1090 */         switch ((int)(wParam & 0xFFF0L)) {
/*      */           case 61760:
/*      */           case 61808:
/* 1093 */             return 0L;
/*      */           case 61536:
/* 1095 */             this.close_requested = true;
/* 1096 */             return 0L;
/*      */         } 
/*      */         break;
/*      */       case 15:
/* 1100 */         this.is_dirty = true;
/*      */         break;
/*      */       case 675:
/* 1103 */         this.mouseInside = false;
/* 1104 */         this.trackingMouse = false;
/*      */         break;
/*      */       case 31:
/* 1107 */         nReleaseCapture();
/*      */       
/*      */       case 533:
/* 1110 */         if (this.captureMouse != -1) {
/* 1111 */           handleMouseButton(this.captureMouse, 0, millis);
/* 1112 */           this.captureMouse = -1;
/*      */         } 
/* 1114 */         return 0L;
/*      */       case 71:
/* 1116 */         if (getWindowRect(hwnd, rect_buffer)) {
/* 1117 */           rect.copyFromBuffer(rect_buffer);
/* 1118 */           this.x = rect.left;
/* 1119 */           this.y = rect.top; break;
/*      */         } 
/* 1121 */         LWJGLUtil.log("WM_WINDOWPOSCHANGED: Unable to get window rect");
/*      */         break;
/*      */       
/*      */       case 127:
/* 1125 */         this.iconsLoaded = true;
/*      */         break;
/*      */     } 
/*      */     
/* 1129 */     return defWindowProc(hwnd, msg, wParam, lParam);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getX() {
/* 1135 */     return this.x;
/*      */   }
/*      */   
/*      */   public int getY() {
/* 1139 */     return this.y;
/*      */   }
/*      */   
/*      */   public int getWidth() {
/* 1143 */     return this.width;
/*      */   }
/*      */   
/*      */   public int getHeight() {
/* 1147 */     return this.height;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInsideWindow() {
/* 1153 */     return this.mouseInside;
/*      */   }
/*      */   
/*      */   public void setResizable(boolean resizable) {
/* 1157 */     if (this.resizable == resizable) {
/*      */       return;
/*      */     }
/* 1160 */     this.resized = false;
/* 1161 */     this.resizable = resizable;
/*      */     
/* 1163 */     int style = (int)getWindowLongPtr(this.hwnd, -16);
/* 1164 */     int styleex = (int)getWindowLongPtr(this.hwnd, -20);
/*      */ 
/*      */     
/* 1167 */     setWindowLongPtr(this.hwnd, -16, (style = (resizable && !Display.isFullscreen()) ? (style | 0x50000) : (style & 0xFFFAFFFF)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1177 */     getGlobalClientRect(this.hwnd, rect);
/* 1178 */     rect.copyToBuffer(rect_buffer);
/* 1179 */     adjustWindowRectEx(rect_buffer, style, false, styleex);
/* 1180 */     rect.copyFromBuffer(rect_buffer);
/*      */ 
/*      */     
/* 1183 */     setWindowPos(this.hwnd, 0L, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, 36L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1192 */     updateWidthAndHeight();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean wasResized() {
/* 1198 */     if (this.resized) {
/* 1199 */       this.resized = false;
/* 1200 */       return true;
/*      */     } 
/* 1202 */     return false;
/*      */   } private static native long nCreateWindow(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, long paramLong) throws LWJGLException; private static native void nReleaseDC(long paramLong1, long paramLong2); private static native void nDestroyWindow(long paramLong); private static native void clipCursor(IntBuffer paramIntBuffer) throws LWJGLException; private static native void nSwitchDisplayMode(DisplayMode paramDisplayMode) throws LWJGLException; private static native void showWindow(long paramLong, int paramInt); private static native void setForegroundWindow(long paramLong); private static native void setFocus(long paramLong); private static native void nResetDisplayMode(); private static native ByteBuffer convertToNativeRamp(FloatBuffer paramFloatBuffer) throws LWJGLException; private static native ByteBuffer getCurrentGammaRamp() throws LWJGLException; private static native void nSetGammaRamp(ByteBuffer paramByteBuffer) throws LWJGLException; private native WindowsFileVersion nGetVersion(String paramString); private static native DisplayMode getCurrentDisplayMode() throws LWJGLException; private static native void nSetTitle(long paramLong1, long paramLong2); private static native void nUpdate(); private static native void nReshape(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2); public native DisplayMode[] getAvailableDisplayModes() throws LWJGLException; private static native void nSetCursorPosition(int paramInt1, int paramInt2); static native void nSetNativeCursor(long paramLong, Object paramObject) throws LWJGLException; static native int getSystemMetrics(int paramInt);
/*      */   private static native long getDllInstance();
/*      */   public float getPixelScaleFactor() {
/* 1206 */     return 1.0F;
/*      */   } private static native long getDC(long paramLong); private static native long getDesktopWindow(); private static native long getForegroundWindow(); public static native ByteBuffer nCreateCursor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, IntBuffer paramIntBuffer1, int paramInt6, IntBuffer paramIntBuffer2, int paramInt7) throws LWJGLException; static native void doDestroyCursor(Object paramObject); private native int nGetPbufferCapabilities(PixelFormat paramPixelFormat) throws LWJGLException; private static native long createIcon(int paramInt1, int paramInt2, IntBuffer paramIntBuffer); private static native void destroyIcon(long paramLong); private static native long sendMessage(long paramLong1, long paramLong2, long paramLong3, long paramLong4); private static native long setWindowLongPtr(long paramLong1, int paramInt, long paramLong2); private static native long getWindowLongPtr(long paramLong, int paramInt); private static native boolean setWindowPos(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong3); private static native long nSetCapture(long paramLong);
/*      */   private static native boolean nReleaseCapture();
/*      */   private static native void getClientRect(long paramLong, IntBuffer paramIntBuffer);
/*      */   private static native void clientToScreen(long paramLong, IntBuffer paramIntBuffer);
/*      */   private static native void setWindowProc(Method paramMethod);
/*      */   private static native long defWindowProc(long paramLong1, int paramInt, long paramLong2, long paramLong3);
/*      */   private native boolean getWindowRect(long paramLong, IntBuffer paramIntBuffer);
/*      */   private native boolean nTrackMouseEvent(long paramLong);
/*      */   private native boolean adjustWindowRectEx(IntBuffer paramIntBuffer, int paramInt1, boolean paramBoolean, int paramInt2);
/*      */   private static final class Rect { public int left; public int top;
/*      */     public void copyToBuffer(IntBuffer buffer) {
/* 1218 */       buffer.put(0, this.left).put(1, this.top).put(2, this.right).put(3, this.bottom);
/*      */     }
/*      */     public int right;
/*      */     public int bottom;
/*      */     
/*      */     private Rect() {}
/*      */     
/*      */     public void copyFromBuffer(IntBuffer buffer) {
/* 1226 */       this.left = buffer.get(0);
/* 1227 */       this.top = buffer.get(1);
/* 1228 */       this.right = buffer.get(2);
/* 1229 */       this.bottom = buffer.get(3);
/*      */     }
/*      */     
/*      */     public void offset(int offset_x, int offset_y) {
/* 1233 */       this.left += offset_x;
/* 1234 */       this.top += offset_y;
/* 1235 */       this.right += offset_x;
/* 1236 */       this.bottom += offset_y;
/*      */     }
/*      */     
/*      */     public static void intersect(Rect r1, Rect r2, Rect dst) {
/* 1240 */       dst.left = Math.max(r1.left, r2.left);
/* 1241 */       dst.top = Math.max(r1.top, r2.top);
/* 1242 */       dst.right = Math.min(r1.right, r2.right);
/* 1243 */       dst.bottom = Math.min(r1.bottom, r2.bottom);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1247 */       return "Rect: left = " + this.left + " top = " + this.top + " right = " + this.right + " bottom = " + this.bottom + ", width: " + (this.right - this.left) + ", height: " + (this.bottom - this.top);
/*      */     } }
/*      */ 
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\WindowsDisplay.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */