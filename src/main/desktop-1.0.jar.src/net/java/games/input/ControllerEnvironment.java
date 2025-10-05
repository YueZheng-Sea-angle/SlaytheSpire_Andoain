/*     */ package net.java.games.input;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class ControllerEnvironment
/*     */ {
/*     */   static void logln(String msg) {
/*  74 */     log(msg + "\n");
/*     */   }
/*     */   
/*     */   static void log(String msg) {
/*  78 */     Logger.getLogger(ControllerEnvironment.class.getName()).info(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private static ControllerEnvironment defaultEnvironment = new DefaultControllerEnvironment();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected final ArrayList controllerListeners = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean $assertionsDisabled;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addControllerListener(ControllerListener l) {
/* 108 */     assert l != null;
/* 109 */     this.controllerListeners.add(l);
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
/*     */   public void removeControllerListener(ControllerListener l) {
/* 123 */     assert l != null;
/* 124 */     this.controllerListeners.remove(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireControllerAdded(Controller c) {
/* 132 */     ControllerEvent ev = new ControllerEvent(c);
/* 133 */     Iterator it = this.controllerListeners.iterator();
/* 134 */     while (it.hasNext()) {
/* 135 */       ((ControllerListener)it.next()).controllerAdded(ev);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fireControllerRemoved(Controller c) {
/* 144 */     ControllerEvent ev = new ControllerEvent(c);
/* 145 */     Iterator it = this.controllerListeners.iterator();
/* 146 */     while (it.hasNext()) {
/* 147 */       ((ControllerListener)it.next()).controllerRemoved(ev);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ControllerEnvironment getDefaultEnvironment() {
/* 156 */     return defaultEnvironment;
/*     */   }
/*     */   
/*     */   public abstract Controller[] getControllers();
/*     */   
/*     */   public abstract boolean isSupported();
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\net\java\games\input\ControllerEnvironment.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */