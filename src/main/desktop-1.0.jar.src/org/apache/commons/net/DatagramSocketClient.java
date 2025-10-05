/*     */ package org.apache.commons.net;
/*     */ 
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DatagramSocketClient
/*     */ {
/*  56 */   private static final DatagramSocketFactory __DEFAULT_SOCKET_FACTORY = new DefaultDatagramSocketFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private Charset charset = Charset.defaultCharset();
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _timeout_;
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatagramSocket _socket_;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean _isOpen_;
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatagramSocketFactory _socketFactory_;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DatagramSocketClient() {
/*  84 */     this._socket_ = null;
/*  85 */     this._timeout_ = 0;
/*  86 */     this._isOpen_ = false;
/*  87 */     this._socketFactory_ = __DEFAULT_SOCKET_FACTORY;
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
/*     */   public void open() throws SocketException {
/* 104 */     this._socket_ = this._socketFactory_.createDatagramSocket();
/* 105 */     this._socket_.setSoTimeout(this._timeout_);
/* 106 */     this._isOpen_ = true;
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
/*     */   public void open(int port) throws SocketException {
/* 124 */     this._socket_ = this._socketFactory_.createDatagramSocket(port);
/* 125 */     this._socket_.setSoTimeout(this._timeout_);
/* 126 */     this._isOpen_ = true;
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
/*     */   public void open(int port, InetAddress laddr) throws SocketException {
/* 146 */     this._socket_ = this._socketFactory_.createDatagramSocket(port, laddr);
/* 147 */     this._socket_.setSoTimeout(this._timeout_);
/* 148 */     this._isOpen_ = true;
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
/*     */   public void close() {
/* 163 */     if (this._socket_ != null) {
/* 164 */       this._socket_.close();
/*     */     }
/* 166 */     this._socket_ = null;
/* 167 */     this._isOpen_ = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 178 */     return this._isOpen_;
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
/*     */   public void setDefaultTimeout(int timeout) {
/* 195 */     this._timeout_ = timeout;
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
/*     */   public int getDefaultTimeout() {
/* 208 */     return this._timeout_;
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
/*     */   public void setSoTimeout(int timeout) throws SocketException {
/* 223 */     this._socket_.setSoTimeout(timeout);
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
/*     */   public int getSoTimeout() throws SocketException {
/* 237 */     return this._socket_.getSoTimeout();
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
/*     */   public int getLocalPort() {
/* 251 */     return this._socket_.getLocalPort();
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
/*     */   public InetAddress getLocalAddress() {
/* 264 */     return this._socket_.getLocalAddress();
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
/*     */   public void setDatagramSocketFactory(DatagramSocketFactory factory) {
/* 279 */     if (factory == null) {
/* 280 */       this._socketFactory_ = __DEFAULT_SOCKET_FACTORY;
/*     */     } else {
/* 282 */       this._socketFactory_ = factory;
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
/*     */   public String getCharsetName() {
/* 294 */     return this.charset.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 304 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 314 */     this.charset = charset;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\DatagramSocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */