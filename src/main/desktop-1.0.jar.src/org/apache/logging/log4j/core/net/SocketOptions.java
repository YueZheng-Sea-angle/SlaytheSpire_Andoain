/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.util.Builder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "SocketOptions", category = "Core", printObject = true)
/*     */ public class SocketOptions
/*     */   implements Builder<SocketOptions>, Cloneable
/*     */ {
/*     */   @PluginBuilderAttribute
/*     */   private Boolean keepAlive;
/*     */   @PluginBuilderAttribute
/*     */   private Boolean oobInline;
/*     */   @PluginElement("PerformancePreferences")
/*     */   private SocketPerformancePreferences performancePreferences;
/*     */   @PluginBuilderAttribute
/*     */   private Integer receiveBufferSize;
/*     */   @PluginBuilderAttribute
/*     */   private Boolean reuseAddress;
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static SocketOptions newBuilder() {
/*  37 */     return new SocketOptions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderAttribute
/*     */   private Rfc1349TrafficClass rfc1349TrafficClass;
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderAttribute
/*     */   private Integer sendBufferSize;
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderAttribute
/*     */   private Integer soLinger;
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderAttribute
/*     */   private Integer soTimeout;
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderAttribute
/*     */   private Boolean tcpNoDelay;
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderAttribute
/*     */   private Integer trafficClass;
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(Socket socket) throws SocketException {
/*  74 */     if (this.keepAlive != null) {
/*  75 */       socket.setKeepAlive(this.keepAlive.booleanValue());
/*     */     }
/*  77 */     if (this.oobInline != null) {
/*  78 */       socket.setOOBInline(this.oobInline.booleanValue());
/*     */     }
/*  80 */     if (this.reuseAddress != null) {
/*  81 */       socket.setReuseAddress(this.reuseAddress.booleanValue());
/*     */     }
/*  83 */     if (this.performancePreferences != null) {
/*  84 */       this.performancePreferences.apply(socket);
/*     */     }
/*  86 */     if (this.receiveBufferSize != null) {
/*  87 */       socket.setReceiveBufferSize(this.receiveBufferSize.intValue());
/*     */     }
/*  89 */     if (this.soLinger != null) {
/*  90 */       socket.setSoLinger(true, this.soLinger.intValue());
/*     */     }
/*  92 */     if (this.soTimeout != null) {
/*  93 */       socket.setSoTimeout(this.soTimeout.intValue());
/*     */     }
/*  95 */     if (this.tcpNoDelay != null) {
/*  96 */       socket.setTcpNoDelay(this.tcpNoDelay.booleanValue());
/*     */     }
/*  98 */     Integer actualTrafficClass = getActualTrafficClass();
/*  99 */     if (actualTrafficClass != null) {
/* 100 */       socket.setTrafficClass(actualTrafficClass.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketOptions build() {
/*     */     try {
/* 107 */       return (SocketOptions)clone();
/* 108 */     } catch (CloneNotSupportedException e) {
/* 109 */       throw new IllegalStateException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Integer getActualTrafficClass() {
/* 114 */     if (this.trafficClass != null && this.rfc1349TrafficClass != null) {
/* 115 */       throw new IllegalStateException("You MUST not set both customTrafficClass and trafficClass.");
/*     */     }
/* 117 */     if (this.trafficClass != null) {
/* 118 */       return this.trafficClass;
/*     */     }
/* 120 */     if (this.rfc1349TrafficClass != null) {
/* 121 */       return Integer.valueOf(this.rfc1349TrafficClass.value());
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public SocketPerformancePreferences getPerformancePreferences() {
/* 127 */     return this.performancePreferences;
/*     */   }
/*     */   
/*     */   public Integer getReceiveBufferSize() {
/* 131 */     return this.receiveBufferSize;
/*     */   }
/*     */   
/*     */   public Rfc1349TrafficClass getRfc1349TrafficClass() {
/* 135 */     return this.rfc1349TrafficClass;
/*     */   }
/*     */   
/*     */   public Integer getSendBufferSize() {
/* 139 */     return this.sendBufferSize;
/*     */   }
/*     */   
/*     */   public Integer getSoLinger() {
/* 143 */     return this.soLinger;
/*     */   }
/*     */   
/*     */   public Integer getSoTimeout() {
/* 147 */     return this.soTimeout;
/*     */   }
/*     */   
/*     */   public Integer getTrafficClass() {
/* 151 */     return this.trafficClass;
/*     */   }
/*     */   
/*     */   public Boolean isKeepAlive() {
/* 155 */     return this.keepAlive;
/*     */   }
/*     */   
/*     */   public Boolean isOobInline() {
/* 159 */     return this.oobInline;
/*     */   }
/*     */   
/*     */   public Boolean isReuseAddress() {
/* 163 */     return this.reuseAddress;
/*     */   }
/*     */   
/*     */   public Boolean isTcpNoDelay() {
/* 167 */     return this.tcpNoDelay;
/*     */   }
/*     */   
/*     */   public SocketOptions setKeepAlive(boolean keepAlive) {
/* 171 */     this.keepAlive = Boolean.valueOf(keepAlive);
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setOobInline(boolean oobInline) {
/* 176 */     this.oobInline = Boolean.valueOf(oobInline);
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setPerformancePreferences(SocketPerformancePreferences performancePreferences) {
/* 181 */     this.performancePreferences = performancePreferences;
/* 182 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setReceiveBufferSize(int receiveBufferSize) {
/* 186 */     this.receiveBufferSize = Integer.valueOf(receiveBufferSize);
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setReuseAddress(boolean reuseAddress) {
/* 191 */     this.reuseAddress = Boolean.valueOf(reuseAddress);
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setRfc1349TrafficClass(Rfc1349TrafficClass trafficClass) {
/* 196 */     this.rfc1349TrafficClass = trafficClass;
/* 197 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setSendBufferSize(int sendBufferSize) {
/* 201 */     this.sendBufferSize = Integer.valueOf(sendBufferSize);
/* 202 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setSoLinger(int soLinger) {
/* 206 */     this.soLinger = Integer.valueOf(soLinger);
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setSoTimeout(int soTimeout) {
/* 211 */     this.soTimeout = Integer.valueOf(soTimeout);
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setTcpNoDelay(boolean tcpNoDelay) {
/* 216 */     this.tcpNoDelay = Boolean.valueOf(tcpNoDelay);
/* 217 */     return this;
/*     */   }
/*     */   
/*     */   public SocketOptions setTrafficClass(int trafficClass) {
/* 221 */     this.trafficClass = Integer.valueOf(trafficClass);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 227 */     return "SocketOptions [keepAlive=" + this.keepAlive + ", oobInline=" + this.oobInline + ", performancePreferences=" + this.performancePreferences + ", receiveBufferSize=" + this.receiveBufferSize + ", reuseAddress=" + this.reuseAddress + ", rfc1349TrafficClass=" + this.rfc1349TrafficClass + ", sendBufferSize=" + this.sendBufferSize + ", soLinger=" + this.soLinger + ", soTimeout=" + this.soTimeout + ", tcpNoDelay=" + this.tcpNoDelay + ", trafficClass=" + this.trafficClass + "]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\SocketOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */