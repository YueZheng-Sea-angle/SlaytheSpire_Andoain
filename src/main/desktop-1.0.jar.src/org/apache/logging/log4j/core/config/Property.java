/*    */ package org.apache.logging.log4j.core.config;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*    */ import org.apache.logging.log4j.core.config.plugins.PluginValue;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ @Plugin(name = "property", category = "Core", printObject = true)
/*    */ public final class Property
/*    */ {
/* 38 */   public static final Property[] EMPTY_ARRAY = new Property[0];
/*    */   
/* 40 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/*    */   private final String name;
/*    */   private final String value;
/*    */   private final boolean valueNeedsLookup;
/*    */   
/*    */   private Property(String name, String value) {
/* 47 */     this.name = name;
/* 48 */     this.value = value;
/* 49 */     this.valueNeedsLookup = (value != null && value.contains("${"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 57 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 65 */     return Objects.toString(this.value, "");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValueNeedsLookup() {
/* 73 */     return this.valueNeedsLookup;
/*    */   }
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
/*    */   @PluginFactory
/*    */   public static Property createProperty(@PluginAttribute("name") String name, @PluginValue("value") String value) {
/* 87 */     if (name == null) {
/* 88 */       LOGGER.error("Property name cannot be null");
/*    */     }
/* 90 */     return new Property(name, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return this.name + '=' + getValue();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\config\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */