/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ import java.util.Objects;
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
/*    */ public class OrderComparator
/*    */   implements Comparator<Class<?>>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 31 */   private static final Comparator<Class<?>> INSTANCE = new OrderComparator();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Comparator<Class<?>> getInstance() {
/* 39 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Class<?> lhs, Class<?> rhs) {
/* 44 */     Order lhsOrder = (Order)((Class)Objects.<Class<?>>requireNonNull(lhs, "lhs")).getAnnotation(Order.class);
/* 45 */     Order rhsOrder = (Order)((Class)Objects.<Class<?>>requireNonNull(rhs, "rhs")).getAnnotation(Order.class);
/* 46 */     if (lhsOrder == null && rhsOrder == null)
/*    */     {
/* 48 */       return 0;
/*    */     }
/*    */     
/* 51 */     if (rhsOrder == null) {
/* 52 */       return -1;
/*    */     }
/* 54 */     if (lhsOrder == null) {
/* 55 */       return 1;
/*    */     }
/*    */     
/* 58 */     return Integer.signum(rhsOrder.value() - lhsOrder.value());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\OrderComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */