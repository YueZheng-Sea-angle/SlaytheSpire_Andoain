/*     */ package org.apache.commons.net.tftp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.net.io.FromNetASCIIOutputStream;
/*     */ import org.apache.commons.net.io.ToNetASCIIInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TFTPClient
/*     */   extends TFTP
/*     */ {
/*     */   public static final int DEFAULT_MAX_TIMEOUTS = 5;
/*  75 */   private int __maxTimeouts = 5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTimeouts(int numTimeouts) {
/*  90 */     if (numTimeouts < 1) {
/*  91 */       this.__maxTimeouts = 1;
/*     */     } else {
/*  93 */       this.__maxTimeouts = numTimeouts;
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
/*     */   public int getMaxTimeouts() {
/* 105 */     return this.__maxTimeouts;
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
/*     */   public int receiveFile(String filename, int mode, OutputStream output, InetAddress host, int port) throws IOException {
/*     */     FromNetASCIIOutputStream fromNetASCIIOutputStream;
/* 130 */     TFTPPacket received = null;
/*     */ 
/*     */     
/* 133 */     TFTPAckPacket ack = new TFTPAckPacket(host, port, 0);
/*     */     
/* 135 */     beginBufferedOps();
/*     */     
/* 137 */     int bytesRead = 0, hostPort = bytesRead, lastBlock = hostPort, dataLength = lastBlock;
/* 138 */     int block = 1;
/*     */     
/* 140 */     if (mode == 0) {
/* 141 */       fromNetASCIIOutputStream = new FromNetASCIIOutputStream(output);
/*     */     }
/*     */     
/* 144 */     TFTPPacket sent = new TFTPReadRequestPacket(host, port, filename, mode);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     do {
/* 150 */       bufferedSend(sent);
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 155 */         int timeouts = 0;
/*     */ 
/*     */         
/*     */         try {
/* 159 */           received = bufferedReceive();
/*     */         
/*     */         }
/* 162 */         catch (SocketException e) {
/*     */           
/* 164 */           if (++timeouts >= this.__maxTimeouts) {
/*     */             
/* 166 */             endBufferedOps();
/* 167 */             throw new IOException("Connection timed out.");
/*     */           } 
/*     */           
/*     */           break;
/* 171 */         } catch (InterruptedIOException e) {
/*     */           
/* 173 */           if (++timeouts >= this.__maxTimeouts) {
/*     */             
/* 175 */             endBufferedOps();
/* 176 */             throw new IOException("Connection timed out.");
/*     */           } 
/*     */           
/*     */           break;
/* 180 */         } catch (TFTPPacketException e) {
/*     */           
/* 182 */           endBufferedOps();
/* 183 */           throw new IOException("Bad packet: " + e.getMessage());
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 189 */         if (lastBlock == 0) {
/*     */           
/* 191 */           hostPort = received.getPort();
/* 192 */           ack.setPort(hostPort);
/* 193 */           if (!host.equals(received.getAddress())) {
/*     */             
/* 195 */             host = received.getAddress();
/* 196 */             ack.setAddress(host);
/* 197 */             sent.setAddress(host);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 203 */         if (host.equals(received.getAddress()) && received.getPort() == hostPort) {
/*     */           TFTPErrorPacket error;
/*     */           
/*     */           TFTPDataPacket data;
/* 207 */           switch (received.getType()) {
/*     */             
/*     */             case 5:
/* 210 */               error = (TFTPErrorPacket)received;
/* 211 */               endBufferedOps();
/* 212 */               throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
/*     */             
/*     */             case 3:
/* 215 */               data = (TFTPDataPacket)received;
/* 216 */               dataLength = data.getDataLength();
/*     */               
/* 218 */               lastBlock = data.getBlockNumber();
/*     */               
/* 220 */               if (lastBlock == block) {
/*     */ 
/*     */                 
/*     */                 try {
/* 224 */                   fromNetASCIIOutputStream.write(data.getData(), data.getDataOffset(), dataLength);
/*     */                 
/*     */                 }
/* 227 */                 catch (IOException e) {
/*     */                   
/* 229 */                   error = new TFTPErrorPacket(host, hostPort, 3, "File write failed.");
/*     */ 
/*     */                   
/* 232 */                   bufferedSend(error);
/* 233 */                   endBufferedOps();
/* 234 */                   throw e;
/*     */                 } 
/* 236 */                 block++;
/* 237 */                 if (block > 65535)
/*     */                 {
/*     */                   
/* 240 */                   block = 0;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 break;
/*     */               } 
/*     */               
/* 247 */               discardPackets();
/*     */               
/* 249 */               if (lastBlock == ((block == 0) ? 65535 : (block - 1))) {
/*     */                 break;
/*     */               }
/*     */               continue;
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             default:
/* 258 */               endBufferedOps();
/* 259 */               throw new IOException("Received unexpected packet type.");
/*     */           } 
/*     */ 
/*     */         
/*     */         } else {
/* 264 */           TFTPErrorPacket error = new TFTPErrorPacket(received.getAddress(), received.getPort(), 5, "Unexpected host or port.");
/*     */ 
/*     */ 
/*     */           
/* 268 */           bufferedSend(error);
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 277 */         ack.setBlockNumber(lastBlock);
/* 278 */         sent = ack;
/* 279 */         bytesRead += dataLength;
/*     */         break;
/*     */       } 
/* 282 */     } while (dataLength == 512);
/*     */     
/* 284 */     bufferedSend(sent);
/* 285 */     endBufferedOps();
/*     */     
/* 287 */     return bytesRead;
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
/*     */   public int receiveFile(String filename, int mode, OutputStream output, String hostname, int port) throws UnknownHostException, IOException {
/* 313 */     return receiveFile(filename, mode, output, InetAddress.getByName(hostname), port);
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
/*     */   public int receiveFile(String filename, int mode, OutputStream output, InetAddress host) throws IOException {
/* 333 */     return receiveFile(filename, mode, output, host, 69);
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
/*     */   public int receiveFile(String filename, int mode, OutputStream output, String hostname) throws UnknownHostException, IOException {
/* 352 */     return receiveFile(filename, mode, output, InetAddress.getByName(hostname), 69);
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
/*     */   public void sendFile(String filename, int mode, InputStream input, InetAddress host, int port) throws IOException {
/*     */     ToNetASCIIInputStream toNetASCIIInputStream;
/* 378 */     TFTPPacket received = null;
/*     */     
/* 380 */     TFTPDataPacket data = new TFTPDataPacket(host, port, 0, this._sendBuffer, 4, 0);
/*     */ 
/*     */ 
/*     */     
/* 384 */     boolean justStarted = true;
/*     */     
/* 386 */     beginBufferedOps();
/*     */     
/* 388 */     int totalThisPacket = 0, bytesRead = totalThisPacket, hostPort = bytesRead, lastBlock = hostPort, dataLength = lastBlock;
/* 389 */     int block = 0;
/* 390 */     boolean lastAckWait = false;
/*     */     
/* 392 */     if (mode == 0) {
/* 393 */       toNetASCIIInputStream = new ToNetASCIIInputStream(input);
/*     */     }
/*     */     
/* 396 */     TFTPPacket sent = new TFTPWriteRequestPacket(host, port, filename, mode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 404 */       bufferedSend(sent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       label52: while (true) {
/* 412 */         int timeouts = 0;
/*     */ 
/*     */         
/*     */         try {
/* 416 */           received = bufferedReceive();
/*     */         
/*     */         }
/* 419 */         catch (SocketException e) {
/*     */           
/* 421 */           if (++timeouts >= this.__maxTimeouts) {
/*     */             
/* 423 */             endBufferedOps();
/* 424 */             throw new IOException("Connection timed out.");
/*     */           } 
/*     */           
/*     */           break label52;
/* 428 */         } catch (InterruptedIOException e) {
/*     */           
/* 430 */           if (++timeouts >= this.__maxTimeouts) {
/*     */             
/* 432 */             endBufferedOps();
/* 433 */             throw new IOException("Connection timed out.");
/*     */           } 
/*     */           
/*     */           break label52;
/* 437 */         } catch (TFTPPacketException e) {
/*     */           
/* 439 */           endBufferedOps();
/* 440 */           throw new IOException("Bad packet: " + e.getMessage());
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 448 */         if (justStarted) {
/*     */           
/* 450 */           justStarted = false;
/* 451 */           hostPort = received.getPort();
/* 452 */           data.setPort(hostPort);
/* 453 */           if (!host.equals(received.getAddress())) {
/*     */             
/* 455 */             host = received.getAddress();
/* 456 */             data.setAddress(host);
/* 457 */             sent.setAddress(host);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 463 */         if (host.equals(received.getAddress())) { if (received.getPort() == hostPort) {
/*     */             TFTPErrorPacket error;
/*     */             
/*     */             TFTPAckPacket ack;
/* 467 */             switch (received.getType()) {
/*     */               
/*     */               case 5:
/* 470 */                 error = (TFTPErrorPacket)received;
/* 471 */                 endBufferedOps();
/* 472 */                 throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
/*     */               
/*     */               case 4:
/* 475 */                 ack = (TFTPAckPacket)received;
/*     */                 
/* 477 */                 lastBlock = ack.getBlockNumber();
/*     */                 
/* 479 */                 if (lastBlock == block) {
/*     */                   
/* 481 */                   block++;
/* 482 */                   if (block > 65535)
/*     */                   {
/*     */                     
/* 485 */                     block = 0;
/*     */                   }
/* 487 */                   if (lastAckWait) {
/*     */                     break;
/*     */                   }
/*     */ 
/*     */ 
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */ 
/*     */                 
/* 497 */                 discardPackets();
/*     */                 continue;
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               default:
/* 504 */                 endBufferedOps();
/* 505 */                 throw new IOException("Received unexpected packet type.");
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 526 */             dataLength = 512;
/* 527 */             int offset = 4;
/* 528 */             totalThisPacket = 0;
/*     */             
/* 530 */             while (dataLength > 0 && (bytesRead = toNetASCIIInputStream.read(this._sendBuffer, offset, dataLength)) > 0) {
/*     */               
/* 532 */               offset += bytesRead;
/* 533 */               dataLength -= bytesRead;
/* 534 */               totalThisPacket += bytesRead;
/*     */             } 
/*     */             
/* 537 */             if (totalThisPacket < 512)
/*     */             {
/* 539 */               lastAckWait = true;
/*     */             }
/* 541 */             data.setBlockNumber(block);
/* 542 */             data.setData(this._sendBuffer, 4, totalThisPacket);
/* 543 */             sent = data; break label52;
/*     */           }  continue; }
/* 545 */          TFTPErrorPacket tFTPErrorPacket = new TFTPErrorPacket(received.getAddress(), received.getPort(), 5, "Unexpected host or port."); bufferedSend(tFTPErrorPacket); if (totalThisPacket <= 0 && !lastAckWait)
/*     */           break; 
/*     */       } 
/*     */       break;
/*     */     } 
/* 550 */     endBufferedOps();
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
/*     */   public void sendFile(String filename, int mode, InputStream input, String hostname, int port) throws UnknownHostException, IOException {
/* 576 */     sendFile(filename, mode, input, InetAddress.getByName(hostname), port);
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
/*     */   public void sendFile(String filename, int mode, InputStream input, InetAddress host) throws IOException {
/* 596 */     sendFile(filename, mode, input, host, 69);
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
/*     */   public void sendFile(String filename, int mode, InputStream input, String hostname) throws UnknownHostException, IOException {
/* 615 */     sendFile(filename, mode, input, InetAddress.getByName(hostname), 69);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\tftp\TFTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */