/*    */ package org.apache.logging.log4j.core.config.arbiters;
/*    */ 
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
/*    */ 
/*    */ @Plugin(name = "DefaultArbiter", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
/*    */ public class DefaultArbiter
/*    */   implements Arbiter
/*    */ {
/*    */   public boolean isCondition() {
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static Builder newBuilder() {
/* 40 */     return new Builder();
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */     implements org.apache.logging.log4j.core.util.Builder<DefaultArbiter> {
/*    */     public Builder asBuilder() {
/* 46 */       return this;
/*    */     }
/*    */     
/*    */     public DefaultArbiter build() {
/* 50 */       return new DefaultArbiter();
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\arbiters\DefaultArbiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */