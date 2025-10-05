/*     */ package org.lwjgl.opengl;
/*     */ 
/*     */ import java.awt.Canvas;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.lwjgl.LWJGLException;
/*     */ import org.lwjgl.LWJGLUtil;
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
/*     */ final class AWTSurfaceLock
/*     */ {
/*     */   private static final int WAIT_DELAY_MILLIS = 100;
/*  60 */   private final ByteBuffer lock_buffer = createHandle();
/*     */   
/*     */   private boolean firstLockSucceeded;
/*     */ 
/*     */   
/*     */   public ByteBuffer lockAndGetHandle(Canvas component) throws LWJGLException {
/*  66 */     while (!privilegedLockAndInitHandle(component)) {
/*  67 */       LWJGLUtil.log("Could not get drawing surface info, retrying...");
/*     */       try {
/*  69 */         Thread.sleep(100L);
/*  70 */       } catch (InterruptedException e) {
/*  71 */         LWJGLUtil.log("Interrupted while retrying: " + e);
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     return this.lock_buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean privilegedLockAndInitHandle(final Canvas component) throws LWJGLException {
/*  85 */     if (this.firstLockSucceeded) {
/*  86 */       return lockAndInitHandle(this.lock_buffer, component);
/*     */     }
/*     */     try {
/*  89 */       this.firstLockSucceeded = ((Boolean)AccessController.<Boolean>doPrivileged(new PrivilegedExceptionAction<Boolean>() {
/*     */             public Boolean run() throws LWJGLException {
/*  91 */               return Boolean.valueOf(AWTSurfaceLock.lockAndInitHandle(AWTSurfaceLock.this.lock_buffer, component));
/*     */             }
/*     */           })).booleanValue();
/*  94 */       return this.firstLockSucceeded;
/*  95 */     } catch (PrivilegedActionException e) {
/*  96 */       throw (LWJGLException)e.getException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void unlock() throws LWJGLException {
/* 103 */     nUnlock(this.lock_buffer);
/*     */   }
/*     */   
/*     */   private static native ByteBuffer createHandle();
/*     */   
/*     */   private static native boolean lockAndInitHandle(ByteBuffer paramByteBuffer, Canvas paramCanvas) throws LWJGLException;
/*     */   
/*     */   private static native void nUnlock(ByteBuffer paramByteBuffer) throws LWJGLException;
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\AWTSurfaceLock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */