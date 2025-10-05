/*     */ package org.apache.commons.net.telnet;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TelnetInputStream
/*     */   extends BufferedInputStream
/*     */   implements Runnable
/*     */ {
/*     */   private static final int EOF = -1;
/*     */   private static final int WOULD_BLOCK = -2;
/*     */   static final int _STATE_DATA = 0;
/*     */   static final int _STATE_IAC = 1;
/*     */   static final int _STATE_WILL = 2;
/*     */   static final int _STATE_WONT = 3;
/*     */   static final int _STATE_DO = 4;
/*     */   static final int _STATE_DONT = 5;
/*     */   static final int _STATE_SB = 6;
/*     */   static final int _STATE_SE = 7;
/*     */   static final int _STATE_CR = 8;
/*     */   static final int _STATE_IAC_SB = 9;
/*     */   private boolean __hasReachedEOF;
/*     */   private volatile boolean __isClosed;
/*     */   private boolean __readIsWaiting;
/*     */   private int __receiveState;
/*     */   private int __queueHead;
/*     */   private int __queueTail;
/*     */   private int __bytesAvailable;
/*     */   private final int[] __queue;
/*     */   private final TelnetClient __client;
/*     */   private final Thread __thread;
/*     */   private IOException __ioException;
/*  48 */   private final int[] __suboption = new int[512];
/*  49 */   private int __suboption_count = 0;
/*     */ 
/*     */   
/*     */   private volatile boolean __threaded;
/*     */ 
/*     */ 
/*     */   
/*     */   TelnetInputStream(InputStream input, TelnetClient client, boolean readerThread) {
/*  57 */     super(input);
/*  58 */     this.__client = client;
/*  59 */     this.__receiveState = 0;
/*  60 */     this.__isClosed = true;
/*  61 */     this.__hasReachedEOF = false;
/*     */ 
/*     */     
/*  64 */     this.__queue = new int[2049];
/*  65 */     this.__queueHead = 0;
/*  66 */     this.__queueTail = 0;
/*  67 */     this.__bytesAvailable = 0;
/*  68 */     this.__ioException = null;
/*  69 */     this.__readIsWaiting = false;
/*  70 */     this.__threaded = false;
/*  71 */     if (readerThread) {
/*  72 */       this.__thread = new Thread(this);
/*     */     } else {
/*  74 */       this.__thread = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   TelnetInputStream(InputStream input, TelnetClient client) {
/*  79 */     this(input, client, true);
/*     */   }
/*     */ 
/*     */   
/*     */   void _start() {
/*  84 */     if (this.__thread == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  89 */     this.__isClosed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     int priority = Thread.currentThread().getPriority() + 1;
/*  95 */     if (priority > 10) {
/*  96 */       priority = 10;
/*     */     }
/*  98 */     this.__thread.setPriority(priority);
/*  99 */     this.__thread.setDaemon(true);
/* 100 */     this.__thread.start();
/* 101 */     this.__threaded = true;
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
/*     */   private int __read(boolean mayBlock) throws IOException {
/*     */     int ch;
/*     */     while (true) {
/* 127 */       if (!mayBlock && super.available() == 0) {
/* 128 */         return -2;
/*     */       }
/*     */ 
/*     */       
/* 132 */       if ((ch = super.read()) < 0) {
/* 133 */         return -1;
/*     */       }
/*     */       
/* 136 */       ch &= 0xFF;
/*     */ 
/*     */       
/* 139 */       synchronized (this.__client) {
/*     */         
/* 141 */         this.__client._processAYTResponse();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 146 */       this.__client._spyRead(ch);
/*     */ 
/*     */       
/* 149 */       switch (this.__receiveState) {
/*     */ 
/*     */         
/*     */         case 8:
/* 153 */           if (ch == 0) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 0:
/* 165 */           if (ch == 255) {
/*     */             
/* 167 */             this.__receiveState = 1;
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 172 */           if (ch == 13) {
/*     */             
/* 174 */             synchronized (this.__client) {
/*     */               
/* 176 */               if (this.__client._requestedDont(0)) {
/* 177 */                 this.__receiveState = 8;
/*     */               } else {
/* 179 */                 this.__receiveState = 0;
/*     */               } 
/*     */             }  break;
/*     */           } 
/* 183 */           this.__receiveState = 0;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1:
/* 188 */           switch (ch) {
/*     */             
/*     */             case 251:
/* 191 */               this.__receiveState = 2;
/*     */               continue;
/*     */             case 252:
/* 194 */               this.__receiveState = 3;
/*     */               continue;
/*     */             case 253:
/* 197 */               this.__receiveState = 4;
/*     */               continue;
/*     */             case 254:
/* 200 */               this.__receiveState = 5;
/*     */               continue;
/*     */             
/*     */             case 250:
/* 204 */               this.__suboption_count = 0;
/* 205 */               this.__receiveState = 6;
/*     */               continue;
/*     */             
/*     */             case 255:
/* 209 */               this.__receiveState = 0;
/*     */               break;
/*     */             case 240:
/* 212 */               this.__receiveState = 0;
/*     */               continue;
/*     */           } 
/* 215 */           this.__receiveState = 0;
/* 216 */           this.__client._processCommand(ch);
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 2:
/* 221 */           synchronized (this.__client) {
/*     */             
/* 223 */             this.__client._processWill(ch);
/* 224 */             this.__client._flushOutputStream();
/*     */           } 
/* 226 */           this.__receiveState = 0;
/*     */           continue;
/*     */         case 3:
/* 229 */           synchronized (this.__client) {
/*     */             
/* 231 */             this.__client._processWont(ch);
/* 232 */             this.__client._flushOutputStream();
/*     */           } 
/* 234 */           this.__receiveState = 0;
/*     */           continue;
/*     */         case 4:
/* 237 */           synchronized (this.__client) {
/*     */             
/* 239 */             this.__client._processDo(ch);
/* 240 */             this.__client._flushOutputStream();
/*     */           } 
/* 242 */           this.__receiveState = 0;
/*     */           continue;
/*     */         case 5:
/* 245 */           synchronized (this.__client) {
/*     */             
/* 247 */             this.__client._processDont(ch);
/* 248 */             this.__client._flushOutputStream();
/*     */           } 
/* 250 */           this.__receiveState = 0;
/*     */           continue;
/*     */         
/*     */         case 6:
/* 254 */           switch (ch) {
/*     */             
/*     */             case 255:
/* 257 */               this.__receiveState = 9;
/*     */               continue;
/*     */           } 
/*     */           
/* 261 */           if (this.__suboption_count < this.__suboption.length) {
/* 262 */             this.__suboption[this.__suboption_count++] = ch;
/*     */           }
/*     */ 
/*     */           
/* 266 */           this.__receiveState = 6;
/*     */           continue;
/*     */         case 9:
/* 269 */           switch (ch) {
/*     */             
/*     */             case 240:
/* 272 */               synchronized (this.__client) {
/*     */                 
/* 274 */                 this.__client._processSuboption(this.__suboption, this.__suboption_count);
/* 275 */                 this.__client._flushOutputStream();
/*     */               } 
/* 277 */               this.__receiveState = 0;
/*     */               continue;
/*     */             case 255:
/* 280 */               if (this.__suboption_count < this.__suboption.length) {
/* 281 */                 this.__suboption[this.__suboption_count++] = ch;
/*     */               }
/*     */               break;
/*     */           } 
/*     */ 
/*     */           
/* 287 */           this.__receiveState = 6;
/*     */           continue;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       break;
/*     */     } 
/* 295 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean __processChar(int ch) throws InterruptedException {
/*     */     boolean bufferWasEmpty;
/* 307 */     synchronized (this.__queue) {
/*     */       
/* 309 */       bufferWasEmpty = (this.__bytesAvailable == 0);
/* 310 */       while (this.__bytesAvailable >= this.__queue.length - 1) {
/*     */ 
/*     */ 
/*     */         
/* 314 */         if (this.__threaded) {
/*     */           
/* 316 */           this.__queue.notify();
/*     */           
/*     */           try {
/* 319 */             this.__queue.wait();
/*     */           }
/* 321 */           catch (InterruptedException e) {
/*     */             
/* 323 */             throw e;
/*     */           } 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 330 */         throw new IllegalStateException("Queue is full! Cannot process another character.");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 335 */       if (this.__readIsWaiting && this.__threaded)
/*     */       {
/* 337 */         this.__queue.notify();
/*     */       }
/*     */       
/* 340 */       this.__queue[this.__queueTail] = ch;
/* 341 */       this.__bytesAvailable++;
/*     */       
/* 343 */       if (++this.__queueTail >= this.__queue.length) {
/* 344 */         this.__queueTail = 0;
/*     */       }
/*     */     } 
/* 347 */     return bufferWasEmpty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 356 */     synchronized (this.__queue) {
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         
/* 361 */         if (this.__ioException != null) {
/*     */ 
/*     */           
/* 364 */           IOException e = this.__ioException;
/* 365 */           this.__ioException = null;
/* 366 */           throw e;
/*     */         } 
/*     */         
/* 369 */         if (this.__bytesAvailable == 0) {
/*     */ 
/*     */           
/* 372 */           if (this.__hasReachedEOF) {
/* 373 */             return -1;
/*     */           }
/*     */ 
/*     */           
/* 377 */           if (this.__threaded) {
/*     */             
/* 379 */             this.__queue.notify();
/*     */             
/*     */             try {
/* 382 */               this.__readIsWaiting = true;
/* 383 */               this.__queue.wait();
/* 384 */               this.__readIsWaiting = false;
/*     */             }
/* 386 */             catch (InterruptedException e) {
/*     */               
/* 388 */               throw new InterruptedIOException("Fatal thread interruption during read.");
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/* 394 */           this.__readIsWaiting = true;
/*     */           
/* 396 */           boolean mayBlock = true;
/*     */           
/*     */           do {
/*     */             int i;
/*     */             
/*     */             try {
/* 402 */               if ((i = __read(mayBlock)) < 0 && 
/* 403 */                 i != -2) {
/* 404 */                 return i;
/*     */               
/*     */               }
/*     */             }
/* 408 */             catch (InterruptedIOException e) {
/*     */               
/* 410 */               synchronized (this.__queue) {
/*     */                 
/* 412 */                 this.__ioException = e;
/* 413 */                 this.__queue.notifyAll();
/*     */                 
/*     */                 try {
/* 416 */                   this.__queue.wait(100L);
/*     */                 }
/* 418 */                 catch (InterruptedException interrupted) {}
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/* 423 */               return -1;
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 429 */               if (i != -2)
/*     */               {
/* 431 */                 __processChar(i);
/*     */               }
/*     */             }
/* 434 */             catch (InterruptedException e) {
/*     */               
/* 436 */               if (this.__isClosed) {
/* 437 */                 return -1;
/*     */               }
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 443 */             mayBlock = false;
/*     */ 
/*     */           
/*     */           }
/* 447 */           while (super.available() > 0 && this.__bytesAvailable < this.__queue.length - 1);
/*     */           
/* 449 */           this.__readIsWaiting = false;
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 457 */       int ch = this.__queue[this.__queueHead];
/*     */       
/* 459 */       if (++this.__queueHead >= this.__queue.length) {
/* 460 */         this.__queueHead = 0;
/*     */       }
/*     */       
/* 463 */       this.__bytesAvailable--;
/*     */ 
/*     */       
/* 466 */       if (this.__bytesAvailable == 0 && this.__threaded) {
/* 467 */         this.__queue.notify();
/*     */       }
/*     */       
/* 470 */       return ch;
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
/*     */   public int read(byte[] buffer) throws IOException {
/* 491 */     return read(buffer, 0, buffer.length);
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
/*     */   public int read(byte[] buffer, int offset, int length) throws IOException {
/* 514 */     if (length < 1) {
/* 515 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 519 */     synchronized (this.__queue) {
/*     */       
/* 521 */       if (length > this.__bytesAvailable) {
/* 522 */         length = this.__bytesAvailable;
/*     */       }
/*     */     } 
/*     */     int ch;
/* 526 */     if ((ch = read()) == -1) {
/* 527 */       return -1;
/*     */     }
/*     */     
/* 530 */     int off = offset;
/*     */ 
/*     */     
/*     */     do {
/* 534 */       buffer[offset++] = (byte)ch;
/*     */     }
/* 536 */     while (--length > 0 && (ch = read()) != -1);
/*     */ 
/*     */     
/* 539 */     return offset - off;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 547 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 554 */     synchronized (this.__queue) {
/*     */       
/* 556 */       if (this.__threaded) {
/* 557 */         return this.__bytesAvailable;
/*     */       }
/* 559 */       return this.__bytesAvailable + super.available();
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
/*     */   public void close() throws IOException {
/* 574 */     super.close();
/*     */     
/* 576 */     synchronized (this.__queue) {
/*     */       
/* 578 */       this.__hasReachedEOF = true;
/* 579 */       this.__isClosed = true;
/*     */       
/* 581 */       if (this.__thread != null && this.__thread.isAlive())
/*     */       {
/* 583 */         this.__thread.interrupt();
/*     */       }
/*     */       
/* 586 */       this.__queue.notifyAll();
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
/*     */   public void run() {
/*     */     try {
/* 599 */       while (!this.__isClosed) {
/*     */         int ch;
/*     */         
/*     */         try {
/* 603 */           if ((ch = __read(true)) < 0) {
/*     */             break;
/*     */           }
/*     */         }
/* 607 */         catch (InterruptedIOException e) {
/*     */           
/* 609 */           synchronized (this.__queue) {
/*     */             
/* 611 */             this.__ioException = e;
/* 612 */             this.__queue.notifyAll();
/*     */             
/*     */             try {
/* 615 */               this.__queue.wait(100L);
/*     */             }
/* 617 */             catch (InterruptedException interrupted) {
/*     */               
/* 619 */               if (this.__isClosed) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           } 
/*     */           continue;
/* 625 */         } catch (RuntimeException re) {
/*     */ 
/*     */ 
/*     */           
/* 629 */           super.close();
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */         
/* 636 */         boolean notify = false;
/*     */         
/*     */         try {
/* 639 */           notify = __processChar(ch);
/*     */         }
/* 641 */         catch (InterruptedException e) {
/*     */           
/* 643 */           if (this.__isClosed) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 649 */         if (notify) {
/* 650 */           this.__client.notifyInputListener();
/*     */         }
/*     */       }
/*     */     
/* 654 */     } catch (IOException ioe) {
/*     */       
/* 656 */       synchronized (this.__queue) {
/*     */         
/* 658 */         this.__ioException = ioe;
/*     */       } 
/* 660 */       this.__client.notifyInputListener();
/*     */     } 
/*     */     
/* 663 */     synchronized (this.__queue) {
/*     */       
/* 665 */       this.__isClosed = true;
/* 666 */       this.__hasReachedEOF = true;
/* 667 */       this.__queue.notify();
/*     */     } 
/*     */     
/* 670 */     this.__threaded = false;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\telnet\TelnetInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */