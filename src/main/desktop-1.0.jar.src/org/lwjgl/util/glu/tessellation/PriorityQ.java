/*     */ package org.lwjgl.util.glu.tessellation;
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
/*     */ abstract class PriorityQ
/*     */ {
/*     */   public static final int INIT_SIZE = 32;
/*     */   
/*     */   public static class PQnode
/*     */   {
/*     */     int handle;
/*     */   }
/*     */   
/*     */   public static class PQhandleElem
/*     */   {
/*     */     Object key;
/*     */     int node;
/*     */   }
/*     */   
/*     */   public static boolean LEQ(Leq leq, Object x, Object y) {
/* 111 */     return Geom.VertLeq((GLUvertex)x, (GLUvertex)y);
/*     */   }
/*     */   
/*     */   static PriorityQ pqNewPriorityQ(Leq leq) {
/* 115 */     return new PriorityQSort(leq);
/*     */   }
/*     */   
/*     */   abstract void pqDeletePriorityQ();
/*     */   
/*     */   abstract boolean pqInit();
/*     */   
/*     */   abstract int pqInsert(Object paramObject);
/*     */   
/*     */   abstract Object pqExtractMin();
/*     */   
/*     */   abstract void pqDelete(int paramInt);
/*     */   
/*     */   abstract Object pqMinimum();
/*     */   
/*     */   abstract boolean pqIsEmpty();
/*     */   
/*     */   public static interface Leq {
/*     */     boolean leq(Object param1Object1, Object param1Object2);
/*     */   }
/*     */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\lwjg\\util\glu\tessellation\PriorityQ.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */