/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.SortedArrayStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CopyOnWriteSortedArrayThreadContextMap
/*     */   implements ReadOnlyThreadContextMap, ObjectThreadContextMap, CopyOnWrite
/*     */ {
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   protected static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
/*  56 */   private static final StringMap EMPTY_CONTEXT_DATA = (StringMap)new SortedArrayStringMap(1);
/*     */   
/*     */   private static volatile int initialCapacity;
/*     */   
/*     */   private static volatile boolean inheritableMap;
/*     */   
/*     */   private final ThreadLocal<StringMap> localMap;
/*     */ 
/*     */   
/*     */   static void init() {
/*  66 */     PropertiesUtil properties = PropertiesUtil.getProperties();
/*  67 */     initialCapacity = properties.getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16);
/*  68 */     inheritableMap = properties.getBooleanProperty("isThreadContextMapInheritable");
/*     */   }
/*     */   
/*     */   static {
/*  72 */     EMPTY_CONTEXT_DATA.freeze();
/*  73 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CopyOnWriteSortedArrayThreadContextMap() {
/*  79 */     this.localMap = createThreadLocalMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadLocal<StringMap> createThreadLocalMap() {
/*  85 */     if (inheritableMap) {
/*  86 */       return new InheritableThreadLocal<StringMap>()
/*     */         {
/*     */           protected StringMap childValue(StringMap parentValue) {
/*  89 */             if (parentValue == null) {
/*  90 */               return null;
/*     */             }
/*  92 */             StringMap stringMap = CopyOnWriteSortedArrayThreadContextMap.this.createStringMap((ReadOnlyStringMap)parentValue);
/*  93 */             stringMap.freeze();
/*  94 */             return stringMap;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*  99 */     return new ThreadLocal<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringMap createStringMap() {
/* 110 */     return (StringMap)new SortedArrayStringMap(initialCapacity);
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
/*     */   protected StringMap createStringMap(ReadOnlyStringMap original) {
/* 123 */     return (StringMap)new SortedArrayStringMap(original);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/* 128 */     putValue(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 133 */     StringMap map = this.localMap.get();
/* 134 */     map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
/* 135 */     map.putValue(key, value);
/* 136 */     map.freeze();
/* 137 */     this.localMap.set(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> values) {
/* 142 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 145 */     StringMap map = this.localMap.get();
/* 146 */     map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
/* 147 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 148 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/* 150 */     map.freeze();
/* 151 */     this.localMap.set(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> void putAllValues(Map<String, V> values) {
/* 156 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 159 */     StringMap map = this.localMap.get();
/* 160 */     map = (map == null) ? createStringMap() : createStringMap((ReadOnlyStringMap)map);
/* 161 */     for (Map.Entry<String, V> entry : values.entrySet()) {
/* 162 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/* 164 */     map.freeze();
/* 165 */     this.localMap.set(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 170 */     return getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 175 */     StringMap map = this.localMap.get();
/* 176 */     return (map == null) ? null : (V)map.getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 181 */     StringMap map = this.localMap.get();
/* 182 */     if (map != null) {
/* 183 */       StringMap copy = createStringMap((ReadOnlyStringMap)map);
/* 184 */       copy.remove(key);
/* 185 */       copy.freeze();
/* 186 */       this.localMap.set(copy);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(Iterable<String> keys) {
/* 192 */     StringMap map = this.localMap.get();
/* 193 */     if (map != null) {
/* 194 */       StringMap copy = createStringMap((ReadOnlyStringMap)map);
/* 195 */       for (String key : keys) {
/* 196 */         copy.remove(key);
/*     */       }
/* 198 */       copy.freeze();
/* 199 */       this.localMap.set(copy);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 205 */     this.localMap.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 210 */     StringMap map = this.localMap.get();
/* 211 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 216 */     StringMap map = this.localMap.get();
/* 217 */     return (map == null) ? new HashMap<>() : map.toMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMap getReadOnlyContextData() {
/* 225 */     StringMap map = this.localMap.get();
/* 226 */     return (map == null) ? EMPTY_CONTEXT_DATA : map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 231 */     StringMap map = this.localMap.get();
/* 232 */     return (map == null) ? null : Collections.<String, String>unmodifiableMap(map.toMap());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 237 */     StringMap map = this.localMap.get();
/* 238 */     return (map == null || map.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 243 */     StringMap map = this.localMap.get();
/* 244 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 249 */     int prime = 31;
/* 250 */     int result = 1;
/* 251 */     StringMap map = this.localMap.get();
/* 252 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 253 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 258 */     if (this == obj) {
/* 259 */       return true;
/*     */     }
/* 261 */     if (obj == null) {
/* 262 */       return false;
/*     */     }
/* 264 */     if (!(obj instanceof ThreadContextMap)) {
/* 265 */       return false;
/*     */     }
/* 267 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 268 */     Map<String, String> map = getImmutableMapOrNull();
/* 269 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 270 */     return Objects.equals(map, otherMap);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\CopyOnWriteSortedArrayThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */