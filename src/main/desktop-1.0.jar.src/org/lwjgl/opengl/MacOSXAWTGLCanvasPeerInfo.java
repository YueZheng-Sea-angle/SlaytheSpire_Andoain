/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.awt.Canvas;
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
/*    */ final class MacOSXAWTGLCanvasPeerInfo
/*    */   extends MacOSXCanvasPeerInfo
/*    */ {
/*    */   private final Canvas component;
/*    */   
/*    */   MacOSXAWTGLCanvasPeerInfo(Canvas component, PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
/* 48 */     super(pixel_format, attribs, support_pbuffer);
/* 49 */     this.component = component;
/*    */   }
/*    */   
/*    */   protected void doLockAndInitHandle() throws LWJGLException {
/* 53 */     initHandle(this.component);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\MacOSXAWTGLCanvasPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */