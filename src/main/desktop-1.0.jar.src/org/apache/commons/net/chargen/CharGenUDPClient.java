/*     */ package org.apache.commons.net.chargen;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharGenUDPClient
/*     */   extends DatagramSocketClient
/*     */ {
/*     */   public static final int SYSTAT_PORT = 11;
/*     */   public static final int NETSTAT_PORT = 15;
/*     */   public static final int QUOTE_OF_DAY_PORT = 17;
/*     */   public static final int CHARGEN_PORT = 19;
/*     */   public static final int DEFAULT_PORT = 19;
/*  77 */   private final byte[] __receiveData = new byte[512];
/*  78 */   private final DatagramPacket __receivePacket = new DatagramPacket(this.__receiveData, this.__receiveData.length);
/*  79 */   private final DatagramPacket __sendPacket = new DatagramPacket(new byte[0], 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(InetAddress host, int port) throws IOException {
/*  94 */     this.__sendPacket.setAddress(host);
/*  95 */     this.__sendPacket.setPort(port);
/*  96 */     this._socket_.send(this.__sendPacket);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(InetAddress host) throws IOException {
/* 105 */     send(host, 19);
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
/*     */   public byte[] receive() throws IOException {
/* 121 */     this._socket_.receive(this.__receivePacket);
/*     */     int length;
/* 123 */     byte[] result = new byte[length = this.__receivePacket.getLength()];
/* 124 */     System.arraycopy(this.__receiveData, 0, result, 0, length);
/*     */     
/* 126 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\chargen\CharGenUDPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */