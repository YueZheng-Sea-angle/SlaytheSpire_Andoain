/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.net.Socket;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import org.apache.commons.net.util.Base64;
/*     */ import org.apache.commons.net.util.SSLContextUtils;
/*     */ import org.apache.commons.net.util.SSLSocketUtils;
/*     */ import org.apache.commons.net.util.TrustManagerUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTPSClient
/*     */   extends FTPClient
/*     */ {
/*     */   public static final int DEFAULT_FTPS_DATA_PORT = 989;
/*     */   public static final int DEFAULT_FTPS_PORT = 990;
/*  64 */   private static final String[] PROT_COMMAND_VALUE = new String[] { "C", "E", "S", "P" };
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_PROT = "C";
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_PROTOCOL = "TLS";
/*     */   
/*     */   private static final String CMD_AUTH = "AUTH";
/*     */   
/*     */   private static final String CMD_ADAT = "ADAT";
/*     */   
/*     */   private static final String CMD_PROT = "PROT";
/*     */   
/*     */   private static final String CMD_PBSZ = "PBSZ";
/*     */   
/*     */   private static final String CMD_MIC = "MIC";
/*     */   
/*     */   private static final String CMD_CONF = "CONF";
/*     */   
/*     */   private static final String CMD_ENC = "ENC";
/*     */   
/*     */   private static final String CMD_CCC = "CCC";
/*     */   
/*     */   private final boolean isImplicit;
/*     */   
/*     */   private final String protocol;
/*     */   
/*  92 */   private String auth = "TLS";
/*     */   
/*     */   private SSLContext context;
/*     */   
/*     */   private Socket plainSocket;
/*     */   
/*     */   private boolean isCreation = true;
/*     */   
/*     */   private boolean isClientMode = true;
/*     */   
/*     */   private boolean isNeedClientAuth = false;
/*     */   
/*     */   private boolean isWantClientAuth = false;
/*     */   
/* 106 */   private String[] suites = null;
/*     */   
/* 108 */   private String[] protocols = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private TrustManager trustManager = TrustManagerUtils.getValidateServerCertificateTrustManager();
/*     */ 
/*     */   
/* 116 */   private KeyManager keyManager = null;
/*     */ 
/*     */   
/* 119 */   private HostnameVerifier hostnameVerifier = null;
/*     */ 
/*     */   
/*     */   private boolean tlsEndpointChecking;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static String KEYSTORE_ALGORITHM;
/*     */ 
/*     */   
/*     */   public FTPSClient() {
/* 130 */     this("TLS", false);
/*     */   }
/*     */   @Deprecated
/*     */   public static String TRUSTSTORE_ALGORITHM; @Deprecated
/*     */   public static String PROVIDER;
/*     */   @Deprecated
/*     */   public static String STORE_TYPE;
/*     */   
/*     */   public FTPSClient(boolean isImplicit) {
/* 139 */     this("TLS", isImplicit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPSClient(String protocol) {
/* 148 */     this(protocol, false);
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
/*     */   public FTPSClient(String protocol, boolean isImplicit) {
/* 161 */     this.protocol = protocol;
/* 162 */     this.isImplicit = isImplicit;
/* 163 */     if (isImplicit) {
/* 164 */       setDefaultPort(990);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPSClient(boolean isImplicit, SSLContext context) {
/* 175 */     this("TLS", isImplicit);
/* 176 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTPSClient(SSLContext context) {
/* 186 */     this(false, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthValue(String auth) {
/* 196 */     this.auth = auth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAuthValue() {
/* 204 */     return this.auth;
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
/*     */   protected void _connectAction_() throws IOException {
/* 219 */     if (this.isImplicit) {
/* 220 */       sslNegotiation();
/*     */     }
/* 222 */     super._connectAction_();
/*     */     
/* 224 */     if (!this.isImplicit) {
/* 225 */       execAUTH();
/* 226 */       sslNegotiation();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void execAUTH() throws SSLException, IOException {
/* 237 */     int replyCode = sendCommand("AUTH", this.auth);
/* 238 */     if (334 != replyCode)
/*     */     {
/*     */       
/* 241 */       if (234 != replyCode) {
/* 242 */         throw new SSLException(getReplyString());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initSslContext() throws IOException {
/* 251 */     if (this.context == null) {
/* 252 */       this.context = SSLContextUtils.createSSLContext(this.protocol, getKeyManager(), getTrustManager());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sslNegotiation() throws IOException {
/* 262 */     this.plainSocket = this._socket_;
/* 263 */     initSslContext();
/*     */     
/* 265 */     SSLSocketFactory ssf = this.context.getSocketFactory();
/* 266 */     String host = (this._hostname_ != null) ? this._hostname_ : getRemoteAddress().getHostAddress();
/* 267 */     int port = this._socket_.getPort();
/* 268 */     SSLSocket socket = (SSLSocket)ssf.createSocket(this._socket_, host, port, false);
/*     */     
/* 270 */     socket.setEnableSessionCreation(this.isCreation);
/* 271 */     socket.setUseClientMode(this.isClientMode);
/*     */ 
/*     */     
/* 274 */     if (this.isClientMode) {
/* 275 */       if (this.tlsEndpointChecking) {
/* 276 */         SSLSocketUtils.enableEndpointNameVerification(socket);
/*     */       }
/*     */     } else {
/* 279 */       socket.setNeedClientAuth(this.isNeedClientAuth);
/* 280 */       socket.setWantClientAuth(this.isWantClientAuth);
/*     */     } 
/*     */     
/* 283 */     if (this.protocols != null) {
/* 284 */       socket.setEnabledProtocols(this.protocols);
/*     */     }
/* 286 */     if (this.suites != null) {
/* 287 */       socket.setEnabledCipherSuites(this.suites);
/*     */     }
/* 289 */     socket.startHandshake();
/*     */ 
/*     */     
/* 292 */     this._socket_ = socket;
/* 293 */     this._controlInput_ = new BufferedReader(new InputStreamReader(socket.getInputStream(), getControlEncoding()));
/*     */     
/* 295 */     this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), getControlEncoding()));
/*     */ 
/*     */     
/* 298 */     if (this.isClientMode && 
/* 299 */       this.hostnameVerifier != null && !this.hostnameVerifier.verify(host, socket.getSession())) {
/* 300 */       throw new SSLHandshakeException("Hostname doesn't match certificate");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyManager getKeyManager() {
/* 310 */     return this.keyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManager(KeyManager keyManager) {
/* 320 */     this.keyManager = keyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledSessionCreation(boolean isCreation) {
/* 328 */     this.isCreation = isCreation;
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
/*     */   public boolean getEnableSessionCreation() {
/* 341 */     if (this._socket_ instanceof SSLSocket) {
/* 342 */       return ((SSLSocket)this._socket_).getEnableSessionCreation();
/*     */     }
/* 344 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNeedClientAuth(boolean isNeedClientAuth) {
/* 352 */     this.isNeedClientAuth = isNeedClientAuth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNeedClientAuth() {
/* 362 */     if (this._socket_ instanceof SSLSocket) {
/* 363 */       return ((SSLSocket)this._socket_).getNeedClientAuth();
/*     */     }
/* 365 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWantClientAuth(boolean isWantClientAuth) {
/* 375 */     this.isWantClientAuth = isWantClientAuth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getWantClientAuth() {
/* 385 */     if (this._socket_ instanceof SSLSocket) {
/* 386 */       return ((SSLSocket)this._socket_).getWantClientAuth();
/*     */     }
/* 388 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseClientMode(boolean isClientMode) {
/* 397 */     this.isClientMode = isClientMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseClientMode() {
/* 408 */     if (this._socket_ instanceof SSLSocket) {
/* 409 */       return ((SSLSocket)this._socket_).getUseClientMode();
/*     */     }
/* 411 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledCipherSuites(String[] cipherSuites) {
/* 420 */     this.suites = new String[cipherSuites.length];
/* 421 */     System.arraycopy(cipherSuites, 0, this.suites, 0, cipherSuites.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledCipherSuites() {
/* 431 */     if (this._socket_ instanceof SSLSocket) {
/* 432 */       return ((SSLSocket)this._socket_).getEnabledCipherSuites();
/*     */     }
/* 434 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledProtocols(String[] protocolVersions) {
/* 443 */     this.protocols = new String[protocolVersions.length];
/* 444 */     System.arraycopy(protocolVersions, 0, this.protocols, 0, protocolVersions.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledProtocols() {
/* 454 */     if (this._socket_ instanceof SSLSocket) {
/* 455 */       return ((SSLSocket)this._socket_).getEnabledProtocols();
/*     */     }
/* 457 */     return null;
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
/*     */   public void execPBSZ(long pbsz) throws SSLException, IOException {
/* 469 */     if (pbsz < 0L || 4294967295L < pbsz) {
/* 470 */       throw new IllegalArgumentException();
/*     */     }
/* 472 */     int status = sendCommand("PBSZ", String.valueOf(pbsz));
/* 473 */     if (200 != status) {
/* 474 */       throw new SSLException(getReplyString());
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
/*     */   public long parsePBSZ(long pbsz) throws SSLException, IOException {
/* 491 */     execPBSZ(pbsz);
/* 492 */     long minvalue = pbsz;
/* 493 */     String remainder = extractPrefixedData("PBSZ=", getReplyString());
/* 494 */     if (remainder != null) {
/* 495 */       long replysz = Long.parseLong(remainder);
/* 496 */       if (replysz < minvalue) {
/* 497 */         minvalue = replysz;
/*     */       }
/*     */     } 
/* 500 */     return minvalue;
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
/*     */   public void execPROT(String prot) throws SSLException, IOException {
/* 521 */     if (prot == null) {
/* 522 */       prot = "C";
/*     */     }
/* 524 */     if (!checkPROTValue(prot)) {
/* 525 */       throw new IllegalArgumentException();
/*     */     }
/* 527 */     if (200 != sendCommand("PROT", prot)) {
/* 528 */       throw new SSLException(getReplyString());
/*     */     }
/* 530 */     if ("C".equals(prot)) {
/* 531 */       setSocketFactory(null);
/* 532 */       setServerSocketFactory(null);
/*     */     } else {
/* 534 */       setSocketFactory(new FTPSSocketFactory(this.context));
/* 535 */       setServerSocketFactory(new FTPSServerSocketFactory(this.context));
/* 536 */       initSslContext();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkPROTValue(String prot) {
/* 546 */     for (String element : PROT_COMMAND_VALUE) {
/*     */       
/* 548 */       if (element.equals(prot)) {
/* 549 */         return true;
/*     */       }
/*     */     } 
/* 552 */     return false;
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
/*     */   public int sendCommand(String command, String args) throws IOException {
/* 568 */     int repCode = super.sendCommand(command, args);
/*     */     
/* 570 */     if ("CCC".equals(command)) {
/* 571 */       if (200 == repCode) {
/* 572 */         this._socket_.close();
/* 573 */         this._socket_ = this.plainSocket;
/* 574 */         this._controlInput_ = new BufferedReader(new InputStreamReader(this._socket_.getInputStream(), getControlEncoding()));
/*     */ 
/*     */         
/* 577 */         this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(this._socket_.getOutputStream(), getControlEncoding()));
/*     */       }
/*     */       else {
/*     */         
/* 581 */         throw new SSLException(getReplyString());
/*     */       } 
/*     */     }
/* 584 */     return repCode;
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
/*     */   @Deprecated
/*     */   protected Socket _openDataConnection_(int command, String arg) throws IOException {
/* 607 */     return _openDataConnection_(FTPCommand.getCommand(command), arg);
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
/*     */   protected Socket _openDataConnection_(String command, String arg) throws IOException {
/* 627 */     Socket socket = super._openDataConnection_(command, arg);
/* 628 */     _prepareDataSocket_(socket);
/* 629 */     if (socket instanceof SSLSocket) {
/* 630 */       SSLSocket sslSocket = (SSLSocket)socket;
/*     */       
/* 632 */       sslSocket.setUseClientMode(this.isClientMode);
/* 633 */       sslSocket.setEnableSessionCreation(this.isCreation);
/*     */ 
/*     */       
/* 636 */       if (!this.isClientMode) {
/* 637 */         sslSocket.setNeedClientAuth(this.isNeedClientAuth);
/* 638 */         sslSocket.setWantClientAuth(this.isWantClientAuth);
/*     */       } 
/* 640 */       if (this.suites != null) {
/* 641 */         sslSocket.setEnabledCipherSuites(this.suites);
/*     */       }
/* 643 */       if (this.protocols != null) {
/* 644 */         sslSocket.setEnabledProtocols(this.protocols);
/*     */       }
/* 646 */       sslSocket.startHandshake();
/*     */     } 
/*     */     
/* 649 */     return socket;
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
/*     */   protected void _prepareDataSocket_(Socket socket) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrustManager getTrustManager() {
/* 672 */     return this.trustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManager(TrustManager trustManager) {
/* 683 */     this.trustManager = trustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 694 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostnameVerifier(HostnameVerifier newHostnameVerifier) {
/* 705 */     this.hostnameVerifier = newHostnameVerifier;
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
/*     */   public boolean isEndpointCheckingEnabled() {
/* 719 */     return this.tlsEndpointChecking;
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
/*     */   public void setEndpointCheckingEnabled(boolean enable) {
/* 733 */     this.tlsEndpointChecking = enable;
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
/* 749 */     super.disconnect();
/* 750 */     setSocketFactory(null);
/* 751 */     setServerSocketFactory(null);
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
/*     */   public int execAUTH(String mechanism) throws IOException {
/* 764 */     return sendCommand("AUTH", mechanism);
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
/*     */   public int execADAT(byte[] data) throws IOException {
/* 777 */     if (data != null)
/*     */     {
/* 779 */       return sendCommand("ADAT", Base64.encodeBase64StringUnChunked(data));
/*     */     }
/*     */ 
/*     */     
/* 783 */     return sendCommand("ADAT");
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
/*     */   public int execCCC() throws IOException {
/* 798 */     int repCode = sendCommand("CCC");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 810 */     return repCode;
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
/*     */   public int execMIC(byte[] data) throws IOException {
/* 823 */     if (data != null)
/*     */     {
/* 825 */       return sendCommand("MIC", Base64.encodeBase64StringUnChunked(data));
/*     */     }
/*     */ 
/*     */     
/* 829 */     return sendCommand("MIC", "");
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
/*     */   public int execCONF(byte[] data) throws IOException {
/* 843 */     if (data != null)
/*     */     {
/* 845 */       return sendCommand("CONF", Base64.encodeBase64StringUnChunked(data));
/*     */     }
/*     */ 
/*     */     
/* 849 */     return sendCommand("CONF", "");
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
/*     */   public int execENC(byte[] data) throws IOException {
/* 863 */     if (data != null)
/*     */     {
/* 865 */       return sendCommand("ENC", Base64.encodeBase64StringUnChunked(data));
/*     */     }
/*     */ 
/*     */     
/* 869 */     return sendCommand("ENC", "");
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
/*     */   public byte[] parseADATReply(String reply) {
/* 881 */     if (reply == null) {
/* 882 */       return null;
/*     */     }
/* 884 */     return Base64.decodeBase64(extractPrefixedData("ADAT=", reply));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String extractPrefixedData(String prefix, String reply) {
/* 895 */     int idx = reply.indexOf(prefix);
/* 896 */     if (idx == -1) {
/* 897 */       return null;
/*     */     }
/*     */     
/* 900 */     return reply.substring(idx + prefix.length()).trim();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPSClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */