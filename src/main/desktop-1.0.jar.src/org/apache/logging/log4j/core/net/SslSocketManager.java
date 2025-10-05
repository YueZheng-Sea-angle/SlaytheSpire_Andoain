/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SslSocketManager
/*     */   extends TcpSocketManager
/*     */ {
/*     */   public static final int DEFAULT_PORT = 6514;
/*  39 */   private static final SslSocketManagerFactory FACTORY = new SslSocketManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final SslConfiguration sslConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/*  63 */     super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
/*  64 */     this.sslConfig = sslConfig;
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
/*     */   public SslSocketManager(String name, OutputStream os, Socket sock, SslConfiguration sslConfig, InetAddress inetAddress, String host, int port, int connectTimeoutMillis, int reconnectionDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/*  86 */     super(name, os, sock, inetAddress, host, port, connectTimeoutMillis, reconnectionDelayMillis, immediateFail, layout, bufferSize, socketOptions);
/*  87 */     this.sslConfig = sslConfig;
/*     */   }
/*     */   
/*     */   private static class SslFactoryData
/*     */     extends TcpSocketManager.FactoryData
/*     */   {
/*     */     protected SslConfiguration sslConfiguration;
/*     */     
/*     */     public SslFactoryData(SslConfiguration sslConfiguration, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/*  96 */       super(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
/*     */       
/*  98 */       this.sslConfiguration = sslConfiguration;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 103 */       return "SslFactoryData [sslConfiguration=" + this.sslConfiguration + ", host=" + this.host + ", port=" + this.port + ", connectTimeoutMillis=" + this.connectTimeoutMillis + ", reconnectDelayMillis=" + this.reconnectDelayMillis + ", immediateFail=" + this.immediateFail + ", layout=" + this.layout + ", bufferSize=" + this.bufferSize + ", socketOptions=" + this.socketOptions + "]";
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
/*     */   @Deprecated
/*     */   public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 117 */     return getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static SslSocketManager getSocketManager(SslConfiguration sslConfig, String host, int port, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 123 */     if (Strings.isEmpty(host)) {
/* 124 */       throw new IllegalArgumentException("A host name is required");
/*     */     }
/* 126 */     if (port <= 0) {
/* 127 */       port = 6514;
/*     */     }
/* 129 */     if (reconnectDelayMillis == 0) {
/* 130 */       reconnectDelayMillis = 30000;
/*     */     }
/* 132 */     String name = "TLS:" + host + ':' + port;
/* 133 */     return (SslSocketManager)getManager(name, new SslFactoryData(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Socket createSocket(InetSocketAddress socketAddress) throws IOException {
/* 139 */     SSLSocketFactory socketFactory = createSslSocketFactory(this.sslConfig);
/* 140 */     Socket newSocket = socketFactory.createSocket();
/* 141 */     newSocket.connect(socketAddress, getConnectTimeoutMillis());
/* 142 */     return newSocket;
/*     */   }
/*     */ 
/*     */   
/*     */   private static SSLSocketFactory createSslSocketFactory(SslConfiguration sslConf) {
/*     */     SSLSocketFactory socketFactory;
/* 148 */     if (sslConf != null) {
/* 149 */       socketFactory = sslConf.getSslSocketFactory();
/*     */     } else {
/* 151 */       socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     } 
/*     */     
/* 154 */     return socketFactory;
/*     */   }
/*     */   
/*     */   private static class SslSocketManagerFactory
/*     */     extends TcpSocketManager.TcpSocketManagerFactory<SslSocketManager, SslFactoryData>
/*     */   {
/*     */     private SslSocketManagerFactory() {}
/*     */     
/*     */     SslSocketManager createManager(String name, OutputStream os, Socket socket, InetAddress inetAddress, SslSocketManager.SslFactoryData data) {
/* 163 */       return new SslSocketManager(name, os, socket, data.sslConfiguration, inetAddress, data.host, data.port, data.connectTimeoutMillis, data.reconnectDelayMillis, data.immediateFail, data.layout, data.bufferSize, data.socketOptions);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Socket createSocket(SslSocketManager.SslFactoryData data) throws IOException {
/* 170 */       List<InetSocketAddress> socketAddresses = resolver.resolveHost(data.host, data.port);
/* 171 */       IOException ioe = null;
/* 172 */       for (InetSocketAddress socketAddress : socketAddresses) {
/*     */         try {
/* 174 */           return SslSocketManager.createSocket(socketAddress, data.connectTimeoutMillis, data.sslConfiguration, data.socketOptions);
/*     */         }
/* 176 */         catch (IOException ex) {
/* 177 */           ioe = ex;
/*     */         } 
/*     */       } 
/* 180 */       throw new IOException(errorMessage(data, socketAddresses), ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static Socket createSocket(InetSocketAddress socketAddress, int connectTimeoutMillis, SslConfiguration sslConfiguration, SocketOptions socketOptions) throws IOException {
/* 186 */     SSLSocketFactory socketFactory = createSslSocketFactory(sslConfiguration);
/* 187 */     SSLSocket socket = (SSLSocket)socketFactory.createSocket();
/* 188 */     if (socketOptions != null)
/*     */     {
/* 190 */       socketOptions.apply(socket);
/*     */     }
/* 192 */     socket.connect(socketAddress, connectTimeoutMillis);
/* 193 */     if (socketOptions != null)
/*     */     {
/* 195 */       socketOptions.apply(socket);
/*     */     }
/* 197 */     return socket;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\SslSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */