/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ public final class RuntimeStrSubstitutor
/*    */   extends StrSubstitutor
/*    */ {
/*    */   public RuntimeStrSubstitutor() {}
/*    */   
/*    */   public RuntimeStrSubstitutor(Map<String, String> valueMap) {
/* 31 */     super(valueMap);
/*    */   }
/*    */   
/*    */   public RuntimeStrSubstitutor(Properties properties) {
/* 35 */     super(properties);
/*    */   }
/*    */   
/*    */   public RuntimeStrSubstitutor(StrLookup lookup) {
/* 39 */     super(lookup);
/*    */   }
/*    */   
/*    */   public RuntimeStrSubstitutor(StrSubstitutor other) {
/* 43 */     super(other);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isRecursiveEvaluationAllowed() {
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   void setRecursiveEvaluationAllowed(boolean recursiveEvaluationAllowed) {
/* 53 */     throw new UnsupportedOperationException("recursiveEvaluationAllowed cannot be modified within RuntimeStrSubstitutor");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "RuntimeStrSubstitutor{" + super.toString() + "}";
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\RuntimeStrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */