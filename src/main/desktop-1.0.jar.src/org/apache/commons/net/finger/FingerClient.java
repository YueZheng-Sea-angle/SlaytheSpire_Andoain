/*     */ package org.apache.commons.net.finger;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import org.apache.commons.net.SocketClient;
/*     */ import org.apache.commons.net.util.Charsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FingerClient
/*     */   extends SocketClient
/*     */ {
/*     */   public static final int DEFAULT_PORT = 79;
/*     */   private static final String __LONG_FLAG = "/W ";
/*  62 */   private transient char[] __buffer = new char[1024];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FingerClient() {
/*  70 */     setDefaultPort(79);
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
/*     */   public String query(boolean longOutput, String username) throws IOException {
/*  87 */     StringBuilder result = new StringBuilder(this.__buffer.length);
/*     */ 
/*     */     
/*  90 */     BufferedReader input = new BufferedReader(new InputStreamReader(getInputStream(longOutput, username), getCharset()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*     */       while (true) {
/*  97 */         int read = input.read(this.__buffer, 0, this.__buffer.length);
/*  98 */         if (read <= 0) {
/*     */           break;
/*     */         }
/* 101 */         result.append(this.__buffer, 0, read);
/*     */       } 
/*     */     } finally {
/* 104 */       input.close();
/*     */     } 
/*     */     
/* 107 */     return result.toString();
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
/*     */   public String query(boolean longOutput) throws IOException {
/* 123 */     return query(longOutput, "");
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
/*     */   public InputStream getInputStream(boolean longOutput, String username) throws IOException {
/* 142 */     return getInputStream(longOutput, username, (String)null);
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
/*     */   public InputStream getInputStream(boolean longOutput, String username, String encoding) throws IOException {
/* 163 */     StringBuilder buffer = new StringBuilder(64);
/* 164 */     if (longOutput) {
/* 165 */       buffer.append("/W ");
/*     */     }
/* 167 */     buffer.append(username);
/* 168 */     buffer.append("\r\n");
/*     */ 
/*     */     
/* 171 */     byte[] encodedQuery = buffer.toString().getBytes(Charsets.toCharset(encoding).name());
/*     */     
/* 173 */     DataOutputStream output = new DataOutputStream(new BufferedOutputStream(this._output_, 1024));
/* 174 */     output.write(encodedQuery, 0, encodedQuery.length);
/* 175 */     output.flush();
/*     */     
/* 177 */     return this._input_;
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
/*     */   public InputStream getInputStream(boolean longOutput) throws IOException {
/* 195 */     return getInputStream(longOutput, "");
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\finger\FingerClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */