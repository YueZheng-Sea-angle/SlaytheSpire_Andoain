/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.logging.log4j.LoggingException;
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
/*    */ public final class Base64Util
/*    */ {
/* 29 */   private static Method encodeMethod = null;
/* 30 */   private static Object encoder = null;
/*    */   
/*    */   static {
/*    */     try {
/* 34 */       Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
/* 35 */       Class<?> encoderClazz = LoaderUtil.loadClass("java.util.Base64$Encoder");
/* 36 */       Method method = clazz.getMethod("getEncoder", new Class[0]);
/* 37 */       encoder = method.invoke(null, new Object[0]);
/* 38 */       encodeMethod = encoderClazz.getMethod("encodeToString", new Class[] { byte[].class });
/* 39 */     } catch (Exception ex) {
/*    */       try {
/* 41 */         Class<?> clazz = LoaderUtil.loadClass("javax.xml.bind.DataTypeConverter");
/* 42 */         encodeMethod = clazz.getMethod("printBase64Binary", new Class[0]);
/* 43 */       } catch (Exception ex2) {
/* 44 */         LowLevelLogUtil.logException("Unable to create a Base64 Encoder", ex2);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String encode(String str) {
/* 53 */     if (str == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     byte[] data = str.getBytes();
/* 57 */     if (encodeMethod != null) {
/*    */       try {
/* 59 */         return (String)encodeMethod.invoke(encoder, new Object[] { data });
/* 60 */       } catch (Exception ex) {
/* 61 */         throw new LoggingException("Unable to encode String", ex);
/*    */       } 
/*    */     }
/* 64 */     throw new LoggingException("No Encoder, unable to encode string");
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Base64Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */