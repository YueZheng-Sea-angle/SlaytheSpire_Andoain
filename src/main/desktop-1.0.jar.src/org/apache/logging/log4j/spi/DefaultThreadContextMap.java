/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.util.BiConsumer;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
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
/*     */ public class DefaultThreadContextMap
/*     */   implements ThreadContextMap, ReadOnlyStringMap
/*     */ {
/*     */   private static final long serialVersionUID = 8218007901108944053L;
/*     */   public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
/*     */   private final boolean useMap;
/*     */   private final ThreadLocal<Map<String, String>> localMap;
/*     */   private static boolean inheritableMap;
/*     */   
/*     */   static {
/*  50 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
/*  56 */     if (inheritableMap) {
/*  57 */       return new InheritableThreadLocal<Map<String, String>>()
/*     */         {
/*     */           protected Map<String, String> childValue(Map<String, String> parentValue) {
/*  60 */             return (parentValue != null && isMapEnabled) ? 
/*  61 */               Collections.<String, String>unmodifiableMap(new HashMap<>(parentValue)) : null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*  67 */     return new ThreadLocal<>();
/*     */   }
/*     */   
/*     */   static void init() {
/*  71 */     inheritableMap = PropertiesUtil.getProperties().getBooleanProperty("isThreadContextMapInheritable");
/*     */   }
/*     */   
/*     */   public DefaultThreadContextMap() {
/*  75 */     this(true);
/*     */   }
/*     */   
/*     */   public DefaultThreadContextMap(boolean useMap) {
/*  79 */     this.useMap = useMap;
/*  80 */     this.localMap = createThreadLocalMap(useMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String key, String value) {
/*  85 */     if (!this.useMap) {
/*     */       return;
/*     */     }
/*  88 */     Map<String, String> map = this.localMap.get();
/*  89 */     map = (map == null) ? new HashMap<>(1) : new HashMap<>(map);
/*  90 */     map.put(key, value);
/*  91 */     this.localMap.set(Collections.unmodifiableMap(map));
/*     */   }
/*     */   
/*     */   public void putAll(Map<String, String> m) {
/*  95 */     if (!this.useMap) {
/*     */       return;
/*     */     }
/*  98 */     Map<String, String> map = this.localMap.get();
/*  99 */     map = (map == null) ? new HashMap<>(m.size()) : new HashMap<>(map);
/* 100 */     for (Map.Entry<String, String> e : m.entrySet()) {
/* 101 */       map.put(e.getKey(), e.getValue());
/*     */     }
/* 103 */     this.localMap.set(Collections.unmodifiableMap(map));
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 108 */     Map<String, String> map = this.localMap.get();
/* 109 */     return (map == null) ? null : map.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 114 */     Map<String, String> map = this.localMap.get();
/* 115 */     if (map != null) {
/* 116 */       Map<String, String> copy = new HashMap<>(map);
/* 117 */       copy.remove(key);
/* 118 */       this.localMap.set(Collections.unmodifiableMap(copy));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeAll(Iterable<String> keys) {
/* 123 */     Map<String, String> map = this.localMap.get();
/* 124 */     if (map != null) {
/* 125 */       Map<String, String> copy = new HashMap<>(map);
/* 126 */       for (String key : keys) {
/* 127 */         copy.remove(key);
/*     */       }
/* 129 */       this.localMap.set(Collections.unmodifiableMap(copy));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 135 */     this.localMap.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toMap() {
/* 140 */     return getCopy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 145 */     Map<String, String> map = this.localMap.get();
/* 146 */     return (map != null && map.containsKey(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> void forEach(BiConsumer<String, ? super V> action) {
/* 151 */     Map<String, String> map = this.localMap.get();
/* 152 */     if (map == null) {
/*     */       return;
/*     */     }
/* 155 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/*     */ 
/*     */ 
/*     */       
/* 159 */       V value = (V)entry.getValue();
/* 160 */       action.accept(entry.getKey(), value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) {
/* 166 */     Map<String, String> map = this.localMap.get();
/* 167 */     if (map == null) {
/*     */       return;
/*     */     }
/* 170 */     for (Map.Entry<String, String> entry : map.entrySet()) {
/*     */ 
/*     */ 
/*     */       
/* 174 */       V value = (V)entry.getValue();
/* 175 */       action.accept(entry.getKey(), value, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 182 */     Map<String, String> map = this.localMap.get();
/* 183 */     return (map == null) ? null : (V)map.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getCopy() {
/* 188 */     Map<String, String> map = this.localMap.get();
/* 189 */     return (map == null) ? new HashMap<>() : new HashMap<>(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getImmutableMapOrNull() {
/* 194 */     return this.localMap.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 199 */     Map<String, String> map = this.localMap.get();
/* 200 */     return (map == null || map.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 205 */     Map<String, String> map = this.localMap.get();
/* 206 */     return (map == null) ? 0 : map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 211 */     Map<String, String> map = this.localMap.get();
/* 212 */     return (map == null) ? "{}" : map.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 217 */     int prime = 31;
/* 218 */     int result = 1;
/* 219 */     Map<String, String> map = this.localMap.get();
/* 220 */     result = 31 * result + ((map == null) ? 0 : map.hashCode());
/* 221 */     result = 31 * result + Boolean.valueOf(this.useMap).hashCode();
/* 222 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 227 */     if (this == obj) {
/* 228 */       return true;
/*     */     }
/* 230 */     if (obj == null) {
/* 231 */       return false;
/*     */     }
/* 233 */     if (obj instanceof DefaultThreadContextMap) {
/* 234 */       DefaultThreadContextMap defaultThreadContextMap = (DefaultThreadContextMap)obj;
/* 235 */       if (this.useMap != defaultThreadContextMap.useMap) {
/* 236 */         return false;
/*     */       }
/*     */     } 
/* 239 */     if (!(obj instanceof ThreadContextMap)) {
/* 240 */       return false;
/*     */     }
/* 242 */     ThreadContextMap other = (ThreadContextMap)obj;
/* 243 */     Map<String, String> map = this.localMap.get();
/* 244 */     Map<String, String> otherMap = other.getImmutableMapOrNull();
/* 245 */     return Objects.equals(map, otherMap);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\DefaultThreadContextMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */