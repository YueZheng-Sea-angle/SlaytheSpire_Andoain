/*     */ package org.apache.commons.net.telnet;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TelnetOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final TelnetClient __client;
/*     */   private final boolean __convertCRtoCRLF = true;
/*     */   private boolean __lastWasCR = false;
/*     */   
/*     */   TelnetOutputStream(TelnetClient client) {
/*  44 */     this.__client = client;
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
/*     */   public void write(int ch) throws IOException {
/*  59 */     synchronized (this.__client) {
/*     */       
/*  61 */       ch &= 0xFF;
/*     */       
/*  63 */       if (this.__client._requestedWont(0)) {
/*     */         
/*  65 */         if (this.__lastWasCR) {
/*     */ 
/*     */ 
/*     */           
/*  69 */           this.__client._sendByte(10);
/*  70 */           if (ch == 10) {
/*     */             
/*  72 */             this.__lastWasCR = false;
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*  82 */         switch (ch) {
/*     */           
/*     */           case 13:
/*  85 */             this.__client._sendByte(13);
/*  86 */             this.__lastWasCR = true;
/*     */             break;
/*     */           case 10:
/*  89 */             if (!this.__lastWasCR) {
/*  90 */               this.__client._sendByte(13);
/*     */             }
/*  92 */             this.__client._sendByte(ch);
/*  93 */             this.__lastWasCR = false;
/*     */             break;
/*     */           case 255:
/*  96 */             this.__client._sendByte(255);
/*  97 */             this.__client._sendByte(255);
/*  98 */             this.__lastWasCR = false;
/*     */             break;
/*     */           default:
/* 101 */             this.__client._sendByte(ch);
/* 102 */             this.__lastWasCR = false;
/*     */             break;
/*     */         } 
/*     */       
/* 106 */       } else if (ch == 255) {
/*     */         
/* 108 */         this.__client._sendByte(ch);
/* 109 */         this.__client._sendByte(255);
/*     */       } else {
/* 111 */         this.__client._sendByte(ch);
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
/*     */   public void write(byte[] buffer) throws IOException {
/* 127 */     write(buffer, 0, buffer.length);
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
/*     */   public void write(byte[] buffer, int offset, int length) throws IOException {
/* 144 */     synchronized (this.__client) {
/*     */       
/* 146 */       while (length-- > 0) {
/* 147 */         write(buffer[offset++]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 156 */     this.__client._flushOutputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 163 */     this.__client._closeOutputStream();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\TelnetOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */