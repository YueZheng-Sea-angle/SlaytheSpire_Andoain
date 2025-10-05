/*    */ package org.apache.logging.log4j.core.config.arbiters;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*    */ @Plugin(name = "Select", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
/*    */ public class SelectArbiter
/*    */ {
/*    */   public Arbiter evaluateConditions(List<Arbiter> conditions) {
/* 35 */     Optional<Arbiter> opt = conditions.stream().filter(c -> c instanceof DefaultArbiter).reduce((a, b) -> {
/*    */           throw new IllegalStateException("Multiple elements: " + a + ", " + b);
/*    */         });
/* 38 */     for (Arbiter condition : conditions) {
/* 39 */       if (condition instanceof DefaultArbiter) {
/*    */         continue;
/*    */       }
/* 42 */       if (condition.isCondition()) {
/* 43 */         return condition;
/*    */       }
/*    */     } 
/* 46 */     return opt.orElse(null);
/*    */   }
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static Builder newBuilder() {
/* 51 */     return new Builder();
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     implements org.apache.logging.log4j.core.util.Builder<SelectArbiter> {
/*    */     public Builder asBuilder() {
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     public SelectArbiter build() {
/* 61 */       return new SelectArbiter();
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\arbiters\SelectArbiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */