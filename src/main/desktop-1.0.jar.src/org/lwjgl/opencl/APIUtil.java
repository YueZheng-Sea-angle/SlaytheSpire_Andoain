/*     */ package org.lwjgl.opencl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.DoubleBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.LWJGLUtil;
/*     */ import org.lwjgl.MemoryUtil;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.PointerWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class APIUtil
/*     */ {
/*     */   private static final int INITIAL_BUFFER_SIZE = 256;
/*     */   private static final int INITIAL_LENGTHS_SIZE = 4;
/*     */   private static final int BUFFERS_SIZE = 32;
/*     */   
/*  59 */   private static final ThreadLocal<char[]> arrayTL = new ThreadLocal<char[]>() { protected char[] initialValue() {
/*  60 */         return new char[256];
/*     */       } }
/*     */   ;
/*  63 */   private static final ThreadLocal<ByteBuffer> bufferByteTL = new ThreadLocal<ByteBuffer>() { protected ByteBuffer initialValue() {
/*  64 */         return BufferUtils.createByteBuffer(256);
/*     */       } }
/*     */   ;
/*  67 */   private static final ThreadLocal<PointerBuffer> bufferPointerTL = new ThreadLocal<PointerBuffer>() { protected PointerBuffer initialValue() {
/*  68 */         return BufferUtils.createPointerBuffer(256);
/*     */       } }
/*     */   ;
/*  71 */   private static final ThreadLocal<PointerBuffer> lengthsTL = new ThreadLocal<PointerBuffer>() { protected PointerBuffer initialValue() {
/*  72 */         return BufferUtils.createPointerBuffer(4);
/*     */       } }
/*     */   ;
/*  75 */   private static final ThreadLocal<Buffers> buffersTL = new ThreadLocal<Buffers>() { protected APIUtil.Buffers initialValue() {
/*  76 */         return new APIUtil.Buffers();
/*     */       } }
/*     */   ;
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] getArray(int size) {
/*  83 */     char[] array = arrayTL.get();
/*     */     
/*  85 */     if (array.length < size) {
/*  86 */       int sizeNew = array.length << 1;
/*  87 */       while (sizeNew < size) {
/*  88 */         sizeNew <<= 1;
/*     */       }
/*  90 */       array = new char[size];
/*  91 */       arrayTL.set(array);
/*     */     } 
/*     */     
/*  94 */     return array;
/*     */   }
/*     */   
/*     */   static ByteBuffer getBufferByte(int size) {
/*  98 */     ByteBuffer buffer = bufferByteTL.get();
/*     */     
/* 100 */     if (buffer.capacity() < size) {
/* 101 */       int sizeNew = buffer.capacity() << 1;
/* 102 */       while (sizeNew < size) {
/* 103 */         sizeNew <<= 1;
/*     */       }
/* 105 */       buffer = BufferUtils.createByteBuffer(size);
/* 106 */       bufferByteTL.set(buffer);
/*     */     } else {
/* 108 */       buffer.clear();
/*     */     } 
/* 110 */     return buffer;
/*     */   }
/*     */   
/*     */   private static ByteBuffer getBufferByteOffset(int size) {
/* 114 */     ByteBuffer buffer = bufferByteTL.get();
/*     */     
/* 116 */     if (buffer.capacity() < size) {
/* 117 */       int sizeNew = buffer.capacity() << 1;
/* 118 */       while (sizeNew < size) {
/* 119 */         sizeNew <<= 1;
/*     */       }
/* 121 */       ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
/* 122 */       bufferNew.put(buffer);
/* 123 */       bufferByteTL.set(buffer = bufferNew);
/*     */     } else {
/* 125 */       buffer.position(buffer.limit());
/* 126 */       buffer.limit(buffer.capacity());
/*     */     } 
/*     */     
/* 129 */     return buffer;
/*     */   }
/*     */   
/*     */   static PointerBuffer getBufferPointer(int size) {
/* 133 */     PointerBuffer buffer = bufferPointerTL.get();
/*     */     
/* 135 */     if (buffer.capacity() < size) {
/* 136 */       int sizeNew = buffer.capacity() << 1;
/* 137 */       while (sizeNew < size) {
/* 138 */         sizeNew <<= 1;
/*     */       }
/* 140 */       buffer = BufferUtils.createPointerBuffer(size);
/* 141 */       bufferPointerTL.set(buffer);
/*     */     } else {
/* 143 */       buffer.clear();
/*     */     } 
/* 145 */     return buffer;
/*     */   }
/*     */   static ShortBuffer getBufferShort() {
/* 148 */     return ((Buffers)buffersTL.get()).shorts;
/*     */   } static IntBuffer getBufferInt() {
/* 150 */     return ((Buffers)buffersTL.get()).ints;
/*     */   } static IntBuffer getBufferIntDebug() {
/* 152 */     return ((Buffers)buffersTL.get()).intsDebug;
/*     */   } static LongBuffer getBufferLong() {
/* 154 */     return ((Buffers)buffersTL.get()).longs;
/*     */   } static FloatBuffer getBufferFloat() {
/* 156 */     return ((Buffers)buffersTL.get()).floats;
/*     */   } static DoubleBuffer getBufferDouble() {
/* 158 */     return ((Buffers)buffersTL.get()).doubles;
/*     */   } static PointerBuffer getBufferPointer() {
/* 160 */     return ((Buffers)buffersTL.get()).pointers;
/*     */   }
/*     */   static PointerBuffer getLengths() {
/* 163 */     return getLengths(1);
/*     */   }
/*     */   
/*     */   static PointerBuffer getLengths(int size) {
/* 167 */     PointerBuffer lengths = lengthsTL.get();
/*     */     
/* 169 */     if (lengths.capacity() < size) {
/* 170 */       int sizeNew = lengths.capacity();
/* 171 */       while (sizeNew < size) {
/* 172 */         sizeNew <<= 1;
/*     */       }
/* 174 */       lengths = BufferUtils.createPointerBuffer(size);
/* 175 */       lengthsTL.set(lengths);
/*     */     } else {
/* 177 */       lengths.clear();
/*     */     } 
/* 179 */     return lengths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer encode(ByteBuffer buffer, CharSequence string) {
/* 189 */     for (int i = 0; i < string.length(); i++) {
/* 190 */       char c = string.charAt(i);
/* 191 */       if (LWJGLUtil.DEBUG && '' <= c) {
/* 192 */         buffer.put((byte)26);
/*     */       } else {
/* 194 */         buffer.put((byte)c);
/*     */       } 
/*     */     } 
/* 197 */     return buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getString(ByteBuffer buffer) {
/* 208 */     int length = buffer.remaining();
/* 209 */     char[] charArray = getArray(length);
/*     */     
/* 211 */     for (int i = buffer.position(); i < buffer.limit(); i++) {
/* 212 */       charArray[i - buffer.position()] = (char)buffer.get(i);
/*     */     }
/* 214 */     return new String(charArray, 0, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getBuffer(CharSequence string) {
/* 225 */     ByteBuffer buffer = encode(getBufferByte(string.length()), string);
/* 226 */     buffer.flip();
/* 227 */     return MemoryUtil.getAddress0(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getBuffer(CharSequence string, int offset) {
/* 238 */     ByteBuffer buffer = encode(getBufferByteOffset(offset + string.length()), string);
/* 239 */     buffer.flip();
/* 240 */     return MemoryUtil.getAddress(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getBufferNT(CharSequence string) {
/* 251 */     ByteBuffer buffer = encode(getBufferByte(string.length() + 1), string);
/* 252 */     buffer.put((byte)0);
/* 253 */     buffer.flip();
/* 254 */     return MemoryUtil.getAddress0(buffer);
/*     */   }
/*     */   
/*     */   static int getTotalLength(CharSequence[] strings) {
/* 258 */     int length = 0;
/* 259 */     for (CharSequence string : strings) {
/* 260 */       length += string.length();
/*     */     }
/* 262 */     return length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getBuffer(CharSequence[] strings) {
/* 273 */     ByteBuffer buffer = getBufferByte(getTotalLength(strings));
/*     */     
/* 275 */     for (CharSequence string : strings) {
/* 276 */       encode(buffer, string);
/*     */     }
/* 278 */     buffer.flip();
/* 279 */     return MemoryUtil.getAddress0(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getBufferNT(CharSequence[] strings) {
/* 290 */     ByteBuffer buffer = getBufferByte(getTotalLength(strings) + strings.length);
/*     */     
/* 292 */     for (CharSequence string : strings) {
/* 293 */       encode(buffer, string);
/* 294 */       buffer.put((byte)0);
/*     */     } 
/*     */     
/* 297 */     buffer.flip();
/* 298 */     return MemoryUtil.getAddress0(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getLengths(CharSequence[] strings) {
/* 309 */     PointerBuffer buffer = getLengths(strings.length);
/*     */     
/* 311 */     for (CharSequence string : strings) {
/* 312 */       buffer.put(string.length());
/*     */     }
/* 314 */     buffer.flip();
/* 315 */     return MemoryUtil.getAddress0(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getLengths(ByteBuffer[] buffers) {
/* 326 */     PointerBuffer lengths = getLengths(buffers.length);
/*     */     
/* 328 */     for (ByteBuffer buffer : buffers) {
/* 329 */       lengths.put(buffer.remaining());
/*     */     }
/* 331 */     lengths.flip();
/* 332 */     return MemoryUtil.getAddress0(lengths);
/*     */   }
/*     */   
/*     */   static int getSize(PointerBuffer lengths) {
/* 336 */     long size = 0L;
/* 337 */     for (int i = lengths.position(); i < lengths.limit(); i++) {
/* 338 */       size += lengths.get(i);
/*     */     }
/* 340 */     return (int)size;
/*     */   }
/*     */   
/*     */   static long getPointer(PointerWrapper pointer) {
/* 344 */     return MemoryUtil.getAddress0(getBufferPointer().put(0, pointer));
/*     */   }
/*     */   
/*     */   static long getPointerSafe(PointerWrapper pointer) {
/* 348 */     return MemoryUtil.getAddress0(getBufferPointer().put(0, (pointer == null) ? 0L : pointer.getPointer()));
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
/*     */   private static class Buffers
/*     */   {
/* 364 */     final ShortBuffer shorts = BufferUtils.createShortBuffer(32);
/* 365 */     final IntBuffer ints = BufferUtils.createIntBuffer(32);
/* 366 */     final IntBuffer intsDebug = BufferUtils.createIntBuffer(1);
/* 367 */     final LongBuffer longs = BufferUtils.createLongBuffer(32);
/*     */     
/* 369 */     final FloatBuffer floats = BufferUtils.createFloatBuffer(32);
/* 370 */     final DoubleBuffer doubles = BufferUtils.createDoubleBuffer(32);
/*     */     
/* 372 */     final PointerBuffer pointers = BufferUtils.createPointerBuffer(32);
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
/*     */   static Set<String> getExtensions(String extensionList) {
/* 384 */     Set<String> extensions = new HashSet<String>();
/*     */     
/* 386 */     if (extensionList != null) {
/* 387 */       StringTokenizer tokenizer = new StringTokenizer(extensionList);
/* 388 */       while (tokenizer.hasMoreTokens()) {
/* 389 */         extensions.add(tokenizer.nextToken());
/*     */       }
/*     */     } 
/* 392 */     return extensions;
/*     */   }
/*     */   
/*     */   static boolean isDevicesParam(int param_name) {
/* 396 */     switch (param_name) {
/*     */       case 4225:
/*     */       case 8198:
/*     */       case 8199:
/*     */       case 268435458:
/*     */       case 268435459:
/* 402 */         return true;
/*     */     } 
/*     */     
/* 405 */     return false;
/*     */   }
/*     */   
/*     */   static CLPlatform getCLPlatform(PointerBuffer properties) {
/* 409 */     long platformID = 0L;
/*     */     
/* 411 */     int keys = properties.remaining() / 2;
/* 412 */     for (int k = 0; k < keys; k++) {
/* 413 */       long key = properties.get(k << 1);
/* 414 */       if (key == 0L) {
/*     */         break;
/*     */       }
/* 417 */       if (key == 4228L) {
/* 418 */         platformID = properties.get((k << 1) + 1);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 423 */     if (platformID == 0L) {
/* 424 */       throw new IllegalArgumentException("Could not find CL_CONTEXT_PLATFORM in cl_context_properties.");
/*     */     }
/* 426 */     CLPlatform platform = CLPlatform.getCLPlatform(platformID);
/* 427 */     if (platform == null) {
/* 428 */       throw new IllegalStateException("Could not find a valid CLPlatform. Make sure clGetPlatformIDs has been used before.");
/*     */     }
/* 430 */     return platform;
/*     */   }
/*     */   
/*     */   static ByteBuffer getNativeKernelArgs(long user_func_ref, CLMem[] clMems, long[] sizes) {
/* 434 */     ByteBuffer args = getBufferByte(12 + ((clMems == null) ? 0 : (clMems.length * (4 + PointerBuffer.getPointerSize()))));
/*     */     
/* 436 */     args.putLong(0, user_func_ref);
/* 437 */     if (clMems == null) {
/* 438 */       args.putInt(8, 0);
/*     */     } else {
/* 440 */       args.putInt(8, clMems.length);
/* 441 */       int byteIndex = 12;
/* 442 */       for (int i = 0; i < clMems.length; i++) {
/* 443 */         if (LWJGLUtil.DEBUG && !clMems[i].isValid())
/* 444 */           throw new IllegalArgumentException("An invalid CLMem object was specified."); 
/* 445 */         args.putInt(byteIndex, (int)sizes[i]);
/* 446 */         byteIndex += 4 + PointerBuffer.getPointerSize();
/*     */       } 
/*     */     } 
/*     */     
/* 450 */     return args;
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
/*     */   static void releaseObjects(CLDevice device) {
/* 462 */     if (!device.isValid() || device.getReferenceCount() > 1) {
/*     */       return;
/*     */     }
/* 465 */     releaseObjects(device.getSubCLDeviceRegistry(), DESTRUCTOR_CLSubDevice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void releaseObjects(CLContext context) {
/* 475 */     if (!context.isValid() || context.getReferenceCount() > 1) {
/*     */       return;
/*     */     }
/* 478 */     releaseObjects(context.getCLEventRegistry(), DESTRUCTOR_CLEvent);
/* 479 */     releaseObjects(context.getCLProgramRegistry(), DESTRUCTOR_CLProgram);
/* 480 */     releaseObjects(context.getCLSamplerRegistry(), DESTRUCTOR_CLSampler);
/* 481 */     releaseObjects(context.getCLMemRegistry(), DESTRUCTOR_CLMem);
/* 482 */     releaseObjects(context.getCLCommandQueueRegistry(), DESTRUCTOR_CLCommandQueue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void releaseObjects(CLProgram program) {
/* 492 */     if (!program.isValid() || program.getReferenceCount() > 1) {
/*     */       return;
/*     */     }
/* 495 */     releaseObjects(program.getCLKernelRegistry(), DESTRUCTOR_CLKernel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void releaseObjects(CLCommandQueue queue) {
/* 505 */     if (!queue.isValid() || queue.getReferenceCount() > 1) {
/*     */       return;
/*     */     }
/* 508 */     releaseObjects(queue.getCLEventRegistry(), DESTRUCTOR_CLEvent);
/*     */   }
/*     */   
/*     */   private static <T extends CLObjectChild> void releaseObjects(CLObjectRegistry<T> registry, ObjectDestructor<T> destructor) {
/* 512 */     if (registry.isEmpty()) {
/*     */       return;
/*     */     }
/* 515 */     for (FastLongMap.Entry<T> entry : registry.getAll()) {
/* 516 */       CLObjectChild cLObjectChild = (CLObjectChild)entry.value;
/* 517 */       while (cLObjectChild.isValid())
/* 518 */         destructor.release((T)cLObjectChild); 
/*     */     } 
/*     */   }
/*     */   
/* 522 */   private static final ObjectDestructor<CLDevice> DESTRUCTOR_CLSubDevice = new ObjectDestructor<CLDevice>() { public void release(CLDevice object) {
/* 523 */         EXTDeviceFission.clReleaseDeviceEXT(object);
/*     */       } }
/* 525 */   ; private static final ObjectDestructor<CLMem> DESTRUCTOR_CLMem = new ObjectDestructor<CLMem>() { public void release(CLMem object) {
/* 526 */         CL10.clReleaseMemObject(object);
/*     */       } }
/* 528 */   ; private static final ObjectDestructor<CLCommandQueue> DESTRUCTOR_CLCommandQueue = new ObjectDestructor<CLCommandQueue>() { public void release(CLCommandQueue object) {
/* 529 */         CL10.clReleaseCommandQueue(object);
/*     */       } }
/* 531 */   ; private static final ObjectDestructor<CLSampler> DESTRUCTOR_CLSampler = new ObjectDestructor<CLSampler>() { public void release(CLSampler object) {
/* 532 */         CL10.clReleaseSampler(object);
/*     */       } }
/* 534 */   ; private static final ObjectDestructor<CLProgram> DESTRUCTOR_CLProgram = new ObjectDestructor<CLProgram>() { public void release(CLProgram object) {
/* 535 */         CL10.clReleaseProgram(object);
/*     */       } }
/* 537 */   ; private static final ObjectDestructor<CLKernel> DESTRUCTOR_CLKernel = new ObjectDestructor<CLKernel>() { public void release(CLKernel object) {
/* 538 */         CL10.clReleaseKernel(object);
/*     */       } }
/* 540 */   ; private static final ObjectDestructor<CLEvent> DESTRUCTOR_CLEvent = new ObjectDestructor<CLEvent>() { public void release(CLEvent object) {
/* 541 */         CL10.clReleaseEvent(object);
/*     */       } }
/*     */   ;
/*     */   
/*     */   private static interface ObjectDestructor<T extends CLObjectChild> {
/*     */     void release(T param1T);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjgl\opencl\APIUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */