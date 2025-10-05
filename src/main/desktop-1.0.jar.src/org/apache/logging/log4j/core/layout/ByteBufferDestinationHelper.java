/*    */ package org.apache.logging.log4j.core.layout;
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
/*    */ public final class ByteBufferDestinationHelper
/*    */ {
/*    */   public static void writeToUnsynchronized(ByteBuffer source, ByteBufferDestination destination) {
/* 39 */     ByteBuffer destBuff = destination.getByteBuffer();
/* 40 */     while (source.remaining() > destBuff.remaining()) {
/* 41 */       int originalLimit = source.limit();
/* 42 */       source.limit(Math.min(source.limit(), source.position() + destBuff.remaining()));
/* 43 */       destBuff.put(source);
/* 44 */       source.limit(originalLimit);
/* 45 */       destBuff = destination.drain(destBuff);
/*    */     } 
/* 47 */     destBuff.put(source);
/*    */   }
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
/*    */   public static void writeToUnsynchronized(byte[] data, int offset, int length, ByteBufferDestination destination) {
/* 62 */     ByteBuffer buffer = destination.getByteBuffer();
/* 63 */     while (length > buffer.remaining()) {
/* 64 */       int chunk = buffer.remaining();
/* 65 */       buffer.put(data, offset, chunk);
/* 66 */       offset += chunk;
/* 67 */       length -= chunk;
/* 68 */       buffer = destination.drain(buffer);
/*    */     } 
/* 70 */     buffer.put(data, offset, length);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\ByteBufferDestinationHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */