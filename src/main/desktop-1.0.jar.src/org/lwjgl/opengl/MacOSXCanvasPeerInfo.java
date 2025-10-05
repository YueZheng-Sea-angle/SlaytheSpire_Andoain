/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.lwjgl.LWJGLException;
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
/*     */ abstract class MacOSXCanvasPeerInfo
/*     */   extends MacOSXPeerInfo
/*     */ {
/*  57 */   private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
/*     */   public ByteBuffer window_handle;
/*     */   
/*     */   protected MacOSXCanvasPeerInfo(PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
/*  61 */     super(pixel_format, attribs, true, true, support_pbuffer, true);
/*     */   }
/*     */   
/*     */   protected void initHandle(Canvas component) throws LWJGLException {
/*  65 */     boolean forceCALayer = true;
/*  66 */     boolean autoResizable = true;
/*     */     
/*  68 */     String javaVersion = System.getProperty("java.version");
/*     */     
/*  70 */     if (javaVersion.startsWith("1.5") || javaVersion.startsWith("1.6")) {
/*     */ 
/*     */ 
/*     */       
/*  74 */       forceCALayer = false;
/*     */     }
/*  76 */     else if (javaVersion.startsWith("1.7")) {
/*  77 */       autoResizable = false;
/*     */     } 
/*     */     
/*  80 */     Insets insets = getInsets(component);
/*     */     
/*  82 */     int top = (insets != null) ? insets.top : 0;
/*  83 */     int left = (insets != null) ? insets.left : 0;
/*     */     
/*  85 */     this.window_handle = nInitHandle(this.awt_surface.lockAndGetHandle(component), getHandle(), this.window_handle, forceCALayer, autoResizable, component.getX() - left, component.getY() - top);
/*     */     
/*  87 */     if (javaVersion.startsWith("1.7")) {
/*     */ 
/*     */       
/*  90 */       addComponentListener(component);
/*     */       
/*  92 */       reSetLayerBounds(component, getHandle());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addComponentListener(final Canvas component) {
/*  98 */     ComponentListener[] components = component.getComponentListeners();
/*     */ 
/*     */     
/* 101 */     for (int i = 0; i < components.length; i++) {
/* 102 */       ComponentListener c = components[i];
/* 103 */       if (c.toString() == "CanvasPeerInfoListener") {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 108 */     ComponentListener comp = new ComponentListener()
/*     */       {
/*     */         public void componentHidden(ComponentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void componentMoved(ComponentEvent e) {
/* 116 */           MacOSXCanvasPeerInfo.reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void componentResized(ComponentEvent e) {
/* 122 */           MacOSXCanvasPeerInfo.reSetLayerBounds(component, MacOSXCanvasPeerInfo.this.getHandle());
/*     */         }
/*     */ 
/*     */         
/*     */         public void componentShown(ComponentEvent e) {}
/*     */ 
/*     */         
/*     */         public String toString() {
/* 130 */           return "CanvasPeerInfoListener";
/*     */         }
/*     */       };
/*     */     
/* 134 */     component.addComponentListener(comp);
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
/*     */   private static void reSetLayerBounds(Canvas component, ByteBuffer peer_info_handle) {
/* 151 */     Component peer = SwingUtilities.getRoot(component);
/*     */     
/* 153 */     Point rtLoc = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), peer);
/* 154 */     int x = (int)rtLoc.getX(), y = (int)rtLoc.getY();
/*     */     
/* 156 */     Insets insets = getInsets(component);
/* 157 */     x -= (insets != null) ? insets.left : 0;
/* 158 */     y -= (insets != null) ? insets.top : 0;
/*     */ 
/*     */     
/* 161 */     y = peer.getHeight() - y - component.getHeight();
/*     */     
/* 163 */     nSetLayerBounds(peer_info_handle, x, y, component.getWidth(), component.getHeight());
/*     */   }
/*     */   
/*     */   protected void doUnlock() throws LWJGLException {
/* 167 */     this.awt_surface.unlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Insets getInsets(Canvas component) {
/* 174 */     Container c = SwingUtilities.getRootPane(component);
/*     */     
/* 176 */     if (c != null) {
/* 177 */       return c.getInsets();
/*     */     }
/* 179 */     return new Insets(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   private static native ByteBuffer nInitHandle(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3, boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2) throws LWJGLException;
/*     */   
/*     */   private static native void nSetLayerPosition(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
/*     */   
/*     */   private static native void nSetLayerBounds(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\MacOSXCanvasPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */