/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Constants
/*     */ {
/*  30 */   public static final boolean IS_WEB_APP = PropertiesUtil.getProperties().getBooleanProperty("log4j2.is.webapp", (
/*  31 */       isClassAvailable("javax.servlet.Servlet") || 
/*  32 */       isClassAvailable("jakarta.servlet.Servlet")));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   public static final boolean ENABLE_THREADLOCALS = (!IS_WEB_APP && PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.threadlocals", true));
/*     */ 
/*     */   
/*  44 */   public static final int JAVA_MAJOR_VERSION = getMajorVersion();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final int MAX_REUSABLE_MESSAGE_SIZE = size("log4j.maxReusableMsgSize", 518);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LOG4J2_DEBUG = "log4j2.debug";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int size(String property, int defaultValue) {
/*  69 */     return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isClassAvailable(String className) {
/*     */     try {
/*  80 */       return (LoaderUtil.loadClass(className) != null);
/*  81 */     } catch (Throwable e) {
/*  82 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getMajorVersion() {
/* 103 */     return getMajorVersion(System.getProperty("java.version"));
/*     */   }
/*     */   
/*     */   static int getMajorVersion(String version) {
/* 107 */     String[] parts = version.split("-|\\.");
/*     */     
/*     */     try {
/* 110 */       int token = Integer.parseInt(parts[0]);
/* 111 */       boolean isJEP223 = (token != 1);
/* 112 */       if (isJEP223) {
/* 113 */         return token;
/*     */       }
/* 115 */       return Integer.parseInt(parts[1]);
/* 116 */     } catch (Exception ex) {
/* 117 */       return 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Constants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */