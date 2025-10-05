/*     */ package org.apache.commons.net.discard;
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
/*     */ 
/*     */ 
/*     */ public class DiscardUDPClient
/*     */   extends DatagramSocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 9;
/*  48 */   DatagramPacket _sendPacket = new DatagramPacket(new byte[0], 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(byte[] data, int length, InetAddress host, int port) throws IOException {
/*  66 */     this._sendPacket.setData(data);
/*  67 */     this._sendPacket.setLength(length);
/*  68 */     this._sendPacket.setAddress(host);
/*  69 */     this._sendPacket.setPort(port);
/*  70 */     this._socket_.send(this._sendPacket);
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
/*     */   public void send(byte[] data, int length, InetAddress host) throws IOException {
/*  86 */     send(data, length, host, 9);
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
/*     */   public void send(byte[] data, InetAddress host) throws IOException {
/* 100 */     send(data, data.length, host, 9);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\discard\DiscardUDPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */