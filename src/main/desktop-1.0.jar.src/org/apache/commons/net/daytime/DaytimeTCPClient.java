/*    */ package org.apache.commons.net.daytime;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.commons.net.SocketClient;
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
/*    */ public final class DaytimeTCPClient
/*    */   extends SocketClient
/*    */ {
/*    */   public static final int DEFAULT_PORT = 13;
/* 44 */   private final char[] __buffer = new char[64];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DaytimeTCPClient() {
/* 52 */     setDefaultPort(13);
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
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTime() throws IOException {
/* 71 */     StringBuilder result = new StringBuilder(this.__buffer.length);
/*    */ 
/*    */     
/* 74 */     BufferedReader reader = new BufferedReader(new InputStreamReader(this._input_, getCharset()));
/*    */ 
/*    */     
/*    */     while (true) {
/* 78 */       int read = reader.read(this.__buffer, 0, this.__buffer.length);
/* 79 */       if (read <= 0) {
/*    */         break;
/*    */       }
/* 82 */       result.append(this.__buffer, 0, read);
/*    */     } 
/*    */     
/* 85 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\daytime\DaytimeTCPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */