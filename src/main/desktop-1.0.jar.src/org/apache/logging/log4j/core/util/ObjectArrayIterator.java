/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectArrayIterator<E>
/*     */   implements Iterator<E>
/*     */ {
/*     */   final E[] array;
/*     */   final int startIndex;
/*     */   final int endIndex;
/*  44 */   int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public ObjectArrayIterator(E... array) {
/*  56 */     this(array, 0, array.length);
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
/*     */   public ObjectArrayIterator(E[] array, int start) {
/*  69 */     this(array, start, array.length);
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
/*     */   public ObjectArrayIterator(E[] array, int start, int end) {
/*  84 */     if (start < 0) {
/*  85 */       throw new ArrayIndexOutOfBoundsException("Start index must not be less than zero");
/*     */     }
/*  87 */     if (end > array.length) {
/*  88 */       throw new ArrayIndexOutOfBoundsException("End index must not be greater than the array length");
/*     */     }
/*  90 */     if (start > array.length) {
/*  91 */       throw new ArrayIndexOutOfBoundsException("Start index must not be greater than the array length");
/*     */     }
/*  93 */     if (end < start) {
/*  94 */       throw new IllegalArgumentException("End index must not be less than start index");
/*     */     }
/*  96 */     this.array = array;
/*  97 */     this.startIndex = start;
/*  98 */     this.endIndex = end;
/*  99 */     this.index = start;
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
/*     */   public boolean hasNext() {
/* 112 */     return (this.index < this.endIndex);
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
/*     */   public E next() {
/* 124 */     if (!hasNext()) {
/* 125 */       throw new NoSuchElementException();
/*     */     }
/* 127 */     return this.array[this.index++];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 137 */     throw new UnsupportedOperationException("remove() method is not supported for an ObjectArrayIterator");
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
/*     */   public E[] getArray() {
/* 149 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartIndex() {
/* 158 */     return this.startIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndIndex() {
/* 167 */     return this.endIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 175 */     this.index = this.startIndex;
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ObjectArrayIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */