/*    */ package org.apache.logging.log4j.core.appender;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Objects;
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
/*    */ public class TlsSyslogFrame
/*    */ {
/*    */   private final String message;
/*    */   private final int byteLength;
/*    */   
/*    */   public TlsSyslogFrame(String message) {
/* 34 */     this.message = message;
/* 35 */     byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
/* 36 */     this.byteLength = messageBytes.length;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 40 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return Integer.toString(this.byteLength) + ' ' + this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     return 31 + Objects.hashCode(this.message);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 55 */     if (this == obj) {
/* 56 */       return true;
/*    */     }
/* 58 */     if (obj == null) {
/* 59 */       return false;
/*    */     }
/* 61 */     if (!(obj instanceof TlsSyslogFrame)) {
/* 62 */       return false;
/*    */     }
/* 64 */     TlsSyslogFrame other = (TlsSyslogFrame)obj;
/* 65 */     if (!Objects.equals(this.message, other.message)) {
/* 66 */       return false;
/*    */     }
/* 68 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\appender\TlsSyslogFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */