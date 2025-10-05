/*     */ package org.apache.commons.net.smtp;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import org.apache.commons.net.io.CRLFLineReader;
/*     */ import org.apache.commons.net.util.SSLContextUtils;
/*     */ import org.apache.commons.net.util.SSLSocketUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SMTPSClient
/*     */   extends SMTPClient
/*     */ {
/*     */   private static final String DEFAULT_PROTOCOL = "TLS";
/*     */   private final boolean isImplicit;
/*     */   private final String protocol;
/*  65 */   private SSLContext context = null;
/*     */ 
/*     */   
/*  68 */   private String[] suites = null;
/*     */   
/*  70 */   private String[] protocols = null;
/*     */ 
/*     */   
/*  73 */   private TrustManager trustManager = null;
/*     */ 
/*     */   
/*  76 */   private KeyManager keyManager = null;
/*     */ 
/*     */   
/*  79 */   private HostnameVerifier hostnameVerifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean tlsEndpointChecking;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPSClient() {
/*  90 */     this("TLS", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPSClient(boolean implicit) {
/*  99 */     this("TLS", implicit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPSClient(String proto) {
/* 108 */     this(proto, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPSClient(String proto, boolean implicit) {
/* 118 */     this.protocol = proto;
/* 119 */     this.isImplicit = implicit;
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
/*     */   public SMTPSClient(String proto, boolean implicit, String encoding) {
/* 131 */     super(encoding);
/* 132 */     this.protocol = proto;
/* 133 */     this.isImplicit = implicit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPSClient(boolean implicit, SSLContext ctx) {
/* 143 */     this.isImplicit = implicit;
/* 144 */     this.context = ctx;
/* 145 */     this.protocol = "TLS";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPSClient(SSLContext context) {
/* 155 */     this(false, context);
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
/* 170 */     if (this.isImplicit) {
/* 171 */       performSSLNegotiation();
/*     */     }
/* 173 */     super._connectAction_();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initSSLContext() throws IOException {
/* 183 */     if (this.context == null)
/*     */     {
/* 185 */       this.context = SSLContextUtils.createSSLContext(this.protocol, getKeyManager(), getTrustManager());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void performSSLNegotiation() throws IOException {
/* 196 */     initSSLContext();
/*     */     
/* 198 */     SSLSocketFactory ssf = this.context.getSocketFactory();
/* 199 */     String host = (this._hostname_ != null) ? this._hostname_ : getRemoteAddress().getHostAddress();
/* 200 */     int port = getRemotePort();
/* 201 */     SSLSocket socket = (SSLSocket)ssf.createSocket(this._socket_, host, port, true);
/*     */     
/* 203 */     socket.setEnableSessionCreation(true);
/* 204 */     socket.setUseClientMode(true);
/*     */     
/* 206 */     if (this.tlsEndpointChecking) {
/* 207 */       SSLSocketUtils.enableEndpointNameVerification(socket);
/*     */     }
/* 209 */     if (this.protocols != null) {
/* 210 */       socket.setEnabledProtocols(this.protocols);
/*     */     }
/* 212 */     if (this.suites != null) {
/* 213 */       socket.setEnabledCipherSuites(this.suites);
/*     */     }
/* 215 */     socket.startHandshake();
/*     */ 
/*     */     
/* 218 */     this._socket_ = socket;
/* 219 */     this._input_ = socket.getInputStream();
/* 220 */     this._output_ = socket.getOutputStream();
/* 221 */     this._reader = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, this.encoding));
/*     */     
/* 223 */     this._writer = new BufferedWriter(new OutputStreamWriter(this._output_, this.encoding));
/*     */ 
/*     */     
/* 226 */     if (this.hostnameVerifier != null && !this.hostnameVerifier.verify(host, socket.getSession())) {
/* 227 */       throw new SSLHandshakeException("Hostname doesn't match certificate");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyManager getKeyManager() {
/* 237 */     return this.keyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManager(KeyManager newKeyManager) {
/* 247 */     this.keyManager = newKeyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledCipherSuites(String[] cipherSuites) {
/* 257 */     this.suites = new String[cipherSuites.length];
/* 258 */     System.arraycopy(cipherSuites, 0, this.suites, 0, cipherSuites.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledCipherSuites() {
/* 269 */     if (this._socket_ instanceof SSLSocket)
/*     */     {
/* 271 */       return ((SSLSocket)this._socket_).getEnabledCipherSuites();
/*     */     }
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledProtocols(String[] protocolVersions) {
/* 283 */     this.protocols = new String[protocolVersions.length];
/* 284 */     System.arraycopy(protocolVersions, 0, this.protocols, 0, protocolVersions.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnabledProtocols() {
/* 295 */     if (this._socket_ instanceof SSLSocket)
/*     */     {
/* 297 */       return ((SSLSocket)this._socket_).getEnabledProtocols();
/*     */     }
/* 299 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execTLS() throws IOException {
/* 310 */     if (!SMTPReply.isPositiveCompletion(sendCommand("STARTTLS")))
/*     */     {
/* 312 */       return false;
/*     */     }
/*     */     
/* 315 */     performSSLNegotiation();
/* 316 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrustManager getTrustManager() {
/* 325 */     return this.trustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManager(TrustManager newTrustManager) {
/* 335 */     this.trustManager = newTrustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 345 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostnameVerifier(HostnameVerifier newHostnameVerifier) {
/* 355 */     this.hostnameVerifier = newHostnameVerifier;
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
/*     */   public boolean isEndpointCheckingEnabled() {
/* 367 */     return this.tlsEndpointChecking;
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
/*     */   public void setEndpointCheckingEnabled(boolean enable) {
/* 379 */     this.tlsEndpointChecking = enable;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\smtp\SMTPSClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */