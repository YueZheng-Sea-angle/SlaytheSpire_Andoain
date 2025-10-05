/*     */ package org.apache.commons.net.bsd;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.BindException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.net.io.SocketInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RCommandClient
/*     */   extends RExecClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 514;
/*     */   public static final int MIN_CLIENT_PORT = 512;
/*     */   public static final int MAX_CLIENT_PORT = 1023;
/*     */   
/*     */   InputStream _createErrorStream() throws IOException {
/* 115 */     int localPort = 1023;
/* 116 */     ServerSocket server = null;
/*     */     
/* 118 */     for (localPort = 1023; localPort >= 512; localPort--) {
/*     */ 
/*     */       
/*     */       try {
/* 122 */         server = this._serverSocketFactory_.createServerSocket(localPort, 1, getLocalAddress());
/*     */ 
/*     */         
/*     */         break;
/* 126 */       } catch (SocketException e) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (server == null) {
/* 133 */       throw new BindException("All ports in use.");
/*     */     }
/*     */     
/* 136 */     this._output_.write(Integer.toString(server.getLocalPort()).getBytes("UTF-8"));
/* 137 */     this._output_.write(0);
/* 138 */     this._output_.flush();
/*     */     
/* 140 */     Socket socket = server.accept();
/* 141 */     server.close();
/*     */     
/* 143 */     if (isRemoteVerificationEnabled() && !verifyRemote(socket)) {
/*     */       
/* 145 */       socket.close();
/* 146 */       throw new IOException("Security violation: unexpected connection attempt by " + socket.getInetAddress().getHostAddress());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 151 */     return (InputStream)new SocketInputStream(socket, socket.getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RCommandClient() {
/* 160 */     setDefaultPort(514);
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
/*     */   
/*     */   public void connect(InetAddress host, int port, InetAddress localAddr) throws SocketException, BindException, IOException {
/* 185 */     int localPort = 1023;
/*     */     
/* 187 */     for (localPort = 1023; localPort >= 512; localPort--) {
/*     */ 
/*     */       
/*     */       try {
/* 191 */         this._socket_ = this._socketFactory_.createSocket(host, port, localAddr, localPort);
/*     */         
/*     */         break;
/* 194 */       } catch (BindException be) {
/*     */ 
/*     */       
/* 197 */       } catch (SocketException e) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     if (localPort < 512) {
/* 205 */       throw new BindException("All ports in use or insufficient permssion.");
/*     */     }
/*     */     
/* 208 */     _connectAction_();
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
/*     */   public void connect(InetAddress host, int port) throws SocketException, IOException {
/* 232 */     connect(host, port, InetAddress.getLocalHost());
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
/*     */   public void connect(String hostname, int port) throws SocketException, IOException, UnknownHostException {
/* 256 */     connect(InetAddress.getByName(hostname), port, InetAddress.getLocalHost());
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
/*     */   public void connect(String hostname, int port, InetAddress localAddr) throws SocketException, IOException {
/* 279 */     connect(InetAddress.getByName(hostname), port, localAddr);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(InetAddress host, int port, InetAddress localAddr, int localPort) throws SocketException, IOException, IllegalArgumentException {
/* 308 */     if (localPort < 512 || localPort > 1023) {
/* 309 */       throw new IllegalArgumentException("Invalid port number " + localPort);
/*     */     }
/* 311 */     super.connect(host, port, localAddr, localPort);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(String hostname, int port, InetAddress localAddr, int localPort) throws SocketException, IOException, IllegalArgumentException, UnknownHostException {
/* 341 */     if (localPort < 512 || localPort > 1023) {
/* 342 */       throw new IllegalArgumentException("Invalid port number " + localPort);
/*     */     }
/* 344 */     super.connect(hostname, port, localAddr, localPort);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rcommand(String localUsername, String remoteUsername, String command, boolean separateErrorStream) throws IOException {
/* 388 */     rexec(localUsername, remoteUsername, command, separateErrorStream);
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
/*     */   public void rcommand(String localUsername, String remoteUsername, String command) throws IOException {
/* 404 */     rcommand(localUsername, remoteUsername, command, false);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\bsd\RCommandClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */