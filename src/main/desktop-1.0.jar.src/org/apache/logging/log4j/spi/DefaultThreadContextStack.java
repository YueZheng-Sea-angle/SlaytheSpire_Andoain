/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultThreadContextStack
/*     */   implements ThreadContextStack, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = 5050501L;
/*  37 */   private static final ThreadLocal<MutableThreadContextStack> STACK = new ThreadLocal<>();
/*     */   
/*     */   private final boolean useStack;
/*     */   
/*     */   public DefaultThreadContextStack(boolean useStack) {
/*  42 */     this.useStack = useStack;
/*     */   }
/*     */   
/*     */   private MutableThreadContextStack getNonNullStackCopy() {
/*  46 */     MutableThreadContextStack values = STACK.get();
/*  47 */     return (values == null) ? new MutableThreadContextStack() : (MutableThreadContextStack)values.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(String s) {
/*  52 */     if (!this.useStack) {
/*  53 */       return false;
/*     */     }
/*  55 */     MutableThreadContextStack copy = getNonNullStackCopy();
/*  56 */     copy.add(s);
/*  57 */     copy.freeze();
/*  58 */     STACK.set(copy);
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends String> strings) {
/*  64 */     if (!this.useStack || strings.isEmpty()) {
/*  65 */       return false;
/*     */     }
/*  67 */     MutableThreadContextStack copy = getNonNullStackCopy();
/*  68 */     copy.addAll(strings);
/*  69 */     copy.freeze();
/*  70 */     STACK.set(copy);
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> asList() {
/*  76 */     MutableThreadContextStack values = STACK.get();
/*  77 */     if (values == null) {
/*  78 */       return Collections.emptyList();
/*     */     }
/*  80 */     return values.asList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  85 */     STACK.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/*  90 */     MutableThreadContextStack values = STACK.get();
/*  91 */     return (values != null && values.contains(o));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> objects) {
/*  96 */     if (objects.isEmpty()) {
/*  97 */       return true;
/*     */     }
/*     */     
/* 100 */     MutableThreadContextStack values = STACK.get();
/* 101 */     return (values != null && values.containsAll(objects));
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContextStack copy() {
/* 106 */     MutableThreadContextStack values = null;
/* 107 */     if (!this.useStack || (values = STACK.get()) == null) {
/* 108 */       return new MutableThreadContextStack();
/*     */     }
/* 110 */     return values.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 115 */     if (this == obj) {
/* 116 */       return true;
/*     */     }
/* 118 */     if (obj == null) {
/* 119 */       return false;
/*     */     }
/* 121 */     if (obj instanceof DefaultThreadContextStack) {
/* 122 */       DefaultThreadContextStack defaultThreadContextStack = (DefaultThreadContextStack)obj;
/* 123 */       if (this.useStack != defaultThreadContextStack.useStack) {
/* 124 */         return false;
/*     */       }
/*     */     } 
/* 127 */     if (!(obj instanceof ThreadContextStack)) {
/* 128 */       return false;
/*     */     }
/* 130 */     ThreadContextStack other = (ThreadContextStack)obj;
/* 131 */     MutableThreadContextStack values = STACK.get();
/* 132 */     if (values == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     return values.equals(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 140 */     MutableThreadContextStack values = STACK.get();
/* 141 */     return (values == null) ? 0 : values.getDepth();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     MutableThreadContextStack values = STACK.get();
/* 147 */     int prime = 31;
/* 148 */     int result = 1;
/*     */     
/* 150 */     result = 31 * result + ((values == null) ? 0 : values.hashCode());
/* 151 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 156 */     MutableThreadContextStack values = STACK.get();
/* 157 */     return (values == null || values.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> iterator() {
/* 162 */     MutableThreadContextStack values = STACK.get();
/* 163 */     if (values == null) {
/* 164 */       List<String> empty = Collections.emptyList();
/* 165 */       return empty.iterator();
/*     */     } 
/* 167 */     return values.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public String peek() {
/* 172 */     MutableThreadContextStack values = STACK.get();
/* 173 */     if (values == null || values.isEmpty()) {
/* 174 */       return "";
/*     */     }
/* 176 */     return values.peek();
/*     */   }
/*     */ 
/*     */   
/*     */   public String pop() {
/* 181 */     if (!this.useStack) {
/* 182 */       return "";
/*     */     }
/* 184 */     MutableThreadContextStack values = STACK.get();
/* 185 */     if (values == null || values.isEmpty())
/*     */     {
/* 187 */       return "";
/*     */     }
/* 189 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 190 */     String result = copy.pop();
/* 191 */     copy.freeze();
/* 192 */     STACK.set(copy);
/* 193 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String message) {
/* 198 */     if (!this.useStack) {
/*     */       return;
/*     */     }
/* 201 */     add(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 206 */     if (!this.useStack) {
/* 207 */       return false;
/*     */     }
/* 209 */     MutableThreadContextStack values = STACK.get();
/* 210 */     if (values == null || values.isEmpty()) {
/* 211 */       return false;
/*     */     }
/* 213 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 214 */     boolean result = copy.remove(o);
/* 215 */     copy.freeze();
/* 216 */     STACK.set(copy);
/* 217 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> objects) {
/* 222 */     if (!this.useStack || objects.isEmpty()) {
/* 223 */       return false;
/*     */     }
/* 225 */     MutableThreadContextStack values = STACK.get();
/* 226 */     if (values == null || values.isEmpty()) {
/* 227 */       return false;
/*     */     }
/* 229 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 230 */     boolean result = copy.removeAll(objects);
/* 231 */     copy.freeze();
/* 232 */     STACK.set(copy);
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> objects) {
/* 238 */     if (!this.useStack || objects.isEmpty()) {
/* 239 */       return false;
/*     */     }
/* 241 */     MutableThreadContextStack values = STACK.get();
/* 242 */     if (values == null || values.isEmpty()) {
/* 243 */       return false;
/*     */     }
/* 245 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 246 */     boolean result = copy.retainAll(objects);
/* 247 */     copy.freeze();
/* 248 */     STACK.set(copy);
/* 249 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 254 */     MutableThreadContextStack values = STACK.get();
/* 255 */     return (values == null) ? 0 : values.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 260 */     MutableThreadContextStack result = STACK.get();
/* 261 */     if (result == null) {
/* 262 */       return (Object[])Strings.EMPTY_ARRAY;
/*     */     }
/* 264 */     return result.toArray(new Object[result.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] ts) {
/* 269 */     MutableThreadContextStack result = STACK.get();
/* 270 */     if (result == null) {
/* 271 */       if (ts.length > 0) {
/* 272 */         ts[0] = null;
/*     */       }
/* 274 */       return ts;
/*     */     } 
/* 276 */     return result.toArray(ts);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 281 */     MutableThreadContextStack values = STACK.get();
/* 282 */     return (values == null) ? "[]" : values.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 287 */     MutableThreadContextStack values = STACK.get();
/* 288 */     if (values == null) {
/* 289 */       buffer.append("[]");
/*     */     } else {
/* 291 */       StringBuilders.appendValue(buffer, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void trim(int depth) {
/* 297 */     if (depth < 0) {
/* 298 */       throw new IllegalArgumentException("Maximum stack depth cannot be negative");
/*     */     }
/* 300 */     MutableThreadContextStack values = STACK.get();
/* 301 */     if (values == null) {
/*     */       return;
/*     */     }
/* 304 */     MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
/* 305 */     copy.trim(depth);
/* 306 */     copy.freeze();
/* 307 */     STACK.set(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 317 */     return STACK.get();
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\DefaultThreadContextStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */