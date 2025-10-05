/*    */ package org.lwjgl;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LinuxSysImplementation
/*    */   extends J2SESysImplementation
/*    */ {
/*    */   private static final int JNI_VERSION = 19;
/*    */   
/*    */   static {
/* 50 */     Toolkit.getDefaultToolkit();
/*    */ 
/*    */     
/* 53 */     AccessController.doPrivileged(new PrivilegedAction() {
/*    */           public Object run() {
/*    */             try {
/* 56 */               System.loadLibrary("jawt");
/* 57 */             } catch (UnsatisfiedLinkError e) {}
/*    */ 
/*    */ 
/*    */             
/* 61 */             return null;
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public int getRequiredJNIVersion() {
/* 67 */     return 19;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean openURL(String url) {
/* 74 */     String[] browsers = { "sensible-browser", "xdg-open", "google-chrome", "chromium", "firefox", "iceweasel", "mozilla", "opera", "konqueror", "nautilus", "galeon", "netscape" };
/*    */     
/* 76 */     for (String browser : browsers) {
/*    */       try {
/* 78 */         LWJGLUtil.execPrivileged(new String[] { browser, url });
/* 79 */         return true;
/* 80 */       } catch (Exception e) {
/*    */         
/* 82 */         e.printStackTrace(System.err);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 87 */     return false;
/*    */   }
/*    */   
/*    */   public boolean has64Bit() {
/* 91 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\LinuxSysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */