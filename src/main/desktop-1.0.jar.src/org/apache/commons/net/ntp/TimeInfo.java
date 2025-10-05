/*     */ package org.apache.commons.net.ntp;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class TimeInfo
/*     */ {
/*     */   private final NtpV3Packet _message;
/*     */   private List<String> _comments;
/*     */   private Long _delay;
/*     */   private Long _offset;
/*     */   private final long _returnTime;
/*     */   private boolean _detailsComputed;
/*     */   
/*     */   public TimeInfo(NtpV3Packet message, long returnTime) {
/*  56 */     this(message, returnTime, null, true);
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
/*     */   public TimeInfo(NtpV3Packet message, long returnTime, List<String> comments) {
/*  69 */     this(message, returnTime, comments, true);
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
/*     */ 
/*     */   
/*     */   public TimeInfo(NtpV3Packet msgPacket, long returnTime, boolean doComputeDetails) {
/*  85 */     this(msgPacket, returnTime, null, doComputeDetails);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeInfo(NtpV3Packet message, long returnTime, List<String> comments, boolean doComputeDetails) {
/* 103 */     if (message == null) {
/* 104 */       throw new IllegalArgumentException("message cannot be null");
/*     */     }
/* 106 */     this._returnTime = returnTime;
/* 107 */     this._message = message;
/* 108 */     this._comments = comments;
/* 109 */     if (doComputeDetails) {
/* 110 */       computeDetails();
/*     */     }
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
/*     */   public void addComment(String comment) {
/* 123 */     if (this._comments == null) {
/* 124 */       this._comments = new ArrayList<String>();
/*     */     }
/* 126 */     this._comments.add(comment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void computeDetails() {
/* 135 */     if (this._detailsComputed) {
/*     */       return;
/*     */     }
/* 138 */     this._detailsComputed = true;
/* 139 */     if (this._comments == null) {
/* 140 */       this._comments = new ArrayList<String>();
/*     */     }
/*     */     
/* 143 */     TimeStamp origNtpTime = this._message.getOriginateTimeStamp();
/* 144 */     long origTime = origNtpTime.getTime();
/*     */ 
/*     */     
/* 147 */     TimeStamp rcvNtpTime = this._message.getReceiveTimeStamp();
/* 148 */     long rcvTime = rcvNtpTime.getTime();
/*     */ 
/*     */     
/* 151 */     TimeStamp xmitNtpTime = this._message.getTransmitTimeStamp();
/* 152 */     long xmitTime = xmitNtpTime.getTime();
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
/* 170 */     if (origNtpTime.ntpValue() == 0L) {
/*     */ 
/*     */ 
/*     */       
/* 174 */       if (xmitNtpTime.ntpValue() != 0L) {
/*     */         
/* 176 */         this._offset = Long.valueOf(xmitTime - this._returnTime);
/* 177 */         this._comments.add("Error: zero orig time -- cannot compute delay");
/*     */       } else {
/* 179 */         this._comments.add("Error: zero orig time -- cannot compute delay/offset");
/*     */       } 
/* 181 */     } else if (rcvNtpTime.ntpValue() == 0L || xmitNtpTime.ntpValue() == 0L) {
/* 182 */       this._comments.add("Warning: zero rcvNtpTime or xmitNtpTime");
/*     */       
/* 184 */       if (origTime > this._returnTime) {
/* 185 */         this._comments.add("Error: OrigTime > DestRcvTime");
/*     */       }
/*     */       else {
/*     */         
/* 189 */         this._delay = Long.valueOf(this._returnTime - origTime);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 195 */       if (rcvNtpTime.ntpValue() != 0L) {
/*     */ 
/*     */         
/* 198 */         this._offset = Long.valueOf(rcvTime - origTime);
/* 199 */       } else if (xmitNtpTime.ntpValue() != 0L) {
/*     */ 
/*     */         
/* 202 */         this._offset = Long.valueOf(xmitTime - this._returnTime);
/*     */       } 
/*     */     } else {
/*     */       
/* 206 */       long delayValue = this._returnTime - origTime;
/*     */       
/* 208 */       if (xmitTime < rcvTime) {
/*     */ 
/*     */         
/* 211 */         this._comments.add("Error: xmitTime < rcvTime");
/*     */       }
/*     */       else {
/*     */         
/* 215 */         long delta = xmitTime - rcvTime;
/*     */ 
/*     */         
/* 218 */         if (delta <= delayValue) {
/*     */           
/* 220 */           delayValue -= delta;
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 225 */         else if (delta - delayValue == 1L) {
/*     */ 
/*     */           
/* 228 */           if (delayValue != 0L) {
/*     */             
/* 230 */             this._comments.add("Info: processing time > total network time by 1 ms -> assume zero delay");
/* 231 */             delayValue = 0L;
/*     */           } 
/*     */         } else {
/* 234 */           this._comments.add("Warning: processing time > total network time");
/*     */         } 
/*     */       } 
/*     */       
/* 238 */       this._delay = Long.valueOf(delayValue);
/* 239 */       if (origTime > this._returnTime) {
/* 240 */         this._comments.add("Error: OrigTime > DestRcvTime");
/*     */       }
/*     */       
/* 243 */       this._offset = Long.valueOf((rcvTime - origTime + xmitTime - this._returnTime) / 2L);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getComments() {
/* 254 */     return this._comments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getDelay() {
/* 264 */     return this._delay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getOffset() {
/* 275 */     return this._offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NtpV3Packet getMessage() {
/* 285 */     return this._message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getAddress() {
/* 294 */     DatagramPacket pkt = this._message.getDatagramPacket();
/* 295 */     return (pkt == null) ? null : pkt.getAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getReturnTime() {
/* 305 */     return this._returnTime;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 322 */     if (this == obj) {
/* 323 */       return true;
/*     */     }
/* 325 */     if (obj == null || getClass() != obj.getClass()) {
/* 326 */       return false;
/*     */     }
/* 328 */     TimeInfo other = (TimeInfo)obj;
/* 329 */     return (this._returnTime == other._returnTime && this._message.equals(other._message));
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
/*     */   public int hashCode() {
/* 342 */     int prime = 31;
/* 343 */     int result = (int)this._returnTime;
/* 344 */     result = 31 * result + this._message.hashCode();
/* 345 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ntp\TimeInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */