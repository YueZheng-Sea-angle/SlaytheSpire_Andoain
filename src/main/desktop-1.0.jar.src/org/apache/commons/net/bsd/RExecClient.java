/*     */ package org.apache.commons.net.bsd;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import org.apache.commons.net.SocketClient;
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
/*     */ public class RExecClient
/*     */   extends SocketClient
/*     */ {
/*     */   protected static final char NULL_CHAR = '\000';
/*     */   public static final int DEFAULT_PORT = 512;
/*     */   private boolean __remoteVerificationEnabled;
/*     */   protected InputStream _errorStream_;
/*     */   
/*     */   InputStream _createErrorStream() throws IOException {
/*  94 */     ServerSocket server = this._serverSocketFactory_.createServerSocket(0, 1, getLocalAddress());
/*     */     
/*  96 */     this._output_.write(Integer.toString(server.getLocalPort()).getBytes("UTF-8"));
/*  97 */     this._output_.write(0);
/*  98 */     this._output_.flush();
/*     */     
/* 100 */     Socket socket = server.accept();
/* 101 */     server.close();
/*     */     
/* 103 */     if (this.__remoteVerificationEnabled && !verifyRemote(socket)) {
/*     */       
/* 105 */       socket.close();
/* 106 */       throw new IOException("Security violation: unexpected connection attempt by " + socket.getInetAddress().getHostAddress());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 111 */     return (InputStream)new SocketInputStream(socket, socket.getInputStream());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RExecClient() {
/* 121 */     this._errorStream_ = null;
/* 122 */     setDefaultPort(512);
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
/*     */   public InputStream getInputStream() {
/* 136 */     return this._input_;
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
/*     */   public OutputStream getOutputStream() {
/* 150 */     return this._output_;
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
/*     */   public InputStream getErrorStream() {
/* 166 */     return this._errorStream_;
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
/*     */   public void rexec(String username, String password, String command, boolean separateErrorStream) throws IOException {
/* 208 */     if (separateErrorStream) {
/*     */       
/* 210 */       this._errorStream_ = _createErrorStream();
/*     */     }
/*     */     else {
/*     */       
/* 214 */       this._output_.write(0);
/*     */     } 
/*     */     
/* 217 */     this._output_.write(username.getBytes(getCharset()));
/* 218 */     this._output_.write(0);
/* 219 */     this._output_.write(password.getBytes(getCharset()));
/* 220 */     this._output_.write(0);
/* 221 */     this._output_.write(command.getBytes(getCharset()));
/* 222 */     this._output_.write(0);
/* 223 */     this._output_.flush();
/*     */     
/* 225 */     int ch = this._input_.read();
/* 226 */     if (ch > 0) {
/* 227 */       StringBuilder buffer = new StringBuilder();
/*     */       
/* 229 */       while ((ch = this._input_.read()) != -1 && ch != 10) {
/* 230 */         buffer.append((char)ch);
/*     */       }
/*     */       
/* 233 */       throw new IOException(buffer.toString());
/* 234 */     }  if (ch < 0) {
/* 235 */       throw new IOException("Server closed connection.");
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
/*     */   public void rexec(String username, String password, String command) throws IOException {
/* 251 */     rexec(username, password, command, false);
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
/*     */   public void disconnect() throws IOException {
/* 263 */     if (this._errorStream_ != null) {
/* 264 */       this._errorStream_.close();
/*     */     }
/* 266 */     this._errorStream_ = null;
/* 267 */     super.disconnect();
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
/*     */   public final void setRemoteVerificationEnabled(boolean enable) {
/* 282 */     this.__remoteVerificationEnabled = enable;
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
/*     */   public final boolean isRemoteVerificationEnabled() {
/* 294 */     return this.__remoteVerificationEnabled;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\bsd\RExecClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */