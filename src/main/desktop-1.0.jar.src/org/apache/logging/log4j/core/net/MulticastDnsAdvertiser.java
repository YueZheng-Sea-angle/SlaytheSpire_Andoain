/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Integers;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ 
/*     */ 
/*     */ @Plugin(name = "multicastdns", category = "Core", elementType = "advertiser", printObject = false)
/*     */ public class MulticastDnsAdvertiser
/*     */   implements Advertiser
/*     */ {
/*  44 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static final int MAX_LENGTH = 255;
/*     */   
/*     */   private static final int DEFAULT_PORT = 4555;
/*  49 */   private static Object jmDNS = initializeJmDns();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> jmDNSClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?> serviceInfoClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object advertise(Map<String, String> properties) {
/*  72 */     Map<String, String> truncatedProperties = new HashMap<>();
/*  73 */     for (Map.Entry<String, String> entry : properties.entrySet()) {
/*  74 */       if (((String)entry.getKey()).length() <= 255 && ((String)entry.getValue()).length() <= 255) {
/*  75 */         truncatedProperties.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     } 
/*  78 */     String protocol = truncatedProperties.get("protocol");
/*  79 */     String zone = "._log4j._" + ((protocol != null) ? protocol : "tcp") + ".local.";
/*     */     
/*  81 */     String portString = truncatedProperties.get("port");
/*  82 */     int port = Integers.parseInt(portString, 4555);
/*     */     
/*  84 */     String name = truncatedProperties.get("name");
/*     */ 
/*     */     
/*  87 */     if (jmDNS != null) {
/*  88 */       Object serviceInfo; boolean isVersion3 = false;
/*     */       
/*     */       try {
/*  91 */         jmDNSClass.getMethod("create", new Class[0]);
/*  92 */         isVersion3 = true;
/*  93 */       } catch (NoSuchMethodException null) {}
/*     */ 
/*     */ 
/*     */       
/*  97 */       if (isVersion3) {
/*  98 */         serviceInfo = buildServiceInfoVersion3(zone, port, name, truncatedProperties);
/*     */       } else {
/* 100 */         serviceInfo = buildServiceInfoVersion1(zone, port, name, truncatedProperties);
/*     */       } 
/*     */       
/*     */       try {
/* 104 */         Method method = jmDNSClass.getMethod("registerService", new Class[] { serviceInfoClass });
/* 105 */         method.invoke(jmDNS, new Object[] { serviceInfo });
/* 106 */       } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 107 */         LOGGER.warn("Unable to invoke registerService method", e);
/* 108 */       } catch (NoSuchMethodException e) {
/* 109 */         LOGGER.warn("No registerService method", e);
/*     */       } 
/* 111 */       return serviceInfo;
/*     */     } 
/* 113 */     LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unadvertise(Object serviceInfo) {
/* 124 */     if (jmDNS != null) {
/*     */       try {
/* 126 */         Method method = jmDNSClass.getMethod("unregisterService", new Class[] { serviceInfoClass });
/* 127 */         method.invoke(jmDNS, new Object[] { serviceInfo });
/* 128 */       } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 129 */         LOGGER.warn("Unable to invoke unregisterService method", e);
/* 130 */       } catch (NoSuchMethodException e) {
/* 131 */         LOGGER.warn("No unregisterService method", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static Object createJmDnsVersion1() {
/*     */     try {
/* 138 */       return jmDNSClass.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 139 */     } catch (InstantiationException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
/* 140 */       LOGGER.warn("Unable to instantiate JMDNS", e);
/*     */       
/* 142 */       return null;
/*     */     } 
/*     */   }
/*     */   private static Object createJmDnsVersion3() {
/*     */     try {
/* 147 */       Method jmDNSCreateMethod = jmDNSClass.getMethod("create", new Class[0]);
/* 148 */       return jmDNSCreateMethod.invoke(null, (Object[])null);
/* 149 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 150 */       LOGGER.warn("Unable to invoke create method", e);
/* 151 */     } catch (NoSuchMethodException e) {
/* 152 */       LOGGER.warn("Unable to get create method", e);
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object buildServiceInfoVersion1(String zone, int port, String name, Map<String, String> properties) {
/* 161 */     Hashtable<String, String> hashtableProperties = new Hashtable<>(properties);
/*     */     try {
/* 163 */       return serviceInfoClass.getConstructor(new Class[] { String.class, String.class, int.class, int.class, int.class, Hashtable.class
/* 164 */           }).newInstance(new Object[] { zone, name, Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(0), hashtableProperties });
/* 165 */     } catch (IllegalAccessException|InstantiationException|java.lang.reflect.InvocationTargetException e) {
/* 166 */       LOGGER.warn("Unable to construct ServiceInfo instance", e);
/* 167 */     } catch (NoSuchMethodException e) {
/* 168 */       LOGGER.warn("Unable to get ServiceInfo constructor", e);
/*     */     } 
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object buildServiceInfoVersion3(String zone, int port, String name, Map<String, String> properties) {
/*     */     try {
/* 176 */       return serviceInfoClass
/*     */         
/* 178 */         .getMethod("create", new Class[] { String.class, String.class, int.class, int.class, int.class, Map.class
/* 179 */           }).invoke(null, new Object[] { zone, name, Integer.valueOf(port), Integer.valueOf(0), Integer.valueOf(0), properties });
/* 180 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 181 */       LOGGER.warn("Unable to invoke create method", e);
/* 182 */     } catch (NoSuchMethodException e) {
/* 183 */       LOGGER.warn("Unable to find create method", e);
/*     */     } 
/* 185 */     return null;
/*     */   }
/*     */   
/*     */   private static Object initializeJmDns() {
/*     */     try {
/* 190 */       jmDNSClass = LoaderUtil.loadClass("javax.jmdns.JmDNS");
/* 191 */       serviceInfoClass = LoaderUtil.loadClass("javax.jmdns.ServiceInfo");
/*     */       
/* 193 */       boolean isVersion3 = false;
/*     */       
/*     */       try {
/* 196 */         jmDNSClass.getMethod("create", new Class[0]);
/* 197 */         isVersion3 = true;
/* 198 */       } catch (NoSuchMethodException noSuchMethodException) {}
/*     */ 
/*     */ 
/*     */       
/* 202 */       if (isVersion3) {
/* 203 */         return createJmDnsVersion3();
/*     */       }
/* 205 */       return createJmDnsVersion1();
/* 206 */     } catch (ClassNotFoundException|ExceptionInInitializerError e) {
/* 207 */       LOGGER.warn("JmDNS or serviceInfo class not found", e);
/*     */       
/* 209 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\MulticastDnsAdvertiser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */