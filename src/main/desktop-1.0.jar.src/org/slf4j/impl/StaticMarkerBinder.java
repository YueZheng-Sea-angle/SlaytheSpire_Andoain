/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.spi.MarkerFactoryBinder;
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
/*    */ public class StaticMarkerBinder
/*    */   implements MarkerFactoryBinder
/*    */ {
/* 45 */   public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
/*    */   
/* 47 */   final IMarkerFactory markerFactory = (IMarkerFactory)new BasicMarkerFactory();
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
/*    */   public static StaticMarkerBinder getSingleton() {
/* 59 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 67 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMarkerFactoryClassStr() {
/* 75 */     return BasicMarkerFactory.class.getName();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\slf4j\impl\StaticMarkerBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */