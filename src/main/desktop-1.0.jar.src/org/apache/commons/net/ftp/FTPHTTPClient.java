/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.net.util.Base64;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTPHTTPClient
/*     */   extends FTPClient
/*     */ {
/*     */   private final String proxyHost;
/*     */   private final int proxyPort;
/*     */   private final String proxyUsername;
/*     */   private final String proxyPassword;
/*  46 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*  47 */   private final Base64 base64 = new Base64();
/*     */   
/*     */   private String tunnelHost;
/*     */   
/*     */   public FTPHTTPClient(String proxyHost, int proxyPort, String proxyUser, String proxyPass) {
/*  52 */     this.proxyHost = proxyHost;
/*  53 */     this.proxyPort = proxyPort;
/*  54 */     this.proxyUsername = proxyUser;
/*  55 */     this.proxyPassword = proxyPass;
/*  56 */     this.tunnelHost = null;
/*     */   }
/*     */   
/*     */   public FTPHTTPClient(String proxyHost, int proxyPort) {
/*  60 */     this(proxyHost, proxyPort, (String)null, (String)null);
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
/*     */   @Deprecated
/*     */   protected Socket _openDataConnection_(int command, String arg) throws IOException {
/*  76 */     return super._openDataConnection_(command, arg);
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
/*     */   protected Socket _openDataConnection_(String command, String arg) throws IOException {
/*  89 */     if (getDataConnectionMode() != 2) {
/*  90 */       throw new IllegalStateException("Only passive connection mode supported");
/*     */     }
/*     */     
/*  93 */     boolean isInet6Address = getRemoteAddress() instanceof java.net.Inet6Address;
/*  94 */     String passiveHost = null;
/*     */     
/*  96 */     boolean attemptEPSV = (isUseEPSVwithIPv4() || isInet6Address);
/*  97 */     if (attemptEPSV && epsv() == 229) {
/*  98 */       _parseExtendedPassiveModeReply(this._replyLines.get(0));
/*  99 */       passiveHost = this.tunnelHost;
/*     */     } else {
/* 101 */       if (isInet6Address) {
/* 102 */         return null;
/*     */       }
/*     */       
/* 105 */       if (pasv() != 227) {
/* 106 */         return null;
/*     */       }
/* 108 */       _parsePassiveModeReply(this._replyLines.get(0));
/* 109 */       passiveHost = getPassiveHost();
/*     */     } 
/*     */     
/* 112 */     Socket socket = this._socketFactory_.createSocket(this.proxyHost, this.proxyPort);
/* 113 */     InputStream is = socket.getInputStream();
/* 114 */     OutputStream os = socket.getOutputStream();
/* 115 */     tunnelHandshake(passiveHost, getPassivePort(), is, os);
/* 116 */     if (getRestartOffset() > 0L && !restart(getRestartOffset())) {
/* 117 */       socket.close();
/* 118 */       return null;
/*     */     } 
/*     */     
/* 121 */     if (!FTPReply.isPositivePreliminary(sendCommand(command, arg))) {
/* 122 */       socket.close();
/* 123 */       return null;
/*     */     } 
/*     */     
/* 126 */     return socket;
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect(String host, int port) throws SocketException, IOException {
/*     */     Reader socketIsReader;
/* 132 */     this._socket_ = this._socketFactory_.createSocket(this.proxyHost, this.proxyPort);
/* 133 */     this._input_ = this._socket_.getInputStream();
/* 134 */     this._output_ = this._socket_.getOutputStream();
/*     */     
/*     */     try {
/* 137 */       socketIsReader = tunnelHandshake(host, port, this._input_, this._output_);
/*     */     }
/* 139 */     catch (Exception e) {
/* 140 */       IOException ioe = new IOException("Could not connect to " + host + " using port " + port);
/* 141 */       ioe.initCause(e);
/* 142 */       throw ioe;
/*     */     } 
/* 144 */     _connectAction_(socketIsReader);
/*     */   }
/*     */ 
/*     */   
/*     */   private BufferedReader tunnelHandshake(String host, int port, InputStream input, OutputStream output) throws IOException, UnsupportedEncodingException {
/* 149 */     String connectString = "CONNECT " + host + ":" + port + " HTTP/1.1";
/* 150 */     String hostString = "Host: " + host + ":" + port;
/*     */     
/* 152 */     this.tunnelHost = host;
/* 153 */     output.write(connectString.getBytes("UTF-8"));
/* 154 */     output.write(CRLF);
/* 155 */     output.write(hostString.getBytes("UTF-8"));
/* 156 */     output.write(CRLF);
/*     */     
/* 158 */     if (this.proxyUsername != null && this.proxyPassword != null) {
/* 159 */       String auth = this.proxyUsername + ":" + this.proxyPassword;
/* 160 */       String header = "Proxy-Authorization: Basic " + this.base64.encodeToString(auth.getBytes("UTF-8"));
/*     */       
/* 162 */       output.write(header.getBytes("UTF-8"));
/*     */     } 
/* 164 */     output.write(CRLF);
/*     */     
/* 166 */     List<String> response = new ArrayList<String>();
/* 167 */     BufferedReader reader = new BufferedReader(new InputStreamReader(input, getCharset()));
/*     */ 
/*     */     
/* 170 */     String line = reader.readLine();
/* 171 */     for (; line != null && line.length() > 0; line = reader.readLine()) {
/* 172 */       response.add(line);
/*     */     }
/*     */     
/* 175 */     int size = response.size();
/* 176 */     if (size == 0) {
/* 177 */       throw new IOException("No response from proxy");
/*     */     }
/*     */     
/* 180 */     String code = null;
/* 181 */     String resp = response.get(0);
/* 182 */     if (resp.startsWith("HTTP/") && resp.length() >= 12) {
/* 183 */       code = resp.substring(9, 12);
/*     */     } else {
/* 185 */       throw new IOException("Invalid response from proxy: " + resp);
/*     */     } 
/*     */     
/* 188 */     if (!"200".equals(code)) {
/* 189 */       StringBuilder msg = new StringBuilder();
/* 190 */       msg.append("HTTPTunnelConnector: connection failed\r\n");
/* 191 */       msg.append("Response received from the proxy:\r\n");
/* 192 */       for (String str : response) {
/* 193 */         msg.append(str);
/* 194 */         msg.append("\r\n");
/*     */       } 
/* 196 */       throw new IOException(msg.toString());
/*     */     } 
/* 198 */     return reader;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPHTTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */