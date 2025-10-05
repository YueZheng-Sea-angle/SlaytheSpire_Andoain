/*     */ package org.lwjgl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Sys
/*     */ {
/*     */   private static final String JNI_LIBRARY_NAME = "lwjgl";
/*     */   private static final String VERSION = "2.9.2";
/*     */   private static final String POSTFIX64BIT = "64";
/*     */   
/*     */   private static void doLoadLibrary(final String lib_name) {
/*  66 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/*  68 */             String library_path = System.getProperty("org.lwjgl.librarypath");
/*  69 */             if (library_path != null) {
/*  70 */               System.load(library_path + File.separator + LWJGLUtil.mapLibraryName(lib_name));
/*     */             } else {
/*  72 */               System.loadLibrary(lib_name);
/*     */             } 
/*  74 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadLibrary(String lib_name) {
/*  81 */     String osArch = System.getProperty("os.arch");
/*  82 */     boolean try64First = (LWJGLUtil.getPlatform() != 2 && ("amd64".equals(osArch) || "x86_64".equals(osArch)));
/*     */     
/*  84 */     Error err = null;
/*  85 */     if (try64First) {
/*     */       try {
/*  87 */         doLoadLibrary(lib_name + "64");
/*     */         return;
/*  89 */       } catch (UnsatisfiedLinkError e) {
/*  90 */         err = e;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  96 */       doLoadLibrary(lib_name);
/*  97 */     } catch (UnsatisfiedLinkError e) {
/*  98 */       if (try64First) {
/*  99 */         throw err;
/*     */       }
/* 101 */       if (implementation.has64Bit()) {
/*     */         try {
/* 103 */           doLoadLibrary(lib_name + "64");
/*     */           return;
/* 105 */         } catch (UnsatisfiedLinkError e2) {
/* 106 */           LWJGLUtil.log("Failed to load 64 bit library: " + e2.getMessage());
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 111 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 116 */   private static final SysImplementation implementation = createImplementation(); static {
/* 117 */     loadLibrary("lwjgl");
/* 118 */   } private static final boolean is64Bit = (implementation.getPointerSize() == 8);
/*     */   static {
/* 120 */     int native_jni_version = implementation.getJNIVersion();
/* 121 */     int required_version = implementation.getRequiredJNIVersion();
/* 122 */     if (native_jni_version != required_version) {
/* 123 */       throw new LinkageError("Version mismatch: jar version is '" + required_version + "', native library version is '" + native_jni_version + "'");
/*     */     }
/* 125 */     implementation.setDebug(LWJGLUtil.DEBUG);
/*     */   }
/*     */   
/*     */   private static SysImplementation createImplementation() {
/* 129 */     switch (LWJGLUtil.getPlatform()) {
/*     */       case 1:
/* 131 */         return new LinuxSysImplementation();
/*     */       case 3:
/* 133 */         return new WindowsSysImplementation();
/*     */       case 2:
/* 135 */         return new MacOSXSysImplementation();
/*     */     } 
/* 137 */     throw new IllegalStateException("Unsupported platform");
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
/*     */   public static String getVersion() {
/* 151 */     return "2.9.2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initialize() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean is64Bit() {
/* 162 */     return is64Bit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getTimerResolution() {
/* 172 */     return implementation.getTimerResolution();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getTime() {
/* 183 */     return implementation.getTime() & Long.MAX_VALUE;
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
/*     */   public static void alert(String title, String message) {
/* 205 */     boolean grabbed = Mouse.isGrabbed();
/* 206 */     if (grabbed) {
/* 207 */       Mouse.setGrabbed(false);
/*     */     }
/* 209 */     if (title == null)
/* 210 */       title = ""; 
/* 211 */     if (message == null)
/* 212 */       message = ""; 
/* 213 */     implementation.alert(title, message);
/* 214 */     if (grabbed) {
/* 215 */       Mouse.setGrabbed(true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean openURL(String url) {
/*     */     try {
/* 236 */       final Class<?> serviceManagerClass = Class.forName("javax.jnlp.ServiceManager");
/* 237 */       Method lookupMethod = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Method run() throws Exception {
/* 239 */               return serviceManagerClass.getMethod("lookup", new Class[] { String.class });
/*     */             }
/*     */           });
/* 242 */       Object basicService = lookupMethod.invoke(serviceManagerClass, new Object[] { "javax.jnlp.BasicService" });
/* 243 */       final Class<?> basicServiceClass = Class.forName("javax.jnlp.BasicService");
/* 244 */       Method showDocumentMethod = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Method run() throws Exception {
/* 246 */               return basicServiceClass.getMethod("showDocument", new Class[] { URL.class });
/*     */             }
/*     */           });
/*     */       try {
/* 250 */         Boolean ret = (Boolean)showDocumentMethod.invoke(basicService, new Object[] { new URL(url) });
/* 251 */         return ret.booleanValue();
/* 252 */       } catch (MalformedURLException e) {
/* 253 */         e.printStackTrace(System.err);
/* 254 */         return false;
/*     */       } 
/* 256 */     } catch (Exception ue) {
/* 257 */       return implementation.openURL(url);
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
/*     */   
/*     */   public static String getClipboard() {
/* 270 */     return implementation.getClipboard();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\Sys.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */