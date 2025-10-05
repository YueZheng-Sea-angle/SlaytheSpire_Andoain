/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.layout.ByteBufferDestination;
/*     */ import org.apache.logging.log4j.core.layout.ByteBufferDestinationHelper;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OutputStreamManager
/*     */   extends AbstractManager
/*     */   implements ByteBufferDestination
/*     */ {
/*     */   protected final Layout<?> layout;
/*     */   protected ByteBuffer byteBuffer;
/*     */   private volatile OutputStream outputStream;
/*     */   private boolean skipFooter;
/*     */   
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader) {
/*  45 */     this(os, streamName, layout, writeHeader, Constants.ENCODER_BYTE_BUFFER_SIZE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader, int bufferSize) {
/*  50 */     this(os, streamName, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected OutputStreamManager(OutputStream os, String streamName, Layout<?> layout, boolean writeHeader, ByteBuffer byteBuffer) {
/*  60 */     super(null, streamName);
/*  61 */     this.outputStream = os;
/*  62 */     this.layout = layout;
/*  63 */     if (writeHeader) {
/*  64 */       writeHeader(os);
/*     */     }
/*  66 */     this.byteBuffer = Objects.<ByteBuffer>requireNonNull(byteBuffer, "byteBuffer");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStreamManager(LoggerContext loggerContext, OutputStream os, String streamName, boolean createOnDemand, Layout<? extends Serializable> layout, boolean writeHeader, ByteBuffer byteBuffer) {
/*  75 */     super(loggerContext, streamName);
/*  76 */     if (createOnDemand && os != null) {
/*  77 */       LOGGER.error("Invalid OutputStreamManager configuration for '{}': You cannot both set the OutputStream and request on-demand.", streamName);
/*     */     }
/*     */ 
/*     */     
/*  81 */     this.layout = layout;
/*  82 */     this.byteBuffer = Objects.<ByteBuffer>requireNonNull(byteBuffer, "byteBuffer");
/*  83 */     this.outputStream = os;
/*  84 */     if (writeHeader) {
/*  85 */       writeHeader(os);
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
/*     */   public static <T> OutputStreamManager getManager(String name, T data, ManagerFactory<? extends OutputStreamManager, T> factory) {
/* 100 */     return AbstractManager.<OutputStreamManager, T>getManager(name, (ManagerFactory)factory, data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected OutputStream createOutputStream() throws IOException {
/* 105 */     throw new IllegalStateException(getClass().getCanonicalName() + " must implement createOutputStream()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipFooter(boolean skipFooter) {
/* 113 */     this.skipFooter = skipFooter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 121 */     writeFooter();
/* 122 */     return closeOutputStream();
/*     */   }
/*     */   
/*     */   protected void writeHeader(OutputStream os) {
/* 126 */     if (this.layout != null && os != null) {
/* 127 */       byte[] header = this.layout.getHeader();
/* 128 */       if (header != null) {
/*     */         try {
/* 130 */           os.write(header, 0, header.length);
/* 131 */         } catch (IOException e) {
/* 132 */           logError("Unable to write header", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeFooter() {
/* 142 */     if (this.layout == null || this.skipFooter) {
/*     */       return;
/*     */     }
/* 145 */     byte[] footer = this.layout.getFooter();
/* 146 */     if (footer != null) {
/* 147 */       write(footer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 156 */     return (getCount() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasOutputStream() {
/* 160 */     return (this.outputStream != null);
/*     */   }
/*     */   
/*     */   protected OutputStream getOutputStream() throws IOException {
/* 164 */     if (this.outputStream == null) {
/* 165 */       this.outputStream = createOutputStream();
/*     */     }
/* 167 */     return this.outputStream;
/*     */   }
/*     */   
/*     */   protected void setOutputStream(OutputStream os) {
/* 171 */     this.outputStream = os;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes) {
/* 180 */     write(bytes, 0, bytes.length, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void write(byte[] bytes, boolean immediateFlush) {
/* 190 */     write(bytes, 0, bytes.length, immediateFlush);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBytes(byte[] data, int offset, int length) {
/* 195 */     write(data, offset, length, false);
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
/*     */   protected void write(byte[] bytes, int offset, int length) {
/* 207 */     writeBytes(bytes, offset, length);
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
/*     */   protected synchronized void write(byte[] bytes, int offset, int length, boolean immediateFlush) {
/* 220 */     if (immediateFlush && this.byteBuffer.position() == 0) {
/* 221 */       writeToDestination(bytes, offset, length);
/* 222 */       flushDestination();
/*     */       return;
/*     */     } 
/* 225 */     if (length >= this.byteBuffer.capacity()) {
/*     */       
/* 227 */       flush();
/* 228 */       writeToDestination(bytes, offset, length);
/*     */     } else {
/* 230 */       if (length > this.byteBuffer.remaining()) {
/* 231 */         flush();
/*     */       }
/* 233 */       this.byteBuffer.put(bytes, offset, length);
/*     */     } 
/* 235 */     if (immediateFlush) {
/* 236 */       flush();
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
/*     */   protected synchronized void writeToDestination(byte[] bytes, int offset, int length) {
/*     */     try {
/* 250 */       getOutputStream().write(bytes, offset, length);
/* 251 */     } catch (IOException ex) {
/* 252 */       throw new AppenderLoggingException("Error writing to stream " + getName(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void flushDestination() {
/* 261 */     OutputStream stream = this.outputStream;
/* 262 */     if (stream != null) {
/*     */       try {
/* 264 */         stream.flush();
/* 265 */       } catch (IOException ex) {
/* 266 */         throw new AppenderLoggingException("Error flushing stream " + getName(), ex);
/*     */       } 
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
/*     */   protected synchronized void flushBuffer(ByteBuffer buf) {
/* 280 */     buf.flip();
/*     */     try {
/* 282 */       if (buf.remaining() > 0) {
/* 283 */         writeToDestination(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining());
/*     */       }
/*     */     } finally {
/* 286 */       buf.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void flush() {
/* 294 */     flushBuffer(this.byteBuffer);
/* 295 */     flushDestination();
/*     */   }
/*     */   
/*     */   protected synchronized boolean closeOutputStream() {
/* 299 */     flush();
/* 300 */     OutputStream stream = this.outputStream;
/* 301 */     if (stream == null || stream == System.out || stream == System.err) {
/* 302 */       return true;
/*     */     }
/*     */     try {
/* 305 */       stream.close();
/* 306 */       LOGGER.debug("OutputStream closed");
/* 307 */     } catch (IOException ex) {
/* 308 */       logError("Unable to close stream", ex);
/* 309 */       return false;
/*     */     } 
/* 311 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getByteBuffer() {
/* 321 */     return this.byteBuffer;
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
/*     */   public ByteBuffer drain(ByteBuffer buf) {
/* 343 */     flushBuffer(buf);
/* 344 */     return buf;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBytes(ByteBuffer data) {
/* 349 */     if (data.remaining() == 0) {
/*     */       return;
/*     */     }
/* 352 */     synchronized (this) {
/* 353 */       ByteBufferDestinationHelper.writeToUnsynchronized(data, this);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\OutputStreamManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */