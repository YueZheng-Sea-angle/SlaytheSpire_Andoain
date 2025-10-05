/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ public class SystemPropertiesPropertySource
/*    */   implements PropertySource
/*    */ {
/*    */   private static final int DEFAULT_PRIORITY = 100;
/*    */   private static final String PREFIX = "log4j2.";
/*    */   
/*    */   public int getPriority() {
/* 36 */     return 100;
/*    */   }
/*    */   
/*    */   public void forEach(BiConsumer<String, String> action) {
/*    */     Properties properties;
/*    */     Object[] keySet;
/*    */     try {
/* 43 */       properties = System.getProperties();
/* 44 */     } catch (SecurityException e) {
/*    */       return;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     synchronized (properties) {
/* 56 */       keySet = properties.keySet().toArray();
/*    */     } 
/*    */ 
/*    */     
/* 60 */     for (Object key : keySet) {
/* 61 */       String keyStr = Objects.toString(key, null);
/* 62 */       action.accept(keyStr, properties.getProperty(keyStr));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
/* 68 */     return "log4j2." + PropertySource.Util.joinAsCamelCase(tokens);
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\SystemPropertiesPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */