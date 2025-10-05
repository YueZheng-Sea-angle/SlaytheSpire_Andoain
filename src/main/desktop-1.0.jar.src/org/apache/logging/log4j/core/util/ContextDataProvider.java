/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.logging.log4j.core.impl.JdkMapAdapterStringMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ContextDataProvider
/*    */ {
/*    */   Map<String, String> supplyContextData();
/*    */   
/*    */   default StringMap supplyStringMap() {
/* 40 */     return (StringMap)new JdkMapAdapterStringMap(supplyContextData());
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\cor\\util\ContextDataProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */