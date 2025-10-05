/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.lwjgl.LWJGLException;
/*    */ import org.lwjgl.opengles.GLContext;
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
/*    */ final class WindowsDisplayPeerInfo
/*    */   extends WindowsPeerInfo
/*    */ {
/*    */   final boolean egl;
/*    */   
/*    */   WindowsDisplayPeerInfo(boolean egl) throws LWJGLException {
/* 49 */     this.egl = egl;
/*    */     
/* 51 */     if (egl) {
/* 52 */       GLContext.loadOpenGLLibrary();
/*    */     } else {
/* 54 */       GLContext.loadOpenGLLibrary();
/*    */     } 
/*    */   }
/*    */   void initDC(long hwnd, long hdc) throws LWJGLException {
/* 58 */     nInitDC(getHandle(), hwnd, hdc);
/*    */   }
/*    */ 
/*    */   
/*    */   private static native void nInitDC(ByteBuffer paramByteBuffer, long paramLong1, long paramLong2);
/*    */ 
/*    */   
/*    */   protected void doLockAndInitHandle() throws LWJGLException {}
/*    */ 
/*    */   
/*    */   protected void doUnlock() throws LWJGLException {}
/*    */   
/*    */   public void destroy() {
/* 71 */     super.destroy();
/*    */     
/* 73 */     if (this.egl) {
/* 74 */       GLContext.unloadOpenGLLibrary();
/*    */     } else {
/* 76 */       GLContext.unloadOpenGLLibrary();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\WindowsDisplayPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */