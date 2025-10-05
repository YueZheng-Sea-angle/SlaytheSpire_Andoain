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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TFTPDataPacket
/*     */   extends TFTPPacket
/*     */ {
/*     */   public static final int MAX_DATA_LENGTH = 512;
/*     */   public static final int MIN_DATA_LENGTH = 0;
/*     */   int _blockNumber;
/*     */   int _length;
/*     */   int _offset;
/*     */   byte[] _data;
/*     */   
/*     */   public TFTPDataPacket(InetAddress destination, int port, int blockNumber, byte[] data, int offset, int length) {
/*  82 */     super(3, destination, port);
/*     */     
/*  84 */     this._blockNumber = blockNumber;
/*  85 */     this._data = data;
/*  86 */     this._offset = offset;
/*     */     
/*  88 */     if (length > 512) {
/*  89 */       this._length = 512;
/*     */     } else {
/*  91 */       this._length = length;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TFTPDataPacket(InetAddress destination, int port, int blockNumber, byte[] data) {
/*  98 */     this(destination, port, blockNumber, data, 0, data.length);
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
/*     */   TFTPDataPacket(DatagramPacket datagram) throws TFTPPacketException {
/* 113 */     super(3, datagram.getAddress(), datagram.getPort());
/*     */     
/* 115 */     this._data = datagram.getData();
/* 116 */     this._offset = 4;
/*     */     
/* 118 */     if (getType() != this._data[1]) {
/* 119 */       throw new TFTPPacketException("TFTP operator code does not match type.");
/*     */     }
/*     */     
/* 122 */     this._blockNumber = (this._data[2] & 0xFF) << 8 | this._data[3] & 0xFF;
/*     */     
/* 124 */     this._length = datagram.getLength() - 4;
/*     */     
/* 126 */     if (this._length > 512) {
/* 127 */       this._length = 512;
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
/*     */   DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
/* 145 */     data[0] = 0;
/* 146 */     data[1] = (byte)this._type;
/* 147 */     data[2] = (byte)((this._blockNumber & 0xFFFF) >> 8);
/* 148 */     data[3] = (byte)(this._blockNumber & 0xFF);
/*     */ 
/*     */     
/* 151 */     if (data != this._data) {
/* 152 */       System.arraycopy(this._data, this._offset, data, 4, this._length);
/*     */     }
/*     */     
/* 155 */     datagram.setAddress(this._address);
/* 156 */     datagram.setPort(this._port);
/* 157 */     datagram.setData(data);
/* 158 */     datagram.setLength(this._length + 4);
/*     */     
/* 160 */     return datagram;
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
/* 180 */     byte[] data = new byte[this._length + 4];
/* 181 */     data[0] = 0;
/* 182 */     data[1] = (byte)this._type;
/* 183 */     data[2] = (byte)((this._blockNumber & 0xFFFF) >> 8);
/* 184 */     data[3] = (byte)(this._blockNumber & 0xFF);
/*     */     
/* 186 */     System.arraycopy(this._data, this._offset, data, 4, this._length);
/*     */     
/* 188 */     return new DatagramPacket(data, this._length + 4, this._address, this._port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockNumber() {
/* 198 */     return this._blockNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockNumber(int blockNumber) {
/* 206 */     this._blockNumber = blockNumber;
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
/*     */   public void setData(byte[] data, int offset, int length) {
/* 218 */     this._data = data;
/* 219 */     this._offset = offset;
/* 220 */     this._length = length;
/*     */     
/* 222 */     if (length > 512) {
/* 223 */       this._length = 512;
/*     */     } else {
/* 225 */       this._length = length;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDataLength() {
/* 236 */     return this._length;
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
/*     */   public int getDataOffset() {
/* 248 */     return this._offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getData() {
/* 258 */     return this._data;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\tftp\TFTPDataPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */