/*    */ package org.apache.logging.log4j.core.config.plugins.convert;
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
/*    */ public class HexConverter
/*    */ {
/*    */   public static byte[] parseHexBinary(String s) {
/* 28 */     int len = s.length();
/* 29 */     byte[] data = new byte[len / 2];
/* 30 */     for (int i = 0; i < len; i += 2) {
/* 31 */       data[i / 2] = 
/* 32 */         (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
/*    */     }
/* 34 */     return data;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\convert\HexConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */