/*    */ package org.apache.logging.log4j.util;
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
/*    */ 
/*    */ public class PropertiesPropertySource
/*    */   implements PropertySource
/*    */ {
/*    */   private static final String PREFIX = "log4j2.";
/*    */   private final Properties properties;
/*    */   
/*    */   public PropertiesPropertySource(Properties properties) {
/* 35 */     this.properties = properties;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPriority() {
/* 40 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(BiConsumer<String, String> action) {
/* 45 */     for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
/* 46 */       action.accept((String)entry.getKey(), (String)entry.getValue());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
/* 52 */     return "log4j2." + PropertySource.Util.joinAsCamelCase(tokens);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\PropertiesPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */