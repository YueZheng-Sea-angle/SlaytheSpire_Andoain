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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TFTPErrorPacket
/*     */   extends TFTPPacket
/*     */ {
/*     */   public static final int UNDEFINED = 0;
/*     */   public static final int FILE_NOT_FOUND = 1;
/*     */   public static final int ACCESS_VIOLATION = 2;
/*     */   public static final int OUT_OF_SPACE = 3;
/*     */   public static final int ILLEGAL_OPERATION = 4;
/*     */   public static final int UNKNOWN_TID = 5;
/*     */   public static final int FILE_EXISTS = 6;
/*     */   public static final int NO_SUCH_USER = 7;
/*     */   int _error;
/*     */   String _message;
/*     */   
/*     */   public TFTPErrorPacket(InetAddress destination, int port, int error, String message) {
/*  90 */     super(5, destination, port);
/*     */     
/*  92 */     this._error = error;
/*  93 */     this._message = message;
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
/*     */   TFTPErrorPacket(DatagramPacket datagram) throws TFTPPacketException {
/* 107 */     super(5, datagram.getAddress(), datagram.getPort());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     byte[] data = datagram.getData();
/* 113 */     int length = datagram.getLength();
/*     */     
/* 115 */     if (getType() != data[1]) {
/* 116 */       throw new TFTPPacketException("TFTP operator code does not match type.");
/*     */     }
/*     */     
/* 119 */     this._error = (data[2] & 0xFF) << 8 | data[3] & 0xFF;
/*     */     
/* 121 */     if (length < 5) {
/* 122 */       throw new TFTPPacketException("Bad error packet. No message.");
/*     */     }
/*     */     
/* 125 */     int index = 4;
/* 126 */     StringBuilder buffer = new StringBuilder();
/*     */     
/* 128 */     while (index < length && data[index] != 0) {
/*     */       
/* 130 */       buffer.append((char)data[index]);
/* 131 */       index++;
/*     */     } 
/*     */     
/* 134 */     this._message = buffer.toString();
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
/*     */   DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
/* 153 */     int length = this._message.length();
/*     */     
/* 155 */     data[0] = 0;
/* 156 */     data[1] = (byte)this._type;
/* 157 */     data[2] = (byte)((this._error & 0xFFFF) >> 8);
/* 158 */     data[3] = (byte)(this._error & 0xFF);
/*     */     
/* 160 */     System.arraycopy(this._message.getBytes(), 0, data, 4, length);
/*     */     
/* 162 */     data[length + 4] = 0;
/*     */     
/* 164 */     datagram.setAddress(this._address);
/* 165 */     datagram.setPort(this._port);
/* 166 */     datagram.setData(data);
/* 167 */     datagram.setLength(length + 4);
/*     */     
/* 169 */     return datagram;
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
/*     */   public DatagramPacket newDatagram() {
/* 191 */     int length = this._message.length();
/*     */     
/* 193 */     byte[] data = new byte[length + 5];
/* 194 */     data[0] = 0;
/* 195 */     data[1] = (byte)this._type;
/* 196 */     data[2] = (byte)((this._error & 0xFFFF) >> 8);
/* 197 */     data[3] = (byte)(this._error & 0xFF);
/*     */     
/* 199 */     System.arraycopy(this._message.getBytes(), 0, data, 4, length);
/*     */     
/* 201 */     data[length + 4] = 0;
/*     */     
/* 203 */     return new DatagramPacket(data, data.length, this._address, this._port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getError() {
/* 214 */     return this._error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 225 */     return this._message;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\tftp\TFTPErrorPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */