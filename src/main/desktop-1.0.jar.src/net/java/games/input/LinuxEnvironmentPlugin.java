/*     */ package net.java.games.input;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.java.games.util.plugins.Plugin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LinuxEnvironmentPlugin
/*     */   extends ControllerEnvironment
/*     */   implements Plugin
/*     */ {
/*     */   private static final String LIBNAME = "jinput-linux";
/*     */   private static final String POSTFIX64BIT = "64";
/*     */   private static boolean supported = false;
/*     */   private final Controller[] controllers;
/*  50 */   private final List devices = new ArrayList();
/*  51 */   private static final LinuxDeviceThread device_thread = new LinuxDeviceThread();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void loadLibrary(final String lib_name) {
/*  61 */     AccessController.doPrivileged(new PrivilegedAction() { private final String val$lib_name;
/*     */           
/*     */           public final Object run() {
/*  64 */             String lib_path = System.getProperty("net.java.games.input.librarypath");
/*     */             try {
/*  66 */               if (lib_path != null)
/*  67 */               { System.load(lib_path + File.separator + System.mapLibraryName(lib_name)); }
/*     */               else
/*  69 */               { System.loadLibrary(lib_name); } 
/*  70 */             } catch (UnsatisfiedLinkError e) {
/*  71 */               ControllerEnvironment.logln("Failed to load library: " + e.getMessage());
/*  72 */               e.printStackTrace();
/*  73 */               LinuxEnvironmentPlugin.supported = false;
/*     */             } 
/*  75 */             return null;
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   static String getPrivilegedProperty(final String property) {
/*  81 */     return AccessController.<String>doPrivileged(new PrivilegedAction() { private final String val$property;
/*     */           public Object run() {
/*  83 */             return System.getProperty(property);
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */   
/*     */   static String getPrivilegedProperty(final String property, final String default_value) {
/*  90 */     return AccessController.<String>doPrivileged(new PrivilegedAction() { private final String val$property; private final String val$default_value;
/*     */           public Object run() {
/*  92 */             return System.getProperty(property, default_value);
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   static {
/*  98 */     String osName = getPrivilegedProperty("os.name", "").trim();
/*  99 */     if (osName.equals("Linux")) {
/* 100 */       supported = true;
/* 101 */       if ("i386".equals(getPrivilegedProperty("os.arch"))) {
/* 102 */         loadLibrary("jinput-linux");
/*     */       } else {
/* 104 */         loadLibrary("jinput-linux64");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final Object execute(LinuxDeviceTask task) throws IOException {
/* 110 */     return device_thread.execute(task);
/*     */   }
/*     */   
/*     */   public LinuxEnvironmentPlugin() {
/* 114 */     if (isSupported()) {
/* 115 */       this.controllers = enumerateControllers();
/* 116 */       logln("Linux plugin claims to have found " + this.controllers.length + " controllers");
/* 117 */       AccessController.doPrivileged(new PrivilegedAction() { private final LinuxEnvironmentPlugin this$0;
/*     */             
/*     */             public final Object run() {
/* 120 */               Runtime.getRuntime().addShutdownHook(new LinuxEnvironmentPlugin.ShutdownHook());
/* 121 */               return null;
/*     */             } }
/*     */         );
/*     */     } else {
/* 125 */       this.controllers = new Controller[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Controller[] getControllers() {
/* 135 */     return this.controllers;
/*     */   }
/*     */   
/*     */   private static final Component[] createComponents(List event_components, LinuxEventDevice device) {
/* 139 */     LinuxEventComponent[][] povs = new LinuxEventComponent[4][2];
/* 140 */     List components = new ArrayList(); int i;
/* 141 */     for (i = 0; i < event_components.size(); i++) {
/* 142 */       LinuxEventComponent event_component = event_components.get(i);
/* 143 */       Component.Identifier identifier = event_component.getIdentifier();
/*     */       
/* 145 */       if (identifier == Component.Identifier.Axis.POV) {
/* 146 */         int native_code = event_component.getDescriptor().getCode();
/* 147 */         switch (native_code) {
/*     */           case 16:
/* 149 */             povs[0][0] = event_component;
/*     */             break;
/*     */           case 17:
/* 152 */             povs[0][1] = event_component;
/*     */             break;
/*     */           case 18:
/* 155 */             povs[1][0] = event_component;
/*     */             break;
/*     */           case 19:
/* 158 */             povs[1][1] = event_component;
/*     */             break;
/*     */           case 20:
/* 161 */             povs[2][0] = event_component;
/*     */             break;
/*     */           case 21:
/* 164 */             povs[2][1] = event_component;
/*     */             break;
/*     */           case 22:
/* 167 */             povs[3][0] = event_component;
/*     */             break;
/*     */           case 23:
/* 170 */             povs[3][1] = event_component;
/*     */             break;
/*     */           default:
/* 173 */             logln("Unknown POV instance: " + native_code);
/*     */             break;
/*     */         } 
/* 176 */       } else if (identifier != null) {
/* 177 */         LinuxComponent component = new LinuxComponent(event_component);
/* 178 */         components.add(component);
/* 179 */         device.registerComponent(event_component.getDescriptor(), component);
/*     */       } 
/*     */     } 
/* 182 */     for (i = 0; i < povs.length; i++) {
/* 183 */       LinuxEventComponent x = povs[i][0];
/* 184 */       LinuxEventComponent y = povs[i][1];
/* 185 */       if (x != null && y != null) {
/* 186 */         LinuxComponent controller_component = new LinuxPOV(x, y);
/* 187 */         components.add(controller_component);
/* 188 */         device.registerComponent(x.getDescriptor(), controller_component);
/* 189 */         device.registerComponent(y.getDescriptor(), controller_component);
/*     */       } 
/*     */     } 
/* 192 */     Component[] components_array = new Component[components.size()];
/* 193 */     components.toArray(components_array);
/* 194 */     return components_array;
/*     */   }
/*     */   
/*     */   private static final Mouse createMouseFromDevice(LinuxEventDevice device, Component[] components) throws IOException {
/* 198 */     Mouse mouse = new LinuxMouse(device, components, new Controller[0], device.getRumblers());
/* 199 */     if (mouse.getX() != null && mouse.getY() != null && mouse.getPrimaryButton() != null) {
/* 200 */       return mouse;
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */   
/*     */   private static final Keyboard createKeyboardFromDevice(LinuxEventDevice device, Component[] components) throws IOException {
/* 206 */     Keyboard keyboard = new LinuxKeyboard(device, components, new Controller[0], device.getRumblers());
/* 207 */     return keyboard;
/*     */   }
/*     */   
/*     */   private static final Controller createJoystickFromDevice(LinuxEventDevice device, Component[] components, Controller.Type type) throws IOException {
/* 211 */     Controller joystick = new LinuxAbstractController(device, components, new Controller[0], device.getRumblers(), type);
/* 212 */     return joystick;
/*     */   }
/*     */   
/*     */   private static final Controller createControllerFromDevice(LinuxEventDevice device) throws IOException {
/* 216 */     List event_components = device.getComponents();
/* 217 */     Component[] components = createComponents(event_components, device);
/* 218 */     Controller.Type type = device.getType();
/*     */     
/* 220 */     if (type == Controller.Type.MOUSE)
/* 221 */       return createMouseFromDevice(device, components); 
/* 222 */     if (type == Controller.Type.KEYBOARD)
/* 223 */       return createKeyboardFromDevice(device, components); 
/* 224 */     if (type == Controller.Type.STICK || type == Controller.Type.GAMEPAD) {
/* 225 */       return createJoystickFromDevice(device, components, type);
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */   
/*     */   private final Controller[] enumerateControllers() {
/* 231 */     List controllers = new ArrayList();
/* 232 */     List eventControllers = new ArrayList();
/* 233 */     List jsControllers = new ArrayList();
/* 234 */     enumerateEventControllers(eventControllers);
/* 235 */     enumerateJoystickControllers(jsControllers);
/*     */     
/* 237 */     for (int i = 0; i < eventControllers.size(); i++) {
/* 238 */       for (int j = 0; j < jsControllers.size(); j++) {
/* 239 */         Controller evController = eventControllers.get(i);
/* 240 */         Controller jsController = jsControllers.get(j);
/*     */ 
/*     */ 
/*     */         
/* 244 */         if (evController.getName().equals(jsController.getName())) {
/*     */           
/* 246 */           Component[] evComponents = evController.getComponents();
/* 247 */           Component[] jsComponents = jsController.getComponents();
/* 248 */           if (evComponents.length == jsComponents.length) {
/* 249 */             boolean foundADifference = false;
/*     */             
/* 251 */             for (int k = 0; k < evComponents.length; k++) {
/*     */               
/* 253 */               if (evComponents[k].getIdentifier() != jsComponents[k].getIdentifier()) {
/* 254 */                 foundADifference = true;
/*     */               }
/*     */             } 
/*     */             
/* 258 */             if (!foundADifference) {
/* 259 */               controllers.add(new LinuxCombinedController((LinuxAbstractController)eventControllers.remove(i), (LinuxJoystickAbstractController)jsControllers.remove(j)));
/* 260 */               i--;
/* 261 */               j--;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 268 */     controllers.addAll(eventControllers);
/* 269 */     controllers.addAll(jsControllers);
/*     */     
/* 271 */     Controller[] controllers_array = new Controller[controllers.size()];
/* 272 */     controllers.toArray(controllers_array);
/* 273 */     return controllers_array;
/*     */   }
/*     */   
/*     */   private static final Component.Identifier.Button getButtonIdentifier(int index) {
/* 277 */     switch (index) {
/*     */       case 0:
/* 279 */         return Component.Identifier.Button._0;
/*     */       case 1:
/* 281 */         return Component.Identifier.Button._1;
/*     */       case 2:
/* 283 */         return Component.Identifier.Button._2;
/*     */       case 3:
/* 285 */         return Component.Identifier.Button._3;
/*     */       case 4:
/* 287 */         return Component.Identifier.Button._4;
/*     */       case 5:
/* 289 */         return Component.Identifier.Button._5;
/*     */       case 6:
/* 291 */         return Component.Identifier.Button._6;
/*     */       case 7:
/* 293 */         return Component.Identifier.Button._7;
/*     */       case 8:
/* 295 */         return Component.Identifier.Button._8;
/*     */       case 9:
/* 297 */         return Component.Identifier.Button._9;
/*     */       case 10:
/* 299 */         return Component.Identifier.Button._10;
/*     */       case 11:
/* 301 */         return Component.Identifier.Button._11;
/*     */       case 12:
/* 303 */         return Component.Identifier.Button._12;
/*     */       case 13:
/* 305 */         return Component.Identifier.Button._13;
/*     */       case 14:
/* 307 */         return Component.Identifier.Button._14;
/*     */       case 15:
/* 309 */         return Component.Identifier.Button._15;
/*     */       case 16:
/* 311 */         return Component.Identifier.Button._16;
/*     */       case 17:
/* 313 */         return Component.Identifier.Button._17;
/*     */       case 18:
/* 315 */         return Component.Identifier.Button._18;
/*     */       case 19:
/* 317 */         return Component.Identifier.Button._19;
/*     */       case 20:
/* 319 */         return Component.Identifier.Button._20;
/*     */       case 21:
/* 321 */         return Component.Identifier.Button._21;
/*     */       case 22:
/* 323 */         return Component.Identifier.Button._22;
/*     */       case 23:
/* 325 */         return Component.Identifier.Button._23;
/*     */       case 24:
/* 327 */         return Component.Identifier.Button._24;
/*     */       case 25:
/* 329 */         return Component.Identifier.Button._25;
/*     */       case 26:
/* 331 */         return Component.Identifier.Button._26;
/*     */       case 27:
/* 333 */         return Component.Identifier.Button._27;
/*     */       case 28:
/* 335 */         return Component.Identifier.Button._28;
/*     */       case 29:
/* 337 */         return Component.Identifier.Button._29;
/*     */       case 30:
/* 339 */         return Component.Identifier.Button._30;
/*     */       case 31:
/* 341 */         return Component.Identifier.Button._31;
/*     */     } 
/* 343 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final Controller createJoystickFromJoystickDevice(LinuxJoystickDevice device) {
/* 348 */     List components = new ArrayList();
/* 349 */     byte[] axisMap = device.getAxisMap();
/* 350 */     char[] buttonMap = device.getButtonMap();
/* 351 */     LinuxJoystickAxis[] hatBits = new LinuxJoystickAxis[6];
/*     */     int i;
/* 353 */     for (i = 0; i < device.getNumButtons(); i++) {
/* 354 */       Component.Identifier button_id = LinuxNativeTypesMap.getButtonID(buttonMap[i]);
/* 355 */       if (button_id != null) {
/* 356 */         LinuxJoystickButton button = new LinuxJoystickButton(button_id);
/* 357 */         device.registerButton(i, button);
/* 358 */         components.add(button);
/*     */       } 
/*     */     } 
/* 361 */     for (i = 0; i < device.getNumAxes(); i++) {
/*     */       
/* 363 */       Component.Identifier.Axis axis_id = (Component.Identifier.Axis)LinuxNativeTypesMap.getAbsAxisID(axisMap[i]);
/* 364 */       LinuxJoystickAxis axis = new LinuxJoystickAxis(axis_id);
/*     */       
/* 366 */       device.registerAxis(i, axis);
/*     */       
/* 368 */       if (axisMap[i] == 16) {
/* 369 */         hatBits[0] = axis;
/* 370 */       } else if (axisMap[i] == 17) {
/* 371 */         hatBits[1] = axis;
/* 372 */         axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[0], hatBits[1]);
/* 373 */         device.registerPOV((LinuxJoystickPOV)axis);
/* 374 */         components.add(axis);
/* 375 */       } else if (axisMap[i] == 18) {
/* 376 */         hatBits[2] = axis;
/* 377 */       } else if (axisMap[i] == 19) {
/* 378 */         hatBits[3] = axis;
/* 379 */         axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[2], hatBits[3]);
/* 380 */         device.registerPOV((LinuxJoystickPOV)axis);
/* 381 */         components.add(axis);
/* 382 */       } else if (axisMap[i] == 20) {
/* 383 */         hatBits[4] = axis;
/* 384 */       } else if (axisMap[i] == 21) {
/* 385 */         hatBits[5] = axis;
/* 386 */         axis = new LinuxJoystickPOV(Component.Identifier.Axis.POV, hatBits[4], hatBits[5]);
/* 387 */         device.registerPOV((LinuxJoystickPOV)axis);
/* 388 */         components.add(axis);
/*     */       } else {
/* 390 */         components.add(axis);
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     return new LinuxJoystickAbstractController(device, components.<Component>toArray(new Component[0]), new Controller[0], new Rumbler[0]);
/*     */   }
/*     */   
/*     */   private final void enumerateJoystickControllers(List controllers) {
/* 398 */     File[] joystick_device_files = enumerateJoystickDeviceFiles("/dev/input");
/* 399 */     if (joystick_device_files == null || joystick_device_files.length == 0) {
/* 400 */       joystick_device_files = enumerateJoystickDeviceFiles("/dev");
/* 401 */       if (joystick_device_files == null)
/*     */         return; 
/*     */     } 
/* 404 */     for (int i = 0; i < joystick_device_files.length; i++) {
/* 405 */       File event_file = joystick_device_files[i];
/*     */       try {
/* 407 */         String path = getAbsolutePathPrivileged(event_file);
/* 408 */         LinuxJoystickDevice device = new LinuxJoystickDevice(path);
/* 409 */         Controller controller = createJoystickFromJoystickDevice(device);
/* 410 */         if (controller != null)
/* 411 */         { controllers.add(controller);
/* 412 */           this.devices.add(device); }
/*     */         else
/* 414 */         { device.close(); } 
/* 415 */       } catch (IOException e) {
/* 416 */         logln("Failed to open device (" + event_file + "): " + e.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final File[] enumerateJoystickDeviceFiles(String dev_path) {
/* 422 */     File dev = new File(dev_path);
/* 423 */     return listFilesPrivileged(dev, new FilenameFilter() {
/*     */           public final boolean accept(File dir, String name) {
/* 425 */             return name.startsWith("js");
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static String getAbsolutePathPrivileged(final File file) {
/* 431 */     return AccessController.<String>doPrivileged(new PrivilegedAction() { private final File val$file;
/*     */           public Object run() {
/* 433 */             return file.getAbsolutePath();
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   private static File[] listFilesPrivileged(final File dir, final FilenameFilter filter) {
/* 439 */     return AccessController.<File[]>doPrivileged(new PrivilegedAction() { private final File val$dir; private final FilenameFilter val$filter;
/*     */           public Object run() {
/* 441 */             File[] files = dir.listFiles(filter);
/* 442 */             Arrays.sort(files, new Comparator() { private final LinuxEnvironmentPlugin.null this$0;
/*     */                   public int compare(Object f1, Object f2) {
/* 444 */                     return ((File)f1).getName().compareTo(((File)f2).getName());
/*     */                   } }
/*     */               );
/* 447 */             return files;
/*     */           } }
/*     */       );
/*     */   }
/*     */   
/*     */   private final void enumerateEventControllers(List controllers) {
/* 453 */     File dev = new File("/dev/input");
/* 454 */     File[] event_device_files = listFilesPrivileged(dev, new FilenameFilter() { private final LinuxEnvironmentPlugin this$0;
/*     */           public final boolean accept(File dir, String name) {
/* 456 */             return name.startsWith("event");
/*     */           } }
/*     */       );
/* 459 */     if (event_device_files == null)
/*     */       return; 
/* 461 */     for (int i = 0; i < event_device_files.length; i++) {
/* 462 */       File event_file = event_device_files[i];
/*     */       try {
/* 464 */         String path = getAbsolutePathPrivileged(event_file);
/* 465 */         LinuxEventDevice device = new LinuxEventDevice(path);
/*     */         try {
/* 467 */           Controller controller = createControllerFromDevice(device);
/* 468 */           if (controller != null)
/* 469 */           { controllers.add(controller);
/* 470 */             this.devices.add(device); }
/*     */           else
/* 472 */           { device.close(); } 
/* 473 */         } catch (IOException e) {
/* 474 */           logln("Failed to create Controller: " + e.getMessage());
/* 475 */           device.close();
/*     */         } 
/* 477 */       } catch (IOException e) {
/* 478 */         logln("Failed to open device (" + event_file + "): " + e.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private final class ShutdownHook extends Thread { private final LinuxEnvironmentPlugin this$0;
/*     */     
/*     */     public final void run() {
/* 485 */       for (int i = 0; i < LinuxEnvironmentPlugin.this.devices.size(); i++) {
/*     */         try {
/* 487 */           LinuxDevice device = LinuxEnvironmentPlugin.this.devices.get(i);
/* 488 */           device.close();
/* 489 */         } catch (IOException e) {
/* 490 */           ControllerEnvironment.logln("Failed to close device: " + e.getMessage());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     private ShutdownHook() {} }
/*     */   
/*     */   public boolean isSupported() {
/* 497 */     return supported;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\LinuxEnvironmentPlugin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */