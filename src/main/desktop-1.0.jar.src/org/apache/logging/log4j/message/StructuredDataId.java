/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ 
/*     */ public class StructuredDataId
/*     */   implements Serializable, StringBuilderFormattable
/*     */ {
/*  32 */   public static final StructuredDataId TIME_QUALITY = new StructuredDataId("timeQuality", null, new String[] { "tzKnown", "isSynced", "syncAccuracy" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static final StructuredDataId ORIGIN = new StructuredDataId("origin", null, new String[] { "ip", "enterpriseId", "software", "swVersion" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static final StructuredDataId META = new StructuredDataId("meta", null, new String[] { "sequenceId", "sysUpTime", "language" });
/*     */ 
/*     */   
/*     */   public static final int RESERVED = -1;
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 9031746276396249990L;
/*     */ 
/*     */   
/*     */   private static final int MAX_LENGTH = 32;
/*     */   
/*     */   private static final String AT_SIGN = "@";
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int enterpriseNumber;
/*     */   
/*     */   private final String[] required;
/*     */   
/*     */   private final String[] optional;
/*     */ 
/*     */   
/*     */   public StructuredDataId(String name) {
/*  67 */     this(name, (String[])null, (String[])null, 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId(String name, int maxLength) {
/*  77 */     this(name, (String[])null, (String[])null, maxLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId(String name, String[] required, String[] optional) {
/*  87 */     this(name, required, optional, 32);
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
/*     */   public StructuredDataId(String name, String[] required, String[] optional, int maxLength) {
/*  99 */     int index = -1;
/* 100 */     if (name != null) {
/* 101 */       if (maxLength <= 0) {
/* 102 */         maxLength = 32;
/*     */       }
/* 104 */       if (name.length() > maxLength) {
/* 105 */         throw new IllegalArgumentException(String.format("Length of id %s exceeds maximum of %d characters", new Object[] { name, 
/* 106 */                 Integer.valueOf(maxLength) }));
/*     */       }
/* 108 */       index = name.indexOf("@");
/*     */     } 
/*     */     
/* 111 */     if (index > 0) {
/* 112 */       this.name = name.substring(0, index);
/* 113 */       this.enterpriseNumber = Integer.parseInt(name.substring(index + 1));
/*     */     } else {
/* 115 */       this.name = name;
/* 116 */       this.enterpriseNumber = -1;
/*     */     } 
/* 118 */     this.required = required;
/* 119 */     this.optional = optional;
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
/*     */   public StructuredDataId(String name, int enterpriseNumber, String[] required, String[] optional) {
/* 132 */     this(name, enterpriseNumber, required, optional, 32);
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
/*     */   public StructuredDataId(String name, int enterpriseNumber, String[] required, String[] optional, int maxLength) {
/* 147 */     if (name == null) {
/* 148 */       throw new IllegalArgumentException("No structured id name was supplied");
/*     */     }
/* 150 */     if (name.contains("@")) {
/* 151 */       throw new IllegalArgumentException("Structured id name cannot contain an " + Strings.quote("@"));
/*     */     }
/* 153 */     if (enterpriseNumber <= 0) {
/* 154 */       throw new IllegalArgumentException("No enterprise number was supplied");
/*     */     }
/* 156 */     this.name = name;
/* 157 */     this.enterpriseNumber = enterpriseNumber;
/* 158 */     String id = name + "@" + enterpriseNumber;
/* 159 */     if (maxLength > 0 && id.length() > maxLength) {
/* 160 */       throw new IllegalArgumentException("Length of id exceeds maximum of " + maxLength + " characters: " + id);
/*     */     }
/* 162 */     this.required = required;
/* 163 */     this.optional = optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId makeId(StructuredDataId id) {
/* 173 */     if (id == null) {
/* 174 */       return this;
/*     */     }
/* 176 */     return makeId(id.getName(), id.getEnterpriseNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId makeId(String defaultId, int anEnterpriseNumber) {
/*     */     String id;
/*     */     String[] req;
/*     */     String[] opt;
/* 190 */     if (anEnterpriseNumber <= 0) {
/* 191 */       return this;
/*     */     }
/* 193 */     if (this.name != null) {
/* 194 */       id = this.name;
/* 195 */       req = this.required;
/* 196 */       opt = this.optional;
/*     */     } else {
/* 198 */       id = defaultId;
/* 199 */       req = null;
/* 200 */       opt = null;
/*     */     } 
/*     */     
/* 203 */     return new StructuredDataId(id, anEnterpriseNumber, req, opt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getRequired() {
/* 212 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getOptional() {
/* 221 */     return this.optional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 230 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEnterpriseNumber() {
/* 239 */     return this.enterpriseNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReserved() {
/* 248 */     return (this.enterpriseNumber <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     StringBuilder sb = new StringBuilder(this.name.length() + 10);
/* 254 */     formatTo(sb);
/* 255 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 260 */     if (isReserved()) {
/* 261 */       buffer.append(this.name);
/*     */     } else {
/* 263 */       buffer.append(this.name).append("@").append(this.enterpriseNumber);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\StructuredDataId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */