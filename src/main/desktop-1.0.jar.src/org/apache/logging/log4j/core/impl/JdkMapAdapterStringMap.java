/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.util.BiConsumer;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StringMap;
/*     */ import org.apache.logging.log4j.util.TriConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JdkMapAdapterStringMap
/*     */   implements StringMap
/*     */ {
/*     */   private static final long serialVersionUID = -7348247784983193612L;
/*     */   private static final String FROZEN = "Frozen collection cannot be modified";
/*     */   private static final Comparator<? super String> NULL_FIRST_COMPARATOR;
/*     */   private final Map<String, String> map;
/*     */   private boolean immutable = false;
/*     */   private transient String[] sortedKeys;
/*     */   private static TriConsumer<String, String, Map<String, String>> PUT_ALL;
/*     */   
/*     */   public JdkMapAdapterStringMap() {
/*     */     this(new HashMap<>());
/*     */   }
/*     */   
/*     */   public JdkMapAdapterStringMap(Map<String, String> map) {
/*     */     this.map = Objects.<Map<String, String>>requireNonNull(map, "map");
/*     */   }
/*     */   
/*     */   static {
/*  36 */     NULL_FIRST_COMPARATOR = ((left, right) -> (left == null) ? -1 : ((right == null) ? 1 : left.compareTo(right)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     PUT_ALL = ((key, value, stringStringMap) -> (String)stringStringMap.put(key, value));
/*     */   } public Map<String, String> toMap() { return this.map; } private void assertNotFrozen() { if (this.immutable) throw new UnsupportedOperationException("Frozen collection cannot be modified");  }
/*     */   public boolean containsKey(String key) { return this.map.containsKey(key); }
/*     */   public <V> void forEach(BiConsumer<String, ? super V> action) { String[] keys = getSortedKeys(); for (int i = 0; i < keys.length; i++) action.accept(keys[i], this.map.get(keys[i]));  }
/* 147 */   public void putValue(String key, Object value) { assertNotFrozen();
/* 148 */     this.map.put(key, (value == null) ? null : String.valueOf(value));
/* 149 */     this.sortedKeys = null; }
/*     */   public <V, S> void forEach(TriConsumer<String, ? super V, S> action, S state) { String[] keys = getSortedKeys(); for (int i = 0; i < keys.length; i++)
/*     */       action.accept(keys[i], this.map.get(keys[i]), state);  }
/*     */   private String[] getSortedKeys() { if (this.sortedKeys == null) {
/*     */       this.sortedKeys = (String[])this.map.keySet().toArray((Object[])new String[this.map.size()]); Arrays.sort(this.sortedKeys, NULL_FIRST_COMPARATOR);
/* 154 */     }  return this.sortedKeys; } public void remove(String key) { if (!this.map.containsKey(key)) {
/*     */       return;
/*     */     }
/* 157 */     assertNotFrozen();
/* 158 */     this.map.remove(key);
/* 159 */     this.sortedKeys = null; }
/*     */   public <V> V getValue(String key) { return (V)this.map.get(key); }
/*     */   public boolean isEmpty() { return this.map.isEmpty(); }
/*     */   public int size() { return this.map.size(); }
/*     */   public void clear() { if (this.map.isEmpty())
/* 164 */       return;  assertNotFrozen(); this.map.clear(); this.sortedKeys = null; } public String toString() { StringBuilder result = new StringBuilder(this.map.size() * 13);
/* 165 */     result.append('{');
/* 166 */     String[] keys = getSortedKeys();
/* 167 */     for (int i = 0; i < keys.length; i++) {
/* 168 */       if (i > 0) {
/* 169 */         result.append(", ");
/*     */       }
/* 171 */       result.append(keys[i]).append('=').append(this.map.get(keys[i]));
/*     */     } 
/* 173 */     result.append('}');
/* 174 */     return result.toString(); } public void freeze() { this.immutable = true; }
/*     */   public boolean isFrozen() { return this.immutable; }
/*     */   public void putAll(ReadOnlyStringMap source) { assertNotFrozen();
/*     */     source.forEach(PUT_ALL, this.map);
/*     */     this.sortedKeys = null; }
/* 179 */   public boolean equals(Object object) { if (object == this) {
/* 180 */       return true;
/*     */     }
/* 182 */     if (!(object instanceof JdkMapAdapterStringMap)) {
/* 183 */       return false;
/*     */     }
/* 185 */     JdkMapAdapterStringMap other = (JdkMapAdapterStringMap)object;
/* 186 */     return (this.map.equals(other.map) && this.immutable == other.immutable); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 191 */     return this.map.hashCode() + (this.immutable ? 31 : 0);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\JdkMapAdapterStringMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */