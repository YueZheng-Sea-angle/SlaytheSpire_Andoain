/*    */ package org.apache.commons.net.smtp;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import java.util.Vector;
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
/*    */ public final class RelayPath
/*    */ {
/*    */   Vector<String> _path;
/*    */   String _emailAddress;
/*    */   
/*    */   public RelayPath(String emailAddress) {
/* 45 */     this._path = new Vector<String>();
/* 46 */     this._emailAddress = emailAddress;
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
/*    */   public void addRelay(String hostname) {
/* 63 */     this._path.addElement(hostname);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     StringBuilder buffer = new StringBuilder();
/*    */ 
/*    */     
/* 77 */     buffer.append('<');
/*    */     
/* 79 */     Enumeration<String> hosts = this._path.elements();
/*    */     
/* 81 */     if (hosts.hasMoreElements()) {
/*    */       
/* 83 */       buffer.append('@');
/* 84 */       buffer.append(hosts.nextElement());
/*    */       
/* 86 */       while (hosts.hasMoreElements()) {
/*    */         
/* 88 */         buffer.append(",@");
/* 89 */         buffer.append(hosts.nextElement());
/*    */       } 
/* 91 */       buffer.append(':');
/*    */     } 
/*    */     
/* 94 */     buffer.append(this._emailAddress);
/* 95 */     buffer.append('>');
/*    */     
/* 97 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\smtp\RelayPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */