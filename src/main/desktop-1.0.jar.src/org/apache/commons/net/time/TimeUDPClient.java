/*     */ package org.apache.commons.net.time;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Date;
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
/*     */ public final class TimeUDPClient
/*     */   extends DatagramSocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 37;
/*     */   public static final long SECONDS_1900_TO_1970 = 2208988800L;
/*  58 */   private final byte[] __dummyData = new byte[1];
/*  59 */   private final byte[] __timeData = new byte[4];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTime(InetAddress host, int port) throws IOException {
/*  79 */     DatagramPacket sendPacket = new DatagramPacket(this.__dummyData, this.__dummyData.length, host, port);
/*     */     
/*  81 */     DatagramPacket receivePacket = new DatagramPacket(this.__timeData, this.__timeData.length);
/*     */     
/*  83 */     this._socket_.send(sendPacket);
/*  84 */     this._socket_.receive(receivePacket);
/*     */     
/*  86 */     long time = 0L;
/*  87 */     time |= ((this.__timeData[0] & 0xFF) << 24) & 0xFFFFFFFFL;
/*  88 */     time |= ((this.__timeData[1] & 0xFF) << 16) & 0xFFFFFFFFL;
/*  89 */     time |= ((this.__timeData[2] & 0xFF) << 8) & 0xFFFFFFFFL;
/*  90 */     time |= (this.__timeData[3] & 0xFF) & 0xFFFFFFFFL;
/*     */     
/*  92 */     return time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTime(InetAddress host) throws IOException {
/* 102 */     return getTime(host, 37);
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
/*     */   public Date getDate(InetAddress host, int port) throws IOException {
/* 118 */     return new Date((getTime(host, port) - 2208988800L) * 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate(InetAddress host) throws IOException {
/* 129 */     return new Date((getTime(host, 37) - 2208988800L) * 1000L);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\time\TimeUDPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */