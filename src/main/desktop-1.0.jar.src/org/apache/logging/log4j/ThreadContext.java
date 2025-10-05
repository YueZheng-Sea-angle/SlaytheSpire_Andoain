/*     */ package org.apache.logging.log4j;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.logging.log4j.message.ParameterizedMessage;
/*     */ import org.apache.logging.log4j.spi.CleanableThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.DefaultThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.DefaultThreadContextStack;
/*     */ import org.apache.logging.log4j.spi.NoOpThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ReadOnlyThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMap2;
/*     */ import org.apache.logging.log4j.spi.ThreadContextMapFactory;
/*     */ import org.apache.logging.log4j.spi.ThreadContextStack;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ThreadContext
/*     */ {
/*     */   private static class EmptyThreadContextStack
/*     */     extends AbstractCollection<String>
/*     */     implements ThreadContextStack
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private EmptyThreadContextStack() {}
/*     */     
/*  59 */     private static final Iterator<String> EMPTY_ITERATOR = new ThreadContext.EmptyIterator<>();
/*     */ 
/*     */     
/*     */     public String pop() {
/*  63 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String peek() {
/*  68 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(String message) {
/*  73 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDepth() {
/*  78 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> asList() {
/*  83 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void trim(int depth) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  94 */       return (o instanceof Collection && ((Collection)o).isEmpty());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 100 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack copy() {
/* 105 */       return (ThreadContext.ContextStack)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 110 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(String e) {
/* 115 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 120 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends String> c) {
/* 125 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 130 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 135 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<String> iterator() {
/* 140 */       return EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 145 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 150 */       return (ThreadContext.ContextStack)this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EmptyIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private EmptyIterator() {}
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 168 */       throw new NoSuchElementException("This is an empty iterator!");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public static final Map<String, String> EMPTY_MAP = Collections.emptyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public static final ThreadContextStack EMPTY_STACK = new EmptyThreadContextStack();
/*     */   
/*     */   private static final String DISABLE_MAP = "disableThreadContextMap";
/*     */   
/*     */   private static final String DISABLE_STACK = "disableThreadContextStack";
/*     */   private static final String DISABLE_ALL = "disableThreadContext";
/*     */   private static boolean useStack;
/*     */   private static ThreadContextMap contextMap;
/*     */   private static ThreadContextStack contextStack;
/*     */   private static ReadOnlyThreadContextMap readOnlyContextMap;
/*     */   
/*     */   static {
/* 202 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void init() {
/* 213 */     ThreadContextMapFactory.init();
/* 214 */     contextMap = null;
/* 215 */     PropertiesUtil managerProps = PropertiesUtil.getProperties();
/* 216 */     boolean disableAll = managerProps.getBooleanProperty("disableThreadContext");
/* 217 */     useStack = (!managerProps.getBooleanProperty("disableThreadContextStack") && !disableAll);
/* 218 */     boolean useMap = (!managerProps.getBooleanProperty("disableThreadContextMap") && !disableAll);
/*     */     
/* 220 */     contextStack = (ThreadContextStack)new DefaultThreadContextStack(useStack);
/* 221 */     if (!useMap) {
/* 222 */       contextMap = (ThreadContextMap)new NoOpThreadContextMap();
/*     */     } else {
/* 224 */       contextMap = ThreadContextMapFactory.createThreadContextMap();
/*     */     } 
/* 226 */     if (contextMap instanceof ReadOnlyThreadContextMap) {
/* 227 */       readOnlyContextMap = (ReadOnlyThreadContextMap)contextMap;
/*     */     } else {
/* 229 */       readOnlyContextMap = null;
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
/*     */   public static void put(String key, String value) {
/* 245 */     contextMap.put(key, value);
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
/*     */   public static void putIfNull(String key, String value) {
/* 261 */     if (!contextMap.containsKey(key)) {
/* 262 */       contextMap.put(key, value);
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
/*     */   public static void putAll(Map<String, String> m) {
/* 276 */     if (contextMap instanceof ThreadContextMap2) {
/* 277 */       ((ThreadContextMap2)contextMap).putAll(m);
/* 278 */     } else if (contextMap instanceof DefaultThreadContextMap) {
/* 279 */       ((DefaultThreadContextMap)contextMap).putAll(m);
/*     */     } else {
/* 281 */       for (Map.Entry<String, String> entry : m.entrySet()) {
/* 282 */         contextMap.put(entry.getKey(), entry.getValue());
/*     */       }
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
/*     */   public static String get(String key) {
/* 298 */     return contextMap.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(String key) {
/* 307 */     contextMap.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeAll(Iterable<String> keys) {
/* 318 */     if (contextMap instanceof CleanableThreadContextMap) {
/* 319 */       ((CleanableThreadContextMap)contextMap).removeAll(keys);
/* 320 */     } else if (contextMap instanceof DefaultThreadContextMap) {
/* 321 */       ((DefaultThreadContextMap)contextMap).removeAll(keys);
/*     */     } else {
/* 323 */       for (String key : keys) {
/* 324 */         contextMap.remove(key);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearMap() {
/* 333 */     contextMap.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearAll() {
/* 340 */     clearMap();
/* 341 */     clearStack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsKey(String key) {
/* 351 */     return contextMap.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getContext() {
/* 360 */     return contextMap.getCopy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getImmutableContext() {
/* 369 */     Map<String, String> map = contextMap.getImmutableMapOrNull();
/* 370 */     return (map == null) ? EMPTY_MAP : map;
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
/*     */   public static ReadOnlyThreadContextMap getThreadContextMap() {
/* 390 */     return readOnlyContextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty() {
/* 399 */     return contextMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearStack() {
/* 406 */     contextStack.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextStack cloneStack() {
/* 415 */     return contextStack.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContextStack getImmutableStack() {
/* 424 */     ContextStack result = contextStack.getImmutableStackOrNull();
/* 425 */     return (result == null) ? (ContextStack)EMPTY_STACK : result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setStack(Collection<String> stack) {
/* 434 */     if (stack.isEmpty() || !useStack) {
/*     */       return;
/*     */     }
/* 437 */     contextStack.clear();
/* 438 */     contextStack.addAll(stack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDepth() {
/* 449 */     return contextStack.getDepth();
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
/*     */   public static String pop() {
/* 463 */     return contextStack.pop();
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
/*     */   public static String peek() {
/* 477 */     return contextStack.peek();
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
/*     */   public static void push(String message) {
/* 490 */     contextStack.push(message);
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
/*     */   public static void push(String message, Object... args) {
/* 506 */     contextStack.push(ParameterizedMessage.format(message, args));
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
/*     */   public static void removeStack() {
/* 526 */     contextStack.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void trim(int depth) {
/* 562 */     contextStack.trim(depth);
/*     */   }
/*     */   
/*     */   public static interface ContextStack extends Serializable, Collection<String> {
/*     */     String pop();
/*     */     
/*     */     String peek();
/*     */     
/*     */     void push(String param1String);
/*     */     
/*     */     int getDepth();
/*     */     
/*     */     List<String> asList();
/*     */     
/*     */     void trim(int param1Int);
/*     */     
/*     */     ContextStack copy();
/*     */     
/*     */     ContextStack getImmutableStackOrNull();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\ThreadContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */