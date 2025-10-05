/*     */ package org.apache.commons.net.tftp;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TFTPRequestPacket
/*     */   extends TFTPPacket
/*     */ {
/*  56 */   static final String[] _modeStrings = new String[] { "netascii", "octet" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static final byte[][] _modeBytes = new byte[][] { { 110, 101, 116, 97, 115, 99, 105, 105, 0 }, { 111, 99, 116, 101, 116, 0 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int _mode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String _filename;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TFTPRequestPacket(InetAddress destination, int port, int type, String filename, int mode) {
/*  90 */     super(type, destination, port);
/*     */     
/*  92 */     this._filename = filename;
/*  93 */     this._mode = mode;
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
/*     */   TFTPRequestPacket(int type, DatagramPacket datagram) throws TFTPPacketException {
/* 110 */     super(type, datagram.getAddress(), datagram.getPort());
/*     */     
/* 112 */     byte[] data = datagram.getData();
/*     */     
/* 114 */     if (getType() != data[1]) {
/* 115 */       throw new TFTPPacketException("TFTP operator code does not match type.");
/*     */     }
/*     */     
/* 118 */     StringBuilder buffer = new StringBuilder();
/*     */     
/* 120 */     int index = 2;
/* 121 */     int length = datagram.getLength();
/*     */     
/* 123 */     while (index < length && data[index] != 0) {
/*     */       
/* 125 */       buffer.append((char)data[index]);
/* 126 */       index++;
/*     */     } 
/*     */     
/* 129 */     this._filename = buffer.toString();
/*     */     
/* 131 */     if (index >= length) {
/* 132 */       throw new TFTPPacketException("Bad filename and mode format.");
/*     */     }
/*     */     
/* 135 */     buffer.setLength(0);
/* 136 */     index++;
/* 137 */     while (index < length && data[index] != 0) {
/*     */       
/* 139 */       buffer.append((char)data[index]);
/* 140 */       index++;
/*     */     } 
/*     */     
/* 143 */     String modeString = buffer.toString().toLowerCase(Locale.ENGLISH);
/* 144 */     length = _modeStrings.length;
/*     */     
/* 146 */     int mode = 0;
/* 147 */     for (index = 0; index < length; index++) {
/*     */       
/* 149 */       if (modeString.equals(_modeStrings[index])) {
/*     */         
/* 151 */         mode = index;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 156 */     this._mode = mode;
/*     */     
/* 158 */     if (index >= length)
/*     */     {
/* 160 */       throw new TFTPPacketException("Unrecognized TFTP transfer mode: " + modeString);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
/* 184 */     int fileLength = this._filename.length();
/* 185 */     int modeLength = (_modeBytes[this._mode]).length;
/*     */     
/* 187 */     data[0] = 0;
/* 188 */     data[1] = (byte)this._type;
/* 189 */     System.arraycopy(this._filename.getBytes(), 0, data, 2, fileLength);
/* 190 */     data[fileLength + 2] = 0;
/* 191 */     System.arraycopy(_modeBytes[this._mode], 0, data, fileLength + 3, modeLength);
/*     */ 
/*     */     
/* 194 */     datagram.setAddress(this._address);
/* 195 */     datagram.setPort(this._port);
/* 196 */     datagram.setData(data);
/* 197 */     datagram.setLength(fileLength + modeLength + 3);
/*     */     
/* 199 */     return datagram;
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
/*     */   public final DatagramPacket newDatagram() {
/* 219 */     int fileLength = this._filename.length();
/* 220 */     int modeLength = (_modeBytes[this._mode]).length;
/*     */     
/* 222 */     byte[] data = new byte[fileLength + modeLength + 4];
/* 223 */     data[0] = 0;
/* 224 */     data[1] = (byte)this._type;
/* 225 */     System.arraycopy(this._filename.getBytes(), 0, data, 2, fileLength);
/* 226 */     data[fileLength + 2] = 0;
/* 227 */     System.arraycopy(_modeBytes[this._mode], 0, data, fileLength + 3, modeLength);
/*     */ 
/*     */     
/* 230 */     return new DatagramPacket(data, data.length, this._address, this._port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMode() {
/* 240 */     return this._mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getFilename() {
/* 250 */     return this._filename;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\tftp\TFTPRequestPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */