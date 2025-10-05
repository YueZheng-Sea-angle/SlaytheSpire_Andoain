/*     */ package org.apache.commons.net.tftp;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TFTPAckPacket
/*     */   extends TFTPPacket
/*     */ {
/*     */   int _blockNumber;
/*     */   
/*     */   public TFTPAckPacket(InetAddress destination, int port, int blockNumber) {
/*  59 */     super(4, destination, port);
/*  60 */     this._blockNumber = blockNumber;
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
/*     */   TFTPAckPacket(DatagramPacket datagram) throws TFTPPacketException {
/*  74 */     super(4, datagram.getAddress(), datagram.getPort());
/*     */ 
/*     */ 
/*     */     
/*  78 */     byte[] data = datagram.getData();
/*     */     
/*  80 */     if (getType() != data[1]) {
/*  81 */       throw new TFTPPacketException("TFTP operator code does not match type.");
/*     */     }
/*     */     
/*  84 */     this._blockNumber = (data[2] & 0xFF) << 8 | data[3] & 0xFF;
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
/*     */   DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
/* 101 */     data[0] = 0;
/* 102 */     data[1] = (byte)this._type;
/* 103 */     data[2] = (byte)((this._blockNumber & 0xFFFF) >> 8);
/* 104 */     data[3] = (byte)(this._blockNumber & 0xFF);
/*     */     
/* 106 */     datagram.setAddress(this._address);
/* 107 */     datagram.setPort(this._port);
/* 108 */     datagram.setData(data);
/* 109 */     datagram.setLength(4);
/*     */     
/* 111 */     return datagram;
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
/*     */   public DatagramPacket newDatagram() {
/* 131 */     byte[] data = new byte[4];
/* 132 */     data[0] = 0;
/* 133 */     data[1] = (byte)this._type;
/* 134 */     data[2] = (byte)((this._blockNumber & 0xFFFF) >> 8);
/* 135 */     data[3] = (byte)(this._blockNumber & 0xFF);
/*     */     
/* 137 */     return new DatagramPacket(data, data.length, this._address, this._port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockNumber() {
/* 148 */     return this._blockNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockNumber(int blockNumber) {
/* 159 */     this._blockNumber = blockNumber;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\tftp\TFTPAckPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */