/*    */ package org.apache.logging.log4j.core.net.ssl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.NoSuchFileException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Arrays;
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
/*    */ class FilePasswordProvider
/*    */   implements PasswordProvider
/*    */ {
/*    */   private final Path passwordPath;
/*    */   
/*    */   public FilePasswordProvider(String passwordFile) throws NoSuchFileException {
/* 57 */     this.passwordPath = Paths.get(passwordFile, new String[0]);
/* 58 */     if (!Files.exists(this.passwordPath, new java.nio.file.LinkOption[0])) {
/* 59 */       throw new NoSuchFileException("PasswordFile '" + passwordFile + "' does not exist");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] getPassword() {
/* 65 */     byte[] bytes = null;
/*    */     try {
/* 67 */       bytes = Files.readAllBytes(this.passwordPath);
/* 68 */       ByteBuffer bb = ByteBuffer.wrap(bytes);
/* 69 */       CharBuffer decoded = Charset.defaultCharset().decode(bb);
/* 70 */       char[] result = new char[decoded.limit()];
/* 71 */       decoded.get(result, 0, result.length);
/* 72 */       decoded.rewind();
/* 73 */       decoded.put(new char[result.length]);
/* 74 */       return result;
/* 75 */     } catch (IOException e) {
/* 76 */       throw new IllegalStateException("Could not read password from " + this.passwordPath + ": " + e, e);
/*    */     } finally {
/* 78 */       if (bytes != null)
/* 79 */         Arrays.fill(bytes, (byte)0); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\ssl\FilePasswordProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */