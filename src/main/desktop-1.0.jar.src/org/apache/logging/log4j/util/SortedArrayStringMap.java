/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortedArrayStringMap
/*     */   implements IndexedStringMap
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */   private static final long serialVersionUID = -5748905872274478116L;
/*     */   private static final int HASHVAL = 31;
/*     */   private static final TriConsumer<String, Object, StringMap> PUT_ALL;
/*     */   
/*     */   static {
/*  70 */     PUT_ALL = ((key, value, contextData) -> contextData.putValue(key, value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static final String[] EMPTY = new String[0];
/*     */   
/*     */   private static final String FROZEN = "Frozen collection cannot be modified";
/*  78 */   private transient String[] keys = EMPTY;
/*  79 */   private transient Object[] values = (Object[])EMPTY;
/*     */   
/*     */   private transient int size;
/*     */   
/*     */   private static final Method setObjectInputFilter;
/*     */   private static final Method getObjectInputFilter;
/*     */   private static final Method newObjectInputFilter;
/*     */   private int threshold;
/*     */   private boolean immutable;
/*     */   private transient boolean iterating;
/*     */   
/*     */   static {
/*  91 */     Method[] methods = ObjectInputStream.class.getMethods();
/*  92 */     Method setMethod = null;
/*  93 */     Method getMethod = null;
/*  94 */     for (Method method : methods) {
/*  95 */       if (method.getName().equals("setObjectInputFilter")) {
/*  96 */         setMethod = method;
/*  97 */       } else if (method.getName().equals("getObjectInputFilter")) {
/*  98 */         getMethod = method;
/*     */       } 
/*     */     } 
/* 101 */     Method newMethod = null;
/*     */     try {
/* 103 */       if (setMethod != null) {
/* 104 */         Class<?> clazz = Class.forName("org.apache.logging.log4j.util.internal.DefaultObjectInputFilter");
/* 105 */         methods = clazz.getMethods();
/* 106 */         for (Method method : methods) {
/* 107 */           if (method.getName().equals("newInstance") && Modifier.isStatic(method.getModifiers())) {
/* 108 */             newMethod = method;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 113 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */     
/* 116 */     newObjectInputFilter = newMethod;
/* 117 */     setObjectInputFilter = setMethod;
/* 118 */     getObjectInputFilter = getMethod;
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
/*     */   public SortedArrayStringMap() {
/* 132 */     this(4);
/*     */   }
/*     */   
/*     */   public SortedArrayStringMap(int initialCapacity) {
/* 136 */     if (initialCapacity < 0) {
/* 137 */       throw new IllegalArgumentException("Initial capacity must be at least zero but was " + initialCapacity);
/*     */     }
/* 139 */     this.threshold = ceilingNextPowerOfTwo((initialCapacity == 0) ? 1 : initialCapacity);
/*     */   }
/*     */   
/*     */   public SortedArrayStringMap(ReadOnlyStringMap other) {
/* 143 */     if (other instanceof SortedArrayStringMap) {
/* 144 */       initFrom0((SortedArrayStringMap)other);
/* 145 */     } else if (other != null) {
/* 146 */       resize(ceilingNextPowerOfTwo(other.size()));
/* 147 */       other.forEach(PUT_ALL, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public SortedArrayStringMap(Map<String, ?> map) {
/* 152 */     resize(ceilingNextPowerOfTwo(map.size()));
/* 153 */     for (Map.Entry<String, ?> entry : map.entrySet()) {
/* 154 */       putValue(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertNotFrozen() {
/* 159 */     if (this.immutable) {
/* 160 */       throw new UnsupportedOperationException("Frozen collection cannot be modified");
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertNoConcurrentModification() {
/* 165 */     if (this.iterating) {
/* 166 */       throw new ConcurrentModificationException();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 172 */     if (this.keys == EMPTY) {
/*     */       return;
/*     */     }
/* 175 */     assertNotFrozen();
/* 176 */     assertNoConcurrentModification();
/*     */     
/* 178 */     Arrays.fill((Object[])this.keys, 0, this.size, (Object)null);
/* 179 */     Arrays.fill(this.values, 0, this.size, (Object)null);
/* 180 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(String key) {
/* 185 */     return (indexOfKey(key) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toMap() {
/* 190 */     Map<String, String> result = new HashMap<>(size());
/* 191 */     for (int i = 0; i < size(); i++) {
/* 192 */       Object value = getValueAt(i);
/* 193 */       result.put(getKeyAt(i), (value == null) ? null : String.valueOf(value));
/*     */     } 
/* 195 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 200 */     this.immutable = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 205 */     return this.immutable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValue(String key) {
/* 211 */     int index = indexOfKey(key);
/* 212 */     if (index < 0) {
/* 213 */       return null;
/*     */     }
/* 215 */     return (V)this.values[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 220 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOfKey(String key) {
/* 225 */     if (this.keys == EMPTY) {
/* 226 */       return -1;
/*     */     }
/* 228 */     if (key == null) {
/* 229 */       return nullKeyIndex();
/*     */     }
/* 231 */     int start = (this.size > 0 && this.keys[0] == null) ? 1 : 0;
/* 232 */     return Arrays.binarySearch((Object[])this.keys, start, this.size, key);
/*     */   }
/*     */   
/*     */   private int nullKeyIndex() {
/* 236 */     return (this.size > 0 && this.keys[0] == null) ? 0 : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 241 */     assertNotFrozen();
/* 242 */     assertNoConcurrentModification();
/*     */     
/* 244 */     if (this.keys == EMPTY) {
/* 245 */       inflateTable(this.threshold);
/*     */     }
/* 247 */     int index = indexOfKey(key);
/* 248 */     if (index >= 0) {
/* 249 */       this.keys[index] = key;
/* 250 */       this.values[index] = value;
/*     */     } else {
/* 252 */       insertAt(index ^ 0xFFFFFFFF, key, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void insertAt(int index, String key, Object value) {
/* 257 */     ensureCapacity();
/* 258 */     System.arraycopy(this.keys, index, this.keys, index + 1, this.size - index);
/* 259 */     System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
/* 260 */     this.keys[index] = key;
/* 261 */     this.values[index] = value;
/* 262 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(ReadOnlyStringMap source) {
/* 267 */     if (source == this || source == null || source.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 273 */     assertNotFrozen();
/* 274 */     assertNoConcurrentModification();
/*     */     
/* 276 */     if (source instanceof SortedArrayStringMap) {
/* 277 */       if (this.size == 0) {
/* 278 */         initFrom0((SortedArrayStringMap)source);
/*     */       } else {
/* 280 */         merge((SortedArrayStringMap)source);
/*     */       } 
/* 282 */     } else if (source != null) {
/* 283 */       source.forEach(PUT_ALL, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initFrom0(SortedArrayStringMap other) {
/* 288 */     if (this.keys.length < other.size) {
/* 289 */       this.keys = new String[other.threshold];
/* 290 */       this.values = new Object[other.threshold];
/*     */     } 
/* 292 */     System.arraycopy(other.keys, 0, this.keys, 0, other.size);
/* 293 */     System.arraycopy(other.values, 0, this.values, 0, other.size);
/*     */     
/* 295 */     this.size = other.size;
/* 296 */     this.threshold = other.threshold;
/*     */   }
/*     */   
/*     */   private void merge(SortedArrayStringMap other) {
/* 300 */     String[] myKeys = this.keys;
/* 301 */     Object[] myVals = this.values;
/* 302 */     int newSize = other.size + this.size;
/* 303 */     this.threshold = ceilingNextPowerOfTwo(newSize);
/* 304 */     if (this.keys.length < this.threshold) {
/* 305 */       this.keys = new String[this.threshold];
/* 306 */       this.values = new Object[this.threshold];
/*     */     } 
/*     */     
/* 309 */     boolean overwrite = true;
/* 310 */     if (other.size() > size()) {
/*     */       
/* 312 */       System.arraycopy(myKeys, 0, this.keys, other.size, this.size);
/* 313 */       System.arraycopy(myVals, 0, this.values, other.size, this.size);
/*     */ 
/*     */       
/* 316 */       System.arraycopy(other.keys, 0, this.keys, 0, other.size);
/* 317 */       System.arraycopy(other.values, 0, this.values, 0, other.size);
/* 318 */       this.size = other.size;
/*     */ 
/*     */       
/* 321 */       overwrite = false;
/*     */     } else {
/* 323 */       System.arraycopy(myKeys, 0, this.keys, 0, this.size);
/* 324 */       System.arraycopy(myVals, 0, this.values, 0, this.size);
/*     */ 
/*     */       
/* 327 */       System.arraycopy(other.keys, 0, this.keys, this.size, other.size);
/* 328 */       System.arraycopy(other.values, 0, this.values, this.size, other.size);
/*     */     } 
/*     */ 
/*     */     
/* 332 */     for (int i = this.size; i < newSize; i++) {
/* 333 */       int index = indexOfKey(this.keys[i]);
/* 334 */       if (index < 0) {
/* 335 */         insertAt(index ^ 0xFFFFFFFF, this.keys[i], this.values[i]);
/* 336 */       } else if (overwrite) {
/* 337 */         this.keys[index] = this.keys[i];
/* 338 */         this.values[index] = this.values[i];
/*     */       } 
/*     */     } 
/*     */     
/* 342 */     Arrays.fill((Object[])this.keys, this.size, newSize, (Object)null);
/* 343 */     Arrays.fill(this.values, this.size, newSize, (Object)null);
/*     */   }
/*     */   
/*     */   private void ensureCapacity() {
/* 347 */     if (this.size >= this.threshold) {
/* 348 */       resize(this.threshold * 2);
/*     */     }
/*     */   }
/*     */   
/*     */   private void resize(int newCapacity) {
/* 353 */     String[] oldKeys = this.keys;
/* 354 */     Object[] oldValues = this.values;
/*     */     
/* 356 */     this.keys = new String[newCapacity];
/* 357 */     this.values = new Object[newCapacity];
/*     */     
/* 359 */     System.arraycopy(oldKeys, 0, this.keys, 0, this.size);
/* 360 */     System.arraycopy(oldValues, 0, this.values, 0, this.size);
/*     */     
/* 362 */     this.threshold = newCapacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void inflateTable(int toSize) {
/* 369 */     this.threshold = toSize;
/* 370 */     this.keys = new String[toSize];
/* 371 */     this.values = new Object[toSize];
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 376 */     if (this.keys == EMPTY) {
/*     */       return;
/*     */     }
/*     */     
/* 380 */     int index = indexOfKey(key);
/* 381 */     if (index >= 0) {
/* 382 */       assertNotFrozen();
/* 383 */       assertNoConcurrentModification();
/*     */       
/* 385 */       System.arraycopy(this.keys, index + 1, this.keys, index, this.size - 1 - index);
/* 386 */       System.arraycopy(this.values, index + 1, this.values, index, this.size - 1 - index);
/* 387 */       this.keys[this.size - 1] = null;
/* 388 */       this.values[this.size - 1] = null;
/* 389 */       this.size--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getKeyAt(int index) {
/* 395 */     if (index < 0 || index >= this.size) {
/* 396 */       return null;
/*     */     }
/* 398 */     return this.keys[index];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> V getValueAt(int index) {
/* 404 */     if (index < 0 || index >= this.size) {
/* 405 */       return null;
/*     */     }
/* 407 */     return (V)this.values[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 412 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V> void forEach(BiConsumer<String, ? super V> action) {
/* 418 */     this.iterating = true;
/*     */     try {
/* 420 */       for (int i = 0; i < this.size; i++) {
/* 421 */         action.accept(this.keys[i], (V)this.values[i]);
/*     */       }
/*     */     } finally {
/* 424 */       this.iterating = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <V, T> void forEach(TriConsumer<String, ? super V, T> action, T state) {
/* 431 */     this.iterating = true;
/*     */     try {
/* 433 */       for (int i = 0; i < this.size; i++) {
/* 434 */         action.accept(this.keys[i], (V)this.values[i], state);
/*     */       }
/*     */     } finally {
/* 437 */       this.iterating = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 443 */     if (obj == this) {
/* 444 */       return true;
/*     */     }
/* 446 */     if (!(obj instanceof SortedArrayStringMap)) {
/* 447 */       return false;
/*     */     }
/* 449 */     SortedArrayStringMap other = (SortedArrayStringMap)obj;
/* 450 */     if (size() != other.size()) {
/* 451 */       return false;
/*     */     }
/* 453 */     for (int i = 0; i < size(); i++) {
/* 454 */       if (!Objects.equals(this.keys[i], other.keys[i])) {
/* 455 */         return false;
/*     */       }
/* 457 */       if (!Objects.equals(this.values[i], other.values[i])) {
/* 458 */         return false;
/*     */       }
/*     */     } 
/* 461 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 466 */     int result = 37;
/* 467 */     result = 31 * result + this.size;
/* 468 */     result = 31 * result + hashCode((Object[])this.keys, this.size);
/* 469 */     result = 31 * result + hashCode(this.values, this.size);
/* 470 */     return result;
/*     */   }
/*     */   
/*     */   private static int hashCode(Object[] values, int length) {
/* 474 */     int result = 1;
/* 475 */     for (int i = 0; i < length; i++) {
/* 476 */       result = 31 * result + ((values[i] == null) ? 0 : values[i].hashCode());
/*     */     }
/* 478 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 483 */     StringBuilder sb = new StringBuilder(256);
/* 484 */     sb.append('{');
/* 485 */     for (int i = 0; i < this.size; i++) {
/* 486 */       if (i > 0) {
/* 487 */         sb.append(", ");
/*     */       }
/* 489 */       sb.append(this.keys[i]).append('=');
/* 490 */       sb.append((this.values[i] == this) ? "(this map)" : this.values[i]);
/*     */     } 
/* 492 */     sb.append('}');
/* 493 */     return sb.toString();
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
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 509 */     s.defaultWriteObject();
/*     */ 
/*     */     
/* 512 */     if (this.keys == EMPTY) {
/* 513 */       s.writeInt(ceilingNextPowerOfTwo(this.threshold));
/*     */     } else {
/* 515 */       s.writeInt(this.keys.length);
/*     */     } 
/*     */ 
/*     */     
/* 519 */     s.writeInt(this.size);
/*     */ 
/*     */     
/* 522 */     if (this.size > 0) {
/* 523 */       for (int i = 0; i < this.size; i++) {
/* 524 */         s.writeObject(this.keys[i]);
/*     */         try {
/* 526 */           s.writeObject(marshall(this.values[i]));
/* 527 */         } catch (Exception e) {
/* 528 */           handleSerializationException(e, i, this.keys[i]);
/* 529 */           s.writeObject(null);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte[] marshall(Object obj) throws IOException {
/* 536 */     if (obj == null) {
/* 537 */       return null;
/*     */     }
/* 539 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/* 540 */     try (ObjectOutputStream oos = new ObjectOutputStream(bout)) {
/* 541 */       oos.writeObject(obj);
/* 542 */       oos.flush();
/* 543 */       return bout.toByteArray();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Object unmarshall(byte[] data, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/*     */     ObjectInputStream ois;
/* 549 */     ByteArrayInputStream bin = new ByteArrayInputStream(data);
/* 550 */     Collection<String> allowedClasses = null;
/*     */     
/* 552 */     if (inputStream instanceof FilteredObjectInputStream) {
/* 553 */       allowedClasses = ((FilteredObjectInputStream)inputStream).getAllowedClasses();
/* 554 */       ois = new FilteredObjectInputStream(bin, allowedClasses);
/*     */     } else {
/*     */       try {
/* 557 */         Object obj = getObjectInputFilter.invoke(inputStream, new Object[0]);
/* 558 */         Object filter = newObjectInputFilter.invoke(null, new Object[] { obj });
/* 559 */         ois = new ObjectInputStream(bin);
/* 560 */         setObjectInputFilter.invoke(ois, new Object[] { filter });
/* 561 */       } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/* 562 */         throw new StreamCorruptedException("Unable to set ObjectInputFilter on stream");
/*     */       } 
/*     */     } 
/*     */     try {
/* 566 */       return ois.readObject();
/*     */     } finally {
/* 568 */       ois.close();
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
/*     */   private static int ceilingNextPowerOfTwo(int x) {
/* 581 */     int BITS_PER_INT = 32;
/* 582 */     return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 590 */     if (!(s instanceof FilteredObjectInputStream) && setObjectInputFilter == null) {
/* 591 */       throw new IllegalArgumentException("readObject requires a FilteredObjectInputStream or an ObjectInputStream that accepts an ObjectInputFilter");
/*     */     }
/*     */     
/* 594 */     s.defaultReadObject();
/*     */ 
/*     */     
/* 597 */     this.keys = EMPTY;
/* 598 */     this.values = (Object[])EMPTY;
/*     */ 
/*     */     
/* 601 */     int capacity = s.readInt();
/* 602 */     if (capacity < 0) {
/* 603 */       throw new InvalidObjectException("Illegal capacity: " + capacity);
/*     */     }
/*     */ 
/*     */     
/* 607 */     int mappings = s.readInt();
/* 608 */     if (mappings < 0) {
/* 609 */       throw new InvalidObjectException("Illegal mappings count: " + mappings);
/*     */     }
/*     */ 
/*     */     
/* 613 */     if (mappings > 0) {
/* 614 */       inflateTable(capacity);
/*     */     } else {
/* 616 */       this.threshold = capacity;
/*     */     } 
/*     */ 
/*     */     
/* 620 */     for (int i = 0; i < mappings; i++) {
/* 621 */       this.keys[i] = (String)s.readObject();
/*     */       try {
/* 623 */         byte[] marshalledObject = (byte[])s.readObject();
/* 624 */         this.values[i] = (marshalledObject == null) ? null : unmarshall(marshalledObject, s);
/* 625 */       } catch (Exception|LinkageError error) {
/* 626 */         handleSerializationException(error, i, this.keys[i]);
/* 627 */         this.values[i] = null;
/*     */       } 
/*     */     } 
/* 630 */     this.size = mappings;
/*     */   }
/*     */   
/*     */   private void handleSerializationException(Throwable t, int i, String key) {
/* 634 */     StatusLogger.getLogger().warn("Ignoring {} for key[{}] ('{}')", String.valueOf(t), Integer.valueOf(i), this.keys[i]);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\SortedArrayStringMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */