/*    */ package net.java.games.input;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EventQueue
/*    */ {
/*    */   private final Event[] queue;
/*    */   private int head;
/*    */   private int tail;
/*    */   
/*    */   public EventQueue(int size) {
/* 54 */     this.queue = new Event[size + 1];
/* 55 */     for (int i = 0; i < this.queue.length; i++) {
/* 56 */       this.queue[i] = new Event();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   final synchronized void add(Event event) {
/* 63 */     this.queue[this.tail].set(event);
/* 64 */     this.tail = increase(this.tail);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   final synchronized boolean isFull() {
/* 72 */     return (increase(this.tail) == this.head);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final int increase(int x) {
/* 79 */     return (x + 1) % this.queue.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final synchronized boolean getNextEvent(Event event) {
/* 89 */     if (this.head == this.tail)
/* 90 */       return false; 
/* 91 */     event.set(this.queue[this.head]);
/* 92 */     this.head = increase(this.head);
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\EventQueue.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */