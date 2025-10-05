/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UuidUtil
/*     */ {
/*  35 */   private static final long[] EMPTY_LONG_ARRAY = new long[0];
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
/*     */ 
/*     */   
/*  42 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
/*     */   
/*  46 */   private static final AtomicInteger COUNT = new AtomicInteger(0);
/*     */   private static final long TYPE1 = 4096L;
/*     */   private static final byte VARIANT = -128;
/*     */   private static final int SEQUENCE_MASK = 16383;
/*     */   private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
/*  51 */   private static final long INITIAL_UUID_SEQNO = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
/*     */   
/*     */   private static final long LOW_MASK = 4294967295L;
/*     */   
/*     */   private static final long MID_MASK = 281470681743360L;
/*     */   private static final long HIGH_MASK = 1152640029630136320L;
/*     */   private static final int NODE_SIZE = 8;
/*     */   private static final int SHIFT_2 = 16;
/*     */   private static final int SHIFT_4 = 32;
/*     */   private static final int SHIFT_6 = 48;
/*     */   private static final int HUNDRED_NANOS_PER_MILLI = 10000;
/*  62 */   private static final long LEAST = initialize(NetUtils.getMacAddress());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long initialize(byte[] mac) {
/*     */     long[] sequences;
/*  75 */     Random randomGenerator = new SecureRandom();
/*  76 */     if (mac == null || mac.length == 0) {
/*  77 */       mac = new byte[6];
/*  78 */       randomGenerator.nextBytes(mac);
/*     */     } 
/*  80 */     int length = (mac.length >= 6) ? 6 : mac.length;
/*  81 */     int index = (mac.length >= 6) ? (mac.length - 6) : 0;
/*  82 */     byte[] node = new byte[8];
/*  83 */     node[0] = Byte.MIN_VALUE;
/*  84 */     node[1] = 0;
/*  85 */     for (int i = 2; i < 8; i++) {
/*  86 */       node[i] = 0;
/*     */     }
/*  88 */     System.arraycopy(mac, index, node, 2, length);
/*  89 */     ByteBuffer buf = ByteBuffer.wrap(node);
/*  90 */     long rand = INITIAL_UUID_SEQNO;
/*  91 */     String assigned = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
/*     */     
/*  93 */     if (assigned == null) {
/*  94 */       sequences = EMPTY_LONG_ARRAY;
/*     */     } else {
/*  96 */       String[] array = assigned.split(Patterns.COMMA_SEPARATOR);
/*  97 */       sequences = new long[array.length];
/*  98 */       int j = 0;
/*  99 */       for (String value : array) {
/* 100 */         sequences[j] = Long.parseLong(value);
/* 101 */         j++;
/*     */       } 
/*     */     } 
/* 104 */     if (rand == 0L) {
/* 105 */       rand = randomGenerator.nextLong();
/*     */     }
/* 107 */     rand &= 0x3FFFL;
/*     */     
/*     */     while (true) {
/* 110 */       boolean duplicate = false;
/* 111 */       for (long sequence : sequences) {
/* 112 */         if (sequence == rand) {
/* 113 */           duplicate = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 117 */       if (duplicate) {
/* 118 */         rand = rand + 1L & 0x3FFFL;
/*     */       }
/* 120 */       if (!duplicate) {
/* 121 */         assigned = (assigned == null) ? Long.toString(rand) : (assigned + ',' + Long.toString(rand));
/* 122 */         System.setProperty("org.apache.logging.log4j.assignedSequences", assigned);
/*     */         
/* 124 */         return buf.getLong() | rand << 48L;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static UUID getTimeBasedUuid() {
/* 146 */     long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (COUNT.incrementAndGet() % 10000);
/* 147 */     long timeLow = (time & 0xFFFFFFFFL) << 32L;
/* 148 */     long timeMid = (time & 0xFFFF00000000L) >> 16L;
/* 149 */     long timeHi = (time & 0xFFF000000000000L) >> 48L;
/* 150 */     long most = timeLow | timeMid | 0x1000L | timeHi;
/* 151 */     return new UUID(most, LEAST);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\UuidUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */