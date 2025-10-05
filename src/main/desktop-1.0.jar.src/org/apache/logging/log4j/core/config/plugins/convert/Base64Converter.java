/*    */ package org.apache.logging.log4j.core.config.plugins.convert;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.Constants;
/*    */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*    */ public class Base64Converter
/*    */ {
/* 32 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/* 33 */   private static Method method = null;
/* 34 */   private static Object decoder = null;
/*    */ 
/*    */   
/*    */   static {
/*    */     try {
/* 39 */       Class<?> clazz = LoaderUtil.loadClass("java.util.Base64");
/* 40 */       Method getDecoder = clazz.getMethod("getDecoder", (Class[])null);
/* 41 */       decoder = getDecoder.invoke(null, (Object[])null);
/* 42 */       clazz = decoder.getClass();
/* 43 */       method = clazz.getMethod("decode", new Class[] { String.class });
/* 44 */     } catch (ClassNotFoundException|NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException classNotFoundException) {}
/*    */ 
/*    */ 
/*    */     
/* 48 */     if (method == null) {
/*    */       
/*    */       try {
/* 51 */         Class<?> clazz = LoaderUtil.loadClass("javax.xml.bind.DatatypeConverter");
/* 52 */         method = clazz.getMethod("parseBase64Binary", new Class[] { String.class });
/* 53 */       } catch (ClassNotFoundException ex) {
/* 54 */         LOGGER.error("No Base64 Converter is available");
/* 55 */       } catch (NoSuchMethodException noSuchMethodException) {}
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] parseBase64Binary(String encoded) {
/* 62 */     if (method == null) {
/* 63 */       LOGGER.error("No base64 converter");
/*    */     } else {
/*    */       try {
/* 66 */         return (byte[])method.invoke(decoder, new Object[] { encoded });
/* 67 */       } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/* 68 */         LOGGER.error("Error decoding string - " + ex.getMessage());
/*    */       } 
/*    */     } 
/* 71 */     return Constants.EMPTY_BYTE_ARRAY;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\plugins\convert\Base64Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */