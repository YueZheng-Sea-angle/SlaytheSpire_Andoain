/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLServerSocket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTPSSocketFactory
/*     */   extends SocketFactory
/*     */ {
/*     */   private final SSLContext context;
/*     */   
/*     */   public FTPSSocketFactory(SSLContext context) {
/*  39 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket() throws IOException {
/*  45 */     return this.context.getSocketFactory().createSocket();
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket(String address, int port) throws UnknownHostException, IOException {
/*  50 */     return this.context.getSocketFactory().createSocket(address, port);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress address, int port) throws IOException {
/*  55 */     return this.context.getSocketFactory().createSocket(address, port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(String address, int port, InetAddress localAddress, int localPort) throws UnknownHostException, IOException {
/*  61 */     return this.context.getSocketFactory().createSocket(address, port, localAddress, localPort);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
/*  66 */     return this.context.getSocketFactory().createSocket(address, port, localAddress, localPort);
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
/*     */   public ServerSocket createServerSocket(int port) throws IOException {
/*  78 */     return init(this.context.getServerSocketFactory().createServerSocket(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerSocket createServerSocket(int port, int backlog) throws IOException {
/*  88 */     return init(this.context.getServerSocketFactory().createServerSocket(port, backlog));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress) throws IOException {
/*  99 */     return init(this.context.getServerSocketFactory().createServerSocket(port, backlog, ifAddress));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ServerSocket init(ServerSocket socket) throws IOException {
/* 108 */     ((SSLServerSocket)socket).setUseClientMode(true);
/* 109 */     return socket;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPSSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */