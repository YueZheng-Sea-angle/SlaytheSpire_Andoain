/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextEncoderHelper
/*     */ {
/*     */   static void encodeTextFallBack(Charset charset, StringBuilder text, ByteBufferDestination destination) {
/*  38 */     byte[] bytes = text.toString().getBytes(charset);
/*  39 */     destination.writeBytes(bytes, 0, bytes.length);
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
/*     */   static void encodeText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, StringBuilder text, ByteBufferDestination destination) throws CharacterCodingException {
/*  56 */     charsetEncoder.reset();
/*  57 */     if (text.length() > charBuf.capacity()) {
/*  58 */       encodeChunkedText(charsetEncoder, charBuf, byteBuf, text, destination);
/*     */       return;
/*     */     } 
/*  61 */     charBuf.clear();
/*  62 */     text.getChars(0, text.length(), charBuf.array(), charBuf.arrayOffset());
/*  63 */     charBuf.limit(text.length());
/*  64 */     CoderResult result = charsetEncoder.encode(charBuf, byteBuf, true);
/*  65 */     writeEncodedText(charsetEncoder, charBuf, byteBuf, destination, result);
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
/*     */   private static void writeEncodedText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, ByteBufferDestination destination, CoderResult result) {
/*  78 */     if (!result.isUnderflow()) {
/*  79 */       writeChunkedEncodedText(charsetEncoder, charBuf, destination, byteBuf, result);
/*     */       return;
/*     */     } 
/*  82 */     result = charsetEncoder.flush(byteBuf);
/*  83 */     if (!result.isUnderflow()) {
/*  84 */       synchronized (destination) {
/*  85 */         flushRemainingBytes(charsetEncoder, destination, byteBuf);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (byteBuf != destination.getByteBuffer()) {
/*  95 */       byteBuf.flip();
/*  96 */       destination.writeBytes(byteBuf);
/*  97 */       byteBuf.clear();
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
/*     */   private static void writeChunkedEncodedText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBufferDestination destination, ByteBuffer byteBuf, CoderResult result) {
/* 111 */     synchronized (destination) {
/* 112 */       byteBuf = writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, true, destination, byteBuf, result);
/*     */       
/* 114 */       flushRemainingBytes(charsetEncoder, destination, byteBuf);
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
/*     */   private static void encodeChunkedText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, StringBuilder text, ByteBufferDestination destination) {
/* 131 */     int start = 0;
/* 132 */     CoderResult result = CoderResult.UNDERFLOW;
/* 133 */     boolean endOfInput = false;
/* 134 */     while (!endOfInput && result.isUnderflow()) {
/* 135 */       charBuf.clear();
/* 136 */       int copied = copy(text, start, charBuf);
/* 137 */       start += copied;
/* 138 */       endOfInput = (start >= text.length());
/* 139 */       charBuf.flip();
/* 140 */       result = charsetEncoder.encode(charBuf, byteBuf, endOfInput);
/*     */     } 
/* 142 */     if (endOfInput) {
/* 143 */       writeEncodedText(charsetEncoder, charBuf, byteBuf, destination, result);
/*     */       return;
/*     */     } 
/* 146 */     synchronized (destination) {
/* 147 */       byteBuf = writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf, result);
/*     */       
/* 149 */       while (!endOfInput) {
/* 150 */         result = CoderResult.UNDERFLOW;
/* 151 */         while (!endOfInput && result.isUnderflow()) {
/* 152 */           charBuf.clear();
/* 153 */           int copied = copy(text, start, charBuf);
/* 154 */           start += copied;
/* 155 */           endOfInput = (start >= text.length());
/* 156 */           charBuf.flip();
/* 157 */           result = charsetEncoder.encode(charBuf, byteBuf, endOfInput);
/*     */         } 
/* 159 */         byteBuf = writeAndEncodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf, result);
/*     */       } 
/*     */       
/* 162 */       flushRemainingBytes(charsetEncoder, destination, byteBuf);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void encodeText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBufferDestination destination) {
/* 172 */     charsetEncoder.reset();
/* 173 */     synchronized (destination) {
/* 174 */       ByteBuffer byteBuf = destination.getByteBuffer();
/* 175 */       byteBuf = encodeAsMuchAsPossible(charsetEncoder, charBuf, true, destination, byteBuf);
/* 176 */       flushRemainingBytes(charsetEncoder, destination, byteBuf);
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
/*     */   private static ByteBuffer writeAndEncodeAsMuchAsPossible(CharsetEncoder charsetEncoder, CharBuffer charBuf, boolean endOfInput, ByteBufferDestination destination, ByteBuffer temp, CoderResult result) {
/*     */     while (true) {
/* 199 */       temp = drainIfByteBufferFull(destination, temp, result);
/* 200 */       if (!result.isOverflow()) {
/*     */         break;
/*     */       }
/* 203 */       result = charsetEncoder.encode(charBuf, temp, endOfInput);
/*     */     } 
/* 205 */     if (!result.isUnderflow()) {
/* 206 */       throwException(result);
/*     */     }
/* 208 */     return temp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void throwException(CoderResult result) {
/*     */     try {
/* 214 */       result.throwException();
/* 215 */     } catch (CharacterCodingException e) {
/* 216 */       throw new IllegalStateException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer encodeAsMuchAsPossible(CharsetEncoder charsetEncoder, CharBuffer charBuf, boolean endOfInput, ByteBufferDestination destination, ByteBuffer temp) {
/*     */     while (true) {
/* 224 */       CoderResult result = charsetEncoder.encode(charBuf, temp, endOfInput);
/* 225 */       temp = drainIfByteBufferFull(destination, temp, result);
/* 226 */       if (!result.isOverflow()) {
/* 227 */         if (!result.isUnderflow()) {
/* 228 */           throwException(result);
/*     */         }
/* 230 */         return temp;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer drainIfByteBufferFull(ByteBufferDestination destination, ByteBuffer temp, CoderResult result) {
/* 249 */     if (result.isOverflow())
/*     */     {
/*     */       
/* 252 */       synchronized (destination) {
/* 253 */         ByteBuffer destinationBuffer = destination.getByteBuffer();
/* 254 */         if (destinationBuffer != temp) {
/* 255 */           temp.flip();
/* 256 */           ByteBufferDestinationHelper.writeToUnsynchronized(temp, destination);
/* 257 */           temp.clear();
/* 258 */           return destination.getByteBuffer();
/*     */         } 
/* 260 */         return destination.drain(destinationBuffer);
/*     */       } 
/*     */     }
/*     */     
/* 264 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void flushRemainingBytes(CharsetEncoder charsetEncoder, ByteBufferDestination destination, ByteBuffer temp) {
/*     */     while (true) {
/* 273 */       CoderResult result = charsetEncoder.flush(temp);
/* 274 */       temp = drainIfByteBufferFull(destination, temp, result);
/* 275 */       if (!result.isOverflow()) {
/* 276 */         if (!result.isUnderflow()) {
/* 277 */           throwException(result);
/*     */         }
/* 279 */         if (temp.remaining() > 0 && temp != destination.getByteBuffer()) {
/* 280 */           temp.flip();
/* 281 */           ByteBufferDestinationHelper.writeToUnsynchronized(temp, destination);
/* 282 */           temp.clear();
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int copy(StringBuilder source, int offset, CharBuffer destination) {
/* 294 */     int length = Math.min(source.length() - offset, destination.remaining());
/* 295 */     char[] array = destination.array();
/* 296 */     int start = destination.position();
/* 297 */     source.getChars(offset, offset + length, array, destination.arrayOffset() + start);
/* 298 */     destination.position(start + length);
/* 299 */     return length;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\TextEncoderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */