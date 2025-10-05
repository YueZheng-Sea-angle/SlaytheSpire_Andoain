/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
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
/*    */ public class CloseShieldWriter
/*    */   extends Writer
/*    */ {
/*    */   private final Writer delegate;
/*    */   
/*    */   public CloseShieldWriter(Writer delegate) {
/* 27 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/* 37 */     this.delegate.flush();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 43 */     this.delegate.write(cbuf, off, len);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\CloseShieldWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */