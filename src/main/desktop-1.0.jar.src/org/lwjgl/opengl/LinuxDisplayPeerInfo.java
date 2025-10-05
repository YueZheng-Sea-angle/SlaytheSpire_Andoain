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
/*    */ final class LinuxDisplayPeerInfo
/*    */   extends LinuxPeerInfo
/*    */ {
/*    */   final boolean egl;
/*    */   
/*    */   LinuxDisplayPeerInfo() throws LWJGLException {
/* 49 */     this.egl = true;
/* 50 */     GLContext.loadOpenGLLibrary();
/*    */   }
/*    */   
/*    */   LinuxDisplayPeerInfo(PixelFormat pixel_format) throws LWJGLException {
/* 54 */     this.egl = false;
/* 55 */     LinuxDisplay.lockAWT();
/*    */     try {
/* 57 */       GLContext.loadOpenGLLibrary();
/*    */       try {
/* 59 */         LinuxDisplay.incDisplay();
/*    */         try {
/* 61 */           initDefaultPeerInfo(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), getHandle(), pixel_format);
/* 62 */         } catch (LWJGLException e) {
/* 63 */           LinuxDisplay.decDisplay();
/* 64 */           throw e;
/*    */         } 
/* 66 */       } catch (LWJGLException e) {
/* 67 */         GLContext.unloadOpenGLLibrary();
/* 68 */         throw e;
/*    */       } 
/*    */     } finally {
/* 71 */       LinuxDisplay.unlockAWT();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doLockAndInitHandle() throws LWJGLException {
/* 77 */     LinuxDisplay.lockAWT();
/*    */     try {
/* 79 */       initDrawable(LinuxDisplay.getWindow(), getHandle());
/*    */     } finally {
/* 81 */       LinuxDisplay.unlockAWT();
/*    */     } 
/*    */   }
/*    */   private static native void initDefaultPeerInfo(long paramLong, int paramInt, ByteBuffer paramByteBuffer, PixelFormat paramPixelFormat) throws LWJGLException;
/*    */   
/*    */   private static native void initDrawable(long paramLong, ByteBuffer paramByteBuffer);
/*    */   
/*    */   protected void doUnlock() throws LWJGLException {}
/*    */   
/*    */   public void destroy() {
/* 91 */     super.destroy();
/*    */     
/* 93 */     if (this.egl) {
/* 94 */       GLContext.unloadOpenGLLibrary();
/*    */     } else {
/* 96 */       LinuxDisplay.lockAWT();
/* 97 */       LinuxDisplay.decDisplay();
/* 98 */       GLContext.unloadOpenGLLibrary();
/* 99 */       LinuxDisplay.unlockAWT();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\LinuxDisplayPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */