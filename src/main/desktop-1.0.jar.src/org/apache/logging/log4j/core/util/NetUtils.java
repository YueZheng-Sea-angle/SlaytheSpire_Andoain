/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
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
/*     */ public final class NetUtils
/*     */ {
/*  44 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UNKNOWN_LOCALHOST = "UNKNOWN_LOCALHOST";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLocalHostname() {
/*     */     try {
/*  59 */       InetAddress addr = InetAddress.getLocalHost();
/*  60 */       return (addr == null) ? "UNKNOWN_LOCALHOST" : addr.getHostName();
/*  61 */     } catch (UnknownHostException uhe) {
/*     */       try {
/*  63 */         Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/*  64 */         if (interfaces != null) {
/*  65 */           while (interfaces.hasMoreElements()) {
/*  66 */             NetworkInterface nic = interfaces.nextElement();
/*  67 */             Enumeration<InetAddress> addresses = nic.getInetAddresses();
/*  68 */             while (addresses.hasMoreElements()) {
/*  69 */               InetAddress address = addresses.nextElement();
/*  70 */               if (!address.isLoopbackAddress()) {
/*  71 */                 String hostname = address.getHostName();
/*  72 */                 if (hostname != null) {
/*  73 */                   return hostname;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }
/*  79 */       } catch (SocketException socketException) {}
/*     */ 
/*     */       
/*  82 */       LOGGER.error("Could not determine local host name", uhe);
/*  83 */       return "UNKNOWN_LOCALHOST";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> getLocalIps() {
/*  92 */     List<String> localIps = new ArrayList<>();
/*  93 */     localIps.add("localhost");
/*  94 */     localIps.add("127.0.0.1");
/*     */     try {
/*  96 */       InetAddress addr = Inet4Address.getLocalHost();
/*  97 */       setHostName(addr, localIps);
/*  98 */     } catch (UnknownHostException unknownHostException) {}
/*     */ 
/*     */     
/*     */     try {
/* 102 */       Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/* 103 */       if (interfaces != null) {
/* 104 */         while (interfaces.hasMoreElements()) {
/* 105 */           NetworkInterface nic = interfaces.nextElement();
/* 106 */           Enumeration<InetAddress> addresses = nic.getInetAddresses();
/* 107 */           while (addresses.hasMoreElements()) {
/* 108 */             InetAddress address = addresses.nextElement();
/* 109 */             setHostName(address, localIps);
/*     */           } 
/*     */         } 
/*     */       }
/* 113 */     } catch (SocketException socketException) {}
/*     */ 
/*     */     
/* 116 */     return localIps;
/*     */   }
/*     */   
/*     */   private static void setHostName(InetAddress address, List<String> localIps) {
/* 120 */     String[] parts = address.toString().split("\\s*/\\s*");
/* 121 */     if (parts.length > 0) {
/* 122 */       for (String part : parts) {
/* 123 */         if (Strings.isNotBlank(part) && !localIps.contains(part)) {
/* 124 */           localIps.add(part);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getMacAddress() {
/* 137 */     byte[] mac = null;
/*     */     try {
/* 139 */       InetAddress localHost = InetAddress.getLocalHost();
/*     */       try {
/* 141 */         NetworkInterface localInterface = NetworkInterface.getByInetAddress(localHost);
/* 142 */         if (isUpAndNotLoopback(localInterface)) {
/* 143 */           mac = localInterface.getHardwareAddress();
/*     */         }
/* 145 */         if (mac == null) {
/* 146 */           Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
/* 147 */           if (networkInterfaces != null) {
/* 148 */             while (networkInterfaces.hasMoreElements() && mac == null) {
/* 149 */               NetworkInterface nic = networkInterfaces.nextElement();
/* 150 */               if (isUpAndNotLoopback(nic)) {
/* 151 */                 mac = nic.getHardwareAddress();
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/* 156 */       } catch (SocketException e) {
/* 157 */         LOGGER.catching(e);
/*     */       } 
/* 159 */       if (ArrayUtils.isEmpty(mac) && localHost != null) {
/*     */         
/* 161 */         byte[] address = localHost.getAddress();
/*     */         
/* 163 */         mac = Arrays.copyOf(address, 6);
/*     */       } 
/* 165 */     } catch (UnknownHostException unknownHostException) {}
/*     */ 
/*     */     
/* 168 */     return mac;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getMacAddressString() {
/* 176 */     byte[] macAddr = getMacAddress();
/* 177 */     if (!ArrayUtils.isEmpty(macAddr)) {
/* 178 */       StringBuilder sb = new StringBuilder(String.format("%02x", new Object[] { Byte.valueOf(macAddr[0]) }));
/* 179 */       for (int i = 1; i < macAddr.length; i++) {
/* 180 */         sb.append(":").append(String.format("%02x", new Object[] { Byte.valueOf(macAddr[i]) }));
/*     */       } 
/* 182 */       return sb.toString();
/*     */     } 
/*     */     
/* 185 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isUpAndNotLoopback(NetworkInterface ni) throws SocketException {
/* 189 */     return (ni != null && !ni.isLoopback() && ni.isUp());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI toURI(String path) {
/*     */     try {
/* 201 */       return new URI(path);
/* 202 */     } catch (URISyntaxException e) {
/*     */ 
/*     */       
/*     */       try {
/* 206 */         URL url = new URL(path);
/* 207 */         return new URI(url.getProtocol(), url.getHost(), url.getPath(), null);
/* 208 */       } catch (MalformedURLException|URISyntaxException nestedEx) {
/* 209 */         return (new File(path)).toURI();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\NetUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */