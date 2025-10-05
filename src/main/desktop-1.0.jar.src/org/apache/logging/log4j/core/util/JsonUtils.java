/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonUtils
/*     */ {
/*  24 */   private static final char[] HC = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int[] ESC_CODES;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  34 */     int[] table = new int[128];
/*     */     
/*  36 */     for (int i = 0; i < 32; i++)
/*     */     {
/*  38 */       table[i] = -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  43 */     table[34] = 34;
/*  44 */     table[92] = 92;
/*     */     
/*  46 */     table[8] = 98;
/*  47 */     table[9] = 116;
/*  48 */     table[12] = 102;
/*  49 */     table[10] = 110;
/*  50 */     table[13] = 114;
/*  51 */     ESC_CODES = table;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final ThreadLocal<char[]> _qbufLocal = (ThreadLocal)new ThreadLocal<>();
/*     */   
/*     */   private static char[] getQBuf() {
/*  60 */     char[] _qbuf = _qbufLocal.get();
/*  61 */     if (_qbuf == null) {
/*  62 */       _qbuf = new char[6];
/*  63 */       _qbuf[0] = '\\';
/*  64 */       _qbuf[2] = '0';
/*  65 */       _qbuf[3] = '0';
/*     */       
/*  67 */       _qbufLocal.set(_qbuf);
/*     */     } 
/*  69 */     return _qbuf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quoteAsString(CharSequence input, StringBuilder output) {
/*  76 */     char[] qbuf = getQBuf();
/*  77 */     int escCodeCount = ESC_CODES.length;
/*  78 */     int inPtr = 0;
/*  79 */     int inputLen = input.length();
/*     */ 
/*     */     
/*  82 */     while (inPtr < inputLen) {
/*     */       
/*     */       while (true) {
/*  85 */         char d, c = input.charAt(inPtr);
/*  86 */         if (c < escCodeCount && ESC_CODES[c] != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  95 */           d = input.charAt(inPtr++);
/*  96 */           int escCode = ESC_CODES[d];
/*  97 */           if (escCode < 0);
/*     */           
/*  99 */           int length = _appendNamed(escCode, qbuf);
/*     */           
/* 101 */           output.append(qbuf, 0, length); continue;
/*     */         }  output.append(d);
/*     */         if (++inPtr >= inputLen)
/*     */           break; 
/*     */       } 
/* 106 */     }  } private static int _appendNumeric(int value, char[] qbuf) { qbuf[1] = 'u';
/*     */     
/* 108 */     qbuf[4] = HC[value >> 4];
/* 109 */     qbuf[5] = HC[value & 0xF];
/* 110 */     return 6; }
/*     */ 
/*     */   
/*     */   private static int _appendNamed(int esc, char[] qbuf) {
/* 114 */     qbuf[1] = (char)esc;
/* 115 */     return 2;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\JsonUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */