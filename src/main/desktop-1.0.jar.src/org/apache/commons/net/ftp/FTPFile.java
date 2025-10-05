/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Formatter;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTPFile
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 9010790363003271996L;
/*     */   public static final int FILE_TYPE = 0;
/*     */   public static final int DIRECTORY_TYPE = 1;
/*     */   public static final int SYMBOLIC_LINK_TYPE = 2;
/*     */   public static final int UNKNOWN_TYPE = 3;
/*     */   public static final int USER_ACCESS = 0;
/*     */   public static final int GROUP_ACCESS = 1;
/*     */   public static final int WORLD_ACCESS = 2;
/*     */   public static final int READ_PERMISSION = 0;
/*     */   public static final int WRITE_PERMISSION = 1;
/*     */   public static final int EXECUTE_PERMISSION = 2;
/*     */   private int _type;
/*     */   private int _hardLinkCount;
/*     */   private long _size;
/*     */   private String _rawListing;
/*     */   private String _user;
/*     */   private String _group;
/*     */   private String _name;
/*     */   private String _link;
/*     */   private Calendar _date;
/*     */   private final boolean[][] _permissions;
/*     */   
/*     */   public FTPFile() {
/*  73 */     this._permissions = new boolean[3][3];
/*  74 */     this._type = 3;
/*     */ 
/*     */     
/*  77 */     this._hardLinkCount = 0;
/*  78 */     this._size = -1L;
/*  79 */     this._user = "";
/*  80 */     this._group = "";
/*  81 */     this._date = null;
/*  82 */     this._name = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   FTPFile(String rawListing) {
/*  93 */     this._permissions = (boolean[][])null;
/*  94 */     this._rawListing = rawListing;
/*  95 */     this._type = 3;
/*     */ 
/*     */     
/*  98 */     this._hardLinkCount = 0;
/*  99 */     this._size = -1L;
/* 100 */     this._user = "";
/* 101 */     this._group = "";
/* 102 */     this._date = null;
/* 103 */     this._name = null;
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
/*     */   public void setRawListing(String rawListing) {
/* 115 */     this._rawListing = rawListing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRawListing() {
/* 126 */     return this._rawListing;
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
/*     */   public boolean isDirectory() {
/* 138 */     return (this._type == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 149 */     return (this._type == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSymbolicLink() {
/* 160 */     return (this._type == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnknown() {
/* 171 */     return (this._type == 3);
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
/*     */   public boolean isValid() {
/* 185 */     return (this._permissions != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int type) {
/* 196 */     this._type = type;
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
/*     */   public int getType() {
/* 208 */     return this._type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 219 */     this._name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 229 */     return this._name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(long size) {
/* 239 */     this._size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 250 */     return this._size;
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
/*     */   public void setHardLinkCount(int links) {
/* 262 */     this._hardLinkCount = links;
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
/*     */   public int getHardLinkCount() {
/* 274 */     return this._hardLinkCount;
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
/*     */   public void setGroup(String group) {
/* 286 */     this._group = group;
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
/*     */   public String getGroup() {
/* 298 */     return this._group;
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
/*     */   public void setUser(String user) {
/* 310 */     this._user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 321 */     return this._user;
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
/*     */   public void setLink(String link) {
/* 333 */     this._link = link;
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
/*     */   public String getLink() {
/* 346 */     return this._link;
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
/*     */   public void setTimestamp(Calendar date) {
/* 359 */     this._date = date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Calendar getTimestamp() {
/* 370 */     return this._date;
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
/*     */   public void setPermission(int access, int permission, boolean value) {
/* 388 */     this._permissions[access][permission] = value;
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
/*     */   
/*     */   public boolean hasPermission(int access, int permission) {
/* 407 */     if (this._permissions == null) {
/* 408 */       return false;
/*     */     }
/* 410 */     return this._permissions[access][permission];
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
/* 421 */     return getRawListing();
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
/*     */   
/*     */   public String toFormattedString() {
/* 440 */     return toFormattedString(null);
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
/*     */   
/*     */   public String toFormattedString(String timezone) {
/* 459 */     if (!isValid()) {
/* 460 */       return "[Invalid: could not parse file entry]";
/*     */     }
/* 462 */     StringBuilder sb = new StringBuilder();
/* 463 */     Formatter fmt = new Formatter(sb);
/* 464 */     sb.append(formatType());
/* 465 */     sb.append(permissionToString(0));
/* 466 */     sb.append(permissionToString(1));
/* 467 */     sb.append(permissionToString(2));
/* 468 */     fmt.format(" %4d", new Object[] { Integer.valueOf(getHardLinkCount()) });
/* 469 */     fmt.format(" %-8s %-8s", new Object[] { getUser(), getGroup() });
/* 470 */     fmt.format(" %8d", new Object[] { Long.valueOf(getSize()) });
/* 471 */     Calendar timestamp = getTimestamp();
/* 472 */     if (timestamp != null) {
/* 473 */       if (timezone != null) {
/* 474 */         TimeZone newZone = TimeZone.getTimeZone(timezone);
/* 475 */         if (!newZone.equals(timestamp.getTimeZone())) {
/* 476 */           Date original = timestamp.getTime();
/* 477 */           Calendar newStamp = Calendar.getInstance(newZone);
/* 478 */           newStamp.setTime(original);
/* 479 */           timestamp = newStamp;
/*     */         } 
/*     */       } 
/* 482 */       fmt.format(" %1$tY-%1$tm-%1$td", new Object[] { timestamp });
/*     */       
/* 484 */       if (timestamp.isSet(11)) {
/* 485 */         fmt.format(" %1$tH", new Object[] { timestamp });
/* 486 */         if (timestamp.isSet(12)) {
/* 487 */           fmt.format(":%1$tM", new Object[] { timestamp });
/* 488 */           if (timestamp.isSet(13)) {
/* 489 */             fmt.format(":%1$tS", new Object[] { timestamp });
/* 490 */             if (timestamp.isSet(14)) {
/* 491 */               fmt.format(".%1$tL", new Object[] { timestamp });
/*     */             }
/*     */           } 
/*     */         } 
/* 495 */         fmt.format(" %1$tZ", new Object[] { timestamp });
/*     */       } 
/*     */     } 
/* 498 */     sb.append(' ');
/* 499 */     sb.append(getName());
/* 500 */     fmt.close();
/* 501 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private char formatType() {
/* 505 */     switch (this._type) {
/*     */       case 0:
/* 507 */         return '-';
/*     */       case 1:
/* 509 */         return 'd';
/*     */       case 2:
/* 511 */         return 'l';
/*     */     } 
/* 513 */     return '?';
/*     */   }
/*     */ 
/*     */   
/*     */   private String permissionToString(int access) {
/* 518 */     StringBuilder sb = new StringBuilder();
/* 519 */     if (hasPermission(access, 0)) {
/* 520 */       sb.append('r');
/*     */     } else {
/* 522 */       sb.append('-');
/*     */     } 
/* 524 */     if (hasPermission(access, 1)) {
/* 525 */       sb.append('w');
/*     */     } else {
/* 527 */       sb.append('-');
/*     */     } 
/* 529 */     if (hasPermission(access, 2)) {
/* 530 */       sb.append('x');
/*     */     } else {
/* 532 */       sb.append('-');
/*     */     } 
/* 534 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\ftp\FTPFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */