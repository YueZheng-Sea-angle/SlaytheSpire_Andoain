/*     */ package org.apache.commons.net.imap;
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
/*     */ public class IMAPSClient
/*     */   extends IMAPClient
/*     */ {
/*     */   public static final int DEFAULT_IMAPS_PORT = 993;
/*     */   public static final String DEFAULT_PROTOCOL = "TLS";
/*     */   private final boolean isImplicit;
/*     */   private final String protocol;
/*  69 */   private SSLContext context = null;
/*     */ 
/*     */   
/*  72 */   private String[] suites = null;
/*     */   
/*  74 */   private String[] protocols = null;
/*     */ 
/*     */ 
/*     */   
/*  78 */   private TrustManager trustManager = null;
/*     */ 
/*     */   
/*  81 */   private KeyManager keyManager = null;
/*     */ 
/*     */   
/*  84 */   private HostnameVerifier hostnameVerifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean tlsEndpointChecking;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPSClient() {
/*  95 */     this("TLS", false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPSClient(boolean implicit) {
/* 104 */     this("TLS", implicit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPSClient(String proto) {
/* 113 */     this(proto, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPSClient(String proto, boolean implicit) {
/* 123 */     this(proto, implicit, (SSLContext)null);
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
/*     */   public IMAPSClient(String proto, boolean implicit, SSLContext ctx) {
/* 135 */     setDefaultPort(993);
/* 136 */     this.protocol = proto;
/* 137 */     this.isImplicit = implicit;
/* 138 */     this.context = ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPSClient(boolean implicit, SSLContext ctx) {
/* 148 */     this("TLS", implicit, ctx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IMAPSClient(SSLContext context) {
/* 157 */     this(false, context);
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
/* 172 */     if (this.isImplicit) {
/* 173 */       performSSLNegotiation();
/*     */     }
/* 175 */     super._connectAction_();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initSSLContext() throws IOException {
/* 185 */     if (this.context == null)
/*     */     {
/* 187 */       this.context = SSLContextUtils.createSSLContext(this.protocol, getKeyManager(), getTrustManager());
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
/* 198 */     initSSLContext();
/*     */     
/* 200 */     SSLSocketFactory ssf = this.context.getSocketFactory();
/* 201 */     String host = (this._hostname_ != null) ? this._hostname_ : getRemoteAddress().getHostAddress();
/* 202 */     int port = getRemotePort();
/* 203 */     SSLSocket socket = (SSLSocket)ssf.createSocket(this._socket_, host, port, true);
/*     */     
/* 205 */     socket.setEnableSessionCreation(true);
/* 206 */     socket.setUseClientMode(true);
/*     */     
/* 208 */     if (this.tlsEndpointChecking) {
/* 209 */       SSLSocketUtils.enableEndpointNameVerification(socket);
/*     */     }
/*     */     
/* 212 */     if (this.protocols != null) {
/* 213 */       socket.setEnabledProtocols(this.protocols);
/*     */     }
/* 215 */     if (this.suites != null) {
/* 216 */       socket.setEnabledCipherSuites(this.suites);
/*     */     }
/* 218 */     socket.startHandshake();
/*     */ 
/*     */     
/* 221 */     this._socket_ = socket;
/* 222 */     this._input_ = socket.getInputStream();
/* 223 */     this._output_ = socket.getOutputStream();
/* 224 */     this._reader = (BufferedReader)new CRLFLineReader(new InputStreamReader(this._input_, "ISO-8859-1"));
/*     */ 
/*     */     
/* 227 */     this.__writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (this.hostnameVerifier != null && !this.hostnameVerifier.verify(host, socket.getSession())) {
/* 232 */       throw new SSLHandshakeException("Hostname doesn't match certificate");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyManager getKeyManager() {
/* 242 */     return this.keyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyManager(KeyManager newKeyManager) {
/* 252 */     this.keyManager = newKeyManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledCipherSuites(String[] cipherSuites) {
/* 262 */     this.suites = new String[cipherSuites.length];
/* 263 */     System.arraycopy(cipherSuites, 0, this.suites, 0, cipherSuites.length);
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
/* 274 */     if (this._socket_ instanceof SSLSocket)
/*     */     {
/* 276 */       return ((SSLSocket)this._socket_).getEnabledCipherSuites();
/*     */     }
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabledProtocols(String[] protocolVersions) {
/* 288 */     this.protocols = new String[protocolVersions.length];
/* 289 */     System.arraycopy(protocolVersions, 0, this.protocols, 0, protocolVersions.length);
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
/* 300 */     if (this._socket_ instanceof SSLSocket)
/*     */     {
/* 302 */       return ((SSLSocket)this._socket_).getEnabledProtocols();
/*     */     }
/* 304 */     return null;
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
/* 316 */     if (sendCommand(IMAPCommand.getCommand(IMAPCommand.STARTTLS)) != 0)
/*     */     {
/* 318 */       return false;
/*     */     }
/*     */     
/* 321 */     performSSLNegotiation();
/* 322 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrustManager getTrustManager() {
/* 331 */     return this.trustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrustManager(TrustManager newTrustManager) {
/* 341 */     this.trustManager = newTrustManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 351 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHostnameVerifier(HostnameVerifier newHostnameVerifier) {
/* 361 */     this.hostnameVerifier = newHostnameVerifier;
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
/* 373 */     return this.tlsEndpointChecking;
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
/* 385 */     this.tlsEndpointChecking = enable;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\imap\IMAPSClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */