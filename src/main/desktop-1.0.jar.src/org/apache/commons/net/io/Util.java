/*     */ package org.apache.commons.net.io;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.net.Socket;
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
/*     */ public final class Util
/*     */ {
/*     */   public static final int DEFAULT_COPY_BUFFER_SIZE = 1024;
/*     */   
/*     */   public static final long copyStream(InputStream source, OutputStream dest, int bufferSize, long streamSize, CopyStreamListener listener, boolean flush) throws CopyStreamException {
/*  97 */     long total = 0L;
/*  98 */     byte[] buffer = new byte[(bufferSize > 0) ? bufferSize : 1024];
/*     */     
/*     */     try {
/*     */       int numBytes;
/* 102 */       while ((numBytes = source.read(buffer)) != -1)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 107 */         if (numBytes == 0) {
/*     */           
/* 109 */           int singleByte = source.read();
/* 110 */           if (singleByte < 0) {
/*     */             break;
/*     */           }
/* 113 */           dest.write(singleByte);
/* 114 */           if (flush) {
/* 115 */             dest.flush();
/*     */           }
/* 117 */           total++;
/* 118 */           if (listener != null) {
/* 119 */             listener.bytesTransferred(total, 1, streamSize);
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 124 */         dest.write(buffer, 0, numBytes);
/* 125 */         if (flush) {
/* 126 */           dest.flush();
/*     */         }
/* 128 */         total += numBytes;
/* 129 */         if (listener != null) {
/* 130 */           listener.bytesTransferred(total, numBytes, streamSize);
/*     */         }
/*     */       }
/*     */     
/* 134 */     } catch (IOException e) {
/*     */       
/* 136 */       throw new CopyStreamException("IOException caught while copying.", total, e);
/*     */     } 
/*     */ 
/*     */     
/* 140 */     return total;
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
/*     */   public static final long copyStream(InputStream source, OutputStream dest, int bufferSize, long streamSize, CopyStreamListener listener) throws CopyStreamException {
/* 183 */     return copyStream(source, dest, bufferSize, streamSize, listener, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long copyStream(InputStream source, OutputStream dest, int bufferSize) throws CopyStreamException {
/* 214 */     return copyStream(source, dest, bufferSize, -1L, null);
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
/*     */   public static final long copyStream(InputStream source, OutputStream dest) throws CopyStreamException {
/* 229 */     return copyStream(source, dest, 1024);
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
/*     */   public static final long copyReader(Reader source, Writer dest, int bufferSize, long streamSize, CopyStreamListener listener) throws CopyStreamException {
/* 272 */     long total = 0L;
/* 273 */     char[] buffer = new char[(bufferSize > 0) ? bufferSize : 1024];
/*     */     
/*     */     try {
/*     */       int numChars;
/* 277 */       while ((numChars = source.read(buffer)) != -1)
/*     */       {
/*     */ 
/*     */         
/* 281 */         if (numChars == 0) {
/*     */           
/* 283 */           int singleChar = source.read();
/* 284 */           if (singleChar < 0) {
/*     */             break;
/*     */           }
/* 287 */           dest.write(singleChar);
/* 288 */           dest.flush();
/* 289 */           total++;
/* 290 */           if (listener != null) {
/* 291 */             listener.bytesTransferred(total, 1, streamSize);
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 296 */         dest.write(buffer, 0, numChars);
/* 297 */         dest.flush();
/* 298 */         total += numChars;
/* 299 */         if (listener != null) {
/* 300 */           listener.bytesTransferred(total, numChars, streamSize);
/*     */         }
/*     */       }
/*     */     
/* 304 */     } catch (IOException e) {
/*     */       
/* 306 */       throw new CopyStreamException("IOException caught while copying.", total, e);
/*     */     } 
/*     */ 
/*     */     
/* 310 */     return total;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long copyReader(Reader source, Writer dest, int bufferSize) throws CopyStreamException {
/* 339 */     return copyReader(source, dest, bufferSize, -1L, null);
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
/*     */   public static final long copyReader(Reader source, Writer dest) throws CopyStreamException {
/* 354 */     return copyReader(source, dest, 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeQuietly(Closeable closeable) {
/* 365 */     if (closeable != null) {
/*     */       try {
/* 367 */         closeable.close();
/* 368 */       } catch (IOException e) {}
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
/*     */   public static void closeQuietly(Socket socket) {
/* 382 */     if (socket != null)
/*     */       try {
/* 384 */         socket.close();
/* 385 */       } catch (IOException e) {} 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\io\Util.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */