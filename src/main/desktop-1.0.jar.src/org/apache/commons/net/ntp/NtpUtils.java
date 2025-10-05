/*     */ package org.apache.commons.net.ntp;
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
/*     */ 
/*     */ 
/*     */ public final class NtpUtils
/*     */ {
/*     */   public static String getHostAddress(int address) {
/*  35 */     return (address >>> 24 & 0xFF) + "." + (address >>> 16 & 0xFF) + "." + (address >>> 8 & 0xFF) + "." + (address >>> 0 & 0xFF);
/*     */   }
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
/*     */   public static String getRefAddress(NtpV3Packet packet) {
/*  49 */     int address = (packet == null) ? 0 : packet.getReferenceId();
/*  50 */     return getHostAddress(address);
/*     */   }
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
/*     */   public static String getReferenceClock(NtpV3Packet message) {
/*  63 */     if (message == null) {
/*  64 */       return "";
/*     */     }
/*  66 */     int refId = message.getReferenceId();
/*  67 */     if (refId == 0) {
/*  68 */       return "";
/*     */     }
/*  70 */     StringBuilder buf = new StringBuilder(4);
/*     */     
/*  72 */     for (int shiftBits = 24; shiftBits >= 0; shiftBits -= 8) {
/*     */       
/*  74 */       char c = (char)(refId >>> shiftBits & 0xFF);
/*  75 */       if (c == '\000') {
/*     */         break;
/*     */       }
/*  78 */       if (!Character.isLetterOrDigit(c)) {
/*  79 */         return "";
/*     */       }
/*  81 */       buf.append(c);
/*     */     } 
/*  83 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getModeName(int mode) {
/*  94 */     switch (mode) {
/*     */       case 0:
/*  96 */         return "Reserved";
/*     */       case 1:
/*  98 */         return "Symmetric Active";
/*     */       case 2:
/* 100 */         return "Symmetric Passive";
/*     */       case 3:
/* 102 */         return "Client";
/*     */       case 4:
/* 104 */         return "Server";
/*     */       case 5:
/* 106 */         return "Broadcast";
/*     */       case 6:
/* 108 */         return "Control";
/*     */       case 7:
/* 110 */         return "Private";
/*     */     } 
/* 112 */     return "Unknown";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ntp\NtpUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */