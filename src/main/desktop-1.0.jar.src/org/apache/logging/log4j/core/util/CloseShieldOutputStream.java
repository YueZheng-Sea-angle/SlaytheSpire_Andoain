/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public class CloseShieldOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final OutputStream delegate;
/*    */   
/*    */   public CloseShieldOutputStream(OutputStream delegate) {
/* 30 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 43 */     this.delegate.flush();
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 48 */     this.delegate.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 53 */     this.delegate.write(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 58 */     this.delegate.write(b);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\CloseShieldOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */