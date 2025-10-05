/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CyclicBuffer<T>
/*    */ {
/*    */   private final T[] ring;
/* 28 */   private int first = 0;
/* 29 */   private int last = 0;
/* 30 */   private int numElems = 0;
/*    */ 
/*    */ 
/*    */   
/*    */   private final Class<T> clazz;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CyclicBuffer(Class<T> clazz, int size) throws IllegalArgumentException {
/* 40 */     if (size < 0) {
/* 41 */       throw new IllegalArgumentException("The maxSize argument (" + size + ") cannot be negative.");
/*    */     }
/* 43 */     this.ring = makeArray(clazz, size);
/* 44 */     this.clazz = clazz;
/*    */   }
/*    */ 
/*    */   
/*    */   private T[] makeArray(Class<T> cls, int size) {
/* 49 */     return (T[])Array.newInstance(cls, size);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void add(T item) {
/* 57 */     if (this.ring.length > 0) {
/* 58 */       this.ring[this.last] = item;
/* 59 */       if (++this.last == this.ring.length) {
/* 60 */         this.last = 0;
/*    */       }
/*    */       
/* 63 */       if (this.numElems < this.ring.length) {
/* 64 */         this.numElems++;
/* 65 */       } else if (++this.first == this.ring.length) {
/* 66 */         this.first = 0;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized T[] removeAll() {
/* 76 */     T[] array = makeArray(this.clazz, this.numElems);
/* 77 */     int index = 0;
/* 78 */     while (this.numElems > 0) {
/* 79 */       this.numElems--;
/* 80 */       array[index++] = this.ring[this.first];
/* 81 */       this.ring[this.first] = null;
/* 82 */       if (++this.first == this.ring.length) {
/* 83 */         this.first = 0;
/*    */       }
/*    */     } 
/* 86 */     return array;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 94 */     return (0 == this.numElems);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\CyclicBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */