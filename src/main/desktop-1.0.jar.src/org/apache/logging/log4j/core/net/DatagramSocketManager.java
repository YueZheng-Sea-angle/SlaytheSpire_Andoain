/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatagramSocketManager
/*     */   extends AbstractSocketManager
/*     */ {
/*  35 */   private static final DatagramSocketManagerFactory FACTORY = new DatagramSocketManagerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DatagramSocketManager(String name, OutputStream os, InetAddress inetAddress, String host, int port, Layout<? extends Serializable> layout, int bufferSize) {
/*  49 */     super(name, os, inetAddress, host, port, layout, true, bufferSize);
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
/*     */   public static DatagramSocketManager getSocketManager(String host, int port, Layout<? extends Serializable> layout, int bufferSize) {
/*  62 */     if (Strings.isEmpty(host)) {
/*  63 */       throw new IllegalArgumentException("A host name is required");
/*     */     }
/*  65 */     if (port <= 0) {
/*  66 */       throw new IllegalArgumentException("A port value is required");
/*     */     }
/*  68 */     return (DatagramSocketManager)getManager("UDP:" + host + ':' + port, new FactoryData(host, port, layout, bufferSize), FACTORY);
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
/*     */   public Map<String, String> getContentFormat() {
/*  83 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/*  84 */     result.put("protocol", "udp");
/*  85 */     result.put("direction", "out");
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FactoryData
/*     */   {
/*     */     private final String host;
/*     */     
/*     */     private final int port;
/*     */     private final Layout<? extends Serializable> layout;
/*     */     private final int bufferSize;
/*     */     
/*     */     public FactoryData(String host, int port, Layout<? extends Serializable> layout, int bufferSize) {
/*  99 */       this.host = host;
/* 100 */       this.port = port;
/* 101 */       this.layout = layout;
/* 102 */       this.bufferSize = bufferSize;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class DatagramSocketManagerFactory
/*     */     implements ManagerFactory<DatagramSocketManager, FactoryData>
/*     */   {
/*     */     private DatagramSocketManagerFactory() {}
/*     */     
/*     */     public DatagramSocketManager createManager(String name, DatagramSocketManager.FactoryData data) {
/*     */       InetAddress inetAddress;
/*     */       try {
/* 115 */         inetAddress = InetAddress.getByName(data.host);
/* 116 */       } catch (UnknownHostException ex) {
/* 117 */         DatagramSocketManager.LOGGER.error("Could not find address of " + data.host, ex);
/* 118 */         return null;
/*     */       } 
/*     */       
/* 121 */       OutputStream os = new DatagramOutputStream(data.host, data.port, data.layout.getHeader(), data.layout.getFooter());
/* 122 */       return new DatagramSocketManager(name, os, inetAddress, data.host, data.port, data.layout, data.bufferSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\DatagramSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */