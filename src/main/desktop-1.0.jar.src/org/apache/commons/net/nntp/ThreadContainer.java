/*     */ package org.apache.commons.net.nntp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ThreadContainer
/*     */ {
/*     */   Threadable threadable;
/*     */   ThreadContainer parent;
/*     */   ThreadContainer next;
/*     */   ThreadContainer child;
/*     */   
/*     */   boolean findChild(ThreadContainer target) {
/*  40 */     if (this.child == null)
/*  41 */       return false; 
/*  42 */     if (this.child == target) {
/*  43 */       return true;
/*     */     }
/*  45 */     return this.child.findChild(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void flush() {
/*  54 */     if (this.parent != null && this.threadable == null) {
/*  55 */       throw new RuntimeException("no threadable in " + toString());
/*     */     }
/*     */     
/*  58 */     this.parent = null;
/*     */     
/*  60 */     if (this.threadable != null) {
/*  61 */       this.threadable.setChild((this.child == null) ? null : this.child.threadable);
/*     */     }
/*     */     
/*  64 */     if (this.child != null) {
/*  65 */       this.child.flush();
/*  66 */       this.child = null;
/*     */     } 
/*     */     
/*  69 */     if (this.threadable != null) {
/*  70 */       this.threadable.setNext((this.next == null) ? null : this.next.threadable);
/*     */     }
/*     */     
/*  73 */     if (this.next != null) {
/*  74 */       this.next.flush();
/*  75 */       this.next = null;
/*     */     } 
/*     */     
/*  78 */     this.threadable = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void reverseChildren() {
/*  86 */     if (this.child != null) {
/*     */       
/*  88 */       ThreadContainer prev = null, kid = this.child, rest = kid.next;
/*  89 */       for (; kid != null; 
/*  90 */         prev = kid, 
/*  91 */         kid = rest, 
/*  92 */         rest = (rest == null) ? null : rest.next)
/*     */       {
/*  94 */         kid.next = prev;
/*     */       }
/*     */       
/*  97 */       this.child = prev;
/*     */ 
/*     */       
/* 100 */       for (kid = this.child; kid != null; kid = kid.next)
/* 101 */         kid.reverseChildren(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\net\nntp\ThreadContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */