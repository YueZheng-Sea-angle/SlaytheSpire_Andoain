/*     */ package net.java.games.input;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Logger;
/*     */ import net.java.games.util.plugins.Plugins;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DefaultControllerEnvironment
/*     */   extends ControllerEnvironment
/*     */ {
/*     */   static String libPath;
/*  65 */   private static Logger log = Logger.getLogger(DefaultControllerEnvironment.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList controllers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void loadLibrary(final String lib_name) {
/*  75 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public final Object run() {
/*  78 */             String lib_path = System.getProperty("net.java.games.input.librarypath");
/*  79 */             if (lib_path != null) {
/*  80 */               System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
/*     */             } else {
/*  82 */               System.loadLibrary(lib_name);
/*  83 */             }  return null;
/*     */           }
/*     */           private final String val$lib_name;
/*     */         });
/*     */   }
/*     */   static String getPrivilegedProperty(final String property) {
/*  89 */     return AccessController.<String>doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/*  91 */             return System.getProperty(property);
/*     */           }
/*     */           private final String val$property;
/*     */         });
/*     */   }
/*     */   
/*     */   static String getPrivilegedProperty(final String property, final String default_value) {
/*  98 */     return AccessController.<String>doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/* 100 */             return System.getProperty(property, default_value);
/*     */           }
/*     */ 
/*     */           
/*     */           private final String val$property;
/*     */           
/*     */           private final String val$default_value;
/*     */         });
/*     */   }
/*     */   
/* 110 */   private Collection loadedPlugins = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Controller[] getControllers() {
/* 123 */     if (this.controllers == null) {
/*     */       
/* 125 */       this.controllers = new ArrayList();
/* 126 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public Object run() {
/* 128 */               DefaultControllerEnvironment.this.scanControllers();
/* 129 */               return null;
/*     */             }
/*     */             private final DefaultControllerEnvironment this$0;
/*     */           });
/* 133 */       String pluginClasses = getPrivilegedProperty("jinput.plugins", "") + " " + getPrivilegedProperty("net.java.games.input.plugins", "");
/* 134 */       if (!getPrivilegedProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !getPrivilegedProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
/* 135 */         String osName = getPrivilegedProperty("os.name", "").trim();
/* 136 */         if (osName.equals("Linux")) {
/* 137 */           pluginClasses = pluginClasses + " net.java.games.input.LinuxEnvironmentPlugin";
/* 138 */         } else if (osName.equals("Mac OS X")) {
/* 139 */           pluginClasses = pluginClasses + " net.java.games.input.OSXEnvironmentPlugin";
/* 140 */         } else if (osName.equals("Windows XP") || osName.equals("Windows Vista") || osName.equals("Windows 7")) {
/* 141 */           pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
/* 142 */         } else if (osName.equals("Windows 98") || osName.equals("Windows 2000")) {
/* 143 */           pluginClasses = pluginClasses + " net.java.games.input.DirectInputEnvironmentPlugin";
/* 144 */         } else if (osName.startsWith("Windows")) {
/* 145 */           log.warning("Found unknown Windows version: " + osName);
/* 146 */           log.info("Attempting to use default windows plug-in.");
/* 147 */           pluginClasses = pluginClasses + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
/*     */         } else {
/* 149 */           log.info("Trying to use default plugin, OS name " + osName + " not recognised");
/*     */         } 
/*     */       } 
/*     */       
/* 153 */       StringTokenizer pluginClassTok = new StringTokenizer(pluginClasses, " \t\n\r\f,;:");
/* 154 */       while (pluginClassTok.hasMoreTokens()) {
/* 155 */         String className = pluginClassTok.nextToken();
/*     */         try {
/* 157 */           if (!this.loadedPlugins.contains(className)) {
/* 158 */             log.info("Loading: " + className);
/* 159 */             Class ceClass = Class.forName(className);
/* 160 */             ControllerEnvironment ce = (ControllerEnvironment)ceClass.newInstance();
/* 161 */             if (ce.isSupported()) {
/* 162 */               addControllers(ce.getControllers());
/* 163 */               this.loadedPlugins.add(ce.getClass().getName()); continue;
/*     */             } 
/* 165 */             logln(ceClass.getName() + " is not supported");
/*     */           }
/*     */         
/* 168 */         } catch (Throwable e) {
/* 169 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/* 173 */     Controller[] ret = new Controller[this.controllers.size()];
/* 174 */     Iterator it = this.controllers.iterator();
/* 175 */     int i = 0;
/* 176 */     while (it.hasNext()) {
/* 177 */       ret[i] = it.next();
/* 178 */       i++;
/*     */     } 
/* 180 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   private void scanControllers() {
/* 185 */     String pluginPathName = getPrivilegedProperty("jinput.controllerPluginPath");
/* 186 */     if (pluginPathName == null) {
/* 187 */       pluginPathName = "controller";
/*     */     }
/*     */     
/* 190 */     scanControllersAt(getPrivilegedProperty("java.home") + File.separator + "lib" + File.separator + pluginPathName);
/*     */     
/* 192 */     scanControllersAt(getPrivilegedProperty("user.dir") + File.separator + pluginPathName);
/*     */   }
/*     */ 
/*     */   
/*     */   private void scanControllersAt(String path) {
/* 197 */     File file = new File(path);
/* 198 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 202 */       Plugins plugins = new Plugins(file);
/* 203 */       Class[] envClasses = plugins.getExtends(ControllerEnvironment.class);
/* 204 */       for (int i = 0; i < envClasses.length; i++) {
/*     */         try {
/* 206 */           ControllerEnvironment.logln("ControllerEnvironment " + envClasses[i].getName() + " loaded by " + envClasses[i].getClassLoader());
/*     */ 
/*     */           
/* 209 */           ControllerEnvironment ce = envClasses[i].newInstance();
/*     */           
/* 211 */           if (ce.isSupported()) {
/* 212 */             addControllers(ce.getControllers());
/* 213 */             this.loadedPlugins.add(ce.getClass().getName());
/*     */           } else {
/* 215 */             logln(envClasses[i].getName() + " is not supported");
/*     */           } 
/* 217 */         } catch (Throwable e) {
/* 218 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/* 221 */     } catch (Exception e) {
/* 222 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addControllers(Controller[] c) {
/* 230 */     for (int i = 0; i < c.length; i++) {
/* 231 */       this.controllers.add(c[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSupported() {
/* 236 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\DefaultControllerEnvironment.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */