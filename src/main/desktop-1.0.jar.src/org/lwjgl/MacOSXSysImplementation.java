/*    */ package org.lwjgl;
/*    */ 
/*    */ import com.apple.eio.FileManager;
/*    */ import java.awt.Toolkit;
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
/*    */ 
/*    */ 
/*    */ final class MacOSXSysImplementation
/*    */   extends J2SESysImplementation
/*    */ {
/*    */   private static final int JNI_VERSION = 25;
/*    */   
/*    */   static {
/* 51 */     Toolkit.getDefaultToolkit();
/*    */   }
/*    */   
/*    */   public int getRequiredJNIVersion() {
/* 55 */     return 25;
/*    */   }
/*    */   
/*    */   public boolean openURL(String url) {
/*    */     try {
/* 60 */       FileManager.openURL(url);
/* 61 */       return true;
/* 62 */     } catch (Exception e) {
/* 63 */       LWJGLUtil.log("Exception occurred while trying to invoke browser: " + e);
/* 64 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\MacOSXSysImplementation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */