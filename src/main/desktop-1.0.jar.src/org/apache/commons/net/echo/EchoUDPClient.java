/*     */ package org.apache.commons.net.echo;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import org.apache.commons.net.discard.DiscardUDPClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EchoUDPClient
/*     */   extends DiscardUDPClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 7;
/*  46 */   private final DatagramPacket __receivePacket = new DatagramPacket(new byte[0], 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  63 */     send(data, length, host, 7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(byte[] data, InetAddress host) throws IOException {
/*  71 */     send(data, data.length, host, 7);
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
/*     */   public int receive(byte[] data, int length) throws IOException {
/*  88 */     this.__receivePacket.setData(data);
/*  89 */     this.__receivePacket.setLength(length);
/*  90 */     this._socket_.receive(this.__receivePacket);
/*  91 */     return this.__receivePacket.getLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int receive(byte[] data) throws IOException {
/* 101 */     return receive(data, data.length);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\echo\EchoUDPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */