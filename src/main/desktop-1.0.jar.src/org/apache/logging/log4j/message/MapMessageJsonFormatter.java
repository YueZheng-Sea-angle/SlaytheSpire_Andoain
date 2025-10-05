/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.util.IndexedStringMap;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ enum MapMessageJsonFormatter
/*     */ {
/*     */   public static final int MAX_DEPTH;
/*     */   private static final char DQUOTE = '"';
/*     */   private static final char RBRACE = ']';
/*     */   private static final char LBRACE = '[';
/*     */   private static final char COMMA = ',';
/*     */   private static final char RCURLY = '}';
/*     */   private static final char LCURLY = '{';
/*     */   private static final char COLON = ':';
/*     */   
/*     */   static {
/*  50 */     MAX_DEPTH = readMaxDepth();
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
/*     */   private static int readMaxDepth() {
/*  69 */     int maxDepth = PropertiesUtil.getProperties().getIntegerProperty("log4j2.mapMessage.jsonFormatter.maxDepth", 8);
/*  70 */     if (maxDepth < 0) {
/*  71 */       throw new IllegalArgumentException("was expecting a positive maxDepth, found: " + maxDepth);
/*     */     }
/*     */     
/*  74 */     return maxDepth;
/*     */   }
/*     */   
/*     */   static void format(StringBuilder sb, Object object) {
/*  78 */     format(sb, object, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void format(StringBuilder sb, Object object, int depth) {
/*  86 */     if (depth >= MAX_DEPTH) {
/*  87 */       throw new IllegalArgumentException("maxDepth has been exceeded");
/*     */     }
/*     */ 
/*     */     
/*  91 */     if (object == null) {
/*  92 */       sb.append("null");
/*     */ 
/*     */     
/*     */     }
/*  96 */     else if (object instanceof IndexedStringMap) {
/*  97 */       IndexedStringMap map = (IndexedStringMap)object;
/*  98 */       formatIndexedStringMap(sb, map, depth);
/*  99 */     } else if (object instanceof Map) {
/*     */       
/* 101 */       Map<Object, Object> map = (Map<Object, Object>)object;
/* 102 */       formatMap(sb, map, depth);
/*     */ 
/*     */     
/*     */     }
/* 106 */     else if (object instanceof List) {
/*     */       
/* 108 */       List<Object> list = (List<Object>)object;
/* 109 */       formatList(sb, list, depth);
/* 110 */     } else if (object instanceof Collection) {
/*     */       
/* 112 */       Collection<Object> collection = (Collection<Object>)object;
/* 113 */       formatCollection(sb, collection, depth);
/*     */ 
/*     */     
/*     */     }
/* 117 */     else if (object instanceof Number) {
/* 118 */       Number number = (Number)object;
/* 119 */       formatNumber(sb, number);
/* 120 */     } else if (object instanceof Boolean) {
/* 121 */       boolean booleanValue = ((Boolean)object).booleanValue();
/* 122 */       formatBoolean(sb, booleanValue);
/*     */ 
/*     */     
/*     */     }
/* 126 */     else if (object instanceof StringBuilderFormattable) {
/* 127 */       StringBuilderFormattable formattable = (StringBuilderFormattable)object;
/* 128 */       formatFormattable(sb, formattable);
/*     */ 
/*     */     
/*     */     }
/* 132 */     else if (object instanceof char[]) {
/* 133 */       char[] charValues = (char[])object;
/* 134 */       formatCharArray(sb, charValues);
/* 135 */     } else if (object instanceof boolean[]) {
/* 136 */       boolean[] booleanValues = (boolean[])object;
/* 137 */       formatBooleanArray(sb, booleanValues);
/* 138 */     } else if (object instanceof byte[]) {
/* 139 */       byte[] byteValues = (byte[])object;
/* 140 */       formatByteArray(sb, byteValues);
/* 141 */     } else if (object instanceof short[]) {
/* 142 */       short[] shortValues = (short[])object;
/* 143 */       formatShortArray(sb, shortValues);
/* 144 */     } else if (object instanceof int[]) {
/* 145 */       int[] intValues = (int[])object;
/* 146 */       formatIntArray(sb, intValues);
/* 147 */     } else if (object instanceof long[]) {
/* 148 */       long[] longValues = (long[])object;
/* 149 */       formatLongArray(sb, longValues);
/* 150 */     } else if (object instanceof float[]) {
/* 151 */       float[] floatValues = (float[])object;
/* 152 */       formatFloatArray(sb, floatValues);
/* 153 */     } else if (object instanceof double[]) {
/* 154 */       double[] doubleValues = (double[])object;
/* 155 */       formatDoubleArray(sb, doubleValues);
/* 156 */     } else if (object instanceof Object[]) {
/* 157 */       Object[] objectValues = (Object[])object;
/* 158 */       formatObjectArray(sb, objectValues, depth);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 163 */       formatString(sb, object);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatIndexedStringMap(StringBuilder sb, IndexedStringMap map, int depth) {
/* 172 */     sb.append('{');
/* 173 */     int nextDepth = depth + 1;
/* 174 */     for (int entryIndex = 0; entryIndex < map.size(); entryIndex++) {
/* 175 */       String key = map.getKeyAt(entryIndex);
/* 176 */       Object value = map.getValueAt(entryIndex);
/* 177 */       if (entryIndex > 0) {
/* 178 */         sb.append(',');
/*     */       }
/* 180 */       sb.append('"');
/* 181 */       int keyStartIndex = sb.length();
/* 182 */       sb.append(key);
/* 183 */       StringBuilders.escapeJson(sb, keyStartIndex);
/* 184 */       sb.append('"').append(':');
/* 185 */       format(sb, value, nextDepth);
/*     */     } 
/* 187 */     sb.append('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatMap(StringBuilder sb, Map<Object, Object> map, int depth) {
/* 194 */     sb.append('{');
/* 195 */     int nextDepth = depth + 1;
/* 196 */     boolean[] firstEntry = { true };
/* 197 */     map.forEach((key, value) -> {
/*     */           if (key == null) {
/*     */             throw new IllegalArgumentException("null keys are not allowed");
/*     */           }
/*     */           if (firstEntry[0]) {
/*     */             firstEntry[0] = false;
/*     */           } else {
/*     */             sb.append(',');
/*     */           } 
/*     */           sb.append('"');
/*     */           String keyString = String.valueOf(key);
/*     */           int keyStartIndex = sb.length();
/*     */           sb.append(keyString);
/*     */           StringBuilders.escapeJson(sb, keyStartIndex);
/*     */           sb.append('"').append(':');
/*     */           format(sb, value, nextDepth);
/*     */         });
/* 214 */     sb.append('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatList(StringBuilder sb, List<Object> items, int depth) {
/* 221 */     sb.append('[');
/* 222 */     int nextDepth = depth + 1;
/* 223 */     for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
/* 224 */       if (itemIndex > 0) {
/* 225 */         sb.append(',');
/*     */       }
/* 227 */       Object item = items.get(itemIndex);
/* 228 */       format(sb, item, nextDepth);
/*     */     } 
/* 230 */     sb.append(']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatCollection(StringBuilder sb, Collection<Object> items, int depth) {
/* 237 */     sb.append('[');
/* 238 */     int nextDepth = depth + 1;
/* 239 */     boolean[] firstItem = { true };
/* 240 */     items.forEach(item -> {
/*     */           if (firstItem[0]) {
/*     */             firstItem[0] = false;
/*     */           } else {
/*     */             sb.append(',');
/*     */           } 
/*     */           format(sb, item, nextDepth);
/*     */         });
/* 248 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatNumber(StringBuilder sb, Number number) {
/* 252 */     if (number instanceof BigDecimal) {
/* 253 */       BigDecimal decimalNumber = (BigDecimal)number;
/* 254 */       sb.append(decimalNumber.toString());
/* 255 */     } else if (number instanceof Double) {
/* 256 */       double doubleNumber = ((Double)number).doubleValue();
/* 257 */       sb.append(doubleNumber);
/* 258 */     } else if (number instanceof Float) {
/* 259 */       float floatNumber = ((Float)number).floatValue();
/* 260 */       sb.append(floatNumber);
/* 261 */     } else if (number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
/*     */ 
/*     */ 
/*     */       
/* 265 */       long longNumber = number.longValue();
/* 266 */       sb.append(longNumber);
/*     */     } else {
/* 268 */       long longNumber = number.longValue();
/* 269 */       double doubleValue = number.doubleValue();
/* 270 */       if (Double.compare(longNumber, doubleValue) == 0) {
/* 271 */         sb.append(longNumber);
/*     */       } else {
/* 273 */         sb.append(doubleValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void formatBoolean(StringBuilder sb, boolean booleanValue) {
/* 279 */     sb.append(booleanValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatFormattable(StringBuilder sb, StringBuilderFormattable formattable) {
/* 285 */     sb.append('"');
/* 286 */     int startIndex = sb.length();
/* 287 */     formattable.formatTo(sb);
/* 288 */     StringBuilders.escapeJson(sb, startIndex);
/* 289 */     sb.append('"');
/*     */   }
/*     */   
/*     */   private static void formatCharArray(StringBuilder sb, char[] items) {
/* 293 */     sb.append('[');
/* 294 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 295 */       if (itemIndex > 0) {
/* 296 */         sb.append(',');
/*     */       }
/* 298 */       char item = items[itemIndex];
/* 299 */       sb.append('"');
/* 300 */       int startIndex = sb.length();
/* 301 */       sb.append(item);
/* 302 */       StringBuilders.escapeJson(sb, startIndex);
/* 303 */       sb.append('"');
/*     */     } 
/* 305 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatBooleanArray(StringBuilder sb, boolean[] items) {
/* 309 */     sb.append('[');
/* 310 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 311 */       if (itemIndex > 0) {
/* 312 */         sb.append(',');
/*     */       }
/* 314 */       boolean item = items[itemIndex];
/* 315 */       sb.append(item);
/*     */     } 
/* 317 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatByteArray(StringBuilder sb, byte[] items) {
/* 321 */     sb.append('[');
/* 322 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 323 */       if (itemIndex > 0) {
/* 324 */         sb.append(',');
/*     */       }
/* 326 */       byte item = items[itemIndex];
/* 327 */       sb.append(item);
/*     */     } 
/* 329 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatShortArray(StringBuilder sb, short[] items) {
/* 333 */     sb.append('[');
/* 334 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 335 */       if (itemIndex > 0) {
/* 336 */         sb.append(',');
/*     */       }
/* 338 */       short item = items[itemIndex];
/* 339 */       sb.append(item);
/*     */     } 
/* 341 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatIntArray(StringBuilder sb, int[] items) {
/* 345 */     sb.append('[');
/* 346 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 347 */       if (itemIndex > 0) {
/* 348 */         sb.append(',');
/*     */       }
/* 350 */       int item = items[itemIndex];
/* 351 */       sb.append(item);
/*     */     } 
/* 353 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatLongArray(StringBuilder sb, long[] items) {
/* 357 */     sb.append('[');
/* 358 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 359 */       if (itemIndex > 0) {
/* 360 */         sb.append(',');
/*     */       }
/* 362 */       long item = items[itemIndex];
/* 363 */       sb.append(item);
/*     */     } 
/* 365 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatFloatArray(StringBuilder sb, float[] items) {
/* 369 */     sb.append('[');
/* 370 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 371 */       if (itemIndex > 0) {
/* 372 */         sb.append(',');
/*     */       }
/* 374 */       float item = items[itemIndex];
/* 375 */       sb.append(item);
/*     */     } 
/* 377 */     sb.append(']');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatDoubleArray(StringBuilder sb, double[] items) {
/* 383 */     sb.append('[');
/* 384 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 385 */       if (itemIndex > 0) {
/* 386 */         sb.append(',');
/*     */       }
/* 388 */       double item = items[itemIndex];
/* 389 */       sb.append(item);
/*     */     } 
/* 391 */     sb.append(']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatObjectArray(StringBuilder sb, Object[] items, int depth) {
/* 398 */     sb.append('[');
/* 399 */     int nextDepth = depth + 1;
/* 400 */     for (int itemIndex = 0; itemIndex < items.length; itemIndex++) {
/* 401 */       if (itemIndex > 0) {
/* 402 */         sb.append(',');
/*     */       }
/* 404 */       Object item = items[itemIndex];
/* 405 */       format(sb, item, nextDepth);
/*     */     } 
/* 407 */     sb.append(']');
/*     */   }
/*     */   
/*     */   private static void formatString(StringBuilder sb, Object value) {
/* 411 */     sb.append('"');
/* 412 */     int startIndex = sb.length();
/* 413 */     String valueString = String.valueOf(value);
/* 414 */     sb.append(valueString);
/* 415 */     StringBuilders.escapeJson(sb, startIndex);
/* 416 */     sb.append('"');
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\MapMessageJsonFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */