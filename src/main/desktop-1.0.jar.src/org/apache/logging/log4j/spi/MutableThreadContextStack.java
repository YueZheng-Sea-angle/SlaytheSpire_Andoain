/*     */ package org.apache.logging.log4j.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.ThreadContext;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MutableThreadContextStack
/*     */   implements ThreadContextStack, StringBuilderFormattable
/*     */ {
/*     */   private static final long serialVersionUID = 50505011L;
/*     */   private final List<String> list;
/*     */   private boolean frozen;
/*     */   
/*     */   public MutableThreadContextStack() {
/*  45 */     this(new ArrayList<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MutableThreadContextStack(List<String> list) {
/*  53 */     this.list = new ArrayList<>(list);
/*     */   }
/*     */   
/*     */   private MutableThreadContextStack(MutableThreadContextStack stack) {
/*  57 */     this.list = new ArrayList<>(stack.list);
/*     */   }
/*     */   
/*     */   private void checkInvariants() {
/*  61 */     if (this.frozen) {
/*  62 */       throw new UnsupportedOperationException("context stack has been frozen");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String pop() {
/*  68 */     checkInvariants();
/*  69 */     if (this.list.isEmpty()) {
/*  70 */       return null;
/*     */     }
/*  72 */     int last = this.list.size() - 1;
/*  73 */     String result = this.list.remove(last);
/*  74 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String peek() {
/*  79 */     if (this.list.isEmpty()) {
/*  80 */       return null;
/*     */     }
/*  82 */     int last = this.list.size() - 1;
/*  83 */     return this.list.get(last);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String message) {
/*  88 */     checkInvariants();
/*  89 */     this.list.add(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDepth() {
/*  94 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> asList() {
/*  99 */     return this.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void trim(int depth) {
/* 104 */     checkInvariants();
/* 105 */     if (depth < 0) {
/* 106 */       throw new IllegalArgumentException("Maximum stack depth cannot be negative");
/*     */     }
/* 108 */     if (this.list == null) {
/*     */       return;
/*     */     }
/* 111 */     List<String> copy = new ArrayList<>(this.list.size());
/* 112 */     int count = Math.min(depth, this.list.size());
/* 113 */     for (int i = 0; i < count; i++) {
/* 114 */       copy.add(this.list.get(i));
/*     */     }
/* 116 */     this.list.clear();
/* 117 */     this.list.addAll(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContextStack copy() {
/* 122 */     return new MutableThreadContextStack(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 127 */     checkInvariants();
/* 128 */     this.list.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 133 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 138 */     return this.list.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 143 */     return this.list.contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> iterator() {
/* 148 */     return this.list.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 153 */     return this.list.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] ts) {
/* 158 */     return this.list.toArray(ts);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(String s) {
/* 163 */     checkInvariants();
/* 164 */     return this.list.add(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 169 */     checkInvariants();
/* 170 */     return this.list.remove(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> objects) {
/* 175 */     return this.list.containsAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends String> strings) {
/* 180 */     checkInvariants();
/* 181 */     return this.list.addAll(strings);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> objects) {
/* 186 */     checkInvariants();
/* 187 */     return this.list.removeAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> objects) {
/* 192 */     checkInvariants();
/* 193 */     return this.list.retainAll(objects);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 198 */     return String.valueOf(this.list);
/*     */   }
/*     */ 
/*     */   
/*     */   public void formatTo(StringBuilder buffer) {
/* 203 */     buffer.append('[');
/* 204 */     for (int i = 0; i < this.list.size(); i++) {
/* 205 */       if (i > 0) {
/* 206 */         buffer.append(',').append(' ');
/*     */       }
/* 208 */       buffer.append(this.list.get(i));
/*     */     } 
/* 210 */     buffer.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 215 */     return 31 + Objects.hashCode(this.list);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 220 */     if (this == obj) {
/* 221 */       return true;
/*     */     }
/* 223 */     if (obj == null) {
/* 224 */       return false;
/*     */     }
/* 226 */     if (!(obj instanceof ThreadContextStack)) {
/* 227 */       return false;
/*     */     }
/* 229 */     ThreadContextStack other = (ThreadContextStack)obj;
/* 230 */     List<String> otherAsList = other.asList();
/* 231 */     return Objects.equals(this.list, otherAsList);
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadContext.ContextStack getImmutableStackOrNull() {
/* 236 */     return copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeze() {
/* 243 */     this.frozen = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFrozen() {
/* 251 */     return this.frozen;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\spi\MutableThreadContextStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */