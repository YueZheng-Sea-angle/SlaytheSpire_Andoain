/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Facility
/*     */ {
/* 130 */   KERN(0),
/*     */ 
/*     */   
/* 133 */   USER(1),
/*     */ 
/*     */   
/* 136 */   MAIL(2),
/*     */ 
/*     */   
/* 139 */   DAEMON(3),
/*     */ 
/*     */   
/* 142 */   AUTH(4),
/*     */ 
/*     */   
/* 145 */   SYSLOG(5),
/*     */ 
/*     */   
/* 148 */   LPR(6),
/*     */ 
/*     */   
/* 151 */   NEWS(7),
/*     */ 
/*     */   
/* 154 */   UUCP(8),
/*     */ 
/*     */   
/* 157 */   CRON(9),
/*     */ 
/*     */   
/* 160 */   AUTHPRIV(10),
/*     */ 
/*     */   
/* 163 */   FTP(11),
/*     */ 
/*     */   
/* 166 */   NTP(12),
/*     */ 
/*     */   
/* 169 */   LOG_AUDIT(13),
/*     */ 
/*     */   
/* 172 */   LOG_ALERT(14),
/*     */ 
/*     */   
/* 175 */   CLOCK(15),
/*     */ 
/*     */   
/* 178 */   LOCAL0(16),
/*     */ 
/*     */   
/* 181 */   LOCAL1(17),
/*     */ 
/*     */   
/* 184 */   LOCAL2(18),
/*     */ 
/*     */   
/* 187 */   LOCAL3(19),
/*     */ 
/*     */   
/* 190 */   LOCAL4(20),
/*     */ 
/*     */   
/* 193 */   LOCAL5(21),
/*     */ 
/*     */   
/* 196 */   LOCAL6(22),
/*     */ 
/*     */   
/* 199 */   LOCAL7(23);
/*     */   
/*     */   private final int code;
/*     */   
/*     */   Facility(int code) {
/* 204 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Facility toFacility(String name) {
/* 214 */     return toFacility(name, (Facility)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Facility toFacility(String name, Facility defaultFacility) {
/* 225 */     return (Facility)EnglishEnums.valueOf(Facility.class, name, defaultFacility);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCode() {
/* 233 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEqual(String name) {
/* 242 */     return name().equalsIgnoreCase(name);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\net\Facility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */