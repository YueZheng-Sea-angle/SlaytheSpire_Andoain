/*    */ package org.apache.commons.net.util;
/*    */ 
/*    */ import java.nio.charset.Charset;
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
/*    */ public class Charsets
/*    */ {
/*    */   public static Charset toCharset(String charsetName) {
/* 38 */     return (charsetName == null) ? Charset.defaultCharset() : Charset.forName(charsetName);
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
/*    */   public static Charset toCharset(String charsetName, String defaultCharsetName) {
/* 52 */     return (charsetName == null) ? Charset.forName(defaultCharsetName) : Charset.forName(charsetName);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\ne\\util\Charsets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */