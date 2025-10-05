/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.helpers.NOPMDCAdapter;
/*    */ import org.slf4j.spi.MDCAdapter;
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
/*    */ public class StaticMDCBinder
/*    */ {
/* 40 */   public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();
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
/*    */   public static final StaticMDCBinder getSingleton() {
/* 52 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MDCAdapter getMDCA() {
/* 60 */     return (MDCAdapter)new NOPMDCAdapter();
/*    */   }
/*    */   
/*    */   public String getMDCAdapterClassStr() {
/* 64 */     return NOPMDCAdapter.class.getName();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\StaticMDCBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */