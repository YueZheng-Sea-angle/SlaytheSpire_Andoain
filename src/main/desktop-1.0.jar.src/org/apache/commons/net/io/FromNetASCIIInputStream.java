/*     */ package org.apache.commons.net.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FromNetASCIIInputStream
/*     */   extends PushbackInputStream
/*     */ {
/*     */   static final boolean _noConversionRequired;
/*  42 */   static final String _lineSeparator = System.getProperty("line.separator"); static {
/*  43 */     _noConversionRequired = _lineSeparator.equals("\r\n");
/*     */     try {
/*  45 */       _lineSeparatorBytes = _lineSeparator.getBytes("US-ASCII");
/*  46 */     } catch (UnsupportedEncodingException e) {
/*  47 */       throw new RuntimeException("Broken JVM - cannot find US-ASCII charset!", e);
/*     */     } 
/*     */   }
/*     */   static final byte[] _lineSeparatorBytes;
/*  51 */   private int __length = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isConversionRequired() {
/*  64 */     return !_noConversionRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FromNetASCIIInputStream(InputStream input) {
/*  74 */     super(input, _lineSeparatorBytes.length + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int __read() throws IOException {
/*  82 */     int ch = super.read();
/*     */     
/*  84 */     if (ch == 13) {
/*     */       
/*  86 */       ch = super.read();
/*  87 */       if (ch == 10) {
/*     */         
/*  89 */         unread(_lineSeparatorBytes);
/*  90 */         ch = super.read();
/*     */         
/*  92 */         this.__length--;
/*     */       }
/*     */       else {
/*     */         
/*  96 */         if (ch != -1) {
/*  97 */           unread(ch);
/*     */         }
/*  99 */         return 13;
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     return ch;
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
/*     */   public int read() throws IOException {
/* 123 */     if (_noConversionRequired) {
/* 124 */       return super.read();
/*     */     }
/*     */     
/* 127 */     return __read();
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
/*     */   public int read(byte[] buffer) throws IOException {
/* 145 */     return read(buffer, 0, buffer.length);
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
/*     */   public int read(byte[] buffer, int offset, int length) throws IOException {
/* 166 */     if (_noConversionRequired) {
/* 167 */       return super.read(buffer, offset, length);
/*     */     }
/*     */     
/* 170 */     if (length < 1) {
/* 171 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 176 */     int ch = available();
/*     */     
/* 178 */     this.__length = (length > ch) ? ch : length;
/*     */ 
/*     */     
/* 181 */     if (this.__length < 1) {
/* 182 */       this.__length = 1;
/*     */     }
/*     */ 
/*     */     
/* 186 */     if ((ch = __read()) == -1) {
/* 187 */       return -1;
/*     */     }
/*     */     
/* 190 */     int off = offset;
/*     */ 
/*     */     
/*     */     do {
/* 194 */       buffer[offset++] = (byte)ch;
/*     */     }
/* 196 */     while (--this.__length > 0 && (ch = __read()) != -1);
/*     */ 
/*     */     
/* 199 */     return offset - off;
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
/*     */   public int available() throws IOException {
/* 215 */     if (this.in == null) {
/* 216 */       throw new IOException("Stream closed");
/*     */     }
/* 218 */     return this.buf.length - this.pos + this.in.available();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\io\FromNetASCIIInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */