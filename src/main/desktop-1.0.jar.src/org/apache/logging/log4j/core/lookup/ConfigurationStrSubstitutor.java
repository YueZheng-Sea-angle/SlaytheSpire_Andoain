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
/*    */ 
/*    */ 
/*    */ public final class ConfigurationStrSubstitutor
/*    */   extends StrSubstitutor
/*    */ {
/*    */   public ConfigurationStrSubstitutor() {}
/*    */   
/*    */   public ConfigurationStrSubstitutor(Map<String, String> valueMap) {
/* 33 */     super(valueMap);
/*    */   }
/*    */   
/*    */   public ConfigurationStrSubstitutor(Properties properties) {
/* 37 */     super(properties);
/*    */   }
/*    */   
/*    */   public ConfigurationStrSubstitutor(StrLookup lookup) {
/* 41 */     super(lookup);
/*    */   }
/*    */   
/*    */   public ConfigurationStrSubstitutor(StrSubstitutor other) {
/* 45 */     super(other);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isRecursiveEvaluationAllowed() {
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   void setRecursiveEvaluationAllowed(boolean recursiveEvaluationAllowed) {
/* 55 */     throw new UnsupportedOperationException("recursiveEvaluationAllowed cannot be modified within ConfigurationStrSubstitutor");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "ConfigurationStrSubstitutor{" + super.toString() + "}";
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\ConfigurationStrSubstitutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */