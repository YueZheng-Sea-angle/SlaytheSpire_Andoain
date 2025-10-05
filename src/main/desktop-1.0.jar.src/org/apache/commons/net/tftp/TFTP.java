/*     */ package org.apache.commons.net.tftp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.SocketException;
/*     */ import org.apache.commons.net.DatagramSocketClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TFTP
/*     */   extends DatagramSocketClient
/*     */ {
/*     */   public static final int ASCII_MODE = 0;
/*     */   public static final int NETASCII_MODE = 0;
/*     */   public static final int BINARY_MODE = 1;
/*     */   public static final int IMAGE_MODE = 1;
/*     */   public static final int OCTET_MODE = 1;
/*     */   public static final int DEFAULT_TIMEOUT = 5000;
/*     */   public static final int DEFAULT_PORT = 69;
/*     */   static final int PACKET_SIZE = 516;
/*     */   private byte[] __receiveBuffer;
/*     */   private DatagramPacket __receiveDatagram;
/*     */   private DatagramPacket __sendDatagram;
/*     */   byte[] _sendBuffer;
/*     */   
/*     */   public static final String getModeName(int mode) {
/* 120 */     return TFTPRequestPacket._modeStrings[mode];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TFTP() {
/* 129 */     setDefaultTimeout(5000);
/* 130 */     this.__receiveBuffer = null;
/* 131 */     this.__receiveDatagram = null;
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
/*     */   public final void discardPackets() throws IOException {
/* 146 */     DatagramPacket datagram = new DatagramPacket(new byte[516], 516);
/*     */     
/* 148 */     int to = getSoTimeout();
/* 149 */     setSoTimeout(1);
/*     */ 
/*     */     
/*     */     try {
/*     */       while (true) {
/* 154 */         this._socket_.receive(datagram);
/*     */       }
/*     */     }
/* 157 */     catch (SocketException e) {
/*     */ 
/*     */     
/*     */     }
/* 161 */     catch (InterruptedIOException e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     setSoTimeout(to);
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
/*     */   
/*     */   public final TFTPPacket bufferedReceive() throws IOException, InterruptedIOException, SocketException, TFTPPacketException {
/* 200 */     this.__receiveDatagram.setData(this.__receiveBuffer);
/* 201 */     this.__receiveDatagram.setLength(this.__receiveBuffer.length);
/* 202 */     this._socket_.receive(this.__receiveDatagram);
/*     */     
/* 204 */     return TFTPPacket.newTFTPPacket(this.__receiveDatagram);
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
/*     */   public final void bufferedSend(TFTPPacket packet) throws IOException {
/* 227 */     this._socket_.send(packet._newDatagram(this.__sendDatagram, this._sendBuffer));
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
/*     */   public final void beginBufferedOps() {
/* 241 */     this.__receiveBuffer = new byte[516];
/* 242 */     this.__receiveDatagram = new DatagramPacket(this.__receiveBuffer, this.__receiveBuffer.length);
/*     */     
/* 244 */     this._sendBuffer = new byte[516];
/* 245 */     this.__sendDatagram = new DatagramPacket(this._sendBuffer, this._sendBuffer.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endBufferedOps() {
/* 254 */     this.__receiveBuffer = null;
/* 255 */     this.__receiveDatagram = null;
/* 256 */     this._sendBuffer = null;
/* 257 */     this.__sendDatagram = null;
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
/*     */   public final void send(TFTPPacket packet) throws IOException {
/* 269 */     this._socket_.send(packet.newDatagram());
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
/*     */   public final TFTPPacket receive() throws IOException, InterruptedIOException, SocketException, TFTPPacketException {
/* 293 */     DatagramPacket packet = new DatagramPacket(new byte[516], 516);
/*     */     
/* 295 */     this._socket_.receive(packet);
/*     */     
/* 297 */     return TFTPPacket.newTFTPPacket(packet);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\tftp\TFTP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */