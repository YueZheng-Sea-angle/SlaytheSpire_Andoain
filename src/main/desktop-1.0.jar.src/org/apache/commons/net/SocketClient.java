/*     */ package org.apache.commons.net;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.SocketFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SocketClient
/*     */ {
/*     */   public static final String NETASCII_EOL = "\r\n";
/*  64 */   private static final SocketFactory __DEFAULT_SOCKET_FACTORY = SocketFactory.getDefault();
/*     */ 
/*     */ 
/*     */   
/*  68 */   private static final ServerSocketFactory __DEFAULT_SERVER_SOCKET_FACTORY = ServerSocketFactory.getDefault();
/*     */ 
/*     */ 
/*     */   
/*     */   private ProtocolCommandSupport __commandSupport;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int _timeout_;
/*     */ 
/*     */   
/*     */   protected Socket _socket_;
/*     */ 
/*     */   
/*     */   protected String _hostname_;
/*     */ 
/*     */   
/*     */   protected int _defaultPort_;
/*     */ 
/*     */   
/*     */   protected InputStream _input_;
/*     */ 
/*     */   
/*     */   protected OutputStream _output_;
/*     */ 
/*     */   
/*     */   protected SocketFactory _socketFactory_;
/*     */ 
/*     */   
/*     */   protected ServerSocketFactory _serverSocketFactory_;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 0;
/*     */ 
/*     */   
/* 103 */   protected int connectTimeout = 0;
/*     */ 
/*     */   
/* 106 */   private int receiveBufferSize = -1;
/*     */ 
/*     */   
/* 109 */   private int sendBufferSize = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private Proxy connProxy;
/*     */ 
/*     */ 
/*     */   
/* 117 */   private Charset charset = Charset.defaultCharset();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketClient() {
/* 128 */     this._socket_ = null;
/* 129 */     this._hostname_ = null;
/* 130 */     this._input_ = null;
/* 131 */     this._output_ = null;
/* 132 */     this._timeout_ = 0;
/* 133 */     this._defaultPort_ = 0;
/* 134 */     this._socketFactory_ = __DEFAULT_SOCKET_FACTORY;
/* 135 */     this._serverSocketFactory_ = __DEFAULT_SERVER_SOCKET_FACTORY;
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
/*     */   protected void _connectAction_() throws IOException {
/* 158 */     this._socket_.setSoTimeout(this._timeout_);
/* 159 */     this._input_ = this._socket_.getInputStream();
/* 160 */     this._output_ = this._socket_.getOutputStream();
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
/*     */   public void connect(InetAddress host, int port) throws SocketException, IOException {
/* 180 */     this._hostname_ = null;
/* 181 */     this._socket_ = this._socketFactory_.createSocket();
/* 182 */     if (this.receiveBufferSize != -1) {
/* 183 */       this._socket_.setReceiveBufferSize(this.receiveBufferSize);
/*     */     }
/* 185 */     if (this.sendBufferSize != -1) {
/* 186 */       this._socket_.setSendBufferSize(this.sendBufferSize);
/*     */     }
/* 188 */     this._socket_.connect(new InetSocketAddress(host, port), this.connectTimeout);
/* 189 */     _connectAction_();
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
/*     */   public void connect(String hostname, int port) throws SocketException, IOException {
/* 209 */     connect(InetAddress.getByName(hostname), port);
/* 210 */     this._hostname_ = hostname;
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
/*     */   public void connect(InetAddress host, int port, InetAddress localAddr, int localPort) throws SocketException, IOException {
/* 233 */     this._hostname_ = null;
/* 234 */     this._socket_ = this._socketFactory_.createSocket();
/* 235 */     if (this.receiveBufferSize != -1) {
/* 236 */       this._socket_.setReceiveBufferSize(this.receiveBufferSize);
/*     */     }
/* 238 */     if (this.sendBufferSize != -1) {
/* 239 */       this._socket_.setSendBufferSize(this.sendBufferSize);
/*     */     }
/* 241 */     this._socket_.bind(new InetSocketAddress(localAddr, localPort));
/* 242 */     this._socket_.connect(new InetSocketAddress(host, port), this.connectTimeout);
/* 243 */     _connectAction_();
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
/*     */   
/*     */   public void connect(String hostname, int port, InetAddress localAddr, int localPort) throws SocketException, IOException {
/* 267 */     connect(InetAddress.getByName(hostname), port, localAddr, localPort);
/* 268 */     this._hostname_ = hostname;
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
/*     */   public void connect(InetAddress host) throws SocketException, IOException {
/* 286 */     this._hostname_ = null;
/* 287 */     connect(host, this._defaultPort_);
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
/*     */   public void connect(String hostname) throws SocketException, IOException {
/* 306 */     connect(hostname, this._defaultPort_);
/* 307 */     this._hostname_ = hostname;
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
/*     */   public void disconnect() throws IOException {
/* 323 */     closeQuietly(this._socket_);
/* 324 */     closeQuietly(this._input_);
/* 325 */     closeQuietly(this._output_);
/* 326 */     this._socket_ = null;
/* 327 */     this._hostname_ = null;
/* 328 */     this._input_ = null;
/* 329 */     this._output_ = null;
/*     */   }
/*     */   
/*     */   private void closeQuietly(Socket socket) {
/* 333 */     if (socket != null) {
/*     */       try {
/* 335 */         socket.close();
/* 336 */       } catch (IOException e) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeQuietly(Closeable close) {
/* 343 */     if (close != null) {
/*     */       try {
/* 345 */         close.close();
/* 346 */       } catch (IOException e) {}
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
/*     */   public boolean isConnected() {
/* 360 */     if (this._socket_ == null) {
/* 361 */       return false;
/*     */     }
/*     */     
/* 364 */     return this._socket_.isConnected();
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
/*     */   public boolean isAvailable() {
/* 376 */     if (isConnected()) {
/*     */       
/*     */       try {
/* 379 */         if (this._socket_.getInetAddress() == null) {
/* 380 */           return false;
/*     */         }
/* 382 */         if (this._socket_.getPort() == 0) {
/* 383 */           return false;
/*     */         }
/* 385 */         if (this._socket_.getRemoteSocketAddress() == null) {
/* 386 */           return false;
/*     */         }
/* 388 */         if (this._socket_.isClosed()) {
/* 389 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 394 */         if (this._socket_.isInputShutdown()) {
/* 395 */           return false;
/*     */         }
/* 397 */         if (this._socket_.isOutputShutdown()) {
/* 398 */           return false;
/*     */         }
/*     */         
/* 401 */         this._socket_.getInputStream();
/* 402 */         this._socket_.getOutputStream();
/*     */       }
/* 404 */       catch (IOException ioex) {
/*     */         
/* 406 */         return false;
/*     */       } 
/* 408 */       return true;
/*     */     } 
/* 410 */     return false;
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
/*     */   public void setDefaultPort(int port) {
/* 424 */     this._defaultPort_ = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultPort() {
/* 435 */     return this._defaultPort_;
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
/* 452 */     this._timeout_ = timeout;
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
/* 465 */     return this._timeout_;
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
/*     */   public void setSoTimeout(int timeout) throws SocketException {
/* 483 */     this._socket_.setSoTimeout(timeout);
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
/*     */   public void setSendBufferSize(int size) throws SocketException {
/* 495 */     this.sendBufferSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSendBufferSize() {
/* 504 */     return this.sendBufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReceiveBufferSize(int size) throws SocketException {
/* 515 */     this.receiveBufferSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getReceiveBufferSize() {
/* 524 */     return this.receiveBufferSize;
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
/*     */   public int getSoTimeout() throws SocketException {
/* 536 */     return this._socket_.getSoTimeout();
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
/*     */   public void setTcpNoDelay(boolean on) throws SocketException {
/* 549 */     this._socket_.setTcpNoDelay(on);
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
/*     */   public boolean getTcpNoDelay() throws SocketException {
/* 564 */     return this._socket_.getTcpNoDelay();
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
/*     */   public void setKeepAlive(boolean keepAlive) throws SocketException {
/* 580 */     this._socket_.setKeepAlive(keepAlive);
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
/*     */   public boolean getKeepAlive() throws SocketException {
/* 592 */     return this._socket_.getKeepAlive();
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
/*     */   public void setSoLinger(boolean on, int val) throws SocketException {
/* 605 */     this._socket_.setSoLinger(on, val);
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
/*     */   public int getSoLinger() throws SocketException {
/* 619 */     return this._socket_.getSoLinger();
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
/*     */   public int getLocalPort() {
/* 634 */     return this._socket_.getLocalPort();
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
/* 647 */     return this._socket_.getLocalAddress();
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
/*     */   public int getRemotePort() {
/* 661 */     return this._socket_.getPort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 672 */     return this._socket_.getInetAddress();
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
/*     */   public boolean verifyRemote(Socket socket) {
/* 690 */     InetAddress host1 = socket.getInetAddress();
/* 691 */     InetAddress host2 = getRemoteAddress();
/*     */     
/* 693 */     return host1.equals(host2);
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
/*     */   public void setSocketFactory(SocketFactory factory) {
/* 708 */     if (factory == null) {
/* 709 */       this._socketFactory_ = __DEFAULT_SOCKET_FACTORY;
/*     */     } else {
/* 711 */       this._socketFactory_ = factory;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 716 */     this.connProxy = null;
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
/*     */   public void setServerSocketFactory(ServerSocketFactory factory) {
/* 729 */     if (factory == null) {
/* 730 */       this._serverSocketFactory_ = __DEFAULT_SERVER_SOCKET_FACTORY;
/*     */     } else {
/* 732 */       this._serverSocketFactory_ = factory;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectTimeout(int connectTimeout) {
/* 743 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConnectTimeout() {
/* 752 */     return this.connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerSocketFactory getServerSocketFactory() {
/* 761 */     return this._serverSocketFactory_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProtocolCommandListener(ProtocolCommandListener listener) {
/* 772 */     getCommandSupport().addProtocolCommandListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeProtocolCommandListener(ProtocolCommandListener listener) {
/* 782 */     getCommandSupport().removeProtocolCommandListener(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireReplyReceived(int replyCode, String reply) {
/* 793 */     if (getCommandSupport().getListenerCount() > 0) {
/* 794 */       getCommandSupport().fireReplyReceived(replyCode, reply);
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
/*     */   protected void fireCommandSent(String command, String message) {
/* 806 */     if (getCommandSupport().getListenerCount() > 0) {
/* 807 */       getCommandSupport().fireCommandSent(command, message);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createCommandSupport() {
/* 815 */     this.__commandSupport = new ProtocolCommandSupport(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ProtocolCommandSupport getCommandSupport() {
/* 826 */     return this.__commandSupport;
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
/*     */   public void setProxy(Proxy proxy) {
/* 838 */     setSocketFactory(new DefaultSocketFactory(proxy));
/* 839 */     this.connProxy = proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Proxy getProxy() {
/* 847 */     return this.connProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getCharsetName() {
/* 859 */     return this.charset.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 869 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(Charset charset) {
/* 879 */     this.charset = charset;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\SocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */