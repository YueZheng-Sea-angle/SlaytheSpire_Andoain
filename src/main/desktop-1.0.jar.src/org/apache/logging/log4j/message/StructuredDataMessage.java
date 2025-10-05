/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AsynchronouslyFormattable
/*     */ public class StructuredDataMessage
/*     */   extends MapMessage<StructuredDataMessage, String>
/*     */ {
/*     */   private static final long serialVersionUID = 1703221292892071920L;
/*     */   private static final int MAX_LENGTH = 32;
/*     */   private static final int HASHVAL = 31;
/*     */   private StructuredDataId id;
/*     */   private String message;
/*     */   private String type;
/*     */   private final int maxLength;
/*     */   
/*     */   public enum Format
/*     */   {
/*  56 */     XML,
/*     */     
/*  58 */     FULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(String id, String msg, String type) {
/*  68 */     this(id, msg, type, 32);
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
/*     */   public StructuredDataMessage(String id, String msg, String type, int maxLength) {
/*  81 */     this.id = new StructuredDataId(id, null, null, maxLength);
/*  82 */     this.message = msg;
/*  83 */     this.type = type;
/*  84 */     this.maxLength = maxLength;
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
/*     */   public StructuredDataMessage(String id, String msg, String type, Map<String, String> data) {
/*  97 */     this(id, msg, type, data, 32);
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
/*     */   public StructuredDataMessage(String id, String msg, String type, Map<String, String> data, int maxLength) {
/* 112 */     super(data);
/* 113 */     this.id = new StructuredDataId(id, null, null, maxLength);
/* 114 */     this.message = msg;
/* 115 */     this.type = type;
/* 116 */     this.maxLength = maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type) {
/* 126 */     this(id, msg, type, 32);
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
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type, int maxLength) {
/* 138 */     this.id = id;
/* 139 */     this.message = msg;
/* 140 */     this.type = type;
/* 141 */     this.maxLength = maxLength;
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
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data) {
/* 154 */     this(id, msg, type, data, 32);
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
/*     */   public StructuredDataMessage(StructuredDataId id, String msg, String type, Map<String, String> data, int maxLength) {
/* 169 */     super(data);
/* 170 */     this.id = id;
/* 171 */     this.message = msg;
/* 172 */     this.type = type;
/* 173 */     this.maxLength = maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StructuredDataMessage(StructuredDataMessage msg, Map<String, String> map) {
/* 183 */     super(map);
/* 184 */     this.id = msg.id;
/* 185 */     this.message = msg.message;
/* 186 */     this.type = msg.type;
/* 187 */     this.maxLength = 32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructuredDataMessage() {
/* 194 */     this.maxLength = 32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getFormats() {
/* 203 */     String[] formats = new String[(Format.values()).length];
/* 204 */     int i = 0;
/* 205 */     for (Format format : Format.values()) {
/* 206 */       formats[i++] = format.name();
/*     */     }
/* 208 */     return formats;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataId getId() {
/* 216 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setId(String id) {
/* 224 */     this.id = new StructuredDataId(id, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setId(StructuredDataId id) {
/* 232 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 240 */     return this.type;
/*     */   }
/*     */   
/*     */   protected void setType(String type) {
/* 244 */     if (type.length() > 32) {
/* 245 */       throw new IllegalArgumentException("structured data type exceeds maximum length of 32 characters: " + type);
/*     */     }
/* 247 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 252 */     asString(Format.FULL, null, buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(String[] formats, StringBuilder buffer) {
/* 257 */     asString(getFormat(formats), null, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 266 */     return this.message;
/*     */   }
/*     */   
/*     */   protected void setMessageFormat(String msg) {
/* 270 */     this.message = msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asString() {
/* 280 */     return asString(Format.FULL, null);
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
/*     */   public String asString(String format) {
/*     */     try {
/* 293 */       return asString((Format)EnglishEnums.valueOf(Format.class, format), null);
/* 294 */     } catch (IllegalArgumentException ex) {
/* 295 */       return asString();
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
/*     */   
/*     */   public final String asString(Format format, StructuredDataId structuredDataId) {
/* 309 */     StringBuilder sb = new StringBuilder();
/* 310 */     asString(format, structuredDataId, sb);
/* 311 */     return sb.toString();
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
/*     */   public final void asString(Format format, StructuredDataId structuredDataId, StringBuilder sb) {
/* 324 */     boolean full = Format.FULL.equals(format);
/* 325 */     if (full) {
/* 326 */       String myType = getType();
/* 327 */       if (myType == null) {
/*     */         return;
/*     */       }
/* 330 */       sb.append(getType()).append(' ');
/*     */     } 
/* 332 */     StructuredDataId sdId = getId();
/* 333 */     if (sdId != null) {
/* 334 */       sdId = sdId.makeId(structuredDataId);
/*     */     } else {
/* 336 */       sdId = structuredDataId;
/*     */     } 
/* 338 */     if (sdId == null || sdId.getName() == null) {
/*     */       return;
/*     */     }
/* 341 */     if (Format.XML.equals(format)) {
/* 342 */       asXml(sdId, sb);
/*     */       return;
/*     */     } 
/* 345 */     sb.append('[');
/* 346 */     StringBuilders.appendValue(sb, sdId);
/* 347 */     sb.append(' ');
/* 348 */     appendMap(sb);
/* 349 */     sb.append(']');
/* 350 */     if (full) {
/* 351 */       String msg = getFormat();
/* 352 */       if (msg != null) {
/* 353 */         sb.append(' ').append(msg);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void asXml(StructuredDataId structuredDataId, StringBuilder sb) {
/* 359 */     sb.append("<StructuredData>\n");
/* 360 */     sb.append("<type>").append(this.type).append("</type>\n");
/* 361 */     sb.append("<id>").append(structuredDataId).append("</id>\n");
/* 362 */     asXml(sb);
/* 363 */     sb.append("\n</StructuredData>\n");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 372 */     return asString(Format.FULL, (StructuredDataId)null);
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
/*     */   public String getFormattedMessage(String[] formats) {
/* 386 */     return asString(getFormat(formats), (StructuredDataId)null);
/*     */   }
/*     */   
/*     */   private Format getFormat(String[] formats) {
/* 390 */     if (formats != null && formats.length > 0) {
/* 391 */       for (int i = 0; i < formats.length; i++) {
/* 392 */         String format = formats[i];
/* 393 */         if (Format.XML.name().equalsIgnoreCase(format))
/* 394 */           return Format.XML; 
/* 395 */         if (Format.FULL.name().equalsIgnoreCase(format)) {
/* 396 */           return Format.FULL;
/*     */         }
/*     */       } 
/* 399 */       return null;
/*     */     } 
/* 401 */     return Format.FULL;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 406 */     return asString((Format)null, (StructuredDataId)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StructuredDataMessage newInstance(Map<String, String> map) {
/* 412 */     return new StructuredDataMessage(this, map);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 417 */     if (this == o) {
/* 418 */       return true;
/*     */     }
/* 420 */     if (o == null || getClass() != o.getClass()) {
/* 421 */       return false;
/*     */     }
/*     */     
/* 424 */     StructuredDataMessage that = (StructuredDataMessage)o;
/*     */     
/* 426 */     if (!super.equals(o)) {
/* 427 */       return false;
/*     */     }
/* 429 */     if ((this.type != null) ? !this.type.equals(that.type) : (that.type != null)) {
/* 430 */       return false;
/*     */     }
/* 432 */     if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null)) {
/* 433 */       return false;
/*     */     }
/* 435 */     if ((this.message != null) ? !this.message.equals(that.message) : (that.message != null)) {
/* 436 */       return false;
/*     */     }
/*     */     
/* 439 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 444 */     int result = super.hashCode();
/* 445 */     result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
/* 446 */     result = 31 * result + ((this.id != null) ? this.id.hashCode() : 0);
/* 447 */     result = 31 * result + ((this.message != null) ? this.message.hashCode() : 0);
/* 448 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validate(String key, boolean value) {
/* 453 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, byte value) {
/* 461 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, char value) {
/* 469 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, double value) {
/* 477 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, float value) {
/* 485 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, int value) {
/* 493 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, long value) {
/* 501 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, Object value) {
/* 509 */     validateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, short value) {
/* 517 */     validateKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validate(String key, String value) {
/* 522 */     validateKey(key);
/*     */   }
/*     */   
/*     */   protected void validateKey(String key) {
/* 526 */     if (this.maxLength > 0 && key.length() > this.maxLength) {
/* 527 */       throw new IllegalArgumentException("Structured data keys are limited to " + this.maxLength + " characters. key: " + key);
/*     */     }
/*     */     
/* 530 */     for (int i = 0; i < key.length(); i++) {
/* 531 */       char c = key.charAt(i);
/* 532 */       if (c < '!' || c > '~' || c == '=' || c == ']' || c == '"')
/* 533 */         throw new IllegalArgumentException("Structured data keys must contain printable US ASCII charactersand may not contain a space, =, ], or \""); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\StructuredDataMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */