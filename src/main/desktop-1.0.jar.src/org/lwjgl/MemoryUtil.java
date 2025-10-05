/*     */ package org.lwjgl;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MemoryUtil
/*     */ {
/*  52 */   private static final Charset ascii = Charset.forName("ISO-8859-1");
/*  53 */   private static final Charset utf8 = Charset.forName("UTF-8");
/*  54 */   private static final Charset utf16 = Charset.forName("UTF-16LE");
/*     */   private static final Accessor memUtil;
/*     */   
/*     */   static {
/*     */     Accessor accessor;
/*     */   }
/*     */   
/*     */   static {
/*     */     try {
/*  63 */       accessor = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorUnsafe");
/*  64 */     } catch (Exception e0) {
/*     */       
/*     */       try {
/*  67 */         accessor = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorReflectFast");
/*  68 */       } catch (Exception e1) {
/*     */         
/*     */         try {
/*  71 */           accessor = new AccessorReflect();
/*  72 */         } catch (Exception e2) {
/*  73 */           LWJGLUtil.log("Unsupported JVM detected, this will likely result in low performance. Please inform LWJGL developers.");
/*  74 */           accessor = new AccessorJNI();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  79 */     LWJGLUtil.log("MemoryUtil Accessor: " + accessor.getClass().getSimpleName());
/*  80 */     memUtil = accessor;
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
/*     */   public static long getAddress0(Buffer buffer) {
/* 113 */     return memUtil.getAddress(buffer);
/*     */   } public static long getAddress0Safe(Buffer buffer) {
/* 115 */     return (buffer == null) ? 0L : memUtil.getAddress(buffer);
/*     */   } public static long getAddress0(PointerBuffer buffer) {
/* 117 */     return memUtil.getAddress(buffer.getBuffer());
/*     */   } public static long getAddress0Safe(PointerBuffer buffer) {
/* 119 */     return (buffer == null) ? 0L : memUtil.getAddress(buffer.getBuffer());
/*     */   }
/*     */   
/*     */   public static long getAddress(ByteBuffer buffer) {
/* 123 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(ByteBuffer buffer, int position) {
/* 125 */     return getAddress0(buffer) + position;
/*     */   } public static long getAddress(ShortBuffer buffer) {
/* 127 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(ShortBuffer buffer, int position) {
/* 129 */     return getAddress0(buffer) + (position << 1);
/*     */   } public static long getAddress(CharBuffer buffer) {
/* 131 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(CharBuffer buffer, int position) {
/* 133 */     return getAddress0(buffer) + (position << 1);
/*     */   } public static long getAddress(IntBuffer buffer) {
/* 135 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(IntBuffer buffer, int position) {
/* 137 */     return getAddress0(buffer) + (position << 2);
/*     */   } public static long getAddress(FloatBuffer buffer) {
/* 139 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(FloatBuffer buffer, int position) {
/* 141 */     return getAddress0(buffer) + (position << 2);
/*     */   } public static long getAddress(LongBuffer buffer) {
/* 143 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(LongBuffer buffer, int position) {
/* 145 */     return getAddress0(buffer) + (position << 3);
/*     */   } public static long getAddress(DoubleBuffer buffer) {
/* 147 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(DoubleBuffer buffer, int position) {
/* 149 */     return getAddress0(buffer) + (position << 3);
/*     */   } public static long getAddress(PointerBuffer buffer) {
/* 151 */     return getAddress(buffer, buffer.position());
/*     */   } public static long getAddress(PointerBuffer buffer, int position) {
/* 153 */     return getAddress0(buffer) + (position * PointerBuffer.getPointerSize());
/*     */   }
/*     */   
/*     */   public static long getAddressSafe(ByteBuffer buffer) {
/* 157 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(ByteBuffer buffer, int position) {
/* 159 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(ShortBuffer buffer) {
/* 161 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(ShortBuffer buffer, int position) {
/* 163 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(CharBuffer buffer) {
/* 165 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(CharBuffer buffer, int position) {
/* 167 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(IntBuffer buffer) {
/* 169 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(IntBuffer buffer, int position) {
/* 171 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(FloatBuffer buffer) {
/* 173 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(FloatBuffer buffer, int position) {
/* 175 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(LongBuffer buffer) {
/* 177 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(LongBuffer buffer, int position) {
/* 179 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(DoubleBuffer buffer) {
/* 181 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(DoubleBuffer buffer, int position) {
/* 183 */     return (buffer == null) ? 0L : getAddress(buffer, position);
/*     */   } public static long getAddressSafe(PointerBuffer buffer) {
/* 185 */     return (buffer == null) ? 0L : getAddress(buffer);
/*     */   } public static long getAddressSafe(PointerBuffer buffer, int position) {
/* 187 */     return (buffer == null) ? 0L : getAddress(buffer, position);
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
/*     */   public static ByteBuffer encodeASCII(CharSequence text) {
/* 202 */     return encode(text, ascii);
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
/*     */   public static ByteBuffer encodeUTF8(CharSequence text) {
/* 216 */     return encode(text, utf8);
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
/*     */   public static ByteBuffer encodeUTF16(CharSequence text) {
/* 228 */     return encode(text, utf16);
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
/*     */   private static ByteBuffer encode(CharSequence text, Charset charset) {
/* 240 */     if (text == null) {
/* 241 */       return null;
/*     */     }
/* 243 */     return encode(CharBuffer.wrap(new CharSequenceNT(text)), charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer encode(CharBuffer in, Charset charset) {
/* 253 */     CharsetEncoder encoder = charset.newEncoder();
/*     */     
/* 255 */     int n = (int)(in.remaining() * encoder.averageBytesPerChar());
/* 256 */     ByteBuffer out = BufferUtils.createByteBuffer(n);
/*     */     
/* 258 */     if (n == 0 && in.remaining() == 0) {
/* 259 */       return out;
/*     */     }
/* 261 */     encoder.reset();
/*     */     while (true) {
/* 263 */       CoderResult cr = in.hasRemaining() ? encoder.encode(in, out, true) : CoderResult.UNDERFLOW;
/* 264 */       if (cr.isUnderflow()) {
/* 265 */         cr = encoder.flush(out);
/*     */       }
/* 267 */       if (cr.isUnderflow()) {
/*     */         break;
/*     */       }
/* 270 */       if (cr.isOverflow()) {
/* 271 */         n = 2 * n + 1;
/* 272 */         ByteBuffer o = BufferUtils.createByteBuffer(n);
/* 273 */         out.flip();
/* 274 */         o.put(out);
/* 275 */         out = o;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       try {
/* 280 */         cr.throwException();
/* 281 */       } catch (CharacterCodingException e) {
/* 282 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 285 */     out.flip();
/* 286 */     return out;
/*     */   }
/*     */   
/*     */   public static String decodeASCII(ByteBuffer buffer) {
/* 290 */     return decode(buffer, ascii);
/*     */   }
/*     */   
/*     */   public static String decodeUTF8(ByteBuffer buffer) {
/* 294 */     return decode(buffer, utf8);
/*     */   }
/*     */   
/*     */   public static String decodeUTF16(ByteBuffer buffer) {
/* 298 */     return decode(buffer, utf16);
/*     */   }
/*     */   
/*     */   private static String decode(ByteBuffer buffer, Charset charset) {
/* 302 */     if (buffer == null) {
/* 303 */       return null;
/*     */     }
/* 305 */     return decodeImpl(buffer, charset);
/*     */   }
/*     */   
/*     */   private static String decodeImpl(ByteBuffer in, Charset charset) {
/* 309 */     CharsetDecoder decoder = charset.newDecoder();
/*     */     
/* 311 */     int n = (int)(in.remaining() * decoder.averageCharsPerByte());
/* 312 */     CharBuffer out = BufferUtils.createCharBuffer(n);
/*     */     
/* 314 */     if (n == 0 && in.remaining() == 0) {
/* 315 */       return "";
/*     */     }
/* 317 */     decoder.reset();
/*     */     while (true) {
/* 319 */       CoderResult cr = in.hasRemaining() ? decoder.decode(in, out, true) : CoderResult.UNDERFLOW;
/* 320 */       if (cr.isUnderflow()) {
/* 321 */         cr = decoder.flush(out);
/*     */       }
/* 323 */       if (cr.isUnderflow())
/*     */         break; 
/* 325 */       if (cr.isOverflow()) {
/* 326 */         n = 2 * n + 1;
/* 327 */         CharBuffer o = BufferUtils.createCharBuffer(n);
/* 328 */         out.flip();
/* 329 */         o.put(out);
/* 330 */         out = o;
/*     */         continue;
/*     */       } 
/*     */       try {
/* 334 */         cr.throwException();
/* 335 */       } catch (CharacterCodingException e) {
/* 336 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 339 */     out.flip();
/* 340 */     return out.toString();
/*     */   }
/*     */   
/*     */   private static class CharSequenceNT
/*     */     implements CharSequence
/*     */   {
/*     */     final CharSequence source;
/*     */     
/*     */     CharSequenceNT(CharSequence source) {
/* 349 */       this.source = source;
/*     */     }
/*     */     
/*     */     public int length() {
/* 353 */       return this.source.length() + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public char charAt(int index) {
/* 358 */       return (index == this.source.length()) ? Character.MIN_VALUE : this.source.charAt(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSequence subSequence(int start, int end) {
/* 363 */       return new CharSequenceNT(this.source.subSequence(start, Math.min(end, this.source.length())));
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
/*     */   private static Accessor loadAccessor(String className) throws Exception {
/* 375 */     return (Accessor)Class.forName(className).newInstance();
/*     */   }
/*     */   
/*     */   static interface Accessor {
/*     */     long getAddress(Buffer param1Buffer); }
/*     */   
/*     */   private static class AccessorJNI implements Accessor { public long getAddress(Buffer buffer) {
/* 382 */       return BufferUtils.getBufferAddress(buffer);
/*     */     }
/*     */     
/*     */     private AccessorJNI() {} }
/*     */ 
/*     */   
/*     */   private static class AccessorReflect
/*     */     implements Accessor {
/*     */     private final Field address;
/*     */     
/*     */     AccessorReflect() {
/*     */       try {
/* 394 */         this.address = MemoryUtil.getAddressField();
/* 395 */       } catch (NoSuchFieldException e) {
/* 396 */         throw new UnsupportedOperationException(e);
/*     */       } 
/* 398 */       this.address.setAccessible(true);
/*     */     }
/*     */     
/*     */     public long getAddress(Buffer buffer) {
/*     */       try {
/* 403 */         return this.address.getLong(buffer);
/* 404 */       } catch (IllegalAccessException e) {
/*     */         
/* 406 */         return 0L;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static Field getAddressField() throws NoSuchFieldException {
/* 413 */     return getDeclaredFieldRecursive(ByteBuffer.class, "address");
/*     */   }
/*     */   
/*     */   private static Field getDeclaredFieldRecursive(Class<?> root, String fieldName) throws NoSuchFieldException {
/* 417 */     Class<?> type = root;
/*     */     
/*     */     while (true) {
/*     */       
/* 421 */       try { return type.getDeclaredField(fieldName); }
/* 422 */       catch (NoSuchFieldException e)
/* 423 */       { type = type.getSuperclass();
/*     */         
/* 425 */         if (type == null)
/*     */           break;  } 
/* 427 */     }  throw new NoSuchFieldException(fieldName + " does not exist in " + root.getSimpleName() + " or any of its superclasses.");
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\MemoryUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */