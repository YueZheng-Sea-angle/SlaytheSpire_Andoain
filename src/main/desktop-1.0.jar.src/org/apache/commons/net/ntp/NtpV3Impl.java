/*     */ package org.apache.commons.net.ntp;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.util.Arrays;
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
/*     */ public class NtpV3Impl
/*     */   implements NtpV3Packet
/*     */ {
/*     */   private static final int MODE_INDEX = 0;
/*     */   private static final int MODE_SHIFT = 0;
/*     */   private static final int VERSION_INDEX = 0;
/*     */   private static final int VERSION_SHIFT = 3;
/*     */   private static final int LI_INDEX = 0;
/*     */   private static final int LI_SHIFT = 6;
/*     */   private static final int STRATUM_INDEX = 1;
/*     */   private static final int POLL_INDEX = 2;
/*     */   private static final int PRECISION_INDEX = 3;
/*     */   private static final int ROOT_DELAY_INDEX = 4;
/*     */   private static final int ROOT_DISPERSION_INDEX = 8;
/*     */   private static final int REFERENCE_ID_INDEX = 12;
/*     */   private static final int REFERENCE_TIMESTAMP_INDEX = 16;
/*     */   private static final int ORIGINATE_TIMESTAMP_INDEX = 24;
/*     */   private static final int RECEIVE_TIMESTAMP_INDEX = 32;
/*     */   private static final int TRANSMIT_TIMESTAMP_INDEX = 40;
/*  55 */   private final byte[] buf = new byte[48];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile DatagramPacket dp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMode() {
/*  73 */     return ui(this.buf[0]) >> 0 & 0x7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getModeName() {
/*  84 */     return NtpUtils.getModeName(getMode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int mode) {
/*  95 */     this.buf[0] = (byte)(this.buf[0] & 0xF8 | mode & 0x7);
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
/*     */   public int getLeapIndicator() {
/* 110 */     return ui(this.buf[0]) >> 6 & 0x3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLeapIndicator(int li) {
/* 121 */     this.buf[0] = (byte)(this.buf[0] & 0x3F | (li & 0x3) << 6);
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
/*     */   public int getPoll() {
/* 136 */     return this.buf[2];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPoll(int poll) {
/* 147 */     this.buf[2] = (byte)(poll & 0xFF);
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
/*     */   public int getPrecision() {
/* 160 */     return this.buf[3];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrecision(int precision) {
/* 171 */     this.buf[3] = (byte)(precision & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 182 */     return ui(this.buf[0]) >> 3 & 0x7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(int version) {
/* 193 */     this.buf[0] = (byte)(this.buf[0] & 0xC7 | (version & 0x7) << 3);
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
/*     */   public int getStratum() {
/* 206 */     return ui(this.buf[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStratum(int stratum) {
/* 217 */     this.buf[1] = (byte)(stratum & 0xFF);
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
/*     */   public int getRootDelay() {
/* 230 */     return getInt(4);
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
/*     */   public void setRootDelay(int delay) {
/* 242 */     setInt(4, delay);
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
/*     */   public double getRootDelayInMillisDouble() {
/* 256 */     double l = getRootDelay();
/* 257 */     return l / 65.536D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRootDispersion() {
/* 267 */     return getInt(8);
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
/*     */   public void setRootDispersion(int dispersion) {
/* 279 */     setInt(8, dispersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getRootDispersionInMillis() {
/* 290 */     long l = getRootDispersion();
/* 291 */     return l * 1000L / 65536L;
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
/*     */   public double getRootDispersionInMillisDouble() {
/* 303 */     double l = getRootDispersion();
/* 304 */     return l / 65.536D;
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
/*     */   public void setReferenceId(int refId) {
/* 316 */     setInt(12, refId);
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
/*     */   public int getReferenceId() {
/* 328 */     return getInt(12);
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
/*     */   public String getReferenceIdString() {
/* 342 */     int version = getVersion();
/* 343 */     int stratum = getStratum();
/* 344 */     if (version == 3 || version == 4) {
/* 345 */       if (stratum == 0 || stratum == 1) {
/* 346 */         return idAsString();
/*     */       }
/*     */       
/* 349 */       if (version == 4) {
/* 350 */         return idAsHex();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 356 */     if (stratum >= 2) {
/* 357 */       return idAsIPAddress();
/*     */     }
/* 359 */     return idAsHex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String idAsIPAddress() {
/* 368 */     return ui(this.buf[12]) + "." + ui(this.buf[13]) + "." + ui(this.buf[14]) + "." + ui(this.buf[15]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String idAsString() {
/* 376 */     StringBuilder id = new StringBuilder();
/* 377 */     for (int i = 0; i <= 3; i++) {
/* 378 */       char c = (char)this.buf[12 + i];
/* 379 */       if (c == '\000') {
/*     */         break;
/*     */       }
/* 382 */       id.append(c);
/*     */     } 
/* 384 */     return id.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String idAsHex() {
/* 389 */     return Integer.toHexString(getReferenceId());
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
/*     */   public TimeStamp getTransmitTimeStamp() {
/* 401 */     return getTimestamp(40);
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
/*     */   public void setTransmitTime(TimeStamp ts) {
/* 413 */     setTimestamp(40, ts);
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
/*     */   public void setOriginateTimeStamp(TimeStamp ts) {
/* 425 */     setTimestamp(24, ts);
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
/*     */   public TimeStamp getOriginateTimeStamp() {
/* 437 */     return getTimestamp(24);
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
/*     */   public TimeStamp getReferenceTimeStamp() {
/* 449 */     return getTimestamp(16);
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
/*     */   public void setReferenceTime(TimeStamp ts) {
/* 461 */     setTimestamp(16, ts);
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
/*     */   public TimeStamp getReceiveTimeStamp() {
/* 473 */     return getTimestamp(32);
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
/*     */   public void setReceiveTimeStamp(TimeStamp ts) {
/* 485 */     setTimestamp(32, ts);
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
/*     */   public String getType() {
/* 497 */     return "NTP";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getInt(int index) {
/* 505 */     int i = ui(this.buf[index]) << 24 | ui(this.buf[index + 1]) << 16 | ui(this.buf[index + 2]) << 8 | ui(this.buf[index + 3]);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 510 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setInt(int idx, int value) {
/* 521 */     for (int i = 3; i >= 0; i--) {
/* 522 */       this.buf[idx + i] = (byte)(value & 0xFF);
/* 523 */       value >>>= 8;
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
/*     */   private TimeStamp getTimestamp(int index) {
/* 535 */     return new TimeStamp(getLong(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getLong(int index) {
/* 545 */     long i = ul(this.buf[index]) << 56L | ul(this.buf[index + 1]) << 48L | ul(this.buf[index + 2]) << 40L | ul(this.buf[index + 3]) << 32L | ul(this.buf[index + 4]) << 24L | ul(this.buf[index + 5]) << 16L | ul(this.buf[index + 6]) << 8L | ul(this.buf[index + 7]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 553 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setTimestamp(int index, TimeStamp t) {
/* 564 */     long ntpTime = (t == null) ? 0L : t.ntpValue();
/*     */ 
/*     */     
/* 567 */     for (int i = 7; i >= 0; i--) {
/* 568 */       this.buf[index + i] = (byte)(int)(ntpTime & 0xFFL);
/* 569 */       ntpTime >>>= 8L;
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
/*     */   public synchronized DatagramPacket getDatagramPacket() {
/* 582 */     if (this.dp == null) {
/* 583 */       this.dp = new DatagramPacket(this.buf, this.buf.length);
/* 584 */       this.dp.setPort(123);
/*     */     } 
/* 586 */     return this.dp;
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
/*     */   public void setDatagramPacket(DatagramPacket srcDp) {
/* 598 */     if (srcDp == null || srcDp.getLength() < this.buf.length) {
/* 599 */       throw new IllegalArgumentException();
/*     */     }
/* 601 */     byte[] incomingBuf = srcDp.getData();
/* 602 */     int len = srcDp.getLength();
/* 603 */     if (len > this.buf.length) {
/* 604 */       len = this.buf.length;
/*     */     }
/* 606 */     System.arraycopy(incomingBuf, 0, this.buf, 0, len);
/* 607 */     DatagramPacket dp = getDatagramPacket();
/* 608 */     dp.setAddress(srcDp.getAddress());
/* 609 */     int port = srcDp.getPort();
/* 610 */     dp.setPort((port > 0) ? port : 123);
/* 611 */     dp.setData(this.buf);
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
/* 628 */     if (this == obj) {
/* 629 */       return true;
/*     */     }
/* 631 */     if (obj == null || getClass() != obj.getClass()) {
/* 632 */       return false;
/*     */     }
/* 634 */     NtpV3Impl other = (NtpV3Impl)obj;
/* 635 */     return Arrays.equals(this.buf, other.buf);
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
/* 648 */     return Arrays.hashCode(this.buf);
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
/*     */   protected static final int ui(byte b) {
/* 661 */     int i = b & 0xFF;
/* 662 */     return i;
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
/*     */   protected static final long ul(byte b) {
/* 675 */     long i = (b & 0xFF);
/* 676 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 687 */     return "[version:" + getVersion() + ", mode:" + getMode() + ", poll:" + getPoll() + ", precision:" + getPrecision() + ", delay:" + getRootDelay() + ", dispersion(ms):" + getRootDispersionInMillisDouble() + ", id:" + getReferenceIdString() + ", xmitTime:" + getTransmitTimeStamp().toDateString() + " ]";
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ntp\NtpV3Impl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */