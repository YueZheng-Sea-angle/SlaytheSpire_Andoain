/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.net.Socket;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*    */ import org.apache.logging.log4j.core.util.Builder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Plugin(name = "SocketPerformancePreferences", category = "Core", printObject = true)
/*    */ public class SocketPerformancePreferences
/*    */   implements Builder<SocketPerformancePreferences>, Cloneable
/*    */ {
/*    */   @PluginBuilderAttribute
/*    */   @Required
/*    */   private int bandwidth;
/*    */   @PluginBuilderAttribute
/*    */   @Required
/*    */   private int connectionTime;
/*    */   @PluginBuilderAttribute
/*    */   @Required
/*    */   private int latency;
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static SocketPerformancePreferences newBuilder() {
/* 38 */     return new SocketPerformancePreferences();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void apply(Socket socket) {
/* 54 */     socket.setPerformancePreferences(this.connectionTime, this.latency, this.bandwidth);
/*    */   }
/*    */ 
/*    */   
/*    */   public SocketPerformancePreferences build() {
/*    */     try {
/* 60 */       return (SocketPerformancePreferences)clone();
/* 61 */     } catch (CloneNotSupportedException e) {
/* 62 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getBandwidth() {
/* 67 */     return this.bandwidth;
/*    */   }
/*    */   
/*    */   public int getConnectionTime() {
/* 71 */     return this.connectionTime;
/*    */   }
/*    */   
/*    */   public int getLatency() {
/* 75 */     return this.latency;
/*    */   }
/*    */   
/*    */   public void setBandwidth(int bandwidth) {
/* 79 */     this.bandwidth = bandwidth;
/*    */   }
/*    */   
/*    */   public void setConnectionTime(int connectionTime) {
/* 83 */     this.connectionTime = connectionTime;
/*    */   }
/*    */   
/*    */   public void setLatency(int latency) {
/* 87 */     this.latency = latency;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return "SocketPerformancePreferences [bandwidth=" + this.bandwidth + ", connectionTime=" + this.connectionTime + ", latency=" + this.latency + "]";
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\SocketPerformancePreferences.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */