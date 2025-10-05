/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.logging.log4j.util.BiConsumer;
/*     */ import org.apache.logging.log4j.util.EnglishEnums;
/*     */ import org.apache.logging.log4j.util.IndexedReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.IndexedStringMap;
/*     */ import org.apache.logging.log4j.util.MultiFormatStringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ import org.apache.logging.log4j.util.TriConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public class MapMessage<M extends MapMessage<M, V>, V>
/*     */   implements MultiFormatStringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = -5031471831131487120L;
/*     */   private final IndexedStringMap data;
/*     */   
/*     */   public enum MapFormat
/*     */   {
/*  63 */     XML,
/*     */ 
/*     */     
/*  66 */     JSON,
/*     */ 
/*     */     
/*  69 */     JAVA,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     JAVA_UNQUOTED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static MapFormat lookupIgnoreCase(String format) {
/*  85 */       return XML.name().equalsIgnoreCase(format) ? XML : (
/*  86 */         JSON.name().equalsIgnoreCase(format) ? JSON : (
/*  87 */         JAVA.name().equalsIgnoreCase(format) ? JAVA : (
/*  88 */         JAVA_UNQUOTED.name().equalsIgnoreCase(format) ? JAVA_UNQUOTED : null)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static String[] names() {
/*  98 */       return new String[] { XML.name(), JSON.name(), JAVA.name(), JAVA_UNQUOTED.name() };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage() {
/* 108 */     this.data = (IndexedStringMap)new SortedArrayStringMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage(int initialCapacity) {
/* 117 */     this.data = (IndexedStringMap)new SortedArrayStringMap(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapMessage(Map<String, V> map) {
/* 125 */     this.data = (IndexedStringMap)new SortedArrayStringMap(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getFormats() {
/* 130 */     return MapFormat.names();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getParameters() {
/* 139 */     Object[] result = new Object[this.data.size()];
/* 140 */     for (int i = 0; i < this.data.size(); i++) {
/* 141 */       result[i] = this.data.getValueAt(i);
/*     */     }
/* 143 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 152 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, V> getData() {
/* 161 */     TreeMap<String, V> result = new TreeMap<>();
/* 162 */     for (int i = 0; i < this.data.size(); i++)
/*     */     {
/* 164 */       result.put(this.data.getKeyAt(i), (V)this.data.getValueAt(i));
/*     */     }
/* 166 */     return Collections.unmodifiableMap(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexedReadOnlyStringMap getIndexedReadOnlyStringMap() {
/* 174 */     return (IndexedReadOnlyStringMap)this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 181 */     this.data.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 192 */     return this.data.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String candidateKey, String value) {
/* 201 */     if (value == null) {
/* 202 */       throw new IllegalArgumentException("No value provided for key " + candidateKey);
/*     */     }
/* 204 */     String key = toKey(candidateKey);
/* 205 */     validate(key, value);
/* 206 */     this.data.putValue(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> map) {
/* 214 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/* 215 */       this.data.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 225 */     Object result = this.data.getValue(key);
/* 226 */     return ParameterFormatter.deepToString(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remove(String key) {
/* 235 */     String result = get(key);
/* 236 */     this.data.remove(key);
/* 237 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asString() {
/* 246 */     return format((MapFormat)null, new StringBuilder()).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String asString(String format) {
/*     */     try {
/* 257 */       return format((MapFormat)EnglishEnums.valueOf(MapFormat.class, format), new StringBuilder()).toString();
/* 258 */     } catch (IllegalArgumentException ex) {
/* 259 */       return asString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <CV> void forEach(BiConsumer<String, ? super CV> action) {
/* 282 */     this.data.forEach(action);
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
/*     */   public <CV, S> void forEach(TriConsumer<String, ? super CV, S> action, S state) {
/* 311 */     this.data.forEach(action, state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuilder format(MapFormat format, StringBuilder sb) {
/* 321 */     if (format == null)
/* 322 */     { appendMap(sb); }
/*     */     else
/* 324 */     { switch (format)
/*     */       { case XML:
/* 326 */           asXml(sb);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 345 */           return sb;case JSON: asJson(sb); return sb;case JAVA: asJava(sb); return sb;case JAVA_UNQUOTED: asJavaUnquoted(sb); return sb; }  appendMap(sb); }  return sb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void asXml(StringBuilder sb) {
/* 354 */     sb.append("<Map>\n");
/* 355 */     for (int i = 0; i < this.data.size(); i++) {
/* 356 */       sb.append("  <Entry key=\"")
/* 357 */         .append(this.data.getKeyAt(i))
/* 358 */         .append("\">");
/* 359 */       int size = sb.length();
/* 360 */       ParameterFormatter.recursiveDeepToString(this.data.getValueAt(i), sb);
/* 361 */       StringBuilders.escapeXml(sb, size);
/* 362 */       sb.append("</Entry>\n");
/*     */     } 
/* 364 */     sb.append("</Map>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFormattedMessage() {
/* 373 */     return asString();
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
/*     */   public String getFormattedMessage(String[] formats) {
/* 388 */     return format(getFormat(formats), new StringBuilder()).toString();
/*     */   }
/*     */   
/*     */   private MapFormat getFormat(String[] formats) {
/* 392 */     if (formats == null || formats.length == 0) {
/* 393 */       return null;
/*     */     }
/* 395 */     for (int i = 0; i < formats.length; i++) {
/* 396 */       MapFormat mapFormat = MapFormat.lookupIgnoreCase(formats[i]);
/* 397 */       if (mapFormat != null) {
/* 398 */         return mapFormat;
/*     */       }
/*     */     } 
/* 401 */     return null;
/*     */   }
/*     */   
/*     */   protected void appendMap(StringBuilder sb) {
/* 405 */     for (int i = 0; i < this.data.size(); i++) {
/* 406 */       if (i > 0) {
/* 407 */         sb.append(' ');
/*     */       }
/* 409 */       sb.append(this.data.getKeyAt(i)).append('=').append('"');
/* 410 */       ParameterFormatter.recursiveDeepToString(this.data.getValueAt(i), sb);
/* 411 */       sb.append('"');
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void asJson(StringBuilder sb) {
/* 416 */     MapMessageJsonFormatter.format(sb, this.data);
/*     */   }
/*     */   
/*     */   protected void asJavaUnquoted(StringBuilder sb) {
/* 420 */     asJava(sb, false);
/*     */   }
/*     */   
/*     */   protected void asJava(StringBuilder sb) {
/* 424 */     asJava(sb, true);
/*     */   }
/*     */   
/*     */   private void asJava(StringBuilder sb, boolean quoted) {
/* 428 */     sb.append('{');
/* 429 */     for (int i = 0; i < this.data.size(); i++) {
/* 430 */       if (i > 0) {
/* 431 */         sb.append(", ");
/*     */       }
/* 433 */       sb.append(this.data.getKeyAt(i)).append('=');
/* 434 */       if (quoted) {
/* 435 */         sb.append('"');
/*     */       }
/* 437 */       ParameterFormatter.recursiveDeepToString(this.data.getValueAt(i), sb);
/* 438 */       if (quoted) {
/* 439 */         sb.append('"');
/*     */       }
/*     */     } 
/* 442 */     sb.append('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M newInstance(Map<String, V> map) {
/* 452 */     return (M)new MapMessage(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 457 */     return asString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 462 */     format((MapFormat)null, buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(String[] formats, StringBuilder buffer) {
/* 467 */     format(getFormat(formats), buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 472 */     if (this == o) {
/* 473 */       return true;
/*     */     }
/* 475 */     if (o == null || getClass() != o.getClass()) {
/* 476 */       return false;
/*     */     }
/*     */     
/* 479 */     MapMessage<?, ?> that = (MapMessage<?, ?>)o;
/*     */     
/* 481 */     return this.data.equals(that.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 486 */     return this.data.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/* 496 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, boolean value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, byte value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, char value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, double value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, float value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, int value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, long value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, Object value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, short value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validate(String key, String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String toKey(String candidateKey) {
/* 597 */     return candidateKey;
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
/*     */   public M with(String candidateKey, boolean value) {
/* 609 */     String key = toKey(candidateKey);
/* 610 */     validate(key, value);
/* 611 */     this.data.putValue(key, Boolean.valueOf(value));
/* 612 */     return (M)this;
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
/*     */   public M with(String candidateKey, byte value) {
/* 624 */     String key = toKey(candidateKey);
/* 625 */     validate(key, value);
/* 626 */     this.data.putValue(key, Byte.valueOf(value));
/* 627 */     return (M)this;
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
/*     */   public M with(String candidateKey, char value) {
/* 639 */     String key = toKey(candidateKey);
/* 640 */     validate(key, value);
/* 641 */     this.data.putValue(key, Character.valueOf(value));
/* 642 */     return (M)this;
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
/*     */   public M with(String candidateKey, double value) {
/* 655 */     String key = toKey(candidateKey);
/* 656 */     validate(key, value);
/* 657 */     this.data.putValue(key, Double.valueOf(value));
/* 658 */     return (M)this;
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
/*     */   public M with(String candidateKey, float value) {
/* 670 */     String key = toKey(candidateKey);
/* 671 */     validate(key, value);
/* 672 */     this.data.putValue(key, Float.valueOf(value));
/* 673 */     return (M)this;
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
/*     */   public M with(String candidateKey, int value) {
/* 685 */     String key = toKey(candidateKey);
/* 686 */     validate(key, value);
/* 687 */     this.data.putValue(key, Integer.valueOf(value));
/* 688 */     return (M)this;
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
/*     */   public M with(String candidateKey, long value) {
/* 700 */     String key = toKey(candidateKey);
/* 701 */     validate(key, value);
/* 702 */     this.data.putValue(key, Long.valueOf(value));
/* 703 */     return (M)this;
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
/*     */   public M with(String candidateKey, Object value) {
/* 715 */     String key = toKey(candidateKey);
/* 716 */     validate(key, value);
/* 717 */     this.data.putValue(key, value);
/* 718 */     return (M)this;
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
/*     */   public M with(String candidateKey, short value) {
/* 730 */     String key = toKey(candidateKey);
/* 731 */     validate(key, value);
/* 732 */     this.data.putValue(key, Short.valueOf(value));
/* 733 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M with(String candidateKey, String value) {
/* 744 */     String key = toKey(candidateKey);
/* 745 */     put(key, value);
/* 746 */     return (M)this;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\message\MapMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */