/*    */ package org.apache.logging.log4j.core.impl;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.ThreadContext;
/*    */ import org.apache.logging.log4j.core.util.ContextDataProvider;
/*    */ import org.apache.logging.log4j.util.StringMap;
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
/*    */ public class ThreadContextDataProvider
/*    */   implements ContextDataProvider
/*    */ {
/*    */   public Map<String, String> supplyContextData() {
/* 32 */     return ThreadContext.getImmutableContext();
/*    */   }
/*    */ 
/*    */   
/*    */   public StringMap supplyStringMap() {
/* 37 */     return ThreadContext.getThreadContextMap().getReadOnlyContextData();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\impl\ThreadContextDataProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */