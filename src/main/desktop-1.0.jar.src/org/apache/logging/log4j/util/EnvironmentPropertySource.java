/*    */ package org.apache.logging.log4j.util;
/*    */ 
/*    */ import java.util.Map;
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
/*    */ 
/*    */ public class EnvironmentPropertySource
/*    */   implements PropertySource
/*    */ {
/*    */   private static final String PREFIX = "LOG4J_";
/*    */   private static final int DEFAULT_PRIORITY = -100;
/*    */   
/*    */   public int getPriority() {
/* 37 */     return -100;
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(BiConsumer<String, String> action) {
/*    */     Map<String, String> getenv;
/*    */     try {
/* 44 */       getenv = System.getenv();
/* 45 */     } catch (SecurityException e) {
/*    */       
/* 47 */       LowLevelLogUtil.logException("The system environment variables are not available to Log4j due to security restrictions: " + e, e);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 52 */     for (Map.Entry<String, String> entry : getenv.entrySet()) {
/* 53 */       String key = entry.getKey();
/* 54 */       if (key.startsWith("LOG4J_")) {
/* 55 */         action.accept(key.substring("LOG4J_".length()), entry.getValue());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
/* 62 */     StringBuilder sb = new StringBuilder("LOG4J");
/* 63 */     for (CharSequence token : tokens) {
/* 64 */       sb.append('_');
/* 65 */       for (int i = 0; i < token.length(); i++) {
/* 66 */         sb.append(Character.toUpperCase(token.charAt(i)));
/*    */       }
/*    */     } 
/* 69 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4\\util\EnvironmentPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */