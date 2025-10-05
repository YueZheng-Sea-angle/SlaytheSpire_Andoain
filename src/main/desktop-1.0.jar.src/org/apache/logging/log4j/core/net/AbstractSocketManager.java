/*    */ package org.apache.logging.log4j.core.net;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.net.InetAddress;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.Layout;
/*    */ import org.apache.logging.log4j.core.appender.OutputStreamManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSocketManager
/*    */   extends OutputStreamManager
/*    */ {
/*    */   protected final InetAddress inetAddress;
/*    */   protected final String host;
/*    */   protected final int port;
/*    */   
/*    */   public AbstractSocketManager(String name, OutputStream os, InetAddress inetAddress, String host, int port, Layout<? extends Serializable> layout, boolean writeHeader, int bufferSize) {
/* 60 */     super(os, name, layout, writeHeader, bufferSize);
/* 61 */     this.inetAddress = inetAddress;
/* 62 */     this.host = host;
/* 63 */     this.port = port;
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
/*    */   public Map<String, String> getContentFormat() {
/* 77 */     Map<String, String> result = new HashMap<>(super.getContentFormat());
/* 78 */     result.put("port", Integer.toString(this.port));
/* 79 */     result.put("address", this.inetAddress.getHostAddress());
/* 80 */     return result;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\AbstractSocketManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */