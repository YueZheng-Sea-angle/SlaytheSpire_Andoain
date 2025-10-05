/*      */ package org.apache.commons.net.util;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Base64
/*      */ {
/*      */   private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
/*      */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*      */   static final int CHUNK_SIZE = 76;
/*   71 */   private static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*      */   
/*   73 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   82 */   private static final byte[] STANDARD_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   95 */   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte PAD = 61;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  119 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MASK_6BITS = 63;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MASK_8BITS = 255;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte[] encodeTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int lineLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final byte[] lineSeparator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int decodeSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int encodeSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] buffer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int pos;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readPos;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int currentLinePos;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int modulus;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean eof;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int x;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Base64() {
/*  219 */     this(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Base64(boolean urlSafe) {
/*  238 */     this(76, CHUNK_SEPARATOR, urlSafe);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Base64(int lineLength) {
/*  260 */     this(lineLength, CHUNK_SEPARATOR);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Base64(int lineLength, byte[] lineSeparator) {
/*  286 */     this(lineLength, lineSeparator, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
/*  315 */     if (lineSeparator == null) {
/*  316 */       lineLength = 0;
/*  317 */       lineSeparator = EMPTY_BYTE_ARRAY;
/*      */     } 
/*  319 */     this.lineLength = (lineLength > 0) ? (lineLength / 4 * 4) : 0;
/*  320 */     this.lineSeparator = new byte[lineSeparator.length];
/*  321 */     System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*  322 */     if (lineLength > 0) {
/*  323 */       this.encodeSize = 4 + lineSeparator.length;
/*      */     } else {
/*  325 */       this.encodeSize = 4;
/*      */     } 
/*  327 */     this.decodeSize = this.encodeSize - 1;
/*  328 */     if (containsBase64Byte(lineSeparator)) {
/*  329 */       String sep = newStringUtf8(lineSeparator);
/*  330 */       throw new IllegalArgumentException("lineSeperator must not contain base64 characters: [" + sep + "]");
/*      */     } 
/*  332 */     this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUrlSafe() {
/*  342 */     return (this.encodeTable == URL_SAFE_ENCODE_TABLE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean hasData() {
/*  351 */     return (this.buffer != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int avail() {
/*  360 */     return (this.buffer != null) ? (this.pos - this.readPos) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private void resizeBuffer() {
/*  365 */     if (this.buffer == null) {
/*  366 */       this.buffer = new byte[8192];
/*  367 */       this.pos = 0;
/*  368 */       this.readPos = 0;
/*      */     } else {
/*  370 */       byte[] b = new byte[this.buffer.length * 2];
/*  371 */       System.arraycopy(this.buffer, 0, b, 0, this.buffer.length);
/*  372 */       this.buffer = b;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int readResults(byte[] b, int bPos, int bAvail) {
/*  389 */     if (this.buffer != null) {
/*  390 */       int len = Math.min(avail(), bAvail);
/*  391 */       if (this.buffer != b) {
/*  392 */         System.arraycopy(this.buffer, this.readPos, b, bPos, len);
/*  393 */         this.readPos += len;
/*  394 */         if (this.readPos >= this.pos) {
/*  395 */           this.buffer = null;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  400 */         this.buffer = null;
/*      */       } 
/*  402 */       return len;
/*      */     } 
/*  404 */     return this.eof ? -1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setInitialBuffer(byte[] out, int outPos, int outAvail) {
/*  421 */     if (out != null && out.length == outAvail) {
/*  422 */       this.buffer = out;
/*  423 */       this.pos = outPos;
/*  424 */       this.readPos = outPos;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void encode(byte[] in, int inPos, int inAvail) {
/*  447 */     if (this.eof) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  452 */     if (inAvail < 0) {
/*  453 */       this.eof = true;
/*  454 */       if (this.buffer == null || this.buffer.length - this.pos < this.encodeSize) {
/*  455 */         resizeBuffer();
/*      */       }
/*  457 */       switch (this.modulus) {
/*      */         case 1:
/*  459 */           this.buffer[this.pos++] = this.encodeTable[this.x >> 2 & 0x3F];
/*  460 */           this.buffer[this.pos++] = this.encodeTable[this.x << 4 & 0x3F];
/*      */           
/*  462 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/*  463 */             this.buffer[this.pos++] = 61;
/*  464 */             this.buffer[this.pos++] = 61;
/*      */           } 
/*      */           break;
/*      */         
/*      */         case 2:
/*  469 */           this.buffer[this.pos++] = this.encodeTable[this.x >> 10 & 0x3F];
/*  470 */           this.buffer[this.pos++] = this.encodeTable[this.x >> 4 & 0x3F];
/*  471 */           this.buffer[this.pos++] = this.encodeTable[this.x << 2 & 0x3F];
/*      */           
/*  473 */           if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/*  474 */             this.buffer[this.pos++] = 61;
/*      */           }
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/*  480 */       if (this.lineLength > 0 && this.pos > 0) {
/*  481 */         System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
/*  482 */         this.pos += this.lineSeparator.length;
/*      */       } 
/*      */     } else {
/*  485 */       for (int i = 0; i < inAvail; i++) {
/*  486 */         if (this.buffer == null || this.buffer.length - this.pos < this.encodeSize) {
/*  487 */           resizeBuffer();
/*      */         }
/*  489 */         this.modulus = ++this.modulus % 3;
/*  490 */         int b = in[inPos++];
/*  491 */         if (b < 0) {
/*  492 */           b += 256;
/*      */         }
/*  494 */         this.x = (this.x << 8) + b;
/*  495 */         if (0 == this.modulus) {
/*  496 */           this.buffer[this.pos++] = this.encodeTable[this.x >> 18 & 0x3F];
/*  497 */           this.buffer[this.pos++] = this.encodeTable[this.x >> 12 & 0x3F];
/*  498 */           this.buffer[this.pos++] = this.encodeTable[this.x >> 6 & 0x3F];
/*  499 */           this.buffer[this.pos++] = this.encodeTable[this.x & 0x3F];
/*  500 */           this.currentLinePos += 4;
/*  501 */           if (this.lineLength > 0 && this.lineLength <= this.currentLinePos) {
/*  502 */             System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
/*  503 */             this.pos += this.lineSeparator.length;
/*  504 */             this.currentLinePos = 0;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void decode(byte[] in, int inPos, int inAvail) {
/*  535 */     if (this.eof) {
/*      */       return;
/*      */     }
/*  538 */     if (inAvail < 0) {
/*  539 */       this.eof = true;
/*      */     }
/*  541 */     for (int i = 0; i < inAvail; i++) {
/*  542 */       if (this.buffer == null || this.buffer.length - this.pos < this.decodeSize) {
/*  543 */         resizeBuffer();
/*      */       }
/*  545 */       byte b = in[inPos++];
/*  546 */       if (b == 61) {
/*      */         
/*  548 */         this.eof = true;
/*      */         break;
/*      */       } 
/*  551 */       if (b >= 0 && b < DECODE_TABLE.length) {
/*  552 */         int result = DECODE_TABLE[b];
/*  553 */         if (result >= 0) {
/*  554 */           this.modulus = ++this.modulus % 4;
/*  555 */           this.x = (this.x << 6) + result;
/*  556 */           if (this.modulus == 0) {
/*  557 */             this.buffer[this.pos++] = (byte)(this.x >> 16 & 0xFF);
/*  558 */             this.buffer[this.pos++] = (byte)(this.x >> 8 & 0xFF);
/*  559 */             this.buffer[this.pos++] = (byte)(this.x & 0xFF);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  569 */     if (this.eof && this.modulus != 0) {
/*  570 */       this.x <<= 6;
/*  571 */       switch (this.modulus) {
/*      */         case 2:
/*  573 */           this.x <<= 6;
/*  574 */           this.buffer[this.pos++] = (byte)(this.x >> 16 & 0xFF);
/*      */           break;
/*      */         case 3:
/*  577 */           this.buffer[this.pos++] = (byte)(this.x >> 16 & 0xFF);
/*  578 */           this.buffer[this.pos++] = (byte)(this.x >> 8 & 0xFF);
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isBase64(byte octet) {
/*  595 */     return (octet == 61 || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isArrayByteBase64(byte[] arrayOctet) {
/*  608 */     for (int i = 0; i < arrayOctet.length; i++) {
/*  609 */       if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
/*  610 */         return false;
/*      */       }
/*      */     } 
/*  613 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean containsBase64Byte(byte[] arrayOctet) {
/*  624 */     for (byte element : arrayOctet) {
/*      */       
/*  626 */       if (isBase64(element)) {
/*  627 */         return true;
/*      */       }
/*      */     } 
/*  630 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBase64(byte[] binaryData) {
/*  641 */     return encodeBase64(binaryData, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBase64String(byte[] binaryData) {
/*  655 */     return newStringUtf8(encodeBase64(binaryData, true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBase64StringUnChunked(byte[] binaryData) {
/*  669 */     return newStringUtf8(encodeBase64(binaryData, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBase64String(byte[] binaryData, boolean useChunking) {
/*  682 */     return newStringUtf8(encodeBase64(binaryData, useChunking));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBase64URLSafe(byte[] binaryData) {
/*  695 */     return encodeBase64(binaryData, false, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String encodeBase64URLSafeString(byte[] binaryData) {
/*  708 */     return newStringUtf8(encodeBase64(binaryData, false, true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBase64Chunked(byte[] binaryData) {
/*  719 */     return encodeBase64(binaryData, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] decode(String pArray) {
/*  731 */     return decode(getBytesUtf8(pArray));
/*      */   }
/*      */   
/*      */   private byte[] getBytesUtf8(String pArray) {
/*      */     try {
/*  736 */       return pArray.getBytes("UTF8");
/*  737 */     } catch (UnsupportedEncodingException e) {
/*  738 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] decode(byte[] pArray) {
/*  750 */     reset();
/*  751 */     if (pArray == null || pArray.length == 0) {
/*  752 */       return pArray;
/*      */     }
/*  754 */     long len = (pArray.length * 3 / 4);
/*  755 */     byte[] buf = new byte[(int)len];
/*  756 */     setInitialBuffer(buf, 0, buf.length);
/*  757 */     decode(pArray, 0, pArray.length);
/*  758 */     decode(pArray, 0, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  766 */     byte[] result = new byte[this.pos];
/*  767 */     readResults(result, 0, result.length);
/*  768 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
/*  783 */     return encodeBase64(binaryData, isChunked, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
/*  801 */     return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
/*  821 */     if (binaryData == null || binaryData.length == 0) {
/*  822 */       return binaryData;
/*      */     }
/*      */     
/*  825 */     long len = getEncodeLength(binaryData, isChunked ? 76 : 0, isChunked ? CHUNK_SEPARATOR : EMPTY_BYTE_ARRAY);
/*  826 */     if (len > maxResultSize) {
/*  827 */       throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + len + ") than the specified maxium size of " + maxResultSize);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  833 */     Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
/*  834 */     return b64.encode(binaryData);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decodeBase64(String base64String) {
/*  846 */     return (new Base64()).decode(base64String);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] decodeBase64(byte[] base64Data) {
/*  857 */     return (new Base64()).decode(base64Data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isWhiteSpace(byte byteToCheck) {
/*  870 */     switch (byteToCheck) {
/*      */       case 9:
/*      */       case 10:
/*      */       case 13:
/*      */       case 32:
/*  875 */         return true;
/*      */     } 
/*  877 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String encodeToString(byte[] pArray) {
/*  890 */     return newStringUtf8(encode(pArray));
/*      */   }
/*      */   
/*      */   private static String newStringUtf8(byte[] encode) {
/*  894 */     String str = null;
/*      */     try {
/*  896 */       str = new String(encode, "UTF8");
/*  897 */     } catch (UnsupportedEncodingException ue) {
/*  898 */       throw new RuntimeException(ue);
/*      */     } 
/*  900 */     return str;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] encode(byte[] pArray) {
/*  911 */     reset();
/*  912 */     if (pArray == null || pArray.length == 0) {
/*  913 */       return pArray;
/*      */     }
/*  915 */     long len = getEncodeLength(pArray, this.lineLength, this.lineSeparator);
/*  916 */     byte[] buf = new byte[(int)len];
/*  917 */     setInitialBuffer(buf, 0, buf.length);
/*  918 */     encode(pArray, 0, pArray.length);
/*  919 */     encode(pArray, 0, -1);
/*      */     
/*  921 */     if (this.buffer != buf) {
/*  922 */       readResults(buf, 0, buf.length);
/*      */     }
/*      */ 
/*      */     
/*  926 */     if (isUrlSafe() && this.pos < buf.length) {
/*  927 */       byte[] smallerBuf = new byte[this.pos];
/*  928 */       System.arraycopy(buf, 0, smallerBuf, 0, this.pos);
/*  929 */       buf = smallerBuf;
/*      */     } 
/*  931 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static long getEncodeLength(byte[] pArray, int chunkSize, byte[] chunkSeparator) {
/*  947 */     chunkSize = chunkSize / 4 * 4;
/*      */     
/*  949 */     long len = (pArray.length * 4 / 3);
/*  950 */     long mod = len % 4L;
/*  951 */     if (mod != 0L) {
/*  952 */       len += 4L - mod;
/*      */     }
/*  954 */     if (chunkSize > 0) {
/*  955 */       boolean lenChunksPerfectly = (len % chunkSize == 0L);
/*  956 */       len += len / chunkSize * chunkSeparator.length;
/*  957 */       if (!lenChunksPerfectly) {
/*  958 */         len += chunkSeparator.length;
/*      */       }
/*      */     } 
/*  961 */     return len;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigInteger decodeInteger(byte[] pArray) {
/*  974 */     return new BigInteger(1, decodeBase64(pArray));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] encodeInteger(BigInteger bigInt) {
/*  988 */     if (bigInt == null) {
/*  989 */       throw new NullPointerException("encodeInteger called with null parameter");
/*      */     }
/*  991 */     return encodeBase64(toIntegerBytes(bigInt), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] toIntegerBytes(BigInteger bigInt) {
/* 1002 */     int bitlen = bigInt.bitLength();
/*      */     
/* 1004 */     bitlen = bitlen + 7 >> 3 << 3;
/* 1005 */     byte[] bigBytes = bigInt.toByteArray();
/*      */     
/* 1007 */     if (bigInt.bitLength() % 8 != 0 && bigInt.bitLength() / 8 + 1 == bitlen / 8) {
/* 1008 */       return bigBytes;
/*      */     }
/*      */     
/* 1011 */     int startSrc = 0;
/* 1012 */     int len = bigBytes.length;
/*      */ 
/*      */     
/* 1015 */     if (bigInt.bitLength() % 8 == 0) {
/* 1016 */       startSrc = 1;
/* 1017 */       len--;
/*      */     } 
/* 1019 */     int startDst = bitlen / 8 - len;
/* 1020 */     byte[] resizedBytes = new byte[bitlen / 8];
/* 1021 */     System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
/* 1022 */     return resizedBytes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reset() {
/* 1029 */     this.buffer = null;
/* 1030 */     this.pos = 0;
/* 1031 */     this.readPos = 0;
/* 1032 */     this.currentLinePos = 0;
/* 1033 */     this.modulus = 0;
/* 1034 */     this.eof = false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   int getLineLength() {
/* 1040 */     return this.lineLength;
/*      */   }
/*      */   
/*      */   byte[] getLineSeparator() {
/* 1044 */     return (byte[])this.lineSeparator.clone();
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\ne\\util\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */