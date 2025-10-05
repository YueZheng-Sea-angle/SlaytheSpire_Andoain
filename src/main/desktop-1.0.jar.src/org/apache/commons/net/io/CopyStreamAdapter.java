/*     */ package org.apache.commons.net.io;
/*     */ 
/*     */ import java.util.EventListener;
/*     */ import org.apache.commons.net.util.ListenerList;
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
/*     */ public class CopyStreamAdapter
/*     */   implements CopyStreamListener
/*     */ {
/*  50 */   private final ListenerList internalListeners = new ListenerList();
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
/*     */   public void bytesTransferred(CopyStreamEvent event) {
/*  66 */     for (EventListener listener : this.internalListeners)
/*     */     {
/*  68 */       ((CopyStreamListener)listener).bytesTransferred(event);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
/*  91 */     for (EventListener listener : this.internalListeners)
/*     */     {
/*  93 */       ((CopyStreamListener)listener).bytesTransferred(totalBytesTransferred, bytesTransferred, streamSize);
/*     */     }
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
/*     */   public void addCopyStreamListener(CopyStreamListener listener) {
/* 106 */     this.internalListeners.addListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCopyStreamListener(CopyStreamListener listener) {
/* 116 */     this.internalListeners.removeListener(listener);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\io\CopyStreamAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */