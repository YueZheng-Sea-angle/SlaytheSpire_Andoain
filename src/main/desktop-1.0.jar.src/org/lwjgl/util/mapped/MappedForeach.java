/*    */ package org.lwjgl.util.mapped;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ final class MappedForeach<T extends MappedObject>
/*    */   implements Iterable<T>
/*    */ {
/*    */   final T mapped;
/*    */   final int elementCount;
/*    */   
/*    */   MappedForeach(T mapped, int elementCount) {
/* 47 */     this.mapped = mapped;
/* 48 */     this.elementCount = elementCount;
/*    */   }
/*    */   
/*    */   public Iterator<T> iterator() {
/* 52 */     return new Iterator<T>()
/*    */       {
/*    */         private int index;
/*    */         
/*    */         public boolean hasNext() {
/* 57 */           return (this.index < MappedForeach.this.elementCount);
/*    */         }
/*    */         
/*    */         public T next() {
/* 61 */           MappedForeach.this.mapped.setViewAddress(MappedForeach.this.mapped.getViewAddress(this.index++));
/* 62 */           return MappedForeach.this.mapped;
/*    */         }
/*    */         
/*    */         public void remove() {
/* 66 */           throw new UnsupportedOperationException();
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\mapped\MappedForeach.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */