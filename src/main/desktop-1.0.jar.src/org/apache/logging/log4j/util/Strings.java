/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Strings
/*     */ {
/*  30 */   private static final ThreadLocal<StringBuilder> tempStr = ThreadLocal.withInitial(StringBuilder::new);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String EMPTY = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public static final String[] EMPTY_ARRAY = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final String LINE_SEPARATOR = PropertiesUtil.getProperties().getStringProperty("line.separator", "\n");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String dquote(String str) {
/*  56 */     return '"' + str + '"';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBlank(String s) {
/*  67 */     if (s == null || s.isEmpty()) {
/*  68 */       return true;
/*     */     }
/*  70 */     for (int i = 0; i < s.length(); i++) {
/*  71 */       char c = s.charAt(i);
/*  72 */       if (!Character.isWhitespace(c)) {
/*  73 */         return false;
/*     */       }
/*     */     } 
/*  76 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(CharSequence cs) {
/* 105 */     return (cs == null || cs.length() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNotBlank(String s) {
/* 115 */     return !isBlank(s);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNotEmpty(CharSequence cs) {
/* 139 */     return !isEmpty(cs);
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
/*     */   public static String join(Iterable<?> iterable, char separator) {
/* 154 */     if (iterable == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     return join(iterable.iterator(), separator);
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
/*     */   public static String join(Iterator<?> iterator, char separator) {
/* 174 */     if (iterator == null) {
/* 175 */       return null;
/*     */     }
/* 177 */     if (!iterator.hasNext()) {
/* 178 */       return "";
/*     */     }
/* 180 */     Object first = iterator.next();
/* 181 */     if (!iterator.hasNext()) {
/* 182 */       return Objects.toString(first, "");
/*     */     }
/*     */ 
/*     */     
/* 186 */     StringBuilder buf = new StringBuilder(256);
/* 187 */     if (first != null) {
/* 188 */       buf.append(first);
/*     */     }
/*     */     
/* 191 */     while (iterator.hasNext()) {
/* 192 */       buf.append(separator);
/* 193 */       Object obj = iterator.next();
/* 194 */       if (obj != null) {
/* 195 */         buf.append(obj);
/*     */       }
/*     */     } 
/*     */     
/* 199 */     return buf.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String left(String str, int len) {
/* 227 */     if (str == null) {
/* 228 */       return null;
/*     */     }
/* 230 */     if (len < 0) {
/* 231 */       return "";
/*     */     }
/* 233 */     if (str.length() <= len) {
/* 234 */       return str;
/*     */     }
/* 236 */     return str.substring(0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String quote(String str) {
/* 246 */     return '\'' + str + '\'';
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String trimToNull(String str) {
/* 274 */     String ts = (str == null) ? null : str.trim();
/* 275 */     return isEmpty(ts) ? null : ts;
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
/*     */   public static String toRootUpperCase(String str) {
/* 289 */     return str.toUpperCase(Locale.ROOT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String concat(String str1, String str2) {
/* 299 */     if (isEmpty(str1))
/* 300 */       return str2; 
/* 301 */     if (isEmpty(str2)) {
/* 302 */       return str1;
/*     */     }
/* 304 */     StringBuilder sb = tempStr.get();
/*     */     try {
/* 306 */       return sb.append(str1).append(str2).toString();
/*     */     } finally {
/* 308 */       sb.setLength(0);
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
/*     */   public static String repeat(String str, int count) {
/* 320 */     Objects.requireNonNull(str, "str");
/* 321 */     if (count < 0) {
/* 322 */       throw new IllegalArgumentException("count");
/*     */     }
/* 324 */     StringBuilder sb = tempStr.get();
/*     */     try {
/* 326 */       for (int index = 0; index < count; index++) {
/* 327 */         sb.append(str);
/*     */       }
/* 329 */       return sb.toString();
/*     */     } finally {
/* 331 */       sb.setLength(0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */