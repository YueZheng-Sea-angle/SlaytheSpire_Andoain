/*    */ package org.apache.logging.log4j.core.lookup;
/*    */ 
/*    */ import org.apache.logging.log4j.core.config.Configuration;
/*    */ import org.apache.logging.log4j.core.config.ConfigurationAware;
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
/*    */ public abstract class AbstractConfigurationAwareLookup
/*    */   extends AbstractLookup
/*    */   implements ConfigurationAware
/*    */ {
/*    */   protected Configuration configuration;
/*    */   
/*    */   public void setConfiguration(Configuration configuration) {
/* 33 */     this.configuration = configuration;
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\lookup\AbstractConfigurationAwareLookup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */