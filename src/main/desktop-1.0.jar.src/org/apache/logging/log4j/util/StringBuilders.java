/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringBuilders
/*     */ {
/*     */   public static StringBuilder appendDqValue(StringBuilder sb, Object value) {
/*  38 */     return sb.append('"').append(value).append('"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder appendKeyDqValue(StringBuilder sb, Map.Entry<String, String> entry) {
/*  49 */     return appendKeyDqValue(sb, entry.getKey(), entry.getValue());
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
/*     */   public static StringBuilder appendKeyDqValue(StringBuilder sb, String key, Object value) {
/*  61 */     return sb.append(key).append('=').append('"').append(value).append('"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void appendValue(StringBuilder stringBuilder, Object obj) {
/*  72 */     if (!appendSpecificTypes(stringBuilder, obj)) {
/*  73 */       stringBuilder.append(obj);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean appendSpecificTypes(StringBuilder stringBuilder, Object obj) {
/*  78 */     if (obj == null || obj instanceof String) {
/*  79 */       stringBuilder.append((String)obj);
/*  80 */     } else if (obj instanceof StringBuilderFormattable) {
/*  81 */       ((StringBuilderFormattable)obj).formatTo(stringBuilder);
/*  82 */     } else if (obj instanceof CharSequence) {
/*  83 */       stringBuilder.append((CharSequence)obj);
/*  84 */     } else if (obj instanceof Integer) {
/*  85 */       stringBuilder.append(((Integer)obj).intValue());
/*  86 */     } else if (obj instanceof Long) {
/*  87 */       stringBuilder.append(((Long)obj).longValue());
/*  88 */     } else if (obj instanceof Double) {
/*  89 */       stringBuilder.append(((Double)obj).doubleValue());
/*  90 */     } else if (obj instanceof Boolean) {
/*  91 */       stringBuilder.append(((Boolean)obj).booleanValue());
/*  92 */     } else if (obj instanceof Character) {
/*  93 */       stringBuilder.append(((Character)obj).charValue());
/*  94 */     } else if (obj instanceof Short) {
/*  95 */       stringBuilder.append(((Short)obj).shortValue());
/*  96 */     } else if (obj instanceof Float) {
/*  97 */       stringBuilder.append(((Float)obj).floatValue());
/*  98 */     } else if (obj instanceof Byte) {
/*  99 */       stringBuilder.append(((Byte)obj).byteValue());
/*     */     } else {
/* 101 */       return false;
/*     */     } 
/* 103 */     return true;
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
/*     */   public static boolean equals(CharSequence left, int leftOffset, int leftLength, CharSequence right, int rightOffset, int rightLength) {
/* 120 */     if (leftLength == rightLength) {
/* 121 */       for (int i = 0; i < rightLength; i++) {
/* 122 */         if (left.charAt(i + leftOffset) != right.charAt(i + rightOffset)) {
/* 123 */           return false;
/*     */         }
/*     */       } 
/* 126 */       return true;
/*     */     } 
/* 128 */     return false;
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
/*     */   public static boolean equalsIgnoreCase(CharSequence left, int leftOffset, int leftLength, CharSequence right, int rightOffset, int rightLength) {
/* 145 */     if (leftLength == rightLength) {
/* 146 */       for (int i = 0; i < rightLength; i++) {
/* 147 */         if (Character.toLowerCase(left.charAt(i + leftOffset)) != Character.toLowerCase(right.charAt(i + rightOffset))) {
/* 148 */           return false;
/*     */         }
/*     */       } 
/* 151 */       return true;
/*     */     } 
/* 153 */     return false;
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
/*     */   public static void trimToMaxSize(StringBuilder stringBuilder, int maxSize) {
/* 165 */     if (stringBuilder != null && stringBuilder.capacity() > maxSize) {
/* 166 */       stringBuilder.setLength(maxSize);
/* 167 */       stringBuilder.trimToSize();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void escapeJson(StringBuilder toAppendTo, int start) {
/* 172 */     int escapeCount = 0;
/* 173 */     for (int i = start; i < toAppendTo.length(); i++) {
/* 174 */       char c = toAppendTo.charAt(i);
/* 175 */       switch (c) {
/*     */         case '\b':
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case '"':
/*     */         case '\\':
/* 183 */           escapeCount++;
/*     */           break;
/*     */         default:
/* 186 */           if (Character.isISOControl(c)) {
/* 187 */             escapeCount += 5;
/*     */           }
/*     */           break;
/*     */       } 
/*     */     } 
/* 192 */     int lastChar = toAppendTo.length() - 1;
/* 193 */     toAppendTo.setLength(toAppendTo.length() + escapeCount);
/* 194 */     int lastPos = toAppendTo.length() - 1;
/*     */     
/* 196 */     for (int j = lastChar; lastPos > j; j--) {
/* 197 */       char c = toAppendTo.charAt(j);
/* 198 */       switch (c) {
/*     */         case '\b':
/* 200 */           lastPos = escapeAndDecrement(toAppendTo, lastPos, 'b');
/*     */           break;
/*     */         
/*     */         case '\t':
/* 204 */           lastPos = escapeAndDecrement(toAppendTo, lastPos, 't');
/*     */           break;
/*     */         
/*     */         case '\f':
/* 208 */           lastPos = escapeAndDecrement(toAppendTo, lastPos, 'f');
/*     */           break;
/*     */         
/*     */         case '\n':
/* 212 */           lastPos = escapeAndDecrement(toAppendTo, lastPos, 'n');
/*     */           break;
/*     */         
/*     */         case '\r':
/* 216 */           lastPos = escapeAndDecrement(toAppendTo, lastPos, 'r');
/*     */           break;
/*     */         
/*     */         case '"':
/*     */         case '\\':
/* 221 */           lastPos = escapeAndDecrement(toAppendTo, lastPos, c);
/*     */           break;
/*     */         
/*     */         default:
/* 225 */           if (Character.isISOControl(c)) {
/*     */             
/* 227 */             toAppendTo.setCharAt(lastPos--, Chars.getUpperCaseHex(c & 0xF));
/* 228 */             toAppendTo.setCharAt(lastPos--, Chars.getUpperCaseHex((c & 0xF0) >> 4));
/* 229 */             toAppendTo.setCharAt(lastPos--, '0');
/* 230 */             toAppendTo.setCharAt(lastPos--, '0');
/* 231 */             toAppendTo.setCharAt(lastPos--, 'u');
/* 232 */             toAppendTo.setCharAt(lastPos--, '\\'); break;
/*     */           } 
/* 234 */           toAppendTo.setCharAt(lastPos, c);
/* 235 */           lastPos--;
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int escapeAndDecrement(StringBuilder toAppendTo, int lastPos, char c) {
/* 242 */     toAppendTo.setCharAt(lastPos--, c);
/* 243 */     toAppendTo.setCharAt(lastPos--, '\\');
/* 244 */     return lastPos;
/*     */   }
/*     */   
/*     */   public static void escapeXml(StringBuilder toAppendTo, int start) {
/* 248 */     int escapeCount = 0;
/* 249 */     for (int i = start; i < toAppendTo.length(); i++) {
/* 250 */       char c = toAppendTo.charAt(i);
/* 251 */       switch (c) {
/*     */         case '&':
/* 253 */           escapeCount += 4;
/*     */           break;
/*     */         case '<':
/*     */         case '>':
/* 257 */           escapeCount += 3;
/*     */           break;
/*     */         case '"':
/*     */         case '\'':
/* 261 */           escapeCount += 5;
/*     */           break;
/*     */       } 
/*     */     } 
/* 265 */     int lastChar = toAppendTo.length() - 1;
/* 266 */     toAppendTo.setLength(toAppendTo.length() + escapeCount);
/* 267 */     int lastPos = toAppendTo.length() - 1;
/*     */     
/* 269 */     for (int j = lastChar; lastPos > j; j--) {
/* 270 */       char c = toAppendTo.charAt(j);
/* 271 */       switch (c) {
/*     */         case '&':
/* 273 */           toAppendTo.setCharAt(lastPos--, ';');
/* 274 */           toAppendTo.setCharAt(lastPos--, 'p');
/* 275 */           toAppendTo.setCharAt(lastPos--, 'm');
/* 276 */           toAppendTo.setCharAt(lastPos--, 'a');
/* 277 */           toAppendTo.setCharAt(lastPos--, '&');
/*     */           break;
/*     */         case '<':
/* 280 */           toAppendTo.setCharAt(lastPos--, ';');
/* 281 */           toAppendTo.setCharAt(lastPos--, 't');
/* 282 */           toAppendTo.setCharAt(lastPos--, 'l');
/* 283 */           toAppendTo.setCharAt(lastPos--, '&');
/*     */           break;
/*     */         case '>':
/* 286 */           toAppendTo.setCharAt(lastPos--, ';');
/* 287 */           toAppendTo.setCharAt(lastPos--, 't');
/* 288 */           toAppendTo.setCharAt(lastPos--, 'g');
/* 289 */           toAppendTo.setCharAt(lastPos--, '&');
/*     */           break;
/*     */         case '"':
/* 292 */           toAppendTo.setCharAt(lastPos--, ';');
/* 293 */           toAppendTo.setCharAt(lastPos--, 't');
/* 294 */           toAppendTo.setCharAt(lastPos--, 'o');
/* 295 */           toAppendTo.setCharAt(lastPos--, 'u');
/* 296 */           toAppendTo.setCharAt(lastPos--, 'q');
/* 297 */           toAppendTo.setCharAt(lastPos--, '&');
/*     */           break;
/*     */         case '\'':
/* 300 */           toAppendTo.setCharAt(lastPos--, ';');
/* 301 */           toAppendTo.setCharAt(lastPos--, 's');
/* 302 */           toAppendTo.setCharAt(lastPos--, 'o');
/* 303 */           toAppendTo.setCharAt(lastPos--, 'p');
/* 304 */           toAppendTo.setCharAt(lastPos--, 'a');
/* 305 */           toAppendTo.setCharAt(lastPos--, '&');
/*     */           break;
/*     */         default:
/* 308 */           toAppendTo.setCharAt(lastPos--, c);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\StringBuilders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */