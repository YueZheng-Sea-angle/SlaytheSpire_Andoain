/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SocketAddress", category = "Core", printObject = true)
/*     */ public class SocketAddress
/*     */ {
/*     */   private final InetSocketAddress socketAddress;
/*     */   
/*     */   public static SocketAddress getLoopback() {
/*  43 */     return new SocketAddress(InetAddress.getLoopbackAddress(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketAddress(InetAddress host, int port) {
/*  50 */     this.socketAddress = new InetSocketAddress(host, port);
/*     */   }
/*     */   
/*     */   public InetSocketAddress getSocketAddress() {
/*  54 */     return this.socketAddress;
/*     */   }
/*     */   
/*     */   public int getPort() {
/*  58 */     return this.socketAddress.getPort();
/*     */   }
/*     */   
/*     */   public InetAddress getAddress() {
/*  62 */     return this.socketAddress.getAddress();
/*     */   }
/*     */   
/*     */   public String getHostName() {
/*  66 */     return this.socketAddress.getHostName();
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  71 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<SocketAddress>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @ValidHost
/*     */     private InetAddress host;
/*     */     @PluginBuilderAttribute
/*     */     @ValidPort
/*     */     private int port;
/*     */     
/*     */     public Builder setHost(InetAddress host) {
/*  85 */       this.host = host;
/*  86 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPort(int port) {
/*  90 */       this.port = port;
/*  91 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public SocketAddress build() {
/*  96 */       return new SocketAddress(this.host, this.port);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return this.socketAddress.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\SocketAddress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */