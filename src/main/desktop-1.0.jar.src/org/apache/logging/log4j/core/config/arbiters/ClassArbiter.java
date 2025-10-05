/*    */ package org.apache.logging.log4j.core.config.arbiters;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*    */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*    */ @Plugin(name = "ClassArbiter", category = "Core", elementType = "Arbiter", printObject = true, deferChildren = true)
/*    */ public class ClassArbiter
/*    */   implements Arbiter
/*    */ {
/*    */   private final String className;
/*    */   
/*    */   private ClassArbiter(String className) {
/* 35 */     this.className = className;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCondition() {
/* 40 */     return LoaderUtil.isClassAvailable(this.className);
/*    */   }
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static SystemPropertyArbiter.Builder newBuilder() {
/* 45 */     return new SystemPropertyArbiter.Builder();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     implements org.apache.logging.log4j.core.util.Builder<ClassArbiter>
/*    */   {
/*    */     public static final String ATTR_CLASS_NAME = "className";
/*    */ 
/*    */     
/*    */     @PluginBuilderAttribute("className")
/*    */     private String className;
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder setClassName(String className) {
/* 62 */       this.className = className;
/* 63 */       return asBuilder();
/*    */     }
/*    */     
/*    */     public Builder asBuilder() {
/* 67 */       return this;
/*    */     }
/*    */     
/*    */     public ClassArbiter build() {
/* 71 */       return new ClassArbiter(this.className);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\arbiters\ClassArbiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */