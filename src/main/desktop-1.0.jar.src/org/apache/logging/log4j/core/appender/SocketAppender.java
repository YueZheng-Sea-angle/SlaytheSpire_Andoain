/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
/*     */ import org.apache.logging.log4j.core.net.AbstractSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.net.DatagramSocketManager;
/*     */ import org.apache.logging.log4j.core.net.Protocol;
/*     */ import org.apache.logging.log4j.core.net.SocketOptions;
/*     */ import org.apache.logging.log4j.core.net.SslSocketManager;
/*     */ import org.apache.logging.log4j.core.net.TcpSocketManager;
/*     */ import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Socket", category = "Core", elementType = "appender", printObject = true)
/*     */ public class SocketAppender
/*     */   extends AbstractOutputStreamAppender<AbstractSocketManager>
/*     */ {
/*     */   private final Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static abstract class AbstractBuilder<B extends AbstractBuilder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute
/*     */     private int connectTimeoutMillis;
/*     */     @PluginBuilderAttribute
/*     */     @ValidHost
/*  80 */     private String host = "localhost";
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean immediateFail = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @ValidPort
/*     */     private int port;
/*     */     
/*     */     @PluginBuilderAttribute
/*  91 */     private Protocol protocol = Protocol.TCP;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @PluginAliases({"reconnectDelay", "reconnectionDelay", "delayMillis", "reconnectionDelayMillis"})
/*     */     private int reconnectDelayMillis;
/*     */     
/*     */     @PluginElement("SocketOptions")
/*     */     private SocketOptions socketOptions;
/*     */     
/*     */     @PluginElement("SslConfiguration")
/*     */     @PluginAliases({"SslConfig"})
/*     */     private SslConfiguration sslConfiguration;
/*     */ 
/*     */     
/*     */     public boolean getAdvertise() {
/* 106 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public int getConnectTimeoutMillis() {
/* 110 */       return this.connectTimeoutMillis;
/*     */     }
/*     */     
/*     */     public String getHost() {
/* 114 */       return this.host;
/*     */     }
/*     */     
/*     */     public int getPort() {
/* 118 */       return this.port;
/*     */     }
/*     */     
/*     */     public Protocol getProtocol() {
/* 122 */       return this.protocol;
/*     */     }
/*     */     
/*     */     public SslConfiguration getSslConfiguration() {
/* 126 */       return this.sslConfiguration;
/*     */     }
/*     */     
/*     */     public boolean getImmediateFail() {
/* 130 */       return this.immediateFail;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 134 */       this.advertise = advertise;
/* 135 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withConnectTimeoutMillis(int connectTimeoutMillis) {
/* 139 */       this.connectTimeoutMillis = connectTimeoutMillis;
/* 140 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withHost(String host) {
/* 144 */       this.host = host;
/* 145 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withImmediateFail(boolean immediateFail) {
/* 149 */       this.immediateFail = immediateFail;
/* 150 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withPort(int port) {
/* 154 */       this.port = port;
/* 155 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withProtocol(Protocol protocol) {
/* 159 */       this.protocol = protocol;
/* 160 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withReconnectDelayMillis(int reconnectDelayMillis) {
/* 164 */       this.reconnectDelayMillis = reconnectDelayMillis;
/* 165 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withSocketOptions(SocketOptions socketOptions) {
/* 169 */       this.socketOptions = socketOptions;
/* 170 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withSslConfiguration(SslConfiguration sslConfiguration) {
/* 174 */       this.sslConfiguration = sslConfiguration;
/* 175 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public int getReconnectDelayMillis() {
/* 179 */       return this.reconnectDelayMillis;
/*     */     }
/*     */     
/*     */     public SocketOptions getSocketOptions() {
/* 183 */       return this.socketOptions;
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
/*     */   public static class Builder
/*     */     extends AbstractBuilder<Builder>
/*     */     implements org.apache.logging.log4j.core.util.Builder<SocketAppender>
/*     */   {
/*     */     public SocketAppender build() {
/* 201 */       boolean immediateFlush = isImmediateFlush();
/* 202 */       boolean bufferedIo = isBufferedIo();
/* 203 */       Layout<? extends Serializable> layout = getLayout();
/* 204 */       if (layout == null) {
/* 205 */         SocketAppender.LOGGER.error("No layout provided for SocketAppender");
/* 206 */         return null;
/*     */       } 
/*     */       
/* 209 */       String name = getName();
/* 210 */       if (name == null) {
/* 211 */         SocketAppender.LOGGER.error("No name provided for SocketAppender");
/* 212 */         return null;
/*     */       } 
/*     */       
/* 215 */       Protocol protocol = getProtocol();
/* 216 */       Protocol actualProtocol = (protocol != null) ? protocol : Protocol.TCP;
/* 217 */       if (actualProtocol == Protocol.UDP) {
/* 218 */         immediateFlush = true;
/*     */       }
/*     */       
/* 221 */       AbstractSocketManager manager = SocketAppender.createSocketManager(name, actualProtocol, getHost(), getPort(), 
/* 222 */           getConnectTimeoutMillis(), getSslConfiguration(), getReconnectDelayMillis(), getImmediateFail(), layout, getBufferSize(), getSocketOptions());
/*     */       
/* 224 */       return new SocketAppender(name, layout, getFilter(), manager, isIgnoreExceptions(), (!bufferedIo || immediateFlush), 
/* 225 */           getAdvertise() ? getConfiguration().getAdvertiser() : null, 
/* 226 */           getPropertyArray());
/*     */     }
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/* 232 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser, Property[] properties) {
/* 241 */     super(name, layout, filter, ignoreExceptions, immediateFlush, properties, manager);
/* 242 */     if (advertiser != null) {
/* 243 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 244 */       configuration.putAll(manager.getContentFormat());
/* 245 */       configuration.put("contentType", layout.getContentType());
/* 246 */       configuration.put("name", name);
/* 247 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 249 */       this.advertisement = null;
/*     */     } 
/* 251 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SocketAppender(String name, Layout<? extends Serializable> layout, Filter filter, AbstractSocketManager manager, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/* 261 */     this(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser, Property.EMPTY_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 266 */     setStopping();
/* 267 */     stop(timeout, timeUnit, false);
/* 268 */     if (this.advertiser != null) {
/* 269 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 271 */     setStopped();
/* 272 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   @PluginFactory
/*     */   public static SocketAppender createAppender(String host, int port, Protocol protocol, SslConfiguration sslConfig, int connectTimeoutMillis, int reconnectDelayMillis, boolean immediateFail, String name, boolean immediateFlush, boolean ignoreExceptions, Layout<? extends Serializable> layout, Filter filter, boolean advertise, Configuration configuration) {
/* 331 */     return ((Builder)newBuilder()
/* 332 */       .withAdvertise(advertise)
/* 333 */       .setConfiguration(configuration)
/* 334 */       .withConnectTimeoutMillis(connectTimeoutMillis).setFilter(filter))
/* 335 */       .withHost(host).setIgnoreExceptions(ignoreExceptions)
/* 336 */       .withImmediateFail(immediateFail).setLayout(layout).setName(name)
/* 337 */       .withPort(port)
/* 338 */       .withProtocol(protocol)
/* 339 */       .withReconnectDelayMillis(reconnectDelayMillis)
/* 340 */       .withSslConfiguration(sslConfig)
/* 341 */       .build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static SocketAppender createAppender(String host, String portNum, String protocolIn, SslConfiguration sslConfig, int connectTimeoutMillis, String delayMillis, String immediateFail, String name, String immediateFlush, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, Configuration config) {
/* 399 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 400 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 401 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 402 */     boolean fail = Booleans.parseBoolean(immediateFail, true);
/* 403 */     int reconnectDelayMillis = AbstractAppender.parseInt(delayMillis, 0);
/* 404 */     int port = AbstractAppender.parseInt(portNum, 0);
/* 405 */     Protocol p = (protocolIn == null) ? Protocol.UDP : Protocol.valueOf(protocolIn);
/* 406 */     return createAppender(host, port, p, sslConfig, connectTimeoutMillis, reconnectDelayMillis, fail, name, isFlush, ignoreExceptions, layout, filter, isAdvertise, config);
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
/*     */   @Deprecated
/*     */   protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, int connectTimeoutMillis, SslConfiguration sslConfig, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize) {
/* 421 */     return createSocketManager(name, protocol, host, port, connectTimeoutMillis, sslConfig, reconnectDelayMillis, immediateFail, layout, bufferSize, (SocketOptions)null);
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
/*     */   protected static AbstractSocketManager createSocketManager(String name, Protocol protocol, String host, int port, int connectTimeoutMillis, SslConfiguration sslConfig, int reconnectDelayMillis, boolean immediateFail, Layout<? extends Serializable> layout, int bufferSize, SocketOptions socketOptions) {
/* 434 */     if (protocol == Protocol.TCP && sslConfig != null)
/*     */     {
/* 436 */       protocol = Protocol.SSL;
/*     */     }
/* 438 */     if (protocol != Protocol.SSL && sslConfig != null) {
/* 439 */       LOGGER.info("Appender {} ignoring SSL configuration for {} protocol", name, protocol);
/*     */     }
/* 441 */     switch (protocol) {
/*     */       case TCP:
/* 443 */         return (AbstractSocketManager)TcpSocketManager.getSocketManager(host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
/*     */       
/*     */       case UDP:
/* 446 */         return (AbstractSocketManager)DatagramSocketManager.getSocketManager(host, port, layout, bufferSize);
/*     */       case SSL:
/* 448 */         return (AbstractSocketManager)SslSocketManager.getSocketManager(sslConfig, host, port, connectTimeoutMillis, reconnectDelayMillis, immediateFail, layout, bufferSize, socketOptions);
/*     */     } 
/*     */     
/* 451 */     throw new IllegalArgumentException(protocol.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void directEncodeEvent(LogEvent event) {
/* 459 */     writeByteArrayToManager(event);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\SocketAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */