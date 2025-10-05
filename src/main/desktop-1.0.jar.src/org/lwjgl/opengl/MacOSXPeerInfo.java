/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.lwjgl.LWJGLException;
/*    */ import org.lwjgl.LWJGLUtil;
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
/*    */ abstract class MacOSXPeerInfo
/*    */   extends PeerInfo
/*    */ {
/*    */   private static native ByteBuffer createHandle();
/*    */   
/*    */   MacOSXPeerInfo(PixelFormat pixel_format, ContextAttribs attribs, boolean use_display_bpp, boolean support_window, boolean support_pbuffer, boolean double_buffered) throws LWJGLException {
/* 47 */     super(createHandle());
/*    */     
/* 49 */     boolean gl32 = (attribs != null && (3 < attribs.getMajorVersion() || (attribs.getMajorVersion() == 3 && 2 <= attribs.getMinorVersion())) && attribs.isProfileCore());
/* 50 */     if (gl32 && !LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 7)) {
/* 51 */       throw new LWJGLException("OpenGL 3.2+ requested, but it requires MacOS X 10.7 or newer");
/*    */     }
/* 53 */     choosePixelFormat(pixel_format, gl32, use_display_bpp, support_window, support_pbuffer, double_buffered);
/*    */   }
/*    */ 
/*    */   
/*    */   private void choosePixelFormat(PixelFormat pixel_format, boolean gl32, boolean use_display_bpp, boolean support_window, boolean support_pbuffer, boolean double_buffered) throws LWJGLException {
/* 58 */     nChoosePixelFormat(getHandle(), pixel_format, gl32, use_display_bpp, support_window, support_pbuffer, double_buffered);
/*    */   }
/*    */ 
/*    */   
/*    */   public void destroy() {
/* 63 */     nDestroy(getHandle());
/*    */   }
/*    */   
/*    */   private static native void nChoosePixelFormat(ByteBuffer paramByteBuffer, PixelFormat paramPixelFormat, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5) throws LWJGLException;
/*    */   
/*    */   private static native void nDestroy(ByteBuffer paramByteBuffer);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\MacOSXPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */