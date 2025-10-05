/*    */ package org.apache.commons.net.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.EventListener;
/*    */ import java.util.Iterator;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*    */ public class ListenerList
/*    */   implements Serializable, Iterable<EventListener>
/*    */ {
/*    */   private static final long serialVersionUID = -1934227607974228213L;
/* 36 */   private final CopyOnWriteArrayList<EventListener> __listeners = new CopyOnWriteArrayList<EventListener>();
/*    */ 
/*    */ 
/*    */   
/*    */   public void addListener(EventListener listener) {
/* 41 */     this.__listeners.add(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeListener(EventListener listener) {
/* 46 */     this.__listeners.remove(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getListenerCount() {
/* 51 */     return this.__listeners.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<EventListener> iterator() {
/* 63 */     return this.__listeners.iterator();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\commons\ne\\util\ListenerList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */