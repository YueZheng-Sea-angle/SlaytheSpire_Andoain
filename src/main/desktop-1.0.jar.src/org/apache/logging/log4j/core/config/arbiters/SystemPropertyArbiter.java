/*    */ package org.apache.logging.log4j.core.config.arbiters;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
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
/*    */ @Plugin(name = "SystemPropertyArbiter", category = "Core", elementType = "Arbiter", deferChildren = true, printObject = true)
/*    */ public class SystemPropertyArbiter
/*    */   implements Arbiter
/*    */ {
/*    */   private final String propertyName;
/*    */   private final String propertyValue;
/*    */   
/*    */   private SystemPropertyArbiter(String propertyName, String propertyValue) {
/* 35 */     this.propertyName = propertyName;
/* 36 */     this.propertyValue = propertyValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCondition() {
/* 46 */     String value = System.getProperty(this.propertyName);
/* 47 */     return (value != null && (this.propertyValue == null || value.equals(this.propertyValue)));
/*    */   }
/*    */   
/*    */   @PluginBuilderFactory
/*    */   public static Builder newBuilder() {
/* 52 */     return new Builder();
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */     implements org.apache.logging.log4j.core.util.Builder<SystemPropertyArbiter>
/*    */   {
/*    */     public static final String ATTR_PROPERTY_NAME = "propertyName";
/*    */     
/*    */     public static final String ATTR_PROPERTY_VALUE = "propertyValue";
/*    */     
/*    */     @PluginBuilderAttribute("propertyName")
/*    */     private String propertyName;
/*    */     
/*    */     @PluginBuilderAttribute("propertyValue")
/*    */     private String propertyValue;
/*    */ 
/*    */     
/*    */     public Builder setPropertyName(String propertyName) {
/* 71 */       this.propertyName = propertyName;
/* 72 */       return asBuilder();
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Builder setPropertyValue(String propertyValue) {
/* 81 */       this.propertyName = propertyValue;
/* 82 */       return asBuilder();
/*    */     }
/*    */     
/*    */     public Builder asBuilder() {
/* 86 */       return this;
/*    */     }
/*    */     
/*    */     public SystemPropertyArbiter build() {
/* 90 */       return new SystemPropertyArbiter(this.propertyName, this.propertyValue);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\arbiters\SystemPropertyArbiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */