/*    */ package org.lwjgl.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ abstract class LinuxPeerInfo
/*    */   extends PeerInfo
/*    */ {
/*    */   private static native ByteBuffer createHandle();
/*    */   
/*    */   LinuxPeerInfo() {
/* 44 */     super(createHandle());
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getDisplay() {
/* 49 */     return nGetDisplay(getHandle());
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getDrawable() {
/* 54 */     return nGetDrawable(getHandle());
/*    */   }
/*    */   
/*    */   private static native long nGetDisplay(ByteBuffer paramByteBuffer);
/*    */   
/*    */   private static native long nGetDrawable(ByteBuffer paramByteBuffer);
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opengl\LinuxPeerInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */