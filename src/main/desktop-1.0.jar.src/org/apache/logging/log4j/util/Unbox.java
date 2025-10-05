/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class Unbox
/*     */ {
/*  50 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   private static final int BITS_PER_INT = 32;
/*     */   private static final int RINGBUFFER_MIN_SIZE = 32;
/*  53 */   private static final int RINGBUFFER_SIZE = calculateRingBufferSize("log4j.unbox.ringbuffer.size");
/*  54 */   private static final int MASK = RINGBUFFER_SIZE - 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WebSafeState
/*     */   {
/*  66 */     private final ThreadLocal<StringBuilder[]> ringBuffer = (ThreadLocal)new ThreadLocal<>();
/*  67 */     private final ThreadLocal<int[]> current = (ThreadLocal)new ThreadLocal<>();
/*     */     
/*     */     public StringBuilder getStringBuilder() {
/*  70 */       StringBuilder[] array = this.ringBuffer.get();
/*  71 */       if (array == null) {
/*  72 */         array = new StringBuilder[Unbox.RINGBUFFER_SIZE];
/*  73 */         for (int i = 0; i < array.length; i++) {
/*  74 */           array[i] = new StringBuilder(21);
/*     */         }
/*  76 */         this.ringBuffer.set(array);
/*  77 */         this.current.set(new int[1]);
/*     */       } 
/*  79 */       int[] index = this.current.get();
/*  80 */       index[0] = index[0] + 1; StringBuilder result = array[Unbox.MASK & index[0]];
/*  81 */       result.setLength(0);
/*  82 */       return result;
/*     */     }
/*     */     
/*     */     public boolean isBoxedPrimitive(StringBuilder text) {
/*  86 */       StringBuilder[] array = this.ringBuffer.get();
/*  87 */       if (array == null) {
/*  88 */         return false;
/*     */       }
/*  90 */       for (int i = 0; i < array.length; i++) {
/*  91 */         if (text == array[i]) {
/*  92 */           return true;
/*     */         }
/*     */       } 
/*  95 */       return false;
/*     */     }
/*     */     
/*     */     private WebSafeState() {} }
/*     */   
/* 100 */   private static class State { private final StringBuilder[] ringBuffer = new StringBuilder[Unbox.RINGBUFFER_SIZE]; private int current;
/*     */     
/*     */     State() {
/* 103 */       for (int i = 0; i < this.ringBuffer.length; i++) {
/* 104 */         this.ringBuffer[i] = new StringBuilder(21);
/*     */       }
/*     */     }
/*     */     
/*     */     public StringBuilder getStringBuilder() {
/* 109 */       StringBuilder result = this.ringBuffer[Unbox.MASK & this.current++];
/* 110 */       result.setLength(0);
/* 111 */       return result;
/*     */     }
/*     */     
/*     */     public boolean isBoxedPrimitive(StringBuilder text) {
/* 115 */       for (int i = 0; i < this.ringBuffer.length; i++) {
/* 116 */         if (text == this.ringBuffer[i]) {
/* 117 */           return true;
/*     */         }
/*     */       } 
/* 120 */       return false;
/*     */     } }
/*     */   
/* 123 */   private static ThreadLocal<State> threadLocalState = new ThreadLocal<>();
/* 124 */   private static WebSafeState webSafeState = new WebSafeState();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int calculateRingBufferSize(String propertyName) {
/* 131 */     String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, 
/* 132 */         String.valueOf(32));
/*     */     try {
/* 134 */       int size = Integer.parseInt(userPreferredRBSize);
/* 135 */       if (size < 32) {
/* 136 */         size = 32;
/* 137 */         LOGGER.warn("Invalid {} {}, using minimum size {}.", propertyName, userPreferredRBSize, 
/* 138 */             Integer.valueOf(32));
/*     */       } 
/* 140 */       return ceilingNextPowerOfTwo(size);
/* 141 */     } catch (Exception ex) {
/* 142 */       LOGGER.warn("Invalid {} {}, using default size {}.", propertyName, userPreferredRBSize, 
/* 143 */           Integer.valueOf(32));
/* 144 */       return 32;
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
/*     */   private static int ceilingNextPowerOfTwo(int x) {
/* 157 */     return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(float value) {
/* 169 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(double value) {
/* 181 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(short value) {
/* 193 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(int value) {
/* 205 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(char value) {
/* 217 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(long value) {
/* 229 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(byte value) {
/* 241 */     return getSB().append(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PerformanceSensitive({"allocation"})
/*     */   public static StringBuilder box(boolean value) {
/* 253 */     return getSB().append(value);
/*     */   }
/*     */   
/*     */   private static State getState() {
/* 257 */     State state = threadLocalState.get();
/* 258 */     if (state == null) {
/* 259 */       state = new State();
/* 260 */       threadLocalState.set(state);
/*     */     } 
/* 262 */     return state;
/*     */   }
/*     */   
/*     */   private static StringBuilder getSB() {
/* 266 */     return Constants.ENABLE_THREADLOCALS ? getState().getStringBuilder() : webSafeState.getStringBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   static int getRingbufferSize() {
/* 271 */     return RINGBUFFER_SIZE;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Unbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */