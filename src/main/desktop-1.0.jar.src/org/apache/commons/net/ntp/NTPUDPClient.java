/*     */ package org.apache.commons.net.ntp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
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
/*     */ public final class NTPUDPClient
/*     */   extends DatagramSocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 123;
/*  46 */   private int _version = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeInfo getTime(InetAddress host, int port) throws IOException {
/*  63 */     if (!isOpen())
/*     */     {
/*  65 */       open();
/*     */     }
/*     */     
/*  68 */     NtpV3Packet message = new NtpV3Impl();
/*  69 */     message.setMode(3);
/*  70 */     message.setVersion(this._version);
/*  71 */     DatagramPacket sendPacket = message.getDatagramPacket();
/*  72 */     sendPacket.setAddress(host);
/*  73 */     sendPacket.setPort(port);
/*     */     
/*  75 */     NtpV3Packet recMessage = new NtpV3Impl();
/*  76 */     DatagramPacket receivePacket = recMessage.getDatagramPacket();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     TimeStamp now = TimeStamp.getCurrentTime();
/*     */ 
/*     */ 
/*     */     
/*  88 */     message.setTransmitTime(now);
/*     */     
/*  90 */     this._socket_.send(sendPacket);
/*  91 */     this._socket_.receive(receivePacket);
/*     */     
/*  93 */     long returnTime = System.currentTimeMillis();
/*     */     
/*  95 */     TimeInfo info = new TimeInfo(recMessage, returnTime, false);
/*     */     
/*  97 */     return info;
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
/*     */   public TimeInfo getTime(InetAddress host) throws IOException {
/* 113 */     return getTime(host, 123);
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
/*     */   public int getVersion() {
/* 125 */     return this._version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(int version) {
/* 136 */     this._version = version;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ntp\NTPUDPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */