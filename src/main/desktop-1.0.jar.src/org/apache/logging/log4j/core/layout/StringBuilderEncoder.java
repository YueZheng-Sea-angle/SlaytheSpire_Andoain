/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import java.nio.charset.CodingErrorAction;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.core.util.Constants;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class StringBuilderEncoder
/*    */   implements Encoder<StringBuilder>
/*    */ {
/*    */   private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
/* 46 */   private final ThreadLocal<Object[]> threadLocal = new ThreadLocal();
/*    */   private final Charset charset;
/*    */   private final int charBufferSize;
/*    */   private final int byteBufferSize;
/*    */   
/*    */   public StringBuilderEncoder(Charset charset) {
/* 52 */     this(charset, Constants.ENCODER_CHAR_BUFFER_SIZE, 8192);
/*    */   }
/*    */   
/*    */   public StringBuilderEncoder(Charset charset, int charBufferSize, int byteBufferSize) {
/* 56 */     this.charBufferSize = charBufferSize;
/* 57 */     this.byteBufferSize = byteBufferSize;
/* 58 */     this.charset = Objects.<Charset>requireNonNull(charset, "charset");
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(StringBuilder source, ByteBufferDestination destination) {
/*    */     try {
/* 64 */       Object[] threadLocalState = getThreadLocalState();
/* 65 */       CharsetEncoder charsetEncoder = (CharsetEncoder)threadLocalState[0];
/* 66 */       CharBuffer charBuffer = (CharBuffer)threadLocalState[1];
/* 67 */       ByteBuffer byteBuffer = (ByteBuffer)threadLocalState[2];
/* 68 */       TextEncoderHelper.encodeText(charsetEncoder, charBuffer, byteBuffer, source, destination);
/* 69 */     } catch (Exception ex) {
/* 70 */       logEncodeTextException(ex, source, destination);
/* 71 */       TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
/*    */     } 
/*    */   }
/*    */   
/*    */   private Object[] getThreadLocalState() {
/* 76 */     Object[] threadLocalState = this.threadLocal.get();
/* 77 */     if (threadLocalState == null) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 82 */       threadLocalState = new Object[] { this.charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE), CharBuffer.allocate(this.charBufferSize), ByteBuffer.allocate(this.byteBufferSize) };
/*    */       
/* 84 */       this.threadLocal.set(threadLocalState);
/*    */     } else {
/* 86 */       ((CharsetEncoder)threadLocalState[0]).reset();
/* 87 */       ((CharBuffer)threadLocalState[1]).clear();
/* 88 */       ((ByteBuffer)threadLocalState[2]).clear();
/*    */     } 
/* 90 */     return threadLocalState;
/*    */   }
/*    */ 
/*    */   
/*    */   private void logEncodeTextException(Exception ex, StringBuilder text, ByteBufferDestination destination) {
/* 95 */     StatusLogger.getLogger().error("Recovering from StringBuilderEncoder.encode('{}') error: {}", text, ex, ex);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\layout\StringBuilderEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */