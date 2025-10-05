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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GarbageFreeSortedArrayThreadContextMap
/*     */   implements ReadOnlyThreadContextMap, ObjectThreadContextMap
/*     */ {
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   protected static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
/*     */   protected final ThreadLocal<StringMap> localMap;
/*     */   private static volatile int initialCapacity;
/*     */   private static volatile boolean inheritableMap;
/*     */   
/*     */   static void init() {
/*  66 */     PropertiesUtil properties = PropertiesUtil.getProperties();
/*  67 */     initialCapacity = properties.getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16);
/*  68 */     inheritableMap = properties.getBooleanProperty("isThreadContextMapInheritable");
/*     */   }
/*     */   
/*     */   static {
/*  72 */     init();
/*     */   }
/*     */   
/*     */   public GarbageFreeSortedArrayThreadContextMap() {
/*  76 */     this.localMap = createThreadLocalMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ThreadLocal<StringMap> createThreadLocalMap() {
/*  82 */     if (inheritableMap) {
/*  83 */       return new InheritableThreadLocal<StringMap>()
/*     */         {
/*     */           protected StringMap childValue(StringMap parentValue) {
/*  86 */             return (parentValue != null) ? GarbageFreeSortedArrayThreadContextMap.this.createStringMap((ReadOnlyStringMap)parentValue) : null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*  91 */     return new ThreadLocal<>();
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
/* 102 */     return (StringMap)new SortedArrayStringMap(initialCapacity);
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
/* 115 */     return (StringMap)new SortedArrayStringMap(original);
/*     */   }
/*     */   
/*     */   private StringMap getThreadLocalMap() {
/* 119 */     StringMap map = this.localMap.get();
/* 120 */     if (map == null) {
/* 121 */       map = createStringMap();
/* 122 */       this.localMap.set(map);
/*     */     } 
/* 124 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/* 129 */     getThreadLocalMap().putValue(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 134 */     getThreadLocalMap().putValue(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<String, String> values) {
/* 139 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 142 */     StringMap map = getThreadLocalMap();
/* 143 */     for (Map.Entry<String, String> entry : values.entrySet()) {
/* 144 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> void putAllValues(Map<String, V> values) {
/* 150 */     if (values == null || values.isEmpty()) {
/*     */       return;
/*     */     }
/* 153 */     StringMap map = getThreadLocalMap();
/* 154 */     for (Map.Entry<String, V> entry : values.entrySet()) {
/* 155 */       map.putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 161 */     return getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 166 */     StringMap map = this.localMap.get();
/* 167 */     return (map == null) ? null : (V)map.getValue(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 172 */     StringMap map = this.localMap.get();
/* 173 */     if (map != null) {
/* 174 */       map.remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(Iterable<String> keys) {
/* 180 */     StringMap map = this.localMap.get();
/* 181 */     if (map != null) {
/* 182 */       for (String key : keys) {
/* 183 */         map.remove(key);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 190 */     StringMap map = this.localMap.get();
/* 191 */     if (map != null) {
/* 192 */       map.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 198 */     StringMap map = this.localMap.get();
/* 199 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 204 */     StringMap map = this.localMap.get();
/* 205 */     return (map == null) ? new HashMap<>() : map.toMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringMap getReadOnlyContextData() {
/* 213 */     StringMap map = this.localMap.get();
/* 214 */     if (map == null) {
/* 215 */       map = createStringMap();
/* 216 */       this.localMap.set(map);
/*     */     } 
/* 218 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 223 */     StringMap map = this.localMap.get();
/* 224 */     return (map == null) ? null : Collections.<String, String>unmodifiableMap(map.toMap());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 229 */     StringMap map = this.localMap.get();
/* 230 */     return (map == null || map.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 235 */     StringMap map = this.localMap.get();
/* 236 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 241 */     int prime = 31;
/* 242 */     int result = 1;
/* 243 */     StringMap map = this.localMap.get();
/* 244 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 245 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 250 */     if (this == obj) {
/* 251 */       return true;
/*     */     }
/* 253 */     if (obj == null) {
/* 254 */       return false;
/*     */     }
/* 256 */     if (!(obj instanceof ThreadContextMap)) {
/* 257 */       return false;
/*     */     }
/* 259 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 260 */     Map<String, String> map = getImmutableMapOrNull();
/* 261 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 262 */     return Objects.equals(map, otherMap);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\GarbageFreeSortedArrayThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */