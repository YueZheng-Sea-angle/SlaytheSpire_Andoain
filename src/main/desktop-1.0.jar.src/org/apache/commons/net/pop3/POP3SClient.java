/*     */ package org.apache.commons.net.pop3;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class POP3SClient
/*     */   extends POP3Client
/*     */ {
/*     */   private static final int DEFAULT_POP3S_PORT = 995;
/*     */   private static final String DEFAULT_PROTOCOL = "TLS";
/*     */   private final boolean isImplicit;
/*     */   private final String protocol;
/*  73 */   private SSLContext context = null;
/*     */ 
/*     */   
/*  76 */   private String[] suites = null;
/*     */   
/*  78 */   private String[] protocols = null;
/*     */ 
/*     */ 
/*     */   
/*  82 */   private TrustManager trustManager = null;
/*     */ 
/*     */   
/*  85 */   private KeyManager keyManager = null;
/*     */ 
/*     */   
/*  88 */   private HostnameVerifier hostnameVerifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean tlsEndpointChecking;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public POP3SClient() {
/*  99 */     this("TLS", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public POP3SClient(boolean implicit) {
/* 108 */     this("TLS", implicit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public POP3SClient(String proto) {
/* 118 */     this(proto, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public POP3SClient(String proto, boolean implicit) {
/* 128 */     this(proto, implicit, (SSLContext)null);
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
/*     */   public POP3SClient(String proto, boolean implicit, SSLContext ctx) {
/* 141 */     this.protocol = proto;
/* 142 */     this.isImplicit = implicit;
/* 143 */     this.context = ctx;
/* 144 */     if (this.isImplicit) {
/* 145 */       setDefaultPort(995);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public POP3SClient(boolean implicit, SSLContext ctx) {
/* 156 */     this("TLS", implicit, ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public POP3SClient(SSLContext context) {
/* 166 */     this(false, context);
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
/* 181 */     if (this.isImplicit) {
/* 182 */       performSSLNegotiation();
/*     */     }
/* 184 */     super._connectAction_();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initSSLContext() throws IOException {
/* 194 */     if (this.context == null)
/*     */     {
/* 196 */       this.context = SSLContextUtils.createSSLContext(this.protocol, getKeyManager(), getTrustManager());
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
/* 207 */     initSSLContext();
/*     */     
/* 209 */     SSLSocketFactory ssf = this.context.getSocketFactory();
/* 210 */     String host = (this._hostname_ != null) ? this._hostname_ : getRemoteAddress().getHostAddress();
/* 211 */     int port = getRemotePort();
/* 212 */     SSLSocket socket = (SSLSocket)ssf.createSocket(this._socket_, host, port, true);
/*     */     
/* 214 */     socket.setEnableSessionCreation(true);
/* 215 */     socket.setUseClientMode(true);
/*     */     
/* 217 */     if (this.tlsEndpointChecking) {
/* 218 */       SSLSocketUtils.enableEndpointNameVerification(socket);
/*     */     }
/*     */     
/* 221 */     if (this.protocols != null) {
/* 222 */       socket.setEnabledProtocols(this.protocols);
/*     */     }
/* 224 */     if (this.suites != null) {
/* 225 */       socket.setEnabledCipherSuites(this.suites);
/*     */     }
/* 227 */     socket.startHandshake();
/*     */ 
/*     */     
/* 230 */     this._socket_ = socket;
/* 231 */     this._input_ = socket.getInputStream();
/* 232 */     this._output_ = socket.getOutputStream();
/* 233 */     this._reader = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
/* 234 */     this._writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
/*     */     
/* 236 */     if (this.hostnameVerifier != null && !this.hostnameVerifier.verify(host, socket.getSession())) {
/* 237 */       throw new SSLHandshakeException("Hostname doesn't match certificate");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyManager getKeyManager() {
/* 247 */     return this.keyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManager(KeyManager newKeyManager) {
/* 257 */     this.keyManager = newKeyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledCipherSuites(String[] cipherSuites) {
/* 267 */     this.suites = new String[cipherSuites.length];
/* 268 */     System.arraycopy(cipherSuites, 0, this.suites, 0, cipherSuites.length);
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
/* 279 */     if (this._socket_ instanceof SSLSocket)
/*     */     {
/* 281 */       return ((SSLSocket)this._socket_).getEnabledCipherSuites();
/*     */     }
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledProtocols(String[] protocolVersions) {
/* 293 */     this.protocols = new String[protocolVersions.length];
/* 294 */     System.arraycopy(protocolVersions, 0, this.protocols, 0, protocolVersions.length);
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
/* 305 */     if (this._socket_ instanceof SSLSocket)
/*     */     {
/* 307 */       return ((SSLSocket)this._socket_).getEnabledProtocols();
/*     */     }
/* 309 */     return null;
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
/*     */   public boolean execTLS() throws SSLException, IOException {
/* 321 */     if (sendCommand("STLS") != 0)
/*     */     {
/* 323 */       return false;
/*     */     }
/*     */     
/* 326 */     performSSLNegotiation();
/* 327 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrustManager getTrustManager() {
/* 336 */     return this.trustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManager(TrustManager newTrustManager) {
/* 346 */     this.trustManager = newTrustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 356 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostnameVerifier(HostnameVerifier newHostnameVerifier) {
/* 366 */     this.hostnameVerifier = newHostnameVerifier;
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
/* 378 */     return this.tlsEndpointChecking;
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
/* 390 */     this.tlsEndpointChecking = enable;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\pop3\POP3SClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */