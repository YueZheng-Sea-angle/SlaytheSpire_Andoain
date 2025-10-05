/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.lwjgl.LWJGLException;
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
/*    */ final class MacOSXPbufferPeerInfo
/*    */   extends MacOSXPeerInfo
/*    */ {
/*    */   MacOSXPbufferPeerInfo(int width, int height, PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
/* 46 */     super(pixel_format, attribs, false, false, true, false);
/* 47 */     nCreate(getHandle(), width, height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void destroy() {
/* 52 */     nDestroy(getHandle());
/*    */   }
/*    */   
/*    */   private static native void nCreate(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2) throws LWJGLException;
/*    */   
/*    */   private static native void nDestroy(ByteBuffer paramByteBuffer);
/*    */   
/*    */   protected void doLockAndInitHandle() throws LWJGLException {}
/*    */   
/*    */   protected void doUnlock() throws LWJGLException {}
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\MacOSXPbufferPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */